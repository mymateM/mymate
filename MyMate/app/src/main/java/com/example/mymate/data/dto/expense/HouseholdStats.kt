package com.example.mymate.data.dto.expense

data class HouseholdStats (
    var house_id: String = "",
    var household_name: String = "",
    var by_now_expense: String = "0",
    var by_now_budget_ratio: String = "0",
    var settlement_d_day: String = "",
    var by_previous_expense: String = "0",
    var now_expense_diff: String = "0",
    var is_household_budget_over_warn: Boolean = false,
    var expense_duration: String = ""
)