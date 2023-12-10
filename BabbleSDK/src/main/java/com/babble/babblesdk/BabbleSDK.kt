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
                BabbleSDKController.getInstance(instance.activity!!)?.init(userId)
            } else {
                BabbleSdkHelper.initializationFailed()
            }
        }

        fun triggerSurvey(trigger: String, properties: HashMap<String, Any?>? = null) {
            if (this::instance.isInitialized) {
                BabbleSDKController.getInstance(instance.activity!!)
                    ?.trigger(trigger = trigger, properties = properties)
            } else {
                BabbleSdkHelper.notInitialized()
            }
        }

        fun cancelSurvey() {
            if (this::instance.isInitialized) {
                BabbleSDKController.getInstance(instance.activity!!)?.cancelSurvey()
            } else {
                BabbleSdkHelper.notInitialized()
            }
        }

        fun setCustomerId(customerId: String?, userDetails: HashMap<String, Any?>? = null) {
            if (this::instance.isInitialized) {
                BabbleSDKController.getInstance(instance.activity!!)
                    ?.setCustomerId(customerId = customerId, userDetails = userDetails)
            }
        }
    }
}