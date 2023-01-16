package com.babble.babblesdk

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import com.babble.babblesdk.model.getQuestionModel.Questions
import com.babble.babblesdk.model.getSurveyResponse.SurveyResponse
import com.babble.babblesdk.model.triggerModel.TriggerModel
import com.babble.babblesdk.repository.ApiClient
import com.babble.babblesdk.repository.BabbleApiInterface
import com.babble.babblesdk.ui.SurveyActivity
import com.babble.babblesdk.utils.BabbleConstants
import com.babble.babblesdk.utils.BabbleSdkHelper
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*


internal class BabbleSDKController(context: AppCompatActivity) {
    var mContext: AppCompatActivity? = context
    var triggerData: List<TriggerModel>? = null
    var surveyList: List<SurveyResponse>? = null
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
          val surveyData =  surveyList?.find { it.triggerId==trigger }
            if(surveyData!=null) {
                val surveyIntent =
                    Intent(mContext!!.applicationContext, SurveyActivity::class.java)
                surveyIntent.putExtra(
                    BabbleConstants.surveyDetail,
                    Gson().toJson(surveyData)
                )
                surveyIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                mContext?.startActivity(surveyIntent)
            }else{
                BabbleSdkHelper.surveyNotFoundForTrigger()
            }

        } else {
            BabbleSdkHelper.notInitialized()
        }
    }

    fun init(apiKey: String) {
        this.apiKey=apiKey
        val connectAPI: BabbleApiInterface = ApiClient.getInstance().create(
            BabbleApiInterface::class.java
        )
        connectAPI.getAllTrigger(apiKey).enqueue(object :Callback<List<TriggerModel>>{
            override fun onResponse(
                call: Call<List<TriggerModel>>,
                response: Response<List<TriggerModel>>
            ) {

                if (response.isSuccessful) {
                    triggerData = response.body()
                    connectAPI.getSurvey(apiKey).enqueue(object :Callback<List<SurveyResponse>>{
                        override fun onResponse(
                            call: Call<List<SurveyResponse>>,
                            response: Response<List<SurveyResponse>>
                        ) {

                            if (response.isSuccessful) {
                                isInitialize = true
                                surveyList = response.body()
                            }
                        }

                        override fun onFailure(call: Call<List<SurveyResponse>>, t: Throwable) {
                            BabbleSdkHelper.initializationFailed()
                        }
                    })


                } else {
                    BabbleSdkHelper.initializationFailed()
                }
            }

            override fun onFailure(call: Call<List<TriggerModel>>, t: Throwable) {
                BabbleSdkHelper.initializationFailed()
            }
        })
    }
}
