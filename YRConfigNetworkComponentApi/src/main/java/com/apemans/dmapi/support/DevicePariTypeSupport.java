/*
 * * * Copyright (c) 2021 海龙 Inc. All rights reserved.
 */

package com.apemans.dmapi.support;

/***********************************************************
 * 作者: caro
 * 日期: 2021/8/23 10:23
 * 说明: 设备配对支持类型
 *
 * 备注:
 *
 ***********************************************************/
public @interface DevicePariTypeSupport {

    /**
     * 机器扫码WiFi信息配网
     * 流程特点：
     * Step 1：手机根据路由信息[WiFi ssid + password]+用户信息生成二维码，并让设备扫码
     * Step 2：设备获取到二维码信息后，完成设备绑定和上线
     */
    int wifi_qr_pair = 0;

    /**
     * WiFi Ap模式配网
     * 流程特点：
     * Step 1：手机通过连接设备的WiFi热点后，将配网的[WiFi ssid + password]发送给设备
     * Step 2：设备获取到二维码信息后，完成设备绑定和上线
     */
    int wifi_ap_pair = 1;

    /**
     * App 手机扫码配网 【设备支持如：网关设备W0-Hub 】
     * 流程特点：
     * 1：用户使用手机扫码机身二维码[机器uuid生成的二维码]，将设备绑定到设备服务器，再接入网线使其上线
     */
    int app_scan_code_qr_pair = 2;

    /**
     * WiFi蓝牙配网 【设备支持如：网关设备W0-Hub 】
     * 流程特点：
     * Step 1：通过蓝牙连接，将路由WiFi信息用户信息等发送给设备
     * Step 2：设备获取到配网信息后，完成设备绑定和上线
     */
    int wifi_ble_pair = 3;

    /**
     * 低功耗设备+网关配网【网关设备W0-Hub 】
     * 流程特点：
     * --- 前提-----
     * Step 1：低功耗设备
     * Step 2：网关设备上线
     * ----配网-----
     * Step 1:低功耗进入配网状态
     * Step 2:和对应网关设备建立连接
     * Step 3:网关设备将低功耗设备信息上报Iot服务器并完成上线
     *【每台网关设备最多支持4台低功耗设备，1拖4】
     *
     */
    int low_power_plus_gateway_pair= 4;

    /**
     * AP直连配网
     * 流程特点：
     * Step 1:通过连接设备WiFi热点(特定类型ssid)直接进行连接
     * Step 2:使用Tcp协议
     * Step 3:心跳保存连接
     * 如：海思运动相机，长链接设备M1
     */
    int ap_mode_direct_pair = 5;

    /**
     * 低功耗AP直连配网
     * 流程特点：
     * Step 1：先通过连接设备蓝牙唤醒WiFi热点，
     * Step 2：再通过连接设备WiFi热点(特定类型ssid)直接进行连接
     * Step 3：使用私有协议或P2P协议
     * Step 4：通过心跳保存连接
     * 如：Seeker运动相机，打猎相机HC320
     */
    int ap_mode_ble_pair = 6;

    /**
     * Wi-Fi Quick Connect (EZ) mode
     * Wi-Fi快连（EZ）模式
     * 就是手机App端发送包含Wi-Fi用户名Wi-Fi密码的UDP广播包或者组播包，智能终端的Wi-Fi芯片可以接收到该UDP包，
     * 只要知道UDP的组织形式，就可以通过接收到的UDP包解密出Wi-Fi用户名和密码，然后智能硬件配置收到的Wi-Fi用户名和密码到指定的Wi-Fi AP 上。
     */
    int WiFi_EZ_MODE = 7;

    /**
     * 热点配网（兼容）模式
     * Hotspot distribution network (compatible) mode
     * 热点配网（兼容）模式： App 配置手机连接到智能硬件（Wi-Fi芯片的AP），手机和Wi-Fi芯片直接建立通讯，
     * 将要配置的Wi-Fi用户名和Wi-Fi密码发送给智能硬件，此时智能硬件可以连接到配置的路由器上。
     *
     * Wi-Fi快连（EZ模式）相对于热点配网（AP模式）连接方式操作更简便，省了很多步骤，首次配置速度更快，但是部分手机和路由器可能会不兼容Wi-Fi快连（EZ模式），这时就需要用到热点配网（AP模式）了。
     */
    int HOTSPOT_COMPAT_MODE = 8;

    /**
     * AP直连模式
     * 如：运动相机
     */
    int AP_Direct_MODE = 9;
}
