package com.example.mymate.presentation.home

import android.content.Context
import android.graphics.Typeface
import android.icu.text.DecimalFormat
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
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isGone
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mymate.R
import com.example.mymate.RetrofitClientInstance
import com.example.mymate.data.dto.report.HouseholdReportProcessed
import com.example.mymate.databinding.MainReportHouseholdFragmentBinding
import com.example.mymate.presentation.home.adapter.MainReportListAdapter
import com.example.mymate.presentation.main.MainActivity
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import java.time.format.DateTimeFormatter

class MainReportHouseholdFragment: Fragment() {
    lateinit var mainActivity: MainActivity

    private val retrofit = RetrofitClientInstance.client
    private var formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    private var _binding: MainReportHouseholdFragmentBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MainReportViewModel by activityViewModels()

    private val colorItemList = ArrayList<Int>()

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
        initColors()
        initHousehold()

        return binding.root
    }

    private fun initBinding(inflater: LayoutInflater, container: ViewGroup?) {
        _binding = MainReportHouseholdFragmentBinding.inflate(inflater, container)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.vm = viewModel
    }

    private fun initColors() {
        colorItemList.add(ContextCompat.getColor(mainActivity, R.color.pie_yellow))
        colorItemList.add(ContextCompat.getColor(mainActivity, R.color.pie_red))
        colorItemList.add(ContextCompat.getColor(mainActivity, R.color.pie_blue))
        colorItemList.add(ContextCompat.getColor(mainActivity, R.color.pie_gray))
        colorItemList.add(ContextCompat.getColor(mainActivity, R.color.pie_green))
        colorItemList.add(ContextCompat.getColor(mainActivity, R.color.pie_pink))
        colorItemList.add(ContextCompat.getColor(mainActivity, R.color.pie_purple))
    }

    private fun getColorsByCategory(category: String): Int {
        return when(category) {
            "식비" ->  colorItemList[0]
            "생활" ->  colorItemList[6]
            "쇼핑" ->  colorItemList[5]
            "교통" ->  colorItemList[2]
            "의료" ->  colorItemList[1]
            "고지서" ->  colorItemList[0]
            "교육" ->  colorItemList[4]
            else ->  colorItemList[3]
        }
    }

    @RequiresApi(Build.VERSION_CODES.P)
    private fun initHousehold() {
        val suitMediumTypeface = Typeface.create(ResourcesCompat.getFont(mainActivity, R.font.suit_medium), Typeface.NORMAL)
        val suitBoldTypeface = Typeface.create(ResourcesCompat.getFont(mainActivity, R.font.suit_bold), Typeface.NORMAL)

        viewModel.householdReport.observe(viewLifecycleOwner) {
            initPieRecycle(it)
        }

        viewModel.houseCategoryTitle.observe(viewLifecycleOwner) {
            val categoryTitle = SpannableStringBuilder("이 달의 많이 쓴 카테고리는\n${it}이에요")
            categoryTitle.setSpan(ForegroundColorSpan(getColorsByCategory(it)), 16, 18, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            binding.categorytitle.text = categoryTitle

            if (it == "") {
                binding.categorytitle.text = "이번 달에는\n소비를 등록하지 않았어요."
            }
        }

        viewModel.houseTotalExpense.observe(viewLifecycleOwner) {
            val piemidtxt = SpannableStringBuilder("총 지출\n${it}원")
            piemidtxt.setSpan(ForegroundColorSpan(ContextCompat.getColor(mainActivity, R.color.graydark_text)), 0, 4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            piemidtxt.setSpan(AbsoluteSizeSpan(16, true), 0, 4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            piemidtxt.setSpan(TypefaceSpan(suitMediumTypeface), 0, 4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            piemidtxt.setSpan(TypefaceSpan(suitBoldTypeface), piemidtxt.length - 1, piemidtxt.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            binding.pieMidText.text = piemidtxt

            if (it == null || it == "" || it == "0") {
                binding.categorypie.isGone = true
                binding.pieMidText.isGone = true
                binding.pieMidText.text = "이번 달에는 소비를 등록하지 않았어요."
            }
        }

        viewModel.houseIsOver.observe(viewLifecycleOwner) {
            if (it) {
                binding.title.text = "앗!\n예산보다 지출이 커요"
                binding.character.setImageDrawable(ContextCompat.getDrawable(mainActivity,
                    R.drawable.character_report_more
                ))
            } else {
                binding.title.text = "대단해요!\n예산을 넘지 않았어요"
                binding.character.setImageDrawable(ContextCompat.getDrawable(mainActivity,
                    R.drawable.character_report_less
                ))
            }
        }
    }

    private fun initPieRecycle(userData: HouseholdReportProcessed) {
        //Piechart Setting
        val pieChart = binding.categorypie
        pieChart.setUsePercentValues(true)
        val entries = ArrayList<PieEntry>()
        val colorItem = ArrayList<Int>()
        for (i in 0 until userData.categoryName.size) {
            entries.add(PieEntry(userData.categoryRatio[i] * 100))
            colorItem.add(getColorsByCategory(userData.categoryName[i]))
        }
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
            holeRadius = 85f
            setHoleColor(ContextCompat.getColor(mainActivity, R.color.white))
            legend.isEnabled = false
            setTouchEnabled(false)
        }
        pieChart.invalidate()

        //RecyclerView Setting
        val manager: RecyclerView.LayoutManager = LinearLayoutManager(mainActivity)
        val adapter = MainReportListAdapter(userData.categoryName, userData.categoryRatio, userData.categoryAbs, colorItemList)
        binding.householdlist.adapter = adapter
        binding.householdlist.layoutManager = manager
    }
}