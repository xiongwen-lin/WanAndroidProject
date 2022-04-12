package com.apemans.custom.util

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.view.*
import android.widget.Button
import android.widget.TextView
import com.apemans.custom.R
import com.dylanc.longan.application


class DialogUtils(context: Context) : Dialog(context) {

    private fun showConfirmWithSubMsgDialog(ctx : Context, title : String, message : String,
                                            leftBtn : String, rightBtn : String, listener : OnClickButtonListener
    ) : AlertDialog? {
        return showDialog(ctx, title, message, leftBtn, rightBtn, listener)
    }

    fun showConfirmWithSubMsgDialog(ctx : Context, title : Int, message : Int,
                                    leftBtn : Int, rightBtn : Int, listener : OnClickButtonListener
    ) : AlertDialog? {
        return showConfirmWithSubMsgDialog(ctx, ctx.getString(title), ctx.getString(message),  ctx.getString(leftBtn),ctx.getString(rightBtn) , listener)
    }


    private fun showDialog(ctx : Context, title : String, message : String,
                           leftBtn : String, rightBtn : String, listener : OnClickButtonListener
    ) : AlertDialog?{
        val view : View = LayoutInflater.from(ctx).inflate(R.layout.dialog_confirm_with_submessage, null)
        val dialog : AlertDialog? = AlertDialog.Builder(ctx,R.style.transparent_background_dialog)
            .setView(view).create()
        var titleString = view.findViewById<TextView>(R.id.tvTitle)
        titleString.text = title

        var textString = view.findViewById<TextView>(R.id.tvMessage)
        textString.text = message

        var btnLeft = view.findViewById<Button>(R.id.btnCancel)
        btnLeft.text = leftBtn

        btnLeft.setOnClickListener {
            listener.onClickLeft()
            dialog?.dismiss()
        }

        var btnRight = view.findViewById<Button>(R.id.btnOk)
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
