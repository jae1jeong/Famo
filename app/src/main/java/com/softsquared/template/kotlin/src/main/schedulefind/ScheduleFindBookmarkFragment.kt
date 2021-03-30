package com.softsquared.template.kotlin.src.main.schedulefind

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.ArrayList
import kotlinx.android.synthetic.main.fragment_schedule_find_bookmark.*

class ScheduleFindBookmarkFragment : Fragment(), ScheduleBookmarkView, AddMemoView {

    var recyclerViewBookmark: RecyclerView? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //setContentView 같다
        val view = inflater.inflate(
            R.layout.fragment_schedule_find_bookmark, container,
            false
        )

        recyclerViewBookmark = view.findViewById(R.id.recyclerView_bookmark)

        GlobalScope.launch(Dispatchers.IO) {
            delay(1000)
            ScheduleBookmarkService(this@ScheduleFindBookmarkFragment).tryGetScheduleBookmark(0, 2)
        }

        return view
    }

    override fun onGetScheduleBookmarkSuccess(response: ScheduleBookmarkResponse) {

        when (response.code) {
            100 -> {
                Log.d("TAG", "onGetScheduleBookmarkSuccess: 즐겨찾기 일정조회성공")

                val boomarkList: ArrayList<WholeScheduleBookmarkData> = arrayListOf()

                for (i in 0 until response.data.size) {

                    if (response.data[i].colorInfo != null) {
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
                    } else {
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
                recyclerViewBookmark!!.layoutManager = LinearLayoutManager(
                    context, LinearLayoutManager.VERTICAL, false
                )
                recyclerViewBookmark!!.setHasFixedSize(true)
                recyclerViewBookmark!!.adapter = ScheduleBookmarkAdapter(boomarkList) { it ->
                    val detailDialog = ScheduleDetailDialog(context!!)
                    val scheduleItem = MemoItem(
                        it.scheduleID,
                        "",
                        0,
                        it.scheduleName,
                        it.scheduleMemo,
                        false,
                        null,
                    "")

                    detailDialog.start(scheduleItem,"탑제목?")
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
                Log.d(
                    "TAG",
                    "onGetScheduleBookmarkSuccess: 즐겨찾기 일정조회성공 ${response.message.toString()}"
                )
            }
        }

    }

    override fun onGetScheduleBookmarkFail(message: String) {
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