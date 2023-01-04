package com.babble.babblesdk

import androidx.appcompat.app.AppCompatActivity
import com.babble.babblesdk.repository.ApiClient
import com.babble.babblesdk.repository.BabbleApiInterface
import com.babble.babblesdk.ui.SurveyBottomSheetFragment
import com.babble.babblesdk.utils.BabbleSdkHelper
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback


class BabbleSDKController(context: AppCompatActivity) {
    var mContext: AppCompatActivity? = context
    var babbleUserId: String? = null
    var isInitialize: Boolean = false

    companion object {
        var sc: BabbleSDKController? = null
        fun getInstance(context: AppCompatActivity): BabbleSDKController? {
            if (sc == null) {
                sc = BabbleSDKController(context)
            }
            return sc
        }

    }

    fun trigger(trigger: String) {
        if (isInitialize) {
            val surveyBottomSheet = SurveyBottomSheetFragment(babbleUserId)
            surveyBottomSheet.isCancelable = false;
            mContext?.let { surveyBottomSheet.show(it.supportFragmentManager, "") }
        } else {
            BabbleSdkHelper.notInitialized()
        }
    }

    fun init(apiKey: String) {
        val connectAPI: BabbleApiInterface = ApiClient.getInstance().create(
            BabbleApiInterface::class.java
        )
        connectAPI.initializeSdk(apiKey)?.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(
                call: Call<ResponseBody>,
                response: retrofit2.Response<ResponseBody>
            ) {
                if (response.isSuccessful) {
                    isInitialize = true;
                    babbleUserId = "babble_test_id"
                } else {
                    isInitialize = true;
                    babbleUserId = "babble_test_id"
                    BabbleSdkHelper.initializationFailed()
                }

            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                BabbleSdkHelper.initializationFailed()
            }
        })
    }
}