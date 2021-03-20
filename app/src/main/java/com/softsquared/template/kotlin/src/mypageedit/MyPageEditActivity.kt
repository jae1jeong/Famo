package com.softsquared.template.kotlin.src.mypageedit

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.softsquared.template.kotlin.R
import com.softsquared.template.kotlin.config.BaseActivity
import com.softsquared.template.kotlin.databinding.ActivityMyPageBinding
import com.softsquared.template.kotlin.databinding.ActivityMyPageEditBinding
import com.softsquared.template.kotlin.src.main.mypage.MyPageActivityView
import com.softsquared.template.kotlin.src.main.mypage.MyPageFragment
import com.softsquared.template.kotlin.src.main.mypage.edit.LogoutDialog
import com.softsquared.template.kotlin.src.main.mypage.edit.MyPageEditFragment

class MyPageEditActivity : BaseActivity<ActivityMyPageEditBinding>(ActivityMyPageEditBinding::inflate){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
}