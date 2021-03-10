package com.softsquared.template.kotlin.src.main.mypage

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import com.bumptech.glide.Glide
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

    var token : String? = null
    var name : String? = null
    var img : String?= null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var extra = this.arguments
        if (extra != null) {
            extra = arguments
            token = extra?.getString("token")
            name = extra?.getString("name")
            img = extra?.getString("img").toString()
            Log.d("MyPageFragment 잘들어 왔나 token", "값: $token")
            Log.d("MyPageFragment 잘들어 왔나 name", "값: $name")
            Log.d("MyPageFragment 잘들어 왔나 img", "값: $img")
        }

        Glide.with(this).load(img)
            .centerCrop().into(binding.myPageImg)

        binding.myPageTvName.text = name

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