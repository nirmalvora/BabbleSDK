package com.babble.babblesdk.model.getQuestionModel

import com.babble.babblesdk.model.getQuestionModel.SequenceNo
import com.google.gson.annotations.SerializedName
import java.io.Serializable


data class Fields (

  @SerializedName("survey_id"        ) var surveyId       : SurveyId?       = SurveyId(),
  @SerializedName("template_id"      ) var templateId     : TemplateId?     = TemplateId(),
  @SerializedName("question_type_id" ) var questionTypeId : QuestionTypeId? = QuestionTypeId(),
  @SerializedName("answers"          ) var answers        : Answers?        = Answers(),
  @SerializedName("answersText"      ) var answersText    : String?         = null,
  @SerializedName("selectedRating"   ) var selectedRating : Int?            = null,
  @SerializedName("inactive"         ) var inactive       : Inactive?       = Inactive(),
  @SerializedName("question_text"    ) var questionText   : StringValue?    = StringValue(),
  @SerializedName("is_default"       ) var isDefault      : IsDefault?      = IsDefault(),
  @SerializedName("cta_text"         ) var ctaText        : CtaText?        = CtaText(),
  @SerializedName("sequence_no"      ) var sequenceNo     : SequenceNo?     = SequenceNo(),
  @SerializedName("question_desc"    ) var questionDesc   : QuestionDesc?   = QuestionDesc(),
  @SerializedName("max_val_description" ) var maxValDescription : StringValue? = StringValue(),
  @SerializedName("min_val_description" ) var minValDescription : StringValue? = StringValue()

): Serializable
