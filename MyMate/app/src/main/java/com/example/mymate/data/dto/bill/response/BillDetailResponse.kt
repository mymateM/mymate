package com.example.mymate.data.dto.bill.response

import com.example.mymate.data.dto.bill.BillDetail

data class BillDetailResponse (
    var message: String = "",
    var status: String = "",
    var data: BillDetail = BillDetail()
)
