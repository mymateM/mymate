package com.example.prototype

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface getSettlementBill {
    @GET("settlement/roommate/{userId}")
    fun getSettlementBill(@Path("userId") userId: String): Call<send_money>
}