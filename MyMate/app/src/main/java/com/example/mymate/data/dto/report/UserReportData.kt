package com.example.mymate.data.dto.report

data class UserReportData (
    var ReportDate: ReportDate = ReportDate(),
    var expense_categories: ArrayList<ExpenseCategory> = ArrayList()
)