package com.softsquared.template.kotlin.src.main.schedulefind.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.softsquared.template.kotlin.R
import com.softsquared.template.kotlin.src.main.schedulefind.models.ScheduleBookmarkData

open class ScheduleBookmarkAdapter(bookmarkList:ArrayList<ScheduleBookmarkData>):
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val VIEW_TYPE_ITEM = 0
    val VIEW_TYPE_LOADING = 1

    var bookmarkList: ArrayList<ScheduleBookmarkData>? = null

    init {
        this.bookmarkList = bookmarkList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return if (viewType == VIEW_TYPE_ITEM) {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_schedule_find_bookmark_item, parent, false)
            ScheduleBookmarkHolder(view)
        } else {
            val view =
                LayoutInflater.from(parent.context).inflate(R.layout.item_loading, parent, false)
            LoadingViewHolder(view)
        }


//        val view = LayoutInflater.from(parent.context)
//            .inflate(R.layout.recyclerview_schedule_find_bookmark_item,parent,false)
//        view.setOnClickListener {
//
//        }
//        return ScheduleBookmarkHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
//        holder.title.text = bookmarkList[position].title
//        holder.times.text = bookmarkList[position].times
        if (holder is ScheduleBookmarkHolder) {
            populateItemRows(holder as ScheduleBookmarkHolder, position)
        } else if (holder is LoadingViewHolder) {
            showLoadingView(holder as LoadingViewHolder, position)
        }
    }

    override fun getItemCount(): Int = bookmarkList!!.size

    open fun showLoadingView(holder: LoadingViewHolder, position: Int) {}

    open fun populateItemRows(holder: ScheduleBookmarkHolder, position: Int) {
        val item = bookmarkList?.get(position)
        holder.title.text = bookmarkList?.get(position)!!.title
        holder.times.text = bookmarkList?.get(position)!!.times
    }

    class ScheduleBookmarkHolder(itemView : View): RecyclerView.ViewHolder(itemView){

        val title = itemView.findViewById<TextView>(R.id.recycler_part_title)
        val times = itemView.findViewById<TextView>(R.id.recycler_part_times)

    }

    class LoadingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val progressBar: ProgressBar

        init {
            progressBar = itemView.findViewById(R.id.progressBar)
        }
    }

}