package com.babble.babblesdk.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable


data class InitResponse (

  @SerializedName("babble_user_id" ) var babbleUserId : String? = null

): Serializable