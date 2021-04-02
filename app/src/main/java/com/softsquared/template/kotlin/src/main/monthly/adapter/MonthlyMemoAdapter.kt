package com.softsquared.template.kotlin.src.main.monthly.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.GradientDrawable
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.softsquared.template.kotlin.R
import com.softsquared.template.kotlin.databinding.ItemMonthlyMemoBinding
import com.softsquared.template.kotlin.src.main.today.models.MemoItem
import com.softsquared.template.kotlin.util.CategoryColorPicker

class MonthlyMemoAdapter(var memoList:MutableList<MemoItem>,private val context: Context,private val clickListener:(MemoItem)->Unit,private val deleteListener:(MemoItem)->Unit,private val shareListener:(MemoItem)->Unit):RecyclerView.Adapter<MonthlyMemoAdapter.MonthlyMemoViewHolder>(){
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
        holder.binding.monthlyItemBtnMemoOther.setOnClickListener {
            val popUp = PopupMenu(context,holder.binding.monthlyItemBtnMemoOther)
            popUp.inflate(R.menu.monthly_other_btn)
            popUp.setOnMenuItemClickListener(object:PopupMenu.OnMenuItemClickListener{
                override fun onMenuItemClick(item: MenuItem?): Boolean {
                    when(item?.itemId){
                        R.id.other_btn_share->{
                            shareListener(memo)
                        }
                        R.id.other_btn_delete->{
                            deleteListener(memo)
                        }
                    }
                    return true
                }

            })
            popUp.show()
        }
        holder.itemView.setOnClickListener {
            clickListener(memo)
        }
        if(memo.description == ""){
            val params = holder.itemView.layoutParams
            params.height = 350
            holder.itemView.layoutParams = params
            holder.itemView.requestLayout()
        }

        val categoryColor = CategoryColorPicker.setCategoryColor(memo.colorState)
        val shape = GradientDrawable()
        Log.d("TAG", "setCategoryColorRadius: $categoryColor")
        shape.cornerRadius = 180F
        shape.setColorFilter(Color.parseColor(categoryColor), PorterDuff.Mode.SRC_IN)
        holder.binding.itemMonthlyCategoryColor.background = shape

    }


    fun setNewMemoList(newMemoList:ArrayList<MemoItem>){
        this.memoList = newMemoList
        notifyDataSetChanged()
    }

    fun swapItems(fromPosition:Int, toPosition:Int){
        if (fromPosition != toPosition) {
            if (fromPosition < toPosition) {
                for (i in fromPosition until toPosition) {
                    var temp = memoList[i+1]
                    memoList[i + 1] = memoList[i]
                    memoList[i] = temp

                }
            } else {
                for (i in toPosition..fromPosition-1) {
                    var temp = memoList[fromPosition-i]
                    memoList[fromPosition-i] = memoList[fromPosition-i-1]
                    memoList[fromPosition-i-1] = temp
                }
            }

            notifyItemMoved(fromPosition, toPosition)
        }
    }


    override fun getItemCount(): Int  = memoList.size


}