package com.babble.babblesdk.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class SurveyCloseRequest (
    @SerializedName("survey_instance_id") var surveyInstanceId: String? = null,
): Serializable