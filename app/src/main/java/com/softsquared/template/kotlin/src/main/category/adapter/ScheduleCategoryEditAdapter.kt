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
import com.softsquared.template.kotlin.src.main.schedulefind.models.ScheduleCategoryData

class ScheduleCategoryEditAdapter(var categoryEditList: ArrayList<ScheduleCategoryData>) :
    RecyclerView.Adapter<ScheduleCategoryEditAdapter.ScheduleCategoryEditHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScheduleCategoryEditHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recyclerview_category_edit_item, parent, false)

        view.setOnClickListener {

        }

        return ScheduleCategoryEditHolder(view).apply {
            itemView.setOnClickListener {
                val curPos: Int = adapterPosition

                Log.d("로그", "test: delete 클릭 확인")
                removeItem(curPos)
                notifyDataSetChanged()
            }
        }
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

    class ScheduleCategoryEditHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val text = itemView.findViewById<EditText>(R.id.recyclerview_category_edit_et_content)
        val delete = itemView.findViewById<ImageView>(R.id.category_edit_btn_delete)



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