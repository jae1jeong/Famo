package com.softsquared.template.kotlin.src.main.adapter

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.softsquared.template.kotlin.R
import com.softsquared.template.kotlin.src.main.models.MainScheduleCategory
import com.softsquared.template.kotlin.src.main.schedulefind.models.CategoryScheduleInquiryData
import com.softsquared.template.kotlin.src.main.schedulefind.models.ScheduleCategoryData
import org.w3c.dom.Text

class MainCategoryAdapter(private var categoryList:ArrayList<MainScheduleCategory>, private val context: Context, private val clickListener:(MainScheduleCategory)->Unit):RecyclerView.Adapter<MainCategoryAdapter.MainCategoryViewHolder>() {
    private var selectedCategoryView:TextView? = null


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
        val scheduleCategory = categoryList[position]
        holder.categoryText.text = scheduleCategory.text
        holder.itemView.setOnClickListener {
            clickListener(scheduleCategory)
            // 처음 선택할때
            if(selectedCategoryView == null){
                val shape = GradientDrawable()
                shape.cornerRadius = 180F
                shape.setColor(Color.parseColor(categoryList[position].color))
                holder.itemView.background = shape
                holder.categoryText.setTextColor(Color.WHITE)
            }else{
                // 이전에 선택한 다른 값이 있을때
                selectedCategoryView!!.setBackgroundDrawable(context.resources.getDrawable(R.drawable.background_main_category))
                selectedCategoryView!!.setTextColor(Color.parseColor("#aeb7c4"))
                val shape = GradientDrawable()
                shape.cornerRadius = 180F
                shape.setColor(Color.parseColor(categoryList[position].color))
                (holder.itemView as TextView).setTextColor(Color.WHITE)
                holder.itemView.background = shape
            }
            selectedCategoryView = holder.itemView as TextView

        }
    }

    override fun getItemCount(): Int = categoryList.size

    fun setNewCategoryList(newCategoryList:ArrayList<MainScheduleCategory>){
        this.categoryList = newCategoryList
        notifyDataSetChanged()
    }

}