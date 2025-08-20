package com.app.stronglife.util

import androidx.compose.runtime.Composable
import java.text.NumberFormat
import java.util.Locale

@Composable
fun NumberWithComma(value: String): String {
    return try {
        val number = value.toDouble()
        val formatter = NumberFormat.getNumberInstance(Locale.getDefault())
        formatter.format(number)
    } catch (e: NumberFormatException) {
        value // 숫자가 아니면 그대로 반환
    }
}