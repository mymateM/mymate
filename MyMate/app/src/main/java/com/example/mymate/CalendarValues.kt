package com.example.mymate

import android.util.Log

class CalendarValues {
    var firstDay = -1
    var lastDay = -1
    var tempDay = -1
    var lastMid = false

    fun daycheck() {

        if (firstDay < 0 && lastDay < 0) {
            firstDay = tempDay
            lastMid = false
        } else if (firstDay < tempDay && lastDay < 0) {
            lastDay = tempDay
            lastMid = false
        } else if (firstDay > tempDay && lastDay < 0) {
            lastDay = firstDay
            firstDay = tempDay
            lastMid = false
        } else if (firstDay < 0 && lastDay > tempDay) {
            firstDay = tempDay
            lastMid = false
        } else if (firstDay < 0 && lastDay < tempDay) {
            firstDay = lastDay
            lastDay = tempDay
            lastMid = false
        } else if (tempDay == firstDay && lastDay < 0) {
            firstDay = -1
            lastMid = false
        } else if (tempDay < firstDay) {
            firstDay = tempDay
            lastMid = false
        } else if (tempDay in (firstDay + 1) until lastDay && !lastMid) {
            firstDay = tempDay
            lastMid = true
        } else if (tempDay in (firstDay + 1) until lastDay && lastMid) {
            lastDay = tempDay
            lastMid = false
        } else if (tempDay > lastDay) {
            lastDay = tempDay
            lastMid = false
        } else if (tempDay == firstDay) {
            firstDay = -1
            lastMid = false
        } else if (tempDay == lastDay) {
            lastDay = -1
            lastMid = false
        }
    }

    fun setDay(day: Int) {
        tempDay = day
    }
}