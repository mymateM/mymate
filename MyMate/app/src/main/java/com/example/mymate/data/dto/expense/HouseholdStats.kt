package com.example.mymate.data.dto.expense

data class HouseholdStats (
    var house_id: String = "",
    var household_name: String = "",
    var by_now_expense: String = "",
    var by_now_budget_ratio: String = "",
    var settlement_d_day: String = "",
    var by_previous_expense: String = "",
    var now_expense_diff: String = "",
    var is_household_budget_over_warn: Boolean = false,
    var expense_duration: String = ""
)