package com.makeus.jfive.famo.src.presentation.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.makeus.jfive.famo.config.BaseViewModel
import com.makeus.jfive.famo.src.common.BottomSheetDialogState
import com.makeus.jfive.famo.src.common.Resource
import com.makeus.jfive.famo.src.domain.model.main.*
import com.makeus.jfive.famo.src.domain.model.month.MonthMemo
import com.makeus.jfive.famo.src.domain.model.today.TodayMemo
import com.makeus.jfive.famo.src.domain.model.today.mapperToTodayMemo
import com.makeus.jfive.famo.src.domain.use_case.get_detail_memo.GetDetailMemoUseCase
import com.makeus.jfive.famo.src.domain.use_case.get_done_schdule_count.GetDoneScheduleCountUseCase
import com.makeus.jfive.famo.src.domain.use_case.get_remain_schedule_count.GetRemainScheduleCountUseCase
import com.makeus.jfive.famo.src.domain.use_case.get_today_memo.GetTodayMemosUseCase
import com.makeus.jfive.famo.src.domain.use_case.get_user_category.GetUserCategoryUseCase
import com.makeus.jfive.famo.src.domain.use_case.patch_memo.PatchMemoUseCase
import com.makeus.jfive.famo.src.domain.use_case.post_add_memo.PostAddMemoUseCase
import com.makeus.jfive.famo.src.main.models.MainScheduleCategory
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val getRemainScheduleCountUseCase: GetRemainScheduleCountUseCase,
    private val getDoneScheduleCountUseCase: GetDoneScheduleCountUseCase,
    private val postAddMemoUseCase: PostAddMemoUseCase,
    private val getUserCategoryUseCase: GetUserCategoryUseCase,
    private val getDetailMemoUseCase: GetDetailMemoUseCase,
    private val getTodayMemoUseCase: GetTodayMemosUseCase,
    private val patchMemoUseCase: PatchMemoUseCase
) : BaseViewModel() {

    private val _remainScheduleCount = MutableLiveData<Int>()
    val remainScheduleCount: LiveData<Int> get() = _remainScheduleCount
    private val _doneScheduleCount = MutableLiveData<Int>()
    val doneScheduleCount: LiveData<Int> get() = _doneScheduleCount
    private val _editingMemo = MutableLiveData<PatchMemo?>()
    val editingMemo: LiveData<PatchMemo?> get() = _editingMemo
    private val _userCategoryList = MutableLiveData<List<MainScheduleCategory>>()
    val userCategoryList: LiveData<List<MainScheduleCategory>> get() = _userCategoryList
    val selectedDate = MutableLiveData<String>()
    val selectedCategoryId = MutableLiveData<Int?>()
    private val _detailMemo = MutableLiveData<Resource<DetailMemo>>()
    val detailMemo:LiveData<Resource<DetailMemo>> get() = _detailMemo
    private val _editingScheduleID = MutableLiveData<Int?>()
    val editingScheduleID:LiveData<Int?> get() = _editingScheduleID
    private val _todayMemos = MutableLiveData<List<TodayMemo>>()
    val todayMemos:LiveData<List<TodayMemo>> get() = _todayMemos
    val addMemoRes = MutableLiveData<Boolean>()


    private val TAG = "TAG"

    fun getScheduleCounts(date: String) {
        compositeDisposable.add(
            Observable.zip(
                getRemainScheduleCountUseCase.execute(date).subscribeOn(Schedulers.io()),
                getDoneScheduleCountUseCase.execute().subscribeOn(Schedulers.io()),
                { remainCnt, doneCntDto ->
                    _remainScheduleCount.postValue(remainCnt.data.get(0).count)
                    val doneCnt = doneCntDto.doneScheduleCountDto.get(0).doneCount
                    _doneScheduleCount.postValue(doneCnt)
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe()
        )
    }

    fun postAddMemo(postTodayRequestAddMemo: PostTodayRequestAddMemo) {
        compositeDisposable.add(
            postAddMemoUseCase.execute(postTodayRequestAddMemo)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    addMemoRes.postValue(true)
                    getTodayMemos()
                }, {
                    addMemoRes.postValue(false)
                    Log.e("TAG", "postAddMemo: $it")
                })
        )
    }

    fun getUserCategory() {
        compositeDisposable.add(
            getUserCategoryUseCase.execute()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    _userCategoryList.postValue(it.categoryDto)
                }, {
                    Log.e(TAG, "getUserCategory: $it")
                })
        )
    }
    fun setEditingMemo(patchMemo: PatchMemo?){
        _editingMemo.postValue(patchMemo)
    }

    fun setEditingScheduleId(scheduleId: Int?){
        _editingScheduleID.postValue(scheduleId)
    }

    fun getDetailMemo(scheduleId:Int){
        compositeDisposable.add(
            getDetailMemoUseCase.execute(scheduleId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    _detailMemo.postValue(Resource.Loading())
                }
                .subscribe({
                           _detailMemo.postValue(Resource.Success(it.detailMemoDto.get(0)))
                },{
                  _detailMemo.postValue(Resource.Error(it))
                })
        )
    }

    fun getTodayMemos(){
        compositeDisposable.add(
            getTodayMemoUseCase.execute()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    it.todayMemoDtoList.mapperToTodayMemo().let {
                    _todayMemos.postValue(it)
                }
                },{
                    Log.e(TAG, "getTodayMemos: ${it.message}", )
                })
        )
    }

    fun plusRemainCount() {
        _remainScheduleCount.postValue(_remainScheduleCount.value?.plus(1))
    }

    fun minusRemainCount() {
        _remainScheduleCount.postValue(_remainScheduleCount.value?.minus(1))
    }

    fun plusDoneCount() {
        _doneScheduleCount.postValue(_doneScheduleCount.value?.plus(1))
    }

    fun minusDoneCount() {
        _doneScheduleCount.postValue(_doneScheduleCount.value?.minus(1))
    }
    fun patchMemo(scheduleId:Int,patchMemo: PatchMemo){
        compositeDisposable.add(
            patchMemoUseCase.execute(scheduleId, patchMemo)
                .subscribeOn(Schedulers.io())
                .subscribe({
                    getTodayMemos()
                },{

                })
        )
    }
}