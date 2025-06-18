package com.example.mymate

import android.content.Context
import android.graphics.Typeface
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import com.example.mymate.data.dto.bill.response.BillDetailResponse
import com.example.mymate.data.remote.api.RetrofitClientInstance
import com.example.mymate.databinding.ActivityBilldetailBinding

class BillDetailActivity: AppCompatActivity() {
    lateinit var binding: ActivityBilldetailBinding
    lateinit var userRepo: DataStoreRepoUser
    lateinit var context: Context
    var retrofit = RetrofitClientInstance.client
    //var endpoint = retrofit?.create(getBill::class.java)
    var detailresponse: BillDetailResponse? = BillDetailResponse()
    //var endpointdelete = retrofit?.create(deleteBill::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityBilldetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        userRepo = DataStoreRepoUser(dataStore)
        context = this

        val id = intent.getStringExtra("itemid")
        val category = intent.getStringExtra("category")
        val suitBoldTypeface = Typeface.create(ResourcesCompat.getFont(this, R.font.suit_bold), Typeface.NORMAL)

        /* var accessToken = ""
        runBlocking {
            accessToken = userRepo.userAccessReadFlow.first().toString()
        }
        endpoint!!.getBill("Bearer $accessToken", id!!).enqueue(object : Callback<BillDetailResponse> {
            @RequiresApi(Build.VERSION_CODES.P)
            override fun onResponse(
                call: Call<BillDetailResponse>,
                response: Response<BillDetailResponse>
            ) {
                detailresponse = response.body()
                if (response.isSuccessful) {
                    binding.category.text = category
                    binding.duedate.text = detailresponse!!.data.bill_payment_date.replace("-", ".")
                    binding.memo.text = detailresponse!!.data.bill_memo
                    val amount = SpannableStringBuilder("${digitprocessing(detailresponse!!.data.bill_payment_amount)}원")
                    amount.setSpan(TypefaceSpan(suitBoldTypeface), amount.length - 1, amount.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                    binding.billamount.text = amount

                    val datelist = detailresponse!!.data.register_date.split("-")
                    val year = (datelist[0].toInt() % 1000 % 100).toString()
                    val month = datelist[1].toInt().toString()
                    val today = datelist[2].toInt().toString()
                    val enrolldate = year + "년 " + month + "월 " + today + "일"
                    binding.enrolldate.text = enrolldate

                    binding.deletebtn.setOnClickListener {
                        binding.deletepopup.isGone = false
                    }

                    binding.deleteconfirm.setOnClickListener {
                        deleteBills(id)
                    }


                    binding.cover.setOnClickListener {
                        binding.deletepopup.isGone = true
                    }

                    binding.dismiss.setOnClickListener {
                        binding.deletepopup.isGone = true
                    }

                } else {
                    binding.category.text = category
                    Toast.makeText(context, "실패", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<BillDetailResponse>, t: Throwable) {
                Toast.makeText(context, "연결 실패(고지서 상세)", Toast.LENGTH_SHORT).show()
            }
        }) */

        binding.backbtn.setOnClickListener {
            finish()
        }
    }

    override fun onBackPressed() {
        finish()
    }

    /* private fun deleteBills(id: String) {
        var accessToken = ""
        runBlocking {
            accessToken = userRepo.userAccessReadFlow.first().toString()
        }
        endpointdelete!!.deleteBill("Bearer $accessToken", bill_id_list = id).enqueue(object : Callback<DefaultResponse> {
            override fun onResponse(
                call: Call<DefaultResponse>,
                response: Response<DefaultResponse>
            ) {
                if (response.isSuccessful) {
                    finish()
                }
            }

            override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
            }

        })
    } */
}