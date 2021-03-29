package com.softsquared.template.kotlin.src.main.schedulefind

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.lakue.pagingbutton.OnPageSelectListener
import com.softsquared.template.kotlin.R
import com.softsquared.template.kotlin.config.ApplicationClass
import com.softsquared.template.kotlin.config.BaseFragment
import com.softsquared.template.kotlin.config.BaseResponse
import com.softsquared.template.kotlin.databinding.FragmentScheduleFindBinding
import com.softsquared.template.kotlin.src.main.MainActivity
import com.softsquared.template.kotlin.src.main.category.CategoryEditActivity
import com.softsquared.template.kotlin.src.main.schedulefind.adapter.*
import com.softsquared.template.kotlin.src.main.schedulefind.models.*
import com.softsquared.template.kotlin.src.searchhistories.ScheduleSearchActivity
import com.softsquared.template.kotlin.src.wholeschedule.models.LatelyScheduleInquiryResponse
import com.softsquared.template.kotlin.util.Constants
import java.util.*
import kotlin.collections.ArrayList

class ScheduleFindFragment() : BaseFragment<FragmentScheduleFindBinding>
    (FragmentScheduleFindBinding::bind, R.layout.fragment_schedule_find),
    IScheduleCategoryRecyclerView, CategoryInquiryView, ScheduleFindView,
    ScheduleBookmarkView {

    //카테고리 편집으로 보내줄 변수
    var name = ""
    var color = ""
    var size = 0
    var categoryID = ""

    //전체페이징수
    var wholePagingCnt = 0

    var pagingCnt = 1

    private lateinit var scheduleCategoryAdapter: ScheduleCategoryAdapter

    // 전체 일정 어댑터
    private lateinit var scheduleWholeAdapter: ScheduleWholeAdapter

    private val partList: ArrayList<ScheduleWholeData> = arrayListOf()

    @SuppressLint("ResourceAsColor", "ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //처음에는 메인fragment
        childFragmentManager.beginTransaction()
            .replace(R.id.schedule_find_main_fragment, ScheduleFindMainFragment())
            .commit()

//        ScheduleFindService(this).tryGetWholeScheduleCount()

//         검색
//        if (word != null){
//            word = ApplicationClass.sSharedPreferences.getString(Constants.SEARCHWROD,null)!!
////            ScheduleFindService(this).tryGetScheduleSearch(searchWord)
//        }


//        val searchWord = ""

        val token =
            ApplicationClass.sSharedPreferences.getString(ApplicationClass.X_ACCESS_TOKEN, null)
                .toString()
        Log.d("TAG", "일정찾기 홈 $token")

        val word = ApplicationClass.sSharedPreferences.getString(Constants.SEARCHWROD, null)

        //검색
        if (word != null){
            binding.scheduleFindBtn.setText(word)
        }

        //검색창 클릭 시
        binding.scheduleFindBtn.setOnClickListener {
            val intent = Intent(activity, ScheduleSearchActivity::class.java)
            startActivity(intent)
        }

        // +버튼 클릭 시 카테고리 편집으로 이동
        binding.scheduleFindBtnCategory.setOnClickListener {
            val intent = Intent(activity, CategoryEditActivity::class.java)
            intent.putExtra("name", name)
            intent.putExtra("color", color)
            intent.putExtra("size", size)
            intent.putExtra("categoryID", categoryID)
            startActivity(intent)
        }

        binding.scheduleFindIvSearch.setOnClickListener {
            childFragmentManager.beginTransaction()
                .replace(R.id.schedule_find_main_fragment, ScheduleFindMainFragment())
                .commit()
            CategoryInquiryService(this).tryGetUserCategoryInquiry()
        }

    }


    override fun viewPagerApiRequest() {
        super.viewPagerApiRequest()
        //카테고리
        CategoryInquiryService(this).tryGetUserCategoryInquiry()
    }

    //프래그먼트 간 이동하기 위한 선언
    companion object {
        fun newInstance(): ScheduleFindFragment {    // shs: 함수의 반환 형이 Fragment 형이라...
            return ScheduleFindFragment()
        }
    }

    //카테고리 클릭 시 카테고리별 일정으로 이동
    override fun onItemMoveBtnClicked(scheduleCategoryID: Int) {
        val scheduleFindCategoryFragment = ScheduleFindCategoryFragment()
        val bundle = Bundle()
//        bundle.putInt("categoryID", position)
        bundle.putInt("scheduleCategoryID", scheduleCategoryID)
        scheduleFindCategoryFragment.arguments = bundle
        childFragmentManager.beginTransaction()
            .replace(R.id.schedule_find_main_fragment, scheduleFindCategoryFragment)
            .commit()
    }

    override fun onMoveFilterFragment(scheduleCategoryID: Int) {
        val scheduleFindFilterBottomDialogBinding =
            SchedulefindFilterBottomDialogFragment()
        scheduleFindFilterBottomDialogBinding.show(
            fragmentManager!!, scheduleFindFilterBottomDialogBinding.tag
        )
    }

    //클릭 시 카테고리 색상변경을 위한 카테고리 색상을 가져와서 분배하는 작업
    //어댑터에서 color값을 가져오기위한 함수
    override fun onColor(): ArrayList<String> {

        var size = 0
        var colorList: List<String>? = null
        val colorStrList = ArrayList<String>()
        val colorID = ArrayList<Int>()
        colorList = color.split(":")

        for (i in color.indices) {
            if (color.substring(i, i + 1) == ":") {
                size++
            }
        }
        Log.d("로그", "사이즈 : $size")

        for (i in 0 until size) {

            if (colorList[i] == "#FF8484") {
                colorStrList!!.add("#FF8484")
                colorID.add(1)
            }
            if (colorList[i] == "#FCBC71") {
                colorStrList!!.add("#FCBC71")
                colorID.add(2)
            }
            if (colorList[i] == "#FCDC71") {
                colorStrList!!.add("#FCDC71")
                colorID.add(3)
            }
            if (colorList[i] == "#C6EF84") {
                colorStrList!!.add("#C6EF84")
                colorID.add(4)
            }
            if (colorList[i] == "#7ED391") {
                colorStrList!!.add("#7ED391")
                colorID.add(5)
            }
            if (colorList[i] == "#93EAD9") {
                colorStrList!!.add("#93EAD9")
                colorID.add(6)
            }
            if (colorList[i] == "#7CC3FF") {
                colorStrList!!.add("#7CC3FF")
                colorID.add(7)
            }
            if (colorList[i] == "#6D92F7") {
                colorStrList!!.add("#6D92F7")
                colorID.add(8)
            }
            if (colorList[i] == "#AB93FA") {
                colorStrList!!.add("#AB93FA")
                colorID.add(9)
            }
            if (colorList[i] == "#FFA2BE") {
                colorStrList!!.add("#FFA2BE")
                colorID.add(10)
            }

        }

        return colorStrList
    }

    //유저별 카테고리조회 성공
    override fun onGetUserCategoryInquirySuccess(responseUser: UserCategoryInquiryResponse) {
        when (responseUser.code) {
            100 -> {
                Log.d("TAG", "onGetCategoryInquirySuccess: 카테고리조회성공")
                val categoryList: ArrayList<ScheduleCategoryData> = arrayListOf()

                for (i in 0 until responseUser.data.size) {
                    categoryList.add(
                        ScheduleCategoryData(
                            responseUser.data[i].categoryID,
                            responseUser.data[i].categoryName,
                            "#00000000"
                        )
                    )
//                    #00000000

                    name += responseUser.data[i].categoryName + ":"
                    color += responseUser.data[i].colorInfo + ":"
                    size = responseUser.data.size

                    categoryID += "${responseUser.data[i].categoryID}:"
                }

                Log.d("TAG", "name: $name")
                Log.d("TAG", "color: $color")
                binding.recyclerviewCategory.layoutManager = LinearLayoutManager(
                    context, LinearLayoutManager.HORIZONTAL, false
                )
                binding.recyclerviewCategory.setHasFixedSize(true)
                binding.recyclerviewCategory.adapter = ScheduleCategoryAdapter(categoryList, this)
//                scheduleCategoryAdapter.notifyDataSetChanged()

            }
            else -> {
                showCustomToast("실패 메시지 : ${responseUser.message}")
            }
        }
    }

    override fun onGetUserCategoryInquiryFail(message: String) {
    }

    override fun onGetCategoryInquirySuccess(categoryInquiryResponse: CategoryInquiryResponse) {

    }

    override fun onGetCategoryInquiryFail(message: String) {
    }

    //전체일정조회 성공
    override fun onGetWholeScheduleInquirySuccess(response: WholeScheduleInquiryResponse) {

    }

    override fun onGetWholeScheduleInquiryFail(message: String) {
    }

    override fun onPostBookmarkSuccess(response: BaseResponse) {

    }

    override fun onPostBookmarkFail(message: String) {
    }

    override fun onGetWholeScheduleCountSuccess(response: WholeScheduleCountResponse) {

    }

    override fun onGetWholeScheduleCountFailure(message: String) {
    }

    override fun onGetLatelyScheduleFindInquirySuccess(response: LatelyScheduleInquiryResponse) {


    }

    override fun onGetLatelySchedulefindInquiryFail(message: String) {
    }

    override fun onGetTodayRestScheduleSuccess(response: TodayRestScheduleResponse) {

    }

    override fun onGetTodayRestScheduleFail(message: String) {
    }

    override fun onGetScheduleSearchSuccess(response: ScheduleSearchResponse) {

    }

    override fun onGetScheduleSearchFail(message: String) {
    }

    override fun onGetScheduleBookmarkSuccess(response: ScheduleBookmarkResponse) {

    }

    override fun onGetScheduleBookmarkFail(message: String) {
    }


    override fun onResume() {
        super.onResume()

        val word: String? =
            ApplicationClass.sSharedPreferences.getString(Constants.SEARCHWROD, null)
        Log.d("TAG", "onResume: ㅇㅇㅇㅇ $word")

        if (word != null) {
            Log.d("TAG", "onResume: 안 $word")

            if (word!!.length > 0) {
                val scheduleFindCategoryFragment = ScheduleFindCategoryFragment()
                childFragmentManager.beginTransaction()
                    .replace(R.id.schedule_find_main_fragment, scheduleFindCategoryFragment)
                    .commit()
            }
        }

    }

}