package com.babble.babblesdk.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import com.babble.babblesdk.databinding.FragmentBabbleWelcomeBinding
import com.babble.babblesdk.model.questionsForUser.UserQuestionResponse
import com.babble.babblesdk.utils.BabbleConstants
import com.babble.babblesdk.utils.BabbleSdkHelper
import java.util.*
import kotlin.concurrent.timerTask


internal class BabbleWelcomeFragment : BaseFragment() {
    private lateinit var binding: FragmentBabbleWelcomeBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentBabbleWelcomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val field = questionData.document?.fields
        val questionText = field?.questionText?.stringValue ?: ""
        val questionDesc = field?.questionDesc?.stringValue ?: ""
        val buttonText = field?.ctaText?.stringValue ?: ""
        binding.btnLayout.nextButton.setOnClickListener {
            surveyActivity!!.addUserResponse(questionData)
        }
        binding.surveyTitle.text = questionText
        binding.surveySubTitle.text = questionDesc
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

            val timer = Timer()
            timer.schedule(timerTask {
                requireActivity().finish()
            }, 1000)
        }
        BabbleSdkHelper.submitButtonBeautification(requireActivity(), binding.btnLayout.nextButton)
    }

    private fun getVisibility(text: String): Int {
        return if (text.isEmpty()) View.GONE else View.VISIBLE
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: UserQuestionResponse) =
            BabbleWelcomeFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(BabbleConstants.questionData, param1)
                }
            }
    }

}