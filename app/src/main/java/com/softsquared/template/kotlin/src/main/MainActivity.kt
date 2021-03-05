package com.softsquared.template.kotlin.src.main

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.tabs.TabLayout
import com.softsquared.template.kotlin.R
import com.softsquared.template.kotlin.config.BaseActivity
import com.softsquared.template.kotlin.databinding.ActivityMainBinding
import com.softsquared.template.kotlin.src.main.adapter.MainPagerAdapter
import com.softsquared.template.kotlin.src.main.addmemo.AddMemoFragment
import com.softsquared.template.kotlin.src.main.monthly.MonthlyFragment
import com.softsquared.template.kotlin.src.main.schedulefind.ScheduleFindFragment
import com.softsquared.template.kotlin.src.main.today.TodayFragment

class MainActivity : BaseActivity<ActivityMainBinding>(ActivityMainBinding::inflate) {
    private var clicked = false // FAB 버튼 변수
    // FAB 버튼 애니메이션
    private val fromBottom: Animation by lazy { AnimationUtils.loadAnimation(this,R.anim.from_bottom_anim) }
    private val toBottom: Animation by lazy { AnimationUtils.loadAnimation(this,R.anim.to_bottom_anim) }




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // viewPager
        val adapter = MainPagerAdapter(supportFragmentManager)
        adapter.addFragment(MonthlyFragment(),"월간")
        adapter.addFragment(TodayFragment(),"오늘")
        adapter.addFragment(ScheduleFindFragment(),"일정 찾기")
        binding.mainViewPager.adapter = adapter
        binding.mainTabLayout.setupWithViewPager(binding.mainViewPager)



        // FAB 메인 액션 버튼
        binding.mainBtnActionMain.setOnClickListener {
            onActionButtonClicked()
        }

        // FAB 글쓰기 버튼
        binding.mainBtnActionSub.setOnClickListener {
            showBottomAddScheduleSheetDialog()
        }

        // 다른 부분을 눌렀을때 FAB 버튼 비활성화
        binding.mainLayout.setOnClickListener {
            if(clicked){
                onActionButtonClicked()
            }
        }


    }

    // FAB 액션 버튼 메서드
    private fun onActionButtonClicked() {
        setVisibility(clicked)
        setAnimation(clicked)
        clicked  = !clicked
    }

    // FAB 액션 버튼 애니메이션
    private fun setAnimation(clicked: Boolean) {
        if(!clicked){
            binding.mainBtnActionSub.startAnimation(fromBottom)
        }else{
            binding.mainBtnActionSub.startAnimation(toBottom)
        }
    }

    // FAB 액션 버튼 비저빌리티
    private fun setVisibility(clicked:Boolean){
        if(!clicked){
            binding.mainBtnActionSub.visibility = View.VISIBLE
            binding.mainFrameLayout.setBackgroundColor(resources.getColor(R.color.transparent_black))
        }else{
            binding.mainBtnActionSub.visibility = View.GONE
            binding.mainFrameLayout.setBackgroundColor(Color.TRANSPARENT)
        }
    }


    // 프래그먼트에서도 BottomSheetDialog를 호출할 수 있게 메서드로 작성
    fun showBottomAddScheduleSheetDialog(){
        val sheet = AddMemoFragment()
        sheet.show(supportFragmentManager,"AddMemoFragment")
    }
}