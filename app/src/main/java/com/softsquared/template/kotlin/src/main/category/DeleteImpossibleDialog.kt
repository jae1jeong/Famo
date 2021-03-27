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


class DeleteImpossibleDialog(context: Context) : Dialog(context) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.category_delete_impossible_dialog);

        val check : Button = findViewById(R.id.delete_impossible_check)

        check.setOnClickListener {
            dismiss()
        }

    }
}