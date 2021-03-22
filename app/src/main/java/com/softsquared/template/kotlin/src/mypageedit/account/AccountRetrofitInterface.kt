package com.softsquared.template.kotlin.src.mypageedit.account

import com.softsquared.template.kotlin.src.mypageedit.models.AccountWithdrawalResponse
import retrofit2.Call
import retrofit2.http.PATCH

interface AccountRetrofitInterface {

    //회원탈퇴
    @PATCH("users/account")
    fun patchAccountWithdrawal() : Call<AccountWithdrawalResponse>
}