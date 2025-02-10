package com.example.mymate.data.dto.expense

data class CurrentStatus (
    var household: HouseholdStats = HouseholdStats(),
    var me: UserStats = UserStats()
)