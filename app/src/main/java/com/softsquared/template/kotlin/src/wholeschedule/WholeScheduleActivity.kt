package com.softsquared.template.kotlin.src.wholeschedule

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.FrameLayout
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.softsquared.template.kotlin.config.BaseActivity
import com.softsquared.template.kotlin.databinding.ActivityWholeScheduleBinding
import com.softsquared.template.kotlin.src.wholeschedule.adapter.WholeSchedulePagerAdapter
import com.softsquared.template.kotlin.src.wholeschedule.bookmark.WholeScheduleBookmarkFragment
import com.softsquared.template.kotlin.src.wholeschedule.lately.WholeLatelyScheduleFragment

class WholeScheduleActivity : BaseActivity<ActivityWholeScheduleBinding>
    (ActivityWholeScheduleBinding::inflate) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val check = intent.getBooleanExtra("boolean",true)
        Log.d("TAG", "WholeScheduleActivity : $check")

        val scheduleFindBookmarkFragment = WholeScheduleBookmarkFragment()
        val scheduleFindLatelyFragment = WholeLatelyScheduleFragment()
        val bundle = Bundle()
        bundle.getBoolean("boolean",check)
        scheduleFindBookmarkFragment.arguments = bundle
        scheduleFindLatelyFragment.arguments = bundle

        // viewPager
        val adapter = WholeSchedulePagerAdapter(supportFragmentManager)
        adapter.addFragment(scheduleFindBookmarkFragment,"즐겨찾기")
        adapter.addFragment(scheduleFindLatelyFragment,"최근")
        binding.wholeScheduleViewPager.adapter = adapter
        binding.wholeScheduleTabLayout.setupWithViewPager(binding.wholeScheduleViewPager)

        binding.wholeScheduleTabLayout.setSelectedTabIndicatorColor(Color.parseColor("#000000")); // 밑줄색
        binding.wholeScheduleTabLayout.setSelectedTabIndicatorHeight(9); // 밑줄높이(두께)

        //X버튼
        binding.wholeScheduleXBtn.setOnClickListener {
            finish()
        }
    }

}