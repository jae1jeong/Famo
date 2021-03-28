package com.softsquared.template.kotlin.src.main.schedulefind.adapter

import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.JsonElement
import com.softsquared.template.kotlin.R
import com.softsquared.template.kotlin.config.BaseResponse
import com.softsquared.template.kotlin.src.main.AddMemoService
import com.softsquared.template.kotlin.src.main.AddMemoView
import com.softsquared.template.kotlin.src.main.models.DetailMemoResponse
import com.softsquared.template.kotlin.src.main.schedulefind.models.WholeScheduleBookmarkData


open class ScheduleBookmarkAdapter(var bookmarkListWhole: ArrayList<WholeScheduleBookmarkData>,
    myScheduleCategoryRecyclerView: IScheduleCategoryRecyclerView) :
    RecyclerView.Adapter<ScheduleBookmarkAdapter.ScheduleBookmarkHolder>(), AddMemoView {

    private var iScheduleCategoryRecyclerView: IScheduleCategoryRecyclerView? = null

    private var mCallback: OnItemClick? = null

    private var context: Context? = null

    interface OnItemClick {
        fun onClick(value: String?)
    }

    open fun RecycleAdapter(context: Context, listener: OnItemClick) {
        this.context = context
        this.mCallback = listener
    }

    init {
        Log.d("TAG", "ScheduleCategoryAdapter: init() called ")
        this.iScheduleCategoryRecyclerView = myScheduleCategoryRecyclerView
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScheduleBookmarkHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recyclerview_schedule_find_bookmark_item, parent, false)
        view.setOnClickListener {
            AddMemoService(this@ScheduleBookmarkAdapter).tryGetDetailMemo(
                98
            )

        }
        return ScheduleBookmarkHolder(view)
    }

    override fun getItemCount(): Int = bookmarkListWhole.size

    override fun onBindViewHolder(holder: ScheduleBookmarkHolder, position: Int) {
        holder.scheduleName.text = bookmarkListWhole[position].scheduleName
        holder.scheduleDate.text = bookmarkListWhole[position].scheduleDate
        holder.color.setColorFilter(Color.parseColor(bookmarkListWhole[position].colorInfo))
    }

    class ScheduleBookmarkHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val scheduleName = itemView.findViewById<TextView>(R.id.recycler_part_title)
        val scheduleDate = itemView.findViewById<TextView>(R.id.recycler_part_times)
        val color = itemView.findViewById<ImageView>(R.id.recycler_part_color)

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
            when (response.code) {
                100 -> {
                iScheduleCategoryRecyclerView!!.onScheduleDetail()
                }

//                var iSearchRecyclerViewInterface: IScheduleCategoryRecyclerView


//                100 -> {
//                    val responseJsonArray = response.data.asJsonArray
//                    responseJsonArray.forEach {
//                        val memoJsonObject = it.asJsonObject
//                        val memoTitle = memoJsonObject.get("scheduleName").asString
//                        val memoDate = memoJsonObject.get("scheduleDate").asString
//                        val memoContentJsonElement: JsonElement? =
//                            memoJsonObject.get("scheduleMemo")
//                        var memoContent = ""
//                        if (!memoContentJsonElement!!.isJsonNull) {
//                            memoContent = memoContentJsonElement.asString
//                        }
//
//                        val scheduleTime: String? = memoJsonObject.get("scheduleTime").asString
//                        val memoColor = memoJsonObject.get("colorInfo").asString
//                        setFormBottomSheetDialog(memoTitle, memoContent, memoDate)
//                    }
//                }
//                else -> {
////                    showCustomToast(response.message.toString())
//                }
            }
    }

    override fun onGetDetailMemoFailure(message: String) {
    }


}