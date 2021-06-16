package com.softsquared.template.kotlin.util

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Rect
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MovieItemDecoration(
    private val size10: Int,
    private val size5: Int
) :
    RecyclerView.ItemDecoration() {

//    fun MovieItemDecoration(context: Context?) {
//        size10 = dpToPx(context!!, 10)
//        size5 = dpToPx(context, 5)
//    }

//    constructor(context: Context?) : this() {
//        size10 = dpToPx(context!!, 10)
//        size5 = dpToPx(context, 5)
//    }

    @SuppressLint("RtlHardcoded")
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        val position = parent.getChildAdapterPosition(view)
        val itemCount = state.itemCount

        //상하 설정
        if (position == 0 || position == 1) {
            // 첫번 째 줄 아이템
//            outRect.top = size10
//            outRect.bottom = size10
            outRect.top = size5
            outRect.bottom = size5
        } else {
//            outRect.bottom = size10
            outRect.top = size5
            outRect.bottom = size5
        }

        // spanIndex = 0 -> 왼쪽
        // spanIndex = 1 -> 오른쪽


        // spanIndex = 0 -> 왼쪽
        // spanIndex = 1 -> 오른쪽
        val lp = view.layoutParams as GridLayoutManager.LayoutParams
        val spanIndex = lp.spanIndex

        if (spanIndex == 0) {
            //왼쪽 아이템
//            outRect.left = size10
//            outRect.right = size10
        } else if (spanIndex == 1) {
            //오른쪽 아이템
            outRect.left = size5
            outRect.right = size10

//            outRect.right = Gravity.RIGHT

        }

//        outRect.right = size10
//        outRect.left = size10
    }

    // dp -> pixel 단위로 변경
    fun dpToPx(context: Context, dp: Int): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp.toFloat(),
            context.resources.displayMetrics
        ).toInt()
    }
}