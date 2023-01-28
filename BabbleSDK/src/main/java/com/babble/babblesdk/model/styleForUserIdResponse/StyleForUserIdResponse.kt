package com.babble.babblesdk.model.styleForUserIdResponse

import com.google.gson.annotations.SerializedName
import java.io.Serializable


data class StyleForUserIdResponse(

    @SerializedName("document") var document: StyleForUserIdDocument? = StyleForUserIdDocument(),
    @SerializedName("readTime") var readTime: String? = null,

    ) : Serializable

data class StyleForUserIdDocument(

    @SerializedName("name") var name: String? = null,
    @SerializedName("fields") var fields: StyleForUserIdFields? = StyleForUserIdFields(),
    @SerializedName("createTime") var createTime: String? = null,
    @SerializedName("updateTime") var updateTime: String? = null,

    ) : Serializable

data class StyleForUserIdFields(

    @SerializedName("main_color") var mainColor: MainColor? = MainColor(),
    @SerializedName("user_id") var userId: UserId? = UserId(),

    ) : Serializable


data class UserId(

    @SerializedName("stringValue") var stringValue: String? = null,

    ) : Serializable


data class MainColor(
    @SerializedName("stringValue") var stringValue: String? = null,
) : Serializable