package com.emerald.postoffice.data.model

import org.junit.Assert.*
import org.junit.Test

/**
 * Extended tests for User-related models — covers every remaining branch
 * to bring UserModelTest to ~100 % line/branch coverage.
 */
class UserModelExtendedTest {

    // ─── User: remaining branch paths ─────────────────────────────────────────

    @Test
    fun userWithNonNullAddress() {
        val user = User(id = 1, address = "456 Silom Rd, Bangkok")
        assertEquals("456 Silom Rd, Bangkok", user.address)
    }

    @Test
    fun userWithNonNullCreatedAt() {
        val user = User(id = 1, createdAt = "2024-01-01T00:00:00Z")
        assertEquals("2024-01-01T00:00:00Z", user.createdAt)
    }

    @Test
    fun userHashCodeConsistentWithEquality() {
        val a = User(id = 5, email = "x@x.com")
        val b = User(id = 5, email = "x@x.com")
        assertEquals(a.hashCode(), b.hashCode())
    }

    @Test
    fun userNotEqualToNull() {
        val u = User(id = 1)
        assertFalse(u.equals(null))
    }

    @Test
    fun userNotEqualToString() {
        val u = User(id = 1)
        @Suppress("AssertBetweenInconvertibleTypes")
        assertFalse(u.equals("user"))
    }

    @Test
    fun userInequalityWhenIdDiffers() {
        val a = User(id = 1)
        val b = User(id = 2)
        assertNotEquals(a, b)
    }

    @Test
    fun userInequalityWhenEmailDiffers() {
        val a = User(id = 1, email = "a@a.com")
        val b = User(id = 1, email = "b@b.com")
        assertNotEquals(a, b)
    }

    @Test
    fun userRoleCanBeAdmin() {
        val u = User(role = "admin")
        assertEquals("admin", u.role)
    }

    @Test
    fun userRoleCanBeCustom() {
        val u = User(role = "staff")
        assertEquals("staff", u.role)
    }

    @Test
    fun userToStringContainsId() {
        val u = User(id = 42)
        assertTrue(u.toString().contains("42"))
    }

    // ─── LoginRequest: edge cases ─────────────────────────────────────────────

    @Test
    fun loginRequestHashCodeConsistency() {
        val a = LoginRequest("u@test.com", "pw")
        val b = LoginRequest("u@test.com", "pw")
        assertEquals(a.hashCode(), b.hashCode())
    }

    @Test
    fun loginRequestInequalityWhenPasswordDiffers() {
        val a = LoginRequest("u@test.com", "pw1")
        val b = LoginRequest("u@test.com", "pw2")
        assertNotEquals(a, b)
    }

    @Test
    fun loginRequestInequalityWhenEmailDiffers() {
        val a = LoginRequest("a@test.com", "pw")
        val b = LoginRequest("b@test.com", "pw")
        assertNotEquals(a, b)
    }

    @Test
    fun loginRequestToStringContainsEmail() {
        val req = LoginRequest("admin@test.com", "secret")
        assertTrue(req.toString().contains("admin@test.com"))
    }

    // ─── RegisterRequest: edge cases ─────────────────────────────────────────

    @Test
    fun registerRequestEqualityForIdenticalObjects() {
        val a = RegisterRequest("John", "Doe", "j@d.com", "pw")
        val b = RegisterRequest("John", "Doe", "j@d.com", "pw")
        assertEquals(a, b)
    }

    @Test
    fun registerRequestInequalityWhenLastNameDiffers() {
        val a = RegisterRequest("John", "Doe", "j@d.com", "pw")
        val b = RegisterRequest("John", "Smith", "j@d.com", "pw")
        assertNotEquals(a, b)
    }

    @Test
    fun registerRequestCopy() {
        val original = RegisterRequest("Jane", "Doe", "jane@d.com", "pw1")
        val updated = original.copy(password = "pw2")
        assertEquals("pw2", updated.password)
        assertEquals("pw1", original.password)
    }

    // ─── LoginResponse: all field combinations ────────────────────────────────

    @Test
    fun loginResponseSuccessWithAllFields() {
        val resp = LoginResponse(
            success = true,
            userId = 99,
            name = "Charlie",
            email = "charlie@test.com",
            role = "admin",
            message = "Welcome back",
            error = null
        )
        assertTrue(resp.success)
        assertEquals(99, resp.userId)
        assertEquals("Charlie", resp.name)
        assertEquals("charlie@test.com", resp.email)
        assertEquals("admin", resp.role)
        assertEquals("Welcome back", resp.message)
        assertNull(resp.error)
    }

    @Test
    fun loginResponseEqualityForIdenticalResponses() {
        val a = LoginResponse(success = true, userId = 1)
        val b = LoginResponse(success = true, userId = 1)
        assertEquals(a, b)
    }

    @Test
    fun loginResponseInequalityWhenUserIdDiffers() {
        val a = LoginResponse(success = true, userId = 1)
        val b = LoginResponse(success = true, userId = 2)
        assertNotEquals(a, b)
    }

    @Test
    fun loginResponseCopyChangesField() {
        val original = LoginResponse(success = true, userId = 1, name = "Alice")
        val updated = original.copy(name = "Bob")
        assertEquals("Bob", updated.name)
        assertEquals("Alice", original.name)
    }

    // ─── RegisterResponse: edge cases ────────────────────────────────────────

    @Test
    fun registerResponseEqualityForIdenticalObjects() {
        val a = RegisterResponse(success = true, userId = 10, message = "Created")
        val b = RegisterResponse(success = true, userId = 10, message = "Created")
        assertEquals(a, b)
    }

    @Test
    fun registerResponseWithNullUserIdAndMessage() {
        val resp = RegisterResponse(success = false)
        assertNull(resp.userId)
        assertNull(resp.message)
    }

    @Test
    fun registerResponseCopy() {
        val original = RegisterResponse(success = false, message = "Error")
        val fixed = original.copy(success = true, userId = 5)
        assertTrue(fixed.success)
        assertEquals(5, fixed.userId)
    }

    // ─── ProfileResponse: edge cases ─────────────────────────────────────────

    @Test
    fun profileResponseEqualityForIdenticalObjects() {
        val a = ProfileResponse(id = 1, name = "Alice", email = "a@a.com", role = "member")
        val b = ProfileResponse(id = 1, name = "Alice", email = "a@a.com", role = "member")
        assertEquals(a, b)
    }

    @Test
    fun profileResponseWithNonNullCreatedAt() {
        val p = ProfileResponse(id = 1, createdAt = "2025-01-01T00:00:00Z")
        assertNotNull(p.createdAt)
    }

    @Test
    fun profileResponseCopyChangesRole() {
        val original = ProfileResponse(id = 1, role = "member")
        val updated = original.copy(role = "admin")
        assertEquals("admin", updated.role)
        assertEquals("member", original.role)
    }

    @Test
    fun profileResponseToStringContainsName() {
        val p = ProfileResponse(id = 1, name = "Zara")
        assertTrue(p.toString().contains("Zara"))
    }

    // ─── UserStats: arithmetic edge cases ────────────────────────────────────

    @Test
    fun userStatsAllZerosByDefault() {
        val s = UserStats()
        assertEquals(0, s.total)
        assertEquals(0.0, s.totalSpend.toDouble(), 0.001)
    }

    @Test
    fun userStatsTotalSpendParsedAsDouble() {
        val s = UserStats(totalSpend = "9999.99")
        assertEquals(9999.99, s.totalSpend.toDouble(), 0.001)
    }

    @Test
    fun userStatsCopyChangesTotal() {
        val original = UserStats(total = 5)
        val updated = original.copy(total = 10)
        assertEquals(10, updated.total)
        assertEquals(5, original.total)
    }

    @Test
    fun userStatsEqualityForIdenticalObjects() {
        val a = UserStats(total = 3, delivered = "2", transit = "1", pending = "0")
        val b = UserStats(total = 3, delivered = "2", transit = "1", pending = "0")
        assertEquals(a, b)
    }

    @Test
    fun userStatsInequalityWhenTotalDiffers() {
        val a = UserStats(total = 5)
        val b = UserStats(total = 6)
        assertNotEquals(a, b)
    }
}
