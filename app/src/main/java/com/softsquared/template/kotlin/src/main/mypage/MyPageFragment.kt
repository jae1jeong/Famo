package com.softsquared.template.kotlin.src.main.mypage

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.softsquared.template.kotlin.R
import com.softsquared.template.kotlin.config.BaseFragment
import com.softsquared.template.kotlin.databinding.FragmentMonthlyBinding
import com.softsquared.template.kotlin.databinding.FragmentMypageBinding
import com.softsquared.template.kotlin.src.main.MainActivity
import com.softsquared.template.kotlin.src.main.category.CategoryFragment
import com.softsquared.template.kotlin.src.main.mypage.edit.MyPageEditFragment
import com.softsquared.template.kotlin.src.main.schedulefind.ScheduleFindFragment
import java.lang.reflect.Array.newInstance

class MyPageFragment(val myPageActivityView: MyPageActivityView): BaseFragment<FragmentMypageBinding>(FragmentMypageBinding::bind,
        R.layout.fragment_mypage) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //이미지 앞으로 내보내기
        binding.myPageSetting.bringToFront()

        //프로필 편집으로 이동
        binding.myPageImg.setOnClickListener {
//            val intent = Intent(activity,MyPageActivity::class.java)
//            startActivity(intent)
            myPageActivityView.moveMyPageEdit()
        }

        //뒤로가기
        binding.myPageBtnBack.setOnClickListener {
//            binding.categoryEditLinear.visibility = View.GONE
//            val intent = Intent(context,MyPageActivity::class.java)
//            startActivity(intent)
            (activity as MyPageActivity).moveScheduleFind()
        }
    }

}