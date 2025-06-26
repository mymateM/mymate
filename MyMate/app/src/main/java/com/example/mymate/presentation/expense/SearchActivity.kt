package com.example.mymate.presentation.expense

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.TextWatcher
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.isGone
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mymate.*
import com.example.mymate.databinding.ActivitySearchBinding
import com.example.mymate.presentation.expense.adapter.CalendarModaleAdapter
import com.example.mymate.presentation.expense.adapter.SearchListContainerAdapter
import com.example.mymate.presentation.expense.viewmodel.SearchViewModel
import com.example.mymate.presentation.util.CategoryAdapter
import com.example.mymate.util.*
import com.google.android.material.bottomsheet.BottomSheetBehavior
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class SearchActivity: AppCompatActivity() {
    private var _binding: ActivitySearchBinding? = null
    private val binding get() = _binding!!

    lateinit var context: Context
    private lateinit var amountBehavior: BottomSheetBehavior<ConstraintLayout>
    private lateinit var calendarBehavior: BottomSheetBehavior<ConstraintLayout>
    private lateinit var categoryBehavior: BottomSheetBehavior<ConstraintLayout>
    private lateinit var orderBehavior: BottomSheetBehavior<ConstraintLayout>

    private var monthFormatter = DateTimeFormatter.ofPattern("MM월")
    private var yearFormatter = DateTimeFormatter.ofPattern("yyyy")
    private val viewModel: SearchViewModel by viewModels()

    private val bottomSheets by lazy {
        listOf(
            amountBehavior,
            calendarBehavior,
            categoryBehavior,
            orderBehavior
        )
    }

    private val textWatcher = object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            val minText = binding.searchamount.minimumedit.text.toString()
            val maxText = binding.searchamount.maximumedit.text.toString()

            val isAnyFilled = minText.isNotEmpty() || maxText.isNotEmpty()
            val background = if (isAnyFilled) {
                R.drawable.button_loginbarselected
            } else {
                R.drawable.button_loginbardefault
            }
            binding.searchamount.amountset.setBackgroundResource(background)
        }

        override fun afterTextChanged(p0: Editable?) {}
    }

    private val editorActionListener = TextView.OnEditorActionListener { _, actionId, _ ->
        if (actionId == EditorInfo.IME_ACTION_DONE ||
            actionId == EditorInfo.IME_ACTION_SEND
        ) { hidekeyboard() }
        false
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        context = this

        binding.cover.isGone = true

        screenInit()
        bottomSheetInit()
        setCategoryView()

        viewModel.selectedDate.observe(this) {
            setCalendarView(it)
        }

        unselected(binding.listupbutton)
        binding.searchlistup.listfromrecent.setTextColor(ContextCompat.getColor(context, R.color.black_text))

        binding.searchcalendar.monthLast.setOnClickListener {
            viewModel.setCalendar(null, viewModel.selectedDate.value!!.minusMonths(1))
        }

        binding.searchcalendar.monthNext.setOnClickListener {
            viewModel.setCalendar(null, viewModel.selectedDate.value!!.plusMonths(1))
        }

        binding.searchEdit.setOnClickListener {
            Toast.makeText(context, "준비중이에요! 지금은 검색 조건을 사용해 보세요!", Toast.LENGTH_SHORT).show()
        }

        binding.back.setOnClickListener {
            finish()
            overridePendingTransition(R.anim.none, R.anim.left_exit)
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
        overridePendingTransition(R.anim.none, R.anim.left_exit)
    }

    private fun collapseAllExcept(except: BottomSheetBehavior<*>?) {
        bottomSheets.forEach { if (it != except) it.state = BottomSheetBehavior.STATE_COLLAPSED }
        if (except == null) binding.cover.isGone = true
    }

    private fun toggleBottomSheet(button: TextView, sheet: BottomSheetBehavior<*>) {
        val isExpanded = sheet.state == BottomSheetBehavior.STATE_EXPANDED
        if (isExpanded) {
            sheet.state = BottomSheetBehavior.STATE_COLLAPSED
            binding.cover.isGone = true
        } else {
            collapseAllExcept(sheet)
            sheet.state = BottomSheetBehavior.STATE_EXPANDED
            binding.cover.isGone = false
        }
    }

    private fun screenInit() {
        viewModel.amountText.observe(this) {
            binding.amountbutton.text = it
            if (it != "가격") {
                selected(binding.amountbutton)
                binding.searchamount.amountset.setBackgroundResource(R.drawable.button_loginbarselected)
            } else {
                unselected(binding.amountbutton)
                binding.searchamount.amountset.setBackgroundResource(R.drawable.button_loginbardefault)
            }
        }

        viewModel.categoryText.observe(this) {
            binding.categorybutton.text = it
            if (it != "카테고리") {
                selected(binding.categorybutton)
                binding.searchcategory.categoryset.setBackgroundResource(R.drawable.button_loginbarselected)
            } else {
                unselected(binding.categorybutton)
                binding.searchcategory.categoryset.setBackgroundResource(R.drawable.button_loginbardefault)
            }
        }

        viewModel.periodText.observe(this) {
            binding.calendarbutton.text = it
            if (it != "기간") {
                selected(binding.calendarbutton)
                binding.searchcalendar.calendarset.setBackgroundResource(R.drawable.button_loginbarselected)
            } else {
                unselected(binding.calendarbutton)
                binding.searchcalendar.calendarset.setBackgroundResource(R.drawable.button_loginbardefault)
            }
        }

        viewModel.sortText.observe(this) {
            binding.listupbutton.text = it
            binding.searchlistup.run {
                if (listfromold.text == it) {
                    listfromold.setTextColor(ContextCompat.getColor(context, R.color.black_text))
                    listfromrecent.setTextColor(ContextCompat.getColor(context, R.color.graylight_text))
                } else {
                    listfromrecent.setTextColor(ContextCompat.getColor(context, R.color.black_text))
                    listfromold.setTextColor(ContextCompat.getColor(context, R.color.graydark_text))
                }
            }
            if (it != "정렬") selected(binding.listupbutton) else unselected(binding.listupbutton)
        }

        binding.searchlistcontainer.layoutManager = LinearLayoutManager(this)
        binding.searchlistcontainer.adapter = SearchListContainerAdapter(ArrayList())
        viewModel.searchList.observe(this) {
            (binding.searchlistcontainer.adapter as SearchListContainerAdapter).apply {
                update(it)
            }
        }
    }

    private fun bottomSheetInit() {
        amountBehavior = BottomSheetBehavior.from(binding.searchamount.root)
        calendarBehavior = BottomSheetBehavior.from(binding.searchcalendar.root)
        categoryBehavior = BottomSheetBehavior.from(binding.searchcategory.root)
        orderBehavior = BottomSheetBehavior.from(binding.searchlistup.root)

        //클릭 이벤트 막기 위한 빈 click listener
        binding.searchamount.root.isClickable = false
        binding.searchcalendar.root.isClickable = false
        binding.searchcategory.root.isClickable = false
        binding.searchlistup.root.isClickable = false

        binding.refreshButton.setOnClickListener {
            viewModel.initOption()
            binding.run {
                searchlistup.listfromrecent.setTextColor(ContextCompat.getColor(context, R.color.black_text))
                startgraphic.isGone = false
                (searchlistcontainer.adapter as SearchListContainerAdapter).update(ArrayList()) //TODO: 어댑터 새로 만들지 말고 업데이트
            }
            viewModel.initOption()
            viewModel.setCalendar(null, LocalDate.now())
            setCategoryView()
        }

        binding.calendarbutton.setOnClickListener { toggleBottomSheet(binding.calendarbutton, calendarBehavior) }

        binding.amountbutton.setOnClickListener { toggleBottomSheet(binding.amountbutton, amountBehavior) }

        binding.categorybutton.setOnClickListener { toggleBottomSheet(binding.categorybutton, categoryBehavior) }

        binding.listupbutton.setOnClickListener { toggleBottomSheet(binding.listupbutton, orderBehavior) }

        binding.cover.setOnClickListener {
            collapseAllExcept(null)
            if (binding.calendarbutton.text == "기간" && binding.amountbutton.text == "가격" && binding.categorybutton.text == "카테고리") {
                binding.startgraphic.isGone = false
            }
            binding.cover.isGone = true
        }

        binding.searchamount.amountset.setOnClickListener {
            viewModel.setOption(SearchViewModel.AMOUNT, minData = binding.searchamount.minimumedit.text.toString(), maxData = binding.searchamount.maximumedit.text.toString())
            amountBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
            binding.cover.isGone = true
            viewModel.search()
        }

        binding.searchamount.minimumedit.setOnEditorActionListener(editorActionListener)
        binding.searchamount.maximumedit.setOnEditorActionListener(editorActionListener)

        binding.searchamount.maximumedit.addTextChangedListener(textWatcher)
        binding.searchamount.minimumedit.addTextChangedListener(textWatcher)

        binding.searchcalendar.calendarset.setOnClickListener {
            collapseAllExcept(null)
            viewModel.search()
        }

        binding.searchcategory.categoryset.setOnClickListener {
            collapseAllExcept(null)
            viewModel.search()
        }

        binding.searchlistup.listfromrecent.setOnClickListener {
            collapseAllExcept(null)
            viewModel.setOption(SearchViewModel.SORT, 1)
            if (binding.amountbutton.text != "가격" || binding.calendarbutton.text != "기간" || binding.categorybutton.text != "카테고리") {
                viewModel.search()
            } else { binding.startgraphic.isGone = false }
        }

        binding.searchlistup.listfromold.setOnClickListener {
            collapseAllExcept(null)
            viewModel.setOption(SearchViewModel.SORT, -1)
            if (binding.amountbutton.text != "가격" || binding.calendarbutton.text != "기간" || binding.categorybutton.text != "카테고리") {
                viewModel.search()
            } else {
                binding.startgraphic.isGone = false
            }
        }
    }

    private fun selected(view: TextView) {
        view.setBackgroundResource(R.drawable.button_selectedboxround)
        view.setTextColor(ContextCompat.getColor(context, R.color.purpleblue_select))
        view.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.arrow_down_selection, 0)
        binding.startgraphic.isGone = true
    }

    private fun unselected(view: TextView) {
        view.setBackgroundResource(R.drawable.button_selectboxnull)
        view.setTextColor(ContextCompat.getColor(context, R.color.graydark_text))
        view.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.arrow_down, 0)
    }

    private fun hidekeyboard() {
        //키보드 내리기
        val imm = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        val focusView = currentFocus ?: window.decorView.findFocus()
        focusView.let {
            imm.hideSoftInputFromWindow(it.windowToken, 0)
            it.clearFocus()
        }
    }

    private fun setCategoryView() {
        val adapter = CategoryAdapter(context, viewModel.categorySelection.value!!)
        val manager: RecyclerView.LayoutManager = GridLayoutManager(context, 3)

        binding.searchcategory.categorylist.itemAnimator = null
        binding.searchcategory.categorylist.layoutManager = manager
        binding.searchcategory.categorylist.adapter = adapter.apply {
            setOnItemClickListener(object : CategoryAdapter.OnItemClickListener {
                override fun onItemClick(position: Int) {
                    viewModel.setOption(SearchViewModel.CATEGORY, position)
                }
            })
        }
    }

    private fun setCalendarView(date: LocalDate) {
        //calendar header
        val month = SpannableStringBuilder(date.format(monthFormatter))
        val font = FontManager
        if (date.monthValue < 10) {
            month.setSpan(font.montserratBold.getTypefaceSpan(), 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        } else if (date.monthValue >= 10) {
            month.setSpan(font.montserratBold.getTypefaceSpan(), 0, 2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        }
        month.setSpan(font.suitBold.getTypefaceSpan(), month.lastIndex, month.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        binding.searchcalendar.monthText.text = month
        binding.searchcalendar.yeartext.text = date.format(yearFormatter)

        //generate date lists
        viewModel.setDayList()
        val dayList = viewModel.dayList.value!!
        //recyclerview setting
        if (binding.searchcalendar.mainCalendar.adapter == null) {
            binding.searchcalendar.mainCalendar.adapter = CalendarModaleAdapter(context, dayList, viewModel.calendarValue.value!!, date.monthValue)
        }
        val manager: RecyclerView.LayoutManager = GridLayoutManager(context, 7)
        binding.searchcalendar.mainCalendar.itemAnimator = null
        binding.searchcalendar.mainCalendar.layoutManager = manager
        (binding.searchcalendar.mainCalendar.adapter as CalendarModaleAdapter).apply {
            setOnItemClickListener(object : CalendarModaleAdapter.OnItemClickListener {
                override fun onItemClick(position: Int) {
                    viewModel.setCalendar(position, null)
                    viewModel.setOption(SearchViewModel.PERIOD)
                }
            })

            setData(viewModel.dayList.value!!, viewModel.selectedDate.value!!.monthValue)
        }

        viewModel.calendarValue.observe(this) {
            (binding.searchcalendar.mainCalendar.adapter as CalendarModaleAdapter).apply {
                setPeriod(it)
            }
        }
    }
}