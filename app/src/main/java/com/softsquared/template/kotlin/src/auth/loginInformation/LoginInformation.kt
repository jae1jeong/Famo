package com.softsquared.template.kotlin.src.auth.loginInformation

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import com.kakao.sdk.auth.LoginClient
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.KakaoSdk
import com.kakao.sdk.user.UserApiClient
import com.kakao.sdk.user.model.User
import com.softsquared.template.kotlin.config.BaseActivity
import com.softsquared.template.kotlin.databinding.ActivityLoginInfoBinding
import com.softsquared.template.kotlin.src.auth.login.LoginActivity
import com.softsquared.template.kotlin.src.auth.loginInformation.models.KakaoLoginResponse
import com.softsquared.template.kotlin.src.auth.signup.SignUpActivity
import com.softsquared.template.kotlin.src.auth.test.Test
import com.softsquared.template.kotlin.src.main.MainActivity
import com.softsquared.template.kotlin.src.main.mypage.MyPageActivity

class LoginInformation:BaseActivity<ActivityLoginInfoBinding>(ActivityLoginInfoBinding::inflate),
    LoginInformationView{
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

        //카카오 로그인 초기화
        KakaoSdk.init(this, "850f56e3e5ba5613faf68a8aaa4b95bc")

//        카카오로그인 공통 callback
        val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
            UserApiClient.instance.me { user: User?, throwable:Throwable? ->
                if (user != null) {
                    Log.i(
                        "TAG", "사용자 정보 요청 성공 " +
                                "\nid: ${user.id} " +
                                "\nthumbnailImageUrl: ${user.kakaoAccount?.profile!!.thumbnailImageUrl} " +
                                "\nnickname: ${user.kakaoAccount!!.profile!!.nickname}" +
                                "\ntoken: ${token!!.accessToken} "
                    )
                    val accessToken = token.accessToken
                    val refreshToken = token.refreshToken
                    val name = user.kakaoAccount!!.profile!!.nickname
                    val img = user.kakaoAccount!!.profile!!.thumbnailImageUrl

                    val intent = Intent(this, MainActivity::class.java)
                    intent.putExtra("accessToken",accessToken)
                    intent.putExtra("refreshToken",refreshToken)
                    intent.putExtra("name",name)
                    intent.putExtra("img",img)
                    startActivity(intent)
//                    LoginInformationService(this).tryGetKakaoLogin()
                }
                if (throwable != null) {
                    Log.w("로그", "invoke: " + throwable.localizedMessage)
//                Log.e(TAG, "사용자 정보 요청 실패", throwable)
                }
            }

        }

//        카카오 로그인 버튼 클릭
        binding.loginInfoBtnKakaoLogin.setOnClickListener {
            LoginInformationService(this).tryGetKakaoLogin()

//             어플에 카톡이 깔려있는 경우
            if (LoginClient.instance.isKakaoTalkLoginAvailable(this)) {
                LoginClient.instance.loginWithKakaoTalk(this, callback = callback)

//             어플이 없을 경우 카톡홈페이지를 통하여 연결
            } else {
                LoginClient.instance.loginWithKakaoAccount(this, callback = callback)

            }

        }
    }

    override fun onGetKakaoLoginSuccess(kakaoLoginResponse: KakaoLoginResponse) {
        when(kakaoLoginResponse.code){
            100 -> {
                Log.d("TAG", "onGetKakaoLoginSuccess: 카카오로그인 성공")
                showCustomToast("카카오 로그인 성공")
                val intent = Intent(this,MyPageActivity::class.java)
                startActivity(intent)

            }else -> {
                showCustomToast("실패 메시지 : ${kakaoLoginResponse.message}")
            Log.d("TAG", "실패매시지: ${kakaoLoginResponse.message}")
            }
        }
    }

    override fun onGetKakaoLoginFail(message: String) {
        Log.d("TAG", "onGetKakaoLoginFail 실패매시지: 실패")

    }
}