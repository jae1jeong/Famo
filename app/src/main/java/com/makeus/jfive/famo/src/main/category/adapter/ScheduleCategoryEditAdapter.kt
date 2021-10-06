package com.makeus.jfive.famo.src.main.category.adapter

import android.content.ContentValues.TAG
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.makeus.jfive.famo.R
import com.makeus.jfive.famo.config.BaseResponse
import com.makeus.jfive.famo.src.main.category.*
import com.makeus.jfive.famo.src.main.category.models.CategoryInsertResponse
import com.makeus.jfive.famo.src.main.schedulefind.CategoryInquiryView
import com.makeus.jfive.famo.src.main.schedulefind.models.CategoryInquiryResponse
import com.makeus.jfive.famo.src.main.schedulefind.models.UserCategoryInquiryResponse
import com.makeus.jfive.famo.src.main.schedulefind.models.ScheduleCategoryData

class ScheduleCategoryEditAdapter(
    var categoryEditList: ArrayList<ScheduleCategoryData>,
    categoryRecyclerView: ICategoryRecyclerView
) :
    RecyclerView.Adapter<ScheduleCategoryEditAdapter.ScheduleCategoryEditHolder>(),
    CategoryEditView, CategoryInquiryView, DeleteDialog.deleteButtonClickListener {

    private var deleteDialog: DeleteDialog? = null

    private var iCategoryRecyclerView: ICategoryRecyclerView? = null

    var categoryID = 0

    var dataSize = 0

    init {
        Log.d(TAG, "ScheduleCategoryEditAdapter - init() called")
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

                    categoryID = categoryEditList[adapterPosition].id

                    iCategoryRecyclerView!!.onItemDeleteBtnClicked(categoryID)

//                    val edit = ApplicationClass.sSharedPreferences.edit()
//                    val check = ApplicationClass.sSharedPreferences.getString(
//                        Constants.CATEGORY_DELETE_CHECK,
//                        null
//                    )
//
//                    if (check == "check") {
//                        Log.d("TAG", "check: 확인")
//                        removeItem(adapterPosition)
//                        notifyDataSetChanged()
//                        edit.remove(Constants.CATEGORY_DELETE_CHECK)
//                        edit.apply()
//                    }else{
//                        Log.d("TAG", "check: 확인X")
//                    }

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
                        categoryEditList[adapterPosition].text,
                        categoryEditList[adapterPosition].color
                    )
                    Log.d(TAG, "클릭:${categoryEditList[adapterPosition].id} ")
                    Log.d(TAG, "클릭:${categoryEditList[adapterPosition].text} ")

//                    color.setColorFilter(Color.parseColor("getColor"))


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

        dataSize = categoryInquiryResponse.data.size


    }

    override fun onGetCategoryInquiryFail(message: String) {
    }

    override fun onDialogButtonClick(view: View?) {

        when (view!!.id) {
            R.id.delete_check -> {
                Log.d(TAG, "onDialogButtonClick: delete확인")
                iCategoryRecyclerView!!.onDeleteImpossible()
//                CategoryInquiryService(this@ScheduleCategoryEditAdapter).tryGetCategoryInquiry()

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