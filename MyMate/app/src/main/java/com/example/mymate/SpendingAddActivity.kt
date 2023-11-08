package com.example.mymate

import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.isGone
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mymate.databinding.ActivitySpendingaddBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.create
import java.time.LocalDate
import kotlin.reflect.jvm.internal.impl.descriptors.Visibilities.Local

class SpendingAddActivity: AppCompatActivity() {
    lateinit var binding: ActivitySpendingaddBinding
    lateinit var context: Context
    lateinit var userRepo: DataStoreRepoUser
    lateinit var behavior: BottomSheetBehavior<ConstraintLayout>

    var store: String? = ""
    var category: String? = ""
    var memo: String? = "없음"
    var amount: String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySpendingaddBinding.inflate(layoutInflater)
        setContentView(binding.root)
        userRepo = DataStoreRepoUser(dataStore)
        context = this

        val enrolldate = "${LocalDate.now().year % 100}년 ${LocalDate.now().monthValue}월 ${LocalDate.now().dayOfMonth}일"

        binding.enrolldate.text = enrolldate

        binding.backbtn.setOnClickListener {
            finish()
        }

        binding.storeEdit.setOnClickListener {
            val storeintent = Intent(context, SpendingAddStoreActivity::class.java)
            if (binding.store.text != "없음") {
                storeintent.putExtra("store", binding.store.text)
            }
            if (binding.category.text != "없음") {
                storeintent.putExtra("category", binding.category.text)
            }
            if (binding.memo.text != "없음") {
                storeintent.putExtra("memo", binding.memo.text)
            }
            if (binding.amountEdit.text.isNotEmpty()) {
                storeintent.putExtra("amount", binding.amountEdit.text.toString())
            }
            startActivityForResult(storeintent, 98)
        }

        binding.memoEdit.setOnClickListener {
            val memointent = Intent(context, SpendingAddMemoActivity::class.java)
            if (binding.store.text != "없음") {
                memointent.putExtra("store", binding.store.text)
            }
            if (binding.category.text != "없음") {
                memointent.putExtra("category", binding.category.text)
            }
            if (binding.memo.text != "없음") {
                memointent.putExtra("memo", binding.memo.text)
            }
            if (binding.amountEdit.text.isNotEmpty()) {
                memointent.putExtra("amount", binding.amountEdit.text.toString())
            }
            startActivityForResult(memointent, 99)
        }

        binding.completedbtn.setOnClickListener {
            pushspending()
        }

        binding.bottomlayout.setOnClickListener {
            hidekeyboard()
        }

        modaleCategoryInit()
        setCategoryView()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            if (requestCode == 99) {
                memo = data?.getStringExtra("memo")
                if (!memo.isNullOrBlank()) {
                    binding.memo.text = memo
                    binding.memo.setTextColor(ContextCompat.getColor(context, R.color.black_text))
                } else {
                    binding.memo.text = "없음"
                    binding.memo.setTextColor(ContextCompat.getColor(context, R.color.graydark_text))
                }
            } else if (requestCode == 98) {
                store = data?.getStringExtra("store")

                if (!store.isNullOrBlank()) {
                    binding.store.text = store
                    binding.store.setTextColor(ContextCompat.getColor(context, R.color.black_text))
                } else {
                    binding.store.text = "없음"
                    binding.store.setTextColor(ContextCompat.getColor(context, R.color.graydark_text))
                }
            }
        }
    }

    private fun hidekeyboard() {
        //키보드 내리기
        val imm = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        val focusview = currentFocus
        imm.hideSoftInputFromWindow(this.window.decorView.applicationWindowToken, 0)
        focusview?.clearFocus()
    }

    private fun pushspending() {
        var retrofit = RetrofitClientInstance.client
        var endpoint = retrofit?.create(putDailyExpense::class.java)
        var memberendpoint = retrofit?.create(getMemberId::class.java)
        var sending: expensetosend = expensetosend()
        var id: getMemberIdResponse = getMemberIdResponse()
        var accessToken = ""
        runBlocking {
            accessToken = userRepo.userAccessReadFlow.first().toString()
        }
        memberendpoint!!.getMemberId("Bearer $accessToken").enqueue(object : Callback<getMemberIdResponse> {
            override fun onResponse(
                call: Call<getMemberIdResponse>,
                response: Response<getMemberIdResponse>
            ) {
                if (response.isSuccessful) {
                    id = response.body()!!
                    sending.settlementSubjectIds = id.data
                    Log.d("sendingIDS", id.data.size.toString())
                    sending.expenseAmount = binding.amountEdit.text.toString()
                    sending.expenseMemo = binding.memo.text.toString()
                    sending.expenseStore = binding.store.text.toString()
                    when (binding.category.text) {
                        "식비" -> sending.expenseCategory = "FOOD"
                        "쇼핑" -> sending.expenseCategory = "SHOPPING"
                        "교통" -> sending.expenseCategory = "TRANSPORT"
                        "의료" -> sending.expenseCategory = "MEDICAL"
                        "생활" -> sending.expenseCategory = "HOUSEITEM"
                        "교육" -> sending.expenseCategory = "EDUCATION"
                        "기타" -> sending.expenseCategory = "ETC"
                    }

                    val month = LocalDate.now().monthValue
                    val today = LocalDate.now().dayOfMonth

                    if (month < 10 && today < 10) {
                        sending.expenseDate = "${LocalDate.now().year}-0${month}-0${today}"
                    } else if (month < 10) {
                        sending.expenseDate = "${LocalDate.now().year}-0${month}-${today}"
                    } else if (today < 10) {
                        sending.expenseDate = "${LocalDate.now().year}-${month}-0${today}"
                    } else {
                        sending.expenseDate = "${LocalDate.now().year}-${month}-${today}"
                    }
                    endpoint!!.putDailyExpense("Bearer $accessToken", sending).enqueue(object : Callback<postbillResponse> {
                        override fun onResponse(
                            call: Call<postbillResponse>,
                            response: Response<postbillResponse>
                        ) {
                            if (response.isSuccessful) {
                                finish()
                            }
                        }

                        override fun onFailure(call: Call<postbillResponse>, t: Throwable) {
                            Toast.makeText(context, "연결 실패(내역 추가)", Toast.LENGTH_SHORT).show()
                        }
                    })
                }
            }

            override fun onFailure(call: Call<getMemberIdResponse>, t: Throwable) {
                Toast.makeText(context, "연결 실패(멤버 id)", Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun modaleCategoryInit() {
        behavior = BottomSheetBehavior.from(binding.categorymodale.root)
        behavior.state = BottomSheetBehavior.STATE_COLLAPSED
        binding.cover.isGone = true

        binding.categoryEdit.setOnClickListener {
            behavior.state = BottomSheetBehavior.STATE_EXPANDED
            binding.cover.isGone = false
        }
        binding.cover.setOnClickListener {
            behavior.state = BottomSheetBehavior.STATE_COLLAPSED
            binding.cover.isGone = true
        }
        binding.categorymodale.categoryset.setOnClickListener {
            binding.cover.isGone = true
            behavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }
    }

    private fun setCategoryView() {
        val defaultimgList = arrayListOf<Drawable>()
        defaultimgList.add(ContextCompat.getDrawable(context, R.drawable.icon_food_default)!!)
        defaultimgList.add(ContextCompat.getDrawable(context, R.drawable.icon_life_default)!!)
        defaultimgList.add(ContextCompat.getDrawable(context, R.drawable.icon_shopping_default)!!)
        defaultimgList.add(ContextCompat.getDrawable(context, R.drawable.icon_traffic_default)!!)
        defaultimgList.add(ContextCompat.getDrawable(context, R.drawable.icon_medical_default)!!)
        defaultimgList.add(ContextCompat.getDrawable(context, R.drawable.icon_edu_default)!!)
        defaultimgList.add(ContextCompat.getDrawable(context, R.drawable.icon_etc_default)!!)

        val selectedimgList = arrayListOf<Drawable>()
        selectedimgList.add(ContextCompat.getDrawable(context, R.drawable.icon_food_select)!!)
        selectedimgList.add(ContextCompat.getDrawable(context, R.drawable.icon_life_select)!!)
        selectedimgList.add(ContextCompat.getDrawable(context, R.drawable.icon_shopping_select)!!)
        selectedimgList.add(ContextCompat.getDrawable(context, R.drawable.icon_traffic_select)!!)
        selectedimgList.add(ContextCompat.getDrawable(context, R.drawable.icon_medical_select)!!)
        selectedimgList.add(ContextCompat.getDrawable(context, R.drawable.icon_edu_select)!!)
        selectedimgList.add(ContextCompat.getDrawable(context, R.drawable.icon_etc_select)!!)

        val categorynameList = arrayListOf<String>()
        categorynameList.add("식비")
        categorynameList.add("생활")
        categorynameList.add("쇼핑")
        categorynameList.add("교통")
        categorynameList.add("의료")
        categorynameList.add("고지서")
        categorynameList.add("교육")
        categorynameList.add("기타")

        var tagList = arrayListOf<Boolean>()
        for (i in 1 .. 8) {
            tagList.add(false)
        }
        val dataList = arrayListOf<Boolean>()
        for (i in 1 .. 8) {
            dataList.add(false)
        }
        var dataname = arrayListOf<String>()
        for (i in 1 .. 8) {
            dataname.add("")
        }
        val adapter = CategoryAdapter(context, defaultimgList, categorynameList, tagList, selectedimgList)
        val manager: RecyclerView.LayoutManager = GridLayoutManager(context, 3)

        var data = ""

        binding.categorymodale.categorylist.itemAnimator = null
        binding.categorymodale.categorylist.layoutManager = manager
        binding.categorymodale.categorylist.adapter = adapter.apply {
            setOnItemClickListener(object : CategoryAdapter.OnItemClickListener {
                override fun onItemClick(position: Int) {
                    if (dataList[position]) {
                        dataList[position] = false
                        data = ""
                        binding.category.text = "없음"
                        binding.category.setTextColor(ContextCompat.getColor(context, R.color.graydark_text))
                    } else {
                        for (i in 0 until dataList.size) {
                            dataList[i] = false
                        }
                        dataList[position] = true
                        data = categorynameList[position]
                        binding.category.text = data
                        binding.category.setTextColor(ContextCompat.getColor(context, R.color.black_text))
                    }
                }
            })
        }
    }
}