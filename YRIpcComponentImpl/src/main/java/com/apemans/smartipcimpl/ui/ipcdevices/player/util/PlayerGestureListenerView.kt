package com.apemans.smartipcimpl.ui.ipcdevices.player.util

import android.content.pm.ActivityInfo
import android.view.View
import android.widget.ImageView
import com.apemans.smartipcimpl.R
import com.apemans.smartipcimpl.constants.*
import com.apemans.smartipcimpl.ui.ipcdevices.player.view.guideview.PanelComponent
import com.nooie.common.utils.graphics.DisplayUtil
import com.nooie.sdk.media.NooieMediaPlayer
import com.nooie.sdk.media.listener.PlayerGestureListener

/**
 * @Author:QCoder
 * @Description: 播放器手势控制,代码抽离
 * @Date: 2021/12/27-15:52
 */
class PlayerGestureListenerView(
    var isSupportH: Boolean,
    var isSupportV: Boolean,
    var ivDirectionControlBg: ImageView,
    var ivGestureLeftArrow: ImageView,
    var ivGestureTopArrow: ImageView,
    var ivGestureRightArrow: ImageView,
    var ivGestureBottomArrow: ImageView
) : PlayerGestureListener {


    override fun onMoveLeft(player: NooieMediaPlayer?) {
        displayGestureGuideView(true, GESTURE_MOVE_LEFT)
    }

    override fun onMoveRight(player: NooieMediaPlayer?) {
        displayGestureGuideView(true, GESTURE_MOVE_RIGHT)
    }

    override fun onMoveUp(player: NooieMediaPlayer?) {
        displayGestureGuideView(true, GESTURE_MOVE_TOP)
    }

    override fun onMoveDown(player: NooieMediaPlayer?) {
        displayGestureGuideView(true, GESTURE_MOVE_BOTTOM)
    }

    override fun onTouchUp(player: NooieMediaPlayer?) {
        displayGestureGuideView(false, GESTURE_TOUCH_UP)
    }

    override fun onTouchDown(player: NooieMediaPlayer?) {
        displayGestureGuideView(true, GESTURE_TOUCH_DOWN)
    }

    override fun onSingleClick(player: NooieMediaPlayer?) {

    }

    override fun onDoubleClick(player: NooieMediaPlayer?) {

    }



    /**
     * 手势
     */
    private fun displayGestureGuideView(show: Boolean, gestureType: Int) {
         if (!isSupportH &&!isSupportV){ //不支持左右上下手势
             return
         }
        if (!show) {
            ivDirectionControlBg?.visibility = View.GONE
            ivGestureLeftArrow?.visibility = View.GONE
            ivGestureTopArrow?.visibility = View.GONE
            ivGestureRightArrow?.visibility = View.GONE
            ivGestureBottomArrow?.visibility = View.GONE
            return
        }
        when (gestureType) {
            GESTURE_TOUCH_DOWN -> {
                ivDirectionControlBg?.visibility = View.VISIBLE
                ivGestureLeftArrow?.setImageResource(R.drawable.gesture_left_arrow)
                ivGestureTopArrow?.setImageResource(R.drawable.gesture_left_arrow)
                ivGestureRightArrow?.setImageResource(R.drawable.gesture_left_arrow)
                ivGestureBottomArrow?.setImageResource(R.drawable.gesture_left_arrow)
                ivGestureLeftArrow?.visibility = if (isSupportH) View.VISIBLE else View.GONE
                ivGestureTopArrow?.visibility = if (isSupportV) View.VISIBLE else View.GONE
                ivGestureRightArrow?.visibility = if (isSupportH) View.VISIBLE else View.GONE
                ivGestureBottomArrow?.visibility = if (isSupportV) View.VISIBLE else View.GONE
            }
            GESTURE_TOUCH_UP -> {
                ivDirectionControlBg?.visibility = View.GONE
                ivGestureLeftArrow?.setImageResource(R.drawable.gesture_left_arrow)
                ivGestureTopArrow?.setImageResource(R.drawable.gesture_left_arrow)
                ivGestureRightArrow?.setImageResource(R.drawable.gesture_left_arrow)
                ivGestureBottomArrow?.setImageResource(R.drawable.gesture_left_arrow)
                ivGestureLeftArrow?.visibility = View.GONE
                ivGestureTopArrow?.visibility = View.GONE
                ivGestureRightArrow?.visibility = View.GONE
                ivGestureBottomArrow?.visibility = View.GONE
            }
            GESTURE_MOVE_LEFT -> {
                ivGestureLeftArrow?.setImageResource(R.drawable.gesture_left__active_arrow)
                ivGestureLeftArrow?.visibility = if (isSupportH) View.VISIBLE else View.GONE
                ivGestureTopArrow?.visibility = if (isSupportV) View.VISIBLE else View.GONE
                ivGestureRightArrow?.visibility = if (isSupportH) View.VISIBLE else View.GONE
                ivGestureBottomArrow?.visibility = if (isSupportV) View.VISIBLE else View.GONE
            }
            GESTURE_MOVE_TOP -> {
                ivGestureTopArrow?.setImageResource(R.drawable.gesture_left__active_arrow)
                ivGestureLeftArrow?.visibility = if (isSupportH) View.VISIBLE else View.GONE
                ivGestureTopArrow?.visibility = if (isSupportV) View.VISIBLE else View.GONE
                ivGestureRightArrow?.visibility = if (isSupportH) View.VISIBLE else View.GONE
                ivGestureBottomArrow?.visibility = if (isSupportV) View.VISIBLE else View.GONE
            }
            GESTURE_MOVE_RIGHT -> {
                ivGestureRightArrow?.setImageResource(R.drawable.gesture_left__active_arrow)
                ivGestureLeftArrow?.visibility = if (isSupportH) View.VISIBLE else View.GONE
                ivGestureTopArrow?.visibility = if (isSupportV) View.VISIBLE else View.GONE
                ivGestureRightArrow?.visibility = if (isSupportH) View.VISIBLE else View.GONE
                ivGestureBottomArrow?.visibility = if (isSupportV) View.VISIBLE else View.GONE
            }
            GESTURE_MOVE_BOTTOM -> {
                ivGestureBottomArrow?.setImageResource(R.drawable.gesture_left__active_arrow)
                ivGestureLeftArrow?.visibility = if (isSupportH) View.VISIBLE else View.GONE
                ivGestureTopArrow?.visibility = if (isSupportV) View.VISIBLE else View.GONE
                ivGestureRightArrow?.visibility = if (isSupportH) View.VISIBLE else View.GONE
                ivGestureBottomArrow?.visibility = if (isSupportV) View.VISIBLE else View.GONE
            }
        }
    }
}