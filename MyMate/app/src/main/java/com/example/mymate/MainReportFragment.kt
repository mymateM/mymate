package com.example.mymate

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.mymate.databinding.MainReportFragmentBinding
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry

class MainReportFragment : Fragment() {
    lateinit var binding: MainReportFragmentBinding
    lateinit var mainActivity: MainActivity

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as MainActivity
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = MainReportFragmentBinding.inflate(inflater, container, false)

        //여기부터 파이차트 테스트
        var pieChart = binding.pieTest
        pieChart.setUsePercentValues(true)
        var entries = ArrayList<PieEntry>()
        entries.add(PieEntry(508f, "생필품"))
        entries.add(PieEntry(600f, "식사"))
        entries.add(PieEntry(750f, "월세"))
        entries.add(PieEntry(508f, "공과금"))
        entries.add(PieEntry(670f, "기타"))
        var colorItem = ArrayList<Int>()
        colorItem.add(ContextCompat.getColor(mainActivity, R.color.pink_character))
        colorItem.add(ContextCompat.getColor(mainActivity, R.color.green_character))
        colorItem.add(ContextCompat.getColor(mainActivity, R.color.blue_character))
        colorItem.add(ContextCompat.getColor(mainActivity, R.color.yellow_character))
        colorItem.add(ContextCompat.getColor(mainActivity, R.color.purplevivid_graph))
        var pieDataSet = PieDataSet(entries, "")
        pieDataSet.apply {
            colors = colorItem
            valueTextColor = ContextCompat.getColor(mainActivity, R.color.black_text)
            valueTextSize = 16f
            setDrawValues(false)
        }
        pieChart.apply {
            data = PieData(pieDataSet)
            description.isEnabled = false
            isRotationEnabled = false
            holeRadius = 0f
            transparentCircleRadius = 0f
        }

        return binding.root
    }

    override fun onResume() {
        //TODO: refresh data
        super.onResume()
    }
}