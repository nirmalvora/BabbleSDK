package com.babble.babblesdk.model.surveyForUsers


import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class UserSurveyResponse(

    @SerializedName("document") var document: UserSurveyDocument? = UserSurveyDocument(),
    @SerializedName("readTime") var readTime: String? = null

) : Serializable

data class UserSurveyDocument(

    @SerializedName("name") var name: String? = null,
    @SerializedName("fields") var fields: UserSurveyFields? = UserSurveyFields(),
    @SerializedName("createTime") var createTime: String? = null,
    @SerializedName("updateTime") var updateTime: String? = null

) : Serializable

data class UserSurveyFields(

    @SerializedName("start_date") var startDate: UserSurveyString? = UserSurveyString(),
    @SerializedName("created_at") var createdAt: UserSurveyString? = UserSurveyString(),
    @SerializedName("trigger_id") var triggerId: UserSurveyString? = UserSurveyString(),
    @SerializedName("user_id") var userId: UserSurveyString? = UserSurveyString(),
    @SerializedName("title") var title: UserSurveyString? = UserSurveyString(),
    @SerializedName("status") var status: UserSurveyString? = UserSurveyString(),
    @SerializedName("max_responses") var maxResponses: UserSurveyString? = UserSurveyString(),
    @SerializedName("updated_at") var updatedAt: UserSurveyString? = UserSurveyString(),
    @SerializedName("end_date") var endDate: UserSurveyString? = UserSurveyString(),
    @SerializedName("event_name") var eventName: UserSurveyString? = UserSurveyString(),
    @SerializedName("relevance_period") var relevancePeriod: UserSurveyInteger? = UserSurveyInteger(),
    @SerializedName("cohort_id") var cohortId: UserSurveyString? = UserSurveyString()

) : Serializable

data class UserSurveyString(

    @SerializedName("stringValue") var stringValue: String? = null

) : Serializable

data class UserSurveyInteger(

    @SerializedName("integerValue") var integerValue: String? = null

) : Serializable
