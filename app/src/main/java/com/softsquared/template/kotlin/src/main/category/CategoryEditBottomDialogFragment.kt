package com.softsquared.template.kotlin.src.main.category

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.softsquared.template.kotlin.R
import com.softsquared.template.kotlin.config.ApplicationClass
import com.softsquared.template.kotlin.config.BaseResponse
import com.softsquared.template.kotlin.src.main.category.adapter.ScheduleCategoryEditAdapter
import com.softsquared.template.kotlin.src.main.category.models.CategoryInsertResponse
import com.softsquared.template.kotlin.src.main.category.models.CategoryUpdateRequest
import com.softsquared.template.kotlin.src.main.schedulefind.CategoryInquiryService
import com.softsquared.template.kotlin.src.main.schedulefind.CategoryInquiryView
import com.softsquared.template.kotlin.src.main.schedulefind.models.CategoryInquiryResponse
import com.softsquared.template.kotlin.src.main.schedulefind.models.UserCategoryInquiryResponse
import com.softsquared.template.kotlin.util.onMyTextChanged
import kotlinx.android.synthetic.main.fragment_category_add_bottom_dialog.*
import kotlinx.android.synthetic.main.fragment_category_color_bottom_dialog.*

class CategoryEditBottomDialogFragment : BottomSheetDialogFragment(),
    CategoryEditView, CategoryInquiryView {

    //bundle로 받기위한 변수
    var bundleColor: String? = null
    var bundleSelectName: String? = null
    var bundleWholeName: String? = null
    var size: Int? = null
    var categoryID: Int? = null
    var bundleSelectColor : String? = null

    //bundle로 받은 값을 나누기 위한 변수
    var bundleCheckColor: List<String>? = null

    //색상 추가 입력변수
    var text = ""
    var color: Int? = null

    private lateinit var categoryEditAdapter: ScheduleCategoryEditAdapter

    //선택된 칼라에 대한 변수
    var selectColor = 0

    //이미선택되있는 색상을 구분하기위한 변수
    val num = ArrayList<Int>()
    val checkNum = ArrayList<Int>()

    // 중복카테고리명을 판별하기 위한 변수
    var bundleName : List<String>? = null
    var bundleNameList = ArrayList<String>()
    var isBooleanList = ArrayList<Boolean>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_category_color_bottom_dialog, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var extra = this.arguments
        if (extra != null) {
            extra = arguments
            size = extra!!.getInt("size", 0)
            categoryID = extra.getInt("categoryID", 0)
            bundleSelectColor = extra.getString("selectColor")
            bundleColor = extra.getString("color")
            bundleSelectName = extra.getString("selectName")
            bundleWholeName = extra.getString("wholeName")
            Log.d("CategoryEditBottomDialogFragment", "size: $size")
            Log.d("CategoryEditBottomDialogFragment", "categoryID값: $categoryID")
            Log.d("CategoryEditBottomDialogFragment", "bundleColor값: $bundleColor")
            Log.d("CategoryEditBottomDialogFragment", "selectName: $bundleSelectName")
            Log.d("CategoryEditBottomDialogFragment", "bundleSelectColor: $bundleSelectColor")
            Log.d("CategoryEditBottomDialogFragment", "bundleWholeName: $bundleWholeName")
        }

        //text값 가져오기
        category_edit_et.setText(bundleSelectName)

        for (i in 0 until 10 ){
            isBooleanList.add(true)
        }

        //클릭리스너 여부
        var isBoolean1 = true
        var isBoolean2 = true
        var isBoolean3 = true
        var isBoolean4 = true
        var isBoolean5 = true
        var isBoolean6 = true
        var isBoolean7 = true
        var isBoolean8 = true
        var isBoolean9 = true
        var isBoolean10 = true

        //이미 선택한 색상을 구분하기위한 List
        val categoryColor = ArrayList<String>()
        categoryColor.add("#FF8484")
        categoryColor.add("#FCBC71")
        categoryColor.add("#FCDC71")
        categoryColor.add("#C6EF84")
        categoryColor.add("#7ED391")
        categoryColor.add("#93EAD9")
        categoryColor.add("#7CC3FF")
        categoryColor.add("#6D92F7")
        categoryColor.add("#AB93FA")
        categoryColor.add("#FFA2BE")

        //색상 클릭 시 check표시를 위한 변수
        var color1 = 1
        var color2 = 1
        var color3 = 1
        var color4 = 1
        var color5 = 1
        var color6 = 1
        var color7 = 1
        var color8 = 1
        var color9 = 1
        var color10 = 1

        bundleCheckColor = bundleColor!!.split(":")
        bundleName = bundleWholeName!!.split(":")

        //이미 선택된 색상 찾기
        for (i in 0 until size!!) {
            for (j in 0 until categoryColor.size) {

                if (bundleCheckColor!![i].contains(categoryColor[j])) {
                    if (bundleCheckColor!![i] != bundleSelectColor){
                        num.add(j + 1)
                    }else{
                        checkNum.add(j+1)
                    }
                }
            }
            bundleNameList.add(bundleName!![i])
            Log.d("TAG", "bundleNameList: ${bundleNameList[i]}")

        }

        // 이미 선택된 색상이면, default색상 처리 및 클릭 시 체크표시 안나오게 설정
        for (i in 0 until num.size) {
            when (num[i]) {
                1 -> {
                    category_edit_color_1.setColorFilter(Color.parseColor("#CED5D9"))
                    category_edit_color1_check.background = null
                    isBoolean1 = false
                }
                2 -> {
                    category_edit_color_2.setColorFilter(Color.parseColor("#CED5D9"))
                    category_edit_color2_check.background = null
                    isBoolean2 = false
                }
                3 -> {
                    category_edit_color_3.setColorFilter(Color.parseColor("#CED5D9"))
                    category_edit_color3_check.background = null
                    isBoolean3 = false
                }
                4 -> {
                    category_edit_color_4.setColorFilter(Color.parseColor("#CED5D9"))
                    category_edit_color4_check.background = null
                    isBoolean4 = false
                }
                5 -> {
                    category_edit_color_5.setColorFilter(Color.parseColor("#CED5D9"))
                    category_edit_color5_check.background = null
                    isBoolean5 = false
                }
                6 -> {
                    category_edit_color_6.setColorFilter(Color.parseColor("#CED5D9"))
                    category_edit_color6_check.background = null
                    isBoolean6 = false
                }
                7 -> {
                    category_edit_color_7.setColorFilter(Color.parseColor("#CED5D9"))
                    category_edit_color7_check.background = null
                    isBoolean7 = false
                }
                8 -> {
                    category_edit_color_8.setColorFilter(Color.parseColor("#CED5D9"))
                    category_edit_color8_check.background = null
                    isBoolean8 = false
                }
                9 -> {
                    category_edit_color_9.setColorFilter(Color.parseColor("#CED5D9"))
                    category_edit_color9_check.background = null
                    isBoolean9 = false
                }
                10 -> {
                    category_edit_color_10.setColorFilter(Color.parseColor("#CED5D9"))
                    category_edit_color10_check.background = null
                    isBoolean10 = false
                }
            }
        }

        //선택된 색상은 체크표시
        for (i in 0 until checkNum.size) {
            when (checkNum[i]) {
                1 -> {
                    category_edit_color1_check.visibility = View.VISIBLE
                    selectColor = 1
                    color1++
                }
                2 -> {
                    category_edit_color2_check.visibility = View.VISIBLE
                    selectColor = 2
                    color2++
                }
                3 -> {
                    category_edit_color3_check.visibility = View.VISIBLE
                    selectColor = 3
                    color3++
                }
                4 -> {
                    category_edit_color4_check.visibility = View.VISIBLE
                    selectColor = 4
                    color4++
                }
                5 -> {
                    category_edit_color5_check.visibility = View.VISIBLE
                    selectColor = 5
                    color5++
                }
                6 -> {
                    category_edit_color6_check.visibility = View.VISIBLE
                    selectColor = 6
                    color6++
                }
                7 -> {
                    category_edit_color7_check.visibility = View.VISIBLE
                    selectColor = 7
                    color7++
                }
                8 -> {
                    category_edit_color8_check.visibility = View.VISIBLE
                    selectColor = 8
                    color8++
                }
                9 -> {
                    category_edit_color9_check.visibility = View.VISIBLE
                    selectColor = 9
                    color9++
                }
                10 -> {
                    category_edit_color10_check.visibility = View.VISIBLE
                    selectColor = 10
                    color10++
                }
            }
        }

        //이름/색상 선택 안할 경우
        category_edit_no_save.setOnClickListener {
            if (selectColor != 0 || text.isNotEmpty()) {
                Toast.makeText(context, "이름 혹은 색상을 선택해주세요\n혹은 이미 사용 중인 카테고리명입니다.",
                    Toast.LENGTH_SHORT).show() }
        }

        //완료 버튼
        category_edit_save.setOnClickListener {
            text = category_edit_et.text.toString()
            Log.d("TAG", "완료버튼: $text")

            if (selectColor == 0) {
                Toast.makeText(context, "색상을 선택해주세요", Toast.LENGTH_SHORT).show()
            } else {

                val categoryUpdateRequest = CategoryUpdateRequest(
                    categoryName = text,
                    categoryColor = selectColor
                )
                Log.d("TAG", "selectColor: $selectColor")
                CategoryEditService(this).tryPatchCategoryEditUpdate(
                    categoryID.toString(),
                    categoryUpdateRequest
                )
            }
        }

        //EditText 변화에 따른 완료버튼 활성화
        category_edit_et.onMyTextChanged {
            if (it.toString().count() >= 0){
                check()
            }
        }

        //X버튼
        category_edit_x_btn.setOnClickListener {
            dismiss()
        }

        //첫번째 색상 클릭 시
        if (isBoolean1){
            category_edit_color_1.setOnClickListener {
                //홀수면 체크표시, 짝수면 체크X
                if (color1 % 2 != 0) {

                    ApplicationClass.sSharedPreferences.edit().putString("color", "#FFA2BE").apply()

                    category_edit_color1_check.visibility = View.VISIBLE
                    selectColor = 1

                    //체크표시 될 때 나머지 색상들 체크해제
                    category_edit_color2_check.visibility = View.GONE
                    category_edit_color3_check.visibility = View.GONE
                    category_edit_color4_check.visibility = View.GONE
                    category_edit_color5_check.visibility = View.GONE
                    category_edit_color6_check.visibility = View.GONE
                    category_edit_color7_check.visibility = View.GONE
                    category_edit_color8_check.visibility = View.GONE
                    category_edit_color9_check.visibility = View.GONE
                    category_edit_color10_check.visibility = View.GONE

                    //클릭 할 때마다 조건을 확인하여 완료버튼 활성화
                    check()

                } else {
                    category_edit_color1_check.visibility = View.GONE
                    selectColor = 0
                    check()
                }
                color1++
            }
        }

        if (isBoolean2){
            category_edit_color_2.setOnClickListener {
                if (color2 % 2 != 0) {
                    category_edit_color2_check.visibility = View.VISIBLE
                    selectColor = 2

                    category_edit_color1_check.visibility = View.GONE
                    category_edit_color3_check.visibility = View.GONE
                    category_edit_color4_check.visibility = View.GONE
                    category_edit_color5_check.visibility = View.GONE
                    category_edit_color6_check.visibility = View.GONE
                    category_edit_color7_check.visibility = View.GONE
                    category_edit_color8_check.visibility = View.GONE
                    category_edit_color9_check.visibility = View.GONE
                    category_edit_color10_check.visibility = View.GONE

                    check()

                } else {
                    category_edit_color2_check.visibility = View.GONE
                    selectColor = 0
                    check()
                }
                color2++
            }
        }

        if (isBoolean3){
            category_edit_color_3.setOnClickListener {
                if (color3 % 2 != 0) {
                    category_edit_color3_check.visibility = View.VISIBLE
                    selectColor = 3

                    category_edit_color1_check.visibility = View.GONE
                    category_edit_color2_check.visibility = View.GONE
                    category_edit_color4_check.visibility = View.GONE
                    category_edit_color5_check.visibility = View.GONE
                    category_edit_color6_check.visibility = View.GONE
                    category_edit_color7_check.visibility = View.GONE
                    category_edit_color8_check.visibility = View.GONE
                    category_edit_color9_check.visibility = View.GONE
                    category_edit_color10_check.visibility = View.GONE
                    check()

                } else {
                    category_edit_color3_check.visibility = View.GONE
                    selectColor = 0
                    check()

                }
                color3++
            }
        }

        if (isBoolean4){
            category_edit_color_4.setOnClickListener {
                if (color4 % 2 != 0) {
                    category_edit_color4_check.visibility = View.VISIBLE
                    selectColor = 4

                    category_edit_color1_check.visibility = View.GONE
                    category_edit_color2_check.visibility = View.GONE
                    category_edit_color3_check.visibility = View.GONE
                    category_edit_color5_check.visibility = View.GONE
                    category_edit_color6_check.visibility = View.GONE
                    category_edit_color7_check.visibility = View.GONE
                    category_edit_color8_check.visibility = View.GONE
                    category_edit_color9_check.visibility = View.GONE
                    category_edit_color10_check.visibility = View.GONE
                    check()

                } else {
                    category_edit_color4_check.visibility = View.GONE
                    selectColor = 0
                    check()

                }
                color4++
            }
        }

        if (isBoolean5){
            category_edit_color_5.setOnClickListener {
                if (color5 % 2 != 0) {
                    category_edit_color5_check.visibility = View.VISIBLE
                    selectColor = 5

                    category_edit_color1_check.visibility = View.GONE
                    category_edit_color2_check.visibility = View.GONE
                    category_edit_color3_check.visibility = View.GONE
                    category_edit_color4_check.visibility = View.GONE
                    category_edit_color6_check.visibility = View.GONE
                    category_edit_color7_check.visibility = View.GONE
                    category_edit_color8_check.visibility = View.GONE
                    category_edit_color9_check.visibility = View.GONE
                    category_edit_color10_check.visibility = View.GONE
                    check()

                } else {
                    category_edit_color5_check.visibility = View.GONE
                    selectColor = 0
                    check()

                }
                color5++
            }
        }

        if (isBoolean6){
            category_edit_color_6.setOnClickListener {
                if (color6 % 2 != 0) {
                    category_edit_color6_check.visibility = View.VISIBLE
                    selectColor = 6

                    category_edit_color1_check.visibility = View.GONE
                    category_edit_color2_check.visibility = View.GONE
                    category_edit_color3_check.visibility = View.GONE
                    category_edit_color4_check.visibility = View.GONE
                    category_edit_color5_check.visibility = View.GONE
                    category_edit_color7_check.visibility = View.GONE
                    category_edit_color8_check.visibility = View.GONE
                    category_edit_color9_check.visibility = View.GONE
                    category_edit_color10_check.visibility = View.GONE
                    check()

                } else {
                    category_edit_color6_check.visibility = View.GONE
                    selectColor = 0
                    check()

                }
                color6++
            }
        }

        if (isBoolean7){
            category_edit_color_7.setOnClickListener {
                if (color7 % 2 != 0) {
                    category_edit_color7_check.visibility = View.VISIBLE
                    selectColor = 7

                    category_edit_color1_check.visibility = View.GONE
                    category_edit_color2_check.visibility = View.GONE
                    category_edit_color3_check.visibility = View.GONE
                    category_edit_color4_check.visibility = View.GONE
                    category_edit_color5_check.visibility = View.GONE
                    category_edit_color6_check.visibility = View.GONE
                    category_edit_color8_check.visibility = View.GONE
                    category_edit_color9_check.visibility = View.GONE
                    category_edit_color10_check.visibility = View.GONE
                    check()

                } else {
                    category_edit_color7_check.visibility = View.GONE
                    selectColor = 0
                    check()

                }
                color7++
            }
        }

        if (isBoolean8){
            category_edit_color_8.setOnClickListener {
                if (color8 % 2 != 0) {
                    category_edit_color8_check.visibility = View.VISIBLE
                    selectColor = 8

                    category_edit_color1_check.visibility = View.GONE
                    category_edit_color2_check.visibility = View.GONE
                    category_edit_color3_check.visibility = View.GONE
                    category_edit_color4_check.visibility = View.GONE
                    category_edit_color5_check.visibility = View.GONE
                    category_edit_color6_check.visibility = View.GONE
                    category_edit_color7_check.visibility = View.GONE
                    category_edit_color9_check.visibility = View.GONE
                    category_edit_color10_check.visibility = View.GONE
                    check()

                } else {
                    category_edit_color8_check.visibility = View.GONE
                    selectColor = 0
                    check()
                }
                color8++
            }
        }

        if (isBoolean9){
            category_edit_color_9.setOnClickListener {
                if (color9 % 2 != 0) {
                    category_edit_color9_check.visibility = View.VISIBLE
                    selectColor = 9

                    category_edit_color1_check.visibility = View.GONE
                    category_edit_color2_check.visibility = View.GONE
                    category_edit_color3_check.visibility = View.GONE
                    category_edit_color4_check.visibility = View.GONE
                    category_edit_color5_check.visibility = View.GONE
                    category_edit_color6_check.visibility = View.GONE
                    category_edit_color7_check.visibility = View.GONE
                    category_edit_color8_check.visibility = View.GONE
                    category_edit_color10_check.visibility = View.GONE
                    check()

                } else {
                    category_edit_color9_check.visibility = View.GONE
                    selectColor = 0
                    check()

                }
                color9++
            }
        }

        if (isBoolean10){
            category_edit_color_10.setOnClickListener {
                if (color10 % 2 != 0) {
                    category_edit_color10_check.visibility = View.VISIBLE
                    selectColor = 10

                    category_edit_color1_check.visibility = View.GONE
                    category_edit_color2_check.visibility = View.GONE
                    category_edit_color3_check.visibility = View.GONE
                    category_edit_color4_check.visibility = View.GONE
                    category_edit_color5_check.visibility = View.GONE
                    category_edit_color6_check.visibility = View.GONE
                    category_edit_color7_check.visibility = View.GONE
                    category_edit_color8_check.visibility = View.GONE
                    category_edit_color9_check.visibility = View.GONE
                    check()

                } else {
                    category_edit_color10_check.visibility = View.GONE
                    selectColor = 0
                    check()

                }
                color10++
            }
        }

    }

    //완료버튼 활성화확인 함수
    fun check() {
        text = category_edit_et.text.toString()

        //이미 선택되있는 색상
        for (i in 0 until num.size) {

            if (selectColor == num[i]) {
                category_edit_save.visibility = View.GONE
                category_edit_no_save.visibility = View.VISIBLE
                selectColor = 0
            }
        }

        if (selectColor != 0 && text.isNotEmpty()) {
            category_edit_save.visibility = View.VISIBLE
            category_edit_no_save.visibility = View.GONE
        } else {
            category_edit_save.visibility = View.GONE
            category_edit_no_save.visibility = View.VISIBLE
        }

        //동일 카테고리명 판별
        for (i in 0 until bundleNameList.size){
            if (bundleNameList[i] == text){
                category_edit_save.visibility = View.GONE
                category_edit_no_save.visibility = View.VISIBLE
            }
        }
    }

    override fun onPostCategoryInsertSuccess(response: CategoryInsertResponse) {
    }

    override fun onPostCategoryInsertFail(message: String) {
    }

    override fun onDeleteCategoryDeleteSuccess(response: BaseResponse) {
    }

    override fun onDeleteCategoryDeleteFail(message: String) {
    }

    override fun onPatchCategoryUpdateSuccess(response: BaseResponse) {

        when (response.code) {
            100 -> {
                Log.d("TAG", "onPatchCategoryUpdateSuccess: 카테고리 수정 성공")
                CategoryInquiryService(this).tryGetUserCategoryInquiry()
            }
            else -> {

            }
        }
    }

    override fun onPatchCategoryUpdateFail(message: String) {
    }

    override fun onGetUserCategoryInquirySuccess(responseUser: UserCategoryInquiryResponse) {
        when (responseUser.code) {
            100 -> {
                Log.d("TAG", "onGetCategoryInquirySuccess: 카테고리 조회 성공")
                val intent = Intent(activity, CategoryEditActivity::class.java)
                var inputName = ""
                var inputColor = ""
                var inputSize = 0
                var inputCategoryID = ""

                for (i in 0 until responseUser.data.size) {
                    inputName += responseUser.data[i].categoryName + ":"
                    inputColor += responseUser.data[i].colorInfo + ":"
                    inputCategoryID += "${responseUser.data[i].categoryID}:"
                }
                inputSize = responseUser.data.size
                intent.putExtra("name", inputName)
                intent.putExtra("color", inputColor)
                intent.putExtra("size", inputSize)
                intent.putExtra("categoryID", inputCategoryID)
                startActivity(intent)
            }
            else -> {

            }
        }
    }

    override fun onGetUserCategoryInquiryFail(message: String) {
    }

    override fun onGetCategoryInquirySuccess(categoryInquiryResponse: CategoryInquiryResponse) {
    }

    override fun onGetCategoryInquiryFail(message: String) {
    }
}