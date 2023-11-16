package com.example.mymate

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isGone
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mymate.databinding.AlarmActFragmentBinding
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.create
import java.text.SimpleDateFormat

class AlarmActFragment : Fragment() {
    lateinit var alarmActivity: AlarmActivity
    lateinit var binding: AlarmActFragmentBinding
    lateinit var userRepo: DataStoreRepoUser
    private val retrofit = RetrofitClientInstance.client
    private var notiList = ArrayList<activityNoti>()

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
        binding = AlarmActFragmentBinding.inflate(inflater, container, false)
        userRepo = DataStoreRepoUser(alarmActivity.dataStore)
        getNoti()
        return binding.root
    }

    fun getNoti() {
        val endpoint = retrofit?.create(getActivityNoti::class.java)
        var accesstoken = ""
        var notiresponse: activityNotiResponse
        var alarmList = ArrayList<ArrayList<activityNoti>>()
        runBlocking {
            accesstoken = userRepo.userAccessReadFlow.first().toString()
        }
        endpoint!!.activityNoti("Bearer $accesstoken").enqueue(object : Callback<activityNotiResponse> {
            override fun onResponse(
                call: Call<activityNotiResponse>,
                response: Response<activityNotiResponse>
            ) {
                notiresponse = response.body()!!
                notiList = notiresponse.data.activityNotificationResponses
                var notilength = notiList.size
                var datebefore = notiList[0].created_at.substring(0 until 10)
                var temp = 0
                var alarmlistposition = 0
                alarmList.add(ArrayList())
                while (temp < notilength) {
                    if (datebefore == notiList[temp].created_at.substring(0 until 10)) {
                        alarmList[alarmlistposition].add(notiList[temp])
                        temp++
                    } else {
                        datebefore = notiList[temp].created_at.substring(0 until 10)
                        alarmlistposition++
                        alarmList.add(ArrayList())
                        alarmList[alarmlistposition].add(notiList[temp])
                        temp++
                    }
                }

                val adapter = AlarmActContainerAdapter(alarmList)
                val manager: RecyclerView.LayoutManager = LinearLayoutManager(context)
                binding.alarmactlist.layoutManager = manager
                binding.alarmactlist.adapter = adapter

                val readEndpoint = retrofit?.create(readActivityNoti::class.java)
                readEndpoint!!.readActivityNoti("Bearer $accesstoken", notiList[0].activity_notification_id).enqueue(object: Callback<defaultResponse> {
                    override fun onResponse(
                        call: Call<defaultResponse>,
                        response: Response<defaultResponse>
                    ) {

                    }

                    override fun onFailure(call: Call<defaultResponse>, t: Throwable) {
                        Toast.makeText(alarmActivity, "연결 실패(활동-읽음)", Toast.LENGTH_SHORT).show()
                    }

                })
            }

            override fun onFailure(call: Call<activityNotiResponse>, t: Throwable) {
                Toast.makeText(context, "연결 실패", Toast.LENGTH_SHORT).show()
            }

        })
    }

    override fun onResume() {
        //TODO: refresh data
        super.onResume()
    }
}