package com.babble.babblesdk.model.triggerModel

import com.google.gson.annotations.SerializedName


data class TriggerModel (
    @SerializedName("description"     ) var description   : String?           = null,
    @SerializedName("last_updated_at" ) var lastUpdatedAt : String?           = null,
    @SerializedName("platform"        ) var platform      : ArrayList<String> = arrayListOf(),
    @SerializedName("name"            ) var name          : String?           = null,
    @SerializedName("user_id"         ) var userId        : String?           = null,
    @SerializedName("created_at"      ) var createdAt     : String?           = null,
    @SerializedName("status"          ) var status        : String?           = null,
    @SerializedName("id"              ) var id            : String?           = null
)