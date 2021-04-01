package com.softsquared.template.kotlin.src.wholeschedule.bookmark

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.lakue.pagingbutton.OnPageSelectListener
import com.softsquared.template.kotlin.R
import com.softsquared.template.kotlin.config.ApplicationClass
import com.softsquared.template.kotlin.config.BaseFragment
import com.softsquared.template.kotlin.databinding.FragmentScheduleFindBookmarkBinding
import com.softsquared.template.kotlin.src.main.MainActivity
import com.softsquared.template.kotlin.src.main.schedulefind.models.ScheduleBookmarkResponse
import com.softsquared.template.kotlin.src.main.schedulefind.models.WholeScheduleBookmarkData
import com.softsquared.template.kotlin.src.main.schedulefind.models.WholeScheduleLatelyData
import com.softsquared.template.kotlin.src.main.today.models.MemoItem
import com.softsquared.template.kotlin.src.wholeschedule.WholeScheduleActivity
import com.softsquared.template.kotlin.src.wholeschedule.bookmark.adapter.WholeScheduleBookmarkAdapter
import com.softsquared.template.kotlin.util.Constants
import com.softsquared.template.kotlin.util.ScheduleDetailDialog
import kotlinx.android.synthetic.main.category_delete_impossible_dialog.*
import java.util.ArrayList

class WholeScheduleBookmarkFragment : BaseFragment<FragmentScheduleFindBookmarkBinding>(
    FragmentScheduleFindBookmarkBinding::bind, R.layout.fragment_schedule_find_bookmark
),
    WholeBookmarkScheduleView {

    var bookmarkSchedulePagingCnt = 0
    var testCnt = 0

    companion object{
        lateinit var wholeScheduleBookmarkAdapter:WholeScheduleBookmarkAdapter
        val bookmarkListWhole: ArrayList<WholeScheduleBookmarkData> = arrayListOf()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        wholeScheduleBookmarkAdapter = WholeScheduleBookmarkAdapter(bookmarkListWhole){}

        binding.scheduelFindBookmarkImageNoItem.setOnClickListener {
            (activity as WholeScheduleActivity).stateChangeBottomSheet(Constants.COLLASPE)
        }

        //페이징 수를 위해 설정
        if (testCnt > 0) {
            WholeBookmarkScheduleService(this).tryGetScheduleBookmark(0, 10)
        } else {
            showLoadingDialog(context!!)
            WholeBookmarkScheduleService(this).tryGetScheduleBookmark(0, 999)
        }


        //한 번에 표시되는 버튼 수 (기본값 : 5)
        binding.wholeBookmarkSchedulePaging.setPageItemCount(4);

        //페이지 리스너를 클릭했을 때의 이벤트
        binding.wholeBookmarkSchedulePaging.setOnPageSelectListener(object : OnPageSelectListener {
            //PrevButton Click
            override fun onPageBefore(now_page: Int) {
                //prev 버튼을 클릭하면 버튼이 재설정되고 버튼이 그려집니다.
                binding.wholeBookmarkSchedulePaging.addBottomPageButton(
                    bookmarkSchedulePagingCnt,
                    now_page
                )
                WholeBookmarkScheduleService(this@WholeScheduleBookmarkFragment)
                    .tryGetScheduleBookmark(((now_page - 1)* 10), 10)

            }

            override fun onPageCenter(now_page: Int) {
                WholeBookmarkScheduleService(this@WholeScheduleBookmarkFragment)
                    .tryGetScheduleBookmark(((now_page - 1) * 10), 10)

            }

            //NextButton Click
            override fun onPageNext(now_page: Int) {
                //next 버튼을 클릭하면 버튼이 재설정되고 버튼이 그려집니다.
                binding.wholeBookmarkSchedulePaging.addBottomPageButton(
                    bookmarkSchedulePagingCnt,
                    now_page
                )
                WholeBookmarkScheduleService(this@WholeScheduleBookmarkFragment)
                    .tryGetScheduleBookmark(((now_page - 1) * 10), 10)

            }
        })

    }


    override fun onGetScheduleBookmarkSuccess(response: ScheduleBookmarkResponse) {

        when (response.code) {
            100 -> {
                bookmarkListWhole.clear()
                Log.d("TAG", "onGetScheduleBookmarkSuccess: 전체즐겨찾기조회성공")

                if (response.data.size == 0) {
                    binding.scheduleFindBookmark!!.visibility = View.GONE
                    binding.scheduleFindBookmarkFrameLayoutNoItem!!.visibility = View.VISIBLE

                } else {
                    binding.scheduleFindBookmark!!.visibility = View.VISIBLE
                    binding.scheduleFindBookmarkFrameLayoutNoItem!!.visibility = View.GONE
                    if (testCnt == 0) {
                        val cnt = response.data.size
                        //페이징수 세팅
                        if (cnt % 10 == 0) {
                            bookmarkSchedulePagingCnt = cnt / 10
                        } else {
                            bookmarkSchedulePagingCnt = (cnt / 10) + 1
                        }
                        binding.wholeBookmarkSchedulePaging.addBottomPageButton(
                                bookmarkSchedulePagingCnt,
                                1
                        )
                        testCnt++
                        WholeBookmarkScheduleService(this).tryGetScheduleBookmark(0, 10)

                    }


                    if (testCnt != 0) {

                        for (i in 0 until response.data.size) {
                            Log.d("TAG", "onGetScheduleBookmarkSuccess: ${response.data.size} ${testCnt}")
                            if (response.data[i].schedulePick == -1 && response.data[i].colorInfo != null) {
                                bookmarkListWhole.add(
                                        WholeScheduleBookmarkData(
                                                response.data[i].scheduleID,
                                                response.data[i].scheduleDate,
                                                response.data[i].scheduleName,
                                                response.data[i].scheduleMemo,
                                                R.drawable.schedule_find_inbookmark,
                                                response.data[i].categoryID, response.data[i].colorInfo
                                        )
                                )

                            } else if (response.data[i].schedulePick == -1 && response.data[i].colorInfo == null) {
                                bookmarkListWhole.add(
                                        WholeScheduleBookmarkData(
                                                response.data[i].scheduleID,
                                                response.data[i].scheduleDate,
                                                response.data[i].scheduleName,
                                                response.data[i].scheduleMemo,
                                                R.drawable.schedule_find_inbookmark,
                                                response.data[i].categoryID,
                                                "#CED5D9"
                                        )
                                )
                            } else if (response.data[i].schedulePick == 1 && response.data[i].colorInfo == null) {
                                bookmarkListWhole.add(
                                        WholeScheduleBookmarkData(
                                                response.data[i].scheduleID,
                                                response.data[i].scheduleDate,
                                                response.data[i].scheduleName,
                                                response.data[i].scheduleMemo,
                                                R.drawable.schedule_find_bookmark,
                                                response.data[i].categoryID,
                                                "#CED5D9"
                                        )
                                )
                            } else {
                                bookmarkListWhole.add(
                                        WholeScheduleBookmarkData(
                                                response.data[i].scheduleID,
                                                response.data[i].scheduleDate,
                                                response.data[i].scheduleName,
                                                response.data[i].scheduleMemo,
                                                R.drawable.schedule_find_bookmark,
                                                response.data[i].categoryID,
                                                response.data[i].colorInfo
                                        )
                                )
                            }
                        }

                        // 즐겨찾기/최근 일정 리사이클러뷰 연결
                        binding.recyclerViewBookmark.layoutManager = GridLayoutManager(
                                context, 2, GridLayoutManager.VERTICAL, false
                        )

                        wholeScheduleBookmarkAdapter = WholeScheduleBookmarkAdapter(bookmarkListWhole) {
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
                            ,0)
                            detailDialog.start(scheduleItem, null)
                            detailDialog.setOnModifyBtnClickedListener {
                                // 스케쥴 ID 보내기
                                val edit = ApplicationClass.sSharedPreferences.edit()
                                edit.putInt(Constants.EDIT_SCHEDULE_ID, it.scheduleID)
                                edit.apply()
                                Constants.IS_EDIT = true

                                //바텀 시트 다이얼로그 확장
                                (activity as WholeScheduleActivity).stateChangeBottomSheet(Constants.EXPAND)
                            }
                        }
                        binding.recyclerViewBookmark.setHasFixedSize(true)
                        binding.recyclerViewBookmark.adapter = wholeScheduleBookmarkAdapter

                    }
                }


            }


        }
        dismissLoadingDialog()
    }

    override fun onGetScheduleBookmarkFail(message: String) {
    }
}