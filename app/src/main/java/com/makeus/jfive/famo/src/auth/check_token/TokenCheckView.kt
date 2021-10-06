package com.makeus.jfive.famo.src.auth.check_token

interface TokenCheckView {
    fun onTokenCheckSuccess()
    fun onTokenCheckFailure()
}