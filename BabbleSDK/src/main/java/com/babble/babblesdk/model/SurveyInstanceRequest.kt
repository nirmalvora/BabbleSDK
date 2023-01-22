package com.babble.babblesdk.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable


data class SurveyInstanceRequest (

    @SerializedName("survey_id"          ) var surveyId         : String? = null,
    @SerializedName("user_id"            ) var userId           : String? = null,
    @SerializedName("time_val"           ) var timeVal          : String? = null,
    @SerializedName("customer_id"        ) var customerId       : String? = null,
    @SerializedName("survey_instance_id" ) var surveyInstanceId : String? = null,
    @SerializedName("backend_event_ids" ) var backendEventIds : List<String> = arrayListOf()

): Serializable