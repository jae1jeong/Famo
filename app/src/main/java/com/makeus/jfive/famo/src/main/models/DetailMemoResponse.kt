package com.makeus.jfive.famo.src.main.models

import com.google.gson.JsonArray
import com.makeus.jfive.famo.config.BaseResponse

data class DetailMemoResponse (
     val data:JsonArray
        ):BaseResponse()