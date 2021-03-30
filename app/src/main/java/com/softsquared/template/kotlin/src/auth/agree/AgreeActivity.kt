package com.softsquared.template.kotlin.src.auth.agree

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.softsquared.template.kotlin.config.ApplicationClass
import com.softsquared.template.kotlin.config.BaseActivity
import com.softsquared.template.kotlin.config.BaseResponse
import com.softsquared.template.kotlin.databinding.ActivityAgreeBinding
import com.softsquared.template.kotlin.databinding.ActivityKakaoLoginNumberBinding
import com.softsquared.template.kotlin.src.auth.kakaologin.KakaoLoginNumberService
import com.softsquared.template.kotlin.src.auth.kakaologin.KakaoLoginNumberView
import com.softsquared.template.kotlin.src.auth.kakaologin.models.PatchKakaoLoginNumberRequest
import com.softsquared.template.kotlin.src.auth.signup.SignUpActivity
import com.softsquared.template.kotlin.src.main.MainActivity
import com.softsquared.template.kotlin.util.Constants

class AgreeActivity : BaseActivity<ActivityAgreeBinding>
    (ActivityAgreeBinding::inflate) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.agreeBtnStartSignup.setOnClickListener {
            val intent = Intent(this,SignUpActivity::class.java)
            startActivity(intent)
            finish()
        }

    }
}