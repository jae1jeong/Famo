package com.makeus.jfive.famo.src.searchhistories

import android.os.Bundle
import android.util.Log
import android.view.inputmethod.EditorInfo.IME_ACTION_SEARCH
import android.widget.TextView.OnEditorActionListener
import androidx.recyclerview.widget.LinearLayoutManager
import com.makeus.jfive.famo.config.ApplicationClass
import com.makeus.jfive.famo.config.BaseActivity
import com.makeus.jfive.famo.config.BaseResponse
import com.makeus.jfive.famo.databinding.ActivityScheduleSearchBinding
import com.makeus.jfive.famo.src.main.schedulefind.adapter.IScheduleCategoryRecyclerView
import com.makeus.jfive.famo.src.searchhistories.adapter.ScheduleSearchListAdapter
import com.makeus.jfive.famo.src.searchhistories.models.SearchHistoriesResponse
import com.makeus.jfive.famo.src.searchhistories.models.SearchListData
import com.makeus.jfive.famo.util.Constants


class ScheduleSearchActivity() : BaseActivity<ActivityScheduleSearchBinding>
    (ActivityScheduleSearchBinding::inflate), SearchHistoriesView {

    private var iScheduleCategoryRecyclerView: IScheduleCategoryRecyclerView? = null

//    init {
//        Log.d("TAG", "1a2a3a: init() called ")
//        this.iScheduleCategoryRecyclerView = myScheduleCategoryRecyclerView
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        SearchHistoriesService(this).tryGetSearchHistories()


        binding.scheduleSearchEt.setOnEditorActionListener(OnEditorActionListener { v, actionId, event ->
            val searchWord = binding.scheduleSearchEt.text.toString()

            if (searchWord.isNotEmpty()) {
                Log.d("TAG", "onCreate: 검색어1")
                when (actionId) {
                    IME_ACTION_SEARCH -> {
                        val edit = ApplicationClass.sSharedPreferences.edit()
                        edit.putString(Constants.SEARCHWROD, searchWord)
                        edit.putString(Constants.SEARCH_WROD_COLOR, searchWord)
                        edit.putString(Constants.SEARCH_TEST, searchWord)
                        edit.apply()
                        Constants.SEARCH_CHECK = true
                        finish()

//                    MainActivity().onMoveScheduleFind(searchWord)
//                    iScheduleCategoryRecyclerView!!.onItemMoveBtnClicked(1,1)
                    }
                }

            } else {
                Log.d("TAG", "onCreate: 검색어2")
                showCustomToast("검색어를 입력해주세요.")
            }
            false

        })


        //뒤로가기
        binding.searchBack.setOnClickListener {
            Constants.SEARCH_CHECK = false
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

    override fun onGetSearchHistoriesSuccess(response: SearchHistoriesResponse) {

        when (response.code) {

            100 -> {
                Log.d("TAG", "onGetSearchHistoriesSuccess: 검식기록조회성공")
                val searchList: ArrayList<SearchListData> = arrayListOf()

                for (i in 0 until response.data.size) {
                    searchList.add(
                        SearchListData(
                            response.data[i].searchHistory
                        )
                    )
                }

                binding.recyclerviewSearchList.layoutManager = LinearLayoutManager(
                    this,
                    LinearLayoutManager.VERTICAL, false
                )
                binding.recyclerviewSearchList.setHasFixedSize(true)
                binding.recyclerviewSearchList.adapter = ScheduleSearchListAdapter(searchList)
            }
            else -> {
                Log.d(
                    "TAG",
                    "onGetSearchHistoriesSuccess: 검식기록조회실패 - ${response.message.toString()}"
                )
            }
        }


    }

    override fun onGetSearchHistoriesFail(message: String) {

    }

    override fun onDeleteSearchHistoriesSuccess(response: BaseResponse) {
    }

    override fun onDeleteSearchHistoriesFail(message: String) {
    }

}