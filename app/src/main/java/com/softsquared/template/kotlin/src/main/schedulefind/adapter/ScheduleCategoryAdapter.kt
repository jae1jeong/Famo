package com.softsquared.template.kotlin.src.main.schedulefind.adapter

import android.content.Intent
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.softsquared.template.kotlin.R
import com.softsquared.template.kotlin.src.main.schedulefind.CategoryInquiryView
import com.softsquared.template.kotlin.src.main.schedulefind.models.CategoryInquiryResponse
import com.softsquared.template.kotlin.src.main.schedulefind.models.UserCategoryInquiryResponse
import com.softsquared.template.kotlin.src.main.schedulefind.models.ScheduleCategoryData

class ScheduleCategoryAdapter(
    var categoryList: ArrayList<ScheduleCategoryData>,
    myScheduleCategoryRecyclerView: IScheduleCategoryRecyclerView) :
    RecyclerView.Adapter<ScheduleCategoryAdapter.ScheduleCategoryHolder>(), CategoryInquiryView {

    private var iScheduleCategoryRecyclerView: IScheduleCategoryRecyclerView? = null

    var mPreviousIndex = -1

    init {
        Log.d("TAG", "ScheduleCategoryAdapter: init() called ")
        this.iScheduleCategoryRecyclerView = myScheduleCategoryRecyclerView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScheduleCategoryHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recyclerview_schedule_find_category_item, parent, false)
        view.setOnClickListener {

        }
        return ScheduleCategoryHolder(view, iScheduleCategoryRecyclerView!!).apply {
            itemView.setOnClickListener {
            }
        }
    }

    override fun onBindViewHolder(holder: ScheduleCategoryHolder, position: Int) {
//        holder.text.text = categoryList[position].text
        holder.text.text = categoryList[position].text
        holder.color.setColorFilter(Color.parseColor(categoryList[position].color))
//        holder.color.setImageResource(categoryList[position])
//        holder.color.setImageResource(categoryList[position].color)
//        holder.color.setColorFilter(Color.parseColor(response.data[0].colorInfo))
//        img.setColorFilter(Color.parseColor("#FF0000"))
    }

    override fun getItemCount(): Int = categoryList.size

    fun moveFragment() {

    }

    fun addItem(scheduleCategoryData: ScheduleCategoryData) {
        categoryList.add(scheduleCategoryData)
    }

    class ScheduleCategoryHolder(itemView: View,
        myScheduleCategoryRecyclerView: IScheduleCategoryRecyclerView
    ) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        private var iSearchRecyclerViewInterface: IScheduleCategoryRecyclerView

        val text = itemView.findViewById<TextView>(R.id.recyclerview_category_text)
        val color = itemView.findViewById<ImageView>(R.id.recyclerview_category_color)
//        val button = itemView.findViewById<Button>(R.id.recyclerview_category_text)
//        val category = itemView.findViewById<RelativeLayout>(R.id.item_category_list)

        init {
            //리스너연결
//            category.setOnClickListener(this)
            text.setOnClickListener(this)
            color.setOnClickListener(this)
            this.iSearchRecyclerViewInterface = myScheduleCategoryRecyclerView
        }

        override fun onClick(view: View?) {
            when (view) {
                color,text -> {
                    Log.d("로그", "onClick: 카테고리 클릭: $adapterPosition")
                    val wholeColor = iSearchRecyclerViewInterface.onColor()
                    var size = 0
                    var colorList: List<String>? = null
                    var categoryID = ArrayList<Int>()
                    Log.d("로그", "칼라값 받아오기 테스트: $color")
                    colorList = wholeColor.split(":")

                    val cnt = 0

                    for (i in wholeColor.indices) {
                        if (wholeColor.substring(i, i + 1) == ":") {
                            size++
                        }
                    }
                    Log.d("로그", "사이즈 : $size")
                    // 5 1 2

                    for (i in 0 until size) {

                        if (colorList[i] == "#FF8484") {
                            categoryID!!.add(1)
                        }
                        if (colorList[i] == "#FCBC71") {
                            categoryID!!.add(2)
                        }
                        if (colorList[i] == "#FCDC71") {
                            categoryID!!.add(3)
                        }
                        if (colorList[i] == "#C6EF84") {
                            categoryID!!.add(4)
                        }
                        if (colorList[i] == "#7ED391") {
                            categoryID!!.add(5)
                        }
                        if (colorList[i] == "#93EAD9") {
                            categoryID!!.add(6)
                        }
                        if (colorList[i] == "#7CC3FF") {
                            categoryID!!.add(7)
                        }
                        if (colorList[i] == "#6D92F7") {
                            categoryID!!.add(8)
                        }
                        if (colorList[i] == "#AB93FA") {
                            categoryID!!.add(9)
                        }
                        if (colorList[i] == "#FFA2BE") {
                            categoryID!!.add(10)
                        }

                    }
                    Log.d("TAG", "categoryID: $categoryID")

                    if (adapterPosition == 0) {
                        this.iSearchRecyclerViewInterface.onItemMoveBtnClicked(categoryID[0])
                        color.setColorFilter(Color.parseColor("#FF8484"))
                    } else if (adapterPosition == 1) {
                        this.iSearchRecyclerViewInterface.onItemMoveBtnClicked(categoryID[1])
                        color.setColorFilter(Color.parseColor("#FCBC71"))
                    } else if (adapterPosition == 2) {
                        this.iSearchRecyclerViewInterface.onItemMoveBtnClicked(categoryID[2])
                        color.setColorFilter(Color.parseColor("#FCDC71"))
                    } else if (adapterPosition == 3) {
                        this.iSearchRecyclerViewInterface.onItemMoveBtnClicked(categoryID[3])
                        color.setColorFilter(Color.parseColor("#C6EF84"))
                    } else if (adapterPosition == 4) {
                        this.iSearchRecyclerViewInterface.onItemMoveBtnClicked(categoryID[4])
                        color.setColorFilter(Color.parseColor("#7ED391"))
                    } else if (adapterPosition == 5) {
                        this.iSearchRecyclerViewInterface.onItemMoveBtnClicked(categoryID[5])
                        color.setColorFilter(Color.parseColor("#93EAD9"))
                    } else if (adapterPosition == 6) {
                        this.iSearchRecyclerViewInterface.onItemMoveBtnClicked(categoryID[6])
                        color.setColorFilter(Color.parseColor("#7CC3FF"))
                    } else if (adapterPosition == 7) {
                        this.iSearchRecyclerViewInterface.onItemMoveBtnClicked(categoryID[7])
                        color.setColorFilter(Color.parseColor("#6D92F7"))
                    } else if (adapterPosition == 8) {
                        this.iSearchRecyclerViewInterface.onItemMoveBtnClicked(categoryID[8])
                        color.setColorFilter(Color.parseColor("#AB93FA"))
                    } else if (adapterPosition == 9) {
                        this.iSearchRecyclerViewInterface.onItemMoveBtnClicked(categoryID[9])
                        color.setColorFilter(Color.parseColor("#FFA2BE"))
                    }

                    Log.d("TAG", "onClick: 다시돌아옴?")

//                    when(color[i]){
//                            "#FF8484" -> {
//                            }
//                            "#FCBC71" -> {
//                                this.iSearchRecyclerViewInterface.onItemMoveBtnClicked(2)
//                                break
//                            }
//                            "#FCDC71" -> this.iSearchRecyclerViewInterface.onItemMoveBtnClicked(3)
//                            "#C6EF84" -> this.iSearchRecyclerViewInterface.onItemMoveBtnClicked(4)
//                            "#7ED391" -> this.iSearchRecyclerViewInterface.onItemMoveBtnClicked(5)
//                            "#93EAD9" -> this.iSearchRecyclerViewInterface.onItemMoveBtnClicked(6)
//                            "#7CC3FF" -> this.iSearchRecyclerViewInterface.onItemMoveBtnClicked(7)
//                            "#6D92F7" -> this.iSearchRecyclerViewInterface.onItemMoveBtnClicked(8)
//                            "#AB93FA" -> this.iSearchRecyclerViewInterface.onItemMoveBtnClicked(9)
//                            "#FFA2BE" -> this.iSearchRecyclerViewInterface.onItemMoveBtnClicked(10)
//                        }

//                    val a = "aa"
//                    var num = 0
//                    if (a == "aa"){
//                        num = 5
//                    }
//
//
//                    when(adapterPosition){
//                        0 -> num
//                    }
                }
            }
        }

    }

    override fun onGetUserCategoryInquirySuccess(responseUser: UserCategoryInquiryResponse) {
    }

    override fun onGetUserCategoryInquiryFail(message: String) {
    }

    override fun onGetCategoryInquirySuccess(categoryInquiryResponse: CategoryInquiryResponse) {
    }

    override fun onGetCategoryInquiryFail(message: String) {
    }

}