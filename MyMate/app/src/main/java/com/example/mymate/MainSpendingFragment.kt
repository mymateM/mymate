package com.example.mymate

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.mymate.databinding.MainSpendingFragmentBinding

class MainSpendingFragment : Fragment() {
    lateinit var binding: MainSpendingFragmentBinding
    lateinit var mainActivity: MainActivity

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as MainActivity
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = MainSpendingFragmentBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onResume() {
        //TODO: refresh data
        super.onResume()
    }
}