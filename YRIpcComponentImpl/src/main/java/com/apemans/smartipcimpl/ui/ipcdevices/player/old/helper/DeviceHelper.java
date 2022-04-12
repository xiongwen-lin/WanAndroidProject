package com.apemans.smartipcimpl.ui.ipcdevices.player.old.helper;

import com.apemans.smartipcimpl.ui.ipcdevices.player.old.bean.IpcType;
import com.nooie.common.utils.log.NooieLog;

/**
 * @Author:dongbeihu
 * @Description:
 * @Date: 2021/12/15-21:08
 */
public class DeviceHelper {
    public static int convertNooieModel(IpcType type, String originalType) {
        NooieLog.d("-->> DeviceHelper convertNooieModel type=" + type.getType() + " originalType=" + originalType);
        if (type == IpcType.IPC_720) {
            return 1;
        } else if (type != IpcType.IPC_100 && type != IpcType.PC530 && type != IpcType.PC530PRO && type != IpcType.PC540 && type != IpcType.PC650 && type != IpcType.PC660 && type != IpcType.PC660PRO && type != IpcType.SC100 && type != IpcType.SC210 && type != IpcType.SC220 && type != IpcType.TR100 && type != IpcType.TS200 && type != IpcType.P1 && type != IpcType.P2 && type != IpcType.P3 && type != IpcType.P3Pro && type != IpcType.P4 && type != IpcType.K1 && type != IpcType.K2) {
            if (type != IpcType.IPC_200 && type != IpcType.PC730 && type != IpcType.PC770 && type != IpcType.TS100 && type != IpcType.Q1 && type != IpcType.T1) {
                if (type != IpcType.EC810_CAM && type != IpcType.EC810PRO && type != IpcType.EC810_PLUS && type != IpcType.HC320 && type != IpcType.W0_CAM && type != IpcType.W1) {
                    if (type == IpcType.W2) {
                        return 5;
                    } else if (type != IpcType.MC120 && type != IpcType.M1) {
                        return type == IpcType.PC420 && "PC420F".equalsIgnoreCase(originalType) ? 5 : 2;
                    } else {
                        return 11;
                    }
                } else {
                    return 10;
                }
            } else {
                return 4;
            }
        } else {
            return 3;
        }
    }
}
