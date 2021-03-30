package com.softsquared.template.kotlin.src.auth.loginInformation

import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.kakao.sdk.auth.LoginClient
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.KakaoSdk
import com.kakao.sdk.user.UserApiClient
import com.kakao.sdk.user.model.User
import com.softsquared.template.kotlin.config.ApplicationClass
import com.softsquared.template.kotlin.config.BaseActivity
import com.softsquared.template.kotlin.config.BaseResponse
import com.softsquared.template.kotlin.databinding.ActivityLoginInfoBinding
import com.softsquared.template.kotlin.src.auth.agree.AgreeActivity
import com.softsquared.template.kotlin.src.auth.find.FindUserActivity
import com.softsquared.template.kotlin.src.auth.login.LoginActivity
import com.softsquared.template.kotlin.src.auth.loginInformation.models.KakaoLoginResponse
import com.softsquared.template.kotlin.src.auth.loginInformation.models.PostKakaoLoginRequest
import com.softsquared.template.kotlin.src.auth.signup.SignUpActivity
import com.softsquared.template.kotlin.src.auth.kakaologin.KakaoLoginNumberActivity
import com.softsquared.template.kotlin.src.main.MainActivity
import com.softsquared.template.kotlin.util.Constants

class LoginInformation:BaseActivity<ActivityLoginInfoBinding>(ActivityLoginInfoBinding::inflate),
    LoginInformationView{

    var kakaoUrl = ""
    var kakaoEmail = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 로그인 버튼
        binding.loginInfoBtnLogin.setOnClickListener {
            //임시
            startActivity(Intent(this,LoginActivity::class.java))
        }
        // 회원가입 버튼
        binding.loginInfoTextSignUp.setOnClickListener {
            startActivity(Intent(this,AgreeActivity::class.java))
        }
        // 아이디/비밀번호 찾기 버튼
        binding.loginInfoTextFindIdPassword.setOnClickListener {
            startActivity(Intent(this,FindUserActivity::class.java))
        }

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
                    if (user.kakaoAccount?.profile!!.thumbnailImageUrl != null){
                        kakaoUrl = user.kakaoAccount!!.profile!!.thumbnailImageUrl
                    }

                    kakaoEmail = user.kakaoAccount!!.email.toString()

//                    val intent = Intent(this, MainActivity::class.java)
//                    intent.putExtra("accessToken",accessToken)
//                    intent.putExtra("refreshToken",refreshToken)
//                    intent.putExtra("name",name)
//                    intent.putExtra("img",img)
//                    startActivity(intent)
                    val postKakaoLoginRequest = PostKakaoLoginRequest(
                        kakaoAccessToken = accessToken,
                        kakaoRefreshToken = refreshToken
                    )

                    LoginInformationService(this).tryGetKakaoLogin(postKakaoLoginRequest)
                }
                if (throwable != null) {
                    Log.w("로그", "invoke: " + throwable.localizedMessage)
//                Log.e(TAG, "사용자 정보 요청 실패", throwable)
                }
            }

        }

//        카카오 로그인 버튼 클릭
        binding.loginInfoBtnKakaoLogin.setOnClickListener {

//             어플에 카톡이 깔려있는 경우
            if (LoginClient.instance.isKakaoTalkLoginAvailable(this)) {
                LoginClient.instance.loginWithKakaoTalk(this, callback = callback)
//             어플이 없을 경우 카톡홈페이지를 통하여 연결
            } else {
                LoginClient.instance.loginWithKakaoAccount(this, callback = callback)

            }

        }
    }

    override fun onPostKakaoLoginSuccess(response: KakaoLoginResponse) {

        val edit = ApplicationClass.sSharedPreferences.edit()
//        edit.putString(Constants.KAKAO_JWT, response.jwt)
        edit.putString(ApplicationClass.X_ACCESS_TOKEN, response.jwt)
        edit.putInt(Constants.USER_ID, response.userID)
        edit.putString(Constants.USER_NICKNAME, response.nickname)
        edit.putString(Constants.KAKAO_THUMBNAILIMAGEURL, kakaoUrl)
        edit.putString(Constants.KAKAO_EMAIL, kakaoEmail)
        edit.apply()

        when(response.code){

            100 -> {
                Log.d("TAG", "onGetKakaoLoginSuccess: 소셜로그인 요청 성공, 이미있는 계정인 경우동 홈화면으로 이동")
                val intent = Intent(this,MainActivity::class.java)
                startActivity(intent)
            }

            101 -> {
                Log.d("TAG", "onGetKakaoLoginSuccess: 소셜로그인 요청 성공,  계정인 경우첫 번호 입력 화면으로 이")
                showCustomToast("카카오 로그인 성공")
                val intent = Intent(this,KakaoLoginNumberActivity::class.java)
                startActivity(intent)
            }

        }
    }

    override fun onPostKakaoLoginFail(message: String) {
    }
}