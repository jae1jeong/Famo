package com.softsquared.template.kotlin.src.auth.signup

import android.content.Intent
import android.os.Bundle
import com.softsquared.template.kotlin.config.BaseActivity
import com.softsquared.template.kotlin.databinding.ActivitySignUpBinding
import com.softsquared.template.kotlin.src.auth.login.LoginActivity

class SignUpActivity:BaseActivity<ActivitySignUpBinding>(ActivitySignUpBinding::inflate) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val editId = binding.signUpEditId
        val editPassword = binding.signUpEditPassword
        val editRePassword = binding.signUpEditRePassword
        val editNickname = binding.signUpEditNickname


        // 회원가입 검증
        binding.signUpBtnSignUp.setOnClickListener {
            if(editId.text != null && editPassword.text != null && editRePassword.text != null && editNickname.text != null){
                if(editPassword.text == editRePassword.text){
                    showCustomToast("회원가입이 완료되었습니다.")
                    startActivity(Intent(this,LoginActivity::class.java))
                    finish()
                }else{
                    if(editPassword.text != null && editRePassword.text != null && editPassword.text != editRePassword.text){
                        showCustomToast("비밀번호와 비밀번호 확인이 일치하지 않습니다.")
                    }
                }
            }else{
                showCustomToast("입력한 부분이 없는지 확인해주세요.")
            }
        }
    }
}