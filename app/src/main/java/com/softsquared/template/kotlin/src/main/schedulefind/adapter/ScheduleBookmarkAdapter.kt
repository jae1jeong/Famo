package com.softsquared.template.kotlin.src.main.schedulefind.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.softsquared.template.kotlin.R
import com.softsquared.template.kotlin.src.main.schedulefind.models.WholeScheduleBookmarkData

open class ScheduleBookmarkAdapter(var bookmarkListWhole: ArrayList<WholeScheduleBookmarkData>) :
    RecyclerView.Adapter<ScheduleBookmarkAdapter.ScheduleBookmarkHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScheduleBookmarkHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recyclerview_schedule_find_bookmark_item, parent, false)
        view.setOnClickListener {

        }
        return ScheduleBookmarkHolder(view)
    }

    override fun getItemCount(): Int = bookmarkListWhole.size

    override fun onBindViewHolder(holder: ScheduleBookmarkHolder, position: Int) {
        holder.scheduleName.text = bookmarkListWhole[position].scheduleName
        holder.scheduleDate.text = bookmarkListWhole[position].scheduleDate
        holder.color.setColorFilter(Color.parseColor(bookmarkListWhole[position].colorInfo))
    }

    class ScheduleBookmarkHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val scheduleName = itemView.findViewById<TextView>(R.id.recycler_part_title)
        val scheduleDate = itemView.findViewById<TextView>(R.id.recycler_part_times)
        val color = itemView.findViewById<ImageView>(R.id.recycler_part_color)

    }


}