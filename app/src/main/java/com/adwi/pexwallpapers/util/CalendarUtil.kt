package com.adwi.pexwallpapers.util

import android.os.Build
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class CalendarUtil {

    fun getTodayDate() =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val now = LocalDateTime.now()
            val formatter = DateTimeFormatter.ofPattern("dd-MM")
            val formatted = now.format(formatter)
            formatted
        } else {
            "21 Aug"
        }

    fun getDayOfWeek() =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            LocalDate.now().dayOfWeek.name
        else "Saturday"
}

