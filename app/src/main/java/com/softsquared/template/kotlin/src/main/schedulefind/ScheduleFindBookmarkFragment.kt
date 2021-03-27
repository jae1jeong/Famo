package com.softsquared.template.kotlin.src.main.schedulefind

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.softsquared.template.kotlin.R
import com.softsquared.template.kotlin.config.BaseFragment
import com.softsquared.template.kotlin.databinding.FragmentScheduleFindBookmarkBinding
import com.softsquared.template.kotlin.src.main.schedulefind.adapter.ScheduleBookmarkAdapter
import com.softsquared.template.kotlin.src.main.schedulefind.models.ScheduleBookmarkResponse
import com.softsquared.template.kotlin.src.main.schedulefind.models.WholeScheduleBookmarkData
import java.util.ArrayList

class ScheduleFindBookmarkFragment : BaseFragment<FragmentScheduleFindBookmarkBinding>(
    FragmentScheduleFindBookmarkBinding::bind, R.layout.fragment_schedule_find_bookmark),
    ScheduleBookmarkView{

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ScheduleBookmarkService(this).tryGetScheduleBookmark(0,2)
    }

    override fun onGetScheduleBookmarkSuccess(response: ScheduleBookmarkResponse) {

        when (response.code) {
            100 -> {
                showCustomToast("즐겨찾기 일정조회성공")
                Log.d("TAG", "onGetScheduleBookmarkSuccess: 즐겨찾기 일정조회성공")

                val boomarkList: ArrayList<WholeScheduleBookmarkData> = arrayListOf()

                for (i in 0 until response.data.size) {
                    boomarkList.add(
                        WholeScheduleBookmarkData(
                            response.data[i].scheduleID,
                            response.data[i].scheduleDate,
                            response.data[i].scheduleName,
                            response.data[i].scheduleMemo,
                            response.data[i].schedulePick,
                            response.data[i].categoryID,
                            response.data[i].colorInfo
                        )
                    )
                }

                binding.recyclerViewBookmark.layoutManager = LinearLayoutManager(
                    context, LinearLayoutManager.VERTICAL, false
                )
                binding.recyclerViewBookmark.setHasFixedSize(true)
                binding.recyclerViewBookmark.adapter = ScheduleBookmarkAdapter(boomarkList)
//                scheduleCategoryAdapter.notifyDataSetChanged()


            }
            else -> {
                showCustomToast("즐겨찾기 실패")
                Log.d(
                    "TAG",
                    "onGetScheduleBookmarkSuccess: 즐겨찾기 일정조회성공 ${response.message.toString()}"
                )
            }
        }
    }

    override fun onGetScheduleBookmarkFail(message: String) {
    }
}