package com.softsquared.template.kotlin.src.searchhistories.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.softsquared.template.kotlin.R
import com.softsquared.template.kotlin.config.BaseResponse
import com.softsquared.template.kotlin.src.searchhistories.SearchHistoriesService
import com.softsquared.template.kotlin.src.searchhistories.SearchHistoriesView
import com.softsquared.template.kotlin.src.searchhistories.models.SearchHistoriesResponse
import com.softsquared.template.kotlin.src.searchhistories.models.SearchListData
import com.softsquared.template.kotlin.util.Constants

class ScheduleSearchListAdapter(var searchList: ArrayList<SearchListData>) :
    RecyclerView.Adapter<ScheduleSearchListAdapter.SearchListHolder>(),
    SearchHistoriesView{


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchListHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recyclerview_schedule_search_list_item, parent, false)
        view.setOnClickListener {

        }
        return SearchListHolder(view)
    }

    override fun onBindViewHolder(holder: SearchListHolder, position: Int) {

        holder.text.text = searchList[position].text

    }

    override fun getItemCount(): Int = searchList.size

    fun removeItem(position: Int){
        searchList.removeAt(position)
    }

    inner class SearchListHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener{

        val text = itemView.findViewById<TextView>(R.id.item_search_content)
        val delete = itemView.findViewById<ImageView>(R.id.search_history_delete)

        init {
            delete.setOnClickListener(this)
        }

        override fun onClick(v: View?) {

            when(v){
                delete -> {
                    Log.d("TAG", "onClick: 검색기록삭제버튼클릭")


                    Log.d("TAG", "onClick1: $adapterPosition")
                    Constants.SEARCH_CHECK = false
                    SearchHistoriesService(this@ScheduleSearchListAdapter)
                        .tryDeleteSearchHistories(searchList[adapterPosition].text)
                    removeItem(adapterPosition)
                    notifyDataSetChanged()

                }
            }
        }

    }

    override fun onGetSearchHistoriesSuccess(response: SearchHistoriesResponse) {

        when(response.code){
            100 -> {

            }
            else -> {

            }
        }
    }

    override fun onGetSearchHistoriesFail(message: String) {
    }

    override fun onDeleteSearchHistoriesSuccess(response: BaseResponse) {

        when(response.code){
            100 -> {
                Log.d("TAG", "onDeleteSearchHistoriesSuccess: 검색기록삭제성공")
            }
            else -> {
                Log.d("TAG", "onDeleteSearchHistoriesSuccess: 검색기록삭제실패 ${response.message}")
            }
        }
    }

    override fun onDeleteSearchHistoriesFail(message: String) {
    }

}