package com.babble.babblesdk.ui

import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.content.res.Configuration
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.Window
import android.view.WindowManager
import android.view.animation.DecelerateInterpolator
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.babble.babblesdk.BabbleSDKController
import com.babble.babblesdk.R
import com.babble.babblesdk.TAG
import com.babble.babblesdk.databinding.ActivitySurveyBinding
import com.babble.babblesdk.model.AddResponseRequest
import com.babble.babblesdk.model.SurveyCloseRequest
import com.babble.babblesdk.model.questionsForUser.UserQuestionDocument
import com.babble.babblesdk.model.questionsForUser.UserQuestionFields
import com.babble.babblesdk.model.questionsForUser.UserQuestionInteger
import com.babble.babblesdk.model.questionsForUser.UserQuestionResponse
import com.babble.babblesdk.model.surveyForUsers.UserSurveyResponse
import com.babble.babblesdk.repository.ApiClient
import com.babble.babblesdk.repository.BabbleApiInterface
import com.babble.babblesdk.ui.fragments.BabbleQueFragment
import com.babble.babblesdk.ui.fragments.BabbleQueTextFragment
import com.babble.babblesdk.ui.fragments.BabbleWelcomeFragment
import com.babble.babblesdk.utils.BabbleConstants
import com.babble.babblesdk.utils.BabbleSdkHelper
import com.google.android.play.core.review.ReviewManagerFactory
import com.google.gson.Gson
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*


class SurveyActivity : AppCompatActivity() {
    private var userQuestionList: List<UserQuestionResponse>? = null
    private var surveyData: UserSurveyResponse? = null
    private lateinit var binding: ActivitySurveyBinding
    private var questionId: Int = 0
    private var questionList: List<UserQuestionResponse>? = null
    private var totalQuizQuestion: Int? = null
    private var selectedAnswers: ArrayList<UserQuestionResponse>? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
        binding = ActivitySurveyBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        setUpWindow()
        setUpData()
    }

    private fun setUpData() {

        val surveyDetail = intent.getStringExtra(BabbleConstants.surveyDetail)
        val surveyDataJson = intent.getStringExtra(BabbleConstants.survey)
        userQuestionList =
            Gson().fromJson(surveyDetail, Array<UserQuestionResponse>::class.java).asList()


        surveyData = Gson().fromJson(surveyDataJson!!, UserSurveyResponse::class.java)

        questionList = userQuestionList?.sortedBy {
            it.document?.fields?.sequenceNo?.integerValue?.let { it1 ->
                Integer.parseInt(
                    it1
                )
            }
        }

        if (surveyData?.document?.fields?.isQuiz?.booleanValue == true) {
            totalQuizQuestion = 0
            selectedAnswers = arrayListOf()
            questionList?.forEach {
                if ((it.document?.fields?.questionTypeId?.integerValue ?: "0") == "2") {
                    totalQuizQuestion = totalQuizQuestion!! + 1
                }
            }
            if (questionList?.last()?.document?.fields?.questionTypeId?.integerValue != "9") {
                questionList = questionList?.plus(
                    UserQuestionResponse(
                        document = UserQuestionDocument(
                            fields = UserQuestionFields(questionTypeId = UserQuestionInteger("9"))
                        )
                    )
                )
            }
        }


        binding.pageProgressBar.max = (questionList?.size ?: 0) * 100
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                binding.pageProgressBar.progressTintList =
                    ColorStateList.valueOf(Color.parseColor(BabbleSDKController.getInstance(this)!!.themeColor))
            }
        } catch (nfe: NumberFormatException) {
            BabbleSDKController.getInstance(this)!!.themeColor =
                "#" + Integer.toHexString(ContextCompat.getColor(this, R.color.colorPrimaryDark))
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                binding.pageProgressBar.progressTintList =
                    ColorStateList.valueOf(Color.parseColor(BabbleSDKController.getInstance(this)!!.themeColor))
            }
        }
        setUpUI()
        binding.closeBtnImageView.setOnClickListener {
            finish()
        }

    }

    private fun setUpWindow() {
        val window = this.window
        val wlp = window.attributes
        wlp.gravity = Gravity.BOTTOM
        wlp.width = WindowManager.LayoutParams.MATCH_PARENT
        wlp.height = WindowManager.LayoutParams.WRAP_CONTENT
        wlp.flags = wlp.flags and WindowManager.LayoutParams.FLAG_DIM_BEHIND
        window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        window.setDimAmount(0.25f)
        window.attributes = wlp
    }

    private fun setUpUI() {
        loadFragments()
    }

    private var frag: Fragment? = null
    private fun loadFragments() {
        frag = getFragment()
        if (frag != null) {
            setProgressAnimate()
            val ft = supportFragmentManager.beginTransaction()
            if (questionId == 0) {
                ft.add(R.id.fragment_view, frag!!).commit()
            } else {
                ft.replace(R.id.fragment_view, frag!!).commit()
            }
        }
    }

    private fun setProgressAnimate() {

        val animation: ObjectAnimator = ObjectAnimator.ofInt(
            binding.pageProgressBar, "progress", questionId * 100, (questionId + 1) * 100
        )
        animation.duration = 500
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            animation.setAutoCancel(true)
        }
        animation.interpolator = DecelerateInterpolator()
        animation.start()
    }

    override fun finish() {

        overridePendingTransition(R.anim.nothing, R.anim.slide_down_new_theme)
        val babbleApi: BabbleApiInterface = ApiClient.getInstance().create(
            BabbleApiInterface::class.java
        )
        babbleApi.surveyClose(
            SurveyCloseRequest(
                surveyInstanceId = BabbleSDKController.getInstance(
                    this
                )?.surveyInstanceId
            )
        ).enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                Log.e(TAG, "onResponse: " + response.code())
                BabbleSDKController.getInstance(applicationContext)!!.getBEAndES()
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.e(TAG, "onFailure: " + t.message)
            }

        })
        super.finish()
    }

    private fun getFragment(): Fragment? {
        var frag: Fragment? = null
        try {
            frag = when (questionList!![questionId].document?.fields?.questionTypeId?.integerValue
                ?: "9") {
                "6", "9" -> {
                    var correctAnswerCount: Int? = null
                    if (surveyData?.document?.fields?.isQuiz?.booleanValue == true) {
                        correctAnswerCount = 0
                        selectedAnswers?.forEach { it ->
                            val responseAnswer = it.selectedOptions.joinToString { it }
                            if (it.document?.fields?.correctAnswer?.stringValue == responseAnswer) correctAnswerCount++
                        }
                    }
                    BabbleWelcomeFragment.newInstance(
                        questionList!![questionId],
                        surveyData,
                        totalQuizQuestion,
                        correctAnswerCount
                    )
                }

                "1", "2", "4", "5", "7", "8" -> {
                    BabbleQueFragment.newInstance(questionList!![questionId])
                }

                "3" -> {
                    BabbleQueTextFragment.newInstance(questionList!![questionId])
                }

                else -> {
                    BabbleWelcomeFragment.newInstance(questionList!![questionId], surveyData)
                }
            }
        } catch (_: Exception) {

        }
        return frag
    }

    fun addUserResponse(surveyResponse: UserQuestionResponse?) {
        val questionTypeId = surveyResponse?.document?.fields?.questionTypeId?.integerValue ?: "9"
        var responseAnswer: String? = ""
        var responseForNextQuestion: String? = ""
        when (questionTypeId) {
            "1", "2" -> {
                if ((surveyResponse?.selectedOptions ?: arrayListOf()).isNotEmpty()) {
                    val orderById = (surveyResponse?.document?.fields?.answers?.arrayValue?.values
                        ?: arrayListOf()).withIndex()
                        .associate { (index, it) -> it.stringValue to index }
                    val sortedPeople = (surveyResponse?.selectedOptions
                        ?: arrayListOf()).sortedBy { orderById[it] }
                    responseForNextQuestion = sortedPeople[0]
                }
                responseAnswer = surveyResponse?.selectedOptions?.joinToString { it }
            }

            "3" -> {
                responseAnswer = surveyResponse?.answerText ?: ""
                responseForNextQuestion = responseAnswer
            }

            "4", "5", "7", "8" -> {
                responseAnswer = if (surveyResponse?.selectedRating != null) {
                    surveyResponse.selectedRating.toString()
                } else {
                    null
                }
                responseForNextQuestion = responseAnswer
            }
        }
        selectedAnswers?.add(surveyResponse!!)
        setNextQuestion(surveyResponse!!, responseForNextQuestion, responseAnswer)
    }

    private fun setNextQuestion(
        surveyResponse: UserQuestionResponse,
        checkForNextQuestion: String?,
        responseAnswer: String?,
    ) {
        var hasNextQuestion = true
        if (questionId == (questionList?.size ?: 0) - 1) {
            hasNextQuestion = false
            finish()
        } else {
            if (surveyResponse.document?.fields?.nextQuestion != null && (surveyResponse.document?.fields?.nextQuestion?.get(
                    "mapValue"
                )?.get("fields")
                    ?.get(checkForNextQuestion) != null || surveyResponse.document?.fields?.nextQuestion?.get(
                    "mapValue"
                )?.get("fields")?.get("any") != null)
            ) {
                if ((surveyResponse.document?.fields?.nextQuestion?.get(
                        "mapValue"
                    )?.get("fields")?.get(checkForNextQuestion)?.get("stringValue")
                        ?: "").lowercase() == "in_app_survey"
                ) {
                    val manager = ReviewManagerFactory.create(this)
                    val request = manager.requestReviewFlow()
                    request.addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            // We got the ReviewInfo object
                            val reviewInfo = task.result
                            val flow = manager.launchReviewFlow(this, reviewInfo)
                            flow.addOnCompleteListener { _ ->
                                // The flow has finished. The API does not indicate whether the user
                                // reviewed or not, or even whether the review dialog was shown. Thus, no
                                // matter the result, we continue our app flow.
                            }
                        } else {
                            // There was some problem, log or handle the error code.
                            Log.e(TAG, "triggerSurvey: ${task.exception}")
                        }
                    }
                    hasNextQuestion = false
                    finish()
                } else if ((surveyResponse.document?.fields?.nextQuestion?.get(
                        "mapValue"
                    )?.get("fields")?.get(checkForNextQuestion)?.get("stringValue")
                        ?: "").lowercase() == "babble_whatsapp_referral"
                ) {
                    val indexOfSkipLogic =
                        (surveyResponse.document?.fields?.skipLogicData?.arrayValue?.values
                            ?: arrayListOf()).indexOfFirst {
                            (it.mapValue?.fields?.respVal?.stringValue
                                ?: "") == checkForNextQuestion
                        }
                    var referralText = (surveyResponse.document?.fields?.nextQuestion?.get(
                        "mapValue"
                    )?.get("fields")?.get(checkForNextQuestion)?.get("referral_text") ?: "")
                    if (indexOfSkipLogic != -1) {
                        referralText =
                            (surveyResponse.document?.fields?.skipLogicData?.arrayValue?.values
                                ?: arrayListOf())[indexOfSkipLogic].mapValue?.fields?.referralText?.stringValue
                                ?: ""
                    }

                    val sendIntent = Intent()
                    sendIntent.action = Intent.ACTION_SEND
                    sendIntent.putExtra(Intent.EXTRA_TEXT, referralText)
                    sendIntent.type = "text/plain"
                    sendIntent.setPackage("com.whatsapp")
                    startActivity(sendIntent)
                    hasNextQuestion = false
                    finish()
                } else {

                    if ((surveyResponse.document?.fields?.nextQuestion?.get(
                            "mapValue"
                        )?.get("fields")?.get(checkForNextQuestion)?.get("stringValue")
                            ?: "").lowercase() == "end" || (surveyResponse.document?.fields?.nextQuestion?.get(
                            "mapValue"
                        )?.get("fields")?.get("any")?.get("stringValue") ?: "").lowercase() == "end"
                    ) {
                        hasNextQuestion = false
                        finish()
                    } else {
                        val index =
                            questionList!!.subList(questionId, (questionList ?: arrayListOf()).size)
                                .indexOfFirst {
                                    if ((surveyResponse.document?.fields?.nextQuestion?.get(
                                            "mapValue"
                                        )?.get("fields")?.get(checkForNextQuestion)
                                            ?.get("stringValue") ?: "") != ""
                                    ) {
                                        BabbleSdkHelper.getIdFromStringPath(it.document?.name) == surveyResponse.document!!.fields!!.nextQuestion!!["mapValue"]!!["fields"]!![checkForNextQuestion]!!["stringValue"]
                                    } else if (surveyResponse.document?.fields?.nextQuestion?.get(
                                            "mapValue"
                                        )?.get("fields")?.get("any") != null
                                    ) {
                                        BabbleSdkHelper.getIdFromStringPath(it.document?.name) == surveyResponse.document?.fields?.nextQuestion?.get(
                                            "mapValue"
                                        )?.get("fields")?.get("any")?.get("stringValue")
                                    } else {
                                        false
                                    }
                                }
                        if (index != -1) {
                            questionId += index
                        } else {
                            questionId++
                        }
                    }
                    setUpUI()
                }

            } else if ((questionList?.size ?: 0) - 1 > questionId) {
                questionId++
                setUpUI()
            }
        }
        writeSurveyResponse(
            responseAnswer, surveyResponse, hasNextQuestion
        )
    }

    private fun writeSurveyResponse(
        responseAnswer: String?, surveyResponse: UserQuestionResponse, hasNextQuestion: Boolean
    ) {
        val questionTypeId = surveyResponse.document?.fields?.questionTypeId?.integerValue ?: "9"
        if (questionTypeId != "6" && questionTypeId != "9") {
            val tempQuestionList =
                questionList?.filter { it.document?.fields?.questionTypeId?.integerValue != "6" && it.document?.fields?.questionTypeId?.integerValue != "9" }
            val surveyId = surveyResponse.document?.fields?.surveyId?.stringValue
            val date: String = BabbleSdkHelper.getCurrentDate()
            val requestData = AddResponseRequest(
                surveyId = surveyId,
                questionTypeId = Integer.parseInt(questionTypeId),
                sequenceNo = Integer.parseInt(
                    (surveyResponse.document?.fields?.sequenceNo?.integerValue ?: "-1").toString()
                ),
                surveyInstanceId = BabbleSDKController.getInstance(this)?.surveyInstanceId,
                questionText = surveyResponse.document?.fields?.questionText?.stringValue ?: "",
                responseCreatedAt = date,
                responseUpdatedAt = date,
                shouldMarkComplete = tempQuestionList?.last()?.document?.name == surveyResponse.document?.name,
                shouldMarkPartial = tempQuestionList?.last()?.document?.name != surveyResponse.document?.name,
                response = responseAnswer ?: "",
                nextQuestionTracker = ((questionList!![questionId].document?.fields?.questionTypeId?.integerValue
                    ?: "") != "9") && hasNextQuestion
            )
            val babbleApi: BabbleApiInterface = ApiClient.getInstance().create(
                BabbleApiInterface::class.java
            )
            babbleApi.addResponse(BabbleSDKController.getInstance(this)?.userId, requestData)
                .enqueue(object : Callback<ResponseBody> {
                    override fun onResponse(
                        call: Call<ResponseBody>,
                        response: Response<ResponseBody>,
                    ) {
                        Log.e(TAG, "Survey response saved.")
                    }

                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                        Log.e(TAG, "onFailure: $t")
                    }
                })
        }
    }

    override fun attachBaseContext(newBase: Context?) {
        val newOverride = Configuration(newBase?.resources?.configuration)
        newOverride.fontScale = 1.0f
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            applyOverrideConfiguration(newOverride)
        }
        super.attachBaseContext(newBase)
    }
}