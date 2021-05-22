package com.makeus.jfive.famo.src.main.schedulefind.adapter

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.makeus.jfive.famo.R
import com.makeus.jfive.famo.config.BaseResponse
import com.makeus.jfive.famo.src.main.schedulefind.*
import com.makeus.jfive.famo.src.main.schedulefind.models.BookmarkRequest
import com.makeus.jfive.famo.src.main.schedulefind.models.CategoryScheduleInquiryData

open class CategoryScheduleInquiryAdapter(var categoryList: ArrayList<CategoryScheduleInquiryData>,
    val clickListener : (CategoryScheduleInquiryData) -> Unit) :
    RecyclerView.Adapter<CategoryScheduleInquiryAdapter.ScheduleWholeHolder>(),
    BookmarkView {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScheduleWholeHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recyclerview_schedule_find_whole_item, parent, false)
        view.setOnClickListener {

        }
        return ScheduleWholeHolder(view)
    }

    override fun onBindViewHolder(holder: ScheduleWholeHolder, position: Int) {

        holder.date.text = categoryList[position].date.toString()
        holder.name.text = categoryList[position].name
        holder.memo.text = categoryList[position].memo
        holder.color.setColorFilter(Color.parseColor(categoryList[position].color))

        if (categoryList[position].pick == -1){
            holder.pick.setImageResource(R.drawable.schedule_find_inbookmark)
        }else{
            holder.pick.setImageResource(R.drawable.schedule_find_bookmark)
        }

        holder.itemView.setOnClickListener {
            clickListener(categoryList[position])
        }

//        holder.pick.setOnClickListener {
//
//        }

    }

    override fun getItemCount(): Int = categoryList.size

    inner class ScheduleWholeHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
         View.OnClickListener{

        //        val cardView = itemView.findViewById<CardView>(R.id.cardview)
        val pick = itemView.findViewById<ImageView>(R.id.recycler_whole_isboolean)
        val date = itemView.findViewById<TextView>(R.id.recycler_whole_times)
        val name = itemView.findViewById<TextView>(R.id.recycler_whole_title)
        val memo = itemView.findViewById<TextView>(R.id.recycler_whole_content)
        val color = itemView.findViewById<ImageView>(R.id.wholoe_schedule_border)

        init {
            //리스너연결
            pick.setOnClickListener(this)
        }

        override fun onClick(v: View?) {

            when(v){
                pick -> {
                    Log.d("TAG", "onClick확인: ${categoryList[adapterPosition].pick}")

                    val bookmarkRequest = BookmarkRequest(
                        scheduleID = categoryList[adapterPosition].id
                    )

                    if (categoryList[adapterPosition].pick == -1){
                        pick.setImageResource(R.drawable.schedule_find_bookmark)
                        categoryList[adapterPosition].pick = 1
                    }else{
                        pick.setImageResource(R.drawable.schedule_find_inbookmark)
                        categoryList[adapterPosition].pick = -1
                    }

                    BookmarkService(this@CategoryScheduleInquiryAdapter).tryPostBookmark(bookmarkRequest)
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

//    class LoadingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        private val progressBar: ProgressBar
//
//        init {
//            progressBar = itemView.findViewById(R.id.progressBar)
//        }
//    }

}