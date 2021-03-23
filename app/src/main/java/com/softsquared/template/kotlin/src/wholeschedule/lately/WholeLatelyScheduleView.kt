package com.softsquared.template.kotlin.src.wholeschedule.lately

import com.softsquared.template.kotlin.src.wholeschedule.models.LatelyScheduleInquiryResponse

interface WholeLatelyScheduleView {

    //최근
    fun onGetLatelyScheduleInquirySuccess(response: LatelyScheduleInquiryResponse)
    fun onGetLatelyScheduleInquiryFail(message: String)
}