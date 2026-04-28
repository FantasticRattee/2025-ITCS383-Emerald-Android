package com.emerald.postoffice.data.model

import org.junit.Assert.*
import org.junit.Test

/**
 * Extended tests for data/model classes — covers branches and scenarios
 * not exercised in the primary ModelTest to push line coverage to 100%.
 */
class ModelExtendedTest {

    // ─── Shipment: remaining branches ─────────────────────────────────────────

    @Test
    fun shipmentWithAllNullableFieldsSet() {
        val s = Shipment(
            id = 1,
            eta = "2025-12-01",
            lastUpdate = "2025-11-30",
            weight = "3 kg",
            dims = "10x10x10",
            contents = "Documents",
            createdAt = "2025-11-01",
            method = "PromptPay"
        )
        assertEquals("2025-12-01", s.eta)
        assertEquals("2025-11-30", s.lastUpdate)
        assertEquals("3 kg", s.weight)
        assertEquals("10x10x10", s.dims)
        assertEquals("Documents", s.contents)
        assertEquals("2025-11-01", s.createdAt)
        assertEquals("PromptPay", s.method)
    }

    @Test
    fun shipmentHashCodeConsistentWithEquality() {
        val a = Shipment(id = 7, trackingNumber = "TH007")
        val b = Shipment(id = 7, trackingNumber = "TH007")
        assertEquals(a.hashCode(), b.hashCode())
    }

    @Test
    fun shipmentNotEqualToNull() {
        val s = Shipment(id = 1)
        assertFalse(s.equals(null))
    }

    @Test
    fun shipmentNotEqualToDifferentType() {
        val s = Shipment(id = 1)
        @Suppress("AssertBetweenInconvertibleTypes")
        assertFalse(s.equals("not a shipment"))
    }

    @Test
    fun shipmentCopyPreservesAllNullableFields() {
        val original = Shipment(
            id = 1,
            eta = "2025-12-01",
            weight = "2 kg",
            dims = "20x20x20",
            contents = "Books",
            method = "Cash"
        )
        val copy = original.copy(status = "delivered")
        assertEquals("delivered", copy.status)
        assertEquals("2025-12-01", copy.eta)
        assertEquals("2 kg", copy.weight)
        assertEquals("20x20x20", copy.dims)
        assertEquals("Books", copy.contents)
        assertEquals("Cash", copy.method)
    }

    @Test
    fun shipmentAllStatusValues() {
        val statuses = listOf("pending", "in_transit", "delivered", "failed", "returned")
        statuses.forEach { status ->
            val s = Shipment(status = status)
            assertEquals(status, s.status)
        }
    }

    @Test
    fun shipmentAmountCanBeNegativeForRefund() {
        // Edge case: negative amount (credit/refund scenario)
        val s = Shipment(amount = -50.0)
        assertEquals(-50.0, s.amount, 0.001)
    }

    @Test
    fun shipmentAmountCanBeVeryLarge() {
        val s = Shipment(amount = 999999.99)
        assertEquals(999999.99, s.amount, 0.001)
    }

    @Test
    fun shipmentInsuranceCanBeGreaterThanOne() {
        val s = Shipment(insurance = 2)
        assertEquals(2, s.insurance)
    }

    // ─── ShipmentListResponse: ordering of shipments is preserved ─────────────

    @Test
    fun shipmentListResponsePreservesOrder() {
        val shipments = listOf(
            Shipment(id = 3, trackingNumber = "TH003"),
            Shipment(id = 1, trackingNumber = "TH001"),
            Shipment(id = 2, trackingNumber = "TH002"),
        )
        val resp = ShipmentListResponse(success = true, shipments = shipments)
        assertEquals("TH003", resp.shipments!![0].trackingNumber)
        assertEquals("TH001", resp.shipments[1].trackingNumber)
        assertEquals("TH002", resp.shipments[2].trackingNumber)
    }

    @Test
    fun shipmentListResponseEqualityWhenBothSuccessFalseNullShipments() {
        val a = ShipmentListResponse(success = false)
        val b = ShipmentListResponse(success = false)
        assertEquals(a, b)
    }

    // ─── TrackResponse: message field in both cases ───────────────────────────

    @Test
    fun trackResponseSuccessWithMessage() {
        val resp = TrackResponse(success = true, message = "Found", shipment = Shipment(id = 1))
        assertTrue(resp.success)
        assertEquals("Found", resp.message)
        assertNotNull(resp.shipment)
    }

    @Test
    fun trackResponseEquality() {
        val a = TrackResponse(success = false, message = "Not found")
        val b = TrackResponse(success = false, message = "Not found")
        assertEquals(a, b)
    }

    // ─── CreateShipmentRequest: null optional fields ──────────────────────────

    @Test
    fun createShipmentRequestWithNullOptionals() {
        val req = CreateShipmentRequest(
            userId = 1,
            trackId = "TH100",
            total = "80.0",
            sname = "Alice",
            sprov = "Bangkok",
            rname = "Bob",
            rprov = "Phuket",
            weight = null,
            dims = null,
            contents = null
        )
        assertNull(req.weight)
        assertNull(req.dims)
        assertNull(req.contents)
    }

    @Test
    fun createShipmentRequestEqualityForSameValues() {
        val a = CreateShipmentRequest(
            userId = 1, trackId = "TH100", total = "80.0",
            sname = "A", sprov = "Bangkok", rname = "B", rprov = "Phuket"
        )
        val b = CreateShipmentRequest(
            userId = 1, trackId = "TH100", total = "80.0",
            sname = "A", sprov = "Bangkok", rname = "B", rprov = "Phuket"
        )
        assertEquals(a, b)
    }

    // ─── MonthlyData: edge cases ──────────────────────────────────────────────

    @Test
    fun monthlyDataWithNegativeCount() {
        val data = MonthlyData(month = "2025-01", count = -1)
        assertEquals(-1, data.count)
    }

    @Test
    fun monthlyDataWithLargeCount() {
        val data = MonthlyData(month = "2025-12", count = Int.MAX_VALUE)
        assertEquals(Int.MAX_VALUE, data.count)
    }

    @Test
    fun monthlyDataEqualityCheck() {
        val a = MonthlyData("2025-01", 10)
        val b = MonthlyData("2025-01", 10)
        assertEquals(a, b)
    }

    @Test
    fun monthlyDataInequalityWhenCountDiffers() {
        val a = MonthlyData("2025-01", 10)
        val b = MonthlyData("2025-01", 20)
        assertNotEquals(a, b)
    }

    @Test
    fun monthlyDataInequalityWhenMonthDiffers() {
        val a = MonthlyData("2025-01", 5)
        val b = MonthlyData("2025-02", 5)
        assertNotEquals(a, b)
    }

    // ─── MonthlyResponse: operations on the list ──────────────────────────────

    @Test
    fun monthlyResponseTotalCountAcrossAllMonths() {
        val data = listOf(
            MonthlyData("2025-01", 5),
            MonthlyData("2025-02", 10),
            MonthlyData("2025-03", 15),
        )
        val resp = MonthlyResponse(success = true, data = data)
        val total = resp.data!!.sumOf { it.count }
        assertEquals(30, total)
    }

    @Test
    fun monthlyResponseMaxCountMonth() {
        val data = listOf(
            MonthlyData("2025-01", 3),
            MonthlyData("2025-02", 15),
            MonthlyData("2025-03", 7),
        )
        val resp = MonthlyResponse(success = true, data = data)
        val peak = resp.data!!.maxByOrNull { it.count }
        assertEquals("2025-02", peak?.month)
    }

    // ─── HistoryItem: edge cases ──────────────────────────────────────────────

    @Test
    fun historyItemAmountConvertedToDouble() {
        val item = HistoryItem(amount = "123.45")
        assertEquals(123.45, item.amount.toDouble(), 0.001)
    }

    @Test
    fun historyItemAmountDefaultConvertedToDouble() {
        val item = HistoryItem()
        assertEquals(0.0, item.amount.toDouble(), 0.001)
    }

    @Test
    fun historyItemEqualityCheck() {
        val a = HistoryItem(trackingNumber = "TH001", status = "pending")
        val b = HistoryItem(trackingNumber = "TH001", status = "pending")
        assertEquals(a, b)
    }

    @Test
    fun historyItemInequalityWhenStatusDiffers() {
        val a = HistoryItem(trackingNumber = "TH001", status = "pending")
        val b = HistoryItem(trackingNumber = "TH001", status = "delivered")
        assertNotEquals(a, b)
    }

    @Test
    fun historyItemCopyWithUpdatedStatus() {
        val original = HistoryItem(trackingNumber = "TH555", status = "in_transit")
        val updated = original.copy(status = "delivered")
        assertEquals("TH555", updated.trackingNumber)
        assertEquals("delivered", updated.status)
        assertEquals("in_transit", original.status)
    }

    @Test
    fun historyItemWithAllFieldsPopulated() {
        val item = HistoryItem(
            trackingNumber = "TH999",
            sAddr = "10 Sukhumvit, Bangkok",
            rAddr = "99 Nimman, Chiang Mai",
            receiver = "Carol",
            status = "delivered",
            amount = "350.00",
            paymentMethod = "PromptPay",
            createdAt = "2025-04-01T08:00:00Z"
        )
        assertNotNull(item.createdAt)
        assertNotNull(item.paymentMethod)
        assertEquals("350.00", item.amount)
    }
}
