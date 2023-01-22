package com.babble.babblesdk.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable


data class AddResponseRequest(

    @SerializedName("survey_id") var surveyId: String? = null,
    @SerializedName("question_type_id") var questionTypeId: Int? = null,
    @SerializedName("sequence_no") var sequenceNo: Int? = null,
    @SerializedName("survey_instance_id") var surveyInstanceId: String? = null,
    @SerializedName("question_text") var questionText: String? = null,
    @SerializedName("response_created_at") var responseCreatedAt: String? = null,
    @SerializedName("response_updated_at") var responseUpdatedAt: String? = null,
    @SerializedName("should_mark_complete") var shouldMarkComplete: Boolean? = null,
    @SerializedName("should_mark_partial") var shouldMarkPartial: Boolean? = null,
    @SerializedName("response") var response: String? = null
) : Serializable