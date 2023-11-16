package com.example.mymate

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.mymate.databinding.MainReportMeFragmentBinding

class MainReportMeFragment: Fragment() {
    lateinit var binding: MainReportMeFragmentBinding
    lateinit var mainActivity: MainActivity
    lateinit var userRepo: DataStoreRepoUser

    private val retrofit = RetrofitClientInstance.client

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as MainActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = MainReportMeFragmentBinding.inflate(inflater, container, false)
        userRepo = DataStoreRepoUser(mainActivity.dataStore)

        return binding.root
    }
}