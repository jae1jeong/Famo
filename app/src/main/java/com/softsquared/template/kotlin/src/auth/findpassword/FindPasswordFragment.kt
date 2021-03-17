package com.softsquared.template.kotlin.src.auth.findpassword

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.softsquared.template.kotlin.R
import com.softsquared.template.kotlin.config.BaseFragment
import com.softsquared.template.kotlin.databinding.FragmentFindPasswordBinding

class FindPasswordFragment:BaseFragment<FragmentFindPasswordBinding>(FragmentFindPasswordBinding::bind, R.layout.fragment_find_password) {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return super.onCreateView(inflater, container, savedInstanceState)
    }
}