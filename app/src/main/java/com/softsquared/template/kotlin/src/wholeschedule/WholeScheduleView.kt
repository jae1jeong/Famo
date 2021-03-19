package com.softsquared.template.kotlin.src.wholeschedule

import com.softsquared.template.kotlin.src.wholeschedule.models.LatelyScheduleInquiryResponse

interface WholeScheduleView {

    //최근
    fun onGetLatelyScheduleInquirySuccess(response: LatelyScheduleInquiryResponse)
    fun onGetLatelyScheduleInquiryFail(message: String)
}