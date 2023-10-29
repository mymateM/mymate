package com.example.mymate

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isGone
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mymate.databinding.AlarmSpdFragmentBinding
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AlarmSpdFragment : Fragment() {
    lateinit var alarmActivity: AlarmActivity
    lateinit var binding: AlarmSpdFragmentBinding
    lateinit var userRepo: DataStoreRepoUser
    private val retrofit = RetrofitClientInstance.client
    private var notiList = ArrayList<expenseNoti>()

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
        binding = AlarmSpdFragmentBinding.inflate(inflater, container, false)
        userRepo = DataStoreRepoUser(alarmActivity.dataStore)
        getNoti()
        return binding.root
    }

    fun getNoti() {
        val endpoint = retrofit?.create(getExpenseNoti::class.java)
        var accesstoken = ""
        var notiResponse: expenseNotiResponse
        var alarmList = ArrayList<ArrayList<expenseNoti>>()
        runBlocking {
            accesstoken = userRepo.userAccessReadFlow.first().toString()
        }
        endpoint!!.expenseNoti("Bearer " + accesstoken).enqueue(object: Callback<expenseNotiResponse> {
            override fun onResponse(
                call: Call<expenseNotiResponse>,
                response: Response<expenseNotiResponse>
            ) {
                notiResponse = response.body()!!
                notiList = notiResponse.data.notification_expenses
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
                        alarmlistposition ++
                        alarmList.add(ArrayList())
                        alarmList[alarmlistposition].add(notiList[temp])
                        temp++
                    }
                }

                val adapter = AlarmSpdContainerAdapter(alarmList)
                val manager: RecyclerView.LayoutManager = LinearLayoutManager(context)
                binding.alarmspdlist.layoutManager = manager
                binding.alarmspdlist.adapter = adapter
            }

            override fun onFailure(call: Call<expenseNotiResponse>, t: Throwable) {
                Toast.makeText(context, "연결 실패", Toast.LENGTH_SHORT)
            }

        })
    }

    override fun onResume() {
        //TODO: refresh data
        super.onResume()
    }
}