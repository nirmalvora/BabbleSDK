package com.babble.babblesdk

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import com.babble.babblesdk.model.InitResponse
import com.babble.babblesdk.model.getQuestionModel.QuestionResponse
import com.babble.babblesdk.repository.ApiClient
import com.babble.babblesdk.repository.BabbleApiInterface
import com.babble.babblesdk.ui.SurveyActivity
import com.babble.babblesdk.utils.BabbleSdkHelper
import com.babble.babblesdk.utils.BabbleConstants
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import java.text.SimpleDateFormat
import java.util.*


internal class BabbleSDKController(context: AppCompatActivity) {
    var mContext: AppCompatActivity? = context
    var babbleUserId: String? = null
    private var apiKey: String? = null
    var isInitialize: Boolean = false

    companion object {
        private var sc: BabbleSDKController? = null
        fun getInstance(context: AppCompatActivity): BabbleSDKController? {
            if (sc == null) {
                sc = BabbleSDKController(context)
            }
            return sc
        }

    }

    fun trigger(trigger: String) {
        if (isInitialize) {
            val connectAPI: BabbleApiInterface = ApiClient.getInstance().create(
                BabbleApiInterface::class.java
            )
            val date: String = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssz", Locale.getDefault()).format(
                Date()
            )
            connectAPI.getQuestionForTrigger(apiKey, trigger,date,babbleUserId).enqueue(object : Callback<List<QuestionResponse>> {
                override fun onResponse(
                    call: Call<List<QuestionResponse>>,
                    response: retrofit2.Response<List<QuestionResponse>>
                ) {
                    if (response.isSuccessful) {
                        val surveyIntent =
                            Intent(mContext!!.applicationContext, SurveyActivity::class.java)
                        surveyIntent.putExtra(
                            BabbleConstants.surveyDetail,
                            Gson().toJson(response.body())
                        )
                        surveyIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        mContext?.startActivity(surveyIntent)
                    }
                }

                override fun onFailure(call: Call<List<QuestionResponse>>, t: Throwable) {
                    BabbleSdkHelper.initializationFailed()
                }
            })

        } else {
            BabbleSdkHelper.notInitialized()
        }
    }

    fun init(apiKey: String) {
        this.apiKey=apiKey
        val connectAPI: BabbleApiInterface = ApiClient.getInstance().create(
            BabbleApiInterface::class.java
        )
        connectAPI.initializeSdk(apiKey).enqueue(object : Callback<InitResponse> {
            override fun onResponse(
                call: Call<InitResponse>,
                response: retrofit2.Response<InitResponse>
            ) {
                if (response.isSuccessful) {
                    isInitialize = true
                    babbleUserId = response.body()?.babbleUserId
                } else {
                    BabbleSdkHelper.initializationFailed()
                }

            }

            override fun onFailure(call: Call<InitResponse>, t: Throwable) {
                BabbleSdkHelper.initializationFailed()
            }
        })
    }
}