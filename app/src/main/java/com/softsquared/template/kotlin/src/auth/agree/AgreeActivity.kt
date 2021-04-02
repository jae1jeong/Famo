package com.softsquared.template.kotlin.src.auth.agree

import android.content.Intent
import android.os.Bundle
import com.softsquared.template.kotlin.config.BaseActivity
import com.softsquared.template.kotlin.databinding.ActivityAgreeBinding
import com.softsquared.template.kotlin.src.auth.signup.SignUpActivity

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