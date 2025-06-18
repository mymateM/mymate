package com.example.mymate.presentation.home.fragment

import android.os.Build
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.AbsoluteSizeSpan
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.view.isGone
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mymate.R
import com.example.mymate.data.dto.report.HouseholdReportProcessed
import com.example.mymate.databinding.MainReportHouseholdFragmentBinding
import com.example.mymate.presentation.util.ReportListAdapter
import com.example.mymate.presentation.home.viewmodel.MainReportViewModel
import com.example.mymate.util.Category
import com.example.mymate.util.CategoryColorProvider
import com.example.mymate.util.FontManager
import com.example.mymate.util.getTypefaceSpan
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry

class MainReportHouseholdFragment: Fragment() {
    private var _binding: MainReportHouseholdFragmentBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MainReportViewModel by activityViewModels()

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        initBinding(inflater, container)
        initHousehold()

        return binding.root
    }

    private fun initBinding(inflater: LayoutInflater, container: ViewGroup?) {
        _binding = MainReportHouseholdFragmentBinding.inflate(inflater, container)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.vm = viewModel
    }

    @RequiresApi(Build.VERSION_CODES.P)
    private fun initHousehold() {
        viewModel.householdReport.observe(viewLifecycleOwner) {
            initPieRecycle(it)
        }

        viewModel.houseCategoryTitle.observe(viewLifecycleOwner) {
            val categoryTitle = SpannableStringBuilder("이 달의 많이 쓴 카테고리는\n${it}이에요")
            categoryTitle.setSpan(
                ForegroundColorSpan(
                    CategoryColorProvider.getColorInt(
                        requireContext(),
                        Category.fromDisplayName(it))
                ), 16, 18, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            binding.categorytitle.text = categoryTitle

            if (it == "") {
                binding.categorytitle.text = "이번 달에는\n소비를 등록하지 않았어요."
            }
        }

        viewModel.houseTotalExpense.observe(viewLifecycleOwner) {
            val piemidtxt = SpannableStringBuilder("총 지출\n${it}원")
            piemidtxt.setSpan(
                ForegroundColorSpan(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.graydark_text)
                ), 0, 4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            piemidtxt.setSpan(AbsoluteSizeSpan(16, true), 0, 4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            piemidtxt.setSpan(FontManager.suitMedium.getTypefaceSpan(), 0, 4,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            piemidtxt.setSpan(FontManager.suitBold.getTypefaceSpan(), piemidtxt.length - 1, piemidtxt.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
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
                binding.character.setImageDrawable(ContextCompat.getDrawable(requireContext(),
                    R.drawable.character_report_more
                ))
            } else {
                binding.title.text = "대단해요!\n예산을 넘지 않았어요"
                binding.character.setImageDrawable(ContextCompat.getDrawable(requireContext(),
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
        val adapter = ReportListAdapter(userData.categoryName, userData.categoryRatio, userData.categoryAbs)
        binding.householdlist.adapter = adapter
        binding.householdlist.layoutManager = manager
    }
}