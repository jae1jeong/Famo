package com.makeus.jfive.famo.src.main.schedulefind

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.makeus.jfive.famo.R
import com.makeus.jfive.famo.config.ApplicationClass
import com.makeus.jfive.famo.src.main.MainActivity
import com.makeus.jfive.famo.src.main.schedulefind.adapter.ScheduleLatelyAdapter
import com.makeus.jfive.famo.src.main.schedulefind.models.WholeScheduleLatelyData
import com.makeus.jfive.famo.src.main.today.models.MemoItem
import com.makeus.jfive.famo.src.wholeschedule.lately.WholeLatelyScheduleService
import com.makeus.jfive.famo.src.wholeschedule.lately.WholeLatelyScheduleView
import com.makeus.jfive.famo.src.wholeschedule.models.LatelyScheduleInquiryResponse
import com.makeus.jfive.famo.util.Constants
import com.makeus.jfive.famo.util.ScheduleDetailDialog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ScheduleFindLatelyFragment : Fragment(), WholeLatelyScheduleView {

    var recyclerViewLately : RecyclerView? = null
    var scheduleFindLatelyFrameLayoutNoItem: FrameLayout? = null
    var scheduelFindLatelyImageNoItem : ImageView? = null


    companion object{
        val latelyListWhole: ArrayList<WholeScheduleLatelyData> = arrayListOf()
        lateinit var scheduleLatelyAdapter:ScheduleLatelyAdapter
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        //setContentView 같다
        val view = inflater.inflate(R.layout.fragment_schedule_find_lately, container,
            false)

//        val edit = ApplicationClass.sSharedPreferences.edit()
//        edit.putString(Constants.BOOKMARK_LATELY_CHECH,"lately")
//        edit.apply()

        recyclerViewLately = view.findViewById(R.id.recyclerView_lately)
        scheduleFindLatelyFrameLayoutNoItem = view.findViewById(R.id.schedule_find_lately_frame_layout_no_item)
        scheduelFindLatelyImageNoItem = view.findViewById(R.id.scheduel_find_lately_image_no_item)

        GlobalScope.launch(Dispatchers.IO){
            delay(1000)
            WholeLatelyScheduleService(this@ScheduleFindLatelyFragment)
                .tryGetLatelyScheduleInquiry(0, 2)
        }

        scheduelFindLatelyImageNoItem!!.setOnClickListener {
            (activity as MainActivity).stateChangeBottomSheet(Constants.COLLASPE)
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        scheduleLatelyAdapter = ScheduleLatelyAdapter(latelyListWhole){}
    }
    override fun onGetLatelyScheduleInquirySuccess(response: LatelyScheduleInquiryResponse) {

        when (response.code) {
            100 -> {
                Log.d("TAG", "onGetLatelyScheduleInquirySuccess: 최근일정조회성공")

                latelyListWhole.clear()
                if (response.data.size == 0){
                    recyclerViewLately!!.visibility = View.GONE
                    scheduleFindLatelyFrameLayoutNoItem!!.visibility = View.VISIBLE
                }
                else{
                    recyclerViewLately!!.visibility = View.VISIBLE
                    scheduleFindLatelyFrameLayoutNoItem!!.visibility = View.GONE

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
                    recyclerViewLately!!.layoutManager = LinearLayoutManager(
                        context, LinearLayoutManager.VERTICAL, false
                    )
                    recyclerViewLately!!.setHasFixedSize(true)
                    scheduleLatelyAdapter = ScheduleLatelyAdapter(latelyListWhole) { it ->
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
                        detailDialog.start(scheduleItem,null)
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
                    recyclerViewLately!!.adapter = scheduleLatelyAdapter
                }



            }
            else -> {
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