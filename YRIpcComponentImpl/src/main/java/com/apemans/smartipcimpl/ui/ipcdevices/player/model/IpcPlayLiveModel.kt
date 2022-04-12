package com.apemans.smartipcimpl.ui.ipcdevices.player.model

import android.media.AudioManager
import android.text.TextUtils
import androidx.lifecycle.viewModelScope
import com.apemans.smartipcapi.info.IpcDeviceInfo
import com.apemans.logger.YRLog
import com.apemans.smartipcimpl.R
import com.apemans.smartipcimpl.repository.DeviceManagerRepository
import com.apemans.smartipcimpl.ui.actioncameralist.IpcControlViewModel

import com.apemans.smartipcimpl.ui.ipcdevices.player.bean.DateTimeBean
import com.apemans.smartipcimpl.ui.ipcdevices.player.bean.FunctionMenuItem
import com.apemans.smartipcimpl.ui.ipcdevices.player.old.bean.IpcType
import com.apemans.smartipcimpl.ui.ipcdevices.player.util.NooieDeviceHelper
import com.nooie.common.bean.PhoneVolume
import com.nooie.common.utils.log.NooieLog
import com.nooie.common.utils.tool.SystemUtil

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.launch

/**
 * @Author:dongbeihu
 * @Description: IPC直播实现接口功能
 * @Date: 2021/12/2-15:27
 */
class IpcPlayLiveModel : IpcControlViewModel() {



    /**
     * 获取设备详情
     */
    fun  loadDeviceInfo(deviceId: String) {
        viewModelScope.launch {
            DeviceManagerRepository.getIpcDevice(deviceId)
                .flowOn(Dispatchers.IO)
                .catch { e ->
                    YRLog.d { e }
                }
                .single()
                ?.let {
                    (it.deviceInfo as? IpcDeviceInfo)
                }
        }
    }


    /**
     * 判断是否低功耗设备
     */
    fun isLpDevice(model: String?): Boolean {
        return !TextUtils.isEmpty(model) && (IpcType.getIpcType(model) === IpcType.EC810_CAM
                || IpcType.getIpcType( model ) === IpcType.EC810PRO
                || IpcType.getIpcType(model) === IpcType.HC320
                || IpcType.getIpcType(  model ) === IpcType.EC810_PLUS
                || IpcType.getIpcType(model) === IpcType.W0_CAM
                || IpcType.getIpcType(model) === IpcType.W1
                || IpcType.getIpcType(model) === IpcType.W2)
    }

}