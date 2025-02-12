package com.example.mymate

import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.os.Build
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.AbsoluteSizeSpan
import android.text.style.ForegroundColorSpan
import android.text.style.TypefaceSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isGone
import androidx.fragment.app.Fragment
import com.example.mymate.data.dto.auth.response.LocalRefreshResponse
import com.example.mymate.data.dto.expense.response.HomeInfoResponse
import com.example.mymate.data.dto.notification.response.UserActNotiResponse
import com.example.mymate.databinding.MainHomeFragmentBinding
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.math.absoluteValue

class MainHomeFragment : Fragment() {
    lateinit var binding: MainHomeFragmentBinding
    lateinit var mainActivity: MainActivity
    lateinit var mGoogleSignInClient: GoogleSignInClient
    lateinit var userRepo: DataStoreRepoUser

    lateinit var refreshcode: String
    lateinit var accesscode: String

    private var refreshResponse: LocalRefreshResponse = LocalRefreshResponse()
    var resumed = "00"

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as MainActivity
    }

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = MainHomeFragmentBinding.inflate(inflater, container, false)
        val alarmIntent = Intent(mainActivity, AlarmActivity::class.java)
        binding.alarmbutton.setOnClickListener{
            startActivity(alarmIntent)
            mainActivity.overridePendingTransition(R.anim.right_enter, R.anim.none)
        }

        // binding.logoimage.isGone = true
        /* val templogin = binding.logoimage
        templogin.setOnClickListener {
            UserApiClient.instance.logout { error -> //카카오 로그아웃
                if (error != null) {
                    Log.e(TAG, "로그아웃 실패. SDK에서 토큰 삭제됨", error)
                }
                else {
                    Log.i(TAG, "로그아웃 성공. SDK에서 토큰 삭제됨")
                }
            }
            val account = GoogleSignIn.getLastSignedInAccount(mainActivity)
            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestProfile()
                .requestServerAuthCode(getString(R.string.google_web_client_id))
                .requestIdToken(getString(R.string.google_web_client_id))
                .build()
            mGoogleSignInClient = GoogleSignIn.getClient(mainActivity, gso)
            account?.let {
                Log.i(TAG, "Logged In")
                mGoogleSignInClient.signOut() //구글 로그아웃
            } ?: Log.i(TAG, "Not Yet Logged In")
            try {
                NaverIdLoginSDK.logout()
                Log.i(TAG, "로그아웃 성공, SDK에서 네이버 토큰 삭제됨")
            } finally {
                Log.i(TAG, "로그아웃 실패, SDK에 네이버 토큰 없음")
            } // 네이버 로그아웃
            startActivity(Intent(mainActivity, LoginActivity::class.java))
        } */ //로그아웃 코드. 사용하지 않으나 추후 구현 시 사용하기 위해 주석으로 보존함.

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        callServer(mainActivity)
        resumed = "01"
    }

    private fun callServer(context: Context) {
        mainActivity = context as MainActivity
        var retrofit = RetrofitClientInstance.client
        var endpoint = retrofit?.create(getHomeInfo::class.java)
        userRepo = DataStoreRepoUser(mainActivity.dataStore)
        var accessToken = ""
        runBlocking {
            accessToken = userRepo.userAccessReadFlow.first().toString()
        }
        endpoint!!.getHomeInfo("Bearer $accessToken").enqueue(object: Callback<HomeInfoResponse> {
            @RequiresApi(Build.VERSION_CODES.P)
            override fun onResponse(
                call: Call<HomeInfoResponse>,
                response: Response<HomeInfoResponse>
            ) {
                if (response.isSuccessful) {
                    var household = response.body()!!.data.household
                    var me = response.body()!!.data.me
                    val montBoldTypeface = Typeface.create(ResourcesCompat.getFont(mainActivity, R.font.montserrat_bold), Typeface.NORMAL)
                    val suitBoldTypeface = Typeface.create(ResourcesCompat.getFont(mainActivity, R.font.suit_bold), Typeface.NORMAL) // 글씨체 적용을 위한 변수 선언

                    val ratio = household.by_now_expense.toInt() / household.by_previous_expense.toInt() // 지난 달 대비 현재 사용량
                    setTopBoxData(household.settlement_d_day, household.by_now_expense)
                    setUntilNowData(household.by_now_budget_ratio, household.is_household_budget_over_warn, montBoldTypeface)
                    setPrevMonthCompareData(household.by_previous_expense, household.by_now_expense, household.now_expense_diff, household.expense_duration)
                    setUserBudgetData(me.user_by_now_left_expense, me.user_total_budget, me.user_by_now_total_expense, suitBoldTypeface)
                    //TODO: 그래프 특수경우 대응
                }
            }

            override fun onFailure(call: Call<HomeInfoResponse>, t: Throwable) {
                Toast.makeText(context, "연결 실패(홈)", Toast.LENGTH_SHORT).show()
            }
        })

        val alarmEndpoint = retrofit?.create(getActivityNoti::class.java)
        alarmEndpoint!!.activityNoti("Bearer $accessToken").enqueue(object: Callback<UserActNotiResponse> {
            override fun onResponse(
                call: Call<UserActNotiResponse>,
                response: Response<UserActNotiResponse>
            ) {
                if (response.isSuccessful) {
                    val notiresponse = response.body()!!.data.activityNotificationResponses
                    binding.alarmnoti.isGone = notiresponse[0].is_read
                }
            }

            override fun onFailure(call: Call<UserActNotiResponse>, t: Throwable) {
                Toast.makeText(context, "연결 실패(홈-알람)", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun setTopBoxData(dDay: String, nowExpense: String) {
        binding.dDay.text = "정산일 D${dDay}"
        binding.nownotitext.text = digitprocessing(nowExpense)
    }

    private fun setUntilNowData(spendRatio: String, isBudgetExcessed: Boolean, montbold: Typeface) {
        var spentPercent = SpannableStringBuilder("지금까지 예산의 ${spendRatio}를 썼어요")
        spentPercent.setSpan(ForegroundColorSpan(ContextCompat.getColor(mainActivity, R.color.purplevivid_buttonline)), 9, 9 + spendRatio.length + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        spentPercent.setSpan(TypefaceSpan(montbold), 9, 9 + spendRatio.length + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        binding.spendnoti.text = spentPercent
        binding.spendgraphguide.setGuidelinePercent((0.06 + 0.88 * (spendRatio.toFloat() / 100)).toFloat())
        binding.spendgraphpercent.text = "${spendRatio}%"
        if (spendRatio.toInt() > 100) {
            binding.graphgraphichead.setImageDrawable(ContextCompat.getDrawable(mainActivity, R.drawable.trangle_red))
            binding.spendgraphpercent.setBackgroundResource(R.drawable.box_radius8_red)
            binding.spendgraphicguide.setGuidelinePercent(0.87f)
            binding.spendgraphguide.setGuidelinePercent(0.94f)
            binding.spendoverguide.setGuidelinePercent((0.94 - 0.88 *((spendRatio.toFloat() - 100f) / 100)).toFloat())
            spentPercent = SpannableStringBuilder("지금까지 예산을 ${spendRatio.toInt() - 100}% 초과했어요")
            spentPercent.setSpan(ForegroundColorSpan(ContextCompat.getColor(mainActivity, R.color.purplevivid_buttonline)), 9, 9 + (spendRatio.toInt() - 100).toString().length+1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            spentPercent.setSpan(TypefaceSpan(montbold), 9, 9 + (spendRatio.toInt() - 100).toString().length+1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        } else {
            binding.graphgraphichead.setImageDrawable(ContextCompat.getDrawable(mainActivity, R.drawable.trangle_purple))
            binding.spendgraphpercent.setBackgroundResource(R.drawable.box_radius8_purple)
            if ((0.06 + 0.88 * (spendRatio.toFloat() / 100)).toFloat() > 0.87f) {
                binding.spendgraphicguide.setGuidelinePercent(0.87f)
                binding.spendoverguide.setGuidelinePercent(0.94f)
            } else if ((0.06 + 0.88 * (spendRatio.toFloat() / 100)).toFloat() < 0.125f) {
                binding.spendgraphicguide.setGuidelinePercent(0.125f)
                binding.spendoverguide.setGuidelinePercent(0.94f)
            } else {
                binding.spendgraphicguide.setGuidelinePercent((0.06 + 0.88 * (spendRatio.toFloat() / 100)).toFloat())
                binding.spendoverguide.setGuidelinePercent(0.94f)
            }
        }
        if (isBudgetExcessed) {
            binding.homestatustxt.text = "이대로라면 예산을 초과할 것 같아요"
        } else {
            binding.homestatustxt.text = "아주 잘 하고 있어요. 이대로만 유지하는 게 좋겠어요!"
        }
    }

    private fun setPrevMonthCompareData(previousExpense: String, nowExpense: String, expenseDiff: String, expenseDuration: String) { // 지난달과의 비교 그래프 세팅
        var compareResultTitle = SpannableStringBuilder("지난 달 대비")
        if (previousExpense.toInt() < nowExpense.toInt()) {
            compareResultTitle = SpannableStringBuilder("지난 달 ${expenseDuration}일간 대비 더 썼어요")
            binding.statusbilltext.setTextColor(ContextCompat.getColor(mainActivity, R.color.red_text))
            binding.statussubtextsmall.setTextColor(ContextCompat.getColor(mainActivity, R.color.red_text))
            binding.presentComparetop.isGone = false
            binding.presentComparebody.setImageDrawable(ContextCompat.getDrawable(mainActivity, R.drawable.box_noradius))
            binding.presentguidetop.setGuidelinePercent(0.185f)
            binding.presentguidemid.setGuidelinePercent(0.365f)
            binding.compareicon.setImageResource(R.drawable.more)
        } else {
            compareResultTitle = SpannableStringBuilder("지난 달 ${expenseDuration}일간 대비 덜 썼어요")
            binding.statusbilltext.setTextColor(ContextCompat.getColor(mainActivity, R.color.pie_green))
            binding.statussubtextsmall.setTextColor(ContextCompat.getColor(mainActivity, R.color.pie_green))
            binding.presentComparetop.isGone = true
            binding.compareicon.setImageResource(R.drawable.less)
            binding.presentComparebody.setImageDrawable(ContextCompat.getDrawable(mainActivity, R.drawable.graph_hometop))
            binding.presentguidemid.setGuidelinePercent((0.365 + 0.235 * (nowExpense.toFloat() / previousExpense.toFloat())).toFloat())
        }
        compareResultTitle.setSpan(ForegroundColorSpan(ContextCompat.getColor(mainActivity, R.color.purplevivid_buttonline)), compareResultTitle.length - 5, compareResultTitle.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        binding.comparebigtxt.text = compareResultTitle
        binding.statusbilltext.text = digitprocessing(expenseDiff.toInt().absoluteValue.toString())
    }

    private fun setUserBudgetData(leftExpense: String, totalBudget: String, spentBudget: String, suitbold: Typeface) {
        val remainingbudget = SpannableStringBuilder("${digitprocessing(leftExpense)}원")
        val totalbudget = SpannableStringBuilder("${digitprocessing(totalBudget)}원")
        remainingbudget.setSpan(TypefaceSpan(suitbold), remainingbudget.length - 1, remainingbudget.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        totalbudget.setSpan(TypefaceSpan(suitbold), totalbudget.length - 1, totalbudget.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        binding.remainingbudget.text = remainingbudget
        binding.totalbudget.text = totalbudget
        val spentpercentfloat = (spentBudget.toFloat() / totalBudget.toFloat() * 100)
        val leftpercentfloat = (leftExpense.toFloat() / totalBudget.toFloat() * 100)
        pieapply(totalBudget.toInt(), spentpercentfloat, leftpercentfloat, spentBudget.toInt())
    }

    private fun pieapply(total: Int, now_left: Float, now_total: Float, realtotal: Int) {
        val montBoldTypeface = Typeface.create(ResourcesCompat.getFont(mainActivity, R.font.montserrat_bold), Typeface.NORMAL)
        val suitBoldTypeface = Typeface.create(ResourcesCompat.getFont(mainActivity, R.font.suit_bold), Typeface.NORMAL)
        val suitSemiBoldTypeface = Typeface.create(ResourcesCompat.getFont(mainActivity, R.font.suit_semibold), Typeface.NORMAL)
        val piemidtext = SpannableStringBuilder("${digitprocessing(realtotal.toString())}원\n오늘까지 썼어요")
        piemidtext.setSpan(AbsoluteSizeSpan(18, true), 0, digitprocessing(realtotal.toString()).length + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        piemidtext.setSpan(AbsoluteSizeSpan(16, true), digitprocessing(realtotal.toString()).length + 2, piemidtext.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        piemidtext.setSpan(TypefaceSpan(montBoldTypeface), 0, digitprocessing(realtotal.toString()).length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        piemidtext.setSpan(TypefaceSpan(suitBoldTypeface), digitprocessing(realtotal.toString()).length, digitprocessing(realtotal.toString()).length + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        piemidtext.setSpan(TypefaceSpan(suitSemiBoldTypeface), digitprocessing(realtotal.toString()).length + 1, piemidtext.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        piemidtext.setSpan(ForegroundColorSpan(ContextCompat.getColor(mainActivity, R.color.purplevivid_buttonline)), 0, digitprocessing(realtotal.toString()).length + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        piemidtext.setSpan(ForegroundColorSpan(ContextCompat.getColor(mainActivity, R.color.black_text)), digitprocessing(realtotal.toString()).length + 1, piemidtext.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        binding.myPieMidText.text = piemidtext
        val pieChart = binding.myPieChart
        pieChart.maxAngle = 180f
        pieChart.setUsePercentValues(true)
        val entries = ArrayList<PieEntry>()
        var spendfornow = (now_left)
        var leftfornow = (now_total)
        if (now_left + now_total >= 100f) {
            spendfornow = 100f
            leftfornow = 0f
        }
        entries.add(PieEntry(spendfornow))
        entries.add(PieEntry(leftfornow))
        val colorItem = ArrayList<Int>()
        colorItem.add(ContextCompat.getColor(mainActivity, R.color.purplevivid_buttonline))
        colorItem.add(ContextCompat.getColor(mainActivity, R.color.white_graphbackground))
        val pieDataSet = PieDataSet(entries, "")
        pieDataSet.apply {
            colors = colorItem
            setDrawValues(false)
        }
        pieChart.apply {
            data = PieData(pieDataSet)
            description.isEnabled = false
            isRotationEnabled = false
            transparentCircleRadius = 0f
            holeRadius = 81f
            setHoleColor(ContextCompat.getColor(mainActivity, android.R.color.transparent))
            legend.isEnabled = false
            rotation = -90f
            setTouchEnabled(false)
        }
        pieChart.invalidate()
    }

    private fun digitprocessing(digits: String): String { // TODO: common 패키지로 뺄 것
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
        callServer(mainActivity)
    }
}