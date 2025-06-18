package com.example.mymate.data.repository.home

import com.example.mymate.DataStoreRepoUser
import com.example.mymate.data.remote.api.RetrofitClientInstance
import com.example.mymate.data.dto.expense.response.CalendarResponse
import com.example.mymate.data.dto.expense.response.DailyExpenseResponse
import com.example.mymate.data.remote.api.expense.ExpenseApi
import java.time.LocalDate

class MainSpendingRepository(private val userRepo: DataStoreRepoUser) {
    private val endPoint = RetrofitClientInstance.client?.create(ExpenseApi::class.java)

    suspend fun getDailyExpense(date: LocalDate): DailyExpenseResponse {
        val accessToken = userRepo.userAccessReadFlow.toString()
        var expenses = DailyExpenseResponse()
        try {
            val result = endPoint!!.getDailyExpense("Bearer $accessToken", date.year.toString(), processMonth(date), processDate(date))
            expenses = result.body()!!
        } catch (e: Exception) {
            //TODO: 연결 불가할 경우 코드 작성 - Timeout, invalid Access 대응할 것.
        }
        return expenses
    }

    suspend fun getCalendarInfo(date: LocalDate): CalendarResponse {
        val accessToken = userRepo.userAccessReadFlow.toString()
        var calendarInfo = CalendarResponse()
        try {
            val result = endPoint!!.getCalendar("Bearer $accessToken", date.year.toString(), date.monthValue.toString(), date.dayOfMonth.toString())
            calendarInfo = result.body()!!
        } catch (e: Exception) {
            //TODO: 연결 불가할 경우 코드 작성 - Timeout, invalid Access 대응할 것.
        }
        return calendarInfo
    }

    private fun processMonth(date: LocalDate): String {
        val month = if (date.monthValue < 10) {
            "0${date.monthValue}"
        } else {
            date.monthValue.toString()
        }
        return month
    }

    private fun processDate(date: LocalDate): String {
        val date = if (date.dayOfMonth < 10) {
            "0${date.dayOfMonth}"
        } else {
            date.dayOfMonth.toString()
        }
        return date
    }
}