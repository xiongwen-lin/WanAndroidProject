package com.apemans.quickui.picker

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.NumberPicker
import com.apemans.quickui.databinding.LayoutHourMinPickerBinding

/***********************************************************
 * 作者: zhengruidong@apemans.com
 * 日期: 2021/11/25 4:22 下午
 * 说明:
 *
 * 备注:
 *
 ***********************************************************/
const val HOUR_MIN_PICKER_TYPE_NORMAL = 1
const val HOUR_MIN_PICKER_TYPE_HALF_HOUR = 2
class HourMinPicker(context : Context, attrs : AttributeSet) : LinearLayout(context, attrs) {

    private val HOUR_LENGTH_OF_DAY = 24
    private val MIN_LENGTH_OF_HOUR = 60
    private val MIN_LENGTH_OF_HALF_HOUR = 2

    private val binding = LayoutHourMinPickerBinding.inflate(LayoutInflater.from(context), this, true)

    init {
        init()
    }

    fun obtainHourPicker() : NumberPicker {
        return binding.hourMinPickerHour
    }

    fun obtainMinPicker() : NumberPicker {
        return binding.hourMinPickerMin
    }

    fun setupHourMinPickerType(type : Int) {
        when (type) {
            HOUR_MIN_PICKER_TYPE_HALF_HOUR -> {
                initHourPicker(type, obtainHourPicker(), (HOUR_LENGTH_OF_DAY + 1))
                initMinPicker(type, obtainMinPicker(), MIN_LENGTH_OF_HALF_HOUR)
            }
            else -> {
                initHourPicker(type, obtainHourPicker(), HOUR_LENGTH_OF_DAY)
                initMinPicker(type, obtainMinPicker(), MIN_LENGTH_OF_HOUR)
            }
        }
    }

    private fun init() {
    }

    private fun initHourPicker(type: Int, picker: NumberPicker, valueSize: Int) {
        if (valueSize < 1) {
            return
        }
        val values = Array<String>(valueSize) { it ->
            println("-->> debug initHourPicker $it")
            it.toString()
        }
        initPick(picker, values)
    }

    private fun initMinPicker(type: Int, picker: NumberPicker, valueSize: Int) {
        if (valueSize < 1) {
            return
        }
        val values = Array<String>(valueSize) { it ->
            println("-->> debug initHourPicker HOUR_MIN_PICKER_TYPE_HALF_HOUR index $it")
            when (type) {
                HOUR_MIN_PICKER_TYPE_HALF_HOUR -> {
                    if (it == 1) {
                        "30"
                    } else {
                        "0"
                    }
                }
                else -> {
                    it.toString()
                }
            }
        }
        initPick(picker, values)
    }

    private fun initPick(picker: NumberPicker, values: Array<String>) {
        if (values.isEmpty()) {
            return
        }
        picker.displayedValues = values
        picker.minValue = 0
        picker.maxValue = values.size - 1
        picker.value = 0
    }
}