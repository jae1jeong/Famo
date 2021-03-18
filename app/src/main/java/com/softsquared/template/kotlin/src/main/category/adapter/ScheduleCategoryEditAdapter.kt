package com.softsquared.template.kotlin.src.main.category.adapter

import android.content.ContentValues.TAG
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.softsquared.template.kotlin.R
import com.softsquared.template.kotlin.config.BaseResponse
import com.softsquared.template.kotlin.src.main.category.*
import com.softsquared.template.kotlin.src.main.category.models.CategoryInsertResponse
import com.softsquared.template.kotlin.src.main.schedulefind.CategoryInquiryView
import com.softsquared.template.kotlin.src.main.schedulefind.models.CategoryInquiryResponse
import com.softsquared.template.kotlin.src.main.schedulefind.models.UserCategoryInquiryResponse
import com.softsquared.template.kotlin.src.main.schedulefind.models.ScheduleCategoryData

class ScheduleCategoryEditAdapter(
    var categoryEditList: ArrayList<ScheduleCategoryData>,
    categoryRecyclerView: ICategoryRecyclerView
) :
    RecyclerView.Adapter<ScheduleCategoryEditAdapter.ScheduleCategoryEditHolder>(),
    CategoryEditView, CategoryInquiryView {

    private var iCategoryRecyclerView: ICategoryRecyclerView? = null

    val categoryID = 0

    init {
        Log.d(TAG, "SearchHistoryRecyclerViewAdapter - init() called")
        this.iCategoryRecyclerView = categoryRecyclerView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScheduleCategoryEditHolder {

        val scheduleCategoryEditHolder = ScheduleCategoryEditHolder(
            LayoutInflater
                .from(parent.context)
                .inflate(R.layout.recyclerview_category_edit_item, parent, false),
            this.iCategoryRecyclerView!!
        )

        return scheduleCategoryEditHolder

        //        val view = LayoutInflater.from(parent.context)
//            .inflate(R.layout.recyclerview_category_edit_item, parent, false)
//
//        return ScheduleCategoryEditHolder(view)
    }

    override fun onBindViewHolder(holder: ScheduleCategoryEditHolder, position: Int) {
        holder.text.text = categoryEditList[position].text
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

    inner class ScheduleCategoryEditHolder(
        itemView: View,
        categoryRecyclerView: ICategoryRecyclerView
    ) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {

        //        val text = itemView.findViewById<EditText>(R.id.recyclerview_category_edit_et_content)
        val text = itemView.findViewById<TextView>(R.id.recyclerview_category_edit_tv_content)
        val delete = itemView.findViewById<ImageView>(R.id.category_edit_btn_delete)
        val color = itemView.findViewById<ImageView>(R.id.category_edit_btn_color)
        private lateinit var mCategoryRecyclerView: ICategoryRecyclerView

        init {
            //리스너연결
            delete.setOnClickListener(this)
            color.setOnClickListener(this)
            text.setOnClickListener(this)
            mCategoryRecyclerView = categoryRecyclerView
        }

        override fun onClick(view: View?) {

            Log.d("로그", "view: ")
            when (view) {
                delete -> {
                    Log.d("로그", "deleteSearchBtn: 검색 삭제 버튼 클릭")
                    Log.d("로그", "adapterPosition: $adapterPosition")

//                    val categoryID = iCategoryRecyclerView!!.onCategoryID()
//                    Log.d("로그", "categoryID어탭터: $categoryID")

//                    iCategoryRecyclerView!!.onItemDeleteBtnClicked(adapterPosition)
//                    CategoryEditService(this).

//                    CategoryEditService(this@ScheduleCategoryEditAdapter)
//                        .tryDeleteCategoryEditDelete(categoryID)
//                    this.mySearchRecyclerViewInterface.onSearchItemDeleteBtnClicked(adapterPosition)

//                    CategoryInquiryService(this@ScheduleCategoryEditAdapter).tryGetCategoryInquiry()


//                    Log.d(TAG, "onClick: ")

                    val categoryID = categoryEditList[adapterPosition].id.toString()
//                    if (adapterPosition == 0) {
//                        categoryID =
////                            iCategoryRecyclerView!!.onCategoryID2().toString()
//                            Log.d(TAG, "0일때 삭제id값: ${categoryEditList[adapterPosition].id}").toString()
//                    } else {
//                        categoryID =
////                            iCategoryRecyclerView!!.onCategoryID(categoryEditList[adapterPosition].id)
////                                .toString()
//                            Log.d(TAG, "삭제id값: ${categoryEditList[adapterPosition].id}").toString()
//                    }
                    CategoryEditService(this@ScheduleCategoryEditAdapter)
                        .tryDeleteCategoryEditDelete(categoryID)
                    removeItem(adapterPosition)
                    notifyDataSetChanged()
                }
                color, text -> {
                    Log.d("로그", "색상변경 버튼 클릭")
                    Log.d("로그", "위치값 :$adapterPosition")
                    val categoryAddBottomDialogFragment = CategoryEditBottomDialogFragment()
//                    val bunble = Bundle()
//                    bunble.putString("color", wholeColor)
//                    bunble.putInt("size", size!!)
//                                (activity as MainActivity).replaceFragment(ScheduleFindFragment.newInstance());

//                    categoryAddBottomDialogFragment.arguments = bunble

                    iCategoryRecyclerView!!.onMoveFragment(
                        categoryEditList[adapterPosition].id,
                        categoryEditList[adapterPosition].text
                    )
                    Log.d(TAG, "클릭:${categoryEditList[adapterPosition].id} ")
                    Log.d(TAG, "클릭:${categoryEditList[adapterPosition].text} ")
//
//                    val getColor = ApplicationClass.sSharedPreferences.getString("color", "#FFA2BE")
//
                    color.setColorFilter(Color.parseColor("getColor"))


                }
            }
        }


    }

    override fun onPostCategoryInsertSuccess(response: CategoryInsertResponse) {

    }

    override fun onPostCategoryInsertFail(message: String) {
    }

    override fun onDeleteCategoryDeleteSuccess(response: BaseResponse) {

        when (response.code) {
            100 -> {
                Log.d("TAG", "onDeleteCategoryDeleteSuccess: 카테고리삭제 테스트")
//                CategoryInquiryService(this).tryGetCategoryInquiry()

            }
            else -> {
                Log.d(TAG, "onDeleteCategoryDeleteSuccess: ${response.message.toString()}")
            }
        }

    }

    override fun onDeleteCategoryDeleteFail(message: String) {
    }


    override fun onPatchCategoryUpdateSuccess(response: BaseResponse) {
    }

    override fun onPatchCategoryUpdateFail(message: String) {
    }

    override fun onGetUserCategoryInquirySuccess(responseUser: UserCategoryInquiryResponse) {
        when (responseUser.code) {
            100 -> {
                Log.d("TAG", "onGetCategoryInquirySuccess: 카테고리 조회 성공")
//                iCategoryRecyclerView!!.onMoveDeleteUpdate()
            }
            else -> {
                Log.d(TAG, "onDeleteCategoryDeleteSuccess: ${responseUser.message.toString()}")
            }
        }


    }

    override fun onGetUserCategoryInquiryFail(message: String) {
    }

    override fun onGetCategoryInquirySuccess(categoryInquiryResponse: CategoryInquiryResponse) {
    }

    override fun onGetCategoryInquiryFail(message: String) {
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