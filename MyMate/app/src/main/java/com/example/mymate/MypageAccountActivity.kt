package com.example.mymate

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.os.Build
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.TypefaceSpan
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.example.mymate.databinding.ActivityMypageAccountBinding
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MypageAccountActivity: AppCompatActivity() {
    lateinit var binding: ActivityMypageAccountBinding
    lateinit var context: Context
    lateinit var userRepo: DataStoreRepoUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMypageAccountBinding.inflate(layoutInflater)
        setContentView(binding.root)
        context = this
        userRepo = DataStoreRepoUser(dataStore)

        val montBoldTypeface = Typeface.create(ResourcesCompat.getFont(context, R.font.montserrat_bold), Typeface.NORMAL)
        val suitBoldTypeface = Typeface.create(ResourcesCompat.getFont(context, R.font.suit_bold), Typeface.NORMAL)

        val retrofit = RetrofitClientInstance.client
        val endpoint = retrofit?.create(getMyAccount::class.java)
        var accessToken = ""
        runBlocking {
            accessToken = userRepo.userAccessReadFlow.first().toString()
        }

        endpoint!!.getMyAccount("Bearer $accessToken").enqueue(object : Callback<myAccountResponse> {
            @RequiresApi(Build.VERSION_CODES.P)
            override fun onResponse(
                call: Call<myAccountResponse>,
                response: Response<myAccountResponse>
            ) {
                if (response.isSuccessful) {
                    val myaccount = response.body()!!.data
                    val banktype = myaccount.account_bank.replace("은행", "")
                    val banknumber = myaccount.account_number
                    val fullspan = SpannableStringBuilder("$banktype $banknumber")
                    fullspan.setSpan(TypefaceSpan(suitBoldTypeface), 0, banktype.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                    fullspan.setSpan(TypefaceSpan(montBoldTypeface), banktype.length, fullspan.lastIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                    binding.account.text = fullspan

                    val adapter = MypageAccountAdapter(myaccount.members)
                    val manager = LinearLayoutManager(context)
                    binding.accountList.layoutManager = manager
                    binding.accountList.adapter = adapter.apply {
                        setOnItemClickListener(object : MypageAccountAdapter.OnItemClickListener {
                            override fun onItemClick(item: mateAccount, position: Int) {
                                val clip = ClipData.newPlainText("account", item.account_bank.replace("은행", "") + " " + item.account_number)
                                val clipboard = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
                                clipboard.setPrimaryClip(clip)
                                val toasttxt = "${item.user_name}의\n계좌를 복사했어요"
                                Toast.makeText(context, toasttxt, Toast.LENGTH_SHORT).show()
                            }
                        })
                    }
                }
            }

            override fun onFailure(call: Call<myAccountResponse>, t: Throwable) {
                Toast.makeText(context, "연결 실패(사용자 계좌)", Toast.LENGTH_SHORT).show()
            }

        })

        binding.back.setOnClickListener {
            finish()
            overridePendingTransition(R.anim.none, R.anim.left_exit)
        }

        binding.accountEdit.setOnClickListener {
            startActivity(Intent(context, MypageEditAccountActivity::class.java))
        }
    }

    override fun onBackPressed() {
        finish()
        overridePendingTransition(R.anim.none, R.anim.left_exit)
    }
}