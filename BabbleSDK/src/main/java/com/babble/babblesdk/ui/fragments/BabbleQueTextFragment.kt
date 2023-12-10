package com.babble.babblesdk.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.babble.babblesdk.databinding.FragmentBabbleQueTextBinding
import com.babble.babblesdk.model.questionsForUser.UserQuestionResponse
import com.babble.babblesdk.utils.BabbleConstants
import com.babble.babblesdk.utils.BabbleStyleHelper

internal class BabbleQueTextFragment : BaseFragment() {
    private lateinit var binding: FragmentBabbleQueTextBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentBabbleQueTextBinding.inflate(inflater, container, false)
        val field = questionData.document?.fields
        val questionText = field?.questionText?.stringValue ?: ""
        val questionDesc = field?.questionDesc?.stringValue ?: ""
        val buttonText = field?.ctaText?.stringValue ?: ""
        binding.btnLayout.nextButton.setOnClickListener {
            questionData.answerText = binding.childUserInput.text.toString()
            surveyActivity!!.addUserResponse(questionData)
        }

        binding.surveyTitle.text = questionText
        binding.surveySubTitle.text = questionDesc
        binding.btnLayout.nextButton.text = buttonText
        binding.surveyTitle.visibility = getVisibility(questionText)
        binding.surveySubTitle.visibility = getVisibility(questionDesc)
        binding.btnLayout.nextButton.visibility = getVisibility(buttonText)
        BabbleStyleHelper.setEditTextColor(binding.childUserInput)
        BabbleStyleHelper.setTextStyle(binding.surveyTitle)
        BabbleStyleHelper.setTextStyle(binding.surveySubTitle)
        BabbleStyleHelper.submitButtonBeautification(binding.btnLayout.nextButton)
        return binding.root
    }

    private fun getVisibility(text: String): Int {
        return if (text.isEmpty()) View.GONE else View.VISIBLE
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: UserQuestionResponse) = BabbleQueTextFragment().apply {
            arguments = Bundle().apply {
                putSerializable(BabbleConstants.questionData, param1)
            }
        }
    }

}