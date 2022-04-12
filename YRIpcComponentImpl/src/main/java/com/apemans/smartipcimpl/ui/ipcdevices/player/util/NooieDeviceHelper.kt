package com.apemans.smartipcimpl.ui.ipcdevices.player.util

import android.text.TextUtils
import com.apemans.smartipcimpl.ui.ipcdevices.player.bean.CONNECTION_MODE_AP_DIRECT
import com.apemans.smartipcimpl.ui.ipcdevices.player.old.bean.IpcType
import com.nooie.common.utils.data.DataHelper


/**
 * @Author:dongbeihu
 * @Description:
 * @Date: 2021/12/8-14:56
 */
object NooieDeviceHelper {
    fun mergeIpcType(ipcType: IpcType?): IpcType? {
        return if (ipcType === IpcType.PC420 || ipcType === IpcType.PC440 || ipcType === IpcType.C1 || ipcType === IpcType.A1 || ipcType === IpcType.TC100 || ipcType === IpcType.XC100) {
            IpcType.PC420
        } else if (ipcType === IpcType.PC530 || ipcType === IpcType.PC540 || ipcType === IpcType.PC650 || ipcType === IpcType.SC210 || ipcType === IpcType.SC220 || ipcType === IpcType.SC100 || ipcType === IpcType.PC530PRO || ipcType === IpcType.PC660 || ipcType === IpcType.PC660PRO || ipcType === IpcType.P3 || ipcType === IpcType.PC530PRO || ipcType === IpcType.P1 || ipcType === IpcType.P2 || ipcType === IpcType.P4 || ipcType === IpcType.K1 || ipcType === IpcType.K2 || ipcType === IpcType.TR100 || ipcType === IpcType.TS200) {
            IpcType.PC530
        } else if (ipcType === IpcType.PC730 || ipcType === IpcType.PC770 || ipcType === IpcType.Q1 || ipcType === IpcType.T1 || ipcType === IpcType.TS100) {
            IpcType.PC730
        } else if (ipcType === IpcType.MC120 || ipcType === IpcType.M1) {
            IpcType.MC120
        } else if (ipcType === IpcType.EC810_CAM || ipcType === IpcType.W0_CAM) {
            IpcType.EC810_CAM
        } else if (ipcType === IpcType.EC810PRO || ipcType === IpcType.W1) {
            IpcType.EC810PRO
        } else if (ipcType === IpcType.EC810_PLUS || ipcType === IpcType.W2) {
            IpcType.EC810_PLUS
        } else if (ipcType === IpcType.HC320) {
            IpcType.HC320
        } else {
            ipcType
        }
    }


    fun isSortLinkDevice(model: String?, isSubDevice: Boolean, connectionMode: Int): Boolean {
        return !TextUtils.isEmpty(model) && (IpcType.getIpcType(model) === IpcType.EC810PRO || IpcType.getIpcType(model) === IpcType.HC320 || IpcType.getIpcType(model) === IpcType.EC810_PLUS || IpcType.getIpcType(model) === IpcType.W1 || IpcType.getIpcType(model) === IpcType.W2) && !isSubDevice && connectionMode != CONNECTION_MODE_AP_DIRECT
    }

    /**
     *
     * @param version1
     * @param version2
     * @return version1 > version2 as 1, version1 == version2 as 0, version1 < version2 as -1
     */
    fun compareVersion(version1: String, version2: String): Int {
        if (TextUtils.isEmpty(version1) || TextUtils.isEmpty(version2)) {
            return 0
        }
        val versionCodes1 = version1.split("\\.").toTypedArray()
        val versionCodes2 = version2.split("\\.").toTypedArray()
        return if (versionCodes1 != null && versionCodes2 != null && versionCodes1.size == versionCodes2.size && versionCodes1.size == 3) {
            if (DataHelper.toInt(versionCodes1[0]) > DataHelper.toInt(
                    versionCodes2[0]
                )
            ) {
                1
            } else if (DataHelper.toInt(versionCodes1[0]) == DataHelper.toInt(
                    versionCodes2[0]
                )
            ) {
                if (DataHelper.toInt(versionCodes1[1]) > DataHelper.toInt(
                        versionCodes2[1]
                    )
                ) {
                    1
                } else if (DataHelper.toInt(versionCodes1[1]) == DataHelper.toInt(
                        versionCodes2[1]
                    )
                ) {
                    if (DataHelper.toInt(versionCodes1[2]) > DataHelper.toInt(
                            versionCodes2[2]
                        )
                    ) {
                        1
                    } else if (DataHelper.toInt(versionCodes1[2]) == DataHelper.toInt(
                            versionCodes2[2]
                        )
                    ) {
                        0
                    } else {
                        -1
                    }
                } else {
                    -1
                }
            } else {
                -1
            }
        } else 0
    }

    fun isLpDevice(model: String?): Boolean {
        return !TextUtils.isEmpty(model) && (IpcType.getIpcType(model) === IpcType.EC810_CAM || IpcType.getIpcType( model)
                === IpcType.EC810PRO || IpcType.getIpcType(model) === IpcType.HC320 || IpcType.getIpcType( model
        ) === IpcType.EC810_PLUS || IpcType.getIpcType(model) === IpcType.W0_CAM || IpcType.getIpcType(
            model
        ) === IpcType.W1 || IpcType.getIpcType(model) === IpcType.W2)
    }
}