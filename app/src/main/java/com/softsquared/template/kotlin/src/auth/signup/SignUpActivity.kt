package com.softsquared.template.kotlin.src.auth.signup

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import com.softsquared.template.kotlin.config.BaseActivity
import com.softsquared.template.kotlin.config.BaseResponse
import com.softsquared.template.kotlin.databinding.ActivitySignUpBinding
import com.softsquared.template.kotlin.src.auth.login.LoginActivity
import com.softsquared.template.kotlin.src.auth.signup.models.CheckAuthNumberResponse
import com.softsquared.template.kotlin.src.auth.signup.models.PostRequestSendMessage
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
        var isAuth = false

        setSupportActionBar(binding.signUpToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        binding.signUpLinearAuthZone.visibility = View.GONE
        // 회원가입 검증
        binding.signUpBtnSignUp.setOnClickListener {

            if(isAuth){
//                showCustomToast(" ${editId.text} ${editPassword.text} ${editRePassword.text} ${editNickname.text} ${editPhoneNumber.text}")
                showLoadingDialog(this)
                SignUpService(this).tryPostSignUp(PostRequestSignUp(editId.text.toString(),editPassword.text.toString(),editNickname.text.toString(),editPhoneNumber.text.toString()))
            }else{
                showCustomToast("휴대폰 인증을 완료해주세요.")
            }
        }

        // 휴대폰 인증 완료 버튼
        binding.signUpBtnAuthCheck.setOnClickListener {

        }

        // 휴대폰 인증 문자전송 버튼
        binding.signUpBtnAuth.setOnClickListener {
            binding.signUpLinearAuthZone.visibility = View.VISIBLE
            showLoadingDialog(this)
            SignUpService(this).tryPostSendMessage(PostRequestSendMessage(binding.signUpEditPhoneNumber.text.toString()))
        }

        // 닉네임 글자수 제한
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

    override fun onPostSendMessageSuccess(response: BaseResponse) {
        if(response.isSuccess && response.code == 100){
            showCustomToast(response.message.toString())
        }else{
            showCustomToast(response.message.toString())
        }
        dismissLoadingDialog()
    }

    override fun onPostSendMessageFailure(message: String) {
        dismissLoadingDialog()
        showCustomToast(message)
    }

    override fun onGetCheckAuthNumberSuccess(response: CheckAuthNumberResponse) {
    }

    override fun onGetCheckAuthNumberFailure(message: String) {
    }
}