package com.softsquared.template.kotlin.src.searchhistories

import com.softsquared.template.kotlin.config.BaseResponse
import com.softsquared.template.kotlin.src.searchhistories.models.SearchHistoriesResponse
import retrofit2.Call
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Query

interface SearchHistoriesRetrofitInterface {

    @GET("schedules/search/histories")
    fun getSearchHistories() : Call<SearchHistoriesResponse>

    @DELETE("schedules/search/histories")
    fun deleteSearchHistories(@Query("searchHistory") searchHistory : String)
        : Call<BaseResponse>

}