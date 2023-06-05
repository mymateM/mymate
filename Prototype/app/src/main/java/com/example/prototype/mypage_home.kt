package com.example.prototype

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.prototype.databinding.MypageHomeBinding

class mypage_home : Fragment() {

    private var mbinding : MypageHomeBinding? = null
    private val binding get() = mbinding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mbinding = MypageHomeBinding.inflate(inflater, container, false)
        return binding.root
    }
}