package com.softsquared.template.kotlin.src.main.mypage

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.*
import androidx.appcompat.app.AppCompatActivity
import android.provider.MediaStore
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import com.softsquared.template.kotlin.R
import com.softsquared.template.kotlin.config.BaseActivity
import com.softsquared.template.kotlin.databinding.ActivityMyPageBinding
import com.softsquared.template.kotlin.databinding.ActivitySplashBinding
import com.softsquared.template.kotlin.src.auth.information.InformationActivity
import com.softsquared.template.kotlin.src.main.MainActivity
import com.softsquared.template.kotlin.src.main.mypage.edit.MyPageEditFragment
import com.softsquared.template.kotlin.src.main.schedulefind.ScheduleFindFragment
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class MyPageActivity : BaseActivity<ActivityMyPageBinding>(ActivityMyPageBinding::inflate),
    MyPageActivityView{

    private val GET_GALLERY_IMAGE = 200
    val REQUEST_IMAGE_CAPTURE = 1
    lateinit var currentPhotoPath: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportFragmentManager.beginTransaction().add(R.id.main_my_page_fragment, MyPageFragment(this))
            .commitAllowingStateLoss()
    }

    override fun moveMyPage() {
        supportFragmentManager.beginTransaction().replace(R.id.main_my_page_fragment, MyPageFragment(this))
                .commit()
    }
//
    override fun moveMyPageEdit() {
        supportFragmentManager.beginTransaction().replace(R.id.main_my_page_fragment, MyPageEditFragment(this))
                .commit()
    }

    override fun moveScheduleFind() {
//        val intent = Intent(this,MainActivity::class.java)
        val main = MainActivity()
        main.moveScheduleFindFragment()
        finish()
//        startActivity(intent)
//        MainActivity.replaceFragment(ScheduleFindFragment.newInstance());

    }

    override fun settingPermission() {
        val permis = object : PermissionListener {
            //            어떠한 형식을 상속받는 익명 클래스의 객체를 생성하기 위해 다음과 같이 작성
            override fun onPermissionGranted() {
                showCustomToast("권한 허가")
            }

            override fun onPermissionDenied(deniedPermissions: MutableList<String>?) {
                showCustomToast("권한 거부")
                ActivityCompat.finishAffinity(this@MyPageActivity) // 권한 거부시 앱 종료
            }
        }

        TedPermission.with(this)
            .setPermissionListener(permis)
            .setRationaleMessage("카메라 사진 권한 필요")
            .setDeniedMessage("카메라 권한 요청 거부")
            .setPermissions(
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                android.Manifest.permission.READ_EXTERNAL_STORAGE,
                android.Manifest.permission.CAMERA)
            .check()
    }

    @SuppressLint("QueryPermissionsNeeded")
    override fun startCapture() {
//        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
//            takePictureIntent.resolveActivity(packageManager)?.also {
//                val photoFile: File? = try {
//                    createImageFile()
//                } catch (ex: IOException) {
//                    null
//                }
//                photoFile?.also {
//                    val photoURI: Uri = FileProvider.getUriForFile(
//                        this,
//                        "com.softsquared.template.kotlin.fileprovider",
//                        it
//                    )
//                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
//                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
//                }
//            }
//        }
    }

    //이미지 생성
    @SuppressLint("SimpleDateFormat")
    @Throws(IOException::class)
    override fun createImageFile() {
//        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
//        val storageDir: File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
//        return File.createTempFile(
//            "JPEG_${timeStamp}_",
//            ".jpg",
//            storageDir
//        ).apply {
//            currentPhotoPath = absolutePath
//        }
    }

    override fun onActivityResult() {
    }

    //카메라/갤러리
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == GET_GALLERY_IMAGE && resultCode == RESULT_OK && data != null && data.data != null) {
            val selectedImageUri: Uri? = data.data
//            binding.profileUpdateImg.setImageURI(selectedImageUri)
        }

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            val file = File(currentPhotoPath)
            if (Build.VERSION.SDK_INT < 28) {
                val bitmap = MediaStore.Images.Media
                    .getBitmap(contentResolver, Uri.fromFile(file))
//                binding.profileUpdateImg.setImageBitmap(bitmap)
            } else {
                val decode = ImageDecoder.createSource(this.contentResolver,
                    Uri.fromFile(file))
                val bitmap = ImageDecoder.decodeBitmap(decode)
//                binding.profileUpdateImg.setImageBitmap(bitmap)
            }
        }
    }
}