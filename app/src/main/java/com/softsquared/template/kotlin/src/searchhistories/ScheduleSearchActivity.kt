package com.softsquared.template.kotlin.src.searchhistories

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.EditorInfo.IME_ACTION_SEARCH
import android.widget.TextView.OnEditorActionListener
import androidx.recyclerview.widget.LinearLayoutManager
import com.softsquared.template.kotlin.config.ApplicationClass
import com.softsquared.template.kotlin.config.BaseActivity
import com.softsquared.template.kotlin.config.BaseResponse
import com.softsquared.template.kotlin.databinding.ActivityScheduleSearchBinding
import com.softsquared.template.kotlin.src.main.schedulefind.ScheduleFindView
import com.softsquared.template.kotlin.src.main.schedulefind.adapter.IScheduleCategoryRecyclerView
import com.softsquared.template.kotlin.src.main.schedulefind.models.ScheduleSearchResponse
import com.softsquared.template.kotlin.src.main.schedulefind.models.TodayRestScheduleResponse
import com.softsquared.template.kotlin.src.main.schedulefind.models.WholeScheduleCountResponse
import com.softsquared.template.kotlin.src.main.schedulefind.models.WholeScheduleInquiryResponse
import com.softsquared.template.kotlin.src.searchhistories.adapter.ScheduleSearchListAdapter
import com.softsquared.template.kotlin.src.searchhistories.models.SearchHistoriesResponse
import com.softsquared.template.kotlin.src.searchhistories.models.SearchListData
import com.softsquared.template.kotlin.src.wholeschedule.models.LatelyScheduleInquiryResponse
import com.softsquared.template.kotlin.util.Constants
import kotlinx.android.synthetic.main.dialog_detail_schedule.*


class ScheduleSearchActivity() : BaseActivity<ActivityScheduleSearchBinding>
    (ActivityScheduleSearchBinding::inflate), SearchHistoriesView {

    private var iScheduleCategoryRecyclerView: IScheduleCategoryRecyclerView? = null

//    init {
//        Log.d("TAG", "1a2a3a: init() called ")
//        this.iScheduleCategoryRecyclerView = myScheduleCategoryRecyclerView
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        //임시 검색기록 리사이클러뷰
//        createSearchList()

        SearchHistoriesService(this).tryGetSearchHistories()

        binding.scheduleSearchEt.setOnEditorActionListener(OnEditorActionListener { v, actionId, event ->

            val searchWord = binding.scheduleSearchEt.text.toString()

            when (actionId) {
                IME_ACTION_SEARCH -> {
                    showCustomToast("aaaaaa")
                    Log.d("TAG", "ScheduleSearchActivity: ")
//                    finish()

                    val searchWord = binding.scheduleSearchEt.text.toString()

                    val edit = ApplicationClass.sSharedPreferences.edit()
                    edit.putString(Constants.SEARCHWROD, searchWord)
                    edit.apply()
//                    ScheduleFindService(this).tryGetScheduleSearch(searchWord)

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

    override fun onGetSearchHistoriesSuccess(response: SearchHistoriesResponse) {

        when(response.code){

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
                Log.d("TAG", "onGetSearchHistoriesSuccess: 검식기록조회실패 - ${response.message.toString()}")
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