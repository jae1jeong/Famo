package com.softsquared.template.kotlin.src.main.category

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.Button
import com.softsquared.template.kotlin.R


class DeleteImpossibleDialog(context: Context) : Dialog(context) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.category_delete_impossible_dialog);

        val check : Button = findViewById(R.id.delete_impossible_check)

        check.setOnClickListener {
            dismiss()
        }

    }
}