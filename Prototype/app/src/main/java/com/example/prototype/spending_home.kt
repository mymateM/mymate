package com.example.prototype

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.prototype.databinding.SpendingHomeBinding

class spending_home : Fragment() {

    private var hbinding : SpendingHomeBinding? = null
    private val binding get() = hbinding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        hbinding = SpendingHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

}