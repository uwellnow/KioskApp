package com.app.stronglife.data.local.serial

private val HEX_ARRAY = "0123456789ABCDEF".toCharArray()

fun ByteArray.toHex(sep: String = " "): String {
    if (isEmpty()) return ""
    val out = StringBuilder(size * 3)
    forEachIndexed { i, b ->
        val v = b.toInt() and 0xFF
        out.append(HEX_ARRAY[v ushr 4]).append(HEX_ARRAY[v and 0x0F])
        if (sep.isNotEmpty() && i != lastIndex) out.append(sep)
    }
    return out.toString()
}

fun hexToBytes(hex: String): ByteArray {
    val s = hex.replace(" ", "").replace("\n", "")
    require(s.length % 2 == 0) { "Invalid hex length" }
    return ByteArray(s.length / 2) { i ->
        s.substring(i * 2, i * 2 + 2).toInt(16).toByte()
    }
}
