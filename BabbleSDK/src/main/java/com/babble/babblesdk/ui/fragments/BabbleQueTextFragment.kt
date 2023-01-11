package com.babble.babblesdk.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import com.babble.babblesdk.databinding.FragmentBabbleQueTextBinding
import com.babble.babblesdk.model.getQuestionModel.Fields
import com.babble.babblesdk.model.getQuestionModel.QuestionResponse

internal class BabbleQueTextFragment : BaseFragment() {
    private lateinit var binding: FragmentBabbleQueTextBinding
    private var fields: Fields? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBabbleQueTextBinding.inflate(inflater, container, false)
        fields = questionData.document?.fields
        val questionText = fields?.questionText?.stringValue ?: ""
        val questionDesc = fields?.questionDesc?.stringValue ?: ""
        val buttonText = fields?.ctaText?.stringValue ?: ""
        binding.btnLayout.nextButton.setOnClickListener {
            questionData.document?.fields?.answersText = binding.childUserInput.text.toString()
            surveyActivity!!.addUserResponse(questionData.document?.fields)
        }
        binding.surveyTitle.text = questionText
        binding.surveySubTitle.text = questionDesc
        binding.btnLayout.nextButton.text = buttonText
        binding.surveyTitle.visibility = getVisibility(questionText)
        binding.surveySubTitle.visibility = getVisibility(questionDesc)
        binding.btnLayout.nextButton.visibility = getVisibility(buttonText)
        binding.btnLayout.nextButton.isEnabled = false
        binding.btnLayout.nextButton.isClickable = false
        binding.childUserInput.addTextChangedListener { text ->
            if ((text ?: "").isEmpty()) {
                binding.btnLayout.nextButton.isEnabled = false
                binding.btnLayout.nextButton.isClickable = false
            } else {
                binding.btnLayout.nextButton.isEnabled = true
                binding.btnLayout.nextButton.isClickable = true
            }
        }
        return binding.root
    }

    private fun getVisibility(text: String): Int {
        return if (text.isEmpty()) View.GONE else View.VISIBLE
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: QuestionResponse) =
            BabbleQueTextFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(QUESTION_DATA, param1)
                }
            }
    }
}