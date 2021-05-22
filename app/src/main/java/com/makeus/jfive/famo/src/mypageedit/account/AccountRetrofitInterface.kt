package com.makeus.jfive.famo.src.mypageedit.account

import com.makeus.jfive.famo.src.mypageedit.models.AccountWithdrawalResponse
import retrofit2.Call
import retrofit2.http.PATCH

interface AccountRetrofitInterface {

    //회원탈퇴
    @PATCH("users/account")
    fun patchAccountWithdrawal() : Call<AccountWithdrawalResponse>
}