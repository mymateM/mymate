package com.example.mymate

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.example.mymate.databinding.ModaleBillcategoryBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class BillManagerModaleAddFragment : BottomSheetDialogFragment() {
    lateinit var binding: ModaleBillcategoryBinding
    lateinit var billManagerActivity: BillManagerActivity

    override fun onAttach(context: Context) {
        super.onAttach(context)
        billManagerActivity = context as BillManagerActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ModaleBillcategoryBinding.inflate(inflater, container, false)
        var clicked = ""
        var toAdd = Intent(billManagerActivity, BillAddActivity::class.java)
        binding.confirmbtn.text = "직접 입력"

        binding.gasiconcontainer.setOnClickListener {
            if (clicked == "도시가스") {
                binding.confirmbtn.setBackgroundResource(R.drawable.box_modalebutton)
                binding.confirmbtn.isEnabled = false
                clicked = ""
                binding.gasicon.setImageDrawable(ContextCompat.getDrawable(billManagerActivity, R.drawable.billicon_gas_default))
                binding.gasiconcontainer.setImageDrawable(ContextCompat.getDrawable(billManagerActivity, R.drawable.icon_circlebtndefault))
            } else {
                clicked = "도시가스"
                binding.confirmbtn.setBackgroundResource(R.drawable.box_modalebuttonselected)
                binding.gasicon.setImageDrawable(ContextCompat.getDrawable(billManagerActivity, R.drawable.billicon_gas_active))
                binding.gasiconcontainer.setImageDrawable(ContextCompat.getDrawable(billManagerActivity, R.drawable.icon_circlebtnselected))
                binding.electronicicon.setImageDrawable(ContextCompat.getDrawable(billManagerActivity, R.drawable.billicon_electronic_default))
                binding.electroniciconcontainer.setImageDrawable(ContextCompat.getDrawable(billManagerActivity, R.drawable.icon_circlebtndefault))
                binding.watericon.setImageDrawable(ContextCompat.getDrawable(billManagerActivity, R.drawable.billicon_water_default))
                binding.watericoncontainer.setImageDrawable(ContextCompat.getDrawable(billManagerActivity, R.drawable.icon_circlebtndefault))
                binding.etcicon.setImageDrawable(ContextCompat.getDrawable(billManagerActivity, R.drawable.billicon_etc_default))
                binding.etciconcontainer.setImageDrawable(ContextCompat.getDrawable(billManagerActivity, R.drawable.icon_circlebtndefault))
            }

        }

        binding.electroniciconcontainer.setOnClickListener {
            if (clicked == "전기") {
                binding.confirmbtn.setBackgroundResource(R.drawable.box_modalebutton)
                binding.confirmbtn.isEnabled = false
                clicked = ""
                binding.electronicicon.setImageDrawable(ContextCompat.getDrawable(billManagerActivity, R.drawable.billicon_electronic_default))
                binding.electroniciconcontainer.setImageDrawable(ContextCompat.getDrawable(billManagerActivity, R.drawable.icon_circlebtndefault))
            } else {
                clicked = "전기"
                binding.confirmbtn.setBackgroundResource(R.drawable.box_modalebuttonselected)
                binding.electronicicon.setImageDrawable(ContextCompat.getDrawable(billManagerActivity, R.drawable.billicon_electronic_active))
                binding.electroniciconcontainer.setImageDrawable(ContextCompat.getDrawable(billManagerActivity, R.drawable.icon_circlebtnselected))
                binding.gasicon.setImageDrawable(ContextCompat.getDrawable(billManagerActivity, R.drawable.billicon_gas_default))
                binding.gasiconcontainer.setImageDrawable(ContextCompat.getDrawable(billManagerActivity, R.drawable.icon_circlebtndefault))
                binding.watericon.setImageDrawable(ContextCompat.getDrawable(billManagerActivity, R.drawable.billicon_water_default))
                binding.watericoncontainer.setImageDrawable(ContextCompat.getDrawable(billManagerActivity, R.drawable.icon_circlebtndefault))
                binding.etcicon.setImageDrawable(ContextCompat.getDrawable(billManagerActivity, R.drawable.billicon_etc_default))
                binding.etciconcontainer.setImageDrawable(ContextCompat.getDrawable(billManagerActivity, R.drawable.icon_circlebtndefault))
            }
        }

        binding.watericoncontainer.setOnClickListener {
            if (clicked == "수도") {
                binding.confirmbtn.setBackgroundResource(R.drawable.box_modalebutton)
                binding.confirmbtn.isEnabled = false
                clicked = ""
                binding.watericon.setImageDrawable(ContextCompat.getDrawable(billManagerActivity, R.drawable.billicon_water_default))
                binding.watericoncontainer.setImageDrawable(ContextCompat.getDrawable(billManagerActivity, R.drawable.icon_circlebtndefault))
            } else {
                clicked = "수도"
                binding.confirmbtn.setBackgroundResource(R.drawable.box_modalebuttonselected)
                binding.watericon.setImageDrawable(ContextCompat.getDrawable(billManagerActivity, R.drawable.billicon_water_active))
                binding.watericoncontainer.setImageDrawable(ContextCompat.getDrawable(billManagerActivity, R.drawable.icon_circlebtnselected))
                binding.gasicon.setImageDrawable(ContextCompat.getDrawable(billManagerActivity, R.drawable.billicon_gas_default))
                binding.gasiconcontainer.setImageDrawable(ContextCompat.getDrawable(billManagerActivity, R.drawable.icon_circlebtndefault))
                binding.electronicicon.setImageDrawable(ContextCompat.getDrawable(billManagerActivity, R.drawable.billicon_electronic_default))
                binding.electroniciconcontainer.setImageDrawable(ContextCompat.getDrawable(billManagerActivity, R.drawable.icon_circlebtndefault))
                binding.etcicon.setImageDrawable(ContextCompat.getDrawable(billManagerActivity, R.drawable.billicon_etc_default))
                binding.etciconcontainer.setImageDrawable(ContextCompat.getDrawable(billManagerActivity, R.drawable.icon_circlebtndefault))
            }
        }

        binding.etciconcontainer.setOnClickListener {
            if (clicked == "기타") {
                binding.confirmbtn.setBackgroundResource(R.drawable.box_modalebutton)
                binding.confirmbtn.isEnabled = false
                clicked = ""
                binding.etcicon.setImageDrawable(ContextCompat.getDrawable(billManagerActivity, R.drawable.billicon_etc_default))
                binding.etciconcontainer.setImageDrawable(ContextCompat.getDrawable(billManagerActivity, R.drawable.icon_circlebtndefault))
            } else {
                clicked = "기타"
                binding.confirmbtn.setBackgroundResource(R.drawable.box_modalebuttonselected)
                binding.etcicon.setImageDrawable(ContextCompat.getDrawable(billManagerActivity, R.drawable.billicon_etc_active))
                binding.etciconcontainer.setImageDrawable(ContextCompat.getDrawable(billManagerActivity, R.drawable.icon_circlebtnselected))
                binding.gasicon.setImageDrawable(ContextCompat.getDrawable(billManagerActivity, R.drawable.billicon_gas_default))
                binding.gasiconcontainer.setImageDrawable(ContextCompat.getDrawable(billManagerActivity, R.drawable.icon_circlebtndefault))
                binding.electronicicon.setImageDrawable(ContextCompat.getDrawable(billManagerActivity, R.drawable.billicon_electronic_default))
                binding.electroniciconcontainer.setImageDrawable(ContextCompat.getDrawable(billManagerActivity, R.drawable.icon_circlebtndefault))
                binding.watericon.setImageDrawable(ContextCompat.getDrawable(billManagerActivity, R.drawable.billicon_water_default))
                binding.watericoncontainer.setImageDrawable(ContextCompat.getDrawable(billManagerActivity, R.drawable.icon_circlebtndefault))
            }
        }

        binding.confirmbtn.setOnClickListener {
            toAdd.putExtra("category", clicked)
            toAdd.putExtra("memo", "없음")
            startActivity(toAdd)
            dismiss()
        }

        return binding.root
    }
}