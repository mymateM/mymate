package com.example.mymate.presentation.settlement

import android.content.Context
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mymate.*
import com.example.mymate.data.dto.report.HouseholdReportProcessed
import com.example.mymate.databinding.MainReportHouseholdFragmentBinding
import com.example.mymate.presentation.home.adapter.MainReportListAdapter
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry

class SettlementReportHouseholdFragment: Fragment() {
    private var _binding: MainReportHouseholdFragmentBinding? = null
    private val binding get() = _binding!!
    private val viewModel: SettlementViewModel by activityViewModels()
    private lateinit var settlementReport: SettlementReportActivity

    private val colorItemList = ArrayList<Int>() //TODO: color item 관련한 코드 util로 빼기

    override fun onAttach(context: Context) {
        super.onAttach(context)
        settlementReport = context as SettlementReportActivity
    }

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
        _binding = MainReportHouseholdFragmentBinding.inflate(inflater, container, false)
    }

    private fun initHousehold() {
        viewModel.initReport()
        viewModel.houseReportTitle.observe(viewLifecycleOwner) {
            binding.title.text = it
        }

        viewModel.householdCharacter.observe(viewLifecycleOwner) {
            binding.character.setImageDrawable(it)
        }

        viewModel.houseTotalExpense.observe(viewLifecycleOwner) {
            binding.pieMidText.text = it
        }

        viewModel.housePieData.observe(viewLifecycleOwner) {
            initPieRecycle(it)
        }

        viewModel.houseMaxCategory.observe(viewLifecycleOwner) {
            val categoryTitle = SpannableStringBuilder("이 달의 많이 쓴 카테고리는\n${it}입니다")
            categoryTitle.setSpan(ForegroundColorSpan(getColorsByCategory(it)), 16, 18, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            binding.categorytitle.text = categoryTitle

            if (it == "") {
                binding.categorytitle.text = "이번 달에는\n소비를 등록하지 않았어요."
            }
        }
    }

    private fun initColors() {
        colorItemList.add(ContextCompat.getColor(settlementReport, R.color.pie_yellow))
        colorItemList.add(ContextCompat.getColor(settlementReport, R.color.pie_red))
        colorItemList.add(ContextCompat.getColor(settlementReport, R.color.pie_blue))
        colorItemList.add(ContextCompat.getColor(settlementReport, R.color.pie_gray))
        colorItemList.add(ContextCompat.getColor(settlementReport, R.color.pie_green))
        colorItemList.add(ContextCompat.getColor(settlementReport, R.color.pie_pink))
        colorItemList.add(ContextCompat.getColor(settlementReport, R.color.pie_purple))
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
            setHoleColor(ContextCompat.getColor(settlementReport, R.color.white))
            legend.isEnabled = false
            setTouchEnabled(false)
        }
        pieChart.invalidate()

        //RecyclerView Setting
        val manager: RecyclerView.LayoutManager = LinearLayoutManager(settlementReport)
        val adapter = MainReportListAdapter(userData.categoryName, userData.categoryRatio, userData.categoryAbs, colorItemList)
        binding.householdlist.adapter = adapter
        binding.householdlist.layoutManager = manager
    }
}