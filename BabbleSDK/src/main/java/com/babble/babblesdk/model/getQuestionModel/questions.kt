package com.babble.babblesdk.model.getQuestionModel

import com.google.gson.annotations.SerializedName
import java.io.Serializable


data class Questions (
  @SerializedName("survey_id"        ) var surveyId       : String?  = null,
  @SerializedName("template_id"      ) var templateId     : String?  = null,
  @SerializedName("question_type_id" ) var questionTypeId : Int?     = null,
  @SerializedName("inactive"         ) var inactive       : Boolean? = null,
  @SerializedName("question_text"    ) var questionText   : String?  = null,
  @SerializedName("is_default"       ) var isDefault      : Boolean? = null,
  @SerializedName("cta_text"         ) var ctaText        : String?  = null,
  @SerializedName("sequence_no"      ) var sequenceNo     : Int?     = null,
  @SerializedName("question_desc"    ) var questionDesc   : String?  = null,
  @SerializedName("readTime"         ) var readTime       : String?  = null,
  @SerializedName("max_val_description" ) var maxValDescription : String?  = null,
  @SerializedName("min_val_description" ) var minValDescription : String?  = null,
  @SerializedName("answers"          ) var answers        : ArrayList<String> = arrayListOf(),
  @SerializedName("selected_options"          ) var selectedOptions        : ArrayList<String> = arrayListOf(),
  @SerializedName("selected_rating"          ) var selectedRating        : Int? = null,
  @SerializedName("answer_text"          ) var answerText        : String? = null,

) : Serializable