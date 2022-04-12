package com.apemans.quickui.superdialog

import android.content.Context
import com.apemans.quickui.R
import com.apemans.quickui.utils.DisplayHelper.dp2px
import com.apemans.quickui.utils.DisplayHelper.dpToPx
import com.apemans.quickui.utils.screenWidthPx
import com.google.android.material.button.MaterialButton
import com.ly.genjidialog.extensions.convertListenerFun
import com.ly.genjidialog.extensions.newGenjiDialog
import com.ly.genjidialog.other.DialogGravity

/***********************************************************
 * 作者: caro
 * 邮箱: 1025807062@qq.com
 * 日期: 2021/10/21 16:08
 * 说明:
 *
 * 备注:
 *
 ***********************************************************/
class AvatarSelectDialog {
    var take_photo_text: String? = null
    var select_photo_text: String? = null
    var cancel_text: String? = null

    /**
     * 配置你的Text文本以适应翻译
     */
    fun configTextValues(take_photo_text: String = "take a photo", select_photo_text: String = "from album", cancel_text: String = "cancel"): AvatarSelectDialog {
        this.take_photo_text = take_photo_text
        this.select_photo_text = select_photo_text
        this.cancel_text = cancel_text
        return this
    }

    fun show(context: Context, manager: androidx.fragment.app.FragmentManager, cancel: () -> Unit, takePhoto: () -> Unit, select: () -> Unit) {
        newGenjiDialog { genjiDialog ->
            layoutId = R.layout.avatar_select_dilaog
            width = context.screenWidthPx - dpToPx(10) * 2
            //设置竖直方向的margin-margin底部10dp
            verticalMargin = dp2px(context, 10).toFloat()
            convertListenerFun { holder, dialog ->
                val select_photo = holder.getView<MaterialButton>(R.id.select_photo)
                val take_photo = holder.getView<MaterialButton>(R.id.take_photo)
                val cancel_btn = holder.getView<MaterialButton>(R.id.cancel)

                //配置文本多语言

                take_photo?.text = take_photo_text ?: "take a photo"
                select_photo?.text = select_photo_text ?: "from album"
                cancel_btn?.text = cancel_text ?: "cancel"

                holder.setOnClickListener(R.id.cancel) {
                    //Toast.makeText(context, "I click cancel", Toast.LENGTH_SHORT).show()
                    cancel()
                    dialog.dismissAllowingStateLoss()
                }
                holder.setOnClickListener(R.id.select_photo) {
                    //Toast.makeText(context, "I click select_photo", Toast.LENGTH_SHORT).show()
                    takePhoto()
                    dialog.dismissAllowingStateLoss()
                }
                holder.setOnClickListener(R.id.take_photo) {
                    //Toast.makeText(context, "I click take_photo", Toast.LENGTH_SHORT).show()
                    select()
                    dialog.dismissAllowingStateLoss()
                }
            }
        }.showOnWindow(manager, DialogGravity.CENTER_BOTTOM)
    }
}