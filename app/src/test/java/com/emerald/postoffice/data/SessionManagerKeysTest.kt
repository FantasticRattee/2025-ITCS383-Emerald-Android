package com.emerald.postoffice.data

import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Verifies that DataStore preference key names are stable.
 * Changing a key name silently migrates users to a logged-out state,
 * so these tests guard against accidental renames.
 */
class SessionManagerKeysTest {

    @Test
    fun `USER_ID preference key has correct name`() {
        assertEquals("user_id", SessionManager.USER_ID.name)
    }

    @Test
    fun `USER_NAME preference key has correct name`() {
        assertEquals("user_name", SessionManager.USER_NAME.name)
    }

    @Test
    fun `USER_EMAIL preference key has correct name`() {
        assertEquals("user_email", SessionManager.USER_EMAIL.name)
    }

    @Test
    fun `USER_ROLE preference key has correct name`() {
        assertEquals("user_role", SessionManager.USER_ROLE.name)
    }

    @Test
    fun `IS_LOGGED_IN preference key has correct name`() {
        assertEquals("is_logged_in", SessionManager.IS_LOGGED_IN.name)
    }
}
