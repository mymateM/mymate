package com.example.mymate.data.dto.report

data class HouseholdReportProcessed(
    var isOver: Boolean = false,
    val categoryName: ArrayList<String> = ArrayList(),
    val categoryRatio: ArrayList<Float> = ArrayList(),
    val categoryAbs: ArrayList<Int> = ArrayList(),
    var totalExpense: Int = 0,
    var maxCategoryIndex: Int = 0
)
