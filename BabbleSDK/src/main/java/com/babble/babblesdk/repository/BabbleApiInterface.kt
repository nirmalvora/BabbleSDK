package com.babble.babblesdk.repository

import com.babble.babblesdk.model.AddResponseRequest
import com.babble.babblesdk.model.EligibleSurveyRequest
import com.babble.babblesdk.model.EligibleSurveyResponse.EligibleSurveyResponse
import com.babble.babblesdk.model.SurveyInstanceRequest
import com.babble.babblesdk.model.backendEventResponse.BackedEventResponse
import com.babble.babblesdk.model.cohortResponse.CohortResponse
import com.babble.babblesdk.model.questionsForUser.UserQuestionResponse
import com.babble.babblesdk.model.styleForUserIdResponse.StyleForUserIdResponse
import com.babble.babblesdk.model.surveyForUsers.UserSurveyResponse
import com.babble.babblesdk.model.triggerForUser.UserTriggerResponse
import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

internal interface BabbleApiInterface {

    @POST("write_survery_question_response")
    fun addResponse(
        @Header("babble_user_id") userId: String?,
        @Body request: AddResponseRequest
    ): Call<ResponseBody>

    @POST("create_survey_instance")
    fun createSurveyInstance(
        @Body request: SurveyInstanceRequest
    ): Call<ResponseBody>


    @GET("get_backend_events")
    fun getBackendEvents(
        @Header("user_id") userId: String?,
        @Header("customer_id") customerId: String?
    ): Call<List<BackedEventResponse>>

    @GET("get_cohorts")
    fun getCohorts(
        @Header("babble_user_id") userId: String?,
        @Header("customer_id") customerId: String?
    ): Call<List<CohortResponse>>


    @GET("get_surveys_for_user_id")
    fun getSurveyForUserId(@Header("user_id") userId: String?): Observable<List<UserSurveyResponse>>

    @GET("get_triggers_for_user_id")
    fun getTriggerForUserId(@Header("user_id") userId: String?): Observable<List<UserTriggerResponse>>

    @GET("get_questions_for_user_id")
    fun getQuestionForUserId(@Header("user_id") userId: String?): Observable<List<UserQuestionResponse>>

    @GET("get_styles_for_user_id")
    fun getStyleForUserId(@Header("user_id") userId: String?): Observable<List<StyleForUserIdResponse>>

    @POST("eligible_survey_ids")
    fun getEligibleSurveyIds(@Body body: EligibleSurveyRequest): Call<EligibleSurveyResponse>
}