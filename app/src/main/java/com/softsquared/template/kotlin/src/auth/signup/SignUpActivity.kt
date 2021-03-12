package com.softsquared.template.kotlin.src.auth.signup

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import com.softsquared.template.kotlin.config.BaseActivity
import com.softsquared.template.kotlin.databinding.ActivitySignUpBinding
import com.softsquared.template.kotlin.src.auth.login.LoginActivity
import com.softsquared.template.kotlin.src.auth.signup.models.PostRequestSignUp
import com.softsquared.template.kotlin.src.auth.signup.models.SignUpResponse

class SignUpActivity:BaseActivity<ActivitySignUpBinding>(ActivitySignUpBinding::inflate)
,SignUpView{
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val editId = binding.signUpEditId
        val editPassword = binding.signUpEditPassword
        val editRePassword = binding.signUpEditRePassword
        val editNickname = binding.signUpEditNickname
        val editPhoneNumber = binding.signUpEditPhoneNumber

        setSupportActionBar(binding.signUpToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        // 회원가입 검증
        binding.signUpBtnSignUp.setOnClickListener {
//            Log.d(
//                "SignUpActivity",
//                "onCreate: ${editId.text} ${editPassword.text} ${editRePassword.text} ${editNickname.text}"
//            )
            showCustomToast(" ${editId.text} ${editPassword.text} ${editRePassword.text} ${editNickname.text} ${editPhoneNumber.text}")
            showLoadingDialog(this)
            SignUpService(this).tryPostSignUp(PostRequestSignUp(editId.text.toString(),editPassword.text.toString(),editNickname.text.toString(),editPhoneNumber.text.toString()))
        }

        binding.signUpEditNickname.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                binding.signUpTextNicknameLength.text = "${binding.signUpEditNickname.text.length}/6"
            }

            override fun afterTextChanged(p0: Editable?) {
            }

        })
    }

    override fun onPostSignUpSuccess(signUpResponse: SignUpResponse) {
        dismissLoadingDialog()
        if(signUpResponse.isSuccess){
            when(signUpResponse.code){
                100 ->{
                    showCustomToast(signUpResponse.message.toString())
                    startActivity(Intent(this,LoginActivity::class.java))
                    finish()
                }
                else->{
                    showCustomToast(signUpResponse.message.toString())
                }
            }
        }else{
            showCustomToast(signUpResponse.message.toString())
        }

    }

    // test1234 James1234@
    override fun onPostSignUpFailure(message: String) {
        dismissLoadingDialog()
        showCustomToast(message)
    }
}