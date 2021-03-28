package com.softsquared.template.kotlin.src.main.schedulefind

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.softsquared.template.kotlin.R
import com.softsquared.template.kotlin.config.ApplicationClass
import com.softsquared.template.kotlin.config.BaseFragment
import com.softsquared.template.kotlin.config.BaseResponse
import com.softsquared.template.kotlin.databinding.FragmentScheduleFindBookmarkBinding
import com.softsquared.template.kotlin.src.main.AddMemoService
import com.softsquared.template.kotlin.src.main.AddMemoView
import com.softsquared.template.kotlin.src.main.MainActivity
import com.softsquared.template.kotlin.src.main.category.ICategoryRecyclerView
import com.softsquared.template.kotlin.src.main.models.DetailMemoResponse
import com.softsquared.template.kotlin.src.main.schedulefind.adapter.IScheduleCategoryRecyclerView
import com.softsquared.template.kotlin.src.main.schedulefind.adapter.ScheduleBookmarkAdapter
import com.softsquared.template.kotlin.src.main.schedulefind.models.ScheduleBookmarkResponse
import com.softsquared.template.kotlin.src.main.schedulefind.models.WholeScheduleBookmarkData
import com.softsquared.template.kotlin.src.main.today.models.MemoItem
import com.softsquared.template.kotlin.util.Constants
import com.softsquared.template.kotlin.util.ScheduleDetailDialog
import java.util.*
import kotlin.collections.ArrayList


class ScheduleFindBookmarkFragment : BaseFragment<FragmentScheduleFindBookmarkBinding>(
    FragmentScheduleFindBookmarkBinding::bind, R.layout.fragment_schedule_find_bookmark
), ScheduleBookmarkView, ScheduleBookmarkAdapter.OnItemClick, IScheduleCategoryRecyclerView,
    AddMemoView{

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun viewPagerApiRequest() {
        super.viewPagerApiRequest()
        ScheduleBookmarkService(this).tryGetScheduleBookmark(0, 2)
    }

    override fun onGetScheduleBookmarkSuccess(response: ScheduleBookmarkResponse) {

        when (response.code) {
            100 -> {
                showCustomToast("즐겨찾기 일정조회성공")
                Log.d("TAG", "onGetScheduleBookmarkSuccess: 즐겨찾기 일정조회성공")

                val boomarkList: ArrayList<WholeScheduleBookmarkData> = arrayListOf()

                for (i in 0 until response.data.size) {

                    if (response.data[i].colorInfo != null){
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
                    }else{
                        boomarkList.add(
                            WholeScheduleBookmarkData(
                                response.data[i].scheduleID,
                                response.data[i].scheduleDate,
                                response.data[i].scheduleName,
                                response.data[i].scheduleMemo,
                                response.data[i].schedulePick,
                                response.data[i].categoryID,
                                "#ced5d9"
                            )
                        )
                    }


                }

                binding.recyclerViewBookmark.layoutManager = LinearLayoutManager(
                    context, LinearLayoutManager.VERTICAL, false
                )
                binding.recyclerViewBookmark.setHasFixedSize(true)
                binding.recyclerViewBookmark.adapter = ScheduleBookmarkAdapter(boomarkList,this) { it ->
                    val detailDialog = ScheduleDetailDialog(context!!)
                    val scheduleItem = MemoItem(
                        it.scheduleID,
                        "",
                        0,
                        it.scheduleName,
                        it.scheduleMemo,
                        false,
                        null
                    )
                    detailDialog.start(scheduleItem)
                    detailDialog.setOnModifyBtnClickedListener {
                        // 스케쥴 ID 보내기
                        val edit = ApplicationClass.sSharedPreferences.edit()
                        edit.putInt(Constants.EDIT_SCHEDULE_ID, it.scheduleID)
                        edit.apply()
                        Constants.IS_EDIT = true

                        //바텀 시트 다이얼로그 확장
                        (activity as MainActivity).stateChangeBottomSheet(Constants.EXPAND)

                    }

                }
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

    override fun onClick(value: String?) {
    }

    override fun onItemMoveBtnClicked(scheduleCategoryID: Int) {
    }

    override fun onColor(): ArrayList<String> {

        val aa = ArrayList<String>()
        return aa
    }

    override fun onMoveFilterFragment(scheduleCategoryID: Int) {
    }

    override fun onScheduleDetail(memoTitle : String, memoContent: String, memoDate: String) {
        Log.d("TAG", "onScheduleDetail: 다이얼로그확인")
        (activity as MainActivity).setFormBottomSheetDialog(memoTitle, memoContent, memoDate)
    }

    override fun onPostAddMemoSuccess(response: BaseResponse) {
    }

    override fun onPostAddMemoFailure(message: String) {
    }

    override fun onPatchMemoSuccess(response: BaseResponse) {
    }

    override fun onPatchMemoFailure(message: String) {
    }

    override fun onGetDetailMemoSuccess(response: DetailMemoResponse) {

    }

    override fun onGetDetailMemoFailure(message: String) {
    }
}