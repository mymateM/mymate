package com.example.mymate.presentation.alarm.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mymate.databinding.AlarmItemFragmentBinding
import com.example.mymate.presentation.alarm.viewmodel.AlarmViewModel
import com.example.mymate.presentation.alarm.adapter.AlarmActContainerAdapter

class AlarmActFragment : Fragment() {
    private var _binding: AlarmItemFragmentBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AlarmViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        initBinding(inflater, container)
        getNoti()
        return binding.root
    }

    private fun getNoti() {
        viewModel.getActNoti()
        binding.alarmData.layoutManager = LinearLayoutManager(context)
        viewModel.actNoti.observe(viewLifecycleOwner) {
            if (binding.alarmData.adapter != null) {
                binding.alarmData.adapter!!.notifyDataSetChanged()
            } else {
                binding.alarmData.adapter = AlarmActContainerAdapter(it)
            }
        }
    }

    override fun onResume() {
        getNoti()
        super.onResume()
    }

    private fun initBinding(inflater: LayoutInflater, container: ViewGroup?) {
        _binding = AlarmItemFragmentBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.vm = viewModel
    }
}