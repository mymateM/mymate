package com.example.mymate.presentation.settlement.fragment

import android.content.Context
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isGone
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mymate.*
import com.example.mymate.data.dto.report.HouseholdReportProcessed
import com.example.mymate.databinding.MainReportHouseholdFragmentBinding
import com.example.mymate.presentation.home.adapter.MainReportListAdapter
import com.example.mymate.presentation.settlement.SettlementReportActivity
import com.example.mymate.presentation.settlement.viewmodel.SettlementViewModel
import com.example.mymate.util.Category
import com.example.mymate.util.CategoryColorProvider
import com.example.mymate.util.FontManager
import com.example.mymate.util.getTypefaceSpan
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry

class SettlementReportMeFragment: Fragment() {
    private var _binding: MainReportHouseholdFragmentBinding? = null
    private val binding get() = _binding!!
    private val viewModel: SettlementViewModel by activityViewModels()

    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        initBinding(inflater, container)
        initData()

        return binding.root
    }

    private fun initBinding(inflater: LayoutInflater, container: ViewGroup?) {
        _binding = MainReportHouseholdFragmentBinding.inflate(inflater, container, false)
    }

    private fun initData() {
        binding.title.isGone = true
        binding.character.isGone = true

        viewModel.myMaxCategory.observe(viewLifecycleOwner) {
            val categoryTitle = SpannableStringBuilder("이 달의 많이 쓴 카테고리는\n${it}이에요")
            categoryTitle.setSpan(ForegroundColorSpan(
                CategoryColorProvider.getColorInt(
                    requireContext(),
                    Category.fromDisplayName(it))
            ), 16, 18, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            binding.categorytitle.text = categoryTitle

            if (it == "") {
                binding.categorytitle.text = "이번 달에는\n소비를 등록하지 않았어요."
            }
        }

        viewModel.myTotalExpense.observe(viewLifecycleOwner) {
            val totalExpense = SpannableStringBuilder(it)
            totalExpense.run {
                setSpan(FontManager.suitMedium.getTypefaceSpan(), 0, 4,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                setSpan(FontManager.suitBold.getTypefaceSpan(), it.length -1, it.length,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            }
            binding.pieMidText.text = totalExpense.toString()
        }

        viewModel.myPieData.observe(viewLifecycleOwner) {
            initPieRecycle(it)
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
            colorItem.add(CategoryColorProvider.getColorInt(
                requireContext(),
                Category.fromDisplayName(userData.categoryName[i])
            ))
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
            setHoleColor(ContextCompat.getColor(requireContext(), R.color.white))
            legend.isEnabled = false
            setTouchEnabled(false)
        }
        pieChart.invalidate()

        //RecyclerView Setting
        val manager: RecyclerView.LayoutManager = LinearLayoutManager(requireContext())
        val adapter = MainReportListAdapter(userData.categoryName, userData.categoryRatio, userData.categoryAbs)
        binding.householdlist.adapter = adapter
        binding.householdlist.layoutManager = manager
    }
}