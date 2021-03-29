package com.softsquared.template.kotlin.src.main.schedulefind

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.softsquared.template.kotlin.R
import com.softsquared.template.kotlin.config.ApplicationClass
import com.softsquared.template.kotlin.config.BaseFragment
import com.softsquared.template.kotlin.databinding.FragmentScheduleFindLatelyBinding
import com.softsquared.template.kotlin.src.main.MainActivity
import com.softsquared.template.kotlin.src.main.schedulefind.adapter.ScheduleBookmarkAdapter
import com.softsquared.template.kotlin.src.main.schedulefind.adapter.ScheduleLatelyAdapter
import com.softsquared.template.kotlin.src.main.schedulefind.models.WholeScheduleLatelyData
import com.softsquared.template.kotlin.src.main.today.models.MemoItem
import com.softsquared.template.kotlin.src.wholeschedule.lately.WholeLatelyScheduleService
import com.softsquared.template.kotlin.src.wholeschedule.lately.WholeLatelyScheduleView
import com.softsquared.template.kotlin.src.wholeschedule.models.LatelyScheduleInquiryResponse
import com.softsquared.template.kotlin.util.Constants
import com.softsquared.template.kotlin.util.ScheduleDetailDialog

class ScheduleFindLatelyFragment : BaseFragment<FragmentScheduleFindLatelyBinding>(
    FragmentScheduleFindLatelyBinding::bind, R.layout.fragment_schedule_find_lately
), WholeLatelyScheduleView {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

//        createLatelyRecyclerview()


    override fun viewPagerApiRequest() {
        super.viewPagerApiRequest()
//        WholeLatelyScheduleService(this).tryGetLatelyScheduleInquiry(0, 2)
    }


    override fun onGetLatelyScheduleInquirySuccess(response: LatelyScheduleInquiryResponse) {

        when (response.code) {
            100 -> {
                Log.d("TAG", "onGetLatelyScheduleInquirySuccess: 최근일정조회성공")

                val latelyListWhole: ArrayList<WholeScheduleLatelyData> = arrayListOf()

                for (i in 0 until response.data.size) {

                    if (response.data[i].colorInfo != null){
                        latelyListWhole.add(
                            WholeScheduleLatelyData(
                                response.data[i].scheduleID,
                                response.data[i].scheduleDate,
                                response.data[i].scheduleName,
                                response.data[i].scheduleMemo,
                                R.drawable.schedule_find_inbookmark,
                                response.data[i].categoryID,
                                response.data[i].colorInfo
                            )
                        )
                    }else{
                        latelyListWhole.add(
                            WholeScheduleLatelyData(
                                response.data[i].scheduleID,
                                response.data[i].scheduleDate,
                                response.data[i].scheduleName,
                                response.data[i].scheduleMemo,
                                R.drawable.schedule_find_inbookmark,
                                response.data[i].categoryID,
                                "#ced5d9"
                            )
                        )

                    }

                }

                // 즐겨찾기/최근 일정 리사이클러뷰 연결
                binding.recyclerViewLately.layoutManager = LinearLayoutManager(
                    context, LinearLayoutManager.VERTICAL, false
                )
                binding.recyclerViewLately.setHasFixedSize(true)
//                binding.recyclerViewLately.adapter = ScheduleLatelyAdapter(latelyListWhole,this)

                binding.recyclerViewLately.adapter = ScheduleLatelyAdapter(latelyListWhole) { it ->
                    val detailDialog = ScheduleDetailDialog(context!!)
                    val scheduleItem = MemoItem(
                        it.scheduleID,
                        it.scheduleDate,
                        0,
                        it.scheduleName,
                        it.scheduleMemo,
                        false,
                        null,
                        null
                    )
                    detailDialog.start(scheduleItem,null)
                    detailDialog.setOnModifyBtnClickedListener {
                        // 스케쥴 ID 보내기
                        val edit = ApplicationClass.sSharedPreferences.edit()
                        edit.putInt(Constants.EDIT_SCHEDULE_ID, it.scheduleID)
                        edit.apply()
                        Constants.IS_EDIT = true

                        //바텀 시트 다이얼로그 확장
                        (activity as MainActivity).stateChangeBottomSheet(Constants.EXPAND)

//                val recycleAdapter = ScheduleBookmarkAdapter(boomarkList,this,)
//                binding.recyclerViewBookmark.adapter = recycleAdapter
                    }

                }


            }
            else -> {
                showCustomToast("즐겨찾기 실패")
                Log.d(
                    "TAG",
                    "onGetLatelyScheduleInquirySuccess: 최근일정조회실패 - ${response.message.toString()}"
                )
            }
        }
    }

    override fun onGetLatelyScheduleInquiryFail(message: String) {
    }


}