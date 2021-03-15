package com.softsquared.template.kotlin.src.auth.test

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import com.softsquared.template.kotlin.R
import com.softsquared.template.kotlin.config.BaseActivity
import com.softsquared.template.kotlin.databinding.ActivityTestBinding
import com.softsquared.template.kotlin.src.auth.signup.SignUpView
import kotlinx.android.synthetic.main.activity_test.view.*
import kotlin.math.log

class Test: BaseActivity<ActivityTestBinding>(ActivityTestBinding::inflate) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val accessToken = intent.getStringExtra("accessToken")
        val refreshToken = intent.getStringExtra("refreshToken")
        Log.d("TAG", "accessToken: $accessToken")
        Log.d("TAG", "refreshToken: $refreshToken")

        val number : EditText = findViewById(R.id.number)
        val button : Button = findViewById(R.id.button)

        button.setOnClickListener {
            val inputNum = number.text.toString()
            Log.d("TAG", "number: $inputNum")
            Log.d("TAG", "accessToken: $accessToken")
            Log.d("TAG", "refreshToken: $refreshToken")
        }



    }
}