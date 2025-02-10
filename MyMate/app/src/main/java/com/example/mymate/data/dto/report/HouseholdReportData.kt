package com.example.mymate.data.dto.report

data class HouseholdReportData (
    var ReportDate: ReportDate = ReportDate(),
    var is_expense_over_budget: Boolean = false,
    var budget_real_expense_diff: String = "",
    var total_expense: String = "",
    var expense_categories: ArrayList<ExpenseCategory> = ArrayList()
)