package com.example.mymate.data.repository.expense

import com.example.mymate.DataStoreRepoUser
import com.example.mymate.data.dto.expense.ExpenseSummary
import com.example.mymate.data.remote.api.RetrofitClientInstance
import com.example.mymate.data.remote.api.expense.ExpenseApi

class SearchRepository(private val userRepo: DataStoreRepoUser) {
    private val endPoint = RetrofitClientInstance.client?.create(ExpenseApi::class.java)

    suspend fun getSearchResult(
        minExpense: String,
        maxExpense: String,
        sorted: Boolean,
        firstDay: String,
        lastDay: String,
        categoryName: String
    ): ArrayList<ExpenseSummary> {
        val accessToken = userRepo.userAccessReadFlow.toString()
        try {
            val result = endPoint!!.searchExpense(
                "Bearer $accessToken",
                expense_amount_min = minExpense,
                expense_amount_max = maxExpense,
                sorted_by_newest = sorted,
                expense_date_min = firstDay,
                expense_date_max = lastDay,
                expense_category_name = categoryName)
            return result.body()!!.data.expenses
        } catch (e: Exception) {
            //TODO: 연결 불가할 경우 코드 작성 - Timeout, invalid Access 대응할 것.
        }
        return ArrayList()
    }
}