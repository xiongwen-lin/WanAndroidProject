package com.apemans.yruibusiness.utils

import android.content.Context
import com.apemans.quickui.superdialog.LoadingDialog

class LoadingDialogFactory : LoadingFactory() {
    private var loadingDialog: LoadingDialog? = null
    override fun showLoading(context: Context,message: String) {
        loadingDialog = LoadingDialog(context)
        loadingDialog!!.show()
    }

    override fun showMessage(message: String, delay: Int) {

    }

    override fun dismissLoading(delay: Int) {
        loadingDialog?.let {
            if (it.isShowing) {
                it.dismiss()
            }
        }
        loadingDialog = null
    }
}