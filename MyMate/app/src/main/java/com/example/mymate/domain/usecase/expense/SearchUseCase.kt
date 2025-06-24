package com.example.mymate.domain.usecase.expense

import com.example.mymate.data.dto.expense.ExpenseSummary
import com.example.mymate.data.repository.expense.SearchRepository

class SearchUseCase(private val searchRepo: SearchRepository) {
    suspend fun getSearchResult(
        minExpense: String,
        maxExpense: String,
        sorted: Boolean,
        firstDay: String,
        lastDay: String,
        categoryName: String
    ): ArrayList<ArrayList<ExpenseSummary>> {
        val result = searchRepo.getSearchResult(minExpense, maxExpense, sorted, firstDay, lastDay, categoryName)
        val itemList = ArrayList<ArrayList<ExpenseSummary>>()
        if (result.isEmpty()) return itemList
        itemList[0].add(result[0])
        for (i in 1 until itemList.size) {
            if (result[i].expenseDate != result[i-1].expenseDate) {
                itemList.add(ArrayList())
            }
            itemList.last().add(result[i])
        }
        return itemList
    }
}