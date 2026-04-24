package com.emerald.postoffice

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import com.emerald.postoffice.data.SessionManager
import io.mockk.*
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class SessionManagerTest {

    private lateinit var sessionManager: SessionManager
    private lateinit var mockContext: Context
    private lateinit var mockDataStore: DataStore<Preferences>

    @Before
    fun setUp() {
        mockContext = mockk()
        mockDataStore = mockk()
        every { mockContext.dataStore } returns mockDataStore
        sessionManager = SessionManager(mockContext)
    }

    @After
    fun tearDown() {
        clearAllMocks()
    }

    @Test
    fun `userId flow returns correct value when data exists`() = runTest {
        // Given
        val expectedId = 123
        val mockPreferences = mockk<Preferences>()
        every { mockPreferences[SessionManager.USER_ID] } returns expectedId
        every { mockDataStore.data } returns flowOf(mockPreferences)

        // When
        val result = sessionManager.userId

        // Then
        result.collect { id ->
            assertEquals(expectedId, id)
        }
    }

    @Test
    fun `userId flow returns 0 when data does not exist`() = runTest {
        // Given
        val mockPreferences = mockk<Preferences>()
        every { mockPreferences[SessionManager.USER_ID] } returns null
        every { mockDataStore.data } returns flowOf(mockPreferences)

        // When
        val result = sessionManager.userId

        // Then
        result.collect { id ->
            assertEquals(0, id)
        }
    }

    @Test
    fun `userName flow returns correct value when data exists`() = runTest {
        // Given
        val expectedName = "John Doe"
        val mockPreferences = mockk<Preferences>()
        every { mockPreferences[SessionManager.USER_NAME] } returns expectedName
        every { mockDataStore.data } returns flowOf(mockPreferences)

        // When
        val result = sessionManager.userName

        // Then
        result.collect { name ->
            assertEquals(expectedName, name)
        }
    }

    @Test
    fun `userName flow returns empty string when data does not exist`() = runTest {
        // Given
        val mockPreferences = mockk<Preferences>()
        every { mockPreferences[SessionManager.USER_NAME] } returns null
        every { mockDataStore.data } returns flowOf(mockPreferences)

        // When
        val result = sessionManager.userName

        // Then
        result.collect { name ->
            assertEquals("", name)
        }
    }

    @Test
    fun `userEmail flow returns correct value when data exists`() = runTest {
        // Given
        val expectedEmail = "john@example.com"
        val mockPreferences = mockk<Preferences>()
        every { mockPreferences[SessionManager.USER_EMAIL] } returns expectedEmail
        every { mockDataStore.data } returns flowOf(mockPreferences)

        // When
        val result = sessionManager.userEmail

        // Then
        result.collect { email ->
            assertEquals(expectedEmail, email)
        }
    }

    @Test
    fun `userEmail flow returns empty string when data does not exist`() = runTest {
        // Given
        val mockPreferences = mockk<Preferences>()
        every { mockPreferences[SessionManager.USER_EMAIL] } returns null
        every { mockDataStore.data } returns flowOf(mockPreferences)

        // When
        val result = sessionManager.userEmail

        // Then
        result.collect { email ->
            assertEquals("", email)
        }
    }

    @Test
    fun `isLoggedIn flow returns true when data exists`() = runTest {
        // Given
        val expectedLoggedIn = true
        val mockPreferences = mockk<Preferences>()
        every { mockPreferences[SessionManager.IS_LOGGED_IN] } returns expectedLoggedIn
        every { mockDataStore.data } returns flowOf(mockPreferences)

        // When
        val result = sessionManager.isLoggedIn

        // Then
        result.collect { loggedIn ->
            assertEquals(expectedLoggedIn, loggedIn)
        }
    }

    @Test
    fun `isLoggedIn flow returns false when data does not exist`() = runTest {
        // Given
        val mockPreferences = mockk<Preferences>()
        every { mockPreferences[SessionManager.IS_LOGGED_IN] } returns null
        every { mockDataStore.data } returns flowOf(mockPreferences)

        // When
        val result = sessionManager.isLoggedIn

        // Then
        result.collect { loggedIn ->
            assertFalse(loggedIn)
        }
    }

    @Test
    fun `saveSession stores all user data correctly`() = runTest {
        // Given
        val id = 123
        val name = "John Doe"
        val email = "john@example.com"
        val role = "admin"
        val mockEditor = mockk<MutablePreferences>(relaxed = true)
        every { mockDataStore.edit(any()) } coAnswers {
            val block = it.invocation.args[0] as (MutablePreferences) -> Unit
            block(mockEditor)
        }

        // When
        sessionManager.saveSession(id, name, email, role)

        // Then
        coVerify {
            mockDataStore.edit(any())
        }
        verify {
            mockEditor[SessionManager.USER_ID] = id
            mockEditor[SessionManager.USER_NAME] = name
            mockEditor[SessionManager.USER_EMAIL] = email
            mockEditor[SessionManager.USER_ROLE] = role
            mockEditor[SessionManager.IS_LOGGED_IN] = true
        }
    }

    @Test
    fun `clearSession clears all data`() = runTest {
        // Given
        val mockEditor = mockk<MutablePreferences>(relaxed = true)
        every { mockDataStore.edit(any()) } coAnswers {
            val block = it.invocation.args[0] as (MutablePreferences) -> Unit
            block(mockEditor)
        }

        // When
        sessionManager.clearSession()

        // Then
        coVerify {
            mockDataStore.edit(any())
        }
        verify {
            mockEditor.clear()
        }
    }
}