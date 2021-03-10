package com.softsquared.template.kotlin.src.main.schedulefind.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.softsquared.template.kotlin.R
import com.softsquared.template.kotlin.src.main.schedulefind.models.ScheduleBookmarkData
import com.softsquared.template.kotlin.src.main.schedulefind.models.ScheduleWholeData

open class ScheduleWholeAdapter(wholeList: ArrayList<ScheduleWholeData>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val VIEW_TYPE_ITEM = 0
    val VIEW_TYPE_LOADING = 1

    var wholeList: ArrayList<ScheduleWholeData>? = null

    init {
        this.wholeList = wholeList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return if (viewType == VIEW_TYPE_ITEM) {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.recyclerview_schedule_find_whole_item, parent, false)
            ScheduleWholeHolder(view)
        } else {
            val view =
                LayoutInflater.from(parent.context).inflate(R.layout.item_loading, parent, false)
            LoadingViewHolder(view)
        }

//        val view = LayoutInflater.from(parent.context)
//            .inflate(R.layout.recyclerview_schedule_find_whole_item, parent, false)
//        view.setOnClickListener {
//
//        }
//        return ScheduleWholeHolder(view)
    }

    open fun showLoadingView(holder: LoadingViewHolder, position: Int) {}

    open fun populateItemRows(holder: ScheduleWholeHolder, position: Int) {
        val item = wholeList?.get(position)
        holder.title.text = wholeList?.get(position)!!.title
        holder.times.text = wholeList?.get(position)!!.times
        holder.cardView.setBackgroundResource(R.drawable.left_stroke);
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        if (holder is ScheduleWholeHolder) {
            populateItemRows(holder, position)
        } else if (holder is LoadingViewHolder) {
            showLoadingView(holder, position)
        }

//        holder.cardView.setBackgroundResource(R.drawable.left_stroke);
//        holder.isBoolean.setImageResource(wholeList[position].isBoolean)
//        holder.title.text = wholeList[position].title
//        holder.times.text = wholeList[position].times
//        holder.content.text = wholeList[position].content

    }

    override fun getItemCount(): Int = wholeList!!.size

    class ScheduleWholeHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val cardView = itemView.findViewById<CardView>(R.id.cardview)
        val isBoolean = itemView.findViewById<ImageView>(R.id.recycler_whole_isboolean)
        val times = itemView.findViewById<TextView>(R.id.recycler_whole_times)
        val title = itemView.findViewById<TextView>(R.id.recycler_whole_title)
        val content = itemView.findViewById<TextView>(R.id.recycler_whole_content)

    }

    class LoadingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val progressBar: ProgressBar

        init {
            progressBar = itemView.findViewById(R.id.progressBar)
        }
    }

}