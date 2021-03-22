package com.softsquared.template.kotlin.src.main.schedulefind.adapter

import android.content.Intent
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.softsquared.template.kotlin.R
import com.softsquared.template.kotlin.src.main.schedulefind.CategoryInquiryView
import com.softsquared.template.kotlin.src.main.schedulefind.models.CategoryInquiryResponse
import com.softsquared.template.kotlin.src.main.schedulefind.models.UserCategoryInquiryResponse
import com.softsquared.template.kotlin.src.main.schedulefind.models.ScheduleCategoryData
import com.softsquared.template.kotlin.src.main.schedulefind.models.ScheduleCategoryListData

class ScheduleCategoryListAdapter(
    var categoryList: ArrayList<ScheduleCategoryListData>,
    myScheduleCategoryRecyclerView: IScheduleCategoryRecyclerView
) :
    RecyclerView.Adapter<ScheduleCategoryListAdapter.ScheduleCategoryHolder>(), CategoryInquiryView {

    private var iScheduleCategoryRecyclerView: IScheduleCategoryRecyclerView? = null

    init {
        this.iScheduleCategoryRecyclerView = myScheduleCategoryRecyclerView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScheduleCategoryHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recyclerview_schedule_find_category_item, parent, false)
        view.setOnClickListener {

        }
        return ScheduleCategoryHolder(view, iScheduleCategoryRecyclerView!!).apply {
            itemView.setOnClickListener {
                val curPos : Int = adapterPosition
                val categoryList: ScheduleCategoryListData = categoryList.get(curPos)
            }
        }
    }

    override fun onBindViewHolder(holder: ScheduleCategoryHolder, position: Int) {
//        holder.text.text = categoryList[position].text
        holder.text.text = categoryList[position].text
//        holder.color.setImageResource(categoryList[position])
//        holder.color.setImageResource(categoryList[position].color)
//        holder.color.setColorFilter(Color.parseColor(response.data[0].colorInfo))
//        img.setColorFilter(Color.parseColor("#FF0000"))
    }

    override fun getItemCount(): Int = categoryList.size

    class ScheduleCategoryHolder(
        itemView: View, myScheduleCategoryRecyclerView: IScheduleCategoryRecyclerView
    ) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        private var iSearchRecyclerViewInterface: IScheduleCategoryRecyclerView

        val text = itemView.findViewById<TextView>(R.id.recyclerview_category_text)

        init {
            text.setOnClickListener(this)
            this.iSearchRecyclerViewInterface = myScheduleCategoryRecyclerView
        }

        override fun onClick(view: View?) {
            when (view) {
                text -> {
                    Log.d("로그", "onClick: 카테고리 클릭: $adapterPosition")

                    //        holder.color.setColorFilter(Color.parseColor(response.data[0].colorInfo))


                    val wholeColor = iSearchRecyclerViewInterface.onColor()
                    var size = 0
                    var color: List<String>? = null
                    var categoryID = ArrayList<Int>()
                    Log.d("로그", "칼라값 받아오기 테스트: $color")
                    color = wholeColor.split(":")

                    for (i in wholeColor.indices) {
                        if (wholeColor.substring(i, i + 1) == ":") {
                            size++
                        }
                    }
                    Log.d("로그", "사이즈 : $size")
                    // 5 1 2

                    for (i in 0 until size) {

                        if (color[i] == "#FF8484") {
                            categoryID!!.add(1)
                        }
                        if (color[i] == "#FCBC71") {
                            categoryID!!.add(2)
                        }
                        if (color[i] == "#FCDC71") {
                            categoryID!!.add(3)
                        }
                        if (color[i] == "#C6EF84") {
                            categoryID!!.add(4)
                        }
                        if (color[i] == "#7ED391") {
                            categoryID!!.add(5)
                        }
                        if (color[i] == "#93EAD9") {
                            categoryID!!.add(6)
                        }
                        if (color[i] == "#7CC3FF") {
                            categoryID!!.add(7)
                        }
                        if (color[i] == "#6D92F7") {
                            categoryID!!.add(8)
                        }
                        if (color[i] == "#AB93FA") {
                            categoryID!!.add(9)
                        }
                        if (color[i] == "#FFA2BE") {
                            categoryID!!.add(10)
                        }

                    }
                    Log.d("TAG", "categoryID: $categoryID")

//                    if (adapterPosition == 0) {
//                        this.iSearchRecyclerViewInterface.onItemMoveBtnClicked(categoryID[0])
//
//                    } else if (adapterPosition == 1) {
//                        this.iSearchRecyclerViewInterface.onItemMoveBtnClicked(categoryID[1])
//                    } else if (adapterPosition == 2) {
//                        this.iSearchRecyclerViewInterface.onItemMoveBtnClicked(categoryID[2])
//                    } else if (adapterPosition == 3) {
//                        this.iSearchRecyclerViewInterface.onItemMoveBtnClicked(categoryID[3])
//                    } else if (adapterPosition == 4) {
//                        this.iSearchRecyclerViewInterface.onItemMoveBtnClicked(categoryID[4])
//                    } else if (adapterPosition == 5) {
//                        this.iSearchRecyclerViewInterface.onItemMoveBtnClicked(categoryID[5])
//                    } else if (adapterPosition == 6) {
//                        this.iSearchRecyclerViewInterface.onItemMoveBtnClicked(categoryID[6])
//                    } else if (adapterPosition == 7) {
//                        this.iSearchRecyclerViewInterface.onItemMoveBtnClicked(categoryID[7])
//                    } else if (adapterPosition == 8) {
//                        this.iSearchRecyclerViewInterface.onItemMoveBtnClicked(categoryID[8])
//                    } else if (adapterPosition == 9) {
//                        this.iSearchRecyclerViewInterface.onItemMoveBtnClicked(categoryID[9])
//                    }

                    Log.d("TAG", "onClick: 다시돌아옴?")
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