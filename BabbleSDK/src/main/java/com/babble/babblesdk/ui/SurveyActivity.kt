package com.babble.babblesdk.ui

import android.animation.ObjectAnimator
import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.Window
import android.view.WindowManager
import android.view.animation.DecelerateInterpolator
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.babble.babblesdk.R
import com.babble.babblesdk.TAG
import com.babble.babblesdk.databinding.ActivitySurveyBinding
import com.babble.babblesdk.model.AddResponseRequest
import com.babble.babblesdk.model.getQuestionModel.Questions
import com.babble.babblesdk.model.getSurveyResponse.SurveyResponse
import com.babble.babblesdk.repository.ApiClient
import com.babble.babblesdk.repository.BabbleApiInterface
import com.babble.babblesdk.ui.fragments.BabbleQueFragment
import com.babble.babblesdk.ui.fragments.BabbleQueTextFragment
import com.babble.babblesdk.ui.fragments.BabbleWelcomeFragment
import com.babble.babblesdk.utils.BabbleConstants
import com.google.gson.Gson
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import java.text.SimpleDateFormat
import java.util.*


class SurveyActivity : AppCompatActivity() {
    private var surveyData: SurveyResponse? = null
    private lateinit var binding: ActivitySurveyBinding
    private var questionId: Int = 0
    private var questionList: List<Questions>? = null
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
        surveyData = Gson().fromJson(surveyDetail, SurveyResponse::class.java)
        questionList = surveyData?.questions?.sortedBy {
            it.sequenceNo
        }
        binding.pageProgressBar.max = (questionList?.size ?: 0) * 100
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
        window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND) // This flag is required to set otherwise the setDimAmount method will not show any effect
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
            binding.pageProgressBar,
            "progress",
            questionId * 100,
            (questionId + 1) * 100
        )
        animation.duration = 500
        animation.setAutoCancel(true)
        animation.interpolator = DecelerateInterpolator()
        animation.start()
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.nothing, R.anim.slide_down_new_theme)
    }

    private fun getFragment(): Fragment? {
        var frag: Fragment? = null
        try {
            frag = when (questionList!![questionId].questionTypeId
                ?: "9") {
                6, 9 -> {
                    BabbleWelcomeFragment.newInstance(questionList!![questionId])
                }
                1, 2, 4, 5, 7, 8 -> {
                    BabbleQueFragment.newInstance(questionList!![questionId])
                }
                3 -> {
                    BabbleQueTextFragment.newInstance(questionList!![questionId])
                }
                else -> {
                    BabbleWelcomeFragment.newInstance(questionList!![questionId])
                }
            }
        } catch (_: Exception) {

        }
        return frag
    }

    fun addUserResponse(surveyResponse: Questions?) {
        if ((questionList?.size ?: 0) - 1 > questionId) {
            questionId++
            setUpUI()
        }
        var responseAnswer = ""
        when (surveyResponse?.questionTypeId ?: 9) {
            1, 2 -> {
                responseAnswer = surveyResponse?.selectedOptions?.joinToString { it -> it }.toString()
            }
            3 -> {
                responseAnswer = surveyResponse?.answerText ?: ""
            }
            4, 5, 7, 8 -> {
                responseAnswer = (surveyResponse?.selectedRating ?: 0).toString()
            }
        }

        val surveyId = surveyResponse?.surveyId
        val surveyInstanceId = surveyResponse?.surveyId
        val date: String = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssz", Locale.getDefault()).format(
            Date()
        )
        val requestData = AddResponseRequest(
            surveyId = surveyId,
            questionTypeId = (surveyResponse?.questionTypeId ?: 9).toString(),
            sequenceNo = (surveyResponse?.sequenceNo ?:0).toString(),
            surveyInstanceId = surveyInstanceId,
            questionText = surveyResponse?.questionText ?: "",
            responseCreatedAt = date,
            responseUpdatedAt = date,
            shouldMarkComplete = false,
            shouldMarkPartial = false,
            response = responseAnswer
        )
        val connectAPI: BabbleApiInterface = ApiClient.getInstance().create(
            BabbleApiInterface::class.java
        )
        connectAPI.addResponse(requestData).enqueue(object : Callback<ResponseBody> {
            override fun onResponse(
                call: Call<ResponseBody>,
                response: retrofit2.Response<ResponseBody>
            ) {
                Log.e(TAG, "onFailure: $response")
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.e(TAG, "onFailure: $t")
            }
        })
    }
    override fun attachBaseContext(newBase: Context?) {

        val newOverride = Configuration(newBase?.resources?.configuration)
        newOverride.fontScale = 1.0f
        applyOverrideConfiguration(newOverride)

        super.attachBaseContext(newBase)
    }
}