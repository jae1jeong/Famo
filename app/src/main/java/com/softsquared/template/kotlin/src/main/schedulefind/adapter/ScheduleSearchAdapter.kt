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
import com.softsquared.template.kotlin.src.main.schedulefind.BookmarkService
import com.softsquared.template.kotlin.src.main.schedulefind.BookmarkView
import com.softsquared.template.kotlin.src.main.schedulefind.models.*
import com.softsquared.template.kotlin.util.Constants

class ScheduleSearchAdapter(
    var searchList: ArrayList<ScheduleSearchData>,
    val clickListener: (ScheduleSearchData) -> Unit) :
    RecyclerView.Adapter<ScheduleSearchAdapter.ScheduleSearchHolder>(), BookmarkView {

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
        holder.colorInfo.setColorFilter(Color.parseColor(searchList[position].colorInfo))

        if (searchList[position].schedulePick == -1) {
            holder.schedulePick.setImageResource(R.drawable.schedule_find_inbookmark)
        } else {
            holder.schedulePick.setImageResource(R.drawable.schedule_find_bookmark)
        }
//        holder.schedulePick.setImageResource(searchList[position].schedulePick)

        val searchWord =
            ApplicationClass.sSharedPreferences.getString(Constants.SEARCH_WROD_COLOR, null)
        val searchCnt = ApplicationClass.sSharedPreferences.getString(Constants.SEARCH_CNT, null)!!
        Log.d("TAG", "onBindViewHolder: $searchWord")


        try {
            //공백제거
            var name = holder.scheduleName.text
            name = name.replace("\\p{Z}".toRegex(), "")
            var nameForCnt = 0
            //for문 횟수설정
            if (searchWord!!.length == 1) {
                nameForCnt = name.length
            } else {
                nameForCnt = name.length - searchWord.length + 1
            }
            val changeNameColor = SpannableStringBuilder(name)

            //제목 색 변경
            for (i in 0 until nameForCnt) {
                if (searchWord.equals(name.substring(i, searchWord.length + i))) {
                    changeNameColor.setSpan(
                        ForegroundColorSpan(Color.parseColor("#ffae2a")),
                        i, i + searchWord.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                    holder.scheduleName.text = changeNameColor
                }
            }

            var memo = holder.scheduleMemo.text
            memo = memo.replace("\\p{Z}".toRegex(), "")
            var memoForCnt = 0

            if (searchWord!!.length == 1) {
                memoForCnt = memo.length
            } else {
                memoForCnt = memo.length - searchWord.length + 1
            }
            val changeMemoColor = SpannableStringBuilder(memo)

            //검색 내용 색 변경
            for (i in 0 until memoForCnt) {
                if (searchWord.equals(memo.substring(i, searchWord.length + i))) {
                    changeMemoColor.setSpan(
                        ForegroundColorSpan(Color.parseColor("#ffae2a")),
                        i, i + searchWord.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                    holder.scheduleMemo.text = changeMemoColor
                }
            }

        }catch (e : Exception){

        }


//        cnt++
//        if (cnt == Integer.parseInt(searchCnt)) {
//            val edit = ApplicationClass.sSharedPreferences.edit()
//            edit.remove(Constants.SEARCHWROD)
//            edit.remove(Constants.SEARCH_CNT)
//            edit.apply()
//            Log.d("TAG", "onBindViewHolder: 어댑터확인")
//        }

        holder.itemView.setOnClickListener {
            clickListener(searchList[position])
        }

    }

    override fun getItemCount(): Int = searchList.size

    inner class ScheduleSearchHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener{

        val scheduleName = itemView.findViewById<TextView>(R.id.recycler_whole_title)
        val scheduleMemo = itemView.findViewById<TextView>(R.id.recycler_whole_content)
        val scheduleDate = itemView.findViewById<TextView>(R.id.recycler_whole_times)
        val schedulePick = itemView.findViewById<ImageView>(R.id.recycler_whole_isboolean)
        val colorInfo = itemView.findViewById<ImageView>(R.id.wholoe_schedule_border)

        init {
            schedulePick.setOnClickListener(this)
        }

        override fun onClick(v: View?) {

            when(v){
                schedulePick -> {
                    Log.d("TAG", "onClick확인: ${searchList[adapterPosition].schedulePick}")

                    val bookmarkRequest = BookmarkRequest(
                        scheduleID = searchList[position].scheduleID
                    )

                    if (searchList[position].schedulePick == -1){
                        schedulePick.setImageResource(R.drawable.schedule_find_bookmark)
                        searchList[position].schedulePick = 1
                    }else{
                        schedulePick.setImageResource(R.drawable.schedule_find_inbookmark)
                        searchList[position].schedulePick = -1
                    }

                    BookmarkService(this@ScheduleSearchAdapter).tryPostBookmark(bookmarkRequest)
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

