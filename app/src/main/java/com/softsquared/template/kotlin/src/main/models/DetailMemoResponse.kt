package com.softsquared.template.kotlin.src.main.models

import com.google.gson.JsonArray
import com.softsquared.template.kotlin.config.BaseResponse

data class DetailMemoResponse (
     val data:JsonArray
        ):BaseResponse()