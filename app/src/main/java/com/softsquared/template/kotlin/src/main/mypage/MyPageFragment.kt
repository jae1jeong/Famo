package com.softsquared.template.kotlin.src.main.mypage

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import com.bumptech.glide.Glide
import com.softsquared.template.kotlin.R
import com.softsquared.template.kotlin.config.ApplicationClass
import com.softsquared.template.kotlin.config.BaseFragment
import com.softsquared.template.kotlin.databinding.FragmentMypageBinding
import com.softsquared.template.kotlin.src.mypage.models.MyPageResponse
import com.softsquared.template.kotlin.src.mypage.models.RestScheduleCountResponse
import com.softsquared.template.kotlin.src.mypage.models.TotalScheduleCountResponse
import com.softsquared.template.kotlin.src.mypage.MyPageActivity
import com.softsquared.template.kotlin.src.mypage.MyPageActivityView
import com.softsquared.template.kotlin.src.mypage.MyPageService
import com.softsquared.template.kotlin.src.mypage.MyPageView
import com.softsquared.template.kotlin.src.mypage.models.MonthsAchievementsResponse
import com.softsquared.template.kotlin.util.Constants

class MyPageFragment(val myPageActivityView: MyPageActivityView):
    BaseFragment<FragmentMypageBinding>(FragmentMypageBinding::bind, R.layout.fragment_mypage),
    MyPageView {

//    변들로 받을 변수
//    var token : String? = null
//    var name : String? = null
//    var img : String?= null
//    var day : String? = null
//    var goalTitle : String?= null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        var extra = this.arguments
//        if (extra != null) {
//            extra = arguments
//            day = extra?.getString("day").toString()
//            goalTitle = extra?.getString("goalTitle").toString()
//            Log.d("MyPageFragment 잘들어 왔나 day", "값: $day")
//            Log.d("MyPageFragment 잘들어 왔나 goalTitle", "값: $goalTitle")
//        }

        MyPageService(this).tryGetRestScheduleCount("today")
        MyPageService(this).tryGetMyPage()

        //이미지 앞으로 내보내기
        binding.myPageSetting.bringToFront()

        //프로필 편집으로 이동
        binding.myPageImg.setOnClickListener {
//            val intent = Intent(activity,MyPageActivity::class.java)
//            startActivity(intent)

//            myPageActivityView.moveMyPageEdit()
        }

        //뒤로가기
        binding.myPageBtnBack.setOnClickListener {
//            binding.categoryEditLinear.visibility = View.GONE
//            val intent = Intent(context,MyPageActivity::class.java)
//            startActivity(intent)
            (activity as MyPageActivity).goBack()
        }

    }

    //조회성공
    @SuppressLint("SetTextI18n")
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
        showCustomToast(message)
    }

    override fun onGetRestScheduleCountSuccess(response: RestScheduleCountResponse) {

        showCustomToast(response.message.toString())
        if(response.isSuccess && response.code == 100){
            binding.myPageTextRestScheduleCount.text =  response.remainScheduleCount.toString()
        }else{
            Log.d("MyPageFragment", "onGetRestScheduleCountSuccess: ${response.message}")
        }

    }

    override fun onGetRestScheduleCountFailure(message: String) {
        showCustomToast(message)
    }

    override fun onGetTotalScheduleCountSuccess(response: TotalScheduleCountResponse) {
    }

    override fun onGetTotalScheduleCountFailure(message: String) {
    }

    override fun onGetMonthsAchievmentsSuccess(response: MonthsAchievementsResponse) {
    }

    override fun onGetMonthsAchievmentsFailure(message: String) {
    }

//    override fun onGetDoneScheduleCountSuccess(response: DoneScheduleCountResponse) {
//        if (response.isSuccess && response.code == 100){
//            val totalDataJsonArray = response.totaldata.asJsonArray
//            totalDataJsonArray.forEach {
//                val totalData = it.asJsonObject.get("totalScheduleCount").asString
//                binding.myPageTextAllScheduleCount.text = totalData
//            }
//            val DoneScheduleDataJsonArray = response.totaldonedata.asJsonArray
//            DoneScheduleDataJsonArray.forEach {
//                val doneData = it.asJsonObject.get("doneScheduleCount").asString
//                binding.myPageTextDoneScheduleCount.text = doneData
//            }
//        }else{
//            Log.d("MyPageFragment", "onGetRestScheduleCountSuccess: ${response.message}")
//        }
//    }
//
//    override fun onGetDoneScheduleCountFailure(message: String) {
//        showCustomToast(message)
//    }


}