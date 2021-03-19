package com.softsquared.template.kotlin.src.main.schedulefind

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.softsquared.template.kotlin.R
import kotlinx.android.synthetic.main.fragment_schedule_find_filter_bottom_dialog.*


class SchedulefindFilterBottomDialogFragment : BottomSheetDialogFragment() {

    //클릭에 따른 체크표시 활성화를 위한 변수
    var remainCnt = 1
    var completionCnt = 1
    var recentsCnt = 1
    var bookmarkCnt = 1


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_schedule_find_filter_bottom_dialog, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //남은 일정 클릭 시
        filter_btn_remain.setOnClickListener {

            if (remainCnt % 2 != 0){
                filter_btn_remain_squre.visibility = View.VISIBLE
                filter_btn_remain_check.visibility = View.VISIBLE
            }else{
                filter_btn_remain_squre.visibility = View.GONE
                filter_btn_remain_check.visibility = View.GONE
            }
            remainCnt++

        }

        //완료 일정 클릭 시
        filter_btn_completion.setOnClickListener {

            if (completionCnt % 2 != 0){
                filter_btn_completion_squre.visibility = View.VISIBLE
                filter_btn_completion_check.visibility = View.VISIBLE
            }else{
                filter_btn_completion_squre.visibility = View.GONE
                filter_btn_completion_check.visibility = View.GONE
            }
            completionCnt++

        }

        //최신 클릭 시
        filter_btn_recents.setOnClickListener {

            if (recentsCnt % 2 != 0){
                filter_btn_recents_squre.visibility = View.VISIBLE
                filter_btn_recents_check.visibility = View.VISIBLE
            }else{
                filter_btn_recents_squre.visibility = View.GONE
                filter_btn_recents_check.visibility = View.GONE
            }
            recentsCnt++

        }

        //즐겨찾기 클릭 시
        filter_btn_bookmark.setOnClickListener {

            if (bookmarkCnt % 2 != 0){
                filter_btn_bookmark_squre.visibility = View.VISIBLE
                filter_btn_bookmark_check.visibility = View.VISIBLE
            }else{
                filter_btn_bookmark_squre.visibility = View.GONE
                filter_btn_bookmark_check.visibility = View.GONE
            }
            bookmarkCnt++
        }

    }


}