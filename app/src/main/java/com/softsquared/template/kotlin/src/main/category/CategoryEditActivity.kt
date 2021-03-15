package com.softsquared.template.kotlin.src.main.category

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.softsquared.template.kotlin.config.BaseActivity
import com.softsquared.template.kotlin.config.BaseResponse
import com.softsquared.template.kotlin.databinding.ActivityCategoryEditBinding
import com.softsquared.template.kotlin.src.main.MainActivity
import com.softsquared.template.kotlin.src.main.category.adapter.ScheduleCategoryEditAdapter
import com.softsquared.template.kotlin.src.main.category.models.CategoryInsertRequest
import com.softsquared.template.kotlin.src.main.category.models.CategoryInsertResponse
import com.softsquared.template.kotlin.src.main.schedulefind.models.ScheduleCategoryData

class CategoryEditActivity() : BaseActivity<ActivityCategoryEditBinding>
    (ActivityCategoryEditBinding::inflate), ICategoryRecyclerView, CategoryEditView {

    //    private var categoryEditList = ArrayList<ScheduleCategoryData>()
    val categoryEditList: ArrayList<ScheduleCategoryData> = arrayListOf()
    private lateinit var categoryEditAdapter: ScheduleCategoryEditAdapter

    var wholeName: String? = null
    var wholeColor: String? = null

    var color: List<String>? = null
    var name: List<String>? = null
    var tempCategoryID : List<String>? = null


    var size: Int? = null
    var getCategoryID: String? = null

    val intCategoryID = ArrayList<Int>()

//    var tempCategoryID : List<String>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        wholeName = intent.getStringExtra("name")
        wholeColor = intent.getStringExtra("color")
        size = intent.getIntExtra("size", 0)
        getCategoryID = intent.getStringExtra("categoryID")
        Log.d("TAG", "CategoryEditActivity : name : $wholeName")
        Log.d("TAG", "CategoryEditActivity : wholeColor : $wholeColor")
        Log.d("TAG", "CategoryEditActivity : size : $size")
        Log.d("TAG", "CategoryEditActivity : categoryID : $getCategoryID")

//        val a = "생성된 카테고리번호 : 20"
//        val b = a.split(":".toRegex()).toTypedArray()
//        println(b[0])
//        println(b[1])
//        val c = b[1].substring(1, b[1].length)
//        println(c)

        if (wholeName != null && wholeColor != null && getCategoryID != null){
            name = wholeName!!.split(":")
            color = wholeColor!!.split(":")
            tempCategoryID = getCategoryID!!.split(":")


        }

        Log.d("TAG", "tempCategoryID: ${tempCategoryID?.get(0)}")
        Log.d("TAG", "tempCategoryID: ${tempCategoryID?.get(1)}")

        if (tempCategoryID != null){

            for (i in 0 until size!!){
                intCategoryID.add(tempCategoryID?.get(i)!!.toInt())
                Log.d("TAG", "intCategoryID: $intCategoryID")
            }
        }

        //            카테고리 리사이클러뷰
        createCategoryRecyclerview()



//        val categoryID : List<String> = tempCategoryID?.get(1)!!.substring(1, tempCategoryID!![1].length)
//        if (getCategoryID != null){
//            tempCategoryID = getCategoryID!!.split(":".toRegex()).toTypedArray()
//            Log.d("TAG", "getCategoryID: $getCategoryID")
//        }
//        val categoryID : List<String> = tempCategoryID?.get(1)!!.substring(1, tempCategoryID!![1].length)

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


//            val token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySUQiOjUsIm1ldGhvZCI6IkYiLCJpYXQiOjE2MTUzODYyMTEsImV4cCI6MTY0NjkyMjIxMSwic3ViIjoidXNlckluZm8ifQ.laZCThzA823-i5-ZTVyfvqq8PMIgHUdAnP97woIHufQ"

//            val categoryInsertRequest = CategoryInsertRequest(
//                categoryName = "",
//                categoryColor = 1
//            )
//            CategoryEditService(this).tryPostCategoryEditInsert(token,categoryInsertRequest)
//            val scheduleCategoryData =  ScheduleCategoryData("")
//            categoryEditAdapter.addItem(scheduleCategoryData)
//            categoryEditAdapter.notifyDataSetChanged()
        }

        //X 버튼
        binding.categoryEditXBtn.setOnClickListener {
//            (activity as MainActivity).replaceFragment(ScheduleFindFragment.newInstance());
//            (activity as MainActivity).onBackPressed()

//            CategoryEditService(this).tryPatchCategoryEditUpdate("")
//            val categoryInsertRequest = CategoryInsertRequest(
//                categoryName = categoryEditAdapter.onBindViewHolder()
//                categoryColor = categoryID
//            )
//            CategoryEditService(this).tryPostCategoryEditInsert(categoryInsertRequest)
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

    }


    fun createCategoryRecyclerview() {

        for (i in 0 until size!!) {

            categoryEditList.add(
                ScheduleCategoryData(intCategoryID[i],name!![i], color!![i])
            )
        }
//        categoryEditList = arrayListOf(
//            ScheduleCategoryData("학교"),
//            ScheduleCategoryData("알바"),
//            ScheduleCategoryData("친구")
//        )

        categoryEditAdapter = ScheduleCategoryEditAdapter(categoryEditList,this)

//        this.photoGridRecyeclerViewAdapter = PhotoGridRecyclerViewAdapter()
//        this.photoGridRecyeclerViewAdapter.submitList(photoList)
//        //                                                                                                              데이터값을 처음부터/끝부분부터 방향
//        my_photo_recycler_view.layoutManager = GridLayoutManager(this,2,
//            GridLayoutManager.VERTICAL,false)
//        my_photo_recycler_view.adapter = this.photoGridRecyeclerViewAdapter

        binding.recyclerviewEditCategory.layoutManager = LinearLayoutManager(
            this, LinearLayoutManager.VERTICAL, false
        )
        binding.recyclerviewEditCategory.setHasFixedSize(true)
        binding.recyclerviewEditCategory.adapter = categoryEditAdapter
    }

    //삭제버튼 클릭
    override fun onItemDeleteBtnClicked(categoryID: Int) {
        Log.d("aa", "onSearchItemDeleteBtnClicked: ")

        //해당 번쨰를 삭제 및 저장
        categoryEditList.removeAt(categoryID)
        //데이터 덮어쓰기
//        SharedPrefManager.storeSearchHisotryList(this.searchHistoryList)
        //데이터 변경알림
        this.categoryEditAdapter.notifyDataSetChanged()

//        CategoryEditService(this).tryDeleteCategoryEditDelete()
    }

    override fun onCategoryID(categoryID: Int) : Int{
//        val categoryID = tempCategoryID?.get(1)!!.substring(1, tempCategoryID!![1].length)
//        val categoryID = tempCategoryID
//        Log.d("TAG", "onCategoryID함수: $categoryID")
        return categoryID
    }

    override fun onMoveFragment(categoryID : Int, text : String) {
        val categoryEditBottomDialogFragment = CategoryEditBottomDialogFragment()
        val bunble = Bundle()
        bunble.putString("color", wholeColor)
        bunble.putString("name", text)
        bunble.putInt("size", size!!)
        bunble.putInt("categoryID",categoryID)
        categoryEditBottomDialogFragment.arguments = bunble
        categoryEditBottomDialogFragment.show(
            supportFragmentManager, categoryEditBottomDialogFragment.tag
        )
    }

    override fun getColor(color: String) {

    }

    override fun onMoveDeleteUpdate() {
        val intent = Intent(this,CategoryEditActivity::class.java)
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
                    showCustomToast("카테고리 생성 테스트")
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
    }

    override fun onDeleteCategoryDeleteFail(message: String) {
    }

    override fun onPatchCategoryUpdateSuccess(response: BaseResponse) {
    }

    override fun onPatchCategoryUpdateFail(message: String) {
    }

}