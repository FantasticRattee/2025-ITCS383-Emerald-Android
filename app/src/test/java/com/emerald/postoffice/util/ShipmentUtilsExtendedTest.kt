package com.emerald.postoffice.util

import org.junit.Assert.*
import org.junit.Test

/**
 * Extended tests for [ShipmentPriceCalculator] — covers every remaining branch
 * and boundary so that the class reaches 100 % line/branch coverage.
 */
class ShipmentPriceCalculatorExtendedTest {

    // ── basePrice: every explicit branch ─────────────────────────────────────

    @Test
    fun basePriceForExpressIs150() {
        assertEquals(150.0, ShipmentPriceCalculator.basePrice("Express"), 0.001)
    }

    @Test
    fun basePriceForSameDayIs250() {
        assertEquals(250.0, ShipmentPriceCalculator.basePrice("Same Day"), 0.001)
    }

    @Test
    fun basePriceForStandardIs80() {
        assertEquals(80.0, ShipmentPriceCalculator.basePrice("Standard"), 0.001)
    }

    @Test
    fun basePriceDefaultsTo80ForAnyOtherString() {
        listOf("", "EXPRESS", "sameday", "Economy", "Overnight", "  ").forEach { svc ->
            assertEquals(
                "Expected 80.0 for service '$svc'",
                80.0,
                ShipmentPriceCalculator.basePrice(svc),
                0.001
            )
        }
    }

    // ── weightSurcharge: numeric extraction edge cases ────────────────────────

    @Test
    fun weightSurchargeForZeroKgIsZero() {
        assertEquals(0.0, ShipmentPriceCalculator.weightSurcharge("0 kg"), 0.001)
    }

    @Test
    fun weightSurchargeFor0Point1Kg() {
        // 0.1 * 20 = 2.0
        assertEquals(2.0, ShipmentPriceCalculator.weightSurcharge("0.1"), 0.001)
    }

    @Test
    fun weightSurchargeFor10Kg() {
        // 10 * 20 = 200
        assertEquals(200.0, ShipmentPriceCalculator.weightSurcharge("10 kg"), 0.001)
    }

    @Test
    fun weightSurchargeFor100Kg() {
        // 100 * 20 = 2000
        assertEquals(2000.0, ShipmentPriceCalculator.weightSurcharge("100"), 0.001)
    }

    @Test
    fun weightSurchargeStripsLeadingAndTrailingText() {
        // "Weight: 3.5 kg" → "35" after stripping non-digit/non-dot → 3.5 * 20 = 70
        assertEquals(70.0, ShipmentPriceCalculator.weightSurcharge("Weight: 3.5 kg"), 0.001)
    }

    @Test
    fun weightSurchargeForDotOnlyStringReturnsZero() {
        // "." has no parseable Double → 0
        assertEquals(0.0, ShipmentPriceCalculator.weightSurcharge("."), 0.001)
    }

    @Test
    fun weightSurchargeForWhitespaceOnlyReturnsZero() {
        assertEquals(0.0, ShipmentPriceCalculator.weightSurcharge("   "), 0.001)
    }

    // ── insuranceFee ──────────────────────────────────────────────────────────

    @Test
    fun insuranceFeeIsExactly50WhenEnabled() {
        assertEquals(50.0, ShipmentPriceCalculator.insuranceFee(true), 0.0)
    }

    @Test
    fun insuranceFeeIsExactly0WhenDisabled() {
        assertEquals(0.0, ShipmentPriceCalculator.insuranceFee(false), 0.0)
    }

    // ── totalPrice: all service × insurance combinations ─────────────────────

    @Test
    fun totalPriceStandardNoWeightNoInsurance() {
        assertEquals(80.0, ShipmentPriceCalculator.totalPrice("Standard", "", false), 0.001)
    }

    @Test
    fun totalPriceStandardNoWeightWithInsurance() {
        assertEquals(130.0, ShipmentPriceCalculator.totalPrice("Standard", "", true), 0.001)
    }

    @Test
    fun totalPriceExpressNoWeightNoInsurance() {
        assertEquals(150.0, ShipmentPriceCalculator.totalPrice("Express", "", false), 0.001)
    }

    @Test
    fun totalPriceExpressNoWeightWithInsurance() {
        assertEquals(200.0, ShipmentPriceCalculator.totalPrice("Express", "", true), 0.001)
    }

    @Test
    fun totalPriceSameDayNoWeightNoInsurance() {
        assertEquals(250.0, ShipmentPriceCalculator.totalPrice("Same Day", "", false), 0.001)
    }

    @Test
    fun totalPriceSameDayNoWeightWithInsurance() {
        assertEquals(300.0, ShipmentPriceCalculator.totalPrice("Same Day", "", true), 0.001)
    }

    @Test
    fun totalPriceStandardWith5KgAndInsurance() {
        // 80 + 100 + 50 = 230
        assertEquals(230.0, ShipmentPriceCalculator.totalPrice("Standard", "5", true), 0.001)
    }

    @Test
    fun totalPriceExpressWith2Point5KgNoInsurance() {
        // 150 + 50 + 0 = 200
        assertEquals(200.0, ShipmentPriceCalculator.totalPrice("Express", "2.5 kg", false), 0.001)
    }

    @Test
    fun totalPriceSameDayWith10KgWithInsurance() {
        // 250 + 200 + 50 = 500
        assertEquals(500.0, ShipmentPriceCalculator.totalPrice("Same Day", "10 kg", true), 0.001)
    }

    @Test
    fun totalPriceUnknownServiceWith3KgNoInsurance() {
        // 80 (default) + 60 + 0 = 140
        assertEquals(140.0, ShipmentPriceCalculator.totalPrice("Pigeon Post", "3", false), 0.001)
    }
}

/**
 * Extended tests for [ShipmentValidator] — ensures every branch is hit.
 */
class ShipmentValidatorExtendedTest {

    // ── canProceed ────────────────────────────────────────────────────────────

    @Test
    fun canProceedTrueWhenBothFieldsContainOnlySpacesAfterTrim() {
        // Tabs and newlines are also blank
        assertFalse(ShipmentValidator.canProceed("\t", "\n"))
    }

    @Test
    fun canProceedTrueWithSpecialCharactersInRecipient() {
        assertTrue(ShipmentValidator.canProceed("นาย สมชาย", "Bangkok"))
    }

    @Test
    fun canProceedTrueWithSpecialCharactersInDestination() {
        assertTrue(ShipmentValidator.canProceed("Alice", "เชียงใหม่"))
    }

    @Test
    fun canProceedFalseWhenRecipientIsOnlyNewlines() {
        assertFalse(ShipmentValidator.canProceed("\n\n", "Phuket"))
    }

    @Test
    fun canProceedFalseWhenDestinationIsOnlyTabs() {
        assertFalse(ShipmentValidator.canProceed("Bob", "\t\t"))
    }

    @Test
    fun canProceedTrueWhenBothFieldsHaveMinimalContent() {
        assertTrue(ShipmentValidator.canProceed("A", "B"))
    }

    // ── isValidPostalCode: full boundary sweep ────────────────────────────────

    @Test
    fun isValidPostalCodeTrueForAllDigitFiveCharString() {
        assertTrue(ShipmentValidator.isValidPostalCode("12345"))
    }

    @Test
    fun isValidPostalCodeFalseForSingleDigit() {
        assertFalse(ShipmentValidator.isValidPostalCode("1"))
    }

    @Test
    fun isValidPostalCodeFalseForFourDigits() {
        assertFalse(ShipmentValidator.isValidPostalCode("1234"))
    }

    @Test
    fun isValidPostalCodeFalseForSixDigits() {
        assertFalse(ShipmentValidator.isValidPostalCode("123456"))
    }

    @Test
    fun isValidPostalCodeFalseWhenContainsDot() {
        assertFalse(ShipmentValidator.isValidPostalCode("1234."))
    }

    @Test
    fun isValidPostalCodeFalseWhenContainsHyphen() {
        assertFalse(ShipmentValidator.isValidPostalCode("1234-"))
    }

    @Test
    fun isValidPostalCodeFalseWhenContainsThaiCharacters() {
        assertFalse(ShipmentValidator.isValidPostalCode("1034ก"))
    }

    @Test
    fun isValidPostalCodeFalseForNullLikeString() {
        assertFalse(ShipmentValidator.isValidPostalCode("null0"))
    }

    @Test
    fun isValidPostalCodeTrueFor10000() {
        assertTrue(ShipmentValidator.isValidPostalCode("10000"))
    }

    @Test
    fun isValidPostalCodeTrueFor96000() {
        assertTrue(ShipmentValidator.isValidPostalCode("96000"))
    }
}
