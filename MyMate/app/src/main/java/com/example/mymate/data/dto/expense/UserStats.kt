package com.example.mymate.data.dto.expense

data class UserStats (
    var user_id: String = "",
    var user_total_budget: String = "",
    var user_by_now_total_expense: String = "",
    var user_by_now_left_expense: String = ""
)