package com.apemans.quickui.preference

import android.content.Context
import android.text.TextUtils
import android.view.View
import android.widget.SeekBar
import androidx.recyclerview.widget.RecyclerView

/***********************************************************
 * @Author : caro
 * @Date   : 1/8/21
 * @Func:
 * Seek Bar调节ViewHolder
 *
 * @Description:
 *
 *
 ***********************************************************/
class PreferenceBarViewHolder<Event : PreferenceSettingEventCallback>(
    private val preferenceBar: PreferenceBar,
    private val onSettingFuncEventClick: Event? = null,
) : RecyclerView.ViewHolder(preferenceBar as View) {
    private val context: Context = itemView.context

    fun bind(preferenceItem: PreferenceBean) {
        //设置设置项名称
        val settingName = preferenceItem.name
        preferenceBar.setPreferenceName(settingName)
        //设置项当前值-设置项当前参数值，暂不参与翻译
        val curValue = preferenceItem.value
        val supportParams: List<PreferenceBean.Param> = preferenceItem.params
        if (!TextUtils.isEmpty(curValue)) {
            preferenceBar.setDegree(curValue!!)
            preferenceBar.seekBar.progress = curValue.toInt()
        }
        if (preferenceItem.iconDrawable != null) {
            preferenceBar.setPreferenceIcon(preferenceItem.iconDrawable!!)
        }

        var selectProcess = curValue!!.toInt()

        preferenceBar.setSeekBarChange(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(
                seekBar: SeekBar,
                progress: Int,
                fromUser: Boolean,
            ) {
                selectProcess = progress
                //selectProcess = progress+preferenceBar.min
                preferenceBar.setProgress(selectProcess)
                preferenceBar.setDegree(selectProcess.toString())

            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                //Log.i("BarViewHolder", "UI_BRIGHTNESS: "+Thread.currentThread().name)
                onSettingFuncEventClick?.onSeekBar(
                    absoluteAdapterPosition,
                    curValue.toInt(),
                    preferenceItem,
                    selectProcess,
                    preferenceBar
                )
            }

        })
    }
}