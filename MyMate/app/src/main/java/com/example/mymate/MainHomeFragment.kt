package com.example.mymate

import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.AbsoluteSizeSpan
import android.text.style.ForegroundColorSpan
import android.text.style.TypefaceSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isGone
import androidx.core.view.isInvisible
import androidx.datastore.dataStore
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.Fragment
import com.example.mymate.databinding.MainHomeFragmentBinding
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.kakao.sdk.user.UserApiClient
import com.navercorp.nid.NaverIdLoginSDK
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectIndexed
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import okhttp3.internal.wait
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.reflect.Type
import kotlin.math.absoluteValue

class MainHomeFragment : Fragment() {
    lateinit var binding: MainHomeFragmentBinding
    lateinit var mainActivity: MainActivity
    lateinit var mGoogleSignInClient: GoogleSignInClient
    lateinit var userRepo: DataStoreRepoUser

    lateinit var refreshcode: String
    lateinit var accesscode: String

    private var refreshResponse: localRefreshReponse = localRefreshReponse()
    var resumed = "00"

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as MainActivity
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
        binding = MainHomeFragmentBinding.inflate(inflater, container, false)
        val alarmIntent = Intent(mainActivity, AlarmActivity::class.java)
        binding.alarmbutton.setOnClickListener{
            startActivity(alarmIntent)
            mainActivity.overridePendingTransition(R.anim.right_enter, R.anim.none)
        }


        val templogin = binding.logoimage
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
        }

        val tempOnboardingFlow = binding.spendpercentbox
        tempOnboardingFlow.setOnClickListener {
            startActivity(Intent(mainActivity, OnboardingTermsActivity::class.java))
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        comms(mainActivity)
        resumed = "01"
    }

    fun comms(context: Context) {
        mainActivity = context as MainActivity
        var retrofit = RetrofitClientInstance.client
        var endpoint = retrofit?.create(getHomeInfo::class.java)
        userRepo = DataStoreRepoUser(mainActivity.dataStore)
        var accessToken = ""
        runBlocking {
            accessToken = userRepo.userAccessReadFlow.first().toString()
        }
        endpoint!!.getHomeInfo("Bearer $accessToken").enqueue(object: Callback<homeInfoResponse> {
            @RequiresApi(Build.VERSION_CODES.P)
            override fun onResponse(
                call: Call<homeInfoResponse>,
                response: Response<homeInfoResponse>
            ) {
                if (response.isSuccessful) {
                    var household = response.body()!!.data.household
                    var me = response.body()!!.data.me

                    val ratio = household.by_now_expense.toInt() / household.by_previous_expense.toInt()
                    var comparebigtext = SpannableStringBuilder("지난 달 대비")
                    binding.dDay.text = "정산일 D${household.settlement_d_day}"
                    val expensetitle = digitprocessing(household.by_now_expense)
                    binding.nownotitext.text = expensetitle
                    val montBoldTypeface = Typeface.create(ResourcesCompat.getFont(mainActivity, R.font.montserrat_bold), Typeface.NORMAL)
                    val suitBoldTypeface = Typeface.create(ResourcesCompat.getFont(mainActivity, R.font.suit_bold), Typeface.NORMAL)
                    if (household.by_previous_expense.toInt() < household.by_now_expense.toInt()) {
                        comparebigtext = SpannableStringBuilder("지난 달 ${household.expense_duration}일간 대비 더 썼어요")
                        binding.statusbilltext.setTextColor(ContextCompat.getColor(mainActivity, R.color.red_text))
                        binding.statussubtextsmall.setTextColor(ContextCompat.getColor(mainActivity, R.color.red_text))
                        binding.presentComparetop.isGone = false
                        binding.presentComparebody.setImageDrawable(ContextCompat.getDrawable(mainActivity, R.drawable.box_noradius))
                        binding.presentguidetop.setGuidelinePercent(0.185f)
                        binding.presentguidemid.setGuidelinePercent(0.365f)
                        binding.compareicon.setImageResource(R.drawable.more)
                    } else {
                        comparebigtext = SpannableStringBuilder("지난 달 ${household.expense_duration}일간 대비 덜 썼어요")
                        binding.statusbilltext.setTextColor(ContextCompat.getColor(mainActivity, R.color.pie_green))
                        binding.statussubtextsmall.setTextColor(ContextCompat.getColor(mainActivity, R.color.pie_green))
                        binding.presentComparetop.isGone = true
                        binding.compareicon.setImageResource(R.drawable.less)
                        binding.presentComparebody.setImageDrawable(ContextCompat.getDrawable(mainActivity, R.drawable.graph_hometop))
                        binding.presentguidemid.setGuidelinePercent((0.365 + 0.235 * (household.by_now_expense.toFloat() / household.by_previous_expense.toFloat())).toFloat())
                    }
                    comparebigtext.setSpan(ForegroundColorSpan(ContextCompat.getColor(mainActivity, R.color.purplevivid_buttonline)), comparebigtext.length - 5, comparebigtext.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                    binding.comparebigtxt.text = comparebigtext
                    binding.statusbilltext.text = digitprocessing(household.now_expense_diff.toInt().absoluteValue.toString())
                    var spendpercent = SpannableStringBuilder("지금까지 예산의 ${household.by_now_budget_ratio}%를 썼어요")
                    spendpercent.setSpan(ForegroundColorSpan(ContextCompat.getColor(mainActivity, R.color.purplevivid_buttonline)), 9, 9 + household.by_now_budget_ratio.length + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                    spendpercent.setSpan(TypefaceSpan(montBoldTypeface), 9, 9 + household.by_now_budget_ratio.length + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                    binding.spendnoti.text = spendpercent
                    binding.spendgraphguide.setGuidelinePercent((0.06 + 0.88 * (household.by_now_budget_ratio.toFloat() / 100)).toFloat())
                    binding.spendgraphpercent.text = "${household.by_now_budget_ratio}%"
                    if (household.by_now_budget_ratio.toInt() > 100) {
                        binding.spendgraphicguide.setGuidelinePercent(0.87f)
                        binding.spendgraphguide.setGuidelinePercent(0.94f)
                        binding.spendoverguide.setGuidelinePercent((0.94 - 0.88 * ((household.by_now_budget_ratio.toFloat() - 100f) / 100)).toFloat())
                        binding.graphgraphichead.setImageDrawable(ContextCompat.getDrawable(mainActivity, R.drawable.trangle_red))
                        binding.spendgraphpercent.setBackgroundResource(R.drawable.box_radius8_red)
                        spendpercent = SpannableStringBuilder("지금까지 예산을 ${household.by_now_budget_ratio.toInt() - 100}% 초과했어요")
                        spendpercent.setSpan(ForegroundColorSpan(ContextCompat.getColor(mainActivity, R.color.purplevivid_buttonline)), 9, 9 + (household.by_now_budget_ratio.toInt() - 100).toString().length + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                        spendpercent.setSpan(TypefaceSpan(montBoldTypeface), 9, 9 + (household.by_now_budget_ratio.toInt() - 100).toString().length + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

                    } else if ((0.06 + 0.88 * (household.by_now_budget_ratio.toFloat() / 100)).toFloat() > 0.87f) {
                        binding.spendgraphicguide.setGuidelinePercent(0.87f)
                        binding.spendoverguide.setGuidelinePercent(0.94f)
                        binding.graphgraphichead.setImageDrawable(ContextCompat.getDrawable(mainActivity, R.drawable.trangle_purple))
                        binding.spendgraphpercent.setBackgroundResource(R.drawable.box_radius8_purple)
                    } else if ((0.06 + 0.88 * (household.by_now_budget_ratio.toFloat() / 100)).toFloat() < 0.125f) {
                        binding.spendgraphicguide.setGuidelinePercent(0.125f)
                        binding.spendoverguide.setGuidelinePercent(0.94f)
                        binding.graphgraphichead.setImageDrawable(ContextCompat.getDrawable(mainActivity, R.drawable.trangle_purple))
                        binding.spendgraphpercent.setBackgroundResource(R.drawable.box_radius8_purple)
                    } else {
                        binding.spendgraphicguide.setGuidelinePercent((0.06 + 0.88 * (household.by_now_budget_ratio.toFloat() / 100)).toFloat())
                        binding.spendoverguide.setGuidelinePercent(0.94f)
                        binding.graphgraphichead.setImageDrawable(ContextCompat.getDrawable(mainActivity, R.drawable.trangle_purple))
                        binding.spendgraphpercent.setBackgroundResource(R.drawable.box_radius8_purple)
                    }
                    if (household.is_household_budget_over_warn) {
                        binding.homestatustxt.text = "이대로라면 예산을 초과할 것 같아요"
                    } else {
                        binding.homestatustxt.text = "아주 잘 하고 있어요. 이대로만 유지하는 게 좋겠어요!"
                    }
                    val remainingbudget = SpannableStringBuilder("${digitprocessing(me.user_by_now_left_expense)}원")
                    val totalbudget = SpannableStringBuilder("${digitprocessing(me.user_total_budget)}원")
                    remainingbudget.setSpan(TypefaceSpan(suitBoldTypeface), remainingbudget.length - 1, remainingbudget.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                    totalbudget.setSpan(TypefaceSpan(suitBoldTypeface), totalbudget.length - 1, totalbudget.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                    binding.remainingbudget.text = remainingbudget
                    binding.totalbudget.text = totalbudget
                    val spentpercentfloat = (me.user_by_now_total_expense.toFloat() / me.user_total_budget.toFloat() * 100)
                    val leftpercentfloat = (me.user_by_now_left_expense.toFloat() / me.user_total_budget.toFloat() * 100)
                    pieapply(me.user_total_budget.toInt(), spentpercentfloat, leftpercentfloat, me.user_by_now_total_expense.toInt())
                    //TODO: 그래프 특수경우 대응
                }
            }

            override fun onFailure(call: Call<homeInfoResponse>, t: Throwable) {
                Toast.makeText(context, "연결 실패(홈)", Toast.LENGTH_SHORT).show()
            }
        })

        val alarmEndpoint = retrofit?.create(getActivityNoti::class.java)
        alarmEndpoint!!.activityNoti("Bearer $accessToken").enqueue(object: Callback<activityNotiResponse> {
            override fun onResponse(
                call: Call<activityNotiResponse>,
                response: Response<activityNotiResponse>
            ) {
                if (response.isSuccessful) {
                    val notiresponse = response.body()!!.data.activityNotificationResponses
                    binding.alarmnoti.isGone = notiresponse[0].is_read
                }
            }

            override fun onFailure(call: Call<activityNotiResponse>, t: Throwable) {
                Toast.makeText(context, "연결 실패(홈-알람)", Toast.LENGTH_SHORT).show()
            }
        })
    }

    @RequiresApi(Build.VERSION_CODES.P)
    private fun pieapply(total: Int, now_left: Float, now_total: Float, realtotal: Int) {
        val montBoldTypeface = Typeface.create(ResourcesCompat.getFont(mainActivity, R.font.montserrat_bold), Typeface.NORMAL)
        val suitMediumTypeface = Typeface.create(ResourcesCompat.getFont(mainActivity, R.font.suit_medium), Typeface.NORMAL)
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

        comms(mainActivity)
    }
}