package com.softsquared.template.kotlin.src.mypageedit.account

import com.softsquared.template.kotlin.src.mypageedit.models.AccountWithdrawalResponse

interface AccountWithdrwalView {

    //회원탈퇴
    fun onPatchAccountWithdrawalSuccess(response: AccountWithdrawalResponse)
    fun onPatchAccountWithdrawalFail(message: String)
}