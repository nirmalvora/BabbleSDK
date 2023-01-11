package com.babble.babblesdk.model.getQuestionModel

import com.google.gson.annotations.SerializedName
import java.io.Serializable


data class Answers (

    @SerializedName("arrayValue" ) var arrayValue : ArrayValue? = ArrayValue()

): Serializable