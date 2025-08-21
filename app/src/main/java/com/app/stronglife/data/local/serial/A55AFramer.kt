package com.app.stronglife.data.local.serial

/**
 * Frame format: A5 5A | LEN | ...LEN bytes...
 * total = 2(header) + 1(len) + LEN
 *
 * 개선점:
 * - reset() 추가 (재시작 시 잔여 바이트 제거)
 * - 내부 버퍼를 ByteArray + size 포인터로 관리 (removeAt(0) 제거)
 * - 비정상 LEN (과대/음수) 방어 및 재동기화
 * - 부분 프레임은 보류하고 다음 feed()에서 이어받음
 */
internal class A55AFramer(
    private val maxLen: Int = 2048   // 프레임 최대 길이 방어 (필요 시 조정)
) {
    private var buf = ByteArray(4096)
    private var size = 0  // buf[0 until size] 유효

    /** 남은 잔여 바이트를 모두 폐기 */
    fun reset() { size = 0 }

    fun feed(bytes: ByteArray): List<ByteArray> {
        if (bytes.isNotEmpty()) {
            ensureCapacity(size + bytes.size)
            System.arraycopy(bytes, 0, buf, size, bytes.size)
            size += bytes.size
        }

        val frames = ArrayList<ByteArray>()
        var i = 0 // 스캔 포인터

        while (size - i >= 3) {
            // 1) 헤더 동기화 (A5 5A 찾기)
            while (i <= size - 2 && !(buf[i] == 0xA5.toByte() && buf[i + 1] == 0x5A.toByte())) {
                i++
            }
            if (i > size - 2) {
                // 헤더 없음 → i 앞부분 폐기
                discard(i)
                break
            }

            // 2) 길이 확인
            if (size - i < 3) break
            val len = buf[i + 2].toInt() and 0xFF

            // 과도한 LEN 방어(재동기화). 필요 시 정책 변경 가능.
            if (len > maxLen) {
                // 현재 바이트를 헤더로 인정하지 않고 한 바이트만 전진하여 재탐색
                i += 1
                continue
            }

            val total = 3 + len
            if (size - i < total) {
                // 전체 프레임이 아직 안 옴 → 다음 feed()에서 이어서 처리
                break
            }

            // 3) 완전 프레임 추출
            val frame = buf.copyOfRange(i, i + total)
            frames.add(frame)
            i += total
        }

        // 소비한 바이트만큼 앞당기기
        if (i > 0) discard(i)

        return frames
    }

    // ----- 내부 유틸 -----

    private fun ensureCapacity(required: Int) {
        if (required <= buf.size) return
        var newCap = buf.size
        while (newCap < required) newCap *= 2
        buf = buf.copyOf(newCap)
    }

    private fun discard(n: Int) {
        if (n <= 0) return
        val remain = size - n
        if (remain > 0) {
            System.arraycopy(buf, n, buf, 0, remain)
        }
        size = remain
    }
}
