package com.softsquared.template.kotlin.src.main.category.adapter

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.softsquared.template.kotlin.R
import com.softsquared.template.kotlin.config.BaseResponse
import com.softsquared.template.kotlin.src.main.category.CategoryEditService
import com.softsquared.template.kotlin.src.main.category.CategoryEditView
import com.softsquared.template.kotlin.src.main.category.ICategoryRecyclerView
import com.softsquared.template.kotlin.src.main.category.models.CategoryInsertResponse
import com.softsquared.template.kotlin.src.main.schedulefind.models.ScheduleCategoryData

class ScheduleCategoryEditAdapter(var categoryEditList: ArrayList<ScheduleCategoryData>) :
    RecyclerView.Adapter<ScheduleCategoryEditAdapter.ScheduleCategoryEditHolder>(),
    CategoryEditView{

    private var iCategoryRecyclerView: ICategoryRecyclerView? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScheduleCategoryEditHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recyclerview_category_edit_item,parent,false)

        return ScheduleCategoryEditHolder(view)
    }

    override fun onBindViewHolder(holder: ScheduleCategoryEditHolder, position: Int) {
        holder.text.setText(categoryEditList[position].text)
        holder.color.setColorFilter(Color.parseColor(categoryEditList[position].color))
//        holder.color.setColorFilter(categoryEditList[position].color)
//        holder.text.addTextChangedListener()
    }

    override fun getItemCount(): Int = categoryEditList.size

    fun addItem(scheduleCategoryData: ScheduleCategoryData) {
        categoryEditList.add(scheduleCategoryData)
    }

    fun removeItem(position: Int) {
        categoryEditList.removeAt(position)
    }

    inner class ScheduleCategoryEditHolder(itemView: View)
        : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        val text = itemView.findViewById<EditText>(R.id.recyclerview_category_edit_et_content)
        val delete = itemView.findViewById<ImageView>(R.id.category_edit_btn_delete)
        val color = itemView.findViewById<ImageView>(R.id.category_edit_btn_color)

        init {
            //리스너연결
            delete.setOnClickListener(this)
            color.setOnClickListener(this)
        }

        override fun onClick(view: View?) {
            Log.d("로그", "view: ")
            when (view) {
                delete -> {
                    Log.d("로그", "deleteSearchBtn: 검색 삭제 버튼 클릭")
                    Log.d("로그", "adapterPosition: $adapterPosition")
//                    iCategoryRecyclerView!!.onItemDeleteBtnClicked(adapterPosition)
//                    CategoryEditService(this).
//                    removeItem(adapterPosition)
//                    notifyDataSetChanged()
//                    CategoryEditService(this@ScheduleCategoryEditAdapter)
//                        .tryDeleteCategoryEditDelete(1)
//                    this.mySearchRecyclerViewInterface.onSearchItemDeleteBtnClicked(adapterPosition)
                }
                color -> {
                    color.setColorFilter(Color.parseColor("#FF0000"))
                    Log.d("로그", "색상변경 버튼 클릭")
                }
            }
        }


    }

    override fun onPostCategoryInsertSuccess(response: CategoryInsertResponse) {
    }

    override fun onPostCategoryInsertFail(message: String) {
    }

    override fun onDeleteCategoryDeleteSuccess(response: BaseResponse) {
    }

    override fun onDeleteCategoryDeleteFail(message: String) {
    }


    override fun onPatchCategoryUpdateSuccess(response: BaseResponse) {
    }

    override fun onPatchCategoryUpdateFail(message: String) {
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