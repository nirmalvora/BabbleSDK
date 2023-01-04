package com.babble.babblesdk.repository

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header

interface BabbleApiInterface {
    @GET("initialize")
    fun initializeSdk(
        @Header("api_key") headerKey: String?,
    ): Call<ResponseBody>?

}