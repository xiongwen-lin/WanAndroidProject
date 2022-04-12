package com.apemans.smartipcimpl.ui.ipcdevices.player.bean

/**
 * @Author:dongbeihu
 * @Description:
 * @Date: 2021/12/2-14:38 播放区域用户类型
 *
 */
annotation class ConnectionMode {
    companion object {
        const val CONNECTION_MODE_NONE = 0
        const val CONNECTION_MODE_AP = 1
        const val CONNECTION_MODE_QC = 2
        const val CONNECTION_MODE_AP_DIRECT = 3
        const val CONNECTION_MODE_LAN = 4
    }
}