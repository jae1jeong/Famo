package com.softsquared.template.kotlin.src.main.today

import android.content.Context
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.softsquared.template.kotlin.src.main.today.adapter.MemoAdapter

class MemoDragHelper(adapter:MemoAdapter,context: Context,dragDirs:Int,swipeDirs:Int):ItemTouchHelper.SimpleCallback(dragDirs, swipeDirs) {

    val dragAdapter = adapter
    override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
        dragAdapter.swapItems(viewHolder.adapterPosition,target.adapterPosition)
        return true
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
    }


}