package com.emerald.postoffice.util

/**
 * Pure business-logic utilities extracted from the UI layer so they
 * can be tested without any Android framework dependency.
 *
 * These functions mirror the price/validation logic inside
 * CreateShipmentScreen.kt exactly – do NOT change the formulas here
 * without updating the screen as well.
 */
object ShipmentPriceCalculator {

    /**
     * Returns the base price in THB for a given service level.
     *
     * @param service "Express", "Same Day", or any other value → Standard
     */
    fun basePrice(service: String): Double = when (service) {
        "Express"  -> 150.0
        "Same Day" -> 250.0
        else       -> 80.0   // "Standard" and any unknown values
    }

    /**
     * Extracts the numeric weight (kg) from a raw user input string such as
     * "1.5 kg" or "2", then multiplies by 20 THB/kg.
     *
     * Returns 0.0 when the string contains no parseable number.
     */
    fun weightSurcharge(rawWeight: String): Double {
        val kg = rawWeight.replace(Regex("[^0-9.]"), "").toDoubleOrNull() ?: return 0.0
        return kg * 20.0
    }

    /**
     * Returns the insurance fee: 50 THB if [hasInsurance] is true, else 0.
     */
    fun insuranceFee(hasInsurance: Boolean): Double = if (hasInsurance) 50.0 else 0.0

    /**
     * Computes the total shipment price.
     */
    fun totalPrice(service: String, rawWeight: String, hasInsurance: Boolean): Double =
        basePrice(service) + weightSurcharge(rawWeight) + insuranceFee(hasInsurance)
}

/**
 * Utility functions for input validation, mirroring the in-screen validation
 * in CreateShipmentScreen (the Button onClick guard).
 */
object ShipmentValidator {

    /** Returns true when the shipment can proceed to payment. */
    fun canProceed(recipient: String, destination: String): Boolean =
        recipient.isNotBlank() && destination.isNotBlank()

    /** Returns true when a Thai postal code has exactly 5 digit characters. */
    fun isValidPostalCode(zip: String): Boolean =
        zip.length == 5 && zip.all { it.isDigit() }
}
