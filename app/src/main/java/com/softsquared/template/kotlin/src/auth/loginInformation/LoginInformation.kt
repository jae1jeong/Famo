package com.softsquared.template.kotlin.src.auth.loginInformation

import android.content.Intent
import android.os.Bundle
import com.softsquared.template.kotlin.config.BaseActivity
import com.softsquared.template.kotlin.databinding.ActivityLoginInfoBinding
import com.softsquared.template.kotlin.src.auth.login.LoginActivity
import com.softsquared.template.kotlin.src.auth.login.LoginView
import com.softsquared.template.kotlin.src.auth.signup.SignUpActivity
import com.softsquared.template.kotlin.src.main.MainActivity

class LoginInformation:BaseActivity<ActivityLoginInfoBinding>(ActivityLoginInfoBinding::inflate) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 로그인 버튼
        binding.loginInfoBtnLogin.setOnClickListener {
            //임시
            startActivity(Intent(this,LoginActivity::class.java))
        }
        // 회원가입 버튼
        binding.loginInfoTextSignUp.setOnClickListener {
            startActivity(Intent(this,SignUpActivity::class.java))
        }
        // 아이디/비밀번호 찾기 버튼
        binding.loginInfoTextFindIdPassword.setOnClickListener {  }

    }
}