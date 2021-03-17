package com.softsquared.template.kotlin.src.auth.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.softsquared.template.kotlin.config.ApplicationClass
import com.softsquared.template.kotlin.config.BaseActivity
import com.softsquared.template.kotlin.databinding.ActivityLoginBinding
import com.softsquared.template.kotlin.src.auth.login.models.LoginResponse
import com.softsquared.template.kotlin.src.auth.login.models.PostRequestLogin
import com.softsquared.template.kotlin.src.auth.signup.models.SignUpResponse
import com.softsquared.template.kotlin.src.main.MainActivity
import com.softsquared.template.kotlin.util.Constants

class LoginActivity:BaseActivity<ActivityLoginBinding>(ActivityLoginBinding::inflate),LoginView {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // toolbar
        setSupportActionBar(binding.loginToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        // 로그인 버튼
        binding.loginBtnLogin.setOnClickListener {
            if(binding.loginEditId.text.toString() != "" && binding.loginEditPassword.text.toString() != ""){
                showLoadingDialog(this)
                LoginService(this).tryPostLogin(PostRequestLogin(binding.loginEditId.text.toString(),binding.loginEditPassword.text.toString()))
            }
        }
    }

    override fun onPostLoginSuccess(response: LoginResponse) {
        if(response.isSuccess){
            when(response.code){
                100 -> {
                    val edit = ApplicationClass.sSharedPreferences.edit()
                    // jwt 삽입
                    edit.putString(ApplicationClass.X_ACCESS_TOKEN, response.jwt)
                    edit.putInt(Constants.USER_ID, response.userID)
                    edit.putString(Constants.USER_NICKNAME, response.nickname)
                    edit.commit()
                    dismissLoadingDialog()
                    startActivity(Intent(this, MainActivity::class.java))
                    showCustomToast("로그인이 완료되었습니다!")
                    Log.d("TAG", "onPostLoginSuccess: ${ApplicationClass.X_ACCESS_TOKEN}")
                    finish()
                }
                else ->{
                    dismissLoadingDialog()
                    showCustomToast(response.message.toString())
                }
            }
        }else{
            dismissLoadingDialog()
            showCustomToast(response.message.toString())
        }
    }

    override fun onPostLoginFailure(message: String) {
        dismissLoadingDialog()
        showCustomToast(message)
    }

}