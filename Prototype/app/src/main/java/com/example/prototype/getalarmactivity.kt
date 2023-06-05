package com.example.prototype

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface getalarmactivity {
    @GET("notification/activity/{userId}")
    fun getalarmactivity(@Path("userId") userId: String): Call<activity_alarm>
}