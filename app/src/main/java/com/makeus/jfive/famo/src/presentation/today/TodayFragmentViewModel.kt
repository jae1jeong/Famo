package com.makeus.jfive.famo.src.presentation.today

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.makeus.jfive.famo.config.BaseViewModel
import com.makeus.jfive.famo.src.common.Resource
import com.makeus.jfive.famo.src.domain.model.today.*
import com.makeus.jfive.famo.src.domain.use_case.delete_memo.DeleteMemoUseCase
import com.makeus.jfive.famo.src.domain.use_case.get_today_memo.GetTodayMemosUseCase
import com.makeus.jfive.famo.src.domain.use_case.get_top_comment.GetTopCommentUseCase
import com.makeus.jfive.famo.src.domain.use_case.post_item_check.PostCheckTodayMemoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

@HiltViewModel
class TodayFragmentViewModel @Inject constructor(
    private val getTopCommentUseCase: GetTopCommentUseCase,
    private val postCheckTodayMemoUseCase: PostCheckTodayMemoUseCase,
    private val deleteMemoUseCase: DeleteMemoUseCase
):BaseViewModel() {
    private val _commentRes = MutableLiveData<Resource<TopComment>>()
    val commentRes:LiveData<Resource<TopComment>> get() = _commentRes

    fun deleteMemo(scheduleId: Int){
        compositeDisposable.add(
            deleteMemoUseCase.execute(scheduleId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe()
        )
    }

    fun getTopComment(){
        compositeDisposable.add(
            getTopCommentUseCase.execute()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .doOnSubscribe {
                    _commentRes.postValue(Resource.Loading())
                }
                .subscribe({
                    _commentRes.postValue(Resource.Success(it))
                },{
                    _commentRes.postValue(Resource.Error(it))
                })
        )
    }

    fun postCheckTodayModel(scheduleId:Int){
        compositeDisposable.add(
            postCheckTodayMemoUseCase.execute(PostMemoCheck(scheduleId))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                },{
                    Log.e("TAG", "postCheckTodayModel: $it", )
                })
        )
    }

}