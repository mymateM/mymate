package com.example.mymate.presentation.home

import android.content.Context
import android.graphics.Typeface
import android.os.Build
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.TypefaceSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.mymate.*
import com.example.mymate.databinding.MainReportFragmentBinding
import com.example.mymate.presentation.main.MainActivity
import com.google.android.material.tabs.TabLayoutMediator
import java.time.format.DateTimeFormatter

class MainReportFragment : Fragment() {
    lateinit var mainActivity: MainActivity

    val householdFragment = MainReportHouseholdFragment()
    val meFragment = MainReportMeFragment()
    val retrofit = RetrofitClientInstance.client
    private var formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    var resumed = "00"

    private var _binding: MainReportFragmentBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MainReportViewModel by activityViewModels()

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
    ): View {
        initBinding(inflater, container)
        initViewPager()
        initReport()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        resumed = "01"
    }

    private fun initBinding(inflater: LayoutInflater, container: ViewGroup?) {
        _binding = MainReportFragmentBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.vm = viewModel
    }

    private fun initViewPager() {
        val pager2Adapter = viewPager2Adapter(this.requireActivity())
        pager2Adapter.addFragment(householdFragment)
        pager2Adapter.addFragment(meFragment)

        binding.reportPager.offscreenPageLimit = 2
        binding.reportPager.isUserInputEnabled = false

        binding.reportPager.apply {
            adapter = pager2Adapter
        }

        TabLayoutMediator(binding.reportTab, binding.reportPager) {tab, position ->
            when(position) {
                0 -> tab.text = "전체 예산 및 지출"
                1 -> tab.text = "개인 지출"
            }
        }.attach()
    }

    @RequiresApi(Build.VERSION_CODES.P)
    private fun initReport() {
        viewModel.periodDate.observe(viewLifecycleOwner) {
            if (it != null) {
                val montBoldTypeface = Typeface.create(ResourcesCompat.getFont(mainActivity, R.font.montserrat_bold), Typeface.NORMAL)
                val thisperiod = SpannableStringBuilder("${it.monthValue}월 ${it.dayOfMonth}일 -")
                thisperiod.setSpan(TypefaceSpan(montBoldTypeface), 0, it.monthValue.toString().length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                thisperiod.setSpan(TypefaceSpan(montBoldTypeface), it.monthValue.toString().length + 2, thisperiod.length - 3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                binding.thisperiod.text = thisperiod
            }
        }
    }

    override fun onResume() {
        //TODO: refresh data
        super.onResume()
        initViewPager()
    }
}