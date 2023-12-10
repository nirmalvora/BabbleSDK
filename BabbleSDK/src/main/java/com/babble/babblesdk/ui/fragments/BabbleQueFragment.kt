package com.babble.babblesdk.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.babble.babblesdk.adapter.BabbleSurveyAdapter
import com.babble.babblesdk.databinding.FragmentBabbleQueBinding
import com.babble.babblesdk.model.questionsForUser.UserQuestionResponse
import com.babble.babblesdk.utils.BabbleConstants
import com.babble.babblesdk.utils.BabbleGenericClickHandler
import com.babble.babblesdk.utils.BabbleStyleHelper
import java.util.Timer
import kotlin.concurrent.timerTask


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
        BabbleStyleHelper.setTextStyle(binding.surveyTitle)
        BabbleStyleHelper.setTextStyle(binding.surveySubTitle)
        binding.btnLayout.nextButton.visibility = getVisibility(buttonText)
        binding.optionLayout.visibility = View.VISIBLE
        binding.ratingsFullLike.text = ratingsFullLike
        binding.ratingsNotLike.text = ratingsNotLike
        BabbleStyleHelper.setLightTextColor(
            binding.ratingsFullLike
        )
        BabbleStyleHelper.setLightTextColor(binding.ratingsNotLike)

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

        val marginToSet = if ((questionData.document?.fields?.questionTypeId?.integerValue
                ?: "9") == "7" || (questionData.document?.fields?.questionTypeId?.integerValue
                ?: "9") == "8"
        ) {
            20
        } else {
            0
        }
        val layoutRatingsNotLike =
            binding.ratingsNotLike.layoutParams as RelativeLayout.LayoutParams
        layoutRatingsNotLike.setMargins(marginToSet, 0, 0, 0)
        binding.ratingsNotLike.layoutParams = layoutRatingsNotLike

        val layoutParamsRatingsFullLike =
            binding.ratingsFullLike.layoutParams as RelativeLayout.LayoutParams
        layoutParamsRatingsFullLike.setMargins(0, 0, marginToSet, 0)
        binding.ratingsFullLike.layoutParams = layoutParamsRatingsFullLike
        dashboardAdapter = BabbleSurveyAdapter(
            requireActivity(), questionData, this
        )
        binding.surveyOptionsList.layoutManager = mLayoutManager
        binding.surveyOptionsList.itemAnimator = DefaultItemAnimator()
        binding.surveyOptionsList.adapter = dashboardAdapter

        BabbleStyleHelper.submitButtonBeautification(binding.btnLayout.nextButton)
        return binding.root
    }

    private fun getVisibility(text: String): Int {
        return if (text.isEmpty()) View.GONE else View.VISIBLE
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: UserQuestionResponse) = BabbleQueFragment().apply {
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

                if ((questionData.document?.fields?.correctAnswer?.stringValue
                        ?: "").isNotEmpty()
                ) {
                    val timer = Timer()
                    timer.schedule(
                        timerTask {
                            requireActivity().runOnUiThread {
                                surveyActivity!!.addUserResponse(
                                    questionData
                                )
                            }
                        }, 500
                    )
                } else {
                    surveyActivity!!.addUserResponse(questionData)
                }
            }

            "4", "5", "7", "8" -> {
                if (questionData.selectedRating == (position + 1)) {
                    questionData.selectedRating = null
                } else {
                    questionData.selectedRating = (position + 1)
                }

                val delay: Long = if ((questionData.document?.fields?.questionTypeId?.integerValue
                        ?: "9") == "7" || (questionData.document?.fields?.questionTypeId?.integerValue
                        ?: "9") == "8"
                ) {
                    500
                } else {
                    0
                }
                val timer = Timer()
                timer.schedule(
                    timerTask {
                        requireActivity().runOnUiThread {
                            surveyActivity!!.addUserResponse(questionData)
                        }
                    }, delay
                )

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