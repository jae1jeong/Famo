package com.softsquared.template.kotlin.src.mypage

import android.content.Intent
import android.os.*
import android.util.Log
import com.bumptech.glide.Glide
import com.softsquared.template.kotlin.R
import com.softsquared.template.kotlin.config.ApplicationClass
import com.softsquared.template.kotlin.config.BaseActivity
import com.softsquared.template.kotlin.databinding.ActivityMyPageBinding
import com.softsquared.template.kotlin.src.mypage.models.MonthsAchievementsResponse
import com.softsquared.template.kotlin.src.mypage.models.MyPageResponse
import com.softsquared.template.kotlin.src.mypage.models.RestScheduleCountResponse
import com.softsquared.template.kotlin.src.mypage.models.TotalScheduleCountResponse
import com.softsquared.template.kotlin.src.mypageedit.MyPageEditActivity
import com.softsquared.template.kotlin.util.Constants

class MyPageActivity : BaseActivity<ActivityMyPageBinding>(ActivityMyPageBinding::inflate),
    MyPageView {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val day = intent.getStringExtra("day")
        val goalTitle = intent.getStringExtra("goalTitle")

//        MyPageService(this).tryGetRestScheduleCount("today")
        MyPageService(this).tryGetTotalScheduleCount()
        MyPageService(this).tryGetMyPage()
        MyPageService(this).tryGetMonthsAchievement()

        //이미지 앞으로 내보내기
        binding.myPageSetting.bringToFront()

        //프로필 편집으로 이동
        binding.myPageImg.setOnClickListener {
            val intent = Intent(this,MyPageEditActivity::class.java)
            startActivity(intent)
        }

        //뒤로가기
        binding.myPageBack.setOnClickListener {
            goBack()
        }


    }

    // 뒤로가기
    fun goBack(){
        finish()
    }

    override fun onGetMyPageSuccess(response: MyPageResponse) {

        when(response.code){
            100 -> {
                Log.d("TAG", "onGetMyPageSuccess: MyPage조회성공")
                showCustomToast("MyPage조회성공")

                val kakaoImg: String? = ApplicationClass.sSharedPreferences.getString(
                    Constants.KAKAO_THUMBNAILIMAGEURL,
                    null
                )
                val day = ApplicationClass.sSharedPreferences.getString(Constants.DAY, null)
                val name =
                    ApplicationClass.sSharedPreferences.getString(Constants.USER_NICKNAME, null)
                val goalTitle =
                    ApplicationClass.sSharedPreferences.getString(Constants.GOALTITLE, null)
                val dDayCheck =
                    ApplicationClass.sSharedPreferences.getString(Constants.DDAY_SETTING, null)
                val comments =
                    ApplicationClass.sSharedPreferences.getString(Constants.COMMENTS, null)
//                val kakaoName:String? = ApplicationClass.sSharedPreferences.getString(Constants.KAKAO_USER_NICKNAME,null)
//                val famoName = ApplicationClass.sSharedPreferences.getString(Constants.USER_NICKNAME,null)

                if (comments != null){
                    binding.test.text = comments
                }

                if (day != null ){
                    binding.myPageTextDoneScheduleCount.text = day
                }

                //카카오로그인일경우
                if (response.loginMethod == "K") {
                    //카톡프사가 없을때 기본이미지 적용, 있으면 있는거 적용
                    if (kakaoImg != null) {
                        Glide.with(this).load(kakaoImg)
                            .centerCrop().into(binding.myPageImg)
                    } else {
                        Glide.with(this).load(R.drawable.my_page_img2)
                            .centerCrop().into(binding.myPageImg)
                    }
                    //페모로그인일경우 처음에는 기본이미지로
                } else {
                    Glide.with(this).load(R.drawable.my_page_img2)
                        .centerCrop().into(binding.myPageImg)
                }

                //이름 적용
                binding.myPageTvName.text = name

                //디데이가 설정되어있지 않거나, 값이 없으면 기본 문구가 나오게 설정
                if (dDayCheck != "1" || (goalTitle == null && day == null)) {
                    binding.myPageTvComments.text = "오늘 하루도\n힘내세요!"
                } else {
                    binding.myPageTvComments.text = goalTitle + "까지 D-" + day + "일\n남았어요!"
                }

            }
            else -> {
                Log.d("TAG", "onGetMyPageSuccess: ${response.message.toString()}")
                showCustomToast("${response.message.toString()}}")
            }
        }

    }

    override fun onGetMyPageFail(message: String) {
    }

    override fun onGetRestScheduleCountSuccess(response: RestScheduleCountResponse) {

        showCustomToast(response.message.toString())
        if(response.isSuccess && response.code == 100){
            //남은일정
            binding.myPageTextRestScheduleCount.text =  response.remainScheduleCount.toString()
            //전체일정
            binding.myPageTextAllScheduleCount.text = response.remainScheduleCount.toString()
        }else{
            Log.d("MyPageFragment", "onGetRestScheduleCountSuccess: ${response.message}")
        }

    }

    override fun onGetRestScheduleCountFailure(message: String) {
    }



    override fun onGetTotalScheduleCountSuccess(response: TotalScheduleCountResponse) {
        if (response.isSuccess && response.code == 100){
            Log.d("MyPageFragment", "전체 일정/해낸수 조회성공")
                binding.myPageTextAllScheduleCount.text = response.totaldata[0].totalScheduleCount.toString()
                binding.myPageTextDoneScheduleCount.text = response.totaldonedata[0].doneScheduleCount.toString()

                binding.myPageTextRestScheduleCount.text = (response.totaldata[0].totalScheduleCount -
                        response.totaldonedata[0].doneScheduleCount).toString()
        }else{
            Log.d("MyPageFragment", "onGetTotalScheduleCountSuccess: ${response.message}")
        }
    }

    override fun onGetTotalScheduleCountFailure(message: String) {
    }

    override fun onGetMonthsAchievmentsSuccess(response: MonthsAchievementsResponse) {

        when(response.code){
            100 -> {
                showCustomToast("월별달성률조회성공")
                Log.d("TAG", "onGetMonthsAchievmentsSuccess: ${response.data}")
                binding.myPageAchievement.text = response.data.toString()
            }
            else -> {

            }
        }
    }

    override fun onGetMonthsAchievmentsFailure(message: String) {
    }

}