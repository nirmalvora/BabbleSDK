package com.babble.babblesdk.model.getSurveyResponse

import com.babble.babblesdk.model.getQuestionModel.Questions
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class SurveyResponse (

    @SerializedName("user_id"       ) var userId       : String?              = null,
    @SerializedName("status"        ) var status       : String?              = null,
    @SerializedName("trigger_id"    ) var triggerId    : String?              = null,
    @SerializedName("updated_at"    ) var updatedAt    : String?              = null,
    @SerializedName("max_responses" ) var maxResponses : String?              = null,
    @SerializedName("title"         ) var title        : String?              = null,
    @SerializedName("created_at"    ) var createdAt    : String?              = null,
    @SerializedName("start_date"    ) var startDate    : String?              = null,
    @SerializedName("end_date"      ) var endDate      : String?              = null,
    @SerializedName("id"            ) var id           : String?              = null,
    @SerializedName("questions"     ) var questions    : ArrayList<Questions> = arrayListOf()

) : Serializable