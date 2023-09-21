package com.example.mymate

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.os.Build
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.text.style.TypefaceSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import com.example.mymate.databinding.MainMypageFragmentBinding
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import java.lang.reflect.Type

class MainMypageFragment : Fragment() {
    lateinit var binding: MainMypageFragmentBinding
    lateinit var mainActivity: MainActivity

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as MainActivity
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = MainMypageFragmentBinding.inflate(inflater, container, false)

        //pieChart
        var pieChart = binding.mypagePiechart
        pieChart.setUsePercentValues(true)
        var entries = ArrayList<PieEntry>()
        entries.add(PieEntry(40f, ""))
        entries.add(PieEntry(60f, ""))
        var colorItem = ArrayList<Int>()
        colorItem.add(ContextCompat.getColor(mainActivity, R.color.graydark_wireframe))
        colorItem.add(ContextCompat.getColor(mainActivity, R.color.graylight_wireframe))
        var pieDataSet = PieDataSet(entries, "")
        var description = pieChart.description
        //stylespan
        val myTypeface = Typeface.create(ResourcesCompat.getFont(mainActivity, R.font.montserrat_bold), Typeface.NORMAL)
        val spannable = SpannableStringBuilder("00%")
        spannable.setSpan(ForegroundColorSpan(
            ContextCompat.getColor(mainActivity, R.color.black_text)),
            0,
            spannable.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        spannable.setSpan(TypefaceSpan(myTypeface), 0, spannable.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        //till here
        pieDataSet.apply {
            colors = colorItem
            valueTextColor = Color.TRANSPARENT
            valueTextSize = 0f
            setDrawValues(false)
        }
        pieChart.apply {
            data = PieData(pieDataSet)
            description.isEnabled = false
            legend.isEnabled = false
            isRotationEnabled = false
            holeRadius = 70f
            setHoleColor(Color.TRANSPARENT)
            transparentCircleRadius = 0f
            centerText = spannable
            setCenterTextSize(18f)
        }

        return binding.root
    }

    override fun onResume() {
        //TODO: refresh data
        super.onResume()
    }
}