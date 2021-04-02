package com.softsquared.template.kotlin.src.auth.find

import android.os.Bundle
import com.softsquared.template.kotlin.config.ApplicationClass
import com.softsquared.template.kotlin.config.BaseActivity
import com.softsquared.template.kotlin.databinding.ActivityFindUserBinding
import com.softsquared.template.kotlin.src.auth.find.adapter.FindPagerAdapter
import com.softsquared.template.kotlin.src.auth.findid.FindIdFragment
import com.softsquared.template.kotlin.src.auth.findpassword.FindPasswordFragment
import com.softsquared.template.kotlin.util.Constants

class FindUserActivity:BaseActivity<ActivityFindUserBinding>(ActivityFindUserBinding::inflate) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // toolbar
        setSupportActionBar(binding.findUserToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        // viewPager
        val findUserAdapter = FindPagerAdapter(supportFragmentManager)
        findUserAdapter.addFragment(FindIdFragment(),"아이디 찾기")
        findUserAdapter.addFragment(FindPasswordFragment(),"비밀번호 찾기")
        binding.findUserViewpager.adapter = findUserAdapter
        binding.findUserTabLayout.setupWithViewPager(binding.findUserViewpager)

        val idPwdCheck = ApplicationClass.sSharedPreferences.getString(Constants.ID_PASSWORD_CHECH,null)
        if (idPwdCheck != null){

            if (idPwdCheck == "id"){
                binding.findUserViewpager.currentItem = 0
            }else{
                binding.findUserViewpager.currentItem = 1
            }
        }

    }
}