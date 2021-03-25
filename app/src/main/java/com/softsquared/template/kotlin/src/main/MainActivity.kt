package com.softsquared.template.kotlin.src.main

import android.content.Intent
import android.graphics.Point
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager.widget.ViewPager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.gson.JsonElement
import com.softsquared.template.kotlin.R
import com.softsquared.template.kotlin.config.ApplicationClass
import com.softsquared.template.kotlin.config.BaseActivity
import com.softsquared.template.kotlin.config.BaseResponse
import com.softsquared.template.kotlin.databinding.ActivityMainBinding
import com.softsquared.template.kotlin.src.main.adapter.MainCategoryAdapter
import com.softsquared.template.kotlin.src.main.adapter.MainPagerAdapter
import com.softsquared.template.kotlin.src.main.category.CategoryEditActivity
import com.softsquared.template.kotlin.src.main.models.DetailMemoResponse
import com.softsquared.template.kotlin.src.main.models.MainScheduleCategory
import com.softsquared.template.kotlin.src.main.models.PatchMemo
import com.softsquared.template.kotlin.src.main.models.PostTodayRequestAddMemo
import com.softsquared.template.kotlin.src.main.monthly.MonthlyFragment
import com.softsquared.template.kotlin.src.main.schedulefind.*
import com.softsquared.template.kotlin.src.main.schedulefind.adapter.IScheduleCategoryRecyclerView
import com.softsquared.template.kotlin.src.main.schedulefind.models.CategoryInquiryResponse
import com.softsquared.template.kotlin.src.main.schedulefind.models.UserCategoryInquiryResponse
import com.softsquared.template.kotlin.src.main.today.TodayFragment
import com.softsquared.template.kotlin.src.main.today.TodayService
import com.softsquared.template.kotlin.src.main.today.TodayView
import com.softsquared.template.kotlin.src.main.today.models.MemoItem
import com.softsquared.template.kotlin.src.main.today.models.ScheduleItemsResponse
import com.softsquared.template.kotlin.src.main.today.models.TopCommentResponse
import com.softsquared.template.kotlin.src.mypage.MyPageActivity
import com.softsquared.template.kotlin.util.Constants


class MainActivity() : BaseActivity<ActivityMainBinding>(ActivityMainBinding::inflate),
    AddMemoView, TodayView, CategoryInquiryView, IScheduleCategoryRecyclerView{
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<FrameLayout>
    private val categoryList: ArrayList<MainScheduleCategory> = arrayListOf()
    lateinit var categoryScheduleAdapter: MainCategoryAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        CategoryInquiryService(this).tryGetUserCategoryInquiry()

        // viewPager
        val adapter = MainPagerAdapter(supportFragmentManager)
        adapter.addFragment(MonthlyFragment(), "월간")
        adapter.addFragment(TodayFragment(), "오늘")
        adapter.addFragment(ScheduleFindFragment(), "일정 찾기")
        binding.mainViewPager.adapter = adapter
        binding.mainTabLayout.setupWithViewPager(binding.mainViewPager)

        //유저 이미지 클릭 시 마이페이지로 이동
        binding.mainImageProfile.setOnClickListener {
            val intent = Intent(this, MyPageActivity::class.java)
            startActivity(intent)
        }

        // 디바이스 화면 높이 구하기
        val display = windowManager.defaultDisplay
        val size = Point()
        display.getRealSize(size)
        val deviceHeight = size.y
        // 탭 레이아웃 높이와 디바이스 화면 높이 빼기
        val bottomSheetDialogHeight = deviceHeight - 470
        val params = binding.mainFrameBottomSheet.layoutParams
        params.height = bottomSheetDialogHeight

        // 바텀 시트 다이얼로그
        bottomSheetBehavior = BottomSheetBehavior.from(binding.mainFrameBottomSheet)

        bottomSheetBehavior.apply {
            peekHeight = 800
            isHideable = true
            this.state = BottomSheetBehavior.STATE_HIDDEN
        }.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_EXPANDED -> {
                        if (Constants.IS_EDIT) {
                            val editScheduleID = ApplicationClass.sSharedPreferences.getInt(
                                Constants.EDIT_SCHEDULE_ID,
                                -1
                            )
                            if (editScheduleID != -1) {
                                AddMemoService(this@MainActivity).tryGetDetailMemo(
                                    editScheduleID
                                )
                            }
                        }
                    }
                    BottomSheetBehavior.STATE_COLLAPSED -> {
                        if (Constants.IS_EDIT) {
                            Constants.IS_EDIT = false
                        }
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {

                // 바텀 시트 다이얼로그 절반 이하인 상태일때
                if (slideOffset < 0.5) {
                    binding.addMemoDialogBtnOk.visibility = View.VISIBLE
                    binding.addMemoImageScroll.setImageResource(R.drawable.today_write_up_arrow)

                }
                // 바텀 시트 다이얼로그 절반 이상인 상태일때
                else {
                    binding.addMemoImageScroll.setImageResource(R.drawable.today_write_down_arrow)
                    binding.addMemoDialogBtnOk.visibility = View.GONE
                }
            }

        })

        // 바텀 시트 다이얼로그 카테고리 추가 버튼
        binding.addMemoBtnCategoryAdd.setOnClickListener {
            startActivity(Intent(this, CategoryEditActivity::class.java))
        }


        // FAB 버튼
        binding.mainFloatingBtn.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }

        // 바텀시트 다이얼로그 방향키
        binding.addMemoImageScroll.setOnClickListener {
            stateChangeBottomSheet(Constants.EXPAND)
        }

        // 바텀시트 다이얼로그 저장 버튼
        binding.addMemoDialogBtnSave.setOnClickListener {

            if (Constants.IS_EDIT) {
                // 수정하기
                val editScheduleID =
                    ApplicationClass.sSharedPreferences.getInt(Constants.EDIT_SCHEDULE_ID, -1)
                if (editScheduleID != -1) {
                    showLoadingDialog(this)
                    AddMemoService(this).tryPatchMemo(
                        editScheduleID,
                        PatchMemo(
                            binding.addMemoEditTitle.text.toString(),
                            null,
                            null,
                            binding.addMemoEditContent.text.toString()
                        )
                    )
                } else {
                    showCustomToast("수정 오류 스케줄 아이디를 볼러오지 못하였습니다.")
                }
            } else {
                // 일정 추가하기
                showLoadingDialog(this)
                AddMemoService(this).tryPostAddMemo(
                    PostTodayRequestAddMemo(
                        binding.addMemoEditTitle.text.toString(),
                        binding.addMemoEditContent.text.toString(),
                        1
                    )
                )
            }
        }


    }


    // 바텀시트 다이얼로그 상태 관리
    fun stateChangeBottomSheet(state: String) {
        when (state) {
            Constants.EXPAND -> {
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
            }
            Constants.COLLASPE -> {
                bottomSheetBehavior.peekHeight = 350
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
            }
            Constants.HIDE_SHEET -> {
                bottomSheetBehavior.peekHeight = 200
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
            }
        }
    }

    fun onMoveScheduleFind(searchWord: String) {
        Log.d("TAG", "함수호출확인 ")
        val adapter = MainPagerAdapter(supportFragmentManager)
        binding.mainViewPager.adapter = adapter
        binding.mainViewPager.currentItem = 2
    }


    fun replaceFragment(fragment: Fragment) {
        val adapter = MainPagerAdapter(supportFragmentManager)
        adapter.addFragment(fragment, "월간")
        binding.mainViewPager.adapter = adapter
        binding.mainTabLayout.setupWithViewPager(binding.mainViewPager)
//        binding.mainFrameLayout.visibility = View.VISIBLE
//        binding.mainTabLayout.visibility = View.GONE
//        binding.mainImageProfile.visibility = View.GONE
//        supportFragmentManager.beginTransaction().replace(R.id.main_frame_layout,fragment)
//                    .commit()
//        val fragmentManager : FragmentManager = supportFragmentManager;
//        val fragmentTransaction : FragmentTransaction = fragmentManager.beginTransaction();
//        fragmentTransaction.replace(R.id.main_frame_layout, fragment).commit();      // Fragment로 사용할 MainActivity내의 layout공간을 선택합니다.
    }

    override fun onBackPressed() {
//        supportFragmentManager.beginTransaction().replace(R.id.main_frame_layout,ScheduleFindFragment())
//            .commit()

    }

    fun fragmentSetting() {
        binding.mainTabLayout.visibility = View.VISIBLE
        binding.mainImageProfile.visibility = View.VISIBLE
    }

    override fun onItemMoveBtnClicked(position: Int, scheduleCategoryID: Int) {
    }

    override fun onColor(): String {

        return ""
    }

    //일정찾기 - 필터 바텀다이얼로그로 이동
    override fun onMoveFilterFragment(scheduleCategoryID: Int) {
        val scheduleFindFilterBottomDialogBinding =
            SchedulefindFilterBottomDialogFragment(scheduleCategoryID)
        scheduleFindFilterBottomDialogBinding.show(
            supportFragmentManager, scheduleFindFilterBottomDialogBinding.tag
        )
    }

    override fun onPostAddMemoSuccess(response: BaseResponse) {
        if (response.isSuccess) {
            when (response.code) {
                100 -> {
                    showCustomToast("일정이 작성 되었습니다!")
                    stateChangeBottomSheet(Constants.COLLASPE)
                    TodayService(this).onGetScheduleItems()
                    // 초기화
                    binding.addMemoEditTitle.setText("")
                    binding.addMemoEditContent.setText("")
                }
                else -> {
                    showCustomToast(response.message.toString())
                }
            }
            dismissLoadingDialog()
        } else {
            dismissLoadingDialog()
            showCustomToast(response.message.toString())
        }

    }

    override fun onPostAddMemoFailure(message: String) {
        dismissLoadingDialog()
        showCustomToast(message)
    }

    override fun onPatchMemoSuccess(response: BaseResponse) {
        if (response.isSuccess) {
            when (response.code) {
                100 -> {
                    Constants.IS_EDIT = false
                    dismissLoadingDialog()
                    showCustomToast(response.message.toString())
                    showLoadingDialog(this)
                    TodayService(this).onGetScheduleItems()
                }
                else -> {
                    dismissLoadingDialog()
                    showCustomToast(response.message.toString())
                }
            }
        } else {
            dismissLoadingDialog()
            showCustomToast(response.message.toString())
        }
    }

    override fun onPatchMemoFailure(message: String) {
        dismissLoadingDialog()
        showCustomToast(message)
    }

    override fun onGetDetailMemoSuccess(response: DetailMemoResponse) {
        if (response.isSuccess) {
            when (response.code) {
                100 -> {
                    val responseJsonArray = response.data.asJsonArray
                    responseJsonArray.forEach {
                        val memoJsonObject = it.asJsonObject
                        val memoTitle = memoJsonObject.get("scheduleName").asString
                        val memoDate = memoJsonObject.get("scheduleDate").asString
                        val memoContentJsonElement: JsonElement? =
                            memoJsonObject.get("scheduleMemo")
                        var memoContent = ""
                        if (!memoContentJsonElement!!.isJsonNull) {
                            memoContent = memoContentJsonElement.asString
                        }

//                        val scheduleTime:String? = memoJsonObject.get("scheduleTime").asString
//                        val memoColor = memoJsonObject.get("colorInfo").asString
                        binding.addMemoEditTitle.setText(memoTitle)
                        binding.addMemoEditContent.setText(memoContent)
                        binding.addMemoTextDateInfo.text = memoDate
                    }
                    showCustomToast(response.message.toString())
                }
                else -> {
                    showCustomToast(response.message.toString())
                }
            }
        } else {
            dismissLoadingDialog()
            showCustomToast(response.message.toString())
        }
    }

    override fun onGetDetailMemoFailure(message: String) {
        dismissLoadingDialog()
        showCustomToast(message)
    }

    override fun onGetScheduleItemsSuccess(response: ScheduleItemsResponse) {
        if (response.isSuccess) {
            when (response.code) {
                100 -> {
                    TodayFragment.memoList.clear()
                    val memoJsonArray = response.data.asJsonArray
                    for (i in 0 until memoJsonArray.size()) {
                        val memoJsonObject = memoJsonArray[i].asJsonObject
                        val memoDate = memoJsonObject.get("scheduleDate").asString
                        val memoTitle = memoJsonObject.get("scheduleName").asString
                        var memoContentJsonElement: JsonElement? =
                            memoJsonObject.get("scheduleMemo")
                        val memoPick = memoJsonObject.get("schedulePick").asInt
                        val memoId = memoJsonObject.get("scheduleID").asInt
                        val memoCreatedAt = memoDate.split(" ")
                        var memoCreatedAtMonth = ""
                        var memoCreatedAtDay = 0
                        var memoContent = ""
                        val memoColorInfoJsonElement: JsonElement? =
                            memoJsonObject.get("colorInfo")
                        if (!memoContentJsonElement!!.isJsonNull) {
                            memoContent = memoContentJsonElement.asString
                        }

                        for (i in 0..1) {
                            if (i > 0) {
                                memoCreatedAtMonth = memoCreatedAt[i].replace(" ", "")
                            } else {
                                memoCreatedAtDay = memoCreatedAt[i].replace(" ", "").toInt()
                            }
                        }


                        var memoColorInfo: String? = null
                        if (!memoColorInfoJsonElement!!.isJsonNull) {
                            memoColorInfo = memoColorInfoJsonElement.asString
                        }

                        var memoIsChecked: Boolean? = null
                        memoIsChecked = memoPick >= 0
                        TodayFragment.memoList.add(
                            MemoItem(
                                memoId,
                                memoCreatedAtMonth,
                                memoCreatedAtDay,
                                memoTitle,
                                memoContent,
                                memoIsChecked,
                                memoColorInfo
                            )
                        )
                    }
                    TodayFragment.todayMemoAdapter?.setNewMemoList(TodayFragment.memoList)
                    // 오늘 프래그먼트에서 함수 사용
//                    val todayFragment: TodayFragment = supportFragmentManager.findFragmentById(R.id.main_view_pager) as TodayFragment
//                    todayFragment.checkIsMemoListEmpty()

                    dismissLoadingDialog()
                }
                else -> {
                    dismissLoadingDialog()
                    showCustomToast(response.message.toString())
                }
            }
        } else {
            dismissLoadingDialog()
            showCustomToast(response.message.toString())
        }
    }


    override fun onGetScheduleItemsFailure(message: String) {
        dismissLoadingDialog()
        showCustomToast(message)
    }

    override fun onDeleteMemoSuccess(response: BaseResponse, scheduleID: Int) {
    }

    override fun onDeleteMemoFailure(message: String) {
    }

    override fun onPostItemCheckSuccess(response: BaseResponse) {
    }

    override fun onPostItemCheckFailure(message: String) {
    }

    override fun onGetUserTopCommentSuccess(response: TopCommentResponse) {
    }

    override fun onGetUserTopCommentFailure(message: String) {
    }

    override fun onPostSchedulePositionSuccess(response: BaseResponse) {
    }

    override fun onPostSchedulePositionFailure(message: String) {
    }

    override fun onGetUserCategoryInquirySuccess(responseUser: UserCategoryInquiryResponse) {
        if (responseUser.isSuccess && responseUser.code == 100) {
            for (i in 0 until responseUser.data.size) {
                categoryList.add(
                    MainScheduleCategory(
                        responseUser.data[i].categoryID,
                        responseUser.data[i].categoryName,
                        responseUser.data[i].colorInfo
                    )
                )
            }
            Log.d("TAG", "onGetUserCategoryInquirySuccess: $categoryList")

            categoryScheduleAdapter = MainCategoryAdapter(categoryList, this, {})
            binding.addMemoCategoryRecyclerview.apply {
                layoutManager = LinearLayoutManager(
                    this@MainActivity,
                    LinearLayoutManager.HORIZONTAL,
                    false
                )
                setHasFixedSize(true)
                adapter = categoryScheduleAdapter
            }
        }
    }

    override fun onGetUserCategoryInquiryFail(message: String) {
    }

    override fun onGetCategoryInquirySuccess(categoryInquiryResponse: CategoryInquiryResponse) {
    }

    override fun onGetCategoryInquiryFail(message: String) {
    }

//    override fun onDialogButtonClick(text: String) {
//        Log.d("TAG", "onDialogButtonClick: jj")
//        binding.mainViewPager.currentItem = 2
//    }


}
