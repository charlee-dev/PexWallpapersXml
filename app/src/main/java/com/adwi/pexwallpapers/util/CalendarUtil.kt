package com.adwi.pexwallpapers.util

import android.os.Build
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class CalendarUtil {

    fun getTodayDate(): String = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val now = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("dd-MM")
        now.format(formatter)
    } else {
        "21 Aug"
    }

    fun getDayOfWeek() =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            LocalDate.now().dayOfWeek.name
        else "Saturday"

    fun getCurrentHour(): String {
        val hours = Calendar.getInstance().get(Calendar.HOUR_OF_DAY).toString()
        return if (hours.toInt() < 10) "0$hours" else hours
    }

    fun getCurrentMinutes(): String {
        val minutes = Calendar.getInstance().get(Calendar.MINUTE).toString()
        return if (minutes.toInt() < 10) "0$minutes" else minutes
    }
}

