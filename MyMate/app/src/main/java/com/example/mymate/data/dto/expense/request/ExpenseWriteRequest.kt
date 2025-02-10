package com.example.mymate.data.dto.expense.request

data class ExpenseWriteRequest (
    var expenseDate: String = "",
    var expenseAmount: String = "",
    var settlementSubjectIds: ArrayList<String> = ArrayList(),
    var expenseStore: String = "",
    var expenseCategory: String = "",
    var expenseMemo: String = ""
)