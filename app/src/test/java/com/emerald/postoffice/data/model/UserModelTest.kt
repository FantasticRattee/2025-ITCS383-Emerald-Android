package com.emerald.postoffice.data.model

import org.junit.Assert.*
import org.junit.Test

/**
 * Unit tests for User-related data models.
 */
class UserModelTest {

    // ─── User ─────────────────────────────────────────────────────────────────

    @Test
    fun `User default values are correct`() {
        val user = User()
        assertEquals(0, user.id)
        assertEquals("", user.name)
        assertEquals("", user.email)
        assertEquals("member", user.role)
        assertNull(user.address)
        assertNull(user.createdAt)
    }

    @Test
    fun `User can be constructed with all fields`() {
        val user = User(
            id = 1,
            name = "John Doe",
            email = "john@example.com",
            role = "admin",
            address = "123 Test St",
            createdAt = "2025-01-01T00:00:00Z"
        )
        assertEquals(1, user.id)
        assertEquals("John Doe", user.name)
        assertEquals("john@example.com", user.email)
        assertEquals("admin", user.role)
        assertEquals("123 Test St", user.address)
        assertEquals("2025-01-01T00:00:00Z", user.createdAt)
    }

    @Test
    fun `User equality works correctly`() {
        val a = User(id = 1, email = "a@test.com")
        val b = User(id = 1, email = "a@test.com")
        assertEquals(a, b)
    }

    @Test
    fun `User copy produces correct result`() {
        val original = User(id = 1, name = "Alice", role = "member")
        val promoted = original.copy(role = "admin")
        assertEquals("admin", promoted.role)
        assertEquals("member", original.role) // immutable
    }

    // ─── LoginRequest ─────────────────────────────────────────────────────────

    @Test
    fun `LoginRequest stores email and password`() {
        val req = LoginRequest(email = "test@example.com", password = "secret123")
        assertEquals("test@example.com", req.email)
        assertEquals("secret123", req.password)
    }

    @Test
    fun `LoginRequest with empty credentials`() {
        val req = LoginRequest(email = "", password = "")
        assertEquals("", req.email)
        assertEquals("", req.password)
    }

    @Test
    fun `LoginRequest equality`() {
        val a = LoginRequest("user@test.com", "pass")
        val b = LoginRequest("user@test.com", "pass")
        assertEquals(a, b)
    }

    // ─── RegisterRequest ──────────────────────────────────────────────────────

    @Test
    fun `RegisterRequest stores all fields`() {
        val req = RegisterRequest(
            firstName = "Jane",
            lastName = "Smith",
            email = "jane@example.com",
            password = "P@ssw0rd"
        )
        assertEquals("Jane", req.firstName)
        assertEquals("Smith", req.lastName)
        assertEquals("jane@example.com", req.email)
        assertEquals("P@ssw0rd", req.password)
    }

    // ─── LoginResponse ────────────────────────────────────────────────────────

    @Test
    fun `LoginResponse success scenario`() {
        val resp = LoginResponse(
            success = true,
            userId = 42,
            name = "Alice",
            email = "alice@example.com",
            role = "member"
        )
        assertTrue(resp.success)
        assertEquals(42, resp.userId)
        assertEquals("Alice", resp.name)
        assertEquals("alice@example.com", resp.email)
        assertEquals("member", resp.role)
        assertNull(resp.message)
        assertNull(resp.error)
    }

    @Test
    fun `LoginResponse failure scenario`() {
        val resp = LoginResponse(success = false, error = "Invalid credentials")
        assertFalse(resp.success)
        assertNull(resp.userId)
        assertNull(resp.name)
        assertEquals("Invalid credentials", resp.error)
    }

    @Test
    fun `LoginResponse with message`() {
        val resp = LoginResponse(success = false, message = "Account locked")
        assertFalse(resp.success)
        assertEquals("Account locked", resp.message)
    }

    // ─── RegisterResponse ─────────────────────────────────────────────────────

    @Test
    fun `RegisterResponse success`() {
        val resp = RegisterResponse(success = true, userId = 100, message = "User created")
        assertTrue(resp.success)
        assertEquals(100, resp.userId)
        assertEquals("User created", resp.message)
    }

    @Test
    fun `RegisterResponse failure`() {
        val resp = RegisterResponse(success = false, message = "Email already exists")
        assertFalse(resp.success)
        assertNull(resp.userId)
        assertEquals("Email already exists", resp.message)
    }

    // ─── ProfileResponse ──────────────────────────────────────────────────────

    @Test
    fun `ProfileResponse default values`() {
        val profile = ProfileResponse()
        assertEquals(0, profile.id)
        assertEquals("", profile.name)
        assertEquals("", profile.email)
        assertEquals("member", profile.role)
        assertNull(profile.createdAt)
    }

    @Test
    fun `ProfileResponse with values`() {
        val profile = ProfileResponse(
            id = 7,
            name = "Bob",
            email = "bob@example.com",
            role = "admin",
            createdAt = "2025-06-01T00:00:00Z"
        )
        assertEquals(7, profile.id)
        assertEquals("Bob", profile.name)
        assertEquals("bob@example.com", profile.email)
        assertEquals("admin", profile.role)
        assertEquals("2025-06-01T00:00:00Z", profile.createdAt)
    }

    // ─── UserStats ────────────────────────────────────────────────────────────

    @Test
    fun `UserStats default values`() {
        val stats = UserStats()
        assertEquals(0, stats.total)
        assertEquals("0", stats.delivered)
        assertEquals("0", stats.transit)
        assertEquals("0", stats.pending)
        assertEquals("0", stats.totalSpend)
    }

    @Test
    fun `UserStats with values`() {
        val stats = UserStats(
            total = 100,
            delivered = "80",
            transit = "15",
            pending = "5",
            totalSpend = "12500.00"
        )
        assertEquals(100, stats.total)
        assertEquals("80", stats.delivered)
        assertEquals("15", stats.transit)
        assertEquals("5", stats.pending)
        assertEquals("12500.00", stats.totalSpend)
    }

    @Test
    fun `UserStats delivered plus transit plus pending equals total`() {
        val stats = UserStats(total = 10, delivered = "7", transit = "2", pending = "1")
        val computed = stats.delivered.toInt() + stats.transit.toInt() + stats.pending.toInt()
        assertEquals(stats.total, computed)
    }
}
