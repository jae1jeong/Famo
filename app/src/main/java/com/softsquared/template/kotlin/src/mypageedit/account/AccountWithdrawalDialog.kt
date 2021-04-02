package com.softsquared.template.kotlin.src.mypageedit.account

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import com.kakao.sdk.common.KakaoSdk
import com.softsquared.template.kotlin.R
import com.softsquared.template.kotlin.src.auth.loginInformation.LoginInformation
import com.softsquared.template.kotlin.src.mypageedit.models.AccountWithdrawalResponse

class AccountWithdrawalDialog(context:Context) : Dialog(context)
    ,AccountWithdrwalView{

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.account_withdrawal_dialog);

        val save : Button = findViewById(R.id.account_check)
        val cancel : Button = findViewById(R.id.account_cancel)

        save.setOnClickListener {
            Log.d("TAG", "회원탈퇴성공")
            AccountWithdrwalService(this).tryPatchAccountWithdrawal()
        }

        cancel.setOnClickListener {
            Log.d("TAG", "아니오 눌림 ")
            dismiss()
        }

    }

    override fun onPatchAccountWithdrawalSuccess(response: AccountWithdrawalResponse) {

        when (response.code) {
            100 -> {
                Log.d("TAG", "onPatchAccountWithdrawalSuccess: 회원탈퇴 성공")
                val intent = Intent(context, LoginInformation::class.java)
                context.startActivity(intent)
            }
            else -> {
                Log.d("TAG", "onPatchAccountWithdrawalSuccess: 회원탈퇴 실패 ${response.message.toString()}")
            }
        }

    }

    override fun onPatchAccountWithdrawalFail(message: String) {
    }

}