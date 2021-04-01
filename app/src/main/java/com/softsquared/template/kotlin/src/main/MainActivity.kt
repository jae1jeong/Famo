package com.softsquared.template.kotlin.src.main

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Point
import android.graphics.Typeface
import android.os.Bundle
import android.text.Html
import android.text.Spannable
import android.text.SpannableString
import android.text.style.BackgroundColorSpan
import android.text.style.StyleSpan
import android.text.style.UnderlineSpan
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager.widget.ViewPager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.tabs.TabLayout
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
import com.softsquared.template.kotlin.src.main.monthly.DatePickBottomSheetDialog
import com.softsquared.template.kotlin.src.main.monthly.MonthlyFragment
import com.softsquared.template.kotlin.src.main.schedulefind.*
import com.softsquared.template.kotlin.src.main.schedulefind.adapter.IScheduleCategoryRecyclerView
import com.softsquared.template.kotlin.src.main.schedulefind.adapter.ScheduleBookmarkAdapter
import com.softsquared.template.kotlin.src.main.schedulefind.models.CategoryInquiryResponse
import com.softsquared.template.kotlin.src.main.schedulefind.models.CategoryScheduleInquiryData
import com.softsquared.template.kotlin.src.main.schedulefind.models.UserCategoryInquiryResponse
import com.softsquared.template.kotlin.src.main.today.TodayFragment
import com.softsquared.template.kotlin.src.main.today.TodayService
import com.softsquared.template.kotlin.src.main.today.TodayView
import com.softsquared.template.kotlin.src.main.today.models.MemoItem
import com.softsquared.template.kotlin.src.main.today.models.ScheduleItemsResponse
import com.softsquared.template.kotlin.src.main.today.models.TopCommentResponse
import com.softsquared.template.kotlin.src.mypage.MyPageActivity
import com.softsquared.template.kotlin.src.wholeschedule.WholeScheduleActivity
import com.softsquared.template.kotlin.util.AskDialog
import com.softsquared.template.kotlin.util.CalendarConverter
import com.softsquared.template.kotlin.util.Constants
import kotlinx.android.synthetic.main.fragment_today.*
import java.time.LocalDate


class MainActivity() : BaseActivity<ActivityMainBinding>(ActivityMainBinding::inflate),
    AddMemoView, TodayView, CategoryInquiryView{
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<FrameLayout>

    private var editScheduleID:Int = -1

    companion object{
        lateinit var categoryScheduleAdapter: MainCategoryAdapter
        val categoryList: ArrayList<MainScheduleCategory> = arrayListOf()
        var selectedCategoryId:Int?= null
        var editingDate:String? = null
    }

    //카테고리 편집으로 보내줄 변수
    var name = ""
    var color = ""
    var size = 0
    var categoryID = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        CategoryInquiryService(this).tryGetUserCategoryInquiry()

        // viewPager
        val adapter = MainPagerAdapter(supportFragmentManager)
        adapter.addFragment(MonthlyFragment(), resources.getString(R.string.monthly))
        adapter.addFragment(TodayFragment(), resources.getString(R.string.today))
        adapter.addFragment(ScheduleFindFragment(), resources.getString(R.string.find_schedule))
        binding.mainViewPager.adapter = adapter
        binding.mainTabLayout.setupWithViewPager(binding.mainViewPager)
        binding.mainTabLayout.addOnTabSelectedListener(object:TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab?) {
                val spannable = SpannableString(tab?.text)
                spannable.setSpan(UnderlineSpan(),0,spannable.length,0)
                spannable.setSpan(StyleSpan(Typeface.BOLD),0,spannable.length,0)
                tab?.text = spannable
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                // 밑줄 spannable 텍스트 초기화
                when(tab?.position){
                    0 ->{
                        tab.text = "월간"
                    }
                    1->{
                        tab.text = "오늘"
                    }
                    2->{
                        tab.text = "일정 찾기"
                    }
                    else->{}
                }
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }

        })
        // 처음 앱 실행시 탭 밑줄
//        val firstCreateTabSpannable = SpannableString("월간")
//        firstCreateTabSpannable.setSpan(UnderlineSpan(),0,firstCreateTabSpannable.length,0)
//        firstCreateTabSpannable.setSpan(StyleSpan(Typeface.BOLD),0,firstCreateTabSpannable.length,0)
//        (binding.mainTabLayout.get(0) as TabLayout).text = firstCreateTabSpannable

        binding.mainViewPager.addOnPageChangeListener(object: ViewPager.OnPageChangeListener{
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            }

            override fun onPageSelected(position: Int) {
                when(position){
                    0 ->{
                        binding.mainTopLayout.setBackgroundColor(Color.WHITE)
                    }
                    else->{
                        binding.mainTopLayout.setBackgroundColor(resources.getColor(R.color.light_gray))
                    }
                }
            }

            override fun onPageScrollStateChanged(state: Int) {
            }

        })


        //유저 이미지 클릭 시 마이페이지로 이동
        binding.myPage.setOnClickListener {
            val intent = Intent(this, MyPageActivity::class.java)
            startActivity(intent)
        }


        setHeightBottomSheetDialog()
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
                            editScheduleID = ApplicationClass.sSharedPreferences.getInt(
                                Constants.EDIT_SCHEDULE_ID,
                                -1
                            )
                            if (editScheduleID != -1) {
                                binding.addMemoBottomSheetTextTopTitle.text = "일정 수정하기"
                                AddMemoService(this@MainActivity).tryGetDetailMemo(
                                    editScheduleID
                                )
                            }
                        }else{
                            binding.addMemoBottomSheetTextTopTitle.text = "오늘 일정 추가하기"
                        }
                    }
                    BottomSheetBehavior.STATE_COLLAPSED -> {
                        if (Constants.IS_EDIT) {
                            Constants.IS_EDIT = false
                        } else {
                            val nowDate = LocalDate.now()
                            val dayName = nowDate.dayOfWeek.name
                            binding.addMemoTextDateInfo.text = "$nowDate (${
                                CalendarConverter.dayToKoreanShortDayName(
                                    dayName
                                )
                            })"
                        }
                    }
                    BottomSheetBehavior.STATE_HIDDEN->{
                        if(Constants.IS_EDIT){
                            Constants.IS_EDIT = false
                            setFormBottomSheetDialog("","","")
                            editScheduleID = -1
                            binding.addMemoBottomSheetTextTopTitle.text = "오늘 일정 추가하기"
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

        // 바텀시트 다이얼로그 확인 버튼
        binding.addMemoDialogBtnOk.setOnClickListener {
            if (Constants.IS_EDIT) {
                editScheduleID = ApplicationClass.sSharedPreferences.getInt(
                    Constants.EDIT_SCHEDULE_ID,
                    -1
                )

            }else{
                showLoadingDialog(this)
                AddMemoService(this).tryPostAddMemo(
                    PostTodayRequestAddMemo(
                        binding.addMemoEditTitle.text.toString(),
                        binding.addMemoEditContent.text.toString(),
                        selectedCategoryId,
                            editingDate
                    )
                )
            }
        }

        // 바텀 시트 다이얼로그 카테고리 추가 버튼
        binding.addMemoBtnCategoryAdd.setOnClickListener {
//            startActivity(Intent(this, CategoryEditActivity::class.java))
            val intent = Intent(this,CategoryEditActivity::class.java)
            intent.putExtra("name", name)
            intent.putExtra("color", color)
            intent.putExtra("size", size)
            intent.putExtra("categoryID", categoryID)
            startActivity(intent)
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
                editScheduleID =
                    ApplicationClass.sSharedPreferences.getInt(Constants.EDIT_SCHEDULE_ID, -1)
                if (editScheduleID != -1) {
                    Log.d("TAG", "onCreate: $editingDate")
                    showLoadingDialog(this)
                    AddMemoService(this).tryPatchMemo(
                        editScheduleID,
                        PatchMemo(
                            binding.addMemoEditTitle.text.toString(),
                            editingDate,
                            selectedCategoryId,
                            binding.addMemoEditContent.text.toString()
                        )
                    )
                } else {
                    showCustomToast("수정 오류 스케줄 아이디를 볼러오지 못하였습니다.")
                }
            } else {
                // 일정 추가하기
                    selectedCategoryId = null
                    categoryList.forEach {
                        if(it.selected){
                            selectedCategoryId = it.id
                        }
                    }
                showLoadingDialog(this)
                AddMemoService(this).tryPostAddMemo(
                    PostTodayRequestAddMemo(
                        binding.addMemoEditTitle.text.toString(),
                        binding.addMemoEditContent.text.toString(),
                        selectedCategoryId,
                            editingDate
                    )
                )
            }
        }

        // 바텀시트 다이얼로그 취소 버튼
        binding.addMemoBtnDialogCancel.setOnClickListener {
            stateChangeBottomSheet(Constants.HIDE_SHEET)
        }


        // 바텀시트 다이얼로그 날짜 버튼
        binding.addMemoTextDateInfo.setOnClickListener {
            val datePickBottomSheetDialog = MainEditMemoBottomSheetDialog()
            datePickBottomSheetDialog.show(
                    supportFragmentManager,
                    datePickBottomSheetDialog.tag
            )
        }

    }

    fun receiveDateFromDatePicker(strDate:String){
        editingDate = strDate
        binding.addMemoTextDateInfo.text = "$strDate (${CalendarConverter.dayToKoreanShortDayName(LocalDate.parse(strDate).dayOfWeek.toString())})"
    }

    fun setHeightBottomSheetDialog(){
        // 디바이스 화면 높이 구하기
        val display = windowManager.defaultDisplay
        val size = Point()
        display.getRealSize(size)
        val deviceHeight = size.y
        // 탭 레이아웃 높이와 디바이스 화면 높이 빼기
        val bottomSheetDialogHeight = deviceHeight - 445
        val params = binding.mainFrameBottomSheet.layoutParams
        params.height = bottomSheetDialogHeight
    }

    // 바텀시트 다이얼로그 상태 관리
    fun stateChangeBottomSheet(state: String) {
        when (state) {
            Constants.EXPAND -> {
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
            }
            Constants.COLLASPE -> {
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
            }
            Constants.HIDE_SHEET -> {
                if(Constants.IS_EDIT){
                    AskDialog(this)
                            .setTitle("일정 수정 취소")
                            .setMessage("일정 수정을 취소하시겠습니까?")
                            .setNegativeButton("나가기"){
                                setFormBottomSheetDialog("","","")
                                categoryList.forEach {
                                    if(it.selected){
                                        it.selected = false
                                    }
                                }
                                initializeCategoryAdapter(categoryList)
                                Constants.IS_EDIT = false
                                bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
                            }
                            .setPositiveButton("계속 수정하기"){
                            }
                            .show()
                }else{
                    bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
                }
            }
        }
        setHeightBottomSheetDialog()
    }

    fun onMoveScheduleFind(searchWord: String) {
        Log.d("TAG", "함수호출확인 ")
        val adapter = MainPagerAdapter(supportFragmentManager)
        binding.mainViewPager.adapter = adapter
        binding.mainViewPager.currentItem = 2
    }


    override fun onBackPressed() {
        AskDialog(this)
                .setTitle("앱 종료")
                .setMessage("앱을 종료하시겠습니까?")
                .setPositiveButton("종료"){
                    finishAffinity()
                }
                .setNegativeButton("취소"){}
                .show()
    }


    override fun onPostAddMemoSuccess(response: BaseResponse) {
        if (response.isSuccess) {
            when (response.code) {
                100 -> {
                    showCustomToast("일정이 작성 되었습니다!")
                    stateChangeBottomSheet(Constants.COLLASPE)
                    TodayService(this).onGetScheduleItems()
                    editingDate = null
                    selectedCategoryId = null
                    // 초기화
                    binding.addMemoEditTitle.setText("")
                    binding.addMemoEditContent.setText("")
                    binding.addMemoTextDateInfo.text = ""
                    categoryList.forEach {
                        if(it.selected){
                            it.selected = false
                        }
                    }
                    initializeCategoryAdapter(categoryList)
                    stateChangeBottomSheet(Constants.COLLASPE)
                    TodayFragment.restScheduleCount = TodayFragment.restScheduleCount + 1
                    today_text_rest_schedule.text = "남은 일정 ${TodayFragment.restScheduleCount}개"
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
                    editingDate = null
                    binding.addMemoTextDateInfo.text = ""
                    Constants.IS_EDIT = false
                    dismissLoadingDialog()
                    showCustomToast("일정이 성공적으로 수정되었습니다.")
                    stateChangeBottomSheet(Constants.HIDE_SHEET)
                    TodayFragment.todayMemoAdapter?.let {
                        TodayFragment.todayMemoAdapter!!.memoList.forEach {
                            if (it.id == editScheduleID) {
                                it.title = binding.addMemoEditTitle.text.toString()
                                it.description = binding.addMemoEditContent.text.toString()
                                TodayFragment.todayMemoAdapter?.notifyItemChanged(
                                    TodayFragment.todayMemoAdapter!!.memoList.indexOf(
                                        it
                                    )
                                )
                            }
                        }
                    }
                    MonthlyFragment.monthlyMemoAdapter?.let{
                        MonthlyFragment.monthlyMemoAdapter!!.memoList.forEach {
                            if(it.id == editScheduleID){
                                it.title = binding.addMemoEditTitle.text.toString()
                                it.description = binding.addMemoEditContent.text.toString()
                                MonthlyFragment.monthlyMemoAdapter?.notifyItemChanged(MonthlyFragment.monthlyMemoAdapter!!.memoList.indexOf(it))
                            }
                        }
                    }
                    try{
                        ScheduleFindLatelyFragment.scheduleLatelyAdapter?.let {
                            ScheduleFindLatelyFragment.scheduleLatelyAdapter.latelyList.forEach {
                                if (it.scheduleID == editScheduleID) {
                                    it.scheduleName = binding.addMemoEditTitle.text.toString()
                                    it.scheduleMemo = binding.addMemoEditContent.text.toString()
                                    ScheduleFindLatelyFragment.scheduleLatelyAdapter.notifyItemChanged(ScheduleFindLatelyFragment.latelyListWhole.indexOf(it))
                                }
                            }
                        }
                    }catch (e:UninitializedPropertyAccessException){

                    }

                    try{
                        ScheduleFindMainFragment.scheduleWholeAdapter?.let {
                        ScheduleFindMainFragment.scheduleWholeAdapter.wholeList.forEach {
                            if (it.id == editScheduleID) {
                                it.name = binding.addMemoEditTitle.text.toString()
                                it.memo = binding.addMemoEditContent.text.toString()
                                ScheduleFindMainFragment.scheduleWholeAdapter.notifyItemChanged(ScheduleFindMainFragment.wholeScheduleList.indexOf(it))
                            }
                        }
                    }}
                    catch (e:UninitializedPropertyAccessException){

                    }

                    try{

                        ScheduleFindCategoryFragment.categoryScheduleInquiryAdapter.let {
                            ScheduleFindCategoryFragment.categoryScheduleInquiryAdapter.categoryList.forEach {
                                if (it.id == editScheduleID) {
                                    it.name = binding.addMemoEditTitle.text.toString()
                                    it.memo = binding.addMemoEditContent.text.toString()
                                    ScheduleFindCategoryFragment.categoryScheduleInquiryAdapter.notifyItemChanged(ScheduleFindCategoryFragment.categoryList.indexOf(it))
                                }
                            }
                        }
                    }catch (e:UninitializedPropertyAccessException){
                    }
                    selectedCategoryId = null
                    categoryList.forEach {
                        if(it.selected){
                            it.selected = false
                        }
                    }
                    initializeCategoryAdapter(categoryList)
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
                        setFormBottomSheetDialog(memoTitle, memoContent, memoDate)
                    }
                }
                else -> {
                    showCustomToast(response.message.toString())
                }
            }
        }
        else {
            showCustomToast(response.message.toString())
        }
    }

    fun setFormBottomSheetDialog(memoTitle: String, memoContent: String, memoDate: String){
        binding.addMemoTextDateInfo.text = memoDate
        binding.addMemoEditTitle.setText(memoTitle)
        binding.addMemoEditContent.setText(memoContent)

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
                        val memoScheduleFormDate = memoJsonObject.get("scheduleFormDate").asString
                        val memoColorInfoJsonElement: JsonElement? =
                            memoJsonObject.get("colorInfo")
                        if (!memoContentJsonElement!!.isJsonNull) {
                            memoContent = memoContentJsonElement.asString
                        }
                        val memoScheduleOrder = memoJsonObject.get("scheduleOrder").asInt


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
                                memoColorInfo,
                                memoScheduleFormDate,
                                    memoScheduleOrder
                            )
                        )
                    }
                    TodayFragment.todayMemoAdapter?.setNewMemoList(TodayFragment.memoList)
                    if(TodayFragment.todayMemoAdapter?.itemCount == 1){
                        today_frame_layout_no_item.visibility = View.GONE
                    }
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
                        responseUser.data[i].colorInfo,
                        false
                    )
                )

                name += responseUser.data[i].categoryName + ":"
                color += responseUser.data[i].colorInfo + ":"
                size = responseUser.data.size

                categoryID += "${responseUser.data[i].categoryID}:"
            }
            initializeCategoryAdapter(categoryList)


        }
    }

    fun initializeCategoryAdapter(categoryList:ArrayList<MainScheduleCategory>){
        categoryScheduleAdapter = MainCategoryAdapter(categoryList, this) {
            if(it.selected){
                selectedCategoryId = it.id
            }
        }
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

    override fun onGetUserCategoryInquiryFail(message: String) {
    }

    override fun onGetCategoryInquirySuccess(categoryInquiryResponse: CategoryInquiryResponse) {
    }

    override fun onGetCategoryInquiryFail(message: String) {
    }



}
