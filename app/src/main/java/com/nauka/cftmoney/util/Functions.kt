package com.nauka.cftmoney.util

import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.util.*

//Функция расширения для преобразования полученных дат в более понятный вид
fun String.parseDate(): String {
    return try {
        val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX", Locale.getDefault())
        val formatter = SimpleDateFormat("HH:mm:ss MMM dd yyyy", Locale.getDefault())
        formatter.format(parser.parse(this))
    } catch (e: ParseException) {
        LocalDateTime.now().toString()
    }
}