package com.makeus.jfive.famo.src.mypageedit.logout

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import com.makeus.jfive.famo.R
import com.makeus.jfive.famo.config.ApplicationClass
import com.makeus.jfive.famo.config.BaseResponse
//import com.softsquared.template.kotlin.src.auth.login.LoginActivity
import com.makeus.jfive.famo.src.auth.loginInformation.LoginInformation
import com.makeus.jfive.famo.src.mypageedit.models.MyPageCommentsResponse
import com.makeus.jfive.famo.src.mypage.models.MyPageResponse
import com.makeus.jfive.famo.src.mypageedit.MyPageEditView
import com.makeus.jfive.famo.src.mypageedit.models.SetProfileImageResponse


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