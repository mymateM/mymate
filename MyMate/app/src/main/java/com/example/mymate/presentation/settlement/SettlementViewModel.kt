package com.example.mymate.presentation.settlement

import android.app.Application
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.icu.text.DecimalFormat
import android.text.Spannable
import android.text.SpannableStringBuilder
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.mymate.DataStoreRepoUser
import com.example.mymate.R
import com.example.mymate.data.dto.report.HouseholdReportProcessed
import com.example.mymate.data.repository.SettlementRepository
import com.example.mymate.dataStore
import com.example.mymate.domain.usecase.SettlementUseCase
import com.example.mymate.util.getTypefaceSpan
import kotlinx.coroutines.launch
import java.time.format.DateTimeFormatter

class SettlementViewModel(application: Application): AndroidViewModel(application) {
    private val settlementUseCase = SettlementUseCase(SettlementRepository(DataStoreRepoUser(application.dataStore)))
    private val montBoldTypeface = Typeface.create(ResourcesCompat.getFont(application, R.font.montserrat_bold), Typeface.NORMAL)
    private val suitBoldTypeface = Typeface.create(ResourcesCompat.getFont(application, R.font.suit_bold), Typeface.NORMAL)

    //Settlement Activity 변수
    private val _periodText = MutableLiveData<String>()
    val periodText: LiveData<String> get() = _periodText

    private val _settleTitleText = MutableLiveData<String>()
    val settleTitleText: LiveData<String> get() = _settleTitleText

    private val _totalExpenseText = MutableLiveData<SpannableStringBuilder>()
    val totalExpenseText: LiveData<SpannableStringBuilder> get() = _totalExpenseText

    private val _inoutText = MutableLiveData<String>()
    val inoutText: LiveData<String> get() = _inoutText

    private val _inoutAmountText = MutableLiveData<SpannableStringBuilder>()
    val inoutAmountText: LiveData<SpannableStringBuilder> get() = _inoutAmountText

    private val _totalPortionGuide = MutableLiveData<Float>()
    val totalPortionGuide: LiveData<Float> get() = _totalPortionGuide

    private val _myPortionGuide = MutableLiveData<Float>()
    val myPortionGuide: LiveData<Float> get() = _myPortionGuide

    private val _settleBottomText = MutableLiveData<SpannableStringBuilder>()
    val settleBottomText: LiveData<SpannableStringBuilder> get() = _settleBottomText

    //Settlement Report 변수
    private val _reportPeriodText = MutableLiveData<String>()
    val reportPeriodText: LiveData<String> get() = _reportPeriodText

    //가구 report 변수
    private val _houseReportTitle = MutableLiveData<String>()
    val houseReportTitle: LiveData<String> get() = _houseReportTitle

    private val _householdCharacter = MutableLiveData<Drawable>()
    val householdCharacter: LiveData<Drawable> get() = _householdCharacter

    private val _housePieData = MutableLiveData<HouseholdReportProcessed>()
    val housePieData: LiveData<HouseholdReportProcessed> get() = _housePieData

    private val _houseTotalExpense = MutableLiveData<String>()
    val houseTotalExpense: LiveData<String> get() = _houseTotalExpense

    private val _houseMaxCategory = MutableLiveData<String>()
    val houseMaxCategory: LiveData<String> get() = _houseMaxCategory

    //개인 report 변수
    private val _myMaxCategory = MutableLiveData<String>()
    val myMaxCategory: LiveData<String> get() = _myMaxCategory

    private val _myPieData = MutableLiveData<HouseholdReportProcessed>()
    val myPieData: LiveData<HouseholdReportProcessed> get() = _myPieData

    private val _settleNotiText = MutableLiveData<String>()
    val settleNotiText: LiveData<String> get() = _settleNotiText //modale 귀속

    init {
        initSettleActivityInfo()
    }

    fun initSettleActivityInfo() { //Settlement Activity 바인딩
        viewModelScope.launch {
            val settlementDate = settlementUseCase.getSettlementDate()
            val startDate = settlementUseCase.getPeriodStartDate(settlementDate)
            val endDate = settlementUseCase.getPeriodEndDate(settlementDate)
            _periodText.value = "${startDate.monthValue}.${startDate.dayOfMonth} - ${endDate.monthValue}.${endDate.dayOfMonth}"
            _settleTitleText.value = "\n정산일이 다가왔어요!" //TODO: API 이슈로 가구명 전달 안 되는 중. 원래 형식은 "${가구명}의\n정산일이 다가왔어요!"

            val myData = settlementUseCase.getMySettleInfo()
            val totalBuilder = SpannableStringBuilder("${DecimalFormat("#,###").format(myData.household_expense_total)}원")
            totalBuilder.setSpan(suitBoldTypeface.getTypefaceSpan(), totalBuilder.length - 1, totalBuilder.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            _totalExpenseText.value = totalBuilder

            val inoutBuilder = SpannableStringBuilder("${DecimalFormat("#,###").format(myData.user.settlement_amount)}원")
            inoutBuilder.setSpan(suitBoldTypeface.getTypefaceSpan(), inoutBuilder.length - 1, inoutBuilder.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            _inoutAmountText.value = inoutBuilder
            if (myData.user.is_settlement_sender) {
                _inoutText.value = "정산을 해야 해요"
                val bottomBuilder = SpannableStringBuilder("${DecimalFormat("#,###").format(myData.user.settlement_amount)}원 보내러 가기")
                bottomBuilder.setSpan(montBoldTypeface.getTypefaceSpan(), 0, DecimalFormat("#,###").format(myData.user.settlement_amount).length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                _settleBottomText.value = bottomBuilder
            } else {
                _inoutText.value = "정산을 받아야 해요"
                val bottomBuilder = SpannableStringBuilder("${DecimalFormat("#,###").format(myData.user.settlement_amount)}원 받으러 가기")
                bottomBuilder.setSpan(montBoldTypeface.getTypefaceSpan(), 0, DecimalFormat("#,###").format(myData.user.settlement_amount).length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                _settleBottomText.value = bottomBuilder
            }

            _totalPortionGuide.value = 0.05f + myData.user.ratio_expense.toFloat() / myData.household_expense_total.toFloat() * 0.90f
            _myPortionGuide.value = 0.05f + myData.user.real_expense.toFloat() / myData.household_expense_total.toFloat() * 0.90f
        }
    }

    fun initReport() { //Settlement Report 바인딩 (공통)
        viewModelScope.launch {
            val settlementDate = settlementUseCase.getSettlementDate()
            val startDate = settlementUseCase.getPeriodStartDate(settlementDate)
            val endDate = settlementUseCase.getPeriodEndDate(settlementDate)
            _reportPeriodText.value = "${startDate.monthValue}월 ${startDate.dayOfMonth}일 - ${endDate.monthValue}월 ${endDate.dayOfMonth}일"

            initSettleHouseholdReport(startDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
            initSettleMeReport(startDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
        }
    }

    private fun initSettleHouseholdReport(date: String) { //Settlement Report Household 바인딩
        viewModelScope.launch {
            val reportData = settlementUseCase.getHouseSettleInfo(date)
            if (reportData.is_expense_over_budget) {
                _houseReportTitle.value = "앗!\n예산보다 지출이 커요"
                _householdCharacter.value = ContextCompat.getDrawable(getApplication(), R.drawable.character_report_more)
            } else {
                _houseReportTitle.value = "대단해요! 예산을 넘지 않았어요"
                _householdCharacter.value = ContextCompat.getDrawable(getApplication(), R.drawable.character_report_less)
            }
            val pieData = settlementUseCase.getHouseholdPieData(reportData)
            _housePieData.value = pieData
            _houseMaxCategory.value = pieData.categoryName[pieData.maxCategoryIndex]
            _houseTotalExpense.value = reportData.total_expense
        }
    }

    fun initSettleMeReport(date: String) {
        viewModelScope.launch {
            val reportData = settlementUseCase.getMyReport(date)
            val pieData = settlementUseCase.getMyPieData(reportData)
            _myMaxCategory.value = pieData.categoryName[pieData.maxCategoryIndex]
            _myPieData.value = pieData
        }
    }

    //TODO: fragment & modale의 xml 확인하고 뷰바인딩하기, activity & fragment & modale view 클래스 확인하고 뷰모델과 바인딩하기
}