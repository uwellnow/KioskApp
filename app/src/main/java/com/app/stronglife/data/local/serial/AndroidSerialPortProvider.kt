package com.app.stronglife.data.local.serial

import android.serialport.SerialPort
import java.io.File
import java.io.IOException

internal class AndroidSerialPortProvider {

    data class Opened(
        val port: SerialPort,
        val input: java.io.InputStream,
        val output: java.io.OutputStream
    ) {
        fun closeQuietly() = runCatching { port.tryClose() }
    }

    fun open(cfg: SerialConfig): Opened {
        // 필요시 su 경로 변경 (보드에 따라 /system/xbin/su 일 수도 있음)
        // SerialPort.setSuPath("/system/xbin/su")
        val dev = File(cfg.devicePath)
        if (!dev.exists()) {
            throw IOException("Serial device not found: ${cfg.devicePath} (are you on emulator?)")
        }

        val sp = SerialPort
            .newBuilder(cfg.devicePath, cfg.baudRate)
            .dataBits(cfg.dataBits)
            .parity(cfg.parity)
            .stopBits(cfg.stopBits)
            .build()

        return Opened(sp, sp.getInputStream(), sp.getOutputStream())
    }
}
