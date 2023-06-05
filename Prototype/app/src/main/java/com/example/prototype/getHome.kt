package com.example.prototype

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface  getHome {
    @GET("home/{userId}")
    fun getHome(@Path("userId") userId: String) : Call<request_home>
}