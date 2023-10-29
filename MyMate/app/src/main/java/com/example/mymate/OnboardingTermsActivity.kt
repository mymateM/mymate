package com.example.mymate

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import androidx.core.view.isGone
import com.example.mymate.databinding.ActivityOnboardingTermsBinding

class OnboardingTermsActivity: AppCompatActivity() {
    lateinit var binding: ActivityOnboardingTermsBinding
    lateinit var context: Context
    private var allcheck = false
    private var servicecheck = false
    private var infocheck = false
    private var marketingcheck = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        context = this
        binding = ActivityOnboardingTermsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val constraintSet = ConstraintSet()

        val servicespan = SpannableStringBuilder(binding.serviceterm.text)
        val infospan = SpannableStringBuilder(binding.infoterm.text)

        servicespan.setSpan(ForegroundColorSpan(ContextCompat.getColor(context, R.color.purpleblue_select)), 0, 4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        infospan.setSpan(ForegroundColorSpan(ContextCompat.getColor(context, R.color.purpleblue_select)), 0, 4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

        binding.serviceterm.text = servicespan
        binding.infoterm.text = infospan

        binding.showserviceterm.setOnClickListener {
            if (binding.servicetermtext.isGone) {
                binding.servicetermtext.isGone = false
                binding.infotermtext.isGone = true
                binding.marketingtermtext.isGone = true
                rearrange(constraintSet, binding.infoterm.id, binding.servicetermtext.id)
                rearrange(constraintSet, binding.marketingterm.id, binding.infoterm.id)
            } else {
                binding.servicetermtext.isGone = true
                binding.infotermtext.isGone = true
                binding.marketingtermtext.isGone = true
                rearrange(constraintSet, binding.infoterm.id, binding.serviceterm.id)
                rearrange(constraintSet, binding.marketingterm.id, binding.infoterm.id)
            }
        }

        binding.showinfoterm.setOnClickListener {
            if (binding.infotermtext.isGone) {
                binding.servicetermtext.isGone = true
                binding.infotermtext.isGone = false
                binding.marketingtermtext.isGone = true
                rearrange(constraintSet, binding.marketingterm.id, binding.infotermtext.id)
                rearrange(constraintSet, binding.infoterm.id, binding.serviceterm.id)
            } else {
                binding.servicetermtext.isGone = true
                binding.infotermtext.isGone = true
                binding.marketingtermtext.isGone = true
                rearrange(constraintSet, binding.infoterm.id, binding.serviceterm.id)
                rearrange(constraintSet, binding.marketingterm.id, binding.infoterm.id)
            }
        }

        binding.showmarketingterm.setOnClickListener {
            if (binding.marketingtermtext.isGone) {
                binding.servicetermtext.isGone = true
                binding.infotermtext.isGone = true
                binding.marketingtermtext.isGone = false
                rearrange(constraintSet, binding.infoterm.id, binding.serviceterm.id)
                rearrange(constraintSet, binding.marketingterm.id, binding.infoterm.id)
            } else {
                binding.servicetermtext.isGone = true
                binding.infotermtext.isGone = true
                binding.marketingtermtext.isGone = true
                rearrange(constraintSet, binding.infoterm.id, binding.serviceterm.id)
                rearrange(constraintSet, binding.marketingterm.id, binding.infoterm.id)
            }
        }

        binding.selectall.setOnClickListener {
            if (allcheck) {
                binding.selectall.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.button_selectbox))
                binding.selectallcheck.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.icon_check_default))
                binding.selectserviceterm.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.icon_check_default))
                binding.selectinfoterm.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.icon_check_default))
                binding.selectmarketingterm.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.icon_check_default))
                allcheck = false
                servicecheck = false
                infocheck = false
                marketingcheck = false
                agreement()
            } else {
                binding.selectall.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.button_selectedbox))
                binding.selectallcheck.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.icon_check_active))
                binding.selectserviceterm.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.icon_check_active))
                binding.selectinfoterm.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.icon_check_active))
                binding.selectmarketingterm.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.icon_check_active))
                allcheck = true
                servicecheck = true
                infocheck = true
                marketingcheck = true
                agreement()
            }
        }

        binding.selectserviceterm.setOnClickListener {
            if (servicecheck) {
                binding.selectserviceterm.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.icon_check_default))
                servicecheck = false
                allcheck = false
                agreement()
            } else {
                binding.selectserviceterm.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.icon_check_active))
                servicecheck = true
                agreement()
            }
        }

        binding.serviceterm.setOnClickListener {
            if (servicecheck) {
                binding.selectserviceterm.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.icon_check_default))
                servicecheck = false
                allcheck = false
                agreement()
            } else {
                binding.selectserviceterm.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.icon_check_active))
                servicecheck = true
                agreement()
            }
        }

        binding.selectinfoterm.setOnClickListener {
            if (infocheck) {
                binding.selectinfoterm.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.icon_check_default))
                infocheck = false
                allcheck = false
                agreement()
            } else {
                binding.selectinfoterm.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.icon_check_active))
                infocheck = true
                agreement()
            }
        }

        binding.infoterm.setOnClickListener {
            if (infocheck) {
                binding.selectinfoterm.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.icon_check_default))
                infocheck = false
                allcheck = false
                agreement()
            } else {
                binding.selectinfoterm.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.icon_check_active))
                infocheck = true
                agreement()
            }
        }

        binding.selectmarketingterm.setOnClickListener {
            if (marketingcheck) {
                binding.selectmarketingterm.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.icon_check_default))
                marketingcheck = false
                allcheck = false
                agreement()
            } else {
                binding.selectmarketingterm.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.icon_check_active))
                marketingcheck = true
                agreement()
            }
        }

        binding.marketingterm.setOnClickListener {
            if (marketingcheck) {
                binding.selectmarketingterm.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.icon_check_default))
                marketingcheck = false
                agreement()
            } else {
                binding.selectmarketingterm.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.icon_check_active))
                marketingcheck = true
                agreement()
            }
        }

        binding.tonext.setOnClickListener {
            startActivity(Intent(context, LocalJoinEmailActivity::class.java))
        }

        binding.backbtn.setOnClickListener {
            finish()
        }
    }

    fun agreement() {
        if (allcheck) {
            binding.tonext.isEnabled = true
            binding.tonext.setBackgroundColor(ContextCompat.getColor(context, R.color.purplemute_background))
        } else if (infocheck && servicecheck) {
            if (marketingcheck) {
                binding.selectall.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.button_selectedbox))
                binding.selectallcheck.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.icon_check_active))
                allcheck = true
            } else {
                allcheck = false
                binding.selectall.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.button_selectbox))
                binding.selectallcheck.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.icon_check_default))
            }
            binding.tonext.isEnabled = true
            binding.tonext.setBackgroundColor(ContextCompat.getColor(context, R.color.purplemute_background))
        } else {
            binding.tonext.isEnabled = false
            allcheck = false
            binding.tonext.setBackgroundColor(ContextCompat.getColor(context, R.color.graylight_wireframe))
            binding.selectall.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.button_selectbox))
            binding.selectallcheck.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.icon_check_default))
        }
    }

    fun rearrange(constraintSet: ConstraintSet, targetid: Int, topid: Int) {
        constraintSet.clone(binding.root)
        val margintop = dptopx(22.0.toFloat()).toInt()
        constraintSet.connect(targetid, ConstraintSet.TOP, topid, ConstraintSet.BOTTOM, margintop)
        constraintSet.applyTo(binding.root)
    }

    private fun dptopx(dp: Float): Float {
        val density = resources.displayMetrics.density
        return density * dp
    }
}