package com.makeus.jfive.famo.src.presentation.monthly

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.makeus.jfive.famo.config.BaseViewModel
import com.makeus.jfive.famo.src.common.Resource
import com.makeus.jfive.famo.src.domain.model.month.MonthByDate
import com.makeus.jfive.famo.src.domain.model.month.MonthlyList
import com.makeus.jfive.famo.src.domain.use_case.get_memo_by_date.GetMemoByDateUseCase
import com.makeus.jfive.famo.src.domain.use_case.get_monthly_date_list.GetMonthlyDateListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

@HiltViewModel
class MonthlyFragmentViewModel @Inject constructor(
    private val getMonthlyDateListUseCase: GetMonthlyDateListUseCase,
    private val getMonthByDateUseCase: GetMemoByDateUseCase
):BaseViewModel() {
    private val _monthlyResponse = MutableLiveData<Resource<MonthlyList>>()
    val monthlyListResponse:LiveData<Resource<MonthlyList>> get() = _monthlyResponse
    private val _monthByDateResponse = MutableLiveData<Resource<MonthByDate>>()
    val monthByDateResponse:LiveData<Resource<MonthByDate>> get() = _monthByDateResponse

    fun getMonthlyList(year:Int,month:Int){
        compositeDisposable.add(
            getMonthlyDateListUseCase.execute(month,year)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .doOnSubscribe {
                    _monthlyResponse.postValue(Resource.Loading())
                }
                .subscribe({
                    _monthlyResponse.postValue(Resource.Success(it))
                },{
                    _monthlyResponse.postValue(Resource.Error(it))
                })
        )
    }

    fun getMonthByDate(scheduleDate:String){
        compositeDisposable.add(
            getMonthByDateUseCase.execute(scheduleDate)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    _monthByDateResponse.postValue(Resource.Success(it))
                },{
                    _monthByDateResponse.postValue(Resource.Error(it))
                })
        )
    }
}