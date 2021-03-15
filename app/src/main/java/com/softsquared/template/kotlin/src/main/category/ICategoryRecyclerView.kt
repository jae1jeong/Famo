package com.softsquared.template.kotlin.src.main.category

interface ICategoryRecyclerView {

    fun onItemDeleteBtnClicked(position: Int)

    fun onCategoryID(categoryID: Int) : Int

    fun onMoveFragment(categoryID : Int, text : String)

    fun getColor(color : String)

    fun onMoveDeleteUpdate()
}