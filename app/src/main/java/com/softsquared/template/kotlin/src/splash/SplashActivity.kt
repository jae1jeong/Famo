package com.softsquared.template.kotlin.src.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.softsquared.template.kotlin.config.ApplicationClass
import com.softsquared.template.kotlin.config.BaseActivity
import com.softsquared.template.kotlin.databinding.ActivitySplashBinding
import com.softsquared.template.kotlin.src.auth.information.InformationActivity
import com.softsquared.template.kotlin.src.main.MainActivity

class SplashActivity : BaseActivity<ActivitySplashBinding>(ActivitySplashBinding::inflate) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val jwt:String? = ApplicationClass.sSharedPreferences.getString(ApplicationClass.X_ACCESS_TOKEN,null)
        Handler(Looper.getMainLooper()).postDelayed({
            if (jwt == null){
                startActivity(Intent(this, InformationActivity::class.java))
                finish()
            }else{
                startActivity(Intent(this,MainActivity::class.java))
                finish()
            }
        }, 1500)
    }
}