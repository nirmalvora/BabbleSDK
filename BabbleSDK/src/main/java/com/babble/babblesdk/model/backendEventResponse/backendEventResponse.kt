package com.babble.babblesdk.model.backendEventResponse

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class BackedEventResponse(

    @SerializedName("document") var document: BackedEventDocument? = BackedEventDocument(),
    @SerializedName("readTime") var readTime: String? = null

) : Serializable

data class BackedEventDocument(

    @SerializedName("name") var name: String? = null,
    @SerializedName("fields") var fields: BackedEventFields? = BackedEventFields(),
    @SerializedName("createTime") var createTime: String? = null,
    @SerializedName("updateTime") var updateTime: String? = null

) : Serializable

data class BackedEventFields(

    @SerializedName("created_at") var createdAt: BackedEventString? = BackedEventString(),
    @SerializedName("event_type") var eventType: BackedEventString? = BackedEventString(),
    @SerializedName("user_id") var userId: BackedEventString? = BackedEventString(),
    @SerializedName("survey_instance_id") var surveyInstanceId: BackedEventString? = BackedEventString(),
    @SerializedName("customer_id") var customerId: BackedEventString? = BackedEventString(),
    @SerializedName("event_name") var eventName: BackedEventString? = BackedEventString()

) : Serializable

data class BackedEventString(

    @SerializedName("stringValue") var stringValue: String? = null

) : Serializable
