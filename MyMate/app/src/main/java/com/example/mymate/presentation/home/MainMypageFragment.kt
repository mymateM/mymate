package com.example.mymate.presentation.home

import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.os.Build
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.TypefaceSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.mymate.*
import com.example.mymate.databinding.MainMypageFragmentBinding
import com.example.mymate.presentation.main.MainActivity
import com.example.mymate.util.FontManager
import com.example.mymate.util.getTypefaceSpan

class MainMypageFragment : Fragment() {
    lateinit var mainActivity: MainActivity
    var resumed = "00"

    private var _binding: MainMypageFragmentBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MainMypageViewModel by viewModels()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as MainActivity
    }

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        initBinding(inflater, container)
        initInfo()
        return binding.root
    }

    private fun initBinding(inflater: LayoutInflater, container: ViewGroup?) {
        _binding = MainMypageFragmentBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.vm = viewModel
    }

    @RequiresApi(Build.VERSION_CODES.P)
    private fun initInfo() {
        val budgetIntent = Intent(mainActivity, MypageBudgetActivity::class.java)

        viewModel.profilePic.observe(viewLifecycleOwner) {
            //TODO: 현재 프로필 이미지 정보 존재하지 않음. 존재할 시 업데이트.
        }

        viewModel.settlementDate.observe(viewLifecycleOwner) {
            val settleDateTxt = SpannableStringBuilder(it)
            settleDateTxt.setSpan(FontManager.montserratBold.getTypefaceSpan(), 0, settleDateTxt.length - 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            binding.settleday.text = settleDateTxt
        }

        viewModel.householdBudget.observe(viewLifecycleOwner) {
            val budgetTxt = SpannableStringBuilder(it)
            budgetTxt.setSpan(FontManager.montserratBold.getTypefaceSpan(), 0, budgetTxt.length - 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            binding.budgetamount.text = budgetTxt
            if (it != null) {
                budgetIntent.putExtra("Budget", it)
            } else {
                budgetIntent.putExtra("Budget", "0원")
            }
        }

        viewModel.userPercentage.observe(viewLifecycleOwner) {
            val percentTxt = SpannableStringBuilder(it)
            percentTxt.setSpan(FontManager.montserratBold.getTypefaceSpan(), 0, percentTxt.length - 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            binding.myratio.text = percentTxt
        }

        binding.settledaycontainer.setOnClickListener {
            val dayIntent = Intent(mainActivity, MypageSettledayActivity::class.java)
            startActivity(dayIntent)
            mainActivity.overridePendingTransition(R.anim.right_enter, R.anim.none)
        }

        binding.toSetting.setOnClickListener {
            startActivity(Intent(mainActivity, SettingActivity::class.java))
            mainActivity.overridePendingTransition(R.anim.right_enter, R.anim.none)
        }

        binding.profileEdit.setOnClickListener {
            /*val profileIntent = Intent(mainActivity, MypageEditprofileActivity::class.java)
            profileIntent.putExtra("nickname", binding.nickname.text.toString())
            startActivity(profileIntent)
            mainActivity.overridePendingTransition(R.anim.right_enter, R.anim.none)*/
            Toast.makeText(mainActivity, "전시로 인해 프로필 변경이 불가합니다.", Toast.LENGTH_SHORT).show()
        }

        binding.ratiocontainer.setOnClickListener {
            val ratioIntent = Intent(mainActivity, MypageRatiodetailActivity::class.java)
            ratioIntent.putExtra("ratio", binding.myratio.text.toString())
            startActivity(ratioIntent)
            mainActivity.overridePendingTransition(R.anim.right_enter, R.anim.none)
        }

        binding.budgetcontainer.setOnClickListener {
            startActivity(budgetIntent)
            mainActivity.overridePendingTransition(R.anim.right_enter, R.anim.none)
        }

        binding.accountcontainer.setOnClickListener {
            startActivity(Intent(mainActivity, MypageAccountActivity::class.java))
            mainActivity.overridePendingTransition(R.anim.right_enter, R.anim.none)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        resumed = "01"
    }

    override fun onResume() {
        //TODO: refresh data
        super.onResume()
        initInfo()
    }
}