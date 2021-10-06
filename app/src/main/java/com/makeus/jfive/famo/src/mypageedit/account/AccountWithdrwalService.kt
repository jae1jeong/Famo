package com.makeus.jfive.famo.src.mypageedit.account

import com.makeus.jfive.famo.config.ApplicationClass
import com.makeus.jfive.famo.src.mypageedit.models.AccountWithdrawalResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AccountWithdrwalService(val view : AccountWithdrwalView) {

    //회원탈퇴
    fun tryPatchAccountWithdrawal(){
        val homeRetrofitInterface = ApplicationClass.sRetrofit.create(AccountRetrofitInterface::class.java)
        homeRetrofitInterface.patchAccountWithdrawal().enqueue(object :
            Callback<AccountWithdrawalResponse> {
            override fun onResponse(call: Call<AccountWithdrawalResponse>, response: Response<AccountWithdrawalResponse>) {
                view.onPatchAccountWithdrawalSuccess(response.body() as AccountWithdrawalResponse)
            }

            override fun onFailure(call: Call<AccountWithdrawalResponse>, t: Throwable) {
                view.onPatchAccountWithdrawalFail(t.message ?: "통신 오류")
//                Log.d("인증실패했니", "onGetProfileInqueryFail:  ")
            }
        })
    }
}