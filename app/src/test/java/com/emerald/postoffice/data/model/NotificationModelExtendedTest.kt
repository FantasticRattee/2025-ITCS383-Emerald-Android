package com.emerald.postoffice.data.model

import org.junit.Assert.*
import org.junit.Test

/**
 * Extended tests for Notification-related models — covers remaining branches
 * for 100 % line/branch coverage.
 */
class NotificationModelExtendedTest {

    // ─── AppNotification: remaining branches ──────────────────────────────────

    @Test
    fun appNotificationHashCodeConsistentWithEquality() {
        val a = AppNotification(id = 1, message = "Hello")
        val b = AppNotification(id = 1, message = "Hello")
        assertEquals(a.hashCode(), b.hashCode())
    }

    @Test
    fun appNotificationInequalityWhenIdDiffers() {
        val a = AppNotification(id = 1)
        val b = AppNotification(id = 2)
        assertNotEquals(a, b)
    }

    @Test
    fun appNotificationInequalityWhenTypeDiffers() {
        val a = AppNotification(id = 1, type = "info")
        val b = AppNotification(id = 1, type = "warning")
        assertNotEquals(a, b)
    }

    @Test
    fun appNotificationNotEqualToNull() {
        val n = AppNotification(id = 1)
        assertFalse(n.equals(null))
    }

    @Test
    fun appNotificationToStringContainsId() {
        val n = AppNotification(id = 77)
        assertTrue(n.toString().contains("77"))
    }

    @Test
    fun appNotificationWithNonNullCreatedAt() {
        val n = AppNotification(id = 1, createdAt = "2025-04-28T10:00:00Z")
        assertNotNull(n.createdAt)
        assertEquals("2025-04-28T10:00:00Z", n.createdAt)
    }

    @Test
    fun appNotificationTypeCanBeShipment() {
        val n = AppNotification(type = "shipment")
        assertEquals("shipment", n.type)
    }

    @Test
    fun appNotificationTypeCanBePayment() {
        val n = AppNotification(type = "payment")
        assertEquals("payment", n.type)
    }

    @Test
    fun appNotificationTypeCanBeWarning() {
        val n = AppNotification(type = "warning")
        assertEquals("warning", n.type)
    }

    @Test
    fun appNotificationCopyChangesIsRead() {
        val original = AppNotification(id = 3, isRead = 0)
        val read = original.copy(isRead = 1)
        assertEquals(1, read.isRead)
        assertEquals(0, original.isRead)  // immutable
    }

    // ─── NotificationResponse: list operations ────────────────────────────────

    @Test
    fun notificationResponseReadCountCheck() {
        val list = listOf(
            AppNotification(id = 1, isRead = 1),
            AppNotification(id = 2, isRead = 1),
            AppNotification(id = 3, isRead = 0),
        )
        val resp = NotificationResponse(success = true, notifications = list)
        val readCount = resp.notifications!!.count { it.isRead == 1 }
        assertEquals(2, readCount)
    }

    @Test
    fun notificationResponseLatestNotificationById() {
        val list = listOf(
            AppNotification(id = 10),
            AppNotification(id = 20),
            AppNotification(id = 5),
        )
        val resp = NotificationResponse(success = true, notifications = list)
        val latest = resp.notifications!!.maxByOrNull { it.id }
        assertEquals(20, latest?.id)
    }

    @Test
    fun notificationResponseEqualityForSuccessFalse() {
        val a = NotificationResponse(success = false)
        val b = NotificationResponse(success = false)
        assertEquals(a, b)
    }

    @Test
    fun notificationResponseInequalityWhenSuccessDiffers() {
        val a = NotificationResponse(success = true, notifications = emptyList())
        val b = NotificationResponse(success = false, notifications = emptyList())
        assertNotEquals(a, b)
    }

    // ─── Activity: remaining branches ─────────────────────────────────────────

    @Test
    fun activityHashCodeConsistentWithEquality() {
        val a = Activity(id = 1, type = "login")
        val b = Activity(id = 1, type = "login")
        assertEquals(a.hashCode(), b.hashCode())
    }

    @Test
    fun activityInequalityWhenTypeDiffers() {
        val a = Activity(id = 1, type = "login")
        val b = Activity(id = 1, type = "shipment")
        assertNotEquals(a, b)
    }

    @Test
    fun activityNotEqualToNull() {
        val a = Activity(id = 1)
        assertFalse(a.equals(null))
    }

    @Test
    fun activityToStringContainsTitle() {
        val a = Activity(id = 1, title = "Dashboard Visit")
        assertTrue(a.toString().contains("Dashboard Visit"))
    }

    @Test
    fun activityWithNonNullCreatedAt() {
        val a = Activity(id = 1, createdAt = "2025-04-28T12:00:00Z")
        assertNotNull(a.createdAt)
    }

    @Test
    fun activityCopyChangesSubtitle() {
        val original = Activity(id = 1, subtitle = "OLD")
        val updated = original.copy(subtitle = "NEW")
        assertEquals("NEW", updated.subtitle)
        assertEquals("OLD", original.subtitle)
    }

    @Test
    fun activityAllTypesCanBeStored() {
        val types = listOf("login", "logout", "shipment_created", "payment", "profile_update")
        types.forEach { type ->
            val a = Activity(type = type)
            assertEquals(type, a.type)
        }
    }

    // ─── ActivityResponse: list operations ────────────────────────────────────

    @Test
    fun activityResponseMostRecentActivityById() {
        val list = listOf(
            Activity(id = 1, type = "login"),
            Activity(id = 5, type = "payment"),
            Activity(id = 3, type = "shipment_created"),
        )
        val resp = ActivityResponse(success = true, activities = list)
        val mostRecent = resp.activities!!.maxByOrNull { it.id }
        assertEquals(5, mostRecent?.id)
        assertEquals("payment", mostRecent?.type)
    }

    @Test
    fun activityResponseCountByType() {
        val list = listOf(
            Activity(id = 1, type = "login"),
            Activity(id = 2, type = "login"),
            Activity(id = 3, type = "shipment_created"),
            Activity(id = 4, type = "payment"),
            Activity(id = 5, type = "login"),
        )
        val resp = ActivityResponse(success = true, activities = list)
        val loginCount = resp.activities!!.count { it.type == "login" }
        assertEquals(3, loginCount)
    }

    @Test
    fun activityResponseEqualityForIdenticalObjects() {
        val a = ActivityResponse(success = true, activities = emptyList())
        val b = ActivityResponse(success = true, activities = emptyList())
        assertEquals(a, b)
    }

    @Test
    fun activityResponseInequalityWhenActivitiesDiffer() {
        val a = ActivityResponse(success = true, activities = listOf(Activity(id = 1)))
        val b = ActivityResponse(success = true, activities = listOf(Activity(id = 2)))
        assertNotEquals(a, b)
    }

    @Test
    fun activityResponseCopy() {
        val original = ActivityResponse(success = false, activities = null)
        val updated = original.copy(success = true, activities = emptyList())
        assertTrue(updated.success)
        assertNotNull(updated.activities)
    }
}
