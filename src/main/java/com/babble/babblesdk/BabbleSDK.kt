package com.babble.babblesdk

import androidx.appcompat.app.AppCompatActivity;
import com.babble.babblesdk.utils.BabbleSdkHelper


var TAG = "BabbleSDK"

class BabbleSDK(activity: AppCompatActivity) {
    private var activity: AppCompatActivity? = activity


    companion object {
        lateinit var instance: BabbleSDK
        fun init(activity: AppCompatActivity, apiKey: String) {
            instance = BabbleSDK(activity)
            if (!apiKey.isEmpty()) {
                BabbleSDKController.getInstance(instance.activity!!)
                    ?.init(apiKey)
            } else {
                BabbleSdkHelper.initializationFailed()
            }
        }

        fun triggerSurvey(trigger: String) {
            if (this::instance.isInitialized) {
                BabbleSDKController.getInstance(instance.activity!!)
                    ?.trigger(trigger)
            } else {
                BabbleSdkHelper.notInitialized()
            }
        }
    }
}