package com.example.mymate.presentation.expense

import android.content.Context
import android.icu.text.DecimalFormat
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.TextWatcher
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
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
import com.example.mymate.presentation.util.CategoryAdapter
import com.example.mymate.util.*
import com.google.android.material.bottomsheet.BottomSheetBehavior
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter

class SearchActivity: AppCompatActivity() {
    private var _binding: ActivitySearchBinding? = null
    private val binding get() = _binding!!
    private var selectedDate = LocalDate.now()

    lateinit var context: Context
    lateinit var calendarVal: CalendarValues
    lateinit var behavioramount: BottomSheetBehavior<ConstraintLayout>
    lateinit var behaviorcalendar: BottomSheetBehavior<ConstraintLayout>
    lateinit var behaviorcategory: BottomSheetBehavior<ConstraintLayout>
    lateinit var behaviorlistup: BottomSheetBehavior<ConstraintLayout>
    //lateinit var iteminfo: ArrayList<CalendarItem>

    private var monthformatter = DateTimeFormatter.ofPattern("MM월")
    private var yearformatter = DateTimeFormatter.ofPattern("yyyy")
    private var formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd")
    private var searchformatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    private val bottomSheets by lazy {
        listOf(
            behavioramount,
            behaviorcalendar,
            behaviorcategory,
            behaviorlistup
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
        if (actionId == EditorInfo.IME_ACTION_NEXT ||
            actionId == EditorInfo.IME_ACTION_DONE ||
            actionId == EditorInfo.IME_ACTION_SEND
        ) { hidekeyboard() }
        false
    }


    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        context = this

        binding.cover.isGone = true

        calendarVal = CalendarValues()

        bottomSheetInit()
        setCalendarView(selectedDate)
        setCategoryView()

        unselected(binding.listupbutton)
        binding.searchlistup.listfromrecent.setTextColor(ContextCompat.getColor(context, R.color.black_text))

        binding.searchcalendar.monthLast.setOnClickListener {
            selectedDate = selectedDate.minusMonths(1)
            calendarVal.init()
            setCalendarView(selectedDate)
        }

        binding.searchcalendar.monthNext.setOnClickListener {
            selectedDate = selectedDate.plusMonths(1)
            calendarVal.init()
            setCalendarView(selectedDate)
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
    }

    private fun toggleBottomSheet(button: TextView, sheet: BottomSheetBehavior<*>, defaultText: String) {
        val isExpanded = sheet.state == BottomSheetBehavior.STATE_EXPANDED
        if (isExpanded) {
            sheet.state = BottomSheetBehavior.STATE_COLLAPSED
            binding.cover.isGone = true
            if (button.text == defaultText) unselected(button)
        } else {
            collapseAllExcept(sheet)
            sheet.state = BottomSheetBehavior.STATE_EXPANDED
            binding.cover.isGone = false
            selected(button)
        }
    }

    private fun bottomSheetInit() {
        behavioramount = BottomSheetBehavior.from(binding.searchamount.root)
        behaviorcalendar = BottomSheetBehavior.from(binding.searchcalendar.root)
        behaviorcategory = BottomSheetBehavior.from(binding.searchcategory.root)
        behaviorlistup = BottomSheetBehavior.from(binding.searchlistup.root)

        //클릭 이벤트 막기 위한 빈 click listener
        binding.searchamount.root.setOnClickListener {

        }

        binding.searchcalendar.root.setOnClickListener {

        }

        binding.searchcategory.root.setOnClickListener {

        }

        binding.searchlistup.root.setOnClickListener {

        }

        binding.refreshButton.setOnClickListener {
            binding.run {
                amountbutton.text = "가격"
                categorybutton.text = "카테고리"
                listupbutton.text = "최신순"
                searchlistup.listfromrecent.setTextColor(ContextCompat.getColor(context, R.color.black_text))
                calendarbutton.text = "기간"
                unselected(listupbutton)
                unselected(amountbutton)
                unselected(categorybutton)
                unselected(calendarbutton)
                startgraphic.isGone = false
                searchlistcontainer.adapter = SearchListContainerAdapter(ArrayList())
                searchlistcontainer.layoutManager = LinearLayoutManager(context)
            }
            calendarVal = CalendarValues()
            calendarVal.init()
            selectedDate = LocalDate.now()
            setCalendarView(selectedDate)
            setCategoryView()
        }

        binding.calendarbutton.setOnClickListener {
            toggleBottomSheet(binding.calendarbutton, behaviorcalendar, "기간")
        }

        binding.amountbutton.setOnClickListener {
            toggleBottomSheet(binding.amountbutton, behavioramount, "가격")
        }

        binding.categorybutton.setOnClickListener {
            toggleBottomSheet(binding.categorybutton, behaviorcategory, "카테고리")
        }

        binding.listupbutton.setOnClickListener {
            toggleBottomSheet(binding.listupbutton, behaviorlistup, "정렬")
            if (binding.amountbutton.text != "가격" || binding.calendarbutton.text != "기간" || binding.categorybutton.text != "카테고리") {
                search()
            }
        }

        binding.cover.setOnClickListener {
            collapseAllExcept(null)
            if (binding.calendarbutton.text == "기간") {
                unselected(binding.calendarbutton)
            }
            if (binding.amountbutton.text == "가격") {
                unselected(binding.amountbutton)
            }
            if (binding.categorybutton.text == "카테고리") {
                unselected(binding.categorybutton)
            }
            if (binding.calendarbutton.text == "기간" && binding.amountbutton.text == "가격" && binding.categorybutton.text == "카테고리") {
                binding.startgraphic.isGone = false
            }
        }

        binding.searchamount.amountset.setOnClickListener {
            val minText = binding.searchamount.minimumedit.text.toString()
            val maxText = binding.searchamount.maximumedit.text.toString()
            val minInt = minText.toIntOrNull()
            val maxInt = maxText.toIntOrNull()

            val (minValue, maxValue) = when {
                minInt != null && maxInt != null && minInt > maxInt -> maxInt to minInt
                else -> minInt to maxInt
            }

            val formatter = DecimalFormat("#,###")
            val minimum = minValue?.let { formatter.format(it) } ?: ""
            val maximum = maxValue?.let { formatter.format(it) } ?: ""

            var amount = when {
                minimum.isEmpty() && maximum.isEmpty() -> {
                    unselected(binding.amountbutton)
                    "가격"
                }
                minimum.isEmpty() -> "~ ${maximum}원"
                maximum.isEmpty() -> "${minimum}원 ~"
                else -> "${minimum}원 ~ ${maximum}원"
            }
            binding.amountbutton.text = amount
            behavioramount.state = BottomSheetBehavior.STATE_COLLAPSED
            binding.cover.isGone = true
            search()
        }

        binding.searchamount.minimumedit.setOnEditorActionListener(editorActionListener)
        binding.searchamount.maximumedit.setOnEditorActionListener(editorActionListener)

        binding.searchamount.maximumedit.addTextChangedListener(textWatcher)
        binding.searchamount.minimumedit.addTextChangedListener(textWatcher)

        binding.searchcalendar.calendarset.setOnClickListener {
            collapseAllExcept(null)
            if (binding.calendarbutton.text == "기간") {
                unselected(binding.calendarbutton)
            }
            search()
        }

        binding.searchcategory.categoryset.setOnClickListener {
            collapseAllExcept(null)
            if (binding.categorybutton.text == "카테고리") {
                unselected(binding.categorybutton)
            }
            search()
        }

        binding.searchlistup.listfromrecent.setOnClickListener {
            collapseAllExcept(null)
            binding.listupbutton.text = "최신순"
            binding.searchlistup.listfromrecent.setTextColor(ContextCompat.getColor(context, R.color.black_text))
            binding.searchlistup.listfromold.setTextColor(ContextCompat.getColor(context, R.color.graylight_text))
            if (binding.amountbutton.text != "가격" || binding.calendarbutton.text != "기간" || binding.categorybutton.text != "카테고리") {
                search()
            }
        }

        binding.searchlistup.listfromold.setOnClickListener {
            collapseAllExcept(null)
            binding.listupbutton.text = "과거순"
            binding.searchlistup.listfromrecent.setTextColor(ContextCompat.getColor(context, R.color.graylight_text))
            binding.searchlistup.listfromold.setTextColor(ContextCompat.getColor(context, R.color.black_text))
            if (binding.amountbutton.text != "가격" || binding.calendarbutton.text != "기간" || binding.categorybutton.text != "카테고리") {
                search()
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

    private fun search() {
        val dayList = dayInMonthArray(selectedDate)
        var firstday = if (calendarVal.firstDay != -1) {
            dayList[calendarVal.firstDay]?.format(searchformatter) ?: "2021-11-06"
        } else {
            "2021-11-06"
        }
        var lastday = if (calendarVal.lastDay != -1) {
            dayList[calendarVal.lastDay]?.format(searchformatter) ?: LocalDate.now().format(searchformatter)
        } else {
            LocalDate.now().format(searchformatter)
        }

        var newest = true
        var expense_amount_max = "${Integer.MAX_VALUE}"
        var expense_amount_min = "0"

        val text = binding.amountbutton.text.toString()
        if (text != "가격") {
            val numbers = Regex("""[\d,]+""").findAll(text).map { it.value.replace(",", "") }.toList()
            when (numbers.size) {
                1 -> {
                    if (text.startsWith("~")) {
                        expense_amount_max = numbers[0]
                    } else {
                        expense_amount_min = numbers[0]
                    }
                }
                2 -> {
                    expense_amount_min = numbers[0]
                    expense_amount_max = numbers[1]
                }
            }
        }

        newest = binding.listupbutton.text != "과거순"
        var categoryToSend = Category.values().find { it.displayName == binding.categorybutton.text.toString() }?.name ?: "ETC"

        /* var retrofit = RetrofitClientInstance.client
        var endpoint = retrofit?.create(searchExpense::class.java)
        var accessToken = ""
        runBlocking {
            accessToken = userRepo.userAccessReadFlow.first().toString()
        }
        endpoint!!.searchExpense("Bearer $accessToken", expense_amount_min = expense_amount_min, expense_amount_max = expense_amount_max, sorted_by_newest = newest, expense_date_min = firstday, expense_date_max = lastday, expense_category_name = categorytosend).enqueue(object : Callback<SearchResponse> {
            override fun onResponse(call: Call<SearchResponse>, response: Response<SearchResponse>) {
                if (response.isSuccessful) {
                    Log.d("SEARCH!!!", "성공")
                    var listitem = response.body()!!.data.expenses
                    var itemlist = ArrayList<ArrayList<ExpenseSummary>>()
                    var index = 0
                    for (i in 0 until listitem.size) {
                        if (i == 0) {
                            itemlist.add(ArrayList<ExpenseSummary>())
                            itemlist[0].add(listitem[0])
                        } else if (listitem[i].expenseDate != listitem[i-1].expenseDate) {
                            itemlist.add(ArrayList<ExpenseSummary>())
                            index += 1
                            itemlist[index].add(listitem[i])
                        } else {
                            itemlist[index].add(listitem[i])
                        }
                    }
                    itemlist.add(listitem)
                    val manager: RecyclerView.LayoutManager = LinearLayoutManager(context)
                    val adapter = SearchListContainerAdapter(itemlist)
                    binding.searchlistcontainer.adapter = adapter
                    binding.searchlistcontainer.layoutManager = manager
                }
            }

            override fun onFailure(call: Call<SearchResponse>, t: Throwable) {
                Toast.makeText(context, "연결 실패(검색)", Toast.LENGTH_SHORT).show()
            }

        }) */
    }

    //Todo: persistant bottom sheet가 올라가 있는 상태에서 hidekeyboard test할 것
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
        var tagList = arrayListOf<Boolean>()
        for (i in 1 .. 8) {
            tagList.add(false)
        }
        val dataList = arrayListOf<Boolean>()
        for (i in 1 .. 8) {
            dataList.add(false)
        }
        var dataname = arrayListOf<String>()
        for (i in 1 .. 8) {
            dataname.add("")
        }
        val adapter = CategoryAdapter(context, tagList)
        val manager: RecyclerView.LayoutManager = GridLayoutManager(context, 3)

        var data = ""

        binding.searchcategory.categorylist.itemAnimator = null
        binding.searchcategory.categorylist.layoutManager = manager
        binding.searchcategory.categorylist.adapter = adapter.apply {
            setOnItemClickListener(object : CategoryAdapter.OnItemClickListener {
                override fun onItemClick(position: Int) {
                    if (dataList[position]) {
                        dataList[position] = false
                        data = ""
                        binding.categorybutton.text = "카테고리"
                        binding.searchcategory.categoryset.setBackgroundResource(R.drawable.button_loginbardefault)
                    } else {
                        for (i in 0 until dataList.size) {
                            dataList[i] = false
                        }
                        dataList[position] = true
                        data = Category.fromIndex(position).displayName
                        binding.categorybutton.text = data
                        binding.searchcategory.categoryset.setBackgroundResource(R.drawable.button_loginbarselected)
                    }
                }

            })
        }
    }

    private fun setCalendarView(date: LocalDate) {
        //calendar header
        val month = SpannableStringBuilder(selectedDate.format(monthformatter))
        val font = FontManager
        if (selectedDate.monthValue < 10) {
            month.setSpan(font.montserratBold.getTypefaceSpan(), 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            month.setSpan(font.suitBold.getTypefaceSpan(), month.lastIndex, month.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        } else if (selectedDate.monthValue >= 10) {
            month.setSpan(font.montserratBold.getTypefaceSpan(), 0, 2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            month.setSpan(font.suitBold.getTypefaceSpan(), month.lastIndex, month.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        }
        binding.searchcalendar.monthText.text = month
        binding.searchcalendar.yeartext.text = selectedDate.format(yearformatter)
        //generate date lists
        val dayList = dayInMonthArray(date)
        //recyclerview setting
        val adapter = CalendarModaleAdapter(context, dayList, calendarVal, selectedDate.monthValue)
        val manager: RecyclerView.LayoutManager = GridLayoutManager(context, 7)
        binding.searchcalendar.mainCalendar.itemAnimator = null
        binding.searchcalendar.mainCalendar.layoutManager = manager
        binding.searchcalendar.mainCalendar.adapter = adapter.apply {
            setOnItemClickListener(object : CalendarModaleAdapter.OnItemClickListener {
                override fun onItemClick(value: CalendarValues, position: Int) {
                    val firstDayText = if (calendarVal.firstDay >= 0) {
                        dayList.getOrNull(calendarVal.firstDay)?.format(formatter).orEmpty()
                    } else ""
                    val lastDayText = if (calendarVal.lastDay >= 0) {
                        dayList.getOrNull(calendarVal.lastDay)?.format(formatter).orEmpty()
                    } else ""
                    val calendarText = when {
                        firstDayText.isNotEmpty() && lastDayText.isNotEmpty() -> "$firstDayText-$lastDayText"
                        firstDayText.isNotEmpty() -> firstDayText
                        lastDayText.isNotEmpty() -> lastDayText
                        else -> "기간"
                    }
                    binding.calendarbutton.text = calendarText
                    if (calendarText == "기간") {
                        unselected(binding.calendarbutton)
                        binding.searchcalendar.calendarset.setBackgroundResource(R.drawable.button_loginbardefault)
                    } else {
                        selected(binding.calendarbutton)
                        binding.searchcalendar.calendarset.setBackgroundResource(R.drawable.button_loginbarselected)
                    }
                }
            })
        }
    }

    private fun dayInMonthArray(date: LocalDate): ArrayList<LocalDate?> {
        val dayList = ArrayList<LocalDate?>()
        val lastDay = YearMonth.from(date).lengthOfMonth()
        val firstDay = date.withDayOfMonth(1)
        val dayOfWeek = firstDay.dayOfWeek.value
        val prevMonth = date.minusMonths(1)
        val prevMonthLastDay = prevMonth.lengthOfMonth()
        val nextMonth = date.plusMonths(1).withDayOfMonth(1)

        if (firstDay.dayOfWeek == DayOfWeek.SUNDAY) {
            for (i in 1 .. lastDay) {
                dayList.add(LocalDate.of(date.year, date.monthValue, i))
            }
            val daysToFill = 42 - lastDay
            for (i in 0 until daysToFill) {
                dayList.add(nextMonth.plusDays(i.toLong()))
            }
        } else {
            var overflowDayCounter = 0
            for (i in 1..42) {
                when {
                    i <= dayOfWeek -> {
                        val day = prevMonthLastDay - (dayOfWeek - i)
                        dayList.add(prevMonth.withDayOfMonth(day))
                    }
                    i > lastDay + dayOfWeek -> {
                        dayList.add(nextMonth.plusDays(overflowDayCounter.toLong()))
                        overflowDayCounter++
                    }
                    else -> {
                        val currentDay = i - dayOfWeek
                        dayList.add(LocalDate.of(date.year, date.monthValue, currentDay))
                    }
                }
            }
        }
        return dayList
    }
}