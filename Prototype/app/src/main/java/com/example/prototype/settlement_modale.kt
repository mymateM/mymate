package com.example.prototype

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Context.CLIPBOARD_SERVICE
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import android.widget.AdapterView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.prototype.databinding.SettlementModaleBinding
import com.fasterxml.jackson.annotation.ObjectIdGenerators.StringIdGenerator
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.json.JSONObject
import org.json.JSONArray
import java.io.File
import com.fasterxml.jackson.module.kotlin.readValue
import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import java.io.Serializable

data class send_money(
    @SerializedName("message") var message: String = "",
    @SerializedName("status") var status: String = "",
    @SerializedName("data") var datareceived: data_settlement = data_settlement()
) : Serializable

data class data_settlement(
    @SerializedName("year_month") var year_month: String = "",
    @SerializedName("user") var user: user_settlement = user_settlement(),
    @SerializedName("room_mates") var room_mates : ArrayList<room_mate> = ArrayList<room_mate>()
)

data class user_settlement(
    @SerializedName("user_id") var user_id: String = "",
    @SerializedName("user_name") var user_name: String = "",
    @SerializedName("is_send") var is_send: Boolean = false,
    @SerializedName("user_settlement") var user_settlement: String = ""
)

data class room_mate(
    @SerializedName("userId") var user_id: String = "",
    @SerializedName("user_name") var user_name: String = "",
    @SerializedName("user_ratio") var user_ratio: String = "",
    @SerializedName("user_settlement_ratio") var user_settlement_ratio: String = "",
    @SerializedName("user_account_bank") var user_account_bank: String = "",
    @SerializedName("user_account") var user_account: String,
)

class settlement_modale : BottomSheetDialogFragment() {
    lateinit var settlementActivity : SettlementActivity
    private var sBinding : SettlementModaleBinding? = null
    private val binding get() = sBinding!!
    private var receivedmoney : send_money = send_money()
    lateinit var adapter: ModaleAdapter

    override fun onAttach(context: Context) {
        super.onAttach(context)
        settlementActivity = context as SettlementActivity
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        sBinding = SettlementModaleBinding.inflate(inflater, container, false)
        adapter = ModaleAdapter(settlementActivity, receivedmoney.datareceived.room_mates)
        binding.modallist.adapter = adapter
        binding.modallist.layoutManager = LinearLayoutManager(settlementActivity)
        binding.sendreceivebutton.setBackgroundResource(R.drawable.modale_button_unselected)
        binding.modaleflag.setTextColor(ContextCompat.getColor(settlementActivity ,R.color.graytext))
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        var retrofit = RetrofitClientInstance.client
        val endpoint = retrofit?.create(getSettlementBill::class.java)
        endpoint!!.getSettlementBill("1").enqueue(object: Callback<send_money> {
            override fun onResponse(call: Call<send_money>, response: Response<send_money>) {
                receivedmoney = response.body()!!
                adapter = ModaleAdapter(settlementActivity, receivedmoney.datareceived.room_mates).apply {
                    setOnStateInterface(object: ModaleAdapter.OnSendStateInterface{
                        override fun sendValue(position: Int) {
                            binding.sendreceivebutton.setBackgroundResource(R.drawable.textview_settlementbackground)
                            binding.modaleflag.setTextColor(ContextCompat.getColor(settlementActivity ,R.color.white))
                        }

                    })
                }
                binding.modallist.adapter = adapter
                binding.modallist.layoutManager = LinearLayoutManager(settlementActivity)
                var totaltext = receivedmoney.datareceived.user.user_settlement
                var textlength = totaltext.length
                var totalexpense = ""
                while (0 < textlength) {
                    var substring1 = ""
                    if (textlength == 3) {
                        if (totalexpense == "") {
                            totalexpense = totaltext.substring(0 until 3)
                        } else {
                            totalexpense = totaltext.substring(0 until 3) + "," + totalexpense
                        }
                    } else if (textlength > 3) {
                        substring1 = totaltext.substring(textlength - 3 until textlength)
                        if (totalexpense == "") {
                            totalexpense = substring1
                        } else {
                            totalexpense = substring1 + "," + totalexpense
                        }
                    } else if (textlength < 3) {
                        substring1 = totaltext.substring(0 until textlength)
                        totalexpense = substring1 + "," + totalexpense
                    }

                    textlength = textlength - 3
                }
                binding.totalmoneybill.text = totalexpense

                if (receivedmoney.datareceived.user.is_send == true) {
                    binding.totalmoneyflag.text = "보낼 돈"
                    binding.boldguideheader.text = "동거인 별 보낼 돈"
                    binding.modaleflag.text = "계좌 복사"
                } else if (receivedmoney.datareceived.user.is_send == false) {
                    binding.totalmoneyflag.text = "받을 돈"
                    binding.boldguideheader.text = "동거인 별 받을 돈"
                    binding.modaleflag.text = "송금 요청"
                }

                //TODO: callback implement

                binding.sendreceivebutton.setOnClickListener {
                    if (adapter.thisnum > receivedmoney.datareceived.room_mates.size) {
                        Toast.makeText(settlementActivity, "선택한 항목이 없습니다.", Toast.LENGTH_SHORT).show()
                    } else if (adapter.thisnum < 0) {
                        Toast.makeText(settlementActivity, "선택한 항목이 없습니다.", Toast.LENGTH_SHORT).show()
                    } else {
                        if (receivedmoney.datareceived.user.is_send == true) {
                            val datatogo = receivedmoney.datareceived.room_mates.get(adapter.thisnum)
                            var clipboardManager = requireActivity().getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
                            val clip = ClipData.newPlainText("", datatogo.user_account)
                            clipboardManager.setPrimaryClip(clip)

                            val dialogsend = settlement_send_popup(datatogo)
                            dialogsend.show(settlementActivity.supportFragmentManager, "SettlementSendPopup")
                        } else if (receivedmoney.datareceived.user.is_send == false) {
                            val datatogo = receivedmoney.datareceived.room_mates.get(adapter.thisnum)
                            val dialogreceive = settlement_receive_popup(datatogo)
                            dialogreceive.show(settlementActivity.supportFragmentManager, "SettlementReceivePopup")
                        }
                    }
                }
            }

            override fun onFailure(call: Call<send_money>, t: Throwable) {
                Toast.makeText(settlementActivity, "연결 실패입니다.", Toast.LENGTH_SHORT).show()
            }
        })
        binding.modalexit.setOnClickListener{
            dismiss()
        }
        return binding.root
    }

    override fun getTheme(): Int = R.style.modale_radius

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //TODO: 리스트가 어댑터에 들어가기 전까지는 초기화되어야 한다
    }
}