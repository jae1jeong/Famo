package com.softsquared.template.kotlin.src.auth.information

import android.content.Intent
import android.os.Bundle
import com.softsquared.template.kotlin.R
import com.softsquared.template.kotlin.config.BaseActivity
import com.softsquared.template.kotlin.databinding.ActivityInformationBinding
import com.softsquared.template.kotlin.src.auth.adapter.ViewPagerAdapter
import com.softsquared.template.kotlin.src.auth.loginInformation.LoginInformation
import me.relex.circleindicator.CircleIndicator3

class InformationActivity:BaseActivity<ActivityInformationBinding>(ActivityInformationBinding::inflate) {
    private var titleList = mutableListOf<String>()
    private var descList = mutableListOf<String>()
    private var imagesList = mutableListOf<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        postToList()
        binding.viewPager2.adapter = ViewPagerAdapter(titleList,descList,imagesList)
        val indicator = findViewById<CircleIndicator3>(R.id.information_indicator)
        indicator.setViewPager(binding.viewPager2)
        binding.informationStartButton.setOnClickListener {
            startActivity(Intent(this,LoginInformation::class.java))
            finish()
        }
    }

    private fun addToList(title:String,description:String,image:Int){
        titleList.add(title)
        descList.add(description)
        imagesList.add(image)
    }

    private fun postToList(){
        addToList(resources.getString(R.string.information1_title),resources.getString(R.string.information1_description),R.drawable.information_image)
        addToList(resources.getString(R.string.information2_title),resources.getString(R.string.information2_description),R.drawable.information2_image)
        addToList(resources.getString(R.string.information3_title),resources.getString(R.string.information3_description),R.drawable.information3_image)
    }
}