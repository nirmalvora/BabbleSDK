package com.babble.babblesdk.repository

import com.babble.babblesdk.model.AddResponseRequest
import com.babble.babblesdk.model.InitResponse
import com.babble.babblesdk.model.getQuestionModel.QuestionResponse
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

internal interface BabbleApiInterface {
    @GET("initialize")
    fun initializeSdk(
        @Header("api_key") headerKey: String?,
    ): Call<InitResponse>

    @GET("get_questions_for_trigger")
    fun getQuestionForTrigger(
        @Header("api_key") headerKey: String?,
        @Query("trigger_name") triggerName: String?,
        @Query("time_val") timeVal: String?,
        @Query("customer_id") customerId: String?,

        ): Call<List<QuestionResponse>>

    @POST("write_survery_question_response")
    fun addResponse(
        @Body() request: AddResponseRequest
    ): Call<ResponseBody>

}