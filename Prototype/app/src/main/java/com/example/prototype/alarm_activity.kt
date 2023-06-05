package com.example.prototype

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.prototype.databinding.AlarmActivityBinding
import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

data class activity_alarm (
    @SerializedName("message") var message: String = "",
    @SerializedName("status") var status: Int = 0,
    @SerializedName("data") var activity_received: acalarm = acalarm()
        )

data class acalarm (
    @SerializedName("noti_activites") var activity_noti: ArrayList<acnoti> = ArrayList()
        )

data class acnoti (
    @SerializedName("noti_category") var noti_category: String = "",
    @SerializedName("noti_img_url") var noti_img_url: String = "",
    @SerializedName("noti_created_at") var noti_created_at: String = "",
    @SerializedName("noti_content") var noti_content: String = "",
    @SerializedName("noti_is_read") var noti_is_read: Boolean = false,
    @SerializedName("noti_sender") var user_name: String = ""
        )

class alarm_activity : Fragment() {
    lateinit var alarmActivity : AlarmActivity
    private var fbinding: AlarmActivityBinding? = null
    private val binding get() = fbinding!!
    private var alarmdata: activity_alarm = activity_alarm()
    lateinit var adapter: AlarmActivityAdapter

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
        fbinding = AlarmActivityBinding.inflate(inflater, container, false)
        adapter = AlarmActivityAdapter(alarmActivity, alarmdata.activity_received.activity_noti)
        binding.alarmactivitylist.adapter = adapter
        binding.alarmactivitylist.layoutManager = LinearLayoutManager(alarmActivity)
        var retrofit = RetrofitClientInstance.client
        val endpoint = retrofit?.create(getalarmactivity::class.java)
        endpoint!!.getalarmactivity("1").enqueue(object: Callback<activity_alarm> {
            override fun onResponse(
                call: Call<activity_alarm>,
                response: Response<activity_alarm>
            ) {
                alarmdata = response.body()!!
                adapter = AlarmActivityAdapter(alarmActivity, alarmdata.activity_received.activity_noti)
                binding.alarmactivitylist.adapter = adapter
                binding.alarmactivitylist.layoutManager = LinearLayoutManager(alarmActivity)
            }

            override fun onFailure(call: Call<activity_alarm>, t: Throwable) {
                Toast.makeText(alarmActivity, "연결 실패입니다.", Toast.LENGTH_SHORT).show()
            }

        })
        return binding.root
    }
}