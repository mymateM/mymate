package com.example.mymate.domain.model

data class HomeStats (
    var dDay: String = "정산일",
    var houseExpenseTotal: String = "0",
    var houseExpenseRatioNotiText: String = "지금까지 예산의 를 썼어요",
    var isHouseholdBudgetOver: Boolean = false,
    var houseExpenseRatio: String = "00%",
    var expenseGraphGuide: Float = 0.94f,
    var expenseOverGuide: Float = 0.94f,
    var expenseIndicatorGuide: Float = 0.125f,
    var budgetOverWarn: String = "",
    var isNowBetter: Boolean = false,
    var compareGuideTop: Float = 0.185f,
    var compareGuideMid: Float = 0.365f,
    var periodPassed: String = "0",
    var expenseCompared: String = "0",
    var userExpenseLeft: String = "0",
    var userExpenseUsed: String = "0",
    var userBudget: String = "0"
)