package com.example.mymate

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isGone
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mymate.data.dto.bill.BillSummary
import com.example.mymate.data.dto.bill.Bills
import com.example.mymate.data.dto.bill.response.BillsResponse
import com.example.mymate.data.dto.common.DefaultResponse
import com.example.mymate.databinding.ActivityBilldeleteBinding
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BillDeleteActivity: AppCompatActivity() {
    lateinit var binding: ActivityBilldeleteBinding
    lateinit var context: Context
    lateinit var userRepo: DataStoreRepoUser
    lateinit var responselist: BillsResponse
    lateinit var iteminfo: Bills
    var retrofit = RetrofitClientInstance.client
    var gasendpoint = retrofit?.create(getGasBillList::class.java)
    var electronicendpoint = retrofit?.create(getElectricityBillList::class.java)
    var waterendpoint = retrofit?.create(getWaterBillList::class.java)
    var etcendpoint = retrofit?.create(getEtcBillList::class.java)
    var deleteendpoint = retrofit?.create(deleteBill::class.java)
    var values = BillListValues()
    var checkAll = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityBilldeleteBinding.inflate(layoutInflater)
        setContentView(binding.root)
        userRepo = DataStoreRepoUser(dataStore)
        context = this

        val category = intent.getStringExtra("category")
        binding.title.text = category

        if (category == "도시가스") {
            getGasList(category)
        } else if (category == "전기") {
            getElectronicityList(category)
        } else if (category == "수도") {
            getWaterList(category)
        } else if (category == "기타") {
            getEtcList(category)
        } else {
            Toast.makeText(context, "올바르지 않은 접근입니다.", Toast.LENGTH_SHORT).show()
        }

        binding.back.setOnClickListener {
            finish()
            overridePendingTransition(R.anim.none, R.anim.none)
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
        overridePendingTransition(R.anim.none, R.anim.none)
    }

    private fun getGasList(category: String) {
        var accessToken = ""
        runBlocking {
            accessToken = userRepo.userAccessReadFlow.first().toString()
        }
        gasendpoint!!.getGasBillList("Bearer $accessToken").enqueue(object :
            Callback<BillsResponse> {
            override fun onResponse(
                call: Call<BillsResponse>,
                response: Response<BillsResponse>
            ) {
                responselist = response.body()!!
                iteminfo = responselist.data
                var itemlist: ArrayList<ArrayList<BillSummary>> = ArrayList()
                var temp = 0

                itemlist.add(arrayListOf())

                if (iteminfo.bills.size > 1) {
                    for (i in 0 until iteminfo.bills.size) {
                        if (i != 0) {
                            if (!iteminfo.bills[i].bill_payment_date.isNullOrEmpty()) {
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
                                iteminfo.bills[i].bill_payment_date = "2023-01-05"
                                itemlist[temp].add(iteminfo.bills[i])
                            }
                        } else {
                            itemlist[0].add(iteminfo.bills[i])
                            if (iteminfo.bills[i].bill_payment_date.isNullOrEmpty()) {
                                iteminfo.bills[i].bill_payment_date = "2023-01-05"
                                itemlist[temp].add(iteminfo.bills[i])
                            }
                        }
                    }
                }


                var adapter = BillDeleteListContainerAdapter(itemlist, category, values, checkAll)
                binding.billListcontainer.layoutManager = LinearLayoutManager(context)
                binding.billListcontainer.adapter = adapter

                binding.deleteall.setOnClickListener {
                    if (checkAll) {
                        values.deleteList.clear()
                        checkAll = false
                        adapter = BillDeleteListContainerAdapter(itemlist, category, values, checkAll)
                        binding.billListcontainer.adapter = adapter
                    } else {
                        checkAll = true
                        for (i in 0 until iteminfo.bills.size) {
                            if (values.deleteList.isNotEmpty()) {
                                values.deleteList.clear()
                            }
                            values.putid(iteminfo.bills[i].bill_id)
                        }
                        adapter = BillDeleteListContainerAdapter(itemlist, category, values, true)
                        binding.billListcontainer.adapter = adapter
                    }
                }

                binding.deletebtn.setOnClickListener {
                    binding.deletepopup.isGone = false
                }

                binding.cover.setOnClickListener {
                    binding.deletepopup.isGone = true
                }

                binding.dismiss.setOnClickListener {
                    binding.deletepopup.isGone = true
                }

                binding.deleteconfirm.setOnClickListener {
                    deleteBills()
                }
            }

            override fun onFailure(call: Call<BillsResponse>, t: Throwable) {
                Toast.makeText(context, "연결 실패(고지서 리스트)", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun getElectronicityList(category: String) {
        var accessToken = ""
        runBlocking {
            accessToken = userRepo.userAccessReadFlow.first().toString()
        }
        electronicendpoint!!.getElectricityBillList("Bearer $accessToken").enqueue(object :
            Callback<BillsResponse> {
            override fun onResponse(
                call: Call<BillsResponse>,
                response: Response<BillsResponse>
            ) {
                responselist = response.body()!!
                iteminfo = responselist.data
                var itemlist: ArrayList<ArrayList<BillSummary>> = ArrayList()
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

                var adapter = BillDeleteListContainerAdapter(itemlist, category, values, checkAll)
                binding.billListcontainer.layoutManager = LinearLayoutManager(context)
                binding.billListcontainer.adapter = adapter

                binding.deleteall.setOnClickListener {
                    if (checkAll) {
                        values.deleteList.clear()
                        checkAll = false
                        adapter = BillDeleteListContainerAdapter(itemlist, category, values, checkAll)
                        binding.billListcontainer.adapter = adapter
                    } else {
                        checkAll = true
                        for (i in 0 until iteminfo.bills.size) {
                            if (values.deleteList.isNotEmpty()) {
                                values.deleteList.clear()
                            }
                            values.putid(iteminfo.bills[i].bill_id)
                        }
                        adapter = BillDeleteListContainerAdapter(itemlist, category, values, true)
                        binding.billListcontainer.adapter = adapter
                    }
                }

                binding.deletebtn.setOnClickListener {
                    binding.deletepopup.isGone = false
                }

                binding.cover.setOnClickListener {
                    binding.deletepopup.isGone = true
                }

                binding.dismiss.setOnClickListener {
                    binding.deletepopup.isGone = true
                }

                binding.deleteconfirm.setOnClickListener {
                    deleteBills()
                }
            }

            override fun onFailure(call: Call<BillsResponse>, t: Throwable) {
                Toast.makeText(context, "연결 실패(고지서 리스트)", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun getWaterList(category: String) {
        var accessToken = ""
        runBlocking {
            accessToken = userRepo.userAccessReadFlow.first().toString()
        }
        waterendpoint!!.getWaterBillList("Bearer $accessToken").enqueue(object :
            Callback<BillsResponse> {
            override fun onResponse(
                call: Call<BillsResponse>,
                response: Response<BillsResponse>
            ) {
                responselist = response.body()!!
                iteminfo = responselist.data
                var itemlist: ArrayList<ArrayList<BillSummary>> = ArrayList()
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

                var adapter = BillDeleteListContainerAdapter(itemlist, category!!, values, checkAll)
                binding.billListcontainer.layoutManager = LinearLayoutManager(context)
                binding.billListcontainer.adapter = adapter

                binding.deleteall.setOnClickListener {
                    if (checkAll) {
                        values.deleteList.clear()
                        checkAll = false
                        adapter = BillDeleteListContainerAdapter(itemlist, category, values, checkAll)
                        binding.billListcontainer.adapter = adapter
                    } else {
                        checkAll = true
                        for (i in 0 until iteminfo.bills.size) {
                            if (values.deleteList.isNotEmpty()) {
                                values.deleteList.clear()
                            }
                            values.putid(iteminfo.bills[i].bill_id)
                        }
                        adapter = BillDeleteListContainerAdapter(itemlist, category, values, true)
                        binding.billListcontainer.adapter = adapter
                    }
                }

                binding.deletebtn.setOnClickListener {
                    binding.deletepopup.isGone = false
                }

                binding.cover.setOnClickListener {
                    binding.deletepopup.isGone = true
                }

                binding.dismiss.setOnClickListener {
                    binding.deletepopup.isGone = true
                }

                binding.deleteconfirm.setOnClickListener {
                    deleteBills()
                }
            }

            override fun onFailure(call: Call<BillsResponse>, t: Throwable) {
                Toast.makeText(context, "연결 실패(고지서 리스트)", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun getEtcList(category: String) {
        var accessToken = ""
        runBlocking {
            accessToken = userRepo.userAccessReadFlow.first().toString()
        }
        etcendpoint!!.getEtcBillList("Bearer $accessToken").enqueue(object :
            Callback<BillsResponse> {
            override fun onResponse(
                call: Call<BillsResponse>,
                response: Response<BillsResponse>
            ) {
                responselist = response.body()!!
                iteminfo = responselist.data
                var itemlist: ArrayList<ArrayList<BillSummary>> = ArrayList()
                var temp = 0

                itemlist.add(arrayListOf())

                if (iteminfo.bills.size > 1) {
                    for (i in 0 until iteminfo.bills.size) {
                        if (i != 0) {
                            if (!iteminfo.bills[i].bill_payment_date.isNullOrEmpty()) {
                                var thisone = iteminfo.bills[i].bill_payment_date.split("-")
                                var lastone = iteminfo.bills[i - 1].bill_payment_date.split("-")
                                if (thisone[0] == lastone[0]) {
                                    itemlist[temp].add(iteminfo.bills[i])
                                } else {
                                    temp++
                                    itemlist.add(arrayListOf())
                                    itemlist[temp].add(iteminfo.bills[i])
                                }
                            }
                        } else {
                            itemlist[0].add(iteminfo.bills[i])
                        }
                    }
                }

                var adapter = BillDeleteListContainerAdapter(itemlist, category, values, checkAll)
                binding.billListcontainer.layoutManager = LinearLayoutManager(context)
                binding.billListcontainer.adapter = adapter

                binding.deleteall.setOnClickListener {
                    if (checkAll) {
                        values.deleteList.clear()
                        checkAll = false
                        adapter = BillDeleteListContainerAdapter(itemlist, category, values, checkAll)
                        binding.billListcontainer.adapter = adapter
                    } else {
                        checkAll = true
                        for (i in 0 until iteminfo.bills.size) {
                            if (values.deleteList.isNotEmpty()) {
                                values.deleteList.clear()
                            }
                            values.putid(iteminfo.bills[i].bill_id)
                        }
                        adapter = BillDeleteListContainerAdapter(itemlist, category, values, true)
                        binding.billListcontainer.adapter = adapter
                    }
                }

                binding.deletebtn.setOnClickListener {
                    binding.deletepopup.isGone = false
                }

                binding.cover.setOnClickListener {
                    binding.deletepopup.isGone = true
                }

                binding.dismiss.setOnClickListener {
                    binding.deletepopup.isGone = true
                }

                binding.deleteconfirm.setOnClickListener {
                    deleteBills()
                }
            }

            override fun onFailure(call: Call<BillsResponse>, t: Throwable) {
                Toast.makeText(context, "연결 실패(고지서 리스트)", Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun deleteBills() {
        var accessToken = ""
        runBlocking {
            accessToken = userRepo.userAccessReadFlow.first().toString()
        }
        var deleteid = ""
        for (i in 0 until values.deleteList.size) {
            if (i == 0) {
                deleteid = values.deleteList[0]
            } else {
                deleteid += "," + values.deleteList[i]
            }
        }
        deleteendpoint!!.deleteBill("Bearer $accessToken", bill_id_list = deleteid).enqueue(object : Callback<DefaultResponse> {
            override fun onResponse(
                call: Call<DefaultResponse>,
                response: Response<DefaultResponse>
            ) {
                if (response.isSuccessful) {
                    finish()
                    overridePendingTransition(R.anim.none, R.anim.none)
                }
            }

            override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
            }
        })
    }
}