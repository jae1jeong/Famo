package com.softsquared.template.kotlin.src.main.schedulefind.adapter

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.softsquared.template.kotlin.R
import com.softsquared.template.kotlin.src.main.schedulefind.CategoryInquiryView
import com.softsquared.template.kotlin.src.main.schedulefind.models.CategoryInquiryResponse
import com.softsquared.template.kotlin.src.main.schedulefind.models.UserCategoryInquiryResponse
import com.softsquared.template.kotlin.src.main.schedulefind.models.ScheduleCategoryData
import com.softsquared.template.kotlin.src.mypageedit.MyPageEditService
import com.softsquared.template.kotlin.src.mypageedit.models.PutMyPageUpdateRequest
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*
import kotlin.collections.ArrayList

class ScheduleCategoryAdapter(var categoryList: ArrayList<ScheduleCategoryData>,
    myScheduleCategoryRecyclerView: IScheduleCategoryRecyclerView) :
    RecyclerView.Adapter<ScheduleCategoryAdapter.ScheduleCategoryHolder>(), CategoryInquiryView {

    private var iScheduleCategoryRecyclerView: IScheduleCategoryRecyclerView? = null

    var mPreviousIndex = -1

    private var selectedView: TextView? = null
    var boolean = false
    var cnt = 1

    init {
        Log.d("TAG", "ScheduleCategoryAdapter: init() called ")
        this.iScheduleCategoryRecyclerView = myScheduleCategoryRecyclerView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScheduleCategoryHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recyclerview_schedule_find_category_item, parent, false)

        view.setOnClickListener {

        }
        return ScheduleCategoryHolder(view).apply {
            itemView.setOnClickListener {

            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onBindViewHolder(holder: ScheduleCategoryHolder, position: Int) {

        holder.text.text = categoryList[position].text
        holder.color.setColorFilter(Color.parseColor(categoryList[position].color))

        val colorStrList = iScheduleCategoryRecyclerView!!.onColor()
        Log.d(TAG, "onBindViewHolder: $colorStrList")


        if (mPreviousIndex == position){
            holder.color.setColorFilter(Color.parseColor(colorStrList[position]))
        }else{
            holder.color.setColorFilter(Color.parseColor("#00000000"))
        }

        holder.itemView.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    Log.d(TAG, "onBindViewHolder: 클릭확인")
                    mPreviousIndex = position
                    notifyDataSetChanged()
                    iScheduleCategoryRecyclerView!!.onItemMoveBtnClicked(categoryList[position].id)
                }
            }
            false
        }

//        holder.itemView.setOnClickListener {
//            Log.d(TAG, "onBindViewHolder: 클릭확인")
//            mPreviousIndex = position
//            notifyDataSetChanged()
//            iScheduleCategoryRecyclerView!!.onItemMoveBtnClicked(categoryList[position].id)
//        }

    }

    override fun getItemCount(): Int = categoryList.size

    fun addItem(scheduleCategoryData: ScheduleCategoryData) {
        categoryList.add(scheduleCategoryData)
    }

    inner class ScheduleCategoryHolder(
        itemView: View
    ) : RecyclerView.ViewHolder(itemView) {


        val text = itemView.findViewById<TextView>(R.id.recyclerview_category_text)
        val color = itemView.findViewById<ImageView>(R.id.recyclerview_category_color)
//        val button = itemView.findViewById<Button>(R.id.recyclerview_category_text)
//        val category = itemView.findViewById<RelativeLayout>(R.id.item_category_list)




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