package com.emerald.postoffice.data.model

import org.junit.Assert.*
import org.junit.Test

/**
 * Unit tests for the data model classes.
 *
 * Data classes with default values are pure Kotlin – testable on the JVM
 * with zero Android framework involvement.
 */
class ModelTest {

    // ─── Shipment ─────────────────────────────────────────────────────────────

    @Test
    fun `Shipment default values are correct`() {
        val shipment = Shipment()
        assertEquals(0, shipment.id)
        assertEquals(0, shipment.userId)
        assertEquals("", shipment.trackingNumber)
        assertEquals("", shipment.recipient)
        assertEquals("pending", shipment.status)
        assertEquals(0.0, shipment.amount, 0.0001)
        assertEquals("Parcel", shipment.type)
        assertEquals("Standard", shipment.service)
        assertEquals(0, shipment.insurance)
        assertEquals("None", shipment.handling)
        assertNull(shipment.eta)
        assertNull(shipment.lastUpdate)
        assertNull(shipment.weight)
        assertNull(shipment.dims)
        assertNull(shipment.contents)
        assertNull(shipment.createdAt)
        assertNull(shipment.method)
    }

    @Test
    fun `Shipment can be constructed with custom values`() {
        val shipment = Shipment(
            id = 42,
            userId = 7,
            trackingNumber = "TH123456789",
            recipient = "Alice",
            origin = "Bangkok",
            destination = "Chiang Mai",
            status = "in_transit",
            amount = 150.0,
            type = "Letter",
            service = "Express",
            insurance = 1,
            handling = "Fragile"
        )
        assertEquals(42, shipment.id)
        assertEquals(7, shipment.userId)
        assertEquals("TH123456789", shipment.trackingNumber)
        assertEquals("Alice", shipment.recipient)
        assertEquals("Bangkok", shipment.origin)
        assertEquals("Chiang Mai", shipment.destination)
        assertEquals("in_transit", shipment.status)
        assertEquals(150.0, shipment.amount, 0.0001)
        assertEquals("Letter", shipment.type)
        assertEquals("Express", shipment.service)
        assertEquals(1, shipment.insurance)
        assertEquals("Fragile", shipment.handling)
    }

    @Test
    fun `Shipment data class equality works correctly`() {
        val a = Shipment(id = 1, trackingNumber = "TH001")
        val b = Shipment(id = 1, trackingNumber = "TH001")
        val c = Shipment(id = 2, trackingNumber = "TH002")
        assertEquals(a, b)
        assertNotEquals(a, c)
    }

    @Test
    fun `Shipment copy produces correct result`() {
        val original = Shipment(id = 1, status = "pending")
        val updated = original.copy(status = "delivered")
        assertEquals(1, updated.id)
        assertEquals("delivered", updated.status)
        // original is not mutated
        assertEquals("pending", original.status)
    }

    @Test
    fun `Shipment toString contains key fields`() {
        val shipment = Shipment(id = 99, trackingNumber = "TH999")
        val str = shipment.toString()
        assertTrue(str.contains("99"))
        assertTrue(str.contains("TH999"))
    }

    // ─── ShipmentListResponse ─────────────────────────────────────────────────

    @Test
    fun `ShipmentListResponse with null shipments list`() {
        val response = ShipmentListResponse(success = false, shipments = null)
        assertFalse(response.success)
        assertNull(response.shipments)
    }

    @Test
    fun `ShipmentListResponse with empty list`() {
        val response = ShipmentListResponse(success = true, shipments = emptyList())
        assertTrue(response.success)
        assertNotNull(response.shipments)
        assertEquals(0, response.shipments!!.size)
    }

    @Test
    fun `ShipmentListResponse with multiple shipments`() {
        val shipments = listOf(Shipment(id = 1), Shipment(id = 2), Shipment(id = 3))
        val response = ShipmentListResponse(success = true, shipments = shipments)
        assertEquals(3, response.shipments!!.size)
    }

    // ─── TrackResponse ────────────────────────────────────────────────────────

    @Test
    fun `TrackResponse not found scenario`() {
        val response = TrackResponse(success = false, shipment = null, message = "Not found")
        assertFalse(response.success)
        assertNull(response.shipment)
        assertEquals("Not found", response.message)
    }

    @Test
    fun `TrackResponse found scenario`() {
        val shipment = Shipment(id = 5, trackingNumber = "TH005")
        val response = TrackResponse(success = true, shipment = shipment)
        assertTrue(response.success)
        assertNotNull(response.shipment)
        assertEquals("TH005", response.shipment!!.trackingNumber)
    }

    // ─── CreateShipmentRequest ────────────────────────────────────────────────

    @Test
    fun `CreateShipmentRequest default values are correct`() {
        val req = CreateShipmentRequest(
            userId = 1,
            trackId = "TH100",
            total = "200.0",
            sname = "Bob",
            sprov = "Bangkok",
            rname = "Carol",
            rprov = "Phuket"
        )
        assertEquals("Parcel", req.pkg)
        assertEquals("Standard", req.svc)
        assertEquals("", req.sphone)
        assertEquals("", req.saddr)
        assertEquals("", req.szip)
        assertEquals("", req.rphone)
        assertEquals("", req.raddr)
        assertEquals("", req.rzip)
        assertEquals("0", req.declval)
        assertEquals("No", req.insurance)
        assertEquals("None", req.handling)
        assertEquals("Credit Card", req.paymentMethod)
        assertEquals("", req.paymentRef)
    }

    @Test
    fun `CreateShipmentRequest with custom values`() {
        val req = CreateShipmentRequest(
            userId = 10,
            trackId = "TH200",
            total = "350.50",
            pkg = "Letter",
            svc = "Express",
            sname = "Dave",
            sphone = "0812345678",
            saddr = "123 Main St",
            sprov = "Chiang Mai",
            szip = "50200",
            rname = "Eve",
            rphone = "0898765432",
            raddr = "456 Second St",
            rprov = "Phuket",
            rzip = "83000",
            weight = "2.5 kg",
            dims = "30x20x10 cm",
            contents = "Electronics",
            declval = "5000",
            insurance = "Yes",
            handling = "Fragile",
            paymentMethod = "PromptPay",
            paymentRef = "REF-XYZ"
        )
        assertEquals(10, req.userId)
        assertEquals("TH200", req.trackId)
        assertEquals("350.50", req.total)
        assertEquals("Letter", req.pkg)
        assertEquals("Express", req.svc)
        assertEquals("Dave", req.sname)
        assertEquals("0812345678", req.sphone)
        assertEquals("123 Main St", req.saddr)
        assertEquals("Chiang Mai", req.sprov)
        assertEquals("50200", req.szip)
        assertEquals("Eve", req.rname)
        assertEquals("0898765432", req.rphone)
        assertEquals("456 Second St", req.raddr)
        assertEquals("Phuket", req.rprov)
        assertEquals("83000", req.rzip)
        assertEquals("2.5 kg", req.weight)
        assertEquals("30x20x10 cm", req.dims)
        assertEquals("Electronics", req.contents)
        assertEquals("5000", req.declval)
        assertEquals("Yes", req.insurance)
        assertEquals("Fragile", req.handling)
        assertEquals("PromptPay", req.paymentMethod)
        assertEquals("REF-XYZ", req.paymentRef)
    }

    // ─── CreateShipmentResponse ───────────────────────────────────────────────

    @Test
    fun `CreateShipmentResponse success`() {
        val resp = CreateShipmentResponse(success = true, shipmentId = 55, trackId = "TH055")
        assertTrue(resp.success)
        assertEquals(55, resp.shipmentId)
        assertEquals("TH055", resp.trackId)
        assertNull(resp.error)
    }

    @Test
    fun `CreateShipmentResponse failure`() {
        val resp = CreateShipmentResponse(success = false, error = "Duplicate tracking number")
        assertFalse(resp.success)
        assertNull(resp.shipmentId)
        assertNull(resp.trackId)
        assertEquals("Duplicate tracking number", resp.error)
    }

    // ─── MonthlyData / MonthlyResponse ───────────────────────────────────────

    @Test
    fun `MonthlyData default values`() {
        val data = MonthlyData()
        assertEquals("", data.month)
        assertEquals(0, data.count)
    }

    @Test
    fun `MonthlyData with values`() {
        val data = MonthlyData(month = "2025-01", count = 42)
        assertEquals("2025-01", data.month)
        assertEquals(42, data.count)
    }

    @Test
    fun `MonthlyResponse with data`() {
        val items = listOf(MonthlyData("2025-01", 10), MonthlyData("2025-02", 20))
        val resp = MonthlyResponse(success = true, data = items)
        assertTrue(resp.success)
        assertEquals(2, resp.data!!.size)
        assertEquals(10, resp.data[0].count)
        assertEquals(20, resp.data[1].count)
    }

    @Test
    fun `MonthlyResponse null data`() {
        val resp = MonthlyResponse(success = false, data = null)
        assertFalse(resp.success)
        assertNull(resp.data)
    }

    // ─── HistoryItem ──────────────────────────────────────────────────────────

    @Test
    fun `HistoryItem default values`() {
        val item = HistoryItem()
        assertEquals("", item.trackingNumber)
        assertEquals("", item.sAddr)
        assertEquals("", item.rAddr)
        assertEquals("", item.receiver)
        assertEquals("", item.status)
        assertEquals("0", item.amount)
        assertNull(item.paymentMethod)
        assertNull(item.createdAt)
    }

    @Test
    fun `HistoryItem with values`() {
        val item = HistoryItem(
            trackingNumber = "TH777",
            sAddr = "Bangkok",
            rAddr = "Chiang Mai",
            receiver = "Alice",
            status = "delivered",
            amount = "250.00",
            paymentMethod = "Credit Card",
            createdAt = "2025-01-15T10:00:00Z"
        )
        assertEquals("TH777", item.trackingNumber)
        assertEquals("Bangkok", item.sAddr)
        assertEquals("Chiang Mai", item.rAddr)
        assertEquals("Alice", item.receiver)
        assertEquals("delivered", item.status)
        assertEquals("250.00", item.amount)
        assertEquals("Credit Card", item.paymentMethod)
        assertEquals("2025-01-15T10:00:00Z", item.createdAt)
    }
}
