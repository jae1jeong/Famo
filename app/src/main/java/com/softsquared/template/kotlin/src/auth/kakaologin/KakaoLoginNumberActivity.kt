package com.softsquared.template.kotlin.src.auth.kakaologin

import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.softsquared.template.kotlin.config.ApplicationClass
import com.softsquared.template.kotlin.config.BaseActivity
import com.softsquared.template.kotlin.config.BaseResponse
import com.softsquared.template.kotlin.databinding.ActivityKakaoLoginNumberBinding
import com.softsquared.template.kotlin.src.auth.kakaologin.models.PatchKakaoLoginNumberRequest
import com.softsquared.template.kotlin.src.main.MainActivity
import com.softsquared.template.kotlin.util.Constants

class KakaoLoginNumberActivity: BaseActivity<ActivityKakaoLoginNumberBinding>
    (ActivityKakaoLoginNumberBinding::inflate), KakaoLoginNumberView{

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // toolbar
        setSupportActionBar(binding.kakaoLoginToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        val jwt:String? = ApplicationClass.sSharedPreferences.getString(Constants.KAKAO_JWT,null)

        val accessToken = intent.getStringExtra("accessToken")
        val refreshToken = intent.getStringExtra("refreshToken")
        Log.d("TAG", "accessToken: $accessToken")
        Log.d("TAG", "refreshToken: $refreshToken")


        binding.kakaoLoginBtn.setOnClickListener {

            val patchKakaoLoginNumberRequest = PatchKakaoLoginNumberRequest(
                phoneNumber = binding.kakaoLoginEditNumber.text.toString()
            )
            KakaoLoginNumberService(this).tryPatchKakaoLoginNumber(patchKakaoLoginNumberRequest)
//            val inputNum = number.text.toString()
//            Log.d("TAG", "number: $inputNum")
//            Log.d("TAG", "accessToken: $accessToken")
//            Log.d("TAG", "refreshToken: $refreshToken")
        }

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

//    fun getKeyHash(context:Context):String{
//        val packageInfo = Utility
//        packageInfo.
//    }
}