package ru.androidschool.intensiv.extensions

import android.os.Build
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

fun String.formatDate(pattern: String): String = if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
    val parser = SimpleDateFormat("yyyy-MM-dd", Locale.US)
    val formatter = SimpleDateFormat(pattern, Locale.US)
    formatter.format(parser.parse(this) ?: "")
} else {
    val parsedDate = LocalDate.parse(this, DateTimeFormatter.ISO_DATE)
    parsedDate.format(DateTimeFormatter.ofPattern(pattern))
}
