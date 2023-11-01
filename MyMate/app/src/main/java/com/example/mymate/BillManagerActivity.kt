package com.example.mymate

import android.content.Intent
import android.os.Bundle
import android.view.ContextMenu
import android.view.MenuItem
import android.view.View
import android.widget.PopupMenu
import androidx.appcompat.app.AppCompatActivity
import com.example.mymate.databinding.ActivityBillmanagerBinding

class BillManagerActivity: AppCompatActivity() {
    lateinit var binding: ActivityBillmanagerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityBillmanagerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var managerlist = Intent(this, BillManagerListActivity::class.java)

        binding.backbtn.setOnClickListener {
            finish()
        }
        binding.gasview.setOnClickListener {
            managerlist.putExtra("category", "도시가스")
            startActivity(managerlist)
        }
        binding.electronicview.setOnClickListener {
            managerlist.putExtra("category", "전기")
            startActivity(managerlist)
        }
        binding.waterview.setOnClickListener {
            managerlist.putExtra("category", "수도")
            startActivity(managerlist)
        }
        binding.etcview.setOnClickListener {
            managerlist.putExtra("category", "기타")
            startActivity(managerlist)
        }

        binding.addbtn.setOnClickListener {
            var popupMenu = PopupMenu(this, it)
            menuInflater?.inflate(R.menu.contextmenu_bill, popupMenu.menu)
            popupMenu.show()
            popupMenu.setOnMenuItemClickListener {
                if (it.title == "사진 스캔") {
                    startActivity(Intent(this, BillCameraActivity::class.java))
                    return@setOnMenuItemClickListener true
                }
                return@setOnMenuItemClickListener false
            }
        }
    }
}