package com.example.prototype

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.prototype.databinding.AlarmSpendingBinding
import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

data class expense_alarm (
    @SerializedName("message") var message: String = "",
    @SerializedName("status") var status: Int = 0,
    @SerializedName("data") var activity_received: exalarm = exalarm()
)

data class exalarm (
    @SerializedName("expense_noti") var expense_noti: ArrayList<exnoti> = ArrayList()
)

data class exnoti (
    @SerializedName("user_image_url") var user_image_url: String = "",
    @SerializedName("expense_category") var expense_category: String = "",
    @SerializedName("noti_created_at") var created_at: String = "",
    @SerializedName("noti_is_read") var noti_is_read: Boolean = false,
    @SerializedName("expense_amount") var expense_amount: String = "",
    @SerializedName("sender_name") var sender_name: String = ""
)

class alarm_spending : Fragment() {
    lateinit var alarmActivity : AlarmActivity
    private var fbinding : AlarmSpendingBinding? = null
    private val binding get() = fbinding!!
    private var alarmdata: expense_alarm = expense_alarm()
    lateinit var adapter: AlarmSpendingAdapter

    override fun onAttach(context: Context) {
        super.onAttach(context)

        alarmActivity = context as AlarmActivity
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fbinding = AlarmSpendingBinding.inflate(inflater, container, false)
        adapter = AlarmSpendingAdapter(alarmActivity, alarmdata.activity_received.expense_noti)
        binding.alarmspendinglist.adapter = adapter
        binding.alarmspendinglist.layoutManager = LinearLayoutManager(alarmActivity)
        var retrofit = RetrofitClientInstance.client
        val endpoint = retrofit?.create(getalarmexpense::class.java)
        endpoint!!.getalarmexpense("1").enqueue(object: Callback<expense_alarm>{
            override fun onResponse(call: Call<expense_alarm>, response: Response<expense_alarm>) {
                alarmdata = response.body()!!
                adapter = AlarmSpendingAdapter(alarmActivity, alarmdata.activity_received.expense_noti)
                binding.alarmspendinglist.adapter = adapter
                binding.alarmspendinglist.layoutManager = LinearLayoutManager(alarmActivity)
            }

            override fun onFailure(call: Call<expense_alarm>, t: Throwable) {
                Toast.makeText(alarmActivity, "연결 실패입니다.", Toast.LENGTH_SHORT).show()
            }

        })
        return binding.root
    }

}