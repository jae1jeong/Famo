package com.softsquared.template.kotlin.src.auth.signup

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import com.softsquared.template.kotlin.R
import com.softsquared.template.kotlin.config.BaseActivity
import com.softsquared.template.kotlin.config.BaseResponse
import com.softsquared.template.kotlin.databinding.ActivitySignUpBinding
//import com.softsquared.template.kotlin.src.auth.login.LoginActivity
import com.softsquared.template.kotlin.src.auth.signup.models.*

class SignUpActivity:BaseActivity<ActivitySignUpBinding>(ActivitySignUpBinding::inflate)
,SignUpView{
    private var isAuthMessage = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val editId = binding.signUpEditId
        val editPassword = binding.signUpEditPassword
        val editRePassword = binding.signUpEditRePassword
        val editNickname = binding.signUpEditNickname
        val editPhoneNumber = binding.signUpEditPhoneNumber
        val editAuthNumber = binding.signUpEditAuthNumber

        setSupportActionBar(binding.signUpToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        binding.signUpLinearAuthZone.visibility = View.GONE
        // 회원가입 검증
        binding.signUpBtnSignUp.setOnClickListener {
            if(isAuthMessage){
                showLoadingDialog(this)
                SignUpService(this).tryPostSignUp(PostRequestSignUp(editId.text.toString(),editPassword.text.toString(),editNickname.text.toString(),editPhoneNumber.text.toString()))
            }
        }

        // 휴대폰 인증 확인 버튼
        binding.signUpBtnAuthCheck.setOnClickListener {
            showLoadingDialog(this)
            SignUpService(this).tryGetCheckAuthNumber(GetRequestCheckAuthNumber(binding.signUpEditPhoneNumber.text.toString(),binding.signUpEditAuthNumber.text.toString()))
        }

        // 휴대폰 인증 문자전송 버튼
        binding.signUpBtnSendAuth.setOnClickListener {
            binding.signUpLinearAuthZone.visibility = View.VISIBLE
            if(editPhoneNumber.text.length != 11){
                showCustomToast("휴대폰 번호를 확인해주세요.")
            }else{
//                binding.signUpBtnSendAuth.backgroundTintList = resources.getColorStateList(R.color.black)
                showLoadingDialog(this)
                SignUpService(this).tryPostSendMessage(PostRequestSendMessage(binding.signUpEditPhoneNumber.text.toString()))
            }
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

        // 휴대폰 번호 EditText
        binding.signUpEditPhoneNumber.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                if (p0!!.length == 11) {
                    binding.signUpBtnSendAuth.setTextColor(resources.getColor(R.color.black))
                } else {
                    binding.signUpBtnSendAuth.setTextColor(resources.getColor(R.color.auth_button_text))
                }
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                showCustomToast(p0.toString())
                Log.d("TAG", "afterTextChanged: $p0")

            }
        })
    }

    override fun onPostSignUpSuccess(signUpResponse: SignUpResponse) {
        dismissLoadingDialog()
        if(signUpResponse.isSuccess){
            when(signUpResponse.code){
                100 ->{
                    showCustomToast(signUpResponse.message.toString())
//                    startActivity(Intent(this,LoginActivity::class.java))
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
        if(response.isSuccess && response.code == 100){
            isAuthMessage = true
            showCustomToast(response.message.toString())
        }
        else{
            showCustomToast(response.message.toString())
        }
        dismissLoadingDialog()
    }

    override fun onGetCheckAuthNumberFailure(message: String) {
        dismissLoadingDialog()
        showCustomToast(message)
    }
}