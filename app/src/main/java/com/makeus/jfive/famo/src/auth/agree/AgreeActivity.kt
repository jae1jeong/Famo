package com.makeus.jfive.famo.src.auth.agree

import android.content.Intent
import android.os.Bundle
import com.makeus.jfive.famo.config.BaseActivity
import com.makeus.jfive.famo.databinding.ActivityAgreeBinding
import com.makeus.jfive.famo.src.auth.signup.SignUpActivity

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