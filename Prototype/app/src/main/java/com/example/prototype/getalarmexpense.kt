package com.example.prototype

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface getalarmexpense {
    @GET("notification/expense/{userId}")
    fun getalarmexpense(@Path("userId") userId: String): Call<expense_alarm>
}