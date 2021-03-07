package com.softsquared.template.kotlin.src.main.schedulefind.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.softsquared.template.kotlin.R
import com.softsquared.template.kotlin.src.main.schedulefind.models.ScheduleBookmarkData
import com.softsquared.template.kotlin.src.main.schedulefind.models.ScheduleLatelyData

class ScheduleLatelyAdapter(var latelyList:ArrayList<ScheduleLatelyData>):
    RecyclerView.Adapter<ScheduleLatelyAdapter.ScheduleLatelyHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScheduleLatelyHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recyclerview_schedule_find_bookmark_item,parent,false)
        view.setOnClickListener {

        }
        return ScheduleLatelyHolder(view)
    }

    override fun onBindViewHolder(holder: ScheduleLatelyHolder, position: Int) {
        holder.title.text = latelyList[position].title
        holder.times.text = latelyList[position].times
    }

    override fun getItemCount(): Int = latelyList.size

    class ScheduleLatelyHolder(itemView : View): RecyclerView.ViewHolder(itemView){

        val title = itemView.findViewById<TextView>(R.id.recycler_part_title)
        val times = itemView.findViewById<TextView>(R.id.recycler_part_times)

    }

}