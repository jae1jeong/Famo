package com.softsquared.template.kotlin.src.main.category

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Button
import com.softsquared.template.kotlin.R


class DeleteDialog(context: Context) : Dialog(context), View.OnClickListener {


    lateinit var mDeleteButtonClickListener: deleteButtonClickListener

    interface deleteButtonClickListener {
        fun onDialogButtonClick(view: View?)
    }

    fun setOnDialogButtonClickListener(mListener: deleteButtonClickListener) {
        mDeleteButtonClickListener = mListener
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.category_delete_dialog);

        val delete : Button = findViewById(R.id.delete_check)
        val cancel : Button = findViewById(R.id.delete_cancel)

        delete.setOnClickListener(this)
        cancel.setOnClickListener(this)



    }

    override fun onClick(v: View?) {
        mDeleteButtonClickListener.onDialogButtonClick(v!!)
    }

}