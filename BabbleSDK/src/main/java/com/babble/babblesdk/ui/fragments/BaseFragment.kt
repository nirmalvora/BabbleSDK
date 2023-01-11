package com.babble.babblesdk.ui.fragments

import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.babble.babblesdk.model.getQuestionModel.QuestionResponse
import com.babble.babblesdk.ui.SurveyActivity

internal open class BaseFragment : Fragment() {
    var surveyActivity: SurveyActivity? = null
    lateinit var questionData: QuestionResponse
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
            requireArguments().getSerializable(QUESTION_DATA, QuestionResponse::class.java)!!
        else
            requireArguments().getSerializable(QUESTION_DATA) as QuestionResponse

    }
}