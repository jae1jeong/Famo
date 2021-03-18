package com.softsquared.template.kotlin.src.auth.findpassword

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.softsquared.template.kotlin.R
import com.softsquared.template.kotlin.config.ApplicationClass
import com.softsquared.template.kotlin.config.BaseFragment
import com.softsquared.template.kotlin.config.BaseResponse
import com.softsquared.template.kotlin.databinding.FragmentFindPasswordBinding
import com.softsquared.template.kotlin.src.auth.find.FindService
import com.softsquared.template.kotlin.src.auth.find.FindView
import com.softsquared.template.kotlin.src.auth.find.models.FindIdResponse
import com.softsquared.template.kotlin.src.auth.find.models.GetCompareIdPhoneNumber
import com.softsquared.template.kotlin.src.auth.setnewpassword.SetNewPasswordActivity
import com.softsquared.template.kotlin.src.auth.signup.SignUpService
import com.softsquared.template.kotlin.src.auth.signup.SignUpView
import com.softsquared.template.kotlin.src.auth.signup.models.CheckAuthNumberResponse
import com.softsquared.template.kotlin.src.auth.signup.models.GetRequestCheckAuthNumber
import com.softsquared.template.kotlin.src.auth.signup.models.PostRequestSendMessage
import com.softsquared.template.kotlin.src.auth.signup.models.SignUpResponse
import com.softsquared.template.kotlin.util.Constants

class FindPasswordFragment:BaseFragment<FragmentFindPasswordBinding>(FragmentFindPasswordBinding::bind, R.layout.fragment_find_password),
    SignUpView,FindView{
    private var isPhoneAuth = true
    private var otpToken:String = ""
    private var previousFindId = ""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.findPasswordEditPhoneNumber.addTextChangedListener(object:TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                if(p0?.length == 11){
                    binding.findPasswordBtnSendMessage.setTextColor(resources.getColor(R.color.black))
                }else{
                    binding.findPasswordBtnSendMessage.setTextColor(resources.getColor(R.color.auth_button_text))
                }

            }

        })

        // 휴대폰 인증 문자전송 버튼
        binding.findPasswordBtnSendMessage.setOnClickListener {
            Log.d("phoneNumberLength", "onViewCreated: ${binding.findPasswordBtnSendMessage.text.length}")
            if(binding.signUpEditId.text.length > 0){
                if(binding.findPasswordEditPhoneNumber.text.length == 11){
                    previousFindId = binding.signUpEditId.text.toString()
                    showLoadingDialog(context!!)
                    SignUpService(this).tryPostSendMessage(PostRequestSendMessage(binding.findPasswordEditPhoneNumber.text.toString()))
                }else{
                    showCustomToast("휴대폰 번호를 확인해주세요.")
                }
            }else{
                showCustomToast("휴대폰 번호를 입력하기 전에 아이디를 입력해주세요.")
            }
        }
        // 휴대폰 인증 확인 버튼
        binding.findPasswordBtnAuthCheck.setOnClickListener {
            showLoadingDialog(context!!)
            SignUpService(this).tryGetCheckAuthNumber(GetRequestCheckAuthNumber(binding.findPasswordEditPhoneNumber.text.toString(),binding.findPasswordEditAuthNumber.text.toString()))
        }

        // 휴대폰 인증 확인 버튼
        binding.findPasswordBtnAuthCheck.setOnClickListener {
            showLoadingDialog(context!!)
            SignUpService(this).tryGetCheckAuthNumber(GetRequestCheckAuthNumber(binding.findPasswordEditPhoneNumber.text.toString(),binding.findPasswordEditAuthNumber.text.toString()))
        }

        // 확인 버튼
        binding.findPasswordBtnFindPassword.setOnClickListener {
            if(otpToken != "" && isPhoneAuth){
                if(previousFindId == binding.signUpEditId.text.toString()){
                    showLoadingDialog(context!!)
                    Log.d("TAG", "onViewCreated: ${binding.signUpEditId.text.toString()} $otpToken")
                    FindService(this).tryGetCompareIdPhoneNumber(binding.signUpEditId.text.toString(),otpToken)
                }else{
                    binding.signUpEditId.setText("")
                    binding.findPasswordEditAuthNumber.setText("")
                    binding.findPasswordEditPhoneNumber.setText("")
                    showCustomToast("잘못된 요청입니다. 다시 시도하여 주세요.")
                }
            }
            else{
                showCustomToast("비밀번호 찾기를 다시 시도하여주세요.")
            }
        }
    }

    override fun onPostSignUpSuccess(signUpResponse: SignUpResponse) {
    }

    override fun onPostSignUpFailure(message: String) {
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
            // 휴대폰 인증 완료 변수
            isPhoneAuth = true
            // otp 헤더
            otpToken = response.jwt
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

    override fun onGetFindIdSuccess(response: FindIdResponse) {
    }

    override fun onGetFindIdFailure(message: String) {
    }

    override fun onGetCompareSuccess(response: BaseResponse) {
        if(response.isSuccess){
            when(response.code){
                100->{
                    val intent = Intent(context!!,SetNewPasswordActivity::class.java)
                    intent.putExtra(Constants.OTP_TOKEN,otpToken)
                    startActivity(intent)
                }
                407->{
                    // 어떻게 할지 정해야함
                }
                else->{
                    showCustomToast(response.message.toString())
                }
            }
            dismissLoadingDialog()
        }else{
            showCustomToast(response.message.toString())
            dismissLoadingDialog()
        }
    }

    override fun onGetCompareFailure(message: String) {
        dismissLoadingDialog()
        showCustomToast(message)
    }
}