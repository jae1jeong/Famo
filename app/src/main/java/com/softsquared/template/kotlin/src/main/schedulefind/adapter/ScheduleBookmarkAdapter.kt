package com.softsquared.template.kotlin.src.main.schedulefind.adapter

import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.JsonElement
import com.softsquared.template.kotlin.R
import com.softsquared.template.kotlin.config.ApplicationClass
import com.softsquared.template.kotlin.config.BaseResponse
import com.softsquared.template.kotlin.src.main.AddMemoService
import com.softsquared.template.kotlin.src.main.AddMemoView
import com.softsquared.template.kotlin.src.main.MainActivity
import com.softsquared.template.kotlin.src.main.models.DetailMemoResponse
import com.softsquared.template.kotlin.src.main.schedulefind.models.WholeScheduleBookmarkData
import com.softsquared.template.kotlin.src.main.today.TodayFragment
import com.softsquared.template.kotlin.src.main.today.TodayService
import com.softsquared.template.kotlin.src.main.today.TodayView
import com.softsquared.template.kotlin.src.main.today.adapter.MemoAdapter
import com.softsquared.template.kotlin.src.main.today.models.CheckItemRequest
import com.softsquared.template.kotlin.src.main.today.models.ScheduleItemsResponse
import com.softsquared.template.kotlin.src.main.today.models.TopCommentResponse
import com.softsquared.template.kotlin.util.Constants
import com.softsquared.template.kotlin.util.ScheduleDetailDialog


class ScheduleBookmarkAdapter(
    var bookmarkListWhole: ArrayList<WholeScheduleBookmarkData>,
    myScheduleCategoryRecyclerView: IScheduleCategoryRecyclerView,
    val clickListener:(WholeScheduleBookmarkData)->Unit
) :
    RecyclerView.Adapter<ScheduleBookmarkAdapter.ScheduleBookmarkHolder>(), AddMemoView,
    TodayView {

    private var iScheduleCategoryRecyclerView: IScheduleCategoryRecyclerView? = null

    private var mCallback: OnItemClick? = null

    private var context: Context? = null

    interface OnItemClick {
        fun onClick(value: String?)
    }

    open fun RecycleAdapter(context: Context, listener: OnItemClick) {
        this.context = context
        this.mCallback = listener
    }

    init {
        Log.d("TAG", "ScheduleCategoryAdapter: init() called ")
        this.iScheduleCategoryRecyclerView = myScheduleCategoryRecyclerView
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScheduleBookmarkHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recyclerview_schedule_find_bookmark_item, parent, false)
//        view.setOnClickListener {
////            AddMemoService(this@ScheduleBookmarkAdapter).tryGetDetailMemo(
////                98
////            )
//
//        }
        return ScheduleBookmarkHolder(view)
    }

    override fun getItemCount(): Int = bookmarkListWhole.size

    override fun onBindViewHolder(holder: ScheduleBookmarkHolder, position: Int) {
        holder.scheduleName.text = bookmarkListWhole[position].scheduleName
        holder.scheduleDate.text = bookmarkListWhole[position].scheduleDate

        if (bookmarkListWhole[position].colorInfo != null){
            holder.color.setColorFilter(Color.parseColor(bookmarkListWhole[position].colorInfo))
        }else{
            holder.color.setColorFilter(Color.parseColor("#ced5d9"))
        }

        holder.itemView.setOnClickListener {
            clickListener(bookmarkListWhole[position])
        }
    }

    class ScheduleBookmarkHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val scheduleName = itemView.findViewById<TextView>(R.id.recycler_part_title)
        val scheduleDate = itemView.findViewById<TextView>(R.id.recycler_part_times)
        val color = itemView.findViewById<ImageView>(R.id.recycler_part_color)

    }

    override fun onPostAddMemoSuccess(response: BaseResponse) {
    }

    override fun onPostAddMemoFailure(message: String) {
    }

    override fun onPatchMemoSuccess(response: BaseResponse) {
    }

    override fun onPatchMemoFailure(message: String) {
    }

    override fun onGetScheduleItemsSuccess(response: ScheduleItemsResponse) {
    }

    override fun onGetScheduleItemsFailure(message: String) {
    }

    override fun onDeleteMemoSuccess(response: BaseResponse, scheduleID: Int) {
    }

    override fun onDeleteMemoFailure(message: String) {
    }

    override fun onPostItemCheckSuccess(response: BaseResponse) {
    }

    override fun onPostItemCheckFailure(message: String) {
    }

    override fun onGetUserTopCommentSuccess(response: TopCommentResponse) {
    }

    override fun onGetUserTopCommentFailure(message: String) {
    }

    override fun onPostSchedulePositionSuccess(response: BaseResponse) {
    }

    override fun onPostSchedulePositionFailure(message: String) {
    }

    override fun onGetDetailMemoSuccess(response: DetailMemoResponse) {
        when (response.code) {
            100 -> {

                TodayFragment.todayMemoAdapter = MemoAdapter(TodayFragment.memoList, context!!, {
                    // 디테일 다이얼로그
                    val scheduleDetailDialog = ScheduleDetailDialog(context!!)
                    // 디테일 다이얼로그 수정하기 버튼
                    // 날짜 form 만들어야함
                    scheduleDetailDialog.start(it)
                }, {
                    // 일정완료 버튼
                    TodayService(this@ScheduleBookmarkAdapter).onPostCheckItem(CheckItemRequest(it.id))
                },{

                })


                }


//                iScheduleCategoryRecyclerView!!.onScheduleDetail(memoTitle, memoContent, memoDate)

            }
        }

    override fun onGetDetailMemoFailure(message: String) {
    }


}

