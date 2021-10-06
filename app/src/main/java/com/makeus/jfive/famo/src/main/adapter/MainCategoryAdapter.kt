package com.makeus.jfive.famo.src.main.adapter

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.makeus.jfive.famo.R
import com.makeus.jfive.famo.src.main.models.MainScheduleCategory

class MainCategoryAdapter(private val context: Context, private val clickListener:(MainScheduleCategory)->Unit):RecyclerView.Adapter<MainCategoryAdapter.MainCategoryViewHolder>() {
    private var categoryList:ArrayList<MainScheduleCategory> = arrayListOf()
    private var cacheCategoryPosition:Int = -1
    private var colorList = ColorStateList(
        arrayOf(
            intArrayOf(-android.R.attr.state_checked),
            intArrayOf(android.R.attr.state_checked)
        ),
        intArrayOf(
            Color.parseColor("#bfc5cf"),
            Color.WHITE
        )
    )

    class MainCategoryViewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {
        val categoryText = itemView.findViewById<TextView>(R.id.main_category_text)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainCategoryViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_main_category,parent,false)
        val viewHolder = MainCategoryViewHolder(view)
        return viewHolder
    }


    override fun onBindViewHolder(holder: MainCategoryViewHolder, position: Int) {
        val category = categoryList[position]
        holder.categoryText.text = category.text
        if(category.selected){
            val shape = GradientDrawable()
            shape.cornerRadius = 180F
            shape.setColor(Color.parseColor(categoryList[position].color))
            shape.setStroke(context.resources.getDimension(R.dimen.category_stroke).toInt(),
                colorList
            )
            holder.itemView.background = shape
            holder.categoryText.setTextColor(Color.WHITE)
        }else{
            val shape = GradientDrawable()
            shape.cornerRadius = 180F
            shape.setColor(Color.WHITE)
            shape.setStroke(context.resources.getDimension(R.dimen.category_stroke).toInt(),
                colorList
            )
            holder.itemView.background = shape
            holder.categoryText.setTextColor(Color.parseColor("#aeb7c4"))
        }

        holder.itemView.setOnClickListener {
            clickListener(category)
            if(cacheCategoryPosition == position){
                category.selected = false
                cacheCategoryPosition = -1
                notifyItemChanged(position)
            }else{
                if(cacheCategoryPosition >= 0){
                    categoryList[cacheCategoryPosition].selected = false
                    notifyItemChanged(cacheCategoryPosition)
                }
                category.selected = !category.selected
                notifyItemChanged(position)
                cacheCategoryPosition = position
            }
        }
    }

    override fun getItemCount(): Int = categoryList.size

    fun setNewCategoryList(newCategoryList:ArrayList<MainScheduleCategory>){
        this.categoryList = newCategoryList
        notifyDataSetChanged()
    }

    fun setCategoryAllFalse(){
        this.categoryList.map{
            it.selected = false
        }
        notifyDataSetChanged()
    }


}