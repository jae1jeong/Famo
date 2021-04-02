package com.softsquared.template.kotlin.src.main.schedulefind.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.softsquared.template.kotlin.R
import com.softsquared.template.kotlin.src.main.schedulefind.models.WholeScheduleLatelyData

class ScheduleLatelyAdapter(var latelyList:ArrayList<WholeScheduleLatelyData>,
                            val clickListener:(WholeScheduleLatelyData)->Unit):
    RecyclerView.Adapter<ScheduleLatelyAdapter.ScheduleLatelyHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScheduleLatelyHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recyclerview_schedule_find_bookmark_item,parent,false)
        view.setOnClickListener {

        }
        return ScheduleLatelyHolder(view)
    }

    override fun onBindViewHolder(holder: ScheduleLatelyHolder, position: Int) {
        holder.scheduleName.text = latelyList[position].scheduleName
        holder.scheduleDate.text = latelyList[position].scheduleDate

        if (latelyList[position].categoryID != null){
            holder.color.setColorFilter(Color.parseColor(latelyList[position].colorInfo))
        }else{
            holder.color.setColorFilter(Color.parseColor("#ced5d9"))
        }

        holder.itemView.setOnClickListener {
            clickListener(latelyList[position])
        }
    }

    override fun getItemCount(): Int = latelyList.size

    class ScheduleLatelyHolder(itemView : View): RecyclerView.ViewHolder(itemView){

        val scheduleName = itemView.findViewById<TextView>(R.id.recycler_part_title)
        val scheduleDate = itemView.findViewById<TextView>(R.id.recycler_part_times)
        val color = itemView.findViewById<ImageView>(R.id.recycler_part_color)

    }

}