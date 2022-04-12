package com.apemans.smartipcimpl.ui.ipcdevices.player.com.hdl.ruler.bean;

/**
 * 时间轴移动、拖动的回调
 * Created by HDL on 2017/9/5.
 */

public interface OnBarMoveListenerIPC {


    /**
     * 当拖动完成时回调
     *
     * @param currentTime
     */
    void onBarMoveFinish(long currentTime);


}
