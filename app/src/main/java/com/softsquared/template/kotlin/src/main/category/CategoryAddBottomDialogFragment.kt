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
import com.softsquared.template.kotlin.config.BaseResponse
import com.softsquared.template.kotlin.src.main.category.adapter.ScheduleCategoryEditAdapter
import com.softsquared.template.kotlin.src.main.category.models.CategoryInsertRequest
import com.softsquared.template.kotlin.src.main.category.models.CategoryInsertResponse
import com.softsquared.template.kotlin.src.main.schedulefind.models.ScheduleCategoryData
import com.softsquared.template.kotlin.src.main.schedulefind.models.ScheduleWholeData
import kotlinx.android.synthetic.main.activity_category_edit.category_color_1
import kotlinx.android.synthetic.main.fragment_category_add_bottom_dialog.*

class CategoryAddBottomDialogFragment : BottomSheetDialogFragment(),
    CategoryEditView {

    //bundle로 받기위한 변수
    var bundleColor: String? = null
    var size: Int? = null

    var bundleCheckColor : List<String>? = null

    var text = ""
    var color: Int? = null
//    var categoryEditAdapter: ScheduleCategoryEditAdapter()

    var selectColor = 0

    private lateinit var scheduleCategoryEditAdapter: ScheduleCategoryEditAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_category_add_bottom_dialog, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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

        var extra = this.arguments
        if (extra != null) {
            extra = arguments
            size = extra!!.getInt("size", 0)
            bundleColor = extra.getString("color")
            Log.d("CategoryAddBottomDialogFragment", "값: $size")
            Log.d("CategoryAddBottomDialogFragment", "값: $bundleColor")
        }
//      a b c
        bundleCheckColor = bundleColor!!.split(":")

        val num = ArrayList<Int>()

        for (i in 0 until size!!) {
            for (j in 0 until categoryColor.size ){

                if (bundleCheckColor!![i].contains(categoryColor[j])){
                    num.add(j)
                }
            }
        }

        for (i in 0 until num.size){
            when(num[i]){
                0 -> category_color_1.setColorFilter(Color.parseColor("#CED5D9"))
                1 -> category_color_2.setColorFilter(Color.parseColor("#CED5D9"))
                2 -> category_color_3.setColorFilter(Color.parseColor("#CED5D9"))
                3 -> category_color_4.setColorFilter(Color.parseColor("#CED5D9"))
                4 -> category_color_5.setColorFilter(Color.parseColor("#CED5D9"))
                5 -> category_color_6.setColorFilter(Color.parseColor("#CED5D9"))
                6 -> category_color_7.setColorFilter(Color.parseColor("#CED5D9"))
                7 -> category_color_8.setColorFilter(Color.parseColor("#CED5D9"))
                8 -> category_color_9.setColorFilter(Color.parseColor("#CED5D9"))
                9 -> category_color_10.setColorFilter(Color.parseColor("#CED5D9"))
            }
//            binding.loginRlNumber.background = resources.getDrawable(
//                com.example.airbnb.R.drawable.login_info_input_validation,
//                null
//            )
            when(num[i]){
                0 -> category_color1_check.background = null
                1 -> category_color2_check.background = null
                2 -> category_color3_check.background = null
                3 -> category_color4_check.background = null
                4 -> category_color5_check.background = null
                5 -> category_color6_check.background = null
                6 -> category_color7_check.background = null
                7 -> category_color8_check.background = null
                8 -> category_color9_check.background = null
                9 -> category_color10_check.background = null
            }
        }

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

//        val selectColor1 = 1
//        val selectColor2 = 2
//        val selectColor3 = 3
//        val selectColor4 = 4
//        val selectColor5 = 5
//        val selectColor6 = 6
//        val selectColor7 = 7
//        val selectColor8 = 8
//        val selectColor9 = 9
//        val selectColor10 = 10

        category_add_no_save.setOnClickListener {
            if (selectColor != 0 || text!!.isNotEmpty()){
                Toast.makeText(context,"이름 혹은 색상을 선택해주세요",Toast.LENGTH_SHORT).show()
            }
        }

        category_add_save.setOnClickListener {
            text = category_add_et.text.toString()

            if (selectColor == 0){
                Toast.makeText(context,"색상을 선택해주세요",Toast.LENGTH_SHORT).show()
            }else{
                val categoryInsertRequest = CategoryInsertRequest(
                    categoryName = text!!,
                    categoryColor = selectColor
                )
                Log.d("TAG", "selectColor: $selectColor")
                CategoryEditService(this).tryPostCategoryEditInsert(categoryInsertRequest)
            }


        }

        category_color_1.setOnClickListener {
            if (color1 % 2 != 0){
                category_color1_check.visibility = View.VISIBLE
                selectColor = 1

                category_color2_check.visibility = View.GONE
                category_color3_check.visibility = View.GONE
                category_color4_check.visibility = View.GONE
                category_color5_check.visibility = View.GONE
                category_color6_check.visibility = View.GONE
                category_color7_check.visibility = View.GONE
                category_color8_check.visibility = View.GONE
                category_color9_check.visibility = View.GONE
                category_color10_check.visibility = View.GONE

                check()

            }else{
                category_color1_check.visibility = View.GONE
                selectColor = 0

                check()
            }
            color1++
        }

        category_color_2.setOnClickListener {
            if (color2 % 2 != 0){
                category_color2_check.visibility = View.VISIBLE
                selectColor = 2

                category_color1_check.visibility = View.GONE
                category_color3_check.visibility = View.GONE
                category_color4_check.visibility = View.GONE
                category_color5_check.visibility = View.GONE
                category_color6_check.visibility = View.GONE
                category_color7_check.visibility = View.GONE
                category_color8_check.visibility = View.GONE
                category_color9_check.visibility = View.GONE
                category_color10_check.visibility = View.GONE

                check()
            }else{
                category_color2_check.visibility = View.GONE
                selectColor = 0

                check()
            }
            color2++
        }

        category_color_3.setOnClickListener {
            if (color3 % 2 != 0){
                category_color3_check.visibility = View.VISIBLE
                selectColor = 3

                category_color1_check.visibility = View.GONE
                category_color2_check.visibility = View.GONE
                category_color4_check.visibility = View.GONE
                category_color5_check.visibility = View.GONE
                category_color6_check.visibility = View.GONE
                category_color7_check.visibility = View.GONE
                category_color8_check.visibility = View.GONE
                category_color9_check.visibility = View.GONE
                category_color10_check.visibility = View.GONE

                check()
            }else{
                category_color3_check.visibility = View.GONE
                selectColor = 0

                check()
            }
            color3++
        }

        category_color_4.setOnClickListener {
            if (color4 % 2 != 0){
                category_color4_check.visibility = View.VISIBLE
                selectColor = 4

                category_color1_check.visibility = View.GONE
                category_color2_check.visibility = View.GONE
                category_color3_check.visibility = View.GONE
                category_color5_check.visibility = View.GONE
                category_color6_check.visibility = View.GONE
                category_color7_check.visibility = View.GONE
                category_color8_check.visibility = View.GONE
                category_color9_check.visibility = View.GONE
                category_color10_check.visibility = View.GONE

                check()
            }else{
                category_color4_check.visibility = View.GONE
                selectColor = 0

                check()
            }
            color4++
        }

        category_color_5.setOnClickListener {
            if (color5 % 2 != 0){
                category_color5_check.visibility = View.VISIBLE
                selectColor = 5

                category_color1_check.visibility = View.GONE
                category_color2_check.visibility = View.GONE
                category_color3_check.visibility = View.GONE
                category_color4_check.visibility = View.GONE
                category_color6_check.visibility = View.GONE
                category_color7_check.visibility = View.GONE
                category_color8_check.visibility = View.GONE
                category_color9_check.visibility = View.GONE
                category_color10_check.visibility = View.GONE

                check()
            }else{
                category_color5_check.visibility = View.GONE
                selectColor = 0

                check()
            }
            color5++
        }

        category_color_6.setOnClickListener {
            if (color6 % 2 != 0){
                category_color6_check.visibility = View.VISIBLE
                selectColor = 6

                category_color1_check.visibility = View.GONE
                category_color2_check.visibility = View.GONE
                category_color3_check.visibility = View.GONE
                category_color4_check.visibility = View.GONE
                category_color5_check.visibility = View.GONE
                category_color7_check.visibility = View.GONE
                category_color8_check.visibility = View.GONE
                category_color9_check.visibility = View.GONE
                category_color10_check.visibility = View.GONE

                check()
            }else{
                category_color6_check.visibility = View.GONE
                selectColor = 0

                check()
            }
            color6++
        }

        category_color_7.setOnClickListener {
            if (color7 % 2 != 0){
                category_color7_check.visibility = View.VISIBLE
                selectColor = 7

                category_color1_check.visibility = View.GONE
                category_color2_check.visibility = View.GONE
                category_color3_check.visibility = View.GONE
                category_color4_check.visibility = View.GONE
                category_color5_check.visibility = View.GONE
                category_color6_check.visibility = View.GONE
                category_color8_check.visibility = View.GONE
                category_color9_check.visibility = View.GONE
                category_color10_check.visibility = View.GONE

                check()
            }else{
                category_color7_check.visibility = View.GONE
                selectColor = 0

                check()
            }
            color7++
        }

        category_color_8.setOnClickListener {
            if (color8 % 2 != 0){
                category_color8_check.visibility = View.VISIBLE
                selectColor = 8

                category_color1_check.visibility = View.GONE
                category_color2_check.visibility = View.GONE
                category_color3_check.visibility = View.GONE
                category_color4_check.visibility = View.GONE
                category_color5_check.visibility = View.GONE
                category_color6_check.visibility = View.GONE
                category_color7_check.visibility = View.GONE
                category_color9_check.visibility = View.GONE
                category_color10_check.visibility = View.GONE

                check()
            }else{
                category_color8_check.visibility = View.GONE
                selectColor = 0

                check()
            }
            color8++
        }

        category_color_9.setOnClickListener {
            if (color9 % 2 != 0){
                category_color9_check.visibility = View.VISIBLE
                selectColor = 9

                category_color1_check.visibility = View.GONE
                category_color2_check.visibility = View.GONE
                category_color3_check.visibility = View.GONE
                category_color4_check.visibility = View.GONE
                category_color5_check.visibility = View.GONE
                category_color6_check.visibility = View.GONE
                category_color7_check.visibility = View.GONE
                category_color8_check.visibility = View.GONE
                category_color10_check.visibility = View.GONE

                check()
            }else{
                category_color9_check.visibility = View.GONE
                selectColor = 0

                check()
            }
            color9++
        }

        category_color_10.setOnClickListener {
            if (color10 % 2 != 0){
                category_color10_check.visibility = View.VISIBLE
                selectColor = 10

                category_color1_check.visibility = View.GONE
                category_color2_check.visibility = View.GONE
                category_color3_check.visibility = View.GONE
                category_color4_check.visibility = View.GONE
                category_color5_check.visibility = View.GONE
                category_color6_check.visibility = View.GONE
                category_color7_check.visibility = View.GONE
                category_color8_check.visibility = View.GONE
                category_color9_check.visibility = View.GONE

                check()
            }else{
                category_color10_check.visibility = View.GONE
                selectColor = 0

                check()
            }
            color10++
        }
//        a.setOnClickListener {
////            itemClick(0)
////            dialog?.dismiss()
//        }
//        b.setOnClickListener {
//            itemClick(1)
//            dialog?.dismiss()
//        }
    }

    override fun onPostCategoryInsertSuccess(response: CategoryInsertResponse) {

        when (response.code) {
            100 -> {
                Log.d("TAG", "onPostCategoryInsertSuccess: 카테고리생성 테스트")
                response.data
                var inputColor = ""
                when (selectColor) {
                    0 -> inputColor = "#CED5D9"
                    1 -> inputColor = "#FF8484"
                    2 -> inputColor = "#FCBC71"
                    3 -> inputColor = "#FCDC71"
                    4 -> inputColor = "#C6EF84"
                    5 -> inputColor = "#7ED391"
                    6 -> inputColor = "#93EAD9"
                    7 -> inputColor = "#7CC3FF"
                    8 -> inputColor = "#6D92F7"
                    9 -> inputColor = "#AB93FA"
                    10 -> inputColor = "#FFA2BE"
                }
                val intent = Intent(activity, CategoryEditActivity::class.java)
                intent.putExtra("categoryID", response.data)
                Log.d("TAG", "onPostCategoryInsertSuccess 카테고리생성고유ID : ${response.data}")

                val addList = arrayListOf(
                    ScheduleCategoryData(text, inputColor)
                )
//                val categoryEditList = ArrayList<ScheduleCategoryData>()
                val scheduleCategoryData = ScheduleCategoryData(text, inputColor)
//                text = category_add_et.text.toString()
//                Log.d("TAG", "onPostCategoryInsertSuccess: 카테고리생성 test :$text")
                Log.d("TAG", "onPostCategoryInsertSuccess: 카테고리생성 inputColor:$inputColor")
//                categoryEditList.add(ScheduleCategoryData(text, inputColor))

                val categoryEditAdapter = ScheduleCategoryEditAdapter(addList,activity as CategoryEditActivity)
//                categoryEditAdapter = ScheduleCategoryEditAdapter(addList)

                categoryEditAdapter.addItem(scheduleCategoryData)
                categoryEditAdapter.notifyDataSetChanged()

//                scheduleCategoryEditAdapter.addItem(scheduleCategoryData)
//                scheduleCategoryEditAdapter.notifyDataSetChanged()

//                startActivity(intent)


            }
            else -> {
            }
        }


    }

    fun check(){
        text = category_add_et.text.toString()
        Log.d("TAG", "check: test $text")
        if (selectColor != 0 && text!!.isNotEmpty()){
            category_add_save.visibility = View.VISIBLE
            category_add_no_save.visibility = View.GONE
//            category_add_save.setBackgroundColor(Color.parseColor("#FFAE2A"))
        }else{
//            category_add_save.background = null
            category_add_save.visibility = View.GONE
            category_add_no_save.visibility = View.VISIBLE
        }
    }

    override fun onPostCategoryInsertFail(message: String) {
    }

    override fun onDeleteCategoryDeleteSuccess(response: BaseResponse) {
    }

    override fun onDeleteCategoryDeleteFail(message: String) {
    }

    override fun onPatchCategoryUpdateSuccess(response: BaseResponse) {
    }

    override fun onPatchCategoryUpdateFail(message: String) {
    }
}