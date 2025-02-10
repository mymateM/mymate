package com.example.mymate.data.dto.setting.response

import com.example.mymate.data.dto.setting.HouseSettlementRatio

data class HouseSettlementRatioResponse (
    var message: String = "",
    var status: String = "",
    var data: HouseSettlementRatio = HouseSettlementRatio()
)