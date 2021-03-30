package com.softsquared.template.kotlin.src.mypage

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import android.widget.TextView
import com.github.mikephil.charting.components.MarkerView
import com.github.mikephil.charting.data.CandleEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.utils.MPPointF
import com.github.mikephil.charting.utils.Utils
import com.softsquared.template.kotlin.R


class MyMarkerView : MarkerView {

//    constructor(context: Context?, layoutResource: Int) : super(context, layoutResource)

    private var tvContent: TextView? = null

    constructor(context: Context?, layoutResource: Int) : super(context, layoutResource) {

        tvContent = findViewById<View>(R.id.tvContent) as TextView

    }

    @SuppressLint("SetTextI18n")
    override fun refreshContent(e: Entry?, highlight: Highlight?) {

        if (e is CandleEntry) {

            val ce = e
            tvContent!!.text = "" + Utils.formatNumber(ce.high, 0, true)
        } else {
            tvContent!!.text = "" + Utils.formatNumber(e!!.y, 0, true)
        }

        super.refreshContent(e, highlight)

    }

    override fun getOffset(): MPPointF {
        return MPPointF((-(width / 2)).toFloat(), (-height).toFloat())
    }


}