package com.example.prototype

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.prototype.databinding.SettlementSendPopupBinding

class settlement_receive_popup(var data: room_mate): DialogFragment() {

    private var _binding: SettlementSendPopupBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = SettlementSendPopupBinding.inflate(inflater, container, false)
        val view = binding.root
        binding.popupnametxtSend.text = data.user_name + "에게\n송금 요청 보냈어요"
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        binding.gobackSend.setOnClickListener {
            dismiss()
        }

        binding.gohomeSend.setOnClickListener {
            var gohomeIntent = Intent(context, MainActivity::class.java)
            gohomeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            gohomeIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
            startActivity(gohomeIntent)
        }

        return view
    }
}