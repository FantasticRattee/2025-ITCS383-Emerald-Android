package com.emerald.postoffice.data

import org.junit.Assert.*
import org.junit.Test

/**
 * Unit tests for [ThaiPostalCodes].
 *
 * Strategy:
 *  - Test every distinct prefix group (10-96) with one representative code.
 *  - Test boundary/edge conditions that the function explicitly handles.
 *  - Tests are pure JVM – no Android framework needed.
 */
class ThaiPostalCodesTest {

    // ── Happy-path: known prefixes ────────────────────────────────────────────

    @Test
    fun `lookupProvince returns Bangkok for prefix 10`() {
        assertEquals("Bangkok", ThaiPostalCodes.lookupProvince("10110"))
    }

    @Test
    fun `lookupProvince returns Nonthaburi for prefix 11`() {
        assertEquals("Nonthaburi", ThaiPostalCodes.lookupProvince("11000"))
    }

    @Test
    fun `lookupProvince returns Pathum Thani for prefix 12`() {
        assertEquals("Pathum Thani", ThaiPostalCodes.lookupProvince("12000"))
    }

    @Test
    fun `lookupProvince returns Phra Nakhon Si Ayutthaya for prefix 13`() {
        assertEquals("Phra Nakhon Si Ayutthaya", ThaiPostalCodes.lookupProvince("13000"))
    }

    @Test
    fun `lookupProvince returns Chonburi for prefix 20`() {
        assertEquals("Chonburi", ThaiPostalCodes.lookupProvince("20000"))
    }

    @Test
    fun `lookupProvince returns Chiang Mai for prefix 50`() {
        assertEquals("Chiang Mai", ThaiPostalCodes.lookupProvince("50200"))
    }

    @Test
    fun `lookupProvince returns Phuket for prefix 83`() {
        assertEquals("Phuket", ThaiPostalCodes.lookupProvince("83000"))
    }

    @Test
    fun `lookupProvince returns Songkhla for prefix 90`() {
        assertEquals("Songkhla", ThaiPostalCodes.lookupProvince("90110"))
    }

    @Test
    fun `lookupProvince returns Narathiwat for prefix 96`() {
        assertEquals("Narathiwat", ThaiPostalCodes.lookupProvince("96000"))
    }

    @Test
    fun `lookupProvince returns Khon Kaen for prefix 40`() {
        assertEquals("Khon Kaen", ThaiPostalCodes.lookupProvince("40000"))
    }

    @Test
    fun `lookupProvince returns Chiang Rai for prefix 57`() {
        assertEquals("Chiang Rai", ThaiPostalCodes.lookupProvince("57000"))
    }

    @Test
    fun `lookupProvince returns Bueng Kan for prefix 38`() {
        assertEquals("Bueng Kan", ThaiPostalCodes.lookupProvince("38000"))
    }

    // ── Edge cases ────────────────────────────────────────────────────────────

    @Test
    fun `lookupProvince returns null for empty string`() {
        assertNull(ThaiPostalCodes.lookupProvince(""))
    }

    @Test
    fun `lookupProvince returns null for code shorter than 5 digits`() {
        assertNull(ThaiPostalCodes.lookupProvince("1011"))
    }

    @Test
    fun `lookupProvince returns null for code longer than 5 digits`() {
        assertNull(ThaiPostalCodes.lookupProvince("101100"))
    }

    @Test
    fun `lookupProvince returns null for unknown prefix`() {
        // Prefix "00" does not exist in Thai postal codes
        assertNull(ThaiPostalCodes.lookupProvince("00000"))
    }

    @Test
    fun `lookupProvince returns null for prefix 19 which is unassigned`() {
        assertNull(ThaiPostalCodes.lookupProvince("19000"))
    }

    @Test
    fun `lookupProvince returns null for non-numeric input of length 5`() {
        // Length is 5 but prefix "AB" is not in the map
        assertNull(ThaiPostalCodes.lookupProvince("AB123"))
    }

    @Test
    fun `lookupProvince returns null for whitespace-padded code`() {
        // "10110 " has length 6 – should return null
        assertNull(ThaiPostalCodes.lookupProvince("10110 "))
    }

    @Test
    fun `lookupProvince is case-sensitive to map keys`() {
        // Keys are numeric strings; mixed-case input should not match
        assertNull(ThaiPostalCodes.lookupProvince("1A110"))
    }

    // ── Multiple codes for same province ─────────────────────────────────────

    @Test
    fun `lookupProvince returns Bangkok for multiple Bangkok sub-codes`() {
        // Multiple Bangkok sub-district codes all start with "10"
        listOf("10100", "10110", "10120", "10900").forEach { zip ->
            assertEquals("Bangkok", ThaiPostalCodes.lookupProvince(zip))
        }
    }

    // ── Exhaustive prefix assertions ──────────────────────────────────────────

    @Test
    fun `all known prefixes resolve to non-null province`() {
        val knownCodes = mapOf(
            "10110" to "Bangkok",
            "11000" to "Nonthaburi",
            "12000" to "Pathum Thani",
            "13000" to "Phra Nakhon Si Ayutthaya",
            "14000" to "Ang Thong",
            "15000" to "Lopburi",
            "16000" to "Sing Buri",
            "17000" to "Chai Nat",
            "18000" to "Saraburi",
            "20000" to "Chonburi",
            "21000" to "Rayong",
            "22000" to "Chanthaburi",
            "23000" to "Trat",
            "24000" to "Chachoengsao",
            "25000" to "Prachin Buri",
            "26000" to "Nakhon Nayok",
            "27000" to "Sa Kaeo",
            "30000" to "Nakhon Ratchasima",
            "31000" to "Buriram",
            "32000" to "Surin",
            "33000" to "Si Sa Ket",
            "34000" to "Ubon Ratchathani",
            "35000" to "Yasothon",
            "36000" to "Chaiyaphum",
            "37000" to "Amnat Charoen",
            "38000" to "Bueng Kan",
            "39000" to "Nong Bua Lam Phu",
            "40000" to "Khon Kaen",
            "41000" to "Udon Thani",
            "42000" to "Loei",
            "43000" to "Nong Khai",
            "44000" to "Maha Sarakham",
            "45000" to "Roi Et",
            "46000" to "Kalasin",
            "47000" to "Sakon Nakhon",
            "48000" to "Nakhon Phanom",
            "49000" to "Mukdahan",
            "50000" to "Chiang Mai",
            "51000" to "Lamphun",
            "52000" to "Lampang",
            "53000" to "Uttaradit",
            "54000" to "Phrae",
            "55000" to "Nan",
            "56000" to "Phayao",
            "57000" to "Chiang Rai",
            "58000" to "Mae Hong Son",
            "60000" to "Nakhon Sawan",
            "61000" to "Uthai Thani",
            "62000" to "Kamphaeng Phet",
            "63000" to "Tak",
            "64000" to "Sukhothai",
            "65000" to "Phitsanulok",
            "66000" to "Phichit",
            "67000" to "Phetchabun",
            "70000" to "Ratchaburi",
            "71000" to "Kanchanaburi",
            "72000" to "Suphan Buri",
            "73000" to "Nakhon Pathom",
            "74000" to "Samut Sakhon",
            "75000" to "Samut Songkhram",
            "76000" to "Phetchaburi",
            "77000" to "Prachuap Khiri Khan",
            "80000" to "Nakhon Si Thammarat",
            "81000" to "Krabi",
            "82000" to "Phang Nga",
            "83000" to "Phuket",
            "84000" to "Surat Thani",
            "85000" to "Ranong",
            "86000" to "Chumphon",
            "90000" to "Songkhla",
            "91000" to "Satun",
            "92000" to "Trang",
            "93000" to "Phatthalung",
            "94000" to "Pattani",
            "95000" to "Yala",
            "96000" to "Narathiwat",
        )
        knownCodes.forEach { (zip, expectedProvince) ->
            assertEquals("Failed for zip $zip", expectedProvince, ThaiPostalCodes.lookupProvince(zip))
        }
    }
}
