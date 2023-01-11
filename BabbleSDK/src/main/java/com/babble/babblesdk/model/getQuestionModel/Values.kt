package com.babble.babblesdk.model.getQuestionModel

import com.google.gson.annotations.SerializedName
import java.io.Serializable


data class AnswerValue (

    @SerializedName("stringValue" ) var stringValue : String? = null,
    @SerializedName("selected" ) var selected : Boolean = false

): Serializable