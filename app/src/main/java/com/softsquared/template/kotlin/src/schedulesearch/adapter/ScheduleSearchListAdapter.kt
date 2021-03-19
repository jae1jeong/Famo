package com.softsquared.template.kotlin.src.schedulesearch.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.softsquared.template.kotlin.R
import com.softsquared.template.kotlin.src.schedulesearch.models.SearchListData

class ScheduleSearchListAdapter(var searchList: ArrayList<SearchListData>) :
    RecyclerView.Adapter<ScheduleSearchListAdapter.SearchListHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchListHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recyclerview_schedule_search_list_item, parent, false)
        view.setOnClickListener {

        }
        return SearchListHolder(view)
    }

    override fun onBindViewHolder(holder: SearchListHolder, position: Int) {

        holder.text.text = searchList[position].text

    }

    override fun getItemCount(): Int = searchList.size

    class SearchListHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        val text = itemView.findViewById<TextView>(R.id.item_search_content)

    }

}