package com.babble.babblesdk

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.util.Log
import com.babble.babblesdk.model.CustomerPropertiesRequest
import com.babble.babblesdk.model.EligibleSurveyRequest
import com.babble.babblesdk.model.EligibleSurveyResponse.EligibleSurveyResponse
import com.babble.babblesdk.model.SurveyInstanceRequest
import com.babble.babblesdk.model.backendEventResponse.BackedEventResponse
import com.babble.babblesdk.model.cohortResponse.CohortResponse
import com.babble.babblesdk.model.questionsForUser.UserQuestionResponse
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
import kotlin.concurrent.timerTask


internal class BabbleSDKController(context: Context) {
    private var mContext: Context? = context
    var userId: String? = null
    private var timer = Timer()
    var themeColor: Int = Color.parseColor("#5D5FEF")
    var backgroundColor: Int = Color.parseColor("#5D5FEF")
    var textColor: Int = Color.parseColor("#000000")
    var textColorLight: Int = Color.parseColor("#000000")
    var optionBackgroundColor: Int = Color.parseColor("#000000")
    var greenColor: Int = Color.parseColor("#3eaa1c")
    var redColor: Int = Color.parseColor("#FF0000")
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

        disposable = Observable.zip(babbleApi.getSurveyForUserId(this.userId),
            babbleApi.getTriggerForUserId(this.userId),
            babbleApi.getQuestionForUserId(this.userId),
            object :
                Function3<List<UserSurveyResponse>, List<UserTriggerResponse>, List<UserQuestionResponse>, Unit> {
                override fun invoke(
                    p1: List<UserSurveyResponse>,
                    p2: List<UserTriggerResponse>,
                    p3: List<UserQuestionResponse>,
                ) {
                    userSurveyResponse = p1
                    userTriggerResponse = p2
                    userQuestionResponse = p3
                }
            }).observeOn(AndroidSchedulers.mainThread()).doOnSubscribe { }.doOnTerminate { }
            .subscribe({
                this.isInitialize = true
            }, {
                BabbleSdkHelper.initializationFailed()
            })
    }

    private val chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"

    // add properties
    fun setCustomerId(customerId: String?, userDetails: HashMap<String, Any?>? = null) {

        this.babbleCustomerId = if (customerId != null && customerId != "") {
            customerId
        } else {
            ("Anon" + (1..10).map { chars.random() }.joinToString(""))
        }
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
                        response.body()?.map { getIdFromStringPath(it.document?.name) ?: "" }
                            ?: arrayListOf()
                }

                override fun onFailure(call: Call<List<CohortResponse>>, t: Throwable) {
                    Log.e(TAG, "getCohorts: onFailure: $t")
                }
            })

        getBEAndES()
        if (userDetails != null) {
            babbleApi.setCustomerProperties(
                CustomerPropertiesRequest(
                    userId = this.userId,
                    customerId = this.babbleCustomerId,
                    properties = userDetails
                )
            ).enqueue(object : Callback<ResponseBody> {
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

    fun getBEAndES() {
        val babbleApi: BabbleApiInterface = ApiClient.getInstance().create(
            BabbleApiInterface::class.java
        )
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
    }

    fun trigger(trigger: String, properties: HashMap<String, Any?>? = null) {
        if (isInitialize) {
            val triggerData = userTriggerResponse?.find {
                (it.document?.fields?.name?.stringValue ?: "") == trigger
            }
            val triggerId = getIdFromStringPath(triggerData?.document?.name)

            val surveyList = userSurveyResponse?.sortedByDescending {
                val date: Date? = convertStringToDate(it.document?.fields?.createdAt?.stringValue)
                date
            }?.filter { (it.document?.fields?.triggerId?.stringValue ?: "") == triggerId }

            run breaking@{
                surveyList?.forEach { survey ->
                    val surveyId = getIdFromStringPath(survey.document?.name)
                    val isEligibleSurvey =
                        (eligibleSurveyResponse?.eligibleSurveyIds ?: arrayListOf()).contains(
                            surveyId
                        )
                    if (isEligibleSurvey) {
                        val questionList = userQuestionResponse?.filter {
                            (it.document?.fields?.surveyId?.stringValue ?: "") == surveyId
                        }


                        val cohortId: String? = survey.document?.fields?.cohortId?.stringValue
                        val eventName: String? = survey.document?.fields?.eventName?.stringValue

                        val eventList = backendEvents?.filter {
                            val date: Date? =
                                convertStringToDate(it.document?.fields?.createdAt?.stringValue)
                            val currentDate: Date = convertStringToDate(getCurrentDate(true))!!
                            val calendar = Calendar.getInstance()
                            var dateCheck = false
                            if (date != null && survey.document?.fields?.relevancePeriod?.stringValue != null && (survey.document?.fields?.relevancePeriod?.stringValue
                                    ?: "").isNotEmpty()
                            ) {
                                calendar.time = date
                                calendar.add(
                                    Calendar.HOUR, Integer.parseInt(
                                        survey.document?.fields?.relevancePeriod?.stringValue ?: "0"
                                    )
                                )
                                dateCheck = currentDate.before(
                                    calendar.time
                                )
                            }
                            if (survey.document?.fields?.relevancePeriod?.stringValue == null || (survey.document?.fields?.relevancePeriod?.stringValue
                                    ?: "").isEmpty()
                            ) {
                                dateCheck = true
                            }
                            it.document?.fields?.eventName?.stringValue == eventName && dateCheck
                        }

                        val cohortCheck = (cohortId.isNullOrEmpty() || cohortIds?.contains(
                            cohortId
                        ) == true)

                        val eventCheck =
                            eventName.isNullOrEmpty() || (eventList ?: arrayListOf()).isNotEmpty()


                        val showSurvey = !questionList.isNullOrEmpty() && cohortCheck && eventCheck

                        if (showSurvey) {
                            val randomSample = (0..100).random()
                            val samplingValue = Integer.parseInt(
                                survey.document?.fields?.samplingPercentage?.integerValue ?: "0"
                            )
                            Log.e(TAG, "trigger: $randomSample  $samplingValue")
                            if (randomSample < samplingValue) {


                                timer = Timer()
                                timer.schedule(
                                    timerTask {
                                        createSurveyInstance(
                                            surveyId = surveyId,
                                            eventList = eventList,
                                            properties = properties
                                        )
                                        themeColor = try {
                                            Color.parseColor(
                                                survey.document?.styles?.brandColor ?: "#5D5FEF"
                                            )
                                        } catch (error: Exception) {
                                            Color.parseColor("#5D5FEF")
                                        }

                                        textColor = try {
                                            Color.parseColor(
                                                survey.document?.styles?.textColor ?: "#000000"
                                            )
                                        } catch (error: Exception) {
                                            Color.parseColor("#000000")
                                        }

                                        textColorLight = try {
                                            Color.parseColor(
                                                survey.document?.styles?.textColorLight ?: "#ffffff"
                                            )
                                        } catch (error: Exception) {
                                            Color.parseColor("#ffffff")
                                        }

                                        optionBackgroundColor = try {
                                            Color.parseColor(
                                                survey.document?.styles?.optionBgColor ?: "#5D5FEF"
                                            )
                                        } catch (error: Exception) {
                                            Color.parseColor("#5D5FEF")
                                        }

                                        backgroundColor = try {
                                            Color.parseColor(
                                                survey.document?.styles?.backgroundColor
                                                    ?: "#5D5FEF"
                                            )
                                        } catch (error: Exception) {
                                            Color.parseColor("#5D5FEF")
                                        }

                                        val surveyIntent = Intent(
                                            mContext!!.applicationContext,
                                            SurveyActivity::class.java
                                        )
                                        surveyIntent.putExtra(
                                            BabbleConstants.surveyDetail,
                                            Gson().toJson(questionList)
                                        )
                                        surveyIntent.putExtra(
                                            BabbleConstants.survey, Gson().toJson(survey)
                                        )
                                        surveyIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                        surveyIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
                                        surveyIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                                        mContext?.applicationContext?.startActivity(surveyIntent)
                                    }, (Integer.parseInt(
                                        survey.document?.fields?.triggerDelay?.integerValue ?: "0"
                                    ) * 1000).toLong()
                                )
                                return@breaking
                            } else {
                                BabbleSdkHelper.samplingFail()
                            }
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
                            if (questionList.isNullOrEmpty()) {
                                BabbleSdkHelper.surveyNotFoundForTrigger()
                            }
                        }
                    } else {
                        BabbleSdkHelper.notEligibleSurvey()
                    }
                }
            }


        } else {
            BabbleSdkHelper.notInitialized()
        }
    }

    fun cancelSurvey() {
        try {
            timer.cancel()
            timer.purge()
        } catch (r: Exception) {
            r.printStackTrace()
        }
    }

    private fun createSurveyInstance(
        surveyId: String?,
        eventList: List<BackedEventResponse>?,
        properties: HashMap<String, Any?>?,
    ) {
        surveyInstanceId = BabbleSdkHelper.getRandomString(10)
        val surveyInstanceRequest = SurveyInstanceRequest(customerId = this.babbleCustomerId,
            surveyId = surveyId,
            timeVal = getCurrentDate(),
            userId = this.userId,
            surveyInstanceId = surveyInstanceId,
            backendEventIds = eventList?.map { getIdFromStringPath(it.document?.name) ?: "" }
                ?: arrayListOf(),
            properties = properties,
            devicePlatform = "Android",
            type = "Mobile-app",
            device_type = "Mobile")
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
