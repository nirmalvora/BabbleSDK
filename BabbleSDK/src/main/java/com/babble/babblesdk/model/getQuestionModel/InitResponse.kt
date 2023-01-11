package com.babble.babblesdk.model.getQuestionModel

import com.google.gson.annotations.SerializedName
import java.io.Serializable


data class QuestionResponse (

  @SerializedName("document" ) var document : Document? = Document(),
  @SerializedName("readTime" ) var readTime : String?   = null,
  @SerializedName("survey_id" ) var surveyId : String? = null,
  @SerializedName("survey_instance_id" ) var surveyInstanceId : String? = null,

) : Serializable