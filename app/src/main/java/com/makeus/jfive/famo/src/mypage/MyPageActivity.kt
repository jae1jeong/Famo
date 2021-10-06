package com.makeus.jfive.famo.src.mypage

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.net.Uri
import android.os.*
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import com.google.gson.Gson
import com.makeus.jfive.famo.R
import com.makeus.jfive.famo.config.ApplicationClass
import com.makeus.jfive.famo.config.BaseActivity
import com.makeus.jfive.famo.databinding.ActivityMyPageBinding
import com.makeus.jfive.famo.src.mypage.models.MonthsAchievementsResponse
import com.makeus.jfive.famo.src.mypage.models.MyPageResponse
import com.makeus.jfive.famo.src.mypage.models.RestScheduleCountResponse
import com.makeus.jfive.famo.src.mypage.models.TotalScheduleCountResponse
import com.makeus.jfive.famo.src.mypageedit.MyPageEditActivity
import com.makeus.jfive.famo.util.Constants

class MyPageActivity : BaseActivity<ActivityMyPageBinding>(ActivityMyPageBinding::inflate),
    MyPageView {

    //갤러리/카메라 판별변수
    var check = 100
    var galleryUrl: Uri? = null
    var cameraImg: Bitmap? = null
    val monthsAchievementsMap: Map<String, Int> = mapOf()

    val temList10: ArrayList<String> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val day = intent.getStringExtra("day")
        val goalTitle = intent.getStringExtra("goalTitle")
        check = intent.getIntExtra("check", 100)

        //전체일정
        MyPageService(this).tryGetTotalScheduleCount()
        //내정보조회
        showLoadingDialog(this)
        MyPageService(this).tryGetMyPage()
        //월별달성
//        showLoadingDialog(this)
        MyPageService(this).tryGetMonthsAchievement()

        //이미지 앞으로 내보내기
        binding.myPageSetting.bringToFront()

        //프로필 편집으로 이동
        binding.myPageImg.setOnClickListener {
            val intent = Intent(this, MyPageEditActivity::class.java)
            intent.putExtra("check", check)
            startActivity(intent)
            finish()
        }

        //뒤로가기
        binding.myPageBack.setOnClickListener {
            goBack()
        }

    }

    // 뒤로가기
    fun goBack() {
        finish()
    }

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onGetMyPageSuccess(response: MyPageResponse) {

        when (response.code) {
            100 -> {
                Log.d("TAG", "onGetMyPageSuccess: MyPage조회성공")

                val kakaoImg: String? = ApplicationClass.sSharedPreferences.getString(
                    Constants.KAKAO_THUMBNAILIMAGEURL, null
                )
                val day = ApplicationClass.sSharedPreferences.getString(
                    Constants.DAY,
                    null
                )
                val name = ApplicationClass.sSharedPreferences.getString(
                    Constants.USER_NICKNAME,
                    null
                )
                val goalTitle = ApplicationClass.sSharedPreferences.getString(
                    Constants.GOALTITLE,
                    null
                )
                val dDayCheck = ApplicationClass.sSharedPreferences.getString(
                    Constants.DDAY_CHECK,
                    null
                )
                val comments = ApplicationClass.sSharedPreferences.getString(
                    Constants.COMMENTS, null
                )
//                val gallery =
//                    ApplicationClass.sSharedPreferences.getString(Constants.PROFILE_GALLERY, null)
//
//                val camera =
//                    ApplicationClass.sSharedPreferences.getString(Constants.PROFILE_KAMERA, null)
//
//                if (gallery != null) {
//                    galleryUrl = gallery.toUri()
//                }
//
//                if (camera != null) {
//                    cameraImg = stringToBitmap(camera)
//                }


//                val kakaoName:String? = ApplicationClass.sSharedPreferences.getString(Constants.KAKAO_USER_NICKNAME,null)
//                val famoName = ApplicationClass.sSharedPreferences.getString(Constants.USER_NICKNAME,null)

                Log.d("TAG", "사전 확인 profileImageURL: ${response.profileImageURL}")
                if (response.loginMethod == "K") {

                    if (response.profileImageURL == "null") {
                        //카톡프사가 없을때 기본이미지 적용, 있으면 있는거 적용
                        Log.d("TAG", "profileImageURL: ㅇㅇㅇ")
                        Glide.with(this).load(kakaoImg)
                            .error(R.drawable.my_page_img2)
                            .centerCrop().into(binding.myPageImg)
                    } else {
                        //카톡프사가 없을때 기본이미지 적용, 있으면 있는거 적용
                        Glide.with(this).load(response.profileImageURL)
                            .error(R.drawable.my_page_img2)
                            .centerCrop().into(binding.myPageImg)
                    }

                }

                //페모로그인일경우
                if (response.loginMethod == "F") {

                    Log.d("TAG", "myPage: ㅇㅇ")
                    Log.d("TAG", "myPage: ${response.profileImageURL}")

                    Glide.with(this).load(response.profileImageURL)
                        .error(R.drawable.my_page_img2)
                        .centerCrop().into(binding.myPageImg)

                }

                //이름 적용
                binding.myPageTvName.text = name

                //디데이가 설정되어있지 않으면 기본문구
                if (response.goalStatus == -1 || response.Dday >= 0) {
                    binding.myPageTvComments.text = response.titleComment
                }

                if (response.goalStatus == 1 && response.Dday < 0) {
                    binding.myPageTvComments.text = response.goalTitle + "까지 D" + response.Dday + "일\n남았어요!"
                }

            }
            else -> {
                Log.d("TAG", "onGetMyPageSuccess: ${response.message.toString()}")
            }
        }
        dismissLoadingDialog()
    }

    override fun onGetMyPageFail(message: String) {
        dismissLoadingDialog()
    }

    override fun onGetRestScheduleCountSuccess(response: RestScheduleCountResponse) {

        if (response.isSuccess && response.code == 100) {
            val responseJsonArray = response.data.asJsonArray
            responseJsonArray.forEach {
                val jsonObject = it.asJsonObject
                binding.myPageTextRestScheduleCount.text =
                    jsonObject.get("remainScheduleCount").asString
            }
        } else {
            Log.d("MyPageFragment", "onGetRestScheduleCountSuccess: ${response.message}")
        }

    }

    override fun onGetRestScheduleCountFailure(message: String) {
    }


    override fun onGetTotalScheduleCountSuccess(response: TotalScheduleCountResponse) {
        if (response.isSuccess && response.code == 100) {
            Log.d("MyPageFragment", "전체 일정/해낸수 조회성공")
            binding.myPageTextAllScheduleCount.text =
                response.totaldata[0].totalScheduleCount.toString()
            binding.myPageTextDoneScheduleCount.text =
                response.totaldonedata[0].doneScheduleCount.toString()

            binding.myPageTextRestScheduleCount.text = (response.totaldata[0].totalScheduleCount -
                    response.totaldonedata[0].doneScheduleCount).toString()
        } else {
            Log.d("MyPageFragment", "onGetTotalScheduleCountSuccess: ${response.message}")
        }
    }

    override fun onGetTotalScheduleCountFailure(message: String) {
    }

    override fun onGetMonthsAchievmentsSuccess(response: MonthsAchievementsResponse) {

        when (response.code) {
            100 -> {
                val achievement = response.data.asJsonObject
                val hashMap: Map<String, Int> = Gson().fromJson(
                    achievement.toString(), Map::class.java
                ) as Map<String, Int>
                Log.d("TAG", "onGetMonthsAchievmentsSuccess: $hashMap")


//                val scheduleId = memoJsonObject.get("scheduleID").asInt
                val month = hashMap.get("")

                val list = hashMap.toString()
                val temList = list.substring(1, list.length - 1)
                val temList2 = temList.split(",".toRegex()).toTypedArray()
                //월
                val temList3: ArrayList<String> = ArrayList()
                //달성률
                val temList4: ArrayList<String> = ArrayList()

                for (i in temList2.indices) {
                    temList3.add(temList2[i].split("=".toRegex()).toTypedArray()[0])
                    temList4.add(temList2[i].split("=".toRegex()).toTypedArray()[1])
                }

                for (i in 0 until temList3.size) {
                    temList10.add(temList3[i].split("-")[1])
                }

                //그래프 시작

                val lineChart = findViewById<View>(R.id.chart) as LineChart

                val test = 1

//                for (i in test.size() - 1 downTo 0) {
//                    System.out.println(test.get(i))
//                }


                //그래프 마커좌표
                val entries: ArrayList<Entry> = ArrayList()
//                for (i in temList4.size - 1 downTo 0) {
//                    entries.add(Entry((i + 1).toFloat(), temList4[i].toFloat()))
//                    Log.d("TAG", "값 : ${(i + 1)} : ${temList4[i]}")
//                }
                for (i in 0 until temList10.size) {
                    entries.add(Entry((i + 1).toFloat(), temList4[i].toFloat()))
                }

                //라벨
                val lineDataSet = LineDataSet(entries, "속성명1")
                lineDataSet.lineWidth = 1f
                lineDataSet.circleRadius = 6f
                lineDataSet.setCircleColor(Color.parseColor("#ffae2a"))
                lineDataSet.color = Color.parseColor("#ffae2a")
                lineDataSet.setDrawCircleHole(true)
                lineDataSet.setDrawCircles(true)
                lineDataSet.setDrawHorizontalHighlightIndicator(false)
                lineDataSet.setDrawHighlightIndicators(false)
                //값보여주기
                lineDataSet.setDrawValues(false)


                val lineData = LineData(lineDataSet)
                lineChart.data = lineData

                val xAxis = lineChart.xAxis
                xAxis.textColor = Color.BLACK
                xAxis.enableGridDashedLine(8f, 24f, 0f)

                val yLAxis = lineChart.axisLeft
                yLAxis.textColor = Color.BLACK
//        yLAxis.setDrawAxisLine(false)
//        yLAxis.setDrawLabels(false)
//        yLAxis.setDrawGridLines(false)
//        yLAxis.setDrawLabels(false)
//        yLAxis.setDrawAxisLine(false)
//        yLAxis.setDrawGridLines(false

                //true로 지정시 오른쪽 y좌표도 생김
                val yRAxis = lineChart.axisRight
                yRAxis.setDrawLabels(false)
                yRAxis.setDrawAxisLine(false)
                yRAxis.setDrawGridLines(false)


                //우측하단 description label 제거
//        val des: Description = lineChart.description
//        des.isEnabled = false
//        des.setText("");
                val description = Description()
                description.text = "";
                lineChart.description = description

                lineChart.isDoubleTapToZoomEnabled = false
                lineChart.setDrawGridBackground(true)
//        lineChart.animateY(2000, Easing.EaseInCubic)

                //그리디언트색상
                lineDataSet.setDrawFilled(true)
                val fillGradient =
                    ContextCompat.getDrawable(this, R.drawable.my_page_graph_gradient)
                lineDataSet.fillDrawable = fillGradient

                //setting
                lineChart.run {
                    description.isEnabled =
                        true //차트 옆에 별도로 표기되는 description이다. false로 설정하여 안보이게 했다.
//            setMaxVisibleValueCount(4) // 최대 보이는 그래프 개수를 7개로 정해주었다.
                    setPinchZoom(false) // 핀치줌(두손가락으로 줌인 줌 아웃하는것) 설정
                    setDrawGridBackground(false)//격자구조 넣을건지
                    invalidate()

                    axisLeft.run { //왼쪽 축. 즉 Y방향 축을 뜻한다.
                        axisMaximum = 101f //100 위치에 선을 그리기 위해 101f로 맥시멈을 정해주었다
                        axisMinimum = 0f // 최소값 0
                        granularity = 20f // 50 단위마다 선을 그리려고 granularity 설정 해 주었다.

                        //위 설정이 20f였다면 총 5개의 선이 그려졌을 것
                        setDrawLabels(true) // 값 적는거 허용 (0, 50, 100)
                        setDrawGridLines(false) //격자 라인 활용
                        setDrawAxisLine(true) // 축 그리기 설정

                        axisLineColor =
                            ContextCompat.getColor(context, R.color.graph_color) // 축 색깔 설정
                        gridColor = ContextCompat.getColor(context, R.color.black) // 축 아닌 격자 색깔 설정
//                textColor = ContextCompat.getColor(context,R.color.colorSemi50Black) // 라벨 텍스트 컬러 설정
                        textSize = 11f //라벨 텍스트 크기
                    }
                    xAxis.run {
                        position = XAxis.XAxisPosition.BOTTOM//X축을 아래에다가 둔다.
                        axisMaximum =
                            (temList10.size.toFloat()) //100 위치에 선을 그리기 위해 101f로 맥시멈을 정해주었다
                        axisMinimum = 0f // 최소값 0
//                        axisMinimum = 0F // 최소값 0
                        granularity = 1f // 50 단위마다 선을 그리려고 granularity 설정 해 주었다.
                        setDrawAxisLine(true) // 축 그림
                        setDrawGridLines(false) // 격자
                        setDrawLabels(true) // 값 적는거 허용 (0, 50, 100)

                        gridColor = ContextCompat.getColor(context, R.color.black)
                        axisLineColor = ContextCompat.getColor(context, R.color.graph_color)

//                textColor = ContextCompat.getColor(context,R.color.purple_700) //라벨 색상
                        valueFormatter = MyXAxisFormatter() // 축 라벨 값 바꿔주기 위함
                        paddingTop.plus(10f)

                        textSize = 11f // 텍스트 크기
                    }
                    axisRight.isEnabled = false // 오른쪽 Y축을 안보이게 해줌.
                    setTouchEnabled(true) // 그래프 터치해도 아무 변화없게 막음
                    animateY(1000) // 밑에서부터 올라오는 애니매이션 적용
                    legend.isEnabled = false //차트 범례 설정

                }

                //마커 클릭 시 값보이게
                val marker = MyMarkerView(this, R.layout.markerviewtext)
                marker.chartView = lineChart
                lineChart.marker = marker
            }
            else -> {

            }
        }
//        dismissLoadingDialog()
    }

    override fun onGetMonthsAchievmentsFailure(message: String) {
//        dismissLoadingDialog()
    }

    //x축 값 설정
    inner class MyXAxisFormatter : ValueFormatter() {

        override fun getAxisLabel(value: Float, axis: AxisBase?): String {

            val test = ArrayList<String>()

//            for (i in temList10.size - 1 downTo 0) {
//                test.add(temList10[i] + "월")
//            }

            for (i in 0 until temList10.size) {
                test.add(temList10[i] + "월")
            }
            return test.getOrNull(value.toInt() - 1) ?: value.toString()

        }


    }
}