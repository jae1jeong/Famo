package com.softsquared.template.kotlin.src.main.addmemo

import android.graphics.Point
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.andrefrsousa.superbottomsheet.SuperBottomSheetFragment
import com.google.android.material.tabs.TabLayout
import com.softsquared.template.kotlin.R
import com.softsquared.template.kotlin.config.BaseFragment
import com.softsquared.template.kotlin.src.main.MainActivity

class AddMemoFragment:SuperBottomSheetFragment() {
    private var bottomSheetLinear:LinearLayout ?= null
    private var bottomSheetDialogHeight = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        // 디바이스 화면 높이 구하기
        val display = activity!!.windowManager.defaultDisplay
        val size = Point()
        display.getRealSize(size)
        val deviceHeight = size.y
        // 탭 레이아웃 높이와 디바이스 화면 높이 빼기
        val tabLayoutHeight = (activity as MainActivity).findViewById<TabLayout>(R.id.main_tab_layout).height
        bottomSheetDialogHeight = deviceHeight - tabLayoutHeight
        getExpandedHeight()
        return inflater.inflate(R.layout.bottom_sheet_dialog_add_memo,container,false)
    }

    override fun getExpandedHeight(): Int {
        Log.d("AddMemoFragment", "$bottomSheetDialogHeight:")
        if(bottomSheetDialogHeight == 0){
            return super.getExpandedHeight()
        }else{
            return bottomSheetDialogHeight
        }
    }

}