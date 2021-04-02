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
import com.softsquared.template.kotlin.config.ApplicationClass
import com.softsquared.template.kotlin.config.BaseResponse
import com.softsquared.template.kotlin.src.main.schedulefind.BookmarkService
import com.softsquared.template.kotlin.src.main.schedulefind.BookmarkView
import com.softsquared.template.kotlin.src.main.schedulefind.ScheduleFindService
import com.softsquared.template.kotlin.src.main.schedulefind.models.*
import com.softsquared.template.kotlin.util.Constants

class CategoryFilterAdapter(var categoryFilterList: ArrayList<CategoryFilterData>,
    val clickListener : (CategoryFilterData) -> Unit) : RecyclerView.Adapter<CategoryFilterAdapter.ScheduleWholeHolder>(),
    BookmarkView {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScheduleWholeHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recyclerview_schedule_find_whole_item, parent, false)

        return ScheduleWholeHolder(view)

    }

    override fun onBindViewHolder(holder: ScheduleWholeHolder, position: Int) {

        holder.date.text = categoryFilterList[position].scheduleDate
        holder.name.text = categoryFilterList[position].scheduleName
        holder.memo.text = categoryFilterList[position].scheduleMemo
        holder.border.setColorFilter(Color.parseColor(categoryFilterList[position].colorInfo))

        val deviceWidth =
            ApplicationClass.sSharedPreferences.getInt(Constants.DEVICE_WIDTH.toString(), 0)
        Log.d("TAG", "width: $deviceWidth")

        val width = deviceWidth - 120

        holder.itemView.layoutParams.width = width / 2

        if (categoryFilterList[position].schedulePick == -1){
            holder.pick.setImageResource(R.drawable.schedule_find_inbookmark)
        }else{
            holder.pick.setImageResource(R.drawable.schedule_find_bookmark)
        }

        holder.itemView.setOnClickListener {
            clickListener(categoryFilterList[position])
        }

    }

    override fun getItemCount(): Int = categoryFilterList.size

    inner class ScheduleWholeHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener{

        val pick = itemView.findViewById<ImageView>(R.id.recycler_whole_isboolean)
        val date = itemView.findViewById<TextView>(R.id.recycler_whole_times)
        val name = itemView.findViewById<TextView>(R.id.recycler_whole_title)
        val memo = itemView.findViewById<TextView>(R.id.recycler_whole_content)
        val border = itemView.findViewById<ImageView>(R.id.wholoe_schedule_border)

        init {
            //리스너연결
            pick.setOnClickListener(this)
        }

        override fun onClick(v: View?) {

            when(v){
                pick -> {
                    Log.d("TAG", "onClick확인: ${categoryFilterList[adapterPosition].schedulePick}")

                    val bookmarkRequest = BookmarkRequest(
                        scheduleID = categoryFilterList[position].scheduleID
                    )

                    if (categoryFilterList[position].schedulePick == -1){
                        pick.setImageResource(R.drawable.schedule_find_bookmark)
                        categoryFilterList[position].schedulePick = 1
                    }else{
                        pick.setImageResource(R.drawable.schedule_find_inbookmark)
                        categoryFilterList[position].schedulePick = -1
                    }

                    BookmarkService(this@CategoryFilterAdapter).tryPostBookmark(bookmarkRequest)
                    notifyDataSetChanged()
                }
            }

        }

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


}