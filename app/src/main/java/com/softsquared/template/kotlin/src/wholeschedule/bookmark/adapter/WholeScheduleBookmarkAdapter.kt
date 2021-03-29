package com.softsquared.template.kotlin.src.wholeschedule.bookmark.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.softsquared.template.kotlin.R
import com.softsquared.template.kotlin.src.main.schedulefind.models.WholeScheduleBookmarkData

class WholeScheduleBookmarkAdapter(var bookmarkListWhole:ArrayList<WholeScheduleBookmarkData>,
    val clickListener:(WholeScheduleBookmarkData)->Unit):
    RecyclerView.Adapter<WholeScheduleBookmarkAdapter.ScheduleLatelyHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScheduleLatelyHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recyclerview_schedule_find_whole_item,parent,false)
        view.setOnClickListener {

        }
        return ScheduleLatelyHolder(view)
    }

    override fun onBindViewHolder(holder: ScheduleLatelyHolder, position: Int) {
        holder.scheduleName.text = bookmarkListWhole[position].scheduleName
        holder.scheduleDate.text = bookmarkListWhole[position].scheduleDate
        holder.schedulePick.setImageResource(bookmarkListWhole[position].schedulePick)
        holder.scheduleMemo.text = bookmarkListWhole[position].scheduleMemo

        if (bookmarkListWhole[position].colorInfo != null){
            holder.color.setColorFilter(Color.parseColor(bookmarkListWhole[position].colorInfo))
        }else{
            holder.color.setColorFilter(Color.parseColor("#ced5d9"))
        }

        holder.itemView.setOnClickListener {
            clickListener(bookmarkListWhole[position])
        }


    }

    override fun getItemCount(): Int = bookmarkListWhole.size

    class ScheduleLatelyHolder(itemView : View): RecyclerView.ViewHolder(itemView){

        val scheduleName = itemView.findViewById<TextView>(R.id.recycler_whole_title)
        val scheduleDate = itemView.findViewById<TextView>(R.id.recycler_whole_times)
        val schedulePick = itemView.findViewById<ImageView>(R.id.recycler_whole_isboolean)
        val scheduleMemo = itemView.findViewById<TextView>(R.id.recycler_whole_content)
        val color = itemView.findViewById<ImageView>(R.id.wholoe_schedule_border)

//        val date = itemView.findViewById<TextView>(R.id.recycler_whole_times)
//        val name = itemView.findViewById<TextView>(R.id.recycler_whole_title)

//        val border = itemView.findViewById<ImageView>(R.id.wholoe_schedule_border)

    }

}