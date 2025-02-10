package com.example.mymate.data.dto.report

data class MemberMonthlyResult (
    var settlement_date: ReportDate = ReportDate(),
    var user: UserAndMemberDiff = UserAndMemberDiff(),
    var roommates: ArrayList<MemberMonthlyStatus> = ArrayList()
)