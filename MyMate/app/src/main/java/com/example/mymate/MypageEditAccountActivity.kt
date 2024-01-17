package com.example.mymate

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.TypefaceSpan
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.TextView.OnEditorActionListener
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.isGone
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mymate.databinding.ActivityMypageEditaccountBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MypageEditAccountActivity: AppCompatActivity() {
    lateinit var binding: ActivityMypageEditaccountBinding
    lateinit var userRepo: DataStoreRepoUser
    lateinit var context: Context
    lateinit var behavior: BottomSheetBehavior<ConstraintLayout>
    private val imgList = ArrayList<Drawable>()
    private val nameList = ArrayList<String>()
    private val codeList = ArrayList<String>()
    private var positiontosend = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMypageEditaccountBinding.inflate(layoutInflater)
        setContentView(binding.root)
        userRepo = DataStoreRepoUser(dataStore)
        context = this

        binding.cover.isGone = true

        val retrofit = RetrofitClientInstance.client
        val endpoint = retrofit?.create(getMyAccount::class.java)
        var accessToken = ""
        runBlocking {
            accessToken = userRepo.userAccessReadFlow.first().toString()
        }

        endpoint!!.getMyAccount("Bearer $accessToken").enqueue(object :
            Callback<myAccountResponse> {
            @RequiresApi(Build.VERSION_CODES.P)
            override fun onResponse(
                call: Call<myAccountResponse>,
                response: Response<myAccountResponse>
            ) {
                if (response.isSuccessful) {
                    val myaccount = response.body()!!.data
                    val banktype = myaccount.account_bank
                    val banknumber = myaccount.account_number
                    binding.accountEdit.text = Editable.Factory.getInstance().newEditable(banknumber)
                    binding.bankName.text = banktype
                }
            }

            override fun onFailure(call: Call<myAccountResponse>, t: Throwable) {
                Toast.makeText(context, "연결 실패(사용자 계좌)", Toast.LENGTH_SHORT).show()
            }
        })

        binding.backbtn.setOnClickListener {
            finish()
        }

        binding.accountEdit.setOnEditorActionListener(object: OnEditorActionListener {
            override fun onEditorAction(p0: TextView?, p1: Int, p2: KeyEvent?): Boolean {
                if (p1 == EditorInfo.IME_ACTION_DONE || p1 == EditorInfo.IME_ACTION_GO) {
                    hidekeyboard()
                }
                return false
            }

        })

        binding.completedbtn.setOnClickListener {
            var account = postAccount()
            if (positiontosend != -1) {
                account = postAccount(codeList[positiontosend], binding.accountEdit.text.toString())
                sendAccount(account, accessToken)
            } else if (binding.accountEdit.text.isNotEmpty()) {
                for (i in 0 until codeList.size) {
                    if (nameList[i].contains(binding.bankName.text.toString().substring(0 until 2))) {
                        account = postAccount(codeList[i], binding.accountEdit.text.toString())
                    }
                }
                sendAccount(account, accessToken)
            } else if (binding.accountEdit.text.isEmpty()) {
                Toast.makeText(context, "계좌를 입력해주세요!", Toast.LENGTH_SHORT).show()
            }
        }

        bottomSheetInit()
    }

    private fun sendAccount(account: postAccount, accessToken: String) {
        Toast.makeText(context, account.account_bank + account.account_number, Toast.LENGTH_SHORT).show()
        val retrofit = RetrofitClientInstance.client
        val completeEndpoint = retrofit?.create(postMyAccount::class.java)
        completeEndpoint!!.postMyAccount("Bearer $accessToken", account).enqueue(object: Callback<Response<Void>> {
            override fun onResponse(
                call: Call<Response<Void>>,
                response: Response<Response<Void>>
            ) {
                finish()
            }

            override fun onFailure(call: Call<Response<Void>>, t: Throwable) {
                Toast.makeText(context, "연결 실패(계좌 갱신)", Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun bottomSheetInit() {
        //persistent bottom sheet 제어
        behavior = BottomSheetBehavior.from(binding.modaleAccount.root)
        behavior.peekHeight = 0
        behavior.isDraggable = false
        behavior.isHideable = true
        behavior.state = BottomSheetBehavior.STATE_COLLAPSED
        binding.cover.isGone = true

        binding.togglemodale.setOnClickListener {
            behavior.state = BottomSheetBehavior.STATE_EXPANDED
            binding.cover.isGone = false
        }

        binding.bankName.setOnClickListener {
            behavior.state = BottomSheetBehavior.STATE_EXPANDED
            binding.cover.isGone = false
        }

        binding.cover.setOnClickListener {
            behavior.state = BottomSheetBehavior.STATE_COLLAPSED
            binding.cover.isGone = true
        }

        binding.root.setOnClickListener {
            hidekeyboard()
        }

        listset()
        val adapter = OnboardingAccountAdapter(imgList, nameList)
        val manager = GridLayoutManager(this, 4)
        binding.modaleAccount.bankTable.layoutManager = manager
        binding.modaleAccount.bankTable.adapter = adapter.apply {
            setOnItemClickListener(object : OnboardingAccountAdapter.OnItemClickListener {
                override fun onItemClick(img: Drawable, name: String, position: Int) {
                    binding.bankName.text = name
                    behavior.state = BottomSheetBehavior.STATE_COLLAPSED
                    binding.cover.isGone = true
                    binding.bankName.setTextColor(ContextCompat.getColor(applicationContext, R.color.black_text))
                    positiontosend = position
                }
            })
        }
    }

    private fun listset() {
        imgList.add(ContextCompat.getDrawable(this, R.drawable.bank_busan_gyungnam)!!)
        imgList.add(ContextCompat.getDrawable(this, R.drawable.bank_kb)!!)
        imgList.add(ContextCompat.getDrawable(this, R.drawable.bank_ibk)!!)
        imgList.add(ContextCompat.getDrawable(this, R.drawable.bank_nh)!!)
        imgList.add(ContextCompat.getDrawable(this, R.drawable.bank_daegu)!!)
        imgList.add(ContextCompat.getDrawable(this, R.drawable.bank_citi)!!)
        imgList.add(ContextCompat.getDrawable(this, R.drawable.bank_shinhan)!!)
        imgList.add(ContextCompat.getDrawable(this, R.drawable.bank_woori)!!)
        imgList.add(ContextCompat.getDrawable(this, R.drawable.bank_woochaeguk)!!)
        imgList.add(ContextCompat.getDrawable(this, R.drawable.bank_sc)!!)
        imgList.add(ContextCompat.getDrawable(this, R.drawable.bank_jeju)!!)
        imgList.add(ContextCompat.getDrawable(this, R.drawable.bank_kakao)!!)
        imgList.add(ContextCompat.getDrawable(this, R.drawable.bank_toss)!!)
        imgList.add(ContextCompat.getDrawable(this, R.drawable.bank_hana)!!)

        nameList.add("경남")
        nameList.add("KB국민")
        nameList.add("IBK기업")
        nameList.add("NH농협")
        nameList.add("대구")
        nameList.add("씨티")
        nameList.add("신한")
        nameList.add("우리")
        nameList.add("우체국")
        nameList.add("SC제일")
        nameList.add("제주")
        nameList.add("카카오")
        nameList.add("토스")
        nameList.add("하나")

        codeList.add("BNK")
        codeList.add("KB")
        codeList.add("IBK")
        codeList.add("NH")
        codeList.add("DGB")
        codeList.add("CITI")
        codeList.add("SINHAN")
        codeList.add("WOORI")
        codeList.add("POST")
        codeList.add("SC")
        codeList.add("JEJU")
        codeList.add("KAKAO")
        codeList.add("TOSS")
        codeList.add("HANA")
    }

    private fun hidekeyboard() {
        //키보드 내리기
        val imm = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        val focusview = currentFocus
        imm.hideSoftInputFromWindow(this.window.decorView.applicationWindowToken, 0)
        focusview?.clearFocus()
    }

    override fun onResume() {
        super.onResume()

        val retrofit = RetrofitClientInstance.client
        val endpoint = retrofit?.create(getMyAccount::class.java)
        var accessToken = ""
        runBlocking {
            accessToken = userRepo.userAccessReadFlow.first().toString()
        }


        endpoint!!.getMyAccount("Bearer $accessToken").enqueue(object :
            Callback<myAccountResponse> {
            @RequiresApi(Build.VERSION_CODES.P)
            override fun onResponse(
                call: Call<myAccountResponse>,
                response: Response<myAccountResponse>
            ) {
                if (response.isSuccessful) {
                    val myaccount = response.body()!!.data
                    val banktype = myaccount.account_bank
                    val banknumber = myaccount.account_number
                    binding.accountEdit.text = Editable.Factory.getInstance().newEditable(banknumber)
                    binding.bankName.text = banktype
                }
            }

            override fun onFailure(call: Call<myAccountResponse>, t: Throwable) {
                Toast.makeText(context, "연결 실패(사용자 계좌)", Toast.LENGTH_SHORT).show()
            }
        })
    }
}