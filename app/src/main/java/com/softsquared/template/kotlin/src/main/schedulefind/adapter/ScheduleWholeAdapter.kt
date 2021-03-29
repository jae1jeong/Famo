package com.softsquared.template.kotlin.src.main.schedulefind.adapter

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.softsquared.template.kotlin.R
import com.softsquared.template.kotlin.config.BaseResponse
import com.softsquared.template.kotlin.src.main.schedulefind.ScheduleFindService
import com.softsquared.template.kotlin.src.main.schedulefind.ScheduleFindView
import com.softsquared.template.kotlin.src.main.schedulefind.models.*
import com.softsquared.template.kotlin.src.wholeschedule.models.LatelyScheduleInquiryResponse

class ScheduleWholeAdapter(var wholeList: ArrayList<ScheduleWholeData>,
                           val clickListener:(ScheduleWholeData)->Unit) :
    RecyclerView.Adapter<ScheduleWholeAdapter.ScheduleWholeHolder>(),ScheduleFindView {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScheduleWholeHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recyclerview_schedule_find_whole_item, parent, false)

        return ScheduleWholeHolder(view)

    }

    override fun onBindViewHolder(holder: ScheduleWholeHolder, position: Int) {

        holder.date.text = wholeList[position].date
        holder.pick.setImageResource(wholeList[position].pick)
        holder.name.text = wholeList[position].name
        holder.memo.text = wholeList[position].memo
        holder.border.setColorFilter(Color.parseColor(wholeList[position].color))

        holder.itemView.setOnClickListener {
            clickListener(wholeList[position])
        }

    }

    override fun getItemCount(): Int = wholeList.size

    inner class ScheduleWholeHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
         View.OnClickListener{

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

            when(v){
                pick -> {
                    //즐겨찾기 안되있으면 별표시
                    if (wholeList[adapterPosition].pick == 2131165412) {
                        pick.setImageResource(R.drawable.schedule_find_bookmark)
                        wholeList[adapterPosition].pick = 2131165416
                    } else {
                        pick.setImageResource(R.drawable.schedule_find_inbookmark)
                        wholeList[adapterPosition].pick = 2131165412
                    }
                    ScheduleFindService(this@ScheduleWholeAdapter).tryPostBookmark(bookmarkRequest)
                }

//                shape -> {
//                    val shape : LayerDrawable =  ContextCompat.getDrawable(get,
//                        R.drawable.left_stroke) as LayerDrawable
//
//                    val gradientDrawable = shape
//                        .findDrawableByLayerId(R.id.iv_shape) as GradientDrawable
//
//                    gradientDrawable.setColor(Color.BLUE) // change color
//                }

            }
        }

    }

    override fun onGetWholeScheduleInquirySuccess(response: WholeScheduleInquiryResponse) {

    }

    override fun onGetWholeScheduleInquiryFail(message: String) {
    }

    override fun onPostBookmarkSuccess(response: BaseResponse) {

        when(response.code){
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

//    class LoadingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        private val progressBar: ProgressBar
//
//        init {
//            progressBar = itemView.findViewById(R.id.progressBar)
//        }
//    }

}