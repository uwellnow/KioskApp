package com.app.stronglife.data.local.serial

object Gs805Protocol {
    private const val H1: Int = 0xAA
    private const val H2: Int = 0x55

    fun build(cmd: Int, data: ByteArray = byteArrayOf()): ByteArray {
        require(cmd in 0x00..0xFF)
        val len = 1 /*CMD*/ + data.size + 1 /*SUM*/
        var sum = (H1 + H2 + (len and 0xFF) + cmd) and 0xFF
        for (b in data) sum = (sum + (b.toInt() and 0xFF)) and 0xFF
        return byteArrayOf(H1.toByte(), H2.toByte(), len.toByte(), cmd.toByte()) +
                data + byteArrayOf(sum.toByte())
    }

    fun makeDrink(drinkNo: Int, localOrCmd: Int): ByteArray {
        val d = byteArrayOf(drinkNo.toByte(), localOrCmd.toByte())
        return build(0x01, d)
    }

    fun queryErrorCode(): ByteArray = byteArrayOf(
        0xAA.toByte(), 0x55.toByte(),
        0x02, 0x0C, 0x0D
    )

    /**
     * 3계열 0x15 레시피 저장 (슬롯 8쌍: 분말/물)
     * LEN = 0x23 (35) = CMD(1) + DATA(33) + SUM(1)
     * DATA = Drink_NO(1) + (PowderH, PowderL, WaterH, WaterL) * 8
     * 각 값 범위: 분말 1..999, 물 1..999 (0은 “해당 채널 미사용”)
     * 단위: 기본 0.1초(유량계 기종은 물이 g일 수 있음)
     */
    fun recipeSeries3(
        drinkNo: Int,
        slots: List<Pair<Int, Int>> // [(분말1, 물1), ... (분말8, 물8)]
    ): ByteArray {
        require(drinkNo in 0x01..0x17) { "invalid drinkNo" }
        require(slots.size == 8) { "series-3 requires 8 slots" }

        val data = ByteArray(1 + 8 * 4)
        var i = 0
        data[i++] = drinkNo.toByte()
        for ((powder, water) in slots) {
            require(powder in 0..999 && water in 0..999)
            data[i++] = ((powder shr 8) and 0xFF).toByte()
            data[i++] = (powder and 0xFF).toByte()
            data[i++] = ((water shr 8) and 0xFF).toByte()
            data[i++] = (water and 0xFF).toByte()
        }
        return build(0x15, data)
    }
}
