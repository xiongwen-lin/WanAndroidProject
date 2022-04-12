package com.apemans.smartipcimpl.ui.ipcdevices.player.old.listener;

public interface OnStartPlaybackListener {
    /**
     * 回调播放状态给activity页面
     * @param deviceId
     * @param isCloud
     * @param isExist
     */
    void onPreStartPlayback(String deviceId, boolean isCloud, boolean isExist);
}
