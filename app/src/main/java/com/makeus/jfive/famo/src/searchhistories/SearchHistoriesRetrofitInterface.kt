package com.makeus.jfive.famo.src.searchhistories

import com.makeus.jfive.famo.config.BaseResponse
import com.makeus.jfive.famo.src.searchhistories.models.SearchHistoriesResponse
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