package com.makeus.jfive.famo.src.presentation.today

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.makeus.jfive.famo.R
import com.makeus.jfive.famo.databinding.ItemTodayMemoBinding
import com.makeus.jfive.famo.src.domain.model.today.TodayMemo
import com.makeus.jfive.famo.util.CategoryColorPicker
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.subjects.PublishSubject
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList


class MemoAdapter constructor(private val context: Context)
    :RecyclerView.Adapter<MemoAdapter.MemoViewHolder>() {

    private val noDescItemHeight = context.resources.getDimension(R.dimen.today_memo_no_desc_height).toInt()
    private var memoList:ArrayList<TodayMemo> = arrayListOf()
    private val _clickPublishSubject:PublishSubject<TodayMemo> = PublishSubject.create()
    val clickPublishSubject:PublishSubject<TodayMemo> get() = _clickPublishSubject
    private val _checkPublishSubject:PublishSubject<TodayMemo> = PublishSubject.create()
    val checkPublishSubject:PublishSubject<TodayMemo> get() = _checkPublishSubject
    private val CLICK_DEBOUNCE_TIME = 500L

    inner class MemoViewHolder(val binding:ItemTodayMemoBinding):RecyclerView.ViewHolder(binding.root){

        fun getItemClickObserver(todayMemo:TodayMemo): Observable<TodayMemo>{
            return Observable.create {
                e ->
                binding.root.setOnClickListener {
                    e.onNext(todayMemo)
                }
            }
        }
        fun getItemCheckObserver(todayMemo: TodayMemo):Observable<TodayMemo>{
            return Observable.create { e ->
                binding.todayItemBtnMemoCheck.setOnClickListener {
                    e.onNext(todayMemo)
                    notifyItemChanged(bindingAdapterPosition)
                }
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MemoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_today_memo,parent,false)
        val viewHolder = MemoViewHolder(ItemTodayMemoBinding.bind(view))
        return viewHolder
    }

    override fun onBindViewHolder(holder: MemoViewHolder, position: Int) {
        holder.binding.todayMemo = memoList[position]
        val memo = memoList[position]

        holder.getItemClickObserver(memo)
            .debounce(CLICK_DEBOUNCE_TIME,TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(_clickPublishSubject)
        holder.getItemCheckObserver(memo)
            .debounce(CLICK_DEBOUNCE_TIME,TimeUnit.MILLISECONDS)
            .subscribe(_checkPublishSubject)

        // 체크 버튼 여부에 따라 백그라운드 변경
        // 체크 버튼 리스너
        if(memo.scheduleStatus){
            holder.binding.todayItemBtnMemoCheck.let {
                it.setBackgroundResource(R.drawable.background_btn_acttive)
                it.setColorFilter(context.resources.getColor(R.color.white))
            }
        }else{
              holder.binding.todayItemBtnMemoCheck.let {
                  it.setBackgroundResource(R.drawable.background_check_button_passive)
                  it.setColorFilter(context.resources.getColor(R.color.button_gray))
              }
        }


        // 본문 내용이 없을시
        if(memo.scheduleMemo == ""){
            val params = holder.itemView.layoutParams
            params.height = noDescItemHeight
            holder.itemView.layoutParams = params
        }
        CategoryColorPicker.setCategoryColorRadius(memo.colorInfo,holder.binding.todayTextCategoryColor)

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

    fun setNewMemoList(newMemoList:ArrayList<TodayMemo>){
        this.memoList = newMemoList
        notifyDataSetChanged()
    }

    fun removeMemo(position: Int){
        this.memoList.removeAt(position)
        notifyItemRemoved(position)
    }

    override fun getItemCount(): Int = memoList.size

    fun getMemoByPosition(pos:Int):TodayMemo = memoList[pos]



}