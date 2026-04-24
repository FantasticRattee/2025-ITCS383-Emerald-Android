package com.emerald.postoffice.data.model

import com.google.gson.Gson
import org.junit.Assert.*
import org.junit.Test

class NotificationModelTest {

    private val gson = Gson()

    @Test
    fun `AppNotification default type is info and unread`() {
        val notif = AppNotification()
        assertEquals("info", notif.type)
        assertEquals(0, notif.isRead)
        assertEquals(0, notif.id)
        assertEquals("", notif.message)
    }

    @Test
    fun `Gson deserializes is_read and user_id fields`() {
        val json = """{"id":3,"user_id":7,"message":"Your parcel arrived","type":"info","is_read":1}"""
        val notif = gson.fromJson(json, AppNotification::class.java)
        assertEquals(3, notif.id)
        assertEquals(7, notif.userId)
        assertEquals("Your parcel arrived", notif.message)
        assertEquals(1, notif.isRead)
    }

    @Test
    fun `NotificationResponse with non-empty list`() {
        val notifs = listOf(
            AppNotification(id = 1, message = "Shipped"),
            AppNotification(id = 2, message = "Delivered")
        )
        val response = NotificationResponse(success = true, notifications = notifs)
        assertTrue(response.success)
        assertEquals(2, response.notifications?.size)
    }

    @Test
    fun `NotificationResponse with null list on failure`() {
        val response = NotificationResponse(success = false, notifications = null)
        assertFalse(response.success)
        assertNull(response.notifications)
    }

    @Test
    fun `Activity holds type, title and subtitle`() {
        val activity = Activity(
            id = 10,
            userId = 2,
            type = "shipment_created",
            title = "New shipment",
            subtitle = "TH001234"
        )
        assertEquals("shipment_created", activity.type)
        assertEquals("New shipment", activity.title)
        assertEquals("TH001234", activity.subtitle)
        assertEquals(10, activity.id)
    }

    @Test
    fun `Gson deserializes Activity user_id and created_at`() {
        val json = """{"id":1,"user_id":5,"type":"login","title":"Login","subtitle":"web","created_at":"2024-04-01"}"""
        val activity = gson.fromJson(json, Activity::class.java)
        assertEquals(5, activity.userId)
        assertEquals("login", activity.type)
        assertEquals("2024-04-01", activity.createdAt)
    }

    @Test
    fun `ActivityResponse with activities list`() {
        val activities = listOf(
            Activity(id = 1, title = "Login"),
            Activity(id = 2, title = "Shipment created")
        )
        val response = ActivityResponse(success = true, activities = activities)
        assertTrue(response.success)
        assertEquals(2, response.activities?.size)
        assertEquals("Login", response.activities?.first()?.title)
    }
}
