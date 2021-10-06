package com.makeus.jfive.famo.src.presentation.main

import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.text.SpannableString
import android.text.style.StyleSpan
import android.text.style.UnderlineSpan
import android.util.Log
import android.view.View
import android.view.ViewTreeObserver
import android.widget.FrameLayout
import androidx.activity.viewModels
import androidx.core.view.marginTop
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.makeus.jfive.famo.R
import com.makeus.jfive.famo.config.BaseActivity
import com.makeus.jfive.famo.databinding.ActivityMainBinding
import com.makeus.jfive.famo.src.common.Resource
import com.makeus.jfive.famo.src.domain.model.main.PatchMemo
import com.makeus.jfive.famo.src.main.adapter.MainCategoryAdapter
import com.makeus.jfive.famo.src.main.category.CategoryEditActivity
import com.makeus.jfive.famo.src.main.models.MainScheduleCategory
import com.makeus.jfive.famo.src.domain.model.main.PostTodayRequestAddMemo
import com.makeus.jfive.famo.src.main.MainEditMemoBottomSheetDialog
import com.makeus.jfive.famo.src.main.schedulefind.*
import com.makeus.jfive.famo.src.mypage.MyPageActivity
import com.makeus.jfive.famo.util.*
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_today.*
import java.time.LocalDate

@AndroidEntryPoint
class MainActivity() : BaseActivity<ActivityMainBinding>(ActivityMainBinding::inflate),
    ScheduleDetailDialog.scheduleDetailDialogClickListener {
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<FrameLayout>

    private val fragmentTitleList = arrayListOf<String>()
    private val mainActivityViewModel: MainActivityViewModel by viewModels()

    companion object {
        lateinit var categoryScheduleAdapter: MainCategoryAdapter
        val categoryList: ArrayList<MainScheduleCategory> = arrayListOf()
        var selectedCategoryId: Int? = null
        var editingDate: String? = null
    }

    private lateinit var scheduleDetailDialog: ScheduleDetailDialog
    lateinit var datePickBottomSheetDialog: MainEditMemoBottomSheetDialog
    private val TAG = this.javaClass.simpleName

    //카테고리 편집으로 보내줄 변수
    var name = ""
    var color = ""
    var size = 0
    var categoryID = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setUp()
        datePickBottomSheetDialog = MainEditMemoBottomSheetDialog()
        mainActivityViewModel.getUserCategory()
    }

    private fun setUp() {
        initViewPager()
        initTabLayout()
        initViewModel()
        initCategoryAdapter()
        settingBottomSheet()
        getViewPagerHeight()
        getPeekHeight()
        scheduleDetailDialog = ScheduleDetailDialog(this)
        scheduleDetailDialog.setOnModifyBtnClickedListener(this)
        binding.addMemoDialogBtnSave.setOnClickListener(viewClicks)
        binding.myPage.setOnClickListener(viewClicks)
        binding.addMemoDialogBtnOk.setOnClickListener(viewClicks)
        binding.addMemoBtnCategoryAdd.setOnClickListener(viewClicks)
        binding.mainFloatingBtn.setOnClickListener(viewClicks)
        binding.addMemoImageScroll.setOnClickListener(viewClicks)
        binding.addMemoBtnDialogCancel.setOnClickListener(viewClicks)
        binding.addMemoTextDateInfo.setOnClickListener(viewClicks)
    }

    private fun getPeekHeight() {
        val firstTitleViewObservable = binding.addMemoBottomSheetTextTopTitle.getLayoutSizeObservable()
        val secondTitleViewObservable = binding.addMemoEditTitle.getLayoutSizeObservable()
        val categoryLinearLayoutObservable = binding.addMemoBottomSheetCategoryLnr.getLayoutSizeObservable()
        // 최초 한 개의 값만 방출
        compositeDisposable.add(
            Observable.zip(
                firstTitleViewObservable.subscribeOn(Schedulers.io()),
                secondTitleViewObservable.subscribeOn(Schedulers.io()),
                categoryLinearLayoutObservable.subscribeOn(Schedulers.io()),
                saveHeights
            ).observeOn(AndroidSchedulers.mainThread())
                .take(1)
                .subscribe()
        )
    }
    private val saveHeights:(Int,Int,Int) -> Unit = {a,b,c ->
        setPeekHeight(a+b+c)
    }

    private fun setPeekHeight(totalHeight:Int){
        bottomSheetBehavior.peekHeight = totalHeight
    }

    private fun getViewPagerHeight() {
        binding.mainViewPager.viewTreeObserver.addOnGlobalLayoutListener(object :
            ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                val bottomSheetDialogHeight = binding.mainViewPager.height
                val params = binding.mainFrameBottomSheet.layoutParams
                params.height = bottomSheetDialogHeight
                binding.mainViewPager.viewTreeObserver.removeOnGlobalLayoutListener(this)
            }
        })
    }

    fun View.getLayoutSizeObservable():Observable<Int>{
        return Observable.create {
            e ->
            val listener = ViewTreeObserver.OnGlobalLayoutListener {
                if(measuredHeight > 0){
                    Log.e(TAG, "getLayoutSizeObservable: ${resources.getResourceEntryName(this.id)} ${measuredHeight}+${marginTop}+${paddingTop}" )
                    e.onNext(measuredHeight+marginTop+paddingTop)
                }
            }
            viewTreeObserver.addOnGlobalLayoutListener(listener)
            e.setCancellable {
                viewTreeObserver.removeOnGlobalLayoutListener(listener)
            }
        }
    }

    private fun settingBottomSheet() {
        // 바텀 시트 다이얼로그
        bottomSheetBehavior = BottomSheetBehavior.from(binding.mainFrameBottomSheet)
        bottomSheetBehavior.apply {
            isHideable = true
            this.state = BottomSheetBehavior.STATE_HIDDEN
        }.addBottomSheetCallback(bottomSheetBehaviorSlideListener)
    }

    private fun initViewModel() {
        mainActivityViewModel.userCategoryList.observe(this, {
            categoryScheduleAdapter.setNewCategoryList(it as ArrayList<MainScheduleCategory>)
        })

        mainActivityViewModel.addMemoRes.observe(this, {
            if (it.equals(true)) {
                categoryScheduleAdapter.setCategoryAllFalse()
                stateChangeBottomSheet(Constants.HIDE_SHEET)
                mainActivityViewModel.addMemoRes.postValue(false)
            }
        })

        mainActivityViewModel.editingScheduleID.observe(this, {

        })
        mainActivityViewModel.selectedCategoryId.observe(this, {
            selectedCategoryId = it
        })
        mainActivityViewModel.detailMemo.observe(this, {
            when (it) {
                is Resource.Success -> {
                    it.data?.let { detailMemo ->
                        scheduleDetailDialog.startEdited(detailMemo)
                        mainActivityViewModel.editingScheduleID.value?.let {
                            val editingMemo = PatchMemo(
                                detailMemo.scheduleName,
                                detailMemo.scheduleDate,
                                it,
                                detailMemo.scheduleMemo
                            )
                            mainActivityViewModel.setEditingMemo(editingMemo)
                        }
                    }
                    dismissLoadingDialog()
                }
                is Resource.Error -> {
                    dismissLoadingDialog()
                    Log.e(TAG, "initViewModel: ${it.throwable?.message}")
                }
                is Resource.Loading -> {
                    showLoadingDialog(this)
                }
                is Resource.Loaded -> {
                }
            }
        })
        mainActivityViewModel.editingMemo.observe(this, {
            it?.let {
                binding.addMemoBottomSheetTextTopTitle.text = "일정 수정하기"
                setFormBottomSheetDialog(
                    it.scheduleName ?: "",
                    it.scheduleMemo ?: "",
                    "${LocalDate.now()} ${
                        CalendarConverter.dayToKoreanShortDayName(LocalDate.now().dayOfWeek.name)
                    }"
                )
            } ?: run {
                binding.addMemoBottomSheetTextTopTitle.text = "오늘 일정 추가하기"
                setFormBottomSheetDialog(
                    "",
                    "",
                    "${LocalDate.now()} ${
                        CalendarConverter.dayToKoreanShortDayName(LocalDate.now().dayOfWeek.name)
                    }"
                )
            }
        })
    }


    private val bottomSheetBehaviorSlideListener =
        object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_EXPANDED -> {
                    }
                    BottomSheetBehavior.STATE_COLLAPSED -> {
                    }
                    BottomSheetBehavior.STATE_HIDDEN -> {

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

        }

    private val viewClicks = object : View.OnClickListener {
        override fun onClick(v: View?) {
            when (v?.id) {
                // 바텀시트 다이얼로그 날짜 버튼
                binding.addMemoTextDateInfo.id -> {
                    datePickBottomSheetDialog.show(
                        supportFragmentManager,
                        datePickBottomSheetDialog.tag
                    )
                }
                // 바텀시트 다이얼로그 취소 버튼
                binding.addMemoBtnDialogCancel.id -> {
                    stateChangeBottomSheet(Constants.HIDE_SHEET)
                }
                // 바텀시트 다이얼로그 방향키
                binding.addMemoImageScroll.id -> {
                    stateChangeBottomSheet(Constants.EXPAND)
                }
                // FAB 버튼
                binding.mainFloatingBtn.id -> {
                    bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
                }
                // 바텀 시트 다이얼로그 카테고리 추가 버튼
                binding.addMemoBtnCategoryAdd.id -> {
                    val intent = Intent(this@MainActivity, CategoryEditActivity::class.java)
                    intent.putExtra("name", name)
                    intent.putExtra("color", color)
                    intent.putExtra("size", size)
                    intent.putExtra("categoryID", categoryID)
                    startActivity(intent)
                }
                //유저 이미지 클릭 시 마이페이지로 이동
                binding.myPage.id -> {
                    val intent = Intent(this@MainActivity, MyPageActivity::class.java)
                    startActivity(intent)
                }
                // 바텀시트 다이얼로그 확인 버튼
                binding.addMemoDialogBtnOk.id -> {
                    addMemo()
                    categoryScheduleAdapter.setCategoryAllFalse()
                }
                // 바텀시트 다이얼로그 저장 버튼
                binding.addMemoDialogBtnSave.id -> {
                    mainActivityViewModel.editingMemo.value?.let {
                        mainActivityViewModel.editingScheduleID.value?.let { memoID ->
                            mainActivityViewModel.patchMemo(
                                memoID, it
                            )
                        }
                    } ?: run {
                        addMemo()
                    }
                }
            }
        }
    }

    private fun addMemo() {
        val newMemo = PostTodayRequestAddMemo(
            binding.addMemoEditTitle.text.toString(),
            binding.addMemoEditContent.text.toString(),
            selectedCategoryId,
            editingDate
        )
        mainActivityViewModel.postAddMemo(newMemo)
    }

    private fun initTabLayout() {
        binding.mainTabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                val spannable = SpannableString(tab?.text)
                spannable.setSpan(UnderlineSpan(), 0, spannable.length, 0)
                spannable.setSpan(StyleSpan(Typeface.BOLD), 0, spannable.length, 0)
                tab?.text = spannable
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                // 밑줄 spannable 텍스트 초기화
                when (tab?.position) {
                    0 -> {
                        tab.text = fragmentTitleList[0]
                    }
                    1 -> {
                        tab.text = fragmentTitleList[1]
                    }
                    2 -> {
                        tab.text = fragmentTitleList[2]
                    }
                    else -> {
                    }
                }
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }
        })
    }

    private fun initViewPager() {
        fragmentTitleList.add(resources.getString(R.string.monthly))
        fragmentTitleList.add(resources.getString(R.string.today))
        fragmentTitleList.add(resources.getString(R.string.find_schedule))
        // viewPager
        val adapter = MainPagerAdapter(this)
        binding.mainViewPager.adapter = adapter
        TabLayoutMediator(binding.mainTabLayout, binding.mainViewPager) { tab, position ->
            tab.text = fragmentTitleList.get(position)
        }.attach()
    }


    fun receiveDateFromDatePicker(strDate: String) {
        editingDate = strDate
        binding.addMemoTextDateInfo.text =
            "$strDate (${CalendarConverter.dayToKoreanShortDayName(LocalDate.parse(strDate).dayOfWeek.toString())})"
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
                mainActivityViewModel.editingMemo.value?.let {
                    NewAskDialogBuilder.Builder(this)
                        .setTitle("일정 수정 취소")
                        .setMessage("일정 수정을 취소하시겠습니까?")
                        .setPositiveBtn("계속 수정하기") {}
                        .setNegativeBtn("나가기") {
                            setFormBottomSheetDialog(
                                "",
                                "",
                                "${LocalDate.now()} ${
                                    CalendarConverter.dayToKoreanShortDayName(LocalDate.now().dayOfWeek.name)
                                }"
                            )
                            mainActivityViewModel.setEditingMemo(null)
                            mainActivityViewModel.setEditingScheduleId(null)
                            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
                        }
                        .build()
                        .show()
                } ?: run {
                    bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
                }

            }
        }
    }


    override fun onBackPressed() {
        NewAskDialogBuilder.Builder(this)
            .setTitle("앱 종료")
            .setMessage("앱을 종료 하시겠습니까?")
            .setPositiveBtn("종료") {
                finishAffinity()
            }
            .setNegativeBtn("취소"){}
            .build()
            .show()
    }


    fun setFormBottomSheetDialog(memoTitle: String, memoContent: String, memoDate: String) {
        binding.addMemoTextDateInfo.text = memoDate
        binding.addMemoEditTitle.setText(memoTitle)
        binding.addMemoEditContent.setText(memoContent)
    }

    private fun initCategoryAdapter() {
        categoryScheduleAdapter = MainCategoryAdapter(this) {
            mainActivityViewModel.selectedCategoryId.postValue(it.id)
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

    override fun modifyBtnClick() {
        stateChangeBottomSheet(Constants.EXPAND)
    }
}
