package com.example.mymate

import android.content.Context
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.TextWatcher
import android.text.style.TypefaceSpan
import android.util.Log
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isGone
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.example.mymate.databinding.ActivitySearchBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.internal.format
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.create
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import kotlin.math.max
import kotlin.math.min

class SearchActivity: AppCompatActivity() {
    lateinit var binding: ActivitySearchBinding
    lateinit var context: Context
    lateinit var calendarVal: CalendarValues
    lateinit var behavioramount: BottomSheetBehavior<ConstraintLayout>
    lateinit var behaviorcalendar: BottomSheetBehavior<ConstraintLayout>
    lateinit var behaviorcategory: BottomSheetBehavior<ConstraintLayout>
    lateinit var behaviorlistup: BottomSheetBehavior<ConstraintLayout>
    lateinit var iteminfo: ArrayList<calendarItem>
    lateinit var userRepo: DataStoreRepoUser

    private var selectedDate = LocalDate.now()
    private var year = ""
    private var month = ""
    private var day = ""
    private var monthformatter = DateTimeFormatter.ofPattern("MM월")
    private var dayformatter = DateTimeFormatter.ofPattern("dd")
    private var yearformatter = DateTimeFormatter.ofPattern("yyyy")
    private var formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd")
    private var searchformatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        context = this
        userRepo = DataStoreRepoUser(dataStore)

        binding.cover.isGone = true

        calendarVal = CalendarValues()
        calendarVal.firstDay = -1
        calendarVal.lastDay = -1

        bottomSheetInit()
        setCalendarView(selectedDate)
        setCategoryView()

        selected(binding.listupbutton)
        binding.listupbutton.text = "최신순"

        binding.searchcalendar.monthLast.setOnClickListener {
            selectedDate = selectedDate.minusMonths(1)
            calendarVal.firstDay = -1
            calendarVal.lastDay = -1
            setCalendarView(selectedDate)
        }

        binding.searchcalendar.monthNext.setOnClickListener {
            selectedDate = selectedDate.plusMonths(1)
            calendarVal.firstDay = -1
            calendarVal.lastDay = -1
            setCalendarView(selectedDate)
        }

        binding.backbtn.setOnClickListener {
            finish()
        }
    }

    @RequiresApi(Build.VERSION_CODES.P)
    private fun bottomSheetInit() {
        behavioramount = BottomSheetBehavior.from(binding.searchamount.root)
        behaviorcalendar = BottomSheetBehavior.from(binding.searchcalendar.root)
        behaviorcategory = BottomSheetBehavior.from(binding.searchcategory.root)
        behaviorlistup = BottomSheetBehavior.from(binding.searchlistup.root)

        binding.searchamount.root.setOnClickListener {

        }

        binding.searchcalendar.root.setOnClickListener {

        }

        binding.searchcategory.root.setOnClickListener {

        }

        binding.searchlistup.root.setOnClickListener {

        }

        binding.refreshButton.setOnClickListener {
            binding.amountbutton.text = "가격"
            binding.categorybutton.text = "카테고리"
            binding.listupbutton.text = "최신순"
            binding.searchlistup.listfromrecent.setTextColor(ContextCompat.getColor(context, R.color.black_text))
            binding.calendarbutton.text = "기간"
            unselected(binding.listupbutton)
            unselected(binding.amountbutton)
            unselected(binding.categorybutton)
            unselected(binding.calendarbutton)
            var itemlistrefresh = ArrayList<ArrayList<ExpenseList>>()
            val adapter = SearchListContainerAdapter(itemlistrefresh)
            val manager = LinearLayoutManager(context)
            binding.searchlistcontainer.adapter = adapter
            binding.searchlistcontainer.layoutManager = manager
            calendarVal = CalendarValues()
            calendarVal.firstDay = -1
            calendarVal.lastDay = -1
            selectedDate = LocalDate.now()
            setCalendarView(selectedDate)
            setCategoryView()
        }

        binding.calendarbutton.setOnClickListener {
            if (behaviorcalendar.state == BottomSheetBehavior.STATE_EXPANDED) {
                behaviorcalendar.state = BottomSheetBehavior.STATE_COLLAPSED
                if (binding.calendarbutton.text == "기간") {
                    unselected(binding.calendarbutton)
                }
                binding.cover.isGone = true
            } else {
                behaviorcalendar.state = BottomSheetBehavior.STATE_EXPANDED
                selected(binding.calendarbutton)
                binding.cover.isGone = false
            }
            behavioramount.state = BottomSheetBehavior.STATE_COLLAPSED
            behaviorlistup.state = BottomSheetBehavior.STATE_COLLAPSED
            behaviorcategory.state = BottomSheetBehavior.STATE_COLLAPSED

            search()
        }

        binding.amountbutton.setOnClickListener {
            if (behavioramount.state == BottomSheetBehavior.STATE_EXPANDED) {
                behavioramount.state = BottomSheetBehavior.STATE_COLLAPSED
                binding.cover.isGone = true
                if (binding.amountbutton.text == "가격") {
                    unselected(binding.amountbutton)
                }
            } else {
                behavioramount.state = BottomSheetBehavior.STATE_EXPANDED
                binding.cover.isGone = false
                selected(binding.amountbutton)
            }
            behaviorcalendar.state = BottomSheetBehavior.STATE_COLLAPSED
            behaviorlistup.state = BottomSheetBehavior.STATE_COLLAPSED
            behaviorcategory.state = BottomSheetBehavior.STATE_COLLAPSED
            search()
        }

        binding.categorybutton.setOnClickListener {
            if (behaviorcategory.state == BottomSheetBehavior.STATE_EXPANDED) {
                behaviorcategory.state = BottomSheetBehavior.STATE_COLLAPSED
                binding.cover.isGone = true
                if (binding.categorybutton.text == "카테고리") {
                    unselected(binding.categorybutton)
                }
            } else {
                behaviorcategory.state = BottomSheetBehavior.STATE_EXPANDED
                binding.cover.isGone = false
                selected(binding.categorybutton)
            }
            behaviorcalendar.state = BottomSheetBehavior.STATE_COLLAPSED
            behavioramount.state = BottomSheetBehavior.STATE_COLLAPSED
            behaviorlistup.state = BottomSheetBehavior.STATE_COLLAPSED
            search()
        }

        binding.listupbutton.setOnClickListener {
            if (behaviorlistup.state == BottomSheetBehavior.STATE_EXPANDED) {
                behaviorlistup.state = BottomSheetBehavior.STATE_COLLAPSED
                binding.cover.isGone = true
                if (binding.listupbutton.text == "정렬") {
                    unselected(binding.listupbutton)
                }
            } else {
                behaviorlistup.state = BottomSheetBehavior.STATE_EXPANDED
                binding.cover.isGone = false
            }
            behaviorcalendar.state = BottomSheetBehavior.STATE_COLLAPSED
            behavioramount.state = BottomSheetBehavior.STATE_COLLAPSED
            behaviorcategory.state = BottomSheetBehavior.STATE_COLLAPSED
            search()
        }

        binding.cover.setOnClickListener {
            behaviorcalendar.state = BottomSheetBehavior.STATE_COLLAPSED
            behavioramount.state = BottomSheetBehavior.STATE_COLLAPSED
            behaviorcategory.state = BottomSheetBehavior.STATE_COLLAPSED
            behaviorlistup.state = BottomSheetBehavior.STATE_COLLAPSED
            if (binding.calendarbutton.text == "기간") {
                unselected(binding.calendarbutton)
            }
            if (binding.amountbutton.text == "가격") {
                unselected(binding.amountbutton)
            }
            if (binding.listupbutton.text == "정렬") {
                unselected(binding.listupbutton)
            }
            if (binding.categorybutton.text == "카테고리") {
                unselected(binding.categorybutton)
            }
            search()
            binding.cover.isGone = true
        }

        binding.searchamount.amountset.setOnClickListener {
            var minimum = ""
            var maximum = ""
            var amount = "가격"
            if (binding.searchamount.minimumedit.text.isNotEmpty()) {
                minimum = digitprocessing(binding.searchamount.minimumedit.text.toString())
            }
            if (binding.searchamount.maximumedit.text.isNotEmpty()) {
                maximum = digitprocessing(binding.searchamount.maximumedit.text.toString())
            }
            if (minimum == "" && maximum == "") {
                binding.amountbutton.text = amount
                unselected(binding.amountbutton)
            } else if (minimum == "") {
                amount = "~ " + maximum + "원"
                binding.amountbutton.text = amount
            } else if (maximum == "") {
                amount = minimum + "원 ~"
                binding.amountbutton.text = amount
            } else {
                amount = minimum + "원 ~ " + maximum + "원"
                binding.amountbutton.text = amount
            }
            behavioramount.state = BottomSheetBehavior.STATE_COLLAPSED
            binding.cover.isGone = true
            search()
        }

        binding.searchcalendar.calendarset.setOnClickListener {
            behaviorcalendar.state = BottomSheetBehavior.STATE_COLLAPSED
            binding.cover.isGone = true
            if (binding.calendarbutton.text == "기간") {
                unselected(binding.calendarbutton)
            }
            search()
        }

        binding.searchcategory.categoryset.setOnClickListener {
            behaviorcategory.state = BottomSheetBehavior.STATE_COLLAPSED
            binding.cover.isGone = true
            if (binding.categorybutton.text == "카테고리") {
                unselected(binding.categorybutton)
            }
            search()
        }

        binding.searchlistup.listfromrecent.setOnClickListener {
            behaviorlistup.state = BottomSheetBehavior.STATE_COLLAPSED
            binding.listupbutton.text = "최신순"
            binding.searchlistup.listfromrecent.setTextColor(ContextCompat.getColor(context, R.color.black_text))
            binding.searchlistup.listfromold.setTextColor(ContextCompat.getColor(context, R.color.graylight_text))
            binding.cover.isGone = true
            search()
        }

        binding.searchlistup.listfromold.setOnClickListener {
            behaviorlistup.state = BottomSheetBehavior.STATE_COLLAPSED
            binding.listupbutton.text = "과거순"
            binding.searchlistup.listfromrecent.setTextColor(ContextCompat.getColor(context, R.color.graylight_text))
            binding.searchlistup.listfromold.setTextColor(ContextCompat.getColor(context, R.color.black_text))
            binding.cover.isGone = true
            search()
        }
    }

    private fun selected(view: TextView) {
        view.setBackgroundResource(R.drawable.button_selectedboxround)
        view.setTextColor(ContextCompat.getColor(context, R.color.purpleblue_select))
    }

    private fun unselected(view: TextView) {
        view.setBackgroundResource(R.drawable.button_selectboxnull)
        view.setTextColor(ContextCompat.getColor(context, R.color.graydark_text))
    }

    private fun search() {
        var firstday = ""
        var lastday = ""
        val dayList = dayInMonthArray(selectedDate)
        if (calendarVal.firstDay != -1) {
            firstday = dayList[calendarVal.firstDay]?.format(searchformatter) ?: "2021-11-06"
        } else {
            firstday = "2021-11-06"
        }
        if (calendarVal.lastDay != -1) {
            lastday = dayList[calendarVal.lastDay]?.format(searchformatter) ?: LocalDate.now().format(searchformatter)
        } else {
            lastday = LocalDate.now().format(searchformatter)
        }

        var newest = true
        var expense_amount_max = "${Integer.MAX_VALUE}"
        var expense_amount_min = "0"

        if (binding.amountbutton.text != "가격") {
            if (binding.amountbutton.text.indexOf("원") == binding.amountbutton.text.length - 1) {
                expense_amount_max = (binding.amountbutton.text.substring(binding.amountbutton.text.indexOf(" ") + 1 until binding.amountbutton.text.indexOf("원"))).replace(",", "")
            } else {
                val index = binding.amountbutton.text.indexOf("원")
                if (binding.amountbutton.text.indexOf("원", index + 1) == -1) {
                    expense_amount_min = (binding.amountbutton.text.substring(0 until binding.amountbutton.text.indexOf("원"))).replace(",", "")
                } else {
                    expense_amount_min = (binding.amountbutton.text.substring(0 until binding.amountbutton.text.indexOf("원"))).replace(",", "")
                    val blankindex = binding.amountbutton.text.indexOf(" ")
                    expense_amount_max = (binding.amountbutton.text.substring(binding.amountbutton.text.indexOf(" ", blankindex + 1) + 1 until binding.amountbutton.text.indexOf("원", index + 1))).replace(",", "")
                }
            }
        }

        newest = binding.listupbutton.text != "과거순"
        var categorytosend = ""
        when (binding.categorybutton.text) {
            "식비" -> categorytosend = "FOOD"
            "쇼핑" -> categorytosend = "SHOPPING"
            "교통" -> categorytosend = "TRANSPORT"
            "의료" -> categorytosend = "MEDICAL"
            "생활" -> categorytosend = "HOUSE_ITEM"
            "교육" -> categorytosend = "EDUCATION"
            "기타" -> categorytosend = "ETC"
            "고지서" -> categorytosend = "BILLS"
        }

        var retrofit = RetrofitClientInstance.client
        var endpoint = retrofit?.create(searchExpense::class.java)
        var accessToken = ""
        runBlocking {
            accessToken = userRepo.userAccessReadFlow.first().toString()
        }
        endpoint!!.searchExpense("Bearer $accessToken", expense_amount_min = expense_amount_min, expense_amount_max = expense_amount_max, sorted_by_newest = newest, expense_date_min = firstday, expense_date_max = lastday, expense_category_name = categorytosend).enqueue(object : Callback<searchResponse> {
            override fun onResponse(call: Call<searchResponse>, response: Response<searchResponse>) {
                if (response.isSuccessful) {
                    Log.d("SEARCH!!!", "성공")
                    var listitem = response.body()!!.data.expenses
                    var itemlist = ArrayList<ArrayList<ExpenseList>>()
                    itemlist.add(listitem)
                    val manager: RecyclerView.LayoutManager = LinearLayoutManager(context)
                    val adapter = SearchListContainerAdapter(itemlist)
                    binding.searchlistcontainer.adapter = adapter
                    binding.searchlistcontainer.layoutManager = manager
                }
                Log.d("SEARCH!!!", "반환형 이상함")
            }

            override fun onFailure(call: Call<searchResponse>, t: Throwable) {
                Toast.makeText(context, "연결 실패(검색)", Toast.LENGTH_SHORT).show()
            }

        })
    }

    //오류 있음: persistant bottom sheet가 올라가 있는 상태에서 hidekeyboard 안됨
    private fun hidekeyboard() {
        //키보드 내리기
        val imm = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        val focusview = currentFocus
        imm.hideSoftInputFromWindow(this.window.decorView.applicationWindowToken, 0)
        focusview?.clearFocus()
    }

    private fun digitprocessing(digits: String): String {
        var textlength = digits.length
        var processed = ""
        while (0 < textlength) {
            var substring1 = ""
            if (textlength == 3) {
                if (processed == "") {
                    processed = digits.substring(0 until 3)
                } else {
                    processed = digits.substring(0 until 3) + "," + processed
                }
            } else if (textlength > 3) {
                substring1 = digits.substring(textlength - 3 until textlength)
                if (processed == "") {
                    processed = substring1
                } else {
                    processed = "$substring1,$processed"
                }
            } else {
                substring1 = digits.substring(0 until textlength)
                processed = "$substring1,$processed"
            }

            textlength -= 3
        }

        return processed
    }

    private fun setCategoryView() {
        val defaultimgList = arrayListOf<Drawable>()
        defaultimgList.add(ContextCompat.getDrawable(context, R.drawable.icon_food_default)!!)
        defaultimgList.add(ContextCompat.getDrawable(context, R.drawable.icon_life_default)!!)
        defaultimgList.add(ContextCompat.getDrawable(context, R.drawable.icon_shopping_default)!!)
        defaultimgList.add(ContextCompat.getDrawable(context, R.drawable.icon_traffic_default)!!)
        defaultimgList.add(ContextCompat.getDrawable(context, R.drawable.icon_medical_default)!!)
        defaultimgList.add(ContextCompat.getDrawable(context, R.drawable.icon_bill_default)!!)
        defaultimgList.add(ContextCompat.getDrawable(context, R.drawable.icon_edu_default)!!)
        defaultimgList.add(ContextCompat.getDrawable(context, R.drawable.icon_etc_default)!!)

        val selectedimgList = arrayListOf<Drawable>()
        selectedimgList.add(ContextCompat.getDrawable(context, R.drawable.icon_food_select)!!)
        selectedimgList.add(ContextCompat.getDrawable(context, R.drawable.icon_life_select)!!)
        selectedimgList.add(ContextCompat.getDrawable(context, R.drawable.icon_shopping_select)!!)
        selectedimgList.add(ContextCompat.getDrawable(context, R.drawable.icon_traffic_select)!!)
        selectedimgList.add(ContextCompat.getDrawable(context, R.drawable.icon_medical_select)!!)
        selectedimgList.add(ContextCompat.getDrawable(context, R.drawable.icon_bill_select)!!)
        selectedimgList.add(ContextCompat.getDrawable(context, R.drawable.icon_edu_select)!!)
        selectedimgList.add(ContextCompat.getDrawable(context, R.drawable.icon_etc_select)!!)

        val categorynameList = arrayListOf<String>()
        categorynameList.add("식비")
        categorynameList.add("생활")
        categorynameList.add("쇼핑")
        categorynameList.add("교통")
        categorynameList.add("의료")
        categorynameList.add("고지서")
        categorynameList.add("교육")
        categorynameList.add("기타")

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
        val adapter = CategoryAdapter(context, defaultimgList, categorynameList, tagList, selectedimgList)
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
                    } else {
                        for (i in 0 until dataList.size) {
                            dataList[i] = false
                        }
                        dataList[position] = true
                        data = categorynameList[position]
                        binding.categorybutton.text = data
                    }
                }

            })
        }
    }

    @RequiresApi(Build.VERSION_CODES.P)
    private fun setCalendarView(date: LocalDate) {
        //calendar header
        val month = SpannableStringBuilder(selectedDate.format(monthformatter))
        val montBoldTypeface = Typeface.create(ResourcesCompat.getFont(context, R.font.montserrat_bold), Typeface.NORMAL)
        val suitBoldTypeface = Typeface.create(ResourcesCompat.getFont(context, R.font.suit_bold), Typeface.NORMAL)
        if (selectedDate.monthValue < 10) {
            month.setSpan(TypefaceSpan(montBoldTypeface), 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            month.setSpan(TypefaceSpan(suitBoldTypeface), month.lastIndex, month.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        } else if (selectedDate.monthValue >= 10) {
            month.setSpan(TypefaceSpan(montBoldTypeface), 0, 2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            month.setSpan(TypefaceSpan(suitBoldTypeface), month.lastIndex, month.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        }
        binding.searchcalendar.monthText.text = month
        binding.searchcalendar.yeartext.text = selectedDate.format(yearformatter)
        //generate date lists
        iteminfo = arrayListOf<calendarItem>()
        val dayList = dayInMonthArray(date)
        //recyclerview setting
        val adapter = CalendarModaleAdapter(context, dayList, iteminfo, calendarVal)
        val manager: RecyclerView.LayoutManager = GridLayoutManager(context, 7)
        var firstdaytext = ""
        var lastdaytext = ""
        binding.searchcalendar.mainCalendar.itemAnimator = null
        binding.searchcalendar.mainCalendar.layoutManager = manager
        binding.searchcalendar.mainCalendar.adapter = adapter.apply {
            setOnItemClickListener(object : CalendarModaleAdapter.OnItemClickListener {
                override fun onItemClick(item: calendarItem, position: Int) {
                    if (calendarVal.firstDay != -1) {
                        firstdaytext = dayList[calendarVal.firstDay]?.format(formatter) ?: ""
                    } else {
                        firstdaytext = ""
                    }
                    if (calendarVal.lastDay != -1) {
                        lastdaytext = dayList[calendarVal.lastDay]?.format(formatter) ?: ""
                    } else {
                        lastdaytext = ""
                    }
                    val calendartext = "$firstdaytext-$lastdaytext"
                    binding.calendarbutton.text = calendartext
                    selected(binding.calendarbutton)
                    if (firstdaytext == "") {
                        if (lastdaytext != "") {
                            binding.calendarbutton.text = lastdaytext
                        } else {
                            binding.calendarbutton.text = "기간"
                            unselected(binding.calendarbutton)
                        }
                    } else if (lastdaytext == "") {
                        binding.calendarbutton.text = firstdaytext
                    }
                }
            })
        }
    }

    private fun dayInMonthArray(date: LocalDate): ArrayList<LocalDate?> {
        var dayList = ArrayList<LocalDate?>()
        var yearMonth = YearMonth.from(date)
        var lastDay = yearMonth.lengthOfMonth()
        var firstDay = date.withDayOfMonth(1)
        var dayOfWeek = firstDay.dayOfWeek.value
        var nowdate: Int = 0
        var tempmonth = date
        var tempday = date
        var tempint = 0
        val dayformat = DateTimeFormatter.ofPattern("dd")
        nowdate = date.format(dayformat).toInt()
        if (firstDay.dayOfWeek == DayOfWeek.SUNDAY) {
            for (i in 1..yearMonth.lengthOfMonth()) {
                if (nowdate == i) {
                    dayList.add(LocalDate.of(date.year, date.monthValue, i))
                    iteminfo.add(calendarItem(false, false, false))
                } else {
                    dayList.add(LocalDate.of(date.year, date.monthValue, i))
                    iteminfo.add(calendarItem(false, false, false))
                }
            }
            for (i in 1 .. 11) {
                tempmonth = date.plusMonths(1)
                tempday = tempmonth.withDayOfMonth(1).plusDays(tempint.toLong())
                dayList.add(tempday)
                iteminfo.add(calendarItem(false, false, true))
                tempint++
            }
        } else {
            for (i in 1..42) {
                if (i <= dayOfWeek) {
                    tempmonth = date.minusMonths(1)
                    tempday = tempmonth.withDayOfMonth(tempmonth.lengthOfMonth())
                        .minusDays(dayOfWeek.toLong() - i)
                    dayList.add(tempday)
                    iteminfo.add(calendarItem(false, false, true))
                } else if (i > lastDay + dayOfWeek) {
                    tempmonth = date.plusMonths(1)
                    tempday = tempmonth.withDayOfMonth(1).plusDays(tempint.toLong())
                    dayList.add(tempday)
                    iteminfo.add(calendarItem(false, false, true))
                    tempint++
                } else {
                    if (nowdate == (i - dayOfWeek)) {
                        dayList.add(LocalDate.of(date.year, date.monthValue, i - dayOfWeek))
                        iteminfo.add(calendarItem(false, false, false))
                    } else {
                        dayList.add(LocalDate.of(date.year, date.monthValue, i - dayOfWeek))
                        iteminfo.add(calendarItem(false, false, false))
                    }
                }
            }
        }

        return dayList
    }
}