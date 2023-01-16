package com.babble.babblesdk.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.babble.babblesdk.databinding.FragmentBabbleWelcomeBinding
import com.babble.babblesdk.model.getQuestionModel.Questions
import com.babble.babblesdk.utils.BabbleConstants


internal class BabbleWelcomeFragment : BaseFragment() {
    private lateinit var binding: FragmentBabbleWelcomeBinding
      override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
          binding= FragmentBabbleWelcomeBinding.inflate(inflater, container,false)
          return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val questionText=questionData.questionText?:""
        val questionDesc=questionData.questionDesc?:""
        val buttonText=questionData.ctaText?:""
        binding.btnLayout.nextButton.setOnClickListener {
            surveyActivity!!.addUserResponse(questionData)
        }
        binding.surveyTitle.text= questionText
        binding.surveySubTitle.text= questionDesc
        binding.btnLayout.nextButton.text= buttonText
        binding.surveyTitle.visibility=getVisibility(questionText)
        binding.surveySubTitle.visibility=getVisibility(questionDesc)
        binding.btnLayout.nextButton.visibility=getVisibility(buttonText)
    }
    private fun getVisibility(text:String): Int {
        return if(text.isEmpty()) View.GONE else View.VISIBLE
    }
    companion object {
        @JvmStatic
        fun newInstance(param1: Questions) =
            BabbleWelcomeFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(BabbleConstants.questionData, param1)
                }
            }
    }
}