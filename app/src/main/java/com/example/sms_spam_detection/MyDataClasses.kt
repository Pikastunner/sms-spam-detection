package com.example.sms_spam_detection

import android.text.format.DateUtils
import java.text.SimpleDateFormat
import java.util.*
import java.sql.Timestamp

data class MyNotification(val sender: String, val message: String, val spamProb: Float, val isSpam: Boolean, val timestamp: Long)

object TimeUtils {
    fun getTimeAgo(timestamp: Long): String {
        val now = System.currentTimeMillis()
        val timeDifference = now - timestamp

        return when {
            timeDifference < DateUtils.MINUTE_IN_MILLIS -> {
                "Just now"
            }
            timeDifference < DateUtils.HOUR_IN_MILLIS -> {
                DateUtils.getRelativeTimeSpanString(timestamp, now, DateUtils.MINUTE_IN_MILLIS).toString()
            }
            timeDifference < DateUtils.DAY_IN_MILLIS -> {
                DateUtils.getRelativeTimeSpanString(timestamp, now, DateUtils.HOUR_IN_MILLIS).toString()
            }
            else -> {
                // Format for longer periods, you can customize this as needed
                val dateFormat = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault())
                dateFormat.format(Date(timestamp))
            }
        }
    }
}
