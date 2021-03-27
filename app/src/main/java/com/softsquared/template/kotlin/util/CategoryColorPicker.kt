package com.softsquared.template.kotlin.util

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.util.Log
import android.view.View

object CategoryColorPicker {

    @SuppressLint("ResourceType")
    fun setCategoryColor(colorInfo:String?):String{
        return if(colorInfo == null){
            "#CED5D9"
        }else{
            colorInfo
        }
    }
    // 카테고리 색상과 둥글게 변형
    fun setCategoryColorRadius(colorInfo:String?,view: View){
        val categoryColor = setCategoryColor(colorInfo)
        val shape = GradientDrawable()
        Log.d("TAG", "setCategoryColorRadius: $colorInfo")
        shape.setColor(Color.parseColor(categoryColor))
        shape.cornerRadius = 180F
        view.background = shape
    }


}