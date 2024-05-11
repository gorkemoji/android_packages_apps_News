package com.gorkemoji.news.time

import java.text.SimpleDateFormat
import java.util.*

class DateConverter {
    companion object {
        private const val INPUT_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'"
        private const val OUTPUT_FORMAT = "dd.MM.yyyy HH:mm"

        fun convertDate(inputDate: String): String {
            val inputDateFormat = SimpleDateFormat(INPUT_FORMAT, Locale.getDefault())
            val outputDateFormat = SimpleDateFormat(OUTPUT_FORMAT, Locale.getDefault())
            inputDateFormat.timeZone = TimeZone.getTimeZone("UTC")

            try {
                val date = inputDateFormat.parse(inputDate)
                return outputDateFormat.format(date!!)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return ""
        }
    }
}
