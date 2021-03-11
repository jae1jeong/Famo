package com.softsquared.template.kotlin.src.main.schedulefind.adapter

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.softsquared.template.kotlin.R
import com.softsquared.template.kotlin.src.main.schedulefind.models.ScheduleCategoryData

class ScheduleCategoryAdapter(
    var categoryList: ArrayList<ScheduleCategoryData>,
    myScheduleCategoryRecyclerView: IScheduleCategoryRecyclerView
) :
    RecyclerView.Adapter<ScheduleCategoryAdapter.ScheduleCategoryHolder>() {

    private var iScheduleCategoryRecyclerView: IScheduleCategoryRecyclerView? = null

    init {
        Log.d("TAG", "SearchHistoryRecyclerViewAdapter: init() called ")
        this.iScheduleCategoryRecyclerView = myScheduleCategoryRecyclerView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScheduleCategoryHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recyclerview_schedule_find_category_item, parent, false)
        view.setOnClickListener {

        }
        return ScheduleCategoryHolder(view,iScheduleCategoryRecyclerView!!).apply {
            itemView.setOnClickListener {
            }
        }
    }

    override fun onBindViewHolder(holder: ScheduleCategoryHolder, position: Int) {
//        holder.text.text = categoryList[position].text
        holder.text.text = categoryList[position].text
//        holder.color.setImageResource(categoryList[position])
//        holder.color.setImageResource(categoryList[position].color)
        holder.color.setColorFilter(Color.parseColor("color"))
//        img.setColorFilter(Color.parseColor("#FF0000"))
    }

    override fun getItemCount(): Int = categoryList.size

    fun moveFragment() {

    }

    fun addItem(scheduleCategoryData: ScheduleCategoryData) {
        categoryList.add(scheduleCategoryData)
    }

    class ScheduleCategoryHolder(itemView: View,
        myScheduleCategoryRecyclerView: IScheduleCategoryRecyclerView
    ) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        private var iSearchRecyclerViewInterface : IScheduleCategoryRecyclerView

        val text = itemView.findViewById<TextView>(R.id.recyclerview_category_text)
        val color = itemView.findViewById<ImageView>(R.id.recyclerview_category_color)
//        val button = itemView.findViewById<Button>(R.id.recyclerview_category_text)
//        val category = itemView.findViewById<RelativeLayout>(R.id.item_category_list)

        init {
            //리스너연결
//            category.setOnClickListener(this)
            text.setOnClickListener(this)
            this.iSearchRecyclerViewInterface = myScheduleCategoryRecyclerView
        }

        override fun onClick(view: View?) {
            when (view) {
                text -> {
                    Log.d("로그", "onClick: 카테고리 클릭")
                    this.iSearchRecyclerViewInterface.onItemMoveBtnClicked(adapterPosition)
                }
            }
        }

    }

}