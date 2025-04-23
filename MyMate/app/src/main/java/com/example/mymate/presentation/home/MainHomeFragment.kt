package com.example.mymate.presentation.home

import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.icu.text.DecimalFormat
import android.os.Build
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.AbsoluteSizeSpan
import android.text.style.ForegroundColorSpan
import android.text.style.TypefaceSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isGone
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.mymate.presentation.alarm.AlarmActivity
import com.example.mymate.DataStoreRepoUser
import com.example.mymate.R
import com.example.mymate.data.dto.auth.response.LocalRefreshResponse
import com.example.mymate.databinding.MainHomeFragmentBinding
import com.example.mymate.presentation.main.MainActivity
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.google.android.gms.auth.api.signin.GoogleSignInClient

class MainHomeFragment : Fragment() {
    lateinit var mainActivity: MainActivity
    lateinit var mGoogleSignInClient: GoogleSignInClient
    lateinit var userRepo: DataStoreRepoUser

    lateinit var refreshcode: String
    lateinit var accesscode: String

    private var refreshResponse: LocalRefreshResponse = LocalRefreshResponse()
    var resumed = "00"

    private var _binding: MainHomeFragmentBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MainHomeViewModel by viewModels()

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
        initBinding(inflater, container)
        initAlarm()
        initGraph()
        initTextSpan()
        initPieChart()
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
        resumed = "01"
    }

    override fun onResume() {
        //TODO: refresh data
        super.onResume()
        viewModel.getHomeInfo()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initBinding(inflater: LayoutInflater, container: ViewGroup?) {
        _binding = MainHomeFragmentBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.vm = viewModel
    }

    private fun initAlarm() {
        viewModel.isBadgeGone.observe(viewLifecycleOwner) {
            binding.alarmnoti.isGone = it
        }
    }

    private fun initGraph() {
        viewModel.expenseGraphGuide.observe(viewLifecycleOwner) {
            binding.spendgraphguide.setGuidelinePercent(it.toFloat())
        }

        viewModel.expenseOverGuide.observe(viewLifecycleOwner) {
            binding.spendoverguide.setGuidelinePercent(it.toFloat())
        }

        viewModel.expenseIndicatorGuide.observe(viewLifecycleOwner) {
            binding.spendgraphicguide.setGuidelinePercent(it.toFloat())
        }

        viewModel.isHouseholdBudgetOver.observe(viewLifecycleOwner) {
            if (it) {
                binding.graphgraphichead.setImageDrawable(ContextCompat.getDrawable(mainActivity,
                    R.drawable.trangle_red
                ))
                binding.spendgraphpercent.setBackgroundResource(R.drawable.box_radius8_red)
            } else {
                binding.graphgraphichead.setImageDrawable(ContextCompat.getDrawable(mainActivity,
                    R.drawable.trangle_purple
                ))
                binding.spendgraphpercent.setBackgroundResource(R.drawable.box_radius8_purple)
            }
        }

        viewModel.compareGuideMid.observe(viewLifecycleOwner) {
            binding.presentguidemid.setGuidelinePercent(it.toFloat())
        }

        viewModel.compareGuideTop.observe(viewLifecycleOwner) {
            binding.presentguidetop.setGuidelinePercent(it.toFloat())
        }

        viewModel.isNowBetter.observe(viewLifecycleOwner) {
            binding.presentComparetop.isGone = !it
        }
    }

    private fun initTextSpan() {
        val montBoldTypeface = Typeface.create(ResourcesCompat.getFont(mainActivity, R.font.montserrat_bold), Typeface.NORMAL)
        val suitBoldTypeface = Typeface.create(ResourcesCompat.getFont(mainActivity, R.font.suit_bold), Typeface.NORMAL) // 글씨체 적용을 위한 변수 선언

        viewModel.houseExpenseRatioNotiText.observe(viewLifecycleOwner) {
            val houseExpenseText = SpannableStringBuilder(it)
            houseExpenseText.setSpan(ForegroundColorSpan(ContextCompat.getColor(mainActivity,
                R.color.purplevivid_buttonline
            )), 9, it.length - 5, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            houseExpenseText.setSpan(TypefaceSpan(montBoldTypeface), 9, it.length - 5, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            binding.spendnoti.text = houseExpenseText
        }

        viewModel.periodPassed.observe(viewLifecycleOwner) {
            val periodText = SpannableStringBuilder(it)
            periodText.setSpan(ContextCompat.getColor(mainActivity, R.color.purplevivid_buttonline), it.length - 5, it.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            binding.comparebigtxt.text = periodText
        }

        viewModel.userExpenseLeftText.observe(viewLifecycleOwner) {
            val leftText = SpannableStringBuilder(it)
            leftText.setSpan(TypefaceSpan(suitBoldTypeface), it.length - 1, it.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            binding.remainingbudget.text = leftText
        }

        viewModel.userBudgetText.observe(viewLifecycleOwner) {
            val budgetText = SpannableStringBuilder(it)
            budgetText.setSpan(TypefaceSpan(suitBoldTypeface), it.length - 1, it.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            binding.totalbudget.text = budgetText
        }
    }

    private fun initPieChart() {
        var userExpenseLeft: Float = 1.0f
        var userExpenseUsed: Float = 1.0f
        var userBudget: Float = 1.0f

        viewModel.userExpenseLeft.observe(viewLifecycleOwner) {
            userExpenseLeft = try {
                it.toFloat()
            } catch (e: Exception) {
                1.0f
            }
        }
        viewModel.userExpenseUsed.observe(viewLifecycleOwner) {
            userExpenseUsed = try {
                it.toFloat()
            } catch (e: Exception) {
                1.0f
            }
        }
        viewModel.userBudget.observe(viewLifecycleOwner) {
            userBudget = try {
                it.toFloat()
            } catch (e: Exception) {
                1.0f
            }
            if (userBudget == 0.0f) { userBudget = 1.0f }
        }

        val leftBudgetRatio = (userExpenseLeft / userBudget) * 100
        val usedBudgetRatio = (userExpenseUsed / userBudget) * 100
        val expense = userBudget.toInt()
        val expenseFormatted = DecimalFormat("#,###").format(expense)

        val montBoldTypeface = Typeface.create(ResourcesCompat.getFont(mainActivity, R.font.montserrat_bold), Typeface.NORMAL)
        val suitBoldTypeface = Typeface.create(ResourcesCompat.getFont(mainActivity, R.font.suit_bold), Typeface.NORMAL)
        val suitSemiBoldTypeface = Typeface.create(ResourcesCompat.getFont(mainActivity, R.font.suit_semibold), Typeface.NORMAL)
        val piemidtext = SpannableStringBuilder("${expenseFormatted}원\n오늘까지 썼어요")
        piemidtext.setSpan(AbsoluteSizeSpan(18, true), 0, expenseFormatted.length + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        piemidtext.setSpan(AbsoluteSizeSpan(16, true), expenseFormatted.length + 2, piemidtext.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        piemidtext.setSpan(TypefaceSpan(montBoldTypeface), 0, expenseFormatted.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        piemidtext.setSpan(TypefaceSpan(suitBoldTypeface), expenseFormatted.length, expenseFormatted.length + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        piemidtext.setSpan(TypefaceSpan(suitSemiBoldTypeface), expenseFormatted.length + 1, piemidtext.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        piemidtext.setSpan(ForegroundColorSpan(ContextCompat.getColor(mainActivity, R.color.purplevivid_buttonline)), 0, expenseFormatted.length + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        piemidtext.setSpan(ForegroundColorSpan(ContextCompat.getColor(mainActivity, R.color.black_text)), expenseFormatted.length + 1, piemidtext.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        binding.myPieMidText.text = piemidtext
        val pieChart = binding.myPieChart
        pieChart.maxAngle = 180f
        pieChart.setUsePercentValues(true)
        val entries = ArrayList<PieEntry>()
        var spendfornow = (leftBudgetRatio)
        var leftfornow = (usedBudgetRatio)
        if (leftBudgetRatio + usedBudgetRatio >= 100f) {
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
}