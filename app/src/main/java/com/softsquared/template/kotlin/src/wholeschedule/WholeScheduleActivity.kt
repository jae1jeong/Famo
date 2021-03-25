package com.softsquared.template.kotlin.src.wholeschedule

import android.os.Bundle
import android.util.Log
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

        binding.wholeScheduleXBtn.setOnClickListener {
//            val intent = Intent(this,MainActivity::class.java)
//            startActivity(intent)
            finish()
        }
    }

}