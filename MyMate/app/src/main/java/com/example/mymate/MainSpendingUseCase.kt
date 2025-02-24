package com.example.mymate

import com.example.mymate.data.dto.expense.CalendarInfo
import com.example.mymate.data.dto.expense.CalendarWrapper
import com.example.mymate.data.dto.expense.ExpenseSummary
import com.example.mymate.data.dto.expense.response.DailyExpenseResponse
import java.time.LocalDate
import java.time.YearMonth

class MainSpendingUseCase(private val spendingRepo: MainSpendingRepository) {
    suspend fun getDailyExpenses(date: LocalDate): ArrayList<ExpenseSummary> {
        val response = spendingRepo.getDailyExpense(date)
        return response.data.expenses
    }

    suspend fun getCalendarInfo(date: LocalDate): ArrayList<CalendarInfo> {
        val response = spendingRepo.getCalendarInfo(date).data
        val days = ArrayList<Int>()
        val start = date.withDayOfMonth(1).dayOfWeek.value + 1
        for (i in 1 until start) {
            days.add(0)
        }
        for (i in 1 .. YearMonth.from(date).lengthOfMonth()) {
            days.add(i)
        }
        val len = days.size
        for (i in len .. 35) {
            days.add(0)
        }
        val dateInfo = ArrayList<Int>()
        val expenseAmount = ArrayList<Int>()
        for (i in 0 until response.household_daily_expenses.size) {
            dateInfo.add(response.household_daily_expenses[i].expense_date.toInt())
            expenseAmount.add(response.household_daily_expenses[i].daily_total_expense.toInt())
        }
        val calendarInfo = ArrayList<CalendarInfo>()
        for (i in 0 until days.size) {
            if (dateInfo.contains(days[i])) {
                calendarInfo.add(CalendarInfo(days[i], expenseAmount[dateInfo.indexOf(days[i])]))
            } else {
                calendarInfo.add(CalendarInfo(days[i], 0))
            }
            if (calendarInfo[i].days == date.dayOfMonth) {
                val temp = calendarInfo[i]
                calendarInfo[i] = CalendarInfo(temp.days, temp.dayExpenses, true)
            }
        }

        return calendarInfo
    }
}