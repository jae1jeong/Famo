package com.makeus.jfive.famo.src.auth.find

import android.os.Bundle
import com.makeus.jfive.famo.config.ApplicationClass
import com.makeus.jfive.famo.config.BaseActivity
import com.makeus.jfive.famo.databinding.ActivityFindUserBinding
import com.makeus.jfive.famo.src.auth.find.adapter.FindPagerAdapter
import com.makeus.jfive.famo.src.auth.findid.FindIdFragment
import com.makeus.jfive.famo.src.auth.findpassword.FindPasswordFragment
import com.makeus.jfive.famo.util.Constants

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