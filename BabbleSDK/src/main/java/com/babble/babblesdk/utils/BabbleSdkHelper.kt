package com.babble.babblesdk.utils

import android.util.Log
import com.babble.babblesdk.TAG

internal object BabbleSdkHelper {
    fun initializationFailed() {
        Log.e(TAG, "Initialization failed")
    }

    fun notInitialized() {
        Log.e(TAG, "Babble sdk not Initialized")
    }
}