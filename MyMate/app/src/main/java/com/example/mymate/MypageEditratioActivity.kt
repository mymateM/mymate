package com.example.mymate

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.TextView.OnEditorActionListener
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material3.TopAppBar
import androidx.core.content.ContextCompat
import com.example.mymate.databinding.ActivityMypageEditratioBinding
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.create

class MypageEditratioActivity: AppCompatActivity() {
    lateinit var binding: ActivityMypageEditratioBinding
    lateinit var context: Context
    lateinit var userRepo: DataStoreRepoUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMypageEditratioBinding.inflate(layoutInflater)
        setContentView(binding.root)
        userRepo = DataStoreRepoUser(dataStore)
        context = this
        val retrofit = RetrofitClientInstance.client
        val endpoint = retrofit?.create(getHouseRatio::class.java)
        var accessToken = ""
        binding.completedbtn.isEnabled = false
        binding.completedbtn.setTextColor(ContextCompat.getColor(context, R.color.graydark_text))
        runBlocking {
            accessToken = userRepo.userAccessReadFlow.first().toString()
        }
        endpoint!!.getHouseRatio("Bearer $accessToken").enqueue(object: Callback<houseRatioResponse> {
            override fun onResponse(
                call: Call<houseRatioResponse>,
                response: Response<houseRatioResponse>
            ) {
                if (response.isSuccessful) {
                    val list = response.body()!!.data.household_members
                    binding.myHead.text = list[0].user_nickname
                    binding.myEdit.text = Editable.Factory.getInstance().newEditable(list[0].user_settlement_ratio)
                    binding.member1Head.text = list[1].user_nickname
                    binding.member1Edit.text = Editable.Factory.getInstance().newEditable(list[1].user_settlement_ratio)
                    binding.member2Head.text = list[2].user_nickname
                    binding.member2Edit.text = Editable.Factory.getInstance().newEditable(list[2].user_settlement_ratio)
                    binding.member3Head.text = list[3].user_nickname
                    binding.member3Edit.text = Editable.Factory.getInstance().newEditable(list[3].user_settlement_ratio)

                    binding.completedbtn.isEnabled = true
                    binding.completedbtn.setTextColor(ContextCompat.getColor(context, R.color.purplemute_background))

                    binding.myEdit.addTextChangedListener(object : TextWatcher {
                        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                        }

                        override fun afterTextChanged(p0: Editable?) {
                            if (binding.myEdit.text.toString().isNotEmpty() && binding.myEdit.text.toString().toInt() == 0 && binding.myEdit.text.toString() != "") {
                                Toast.makeText(context, "입력가능한 최소값은 1이에요!", Toast.LENGTH_SHORT).show()
                                binding.myEdit.text = Editable.Factory.getInstance().newEditable(list[0].user_settlement_ratio)
                                binding.member1Edit.text = Editable.Factory.getInstance().newEditable(list[1].user_settlement_ratio)
                                binding.member2Edit.text = Editable.Factory.getInstance().newEditable(list[2].user_settlement_ratio)
                                binding.member3Edit.text = Editable.Factory.getInstance().newEditable(list[3].user_settlement_ratio)
                            }
                        }
                    })

                    binding.member1Edit.addTextChangedListener(object : TextWatcher {
                        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                        }

                        override fun afterTextChanged(p0: Editable?) {
                            if (binding.member1Edit.text.toString().isNotEmpty() && binding.member1Edit.text.toString().toInt() == 0 && binding.member1Edit.text.toString() != "") {
                                Toast.makeText(context, "입력가능한 최소값은 1이에요!", Toast.LENGTH_SHORT).show()
                                binding.myEdit.text = Editable.Factory.getInstance().newEditable(list[0].user_settlement_ratio)
                                binding.member1Edit.text = Editable.Factory.getInstance().newEditable(list[1].user_settlement_ratio)
                                binding.member2Edit.text = Editable.Factory.getInstance().newEditable(list[2].user_settlement_ratio)
                                binding.member3Edit.text = Editable.Factory.getInstance().newEditable(list[3].user_settlement_ratio)
                            }
                        }
                    })

                    binding.member2Edit.addTextChangedListener(object : TextWatcher {
                        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                        }

                        override fun afterTextChanged(p0: Editable?) {
                            if (binding.member2Edit.text.toString().isNotEmpty() && binding.member2Edit.text.toString().toInt() == 0 && binding.member2Edit.text.toString() != "") {
                                Toast.makeText(context, "입력가능한 최소값은 1이에요!", Toast.LENGTH_SHORT).show()
                                binding.myEdit.text = Editable.Factory.getInstance().newEditable(list[0].user_settlement_ratio)
                                binding.member1Edit.text = Editable.Factory.getInstance().newEditable(list[1].user_settlement_ratio)
                                binding.member2Edit.text = Editable.Factory.getInstance().newEditable(list[2].user_settlement_ratio)
                                binding.member3Edit.text = Editable.Factory.getInstance().newEditable(list[3].user_settlement_ratio)
                            }
                        }
                    })

                    binding.member3Edit.addTextChangedListener(object : TextWatcher {
                        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                        }

                        override fun afterTextChanged(p0: Editable?) {
                            if (binding.member3Edit.text.toString().isNotEmpty() && binding.member3Edit.text.toString().toInt() == 0 && binding.member3Edit.text.toString() != "") {
                                Toast.makeText(context, "입력가능한 최소값은 1이에요!", Toast.LENGTH_SHORT).show()
                                binding.myEdit.text = Editable.Factory.getInstance().newEditable(list[0].user_settlement_ratio)
                                binding.member1Edit.text = Editable.Factory.getInstance().newEditable(list[1].user_settlement_ratio)
                                binding.member2Edit.text = Editable.Factory.getInstance().newEditable(list[2].user_settlement_ratio)
                                binding.member3Edit.text = Editable.Factory.getInstance().newEditable(list[3].user_settlement_ratio)
                            }
                        }
                    })

                    binding.completedbtn.setOnClickListener {
                        val completeEndpoint = retrofit.create(postHouseRatio::class.java)
                        val userIdEndpoint = retrofit.create(getMemberId::class.java)
                        userIdEndpoint.getMemberId("Bearer $accessToken").enqueue(object : Callback<getMemberIdResponse> {
                            override fun onResponse(
                                call: Call<getMemberIdResponse>,
                                response: Response<getMemberIdResponse>
                            ) {
                                if (response.isSuccessful) {
                                    val first = binding.myEdit.text.toString()
                                    val second = binding.member1Edit.text.toString()
                                    val third = binding.member2Edit.text.toString()
                                    val fourth = binding.member3Edit.text.toString()
                                    val data = response.body()!!.data

                                    if (first.toInt() + second.toInt() + third.toInt() + fourth.toInt() == 100) {
                                        val listToSend = houseRatioPost()
                                        listToSend.household_members.add(houseMemberSimple(data[0], first))
                                        listToSend.household_members.add(houseMemberSimple(data[1], second))
                                        listToSend.household_members.add(houseMemberSimple(data[2], third))
                                        listToSend.household_members.add(houseMemberSimple(data[3], fourth))

                                        completeEndpoint.postHouseRatio("Bearer $accessToken", listToSend).enqueue(object: Callback<Response<Void>> {
                                            override fun onResponse(
                                                call: Call<Response<Void>>,
                                                response: Response<Response<Void>>
                                            ) {
                                                finish()
                                            }

                                            override fun onFailure(
                                                call: Call<Response<Void>>,
                                                t: Throwable
                                            ) {
                                                Toast.makeText(context, "연결 실패(정산 비율-비율 수정)", Toast.LENGTH_SHORT).show()
                                            }

                                        })
                                    }
                                }
                            }

                            override fun onFailure(call: Call<getMemberIdResponse>, t: Throwable) {
                                Toast.makeText(context, "연결 실패(정산 비율-멤버 Id)", Toast.LENGTH_SHORT).show()
                            }

                        })
                    }
                }
            }

            override fun onFailure(call: Call<houseRatioResponse>, t: Throwable) {
                Toast.makeText(context, "연결 실패(비율 수정-비율 조회)", Toast.LENGTH_SHORT).show()
            }
        })

        binding.myEdit.setOnEditorActionListener(object: OnEditorActionListener {
            override fun onEditorAction(p0: TextView?, p1: Int, p2: KeyEvent?): Boolean {
                if (p1 == EditorInfo.IME_ACTION_NEXT || p1 == EditorInfo.IME_ACTION_DONE) {
                    hidekeyboard()
                    if (binding.myEdit.text.isNullOrEmpty()) {
                        Toast.makeText(context, "입력가능한 최소값은 1이에요!", Toast.LENGTH_SHORT).show()
                        binding.myEdit.text = Editable.Factory.getInstance().newEditable("1")
                    }
                    binding.member1Edit.text = Editable.Factory.getInstance().newEditable(((100 - binding.myEdit.text.toString().toInt()) / 3 + (100 - binding.myEdit.text.toString().toInt()) % 3).toString())
                    binding.member2Edit.text = Editable.Factory.getInstance().newEditable(((100 - binding.myEdit.text.toString().toInt()) / 3).toString())
                    binding.member3Edit.text = Editable.Factory.getInstance().newEditable(((100 - binding.myEdit.text.toString().toInt()) / 3).toString())
                }
                return false
            }
        })

        binding.member1Edit.setOnEditorActionListener(object: OnEditorActionListener {
            override fun onEditorAction(p0: TextView?, p1: Int, p2: KeyEvent?): Boolean {
                if (p1 == EditorInfo.IME_ACTION_NEXT || p1 == EditorInfo.IME_ACTION_DONE) {
                    hidekeyboard()
                    if (binding.member1Edit.text.isNullOrEmpty()) {
                        Toast.makeText(context, "입력가능한 최소값은 1이에요!", Toast.LENGTH_SHORT).show()
                        binding.member1Edit.text = Editable.Factory.getInstance().newEditable("1")
                    }
                    binding.myEdit.text = Editable.Factory.getInstance().newEditable(((100 - binding.member1Edit.text.toString().toInt()) / 3 + (100 - binding.member1Edit.text.toString().toInt()) % 3).toString())
                    binding.member2Edit.text = Editable.Factory.getInstance().newEditable(((100 - binding.member1Edit.text.toString().toInt()) / 3).toString())
                    binding.member3Edit.text = Editable.Factory.getInstance().newEditable(((100 - binding.member1Edit.text.toString().toInt()) / 3).toString())
                }
                return false
            }
        })

        binding.member2Edit.setOnEditorActionListener(object: OnEditorActionListener {
            override fun onEditorAction(p0: TextView?, p1: Int, p2: KeyEvent?): Boolean {
                if (p1 == EditorInfo.IME_ACTION_NEXT || p1 == EditorInfo.IME_ACTION_DONE) {
                    hidekeyboard()
                    if (binding.member2Edit.text.isNullOrEmpty()) {
                        Toast.makeText(context, "입력가능한 최소값은 1이에요!", Toast.LENGTH_SHORT).show()
                        binding.member2Edit.text = Editable.Factory.getInstance().newEditable("1")
                    }
                    binding.myEdit.text = Editable.Factory.getInstance().newEditable(((100 - binding.member2Edit.text.toString().toInt()) / 3 + (100 - binding.member2Edit.text.toString().toInt()) % 3).toString())
                    binding.member1Edit.text = Editable.Factory.getInstance().newEditable(((100 - binding.member2Edit.text.toString().toInt()) / 3).toString())
                    binding.member3Edit.text = Editable.Factory.getInstance().newEditable(((100 - binding.member2Edit.text.toString().toInt()) / 3).toString())
                }
                return false
            }
        })

        binding.member3Edit.setOnEditorActionListener(object: OnEditorActionListener {
            override fun onEditorAction(p0: TextView?, p1: Int, p2: KeyEvent?): Boolean {
                if (p1 == EditorInfo.IME_ACTION_NEXT || p1 == EditorInfo.IME_ACTION_DONE) {
                    hidekeyboard()
                    if (binding.member3Edit.text.isNullOrEmpty()) {
                        Toast.makeText(context, "입력가능한 최소값은 1이에요!", Toast.LENGTH_SHORT).show()
                        binding.member3Edit.text = Editable.Factory.getInstance().newEditable("1")
                    }
                    binding.myEdit.text = Editable.Factory.getInstance().newEditable(((100 - binding.member3Edit.text.toString().toInt()) / 3 + (100 - binding.member3Edit.text.toString().toInt()) % 3).toString())
                    binding.member1Edit.text = Editable.Factory.getInstance().newEditable(((100 - binding.member3Edit.text.toString().toInt()) / 3).toString())
                    binding.member2Edit.text = Editable.Factory.getInstance().newEditable(((100 - binding.member3Edit.text.toString().toInt()) / 3).toString())
                }
                return false
            }
        })

        binding.root.setOnClickListener {
            val focusview = currentFocus
            if (currentFocus == binding.myEdit) {
                if (binding.myEdit.text.isNullOrEmpty()) {
                    Toast.makeText(context, "입력가능한 최소값은 1이에요!", Toast.LENGTH_SHORT).show()
                    binding.myEdit.text = Editable.Factory.getInstance().newEditable("1")
                }
                binding.member1Edit.text = Editable.Factory.getInstance().newEditable(((100 - binding.myEdit.text.toString().toInt()) / 3 + (100 - binding.myEdit.text.toString().toInt()) % 3).toString())
                binding.member2Edit.text = Editable.Factory.getInstance().newEditable(((100 - binding.myEdit.text.toString().toInt()) / 3).toString())
                binding.member3Edit.text = Editable.Factory.getInstance().newEditable(((100 - binding.myEdit.text.toString().toInt()) / 3).toString())
            } else if (currentFocus == binding.member1Edit) {
                if (binding.member1Edit.text.isNullOrEmpty()) {
                    Toast.makeText(context, "입력가능한 최소값은 1이에요!", Toast.LENGTH_SHORT).show()
                    binding.member1Edit.text = Editable.Factory.getInstance().newEditable("1")
                }
                binding.myEdit.text = Editable.Factory.getInstance().newEditable(((100 - binding.member1Edit.text.toString().toInt()) / 3 + (100 - binding.member1Edit.text.toString().toInt()) % 3).toString())
                binding.member2Edit.text = Editable.Factory.getInstance().newEditable(((100 - binding.member1Edit.text.toString().toInt()) / 3).toString())
                binding.member3Edit.text = Editable.Factory.getInstance().newEditable(((100 - binding.member1Edit.text.toString().toInt()) / 3).toString())
            } else if (currentFocus == binding.member2Edit) {
                if (binding.member2Edit.text.isNullOrEmpty()) {
                    Toast.makeText(context, "입력가능한 최소값은 1이에요!", Toast.LENGTH_SHORT).show()
                    binding.member2Edit.text = Editable.Factory.getInstance().newEditable("1")
                }
                binding.myEdit.text = Editable.Factory.getInstance().newEditable(((100 - binding.member2Edit.text.toString().toInt()) / 3 + (100 - binding.member2Edit.text.toString().toInt()) % 3).toString())
                binding.member1Edit.text = Editable.Factory.getInstance().newEditable(((100 - binding.member2Edit.text.toString().toInt()) / 3).toString())
                binding.member3Edit.text = Editable.Factory.getInstance().newEditable(((100 - binding.member2Edit.text.toString().toInt()) / 3).toString())
            } else if (currentFocus == binding.member3Edit) {
                if (binding.member3Edit.text.isNullOrEmpty()) {
                    Toast.makeText(context, "입력가능한 최소값은 1이에요!", Toast.LENGTH_SHORT).show()
                    binding.member3Edit.text = Editable.Factory.getInstance().newEditable("1")
                }
                binding.myEdit.text = Editable.Factory.getInstance().newEditable(((100 - binding.member3Edit.text.toString().toInt()) / 3 + (100 - binding.member3Edit.text.toString().toInt()) % 3).toString())
                binding.member1Edit.text = Editable.Factory.getInstance().newEditable(((100 - binding.member3Edit.text.toString().toInt()) / 3).toString())
                binding.member2Edit.text = Editable.Factory.getInstance().newEditable(((100 - binding.member3Edit.text.toString().toInt()) / 3).toString())
            }
            hidekeyboard()
        }
    }

    private fun hidekeyboard() {
        //키보드 내리기
        val imm = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        val focusview = currentFocus
        imm.hideSoftInputFromWindow(this.window.decorView.applicationWindowToken, 0)
        focusview?.clearFocus()
    }
}