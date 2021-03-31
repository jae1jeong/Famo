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
import com.softsquared.template.kotlin.src.main.schedulefind.models.CategoryScheduleInquiryData
import com.softsquared.template.kotlin.src.main.schedulefind.models.ScheduleWholeData

open class CategoryScheduleInquiryAdapter(var categoryList: ArrayList<CategoryScheduleInquiryData>,
    val clickListener : (CategoryScheduleInquiryData) -> Unit) :
    RecyclerView.Adapter<CategoryScheduleInquiryAdapter.ScheduleWholeHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScheduleWholeHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recyclerview_schedule_find_whole_item, parent, false)
        view.setOnClickListener {

        }
        return ScheduleWholeHolder(view)
    }

    override fun onBindViewHolder(holder: ScheduleWholeHolder, position: Int) {

        holder.date.text = categoryList[position].date.toString()
        holder.pick.setImageResource(categoryList[position].pick)
        holder.name.text = categoryList[position].name
        holder.memo.text = categoryList[position].memo
        holder.color.setColorFilter(Color.parseColor(categoryList[position].color))

        holder.itemView.setOnClickListener {
            clickListener(categoryList[position])
        }

    }

    override fun getItemCount(): Int = categoryList!!.size

    class ScheduleWholeHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
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

            var bookMarkCnt = 1

            when(v){
//                pick -> {
//                    if (bookMarkCnt % 2 != 0) {
//                        pick.setImageResource(R.drawable.schedule_find_bookmark)
//                        Log.d("TAG", "pick: 클릭")
//                    } else {
//                        pick.setImageResource(R.drawable.schedule_find_inbookmark)
//                        Log.d("TAG", "pick: X")
//                    }
//                    bookMarkCnt++
//                }
            }
        }

    }

//    class LoadingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        private val progressBar: ProgressBar
//
//        init {
//            progressBar = itemView.findViewById(R.id.progressBar)
//        }
//    }

}