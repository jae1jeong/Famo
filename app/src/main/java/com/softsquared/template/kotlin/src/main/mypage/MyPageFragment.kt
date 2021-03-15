package com.softsquared.template.kotlin.src.main.mypage

import android.os.Bundle
import android.util.Log
import android.view.View
import com.bumptech.glide.Glide
import com.softsquared.template.kotlin.R
import com.softsquared.template.kotlin.config.ApplicationClass
import com.softsquared.template.kotlin.config.BaseFragment
import com.softsquared.template.kotlin.databinding.FragmentMypageBinding
import com.softsquared.template.kotlin.src.main.mypage.models.MyPageResponse
import com.softsquared.template.kotlin.util.Constants

class MyPageFragment(val myPageActivityView: MyPageActivityView):
    BaseFragment<FragmentMypageBinding>(FragmentMypageBinding::bind, R.layout.fragment_mypage),
    MyPageView{

    var token : String? = null
    var name : String? = null
    var img : String?= null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        MyPageService(this).tryGetMyPage()

        super.onViewCreated(view, savedInstanceState)

        var extra = this.arguments
        if (extra != null) {
            extra = arguments
            token = extra?.getString("token")
            name = extra?.getString("name")
            img = extra?.getString("img").toString()
            Log.d("MyPageFragment 잘들어 왔나 token", "값: $token")
            Log.d("MyPageFragment 잘들어 왔나 name", "값: $name")
            Log.d("MyPageFragment 잘들어 왔나 img", "값: $img")
        }

        val name = ApplicationClass.sSharedPreferences.getString(Constants.USER_NICKNAME,null)

        Glide.with(this).load(img)
            .centerCrop().into(binding.myPageImg)

        binding.myPageTvName.text = name

        //이미지 앞으로 내보내기
        binding.myPageSetting.bringToFront()

        //프로필 편집으로 이동
        binding.myPageImg.setOnClickListener {
//            val intent = Intent(activity,MyPageActivity::class.java)
//            startActivity(intent)

            myPageActivityView.moveMyPageEdit()
        }

        //뒤로가기
        binding.myPageBtnBack.setOnClickListener {
//            binding.categoryEditLinear.visibility = View.GONE
//            val intent = Intent(context,MyPageActivity::class.java)
//            startActivity(intent)
            (activity as MyPageActivity).moveScheduleFind()
        }
    }

    override fun onGetMyPageSuccess(response: MyPageResponse) {

        when(response.code){
            100 -> {
                Log.d("TAG", "onGetMyPageSuccess: MyPage조회성공")
                showCustomToast("MyPage조회성공")

                val kakaoName:String? = ApplicationClass.sSharedPreferences.getString(Constants.KAKAO_USER_NICKNAME,null)
                val kakaoImg:String? = ApplicationClass.sSharedPreferences.getString(Constants.KAKAO_THUMBNAILIMAGEURL,null)

                if (response.loginMethod == "K"){
                    binding.myPageTvName.text = kakaoName

                    if (kakaoImg != null){
                        Glide.with(this).load(kakaoImg)
                            .centerCrop().into(binding.myPageImg)
                    }else{
                        Glide.with(this).load(R.drawable.my_page_img2)
                            .centerCrop().into(binding.myPageImg)
                    }
                }else{
                    binding.myPageTvName.text = name
                    Glide.with(this).load(R.drawable.my_page_img2)
                        .centerCrop().into(binding.myPageImg)
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


}