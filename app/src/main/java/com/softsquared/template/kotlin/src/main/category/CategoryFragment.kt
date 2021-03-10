package com.softsquared.template.kotlin.src.main.category

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.softsquared.template.kotlin.R
import com.softsquared.template.kotlin.config.BaseFragment
import com.softsquared.template.kotlin.databinding.FragmentCategoryEditBinding
import com.softsquared.template.kotlin.src.main.MainActivity
import com.softsquared.template.kotlin.src.main.category.adapter.ScheduleCategoryEditAdapter
import com.softsquared.template.kotlin.src.main.schedulefind.ScheduleFindFragment
import com.softsquared.template.kotlin.src.main.schedulefind.adapter.ScheduleCategoryAdapter
import com.softsquared.template.kotlin.src.main.schedulefind.models.ScheduleCategoryData

class CategoryFragment() : BaseFragment<FragmentCategoryEditBinding>(FragmentCategoryEditBinding::bind,
    R.layout.fragment_category_edit), ICategoryRecyclerView {

    private var categoryEditList = ArrayList<ScheduleCategoryData>()
    private lateinit var categoryEditAdapter: ScheduleCategoryEditAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //카테고리 리사이클러뷰
        createCategoryRecyclerview()

        binding.categoryEditBtnPlus.setOnClickListener {
            val scheduleCategoryData =  ScheduleCategoryData("")
            categoryEditAdapter.addItem(scheduleCategoryData)
            categoryEditAdapter.notifyDataSetChanged()
        }

        //X 버튼
        binding.categoryEditXBtn.setOnClickListener {
//            (activity as MainActivity).replaceFragment(ScheduleFindFragment.newInstance());
//            (activity as MainActivity).onBackPressed()
            val intent = Intent(activity,MainActivity::class.java)
            startActivity(intent)
        }

    }

    fun createCategoryRecyclerview() {
        //테스트 데이터
        categoryEditList = arrayListOf(
            ScheduleCategoryData("학교"),
            ScheduleCategoryData("알바"),
            ScheduleCategoryData("친구")
        )

        categoryEditAdapter = ScheduleCategoryEditAdapter(categoryEditList)

//        this.photoGridRecyeclerViewAdapter = PhotoGridRecyclerViewAdapter()
//        this.photoGridRecyeclerViewAdapter.submitList(photoList)
//        //                                                                                                              데이터값을 처음부터/끝부분부터 방향
//        my_photo_recycler_view.layoutManager = GridLayoutManager(this,2,
//            GridLayoutManager.VERTICAL,false)
//        my_photo_recycler_view.adapter = this.photoGridRecyeclerViewAdapter

        binding.recyclerviewEditCategory.layoutManager = LinearLayoutManager(
            context, LinearLayoutManager.VERTICAL, false
        )
        binding.recyclerviewEditCategory.setHasFixedSize(true)
        binding.recyclerviewEditCategory.adapter = categoryEditAdapter
    }

    //삭제버튼 클릭
    override fun onItemDeleteBtnClicked(position: Int) {
        Log.d("aa", "onSearchItemDeleteBtnClicked: ")
        //해당 번쨰를 삭제 및 저장
        categoryEditList.removeAt(position)
        //데이터 덮어쓰기
//        SharedPrefManager.storeSearchHisotryList(this.searchHistoryList)
        //데이터 변경알림
        this.categoryEditAdapter.notifyDataSetChanged()
    }

    companion object {
        fun newInstance(): CategoryFragment {    // shs: 함수의 반환 형이 Fragment 형이라...
            return CategoryFragment()
        }
    }
}