package com.makeus.jfive.famo.src.presentation.today

import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.makeus.jfive.famo.R
import com.makeus.jfive.famo.config.BaseFragment
import com.makeus.jfive.famo.databinding.FragmentTodayBinding
import com.makeus.jfive.famo.src.common.Resource
import com.makeus.jfive.famo.src.domain.model.today.TodayMemo
import com.makeus.jfive.famo.src.domain.use_case.get_detail_memo.GetDetailMemoUseCase
import com.makeus.jfive.famo.src.domain.use_case.get_done_schdule_count.GetDoneScheduleCountUseCase
import com.makeus.jfive.famo.src.domain.use_case.get_remain_schedule_count.GetRemainScheduleCountUseCase
import com.makeus.jfive.famo.src.domain.use_case.get_today_memo.GetTodayMemosUseCase
import com.makeus.jfive.famo.src.domain.use_case.get_today_memo.GetTodayMemosUseCase_Factory
import com.makeus.jfive.famo.src.domain.use_case.get_user_category.GetUserCategoryUseCase
import com.makeus.jfive.famo.src.domain.use_case.patch_memo.PatchMemoUseCase
import com.makeus.jfive.famo.src.domain.use_case.post_add_memo.PostAddMemoUseCase
import com.makeus.jfive.famo.src.presentation.main.MainActivity
import com.makeus.jfive.famo.src.main.ResultBottomSheetDialog
import com.makeus.jfive.famo.src.main.today.MemoSwipeHelper
import com.makeus.jfive.famo.src.main.today.SwipeButton
import com.makeus.jfive.famo.src.main.today.SwipeButtonClickListener
import com.makeus.jfive.famo.src.main.today.models.*
import com.makeus.jfive.famo.src.presentation.main.MainActivityViewModel
import com.makeus.jfive.famo.util.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_today.*
import javax.inject.Inject


@AndroidEntryPoint
class TodayFragment() :
    BaseFragment<FragmentTodayBinding>(FragmentTodayBinding::bind, R.layout.fragment_today) {

    private val TAG = javaClass.simpleName
    companion object {
        val memoList: ArrayList<MemoItem> = arrayListOf()
        var doneScheduleCount = 0
        var restScheduleCount = 0
    }


    lateinit var todayMemoAdapter: MemoAdapter
    private val todayFragmentViewModel: TodayFragmentViewModel by viewModels()
    @Inject
    lateinit var mSharedPreferences: SharedPreferences
    @Inject
    lateinit var remainScheduleCountUseCase: GetRemainScheduleCountUseCase
    @Inject
    lateinit var doneScheduleCountUseCase: GetDoneScheduleCountUseCase
    @Inject
    lateinit var postAddMemoUseCase: PostAddMemoUseCase
    @Inject
    lateinit var getUserCategoryUseCase: GetUserCategoryUseCase
    @Inject
    lateinit var getDetailMemoUseCase: GetDetailMemoUseCase
    @Inject
    lateinit var getTodayMemosUseCase: GetTodayMemosUseCase
    @Inject
    lateinit var patchMemoUseCase:PatchMemoUseCase


    private val mainActivityViewModel: MainActivityViewModel by activityViewModels {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T =
                MainActivityViewModel(
                    remainScheduleCountUseCase,
                    doneScheduleCountUseCase,
                    postAddMemoUseCase,
                    getUserCategoryUseCase,
                    getDetailMemoUseCase,
                    getTodayMemosUseCase,
                    patchMemoUseCase
                ) as T
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAdapter()
        initViewModel()

        // 메모가 없을때 뷰 클릭
        binding.todayImageNoItem.setOnClickListener {
            (activity as MainActivity).stateChangeBottomSheet(Constants.COLLASPE)
        }
        mainActivityViewModel.getScheduleCounts("today")
        mainActivityViewModel.getTodayMemos()
        todayFragmentViewModel.getTopComment()
        setUpTodayMemoCheck()
        setUpTodayMemoClick()
    }

    private fun changeSchedulePosition(fromPos: Int, targetPos: Int) {
        Log.d("순서", "$fromPos 에서 -> $targetPos 로")
        Log.d(
            "순서",
            "isSuccess: ${memoList[fromPos].id}번 일정 위치를 ${memoList[fromPos].title} $targetPos 로 이동 ${memoList.size}"
        )
    }



    private fun setUpTodayMemoClick() {
        compositeDisposable.add(
            todayMemoAdapter.clickPublishSubject.subscribe {
                // 디테일 다이얼로그
                mainActivityViewModel.getDetailMemo(it.scheduleID)
                mainActivityViewModel.setEditingScheduleId(it.scheduleID)
            }
        )
    }

    private fun stopShimmerAnimation() {
        if (today_shimmer_frame_layout.isAnimationStarted()) {
            today_shimmer_frame_layout.stopShimmerAnimation()
            today_shimmer_header_frame_layout.stopShimmerAnimation()
            today_shimmer_header_frame_layout.visibility = View.GONE
            today_shimmer_frame_layout.visibility = View.GONE
            binding.todayRecyclerView.visibility = View.VISIBLE
            binding.todayLayoutTopMent.visibility = View.VISIBLE
        }
    }

    private fun initAdapter() {
        todayMemoAdapter = MemoAdapter(requireContext())
        // 어댑터
        binding.todayRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = todayMemoAdapter
        }
        // 리사이클러뷰 아이템 스와이프,드래그
        val swipe = object : MemoSwipeHelper(
            todayMemoAdapter,
            requireContext(),
            binding.todayRecyclerView,
            200,
            { fromPos, targetPos ->
                // 서로 바뀐 것 같아서 반대로
                changeSchedulePosition(targetPos, fromPos)
                for (i in 0 until memoList.size) Log.d("순서", "index:$i ${memoList[i].title}")
            }) {
            override fun instantiateMyButton(
                viewHolder: RecyclerView.ViewHolder,
                buffer: MutableList<SwipeButton>
            ) {

                // R.drawable.schedule_delete
                buffer.add(SwipeButton(context!!,
                    "삭제",
                    50,
                    0,
                    Color.parseColor("#32363C"),
                    object : SwipeButtonClickListener {
                        override fun onClick(pos: Int) {
                            NewAskDialogBuilder.Builder(requireContext())
                                .setTitle("일정 삭제")
                                .setMessage("일정을 삭제하시겠습니까")
                                .setPositiveBtn("삭제") {
                                    todayFragmentViewModel.deleteMemo(todayMemoAdapter.getMemoByPosition(pos).scheduleID)
                                    todayMemoAdapter.removeMemo(pos)
                                }
                                .setNegativeBtn("취소"){}
                                .build()
                                .show()
                        }
                    }
                ))
                //R.drawable.schedule_share
                buffer.add(SwipeButton(requireContext(),
                    "공유",
                    50,
                    0,
                    Color.parseColor("#FFAE2A"),
                    object : SwipeButtonClickListener {
                        override fun onClick(pos: Int) {
                            val memo = todayMemoAdapter.getMemoByPosition(pos)
                            val sendStringData =
                                "${memo.scheduleName}\n${memo.scheduleMemo}\n${memo.scheduleFormDate}"
                            val sendIntent = Intent().apply {
                                action = Intent.ACTION_SEND
                                putExtra(Intent.EXTRA_TEXT, sendStringData)
                                type = "text/plain"
                            }
                            if (sendIntent.resolveActivity((activity as MainActivity).packageManager) != null) {
                                startActivity(sendIntent)
                            }
                        }
                    }
                ))
            }
        }
    }

    private fun setUpTodayMemoCheck() {
        compositeDisposable.add(
            todayMemoAdapter.checkPublishSubject.subscribe {
                todayFragmentViewModel.postCheckTodayModel(it.scheduleID)
                it.scheduleStatus = !it.scheduleStatus
                if (it.scheduleStatus) {
                    mainActivityViewModel.plusDoneCount()
                    mainActivityViewModel.minusRemainCount()
                } else {
                    mainActivityViewModel.minusDoneCount()
                    mainActivityViewModel.plusRemainCount()
                }
            }
        )
    }

    private fun initViewModel() {
        todayFragmentViewModel.commentRes.observe(viewLifecycleOwner, { res ->
            when (res) {
                is Resource.Loading -> {
                    today_shimmer_header_frame_layout.startShimmerAnimation()
                    today_shimmer_frame_layout.startShimmerAnimation()
                }
                is Resource.Success -> {
                    res.data?.let {
                        when (it.goalStatus) {
                            1 -> {
                                binding.todayTextGoalTitle.text = String.format(
                                    getString(R.string.today_top_comment_goal_title_format),
                                    it.goalTitle
                                )
                                binding.todayTextGoalSubTitle.text =
                                    String.format(
                                        getString(R.string.today_dday_count_format),
                                        it.dday
                                    )
                            }
                            // 디데이 설정 x
                            -1 -> {
                                binding.todayTextGoalTitle.text = String.format(
                                    getString(R.string.today_top_comment_user_nickname_format),
                                    it.nickname
                                )
                                binding.todayTextGoalSubTitle.text = it.titleComment
                            }
                        }
                    }
                    stopShimmerAnimation()
                }
                is Resource.Error -> {
                    stopShimmerAnimation()
                }

            }

        })
        mainActivityViewModel.todayMemos.observe(viewLifecycleOwner, {
            if (it.size.equals(0)) {
                binding.todayFrameLayoutNoItem.visibility = View.VISIBLE
            } else {
                binding.todayFrameLayoutNoItem.visibility = View.GONE
            }
            todayMemoAdapter.setNewMemoList(it as ArrayList<TodayMemo>)
        })
        mainActivityViewModel.doneScheduleCount.observe(viewLifecycleOwner, {
            binding.todayTextDoneSchedule.text =
                String.format(getString(R.string.today_done_schedule_count_format), it)
        })
        mainActivityViewModel.remainScheduleCount.observe(viewLifecycleOwner, {
            binding.todayTextRestSchedule.text =
                String.format(getString(R.string.today_rest_count_format), it)
        })
    }


    fun showResultFragment() {
        val resultFragment = ResultBottomSheetDialog()
        resultFragment.show(childFragmentManager, resultFragment.tag)
    }

}