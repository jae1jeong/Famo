package com.makeus.jfive.famo.src.main.monthly.calendar

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.makeus.jfive.famo.R
import kotlinx.android.synthetic.main.item_calendar.view.*

class CalendarAdapter:RecyclerView.Adapter<CalendarAdapter.CalendarViewHolder>() {
    inner class CalendarViewHolder(itemView:View):RecyclerView.ViewHolder(itemView){

        fun bind(data:Int){

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalendarViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_calendar,parent,false)
        return CalendarViewHolder(view)
    }

    override fun onBindViewHolder(holder: CalendarViewHolder, position: Int) {
    }

    override fun getItemCount(): Int  = 1
}