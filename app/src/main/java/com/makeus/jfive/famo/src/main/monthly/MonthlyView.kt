package com.makeus.jfive.famo.src.main.monthly

import com.makeus.jfive.famo.src.main.monthly.models.AllMemoResponse
import com.makeus.jfive.famo.src.main.monthly.models.MonthlyMemoItemResponse
import com.makeus.jfive.famo.src.main.monthly.models.MonthlyUserDateListResponse

interface MonthlyView {
    fun onGetMonthlyMemoItemSuccess(response:MonthlyMemoItemResponse)
    fun onGetMonthlyMemoItemFailure(message:String)
    fun onGetAllMemosSuccess(response:AllMemoResponse)
    fun onGetAllMemosFailure(message: String)
    fun onGetUserDateListSuccess(response:MonthlyUserDateListResponse)
    fun onGetUserDateListFailure(message:String)
}