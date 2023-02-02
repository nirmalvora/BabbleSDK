package com.babble.babblesdk.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.babble.babblesdk.adapter.BabbleSurveyAdapter
import com.babble.babblesdk.databinding.FragmentBabbleQueBinding
import com.babble.babblesdk.model.questionsForUser.UserQuestionResponse
import com.babble.babblesdk.utils.BabbleConstants
import com.babble.babblesdk.utils.BabbleGenericClickHandler
import com.babble.babblesdk.utils.BabbleSdkHelper


internal class BabbleQueFragment : BaseFragment(), BabbleGenericClickHandler {
    private lateinit var binding: FragmentBabbleQueBinding
    private var dashboardAdapter: BabbleSurveyAdapter? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentBabbleQueBinding.inflate(inflater, container, false)
        val field = questionData.document?.fields
        val questionText = field?.questionText?.stringValue ?: ""
        val ratingsFullLike = field?.maxValDescription?.stringValue ?: ""
        val ratingsNotLike = field?.minValDescription?.stringValue ?: ""
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
        binding.optionLayout.visibility = View.VISIBLE
        binding.ratingsFullLike.text = ratingsFullLike
        binding.ratingsNotLike.text = ratingsNotLike
        binding.ratingsFullLike.visibility = getVisibility(ratingsFullLike)
        binding.ratingsNotLike.visibility = getVisibility(ratingsNotLike)
        binding.btnLayout.nextButton.visibility = View.GONE
        val mLayoutManager: RecyclerView.LayoutManager =
            when (questionData.document?.fields?.questionTypeId?.integerValue ?: "9") {
                "1" -> {
                    binding.btnLayout.nextButton.visibility = View.VISIBLE
                    LinearLayoutManager(activity)
                }
                "2" -> {
                    LinearLayoutManager(activity)
                }
                "4" -> {
                    GridLayoutManager(activity, 10)
                }
                "5", "7", "8" -> {
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

        BabbleSdkHelper.submitButtonBeautification(requireActivity(),binding.btnLayout.nextButton)
        return binding.root
    }

    private fun getVisibility(text: String): Int {
        return if (text.isEmpty()) View.GONE else View.VISIBLE
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: UserQuestionResponse) =
            BabbleQueFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(BabbleConstants.questionData, param1)
                }
            }
    }

    override fun itemClicked(position: Int) {
        when (questionData.document?.fields?.questionTypeId?.integerValue ?: "9") {
            "1" -> {
                if (questionData.selectedOptions.contains(
                        questionData.document?.fields?.answers?.arrayValue?.values?.get(position)?.stringValue
                            ?: ""
                    )
                ) {
                    questionData.selectedOptions.remove(
                        questionData.document?.fields?.answers?.arrayValue?.values?.get(position)?.stringValue
                            ?: ""
                    )
                } else {
                    questionData.selectedOptions.add(
                        questionData.document?.fields?.answers?.arrayValue?.values?.get(position)?.stringValue
                            ?: ""
                    )

                }
            }
            "2" -> {
                questionData.selectedOptions = arrayListOf()
                questionData.selectedOptions.add(
                    questionData.document?.fields?.answers?.arrayValue?.values?.get(position)?.stringValue
                        ?: ""
                )
                surveyActivity!!.addUserResponse(questionData)
            }
            "4", "5", "7", "8" -> {
                if (questionData.selectedRating == (position + 1)) {
                    questionData.selectedRating = null
                } else {
                    questionData.selectedRating = (position + 1)
                }
                surveyActivity!!.addUserResponse(questionData)
            }
        }
        dashboardAdapter!!.notifyMyList(questionData)
//        checkIfNextButtonEnabled()
    }

    private fun checkIfNextButtonEnabled() {
        when (questionData.document?.fields?.questionTypeId?.integerValue ?: "9") {
            "1", "2" -> {
                val isEnabled = questionData.selectedOptions.size != 0
                binding.btnLayout.nextButton.isEnabled = isEnabled
                binding.btnLayout.nextButton.isClickable = isEnabled
            }
            "4", "5", "7", "8" -> {
                binding.btnLayout.nextButton.isEnabled = questionData.selectedRating != null
                binding.btnLayout.nextButton.isClickable = questionData.selectedRating != null
            }
        }

    }
}