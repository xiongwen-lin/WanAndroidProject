package com.apemans.quickui.preference

/***********************************************************
 * @Author : caro
 * @Date   : 1/9/21
 * @Func:
 *
 *
 * @Description:
 * @ItemFunction
 *
 ***********************************************************/
interface PreferenceSettingEventCallback {
    fun onSwitchEvent(
        position: Int,
        isChecked: Boolean,
        preferenceItem: PreferenceBean,
        supportParams: List<PreferenceBean.Param>?,
        preferenceView: PreferenceView,
        fromUser:Boolean
    ){}

    fun onClick(
        position: Int,
        curValue: String?,
        preferenceItem: PreferenceBean,
        supportParams: List<PreferenceBean.Param>?,
        preferenceView: PreferenceView
    )

    fun onLongPress(
        position: Int,
        curValue: String?,
        preferenceItem: PreferenceBean,
        supportParams: List<PreferenceBean.Param>?,
        preferenceView: PreferenceView
    ){}

    fun onSeekBar(
        position: Int,
        curValue: Int,
        preferenceItem: PreferenceBean,
        process: Int,
        preferenceBar: PreferenceBar
    ){}
}