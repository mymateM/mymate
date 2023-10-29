package com.example.mymate

import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material3.contentColorFor
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mymate.databinding.ActivityBillmanagelistBinding

data class billListItem(
    var date: String = "",
    var name: String = "",
    var amount: String = "",
    var drawable: Drawable
)

class BillManagerListActivity: AppCompatActivity() {
    lateinit var binding: ActivityBillmanagelistBinding
    lateinit var iteminfo: ArrayList<billListItem>
    lateinit var context: Context

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityBillmanagelistBinding.inflate(layoutInflater)
        setContentView(binding.root)
        context = this

        val category = intent.getStringExtra("category")
        binding.title.text = category

        iteminfo = arrayListOf<billListItem>()
        iteminfo.add(0, billListItem("2020년 02월 02일", "sample", "000,000원", ContextCompat.getDrawable(this, R.drawable.settlement_character)!!))
        Log.d("빌", iteminfo[0].name)

        val adapter = BillListAdapter(iteminfo)
        binding.billList.layoutManager = LinearLayoutManager(context)
        binding.billList.adapter = adapter.apply {
            setOnItemClickListener(object : BillListAdapter.OnItemClickListener {
                override fun onItemClick(item: billListItem, position: Int) {
                    var detailIntent = Intent(context, BillDetailActivity::class.java)
                    detailIntent.putExtra("itemname", item.name)
                    detailIntent.putExtra("itemday", item.date)
                    detailIntent.putExtra("itemamount", item.amount)
                    detailIntent.putExtra("category", category)
                    startActivity(detailIntent)
                }
            })
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
}