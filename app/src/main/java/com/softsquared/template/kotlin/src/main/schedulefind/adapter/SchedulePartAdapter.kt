package com.softsquared.template.kotlin.src.main.schedulefind.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.softsquared.template.kotlin.R
import com.softsquared.template.kotlin.src.main.schedulefind.models.SchedulePartData
import com.softsquared.template.kotlin.src.main.schedulefind.models.ScheduleWholeData

class SchedulePartAdapter(var partList:ArrayList<SchedulePartData>): RecyclerView.Adapter<SchedulePartAdapter.SchedulePartHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SchedulePartAdapter.SchedulePartHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_schedule_find_bookmark_item,parent,false)
        view.setOnClickListener {

        }
        return SchedulePartHolder(view)
    }

    override fun onBindViewHolder(holder: SchedulePartHolder, position: Int) {
        holder.title.text = partList[position].title
        holder.times.text = partList[position].times
    }

    override fun getItemCount(): Int = partList.size

    class SchedulePartHolder(itemView : View): RecyclerView.ViewHolder(itemView){

        val title = itemView.findViewById<TextView>(R.id.recycler_part_title)
        val times = itemView.findViewById<TextView>(R.id.recycler_part_times)


    }

}