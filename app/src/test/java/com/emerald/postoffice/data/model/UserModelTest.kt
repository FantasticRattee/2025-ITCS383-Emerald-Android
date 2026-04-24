package com.emerald.postoffice.data.model

import com.google.gson.Gson
import org.junit.Assert.*
import org.junit.Test

class UserModelTest {

    private val gson = Gson()

    @Test
    fun `User has correct default values`() {
        val user = User()
        assertEquals(0, user.id)
        assertEquals("", user.name)
        assertEquals("", user.email)
        assertEquals("member", user.role)
        assertNull(user.address)
        assertNull(user.createdAt)
    }

    @Test
    fun `User copy preserves unchanged fields`() {
        val user = User(id = 1, name = "Alice", email = "alice@example.com", role = "admin")
        val updated = user.copy(name = "Bob")
        assertEquals("Bob", updated.name)
        assertEquals(1, updated.id)
        assertEquals("alice@example.com", updated.email)
        assertEquals("admin", updated.role)
    }

    @Test
    fun `Gson deserializes created_at field to createdAt property`() {
        val json = """{"id":5,"name":"Test","email":"t@t.com","role":"member","created_at":"2024-06-01"}"""
        val user = gson.fromJson(json, User::class.java)
        assertEquals("2024-06-01", user.createdAt)
        assertEquals(5, user.id)
    }

    @Test
    fun `LoginRequest holds email and password`() {
        val request = LoginRequest("user@example.com", "secret123")
        assertEquals("user@example.com", request.email)
        assertEquals("secret123", request.password)
    }

    @Test
    fun `RegisterRequest holds all registration fields`() {
        val request = RegisterRequest("John", "Doe", "john@example.com", "pass")
        assertEquals("John", request.firstName)
        assertEquals("Doe", request.lastName)
        assertEquals("john@example.com", request.email)
        assertEquals("pass", request.password)
    }

    @Test
    fun `LoginResponse represents a successful login`() {
        val response = LoginResponse(
            success = true,
            userId = 42,
            name = "Alice",
            email = "alice@test.com",
            role = "admin"
        )
        assertTrue(response.success)
        assertEquals(42, response.userId)
        assertEquals("admin", response.role)
        assertNull(response.error)
    }

    @Test
    fun `LoginResponse represents a failed login`() {
        val response = LoginResponse(success = false, error = "Invalid credentials")
        assertFalse(response.success)
        assertNull(response.userId)
        assertEquals("Invalid credentials", response.error)
    }

    @Test
    fun `RegisterResponse holds success flag and new user id`() {
        val response = RegisterResponse(success = true, userId = 7, message = "Account created")
        assertTrue(response.success)
        assertEquals(7, response.userId)
        assertEquals("Account created", response.message)
    }

    @Test
    fun `UserStats default values are all zero`() {
        val stats = UserStats()
        assertEquals(0, stats.total)
        assertEquals("0", stats.delivered)
        assertEquals("0", stats.transit)
        assertEquals("0", stats.pending)
        assertEquals("0", stats.totalSpend)
    }
}
