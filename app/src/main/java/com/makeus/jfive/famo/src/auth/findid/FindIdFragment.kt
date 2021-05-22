package com.makeus.jfive.famo.src.auth.findid

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import com.makeus.jfive.famo.R
import com.makeus.jfive.famo.config.BaseFragment
import com.makeus.jfive.famo.config.BaseResponse
import com.makeus.jfive.famo.databinding.FragmentFindIdBinding
import com.makeus.jfive.famo.src.auth.find.FindService
import com.makeus.jfive.famo.src.auth.find.FindView
import com.makeus.jfive.famo.src.auth.find.models.FindIdResponse
import com.makeus.jfive.famo.src.auth.findresult.FindResultActivity
import com.makeus.jfive.famo.src.auth.signup.SignUpService
import com.makeus.jfive.famo.src.auth.signup.SignUpView
import com.makeus.jfive.famo.src.auth.signup.models.CheckAuthNumberResponse
import com.makeus.jfive.famo.src.auth.signup.models.GetRequestCheckAuthNumber
import com.makeus.jfive.famo.src.auth.signup.models.PostRequestSendMessage
import com.makeus.jfive.famo.src.auth.signup.models.SignUpResponse
import com.makeus.jfive.famo.util.Constants

class FindIdFragment:BaseFragment<FragmentFindIdBinding>(FragmentFindIdBinding::bind, R.layout.fragment_find_id),FindView,SignUpView {
    private var isPhoneAuth = false
    private var otpToken:String =""
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.findIdEditPhoneNumber.addTextChangedListener(object:TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                if(p0?.length == 11){
                    binding.findIdBtnAuth.setTextColor(resources.getColor(R.color.black))
                }else{
                    binding.findIdBtnAuth.setTextColor(resources.getColor(R.color.auth_button_text))
                }
            }
        })


        // 인증요청 버튼
        binding.findIdBtnAuth.setOnClickListener {
            showLoadingDialog(context!!)
            SignUpService(this).tryPostSendMessage(PostRequestSendMessage(binding.findIdEditPhoneNumber.text.toString()))
        }
        // 인증확인 버튼
        binding.findIdBtnAuthCheck.setOnClickListener {
            showLoadingDialog(context!!)
            SignUpService(this).tryGetCheckAuthNumber(GetRequestCheckAuthNumber(binding.findIdEditPhoneNumber.text.toString(),binding.findIdEditAuthNumber.text.toString()))
        }
        // 아이디 찾기 버튼
        binding.findIdBtnFindId.setOnClickListener {
            if(isPhoneAuth){
                if(otpToken != ""){
                    showLoadingDialog(context!!)
                    FindService(this).tryGetFindId(otpToken)
                }else{
                    showCustomToast("잘못된 요청입니다.")
                }
            }else{
                showCustomToast("아이디 찾기를 다시 시도하여주세요.")
            }
        }
    }

    override fun onGetFindIdSuccess(response: FindIdResponse) {
        if(response.isSuccess){
            val intent = Intent(context!!,FindResultActivity::class.java)
            when(response.code){
                100 ->{
                    // FAMO로 가입한 회원인 경우
                    val findId = response.loginID
                    intent.putExtra(Constants.FIND_USER_ID,findId)
                    startActivity(intent)
                }
                407->{
                    // 카카오로그인으로 가입한 회원인 경우
                    startActivity(intent)
                }
                else->{
                    showCustomToast(response.message.toString())
                }
            }
        }else{
            showCustomToast(response.message.toString())
        }
        dismissLoadingDialog()

    }

    override fun onGetFindIdFailure(message: String) {
        dismissLoadingDialog()
        showCustomToast(message)
    }

    override fun onGetCompareSuccess(response: BaseResponse) {
    }

    override fun onGetCompareFailure(message: String) {
    }

    override fun onPostSignUpSuccess(signUpResponse: SignUpResponse) {
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

}