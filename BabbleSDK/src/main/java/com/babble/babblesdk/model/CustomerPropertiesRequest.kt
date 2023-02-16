package com.babble.babblesdk.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class CustomerPropertiesRequest (

    @SerializedName("user_id"            ) var userId           : String? = null,
    @SerializedName("customer_id"        ) var customerId       : String? = null,
    @SerializedName("properties" ) var properties: HashMap<String, Any?>? = null

): Serializable