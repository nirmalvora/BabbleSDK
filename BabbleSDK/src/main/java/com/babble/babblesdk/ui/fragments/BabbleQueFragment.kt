package com.babble.babblesdk.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.babble.babblesdk.adapter.BabbleSurveyAdapter
import com.babble.babblesdk.databinding.FragmentBabbleQueBinding
import com.babble.babblesdk.model.getQuestionModel.Questions
import com.babble.babblesdk.utils.BabbleConstants
import com.babble.babblesdk.utils.BabbleGenericClickHandler

internal class BabbleQueFragment : BaseFragment(), BabbleGenericClickHandler {
    private lateinit var binding: FragmentBabbleQueBinding
    private var dashboardAdapter: BabbleSurveyAdapter? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBabbleQueBinding.inflate(inflater, container, false)
        val questionText = questionData.questionText ?: ""
        val ratingsFullLike = questionData.maxValDescription ?: ""
        val ratingsNotLike = questionData.minValDescription ?: ""
        val questionDesc = questionData.questionDesc ?: ""
        val buttonText = questionData.ctaText ?: ""
        binding.btnLayout.nextButton.setOnClickListener {
            surveyActivity!!.addUserResponse(questionData)
        }
        binding.surveyTitle.text = questionText
        binding.surveySubTitle.text = questionDesc
        binding.btnLayout.nextButton.text = buttonText
        binding.surveyTitle.visibility = getVisibility(questionText)
        binding.surveySubTitle.visibility = getVisibility(questionDesc)
        binding.btnLayout.nextButton.visibility = getVisibility(buttonText)
        binding.optionLayout.visibility = View.VISIBLE
        binding.ratingsFullLike.text = ratingsFullLike
        binding.ratingsNotLike.text = ratingsNotLike
        binding.ratingsFullLike.visibility = getVisibility(ratingsFullLike)
        binding.ratingsNotLike.visibility = getVisibility(ratingsNotLike)

        val mLayoutManager: RecyclerView.LayoutManager =
            when (questionData.questionTypeId ?: 9) {
                1, 2 -> {
                    LinearLayoutManager(activity)
                }
                4 -> {
                    GridLayoutManager(activity, 10)
                }
                5, 7, 8 -> {
                    GridLayoutManager(activity, 5)
                }
                else -> {
                    LinearLayoutManager(activity)
                }
            }
        dashboardAdapter = BabbleSurveyAdapter(
            requireActivity(),
            questionData, this
        )
        binding.surveyOptionsList.layoutManager = mLayoutManager
        binding.surveyOptionsList.itemAnimator = DefaultItemAnimator()
        binding.surveyOptionsList.adapter = dashboardAdapter
        binding.btnLayout.nextButton.isEnabled = false
        binding.btnLayout.nextButton.isClickable = false
        return binding.root
    }

    private fun getVisibility(text: String): Int {
        return if (text.isEmpty()) View.GONE else View.VISIBLE
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: Questions) =
            BabbleQueFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(BabbleConstants.questionData, param1)
                }
            }
    }

    override fun itemClicked(position: Int) {
        when (questionData.questionTypeId ?: 9) {
            1 -> {
                if(questionData.selectedOptions.contains(questionData.answers[position])){
                    questionData.selectedOptions.remove(questionData.answers[position])
                }else{
                    questionData.selectedOptions.add(questionData.answers[position])

                }
            }
            2 -> {
                questionData.selectedOptions= arrayListOf()
                questionData.selectedOptions.add(questionData.answers[position])
            }
            4, 5, 7, 8 -> {
                if (questionData.selectedRating == (position + 1)) {
                    questionData.selectedRating = null
                } else {
                    questionData.selectedRating = (position + 1)
                }
            }
        }
        dashboardAdapter!!.notifyMyList(questionData)
        checkIfNextButtonEnabled()
    }

    private fun checkIfNextButtonEnabled() {
        when (questionData.questionTypeId ?: 9) {
            1, 2 -> {
                val isEnabled = questionData.selectedOptions.size!=0
                binding.btnLayout.nextButton.isEnabled = isEnabled
                binding.btnLayout.nextButton.isClickable = isEnabled
            }
            4, 5, 7, 8 -> {
                binding.btnLayout.nextButton.isEnabled = questionData.selectedRating != null
                binding.btnLayout.nextButton.isClickable = questionData.selectedRating != null
            }
        }

    }
}