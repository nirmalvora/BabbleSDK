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
import com.babble.babblesdk.model.getQuestionModel.Fields
import com.babble.babblesdk.model.getQuestionModel.QuestionResponse
import com.babble.babblesdk.utils.BabbleGenericClickHandler

internal class BabbleQueFragment : BaseFragment(), BabbleGenericClickHandler {
    private lateinit var binding: FragmentBabbleQueBinding
    private var fields: Fields? = null
    private var dashboardAdapter: BabbleSurveyAdapter? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBabbleQueBinding.inflate(inflater, container, false)
        fields = questionData.document?.fields
        val questionText = fields?.questionText?.stringValue ?: ""
        val ratingsFullLike = fields?.maxValDescription?.stringValue ?: ""
        val ratingsNotLike = fields?.minValDescription?.stringValue ?: ""
        val questionDesc = fields?.questionDesc?.stringValue ?: ""
        val buttonText = fields?.ctaText?.stringValue ?: ""
        binding.btnLayout.nextButton.setOnClickListener {
            surveyActivity!!.addUserResponse(questionData.document?.fields)
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
            when (fields?.questionTypeId?.integerValue ?: "9") {
                "1", "2" -> {
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
            fields!!, this
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
        fun newInstance(param1: QuestionResponse) =
            BabbleQueFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(QUESTION_DATA, param1)
                }
            }
    }

    override fun itemClicked(position: Int) {
        when (fields?.questionTypeId?.integerValue ?: "9") {
            "1" -> {
                fields?.answers?.arrayValue?.values?.get(position)?.selected =
                    !(fields?.answers?.arrayValue?.values?.get(position)?.selected ?: false)
            }
            "2" -> {
                fields?.answers?.arrayValue?.values?.forEach {
                    it.selected = false
                }
                fields?.answers?.arrayValue?.values?.get(position)?.selected =
                    !(fields?.answers?.arrayValue?.values?.get(position)?.selected ?: false)
            }
            "4", "5", "7", "8" -> {
                if (fields?.selectedRating == (position + 1)) {
                    fields?.selectedRating = null
                } else {
                    fields?.selectedRating = (position + 1)
                }
            }
        }
        dashboardAdapter!!.notifyMyList(fields!!)
        checkIfNextButtonEnabled()
    }

    private fun checkIfNextButtonEnabled() {
        when (fields?.questionTypeId?.integerValue ?: "9") {
            "1" -> {
                val isEnabled = fields?.answers?.arrayValue?.values?.any {
                    it.selected
                }
                binding.btnLayout.nextButton.isEnabled = isEnabled ?: false
                binding.btnLayout.nextButton.isClickable = isEnabled ?: false
            }
            "2" -> {
                val isEnabled = fields?.answers?.arrayValue?.values?.any {
                    it.selected
                }
                binding.btnLayout.nextButton.isEnabled = isEnabled ?: false
                binding.btnLayout.nextButton.isClickable = isEnabled ?: false
            }
            "4", "5", "7", "8" -> {
                binding.btnLayout.nextButton.isEnabled = fields?.selectedRating != null
                binding.btnLayout.nextButton.isClickable = fields?.selectedRating != null
            }
        }

    }
}