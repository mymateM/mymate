package com.example.mymate

import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isGone
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mymate.data.dto.expense.CalendarItem
import com.example.mymate.data.dto.expense.ExpenseSummary
import com.example.mymate.data.dto.expense.response.DailyExpenseResponse
import com.example.mymate.databinding.MainSpendingFragmentBinding
import com.example.mymate.presentation.main.MainActivity
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class MainSpendingFragment : Fragment() {
    private lateinit var mainActivity: MainActivity
    lateinit var behavior: BottomSheetBehavior<ConstraintLayout>
    lateinit var selectedDate: LocalDate
    var resumed = "00"

    var retrofit = RetrofitClientInstance.client
    var endpoint = retrofit?.create(getDailyExpense::class.java)
    
    private var formatter = DateTimeFormatter.ofPattern("yy년 MM월 dd일")

    private var _binding: MainSpendingFragmentBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MainSpendingViewModel by viewModels()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as MainActivity
        selectedDate = LocalDate.now()
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
        bottomSheetInit()
        //modal img settings
        binding.cover.isGone = true

        //calendar settings
        initCalendar()

        //button events
        binding.lastMonth.setOnClickListener {
            selectedDate = selectedDate.minusMonths(1)
            viewModel.setDate(selectedDate)
        }

        binding.nextMonth.setOnClickListener {
            selectedDate = selectedDate.plusMonths(1)
            viewModel.setDate(selectedDate)
        }

        binding.bills.setOnClickListener {
            startActivity(Intent(mainActivity, BillManagerActivity::class.java))
            mainActivity.overridePendingTransition(R.anim.right_enter, R.anim.none)
        }

        binding.alarm.setOnClickListener {
            startActivity(Intent(mainActivity, AlarmActivity::class.java))
            mainActivity.overridePendingTransition(R.anim.right_enter, R.anim.none)
        }

        binding.search.setOnClickListener {
            startActivity(Intent(mainActivity, SearchActivity::class.java))
            mainActivity.overridePendingTransition(R.anim.right_enter, R.anim.none)
        }

        binding.spendingPlus.setOnClickListener {
            val toSpendingPlus = Intent(mainActivity, SpendingAddActivity::class.java)
            toSpendingPlus.putExtra("thedate", formatter.format(selectedDate))
            startActivity(Intent(toSpendingPlus))
        }
        resumed = "01"
        return binding.root
    }

    private fun initBinding(inflater: LayoutInflater, container: ViewGroup?) {
        _binding = MainSpendingFragmentBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.vm = viewModel
    }

    @RequiresApi(Build.VERSION_CODES.P)
    private fun bottomSheetInit() {
        behavior = BottomSheetBehavior.from(binding.datepicker.root)
        behavior.peekHeight = 0
        behavior.isDraggable = false
        behavior.isHideable = true

        binding.selectdate.setOnClickListener {
            behavior.state = BottomSheetBehavior.STATE_EXPANDED
            binding.cover.isGone = false
        }

        binding.yeartext.setOnClickListener {
            behavior.state = BottomSheetBehavior.STATE_EXPANDED
            binding.cover.isGone = false
        }

        binding.selectdatecontainer.setOnClickListener {
            behavior.state = BottomSheetBehavior.STATE_EXPANDED
            binding.cover.isGone = false
        }

        binding.monthText.setOnClickListener {
            behavior.state = BottomSheetBehavior.STATE_EXPANDED
            binding.cover.isGone = false
        }

        binding.datepicker.confirmbtn.setOnClickListener {
            behavior.state = BottomSheetBehavior.STATE_COLLAPSED
            val picker = binding.datepicker.spinnerpicker
            val localDate = viewModel.parseDate(picker.year, picker.month+1, picker.dayOfMonth)
            viewModel.setDate(localDate)
            binding.cover.isGone = true
        }

        binding.cover.setOnClickListener {
            behavior.state = BottomSheetBehavior.STATE_COLLAPSED
            binding.cover.isGone = true
        }
    }

    private fun initCalendar() {
        val adapter = CalendarAdapter()
        viewModel.calendarInfo.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }
        val manager: RecyclerView.LayoutManager = GridLayoutManager(mainActivity, 7)
        binding.mainCalendar.layoutManager = manager
        binding.mainCalendar.adapter = adapter.apply {
            setOnItemClickListener(object : CalendarAdapter.OnItemClickListener {
                override fun onItemClick(item: String, position: Int, day: Int) {
                    if (day != 0) {
                        selectedDate = viewModel.parseDate(selectedDate.year, selectedDate.monthValue, day)
                        adapter.submitList(viewModel.getCalendarInfo(selectedDate))
                        viewModel.setDate(selectedDate)
                    }
                }
            })
        }
        binding.mainCalendar.itemAnimator = null
        viewModel.setDate(selectedDate)
    }

    @RequiresApi(Build.VERSION_CODES.P)
    private fun setDailyExpenceView(date: LocalDate) {
        var accessToken = ""
        var detailResponse: DailyExpenseResponse
        /* runBlocking {
            accessToken = userRepo.userAccessReadFlow.first().toString()
        }
        month = if (date.monthValue < 10) {
            "0${date.monthValue}"
        } else {
            date.monthValue.toString()
        }
        day = if (date.dayOfMonth < 10) {
            "0" + date.dayOfMonth
        } else {
            date.dayOfMonth.toString()
        }
        year = date.year.toString()
        endpoint!!.getDailyExpense("Bearer $accessToken", year, month, day).enqueue(object : Callback<DailyExpenseResponse> {
            override fun onResponse(
                call: Call<DailyExpenseResponse>,
                response: Response<DailyExpenseResponse>
            ) {
                if (response.isSuccessful) {
                    detailResponse = response.body()!!
                    expenseSummary = detailResponse.data.expenses
                    val adapter = SpendingAdapter(mainActivity, expenseSummary)
                    val manager = LinearLayoutManager(mainActivity)
                    binding.dailySpendings.layoutManager = manager
                    binding.dailySpendings.adapter = adapter.apply {
                        setOnItemClickListener(object : SpendingAdapter.OnItemClickListener {
                            override fun onItemClick(item: ExpenseSummary, position: Int) {
                            }
                        })
                    }
                }
            }

            override fun onFailure(call: Call<DailyExpenseResponse>, t: Throwable) {
                Toast.makeText(mainActivity, "연결 실패", Toast.LENGTH_SHORT).show()
                val adapter = SpendingAdapter(mainActivity, expenseSummary)
                val manager = LinearLayoutManager(mainActivity)
                binding.dailySpendings.layoutManager = manager
                binding.dailySpendings.adapter = adapter
                }
        })*/
    }

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onResume() {
        super.onResume()
        bottomSheetInit()

        //modaleimg settings
        binding.cover.isGone = true

        //calendar settings
        initCalendar()

        //button events
        binding.monthLast.setOnClickListener {
            selectedDate = selectedDate.minusMonths(1)
            viewModel.setDate(selectedDate)
        }

        binding.monthNext.setOnClickListener {
            selectedDate = selectedDate.plusMonths(1)
            viewModel.setDate(selectedDate)
        }
    }
}