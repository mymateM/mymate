package com.example.mymate

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.mymate.databinding.ActivitySpendingdetailBinding
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.create

class SpendingDetailActivity: AppCompatActivity() {
    lateinit var binding: ActivitySpendingdetailBinding
    lateinit var context: Context
    lateinit var userRepo: DataStoreRepoUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySpendingdetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        context = this
        userRepo = DataStoreRepoUser(dataStore)

        val retrofit = RetrofitClientInstance.client
        val endpoint = retrofit?.create(getDailySingleExpense::class.java)

        binding.backbtn.setOnClickListener {
            finish()
        }

        var accessToken = ""
        runBlocking {
            accessToken = userRepo.userAccessReadFlow.first().toString()
        }
        val id = intent.getStringExtra("id")

        endpoint!!.getDailySingleExpense("Bearer $accessToken", id!!).enqueue(object : Callback<getDailySingleExpenseResult> {
            override fun onResponse(
                call: Call<getDailySingleExpenseResult>,
                response: Response<getDailySingleExpenseResult>
            ) {
                if (response.isSuccessful) {
                    val result = response.body()!!.data
                    binding.category.text = result.expense_category
                    binding.amountText.text = digitprocessing(result.payment_amount.toDouble().toInt().toString())
                    binding.store.text = result.expense_store
                    val date = result.expense_register_date.split("-")
                    val datetext = (date[0].toInt() % 100).toString() + "년 " + date[1].toInt().toString() + "월 " + date[2].toInt().toString() + "일"
                    binding.enrolldate.text = datetext
                    binding.memo.text = result.expense_memo
                }
            }

            override fun onFailure(call: Call<getDailySingleExpenseResult>, t: Throwable) {
                Toast.makeText(context, "연결 실패(내역 상세)", Toast.LENGTH_SHORT).show()
            }
        })

        binding.deletebtn.setOnClickListener {
            val deleteEndpoint = retrofit?.create(deleteExpense::class.java)
            deleteEndpoint!!.deleteExpense("Bearer $accessToken", id).enqueue(object : Callback<Response<Void>> {
                override fun onResponse(
                    call: Call<Response<Void>>,
                    response: Response<Response<Void>>
                ) {
                    finish()
                }

                override fun onFailure(call: Call<Response<Void>>, t: Throwable) {
                    Toast.makeText(context, "연결 실패-내역 삭제",Toast.LENGTH_SHORT).show()
                }

            })
        }
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