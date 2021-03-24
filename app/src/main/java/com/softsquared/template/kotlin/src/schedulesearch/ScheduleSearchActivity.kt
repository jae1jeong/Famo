package com.softsquared.template.kotlin.src.schedulesearch

import android.os.Bundle
import android.util.Log
import android.view.inputmethod.EditorInfo.IME_ACTION_SEARCH
import android.widget.TextView.OnEditorActionListener
import androidx.recyclerview.widget.LinearLayoutManager
import com.softsquared.template.kotlin.config.ApplicationClass
import com.softsquared.template.kotlin.config.BaseActivity
import com.softsquared.template.kotlin.databinding.ActivityScheduleSearchBinding
import com.softsquared.template.kotlin.src.main.MainActivity
import com.softsquared.template.kotlin.src.main.schedulefind.adapter.IScheduleCategoryRecyclerView
import com.softsquared.template.kotlin.src.schedulesearch.adapter.ScheduleSearchListAdapter
import com.softsquared.template.kotlin.src.schedulesearch.models.SearchListData
import com.softsquared.template.kotlin.util.Constants


class ScheduleSearchActivity() : BaseActivity<ActivityScheduleSearchBinding>
    (ActivityScheduleSearchBinding::inflate) {

    private var iScheduleCategoryRecyclerView: IScheduleCategoryRecyclerView? = null

//    init {
//        Log.d("TAG", "1a2a3a: init() called ")
//        this.iScheduleCategoryRecyclerView = myScheduleCategoryRecyclerView
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //임시 검색기록 리사이클러뷰
        createSearchList()

        binding.scheduleSearchEt.setOnEditorActionListener(OnEditorActionListener { v, actionId, event ->

            val searchWord = binding.scheduleSearchEt.text.toString()

            when(actionId){
                IME_ACTION_SEARCH -> {
                    showCustomToast("aaaaaa")
                    Log.d("TAG", "ScheduleSearchActivity: ")
//                    finish()

                    val searchWord = binding.scheduleSearchEt.text.toString()

                    val edit = ApplicationClass.sSharedPreferences.edit()
                    edit.putString(Constants.SEARCHWROD,searchWord)
                    edit.apply()
                    finish()

//                    MainActivity().onMoveScheduleFind(searchWord)
//                    iScheduleCategoryRecyclerView!!.onItemMoveBtnClicked(1,1)
                }
            }

            false
        })


        //뒤로가기
        binding.searchBack.setOnClickListener {
            finish()
        }

    }

    private fun createSearchList() {
        val searchList = arrayListOf(
            SearchListData("검색1"),
            SearchListData("검색2"),
            SearchListData("검색3"),
            SearchListData("검색4")
        )

        //전체일정 리사이큘러뷰 연결
        binding.recyclerviewSearchList.layoutManager = LinearLayoutManager(
            this,
            LinearLayoutManager.VERTICAL, false
        )
        binding.recyclerviewSearchList.setHasFixedSize(true)
        binding.recyclerviewSearchList.adapter = ScheduleSearchListAdapter(searchList)
    }

}