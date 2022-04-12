package com.apemans.yruibusiness.ui.toolbar

import android.app.Activity
import android.graphics.Color
import androidx.core.view.isVisible
import androidx.core.view.updateLayoutParams
import com.apemans.yruibusiness.databinding.LayoutToolbarBinding
import com.dylanc.longan.isLightStatusBar
import com.dylanc.longan.statusBarColor


/**
 * @author Dylan Cai
 */
class ToolbarAdapter(
    config: ToolbarConfig,
    private val fromFragment: Boolean = false
) : BaseToolbarAdapter<LayoutToolbarBinding>(config) {

  override fun onBindViewHolder(binding: LayoutToolbarBinding) {
    with(binding) {
      (root.context as? Activity)?.apply {
        statusBarColor = Color.WHITE
        if (fromFragment) {
          root.post { isLightStatusBar = true }
        } else {
          isLightStatusBar = true
        }
      }

      tvTitle.textSize = config.titleTextSize
      root.setBackgroundColor(config.backgroundColor)
      root.updateLayoutParams {
        height = config.height.toInt()
      }

      config.title?.let { tvTitle.text = it }
      config.titleRes.takeIf { it > 0 }?.let { tvTitle.setText(it) }
      when (config.navIconType) {
        com.apemans.yruibusiness.ui.toolbar.NavIconType.NONE -> {
          btnNav.isVisible = false
        }
        com.apemans.yruibusiness.ui.toolbar.NavIconType.NORMAL -> {
          btnNav.isVisible = true
          btnNav.setImageResource(config.leftIcon)
          btnNav.setOnClickListener(config.onLeftBtnClick)
        }
      }
      if (config.rightIcon > 0) {
        ivRightIcon.setImageResource(config.rightIcon)
        ivRightIcon.setOnClickListener(config.onRightBtnClick)
        ivRightIcon.isVisible = true
        tvRightText.isVisible = false
      } else if (config.rightTextRes > 0 || config.rightText != null) {
        if (config.rightTextRes > 0) {
          tvRightText.setText(config.rightTextRes)
        } else {
          tvRightText.text = config.rightText
        }
        tvRightText.setOnClickListener(config.onRightBtnClick)
        ivRightIcon.isVisible = false
        tvRightText.isVisible = true
      }
      //todo 完善标题栏功能
    }
  }
}