package com.apemans.smartipcimpl.ui.ipcdevices.player.util

import android.content.Context
import android.media.AudioManager
import com.apemans.smartipcapi.webapi.PresetPointConfigure
import com.apemans.smartipcimpl.constants.PRESET_POINT_FIRST_POSITION
import com.apemans.smartipcimpl.constants.PRESET_POINT_MAX_LEN
import com.nooie.common.bean.PhoneVolume
import com.nooie.common.utils.collection.CollectionUtil
import com.nooie.common.utils.log.NooieLog
import com.nooie.common.utils.tool.SystemUtil
import com.tencent.mmkv.MMKV
import java.util.*

/**
 * @Author:dongbeihu
 * @Description:
 * @Date: 2021/12/17-9:58
 */
object PlayFunctionHelper {
    private val DEFAULT_AUDIO_VOLUME_PERCENT = 0.7f

    private var mPhoneVolume: PhoneVolume? = null     //保存手机原先音量
    /**
     * 调整手机声音
     */
    fun changePhoneVolume( context :Context,isOpenAudio: Boolean) {

        try {
            if (isOpenAudio) {
                val phoneVolume = SystemUtil.getPhoneVolume(context, AudioManager.STREAM_MUSIC)
                val isNeedToRaiseVolume =
                    mPhoneVolume == null && phoneVolume != null && phoneVolume.volume > 0 && phoneVolume.maxVolume > 0 && phoneVolume.volume
                        .toFloat() / phoneVolume.maxVolume < DEFAULT_AUDIO_VOLUME_PERCENT
                if (isNeedToRaiseVolume) {
                    mPhoneVolume = SystemUtil.getPhoneVolume(context, AudioManager.STREAM_MUSIC)
                    SystemUtil.setPhoneVolumeByPercent(context, AudioManager.STREAM_MUSIC, DEFAULT_AUDIO_VOLUME_PERCENT, 0  )
                }
            } else if (mPhoneVolume != null) {
                val volumeIndex = if (mPhoneVolume?.volume!! < 0) 1 else mPhoneVolume!!.volume
                SystemUtil.setPhoneVolume(context, AudioManager.STREAM_MUSIC, volumeIndex, 0)
                mPhoneVolume = null
            }
        } catch (e: Exception) {
            NooieLog.printStackTrace(e)
        }
    }


    /**
     * 记忆用户最后一次喇叭点击状态
     *
     * @param deviceId
     * @param open     当前喇叭的状态,返回点击喇叭后的结果（即与当前状态相反）
     */
     fun saveAudioState(deviceId: String?, open: Boolean) {
        val mmkv =  MMKV.defaultMMKV()
        val audioStateKey  =deviceId +"AudioState"
        mmkv.putBoolean(audioStateKey,open)
    }

    fun getAudioState(deviceId: String?): Boolean {
        val mmkv =  MMKV.defaultMMKV()
        val audioStateKey  =deviceId +"AudioState"
        return  mmkv.getBoolean(audioStateKey,false) //老项目，默认是关闭声音
    }

    fun saveTalkGuide(deviceId: String?, open: Boolean) {
        val mmkv =  MMKV.defaultMMKV()
        val talkGuideKey  =deviceId +"TalkGuide"
        mmkv.putBoolean(talkGuideKey,open)
    }

    fun getTalkGuide(deviceId: String?): Boolean {
        val mmkv =  MMKV.defaultMMKV()
        val talkGuideKey  =deviceId +"TalkGuide"
        return  mmkv.getBoolean(talkGuideKey,true)
    }

    fun getGestureGuideView(deviceId: String?,user :String): Boolean {
        val mmkv =  MMKV.defaultMMKV()
        val talkGuideKey  =deviceId +user+"GestureGuide"
        return  mmkv.getBoolean(talkGuideKey,true)
    }

    fun saveGestureGuideView(deviceId: String?, user :String,isSHow: Boolean) {
        val mmkv =  MMKV.defaultMMKV()
        val talkGuideKey  =deviceId +user+"GestureGuide"
        mmkv.putBoolean(talkGuideKey,isSHow)
    }


    fun getCorrectPresetPointPosition( position: Int,  presetPointConfigures: List<PresetPointConfigure?> ): Int {
        var position = position
        if (CollectionUtil.isEmpty(presetPointConfigures)) {
            return PRESET_POINT_FIRST_POSITION
        }
        if (!checkPresetPointValid(position)) {
            position = PRESET_POINT_MAX_LEN
        }
        val presetPointPositions: MutableList<Int?> = ArrayList()
        for (presetPointConfigure in presetPointConfigures) {
            if (presetPointConfigure != null && checkPresetPointValid( presetPointConfigure?.position )) {
                presetPointPositions.add(presetPointConfigure?.position)
            }
        }
        if (CollectionUtil.isEmpty(presetPointPositions) || CollectionUtil.size(presetPointPositions) >= PRESET_POINT_MAX_LEN
            && presetPointPositions.contains( position)) {
            return position
        }
        val unAddPresetPointPositions: MutableList<Int?> = ArrayList()
        for (i in 1..PRESET_POINT_MAX_LEN) {
            if (!presetPointPositions.contains(i)) {
                unAddPresetPointPositions.add(i)
            }
        }
        return if (CollectionUtil.isEmpty(unAddPresetPointPositions)) position else unAddPresetPointPositions[0]!!
    }

    private fun checkPresetPointValid(position: Int): Boolean {
        return position in 1..PRESET_POINT_MAX_LEN
    }
}