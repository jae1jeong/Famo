package com.makeus.jfive.famo.src.wholeschedule.lately

import com.makeus.jfive.famo.src.wholeschedule.models.LatelyScheduleInquiryResponse

interface WholeLatelyScheduleView {

    //최근
    fun onGetLatelyScheduleInquirySuccess(response: LatelyScheduleInquiryResponse)
    fun onGetLatelyScheduleInquiryFail(message: String)
}