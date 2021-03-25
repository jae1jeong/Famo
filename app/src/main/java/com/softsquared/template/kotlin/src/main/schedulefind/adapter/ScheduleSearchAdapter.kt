package com.softsquared.template.kotlin.src.main.schedulefind.adapter

import android.graphics.Color
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
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
import com.softsquared.template.kotlin.src.main.schedulefind.ScheduleFindService
import com.softsquared.template.kotlin.src.main.schedulefind.ScheduleFindView
import com.softsquared.template.kotlin.src.main.schedulefind.models.*
import com.softsquared.template.kotlin.src.wholeschedule.models.LatelyScheduleInquiryResponse
import com.softsquared.template.kotlin.util.Constants

class ScheduleSearchAdapter(var searchList: ArrayList<ScheduleSearchData>) :
    RecyclerView.Adapter<ScheduleSearchAdapter.ScheduleSearchHolder>() {

    var cnt = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScheduleSearchHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recyclerview_schedule_find_whole_item, parent, false)

        return ScheduleSearchHolder(view)

    }

    override fun onBindViewHolder(holder: ScheduleSearchHolder, position: Int) {

        holder.scheduleName.text = searchList[position].scheduleName
        holder.scheduleMemo.text = searchList[position].scheduleMemo
        holder.scheduleDate.text = searchList[position].scheduleDate
        holder.schedulePick.setImageResource(searchList[position].schedulePick)
        holder.colorInfo.setColorFilter(Color.parseColor(searchList[position].colorInfo))

        val searchWord = ApplicationClass.sSharedPreferences.getString(Constants.SEARCHWROD, null)
        val searchCnt = ApplicationClass.sSharedPreferences.getString(Constants.SEARCH_CNT, null)!!
        Log.d("TAG", "onBindViewHolder: $searchWord")
        //검색단어 색 변경
        val name = holder.scheduleName.text
        val changeNameColor = SpannableStringBuilder(name)

        //검색제목 색 변경
        for (i in name.indices) {

            if (searchWord.equals(name.substring(i, searchWord!!.length + i))) {
                changeNameColor.setSpan(
                    ForegroundColorSpan(Color.parseColor("#ffae2a")),
                    0, searchWord.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                holder.scheduleName.text = changeNameColor
            }
        }

        val memo = holder.scheduleMemo.text
        val changeMemoColor = SpannableStringBuilder(memo)
        //검색 내용 색 변경
        for (i in memo.indices) {

            if (searchWord.equals(memo.substring(i, searchWord!!.length + i))) {
                changeMemoColor.setSpan(
                    ForegroundColorSpan(Color.parseColor("#ffae2a")),
                    0, searchWord.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                holder.scheduleMemo.text = changeMemoColor
            }

        }

        cnt++
        if (cnt == Integer.parseInt(searchCnt)) {
            val edit = ApplicationClass.sSharedPreferences.edit()
            edit.remove(Constants.SEARCHWROD)
            edit.remove(Constants.SEARCH_CNT)
            edit.apply()
            Log.d("TAG", "onBindViewHolder: 어댑터확인")
        }

    }

    override fun getItemCount(): Int = searchList.size

    inner class ScheduleSearchHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val scheduleName = itemView.findViewById<TextView>(R.id.recycler_whole_title)
        val scheduleMemo = itemView.findViewById<TextView>(R.id.recycler_whole_content)
        val scheduleDate = itemView.findViewById<TextView>(R.id.recycler_whole_times)
        val schedulePick = itemView.findViewById<ImageView>(R.id.recycler_whole_isboolean)
        val colorInfo = itemView.findViewById<ImageView>(R.id.wholoe_schedule_border)

    }


}