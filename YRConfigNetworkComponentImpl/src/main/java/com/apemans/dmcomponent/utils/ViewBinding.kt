package com.apemans.dmcomponent.utils

import androidx.core.view.isVisible
import com.apemans.dmcomponent.databinding.DeviceLayoutStepHeaderBinding

/**
 * @author Dylan Cai
 */

var DeviceLayoutStepHeaderBinding.step: Int
    get() = when {
        !root.isVisible -> 0
        dot1.isSelected && !dot2.isSelected && !dot3.isSelected -> 1
        dot1.isSelected && dot2.isSelected && !dot3.isSelected -> 2
        dot1.isSelected && dot2.isSelected && dot3.isSelected -> 3
        else -> throw IllegalStateException()
    }
    set(value) {
        when (value) {
            0 -> {
                root.isVisible = false
            }
            1 -> {
                root.isVisible = true
                dot1.isSelected = true
                dot2.isSelected = false
                dot3.isSelected = false
            }
            2 -> {
                root.isVisible = true
                dot1.isSelected = true
                dot2.isSelected = true
                dot3.isSelected = false
            }
            3 -> {
                root.isVisible = true
                dot1.isSelected = true
                dot2.isSelected = true
                dot3.isSelected = true
            }
            else -> throw IllegalStateException()
        }
    }