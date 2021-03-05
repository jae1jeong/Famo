package com.softsquared.template.kotlin.src.main.schedulefind.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.softsquared.template.kotlin.R
import com.softsquared.template.kotlin.src.main.schedulefind.models.ScheduleWholeData

class ScheduleWholeAdapter(var wholeList:ArrayList<ScheduleWholeData>): RecyclerView.Adapter<ScheduleWholeAdapter.ScheduleWholeHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScheduleWholeHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_schedule_find_whole_item,parent,false)
        view.setOnClickListener {

        }
        return ScheduleWholeHolder(view)
    }

    override fun onBindViewHolder(holder: ScheduleWholeHolder, position: Int) {

        holder.cardView.setBackgroundResource(R.drawable.left_stroke);
        holder.isBoolean.setImageResource(wholeList[position].isBoolean)
        holder.title.text = wholeList[position].title
        holder.times.text = wholeList[position].times
        holder.content.text = wholeList[position].content

    }

    override fun getItemCount(): Int = wholeList.size

    class ScheduleWholeHolder(itemView : View): RecyclerView.ViewHolder(itemView){

        val cardView = itemView.findViewById<CardView>(R.id.cardview)
        val isBoolean = itemView.findViewById<ImageView>(R.id.recycler_whole_isboolean)
        val times = itemView.findViewById<TextView>(R.id.recycler_whole_times)
        val title = itemView.findViewById<TextView>(R.id.recycler_whole_title)
        val content = itemView.findViewById<TextView>(R.id.recycler_whole_content)


    }

}