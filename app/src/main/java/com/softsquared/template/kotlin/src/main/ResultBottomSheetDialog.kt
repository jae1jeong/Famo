package com.softsquared.template.kotlin.src.main

import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.softsquared.template.kotlin.R
import com.softsquared.template.kotlin.config.ApplicationClass
import com.softsquared.template.kotlin.src.mypage.MyPageActivity
import com.softsquared.template.kotlin.util.Constants
import kotlinx.android.synthetic.main.fragment_result_bottom_sheet_dialog.*
import kotlin.random.Random

class ResultBottomSheetDialog:BottomSheetDialogFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?
                              , savedInstanceState: Bundle?): View {


       return inflater.inflate(R.layout.fragment_result_bottom_sheet_dialog,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)
        val randomNum:Int = (0..2).random()
        var userNickname = ""
        userNickname = ApplicationClass.sSharedPreferences.getString(Constants.USER_NICKNAME,"").toString()
        if(userNickname == ""){
            userNickname = ApplicationClass.sSharedPreferences.getString(Constants.KAKAO_USER_NICKNAME,"").toString()
        }
        when(randomNum){
            0 ->{
                result_image.setImageResource(R.drawable.result1)
                result_text_title.text = "${userNickname}님 오늘 하루도\n" +
                        "알찬 하루를 보내셨네요!"
            }
            1->{
                result_image.setImageResource(R.drawable.result2)
                result_text_title.text = "${userNickname}님의 열정으로\n귀차니즘을 뿌셔뿌셔!"
            }
            2->{
                result_image.setImageResource(R.drawable.result3)
                result_text_title.text = "${userNickname}님의 폭발적인\n" +
                        "열정이 멈추지 않네요!"
            }
            else->{dismiss()}
        }
        // 나의 성과 확인하러 가기 버튼
        result_btn_go_to_page.setOnClickListener {
            startActivity(Intent(context!!,MyPageActivity::class.java))
            onDestroy()
        }
        // 다음에 할게요 버튼
        result_btn_next_time.setOnClickListener {
            dismiss()
        }

    }
}