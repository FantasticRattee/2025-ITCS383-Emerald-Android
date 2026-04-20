package com.emerald.postoffice._test_

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import com.emerald.postoffice.data.SessionManager
import io.mockk.*
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class SessionManagerTest {

    private lateinit var mockContext: Context
    private lateinit var mockDataStore: DataStore<Preferences>
    private lateinit var sessionManager: SessionManager

    @BeforeEach
    fun setUp() {
        mockContext = mockk()
        mockDataStore = mockk()
        every { mockContext.dataStore } returns mockDataStore
        sessionManager = SessionManager(mockContext)
    }

    @Test
    fun `userId flow returns default value when no data`() = runBlocking {
        // Given
        val mockPreferences = mockk<Preferences>()
        every { mockPreferences[SessionManager.USER_ID] } returns null
        every { mockDataStore.data } returns flowOf(mockPreferences)

        // When
        val userId = sessionManager.userId

        // Then
        userId.collect { id ->
            assertEquals(0, id)
        }
    }

    @Test
    fun `userId flow returns stored value`() = runBlocking {
        // Given
        val mockPreferences = mockk<Preferences>()
        every { mockPreferences[SessionManager.USER_ID] } returns 123
        every { mockDataStore.data } returns flowOf(mockPreferences)

        // When
        val userId = sessionManager.userId

        // Then
        userId.collect { id ->
            assertEquals(123, id)
        }
    }

    @Test
    fun `userName flow returns default value when no data`() = runBlocking {
        // Given
        val mockPreferences = mockk<Preferences>()
        every { mockPreferences[SessionManager.USER_NAME] } returns null
        every { mockDataStore.data } returns flowOf(mockPreferences)

        // When
        val userName = sessionManager.userName

        // Then
        userName.collect { name ->
            assertEquals("", name)
        }
    }

    @Test
    fun `userName flow returns stored value`() = runBlocking {
        // Given
        val mockPreferences = mockk<Preferences>()
        every { mockPreferences[SessionManager.USER_NAME] } returns "John Doe"
        every { mockDataStore.data } returns flowOf(mockPreferences)

        // When
        val userName = sessionManager.userName

        // Then
        userName.collect { name ->
            assertEquals("John Doe", name)
        }
    }

    @Test
    fun `userEmail flow returns default value when no data`() = runBlocking {
        // Given
        val mockPreferences = mockk<Preferences>()
        every { mockPreferences[SessionManager.USER_EMAIL] } returns null
        every { mockDataStore.data } returns flowOf(mockPreferences)

        // When
        val userEmail = sessionManager.userEmail

        // Then
        userEmail.collect { email ->
            assertEquals("", email)
        }
    }

    @Test
    fun `userEmail flow returns stored value`() = runBlocking {
        // Given
        val mockPreferences = mockk<Preferences>()
        every { mockPreferences[SessionManager.USER_EMAIL] } returns "john@example.com"
        every { mockDataStore.data } returns flowOf(mockPreferences)

        // When
        val userEmail = sessionManager.userEmail

        // Then
        userEmail.collect { email ->
            assertEquals("john@example.com", email)
        }
    }

    @Test
    fun `isLoggedIn flow returns default value when no data`() = runBlocking {
        // Given
        val mockPreferences = mockk<Preferences>()
        every { mockPreferences[SessionManager.IS_LOGGED_IN] } returns null
        every { mockDataStore.data } returns flowOf(mockPreferences)

        // When
        val isLoggedIn = sessionManager.isLoggedIn

        // Then
        isLoggedIn.collect { loggedIn ->
            assertFalse(loggedIn)
        }
    }

    @Test
    fun `isLoggedIn flow returns stored value`() = runBlocking {
        // Given
        val mockPreferences = mockk<Preferences>()
        every { mockPreferences[SessionManager.IS_LOGGED_IN] } returns true
        every { mockDataStore.data } returns flowOf(mockPreferences)

        // When
        val isLoggedIn = sessionManager.isLoggedIn

        // Then
        isLoggedIn.collect { loggedIn ->
            assertTrue(loggedIn)
        }
    }

    @Test
    fun `saveSession stores user data correctly`() = runBlocking {
        // Given
        val mockEditor = mockk<MutablePreferences>(relaxed = true)
        coEvery { mockDataStore.edit(any()) } coAnswers {
            val transform = it.invocation.args[0] as (MutablePreferences) -> Unit
            transform(mockEditor)
            mockEditor
        }

        // When
        sessionManager.saveSession(123, "John Doe", "john@example.com", "admin")

        // Then
        coVerify { mockEditor[SessionManager.USER_ID] = 123 }
        coVerify { mockEditor[SessionManager.USER_NAME] = "John Doe" }
        coVerify { mockEditor[SessionManager.USER_EMAIL] = "john@example.com" }
        coVerify { mockEditor[SessionManager.USER_ROLE] = "admin" }
        coVerify { mockEditor[SessionManager.IS_LOGGED_IN] = true }
    }

    @Test
    fun `clearSession clears all data`() = runBlocking {
        // Given
        val mockEditor = mockk<MutablePreferences>(relaxed = true)
        coEvery { mockDataStore.edit(any()) } coAnswers {
            val transform = it.invocation.args[0] as (MutablePreferences) -> Unit
            transform(mockEditor)
            mockEditor
        }

        // When
        sessionManager.clearSession()

        // Then
        coVerify { mockEditor.clear() }
    }

    @Test
    fun `saveSession with error throws exception`() = runBlocking {
        // Given
        coEvery { mockDataStore.edit(any()) } throws RuntimeException("DataStore error")

        // When & Then
        assertThrows(RuntimeException::class.java) {
            runBlocking {
                sessionManager.saveSession(123, "John Doe", "john@example.com", "admin")
            }
        }
    }

    @Test
    fun `clearSession with error throws exception`() = runBlocking {
        // Given
        coEvery { mockDataStore.edit(any()) } throws RuntimeException("DataStore error")

        // When & Then
        assertThrows(RuntimeException::class.java) {
            runBlocking {
                sessionManager.clearSession()
            }
        }
    }
}