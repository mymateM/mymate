package com.example.mymate.data.dto.bill.response

import com.example.mymate.data.dto.bill.Bills

data class BillsResponse (
    var message: String = "",
    var status: String = "",
    var data: Bills = Bills()
)