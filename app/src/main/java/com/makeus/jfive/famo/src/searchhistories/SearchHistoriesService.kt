package com.makeus.jfive.famo.src.searchhistories

import android.util.Log
import com.makeus.jfive.famo.config.ApplicationClass
import com.makeus.jfive.famo.config.BaseResponse
import com.makeus.jfive.famo.src.searchhistories.models.SearchHistoriesResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchHistoriesService(val view : SearchHistoriesView) {

    //검색기록
    fun tryGetSearchHistories(){
        val homeRetrofitInterface = ApplicationClass.sRetrofit.create(SearchHistoriesRetrofitInterface::class.java)

        homeRetrofitInterface.getSearchHistories().enqueue(object :
            Callback<SearchHistoriesResponse> {
            override fun onResponse(call: Call<SearchHistoriesResponse>,
                                    response: Response<SearchHistoriesResponse>) {
                view.onGetSearchHistoriesSuccess(response.body() as SearchHistoriesResponse)
            }

            override fun onFailure(call: Call<SearchHistoriesResponse>, t: Throwable) {
                Log.d("TAG", "onFailure: 전체최근일정조회오류확인 ${t.message}")
                view.onGetSearchHistoriesFail(t.message ?: "통신 오류")
            }
        })
    }

    //검색기록삭제
    fun tryDeleteSearchHistories(searchHistory : String){
        val homeRetrofitInterface = ApplicationClass.sRetrofit.create(SearchHistoriesRetrofitInterface::class.java)

        homeRetrofitInterface.deleteSearchHistories(searchHistory).enqueue(object :
            Callback<BaseResponse> {
            override fun onResponse(call: Call<BaseResponse>,
                                    response: Response<BaseResponse>) {
                view.onDeleteSearchHistoriesSuccess(response.body() as BaseResponse)
            }

            override fun onFailure(call: Call<BaseResponse>, t: Throwable) {
                Log.d("TAG", "onFailure: 전체최근일정조회오류확인 ${t.message}")
                view.onDeleteSearchHistoriesFail(t.message ?: "통신 오류")
            }
        })
    }
}