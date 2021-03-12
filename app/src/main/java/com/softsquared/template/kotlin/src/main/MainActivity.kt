package com.softsquared.template.kotlin.src.main

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Point
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.marginTop
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.tabs.TabLayout
import com.softsquared.template.kotlin.R
import com.softsquared.template.kotlin.config.ApplicationClass
import com.softsquared.template.kotlin.config.BaseActivity
import com.softsquared.template.kotlin.config.BaseResponse
import com.softsquared.template.kotlin.databinding.ActivityMainBinding
import com.softsquared.template.kotlin.src.main.adapter.MainPagerAdapter
import com.softsquared.template.kotlin.src.main.addmemo.AddMemoFragment
import com.softsquared.template.kotlin.src.main.category.CategoryFragment.Companion.newInstance
import com.softsquared.template.kotlin.src.main.models.PostTodayRequestAddMemo
import com.softsquared.template.kotlin.src.main.monthly.MonthlyFragment
import com.softsquared.template.kotlin.src.main.mypage.MyPageActivity
import com.softsquared.template.kotlin.src.main.mypage.MyPageActivityView
import com.softsquared.template.kotlin.src.main.mypage.MyPageFragment
import com.softsquared.template.kotlin.src.main.schedulefind.ScheduleFindFragment
import com.softsquared.template.kotlin.src.main.today.TodayFragment
import com.softsquared.template.kotlin.src.main.today.TodayView
import com.softsquared.template.kotlin.src.main.today.models.ScheduleItemsResponse

class MainActivity() : BaseActivity<ActivityMainBinding>(ActivityMainBinding::inflate),AddMemoView,TodayView {
    private var clicked = false // FAB 버튼 변수
    private lateinit var bottomSheetBehavior:BottomSheetBehavior<ConstraintLayout>
    // FAB 버튼 애니메이션
    private val fromBottom: Animation by lazy { AnimationUtils.loadAnimation(this,R.anim.from_bottom_anim) }
    private val toBottom: Animation by lazy { AnimationUtils.loadAnimation(this,R.anim.to_bottom_anim) }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // viewPager
        val adapter = MainPagerAdapter(supportFragmentManager)
        adapter.addFragment(MonthlyFragment(),"월간")
        adapter.addFragment(TodayFragment(),"오늘")
        adapter.addFragment(ScheduleFindFragment(),"일정 찾기")
        binding.mainViewPager.adapter = adapter
        binding.mainTabLayout.setupWithViewPager(binding.mainViewPager)

        // FAB 메인 액션 버튼
        binding.mainBtnActionMain.setOnClickListener {
            onActionButtonClicked()
        }

        // FAB 글쓰기 버튼
        binding.mainBtnActionSub.setOnClickListener {
            showBottomAddScheduleSheetDialog()
        }

        val jwt:String? = ApplicationClass.sSharedPreferences.getString(ApplicationClass.X_ACCESS_TOKEN,null)
        Log.d("토큰", "$jwt")

        // 다른 부분을 눌렀을때 FAB 버튼 비활성화
        binding.mainLayout.setOnClickListener {
            if(clicked){
                onActionButtonClicked()
            }
        }


        //유저 이미지 클릭 시 마이페이지로 이동
        binding.mainImageProfile.setOnClickListener {
            val token = intent.getStringExtra("token")
            val name = intent.getStringExtra("name")
            val img = intent.getStringExtra("img")
//            binding.mainFrameLayout.visibility = View.VISIBLE
            val intent = Intent(this,MyPageActivity::class.java)
            intent.putExtra("token",token)
            intent.putExtra("name",name)
            intent.putExtra("img",img)
            startActivity(intent)
//            supportFragmentManager.beginTransaction().replace(R.id.main_frame_layout,MyPageFragment())
//                    .commit()
        }
//         val fragmentTransaction : FragmentTransaction = supportFragmentManager.beginTransaction()
//        fragmentTransaction.add(R.id.main_frame_layout, ScheduleFindFragment.newInstance()).commit();

        // 디바이스 화면 높이 구하기
        val display = windowManager.defaultDisplay
        val size = Point()
        display.getRealSize(size)
        val deviceHeight = size.y
        // 탭 레이아웃 높이와 디바이스 화면 높이 빼기
        val bottomSheetDialogHeight = deviceHeight - 470
        val params = binding.mainFrameBottomSheet.layoutParams
        params.height = bottomSheetDialogHeight
        Log.d(
            "TAG",
            "onCreate: $bottomSheetDialogHeight $deviceHeight ${binding.mainTabLayout.height}"
        )

        // 바텀 시트 다이얼로그
        BottomSheetBehavior.from(binding.mainFrameBottomSheet).apply {
            peekHeight = 500
            this.state = BottomSheetBehavior.STATE_COLLAPSED
        }.addBottomSheetCallback(object:BottomSheetBehavior.BottomSheetCallback(){
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when(newState){
                    BottomSheetBehavior.STATE_EXPANDED->{
                        showCustomToast("Expanded!!")
                        binding.addMemoImageScroll.setImageResource(R.drawable.today_write_down_arrow)
                        binding.addMemoDialogBtnOk.visibility = View.GONE
                    }
                    BottomSheetBehavior.STATE_COLLAPSED->{
                        showCustomToast("Collased!!")
                        binding.addMemoDialogBtnOk.visibility = View.VISIBLE
                        binding.addMemoImageScroll.setImageResource(R.drawable.today_write_up_arrow)
                    }
                }
            }
            override fun onSlide(bottomSheet: View, slideOffset: Float) {
            }

        })



        binding.addMemoDialogBtnSave.setOnClickListener {
            showLoadingDialog(this)
            AddMemoService(this).tryPostAddMemo(PostTodayRequestAddMemo(binding.addMemoEditTitle.text.toString(),binding.addMemoEditContent.text.toString(),1))
        }


    }


    // FAB 액션 버튼 메서드
    private fun onActionButtonClicked() {
        setVisibility(clicked)
        setAnimation(clicked)
        clicked  = !clicked
    }

    // FAB 액션 버튼 애니메이션
    private fun setAnimation(clicked: Boolean) {
        if(!clicked){
            binding.mainBtnActionSub.startAnimation(fromBottom)
        }else{
            binding.mainBtnActionSub.startAnimation(toBottom)
        }
    }

    // FAB 액션 버튼 비저빌리티
    private fun setVisibility(clicked:Boolean){
        if(!clicked){
            binding.mainBtnActionSub.visibility = View.VISIBLE
//            binding.mainFrameLayout.setBackgroundColor(resources.getColor(R.color.transparent_black))
        }else{
            binding.mainBtnActionSub.visibility = View.GONE
//            binding.mainFrameLayout.setBackgroundColor(Color.TRANSPARENT)
        }
    }


    // 프래그먼트에서도 BottomSheetDialog를 호출할 수 있게 메서드로 작성
    fun showBottomAddScheduleSheetDialog(){
        val sheet = AddMemoFragment()
        sheet.show(supportFragmentManager,"AddMemoFragment")
    }

    fun replaceFragment(fragment : Fragment) {
//        binding.mainFrameLayout.visibility = View.VISIBLE
        binding.mainTabLayout.visibility = View.GONE
        binding.mainImageProfile.visibility = View.GONE
//        supportFragmentManager.beginTransaction().replace(R.id.main_frame_layout,fragment)
//                    .commit()
//        val fragmentManager : FragmentManager = supportFragmentManager;
//        val fragmentTransaction : FragmentTransaction = fragmentManager.beginTransaction();
//        fragmentTransaction.replace(R.id.main_frame_layout, fragment).commit();      // Fragment로 사용할 MainActivity내의 layout공간을 선택합니다.
    }

    fun fragmentSetting() {
        binding.mainTabLayout.visibility = View.VISIBLE
        binding.mainImageProfile.visibility = View.VISIBLE
    }

    fun moveScheduleFindFragment() {
//        supportFragmentManager.beginTransaction().replace(R.id.main_frame_layout,ScheduleFindFragment())
//            .commitNowAllowingStateLoss()
        Log.d("TAG", "moveScheduleFindFragment: ㅇㅇㅇㅇㅇㅇㅇ")
    }

    override fun onPostAddMemoSuccess(response: BaseResponse) {
        if(response.isSuccess){
            when(response.code){
                100->{
                    dismissLoadingDialog()
                    showCustomToast("일정이 작성 되었습니다!")
                }
                else->{
                    dismissLoadingDialog()
                    showCustomToast(response.message.toString())
                }
            }
        }else{
            dismissLoadingDialog()
            showCustomToast(response.message.toString())
        }

    }

    override fun onPostAddMemoFailure(message: String) {
        dismissLoadingDialog()
        showCustomToast(message)
    }

    override fun onGetScheduleItemsSuccess(response: ScheduleItemsResponse) {
    }


    override fun onGetScheduleItemsFailure(message: String) {
    }

}