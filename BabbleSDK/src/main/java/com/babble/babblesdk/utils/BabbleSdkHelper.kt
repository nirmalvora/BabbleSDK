package com.babble.babblesdk.utils

import android.graphics.Color
import android.util.Log
import androidx.core.graphics.ColorUtils
import com.babble.babblesdk.TAG
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone

internal object BabbleSdkHelper {
    fun initializationFailed() {
        Log.e(TAG, "Initialization failed")
    }

    fun notInitialized() {
        Log.e(TAG, "Babble sdk not Initialized")
    }

    fun surveyNotFoundForTrigger() {
        Log.e(TAG, "Survey not found for specified trigger")
    }

    fun matchingCohortNotFound() {
        Log.e(TAG, "No matching cohorts")
    }

    fun matchingEventsNotFound() {
        Log.e(TAG, "No matching events")
    }

    fun notEligibleSurvey() {
        Log.e(TAG, "Given survey id is not eligible")
    }

    fun samplingFail() {
        Log.e(TAG, "Sampling fail")
    }

    fun getCurrentDate(useGMT: Boolean = false): String {
        if (useGMT) {
            val cal: Calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"))
            val currentLocalTime: Date = cal.time
            val date: DateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
            date.timeZone = TimeZone.getTimeZone("GMT")
            return date.format(currentLocalTime)
        }
        return SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault()).format(
            Date()
        )
    }

    fun convertStringToDate(date: String?): Date? {
        return date?.let {
            SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault()).parse(
                it
            )
        }
    }

    fun getRandomString(length: Int): String {
        val allowedChars = ('A'..'Z') + ('a'..'z') + ('0'..'9')
        return (1..length).map { allowedChars.random() }.joinToString("")
    }

    fun getIdFromStringPath(string: String?): String? {
        return string?.substring(
            string.lastIndexOf('/').plus(1)
        )

    }

    fun manipulateColor(color: Int, factor: Float): Int {
        var factorTemp = factor
        factorTemp = 1.0f - factorTemp
        return ColorUtils.blendARGB(color, Color.WHITE, factorTemp)
    }


}