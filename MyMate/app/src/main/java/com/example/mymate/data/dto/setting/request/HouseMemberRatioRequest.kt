package com.example.mymate.data.dto.setting.request

import com.example.mymate.data.dto.setting.HouseMemberRatio

data class HouseMemberRatioRequest (
    var household_members: ArrayList<HouseMemberRatio> = ArrayList()
)