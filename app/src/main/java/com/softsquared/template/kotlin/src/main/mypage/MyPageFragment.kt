package com.softsquared.template.kotlin.src.main.mypage

import android.os.Bundle
import android.util.Log
import android.view.View
import com.bumptech.glide.Glide
import com.softsquared.template.kotlin.R
import com.softsquared.template.kotlin.config.ApplicationClass
import com.softsquared.template.kotlin.config.BaseFragment
import com.softsquared.template.kotlin.databinding.FragmentMypageBinding
import com.softsquared.template.kotlin.src.main.mypage.models.MyPageCommentsResponse
import com.softsquared.template.kotlin.util.Constants

class MyPageFragment(val myPageActivityView: MyPageActivityView):
    BaseFragment<FragmentMypageBinding>(FragmentMypageBinding::bind, R.layout.fragment_mypage),
    MyPageView{

    var token : String? = null
    var name : String? = null
    var img : String?= null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        MyPageService(this).tryGetMyPageComments()

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

    override fun onGetMyPageCommentsSuccess(response: MyPageCommentsResponse) {

        when(response.code){
            100 -> {
                Log.d("TAG", "onGetMyPageCommentsSuccess: 상단코멘트 성공")
                showCustomToast("상단코맨트성공")
                binding.myPageTvComments.text = response.titleComment
           }
        }
    }

    override fun onGetMyPageCommentsFail(message: String) {
    }


}