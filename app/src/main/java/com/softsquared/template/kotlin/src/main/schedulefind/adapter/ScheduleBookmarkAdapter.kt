package com.softsquared.template.kotlin.src.main.schedulefind.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.softsquared.template.kotlin.R
import com.softsquared.template.kotlin.src.main.schedulefind.models.ScheduleBookmarkData

class ScheduleBookmarkAdapter(var bookmarkList:ArrayList<ScheduleBookmarkData>):
    RecyclerView.Adapter<ScheduleBookmarkAdapter.ScheduleBookmarkHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScheduleBookmarkHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recyclerview_schedule_find_bookmark_item,parent,false)
        view.setOnClickListener {

        }
        return ScheduleBookmarkHolder(view)
    }

    override fun onBindViewHolder(holder: ScheduleBookmarkHolder, position: Int) {
        holder.title.text = bookmarkList[position].title
        holder.times.text = bookmarkList[position].times
    }

    override fun getItemCount(): Int = bookmarkList.size

    class ScheduleBookmarkHolder(itemView : View): RecyclerView.ViewHolder(itemView){

        val title = itemView.findViewById<TextView>(R.id.recycler_part_title)
        val times = itemView.findViewById<TextView>(R.id.recycler_part_times)

    }

}