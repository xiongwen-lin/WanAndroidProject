/*
 * * * Copyright (c) 2021 海龙 Inc. All rights reserved.
 */

package com.apemans.dmapi.support;

/***********************************************************
 * 作者: caro
 * 日期: 2021/8/26 18:47
 * 说明: 通信协议支持
 * WiFi快连和热点配网
 * 1：Wi-Fi快连（EZ）模式： 就是手机App端发送包含Wi-Fi用户名Wi-Fi密码的UDP广播包或者组播包，智能终端的Wi-Fi芯片可以接收到该UDP包，
 *    只要知道UDP的组织形式，就可以通过接收到的UDP包解密出Wi-Fi用户名和密码，然后智能硬件配置收到的Wi-Fi用户名和密码到指定的Wi-Fi AP 上。
 * 2：热点配网（兼容）模式： App 配置手机连接到智能硬件（Wi-Fi芯片的AP），手机和Wi-Fi芯片直接建立通讯，
 *    将要配置的Wi-Fi用户名和Wi-Fi密码发送给智能硬件，此时智能硬件可以连接到配置的路由器上。
 * 备注:
 * Wi-Fi快联是将Wi-Fi密码发送给设备，热点配网是输入Wi-Fi密码后去连接设备的热点
 *
 ***********************************************************/
public @interface SupportCommunicationType {
    //WiFi协议
    int WIFI = 1;
    //蓝牙协议
    int BLE = 2;
    //ZigBee协议
    int ZIGBEE = 3;
    //WiFi+蓝牙
    int WIFI_BLE = 4;
    //蓝牙+ZigBee协议
    int BLE_ZIGBEE = 5;
    //NB_IOT
    int NB_IOT = 6;
    //WIFI+BLE+Zigbee支持
    int WIFI_BLE_ZIGBEE = 7;

}
