package com.example.prototype

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface getSettlement {

    @GET("settlement/me/{userId}")
    fun getSettlement(@Path("userId") userId: String) : Call<settlementdata>
}