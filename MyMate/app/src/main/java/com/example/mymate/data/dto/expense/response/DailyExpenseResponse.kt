package com.example.mymate.data.dto.expense.response

import com.example.mymate.data.dto.expense.DailyExpenses

data class DailyExpenseResponse (
    var message: String = "",
    var status: String = "",
    var data: DailyExpenses = DailyExpenses()
)