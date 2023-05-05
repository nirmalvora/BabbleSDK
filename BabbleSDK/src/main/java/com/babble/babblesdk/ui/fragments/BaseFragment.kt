package com.babble.babblesdk.ui.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.babble.babblesdk.model.questionsForUser.UserQuestionResponse
import com.babble.babblesdk.ui.SurveyActivity
import com.babble.babblesdk.utils.BabbleConstants

internal open class BaseFragment : Fragment() {
    var surveyActivity: SurveyActivity? = null
    lateinit var questionData: UserQuestionResponse
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
        questionData = requireArguments().getSerializable(BabbleConstants.questionData) as UserQuestionResponse
    }
}