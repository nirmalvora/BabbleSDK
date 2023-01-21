package com.babble.babblesdk

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.util.Log
import com.babble.babblesdk.model.SurveyInstanceRequest
import com.babble.babblesdk.model.questionsForUser.UserQuestionResponse
import com.babble.babblesdk.model.surveyForUsers.UserSurveyResponse
import com.babble.babblesdk.model.triggerForUser.UserTriggerResponse
import com.babble.babblesdk.repository.ApiClient
import com.babble.babblesdk.repository.BabbleApiInterface
import com.babble.babblesdk.ui.SurveyActivity
import com.babble.babblesdk.utils.BabbleConstants
import com.babble.babblesdk.utils.BabbleSdkHelper
import com.google.gson.Gson
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import java.util.*


internal class BabbleSDKController(context: Context) {
    private var mContext: Context? = context

    private var userId: String? = null
    private var babbleCustomerId: String? = null
    private var isInitialize: Boolean = false

    var userSurveyResponse: List<UserSurveyResponse>? = null
    var userTriggerResponse: List<UserTriggerResponse>? = null
    var userQuestionResponse: List<UserQuestionResponse>? = null

    companion object {
        @SuppressLint("StaticFieldLeak")
        private var sc: BabbleSDKController? = null
        fun getInstance(context: Context): BabbleSDKController? {
            if (sc == null) {
                sc = BabbleSDKController(context)
            }
            return sc
        }
    }

    fun trigger(trigger: String) {
        if (isInitialize) {
            val triggerData = userTriggerResponse?.find {
                (it.document?.fields?.name?.stringValue ?: "") == trigger
            }
            val triggerId = triggerData?.document?.name?.substring(
                triggerData.document?.name?.lastIndexOf('/')
                    ?.plus(1) ?: 0
            )
            val survey = userSurveyResponse?.find {
                (it.document?.fields?.triggerId?.stringValue ?: "") == triggerId
            }
            val surveyId = survey?.document?.name?.substring(
                survey.document?.name?.lastIndexOf('/')
                    ?.plus(1) ?: 0
            )

            val questionList = userQuestionResponse?.filter {
                (it.document?.fields?.surveyId?.stringValue ?: "") == surveyId
            }
            val surveyInstanceRequest = SurveyInstanceRequest(
                customerId = this.babbleCustomerId,
                surveyId = surveyId,
                timeVal = BabbleSdkHelper.getCurrentDate(),
                userId = this.userId,
                surveyInstanceId = BabbleSdkHelper.getRandomString(10)
            )
            val babbleApi: BabbleApiInterface = ApiClient.getInstance().create(
                BabbleApiInterface::class.java
            )
            babbleApi.createSurveyInstance(surveyInstanceRequest)
                .enqueue(object : Callback<ResponseBody> {
                    override fun onResponse(
                        call: Call<ResponseBody>,
                        response: retrofit2.Response<ResponseBody>
                    ) {
                        Log.e(TAG, "onResponse: $response")
                        Log.e(TAG, "onResponse: ${response.body()?.string()}")
                    }

                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                        Log.e(TAG, "onFailure: $t")
                    }
                })

            if (questionList != null && questionList.isNotEmpty()) {
                val surveyIntent = Intent(mContext!!.applicationContext, SurveyActivity::class.java)
                surveyIntent.putExtra(
                    BabbleConstants.surveyDetail, Gson().toJson(questionList)
                )
                surveyIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                mContext?.applicationContext?.startActivity(surveyIntent)
            } else {
                BabbleSdkHelper.surveyNotFoundForTrigger()
            }

        } else {
            BabbleSdkHelper.notInitialized()
        }
    }

    fun setCustomerId(customerId: String) {
        this.babbleCustomerId = customerId
        Log.e(TAG, "setCustomerId:${customerId} ")

    }

    private var disposable: Disposable? = null
    fun init(userId: String) {
        this.userId = userId
        val babbleApi: BabbleApiInterface = ApiClient.getInstance().create(
            BabbleApiInterface::class.java
        )

        disposable = Observable.zip(
            babbleApi.getSurveyForUserId(this.userId),
            babbleApi.getTriggerForUserId(this.userId),
            babbleApi.getQuestionForUserId(this.userId),
            object :
                Function3<List<UserSurveyResponse>, List<UserTriggerResponse>, List<UserQuestionResponse>, Unit> {
                override fun invoke(
                    p1: List<UserSurveyResponse>,
                    p2: List<UserTriggerResponse>,
                    p3: List<UserQuestionResponse>
                ) {
                    userSurveyResponse = p1
                    userTriggerResponse = p2
                    userQuestionResponse = p3
                }
            })
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { }
            .doOnTerminate { }
            .subscribe(
                {
                    this.isInitialize = true
                },
                {
                    BabbleSdkHelper.initializationFailed()
                }
            )

    }
}
