package com.babble.babblesdk.ui.fragments

import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.babble.babblesdk.model.getQuestionModel.Questions
import com.babble.babblesdk.ui.SurveyActivity
import com.babble.babblesdk.utils.BabbleConstants

internal open class BaseFragment : Fragment() {
    var surveyActivity: SurveyActivity? = null
    lateinit var questionData: Questions
    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            surveyActivity = context as SurveyActivity
        } catch (ex: Exception) {
            surveyActivity = null
            ex.printStackTrace()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        questionData = if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
            requireArguments().getSerializable(BabbleConstants.questionData, Questions::class.java)!!
        else
            requireArguments().getSerializable(BabbleConstants.questionData) as Questions

    }
}