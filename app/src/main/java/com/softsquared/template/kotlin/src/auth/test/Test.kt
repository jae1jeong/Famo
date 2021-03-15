package com.softsquared.template.kotlin.src.auth.test

import android.content.Intent
import android.os.Bundle
import android.provider.SyncStateContract
import android.util.Log
import android.widget.Button
import android.widget.EditText
import com.softsquared.template.kotlin.R
import com.softsquared.template.kotlin.config.ApplicationClass
import com.softsquared.template.kotlin.config.BaseActivity
import com.softsquared.template.kotlin.config.BaseResponse
import com.softsquared.template.kotlin.databinding.ActivityTestBinding
import com.softsquared.template.kotlin.src.auth.loginInformation.LoginInformationService
import com.softsquared.template.kotlin.src.auth.loginInformation.LoginInformationView
import com.softsquared.template.kotlin.src.auth.loginInformation.models.KakaoLoginResponse
import com.softsquared.template.kotlin.src.auth.loginInformation.models.PatchKakaoLoginNumberRequest
import com.softsquared.template.kotlin.src.auth.signup.SignUpView
import com.softsquared.template.kotlin.src.main.MainActivity
import com.softsquared.template.kotlin.util.Constants
import kotlinx.android.synthetic.main.activity_test.view.*
import kotlin.math.log

class Test: BaseActivity<ActivityTestBinding>(ActivityTestBinding::inflate),
    LoginInformationView{

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val jwt:String? = ApplicationClass.sSharedPreferences.getString(Constants.KAKAO_JWT,null)

        val accessToken = intent.getStringExtra("accessToken")
        val refreshToken = intent.getStringExtra("refreshToken")
        Log.d("TAG", "accessToken: $accessToken")
        Log.d("TAG", "refreshToken: $refreshToken")

        val number : EditText = findViewById(R.id.number)
        val button : Button = findViewById(R.id.button)

        button.setOnClickListener {

            val patchKakaoLoginNumberRequest = PatchKakaoLoginNumberRequest(
                phoneNumber = number.toString()
            )

            LoginInformationService(this).tryPatchKakaoLoginNumber(patchKakaoLoginNumberRequest)
//            val inputNum = number.text.toString()
//            Log.d("TAG", "number: $inputNum")
//            Log.d("TAG", "accessToken: $accessToken")
//            Log.d("TAG", "refreshToken: $refreshToken")
        }



    }

    override fun onPostKakaoLoginSuccess(response: KakaoLoginResponse) {
    }

    override fun onPostKakaoLoginFail(message: String) {
    }

    override fun onPatchKakaoLoginNumberSuccess(response: BaseResponse) {

        when(response.code){
            100 -> {
                Log.d("TAG", "onPatchKakaoLoginNumberSuccess: 번호입력성공")
                val intent = Intent(this,MainActivity::class.java)
                startActivity(intent)
            }else -> {
            Log.d("TAG", "onPatchKakaoLoginNumberSuccess: ${response.message.toString()}")
            }
        }
    }

    override fun onPatchKakaoLoginNumberFail(message: String) {
    }
}