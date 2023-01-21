package com.babble.babblesdk.utils

import android.util.Log
import com.babble.babblesdk.TAG
import java.text.SimpleDateFormat
import java.util.*

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

    fun getCurrentDate(): String {
        return SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssz", Locale.getDefault()).format(
            Date()
        )
    }

    fun getRandomString(length: Int) : String {
        val allowedChars = ('A'..'Z') + ('a'..'z') + ('0'..'9')
        return (1..length)
            .map { allowedChars.random() }
            .joinToString("")
    }
}