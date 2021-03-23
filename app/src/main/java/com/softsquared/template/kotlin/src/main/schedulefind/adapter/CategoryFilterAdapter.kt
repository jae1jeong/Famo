package com.softsquared.template.kotlin.src.main.schedulefind.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.softsquared.template.kotlin.R
import com.softsquared.template.kotlin.src.main.schedulefind.ScheduleFindService
import com.softsquared.template.kotlin.src.main.schedulefind.models.*

class CategoryFilterAdapter(var categoryFilterList: ArrayList<CategoryFilterData>
) : RecyclerView.Adapter<CategoryFilterAdapter.ScheduleWholeHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScheduleWholeHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recyclerview_schedule_find_whole_item, parent, false)

        return ScheduleWholeHolder(view)

    }

    override fun onBindViewHolder(holder: ScheduleWholeHolder, position: Int) {

        holder.date.text = categoryFilterList[position].scheduleDate
        holder.pick.setImageResource(categoryFilterList[position].schedulePick)
        holder.name.text = categoryFilterList[position].scheduleName
        holder.memo.text = categoryFilterList[position].scheduleMemo
        holder.border.setColorFilter(Color.parseColor(categoryFilterList[position].colorInfo))

    }

    override fun getItemCount(): Int = categoryFilterList.size

    inner class ScheduleWholeHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        val pick = itemView.findViewById<ImageView>(R.id.recycler_whole_isboolean)
        val date = itemView.findViewById<TextView>(R.id.recycler_whole_times)
        val name = itemView.findViewById<TextView>(R.id.recycler_whole_title)
        val memo = itemView.findViewById<TextView>(R.id.recycler_whole_content)
        val border = itemView.findViewById<ImageView>(R.id.wholoe_schedule_border)

    }


}