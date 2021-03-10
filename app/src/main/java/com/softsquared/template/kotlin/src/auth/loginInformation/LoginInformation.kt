package com.softsquared.template.kotlin.src.auth.loginInformation

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import com.bumptech.glide.Glide
import com.kakao.sdk.auth.LoginClient
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.KakaoSdk
import com.kakao.sdk.user.UserApiClient
import com.kakao.sdk.user.model.User
import com.softsquared.template.kotlin.config.BaseActivity
import com.softsquared.template.kotlin.databinding.ActivityLoginInfoBinding
import com.softsquared.template.kotlin.src.auth.login.LoginActivity
import com.softsquared.template.kotlin.src.auth.signup.SignUpActivity
import com.softsquared.template.kotlin.src.main.MainActivity

class LoginInformation:BaseActivity<ActivityLoginInfoBinding>(ActivityLoginInfoBinding::inflate) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 로그인 버튼
        binding.loginInfoBtnLogin.setOnClickListener {
            //임시
            startActivity(Intent(this,MainActivity::class.java))
            finish()
        }
        // 회원가입 버튼
        binding.loginInfoTextSignUp.setOnClickListener {
            startActivity(Intent(this,SignUpActivity::class.java))
            finish()
        }
        // 아이디/비밀번호 찾기 버튼
        binding.loginInfoTextFindIdPassword.setOnClickListener {  }

        //카카오 로그인 초기화
        KakaoSdk.init(this, "850f56e3e5ba5613faf68a8aaa4b95bc")

//        카카오로그인 공통 callback
//        val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
//            UserApiClient.instance.me { user: User?, throwable:Throwable? ->
//
//                if (user != null) {
//                    Log.i(
//                        "TAG", "사용자 정보 요청 성공 " +
//                                "\nid: ${user.id} " +
//                                "\nemail: ${user.kakaoAccount?.email} " +
//                                "\nnickname: ${user.kakaoAccount!!.profile!!.nickname}" +
//                                "\ngender: ${user.kakaoAccount!!.gender} " +
//                                "\nage: ${user.kakaoAccount!!.ageRange} "
//                    )
//
//                    val accessToken = token!!.accessToken
//                    val name = user.kakaoAccount!!.profile!!.nickname
//                    val img = user.kakaoAccount!!.profile!!.thumbnailImageUrl
//
//                    val intent = Intent(this,MainActivity::class.java)
//                    intent.putExtra("token",accessToken)
//                    intent.putExtra("name",name)
//                    intent.putExtra("img",img)
//                    startActivity(intent)
//                    finish()
////                    profileImage?.let {
////                        Glide.with(it).load(user.kakaoAccount!!.profile!!.thumbnailImageUrl)
////                            .centerCrop().into(profileImage!!)
////                    }
//                }
//                if (throwable != null) {
//                    Log.w("로그", "invoke: " + throwable.localizedMessage)
////                Log.e(TAG, "사용자 정보 요청 실패", throwable)
//                }
//            }
//
//        }

        //카카오 로그인 버튼 클릭
//        binding.homeLoginBtnKakaoLogin.setOnClickListener {
            // 어플에 카톡이 깔려있는 경우
//            if (LoginClient.instance.isKakaoTalkLoginAvailable(this)) {
//                LoginClient.instance.loginWithKakaoTalk(this, callback = callback)
            // 어플이 없을 경우 카톡홈페이지를 통하여 연결
//            } else {
//                LoginClient.instance.loginWithKakaoAccount(this, callback = callback)
//            }
//
//        }
    }
}