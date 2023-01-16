package com.babble.babblesdk.repository

import com.babble.babblesdk.model.AddResponseRequest
import com.babble.babblesdk.model.getSurveyResponse.SurveyResponse
import com.babble.babblesdk.model.triggerModel.TriggerModel
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

internal interface BabbleApiInterface {

    @GET("get_all_triggers_for_customer")
    fun getAllTrigger(
        @Header("api_key") headerKey: String?,
    ): Call<List<TriggerModel>>

    @GET("get_survey")
    fun getSurvey(
        @Header("api_key") headerKey: String?,
    ): Call<List<SurveyResponse>>


    @POST("write_survery_question_response")
    fun addResponse(
        @Body() request: AddResponseRequest
    ): Call<ResponseBody>

}