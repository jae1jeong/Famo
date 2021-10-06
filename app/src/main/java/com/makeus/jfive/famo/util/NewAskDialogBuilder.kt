package com.makeus.jfive.famo.util

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.WindowManager
import com.makeus.jfive.famo.databinding.FragmentAskDialogBinding

class NewAskDialogBuilder(
    private val context: Context,
    private val builder: NewAskDialogBuilder.Builder
) {
    private val mDialog = Dialog(context)
    private lateinit var _binding: FragmentAskDialogBinding
    private val binding get() = _binding


    fun setUp() {
        _binding = FragmentAskDialogBinding.inflate(mDialog.layoutInflater)
        mDialog.setContentView(binding.root)
        val params = mDialog.window?.attributes
        params?.width = WindowManager.LayoutParams.MATCH_PARENT
        params?.height = WindowManager.LayoutParams.WRAP_CONTENT
        mDialog.window?.attributes = params
        mDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }
    fun show(){
        mDialog.show()
    }

    fun setPositiveBtn(positive: String,positiveBtnListener: () -> Unit) {
        binding.positiveButton.text = positive
        binding.positiveButton.setOnClickListener {
            positiveBtnListener()
            mDialog.dismiss()
        }
    }

    fun setNegativeBtn(negative: String,negativeListener: () -> Unit) {
        binding.negativeButton.text = negative
        binding.negativeButton.setOnClickListener {
            negativeListener()
            mDialog.dismiss()
        }
    }

    fun setTitle(title: String) {
        binding.titleTextView.text = title
    }

    fun setMessage(message: String) {
        binding.messageTextView.text = message
    }


    class Builder(private var context: Context) {
        private var positiveText: String = ""
        private var negativeText: String = ""
        private var positiveBtnListener: () -> Unit = {}
        private var title: String = ""
        private var message: String = ""
        private lateinit var newAskBuilder:NewAskDialogBuilder
        private var negativeBtnListener:()-> Unit = {}

        fun setPositiveBtn(positive: String, setPositiveBtnListener: () -> Unit) : Builder{
            this.positiveText = positive
            this.positiveBtnListener = setPositiveBtnListener
            return this
        }

        fun setNegativeBtn(negative: String,setNegativeBtnListener:()->Unit):Builder {
            this.negativeText = negative
            this.negativeBtnListener = setNegativeBtnListener
            return this
        }

        fun setTitle(title: String):Builder {
            this.title = title
            return this
        }

        fun setMessage(message: String):Builder {
            this.message = message
            return this
        }

        fun build():Builder{
            newAskBuilder = NewAskDialogBuilder(context, this)
            newAskBuilder.setUp()
            newAskBuilder.setMessage(this.message)
            newAskBuilder.setTitle(this.title)
            newAskBuilder.setNegativeBtn(this.negativeText,negativeBtnListener)
            newAskBuilder.setPositiveBtn(this.positiveText,positiveBtnListener)
            return this
        }
        fun show(){
            this.newAskBuilder.show()
        }
    }


}