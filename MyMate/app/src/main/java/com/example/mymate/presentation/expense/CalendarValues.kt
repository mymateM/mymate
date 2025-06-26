package com.example.mymate.presentation.expense

class CalendarValues { //Search에서, 선택 범위를 리턴하는 로직
    var firstDay = -1
    var lastDay = -1
    private var tempDay = -1
    private var lastMid = false

    private fun updateSelectedDays() {
        when {
            firstDay < 0 && lastDay < 0 -> { firstDay = tempDay } //처음 터치
            firstDay < tempDay && lastDay < 0 -> { lastDay = tempDay } //두 번째 터치(이후 날짜 선택)
            firstDay > tempDay && lastDay < 0 -> { //두 번째 터치(이전 날짜 선택)
                lastDay = firstDay
                firstDay = tempDay
            }
            //firstDay의 선택 해제 상황 대응
            firstDay < 0 && lastDay > tempDay -> { firstDay = tempDay }
            firstDay < 0 && lastDay < tempDay -> {
                firstDay = lastDay
                lastDay = tempDay
            }
            //lastDay가 선택해제되었을 때 , firstDay도 해제하는 경우
            tempDay == firstDay && lastDay < 0 -> { firstDay = -1 }
            //구간 선택 상황에서 재선택했을 때
            tempDay < firstDay -> { firstDay = tempDay } //구간 이전 날짜를 선택
            tempDay in (firstDay + 1) until lastDay -> { //구간에 포함된 날짜를 선택
                if (!lastMid) { //첫 번째 선택에서는 firstDay 이동
                    firstDay = tempDay
                    lastMid = true
                    return
                } else { //두 번째 선택에서는 lastDay 이동
                    lastDay = tempDay
                }
            }
            tempDay > lastDay -> { lastDay = tempDay } //구간 이후 날짜를 선택
            //선택 해제
            tempDay == firstDay -> { firstDay = -1 }
            tempDay == lastDay -> { lastDay = -1 }
            else -> return
        }
        lastMid = false
    }


    fun setDay(day: Int) {
        tempDay = day
        updateSelectedDays()
    }

    fun init() {
        firstDay = -1
        lastDay = -1
        lastMid = false
    }

    fun copy(
        firstDay: Int = this.firstDay,
        lastDay: Int = this.lastDay
    ): CalendarValues {
        val newVal = CalendarValues()
        newVal.setDay(firstDay)
        newVal.setDay(lastDay)
        // 필요한 내부 상태 복사도 여기서 같이 진행 가능
        return newVal
    }
}