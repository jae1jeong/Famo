package com.softsquared.template.kotlin.src.main.schedulefind

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.softsquared.template.kotlin.R
import com.softsquared.template.kotlin.config.ApplicationClass
import com.softsquared.template.kotlin.src.main.schedulefind.adapter.IScheduleCategoryRecyclerView
import com.softsquared.template.kotlin.src.main.schedulefind.adapter.ScheduleWholeAdapter
import com.softsquared.template.kotlin.src.main.schedulefind.models.CategoryFilterResponse
import com.softsquared.template.kotlin.src.mypageedit.MyPageEditService
import com.softsquared.template.kotlin.src.mypageedit.models.PutMyPageUpdateRequest
import com.softsquared.template.kotlin.util.Constants
import kotlinx.android.synthetic.main.fragment_schedule_find_filter_bottom_dialog.*
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*

class SchedulefindFilterBottomDialogFragment(myScheduleCategoryRecyclerView: IScheduleCategoryRecyclerView) : BottomSheetDialogFragment(),
    CategoryFilterView, View.OnClickListener {


    private var iScheduleCategoryRecyclerView: IScheduleCategoryRecyclerView? = null

    private var iCategoryFilterInterface: CategoryFilterInterface? = null

//    constructor(context: Context) : super(context){
//        mContext = context
//    }

    init {
        this.iScheduleCategoryRecyclerView = myScheduleCategoryRecyclerView
    }


    lateinit var mOnDialogButtonClickListener: OnDialogButtonClickListener

    interface OnDialogButtonClickListener {
        fun onDialogButtonClick(view: View)

    }

    fun setOnDialogButtonClickListener(mListener: OnDialogButtonClickListener) {
        mOnDialogButtonClickListener = mListener
    }


//    init {
//        Log.d("TAG", "ScheduleCategoryAdapter: init() called ")
//        this.iCategoryFilterInterface = categoryFilterInterface
//    }

    //클릭에 따른 체크표시 활성화를 위한 변수
    var remainCnt = 1
    var completionCnt = 1
    var recentsCnt = 1
    var bookmarkCnt = 1

    var checkNum = 0

    private var id: Int? = null

    // 전체 일정 어댑터
    private lateinit var scheduleWholeAdapter: ScheduleWholeAdapter


    init {
        Log.d("TAG", "ScheduleCategoryAdapter: init() called ")
//        this.id = scheduleCategoryID
    }


//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?):
//            View = inflater.inflate(R.layout.fragment_schedule_find_filter_bottom_dialog, container, false)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ):
            View {
        val view =
            inflater.inflate(R.layout.fragment_schedule_find_filter_bottom_dialog, container, false)
        return view
    }


    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        filter_btn_remain!!.setOnClickListener(this)
        filter_btn_completion!!.setOnClickListener(this)
        filter_btn_recents!!.setOnClickListener(this)
        filter_btn_bookmark!!.setOnClickListener(this)

//        남은 일정 클릭 시
//        filter_btn_remain.setOnTouchListener { _, event ->
//            when (event.action) {
//                MotionEvent.ACTION_DOWN -> {
//
//                    if (remainCnt % 2 != 0) {
//                        filter_btn_remain_squre.visibility = View.VISIBLE
//                        filter_btn_remain_check.visibility = View.VISIBLE
//
//                        filter_btn_completion_squre.visibility = View.GONE
//                        filter_btn_recents_squre.visibility = View.GONE
//                        filter_btn_bookmark_squre.visibility = View.GONE
//                        checkNum = 1
//
//                    } else {
//                        filter_btn_remain_squre.visibility = View.GONE
//                        filter_btn_remain_check.visibility = View.GONE
//                    }
//                    remainCnt++
//
//                }
//            }
//            false
//        }

        filter_btn_remain.setOnClickListener {

            if (remainCnt % 2 != 0) {
                filter_btn_remain_squre.visibility = View.VISIBLE
                filter_btn_remain_check.visibility = View.VISIBLE

                filter_btn_completion_squre.visibility = View.GONE
                filter_btn_recents_squre.visibility = View.GONE
                filter_btn_bookmark_squre.visibility = View.GONE
                checkNum = 1

            } else {
                filter_btn_remain_squre.visibility = View.GONE
                filter_btn_remain_check.visibility = View.GONE
            }
            remainCnt++
        }

//        완료 일정 클릭 시
        filter_btn_completion.setOnClickListener {

            if (completionCnt % 2 != 0) {
                filter_btn_completion_squre.visibility = View.VISIBLE
                filter_btn_completion_check.visibility = View.VISIBLE

                filter_btn_remain_squre.visibility = View.GONE
                filter_btn_recents_squre.visibility = View.GONE
                filter_btn_bookmark_squre.visibility = View.GONE
                checkNum = 2

            } else {
                filter_btn_completion_squre.visibility = View.GONE
                filter_btn_completion_check.visibility = View.GONE
            }
            completionCnt++

        }

//        최신 클릭 시
        filter_btn_recents.setOnClickListener {

            if (recentsCnt % 2 != 0) {
                filter_btn_recents_squre.visibility = View.VISIBLE
                filter_btn_recents_check.visibility = View.VISIBLE

                filter_btn_completion_squre.visibility = View.GONE
                filter_btn_remain_squre.visibility = View.GONE
                filter_btn_bookmark_squre.visibility = View.GONE
                checkNum = 3
            } else {
                filter_btn_recents_squre.visibility = View.GONE
                filter_btn_recents_check.visibility = View.GONE
            }
            recentsCnt++

        }

//        즐겨찾기 클릭 시
        filter_btn_bookmark.setOnClickListener {

            if (bookmarkCnt % 2 != 0) {
                filter_btn_bookmark_squre.visibility = View.VISIBLE
                filter_btn_bookmark_check.visibility = View.VISIBLE

                filter_btn_recents_squre.visibility = View.GONE
                filter_btn_completion_squre.visibility = View.GONE
                filter_btn_remain_squre.visibility = View.GONE
                checkNum = 4
            } else {
                filter_btn_bookmark_squre.visibility = View.GONE
                filter_btn_bookmark_check.visibility = View.GONE
            }
            bookmarkCnt++

        }

    }

    @SuppressLint("InflateParams", "CommitPrefEdits")
    override fun onGetCategoryFilterInquirySuccess(response: CategoryFilterResponse) {


    }

//    override fun onAttach(activity: Activity) {
//        super.onAttach(activity)
//
//        try {
//            mOnDialogButtonClickListener = activity as OnDialogButtonClickListener
//        }catch (e: ClassCastException){
//            Log.d("TAG", "onAttach: 에러")
//
//        }
//    }

//    override fun onActivityCreated(savedInstanceState: Bundle?) {
//        super.onActivityCreated(savedInstanceState)
//
//        filter_btn_remain.setOnClickListener {
//            mOnDialogButtonClickListener.onDialogButtonClick("첫번째")
//            dismiss()
//        }
//    }

    override fun onGetCategoryFilterInquiryFail(message: String) {
    }

    override fun onClick(v: View?) {
        mOnDialogButtonClickListener.onDialogButtonClick(v!!)
    }

    @SuppressLint("CommitPrefEdits")
    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)

        val parentFragment: Fragment? = parentFragment
        if (parentFragment is DialogInterface.OnDismissListener) {
            Log.d("TAG", "onDismiss: 사전확인")

            val edit = ApplicationClass.sSharedPreferences.edit()
            edit.putInt(Constants.FILTER_CHECH_NUM,checkNum)
            edit.apply()

            (parentFragment as DialogInterface.OnDismissListener?)!!.onDismiss(dialog)
        }
    }

    override fun onPause() {
        super.onPause()
        Log.d("TAG", "필터내부 onPause: 테스트")



    }


}

