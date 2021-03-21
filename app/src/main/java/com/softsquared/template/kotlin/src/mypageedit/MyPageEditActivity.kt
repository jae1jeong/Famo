package com.softsquared.template.kotlin.src.mypageedit

import android.annotation.SuppressLint
import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.DatePicker
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import com.bumptech.glide.Glide
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import com.softsquared.template.kotlin.R
import com.softsquared.template.kotlin.config.ApplicationClass
import com.softsquared.template.kotlin.config.BaseActivity
import com.softsquared.template.kotlin.config.BaseResponse
import com.softsquared.template.kotlin.databinding.ActivityMyPageBinding
import com.softsquared.template.kotlin.databinding.ActivityMyPageEditBinding
import com.softsquared.template.kotlin.src.main.mypage.MyPageActivity
import com.softsquared.template.kotlin.src.main.mypage.MyPageActivityView
import com.softsquared.template.kotlin.src.main.mypage.MyPageFragment
import com.softsquared.template.kotlin.src.main.mypage.edit.LogoutDialog
import com.softsquared.template.kotlin.src.main.mypage.edit.MyPageEditFragment
import com.softsquared.template.kotlin.src.main.mypage.edit.MyPageEditService
import com.softsquared.template.kotlin.src.main.mypage.edit.MyPageEditView
import com.softsquared.template.kotlin.src.main.mypage.models.MyPageCommentsResponse
import com.softsquared.template.kotlin.src.main.mypage.models.MyPageResponse
import com.softsquared.template.kotlin.src.main.mypage.models.PutMyPageUpdateRequest
import com.softsquared.template.kotlin.util.Constants
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*

class MyPageEditActivity : BaseActivity<ActivityMyPageEditBinding>
    (ActivityMyPageEditBinding::inflate), MyPageEditView {

    //카메라 변수
    private val GET_GALLERY_IMAGE = 200
    val REQUEST_IMAGE_CAPTURE = 1
    lateinit var currentPhotoPath: String

    //클릭에 따른 visible/gone을 위한 변수
    var topMentCnt = 1
    var dDaySettingCnt = 1
    var accountSettingCnt = 1

    // 날짜
    var strDate = ""
    // 날짜를 선택유무에 대한 변수
    var dateCnt = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        MyPageEditService(this).tryGetMyPage()

        //이미지 클릭 시
        binding.myPageEditImg.setOnClickListener {
            //카메라 권한 설정
            settingPermission()

            // 갤러리/카메라 알림창
            val builder = AlertDialog.Builder(this)
            builder.setTitle("사진 찍기")
                .setMessage("사진을 새로 찍으시거나 사진\n라이브러리에서 선택하세요.")
                .setPositiveButton("카메라",
                    DialogInterface.OnClickListener { dialog, id ->
                        //카메라 시작
                        startCapture()

                    })
                .setNegativeButton("갤러리",
                    DialogInterface.OnClickListener { dialog, id ->

                        val intent = Intent(Intent.ACTION_PICK)
                        intent.setDataAndType(
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                            "image/*"
                        )
                        startActivityForResult(intent, GET_GALLERY_IMAGE)
                    })

            val alertDialog = builder.create()

            //다이얼로그 색상
            alertDialog.setOnShowListener {
                alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.BLUE)
                alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.BLUE)
            }
//            alertDialog.window!!.setBackgroundDrawable()

            alertDialog.show()
        }

        //상단 멘트설정 화살표 클릭 시 밑에 내용 나오게
        binding.myPageEditBtnTopMent.setOnClickListener {
            if (topMentCnt % 2 != 0){
                binding.myPageEditLinearTopMent.visibility = View.VISIBLE
                binding.myPageEditTopMentView.visibility = View.GONE
            }

            if (topMentCnt % 2 == 0){
                binding.myPageEditLinearTopMent.visibility = View.GONE
                binding.myPageEditTopMentView.visibility = View.VISIBLE
            }
            topMentCnt++
//            binding.myPageEditLinearTopMent.layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
        }

        //디데이 설정 버튼 클릭 시
        //버튼 색상 및 화살표 하단내용 나오게
        binding.myPageEditBtnDday.setOnClickListener {
            if (dDaySettingCnt % 2 != 0){
//                binding.myPageEditBtnDday.setColorFilter(Color.parseColor("#FFAE2A"))
                binding.myPageEditBtnDdayCheck.visibility = View.VISIBLE
                binding.myPageEditBtnDdayCheck2.visibility = View.VISIBLE
                binding.myPageEditLinearDdaySetting.visibility = View.VISIBLE
            }

            if (dDaySettingCnt % 2 == 0){
//                binding.myPageEditBtnDday.setColorFilter(Color.parseColor("#FFFFFF"))
                binding.myPageEditBtnDdayCheck.visibility = View.GONE
                binding.myPageEditBtnDdayCheck2.visibility = View.GONE
                binding.myPageEditLinearDdaySetting.visibility = View.GONE
            }

            dDaySettingCnt++
        }

        //계정 설정 화살표 클릭 시
        //하단 내용 나오게
        binding.myPageEditBtnAccountSetting.setOnClickListener {
            if (accountSettingCnt % 2 != 0){
                binding.myPageEditBtnAccountSettingView.visibility = View.GONE
                binding.myPageEditLinearAccountSetting.visibility = View.VISIBLE
            }

            if (accountSettingCnt % 2 == 0){
                binding.myPageEditBtnAccountSettingView.visibility = View.VISIBLE
                binding.myPageEditLinearAccountSetting.visibility = View.GONE
            }

            accountSettingCnt++
        }

        //날짜 변화에 대한
        val listener = DatePicker.OnDateChangedListener { view, year, monthOfYear, dayOfMonth ->

            if (monthOfYear + 1 < 10 && dayOfMonth < 10) {
                strDate = year.toString() + "-" + "0" + (monthOfYear + 1) + "-" + "0" + dayOfMonth
            } else if (monthOfYear + 1 >= 10 && dayOfMonth < 10) {
                strDate = year.toString() + "-" + (monthOfYear + 1) + "-" + "0" + dayOfMonth
            } else if (monthOfYear + 1 < 10 && dayOfMonth >= 10) {
                strDate = year.toString() + "-" + "0" + (monthOfYear + 1) + "-" + dayOfMonth
            } else {
                strDate = year.toString() + "-" + (monthOfYear + 1) + "-" + dayOfMonth
            }

            Toast.makeText(this, strDate, Toast.LENGTH_SHORT).show()
            dateCnt++
        }

        val month: Int = binding.dataPicker.month
        val year = binding.dataPicker.year
        val day = binding.dataPicker.dayOfMonth
        binding.dataPicker.init(year, month, day, listener)


        //체크표시
        binding.myPageEditBtnSave.setOnClickListener {
            val onlyDate: LocalDate = LocalDate.now()

            if (dateCnt == 0){
//                binding.myPageEditEtComments.setText(onlyDate.toString())
                val str = onlyDate.toString()
                val format = SimpleDateFormat("YYYY-MM-DD")
                val nowDate : Date? = format.parse(str)
                strDate = str
            }else{
//                binding.myPageEditEtComments.setText(strDate)

            }

            if (dDaySettingCnt % 2 != 0){
                dDaySettingCnt = 1
            }else{
                dDaySettingCnt = -1
            }

            Log.d("TAG", "상단멘트사전확인: ${binding.myPageEditEtComments.text}")
            val myPageUpdateRequest = PutMyPageUpdateRequest(
                nickname = binding.myPageEditEtName.text.toString(),
                titleComment = binding.myPageEditEtComments.text.toString(),
                goalStatus = dDaySettingCnt,
                goalTitle = binding.myPageEditEtGoaltitle.text.toString(),
                goalDate = strDate
            )

            MyPageEditService(this).tryPutMyPageUpdate(myPageUpdateRequest)
        }

        //로그아웃
        binding.mypageEditBtnLogout.setOnClickListener {
            logoutDialog()
        }

        //X버튼 클릭 시 내정보로 이동
        binding.myPageEditBack.setOnClickListener {
            finish()
        }


    }

    //로그아웃 알림창
    fun logoutDialog() {
        val dialog = LogoutDialog(this)
        dialog.show()
    }

    //권한 설정
    fun settingPermission() {
        val permis = object : PermissionListener {
            //            어떠한 형식을 상속받는 익명 클래스의 객체를 생성하기 위해 다음과 같이 작성
            override fun onPermissionGranted() {
                showCustomToast("권한 허가")
            }

            override fun onPermissionDenied(deniedPermissions: MutableList<String>?) {
                showCustomToast("권한 거부")
                ActivityCompat.finishAffinity(this@MyPageEditActivity) // 권한 거부시 앱 종료
            }
        }

        TedPermission.with(this)
            .setPermissionListener(permis)
            .setRationaleMessage("카메라 사진 권한 필요")
            .setDeniedMessage("카메라 권한 요청 거부")
            .setPermissions(
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                android.Manifest.permission.READ_EXTERNAL_STORAGE,
                android.Manifest.permission.CAMERA
            )
            .check()
    }

    //카메라 시작
    @SuppressLint("QueryPermissionsNeeded")
    fun startCapture() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(this!!.packageManager)?.also {
                val photoFile: File? = try {
                    createImageFile()
                } catch (ex: IOException) {
                    null
                }
                photoFile?.also {
                    val photoURI: Uri = FileProvider.getUriForFile(
                        this!!,
                        "com.softsquared.template.kotlin.fileprovider",
                        it
                    )
                    //정보를 onActivityResult 함수로 전달
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
                }
            }
        }
    }

    //이미지 생성
    @SuppressLint("SimpleDateFormat")
    @Throws(IOException::class)
    fun createImageFile(): File {
        //사진이 생성되면 이름이 겹치지 않게 고유해야하므로 날짜로 만든다
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File? = this!!.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${timeStamp}_",
            ".jpg",
            storageDir
        ).apply {
            currentPhotoPath = absolutePath
        }
    }

    //갤러리/카메라 실행
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        //갤러리
        if (requestCode == GET_GALLERY_IMAGE && resultCode == AppCompatActivity.RESULT_OK && data != null && data.data != null) {
            showCustomToast("갤러리")
            val selectedImageUri: Uri? = data.data
            binding.myPageEditImg.setImageURI(selectedImageUri)
        }

        //카메라
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            showCustomToast("카메라")
            val file = File(currentPhotoPath)
            if (Build.VERSION.SDK_INT < 28) {
                val bitmap = MediaStore.Images.Media
                    .getBitmap(this.contentResolver, Uri.fromFile(file))
                binding.myPageEditImg.setImageBitmap(bitmap)
            } else {
                val decode = ImageDecoder.createSource(
                    this.contentResolver,
                    Uri.fromFile(file)
                )
                val bitmap = ImageDecoder.decodeBitmap(decode)
                binding.myPageEditImg.setImageBitmap(bitmap)
            }
        }
    }

    override fun onGetMyPageCommentsSuccess(response: MyPageCommentsResponse) {
    }

    override fun onGetMyPageCommentsFail(message: String) {
    }

    override fun onGetMyPageSuccess(response: MyPageResponse) {

        when(response.code){
            100 -> {
                Log.d("TAG", "onGetMyPageSuccess: MyPage수정 조회성공")
                showCustomToast("MyPage수정 조회성공")

//                val kakaoName: String? = ApplicationClass.sSharedPreferences.getString(
//                    Constants.KAKAO_USER_NICKNAME,
//                    null
//                )
//                val famoName = ApplicationClass.sSharedPreferences.getString(
//                    Constants.USER_NICKNAME,
//                    null
//                )
                val kakaoImg: String? = ApplicationClass.sSharedPreferences.getString(
                    Constants.KAKAO_THUMBNAILIMAGEURL,
                    null
                )
                val name =
                    ApplicationClass.sSharedPreferences.getString(Constants.USER_NICKNAME, null)

                Log.d("TAG", "onGetMyPageSuccess: kakaoImg :$kakaoImg")
                if (response.loginMethod == "K") {

                    if (kakaoImg!!.isNotEmpty()) {
                        Log.d("TAG", "onGetMyPageSuccess: 카로확인")
                        Glide.with(this).load(kakaoImg)
                            .centerCrop().into(binding.myPageEditImg)
                    } else {
                        Log.d("TAG", "onGetMyPageSuccess: 카로확인2")
                        Glide.with(this).load(R.drawable.my_page_img2)
                            .centerCrop().into(binding.myPageEditImg)
                    }
                } else {
                    Glide.with(this).load(R.drawable.my_page_img2)
                        .centerCrop().into(binding.myPageEditImg)
                }

                //이름 적용
                binding.myPageEditEtName.setText(name)

            }
            else -> {
                Log.d("TAG", "onGetMyPageSuccess: ${response.message.toString()}")
                showCustomToast("${response.message.toString()}}")
            }
        }

    }

    override fun onGetMyPageFail(message: String) {
    }

    override fun onPutMyPageUpdateSuccess(response: BaseResponse) {

        when(response.code){
            100 -> {
                Log.d("TAG", "onPutMyPageUpdateSuccess: MyPage수정성공")
                showCustomToast("수정성공")

                val sampleDate = strDate
                val sf = SimpleDateFormat("yyyy-MM-dd")
                val date = sf.parse(sampleDate)
                val today = Calendar.getInstance()
                val day = ((date.time - today.time.time) / (60 * 60 * 24 * 1000)) + 1

                binding.myPageEditDay.setText(day.toString())
                val name = binding.myPageEditEtName.text.toString()
                val comments = binding.myPageEditEtComments.text.toString()
                val goalTitle = binding.myPageEditEtGoaltitle.text.toString()

                val edit = ApplicationClass.sSharedPreferences.edit()
                edit.putString(Constants.USER_NICKNAME, name)
                edit.putString(Constants.COMMENTS, comments)
                edit.putString(Constants.DAY, day.toString())
                edit.putString(Constants.GOALTITLE, goalTitle)
                edit.putString(Constants.DDAY_SETTING, dDaySettingCnt.toString())
                edit.apply()

//                val intent = Intent(activity, MyPageActivity::class.java)
                intent.putExtra("day", day)
                intent.putExtra("goalTitle", binding.myPageEditEtGoaltitle.toString())
                startActivity(intent)
//                myPageActivityView.moveMyPage()
//                nickname = binding.myPageEditEtName.text.toString(),
//                titleComment = binding.myPageEditEtComments.text.toString(),
//                goalStatus = dDaySettingCnt,
//                goalTitle = binding.myPageEditEtGoaltitle.text.toString(),
//                goalDate = goalDate!!


            }
            else -> {
                Log.d("TAG", "onPutMyPageUpdateSuccess: ${response.message.toString()}")
                showCustomToast("${response.message.toString()}}")
            }
        }

    }

    override fun onPutMyPageUpdateFail(message: String) {
    }
}