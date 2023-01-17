package com.babble.babblesdk

import androidx.appcompat.app.AppCompatActivity
import com.babble.babblesdk.utils.BabbleSdkHelper


var TAG = "BabbleSDK"

class BabbleSDK(activity: AppCompatActivity) {
    private var activity: AppCompatActivity? = activity


    companion object {
        private lateinit var instance: BabbleSDK
        fun init(activity: AppCompatActivity, apiKey: String) {
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