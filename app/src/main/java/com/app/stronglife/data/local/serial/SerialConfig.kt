package com.app.stronglife.data.local.serial

data class SerialConfig(
    val devicePath: String = "/dev/ttyS7",
    val baudRate: Int = 9600,
    val dataBits: Int = 8,   // 5..8
    val parity: Int = 0,     // 0:NONE, 1:ODD, 2:EVEN
    val stopBits: Int = 1,   // 1 or 2
    val readBufferSize: Int = 1024
)
