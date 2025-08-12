package com.app.stronglife.data.local.serial

/**
 * A5 5A | LEN | ...LEN bytes... 구조의 프레임을 추출.
 * 총길이 = 2(FLAG) + 1(LEN) + LEN
 */
internal class A55AFramer {
    private val buf = ArrayList<Byte>(4096)

    fun feed(bytes: ByteArray): List<ByteArray> {
        if (bytes.isNotEmpty()) buf.addAll(bytes.toList())
        val frames = mutableListOf<ByteArray>()

        while (buf.size >= 3) {
            // 헤더 동기화
            var start = -1
            for (i in 0 until buf.size - 1) {
                if (buf[i] == 0xA5.toByte() && buf[i + 1] == 0x5A.toByte()) {
                    start = i; break
                }
            }
            if (start == -1) {
                // 헤더 못 찾음 → 버퍼 비움
                buf.clear(); break
            }
            // 헤더 이전 잡바이트 제거
            if (start > 0) repeat(start) { buf.removeAt(0) }

            if (buf.size < 3) break
            val len = buf[2].toInt() and 0xFF
            val total = 2 + 1 + len
            if (buf.size < total) break

            val frame = ByteArray(total)
            for (i in 0 until total) frame[i] = buf[i]
            repeat(total) { buf.removeAt(0) }
            frames += frame
        }
        return frames
    }
}
