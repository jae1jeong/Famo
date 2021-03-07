package com.softsquared.template.kotlin.src.main.schedulefind

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.core.view.marginTop
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.softsquared.template.kotlin.R
import com.softsquared.template.kotlin.config.ApplicationClass
import com.softsquared.template.kotlin.config.BaseFragment
import com.softsquared.template.kotlin.databinding.FragmentScheduleFindBinding
import com.softsquared.template.kotlin.src.main.MainActivity
import com.softsquared.template.kotlin.src.main.category.CategoryFragment
import com.softsquared.template.kotlin.src.main.schedulefind.adapter.ScheduleBookmarkAdapter
import com.softsquared.template.kotlin.src.main.schedulefind.adapter.ScheduleCategoryAdapter
import com.softsquared.template.kotlin.src.main.schedulefind.adapter.ScheduleWholeAdapter
import com.softsquared.template.kotlin.src.main.schedulefind.models.ScheduleBookmarkData
import com.softsquared.template.kotlin.src.main.schedulefind.models.ScheduleCategoryData
import com.softsquared.template.kotlin.src.main.schedulefind.models.ScheduleWholeData


class ScheduleFindFragment : BaseFragment<FragmentScheduleFindBinding>
    (FragmentScheduleFindBinding::bind, R.layout.fragment_schedule_find) {

    // 각각의 Fragment마다 Instance를 반환해 줄 메소드를 생성합니다.

//    fun newInstance() : ScheduleFindFragment{
//        return newInstance()
//    }

    private val partList: ArrayList<ScheduleWholeData> = arrayListOf()

    @SuppressLint("ResourceAsColor")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //프래그먼트 이동간 gone/visibility설정
        (activity as MainActivity).fragmentSetting()

//        레이아웃 마진 설정
//        val layout1 = binding.scheduleFindLinear
//        val plControl = layout1.layoutParams as RelativeLayout.LayoutParams
//        plControl.topMargin = 30
//        layout1.layoutParams = plControl
//        binding.scheduleFindLinear.marginTop.plus(30)

        // 즐겨찾기의 height 초기 값을 4로 지정
        binding.scheduleFindBookmarkView.layoutParams.height = 4

        // 일정찾기 탭 default설정 - 처음에는 즐겨찾기탭이 보이게, 카테고리 설정
        createWholeScheduleRecyclerview()
        createCategoryRecyclerview()

        // +버튼 클릭 시 카테고리 편집으로 이동
        binding.scheduleFindBtnCategory.setOnClickListener {
            showCustomToast("여기옴??")
            (activity as MainActivity).replaceFragment(CategoryFragment.newInstance());
            binding.scheduleFindLinear.visibility = View.GONE
        }

        //처음 시작은 즐겨찾기/최근 중 즐겨찾기로 선택되게끔
//        binding.scheduleFindTvBookmark.setTextColor(Color.BLACK)
//        binding.scheduleFindLatelyView.visibility = View.GONE

        // 즐겨찾기 리사이클러뷰
//        createBookmarkRecyclerview()
//        val mFragmentTransaction : FragmentTransaction = childFragmentManager.beginTransaction();
        childFragmentManager.beginTransaction()
            .replace(R.id.schedule_find_fragment, ScheduleFindBookmarkFragment())
            .commit()

//        (activity as MainActivity).replaceFragment(ScheduleFindBookmarkFragment.newInstance());

        //즐겨찾기 탭
        binding.scheduleFindTvBookmark.setOnClickListener {
//            createBookmarkRecyclerview()
            binding.scheduleFindTvBookmark.setTextColor(Color.BLACK)
            binding.scheduleFindTvBookmark.setTypeface(null, Typeface.BOLD)
            binding.scheduleFindBookmarkView.setBackgroundColor(Color.BLACK)
            binding.scheduleFindBookmarkView.layoutParams.height = 4

            binding.scheduleFindTvLately.setTextColor(Color.GRAY)
            binding.scheduleFindTvLately.setTypeface(null, Typeface.NORMAL)
            binding.scheduleFindLatelyView.setBackgroundColor(Color.parseColor("#E1DDDD"))
            binding.scheduleFindLatelyView.layoutParams.height = 2

            childFragmentManager.beginTransaction().replace(
                R.id.schedule_find_fragment,
                ScheduleFindBookmarkFragment()
            )
                .commit()
//            (activity as MainActivity).replaceFragment(ScheduleFindBookmarkFragment.newInstance());
        }

        //최근 탭
        binding.scheduleFindTvLately.setOnClickListener {
//            createLatelyRecyclerview()
            binding.scheduleFindTvLately.setTextColor(Color.BLACK)
            binding.scheduleFindTvLately.setTypeface(null, Typeface.BOLD)
            binding.scheduleFindLatelyView.setBackgroundColor(Color.BLACK)
            binding.scheduleFindLatelyView.layoutParams.height = 4

            binding.scheduleFindTvBookmark.setTextColor(Color.GRAY)
            binding.scheduleFindTvBookmark.setTypeface(null, Typeface.NORMAL)
            binding.scheduleFindBookmarkView.setBackgroundColor(Color.parseColor("#E1DDDD"))
            binding.scheduleFindBookmarkView.layoutParams.height = 2

            childFragmentManager.beginTransaction().replace(
                R.id.schedule_find_fragment,
                ScheduleFindLatelyFragment()
            )
                .commit()
//            (activity as MainActivity).replaceFragment(ScheduleFindLatelyFragment.newInstance());
        }

        //자세히 보기 클릭 시
        binding.scheduleFindBtnDetail.setOnClickListener {
            val bundle = Bundle()
            val scheduleFindDetailFragment = ScheduleFindDetailFragment()

            if (binding.scheduleFindBookmarkView.layoutParams.height == 4) {
                binding.scheduleFindLinear.visibility = View.GONE
//                val boolean = true
//                bundle.putBoolean("boolean", boolean)
//                scheduleFindDetailFragment.arguments = bundle
                ApplicationClass.sSharedPreferences.edit().putBoolean("boolean",true).apply()
                (activity as MainActivity).replaceFragment(ScheduleFindDetailFragment.newInstance());
            }

            if (binding.scheduleFindLatelyView.layoutParams.height == 4) {
                binding.scheduleFindLinear.visibility = View.GONE
                ApplicationClass.sSharedPreferences.edit().putBoolean("boolean",false).apply()
                (activity as MainActivity).replaceFragment(ScheduleFindDetailFragment.newInstance());
            }




        }

        // viewPager
//        val adapter = ScheduleFindPagerAdapter(supportFragmentManager as FragmentManager)
//        adapter.addFragment(MonthlyFragment(),"월간")
//        adapter.addFragment(TodayFragment(),"오늘")
//        adapter.addFragment(ScheduleFindFragment(),"일정 찾기")
//        binding.mainViewPager.adapter = adapter
//        binding.mainTabLayout.setupWithViewPager(binding.mainViewPager)

    }

    fun createWholeScheduleRecyclerview() {
        //테스트 데이터
        val wholeList = arrayListOf(
            ScheduleWholeData(
                "2021.02.10", "제목", "내용",
                R.drawable.schedule_find_bookmark
            ),
            ScheduleWholeData(
                "2021.02.10", "제목2", "내용2",
                R.drawable.schedule_find_bookmark
            ),
            ScheduleWholeData(
                "2021.02.10", "제목3", "내용3",
                R.drawable.schedule_find_bookmark
            ),
            ScheduleWholeData(
                "2021.02.10", "제목4", "내용4",
                R.drawable.schedule_find_bookmark
            )
        )

        //전체일정 리사이큘러뷰 연결
        binding.recyclerviewWhole.layoutManager =
            GridLayoutManager(
                context, 2, GridLayoutManager.VERTICAL,
                false
            )
        binding.recyclerviewWhole.setHasFixedSize(true)
        binding.recyclerviewWhole.adapter = ScheduleWholeAdapter(wholeList)
    }

    fun createCategoryRecyclerview() {
        //테스트 데이터
        val categoryList = arrayListOf(
            ScheduleCategoryData("학교"),
            ScheduleCategoryData("알바"),
            ScheduleCategoryData("친구")
        )

        binding.recyclerviewCategory.layoutManager = LinearLayoutManager(
            context, LinearLayoutManager.HORIZONTAL, false
        )
        binding.recyclerviewCategory.setHasFixedSize(true)
        binding.recyclerviewCategory.adapter = ScheduleCategoryAdapter(categoryList)
    }

//    private fun createBookmarkRecyclerview() {
//        //테스트 데이터
//        val bookmarkList = arrayListOf(
//                ScheduleBookmarkData("제목", "시간"),
//                ScheduleBookmarkData("제목", "시간")
//        )
//
//        // 즐겨찾기/최근 일정 리사이클러뷰 연결
//        binding.recyclerViewBookmark.layoutManager = LinearLayoutManager(
//                context,
//                LinearLayoutManager.VERTICAL, false
//        )
//        binding.recyclerViewBookmark.setHasFixedSize(true)
//        binding.recyclerViewBookmark.adapter = ScheduleBookmarkAdapter(bookmarkList)
//    }
//
//    private fun createLatelyRecyclerview() {
//        //테스트 데이터
//        val latelyList = arrayListOf(
//            ScheduleBookmarkData("제목", "시간"),
//            ScheduleBookmarkData("제목", "시간")
//        )
//
//        // 즐겨찾기/최근 일정 리사이클러뷰 연결
//        binding.recyclerViewBookmark.layoutManager = LinearLayoutManager(
//            context,
//            LinearLayoutManager.VERTICAL, false
//        )
//        binding.recyclerViewBookmark.setHasFixedSize(true)
//        binding.recyclerViewBookmark.adapter = ScheduleBookmarkAdapter(latelyList)
//    }

    //프래그먼트 간 이동하기 위한 선언
    companion object {
        fun newInstance(): ScheduleFindFragment {    // shs: 함수의 반환 형이 Fragment 형이라...
            return ScheduleFindFragment()
        }
    }
}