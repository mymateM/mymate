package com.example.mymate

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material3.contentColorFor
import com.example.mymate.databinding.ActivityBilldetailBinding
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BillDetailActivity: AppCompatActivity() {
    lateinit var binding: ActivityBilldetailBinding
    lateinit var userRepo: DataStoreRepoUser
    lateinit var context: Context
    var retrofit = RetrofitClientInstance.client
    var endpoint = retrofit?.create(getBill::class.java)
    var detailresponse: billDetailResponse? = billDetailResponse()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityBilldetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        userRepo = DataStoreRepoUser(dataStore)
        context = this

        val id = intent.getStringExtra("itemid")
        val category = intent.getStringExtra("category")
        getdetail(id!!)

        var accessToken = ""
        runBlocking {
            accessToken = userRepo.userAccessReadFlow.first().toString()
        }
        endpoint!!.getBill("Bearer $accessToken", id).enqueue(object : Callback<billDetailResponse> {
            override fun onResponse(
                call: Call<billDetailResponse>,
                response: Response<billDetailResponse>
            ) {
                detailresponse = response.body()
                if (response.isSuccessful) {
                    binding.category.text = category
                    binding.duedate.text = detailresponse!!.data.bill_payment_date.replace("-", ".")
                    binding.memo.text = detailresponse!!.data.bill_memo
                    binding.billamount.text = digitprocessing(detailresponse!!.data.bill_payment_amount) + "원"
                } else {
                    binding.category.text = category
                    Toast.makeText(context, "실패", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<billDetailResponse>, t: Throwable) {
                Toast.makeText(context, "연결 실패", Toast.LENGTH_SHORT)
            }
        })

        binding.backbtn.setOnClickListener {
            val backintent = Intent(this, BillManagerListActivity::class.java)
            backintent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            backintent.putExtra("category", category)
            startActivity(backintent)
            overridePendingTransition(0, 0)
        }

        binding.billimage.setOnClickListener {
            startActivity(Intent(this, BillImageActivity::class.java))
        }
    }

    override fun onBackPressed() {
        val backintent = Intent(this, BillManagerListActivity::class.java)
        val category = intent.getStringExtra("category")
        backintent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        backintent.putExtra("category", category)
        startActivity(backintent)
        overridePendingTransition(0, 0)
    }

    private fun getdetail(id: String) {
    }

    private fun digitprocessing(digits: String): String {
        var textlength = digits.length
        var processed = ""
        while (0 < textlength) {
            var substring1 = ""
            if (textlength == 3) {
                if (processed == "") {
                    processed = digits.substring(0 until 3)
                } else {
                    processed = digits.substring(0 until 3) + "," + processed
                }
            } else if (textlength > 3) {
                substring1 = digits.substring(textlength - 3 until textlength)
                if (processed == "") {
                    processed = substring1
                } else {
                    processed = "$substring1,$processed"
                }
            } else {
                substring1 = digits.substring(0 until textlength)
                processed = "$substring1,$processed"
            }

            textlength -= 3
        }

        return processed
    }
}