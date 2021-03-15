package com.softsquared.template.kotlin.src.auth.signup.models

data class GetRequestCheckAuthNumber (
      val phoneNumber:String,
      val authCode:String
        )