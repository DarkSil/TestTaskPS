package com.example.testtaskps.utils

import java.text.SimpleDateFormat
import java.util.Calendar

object DateUtil {

    fun getDayAndMonth(millis: Long) : String {
        val formatter = SimpleDateFormat("dd MMM")
        val calendar: Calendar = Calendar.getInstance()
        calendar.timeInMillis = millis
        return formatter.format(calendar.time)
    }

    fun getDate(millis: Long) : String {
        val formatter = SimpleDateFormat("dd MMM yyyy, hh:mm")
        val calendar: Calendar = Calendar.getInstance()
        calendar.timeInMillis = millis
        return formatter.format(calendar.time)
    }

}