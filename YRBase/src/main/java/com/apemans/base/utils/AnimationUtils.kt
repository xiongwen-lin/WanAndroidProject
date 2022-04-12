package com.apemans.base.utils

import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.view.animation.RotateAnimation


/***********************************************************
 * @Author : caro
 * @Date   : 12/17/20
 * @Func:
 * 动画工具箱
 *
 * @Description:
 *
 *
 ***********************************************************/
object AnimationUtils {

    const val ANIMATION_TRANSLATION_DURATION = 200L

    fun translateToRight(view: View, show: Boolean) {
        val x = if (show) 0f else view.width.toFloat()
        val alpha = if (show) 1f else 0f
        view.animate().translationX(-x)
            .alpha(alpha).duration =
            com.apemans.base.utils.AnimationUtils.ANIMATION_TRANSLATION_DURATION
    }

    fun translateToLeft(view: View, show: Boolean) {
        val x = if (show) 0f else view.width.toFloat()
        val alpha = if (show) 1f else 0f
        view.animate().translationX(x)
            .alpha(alpha).duration =
            com.apemans.base.utils.AnimationUtils.ANIMATION_TRANSLATION_DURATION
    }

    fun translateToBottom(view: View, show: Boolean) {
        val y = if (show) 0f else view.height.toFloat()
        val alpha = if (show) 1f else 0f
        view.animate().translationY(y)
            .alpha(alpha).duration =
            com.apemans.base.utils.AnimationUtils.ANIMATION_TRANSLATION_DURATION
    }

    enum class AnimationState {
        STATE_SHOW, STATE_HIDDEN
    }

    /**
     * 渐隐渐现动画
     * @param view 需要实现动画的对象
     * @param state 需要实现的状态
     * @param duration 动画实现的时长（ms）
     */
    fun showAndHiddenAnimation(view: View, state: com.apemans.base.utils.AnimationUtils.AnimationState, duration: Long) {
        var start = 0f
        var end = 0f
        if (state == com.apemans.base.utils.AnimationUtils.AnimationState.STATE_SHOW) {
            end = 1f
            view.visibility = View.VISIBLE
        } else if (state == com.apemans.base.utils.AnimationUtils.AnimationState.STATE_HIDDEN) {
            start = 1f
            view.visibility = View.INVISIBLE
        }
        val animation = AlphaAnimation(start, end)
        animation.duration = duration
        animation.fillAfter = true
        animation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {}
            override fun onAnimationRepeat(animation: Animation) {}
            override fun onAnimationEnd(animation: Animation) {
                view.clearAnimation()
            }
        })
        view.animation = animation
        animation.start()
    }

    fun createRotateAnimation(duration:Long =1000,startOffset:Long=10): RotateAnimation {
        val rotate = RotateAnimation(
            0f,
            360f,
            Animation.RELATIVE_TO_SELF,
            0.5f,
            Animation.RELATIVE_TO_SELF,
            0.5f
        )
        val lin = LinearInterpolator()
        rotate.interpolator = lin
        rotate.duration = duration //设置动画持续周期
        rotate.repeatCount = -1 //设置重复次数
        rotate.fillAfter = true //动画执行完后是否停留在执行完的状态
        rotate.startOffset = startOffset //执行前的等待时间

        return rotate
    }


}