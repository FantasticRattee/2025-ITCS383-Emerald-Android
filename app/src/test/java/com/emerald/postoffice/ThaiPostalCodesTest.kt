package com.emerald.postoffice

import com.emerald.postoffice.data.ThaiPostalCodes
import org.junit.Test
import org.junit.Assert.*

class ThaiPostalCodesTest {

    @Test
    fun `lookupProvince returns correct province for valid zip code`() {
        // Given
        val zip = "10100"

        // When
        val province = ThaiPostalCodes.lookupProvince(zip)

        // Then
        assertEquals("Bangkok", province)
    }

    @Test
    fun `lookupProvince returns null for invalid zip code length`() {
        // Given
        val zip = "123"

        // When
        val province = ThaiPostalCodes.lookupProvince(zip)

        // Then
        assertNull(province)
    }

    @Test
    fun `lookupProvince returns null for zip code with invalid prefix`() {
        // Given
        val zip = "99999"

        // When
        val province = ThaiPostalCodes.lookupProvince(zip)

        // Then
        assertNull(province)
    }

    @Test
    fun `lookupProvince returns correct province for another valid zip code`() {
        // Given
        val zip = "20100"

        // When
        val province = ThaiPostalCodes.lookupProvince(zip)

        // Then
        assertEquals("Chonburi", province)
    }

    @Test
    fun `lookupProvince handles edge case with minimum length zip`() {
        // Given
        val zip = "10000"

        // When
        val province = ThaiPostalCodes.lookupProvince(zip)

        // Then
        assertEquals("Bangkok", province)
    }

    @Test
    fun `lookupProvince handles edge case with maximum length zip but invalid prefix`() {
        // Given
        val zip = "99999"

        // When
        val province = ThaiPostalCodes.lookupProvince(zip)

        // Then
        assertNull(province)
    }
}