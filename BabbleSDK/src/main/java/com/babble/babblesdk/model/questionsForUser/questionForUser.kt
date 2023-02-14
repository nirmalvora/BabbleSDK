package com.babble.babblesdk.model.questionsForUser

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable


data class UserQuestionResponse(

    @SerializedName("document") var document: UserQuestionDocument? = UserQuestionDocument(),
    @SerializedName("readTime") var readTime: String? = null,
    @SerializedName("selected_options") var selectedOptions: ArrayList<String> = arrayListOf(),
    @SerializedName("selected_rating") var selectedRating: Int? = null,
    @SerializedName("answer_text") var answerText: String? = null,
) : Serializable

data class UserQuestionDocument(

    @SerializedName("name") var name: String? = null,
    @SerializedName("fields") var fields: UserQuestionFields? = UserQuestionFields(),
    @SerializedName("createTime") var createTime: String? = null,
    @SerializedName("updateTime") var updateTime: String? = null,

    ) : Serializable

data class UserQuestionFields(
    @SerializedName("user_id") var userId: UserQuestionString? = UserQuestionString(),
    @SerializedName("survey_id") var surveyId: UserQuestionString? = UserQuestionString(),
    @SerializedName("template_id") var templateId: TemplateId? = TemplateId(),
    @SerializedName("question_type_id") var questionTypeId: UserQuestionInteger? = UserQuestionInteger(),
    @SerializedName("selectedRating") var selectedRating: Int? = null,
    @SerializedName("inactive") var inactive: UserQuestionBoolean? = UserQuestionBoolean(),
    @SerializedName("question_text") var questionText: UserQuestionString? = UserQuestionString(),
    @SerializedName("is_default") var isDefault: UserQuestionBoolean? = UserQuestionBoolean(),
    @SerializedName("cta_text") var ctaText: UserQuestionString? = UserQuestionString(),
    @SerializedName("sequence_no") var sequenceNo: SequenceNo? = SequenceNo(),
    @SerializedName("question_desc") var questionDesc: UserQuestionString? = UserQuestionString(),
    @SerializedName("max_val_description") var maxValDescription: UserQuestionString? = UserQuestionString(),
    @SerializedName("min_val_description") var minValDescription: UserQuestionString? = UserQuestionString(),
    @SerializedName("answers") var answers: Answers? = Answers(),
    @SerializedName("next_question") @Expose var nextQuestion: Map<String, Map<String, Map<String, Map<String, String>>>>? = HashMap(),

    ) : Serializable

data class UserQuestionString(

    @SerializedName("stringValue") var stringValue: String? = null,

    ) : Serializable

data class UserQuestionBoolean(

    @SerializedName("booleanValue") var stringValue: Boolean? = null,

    ) : Serializable

data class UserQuestionInteger(

    @SerializedName("integerValue") var integerValue: String? = null,

    ) : Serializable

data class SequenceNo(

    @SerializedName("integerValue") var integerValue: String? = null,

    ) : Serializable

data class TemplateId(

    @SerializedName("nullValue") var nullValue: String? = null,

    ) : Serializable

data class Answers(

    @SerializedName("arrayValue") var arrayValue: ArrayValue? = ArrayValue(),

    ) : Serializable

data class ArrayValue(

    @SerializedName("values") var values: ArrayList<UserQuestionString> = arrayListOf(),

    ) : Serializable