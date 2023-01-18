package com.babble.babblesdk

import android.annotation.SuppressLint
import android.app.Activity
import com.babble.babblesdk.utils.BabbleSdkHelper


var TAG = "BabbleSDK"

class BabbleSDK(activity: Activity) {
    private var activity: Activity? = activity


    companion object {
        @SuppressLint("StaticFieldLeak")
        private lateinit var instance: BabbleSDK
        fun init(activity: Activity, apiKey: String) {
            instance = BabbleSDK(activity)
            if (apiKey.isNotEmpty()) {
                BabbleSDKController.getInstance(instance.activity!!)
                    ?.init(apiKey)
            } else {
                BabbleSdkHelper.initializationFailed()
            }
        }

        fun triggerSurvey(trigger: String,customerId: String?=null,params: Any?=null) {
            if (this::instance.isInitialized) {
                BabbleSDKController.getInstance(instance.activity!!)
                    ?.trigger(trigger=trigger,customerId=customerId, params=params)
            } else {
                BabbleSdkHelper.notInitialized()
            }
        }
    }
}