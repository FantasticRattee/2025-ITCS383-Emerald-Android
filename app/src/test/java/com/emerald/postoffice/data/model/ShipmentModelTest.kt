package com.emerald.postoffice.data.model

import com.google.gson.Gson
import org.junit.Assert.*
import org.junit.Test

class ShipmentModelTest {

    private val gson = Gson()

    @Test
    fun `Shipment default status is pending`() {
        val shipment = Shipment()
        assertEquals("pending", shipment.status)
        assertEquals(0, shipment.id)
        assertEquals(0.0, shipment.amount, 0.0)
    }

    @Test
    fun `Gson deserializes tracking_number to trackingNumber`() {
        val json = """{"id":1,"tracking_number":"TH123456","status":"delivered","amount":250.0}"""
        val shipment = gson.fromJson(json, Shipment::class.java)
        assertEquals("TH123456", shipment.trackingNumber)
        assertEquals("delivered", shipment.status)
        assertEquals(250.0, shipment.amount, 0.001)
    }

    @Test
    fun `Gson deserializes last_update to lastUpdate`() {
        val json = """{"id":2,"tracking_number":"TH999","status":"in_transit","last_update":"2024-01-15"}"""
        val shipment = gson.fromJson(json, Shipment::class.java)
        assertEquals("2024-01-15", shipment.lastUpdate)
    }

    @Test
    fun `ShipmentListResponse with populated list`() {
        val shipments = listOf(
            Shipment(id = 1, trackingNumber = "TH001"),
            Shipment(id = 2, trackingNumber = "TH002")
        )
        val response = ShipmentListResponse(success = true, shipments = shipments)
        assertTrue(response.success)
        assertEquals(2, response.shipments?.size)
        assertEquals("TH001", response.shipments?.first()?.trackingNumber)
    }

    @Test
    fun `ShipmentListResponse with empty list`() {
        val response = ShipmentListResponse(success = true, shipments = emptyList())
        assertTrue(response.success)
        assertTrue(response.shipments!!.isEmpty())
    }

    @Test
    fun `TrackResponse with found shipment`() {
        val shipment = Shipment(id = 5, trackingNumber = "TH777", status = "pending")
        val response = TrackResponse(success = true, shipment = shipment)
        assertTrue(response.success)
        assertEquals("TH777", response.shipment?.trackingNumber)
        assertNull(response.message)
    }

    @Test
    fun `TrackResponse when shipment not found`() {
        val response = TrackResponse(success = false, message = "Tracking number not found")
        assertFalse(response.success)
        assertNull(response.shipment)
        assertEquals("Tracking number not found", response.message)
    }

    @Test
    fun `CreateShipmentResponse on success`() {
        val response = CreateShipmentResponse(success = true, shipmentId = 10, trackId = "TH999")
        assertTrue(response.success)
        assertEquals(10, response.shipmentId)
        assertEquals("TH999", response.trackId)
        assertNull(response.error)
    }

    @Test
    fun `CreateShipmentResponse on failure`() {
        val response = CreateShipmentResponse(success = false, error = "Duplicate tracking ID")
        assertFalse(response.success)
        assertNull(response.shipmentId)
        assertEquals("Duplicate tracking ID", response.error)
    }

    @Test
    fun `MonthlyData holds month string and shipment count`() {
        val data = MonthlyData(month = "2024-03", count = 15)
        assertEquals("2024-03", data.month)
        assertEquals(15, data.count)
    }

    @Test
    fun `MonthlyResponse contains monthly data list`() {
        val dataList = listOf(MonthlyData("2024-01", 5), MonthlyData("2024-02", 8))
        val response = MonthlyResponse(success = true, data = dataList)
        assertTrue(response.success)
        assertEquals(2, response.data?.size)
        assertEquals(5, response.data?.first()?.count)
    }

    @Test
    fun `HistoryItem default amount is zero string`() {
        val item = HistoryItem()
        assertEquals("0", item.amount)
        assertEquals("", item.status)
        assertNull(item.paymentMethod)
        assertNull(item.createdAt)
    }

    @Test
    fun `Gson deserializes HistoryItem tracking_number and created_at`() {
        val json = """{"tracking_number":"TH555","status":"delivered","amount":"300","created_at":"2024-05-10"}"""
        val item = gson.fromJson(json, HistoryItem::class.java)
        assertEquals("TH555", item.trackingNumber)
        assertEquals("300", item.amount)
        assertEquals("2024-05-10", item.createdAt)
    }
}
