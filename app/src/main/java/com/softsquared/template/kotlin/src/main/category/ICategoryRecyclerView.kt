package com.softsquared.template.kotlin.src.main.category

interface ICategoryRecyclerView {

    fun onItemDeleteBtnClicked(position: Int)

    fun onCategoryID() : String

    fun onMoveFragment()
}