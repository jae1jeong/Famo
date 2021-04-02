package com.softsquared.template.kotlin.src.mypageedit.logout

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import com.softsquared.template.kotlin.R
import com.softsquared.template.kotlin.config.ApplicationClass
import com.softsquared.template.kotlin.config.BaseResponse
//import com.softsquared.template.kotlin.src.auth.login.LoginActivity
import com.softsquared.template.kotlin.src.auth.loginInformation.LoginInformation
import com.softsquared.template.kotlin.src.mypageedit.models.MyPageCommentsResponse
import com.softsquared.template.kotlin.src.mypage.models.MyPageResponse
import com.softsquared.template.kotlin.src.mypageedit.MyPageEditView
import com.softsquared.template.kotlin.src.mypageedit.models.SetProfileImageResponse


class LogoutDialog(context:Context) : Dialog(context), MyPageEditView {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.logout_dialog);

        val logout : Button = findViewById(R.id.logout_check)
        val cancel : Button = findViewById(R.id.logout_cancel)

        logout.setOnClickListener {
            Log.d("TAG", "onPositiveClicked: 로그아웃버튼 눌림")
            val edit = ApplicationClass.sSharedPreferences.edit()
            edit.remove(ApplicationClass.X_ACCESS_TOKEN)
            edit.apply()

            val intent = Intent(context,LoginInformation::class.java)
            context.startActivity(intent)

        }

        cancel.setOnClickListener {
            Log.d("TAG", "onPositiveClicked: 취소버튼 눌림")
            dismiss()
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

    override fun onPostProfileImageSuccess(response: SetProfileImageResponse) {
    }

    override fun onPostProfileImageFailure(message: String) {
    }

    override fun onPatchProfileSuccess(response: BaseResponse) {
    }

    override fun onPatchProfileFailure(message: String) {
    }

}