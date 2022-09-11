package com.lovepreet.twitterHomeUi.extensions

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by Lovepreet Singh on 10/09/22.
 */

const val dateFormat: String = "yyyy-MM-dd"

fun String.toDate(): Date{
    val formatStart = SimpleDateFormat(dateFormat, Locale.getDefault())
    try {
        val d = formatStart.parse(this)
        val calendar = Calendar.getInstance()
        calendar.timeZone = TimeZone.getDefault()
        calendar.time = d
        return calendar.time
    }
    catch (e: ParseException) {
        e.printStackTrace()
    }
    return Date()
}