package com.example.prototype

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.SpannableString
import android.text.style.UnderlineSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.compose.ui.graphics.Paint
import androidx.fragment.app.DialogFragment
import com.example.prototype.databinding.SettlementModalePopupBinding
import com.example.prototype.databinding.SettlementSendPopupBinding

class settlement_send_popup(var data: room_mate): DialogFragment() {

    private var _binding: SettlementModalePopupBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = SettlementModalePopupBinding.inflate(inflater, container, false)
        val view = binding.root
        binding.popupnametxt.text = data.user_name + "의\n계좌 복사 완료"
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        var spanstring = SpannableString("")
        if (data.user_account_bank == "kb") {
            spanstring = SpannableString("국민 " + data.user_account)
            spanstring.setSpan(UnderlineSpan(), 0, spanstring.length, 0)
            binding.popupaccount.text = spanstring
        } else if (data.user_account_bank == "woori") {
            spanstring = SpannableString("우리 " + data.user_account)
            spanstring.setSpan(UnderlineSpan(), 0, spanstring.length, 0)
            binding.popupaccount.text = spanstring
        } else if (data.user_account_bank == "nh") {
            spanstring = SpannableString("농협 " + data.user_account)
            spanstring.setSpan(UnderlineSpan(), 0, spanstring.length, 0)
            binding.popupaccount.text = spanstring
        }

        binding.goback.setOnClickListener {
            dismiss()
        }

        binding.gohome.setOnClickListener {
            var gohomeIntent = Intent(context, MainActivity::class.java)
            gohomeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            gohomeIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
            startActivity(gohomeIntent)
        }

        return view
    }
}