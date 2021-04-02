package com.softsquared.template.kotlin.src.main.schedulefind.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.softsquared.template.kotlin.R
import com.softsquared.template.kotlin.src.main.models.MainScheduleCategory
import com.softsquared.template.kotlin.src.main.schedulefind.CategoryInquiryView
import com.softsquared.template.kotlin.src.main.schedulefind.models.CategoryInquiryResponse
import com.softsquared.template.kotlin.src.main.schedulefind.models.UserCategoryInquiryResponse
import kotlin.collections.ArrayList

class ScheduleCategoryAdapter(var categoryList: ArrayList<MainScheduleCategory>,
    myScheduleCategoryRecyclerView: IScheduleCategoryRecyclerView,val context:Context) :
    RecyclerView.Adapter<ScheduleCategoryAdapter.ScheduleCategoryHolder>(), CategoryInquiryView {

    private var iScheduleCategoryRecyclerView: IScheduleCategoryRecyclerView? = null

    var mPreviousIndex = -1

    private var selectedCategoryView: TextView? = null
    var boolean = false
    var cnt = 1

    init {
        this.iScheduleCategoryRecyclerView = myScheduleCategoryRecyclerView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScheduleCategoryHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_main_category, parent, false)

        view.setOnClickListener {

        }
        return ScheduleCategoryHolder(view).apply {
            itemView.setOnClickListener {

            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onBindViewHolder(holder: ScheduleCategoryHolder, position: Int) {

        holder.categoryText.text = categoryList[position].text
        val scheduleCategory = categoryList[position]

        holder.itemView.setOnClickListener {
            // 처음 선택할때
            if(selectedCategoryView == null){
                val shape = GradientDrawable()
                shape.cornerRadius = 180F
                shape.setColor(Color.parseColor(categoryList[position].color))
                holder.itemView.background = shape
                holder.categoryText.setTextColor(Color.WHITE)
                selectedCategoryView = holder.itemView as TextView
                scheduleCategory.selected = true
                iScheduleCategoryRecyclerView!!.onItemMoveBtnClicked(scheduleCategory.id)

            }else{
                // 전에 선택했던 카테고리와 현재 누르려는 카테고리가 경우
                if(selectedCategoryView == holder.itemView as TextView){
                    Log.d("TAG", "onBindViewHolder: 같은 카테고리 선택")
                    iScheduleCategoryRecyclerView!!.onClickedTwice()
                    holder.itemView.setBackgroundDrawable(context.resources.getDrawable(R.drawable.background_main_category))
                    holder.itemView.setTextColor(Color.parseColor("#aeb7c4"))
                    selectedCategoryView = null
                    scheduleCategory.selected = false
                }else{
                    // 다를 경우
                    selectedCategoryView!!.setBackgroundDrawable(context.resources.getDrawable(R.drawable.background_main_category))
                    selectedCategoryView!!.setTextColor(Color.parseColor("#aeb7c4"))
                    categoryList.forEach {
                        if(it.selected){
                            it.selected = false
                        }
                    }
                    val shape = GradientDrawable()
                    shape.cornerRadius = 180F
                    shape.setColor(Color.parseColor(categoryList[position].color))
                    (holder.itemView as TextView).setTextColor(Color.WHITE)
                    holder.itemView.background = shape
                    selectedCategoryView = holder.itemView
                    scheduleCategory.selected = true
                    iScheduleCategoryRecyclerView!!.onItemMoveBtnClicked(scheduleCategory.id)

                }
            }
        }

    }

    override fun getItemCount(): Int = categoryList.size

    fun addItem(scheduleCategoryData: MainScheduleCategory) {
        categoryList.add(scheduleCategoryData)
    }

    inner class ScheduleCategoryHolder(
        itemView: View
    ) : RecyclerView.ViewHolder(itemView) {

        val categoryText = itemView.findViewById<TextView>(R.id.main_category_text)




    }

    override fun onGetUserCategoryInquirySuccess(responseUser: UserCategoryInquiryResponse) {

    }

    override fun onGetUserCategoryInquiryFail(message: String) {
    }

    override fun onGetCategoryInquirySuccess(categoryInquiryResponse: CategoryInquiryResponse) {
    }

    override fun onGetCategoryInquiryFail(message: String) {
    }

}