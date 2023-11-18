package com.example.mymate

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Build
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.text.style.TypefaceSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import com.example.mymate.databinding.MainMypageFragmentBinding
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.create

class MainMypageFragment : Fragment() {
    lateinit var binding: MainMypageFragmentBinding
    lateinit var mainActivity: MainActivity
    lateinit var userRepo: DataStoreRepoUser

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as MainActivity
        userRepo = DataStoreRepoUser(context.dataStore)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = MainMypageFragmentBinding.inflate(inflater, container, false)

        var retrofit = RetrofitClientInstance.client
        var endpoint = retrofit?.create(myPageApi::class.java)
        var accessToken = ""
        runBlocking {
            accessToken = userRepo.userAccessReadFlow.first().toString()
        }

        val montBoldTypeface = Typeface.create(ResourcesCompat.getFont(mainActivity, R.font.montserrat_bold), Typeface.NORMAL)
        val suitBoldTypeface = Typeface.create(ResourcesCompat.getFont(mainActivity, R.font.suit_bold), Typeface.NORMAL)
        val budgetIntent = Intent(mainActivity, MypageBudgetActivity::class.java)

        endpoint!!.myPageApi("Bearer $accessToken").enqueue(object : Callback<myPageApiResponse> {
            override fun onResponse(
                call: Call<myPageApiResponse>,
                response: Response<myPageApiResponse>
            ) {
                if (response.isSuccessful) {
                    val mypagecallback = response.body()!!.data
                    binding.nickname.text = mypagecallback.user_nickname
                    val ratiotext = SpannableStringBuilder(mypagecallback.user_settlement_ratio + "%")
                    ratiotext.setSpan(TypefaceSpan(montBoldTypeface), 0, ratiotext.length - 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                    binding.myratio.text = ratiotext
                    val settledaytext = SpannableStringBuilder(mypagecallback.household_settlement_date.split("-")[2] + "일")
                    settledaytext.setSpan(TypefaceSpan(montBoldTypeface), 0, settledaytext.length - 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                    binding.settleday.text = settledaytext
                    val amounttext = SpannableStringBuilder((mypagecallback.household_budget_amount.toInt() / 10000).toString() + "만원")
                    val budget = digitprocessing(mypagecallback.household_budget_amount) + "원"
                    budgetIntent.putExtra("budget", budget)
                    amounttext.setSpan(TypefaceSpan(montBoldTypeface), 0, amounttext.length - 2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                    binding.budgetamount.text = amounttext
                }
            }

            override fun onFailure(call: Call<myPageApiResponse>, t: Throwable) {
                Toast.makeText(mainActivity, "연결 실패(마이페이지)", Toast.LENGTH_SHORT).show()
            }
        })

        binding.toSetting.setOnClickListener {
            startActivity(Intent(mainActivity, SettingActivity::class.java))
            mainActivity.overridePendingTransition(R.anim.right_enter, R.anim.none)
        }

        binding.profileEdit.setOnClickListener {
            val profileIntent = Intent(mainActivity, MypageEditprofileActivity::class.java)
            profileIntent.putExtra("nickname", binding.nickname.text.toString())
            startActivity(profileIntent)
            mainActivity.overridePendingTransition(R.anim.right_enter, R.anim.none)
        }

        binding.ratiocontainer.setOnClickListener {
            val ratioIntent = Intent(mainActivity, MypageRatiodetailActivity::class.java)
            ratioIntent.putExtra("ratio", binding.myratio.text.toString())
            startActivity(ratioIntent)
            mainActivity.overridePendingTransition(R.anim.right_enter, R.anim.none)
        }

        binding.budgetcontainer.setOnClickListener {
            startActivity(budgetIntent)
            mainActivity.overridePendingTransition(R.anim.right_enter, R.anim.none)
        }

        binding.accountcontainer.setOnClickListener {
            startActivity(Intent(mainActivity, MypageAccountActivity::class.java))
            mainActivity.overridePendingTransition(R.anim.right_enter, R.anim.none)
        }

        return binding.root
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

    override fun onResume() {
        //TODO: refresh data
        super.onResume()
    }
}