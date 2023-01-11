package com.babble.babblesdk.model.getQuestionModel

import com.google.gson.annotations.SerializedName
import java.io.Serializable


data class QuestionTypeId (

  @SerializedName("integerValue" ) var integerValue : String? = null

): Serializable