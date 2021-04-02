package com.softsquared.template.kotlin.src.main.today.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.softsquared.template.kotlin.R
import com.softsquared.template.kotlin.databinding.ItemTodayMemoBinding
import com.softsquared.template.kotlin.src.main.today.models.MemoItem
import com.softsquared.template.kotlin.util.CategoryColorPicker
import java.util.ArrayList

class MemoAdapter(var memoList:MutableList<MemoItem>,private val context: Context,private val clickListener:(MemoItem)->Unit,
                  private val checkListener:(MemoItem)->Unit,private val noItemListener:()->Unit)
    :RecyclerView.Adapter<MemoAdapter.MemoViewHolder>() {
    class MemoViewHolder(val binding:ItemTodayMemoBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MemoAdapter.MemoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_today_memo,parent,false)
        val viewHolder = MemoViewHolder(ItemTodayMemoBinding.bind(view))
        return viewHolder
    }

    override fun onBindViewHolder(holder: MemoAdapter.MemoViewHolder, position: Int) {
        holder.binding.todayMemo = memoList[position]
        val memo = memoList[position]

        holder.binding.root.setOnClickListener {
            clickListener(memo)
        }
        // 체크 버튼 여부에 따라 백그라운드 변경
        changeCheckBtnBackground(memo.isChecked,holder.binding.todayItemBtnMemoCheck)
        // 체크 버튼 리스너
        holder.binding.todayItemBtnMemoCheck.setOnClickListener {
            if(memo.isChecked){
                checkListener(memo)
                memo.isChecked = !memo.isChecked
                changeCheckBtnBackground(memo.isChecked,holder.binding.todayItemBtnMemoCheck)

            }else{
                checkListener(memo)
                memo.isChecked = !memo.isChecked
                changeCheckBtnBackground(memo.isChecked,holder.binding.todayItemBtnMemoCheck)
            }
            notifyItemChanged(position)

        }
        // 본문 내용이 없을시
        if(memo.description == ""){
            val params = holder.itemView.layoutParams
            params.height = 350
            holder.itemView.layoutParams = params
        }
        CategoryColorPicker.setCategoryColorRadius(memo.colorState,holder.binding.todayTextCategoryColor)

    }

    private fun changeCheckBtnBackground(isChecked:Boolean,imageView: ImageView){
        if(isChecked){
            imageView.setBackgroundResource(R.drawable.background_btn_acttive)
            imageView.setColorFilter(context.resources.getColor(R.color.white))
        }
        else{
            imageView.setBackgroundResource(R.drawable.background_check_button_passive)
            imageView.setColorFilter(context.resources.getColor(R.color.button_gray))
        }
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

    fun setNewMemoList(newMemoList:ArrayList<MemoItem>){
        this.memoList = newMemoList
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return memoList.size
    }


}