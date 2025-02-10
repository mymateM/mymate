package com.example.mymate.data.dto.expense.response

import com.example.mymate.data.dto.expense.CalendarWrapper

data class CalendarResponse (
    var message: String = "",
    var status: String = "",
    var data: CalendarWrapper = CalendarWrapper()
)