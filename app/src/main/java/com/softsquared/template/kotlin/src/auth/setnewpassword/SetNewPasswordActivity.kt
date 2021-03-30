package com.softsquared.template.kotlin.src.auth.setnewpassword

import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.softsquared.template.kotlin.config.BaseActivity
import com.softsquared.template.kotlin.config.BaseResponse
import com.softsquared.template.kotlin.databinding.ActivitySetNewPasswordBinding
import com.softsquared.template.kotlin.src.auth.login.LoginActivity
import com.softsquared.template.kotlin.src.auth.setnewpassword.models.PostSetNewPasswordRequest
import com.softsquared.template.kotlin.util.Constants

class SetNewPasswordActivity:BaseActivity<ActivitySetNewPasswordBinding>(ActivitySetNewPasswordBinding::inflate),SetNewPasswordView {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setSupportActionBar(binding.setPasswordToolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val optAuthToken = intent.getStringExtra(Constants.OTP_TOKEN)

        val id = intent.getStringExtra("id")
        Log.d("TAG", "SetNewPasswordActivity id : $id")

        binding.setPasswordBtn.setOnClickListener {
            if(binding.setPasswordEditPassword.text.length == 0 || binding.setPasswordEditRepassword.text.length == 0){
                Log.d("TAG", "onCreate: 1 ")
                showCustomToast("비밀번호와 비밀번호 확인을 입력해주세요.")
            }else{
                if(binding.setPasswordEditRepassword.text.toString() != binding.setPasswordEditPassword.text.toString()){
                    showCustomToast("비밀번호와 비밀번호 확인 값이 같지 않습니다.")
                    Log.d("TAG", "onCreate: 2 ")
                }else{
                    if(optAuthToken == null){
                        showCustomToast("잘못된 요청입니다.")
                    }else{
                        showLoadingDialog(this)
                        SetNewPasswordService(this).trySetNewPassword(optAuthToken, PostSetNewPasswordRequest(id!!,binding.setPasswordEditRepassword.text.toString()))
                    }
                }
            }
        }
    }

    override fun onPostSetNewPasswordSuccess(response: BaseResponse) {
        if(response.isSuccess && response.code == 100){
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            showCustomToast(response.message.toString())
        }
        else{
            showCustomToast(response.message.toString())
        }
        dismissLoadingDialog()
    }

    override fun onPostSetNewPasswordFailure(message: String) {
        dismissLoadingDialog()
        showCustomToast(message)
    }
}