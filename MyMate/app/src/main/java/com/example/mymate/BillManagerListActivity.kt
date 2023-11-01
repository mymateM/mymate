package com.example.mymate

import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.provider.ContactsContract.Data
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material3.contentColorFor
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mymate.databinding.ActivityBillmanagelistBinding
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.create
import java.time.LocalDate

data class billListItem(
    var date: String = "",
    var name: String = "",
    var amount: String = "",
    var drawable: Drawable
)

class BillManagerListActivity: AppCompatActivity() {
    lateinit var binding: ActivityBillmanagelistBinding
    lateinit var iteminfo: bills
    lateinit var context: Context
    lateinit var userRepo: DataStoreRepoUser
    lateinit var responselist: billListResponse
    var retrofit = RetrofitClientInstance.client
    var gasendpoint = retrofit?.create(getGasBillList::class.java)
    var electronicendpoint = retrofit?.create(getElectricityBillList::class.java)
    var waterendpoint = retrofit?.create(getWaterBillList::class.java)
    var etcendpoint = retrofit?.create(getEtcBillList::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityBillmanagelistBinding.inflate(layoutInflater)
        setContentView(binding.root)
        context = this
        userRepo = DataStoreRepoUser(dataStore)

        val category = intent.getStringExtra("category")
        binding.title.text = category
        iteminfo = bills()

        val today = LocalDate.now()
        val year = today.year

        if (category == "도시가스") {
            getGasList(category)
        } else if (category == "전기") {
            getElectronicityList(category)
        } else if (category == "수도") {
            getWaterList(category)
        } else if (category == "기타") {
            getEtcList(category)
        } else {
            Toast.makeText(context, "올바르지 않은 접근입니다.", Toast.LENGTH_SHORT)
        }


        binding.plusbtn.setOnClickListener {
            var ocrIntent = Intent(context, BillCameraActivity::class.java)
            ocrIntent.putExtra("category", category)
            startActivity(ocrIntent)
        }

        binding.backbtn.setOnClickListener {
            finish()
        }
    }

    private fun getGasList(category: String) {
        var accessToken = ""
        runBlocking {
            accessToken = userRepo.userAccessReadFlow.first().toString()
        }
        gasendpoint!!.getGasBillList("Bearer $accessToken").enqueue(object : Callback<billListResponse> {
            override fun onResponse(
                call: Call<billListResponse>,
                response: Response<billListResponse>
            ) {
                responselist = response.body()!!
                iteminfo = responselist.data
                var itemlist: ArrayList<ArrayList<bill>> = ArrayList()
                var temp = 0

                itemlist.add(arrayListOf())

                if (iteminfo.bills.size > 1) {
                    for (i in 0 until iteminfo.bills.size) {
                        if (i != 0) {
                            var thisone = iteminfo.bills[i].bill_payment_date.split("-")
                            var lastone = iteminfo.bills[i - 1].bill_payment_date.split("-")
                            if (thisone[0] == lastone[0]) {
                                itemlist[temp].add(iteminfo.bills[i])
                            } else {
                                temp++
                                itemlist.add(arrayListOf())
                                itemlist[temp].add(iteminfo.bills[i])
                            }
                        } else {
                            itemlist[0].add(iteminfo.bills[i])
                        }
                    }
                }

                val adapter = BillListContainerAdapter(itemlist, category!!)
                binding.billListcontainer.layoutManager = LinearLayoutManager(context)
                binding.billListcontainer.adapter = adapter
            }

            override fun onFailure(call: Call<billListResponse>, t: Throwable) {
                Toast.makeText(context, "연결 실패", Toast.LENGTH_SHORT)
            }
        })
    }

    private fun getElectronicityList(category: String) {
        var accessToken = ""
        runBlocking {
            accessToken = userRepo.userAccessReadFlow.first().toString()
        }
        electronicendpoint!!.getElectricityBillList("Bearer $accessToken").enqueue(object : Callback<billListResponse> {
            override fun onResponse(
                call: Call<billListResponse>,
                response: Response<billListResponse>
            ) {
                responselist = response.body()!!
                iteminfo = responselist.data
                var itemlist: ArrayList<ArrayList<bill>> = ArrayList()
                var temp = 0

                itemlist.add(arrayListOf())

                if (iteminfo.bills.size > 1) {
                    for (i in 0 until iteminfo.bills.size) {
                        if (i != 0) {
                            var thisone = iteminfo.bills[i].bill_payment_date.split("-")
                            var lastone = iteminfo.bills[i - 1].bill_payment_date.split("-")
                            if (thisone[0] == lastone[0]) {
                                itemlist[temp].add(iteminfo.bills[i])
                            } else {
                                temp++
                                itemlist.add(arrayListOf())
                                itemlist[temp].add(iteminfo.bills[i])
                            }
                        } else {
                            itemlist[0].add(iteminfo.bills[i])
                        }
                    }
                }

                val adapter = BillListContainerAdapter(itemlist, category!!)
                binding.billListcontainer.layoutManager = LinearLayoutManager(context)
                binding.billListcontainer.adapter = adapter
            }

            override fun onFailure(call: Call<billListResponse>, t: Throwable) {
                Toast.makeText(context, "연결 실패", Toast.LENGTH_SHORT)
            }
        })
    }

    private fun getWaterList(category: String) {
        var accessToken = ""
        runBlocking {
            accessToken = userRepo.userAccessReadFlow.first().toString()
        }
        waterendpoint!!.getWaterBillList("Bearer $accessToken").enqueue(object : Callback<billListResponse> {
            override fun onResponse(
                call: Call<billListResponse>,
                response: Response<billListResponse>
            ) {
                responselist = response.body()!!
                iteminfo = responselist.data
                var itemlist: ArrayList<ArrayList<bill>> = ArrayList()
                var temp = 0

                itemlist.add(arrayListOf())

                if (iteminfo.bills.size > 1) {
                    for (i in 0 until iteminfo.bills.size) {
                        if (i != 0) {
                            var thisone = iteminfo.bills[i].bill_payment_date.split("-")
                            var lastone = iteminfo.bills[i - 1].bill_payment_date.split("-")
                            if (thisone[0] == lastone[0]) {
                                itemlist[temp].add(iteminfo.bills[i])
                            } else {
                                temp++
                                itemlist.add(arrayListOf())
                                itemlist[temp].add(iteminfo.bills[i])
                            }
                        } else {
                            itemlist[0].add(iteminfo.bills[i])
                        }
                    }
                }

                val adapter = BillListContainerAdapter(itemlist, category!!)
                binding.billListcontainer.layoutManager = LinearLayoutManager(context)
                binding.billListcontainer.adapter = adapter
            }

            override fun onFailure(call: Call<billListResponse>, t: Throwable) {
                Toast.makeText(context, "연결 실패", Toast.LENGTH_SHORT)
            }
        })
    }

    private fun getEtcList(category: String) {
        var accessToken = ""
        runBlocking {
            accessToken = userRepo.userAccessReadFlow.first().toString()
        }
        etcendpoint!!.getEtcBillList("Bearer $accessToken").enqueue(object : Callback<billListResponse> {
            override fun onResponse(
                call: Call<billListResponse>,
                response: Response<billListResponse>
            ) {
                responselist = response.body()!!
                iteminfo = responselist.data
                var itemlist: ArrayList<ArrayList<bill>> = ArrayList()
                var temp = 0

                itemlist.add(arrayListOf())

                if (iteminfo.bills.size > 1) {
                    for (i in 0 until iteminfo.bills.size) {
                        if (i != 0) {
                            var thisone = iteminfo.bills[i].bill_payment_date.split("-")
                            var lastone = iteminfo.bills[i - 1].bill_payment_date.split("-")
                            if (thisone[0] == lastone[0]) {
                                itemlist[temp].add(iteminfo.bills[i])
                            } else {
                                temp++
                                itemlist.add(arrayListOf())
                                itemlist[temp].add(iteminfo.bills[i])
                            }
                        } else {
                            itemlist[0].add(iteminfo.bills[i])
                        }
                    }
                }

                val adapter = BillListContainerAdapter(itemlist, category!!)
                binding.billListcontainer.layoutManager = LinearLayoutManager(context)
                binding.billListcontainer.adapter = adapter
            }

            override fun onFailure(call: Call<billListResponse>, t: Throwable) {
                Toast.makeText(context, "연결 실패", Toast.LENGTH_SHORT)
            }

        })
    }
}