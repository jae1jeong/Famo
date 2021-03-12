package com.softsquared.template.kotlin.src.main.addmemo

import android.graphics.Point
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import com.andrefrsousa.superbottomsheet.SuperBottomSheetFragment
import com.google.android.material.tabs.TabLayout
import com.softsquared.template.kotlin.R
import com.softsquared.template.kotlin.config.BaseResponse
import com.softsquared.template.kotlin.src.main.AddMemoService
import com.softsquared.template.kotlin.src.main.AddMemoView
import com.softsquared.template.kotlin.src.main.MainActivity
import com.softsquared.template.kotlin.src.main.models.PostTodayRequestAddMemo

class AddMemoFragment: SuperBottomSheetFragment(), AddMemoView {
    private var bottomSheetLinear:LinearLayout ?= null
    private var bottomSheetDialogHeight = 0
    private var saveBtn :Button ?= null
    private var memoTitle:EditText ?= null
    private var memoContent:EditText ?=null
    private var okBtn : Button ?= null

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


        return inflater.inflate(R.layout.fragment_bottom_sheet_dialog_add_memo,container,false)
    }

    override fun getExpandedHeight(): Int {
        Log.d("AddMemoFragment", "$bottomSheetDialogHeight:")
        if(bottomSheetDialogHeight == 0){
            return super.getExpandedHeight()
        }else{
            return bottomSheetDialogHeight
        }
    }

    override fun onPostAddMemoSuccess(response: BaseResponse) {
        if(response.isSuccess){
            when(response.code){
                100->{
                    Toast.makeText(context,response.message,Toast.LENGTH_SHORT)
                }
                else->{
                    Toast.makeText(context,response.message,Toast.LENGTH_SHORT)
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        okBtn = okBtn?.findViewById(R.id.add_memo_dialog_btn_ok)
        Log.d("tag", "onViewCreated: $okBtn")
        saveBtn?.setOnClickListener {
            memoTitle = memoTitle?.findViewById(R.id.add_memo_edit_title)
            memoContent = memoContent?.findViewById(R.id.add_memo_edit_content)
            Log.d("tag", "onViewCreated: $memoTitle $memoContent")
            AddMemoService(this).tryPostAddMemo(PostTodayRequestAddMemo(memoTitle?.text.toString(),memoContent?.text.toString(),1))
        }
        okBtn?.setOnClickListener {
            memoTitle = memoTitle?.findViewById(R.id.add_memo_edit_title)
            memoContent = memoContent?.findViewById(R.id.add_memo_edit_content)
            Log.d("tag", "onViewCreated: $memoTitle $memoContent")
            AddMemoService(this).tryPostAddMemo(PostTodayRequestAddMemo(memoTitle?.text.toString(),memoContent?.text.toString(),1))
        }

    }
    override fun onPostAddMemoFailure(message: String) {
        Log.d("AddMemoFragment", "onPostAddMemoFailure: $message")
    }

}