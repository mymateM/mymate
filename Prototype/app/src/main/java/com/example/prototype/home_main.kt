package com.example.prototype

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.text.style.TypefaceSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.textInputServiceFactory
import androidx.compose.ui.text.font.Typeface
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.text.color
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.prototype.databinding.HomeMainBinding
import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.math.abs


data class request_home (
    @SerializedName("message") var message: String = "",
    @SerializedName("status") var status: String = "",
    @SerializedName("data") var datareceived: datahome = datahome()
        )

data class datahome (
    @SerializedName("user") var userhome_home: userhome = userhome(),
    @SerializedName("room_mates") var roomt_mates_home: ArrayList<roomt_mates> = ArrayList(),
    @SerializedName("household") var household_home: household = household(),
        )

data class userhome (
    @SerializedName("user_id") var user_id: String = "",
    @SerializedName("user_name") var user_name: String = "",
    @SerializedName("user_by_now_budget_ratio") var user_now_buget_ratio: String = "",
    @SerializedName("user_ratio") var user_ratio: String = "",
    @SerializedName("user_title_name") var user_title_name: String = "",
    @SerializedName("user_image_url") var user_image: String = "",
    @SerializedName("user_title_days") var user_title_days: String = ""
        )

data class roomt_mates (
    @SerializedName("user_id") var user_id: String = "",
    @SerializedName("user_name") var user_name: String = "",
    @SerializedName("user_by_now_budget_ratio") var user_now_buget_ratio: String = "",
    @SerializedName("user_ratio") var user_ratio: String = "",
    @SerializedName("user_title_name") var user_title_name: String = "",
    @SerializedName("user_image_url") var user_image: String = "",
    @SerializedName("user_title_days") var user_title_days: String = ""
        )

data class household (
    @SerializedName("household_id") var household_id: String = "",
    @SerializedName("household_by_now_budget_ratio") var household_now_budget_ratio: String = "",
    @SerializedName("household_settlement_d_day") var household_settlement_date: String = "",
    @SerializedName("household_by_previous_expense") var household_previous_expense: String = "",
    @SerializedName("household_by_now_expense") var household_now_expense: String = "",
    @SerializedName("household_now_expense_diff") var household_now_expense_diff: String = "",
    @SerializedName("household_budget_over_warn") var household_budget_over_warn: String = ""
        )

data class listItem (
    var profilepic: Int = R.drawable.basicprofile,
    var name: String = "",
    var title: String = "",
    var ratio: String = "",
    var color: Int = 0,
    var flag: String = ""
        )

class home_main : Fragment() {
    lateinit var mainActivity : MainActivity
    private var hbinding : HomeMainBinding? = null
    private val binding get() = hbinding!!
    private var requestHome: request_home = request_home()
    lateinit var adapter: HomeAdapter

    override fun onAttach(context: Context) {
        super.onAttach(context)

        mainActivity = context as MainActivity
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        hbinding = HomeMainBinding.inflate(inflater, container, false)
        val alarmIntent = Intent(mainActivity, AlarmActivity::class.java)
        val settlementIntent = Intent(mainActivity, SettlementActivity::class.java)
        binding.alarmbutton.setOnClickListener{startActivity(alarmIntent)}
        var retrofit = RetrofitClientInstance.client
        var endpoint = retrofit?.create(getHome::class.java)
        binding.settlementdate.setOnClickListener {startActivity(settlementIntent)}
        var window: Window = requireActivity().window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.statusBarColor = ContextCompat.getColor(requireActivity(), R.color.homevisualbackground)
        endpoint!!.getHome("1").enqueue(object: Callback<request_home> {
            override fun onResponse(call: Call<request_home>, response: Response<request_home>) {
                requestHome = response.body()!!
                var list: ArrayList<listItem> = ArrayList<listItem>()
                list.add(
                    listItem(
                        R.drawable.blue_profile, "나", requestHome.datareceived.userhome_home.user_title_name, requestHome.datareceived.userhome_home.user_ratio, ContextCompat.getColor(mainActivity, R.color.muteblue), "blue"
                    )
                )
                var matelist: ArrayList<roomt_mates> = ArrayList()
                matelist = requestHome.datareceived.roomt_mates_home
                var matename = ""
                var matetitle = ""
                var materatio = ""
                var matecolor: Int = ContextCompat.getColor(mainActivity, R.color.mainpurple)
                var i = 0
                while (i < matelist.size) {
                    matename = matelist.get(i).user_name
                    matetitle = matelist.get(i).user_title_name
                    materatio = matelist.get(i).user_ratio

                    if (matetitle == "무소비") {
                        matetitle =
                            "무소비 " + requestHome.datareceived.userhome_home.user_title_days.toString() + "일 달성"
                    } else if (matetitle == "위험") {
                        matetitle = "빨간 불! 지출 위험"
                    }

                    if (i % 4 == 0) {
                        matecolor = ContextCompat.getColor(mainActivity, R.color.mainpink)
                        list.add(
                            listItem(R.drawable.pink, matename, matetitle, materatio, matecolor, "pink")
                        )
                    } else if (i % 3 == 0) {
                        matecolor = ContextCompat.getColor(mainActivity, R.color.mainblue)
                        list.add(
                            listItem(R.drawable.blue, matename, matetitle, materatio, matecolor, "blue")
                        )
                    } else if (i % 2 == 0) {
                        matecolor = ContextCompat.getColor(mainActivity, R.color.mainyellow)
                        list.add(
                            listItem(R.drawable.yellow, matename, matetitle, materatio, matecolor, "yellow")
                        )
                    } else {
                        matecolor = ContextCompat.getColor(mainActivity, R.color.maingreen)
                        list.add(
                            listItem(R.drawable.green, matename, matetitle, materatio, matecolor, "green")
                        )
                    }
                    i += 1
                }
                adapter = HomeAdapter(mainActivity, list)
                binding.ratiolist.adapter = adapter
                binding.ratiolist.layoutManager = LinearLayoutManager(mainActivity)

                binding.horizontalguide.setGuidelinePercent(requestHome.datareceived.household_home.household_now_budget_ratio.toFloat() / 100)
                var houseratio = 0.toFloat()
                if (requestHome.datareceived.household_home.household_now_expense_diff.toFloat() < 0) {
                    houseratio =
                        abs(requestHome.datareceived.household_home.household_now_expense_diff.toFloat()) / requestHome.datareceived.household_home.household_previous_expense.toFloat()
                    binding.verticalguide1.setGuidelinePercent(0.65.toFloat() - houseratio * 0.65.toFloat() + 0.35.toFloat())
                    binding.statustextsmall.text = "덜 썼어요"
                } else if (requestHome.datareceived.household_home.household_now_expense_diff.toFloat() > 0) {
                    houseratio =
                        requestHome.datareceived.household_home.household_now_expense_diff.toFloat() / requestHome.datareceived.household_home.household_previous_expense.toFloat()
                    binding.verticalguide1.setGuidelinePercent(0.35.toFloat())
                    binding.verticalguide2.setGuidelinePercent(0.65.toFloat() - houseratio * 0.65.toFloat() + 0.35.toFloat())
                    binding.statustextsmall.text = "더 썼어요"
                } else if (requestHome.datareceived.household_home.household_now_expense_diff.toFloat() == 0.toFloat()) {
                    binding.verticalguide1.setGuidelinePercent(0.35.toFloat())
                    binding.verticalguide2.setGuidelinePercent(0.35.toFloat())
                    binding.statustextbig.text = "저번 달 같은 기간 "
                    binding.statustextsmall.text = "만큼 썼어요"
                }

                var textlength =
                    requestHome.datareceived.household_home.household_now_expense.length
                var totaltext = requestHome.datareceived.household_home.household_now_expense
                var totalexpense = ""

                while (0 < textlength) {
                    var substring1 = ""
                    if (textlength == 3) {
                        if (totalexpense == "") {
                            totalexpense = totaltext.substring(0 until 3)
                        } else {
                            totalexpense = totaltext.substring(0 until 3) + "," + totalexpense
                        }
                    } else if (textlength > 3) {
                        substring1 = totaltext.substring(textlength - 3 until textlength)
                        if (totalexpense == "") {
                            totalexpense = substring1
                        } else {
                            totalexpense = substring1 + "," + totalexpense
                        }
                    } else if (textlength < 3) {
                        substring1 = totaltext.substring(0 until textlength)
                        totalexpense = substring1 + "," + totalexpense
                    }

                    textlength = textlength - 3
                }
                binding.homemiddletext.text = totalexpense
                binding.billtext.text =
                    requestHome.datareceived.household_home.household_now_budget_ratio + "%"
                binding.settlementtext.text =
                    "D-" + requestHome.datareceived.household_home.household_settlement_date

                if (requestHome.datareceived.household_home.household_budget_over_warn == "false") {
                    binding.warningtext.text = "이대로라면 예산을 초과하지 않아요"
                } else if (requestHome.datareceived.household_home.household_budget_over_warn == "true") {
                    binding.warningtext.text = "이대로라면 예산을 초과할 것 같아요"
                }

                totaltext = abs(requestHome.datareceived.household_home.household_now_expense_diff.toInt()).toString()
                textlength = totaltext.length
                totalexpense = ""

                while (0 < textlength) {
                    var substring1 = ""
                    if (textlength == 3) {
                        if (totalexpense == "") {
                            totalexpense = totaltext.substring(0 until 3)
                        } else {
                            totalexpense = totaltext.substring(0 until 3) + "," + totalexpense
                        }
                    } else if (textlength > 3) {
                        substring1 = totaltext.substring(textlength - 3 until textlength)
                        if (totalexpense == "") {
                            totalexpense = substring1
                        } else {
                            totalexpense = substring1 + "," + totalexpense
                        }
                    } else if (textlength < 3) {
                        substring1 = totaltext.substring(0 until textlength)
                        totalexpense = substring1 + "," + totalexpense
                    }

                    textlength = textlength - 3
                }

                binding.statusbilltext.text = totalexpense
            }

            override fun onFailure(call: Call<request_home>, t: Throwable) {
                Toast.makeText(mainActivity, "연결 실패입니다.", Toast.LENGTH_SHORT).show()
            }
        })
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        var retrofit = RetrofitClientInstance.client
        var endpoint = retrofit?.create(getHome::class.java)
        endpoint!!.getHome("1").enqueue(object: Callback<request_home> {
            override fun onResponse(call: Call<request_home>, response: Response<request_home>) {
                requestHome = response.body()!!
                var list: ArrayList<listItem> = ArrayList<listItem>()
                list.add(
                    listItem(
                        R.drawable.blue_profile, "나", requestHome.datareceived.userhome_home.user_title_name, requestHome.datareceived.userhome_home.user_ratio, ContextCompat.getColor(mainActivity, R.color.muteblue), "blue"
                    )
                )
                var matelist: ArrayList<roomt_mates> = ArrayList()
                matelist = requestHome.datareceived.roomt_mates_home
                var matename = ""
                var matetitle = ""
                var materatio = ""
                var matecolor: Int = ContextCompat.getColor(mainActivity, R.color.mainpurple)
                var i = 0
                while (i < matelist.size) {
                    matename = matelist.get(i).user_name
                    matetitle = matelist.get(i).user_title_name
                    materatio = matelist.get(i).user_ratio

                    if (matetitle == "무소비") {
                        matetitle =
                            "무소비 " + requestHome.datareceived.userhome_home.user_title_days.toString() + "일 달성"
                    } else if (matetitle == "위험") {
                        matetitle = "빨간 불! 지출 위험"
                    }

                    if (i % 4 == 0) {
                        matecolor = ContextCompat.getColor(mainActivity, R.color.mainpink)
                        list.add(
                            listItem(R.drawable.pink, matename, matetitle, materatio, matecolor, "pink")
                        )
                    } else if (i % 3 == 0) {
                        matecolor = ContextCompat.getColor(mainActivity, R.color.mainblue)
                        list.add(
                            listItem(R.drawable.blue, matename, matetitle, materatio, matecolor, "blue")
                        )
                    } else if (i % 2 == 0) {
                        matecolor = ContextCompat.getColor(mainActivity, R.color.mainyellow)
                        list.add(
                            listItem(R.drawable.yellow, matename, matetitle, materatio, matecolor, "yellow")
                        )
                    } else {
                        matecolor = ContextCompat.getColor(mainActivity, R.color.maingreen)
                        list.add(
                            listItem(R.drawable.green, matename, matetitle, materatio, matecolor, "green")
                        )
                    }
                    i += 1
                }
                adapter = HomeAdapter(mainActivity, list)
                binding.ratiolist.adapter = adapter
                binding.ratiolist.layoutManager = LinearLayoutManager(mainActivity)

                binding.horizontalguide.setGuidelinePercent(requestHome.datareceived.household_home.household_now_budget_ratio.toFloat() / 100)
                var houseratio = 0.toFloat()
                if (requestHome.datareceived.household_home.household_now_expense_diff.toFloat() < 0) {
                    houseratio =
                        abs(requestHome.datareceived.household_home.household_now_expense_diff.toFloat()) / requestHome.datareceived.household_home.household_previous_expense.toFloat()
                    binding.verticalguide1.setGuidelinePercent(0.65.toFloat() - houseratio * 0.65.toFloat() + 0.35.toFloat())
                    binding.statustextsmall.text = "덜 썼어요"
                } else if (requestHome.datareceived.household_home.household_now_expense_diff.toFloat() > 0) {
                    houseratio =
                        requestHome.datareceived.household_home.household_now_expense_diff.toFloat() / requestHome.datareceived.household_home.household_previous_expense.toFloat()
                    binding.verticalguide1.setGuidelinePercent(0.35.toFloat())
                    binding.verticalguide2.setGuidelinePercent(0.65.toFloat() - houseratio * 0.65.toFloat() + 0.35.toFloat())
                    binding.statustextsmall.text = "더 썼어요"
                } else if (requestHome.datareceived.household_home.household_now_expense_diff.toFloat() == 0.toFloat()) {
                    binding.verticalguide1.setGuidelinePercent(0.35.toFloat())
                    binding.verticalguide2.setGuidelinePercent(0.35.toFloat())
                    binding.statustextbig.text = "저번 달 같은 기간 "
                    binding.statustextsmall.text = "만큼 썼어요"
                }

                var textlength =
                    requestHome.datareceived.household_home.household_now_expense.length
                var totaltext = requestHome.datareceived.household_home.household_now_expense
                var totalexpense = ""

                while (0 < textlength) {
                    var substring1 = ""
                    if (textlength == 3) {
                        if (totalexpense == "") {
                            totalexpense = totaltext.substring(0 until 3)
                        } else {
                            totalexpense = totaltext.substring(0 until 3) + "," + totalexpense
                        }
                    } else if (textlength > 3) {
                        substring1 = totaltext.substring(textlength - 3 until textlength)
                        if (totalexpense == "") {
                            totalexpense = substring1
                        } else {
                            totalexpense = substring1 + "," + totalexpense
                        }
                    } else if (textlength < 3) {
                        substring1 = totaltext.substring(0 until textlength)
                        totalexpense = substring1 + "," + totalexpense
                    }

                    textlength = textlength - 3
                }
                binding.homemiddletext.text = totalexpense
                binding.billtext.text =
                    requestHome.datareceived.household_home.household_now_budget_ratio + "%"
                binding.settlementtext.text =
                    "D-" + requestHome.datareceived.household_home.household_settlement_date

                if (requestHome.datareceived.household_home.household_budget_over_warn == "false") {
                    binding.warningtext.text = "이대로라면 예산을 초과하지 않아요"
                } else if (requestHome.datareceived.household_home.household_budget_over_warn == "true") {
                    binding.warningtext.text = "이대로라면 예산을 초과할 것 같아요"
                }

                totaltext = abs(requestHome.datareceived.household_home.household_now_expense_diff.toInt()).toString()
                textlength = totaltext.length
                totalexpense = ""

                while (0 < textlength) {
                    var substring1 = ""
                    if (textlength == 3) {
                        if (totalexpense == "") {
                            totalexpense = totaltext.substring(0 until 3)
                        } else {
                            totalexpense = totaltext.substring(0 until 3) + "," + totalexpense
                        }
                    } else if (textlength > 3) {
                        substring1 = totaltext.substring(textlength - 3 until textlength)
                        if (totalexpense == "") {
                            totalexpense = substring1
                        } else {
                            totalexpense = substring1 + "," + totalexpense
                        }
                    } else if (textlength < 3) {
                        substring1 = totaltext.substring(0 until textlength)
                        totalexpense = substring1 + "," + totalexpense
                    }

                    textlength = textlength - 3
                }

                binding.statusbilltext.text = totalexpense
            }

            override fun onFailure(call: Call<request_home>, t: Throwable) {
                Toast.makeText(mainActivity, "연결 실패입니다.", Toast.LENGTH_SHORT).show()
            }
        })
    }
}