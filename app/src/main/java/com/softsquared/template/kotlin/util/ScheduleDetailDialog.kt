package com.softsquared.template.kotlin.src.util

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.GradientDrawable
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.google.gson.JsonElement
import com.softsquared.template.kotlin.R
import com.softsquared.template.kotlin.config.BaseResponse
import com.softsquared.template.kotlin.src.main.AddMemoService
import com.softsquared.template.kotlin.src.main.AddMemoView
import com.softsquared.template.kotlin.src.main.MainActivity
import com.softsquared.template.kotlin.src.main.models.DetailMemoResponse
import com.softsquared.template.kotlin.src.main.today.models.MemoItem
import java.time.LocalDate

class ScheduleDetailDialog(val context: Context): AddMemoView {
    private val dlg = Dialog(context)
    private lateinit var dialogContent:TextView
    private lateinit var dialogTitle:TextView
    private lateinit var modifyBtn: Button
    private lateinit var dialogDialogTopTitle:TextView
    private lateinit var closeBtn: ImageView
    private lateinit var listener: scheduleDetailDialogClickListener
    private lateinit var dialogCategoryName:TextView
    private var categoryName :String?= null

    interface scheduleDetailDialogClickListener{
        fun modifyBtnClicked()
    }

    fun start(memoItem: MemoItem,topTitle:String?){
        AddMemoService(this).tryGetDetailMemo(memoItem.id)
        // 다이얼로그 제목 안보이게 하기
        dlg.requestWindowFeature(Window.FEATURE_NO_TITLE)

        dlg.setContentView(R.layout.dialog_detail_schedule)
        dlg.window?.setBackgroundDrawableResource(R.drawable.background_ask_dialog)

        // 다이얼로그 수정하기 버튼 리스너
        modifyBtn = dlg.findViewById(R.id.dialog_detail_btn_modify)
        modifyBtn.setOnClickListener {
            listener.modifyBtnClicked()
            dlg.dismiss()
        }

        // 닫기 버튼 리스너
        closeBtn = dlg.findViewById(R.id.dialog_detail_btn_close)
        closeBtn.setOnClickListener {
            dlg.dismiss()
        }


        dlg.show()

    }


    fun setOnModifyBtnClickedListener(listener:()->Unit){
        this.listener = object:scheduleDetailDialogClickListener{
            override fun modifyBtnClicked() {
                MainActivity.categoryList.forEach {
                    if(it.text == categoryName){
                        it.selected = true
                        MainActivity.selectedCategoryId = it.id
                    }
                }

                MainActivity.categoryScheduleAdapter.notifyDataSetChanged()
                listener()
            }

        }
    }

    override fun onPostAddMemoSuccess(response: BaseResponse) {
    }

    override fun onPostAddMemoFailure(message: String) {
    }

    override fun onPatchMemoSuccess(response: BaseResponse) {
    }

    override fun onPatchMemoFailure(message: String) {
    }

    override fun onGetDetailMemoSuccess(response: DetailMemoResponse) {
        if (response.isSuccess) {
            when (response.code) {
                100 -> {
                    val responseJsonArray = response.data.asJsonArray
                    responseJsonArray.forEach {
                        val memoJsonObject = it.asJsonObject
                        val memoTitle = memoJsonObject.get("scheduleName").asString
                        val memoDate = memoJsonObject.get("scheduleForm").asString
                        val memoContentJsonElement: JsonElement? =
                                memoJsonObject.get("scheduleMemo")
                        var memoContent = ""
                        if (!memoContentJsonElement!!.isJsonNull) {
                            memoContent = memoContentJsonElement.asString
                        }
                        val memoCategoryNameJsonElement:JsonElement? = memoJsonObject.get("categoryName")
                        val memoCategoryColorJsonElement:JsonElement? = memoJsonObject.get("colorInfo")
                        var memoCategoryColor = ""


                        if(!memoCategoryNameJsonElement!!.isJsonNull){
                            categoryName = memoCategoryNameJsonElement.asString
                        }
                        // 다이얼로그 제목
                        dialogTitle = dlg.findViewById(R.id.dialog_detail_text_title)
                        dialogTitle.text = memoTitle

                        // 다이얼로그 내용
                        dialogContent = dlg.findViewById(R.id.dialog_detail_text_content)
                        dialogContent.text = memoContent

                        // 다이얼로그 상단
                        dialogDialogTopTitle = dlg.findViewById(R.id.dialog_detail_text_date)

                        dialogDialogTopTitle.text = "$memoDate ${CalendarConverter.dayToKoreanShortDayName(LocalDate.parse(memoDate).dayOfWeek.name)}요일"

                        dialogCategoryName = dlg.findViewById(R.id.dialog_detail_text_category)
                        if(!memoCategoryColorJsonElement!!.isJsonNull){
                            dialogCategoryName.text = memoCategoryNameJsonElement?.asString
                            memoCategoryColor = memoCategoryColorJsonElement.asString
                            val shape = GradientDrawable()
                            shape.cornerRadius = 180f
                            shape.setColorFilter(Color.parseColor(memoCategoryColor),PorterDuff.Mode.SRC_IN)
                            dialogCategoryName.setTextColor(Color.WHITE)
                            dialogCategoryName.background = shape
                        }else{
                            dialogCategoryName.visibility = View.GONE
                        }

                    }
                }
                else -> {
                }
            }
        }

    }

    override fun onGetDetailMemoFailure(message: String) {
    }
}