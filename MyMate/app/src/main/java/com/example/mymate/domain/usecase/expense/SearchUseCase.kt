package com.example.mymate.domain.usecase.expense

import android.util.Log
import com.example.mymate.data.dto.expense.ExpenseSubjectDetail
import com.example.mymate.data.dto.expense.ExpenseSummary
import com.example.mymate.data.repository.expense.SearchRepository
import com.example.mymate.util.Category
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class SearchUseCase(private val searchRepo: SearchRepository) {
    private fun getSearchResult(
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

    private var idx = 0
    private val testExpense = ExpenseSummary(
        expenseAmount = "1000",
        expenseStore = "test",
        expenseCategoryName = "ETC",
        expenseCategoryImage = "ETC",
        expenseDate = "2025-05-26"
    )

    fun search(
        rawExpense: String,
        sortedValue: String,
        dayValue: String,
        categoryNameValue: String
    ): ArrayList<ArrayList<ExpenseSummary>> {
        val expense = rawExpense
            .replace("원", "")
            .replace(" ", "")
            .replace(",", "")
            .split("~")
        val (minExpense, maxExpense) = when (expense.size) {
            0 -> { "0" to "0" }
            1 -> { expense[0] to "0" }
            else -> { expense[0] to expense[1] }
        }

        val sorted = sortedValue != "과거순"

        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val day = dayValue
            .replace("-", " ")
            .replace(".", "-")
            .split(" ")
        val (firstDay, lastDay) = when (day.size) {
            0 -> { "2021-11-06" to LocalDate.now().format(formatter) }
            1 -> { day[0] to day[0] }
            else -> { day[0] to day[1] }
        }

        val category = Category.values().find { it.displayName == categoryNameValue }?.name ?: "ETC"
        val result = getSearchResult(minExpense, maxExpense, sorted, firstDay, lastDay, category)
        idx++
        for (i in 0 until idx) {
            if (result.isEmpty()) result.add(ArrayList())
            result[result.lastIndex].add(testExpense)
        }
        return result
    }
}