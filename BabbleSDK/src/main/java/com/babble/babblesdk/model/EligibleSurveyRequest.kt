package com.babble.babblesdk.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class EligibleSurveyRequest(
    @SerializedName("babble_user_id") var babbleUserId: String? = null,
    @SerializedName("customer_id") var customerId: String? = null,
) : Serializable