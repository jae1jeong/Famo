package com.softsquared.template.kotlin.src.main.today.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.softsquared.template.kotlin.R
import com.softsquared.template.kotlin.databinding.ItemTodayMemoBinding
import com.softsquared.template.kotlin.src.main.today.models.MemoItem

class MemoAdapter(var memoList:MutableList<MemoItem>):RecyclerView.Adapter<MemoAdapter.MemoViewHolder>() {
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

    }

    override fun getItemCount(): Int = memoList.size

}