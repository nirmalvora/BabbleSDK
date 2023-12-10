package com.babble.babblesdk.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import com.babble.babblesdk.databinding.FragmentBabbleWelcomeBinding
import com.babble.babblesdk.model.questionsForUser.UserQuestionResponse
import com.babble.babblesdk.model.surveyForUsers.UserSurveyResponse
import com.babble.babblesdk.utils.BabbleConstants
import com.babble.babblesdk.utils.BabbleStyleHelper
import java.util.Timer
import kotlin.concurrent.timerTask


internal class BabbleWelcomeFragment : BaseFragment() {
    private lateinit var binding: FragmentBabbleWelcomeBinding
    private lateinit var surveyData: UserSurveyResponse
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentBabbleWelcomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    private val timer = Timer()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        surveyData =
            requireArguments().getSerializable(BabbleConstants.survey) as UserSurveyResponse

        val field = questionData.document?.fields
        val questionText = field?.questionText?.stringValue ?: ""
        val questionDesc = field?.questionDesc?.stringValue ?: ""
        val buttonText = field?.ctaText?.stringValue ?: ""
        binding.btnLayout.nextButton.setOnClickListener {
            surveyActivity!!.addUserResponse(questionData)
        }
        binding.surveyTitle.text = questionText
        binding.surveySubTitle.text = questionDesc
        BabbleStyleHelper.setTextStyle(binding.surveyTitle)
        BabbleStyleHelper.setTextStyle(binding.surveySubTitle)
        binding.btnLayout.nextButton.text = buttonText
        binding.surveyTitle.visibility = getVisibility(questionText)
        binding.surveySubTitle.visibility = getVisibility(questionDesc)
        binding.btnLayout.nextButton.visibility = getVisibility(buttonText)
        if (questionData.document?.fields?.questionTypeId?.integerValue == "9") {
            val subTitleLayoutParams =
                binding.surveySubTitle.layoutParams as RelativeLayout.LayoutParams
            subTitleLayoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL)
            binding.surveySubTitle.layoutParams = subTitleLayoutParams
            val titleLayoutParams = binding.surveyTitle.layoutParams as RelativeLayout.LayoutParams
            titleLayoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL)
            binding.surveyTitle.layoutParams = titleLayoutParams
            var delayTime: Long = 1000
            if (surveyData.document?.fields?.isQuiz?.booleanValue == true) {
                delayTime = 3000
                val totalQuestion =
                    requireArguments().getSerializable(BabbleConstants.totalQuestion) as Int?
                val correctAnswers =
                    requireArguments().getSerializable(BabbleConstants.correctAnswers) as Int?
                val quizResultText = if (totalQuestion == 1) {
                    if (correctAnswers == 1) {
                        "Correct answer ✅"
                    } else {
                        "Incorrect answer ❌"
                    }
                } else {
                    buildString {
                        append("All Done 😀 ")
                        append(correctAnswers)
                        append(" of ")
                        append(totalQuestion!!)
                        append(" correct ✅")
                    }
                }
                binding.quizResult.text = quizResultText
                BabbleStyleHelper.setTextStyle(binding.quizResult)
                binding.quizResult.visibility = View.VISIBLE
            }

            timer.schedule(timerTask {
                if (surveyActivity != null) {
                    requireActivity().finish()
                }
            }, delayTime)
        }

        BabbleStyleHelper.submitButtonBeautification(binding.btnLayout.nextButton)
    }

    override fun onDetach() {
        timer.cancel()
        super.onDetach()
    }

    private fun getVisibility(text: String): Int {
        return if (text.isEmpty()) View.GONE else View.VISIBLE
    }

    companion object {
        @JvmStatic
        fun newInstance(
            param1: UserQuestionResponse,
            surveyData: UserSurveyResponse?,
            totalQuestion: Int?,
            totalCorrectAnswer: Int?
        ) = BabbleWelcomeFragment().apply {
            arguments = Bundle().apply {
                putSerializable(BabbleConstants.questionData, param1)
                putSerializable(BabbleConstants.survey, surveyData)
                putSerializable(BabbleConstants.totalQuestion, totalQuestion)
                putSerializable(BabbleConstants.correctAnswers, totalCorrectAnswer)
            }
        }

        @JvmStatic
        fun newInstance(param1: UserQuestionResponse, surveyData: UserSurveyResponse?) =
            BabbleWelcomeFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(BabbleConstants.questionData, param1)
                    putSerializable(BabbleConstants.survey, surveyData)
                }
            }
    }
}