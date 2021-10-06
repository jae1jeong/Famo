package com.makeus.jfive.famo.src.mypageedit.account

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import com.makeus.jfive.famo.R
import com.makeus.jfive.famo.src.auth.loginInformation.LoginInformation
import com.makeus.jfive.famo.src.mypageedit.models.AccountWithdrawalResponse

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