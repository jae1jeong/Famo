package com.makeus.jfive.famo.src.mypageedit

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.makeus.jfive.famo.R
import kotlinx.android.synthetic.main.fragment_my_page_edit_bottom_sheet_dialog.*
import java.lang.ClassCastException

class MyPageEditBottomSheetDialog:BottomSheetDialogFragment() {
    lateinit var bottomSheetDialogInterface:MyPageEditBottomSheetDialogInterface

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_my_page_edit_bottom_sheet_dialog,container,false)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        my_page_edit_bottom_sheet_text_select_from_gallery.setOnClickListener {
            Log.d("TAG", "onViewCreated: 갤러리에서 선택")
            bottomSheetDialogInterface.selectImageGallery()
            dismiss()
        }
        my_page_edit_bottom_sheet_text_delete_profile.setOnClickListener {
            Log.d("TAG", "onViewCreated: 프로필 삭제")
            bottomSheetDialogInterface.profileDelete()
            dismiss()
        }


    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try{
            bottomSheetDialogInterface = context as MyPageEditBottomSheetDialogInterface
        }catch (e:ClassCastException){
            Log.e("TAG", "onAttach: $e", )
        }
    }
}