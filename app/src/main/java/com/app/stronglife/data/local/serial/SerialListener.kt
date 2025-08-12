package com.app.stronglife.data.local.serial

interface SerialListener {
    /** A5 5A 프레임을 '원본 그대로' Hex 문자열로 전달 */
    fun onFrame(hex: String)

    /** 원시 바이트(프레이밍 실패 등) 모니터링용 - 선택적 */
    fun onRaw(bytes: ByteArray) {}

    /** 오류/예외 알림 */
    fun onError(t: Throwable)
}
