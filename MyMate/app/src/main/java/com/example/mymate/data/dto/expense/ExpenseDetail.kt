package com.example.mymate.data.dto.expense

data class ExpenseDetail (
    var payment_amount: String = "",
    var expense_memo: String = "",
    var expense_category: String = "",
    var expense_store: String = "",
    var expense_register_date: String = ""
)