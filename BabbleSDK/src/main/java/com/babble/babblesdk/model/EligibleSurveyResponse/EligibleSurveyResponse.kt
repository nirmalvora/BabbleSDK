package com.babble.babblesdk.model.EligibleSurveyResponse

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class EligibleSurveyResponse (

    @SerializedName("eligible_survey_ids" ) var eligibleSurveyIds : ArrayList<String> = arrayListOf()

): Serializable