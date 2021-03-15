package com.softsquared.template.kotlin.src.main.mypage.edit

import android.annotation.SuppressLint
import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import com.bumptech.glide.Glide
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import com.softsquared.template.kotlin.R
import com.softsquared.template.kotlin.config.ApplicationClass
import com.softsquared.template.kotlin.config.BaseFragment
import com.softsquared.template.kotlin.databinding.FragmentMypageEditBinding
import com.softsquared.template.kotlin.src.main.mypage.MyPageActivityView
import com.softsquared.template.kotlin.src.main.mypage.models.MyPageEditResponse
import com.softsquared.template.kotlin.util.Constants
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class MyPageEditFragment(val myPageActivityView: MyPageActivityView) : BaseFragment<FragmentMypageEditBinding>(FragmentMypageEditBinding::bind,
        R.layout.fragment_mypage_edit),MyPageEditView {

    private val GET_GALLERY_IMAGE = 200
    val REQUEST_IMAGE_CAPTURE = 1
    lateinit var currentPhotoPath: String

    var token : String? = null
    var name : String? = null
    var img : String?= null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        MyPageEditService(this).tryGetMyPage()

        var extra = this.arguments
        if (extra != null) {
            extra = arguments
            token = extra?.getString("token")
            name = extra?.getString("name")
            img = extra?.getString("img").toString()
            Log.d("MyPageEditFragment 잘들어 왔나 token", "값: $token")
            Log.d("MyPageEditFragment 잘들어 왔나 name", "값: $name")
            Log.d("MyPageEditFragment 잘들어 왔나 img", "값: $img")
            Glide.with(this).load(img)
                .centerCrop().into(binding.myPageEditImg)
        }

        val name = ApplicationClass.sSharedPreferences.getString(Constants.USER_NICKNAME,null)

        Glide.with(this).load(img)
            .centerCrop().into(binding.myPageEditImg)

        binding.myPageEditTvName.text = name

        //클릭에 따른 visible/gone을 위한 변수
        var topMentCnt = 1
        var dDaySettingCnt = 1
        var accountSettingCnt = 1

        //이미지 클릭 시
        binding.myPageEditImg.setOnClickListener {
            //카메라 권한 설정
            settingPermission()

            // 갤러리/카메라 알림창
            val builder = AlertDialog.Builder(activity!!)
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

        //X버튼 클릭 시 내정보로 이동
        binding.myPageEditBtnX.setOnClickListener {
            myPageActivityView.moveMyPage()
        }

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
                activity?.let { ActivityCompat.finishAffinity(it) } // 권한 거부시 앱 종료
            }
        }

        TedPermission.with(context)
            .setPermissionListener(permis)
            .setRationaleMessage("카메라 사진 권한 필요")
            .setDeniedMessage("카메라 권한 요청 거부")
            .setPermissions(
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                android.Manifest.permission.READ_EXTERNAL_STORAGE,
                android.Manifest.permission.CAMERA)
            .check()
    }

    //카메라 시작
    @SuppressLint("QueryPermissionsNeeded")
    fun startCapture() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(activity!!.packageManager)?.also {
                val photoFile: File? = try {
                    createImageFile()
                } catch (ex: IOException) {
                    null
                }
                photoFile?.also {
                    val photoURI: Uri = FileProvider.getUriForFile(
                        activity!!,
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
        val storageDir: File? = activity!!.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
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
                    .getBitmap(activity!!.contentResolver, Uri.fromFile(file))
                binding.myPageEditImg.setImageBitmap(bitmap)
            } else {
                val decode = ImageDecoder.createSource(activity!!.contentResolver,
                    Uri.fromFile(file))
                val bitmap = ImageDecoder.decodeBitmap(decode)
                binding.myPageEditImg.setImageBitmap(bitmap)
            }
        }
    }

    override fun onGetMyPageEditSuccess(editResponse: MyPageEditResponse) {

        when(editResponse.code){
            100 -> {
                showCustomToast("MyPage조회 성공")
            }else -> {
            showCustomToast("실패 메시지 : ${editResponse.message}")
            Log.d("TAG", "조회실패: ${editResponse.message}")
        }

        }
    }

    override fun onGetMyPageEditFail(message: String) {
    }

}