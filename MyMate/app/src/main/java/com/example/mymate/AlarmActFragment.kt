package com.example.mymate

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.mymate.databinding.AlarmActFragmentBinding

class AlarmActFragment : Fragment() {
    lateinit var alarmActivity: AlarmActivity
    lateinit var binding: AlarmActFragmentBinding

    override fun onAttach(context: Context) {
        super.onAttach(context)
        alarmActivity = context as AlarmActivity
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = AlarmActFragmentBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onResume() {
        //TODO: refresh data
        super.onResume()
    }
}