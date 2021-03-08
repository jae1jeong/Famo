package com.softsquared.template.kotlin.src.main.category.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.softsquared.template.kotlin.R
import com.softsquared.template.kotlin.src.main.category.ICategoryRecyclerView
import com.softsquared.template.kotlin.src.main.schedulefind.adapter.ScheduleBookmarkAdapter
import com.softsquared.template.kotlin.src.main.schedulefind.adapter.ScheduleCategoryAdapter
import com.softsquared.template.kotlin.src.main.schedulefind.models.ScheduleCategoryData

class ScheduleCategoryEditAdapter(var categoryEditList: ArrayList<ScheduleCategoryData>) :
    RecyclerView.Adapter<ScheduleCategoryEditAdapter.ScheduleCategoryEditHolder>() {

    private var iCategoryRecyclerView: ICategoryRecyclerView? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScheduleCategoryEditHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recyclerview_category_edit_item,parent,false)

        return ScheduleCategoryEditHolder(view)
    }



    override fun onBindViewHolder(holder: ScheduleCategoryEditHolder, position: Int) {
        holder.text.setText(categoryEditList[position].text)
    }

    override fun getItemCount(): Int = categoryEditList.size

    fun addItem(scheduleCategoryData: ScheduleCategoryData) {
        categoryEditList.add(scheduleCategoryData)
    }

    fun removeItem(position: Int) {
        categoryEditList.removeAt(position)
    }

    inner class ScheduleCategoryEditHolder(
        itemView: View)
        : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        val text = itemView.findViewById<EditText>(R.id.recyclerview_category_edit_et_content)
        val delete = itemView.findViewById<ImageView>(R.id.category_edit_btn_delete)

        init {
            Log.d("ㅇㅇ", "SearchItemViewHolder: ")
            //리스너연결
            delete.setOnClickListener(this)
        }

        override fun onClick(view: View?) {
            Log.d("로그", "view: ")
            when (view) {
                delete -> {
                    Log.d("로그", "deleteSearchBtn: 검색 삭제 버튼 클릭")
                    Log.d("로그", "adapterPosition: $adapterPosition")
//                    iCategoryRecyclerView!!.onItemDeleteBtnClicked(adapterPosition)
                    removeItem(adapterPosition)
                    notifyDataSetChanged()
//                    this.mySearchRecyclerViewInterface.onSearchItemDeleteBtnClicked(adapterPosition)
                }
            }
        }


    }



//    override fun onClick(view: View?) {
//        Log.d(TAG, "view: ")
//        when(view){
//            deleteSearchBtn -> {
//                Log.d(TAG, "deleteSearchBtn: 검색 삭제 버튼 클릭")
//                this.mySearchRecyclerViewInterface.onSearchItemDeleteBtnClicked(adapterPosition)
//            }
//            constraintSearchItem -> {
//                Log.d(TAG, "constraintSearchItem: 검색 아이템 클릭")
//                this.mySearchRecyclerViewInterface.onSearchItemClicked(adapterPosition)
//            }
//        }
//    }


}