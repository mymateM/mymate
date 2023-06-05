package com.example.prototype

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.example.prototype.databinding.ModalelistBinding

class listsettlementAdapter(val context: Context, val room_mates: ArrayList<room_mate>): BaseAdapter() {

    private var mBinding : ModalelistBinding? = null
    private val binding get() = mBinding!!

    override fun getView(position: Int, p1: View?, p2: ViewGroup?): View {
        mBinding = ModalelistBinding.inflate(LayoutInflater.from(context))

        val username = binding.username
        val description = binding.settlementdescription
        val totalbill = binding.settlementbill

        val room_mate = room_mates[position]

        username.text = room_mate.user_name
        description.text = room_mate.user_account
        totalbill.text = room_mate.user_settlement_ratio

        return mBinding!!.root
    }

    override fun getItemId(p0: Int): Long {
        return 0
    }

    override fun getItem(position: Int): Any {
        return room_mates[position]
    }

    override fun getCount(): Int {
        return room_mates.size
    }

}