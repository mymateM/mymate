package com.example.mymate.data.dto.expense.response

import com.example.mymate.data.dto.expense.ExpenseDetail

data class DailySingleExpenseResponse (
    var message: String = "",
    var status: String = "",
    var data: ExpenseDetail = ExpenseDetail()
)
