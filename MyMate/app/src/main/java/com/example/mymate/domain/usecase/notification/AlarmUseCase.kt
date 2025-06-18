package com.example.mymate.domain.usecase.notification

import com.example.mymate.data.dto.notification.UserActNotiDetail
import com.example.mymate.data.dto.notification.UserExpNotiDetail
import com.example.mymate.data.repository.notification.AlarmRepository

class AlarmUseCase(private val alarmRepo: AlarmRepository) {
    suspend fun getActivityNotiInfo(): ArrayList<UserActNotiDetail> {
        return alarmRepo.getActivityNoti().activityNotificationResponses
    }

    suspend fun getExpenseNotiInfo(): ArrayList<UserExpNotiDetail> {
        return alarmRepo.getExpenseNoti().notification_expenses
    }

    suspend fun isExpenseNotiNew(): Boolean {
        val noti = getExpenseNotiInfo()
        if (noti.size == 0) { return true }
        return alarmRepo.getExpenseNoti().notification_expenses[0].is_read
    }

    suspend fun readActNoti(id: String) {
        alarmRepo.readActivityNoti(id)
    }

    fun readExpNoti(id: String) {
        if (id == "null") return
        alarmRepo.readExpenseNoti(id)
    }

    suspend fun getActNotiByDate(noti: ArrayList<UserActNotiDetail>): ArrayList<ArrayList<UserActNotiDetail>> {
        val containedList = ArrayList<ArrayList<UserActNotiDetail>>()
        if (noti.size == 0) { return containedList }
        var date = noti[0].created_at.substring(0 until 10)
        for (n in noti) {
            if (date == n.created_at.substring(0 until 10)) {
                containedList[containedList.lastIndex].add(n)
            } else {
                date = n.created_at.substring(0 until 10)
                containedList.add(ArrayList())
                containedList[containedList.lastIndex].add(n)
            }
        }
        return containedList
    }

    suspend fun getExpNotiByDate(noti: ArrayList<UserExpNotiDetail>): ArrayList<ArrayList<UserExpNotiDetail>> {
        val containedList = ArrayList<ArrayList<UserExpNotiDetail>>()
        if (noti.size == 0) { return containedList }
        var date = noti[0].created_at.substring(0 until 10)
        for (n in noti) {
            if (date == n.created_at.substring(0 until 10)) {
                containedList[containedList.lastIndex].add(n)
            } else {
                date = n.created_at.substring(0 until 10)
                containedList.add(ArrayList())
                containedList[containedList.lastIndex].add(n)
            }
        }
        return containedList
    }
}