package com.softsquared.template.kotlin.src.main.schedulefind.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.softsquared.template.kotlin.R
import com.softsquared.template.kotlin.src.main.schedulefind.models.ScheduleCategoryData

class ScheduleCategoryAdapter(var categoryList:ArrayList<ScheduleCategoryData>):
    RecyclerView.Adapter<ScheduleCategoryAdapter.ScheduleCategoryHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScheduleCategoryHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recyclerview_schedule_find_category_item,parent,false)
        view.setOnClickListener {

        }
        return ScheduleCategoryHolder(view)
    }

    override fun onBindViewHolder(holder: ScheduleCategoryHolder, position: Int) {
        holder.text.text = categoryList[position].text
    }

    override fun getItemCount(): Int = categoryList.size

    fun addItem(scheduleCategoryData: ScheduleCategoryData) {
        categoryList.add(scheduleCategoryData)
    }

    class ScheduleCategoryHolder(itemView : View): RecyclerView.ViewHolder(itemView){

        val text = itemView.findViewById<TextView>(R.id.recyclerview_category_text)

    }

}