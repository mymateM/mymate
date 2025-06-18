package com.example.mymate.data.remote.service.bill

import com.example.mymate.data.dto.bill.request.BillWriteRequest
import com.example.mymate.data.dto.bill.response.BillCategoryResponse
import com.example.mymate.data.dto.bill.response.BillDetailResponse
import com.example.mymate.data.dto.bill.response.BillsResponse
import com.example.mymate.data.dto.common.DefaultResponse
import retrofit2.Call
import retrofit2.http.*

interface BillApi {
    @GET("api/v1/bills/GAS")
    fun getGasBillList(@Header("Authorization") Authorization: String) : Call<BillsResponse>

    @GET("api/v1/bills/ELECTRICITY")
    fun getElectricityBillList(@Header("Authorization") Authorization: String) : Call<BillsResponse>

    @GET("api/v1/bills/WATER")
    fun getWaterBillList(@Header("Authorization") Authorization: String) : Call<BillsResponse>

    @GET("api/v1/bills/ETC")
    fun getEtcBillList(@Header("Authorization") Authorization: String) : Call<BillsResponse>

    @GET("api/v1/bill/{bill_id}")
    fun getBill(@Header("Authorization") Authorization: String, @Path("bill_id") bill_id: String) : Call<BillDetailResponse>

    @GET("api/v1/bills/category")
    fun getBillCategory(@Header("Authorization") Authorization: String): Call<BillCategoryResponse>

    @POST("api/v1/bill")
    fun postBill(@Header("Authorization") Authorization: String, @Body req: BillWriteRequest): Call<DefaultResponse>

    @DELETE("api/v1/bills")
    fun deleteBill(@Header("Authorization") Authorization: String, @Query("bill_id_list") bill_id_list: String): Call<DefaultResponse>
}