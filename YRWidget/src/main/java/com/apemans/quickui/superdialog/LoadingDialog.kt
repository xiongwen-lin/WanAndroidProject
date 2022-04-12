package com.apemans.quickui.superdialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Gravity
import com.apemans.quickui.R

class LoadingDialog(context: Context) : Dialog(context, R.style.alert_dialog_style) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.theme_dialog_loading)
        window?.setGravity(Gravity.CENTER)
        setCanceledOnTouchOutside(false)
        setCancelable(false)
    }
}