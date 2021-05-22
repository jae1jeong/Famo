package com.makeus.jfive.famo.src.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.makeus.jfive.famo.config.ApplicationClass
import com.makeus.jfive.famo.config.BaseActivity
import com.makeus.jfive.famo.databinding.ActivitySplashBinding
import com.makeus.jfive.famo.src.auth.information.InformationActivity
import com.makeus.jfive.famo.src.main.MainActivity

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