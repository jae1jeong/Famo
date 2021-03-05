package com.softsquared.template.kotlin.src.main.mypage

import android.os.Bundle
import android.view.View
import com.softsquared.template.kotlin.R
import com.softsquared.template.kotlin.config.BaseFragment
import com.softsquared.template.kotlin.databinding.FragmentMonthlyBinding
import com.softsquared.template.kotlin.databinding.FragmentMypageBinding

class MyPageFragment: BaseFragment<FragmentMypageBinding>(FragmentMypageBinding::bind,
        R.layout.fragment_mypage) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //이미지 앞으로 내보내기
        binding.myPageSetting.bringToFront()
    }
}