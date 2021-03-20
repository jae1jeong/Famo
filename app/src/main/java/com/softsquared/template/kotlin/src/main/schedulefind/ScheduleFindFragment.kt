package com.softsquared.template.kotlin.src.main.schedulefind

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.JsonElement
import com.softsquared.template.kotlin.R
import com.softsquared.template.kotlin.config.ApplicationClass
import com.softsquared.template.kotlin.config.BaseFragment
import com.softsquared.template.kotlin.config.BaseResponse
import com.softsquared.template.kotlin.databinding.FragmentScheduleFindBinding
import com.softsquared.template.kotlin.src.main.MainActivity
import com.softsquared.template.kotlin.src.main.category.CategoryEditActivity
import com.softsquared.template.kotlin.src.main.schedulefind.adapter.IScheduleCategoryRecyclerView
import com.softsquared.template.kotlin.src.main.schedulefind.adapter.ScheduleBookmarkAdapter
import com.softsquared.template.kotlin.src.main.schedulefind.adapter.ScheduleCategoryAdapter
import com.softsquared.template.kotlin.src.main.schedulefind.adapter.ScheduleWholeAdapter
import com.softsquared.template.kotlin.src.main.schedulefind.models.*
import com.softsquared.template.kotlin.src.schedulesearch.ScheduleSearchActivity
import com.softsquared.template.kotlin.src.wholeschedule.WholeScheduleActivity
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*
import kotlin.collections.ArrayList

class ScheduleFindFragment : BaseFragment<FragmentScheduleFindBinding>
    (FragmentScheduleFindBinding::bind, R.layout.fragment_schedule_find),
    IScheduleCategoryRecyclerView, CategoryInquiryView, ScheduleFindView {

    //카테고리 편집으로 보내줄 변수
    var name = ""
    var color = ""
    var size = 0
    var categoryID = ""

    private lateinit var scheduleCategoryAdapter: ScheduleCategoryAdapter
    // 전체 일정 어댑터
    private lateinit var scheduleWholeAdapter: ScheduleWholeAdapter

    private val partList: ArrayList<ScheduleWholeData> = arrayListOf()

    @SuppressLint("ResourceAsColor", "ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        //프래그먼트 이동간 gone/visibility설정
        (activity as MainActivity).fragmentSetting()

        binding.scheduleFindMainLinear.visibility = View.VISIBLE
        binding.scheduleFindMainFragment.visibility = View.GONE

//        레이아웃 마진 설정
//        val layout1 = binding.scheduleFindLinear
//        val plControl = layout1.layoutParams as RelativeLayout.LayoutParams
//        plControl.topMargin = 30
//        layout1.layoutParams = plControl
//        binding.scheduleFindLinear.marginTop.plus(30)

        // 즐겨찾기탭의 height 초기 값을 4로 지정
        binding.scheduleFindBookmarkView.layoutParams.height = 4

        // 일정찾기 탭 default설정 - 처음에는 즐겨찾기탭이 보이게, 카테고리 설정
        childFragmentManager.beginTransaction()
            .replace(R.id.schedule_find_fragment, ScheduleFindBookmarkFragment())
            .commit()

        // +버튼 클릭 시 카테고리 편집으로 이동
        binding.scheduleFindBtnCategory.setOnClickListener {
            val intent = Intent(activity, CategoryEditActivity::class.java)
            intent.putExtra("name", name)
            intent.putExtra("color", color)
            intent.putExtra("size", size)
            intent.putExtra("categoryID", categoryID)
            startActivity(intent)
//            (activity as MainActivity).replaceFragment(CategoryEditFragment.newInstance());
//            binding.scheduleFindLinear.visibility = View.GONE
        }

//        처음 시작은 즐겨찾기/최근 중 즐겨찾기로 선택되게끔
        binding.scheduleFindTvBookmark.setTextColor(Color.BLACK)
        binding.scheduleFindLatelyView.visibility = View.GONE

        // 즐겨찾기 리사이클러뷰
//        createBookmarkRecyclerview()
//        val mFragmentTransaction : FragmentTransaction = childFragmentManager.beginTransaction();

//        (activity as MainActivity).replaceFragment(ScheduleFindBookmarkFragment.newInstance());


        //검색창 클릭 시
        binding.scheduleFindBtn.setOnClickListener {
            val intent = Intent(activity,ScheduleSearchActivity::class.java)
            startActivity(intent)
        }


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
                R.id.schedule_find_fragment, ScheduleFindBookmarkFragment()).commit()
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

            // 즐겨찾기가 선택되어 있는 경우
            if (binding.scheduleFindBookmarkView.layoutParams.height == 4) {

                binding.scheduleFindLinear.visibility = View.GONE
                ApplicationClass.sSharedPreferences.edit().putBoolean("boolean", true).apply()
//                (activity as MainActivity).replaceFragment(ScheduleFindDetailFragment.newInstance());
                val intent = Intent(activity, WholeScheduleActivity::class.java)
                startActivity(intent)
            }

            //최근이 선택되어 있는 경우
            if (binding.scheduleFindLatelyView.layoutParams.height == 4) {
                binding.scheduleFindLinear.visibility = View.GONE
//                val boolean = true
//                bundle.putBoolean("boolean", boolean)
//                scheduleFindDetailFragment.arguments = bundle
                ApplicationClass.sSharedPreferences.edit().putBoolean("boolean", false).apply()
                (activity as MainActivity).replaceFragment(ScheduleFindDetailFragment.newInstance());
            }

//            binding.scheduleFindIvSearch.setOnClickListener {
//                Log.d("TAG", "일정찾기 이미지클릭 확인 ")
//                binding.scheduleFindMainFragment.visibility = View.GONE
//                binding.scheduleFindMainLinear.visibility = View.VISIBLE
//            }

            binding.scheduleFindIvSearch.setOnTouchListener { _, event ->
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        Log.d("TAG", "일정찾기 이미지클릭 확인 ")
                        binding.scheduleFindMainFragment.visibility = View.GONE
                        binding.scheduleFindMainLinear.visibility = View.VISIBLE
                    }
                }
                false
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



    override fun viewPagerApiRequest() {
        super.viewPagerApiRequest()
        // 카테고리
        CategoryInquiryService(this).tryGetUserCategoryInquiry()

        //전체일정
        ScheduleFindService(this).tryGetWholeScheduleInquiry(0,10)


    }


//
//    @SuppressLint("SimpleDateFormat")
//    fun createWholeScheduleRecyclerview() {
//        //전체일정 테스트데이터
//        val onlyDate: LocalDate = LocalDate.now()
//
////                binding.myPageEditEtComments.setText(onlyDate.toString())
//            val str = onlyDate.toString()
//            val format = SimpleDateFormat("YYYY-MM-DD")
//            val nowDate : Date? = format.parse(str)
//
//        val wholeList = arrayListOf(
//            ScheduleWholeData(
//                72, nowDate!!, "내용",
//                "내용",R.drawable.schedule_find_bookmark,1,"1"
//            ),
//            ScheduleWholeData(
//                1, nowDate!!, "내용",
//                "내용",R.drawable.schedule_find_bookmark,1,"1"
//            ),ScheduleWholeData(
//                1, nowDate!!, "내용",
//                "내용",R.drawable.schedule_find_bookmark,1,"1"
//            ),ScheduleWholeData(
//                1, nowDate!!, "내용",
//                "내용",R.drawable.schedule_find_bookmark,1,"1"
//            )
//        )
//
//        //전체일정 리사이큘러뷰 연결
//        binding.recyclerviewWhole.layoutManager =
//            GridLayoutManager(
//                context, 2, GridLayoutManager.VERTICAL,
//                false
//            )
//        binding.recyclerviewWhole.setHasFixedSize(true)
//        binding.recyclerviewWhole.adapter = ScheduleWholeAdapter(wholeList)
//    }

    //즐겨찾기 일정 테스트
    private fun createBookmarkRecyclerview() {
        //테스트 데이터
        val bookmarkList = arrayListOf(
                ScheduleBookmarkData("제목", "시간"),
                ScheduleBookmarkData("제목", "시간")
        )

        // 즐겨찾기/최근 일정 리사이클러뷰 연결
        binding.recyclerViewBookmark.layoutManager = LinearLayoutManager(
                context,
                LinearLayoutManager.VERTICAL, false
        )
        binding.recyclerViewBookmark.setHasFixedSize(true)
        binding.recyclerViewBookmark.adapter = ScheduleBookmarkAdapter(bookmarkList)
    }

    //최근 일정 테스트
    private fun createLatelyRecyclerview() {
        //테스트 데이터
        val latelyList = arrayListOf(
            ScheduleBookmarkData("제목", "시간"),
            ScheduleBookmarkData("제목", "시간")
        )

        // 즐겨찾기/최근 일정 리사이클러뷰 연결
        binding.recyclerViewBookmark.layoutManager = LinearLayoutManager(
            context,
            LinearLayoutManager.VERTICAL, false
        )
        binding.recyclerViewBookmark.setHasFixedSize(true)
        binding.recyclerViewBookmark.adapter = ScheduleBookmarkAdapter(latelyList)
    }

    //프래그먼트 간 이동하기 위한 선언
    companion object {
        fun newInstance(): ScheduleFindFragment {    // shs: 함수의 반환 형이 Fragment 형이라...
            return ScheduleFindFragment()
        }
    }

    //카테고리 클릭 시 카테고리별 일정으로 이동
    override fun onItemMoveBtnClicked(position: Int) {
        binding.scheduleFindMainLinear.visibility = View.GONE
        binding.scheduleFindMainFragment.visibility = View.VISIBLE
        Log.d("TAG", "onItemMoveBtnClicked: $position")
//        CategoryInquiryService(this).tryGetCategoryInquiry()
        val scheduleFindCategoryFragment = ScheduleFindCategoryFragment()
        val bundle = Bundle()
        bundle.putInt("categoryID", position)
        scheduleFindCategoryFragment.arguments = bundle
        childFragmentManager.beginTransaction()
            .replace(R.id.schedule_find_main_fragment, scheduleFindCategoryFragment)
            .commit()
    }

    //어댑터에서 color값을 가져오기위한 함수
    override fun onColor(): String {

        return color
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

        when (response.code) {
            100 -> {
                Log.d("TAG", "onGetWholeScheduleInquirySuccess 성공")
                binding.recyclerviewWhole.visibility = View.VISIBLE

                val wholeScheduleJsonArray = response.data
                Log.d("TAG", "wholeScheduleJsonArray: $wholeScheduleJsonArray")
                wholeScheduleJsonArray.forEach {
                    val wholeScheduleJsonObject = it.asJsonObject
                    val scheduleDate = wholeScheduleJsonObject.get("scheduleDate").asString
                    val scheduleName = wholeScheduleJsonObject.get("scheduleName").asString
                    //  일정 내용을 그냥 작성 안 하고 작성할 경우도 인지해야합니다. null로 들어올 수 있어요.
                    val scheduleContentJson:JsonElement? = wholeScheduleJsonObject.get("scheduleMemo")
                    val schedulePick = wholeScheduleJsonObject.get("schedulePick").asInt
                    // 컬러는 카테고리가 없이 작성이 가능해서 null로 들어올 수 있습니다.
                    val scheduleColorInfoJson:JsonElement? = wholeScheduleJsonObject.get("colorInfo")
                    val scheduleStatus = wholeScheduleJsonObject.get("scheduleStatus").asInt

                    val scheduleID = wholeScheduleJsonObject.get("scheduleID").asInt
                    var scheduleColorInfo = "#CED5D9"
                    var scheduleContent = ""

                    // json이기 때문에 그냥 null 체크하면 null인지 모르기 때문에 isJsonNull을 써주세요.
                    if(scheduleColorInfoJson?.isJsonNull!!.equals(false)){
                        // 널이 아닌경우 그대로 컬러 스트링을 넣어줍니다.
                        scheduleColorInfo = scheduleColorInfoJson.asString
                    }
                    if(scheduleContentJson?.isJsonNull!!){
                        // 마찬가지로 일정 내용이 있으면 수정하고 없으면 위에 빈 스트링으로 일정에 넣어줍니다.
                        scheduleContent = scheduleContentJson.asString
                    }
                    partList.add(ScheduleWholeData(scheduleID,scheduleDate,scheduleName,scheduleContent,schedulePick,scheduleStatus,scheduleColorInfo))
                }


                scheduleWholeAdapter = ScheduleWholeAdapter(partList)

                //전체일정 리사이큘러뷰 연결
                binding.recyclerviewWhole.layoutManager =
                        GridLayoutManager(
                                context, 2, GridLayoutManager.VERTICAL,
                                false
                        )
                binding.recyclerviewWhole.setHasFixedSize(true)
                binding.recyclerviewWhole.adapter = scheduleWholeAdapter


            }
            else -> {
                Log.d("TAG", "onGetWholeScheduleInquirySuccess 100이 아닌: ${response.message.toString()}")
            }
        }

    }

    override fun onGetWholeScheduleInquiryFail(message: String) {
    }

    override fun onPostBookmarkSuccess(response: BaseResponse) {

        when(response.code){
            100 -> {
                showCustomToast("즐겨찾기 성공")
                Log.d("TAG", "onPostBookmarkSuccess: 즐겨찾기 성공")
            }
            else -> {
                showCustomToast("즐겨찾기 실패")
                Log.d("TAG", "onPostBookmarkSuccess: 즐겨찾기 실패 ${response.message.toString()}")
            }
        }
    }

    override fun onPostBookmarkFail(message: String) {
    }
}