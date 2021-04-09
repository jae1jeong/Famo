package com.softsquared.template.kotlin.src.wholeschedule

import android.content.Intent
import android.graphics.Color
import android.graphics.Point
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.gson.JsonElement
import com.softsquared.template.kotlin.R
import com.softsquared.template.kotlin.config.ApplicationClass
import com.softsquared.template.kotlin.config.BaseActivity
import com.softsquared.template.kotlin.config.BaseResponse
import com.softsquared.template.kotlin.databinding.ActivityWholeScheduleBinding
import com.softsquared.template.kotlin.src.main.AddMemoService
import com.softsquared.template.kotlin.src.main.AddMemoView
import com.softsquared.template.kotlin.src.main.MainActivity
import com.softsquared.template.kotlin.src.main.MainEditMemoBottomSheetDialog
import com.softsquared.template.kotlin.src.main.adapter.MainCategoryAdapter
import com.softsquared.template.kotlin.src.main.category.CategoryEditActivity
import com.softsquared.template.kotlin.src.main.models.DetailMemoResponse
import com.softsquared.template.kotlin.src.main.models.MainScheduleCategory
import com.softsquared.template.kotlin.src.main.models.PatchMemo
import com.softsquared.template.kotlin.src.main.models.PostTodayRequestAddMemo
import com.softsquared.template.kotlin.src.main.monthly.MonthlyFragment
import com.softsquared.template.kotlin.src.main.schedulefind.ScheduleFindBookmarkFragment
import com.softsquared.template.kotlin.src.main.schedulefind.ScheduleFindCategoryFragment
import com.softsquared.template.kotlin.src.main.schedulefind.ScheduleFindLatelyFragment
import com.softsquared.template.kotlin.src.main.schedulefind.ScheduleFindMainFragment
import com.softsquared.template.kotlin.src.main.today.TodayFragment
import com.softsquared.template.kotlin.src.wholeschedule.adapter.WholeSchedulePagerAdapter
import com.softsquared.template.kotlin.src.wholeschedule.bookmark.WholeScheduleBookmarkFragment
import com.softsquared.template.kotlin.src.wholeschedule.bookmark.adapter.WholeScheduleBookmarkAdapter
import com.softsquared.template.kotlin.src.wholeschedule.lately.WholeLatelyScheduleFragment
import com.softsquared.template.kotlin.util.AskDialog
import com.softsquared.template.kotlin.util.CalendarConverter
import com.softsquared.template.kotlin.util.Constants
import java.time.LocalDate

class WholeScheduleActivity : BaseActivity<ActivityWholeScheduleBinding>
    (ActivityWholeScheduleBinding::inflate),AddMemoView{
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<FrameLayout>
    var name = ""
    var color = ""
    var size = 0
    var categoryID = ""
    lateinit var bottomSheetDialogCategoryAdapter :MainCategoryAdapter


    companion object{
        private var editScheduleID:Int = -1
        lateinit var adapter: WholeSchedulePagerAdapter
    }

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
        adapter = WholeSchedulePagerAdapter(supportFragmentManager)
        adapter.addFragment(scheduleFindBookmarkFragment,"즐겨찾기")
        adapter.addFragment(scheduleFindLatelyFragment,"최근")
        binding.wholeScheduleViewPager.adapter = adapter
        binding.wholeScheduleTabLayout.setupWithViewPager(binding.wholeScheduleViewPager)

        binding.wholeScheduleTabLayout.setSelectedTabIndicatorColor(Color.parseColor("#000000")); // 밑줄색
        binding.wholeScheduleTabLayout.setSelectedTabIndicatorHeight(9); // 밑줄높이(두께)

        val bookmarkLatelyCheck = ApplicationClass.sSharedPreferences.getString(Constants.BOOKMARK_LATELY_CHECH,null)

//        if (bookmarkLatelyCheck != null){
//            if (bookmarkLatelyCheck == "bookmark"){
//                binding.wholeScheduleViewPager.currentItem = 0
//            }else{
//                binding.wholeScheduleViewPager.currentItem = 1
//            }
//        }

        //X버튼
        binding.wholeScheduleXBtn.setOnClickListener {
            finish()
        }

        binding.addMemoCategoryRecyclerview.apply {
            layoutManager = LinearLayoutManager(this@WholeScheduleActivity,LinearLayoutManager.HORIZONTAL,false)
        }
        binding.addMemoCategoryRecyclerview.adapter = MainActivity.categoryScheduleAdapter

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
                                AddMemoService(this@WholeScheduleActivity).tryGetDetailMemo(
                                        editScheduleID
                                )
                            }
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

            } else {
                showCustomToast("잘못된 요청입니다. 다시 시도해주세요.")
            }
        }

        // 바텀 시트 다이얼로그 카테고리 추가 버튼
        binding.addMemoBtnCategoryAdd.setOnClickListener {
            val intent = Intent(this, CategoryEditActivity::class.java)
            intent.putExtra("name", name)
            intent.putExtra("color", color)
            intent.putExtra("size", size)
            intent.putExtra("categoryID", categoryID)
            startActivity(intent)
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
                    showLoadingDialog(this)
                    AddMemoService(this).tryPatchMemo(
                            editScheduleID,
                            PatchMemo(
                                    binding.addMemoEditTitle.text.toString(),
                                    MainActivity.editingDate,
                                    MainActivity.selectedCategoryId,
                                    binding.addMemoEditContent.text.toString()
                            )
                    )
                } else {
                    showCustomToast("수정 오류 스케줄 아이디를 볼러오지 못하였습니다.")
                }
            } else {
            }
        }

        // 바텀시트 다이얼로그 취소 버튼
        binding.addMemoBtnDialogCancel.setOnClickListener {
            stateChangeBottomSheet(Constants.HIDE_SHEET)
        }

        // 바텀시트 다이얼로그 날짜 버튼
        binding.addMemoTextDateInfo.setOnClickListener {
            val datePickBottomSheetDialog = WholeScheduleEditBottomSheetDialog()
            datePickBottomSheetDialog.show(
                    supportFragmentManager,
                    datePickBottomSheetDialog.tag
            )
        }
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
                                MainActivity.categoryList.forEach {
                                    if(it.selected){
                                        it.selected = false
                                    }
                                }
                                initializeCategoryAdapter(MainActivity.categoryList)
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

    fun setFormBottomSheetDialog(memoTitle: String, memoContent: String, memoDate: String){
        binding.addMemoTextDateInfo.text = memoDate
        binding.addMemoEditTitle.setText(memoTitle)
        binding.addMemoEditContent.setText(memoContent)

    }
    fun receiveDateFromDatePicker(strDate:String){
        MainActivity.editingDate = strDate
        binding.addMemoTextDateInfo.text = "$strDate (${CalendarConverter.dayToKoreanShortDayName(LocalDate.parse(strDate).dayOfWeek.toString())})"
    }


    fun initializeCategoryAdapter(categoryList:ArrayList<MainScheduleCategory>){
        MainActivity.categoryScheduleAdapter = MainCategoryAdapter(categoryList, this) {
            if(it.selected){
                MainActivity.selectedCategoryId = it.id
            }
        }
        binding.addMemoCategoryRecyclerview.apply {
            layoutManager = LinearLayoutManager(
                    this@WholeScheduleActivity,
                    LinearLayoutManager.HORIZONTAL,
                    false
            )
//            setHasFixedSize(true)
            adapter = MainActivity.categoryScheduleAdapter
        }
    }


    override fun onPostAddMemoSuccess(response: BaseResponse) {
    }

    override fun onPostAddMemoFailure(message: String) {
    }

    override fun onPatchMemoSuccess(response: BaseResponse) {
        if (response.isSuccess) {
            when (response.code) {
                100 -> {
                    MainActivity.editingDate = null
                    Constants.IS_EDIT = false
                    dismissLoadingDialog()
                    showCustomToast("일정이 성공적으로 수정되었습니다. :)")
                    stateChangeBottomSheet(Constants.HIDE_SHEET)
//                    TodayFragment.todayMemoAdapter?.let {
//                        TodayFragment.todayMemoAdapter!!.memoList.forEach {
//                            if (it.id == editScheduleID) {
//                                it.title = binding.addMemoEditTitle.text.toString()
//                                it.description = binding.addMemoEditContent.text.toString()
//                                TodayFragment.todayMemoAdapter?.notifyItemChanged(
//                                        TodayFragment.todayMemoAdapter!!.memoList.indexOf(
//                                                it
//                                        )
//                                )
//                            }
//                        }
//                    }
//                    MonthlyFragment.monthlyMemoAdapter?.let{
//                        MonthlyFragment.monthlyMemoAdapter!!.memoList.forEach {
//                            if(it.id == editScheduleID){
//                                it.title = binding.addMemoEditTitle.text.toString()
//                                it.description = binding.addMemoEditContent.text.toString()
//                                MonthlyFragment.monthlyMemoAdapter?.notifyItemChanged(MonthlyFragment.monthlyMemoAdapter!!.memoList.indexOf(it))
//                            }
//                        }
//                    }

                    try{
                        WholeScheduleBookmarkFragment.wholeScheduleBookmarkAdapter?.let {
                            WholeScheduleBookmarkFragment.wholeScheduleBookmarkAdapter.bookmarkListWhole.forEach {
                                if (it.scheduleID == editScheduleID) {
                                    it.scheduleName = binding.addMemoEditTitle.text.toString()
                                    it.scheduleMemo = binding.addMemoEditContent.text.toString()
                                    WholeScheduleBookmarkFragment.wholeScheduleBookmarkAdapter?.notifyItemChanged(WholeScheduleBookmarkFragment.wholeScheduleBookmarkAdapter.bookmarkListWhole.indexOf(it))
                                }
                            }
                        }
                    }
                    catch (e:UninitializedPropertyAccessException){}

                    try{
                        WholeLatelyScheduleFragment.wholeScheduleLatelyAdapter?.let {
                            WholeLatelyScheduleFragment.wholeScheduleLatelyAdapter.latelyListWhole.forEach {
                                if (it.scheduleID == editScheduleID) {
                                    it.scheduleName = binding.addMemoEditTitle.text.toString()
                                    it.scheduleMemo = binding.addMemoEditContent.text.toString()
                                    WholeLatelyScheduleFragment.wholeScheduleLatelyAdapter?.notifyItemChanged(WholeLatelyScheduleFragment.wholeScheduleLatelyAdapter.latelyListWhole.indexOf(it))
                                }
                            }
                        }
                    }
                    catch (e:UninitializedPropertyAccessException){}


                    try{
                        ScheduleFindBookmarkFragment.scheduleBookmarkAdapter?.let {
                            ScheduleFindBookmarkFragment.scheduleBookmarkAdapter.bookmarkListWhole.forEach {
                                if (it.scheduleID == editScheduleID) {
                                    it.scheduleName = binding.addMemoEditTitle.text.toString()
                                    it.scheduleMemo = binding.addMemoEditContent.text.toString()
                                    ScheduleFindBookmarkFragment.scheduleBookmarkAdapter?.notifyItemChanged(ScheduleFindBookmarkFragment.bookmarkList.indexOf(it))
                                }
                            }
                        }
                    }
                    catch(e:UninitializedPropertyAccessException){}


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


                    ScheduleFindMainFragment.scheduleWholeAdapter?.let {
                        ScheduleFindMainFragment.scheduleWholeAdapter.wholeList.forEach {
                            if (it.id == editScheduleID) {
                                it.name = binding.addMemoEditTitle.text.toString()
                                it.memo = binding.addMemoEditContent.text.toString()
                                ScheduleFindMainFragment.scheduleWholeAdapter.notifyItemChanged(ScheduleFindMainFragment.wholeScheduleList.indexOf(it))
                            }
                        }
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
//                        ScheduleFindCategoryFragment.categoryScheduleInquiryAdapter = CategoryScheduleInquiryAdapter(){}
                    }


                    // 카테고리 선택 초기화
                    MainActivity.selectedCategoryId = null
                    MainActivity.categoryList.forEach {
                        if(it.selected){
                            it.selected = false
                        }
                    }
                    stateChangeBottomSheet(Constants.HIDE_SHEET)

                }
                else -> {
                    dismissLoadingDialog()
                }
            }
        } else {
            dismissLoadingDialog()
        }
    }


    override fun onPatchMemoFailure(message: String) {
    }

    override fun onGetDetailMemoSuccess(response: DetailMemoResponse) {
        if (response.isSuccess) {
            when (response.code) {
                100 -> {
                    val responseJsonArray = response.data.asJsonArray
                    responseJsonArray.forEach {
                        val memoJsonObject = it.asJsonObject
                        val memoTitle = memoJsonObject.get("scheduleName").asString
                        val memoDate = memoJsonObject.get("scheduleForm").asString
                        val memoContentJsonElement: JsonElement? =
                                memoJsonObject.get("scheduleMemo")
                        var memoContent = ""
                        if (!memoContentJsonElement!!.isJsonNull) {
                            memoContent = memoContentJsonElement.asString
                        }
                        setFormBottomSheetDialog(memoTitle, memoContent, "${memoDate} (${CalendarConverter.dayToKoreanShortDayName(LocalDate.parse(memoDate).dayOfWeek.name)})")
                    }
                }
                else -> {
                }
            }
        }
        else {
        }
    }

    override fun onGetDetailMemoFailure(message: String) {
    }

}