package com.babble.babblesdk.model.getQuestionModel

import com.google.gson.annotations.SerializedName
import java.io.Serializable


data class Document (

  @SerializedName("name"       ) var name       : String? = null,
  @SerializedName("fields"     ) var fields     : Fields? = Fields(),
  @SerializedName("createTime" ) var createTime : String? = null,
  @SerializedName("updateTime" ) var updateTime : String? = null

): Serializable