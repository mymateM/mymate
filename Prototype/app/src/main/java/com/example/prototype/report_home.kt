package com.example.prototype

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.prototype.databinding.ReportHomeBinding

class report_home : Fragment() {

    private var rbinding : ReportHomeBinding? = null
    private val binding get() = rbinding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rbinding = ReportHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

}