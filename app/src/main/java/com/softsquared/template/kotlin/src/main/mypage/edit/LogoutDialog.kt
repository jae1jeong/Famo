package com.softsquared.template.kotlin.src.main.mypage.edit

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.Button
import com.softsquared.template.kotlin.R
import com.softsquared.template.kotlin.config.BaseResponse
import com.softsquared.template.kotlin.src.main.mypage.models.MyPageCommentsResponse
import com.softsquared.template.kotlin.src.main.mypage.models.MyPageResponse


class LogoutDialog(context:Context) : Dialog(context),MyPageEditView {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.logout_dialog);

        val logout : Button = findViewById(R.id.logout_check)
        val cancel : Button = findViewById(R.id.logout_cancel)

        cancel.setOnClickListener {
            Log.d("TAG", "onPositiveClicked: 로그아웃버튼 눌림")
        }

        logout.setOnClickListener {
            Log.d("TAG", "onPositiveClicked: 취소버튼 눌림")
        }

    }

    override fun onGetMyPageCommentsSuccess(response: MyPageCommentsResponse) {
    }

    override fun onGetMyPageCommentsFail(message: String) {
    }

    override fun onGetMyPageSuccess(response: MyPageResponse) {
    }

    override fun onGetMyPageFail(message: String) {
    }

    override fun onPutMyPageUpdateSuccess(response: BaseResponse) {
    }

    override fun onPutMyPageUpdateFail(message: String) {
    }

}