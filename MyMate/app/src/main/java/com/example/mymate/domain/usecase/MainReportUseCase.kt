package com.example.mymate.domain.usecase

import com.example.mymate.data.dto.report.HouseholdReportProcessed
import com.example.mymate.data.dto.report.UserReportProcessed
import com.example.mymate.data.repository.MainReportRepository
import java.time.LocalDate

class MainReportUseCase(private val reportRepo: MainReportRepository) {
    suspend fun getPeriodDate(): LocalDate {
        val today = LocalDate.now().dayOfMonth
        val periodStartDay: Int = try {
            reportRepo.getPeriodDate().toInt()
        } catch (e: Exception) {
            1
        }
        if (today < periodStartDay) {
            return LocalDate.now().minusMonths(1).withDayOfMonth(periodStartDay)
        }
        return LocalDate.now().withDayOfMonth(periodStartDay)
    }

    suspend fun getUserReportData(month: Int, day: Int): UserReportProcessed {
        val processed = UserReportProcessed()
        val rawData = reportRepo.getMyReport(month, day)
        for (i in rawData.expense_categories) {
            processed.categoryName.add(i.category_name)
            processed.categoryRatio.add(i.total_expense_ratio.toFloat())
            processed.categoryAbs.add(i.total_expense_amount.toInt())
            processed.totalExpense += i.total_expense_amount.toInt()
            if (processed.categoryRatio[processed.maxCategoryIndex] < processed.categoryRatio.last()) {
                processed.maxCategoryIndex = processed.categoryRatio.lastIndex
            }
        }

        return processed
    }

    suspend fun getHouseholdReportData(month: Int, day: Int): HouseholdReportProcessed {
        val processed = HouseholdReportProcessed()
        val rawData = reportRepo.getHouseholdReport(month, day)
        for (i in rawData.expense_categories) {
            processed.categoryName.add(i.category_name)
            processed.categoryRatio.add(i.total_expense_ratio.toFloat())
            processed.categoryAbs.add(i.total_expense_amount.toInt())
            processed.totalExpense += i.total_expense_amount.toInt()
            if (processed.categoryRatio[processed.maxCategoryIndex] < processed.categoryRatio.last()) {
                processed.maxCategoryIndex = processed.categoryRatio.lastIndex
            }
        }
        processed.isOver = rawData.is_expense_over_budget
        return processed
    }
}