package com.example.mymate

import android.util.Log

class CalendarValues {
    var firstDay = -1
    var lastDay = -1
    var tempDay = -1

    fun daycheck() {

        if (firstDay < 0 && lastDay < 0) {
            firstDay = tempDay
        } else if (firstDay < tempDay && lastDay < 0) {
            lastDay = tempDay
        } else if (firstDay > tempDay && lastDay < 0) {
            lastDay = firstDay
            firstDay = tempDay
        } else if (firstDay < 0 && lastDay > tempDay) {
            firstDay = tempDay
        } else if (firstDay < 0 && lastDay < tempDay) {
            firstDay = lastDay
            lastDay = tempDay
        } else if (tempDay < firstDay) {
            firstDay = tempDay
        } else if (tempDay > firstDay && tempDay < lastDay) {
            firstDay = tempDay
        } else if (tempDay > lastDay) {
            lastDay = tempDay
        } else if (tempDay == firstDay) {
            firstDay = -1
        } else if (tempDay == lastDay) {
            lastDay = -1
        }
    }

    fun setDay(day: Int) {
        tempDay = day
    }
}