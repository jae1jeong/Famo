package com.makeus.jfive.famo.src.common

sealed class BottomSheetDialogState{
    object Hidden : BottomSheetDialogState()
    object Expand : BottomSheetDialogState()
    object Collapsed:BottomSheetDialogState()
}