package com.babble.babblesdk.model.getQuestionModel

import com.google.gson.annotations.SerializedName
import java.io.Serializable


data class CtaText (

  @SerializedName("stringValue" ) var stringValue : String? = null

): Serializable