package com.emerald.postoffice.util

import org.junit.Assert.*
import org.junit.Test

/**
 * Unit tests for [ShipmentPriceCalculator] and [ShipmentValidator].
 *
 * These are 100 % pure-JVM tests – no Android SDK needed.
 * They cover the same price/validation logic that lives in
 * CreateShipmentScreen.kt, ensuring regressions are caught immediately.
 */
class ShipmentPriceCalculatorTest {

    // ─── basePrice ────────────────────────────────────────────────────────────

    @Test
    fun `basePrice returns 80 for Standard`() {
        assertEquals(80.0, ShipmentPriceCalculator.basePrice("Standard"), 0.001)
    }

    @Test
    fun `basePrice returns 150 for Express`() {
        assertEquals(150.0, ShipmentPriceCalculator.basePrice("Express"), 0.001)
    }

    @Test
    fun `basePrice returns 250 for Same Day`() {
        assertEquals(250.0, ShipmentPriceCalculator.basePrice("Same Day"), 0.001)
    }

    @Test
    fun `basePrice returns 80 for unknown service`() {
        // Unknown values fall through to the Standard default
        assertEquals(80.0, ShipmentPriceCalculator.basePrice("Unknown"), 0.001)
    }

    @Test
    fun `basePrice returns 80 for empty string`() {
        assertEquals(80.0, ShipmentPriceCalculator.basePrice(""), 0.001)
    }

    @Test
    fun `basePrice is case-sensitive`() {
        // "express" (lowercase) is not "Express" → returns Standard price
        assertEquals(80.0, ShipmentPriceCalculator.basePrice("express"), 0.001)
    }

    // ─── weightSurcharge ─────────────────────────────────────────────────────

    @Test
    fun `weightSurcharge returns 0 for empty string`() {
        assertEquals(0.0, ShipmentPriceCalculator.weightSurcharge(""), 0.001)
    }

    @Test
    fun `weightSurcharge returns 0 for non-numeric string`() {
        assertEquals(0.0, ShipmentPriceCalculator.weightSurcharge("abc"), 0.001)
    }

    @Test
    fun `weightSurcharge parses integer kg correctly`() {
        // "2 kg" → 2.0 * 20 = 40
        assertEquals(40.0, ShipmentPriceCalculator.weightSurcharge("2 kg"), 0.001)
    }

    @Test
    fun `weightSurcharge parses decimal kg correctly`() {
        // "1.5 kg" → 1.5 * 20 = 30
        assertEquals(30.0, ShipmentPriceCalculator.weightSurcharge("1.5 kg"), 0.001)
    }

    @Test
    fun `weightSurcharge parses bare number string`() {
        // "3" → 3 * 20 = 60
        assertEquals(60.0, ShipmentPriceCalculator.weightSurcharge("3"), 0.001)
    }

    @Test
    fun `weightSurcharge strips all non-digit non-dot characters`() {
        // "2.75kg" after stripping → 2.75 * 20 = 55
        assertEquals(55.0, ShipmentPriceCalculator.weightSurcharge("2.75kg"), 0.001)
    }

    @Test
    fun `weightSurcharge with zero weight`() {
        assertEquals(0.0, ShipmentPriceCalculator.weightSurcharge("0"), 0.001)
    }

    @Test
    fun `weightSurcharge with very heavy parcel`() {
        // 50 kg → 1000 THB surcharge
        assertEquals(1000.0, ShipmentPriceCalculator.weightSurcharge("50"), 0.001)
    }

    // ─── insuranceFee ─────────────────────────────────────────────────────────

    @Test
    fun `insuranceFee returns 50 when insurance is enabled`() {
        assertEquals(50.0, ShipmentPriceCalculator.insuranceFee(true), 0.001)
    }

    @Test
    fun `insuranceFee returns 0 when insurance is disabled`() {
        assertEquals(0.0, ShipmentPriceCalculator.insuranceFee(false), 0.001)
    }

    // ─── totalPrice ───────────────────────────────────────────────────────────

    @Test
    fun `totalPrice Standard no weight no insurance`() {
        // 80 + 0 + 0 = 80
        assertEquals(80.0, ShipmentPriceCalculator.totalPrice("Standard", "", false), 0.001)
    }

    @Test
    fun `totalPrice Express 1kg no insurance`() {
        // 150 + 20 + 0 = 170
        assertEquals(170.0, ShipmentPriceCalculator.totalPrice("Express", "1 kg", false), 0.001)
    }

    @Test
    fun `totalPrice Same Day 2kg with insurance`() {
        // 250 + 40 + 50 = 340
        assertEquals(340.0, ShipmentPriceCalculator.totalPrice("Same Day", "2 kg", true), 0.001)
    }

    @Test
    fun `totalPrice Standard with insurance`() {
        // 80 + 0 + 50 = 130
        assertEquals(130.0, ShipmentPriceCalculator.totalPrice("Standard", "", true), 0.001)
    }

    @Test
    fun `totalPrice Express 2_5kg with insurance`() {
        // 150 + 50 + 50 = 250
        assertEquals(250.0, ShipmentPriceCalculator.totalPrice("Express", "2.5 kg", true), 0.001)
    }

    @Test
    fun `totalPrice with garbage weight string falls back to zero surcharge`() {
        // 80 + 0 + 0 = 80
        assertEquals(80.0, ShipmentPriceCalculator.totalPrice("Standard", "N/A", false), 0.001)
    }
}

/**
 * Unit tests for [ShipmentValidator].
 */
class ShipmentValidatorTest {

    // ─── canProceed ───────────────────────────────────────────────────────────

    @Test
    fun `canProceed returns true when both fields are filled`() {
        assertTrue(ShipmentValidator.canProceed("Alice", "Chiang Mai"))
    }

    @Test
    fun `canProceed returns false when recipient is blank`() {
        assertFalse(ShipmentValidator.canProceed("", "Chiang Mai"))
    }

    @Test
    fun `canProceed returns false when destination is blank`() {
        assertFalse(ShipmentValidator.canProceed("Alice", ""))
    }

    @Test
    fun `canProceed returns false when both are blank`() {
        assertFalse(ShipmentValidator.canProceed("", ""))
    }

    @Test
    fun `canProceed returns false when recipient is whitespace only`() {
        assertFalse(ShipmentValidator.canProceed("   ", "Phuket"))
    }

    @Test
    fun `canProceed returns false when destination is whitespace only`() {
        assertFalse(ShipmentValidator.canProceed("Bob", "   "))
    }

    @Test
    fun `canProceed returns true with trimmed non-empty strings`() {
        assertTrue(ShipmentValidator.canProceed("  Bob  ".trim(), "Bangkok"))
    }

    // ─── isValidPostalCode ────────────────────────────────────────────────────

    @Test
    fun `isValidPostalCode returns true for exactly 5 digits`() {
        assertTrue(ShipmentValidator.isValidPostalCode("10110"))
    }

    @Test
    fun `isValidPostalCode returns false for 4 digits`() {
        assertFalse(ShipmentValidator.isValidPostalCode("1011"))
    }

    @Test
    fun `isValidPostalCode returns false for 6 digits`() {
        assertFalse(ShipmentValidator.isValidPostalCode("101100"))
    }

    @Test
    fun `isValidPostalCode returns false for empty string`() {
        assertFalse(ShipmentValidator.isValidPostalCode(""))
    }

    @Test
    fun `isValidPostalCode returns false for alphabetic code`() {
        assertFalse(ShipmentValidator.isValidPostalCode("ABCDE"))
    }

    @Test
    fun `isValidPostalCode returns false for alphanumeric code`() {
        assertFalse(ShipmentValidator.isValidPostalCode("1011A"))
    }

    @Test
    fun `isValidPostalCode returns false for code with spaces`() {
        assertFalse(ShipmentValidator.isValidPostalCode("1011 "))
    }

    @Test
    fun `isValidPostalCode returns true for all zeros`() {
        // Structurally valid even if semantically unmapped
        assertTrue(ShipmentValidator.isValidPostalCode("00000"))
    }

    @Test
    fun `isValidPostalCode returns true for all nines`() {
        assertTrue(ShipmentValidator.isValidPostalCode("99999"))
    }
}
