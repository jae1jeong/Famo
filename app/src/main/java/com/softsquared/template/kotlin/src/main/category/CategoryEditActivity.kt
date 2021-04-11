package com.softsquared.template.kotlin.src.main.category

import android.content.ContentValues
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.softsquared.template.kotlin.R
import com.softsquared.template.kotlin.config.ApplicationClass
import com.softsquared.template.kotlin.config.BaseActivity
import com.softsquared.template.kotlin.config.BaseResponse
import com.softsquared.template.kotlin.databinding.ActivityCategoryEditBinding
import com.softsquared.template.kotlin.src.main.MainActivity
import com.softsquared.template.kotlin.src.main.category.adapter.ScheduleCategoryEditAdapter
import com.softsquared.template.kotlin.src.main.category.models.CategoryInsertRequest
import com.softsquared.template.kotlin.src.main.category.models.CategoryInsertResponse
import com.softsquared.template.kotlin.src.main.schedulefind.CategoryInquiryService
import com.softsquared.template.kotlin.src.main.schedulefind.CategoryInquiryView
import com.softsquared.template.kotlin.src.main.schedulefind.SchedulefindFilterBottomDialogFragment
import com.softsquared.template.kotlin.src.main.schedulefind.adapter.IScheduleCategoryRecyclerView
import com.softsquared.template.kotlin.src.main.schedulefind.adapter.ScheduleCategoryAdapter
import com.softsquared.template.kotlin.src.main.schedulefind.models.CategoryInquiryResponse
import com.softsquared.template.kotlin.src.main.schedulefind.models.ScheduleCategoryData
import com.softsquared.template.kotlin.src.main.schedulefind.models.UserCategoryInquiryResponse
import com.softsquared.template.kotlin.src.main.today.TodayFragment
import com.softsquared.template.kotlin.src.main.today.TodayService
import com.softsquared.template.kotlin.src.mypageedit.logout.LogoutDialog
import com.softsquared.template.kotlin.util.AskDialog
import com.softsquared.template.kotlin.util.Constants

class CategoryEditActivity() : BaseActivity<ActivityCategoryEditBinding>
    (ActivityCategoryEditBinding::inflate), ICategoryRecyclerView, CategoryEditView,
    CategoryInquiryView{

    private var deleteDialog: DeleteDialog? = null

    var wholeName: String? = null
    var wholeColor: String? = null

    var color: List<String>? = null
    var name: List<String>? = null
    var tempCategoryID: List<String>? = null

    var size: Int? = null
    var getCategoryID: String? = null

    val intCategoryID = ArrayList<Int>()

    var dataSize = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        CategoryInquiryService(this).tryGetUserCategoryInquiry()

        wholeName = intent.getStringExtra("name")
        wholeColor = intent.getStringExtra("color")
        size = intent.getIntExtra("size", 0)
        getCategoryID = intent.getStringExtra("categoryID")
        Log.d("TAG", "CategoryEditActivity : name : $wholeName")
        Log.d("TAG", "CategoryEditActivity : wholeColor : $wholeColor")
        Log.d("TAG", "CategoryEditActivity : size : $size")
        Log.d("TAG", "CategoryEditActivity : categoryID : $getCategoryID")

        if (wholeName != null && wholeColor != null && getCategoryID != null) {
            name = wholeName!!.split(":")
            color = wholeColor!!.split(":")
            tempCategoryID = getCategoryID!!.split(":")
        }

        if (tempCategoryID != null) {
            for (i in 0 until size!!) {
                intCategoryID.add(tempCategoryID?.get(i)!!.toInt())
                Log.d("TAG", "intCategoryID: $intCategoryID")
            }
        }

        //카테고리 추가
        binding.categoryEditBtnPlus.setOnClickListener {

            val categoryAddBottomDialogFragment = CategoryAddBottomDialogFragment()
            val bundle = Bundle()
            bundle.putString("name", wholeName)
            bundle.putString("color", wholeColor)
            bundle.putInt("size", size!!)
            categoryAddBottomDialogFragment.arguments = bundle
            categoryAddBottomDialogFragment.show(
                supportFragmentManager,
                categoryAddBottomDialogFragment.tag
            )

        }

        //X 버튼
        binding.categoryEditXBtn.setOnClickListener {
            finish()
        }

    }

    //삭제다이얼로그
    fun deleteDialog() {
        val dialog = DeleteDialog(this)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()
    }

    //삭제불가 다이얼로그
    fun deleteImpossibleDialog() {
        val dialog = DeleteImpossibleDialog(this)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()
    }

    //삭제버튼 클릭
    override fun onItemDeleteBtnClicked(categoryID: Int) {
//        deleteDialog =
//            DeleteDialog(this)
//        deleteDialog!!.setOnDialogButtonClickListener(this)
//        deleteDialog!!.setCancelable(false)
//        deleteDialog!!.show()

        CategoryInquiryService(this).tryGetCategoryInquiry(
            categoryID, 0, 10
        )

        AskDialog(this)
            .setTitle("일정삭제")
            .setMessage("일정을 삭제하시겠습니까?")
            .setPositiveButton("삭제"){
                showLoadingDialog(this)
                if (dataSize == 0) {
                    Log.d("TAG", "onItemDeleteBtnClicked: 삭제성공")
//                    val edit = ApplicationClass.sSharedPreferences.edit()
//                    edit.putString(Constants.CATEGORY_DELETE_CHECK,"check")
//                    edit.apply()
                    CategoryEditService(this)
                            .tryDeleteCategoryEditDelete(categoryID.toString())
                    dismissLoadingDialog()
                }else{
                    deleteImpossibleDialog()
                    dismissLoadingDialog()
                }

            }
            .setNegativeButton("취소"){
            }.show()

    }

    override fun onBackPressed() {
        finish()
    }

    //삭제불가
    override fun onDeleteImpossible() {
        deleteImpossibleDialog()
    }

    override fun onCategoryID(categoryID: Int): Int {
//        val categoryID = tempCategoryID?.get(1)!!.substring(1, tempCategoryID!![1].length)
//        val categoryID = tempCategoryID
//        Log.d("TAG", "onCategoryID함수: $categoryID")
        return categoryID
    }

    override fun onCategoryID2(): Int {
//        val categoryID = tempCategoryID?.get(1)!!.substring(1, tempCategoryID!![1].length)
//        val categoryID = tempCategoryID
//        Log.d("TAG", "onCategoryID함수: $categoryID")
        return Integer.parseInt(tempCategoryID!![0])
    }

    override fun onMoveFragment(categoryID: Int, text: String, color : String) {
        val categoryEditBottomDialogFragment = CategoryEditBottomDialogFragment()
        val bunble = Bundle()
        bunble.putString("color", wholeColor)
        bunble.putString("selectColor", color)
        bunble.putString("selectName", text)
        bunble.putString("wholeName",wholeName)
        bunble.putInt("size", size!!)
        bunble.putInt("categoryID", categoryID)
        categoryEditBottomDialogFragment.arguments = bunble
        categoryEditBottomDialogFragment.show(
            supportFragmentManager, categoryEditBottomDialogFragment.tag
        )
    }

    override fun getColor(color: String) {

    }

    override fun onMoveDeleteUpdate() {
        val intent = Intent(this, CategoryEditActivity::class.java)
        intent.putExtra("color", wholeColor)
        intent.putExtra("name", wholeName)
        intent.putExtra("size", size)
        intent.putExtra("categoryID", getCategoryID)
        startActivity(intent)
    }


    companion object {
        fun newInstance(): CategoryEditActivity {    // shs: 함수의 반환 형이 Fragment 형이라...
            return CategoryEditActivity()
        }
    }

    override fun onPostCategoryInsertSuccess(response: CategoryInsertResponse) {

        if (response.isSuccess) {
            when (response.code) {
                100 -> {
                    Log.d("TAG", "onPostCategoryInsertSuccess: 카테고리생성 테스트")
                }
                else -> {
                    showCustomToast(response.message.toString())
                }
            }
        } else {
            showCustomToast(response.message.toString())
        }

    }

    override fun onPostCategoryInsertFail(message: String) {
    }

    override fun onDeleteCategoryDeleteSuccess(response: BaseResponse) {

        CategoryInquiryService(this).tryGetUserCategoryInquiry()
    }

    override fun onDeleteCategoryDeleteFail(message: String) {
    }

    override fun onPatchCategoryUpdateSuccess(response: BaseResponse) {

    }

    override fun onPatchCategoryUpdateFail(message: String) {
    }

    //유저별 카테고리 조회
    override fun onGetUserCategoryInquirySuccess(responseUser: UserCategoryInquiryResponse) {

        when (responseUser.code) {
            100 -> {
                Log.d("TAG", "onGetCategoryInquirySuccess: 유저카테고리조회성공")
                val categoryList: ArrayList<ScheduleCategoryData> = arrayListOf()

                for (i in 0 until responseUser.data.size) {
                    categoryList.add(
                        ScheduleCategoryData(
                            responseUser.data[i].categoryID,
                            responseUser.data[i].categoryName,
                            responseUser.data[i].colorInfo
                        )
                    )
                }

                binding.recyclerviewEditCategory.layoutManager = LinearLayoutManager(
                    this, LinearLayoutManager.VERTICAL, false
                )
                binding.recyclerviewEditCategory.setHasFixedSize(true)
                binding.recyclerviewEditCategory.adapter = ScheduleCategoryEditAdapter(categoryList,this)
//                scheduleCategoryAdapter.notifyDataSetChanged()

            }
            else -> {
                showCustomToast("실패 메시지 : ${responseUser.message}")
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




}