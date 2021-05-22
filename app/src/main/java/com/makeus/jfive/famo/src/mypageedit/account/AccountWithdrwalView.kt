package com.makeus.jfive.famo.src.mypageedit.account

import com.makeus.jfive.famo.src.mypageedit.models.AccountWithdrawalResponse

interface AccountWithdrwalView {

    //회원탈퇴
    fun onPatchAccountWithdrawalSuccess(response: AccountWithdrawalResponse)
    fun onPatchAccountWithdrawalFail(message: String)
}