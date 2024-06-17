package com.infocorp.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class CurrentDate {
    companion object {
        fun getCurrentDate(): String {
            val date = Date()
            val locale = Locale.getDefault()
            val sdf: SimpleDateFormat = SimpleDateFormat("dd.MM.yyyy", locale)
            return sdf.format(date)
        }
    }
}