package com.babble.babblesdk.model.triggerForUser

import com.google.gson.annotations.SerializedName
import java.io.Serializable


data class UserTriggerResponse (

    @SerializedName("document" ) var document : UserTriggerDocument? = UserTriggerDocument(),
    @SerializedName("readTime" ) var readTime : String?   = null

): Serializable

data class UserTriggerDocument (

    @SerializedName("name"       ) var name       : String? = null,
    @SerializedName("fields"     ) var fields     : UserTriggerFields? = UserTriggerFields(),
    @SerializedName("createTime" ) var createTime : String? = null,
    @SerializedName("updateTime" ) var updateTime : String? = null

): Serializable

data class UserTriggerFields (

    @SerializedName("last_updated_at" ) var lastUpdatedAt : UserTriggerString? = UserTriggerString(),
    @SerializedName("status"          ) var status        : UserTriggerString?        = UserTriggerString(),
    @SerializedName("platform"        ) var platform      : Platform?      = Platform(),
    @SerializedName("user_id"         ) var userId        : UserTriggerString?        = UserTriggerString(),
    @SerializedName("description"     ) var description   : UserTriggerString?   = UserTriggerString(),
    @SerializedName("created_at"      ) var createdAt     : UserTriggerString?     = UserTriggerString(),
    @SerializedName("name"            ) var name          : UserTriggerString?          = UserTriggerString()

)

data class Platform (

    @SerializedName("arrayValue" ) var arrayValue : ArrayValue? = ArrayValue()

): Serializable

data class ArrayValue (

    @SerializedName("values" ) var values : ArrayList<UserTriggerString> = arrayListOf()

): Serializable

data class UserTriggerString (

    @SerializedName("stringValue" ) var stringValue : String? = null

): Serializable