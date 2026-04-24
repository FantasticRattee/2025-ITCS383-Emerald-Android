package com.emerald.postoffice

import com.emerald.postoffice.navigation.Routes
import org.junit.Test
import org.junit.Assert.*

class RoutesTest {

    @Test
    fun `payment route encodes shipment data correctly`() {
        // Given
        val amount = 100.0
        val shipmentData = "test data with spaces & special chars"

        // When
        val route = Routes.payment(amount, shipmentData)

        // Then
        assertEquals("payment/100.0/test%20data%20with%20spaces%20%26%20special%20chars", route)
    }

    @Test
    fun `shippingLabel route encodes tracking number and shipment data correctly`() {
        // Given
        val trackingNumber = "TRACK123"
        val shipmentData = "label data"

        // When
        val route = Routes.shippingLabel(trackingNumber, shipmentData)

        // Then
        assertEquals("shipping_label/TRACK123/label%20data", route)
    }

    @Test
    fun `payment route handles empty shipment data`() {
        // Given
        val amount = 0.0
        val shipmentData = ""

        // When
        val route = Routes.payment(amount, shipmentData)

        // Then
        assertEquals("payment/0.0/", route)
    }

    @Test
    fun `shippingLabel route handles special characters in tracking number`() {
        // Given
        val trackingNumber = "TRACK/123"
        val shipmentData = "data"

        // When
        val route = Routes.shippingLabel(trackingNumber, shipmentData)

        // Then
        assertEquals("shipping_label/TRACK%2F123/data", route)
    }
}