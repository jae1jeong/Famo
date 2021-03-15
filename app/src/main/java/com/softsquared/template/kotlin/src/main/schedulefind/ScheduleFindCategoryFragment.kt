package com.softsquared.template.kotlin.src.main.schedulefind

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.PopupMenu
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.softsquared.template.kotlin.R
import com.softsquared.template.kotlin.config.BaseFragment
import com.softsquared.template.kotlin.databinding.FragmentScheduleFindBookmarkBinding
import com.softsquared.template.kotlin.databinding.FragmentScheduleFindCategoryBinding
import com.softsquared.template.kotlin.databinding.FragmentScheduleFindLatelyBinding
import com.softsquared.template.kotlin.src.main.MainActivity
import com.softsquared.template.kotlin.src.main.schedulefind.adapter.ScheduleBookmarkAdapter
import com.softsquared.template.kotlin.src.main.schedulefind.adapter.ScheduleWholeAdapter
import com.softsquared.template.kotlin.src.main.schedulefind.models.CategoryInquiryResponse
import com.softsquared.template.kotlin.src.main.schedulefind.models.ScheduleBookmarkData
import com.softsquared.template.kotlin.src.main.schedulefind.models.ScheduleWholeData
import com.softsquared.template.kotlin.src.main.schedulefind.models.UserCategoryInquiryResponse

class ScheduleFindCategoryFragment : BaseFragment<FragmentScheduleFindCategoryBinding>(
    FragmentScheduleFindCategoryBinding::bind, R.layout.fragment_schedule_find_category
),CategoryInquiryView {

    companion object {
        fun newInstance(): ScheduleFindCategoryFragment {    // shs: 함수의 반환 형이 Fragment 형이라...
            return ScheduleFindCategoryFragment()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        CategoryInquiryService(this).tryGetCategoryInquiry()

        createRecyclerview()

//        (activity as MainActivity).onBackPressed()

        binding.categoryFilter.setOnClickListener {
            val popup = PopupMenu(activity, binding.categoryFilter)

            (activity as MainActivity).menuInflater.inflate(R.menu.schedule_find_filter, popup.menu)

            popup.setOnMenuItemClickListener(PopupListener())

            popup.show()
        }
    }

    inner class PopupListener : PopupMenu.OnMenuItemClickListener {

        override fun onMenuItemClick(item: MenuItem?): Boolean {

//            val intent = Intent(this@MainActivity,ShareActivity::class.java)

//            val intent = Intent(Intent.ACTION_SEND)
//            intent.type = "text/plain"
//            intent.putExtra(Intent.EXTRA_TEXT, "공유하기 테스트") // text는 공유하고 싶은 글자

//            val chooser = Intent.createChooser(intent, "공유하기")

            when (item?.itemId) {
                R.id.item1 -> println("aaaaaaaaaaaa")

//                    startActivity(chooser)
//                    startActivity(intent)

            }

            return false
        }
    }

    private fun createRecyclerview() {
        //테스트 데이터
        val categorySelectList = arrayListOf(
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

        binding.recyclerviewScheduleFindCategory.layoutManager =
            GridLayoutManager(
                context, 2, GridLayoutManager.VERTICAL,
                false
            )
        binding.recyclerviewScheduleFindCategory.setHasFixedSize(true)
        binding.recyclerviewScheduleFindCategory.adapter = ScheduleWholeAdapter(categorySelectList)
    }

    override fun onGetUserCategoryInquirySuccess(responseUser: UserCategoryInquiryResponse) {
    }

    override fun onGetUserCategoryInquiryFail(message: String) {
    }

    override fun onGetCategoryInquirySuccess(categoryInquiryResponse: CategoryInquiryResponse) {
        when(categoryInquiryResponse.code){
            100 -> {
                Log.d("TAG", "onGetCategoryInquirySuccess: 카레고리별일정조회 성공")

            }
        }
    }

    override fun onGetCategoryInquiryFail(message: String) {
    }
}