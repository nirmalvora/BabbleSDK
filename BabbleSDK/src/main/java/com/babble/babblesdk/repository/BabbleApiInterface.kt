package com.babble.babblesdk.repository

import com.babble.babblesdk.model.AddResponseRequest
import com.babble.babblesdk.model.questionsForUser.UserQuestionResponse
import com.babble.babblesdk.model.surveyForUsers.UserSurveyResponse
import com.babble.babblesdk.model.triggerForUser.UserTriggerResponse
import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

internal interface BabbleApiInterface {

    @POST("write_survery_question_response")
    fun addResponse(
        @Body request: AddResponseRequest
    ): Call<ResponseBody>


    @GET("get_surveys_for_user_id")
    fun getSurveyForUserId(@Header("user_id") userId: String?): Observable<List<UserSurveyResponse>>

    @GET("get_triggers_for_user_id")
    fun getTriggerForUserId(@Header("user_id") userId: String?): Observable<List<UserTriggerResponse>>

    @GET("get_questions_for_user_id")
    fun getQuestionForUserId(@Header("user_id") userId: String?): Observable<List<UserQuestionResponse>>
}