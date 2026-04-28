package com.emerald.postoffice.data.model

import org.junit.Assert.*
import org.junit.Test

/**
 * Unit tests for Notification-related data models.
 */
class NotificationModelTest {

    // ─── AppNotification ──────────────────────────────────────────────────────

    @Test
    fun `AppNotification default values are correct`() {
        val notif = AppNotification()
        assertEquals(0, notif.id)
        assertEquals(0, notif.userId)
        assertEquals("", notif.message)
        assertEquals("info", notif.type)
        assertEquals(0, notif.isRead)
        assertNull(notif.createdAt)
    }

    @Test
    fun `AppNotification can be constructed with all fields`() {
        val notif = AppNotification(
            id = 1,
            userId = 42,
            message = "Your package has been dispatched.",
            type = "shipment",
            isRead = 0,
            createdAt = "2025-04-28T10:00:00Z"
        )
        assertEquals(1, notif.id)
        assertEquals(42, notif.userId)
        assertEquals("Your package has been dispatched.", notif.message)
        assertEquals("shipment", notif.type)
        assertEquals(0, notif.isRead)
        assertEquals("2025-04-28T10:00:00Z", notif.createdAt)
    }

    @Test
    fun `AppNotification isRead flag 0 means unread`() {
        val notif = AppNotification(isRead = 0)
        assertEquals(0, notif.isRead)
    }

    @Test
    fun `AppNotification isRead flag 1 means read`() {
        val notif = AppNotification(isRead = 1)
        assertEquals(1, notif.isRead)
    }

    @Test
    fun `AppNotification copy to mark as read`() {
        val original = AppNotification(id = 5, isRead = 0)
        val read = original.copy(isRead = 1)
        assertEquals(0, original.isRead) // immutable
        assertEquals(1, read.isRead)
        assertEquals(5, read.id)
    }

    @Test
    fun `AppNotification equality`() {
        val a = AppNotification(id = 1, message = "Test")
        val b = AppNotification(id = 1, message = "Test")
        assertEquals(a, b)
    }

    @Test
    fun `AppNotification inequality when message differs`() {
        val a = AppNotification(id = 1, message = "Hello")
        val b = AppNotification(id = 1, message = "World")
        assertNotEquals(a, b)
    }

    // ─── NotificationResponse ─────────────────────────────────────────────────

    @Test
    fun `NotificationResponse success with notifications`() {
        val notifications = listOf(
            AppNotification(id = 1, message = "A"),
            AppNotification(id = 2, message = "B")
        )
        val resp = NotificationResponse(success = true, notifications = notifications)
        assertTrue(resp.success)
        assertEquals(2, resp.notifications!!.size)
    }

    @Test
    fun `NotificationResponse success with empty list`() {
        val resp = NotificationResponse(success = true, notifications = emptyList())
        assertTrue(resp.success)
        assertEquals(0, resp.notifications!!.size)
    }

    @Test
    fun `NotificationResponse failure with null list`() {
        val resp = NotificationResponse(success = false, notifications = null)
        assertFalse(resp.success)
        assertNull(resp.notifications)
    }

    @Test
    fun `NotificationResponse unread count can be derived`() {
        val notifications = listOf(
            AppNotification(id = 1, isRead = 0),
            AppNotification(id = 2, isRead = 1),
            AppNotification(id = 3, isRead = 0),
        )
        val resp = NotificationResponse(success = true, notifications = notifications)
        val unreadCount = resp.notifications!!.count { it.isRead == 0 }
        assertEquals(2, unreadCount)
    }

    // ─── Activity ─────────────────────────────────────────────────────────────

    @Test
    fun `Activity default values`() {
        val activity = Activity()
        assertEquals(0, activity.id)
        assertEquals(0, activity.userId)
        assertEquals("", activity.type)
        assertEquals("", activity.title)
        assertEquals("", activity.subtitle)
        assertNull(activity.createdAt)
    }

    @Test
    fun `Activity with values`() {
        val activity = Activity(
            id = 10,
            userId = 5,
            type = "shipment_created",
            title = "New Shipment",
            subtitle = "TH12345",
            createdAt = "2025-04-28T09:00:00Z"
        )
        assertEquals(10, activity.id)
        assertEquals(5, activity.userId)
        assertEquals("shipment_created", activity.type)
        assertEquals("New Shipment", activity.title)
        assertEquals("TH12345", activity.subtitle)
        assertEquals("2025-04-28T09:00:00Z", activity.createdAt)
    }

    @Test
    fun `Activity equality`() {
        val a = Activity(id = 1, title = "Test")
        val b = Activity(id = 1, title = "Test")
        assertEquals(a, b)
    }

    // ─── ActivityResponse ─────────────────────────────────────────────────────

    @Test
    fun `ActivityResponse success with activities`() {
        val activities = listOf(
            Activity(id = 1, type = "login"),
            Activity(id = 2, type = "shipment_created"),
            Activity(id = 3, type = "payment")
        )
        val resp = ActivityResponse(success = true, activities = activities)
        assertTrue(resp.success)
        assertEquals(3, resp.activities!!.size)
        assertEquals("login", resp.activities[0].type)
    }

    @Test
    fun `ActivityResponse failure`() {
        val resp = ActivityResponse(success = false, activities = null)
        assertFalse(resp.success)
        assertNull(resp.activities)
    }

    @Test
    fun `ActivityResponse empty activities`() {
        val resp = ActivityResponse(success = true, activities = emptyList())
        assertTrue(resp.success)
        assertNotNull(resp.activities)
        assertTrue(resp.activities!!.isEmpty())
    }

    @Test
    fun `ActivityResponse filter by type`() {
        val activities = listOf(
            Activity(id = 1, type = "login"),
            Activity(id = 2, type = "shipment_created"),
            Activity(id = 3, type = "login"),
            Activity(id = 4, type = "payment")
        )
        val resp = ActivityResponse(success = true, activities = activities)
        val loginActivities = resp.activities!!.filter { it.type == "login" }
        assertEquals(2, loginActivities.size)
    }
}
