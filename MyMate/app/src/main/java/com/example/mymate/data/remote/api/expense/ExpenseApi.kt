package com.example.mymate.data.remote.api.expense

import com.example.mymate.data.dto.common.DefaultResponse
import com.example.mymate.data.dto.expense.request.ExpenseWriteRequest
import com.example.mymate.data.dto.expense.response.CalendarResponse
import com.example.mymate.data.dto.expense.response.DailyExpenseResponse
import com.example.mymate.data.dto.expense.response.DailySingleExpenseResponse
import com.example.mymate.data.dto.expense.response.SearchResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface ExpenseApi {
    @GET("api/v1/expense/daily-total/day/{year}/{month}/{dayOfMonth}")
    fun getDailyExpense(@Header("Authorization") Authorization: String, @Path("year") year: String, @Path("month") month: String, @Path("dayOfMonth") dayOfMonth: String) : Response<DailyExpenseResponse>

    @POST("api/v1/expense")
    fun putDailyExpense(@Header("Authorization") Authorization: String, @Body req: ExpenseWriteRequest): Call<DefaultResponse>

    @GET("api/v1/expense/{expense_id}")
    fun getDailySingleExpense(@Header("Authorization") Authorization: String, @Path("expense_id") expense_id: String): Call<DailySingleExpenseResponse>

    @POST("api/v1/expense/search") //home?
    fun searchExpense(
        @Header("Authorization") Authorization: String,
        @Query("expense_date_max") expense_date_max: String,
        @Query("expense_date_min") expense_date_min: String,
        @Query("expense_category_name") expense_category_name: String,
        @Query("expense_amount_max") expense_amount_max: String,
        @Query("expense_amount_min") expense_amount_min: String,
        @Query("sorted_by_newest") sorted_by_newest: Boolean
    ): Response<SearchResponse>

    @DELETE("api/v1/expense/{expense_id}")
    fun deleteExpense(@Header("Authorization") Authorization: String, @Path("expense_id") expense_id: String): Call<Response<Void>>

    @GET("api/v1/expense/daily-total/month/{year}/{month}/{day}") //home?
    fun getCalendar(@Header("Authorization") Authorization: String, @Path("year") year: String, @Path("month") month: String, @Path("day") day: String) : Response<CalendarResponse>
}