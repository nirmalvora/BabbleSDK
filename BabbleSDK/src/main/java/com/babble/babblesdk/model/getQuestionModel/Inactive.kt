package com.babble.babblesdk.model.getQuestionModel

import com.google.gson.annotations.SerializedName
import java.io.Serializable


data class Inactive (

  @SerializedName("booleanValue" ) var booleanValue : Boolean? = null

): Serializable