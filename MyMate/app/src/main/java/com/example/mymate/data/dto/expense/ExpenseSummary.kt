package com.example.mymate.data.dto.expense


data class ExpenseSummary (
    var expenseId: String = "",
    var expenseAmount: String = "",
    var expenseStore: String = "",
    var expenseCategoryName: String = "",
    var expenseCategoryImage: String = "",
    var settlementSubjects: ArrayList<ExpenseSubjectDetail> = ArrayList(),
    var expenseDate: String = ""
)