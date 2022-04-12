/*
 * * * Copyright (c) 2021 海龙 Inc. All rights reserved.
 */

package com.apemans.dmapi.support;

import androidx.annotation.StringDef;

import com.apemans.dmapi.support.ea.EAIndoorPlugSupportModel;
import com.apemans.dmapi.support.ea.EAOutdoorPlugSupportModel;
import com.apemans.dmapi.support.ea.EASwitchCommonSupportModel;
import com.apemans.dmapi.support.ea.EASwitchDimmingSupportModel;
import com.apemans.dmapi.support.ipc.IpcBatteryCameraSupportModel;
import com.apemans.dmapi.support.ipc.IpcHubSupportModel;
import com.apemans.dmapi.support.ipc.IpcIndoorCameraPANSupportModel;
import com.apemans.dmapi.support.ipc.IpcIndoorCardCameraSupportModel;
import com.apemans.dmapi.support.ipc.IpcIndoorMiniCameraSupportModel;
import com.apemans.dmapi.support.ipc.IpcOutdoorCameraSupportModel;
import com.apemans.dmapi.support.light.LightBulbSupportModel;
import com.apemans.dmapi.support.light.LightFloorLampSupportModel;
import com.apemans.dmapi.support.light.LightStripLightsSupportModel;
import com.apemans.dmapi.support.router.RouterSupportModel;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/***********************************************************
 * 作者: caro
 * 日期: 2021/8/25 22:25
 * 说明:
 *
 * 备注:
 *
 ***********************************************************/
@StringDef({
        /*----------------------IPC ---------------------------------*/
        //室内Ipc卡片机
        IpcIndoorCardCameraSupportModel.PC420,
        IpcIndoorCardCameraSupportModel.PC440,
        IpcIndoorCardCameraSupportModel.TC100,
        IpcIndoorCardCameraSupportModel.C1,
        IpcIndoorCardCameraSupportModel.A1,
        //室内Ipc摇头机
        IpcIndoorCameraPANSupportModel.PC530,
        IpcIndoorCameraPANSupportModel.PC530PRO,
        IpcIndoorCameraPANSupportModel.PC540,
        IpcIndoorCameraPANSupportModel.SC100,
        IpcIndoorCameraPANSupportModel.SC210,
        IpcIndoorCameraPANSupportModel.TR100,
        IpcIndoorCameraPANSupportModel.TS200,
        IpcIndoorCameraPANSupportModel.P1,
        IpcIndoorCameraPANSupportModel.P2,
        IpcIndoorCameraPANSupportModel.P3,
        IpcIndoorCameraPANSupportModel.P3PRO,
        //室内Ipc Mini相机
        IpcIndoorMiniCameraSupportModel.MC120,
        IpcIndoorMiniCameraSupportModel.M1,
        //电池Ipc摄像相机
        IpcBatteryCameraSupportModel.EC810PRO,
        IpcBatteryCameraSupportModel.W1,
        //户外Ipc摄像相机
        IpcOutdoorCameraSupportModel.PC730,
        IpcOutdoorCameraSupportModel.PC770,
        IpcOutdoorCameraSupportModel.TS100,
        IpcOutdoorCameraSupportModel.Q1,
        IpcOutdoorCameraSupportModel.T1,
        //IPC网关
        IpcHubSupportModel.EC810_HUB,
        IpcHubSupportModel.W1_HUB,
        /*----------------------Router---------------------------------*/
        RouterSupportModel.R2,
        /*----------------------EA--------------------------------------*/
        //室内插座
        EAIndoorPlugSupportModel.SP10,
        EAIndoorPlugSupportModel.SP11,
        EAIndoorPlugSupportModel.SP20,
        EAIndoorPlugSupportModel.SP21,
        EAIndoorPlugSupportModel.SP22,
        EAIndoorPlugSupportModel.SP23,
        EAIndoorPlugSupportModel.SP27,
        EAIndoorPlugSupportModel.SP31,
        EAIndoorPlugSupportModel.SS30N,
        EAIndoorPlugSupportModel.SS60,
        //室外插座
        EAOutdoorPlugSupportModel.SS31,
        EAOutdoorPlugSupportModel.SS32,
        EAOutdoorPlugSupportModel.SS33,
        EAOutdoorPlugSupportModel.SS34,
        EAOutdoorPlugSupportModel.SS36,
        EAOutdoorPlugSupportModel.SS42,
        //通用开关
        EASwitchCommonSupportModel.SR41,
        EASwitchCommonSupportModel.SR42,
        EASwitchCommonSupportModel.SR43,
        EASwitchCommonSupportModel.SR40,
        //调光开关
        EASwitchDimmingSupportModel.SR46,
        /*----------------------Light---------------------------------*/
        //灯泡类型
        LightBulbSupportModel.SB30,
        LightBulbSupportModel.SB50,
        LightBulbSupportModel.SB53,
        LightBulbSupportModel.SB60,
        LightBulbSupportModel.DL46,
        //灯带类型
        LightStripLightsSupportModel.SL02,
        LightStripLightsSupportModel.SL07,
        LightStripLightsSupportModel.SL08,
        LightStripLightsSupportModel.SL12,
        //落地灯类型
        LightFloorLampSupportModel.FL41
})
@Retention(RetentionPolicy.SOURCE)
public @interface SupportModel {
}
