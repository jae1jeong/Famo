package com.softsquared.template.kotlin.src.main.monthly.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.softsquared.template.kotlin.R
import com.softsquared.template.kotlin.databinding.ItemMonthlyMemoBinding
import com.softsquared.template.kotlin.src.main.today.models.MemoItem
import com.softsquared.template.kotlin.util.CategoryColorPicker

class MonthlyMemoAdapter(var memoList:MutableList<MemoItem>,private val context: Context,private val clickListener:(MemoItem)->Unit):RecyclerView.Adapter<MonthlyMemoAdapter.MonthlyMemoViewHolder>(){
    class MonthlyMemoViewHolder(val binding:ItemMonthlyMemoBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MonthlyMemoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_monthly_memo,parent,false)
        val viewHolder = MonthlyMemoViewHolder(ItemMonthlyMemoBinding.bind(view))
        return viewHolder
    }

    @SuppressLint("ResourceType")
    override fun onBindViewHolder(holder: MonthlyMemoViewHolder, position: Int) {
        holder.binding.monthlyMemo = memoList[position]
        val memo = memoList[position]
        CategoryColorPicker.setCategoryColorRadius(memo.colorState,holder.binding.itemMonthlyCategoryColor)

    }

    fun setNewMemoList(newMemoList:ArrayList<MemoItem>){
        this.memoList = newMemoList
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int  = memoList.size


}