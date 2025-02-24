package com.example.mymate.domain

import android.icu.text.DecimalFormat
import com.example.mymate.domain.model.HomeStats
import com.example.mymate.data.dto.expense.response.HomeInfoResponse
import com.example.mymate.data.repository.MainHomeRepository
import kotlin.math.absoluteValue

class MainHomeUseCase(private val homeRepo: MainHomeRepository) {
    private var homeInfo: HomeInfoResponse = HomeInfoResponse()

    suspend fun getHomeStats(): HomeStats {
        homeInfo = homeRepo.getHomeInfo()
        var homeStats = HomeStats()
        // 여기서부터 받아온 데이터를 가공합니다
        homeStats.dDay = "정산일 D${homeInfo.data.household.settlement_d_day}"
        homeStats.houseExpenseTotal = DecimalFormat("#,###").format(homeInfo.data.household.by_now_expense.toInt())
        homeStats.houseExpenseRatio = "${homeInfo.data.household.settlement_d_day}%"
        homeStats = processUntilNowData(homeStats)
        homeStats = processCompareData(homeStats)
        homeStats.userExpenseLeft = homeInfo.data.me.user_by_now_left_expense
        homeStats.userExpenseUsed = homeInfo.data.me.user_by_now_total_expense
        homeStats.userBudget = homeInfo.data.me.user_total_budget
        return homeStats
    }

    private fun processUntilNowData(homeStats: HomeStats): HomeStats { // 현재 소비 그래프 데이터 대응
        val ratio = homeInfo.data.household.by_now_budget_ratio
        homeStats.expenseGraphGuide = 0.94f
        homeStats.expenseOverGuide = 0.94f

        if (ratio.toInt() > 100) {
            homeStats.houseExpenseRatioNotiText = "지금까지 예산을 ${ratio.toInt() - 100}% 초과했어요"
            homeStats.isHouseholdBudgetOver = true
            homeStats.expenseOverGuide = (0.94 - 0.88 *((ratio.toFloat() - 100f) / 100)).toFloat()
            homeStats.expenseIndicatorGuide = 0.87f
        } else {
            homeStats.houseExpenseRatioNotiText = "지금까지 예산의 ${ratio}를 썼어요"
            homeStats.isHouseholdBudgetOver = false
            if ((0.06 + 0.88 * (ratio.toFloat() / 100)).toFloat() > 0.87f) {
                homeStats.expenseIndicatorGuide = 0.87f
            } else if ((0.08 + 0.88 * (ratio.toFloat() / 100)).toFloat() < 0.125) {
                homeStats.expenseIndicatorGuide = 0.125f
            } else {
                homeStats.expenseIndicatorGuide = (0.06 + 0.88 * (ratio.toFloat() / 100)).toFloat()
            }
        }

        if (homeInfo.data.household.is_household_budget_over_warn) {
            homeStats.budgetOverWarn = "이대로라면 예산을 초과할 것 같아요"
        } else {
            homeStats.budgetOverWarn = "아주 잘 하고 있어요. 이대로만 유지하는 게 좋겠어요!"
        }
        return homeStats
    }

    private fun processCompareData(homeStats: HomeStats): HomeStats { // 지난달 비교 그래프 데이터 대응
        homeStats.periodPassed = "지난 달 ${homeInfo.data.household.expense_duration}일 대비 더 썼어요"
        homeStats.compareGuideTop = 0.185f
        homeStats.compareGuideMid = 0.365f
        homeStats.expenseCompared = DecimalFormat("#,###").format(homeInfo.data.household.now_expense_diff.toInt().absoluteValue)
        if (homeInfo.data.household.by_previous_expense.toInt() < homeInfo.data.household.by_now_expense.toInt()) {
            homeStats.isNowBetter = true
            homeStats.periodPassed = "지난 달 ${homeInfo.data.household.expense_duration}일 대비 덜 썼어요"
            homeStats.compareGuideMid = (0.365 + 0.235 * (homeInfo.data.household.by_now_expense.toFloat() / homeInfo.data.household.by_previous_expense.toFloat())).toFloat()
        }
        return homeStats
    }
}