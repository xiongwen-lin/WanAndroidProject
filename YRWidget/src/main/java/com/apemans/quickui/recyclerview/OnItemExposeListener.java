package com.apemans.quickui.recyclerview;

/***********************************************************
 * @Author : caro
 * @Date   : 3/2/21
 * @Func:
 *
 *
 * @Description:
 *
 *
 ***********************************************************/
public interface OnItemExposeListener {
    /**
     * item 可见性回调
     * 回调此方法时 视觉上一定是可见的（无论可见多少）
     * @param visible true，逻辑上可见，即宽/高 >50%
     * @param position item在列表中的位置
     */
    void onItemViewVisible(boolean visible, int position);

    /**
     * 滚动停止
     */
    void onScrollIdled();
}
