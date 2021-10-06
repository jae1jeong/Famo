package com.makeus.jfive.famo.src.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import com.makeus.jfive.famo.config.ApplicationClass
import com.makeus.jfive.famo.config.BaseActivity
import com.makeus.jfive.famo.databinding.ActivitySplashBinding
import com.makeus.jfive.famo.src.auth.check_token.TokenCheckService
import com.makeus.jfive.famo.src.auth.check_token.TokenCheckView
import com.makeus.jfive.famo.src.auth.information.InformationActivity
import com.makeus.jfive.famo.src.presentation.main.MainActivity

class SplashActivity : BaseActivity<ActivitySplashBinding>(ActivitySplashBinding::inflate),
    TokenCheckView {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Handler(Looper.getMainLooper()).postDelayed({
            TokenCheckService(this).tryCheckToken()
        }, 1500)
    }

    override fun onTokenCheckSuccess() {
        val jwt:String? = ApplicationClass.sSharedPreferences.getString(ApplicationClass.X_ACCESS_TOKEN,null)
        Log.d("TAG", "onTokenCheckSuccess: $jwt")
        startActivity(Intent(this, MainActivity::class.java))
        finish()

    }

    override fun onTokenCheckFailure() {
        startActivity(Intent(this, InformationActivity::class.java))
        finish()
    }
}