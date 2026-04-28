package com.emerald.postoffice.data

import org.junit.Assert.*
import org.junit.Test

/**
 * Extended / exhaustive tests for [ThaiPostalCodes.lookupProvince].
 *
 * These tests cover every remaining branch, boundary, and code path
 * to push the class towards 100 % line + branch coverage.
 */
class ThaiPostalCodesExtendedTest {

    // ── Length guard: the function returns null for anything ≠ 5 chars ───────

    @Test
    fun lookupProvinceReturnsNullForLengthZero() {
        assertNull(ThaiPostalCodes.lookupProvince(""))
    }

    @Test
    fun lookupProvinceReturnsNullForLengthOne() {
        assertNull(ThaiPostalCodes.lookupProvince("1"))
    }

    @Test
    fun lookupProvinceReturnsNullForLengthTwo() {
        assertNull(ThaiPostalCodes.lookupProvince("10"))
    }

    @Test
    fun lookupProvinceReturnsNullForLengthThree() {
        assertNull(ThaiPostalCodes.lookupProvince("101"))
    }

    @Test
    fun lookupProvinceReturnsNullForLengthFour() {
        assertNull(ThaiPostalCodes.lookupProvince("1011"))
    }

    @Test
    fun lookupProvinceReturnsNullForLengthSix() {
        assertNull(ThaiPostalCodes.lookupProvince("101100"))
    }

    @Test
    fun lookupProvinceReturnsNullForLengthTen() {
        assertNull(ThaiPostalCodes.lookupProvince("1011001011"))
    }

    // ── Unknown prefixes (length = 5 but prefix not in map) ──────────────────

    @Test
    fun lookupProvinceReturnsNullForPrefix00() {
        assertNull(ThaiPostalCodes.lookupProvince("00000"))
    }

    @Test
    fun lookupProvinceReturnsNullForPrefix09() {
        assertNull(ThaiPostalCodes.lookupProvince("09000"))
    }

    @Test
    fun lookupProvinceReturnsNullForPrefix19() {
        assertNull(ThaiPostalCodes.lookupProvince("19000"))
    }

    @Test
    fun lookupProvinceReturnsNullForPrefix28() {
        assertNull(ThaiPostalCodes.lookupProvince("28000"))
    }

    @Test
    fun lookupProvinceReturnsNullForPrefix29() {
        assertNull(ThaiPostalCodes.lookupProvince("29000"))
    }

    @Test
    fun lookupProvinceReturnsNullForPrefix59() {
        assertNull(ThaiPostalCodes.lookupProvince("59000"))
    }

    @Test
    fun lookupProvinceReturnsNullForPrefix68() {
        assertNull(ThaiPostalCodes.lookupProvince("68000"))
    }

    @Test
    fun lookupProvinceReturnsNullForPrefix69() {
        assertNull(ThaiPostalCodes.lookupProvince("69000"))
    }

    @Test
    fun lookupProvinceReturnsNullForPrefix78() {
        assertNull(ThaiPostalCodes.lookupProvince("78000"))
    }

    @Test
    fun lookupProvinceReturnsNullForPrefix79() {
        assertNull(ThaiPostalCodes.lookupProvince("79000"))
    }

    @Test
    fun lookupProvinceReturnsNullForPrefix87() {
        assertNull(ThaiPostalCodes.lookupProvince("87000"))
    }

    @Test
    fun lookupProvinceReturnsNullForPrefix88() {
        assertNull(ThaiPostalCodes.lookupProvince("88000"))
    }

    @Test
    fun lookupProvinceReturnsNullForPrefix89() {
        assertNull(ThaiPostalCodes.lookupProvince("89000"))
    }

    @Test
    fun lookupProvinceReturnsNullForPrefix97() {
        assertNull(ThaiPostalCodes.lookupProvince("97000"))
    }

    @Test
    fun lookupProvinceReturnsNullForPrefix98() {
        assertNull(ThaiPostalCodes.lookupProvince("98000"))
    }

    @Test
    fun lookupProvinceReturnsNullForPrefix99() {
        assertNull(ThaiPostalCodes.lookupProvince("99000"))
    }

    // ── Non-digit 5-char inputs ───────────────────────────────────────────────

    @Test
    fun lookupProvinceReturnsNullForAllSpaces() {
        assertNull(ThaiPostalCodes.lookupProvince("     "))
    }

    @Test
    fun lookupProvinceReturnsNullForMixedAlphaNumeric() {
        assertNull(ThaiPostalCodes.lookupProvince("10A10"))
    }

    @Test
    fun lookupProvinceReturnsNullForSpecialCharPrefix() {
        assertNull(ThaiPostalCodes.lookupProvince("!0110"))
    }

    @Test
    fun lookupProvinceReturnsNullForThaiCharInput() {
        // Thai chars have length > 5 in some encodings but Kotlin String.length counts code units
        // We use a 5-char string with Thai characters whose prefix won't be in the map
        assertNull(ThaiPostalCodes.lookupProvince("กขคงจ"))
    }

    // ── Happy-path: remaining provinces not tested in the primary test file ───

    @Test
    fun lookupProvinceReturnsAngThongForPrefix14() {
        assertEquals("Ang Thong", ThaiPostalCodes.lookupProvince("14000"))
    }

    @Test
    fun lookupProvinceReturnsLopburiForPrefix15() {
        assertEquals("Lopburi", ThaiPostalCodes.lookupProvince("15000"))
    }

    @Test
    fun lookupProvinceReturnsSingBuriForPrefix16() {
        assertEquals("Sing Buri", ThaiPostalCodes.lookupProvince("16000"))
    }

    @Test
    fun lookupProvinceReturnsChaiNatForPrefix17() {
        assertEquals("Chai Nat", ThaiPostalCodes.lookupProvince("17000"))
    }

    @Test
    fun lookupProvinceReturnsSaraburiForPrefix18() {
        assertEquals("Saraburi", ThaiPostalCodes.lookupProvince("18000"))
    }

    @Test
    fun lookupProvinceReturnsRayongForPrefix21() {
        assertEquals("Rayong", ThaiPostalCodes.lookupProvince("21000"))
    }

    @Test
    fun lookupProvinceReturnsChanthaburiForPrefix22() {
        assertEquals("Chanthaburi", ThaiPostalCodes.lookupProvince("22000"))
    }

    @Test
    fun lookupProvinceReturnsTratForPrefix23() {
        assertEquals("Trat", ThaiPostalCodes.lookupProvince("23000"))
    }

    @Test
    fun lookupProvinceReturnsChachoengsaoForPrefix24() {
        assertEquals("Chachoengsao", ThaiPostalCodes.lookupProvince("24000"))
    }

    @Test
    fun lookupProvinceReturnsPrachinBuriForPrefix25() {
        assertEquals("Prachin Buri", ThaiPostalCodes.lookupProvince("25000"))
    }

    @Test
    fun lookupProvinceReturnsNakhonNayokForPrefix26() {
        assertEquals("Nakhon Nayok", ThaiPostalCodes.lookupProvince("26000"))
    }

    @Test
    fun lookupProvinceReturnsSaKaeoForPrefix27() {
        assertEquals("Sa Kaeo", ThaiPostalCodes.lookupProvince("27000"))
    }

    @Test
    fun lookupProvinceReturnsNakhonRatchasimaForPrefix30() {
        assertEquals("Nakhon Ratchasima", ThaiPostalCodes.lookupProvince("30000"))
    }

    @Test
    fun lookupProvinceReturnsBuriramForPrefix31() {
        assertEquals("Buriram", ThaiPostalCodes.lookupProvince("31000"))
    }

    @Test
    fun lookupProvinceReturnsSurinForPrefix32() {
        assertEquals("Surin", ThaiPostalCodes.lookupProvince("32000"))
    }

    @Test
    fun lookupProvinceReturnsSiSaKetForPrefix33() {
        assertEquals("Si Sa Ket", ThaiPostalCodes.lookupProvince("33000"))
    }

    @Test
    fun lookupProvinceReturnsUbonRatchathaniForPrefix34() {
        assertEquals("Ubon Ratchathani", ThaiPostalCodes.lookupProvince("34000"))
    }

    @Test
    fun lookupProvinceReturnsYasothonForPrefix35() {
        assertEquals("Yasothon", ThaiPostalCodes.lookupProvince("35000"))
    }

    @Test
    fun lookupProvinceReturnsChaiyaphumForPrefix36() {
        assertEquals("Chaiyaphum", ThaiPostalCodes.lookupProvince("36000"))
    }

    @Test
    fun lookupProvinceReturnsAmnatCharoenForPrefix37() {
        assertEquals("Amnat Charoen", ThaiPostalCodes.lookupProvince("37000"))
    }

    @Test
    fun lookupProvinceReturnsNongBuaLamPhuForPrefix39() {
        assertEquals("Nong Bua Lam Phu", ThaiPostalCodes.lookupProvince("39000"))
    }

    @Test
    fun lookupProvinceReturnsUdonThaniForPrefix41() {
        assertEquals("Udon Thani", ThaiPostalCodes.lookupProvince("41000"))
    }

    @Test
    fun lookupProvinceReturnsLoeiForPrefix42() {
        assertEquals("Loei", ThaiPostalCodes.lookupProvince("42000"))
    }

    @Test
    fun lookupProvinceReturnsNongKhaiForPrefix43() {
        assertEquals("Nong Khai", ThaiPostalCodes.lookupProvince("43000"))
    }

    @Test
    fun lookupProvinceReturnsMahaSarakhamForPrefix44() {
        assertEquals("Maha Sarakham", ThaiPostalCodes.lookupProvince("44000"))
    }

    @Test
    fun lookupProvinceReturnsRoiEtForPrefix45() {
        assertEquals("Roi Et", ThaiPostalCodes.lookupProvince("45000"))
    }

    @Test
    fun lookupProvinceReturnsKalasinForPrefix46() {
        assertEquals("Kalasin", ThaiPostalCodes.lookupProvince("46000"))
    }

    @Test
    fun lookupProvinceReturnsSakonNakhonForPrefix47() {
        assertEquals("Sakon Nakhon", ThaiPostalCodes.lookupProvince("47000"))
    }

    @Test
    fun lookupProvinceReturnsNakhonPhanomForPrefix48() {
        assertEquals("Nakhon Phanom", ThaiPostalCodes.lookupProvince("48000"))
    }

    @Test
    fun lookupProvinceReturnsMukdahanForPrefix49() {
        assertEquals("Mukdahan", ThaiPostalCodes.lookupProvince("49000"))
    }

    @Test
    fun lookupProvinceReturnsLamphunForPrefix51() {
        assertEquals("Lamphun", ThaiPostalCodes.lookupProvince("51000"))
    }

    @Test
    fun lookupProvinceReturnsLampangForPrefix52() {
        assertEquals("Lampang", ThaiPostalCodes.lookupProvince("52000"))
    }

    @Test
    fun lookupProvinceReturnsUttaraditForPrefix53() {
        assertEquals("Uttaradit", ThaiPostalCodes.lookupProvince("53000"))
    }

    @Test
    fun lookupProvinceReturnsPhraeFrPrefix54() {
        assertEquals("Phrae", ThaiPostalCodes.lookupProvince("54000"))
    }

    @Test
    fun lookupProvinceReturnsNanForPrefix55() {
        assertEquals("Nan", ThaiPostalCodes.lookupProvince("55000"))
    }

    @Test
    fun lookupProvinceReturnsPhayaoForPrefix56() {
        assertEquals("Phayao", ThaiPostalCodes.lookupProvince("56000"))
    }

    @Test
    fun lookupProvinceReturnsMaeHongSonForPrefix58() {
        assertEquals("Mae Hong Son", ThaiPostalCodes.lookupProvince("58000"))
    }

    @Test
    fun lookupProvinceReturnsNakhonSawanForPrefix60() {
        assertEquals("Nakhon Sawan", ThaiPostalCodes.lookupProvince("60000"))
    }

    @Test
    fun lookupProvinceReturnsUthaiThaniForPrefix61() {
        assertEquals("Uthai Thani", ThaiPostalCodes.lookupProvince("61000"))
    }

    @Test
    fun lookupProvinceReturnsKamphaengPhetForPrefix62() {
        assertEquals("Kamphaeng Phet", ThaiPostalCodes.lookupProvince("62000"))
    }

    @Test
    fun lookupProvinceReturnsTakForPrefix63() {
        assertEquals("Tak", ThaiPostalCodes.lookupProvince("63000"))
    }

    @Test
    fun lookupProvinceReturnsSukhothaiFrPrefix64() {
        assertEquals("Sukhothai", ThaiPostalCodes.lookupProvince("64000"))
    }

    @Test
    fun lookupProvinceReturnsPhitsanulokForPrefix65() {
        assertEquals("Phitsanulok", ThaiPostalCodes.lookupProvince("65000"))
    }

    @Test
    fun lookupProvinceReturnsPhichitForPrefix66() {
        assertEquals("Phichit", ThaiPostalCodes.lookupProvince("66000"))
    }

    @Test
    fun lookupProvinceReturnsPhetchabunForPrefix67() {
        assertEquals("Phetchabun", ThaiPostalCodes.lookupProvince("67000"))
    }

    @Test
    fun lookupProvinceReturnsRatchaburiForPrefix70() {
        assertEquals("Ratchaburi", ThaiPostalCodes.lookupProvince("70000"))
    }

    @Test
    fun lookupProvinceReturnsKanchanaburiForPrefix71() {
        assertEquals("Kanchanaburi", ThaiPostalCodes.lookupProvince("71000"))
    }

    @Test
    fun lookupProvinceReturnsSuphanBuriForPrefix72() {
        assertEquals("Suphan Buri", ThaiPostalCodes.lookupProvince("72000"))
    }

    @Test
    fun lookupProvinceReturnsNakhonPathomForPrefix73() {
        assertEquals("Nakhon Pathom", ThaiPostalCodes.lookupProvince("73000"))
    }

    @Test
    fun lookupProvinceReturnsSamutSakhonForPrefix74() {
        assertEquals("Samut Sakhon", ThaiPostalCodes.lookupProvince("74000"))
    }

    @Test
    fun lookupProvinceReturnsSamutSongkhramForPrefix75() {
        assertEquals("Samut Songkhram", ThaiPostalCodes.lookupProvince("75000"))
    }

    @Test
    fun lookupProvinceReturnsPhetchaburiForPrefix76() {
        assertEquals("Phetchaburi", ThaiPostalCodes.lookupProvince("76000"))
    }

    @Test
    fun lookupProvinceReturnsPrachuapKhiriKhanForPrefix77() {
        assertEquals("Prachuap Khiri Khan", ThaiPostalCodes.lookupProvince("77000"))
    }

    @Test
    fun lookupProvinceReturnsNakhonSiThammaratForPrefix80() {
        assertEquals("Nakhon Si Thammarat", ThaiPostalCodes.lookupProvince("80000"))
    }

    @Test
    fun lookupProvinceReturnsKrabiForPrefix81() {
        assertEquals("Krabi", ThaiPostalCodes.lookupProvince("81000"))
    }

    @Test
    fun lookupProvinceReturnsPhangNgaForPrefix82() {
        assertEquals("Phang Nga", ThaiPostalCodes.lookupProvince("82000"))
    }

    @Test
    fun lookupProvinceReturnsSuratThaniForPrefix84() {
        assertEquals("Surat Thani", ThaiPostalCodes.lookupProvince("84000"))
    }

    @Test
    fun lookupProvinceReturnsRanongForPrefix85() {
        assertEquals("Ranong", ThaiPostalCodes.lookupProvince("85000"))
    }

    @Test
    fun lookupProvinceReturnsChumphonForPrefix86() {
        assertEquals("Chumphon", ThaiPostalCodes.lookupProvince("86000"))
    }

    @Test
    fun lookupProvinceReturnsSatunForPrefix91() {
        assertEquals("Satun", ThaiPostalCodes.lookupProvince("91000"))
    }

    @Test
    fun lookupProvinceReturnsTrangForPrefix92() {
        assertEquals("Trang", ThaiPostalCodes.lookupProvince("92000"))
    }

    @Test
    fun lookupProvinceReturnsPhattalungForPrefix93() {
        assertEquals("Phatthalung", ThaiPostalCodes.lookupProvince("93000"))
    }

    @Test
    fun lookupProvinceReturnsPattaniForPrefix94() {
        assertEquals("Pattani", ThaiPostalCodes.lookupProvince("94000"))
    }

    @Test
    fun lookupProvinceReturnsYalaForPrefix95() {
        assertEquals("Yala", ThaiPostalCodes.lookupProvince("95000"))
    }

    // ── Verify sub-district codes within valid provinces ─────────────────────

    @Test
    fun lookupProvinceReturnsBangkokForSubDistrict10900() {
        assertEquals("Bangkok", ThaiPostalCodes.lookupProvince("10900"))
    }

    @Test
    fun lookupProvinceReturnsChiangMaiForSubDistrict50300() {
        assertEquals("Chiang Mai", ThaiPostalCodes.lookupProvince("50300"))
    }

    @Test
    fun lookupProvinceReturnsPhuketForSubDistrict83110() {
        assertEquals("Phuket", ThaiPostalCodes.lookupProvince("83110"))
    }

    @Test
    fun lookupProvinceReturnsKhonKaenForSubDistrict40260() {
        assertEquals("Khon Kaen", ThaiPostalCodes.lookupProvince("40260"))
    }

    // ── Verify the function is deterministic (same input → same output) ───────

    @Test
    fun lookupProvinceIsDeterministicForBangkok() {
        val first = ThaiPostalCodes.lookupProvince("10110")
        val second = ThaiPostalCodes.lookupProvince("10110")
        assertEquals(first, second)
    }

    @Test
    fun lookupProvinceIsDeterministicForNull() {
        val first = ThaiPostalCodes.lookupProvince("00000")
        val second = ThaiPostalCodes.lookupProvince("00000")
        assertEquals(first, second)
        assertNull(first)
    }
}
