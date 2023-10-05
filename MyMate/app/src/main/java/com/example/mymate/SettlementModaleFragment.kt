package com.example.mymate

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.mymate.databinding.ModaleSettlementBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class SettlementModaleFragment : BottomSheetDialogFragment() {
    lateinit var settlementActivity: SettlementActivity
    lateinit var binding: ModaleSettlementBinding

    override fun onAttach(context: Context) {
        super.onAttach(context)
        settlementActivity = context as SettlementActivity
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ModaleSettlementBinding.inflate(inflater, container, false)
        binding.modaledismiss.setOnClickListener{
            dismiss()
        }
        return binding.root
    }
}