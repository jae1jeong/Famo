package com.makeus.jfive.famo.src.main.schedulefind.adapter

import android.annotation.SuppressLint
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.makeus.jfive.famo.R
import com.makeus.jfive.famo.config.ApplicationClass
import com.makeus.jfive.famo.config.BaseResponse
import com.makeus.jfive.famo.src.main.schedulefind.ScheduleBookmarkView
import com.makeus.jfive.famo.src.main.schedulefind.ScheduleFindService
import com.makeus.jfive.famo.src.main.schedulefind.ScheduleFindView
import com.makeus.jfive.famo.src.main.schedulefind.models.*
import com.makeus.jfive.famo.src.wholeschedule.models.LatelyScheduleInquiryResponse
import com.makeus.jfive.famo.util.Constants


class ScheduleWholeAdapter(
    var wholeList: ArrayList<ScheduleWholeData>,
    myScheduleCategoryRecyclerView: IScheduleCategoryRecyclerView,
    val clickListener: (ScheduleWholeData) -> Unit) :
    RecyclerView.Adapter<ScheduleWholeAdapter.ScheduleWholeHolder>(), ScheduleFindView,
    ScheduleBookmarkView {

    var cnt = 1
    private var iScheduleCategoryRecyclerView: IScheduleCategoryRecyclerView? = null

    init {
        this.iScheduleCategoryRecyclerView = myScheduleCategoryRecyclerView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScheduleWholeHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recyclerview_schedule_find_whole_item, parent, false)

        return ScheduleWholeHolder(view)

    }

    @SuppressLint("RtlHardcoded")
    override fun onBindViewHolder(holder: ScheduleWholeHolder, position: Int) {

        holder.date.text = wholeList[position].date
        holder.name.text = wholeList[position].name
        holder.memo.text = wholeList[position].memo
        holder.border.setColorFilter(Color.parseColor(wholeList[position].color))

        if (wholeList[position].pick == -1) {
            holder.pick.setImageResource(R.drawable.schedule_find_inbookmark)
        } else {
            holder.pick.setImageResource(R.drawable.schedule_find_bookmark)
        }

        val deviceWidth =
            ApplicationClass.sSharedPreferences.getInt(Constants.DEVICE_WIDTH.toString(), 0)
        Log.d("TAG", "width: $deviceWidth")

        val width = deviceWidth - 150
        holder.itemView.layoutParams.width = width / 2
//
//        val params = LinearLayout.LayoutParams(
//            ViewGroup.LayoutParams.WRAP_CONTENT,
//            ViewGroup.LayoutParams.WRAP_CONTENT
//        )
//
//        val params2 = LinearLayout.LayoutParams(
//            ViewGroup.LayoutParams.WRAP_CONTENT,
//            ViewGroup.LayoutParams.WRAP_CONTENT
//        )
//
//        params.setMargins(40, 0,  2,40) // 왼쪽, 위, 오른쪽, 아래 순서입니다.
//        params2.setMargins(0, 0,  0,40) // 왼쪽, 위, 오른쪽, 아래 순서입니다.
//        if (cnt % 2 != 0){
//            Log.d("TAG", "onBindViewHolder: 마진테스트1 : ${holder.itemView.layoutParams.width}")
//
////            holder.itemView.layoutParams = params2
//            cnt++
//        }else{
//            Log.d("TAG", "onBindViewHolder: 마진테스트2 : ${holder.itemView.layoutParams.width}")
////            holder.itemView.layoutParams = params
//            cnt++
//        }

        holder.itemView.setOnClickListener {
            clickListener(wholeList[position])
        }


    }

    override fun getItemCount(): Int = wholeList.size

    inner class ScheduleWholeHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {

        //        val cardView = itemView.findViewById<CardView>(R.id.cardview)
        val pick = itemView.findViewById<ImageView>(R.id.recycler_whole_isboolean)
        val date = itemView.findViewById<TextView>(R.id.recycler_whole_times)
        val name = itemView.findViewById<TextView>(R.id.recycler_whole_title)
        val memo = itemView.findViewById<TextView>(R.id.recycler_whole_content)
        val border = itemView.findViewById<ImageView>(R.id.wholoe_schedule_border)
//        private lateinit var mCategoryRecyclerView: ICategoryRecyclerView

        init {
            //리스너연결
            pick.setOnClickListener(this)
//            shape.setOnClickListener(this)
//            mCategoryRecyclerView = categoryRecyclerView
        }

        override fun onClick(v: View?) {

            val bookmarkRequest = BookmarkRequest(
                scheduleID = wholeList[adapterPosition].id
            )
            Log.d("TAG", "onClick: ${wholeList[0].id}")

            when (v) {
                pick -> {
                    Log.d("TAG", "onClick확인: ${wholeList[adapterPosition].pick}")

                    if (wholeList[adapterPosition].pick == -1) {
                        pick.setImageResource(R.drawable.schedule_find_bookmark)
                        wholeList[adapterPosition].pick = 1
                    } else {
                        pick.setImageResource(R.drawable.schedule_find_inbookmark)
                        wholeList[adapterPosition].pick = -1
                    }

                    ScheduleFindService(this@ScheduleWholeAdapter).tryPostBookmark(bookmarkRequest)
                    notifyItemChanged(adapterPosition, null)
                }

            }
        }

    }

    override fun onGetWholeScheduleInquirySuccess(response: WholeScheduleInquiryResponse) {

    }

    override fun onGetWholeScheduleInquiryFail(message: String) {
    }

    override fun onPostBookmarkSuccess(response: BaseResponse) {

        when (response.code) {
            100 -> {
                Log.d("TAG", "onPostBookmarkSuccess: 즐겨찾기등록 성공")
            }
            else -> {
                Log.d("TAG", "onPostBookmarkSuccess: 즐겨찾기등록 실패 ${response.message.toString()}")
            }
        }

    }

    override fun onPostBookmarkFail(message: String) {
    }

    override fun onGetWholeScheduleCountSuccess(response: WholeScheduleCountResponse) {
    }

    override fun onGetWholeScheduleCountFailure(message: String) {
    }

    override fun onGetLatelyScheduleFindInquirySuccess(response: LatelyScheduleInquiryResponse) {
    }

    override fun onGetLatelySchedulefindInquiryFail(message: String) {
    }

    override fun onGetTodayRestScheduleSuccess(response: TodayRestScheduleResponse) {
    }

    override fun onGetTodayRestScheduleFail(message: String) {
    }

    override fun onGetScheduleSearchSuccess(response: ScheduleSearchResponse) {
    }

    override fun onGetScheduleSearchFail(message: String) {
    }

    override fun onGetScheduleBookmarkSuccess(response: ScheduleBookmarkResponse) {
    }

    override fun onGetScheduleBookmarkFail(message: String) {
    }

//    class LoadingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        private val progressBar: ProgressBar
//
//        init {
//            progressBar = itemView.findViewById(R.id.progressBar)
//        }
//    }

}