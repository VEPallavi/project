package com.utalli.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.utalli.R


class CustomBottomSheetDialog : BottomSheetDialogFragment(){


    fun getInstance(): CustomBottomSheetDialog {
        return CustomBottomSheetDialog()
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view = inflater.inflate(R.layout.layout_bottom_sheet, container, false)
        return view
    }


}