package com.softsquared.template.kotlin.src.main.schedulefind

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.softsquared.template.kotlin.R
import com.softsquared.template.kotlin.config.ApplicationClass
import com.softsquared.template.kotlin.config.BaseFragment
import com.softsquared.template.kotlin.databinding.FragmentScheduleFindDetailBinding
import com.softsquared.template.kotlin.src.main.MainActivity
import com.softsquared.template.kotlin.src.main.schedulefind.adapter.ScheduleBookmarkAdapter
import com.softsquared.template.kotlin.src.main.schedulefind.adapter.ScheduleWholeAdapter
import com.softsquared.template.kotlin.src.main.schedulefind.models.ScheduleBookmarkData
import com.softsquared.template.kotlin.src.main.schedulefind.models.ScheduleWholeData
import java.util.ArrayList

class ScheduleFindDetailFragment : BaseFragment<FragmentScheduleFindDetailBinding>(
    FragmentScheduleFindDetailBinding::bind, R.layout.fragment_schedule_find_detail
) {

    companion object {
        fun newInstance(): ScheduleFindDetailFragment {    // shs: 함수의 반환 형이 Fragment 형이라...
            return ScheduleFindDetailFragment()
        }
    }

    private var adapter: ScheduleWholeAdapter? = null
    private val items = arrayListOf<ScheduleWholeData>()

    private var isLoading = false

    val boolean: Boolean? = null

    @SuppressLint("CommitPrefEdits")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val boolean = ApplicationClass.sSharedPreferences.getBoolean("boolean", true)

//        val bundle = arguments //번들 받기. getArguments() 메소드로 받음.
//        if (bundle != null) {
//            val boolean = bundle.getBoolean("boolean") //Name 받기.
//            Log.d(TAG, "ScheduleFindDetailFragment: boolean : $boolean")
//        }

        populateData()
        initAdapter()
//        initScrollListener()

        Log.d(TAG, "ScheduleFindDetailFragment: boolean null일경우 값 확인 : $boolean")
        //일정 찾기 탭에서 즐겨찾기가 선택되있는 경우
        if (boolean == true) {
            //즐겨찾기가 선택됬을 때 글자 색상 등 설정
//            setUpBookmark()
            //즐겨찾기 리사이클러뷰
//            createBookmarkRecyclerview()
//            val test = ScheduleFindFragment()
//            test.createWholeScheduleRecyclerview()
            //최근 일 경우
        } else {
            //최근이 선택됬을 때 글자 색상 등 설정
//            setUpLately()
            //최근 리사이클러뷰
//            createLatelyRecyclerview()
        }

        //즐겨찾기 클릭 시
        binding.scheduleFindDetailLinearBookmark.setOnClickListener {
            setUpBookmark()
            binding.recyclerviewWholeBookmark.visibility = View.VISIBLE
            binding.recyclerviewWholeLately.visibility = View.GONE
            createBookmarkRecyclerview()

        }

        //최근 클릭 시
        binding.scheduleFindDetailLinearLately.setOnClickListener {
            setUpLately()
            binding.recyclerviewWholeLately.visibility = View.VISIBLE
            binding.recyclerviewWholeBookmark.visibility = View.GONE
            createLatelyRecyclerview()
        }

        //X버튼 클릭 시
        binding.scheduleFindDetailXBtn.setOnClickListener {
//            (activity as MainActivity).replaceFragment(ScheduleFindFragment.newInstance());
            val intent = Intent(activity, MainActivity::class.java)
            startActivity(intent)
        }

        ApplicationClass.sSharedPreferences.edit().remove("boolean")
        ApplicationClass.sSharedPreferences.edit().apply()
    }

    private fun populateData() {
        for (i in 0..12) {
//            items.add(ScheduleBookmarkData("$i","$i"))

//            items.add(ScheduleWholeData("2021.02.10","$i","$i+1",R.drawable.schedule_find_bookmark))
        }
    }

    private fun initAdapter() {
        adapter = ScheduleWholeAdapter(items)
        val layoutManager = GridLayoutManager(
            context, 2, GridLayoutManager.VERTICAL,
            false
        )
        binding.recyclerviewWholeBookmark.adapter = adapter
        binding.recyclerviewWholeBookmark.layoutManager = layoutManager
    }

//    private fun initScrollListener() {
//        binding.recyclerviewWholeBookmark.setOnScrollListener(object :
//            RecyclerView.OnScrollListener() {
//            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
//                super.onScrollStateChanged(recyclerView, newState)
//            }
//
//            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
//                super.onScrolled(recyclerView, dx, dy)
//                val layoutManager = recyclerView.layoutManager as GridLayoutManager?
//                if (!isLoading) {
//                    if (layoutManager != null && layoutManager.findLastCompletelyVisibleItemPosition() == items.size - 1) {
//                        //리스트 마지막
//                        loadMore()
//                        isLoading = true
//                    }
//                }
//            }
//        })
//    }

//    private fun loadMore() {
////        items.add(ScheduleBookmarkData("cc", "cc"))
//        items.add(ScheduleWholeData("2021.02.10","멈추기","멈추기",R.drawable.schedule_find_bookmark))
//        adapter!!.notifyItemInserted(items.size - 1)
//        val handler = Handler()
//        handler.postDelayed({
//            items.removeAt(items.size - 1)
//            val scrollPosition = items.size
//            adapter!!.notifyItemRemoved(scrollPosition)
//            var currentSize = scrollPosition
//            val nextLimit = currentSize + 10
//            while (currentSize - 1 < nextLimit) {
////                items.add(ScheduleBookmarkData("0", "1"))
//                items.add(ScheduleWholeData("2021.02.10","??","??",R.drawable.schedule_find_bookmark))
//                currentSize++
//            }
//            adapter!!.notifyDataSetChanged()
//            isLoading = false
//        }, 2000)
//    }

    private fun setUpLately() {
        binding.scheduleFindDetailTvLately.setTextColor(Color.BLACK)
        binding.scheduleFindDetailTvLately.setTypeface(null, Typeface.BOLD)
        binding.scheduleFindDetailViewLately.setBackgroundColor(Color.BLACK)
        binding.scheduleFindDetailViewLately.layoutParams.height = 4

        binding.scheduleFindDetailTvBookmark.setTextColor(Color.GRAY)
        binding.scheduleFindDetailTvBookmark.setTypeface(null, Typeface.NORMAL)
        binding.scheduleFindDetailViewBookmark.setBackgroundColor(Color.parseColor("#E1DDDD"))
        binding.scheduleFindDetailViewBookmark.layoutParams.height = 2
    }

    private fun setUpBookmark() {
        binding.scheduleFindDetailTvBookmark.setTextColor(Color.BLACK)
        binding.scheduleFindDetailTvBookmark.setTypeface(null, Typeface.BOLD)
        binding.scheduleFindDetailViewBookmark.setBackgroundColor(Color.BLACK)
        binding.scheduleFindDetailViewBookmark.layoutParams.height = 4

        binding.scheduleFindDetailTvLately.setTextColor(Color.GRAY)
        binding.scheduleFindDetailTvLately.setTypeface(null, Typeface.NORMAL)
        binding.scheduleFindDetailViewLately.setBackgroundColor(Color.parseColor("#E1DDDD"))
        binding.scheduleFindDetailViewLately.layoutParams.height = 2
    }

    private fun createBookmarkRecyclerview() {
        binding.recyclerviewWholeBookmark.visibility = View.VISIBLE
        binding.recyclerviewWholeLately.visibility = View.GONE

        //테스트 데이터
//        val wholeList = arrayListOf(
//            ScheduleWholeData(
//                "2021.02.10", "제목", "내용",
//                R.drawable.schedule_find_bookmark
//            ),
//            ScheduleWholeData(
//                "2021.02.10", "제목2", "내용2",
//                R.drawable.schedule_find_bookmark
//            ),
//            ScheduleWholeData(
//                "2021.02.10", "제목3", "내용3",
//                R.drawable.schedule_find_bookmark
//            ),
//            ScheduleWholeData(
//                "2021.02.10", "제목4", "내용4",
//                R.drawable.schedule_find_bookmark
//            )
//        )
//        //전체일정 리사이큘러뷰 연결
//        binding.recyclerviewWholeBookmark.layoutManager =
//            GridLayoutManager(
//                context, 2, GridLayoutManager.VERTICAL,
//                false
//            )
//        binding.recyclerviewWholeBookmark.setHasFixedSize(true)
//        binding.recyclerviewWholeBookmark.adapter = ScheduleWholeAdapter(wholeList)
    }

    private fun createLatelyRecyclerview() {

        binding.recyclerviewWholeLately.visibility = View.VISIBLE
        binding.recyclerviewWholeBookmark.visibility = View.GONE
        //테스트 데이터
        val latelyList = arrayListOf(
            ScheduleBookmarkData("최근제목", "최근시간"),
            ScheduleBookmarkData("최근제목", "최근시간")
        )

        // 즐겨찾기/최근 일정 리사이클러뷰 연결
        binding.recyclerviewWholeLately.layoutManager = LinearLayoutManager(
            context, LinearLayoutManager.VERTICAL, false
        )
        binding.recyclerviewWholeLately.setHasFixedSize(true)
        binding.recyclerviewWholeLately.adapter = ScheduleBookmarkAdapter(latelyList)
    }

}