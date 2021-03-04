package com.softsquared.template.kotlin.src.main.today.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.softsquared.template.kotlin.R
import com.softsquared.template.kotlin.databinding.ItemTodayMemoBinding
import com.softsquared.template.kotlin.src.main.today.models.MemoItem

class MemoAdapter(var memoList:MutableList<MemoItem>,private val context: Context):RecyclerView.Adapter<MemoAdapter.MemoViewHolder>() {
    class MemoViewHolder(val binding:ItemTodayMemoBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MemoAdapter.MemoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_today_memo,parent,false)
        val viewHolder = MemoViewHolder(ItemTodayMemoBinding.bind(view))
        view.setOnClickListener {

        }
        return viewHolder
    }

    override fun onBindViewHolder(holder: MemoAdapter.MemoViewHolder, position: Int) {
        holder.binding.todayMemo = memoList[position]
        val memo = memoList[position]

        // 체크 버튼 여부에 따라 백그라운드 변경
        changeCheckBtnBackground(memo.isChecked,holder.binding.todayItemBtnMemoCheck)
        // 체크 버튼 리스너
        holder.binding.todayItemBtnMemoCheck.setOnClickListener {
            if(memo.isChecked){
                changeCheckBtnBackground(memo.isChecked,holder.binding.todayItemBtnMemoCheck)
                memo.isChecked = !memo.isChecked
            }else{
                changeCheckBtnBackground(memo.isChecked,holder.binding.todayItemBtnMemoCheck)
                memo.isChecked = !memo.isChecked
            }
        }

    }

    private fun changeCheckBtnBackground(isChecked:Boolean,imageView: ImageView){
        if(isChecked){
            imageView.setBackgroundResource(R.drawable.background_check_button_passive)
            imageView.setColorFilter(context.resources.getColor(R.color.button_gray))
        }
        else{
            imageView.setBackgroundResource(R.drawable.background_btn_acttive)
            imageView.setColorFilter(context.resources.getColor(R.color.white))
        }
    }

    override fun getItemCount(): Int = memoList.size

}