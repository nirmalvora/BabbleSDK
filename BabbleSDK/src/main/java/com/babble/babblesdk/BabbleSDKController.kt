package com.babble.babblesdk

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.util.Log
import com.babble.babblesdk.model.CustomerPropertiesRequest
import com.babble.babblesdk.model.EligibleSurveyRequest
import com.babble.babblesdk.model.EligibleSurveyResponse.EligibleSurveyResponse
import com.babble.babblesdk.model.SurveyInstanceRequest
import com.babble.babblesdk.model.backendEventResponse.BackedEventResponse
import com.babble.babblesdk.model.cohortResponse.CohortResponse
import com.babble.babblesdk.model.questionsForUser.UserQuestionResponse
import com.babble.babblesdk.model.styleForUserIdResponse.StyleForUserIdResponse
import com.babble.babblesdk.model.surveyForUsers.UserSurveyResponse
import com.babble.babblesdk.model.triggerForUser.UserTriggerResponse
import com.babble.babblesdk.repository.ApiClient
import com.babble.babblesdk.repository.BabbleApiInterface
import com.babble.babblesdk.ui.SurveyActivity
import com.babble.babblesdk.utils.BabbleConstants
import com.babble.babblesdk.utils.BabbleSdkHelper
import com.babble.babblesdk.utils.BabbleSdkHelper.convertStringToDate
import com.babble.babblesdk.utils.BabbleSdkHelper.getCurrentDate
import com.babble.babblesdk.utils.BabbleSdkHelper.getIdFromStringPath
import com.google.gson.Gson
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*


internal class BabbleSDKController(context: Context) {
    private var mContext: Context? = context
    var userId: String? = null
    var themeColor: String = "#5D5FEF"
    var surveyInstanceId: String? = null
    var cohortIds: List<String>? = null
    var backendEvents: List<BackedEventResponse>? = null
    var eligibleSurveyResponse: EligibleSurveyResponse? = null
    private var babbleCustomerId: String? = null
    private var isInitialize: Boolean = false
    private var disposable: Disposable? = null
    var userSurveyResponse: List<UserSurveyResponse>? = null
    var userTriggerResponse: List<UserTriggerResponse>? = null
    var userQuestionResponse: List<UserQuestionResponse>? = null
    var styleForUserIdResponse: List<StyleForUserIdResponse>? = null

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

    fun init(userId: String) {
        this.userId = userId
        val babbleApi: BabbleApiInterface = ApiClient.getInstance().create(
            BabbleApiInterface::class.java
        )

        disposable = Observable.zip(
            babbleApi.getSurveyForUserId(this.userId),
            babbleApi.getTriggerForUserId(this.userId),
            babbleApi.getQuestionForUserId(this.userId),
            babbleApi.getStyleForUserId(this.userId),
            object :
                Function4<List<UserSurveyResponse>, List<UserTriggerResponse>, List<UserQuestionResponse>, List<StyleForUserIdResponse>, Unit> {
                override fun invoke(
                    p1: List<UserSurveyResponse>,
                    p2: List<UserTriggerResponse>,
                    p3: List<UserQuestionResponse>,
                    p4: List<StyleForUserIdResponse>,
                ) {
                    userSurveyResponse = p1
                    userTriggerResponse = p2
                    userQuestionResponse = p3
                    styleForUserIdResponse = p4
                }
            })
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { }
            .doOnTerminate { }
            .subscribe(
                {
                    this.isInitialize = true
                    if ((styleForUserIdResponse ?: arrayListOf()).isNotEmpty()) {
                        themeColor =
                            styleForUserIdResponse?.get(0)?.document?.fields?.mainColor?.stringValue
                                ?: "#5D5FEF"
                    }
                },
                {
                    BabbleSdkHelper.initializationFailed()
                }
            )
    }

    // add properties
    fun setCustomerId(customerId: String?, userDetails: HashMap<String, Any?>? = null) {
        this.babbleCustomerId = customerId ?: ""
        val babbleApi: BabbleApiInterface = ApiClient.getInstance().create(
            BabbleApiInterface::class.java
        )

        babbleApi.getCohorts(userId = this.userId, customerId = this.babbleCustomerId)
            .enqueue(object : Callback<List<CohortResponse>> {
                override fun onResponse(
                    call: Call<List<CohortResponse>>,
                    response: Response<List<CohortResponse>>,
                ) {
                    cohortIds =
                        response.body()
                            ?.map { getIdFromStringPath(it.document?.name) ?: "" }
                            ?: arrayListOf()
                }

                override fun onFailure(call: Call<List<CohortResponse>>, t: Throwable) {
                    Log.e(TAG, "getCohorts: onFailure: $t")
                }
            })

        babbleApi.getBackendEvents(userId = this.userId, customerId = this.babbleCustomerId)
            .enqueue(object : Callback<List<BackedEventResponse>> {
                override fun onResponse(
                    call: Call<List<BackedEventResponse>>,
                    response: Response<List<BackedEventResponse>>,
                ) {
                    backendEvents = response.body()
                }

                override fun onFailure(call: Call<List<BackedEventResponse>>, t: Throwable) {
                    Log.e(TAG, "getCohorts: onFailure: $t")
                }
            })

        val eligibleSurveyRequest =
            EligibleSurveyRequest(babbleUserId = this.userId, customerId = this.babbleCustomerId)
        babbleApi.getEligibleSurveyIds(eligibleSurveyRequest)
            .enqueue(object : Callback<EligibleSurveyResponse> {
                override fun onResponse(
                    call: Call<EligibleSurveyResponse>,
                    response: Response<EligibleSurveyResponse>,
                ) {
                    eligibleSurveyResponse = response.body()
                }

                override fun onFailure(call: Call<EligibleSurveyResponse>, t: Throwable) {
                    Log.e(TAG, "getCohorts: onFailure: $t")
                }
            })
        if (userDetails != null) {
            babbleApi.setCustomerProperties(CustomerPropertiesRequest(userId = this.userId,
                customerId = this.babbleCustomerId,
                properties = userDetails))
                .enqueue(object : Callback<ResponseBody> {
                    override fun onResponse(
                        call: Call<ResponseBody>,
                        response: Response<ResponseBody>,
                    ) {
                    }

                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                        Log.e(TAG, "createSurveyInstance: onFailure: $t")
                    }
                })
        }
    }

    fun trigger(trigger: String, properties: HashMap<String, Any?>? = null) {
        if (isInitialize) {
            val triggerData = userTriggerResponse?.find {
                (it.document?.fields?.name?.stringValue ?: "") == trigger
            }
            val triggerId = getIdFromStringPath(triggerData?.document?.name)

            val survey = userSurveyResponse?.sortedByDescending {
                val date: Date? = convertStringToDate(it.document?.fields?.createdAt?.stringValue)
                date
            }?.find {
                (it.document?.fields?.triggerId?.stringValue ?: "") == triggerId
            }
            val surveyId = getIdFromStringPath(survey?.document?.name)
            val isEligibleSurvey =
                (eligibleSurveyResponse?.eligibleSurveyIds ?: arrayListOf()).contains(surveyId)
            if (isEligibleSurvey) {
                val questionList = userQuestionResponse?.filter {
                    (it.document?.fields?.surveyId?.stringValue ?: "") == surveyId
                }


                val cohortId: String? = survey?.document?.fields?.cohortId?.stringValue
                val eventName: String? = survey?.document?.fields?.eventName?.stringValue

                val eventList = backendEvents?.filter {
                    val date: Date? =
                        convertStringToDate(it.document?.fields?.createdAt?.stringValue)
                    val currentDate: Date = convertStringToDate(getCurrentDate())!!
                    val calendar = Calendar.getInstance()
                    var dateCheck = false
                    if (date != null && survey?.document?.fields?.relevancePeriod?.stringValue != null && (survey.document?.fields?.relevancePeriod?.stringValue
                            ?: "").isNotEmpty()
                    ) {
                        calendar.time = date
                        calendar.add(
                            Calendar.HOUR,
                            Integer.parseInt(
                                survey.document?.fields?.relevancePeriod?.stringValue ?: "0"
                            )
                        )
                        dateCheck = currentDate.before(
                            calendar.time
                        )
                    }
                    if (survey?.document?.fields?.relevancePeriod?.stringValue == null || (survey.document?.fields?.relevancePeriod?.stringValue
                            ?: "").isEmpty()
                    ) {
                        dateCheck = true
                    }
                    it.document?.fields?.eventName?.stringValue == eventName && dateCheck
                }
                val cohortCheck = (cohortId == null || cohortId.isEmpty() || cohortIds?.contains(
                    cohortId
                ) == true)

                val eventCheck = eventName == null || eventName.isEmpty() || (eventList
                    ?: arrayListOf()).isNotEmpty()

                val showSurvey =
                    questionList != null && questionList.isNotEmpty() && cohortCheck && eventCheck

                if (showSurvey) {
                    createSurveyInstance(surveyId = surveyId,
                        eventList = eventList,
                        properties = properties)
                    val surveyIntent =
                        Intent(mContext!!.applicationContext, SurveyActivity::class.java)
                    surveyIntent.putExtra(
                        BabbleConstants.surveyDetail, Gson().toJson(questionList)
                    )
                    surveyIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    mContext?.applicationContext?.startActivity(surveyIntent)
                } else {
                    if (!cohortCheck && cohortIds?.contains(
                            cohortId
                        ) == false
                    ) {
                        BabbleSdkHelper.matchingCohortNotFound()
                    }
                    if (!eventCheck && (eventList ?: arrayListOf()).isEmpty()) {
                        BabbleSdkHelper.matchingEventsNotFound()
                    }
                    if (questionList != null && questionList.isNotEmpty()) {
                        BabbleSdkHelper.surveyNotFoundForTrigger()
                    }
                }
            } else {
                BabbleSdkHelper.notEligibleSurvey()
            }
        } else {
            BabbleSdkHelper.notInitialized()
        }
    }

    private fun createSurveyInstance(
        surveyId: String?,
        eventList: List<BackedEventResponse>?,
        properties: HashMap<String, Any?>?,
    ) {

        surveyInstanceId = BabbleSdkHelper.getRandomString(10)
        val surveyInstanceRequest = SurveyInstanceRequest(
            customerId = this.babbleCustomerId,
            surveyId = surveyId,
            timeVal = getCurrentDate(),
            userId = this.userId,
            surveyInstanceId = surveyInstanceId,
            backendEventIds = eventList?.map { getIdFromStringPath(it.document?.name) ?: "" }
                ?: arrayListOf(),
            properties = properties,
            devicePlatform = "Android"
        )
        val babbleApi: BabbleApiInterface = ApiClient.getInstance().create(
            BabbleApiInterface::class.java
        )
        babbleApi.createSurveyInstance(surveyInstanceRequest)
            .enqueue(object : Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>,
                ) {
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Log.e(TAG, "createSurveyInstance: onFailure: $t")
                }
            })

    }
}
