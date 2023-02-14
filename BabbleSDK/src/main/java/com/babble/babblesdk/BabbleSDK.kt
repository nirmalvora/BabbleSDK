package com.babble.babblesdk

import android.annotation.SuppressLint
import android.content.Context
import com.babble.babblesdk.utils.BabbleSdkHelper
import java.util.*


var TAG = "BabbleSDK"

class BabbleSDK(activity: Context) {
    private var activity: Context? = activity


    companion object {
        @SuppressLint("StaticFieldLeak")
        private lateinit var instance: BabbleSDK
        fun init(activity: Context, userId: String) {
            instance = BabbleSDK(activity)
            if (userId.isNotEmpty()) {
                BabbleSDKController.getInstance(instance.activity!!)
                    ?.init(userId)
            } else {
                BabbleSdkHelper.initializationFailed()
            }
        }

        fun triggerSurvey(trigger: String) {
            if (this::instance.isInitialized) {
                BabbleSDKController.getInstance(instance.activity!!)
                    ?.trigger(trigger = trigger)
            } else {
                BabbleSdkHelper.notInitialized()
            }
        }

        fun setCustomerId(customerId: String?) {
            if (this::instance.isInitialized) {
                BabbleSDKController.getInstance(instance.activity!!)
                    ?.setCustomerId(customerId = customerId,null)
            }
        }
    }
}