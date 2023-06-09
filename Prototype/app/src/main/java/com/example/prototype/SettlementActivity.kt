package com.example.prototype

import android.content.Context
import android.os.Bundle
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.prototype.databinding.ActivitySettlementBinding
import com.example.prototype.ui.theme.PrototypeTheme
import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

data class settlementdata(
    @SerializedName("message") var message: String = "",
    @SerializedName("status") var status: Int = 0,
    @SerializedName("data") var data: datasettlement = datasettlement()
)

data class datasettlement(
    @SerializedName("expense_total") var expense_total: String = "",
    @SerializedName("year_month") var year_month: String = "",
    @SerializedName("user") var userreceived: usersettlement = usersettlement()
)

data class usersettlement(
    @SerializedName("user_id") var user_id: Int = 0,
    @SerializedName("user_ratio") var user_ratio: String = "",
    @SerializedName("user_real_expense") var user_real_expense: String = "",
    @SerializedName("user_ratio_expense") var user_ratio_expense: String = "",
    @SerializedName("is_send") var is_send: Boolean = false,
    @SerializedName("user_settlement") var user_settlement: String = ""
)

class SettlementActivity : AppCompatActivity() {

    lateinit var binding : ActivitySettlementBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettlementBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val modalebutton = binding.modalepop
        modalebutton.setOnClickListener {
            val modaleView = settlement_modale()
            modaleView.show(supportFragmentManager, modaleView.tag)
        }

        var requestSettlement: settlementdata = settlementdata()

        var retrofit = RetrofitClientInstance.client
        var endpoint = retrofit?.create(getSettlement::class.java)
        endpoint!!.getSettlement("1").enqueue(object: Callback<settlementdata> {
            override fun onResponse(
                call: Call<settlementdata>,
                response: Response<settlementdata>
            ) {

                var totaltext = ""
                var totalexpense = ""
                var textlength = 0

                requestSettlement = response.body()!!
                totaltext = requestSettlement.data.expense_total
                textlength = totaltext.length
                totalexpense = ""
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
                binding.totalexpensetext.text = totalexpense

                var timetext = requestSettlement.data.year_month.split("-")
                var monthnum = timetext[1].toInt()
                var monthtxt = monthnum.toString()
                binding.settlementmonth.text = monthtxt + "월"
                binding.reportxt.text = monthtxt + "월 리포트"

                binding.percentset.text = "(" + requestSettlement.data.userreceived.user_ratio + "%)"
                totaltext = requestSettlement.data.userreceived.user_ratio_expense
                textlength = totaltext.length
                totalexpense = ""
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
                binding.percentbill.text = totalexpense

                totaltext = requestSettlement.data.userreceived.user_real_expense
                textlength = totaltext.length
                totalexpense = ""
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
                binding.realbill.text = totalexpense

                totaltext = requestSettlement.data.userreceived.user_settlement
                textlength = totaltext.length
                totalexpense = ""
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
                binding.totalbill.text = totalexpense
                binding.modalebill.text = totalexpense

                if (requestSettlement.data.userreceived.is_send == true) {
                    binding.modaletext.text = "원 보내러 가기"
                } else {
                    binding.modaletext.text = "원 받으러 가기"
                }

            }

            override fun onFailure(call: Call<settlementdata>, t: Throwable) {
                Toast.makeText(applicationContext, "연결 실패입니다.", Toast.LENGTH_SHORT).show()
            }

        })

        binding.settlementdismiss.setOnClickListener{
            finish()
        }
    }
}