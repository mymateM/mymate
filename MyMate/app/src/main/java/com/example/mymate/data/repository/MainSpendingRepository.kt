package com.example.mymate.data.repository

import com.example.mymate.DataStoreRepoUser
import com.example.mymate.RetrofitClientInstance
import com.example.mymate.data.dto.expense.response.CalendarResponse
import com.example.mymate.data.dto.expense.response.DailyExpenseResponse
import com.example.mymate.getCalendar
import java.time.LocalDate

class MainSpendingRepository(private val userRepo: DataStoreRepoUser) {
    suspend fun getDailyExpense(date: LocalDate): DailyExpenseResponse {
        val accessToken = userRepo.userAccessReadFlow.toString()
        val endpoint = RetrofitClientInstance.client?.create(com.example.mymate.getDailyExpense::class.java)
        var expenses = DailyExpenseResponse()
        try {
            val result = endpoint!!.getDailyExpense("Bearer $accessToken", date.year.toString(), processMonth(date), processDate(date))
            expenses = result.body()!!
        } catch (e: Exception) {
            //TODO: 연결 불가할 경우 코드 작성 - Timeout, invalid Access 대응할 것.
        }
        return expenses
    }

    suspend fun getCalendarInfo(date: LocalDate): CalendarResponse {
        val accessToken = userRepo.userAccessReadFlow.toString()
        val endpoint = RetrofitClientInstance.client?.create(getCalendar::class.java)
        var calendarInfo = CalendarResponse()
        try {
            val result = endpoint!!.getCalendar("Bearer $accessToken", date.year.toString(), date.monthValue.toString(), date.dayOfMonth.toString())
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