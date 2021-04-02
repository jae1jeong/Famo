package com.softsquared.template.kotlin.src.auth.findresult

import android.os.Bundle
import com.softsquared.template.kotlin.config.BaseActivity
import com.softsquared.template.kotlin.databinding.ActivityFindResultBinding
import com.softsquared.template.kotlin.util.Constants

class FindResultActivity:BaseActivity<ActivityFindResultBinding>(ActivityFindResultBinding::inflate) {
    override fun onCreate(savedInstanceState: Bundle?) {
//        setSupportActionBar(binding.findResultToolbar)
//        supportActionBar?.setDisplayHomeAsUpEnabled(true)
//        supportActionBar?.setDisplayShowTitleEnabled(false)

        super.onCreate(savedInstanceState)
        val loginId = intent.getStringExtra(Constants.FIND_USER_ID)
        if(loginId == null){
            binding.findResultTextTitle.text = "고객님은 SNS로 가입하셨습니다."
            binding.findResultTextResult.text = "카카오로 로그인해주세요"
        }else{
            binding.findResultTextTitle.text = "회원님의 아이디입니다."
            binding.findResultTextResult.text = loginId
            showCustomToast(loginId)
        }
    }
}