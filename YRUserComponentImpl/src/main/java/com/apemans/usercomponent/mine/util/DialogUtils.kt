package com.apemans.usercomponent.mine.util

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.view.*
import android.widget.Button
import android.widget.TextView
import com.dylanc.longan.application
import com.apemans.usercomponent.R

class DialogUtils(context: Context) : Dialog(context) {

    fun showLogoutDialog(ctx : Context, title : String, message : String,
                        leftBtn : String, rightBtn : String, listener : OnClickButtonListener) : AlertDialog? {
        return showDialog(ctx, title, message, leftBtn, rightBtn, listener)
    }

    fun showConfirmDialog(ctx : Context, title : String, listener : OnClickButtonListener) : AlertDialog? {
        return showDialog(ctx, title, "", application.resources.getString(R.string.cancel), application.resources.getString(R.string.confirm_upper), listener)
    }

    fun showForceLogoutDialog(ctx : Context, title : String, message : String, listener : OnClickConfirmButtonListener) : AlertDialog? {
        return showConfirmDialog(ctx, title, message, listener)
    }

    private fun showConfirmDialog(ctx : Context, title : String, message : String, listener : OnClickConfirmButtonListener) : AlertDialog?{
        val view : View = LayoutInflater.from(ctx).inflate(R.layout.mine_dialog_information, null)
        val dialog : AlertDialog? = AlertDialog.Builder(ctx,R.style.transparent_background_dialog)
            .setView(view).create()
        var titleString = view.findViewById<TextView>(R.id.tvTitle)
        titleString.text = title

        var textString = view.findViewById<TextView>(R.id.tvMessage)
        textString.text = message

        var btnOk = view.findViewById<Button>(R.id.btnOk)
        btnOk.setOnClickListener {
            listener.onConfirmClick()
            dialog?.dismiss()
        }

        dialog?.show()
        return dialog
    }

    private fun showDialog(ctx : Context, title : String, message : String,
    leftBtn : String, rightBtn : String, listener : OnClickButtonListener) : AlertDialog?{
        val view : View = LayoutInflater.from(ctx).inflate(R.layout.mine_dialog_logout_layout, null)
        val dialog : AlertDialog? = AlertDialog.Builder(ctx,R.style.transparent_background_dialog)
            .setView(view).create()
        var titleString = view.findViewById<TextView>(R.id.tvTitle)
        titleString.text = title

        var textString = view.findViewById<TextView>(R.id.tvText)
        textString.text = message

        var btnLeft = view.findViewById<Button>(R.id.btnLeft)
        btnLeft.text = leftBtn

        btnLeft.setOnClickListener {
            listener.onClickLeft()
            dialog?.dismiss()
        }

        var btnRight = view.findViewById<Button>(R.id.btnRight)
        btnRight.text = rightBtn

        btnRight.setOnClickListener {
            listener.onClickRight()
            dialog?.dismiss()
        }

        dialog?.show()
        return dialog
    }

    interface OnClickButtonListener {
        fun onClickLeft()
        fun onClickRight()
    }

    interface OnClickConfirmButtonListener {
        fun onConfirmClick()
    }


}
