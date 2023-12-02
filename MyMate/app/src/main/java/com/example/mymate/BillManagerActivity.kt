package com.example.mymate

import android.content.Intent
import android.graphics.Typeface
import android.os.Build
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.TypefaceSpan
import android.util.Log
import android.view.ContextMenu
import android.view.MenuItem
import android.view.View
import android.widget.PopupMenu
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isGone
import androidx.datastore.core.DataStore
import com.example.mymate.databinding.ActivityBillmanagerBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

class BillManagerActivity: AppCompatActivity() {
    lateinit var binding: ActivityBillmanagerBinding
    lateinit var userRepo: DataStoreRepoUser

    private var clicked = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userRepo = DataStoreRepoUser(dataStore)
        binding = ActivityBillmanagerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var managerlist = Intent(this, BillManagerListActivity::class.java)

        var retrofit = RetrofitClientInstance.client
        var endpoint = retrofit?.create(getBillCategory::class.java)
        val suitBoldTypeface = Typeface.create(ResourcesCompat.getFont(this, R.font.suit_bold), Typeface.NORMAL)

        var categoryResponse = billCategoryResponse()
        var accessToken = ""
        runBlocking {
            accessToken = userRepo.userAccessReadFlow.first().toString()
        }
        endpoint!!.getBillCategory("Bearer $accessToken").enqueue(object : Callback<billCategoryResponse> {
            @RequiresApi(Build.VERSION_CODES.P)
            override fun onResponse(
                call: Call<billCategoryResponse>,
                response: Response<billCategoryResponse>
            ) {
                categoryResponse = response.body()!!
                if (categoryResponse.data.recent_bill_category.isNotEmpty()) {
                    for (i in 0 until categoryResponse.data.recent_bill_category.size) {
                        val datelist = categoryResponse.data.recent_bill_category[i].bill_payment_date.split("-")
                        val date = "납부기한 " + datelist[1] + "." + datelist[2]
                        val amount = SpannableStringBuilder(digitprocessing(categoryResponse.data.recent_bill_category[i].bill_payment_amount) + "원")
                        amount.setSpan(TypefaceSpan(suitBoldTypeface), amount.lastIndex, amount.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                        if (categoryResponse.data.recent_bill_category[i].bill_category == "전기") {
                            binding.electronicdaytext.text = date
                            binding.electronicbilltext.text = amount
                        } else if (categoryResponse.data.recent_bill_category[i].bill_category == "수도") {
                            binding.waterbilltext.text = amount
                            binding.waterdaytext.text = date
                        } else if (categoryResponse.data.recent_bill_category[i].bill_category == "가스") {
                            binding.gasbilltext.text = amount
                            binding.gasdaytext.text = date
                        } else if (categoryResponse.data.recent_bill_category[i].bill_category == "기타") {
                            binding.etcbilltext.text = amount
                            binding.etcdaytext.text = date
                        }
                    }
                }
            }

            override fun onFailure(call: Call<billCategoryResponse>, t: Throwable) {
                Toast.makeText(applicationContext, "연결 실패(고지서 카테고리)", Toast.LENGTH_SHORT).show()
            }

        })

        binding.backbtn.setOnClickListener {
            finish()
            overridePendingTransition(R.anim.none, R.anim.left_exit)
        }
        binding.gasview.setOnClickListener {
            managerlist.putExtra("category", "도시가스")
            startActivity(managerlist)
        }
        binding.electronicview.setOnClickListener {
            managerlist.putExtra("category", "전기")
            startActivity(managerlist)
        }
        binding.waterview.setOnClickListener {
            managerlist.putExtra("category", "수도")
            startActivity(managerlist)
        }
        binding.etcview.setOnClickListener {
            managerlist.putExtra("category", "기타")
            startActivity(managerlist)
        }

        binding.addbtn.setOnClickListener {
            var popupMenu = PopupMenu(this, it)
            menuInflater?.inflate(R.menu.contextmenu_bill, popupMenu.menu)
            popupMenu.show()
            popupMenu.setOnMenuItemClickListener {
                if (it.title == "사진 스캔") {
                    val modaleView = BillManagerModaleFragment()
                    modaleView.show(supportFragmentManager, modaleView.tag)
                    return@setOnMenuItemClickListener true
                } else {
                    val modaleView = BillManagerModaleAddFragment()
                    modaleView.show(supportFragmentManager, modaleView.tag)
                }
                return@setOnMenuItemClickListener false
            }
        }
    }

    override fun onBackPressed() {
        finish()
        overridePendingTransition(R.anim.none, R.anim.left_exit)
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