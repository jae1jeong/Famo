package com.softsquared.template.kotlin.src.main.category

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import com.softsquared.template.kotlin.R
import com.softsquared.template.kotlin.config.ApplicationClass
import com.softsquared.template.kotlin.config.BaseResponse
import com.softsquared.template.kotlin.src.auth.loginInformation.LoginInformation
import com.softsquared.template.kotlin.src.main.category.adapter.ScheduleCategoryEditAdapter
import com.softsquared.template.kotlin.src.main.schedulefind.SchedulefindFilterBottomDialogFragment
import com.softsquared.template.kotlin.src.mypage.models.MyPageResponse
import com.softsquared.template.kotlin.src.mypageedit.MyPageEditView
import com.softsquared.template.kotlin.src.mypageedit.models.MyPageCommentsResponse
import kotlinx.android.synthetic.main.category_delete_dialog.*
import kotlinx.android.synthetic.main.fragment_schedule_find_filter_bottom_dialog.*
import kotlinx.android.synthetic.main.fragment_schedule_find_filter_bottom_dialog.filter_btn_completion
import kotlinx.android.synthetic.main.fragment_schedule_find_filter_bottom_dialog.filter_btn_remain


class DeleteDialog(context: Context) : Dialog(context), View.OnClickListener {


    lateinit var mDeleteButtonClickListener: deleteButtonClickListener

    interface deleteButtonClickListener {
        fun onDialogButtonClick(view: View?)
    }

    fun setOnDialogButtonClickListener(mListener: deleteButtonClickListener) {
        mDeleteButtonClickListener = mListener
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.category_delete_dialog);

        val delete : Button = findViewById(R.id.delete_check)
        val cancel : Button = findViewById(R.id.delete_cancel)

        delete.setOnClickListener(this)
        cancel.setOnClickListener(this)



    }

    override fun onClick(v: View?) {
        mDeleteButtonClickListener.onDialogButtonClick(v!!)
    }

}