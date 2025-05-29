package com.example.mymate.domain.usecase

import com.example.mymate.data.dto.report.*
import com.example.mymate.data.repository.SettlementRepository
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class SettlementUseCase(private val settleRepo: SettlementRepository) {
    private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    fun getSettlementDate(): Int {
        if (settleRepo.getSettlementDate().isEmpty()) return 1
        return settleRepo.getSettlementDate().toInt()
    }

    fun getPeriodStartDate(settleDay: Int): LocalDate {
        return getPeriodEndDate(settleDay).minusMonths(1).plusDays(1)
    }

    fun getPeriodEndDate(settleDay: Int): LocalDate {
        if (settleDay > LocalDate.now().dayOfMonth) {
            return LocalDate.now().minusMonths(1).withDayOfMonth(settleDay)
        }
        return LocalDate.now().withDayOfMonth(settleDay)
    }

    suspend fun getMySettleInfo(): UserMonthlyResult {
        val settleDay = getSettlementDate()
        return settleRepo.getMySettleInfo(getPeriodStartDate(settleDay).format(formatter), getPeriodEndDate(settleDay).format(formatter))
    }

    suspend fun getMateSettleInfo(): MemberMonthlyResult {
        val settleDay = getSettlementDate()
        return settleRepo.getMateSettleInfo(getPeriodStartDate(settleDay).format(formatter), getPeriodEndDate(settleDay).format(formatter))
    }

    fun getHouseSettleInfo(date: String): HouseholdReportData {
        return settleRepo.getHouseholdReport(date)
    }

    fun getMyReport(date: String): UserReportData {
        return settleRepo.getMyReport(date)
    }

    fun getHouseholdPieData(rawData: HouseholdReportData): HouseholdReportProcessed { //TODO: 비슷한 함수 MainReportUseCase에 있음
        val resultData = processPieData(rawData.expense_categories)
        resultData.isOver = rawData.is_expense_over_budget
        return resultData
    }

    fun getMyPieData(rawData: UserReportData): HouseholdReportProcessed {
        val resultData = processPieData(rawData.expense_categories)
        resultData.isOver = false
        return resultData
    }

    private fun processPieData(data: ArrayList<ExpenseCategory>): HouseholdReportProcessed {
        val resultData = HouseholdReportProcessed()
        for (i in data) {
            resultData.categoryName.add(i.category_name)
            resultData.categoryRatio.add(i.total_expense_ratio.toFloat())
            resultData.categoryAbs.add(i.total_expense_amount.toInt())
            resultData.totalExpense += i.total_expense_amount.toInt()
            if (resultData.categoryRatio[resultData.maxCategoryIndex] < resultData.categoryRatio.last()) {
                resultData.maxCategoryIndex = resultData.categoryRatio.lastIndex
            }
        }
        return resultData
    }

    fun sendMoneyRequest(id: String) {
        settleRepo.sendMoneyRequest(id)
    }
}