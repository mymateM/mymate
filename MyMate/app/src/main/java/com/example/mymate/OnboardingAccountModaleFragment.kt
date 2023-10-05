package com.example.mymate

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mymate.databinding.ModaleOnboardingAccountBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class OnboardingAccountModaleFragment  : BottomSheetDialogFragment() {
    lateinit var binding: ModaleOnboardingAccountBinding
    lateinit var accountActivity: OnboardingAccountActivity

    override fun onAttach(context: Context) {
        super.onAttach(context)
        accountActivity = context as OnboardingAccountActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ModaleOnboardingAccountBinding.inflate(inflater, container, false)
        binding.modaledismiss.setOnClickListener {
            dismiss()
        }
        //var manager: RecyclerView.LayoutManager = GridLayoutManager(accountActivity, 3)
        //binding.bankTable.layoutManager = manager

        return binding.root
    }
}