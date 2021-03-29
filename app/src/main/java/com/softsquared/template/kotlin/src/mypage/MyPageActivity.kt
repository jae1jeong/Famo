package com.softsquared.template.kotlin.src.mypage

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.*
import android.util.Base64
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.net.toUri
import com.bumptech.glide.Glide
import com.google.gson.Gson
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

    //갤러리/카메라 판별변수
    var check = 100
    var galleryUrl: Uri? = null
    var cameraImg: Bitmap? = null
    val monthsAchievementsMap :Map<String, Int> = mapOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val day = intent.getStringExtra("day")
        val goalTitle = intent.getStringExtra("goalTitle")
        check = intent.getIntExtra("check", 100)

        MyPageService(this).tryGetTotalScheduleCount()
        MyPageService(this).tryGetMyPage()
        MyPageService(this).tryGetMonthsAchievement()

        //이미지 앞으로 내보내기
        binding.myPageSetting.bringToFront()

        //프로필 편집으로 이동
        binding.myPageImg.setOnClickListener {
            val intent = Intent(this, MyPageEditActivity::class.java)
            intent.putExtra("check", check)
            startActivity(intent)
        }

        //뒤로가기
        binding.myPageBack.setOnClickListener {
            goBack()
        }


    }

    // 뒤로가기
    fun goBack() {
        finish()
    }

    fun stringToBitmap(encodedString: String): Bitmap? {
        return try {
            val encodeByte: ByteArray = Base64.decode(encodedString, Base64.DEFAULT)
            BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.size)
        } catch (e: Exception) {
            e.message
            null
        }
    }

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onGetMyPageSuccess(response: MyPageResponse) {

        when (response.code) {
            100 -> {
                Log.d("TAG", "onGetMyPageSuccess: MyPage조회성공")
                showCustomToast("MyPage조회성공")

                val kakaoImg: String? = ApplicationClass.sSharedPreferences.getString(
                    Constants.KAKAO_THUMBNAILIMAGEURL,
                    null
                )
                val day = ApplicationClass.sSharedPreferences.getString(
                    Constants.DAY,
                    null
                )
                val name = ApplicationClass.sSharedPreferences.getString(
                    Constants.USER_NICKNAME,
                    null
                )
                val goalTitle = ApplicationClass.sSharedPreferences.getString(
                    Constants.GOALTITLE,
                    null
                )
                val dDayCheck = ApplicationClass.sSharedPreferences
                    .getString(Constants.DDAY_SETTING, null)
                val comments = ApplicationClass.sSharedPreferences.getString(
                    Constants.COMMENTS,
                    null
                )
                val gallery =
                    ApplicationClass.sSharedPreferences.getString(Constants.PROFILE_GALLERY, null)

                val camera =
                    ApplicationClass.sSharedPreferences.getString(Constants.PROFILE_KAMERA, null)

                if (gallery != null) {
                    galleryUrl = gallery.toUri()
                }

                if (camera != null) {
                    cameraImg = stringToBitmap(camera)
                }


//                val kakaoName:String? = ApplicationClass.sSharedPreferences.getString(Constants.KAKAO_USER_NICKNAME,null)
//                val famoName = ApplicationClass.sSharedPreferences.getString(Constants.USER_NICKNAME,null)

                if (day != null) {
                    binding.myPageTextDoneScheduleCount.text = day
                }

                //카카오로그인일경우
                //카톡프사가 없으면 기본 이미지, 있으면 적용

                if (response.loginMethod == "K") {
                    //카톡프사가 없을때 기본이미지 적용, 있으면 있는거 적용
                    if (kakaoImg!!.isEmpty()) {
                        Glide.with(this).load(R.drawable.my_page_img2)
                            .centerCrop().into(binding.myPageImg)
                    } else if (kakaoImg!!.isNotEmpty()) {
                        Glide.with(this).load(kakaoImg)
                            .centerCrop().into(binding.myPageImg)
                    }
                    //check = 1 > 갤러리
                    //check = 2 > 카메라
                    if (check == 1) {
                        binding.myPageImg.setImageURI(galleryUrl)
                    } else if (check == 2) {
                        binding.myPageImg.setImageBitmap(cameraImg)
                    }
                }

                //페모로그인일경우
                if (response.loginMethod == "F") {
                    //처음에는 기본 이미지
                    if (gallery == null && camera == null) {
                        Glide.with(this).load(R.drawable.my_page_img2)
                            .centerCrop().into(binding.myPageImg)
                    } else if (check == 1) {
                        binding.myPageImg.setImageURI(galleryUrl)
                    } else {
                        binding.myPageImg.setImageBitmap(cameraImg)
                    }

                }

                //이름 적용
                binding.myPageTvName.text = name

                //디데이가 설정되어있지 않거나, 값이 없으면 기본 문구가 나오게 설정
                if (dDayCheck != "1" || (goalTitle == null && day == null)) {
                    binding.myPageTvComments.text = "오늘 하루도 힘내세요!"
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
        if (response.isSuccess && response.code == 100) {
            val responseJsonArray = response.data.asJsonArray
            responseJsonArray.forEach {
                val jsonObject = it.asJsonObject
                binding.myPageTextRestScheduleCount.text =  jsonObject.get("remainScheduleCount").asString
            }
        } else {
            Log.d("MyPageFragment", "onGetRestScheduleCountSuccess: ${response.message}")
        }

    }

    override fun onGetRestScheduleCountFailure(message: String) {
    }


    override fun onGetTotalScheduleCountSuccess(response: TotalScheduleCountResponse) {
        if (response.isSuccess && response.code == 100) {
            Log.d("MyPageFragment", "전체 일정/해낸수 조회성공")
            binding.myPageTextAllScheduleCount.text =
                response.totaldata[0].totalScheduleCount.toString()
            binding.myPageTextDoneScheduleCount.text =
                response.totaldonedata[0].doneScheduleCount.toString()

            binding.myPageTextRestScheduleCount.text = (response.totaldata[0].totalScheduleCount -
                    response.totaldonedata[0].doneScheduleCount).toString()
        } else {
            Log.d("MyPageFragment", "onGetTotalScheduleCountSuccess: ${response.message}")
        }
    }

    override fun onGetTotalScheduleCountFailure(message: String) {
    }

    override fun onGetMonthsAchievmentsSuccess(response: MonthsAchievementsResponse) {

        when (response.code) {
            100 -> {
                val achievement = response.data.asJsonObject
                val hashMap: HashMap<String, Int> = Gson().fromJson(
                    achievement.toString(),
                    HashMap::class.java
                )as HashMap<String, Int>
                Log.d("TAG", "onGetMonthsAchievmentsSuccess: ${hashMap}")

            }
            else -> {

            }
        }
    }

    override fun onGetMonthsAchievmentsFailure(message: String) {
    }

}