package com.softsquared.template.kotlin.util

import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Handler
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import androidx.annotation.StringRes
import com.softsquared.template.kotlin.R
import kotlinx.android.synthetic.main.fragment_ask_dialog.view.*

class AskDialog(private val context: Context) {

    private val builder: AlertDialog.Builder by lazy {
        AlertDialog.Builder(context).setView(view)
    }

    private val view: View by lazy {
        View.inflate(context, R.layout.fragment_ask_dialog, null)
    }

    private var dialog: AlertDialog? = null

    // 터치 리스너 구현
    private val onTouchListener = View.OnTouchListener { _, motionEvent ->
        if (motionEvent.action == MotionEvent.ACTION_UP) {
            Handler().postDelayed({
                dismiss()
            }, 5)
        }
        false
    }

    fun setTitle(@StringRes titleId: Int): AskDialog {
        view.titleTextView.text = context.getText(titleId)
        return this
    }

    fun setTitle(title: CharSequence): AskDialog {
        view.titleTextView.text = title
        return this
    }

    fun setMessage(@StringRes messageId: Int): AskDialog {
        view.messageTextView.text = context.getText(messageId)
        return this
    }

    fun setMessage(message: CharSequence): AskDialog {
        view.messageTextView.text = message
        return this
    }

    fun setPositiveButton(@StringRes textId: Int, listener: (view: View) -> (Unit)): AskDialog {
        view.positiveButton.apply {
            text = context.getText(textId)
            setOnClickListener(listener)
            // 터치 리스너 등록
            setOnTouchListener(onTouchListener)
        }
        return this
    }

    fun setPositiveButton(text: CharSequence, listener: (view: View) -> (Unit)): AskDialog {
        view.positiveButton.apply {
            this.text = text
            setOnClickListener(listener)
            // 터치 리스너 등록
            setOnTouchListener(onTouchListener)
        }
        return this
    }

    fun setNegativeButton(@StringRes textId: Int, listener: (view: View) -> (Unit)): AskDialog {
        view.negativeButton.apply {
            text = context.getText(textId)
            this.text = text
            setOnClickListener(listener)
            // 터치 리스너 등록
            setOnTouchListener(onTouchListener)
        }
        return this
    }

    fun setNegativeButton(text: CharSequence, listener: (view: View) -> (Unit)): AskDialog {
        view.negativeButton.apply {
            this.text = text
            setOnClickListener(listener)
            // 터치 리스너 등록
            setOnTouchListener(onTouchListener)
        }
        return this
    }

    fun create() {
        dialog = builder.create()
    }
    fun show() {
        dialog = builder.create()
        dialog?.let{
            dialog!!.window?.setBackgroundDrawableResource(R.drawable.background_ask_dialog)

        }
        val params = WindowManager.LayoutParams()
        params.copyFrom(dialog?.window?.attributes)
        params.width = 1100
        params.height = WindowManager.LayoutParams.WRAP_CONTENT
        dialog?.show()
        val window = dialog?.window
        window?.attributes = params
        window?.setDimAmount(0.7f)
        window?.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
    }

    fun dismiss() {
        dialog?.dismiss()
    }
}