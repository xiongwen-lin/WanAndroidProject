///*
// * * * Copyright (c) 2021 海龙 Inc. All rights reserved.
// */
//
//package com.apemans.tuya.component.repository
//
//import com.dylanc.longan.application
//import com.apemans.dmapi.attrs.DeviceSupportAttrs
//import com.apemans.dmapi.info.EADeviceInfo
//import com.apemans.dmapi.info.LightDeviceInfo
//import com.apemans.dmapi.model.DeviceModel
//import com.apemans.dmapi.support.*
//import com.apemans.dmapi.support.ea.*
//import com.apemans.dmapi.support.light.LightBulbSupportModel
//import com.apemans.dmapi.support.light.LightFloorLampSupportModel
//import com.apemans.dmapi.support.light.LightStripLightsSupportModel
//import com.apemans.dmapi.support.light.LightSupportCategory
//import com.apemans.dmcomponent.R
//import com.apemans.dmcomponent.ui.adddevice.items.DeviceGroup
//
///***********************************************************
// * 作者: caro
// * 日期: 2021/9/3 15:35
// * 说明: 涂鸦设备类型支持数据
// *
// * 备注:
// *
// ***********************************************************/
//class TuyaSupportDeviceDataSource {
//    //分类支持
//    val supportCategories by lazy {
//        listOf(
//            application.getString(R.string.electrical),
//            application.getString(R.string.lighting),
//        )
//    }
//
//    //电工类
//    val electricalDevices by lazy {
//        listOf(
//            //室内插座分类
//            DeviceGroup(application.getString(R.string.indoor_plug)),
//            DeviceModel(
//                EADeviceInfo(
//                    EAIndoorPlugSupportModel.SP10,
//                    EAIndoorPlugSupportModel.SP10,
//                    DeviceSupportMainCategory.EA,
//                    EASupportCategory.indoor_plug_type,
//                    "",
//                    SupportPlatform.tuyaProtocolPlatform,
//                    R.drawable.sp10,
//                    SupportBrand.osaio_brand,
//                    SupportCommunicationType.WIFI
//                )
//            ).apply {
//                val attrs = DeviceSupportAttrs()
//                //配网支持WiFi快连和热点配网
//                attrs.pair_device_type_support.add(DevicePariTypeSupport.WiFi_EZ_MODE)
//                attrs.pair_device_type_support.add(DevicePariTypeSupport.HOTSPOT_COMPAT_MODE)
//                device_attrs = attrs
//            },
//            DeviceModel(
//                EADeviceInfo(
//                    EAIndoorPlugSupportModel.SP11,
//                    EAIndoorPlugSupportModel.SP11,
//                    DeviceSupportMainCategory.EA,
//                    EASupportCategory.indoor_plug_type,
//                    "",
//                    SupportPlatform.tuyaProtocolPlatform,
//                    R.drawable.sp11,
//                    SupportBrand.osaio_brand,
//                    SupportCommunicationType.WIFI
//                )
//            ).apply {
//                val attrs = DeviceSupportAttrs()
//                //配网支持WiFi快连和热点配网
//                attrs.pair_device_type_support.add(DevicePariTypeSupport.WiFi_EZ_MODE)
//                attrs.pair_device_type_support.add(DevicePariTypeSupport.HOTSPOT_COMPAT_MODE)
//                device_attrs = attrs
//            },
//            DeviceModel(
//                EADeviceInfo(
//                    EAIndoorPlugSupportModel.SP20,
//                    EAIndoorPlugSupportModel.SP20,
//                    DeviceSupportMainCategory.EA,
//                    EASupportCategory.indoor_plug_type,
//                    "",
//                    SupportPlatform.tuyaProtocolPlatform,
//                    R.drawable.sp20,
//                    SupportBrand.osaio_brand,
//                    SupportCommunicationType.WIFI
//                )
//            ).apply {
//                val attrs = DeviceSupportAttrs()
//                //配网支持WiFi快连和热点配网
//                attrs.pair_device_type_support.add(DevicePariTypeSupport.WiFi_EZ_MODE)
//                attrs.pair_device_type_support.add(DevicePariTypeSupport.HOTSPOT_COMPAT_MODE)
//                device_attrs = attrs
//            },
//            DeviceModel(
//                EADeviceInfo(
//                    EAIndoorPlugSupportModel.SP21,
//                    EAIndoorPlugSupportModel.SP21,
//                    DeviceSupportMainCategory.EA,
//                    EASupportCategory.indoor_plug_type,
//                    "",
//                    SupportPlatform.tuyaProtocolPlatform,
//                    R.drawable.sp21,
//                    SupportBrand.osaio_brand,
//                    SupportCommunicationType.WIFI
//                )
//            ).apply {
//                val attrs = DeviceSupportAttrs()
//                //配网支持WiFi快连和热点配网
//                attrs.pair_device_type_support.add(DevicePariTypeSupport.WiFi_EZ_MODE)
//                attrs.pair_device_type_support.add(DevicePariTypeSupport.HOTSPOT_COMPAT_MODE)
//                device_attrs = attrs
//            },
//            DeviceModel(
//                EADeviceInfo(
//                    EAIndoorPlugSupportModel.SP22,
//                    EAIndoorPlugSupportModel.SP22,
//                    DeviceSupportMainCategory.EA,
//                    EASupportCategory.indoor_plug_type,
//                    "",
//                    SupportPlatform.tuyaProtocolPlatform,
//                    R.drawable.sp22,
//                    SupportBrand.osaio_brand,
//                    SupportCommunicationType.WIFI
//                )
//            ).apply {
//                val attrs = DeviceSupportAttrs()
//                //配网支持WiFi快连和热点配网
//                attrs.pair_device_type_support.add(DevicePariTypeSupport.WiFi_EZ_MODE)
//                attrs.pair_device_type_support.add(DevicePariTypeSupport.HOTSPOT_COMPAT_MODE)
//                device_attrs = attrs
//            },
//            DeviceModel(
//                EADeviceInfo(
//                    EAIndoorPlugSupportModel.SP23,
//                    EAIndoorPlugSupportModel.SP23,
//                    DeviceSupportMainCategory.EA,
//                    EASupportCategory.indoor_plug_type,
//                    "",
//                    SupportPlatform.tuyaProtocolPlatform,
//                    R.drawable.sp23,
//                    SupportBrand.osaio_brand,
//                    SupportCommunicationType.WIFI
//                )
//            ).apply {
//                val attrs = DeviceSupportAttrs()
//                //配网支持WiFi快连和热点配网
//                attrs.pair_device_type_support.add(DevicePariTypeSupport.WiFi_EZ_MODE)
//                attrs.pair_device_type_support.add(DevicePariTypeSupport.HOTSPOT_COMPAT_MODE)
//                device_attrs = attrs
//            },
//            DeviceModel(
//                EADeviceInfo(
//                    EAIndoorPlugSupportModel.SP27,
//                    EAIndoorPlugSupportModel.SP27,
//                    DeviceSupportMainCategory.EA,
//                    EASupportCategory.indoor_plug_type,
//                    "",
//                    SupportPlatform.tuyaProtocolPlatform,
//                    R.drawable.sp27,
//                    SupportBrand.osaio_brand,
//                    SupportCommunicationType.WIFI
//                )
//            ).apply {
//                val attrs = DeviceSupportAttrs()
//                //配网支持WiFi快连和热点配网
//                attrs.pair_device_type_support.add(DevicePariTypeSupport.WiFi_EZ_MODE)
//                attrs.pair_device_type_support.add(DevicePariTypeSupport.HOTSPOT_COMPAT_MODE)
//                device_attrs = attrs
//            },
//            DeviceModel(
//                EADeviceInfo(
//                    EAIndoorPlugSupportModel.SP31,
//                    EAIndoorPlugSupportModel.SP31,
//                    DeviceSupportMainCategory.EA,
//                    EASupportCategory.indoor_plug_type,
//                    "",
//                    SupportPlatform.tuyaProtocolPlatform,
//                    R.drawable.sp31,
//                    SupportBrand.osaio_brand,
//                    SupportCommunicationType.WIFI
//                )
//            ).apply {
//                val attrs = DeviceSupportAttrs()
//                //配网支持WiFi快连和热点配网
//                attrs.pair_device_type_support.add(DevicePariTypeSupport.WiFi_EZ_MODE)
//                attrs.pair_device_type_support.add(DevicePariTypeSupport.HOTSPOT_COMPAT_MODE)
//                device_attrs = attrs
//            },
//            DeviceModel(
//                EADeviceInfo(
//                    EAIndoorPlugSupportModel.SS30N,
//                    EAIndoorPlugSupportModel.SS30N,
//                    DeviceSupportMainCategory.EA,
//                    EASupportCategory.indoor_plug_type,
//                    "",
//                    SupportPlatform.tuyaProtocolPlatform,
//                    R.drawable.ss30n,
//                    SupportBrand.osaio_brand,
//                    SupportCommunicationType.WIFI
//                )
//            ).apply {
//                val attrs = DeviceSupportAttrs()
//                //配网支持WiFi快连和热点配网
//                attrs.pair_device_type_support.add(DevicePariTypeSupport.WiFi_EZ_MODE)
//                attrs.pair_device_type_support.add(DevicePariTypeSupport.HOTSPOT_COMPAT_MODE)
//                device_attrs = attrs
//            },
//            DeviceModel(
//                EADeviceInfo(
//                    EAIndoorPlugSupportModel.SS60,
//                    EAIndoorPlugSupportModel.SS60,
//                    DeviceSupportMainCategory.EA,
//                    EASupportCategory.indoor_plug_type,
//                    "",
//                    SupportPlatform.tuyaProtocolPlatform,
//                    R.drawable.ss60,
//                    SupportBrand.osaio_brand,
//                    SupportCommunicationType.WIFI
//                )
//            ).apply {
//                val attrs = DeviceSupportAttrs()
//                //配网支持WiFi快连和热点配网
//                attrs.pair_device_type_support.add(DevicePariTypeSupport.WiFi_EZ_MODE)
//                attrs.pair_device_type_support.add(DevicePariTypeSupport.HOTSPOT_COMPAT_MODE)
//                device_attrs = attrs
//            },
//
//            //室外插座分类
//            DeviceGroup(application.getString(R.string.outdoor_plug)),
//            DeviceModel(
//                EADeviceInfo(
//                    EAOutdoorPlugSupportModel.SS31,
//                    EAOutdoorPlugSupportModel.SS31,
//                    DeviceSupportMainCategory.EA,
//                    EASupportCategory.outdoor_plug_type,
//                    "",
//                    SupportPlatform.tuyaProtocolPlatform,
//                    R.drawable.ss31,
//                    SupportBrand.osaio_brand,
//                    SupportCommunicationType.WIFI
//                )
//            ).apply {
//                val attrs = DeviceSupportAttrs()
//                //配网支持WiFi快连和热点配网
//                attrs.pair_device_type_support.add(DevicePariTypeSupport.WiFi_EZ_MODE)
//                attrs.pair_device_type_support.add(DevicePariTypeSupport.HOTSPOT_COMPAT_MODE)
//                device_attrs = attrs
//            },
//            DeviceModel(
//                EADeviceInfo(
//                    EAOutdoorPlugSupportModel.SS32,
//                    EAOutdoorPlugSupportModel.SS32,
//                    DeviceSupportMainCategory.EA,
//                    EASupportCategory.outdoor_plug_type,
//                    "",
//                    SupportPlatform.tuyaProtocolPlatform,
//                    R.drawable.ss32,
//                    SupportBrand.osaio_brand,
//                    SupportCommunicationType.WIFI
//                )
//            ).apply {
//                val attrs = DeviceSupportAttrs()
//                //配网支持WiFi快连和热点配网
//                attrs.pair_device_type_support.add(DevicePariTypeSupport.WiFi_EZ_MODE)
//                attrs.pair_device_type_support.add(DevicePariTypeSupport.HOTSPOT_COMPAT_MODE)
//                device_attrs = attrs
//            },
//            DeviceModel(
//                EADeviceInfo(
//                    EAOutdoorPlugSupportModel.SS33,
//                    EAOutdoorPlugSupportModel.SS33,
//                    DeviceSupportMainCategory.EA,
//                    EASupportCategory.outdoor_plug_type,
//                    "",
//                    SupportPlatform.tuyaProtocolPlatform,
//                    R.drawable.ss33,
//                    SupportBrand.osaio_brand,
//                    SupportCommunicationType.WIFI
//                )
//            ).apply {
//                val attrs = DeviceSupportAttrs()
//                //配网支持WiFi快连和热点配网
//                attrs.pair_device_type_support.add(DevicePariTypeSupport.WiFi_EZ_MODE)
//                attrs.pair_device_type_support.add(DevicePariTypeSupport.HOTSPOT_COMPAT_MODE)
//                device_attrs = attrs
//            },
//            DeviceModel(
//                EADeviceInfo(
//                    EAOutdoorPlugSupportModel.SS34,
//                    EAOutdoorPlugSupportModel.SS34,
//                    DeviceSupportMainCategory.EA,
//                    EASupportCategory.outdoor_plug_type,
//                    "",
//                    SupportPlatform.tuyaProtocolPlatform,
//                    R.drawable.ss34,
//                    SupportBrand.osaio_brand,
//                    SupportCommunicationType.WIFI
//                )
//            ).apply {
//                val attrs = DeviceSupportAttrs()
//                //配网支持WiFi快连和热点配网
//                attrs.pair_device_type_support.add(DevicePariTypeSupport.WiFi_EZ_MODE)
//                attrs.pair_device_type_support.add(DevicePariTypeSupport.HOTSPOT_COMPAT_MODE)
//                device_attrs = attrs
//            },
//            DeviceModel(
//                EADeviceInfo(
//                    EAOutdoorPlugSupportModel.SS36,
//                    EAOutdoorPlugSupportModel.SS36,
//                    DeviceSupportMainCategory.EA,
//                    EASupportCategory.outdoor_plug_type,
//                    "",
//                    SupportPlatform.tuyaProtocolPlatform,
//                    R.drawable.ss36,
//                    SupportBrand.osaio_brand,
//                    SupportCommunicationType.WIFI
//                )
//            ).apply {
//                val attrs = DeviceSupportAttrs()
//                //配网支持WiFi快连和热点配网
//                attrs.pair_device_type_support.add(DevicePariTypeSupport.WiFi_EZ_MODE)
//                attrs.pair_device_type_support.add(DevicePariTypeSupport.HOTSPOT_COMPAT_MODE)
//                device_attrs = attrs
//            },
//            DeviceModel(
//                EADeviceInfo(
//                    EAOutdoorPlugSupportModel.SS42,
//                    EAOutdoorPlugSupportModel.SS42,
//                    DeviceSupportMainCategory.EA,
//                    EASupportCategory.outdoor_plug_type,
//                    "",
//                    SupportPlatform.tuyaProtocolPlatform,
//                    R.drawable.ss42,
//                    SupportBrand.osaio_brand,
//                    SupportCommunicationType.WIFI
//                )
//            ).apply {
//                val attrs = DeviceSupportAttrs()
//                //配网支持WiFi快连和热点配网
//                attrs.pair_device_type_support.add(DevicePariTypeSupport.WiFi_EZ_MODE)
//                attrs.pair_device_type_support.add(DevicePariTypeSupport.HOTSPOT_COMPAT_MODE)
//                device_attrs = attrs
//            },
//
//            //通用开关分类
//            DeviceGroup(application.getString(R.string.wall_switch)),
//            DeviceModel(
//                EADeviceInfo(
//                    EASwitchCommonSupportModel.SR40,
//                    EASwitchCommonSupportModel.SR40,
//                    DeviceSupportMainCategory.EA,
//                    EASupportCategory.switch_common_type,
//                    "",
//                    SupportPlatform.tuyaProtocolPlatform,
//                    R.drawable.sr40,
//                    SupportBrand.osaio_brand,
//                    SupportCommunicationType.WIFI
//                )
//            ).apply {
//                val attrs = DeviceSupportAttrs()
//                //配网支持WiFi快连和热点配网
//                attrs.pair_device_type_support.add(DevicePariTypeSupport.WiFi_EZ_MODE)
//                attrs.pair_device_type_support.add(DevicePariTypeSupport.HOTSPOT_COMPAT_MODE)
//                device_attrs = attrs
//            },
//            DeviceModel(
//                EADeviceInfo(
//                    EASwitchCommonSupportModel.SR41,
//                    EASwitchCommonSupportModel.SR41,
//                    DeviceSupportMainCategory.EA,
//                    EASupportCategory.switch_common_type,
//                    "",
//                    SupportPlatform.tuyaProtocolPlatform,
//                    R.drawable.sr41,
//                    SupportBrand.osaio_brand,
//                    SupportCommunicationType.WIFI
//                )
//            ).apply {
//                val attrs = DeviceSupportAttrs()
//                //配网支持WiFi快连和热点配网
//                attrs.pair_device_type_support.add(DevicePariTypeSupport.WiFi_EZ_MODE)
//                attrs.pair_device_type_support.add(DevicePariTypeSupport.HOTSPOT_COMPAT_MODE)
//                device_attrs = attrs
//            },
//            DeviceModel(
//                EADeviceInfo(
//                    EASwitchCommonSupportModel.SR42,
//                    EASwitchCommonSupportModel.SR42,
//                    DeviceSupportMainCategory.EA,
//                    EASupportCategory.switch_common_type,
//                    "",
//                    SupportPlatform.tuyaProtocolPlatform,
//                    R.drawable.sr42,
//                    SupportBrand.osaio_brand,
//                    SupportCommunicationType.WIFI
//                )
//            ).apply {
//                val attrs = DeviceSupportAttrs()
//                //配网支持WiFi快连和热点配网
//                attrs.pair_device_type_support.add(DevicePariTypeSupport.WiFi_EZ_MODE)
//                attrs.pair_device_type_support.add(DevicePariTypeSupport.HOTSPOT_COMPAT_MODE)
//                device_attrs = attrs
//            },
//            DeviceModel(
//                EADeviceInfo(
//                    EASwitchCommonSupportModel.SR43,
//                    EASwitchCommonSupportModel.SR43,
//                    DeviceSupportMainCategory.EA,
//                    EASupportCategory.switch_common_type,
//                    "",
//                    SupportPlatform.tuyaProtocolPlatform,
//                    R.drawable.sr43,
//                    SupportBrand.osaio_brand,
//                    SupportCommunicationType.WIFI
//                )
//            ).apply {
//                val attrs = DeviceSupportAttrs()
//                //配网支持WiFi快连和热点配网
//                attrs.pair_device_type_support.add(DevicePariTypeSupport.WiFi_EZ_MODE)
//                attrs.pair_device_type_support.add(DevicePariTypeSupport.HOTSPOT_COMPAT_MODE)
//                device_attrs = attrs
//            },
//
//            //调光开关分类
//            DeviceGroup(application.getString(R.string.dimming_switch)),
//            DeviceModel(
//                EADeviceInfo(
//                    EASwitchDimmingSupportModel.SR46,
//                    EASwitchDimmingSupportModel.SR46,
//                    DeviceSupportMainCategory.EA,
//                    EASupportCategory.switch_dimming_type,
//                    "",
//                    SupportPlatform.tuyaProtocolPlatform,
//                    R.drawable.sr46,
//                    SupportBrand.osaio_brand,
//                    SupportCommunicationType.WIFI
//                )
//            ).apply {
//                val attrs = DeviceSupportAttrs()
//                //配网支持WiFi快连和热点配网
//                attrs.pair_device_type_support.add(DevicePariTypeSupport.WiFi_EZ_MODE)
//                attrs.pair_device_type_support.add(DevicePariTypeSupport.HOTSPOT_COMPAT_MODE)
//                device_attrs = attrs
//            }
//        )
//    }
//
//    //照明类
//    val lightingDevices by lazy {
//        listOf(
//            DeviceGroup(application.getString(R.string.bulb)),
//            DeviceModel(
//                LightDeviceInfo(
//                    LightBulbSupportModel.SB30,
//                    LightBulbSupportModel.SB30,
//                    DeviceSupportMainCategory.light,
//                    LightSupportCategory.bulb_type,
//                    "",
//                    SupportPlatform.tuyaProtocolPlatform,
//                    R.drawable.sb50,
//                    SupportBrand.osaio_brand,
//                    SupportCommunicationType.WIFI
//                )
//            ).apply {
//                val attrs = DeviceSupportAttrs()
//                //配网支持WiFi快连和热点配网
//                attrs.pair_device_type_support.add(DevicePariTypeSupport.WiFi_EZ_MODE)
//                attrs.pair_device_type_support.add(DevicePariTypeSupport.HOTSPOT_COMPAT_MODE)
//                device_attrs = attrs
//            },
//            DeviceModel(
//                LightDeviceInfo(
//                    LightBulbSupportModel.SB50,
//                    LightBulbSupportModel.SB50,
//                    DeviceSupportMainCategory.light,
//                    LightSupportCategory.bulb_type,
//                    "",
//                    SupportPlatform.tuyaProtocolPlatform,
//                    R.drawable.sb50,
//                    SupportBrand.osaio_brand,
//                    SupportCommunicationType.WIFI
//                )
//            ).apply {
//                val attrs = DeviceSupportAttrs()
//                //配网支持WiFi快连和热点配网
//                attrs.pair_device_type_support.add(DevicePariTypeSupport.WiFi_EZ_MODE)
//                attrs.pair_device_type_support.add(DevicePariTypeSupport.HOTSPOT_COMPAT_MODE)
//                device_attrs = attrs
//            },
//            DeviceModel(
//                LightDeviceInfo(
//                    LightBulbSupportModel.SB53,
//                    LightBulbSupportModel.SB53,
//                    DeviceSupportMainCategory.light,
//                    LightSupportCategory.bulb_type,
//                    "",
//                    SupportPlatform.tuyaProtocolPlatform,
//                    R.drawable.sb53,
//                    SupportBrand.osaio_brand,
//                    SupportCommunicationType.WIFI
//                )
//            ).apply {
//                val attrs = DeviceSupportAttrs()
//                //配网支持WiFi快连和热点配网
//                attrs.pair_device_type_support.add(DevicePariTypeSupport.WiFi_EZ_MODE)
//                attrs.pair_device_type_support.add(DevicePariTypeSupport.HOTSPOT_COMPAT_MODE)
//                device_attrs = attrs
//            },
//            DeviceModel(
//                LightDeviceInfo(
//                    LightBulbSupportModel.SB60,
//                    LightBulbSupportModel.SB60,
//                    DeviceSupportMainCategory.light,
//                    LightSupportCategory.bulb_type,
//                    "",
//                    SupportPlatform.tuyaProtocolPlatform,
//                    R.drawable.sb50,
//                    SupportBrand.osaio_brand,
//                    SupportCommunicationType.WIFI
//                )
//            ).apply {
//                val attrs = DeviceSupportAttrs()
//                //配网支持WiFi快连和热点配网
//                attrs.pair_device_type_support.add(DevicePariTypeSupport.WiFi_EZ_MODE)
//                attrs.pair_device_type_support.add(DevicePariTypeSupport.HOTSPOT_COMPAT_MODE)
//                device_attrs = attrs
//            },
//            DeviceModel(
//                LightDeviceInfo(
//                    LightBulbSupportModel.DL46,
//                    LightBulbSupportModel.DL46,
//                    DeviceSupportMainCategory.light,
//                    LightSupportCategory.bulb_type,
//                    "",
//                    SupportPlatform.tuyaProtocolPlatform,
//                    R.drawable.dl46,
//                    SupportBrand.osaio_brand,
//                    SupportCommunicationType.WIFI
//                )
//            ).apply {
//                val attrs = DeviceSupportAttrs()
//                //配网支持WiFi快连和热点配网
//                attrs.pair_device_type_support.add(DevicePariTypeSupport.WiFi_EZ_MODE)
//                attrs.pair_device_type_support.add(DevicePariTypeSupport.HOTSPOT_COMPAT_MODE)
//                device_attrs = attrs
//            },
//
//            DeviceGroup(application.getString(R.string.strip_light)),
//            DeviceModel(
//                LightDeviceInfo(
//                    LightStripLightsSupportModel.SL02,
//                    LightStripLightsSupportModel.SL02,
//                    DeviceSupportMainCategory.light,
//                    LightSupportCategory.strip_lights_type,
//                    "",
//                    SupportPlatform.tuyaProtocolPlatform,
//                    R.drawable.sl02,
//                    SupportBrand.osaio_brand,
//                    SupportCommunicationType.WIFI
//                )
//            ).apply {
//                val attrs = DeviceSupportAttrs()
//                //配网支持WiFi快连和热点配网
//                attrs.pair_device_type_support.add(DevicePariTypeSupport.WiFi_EZ_MODE)
//                attrs.pair_device_type_support.add(DevicePariTypeSupport.HOTSPOT_COMPAT_MODE)
//                device_attrs = attrs
//            },
//            DeviceModel(
//                LightDeviceInfo(
//                    LightStripLightsSupportModel.SL07,
//                    LightStripLightsSupportModel.SL07,
//                    DeviceSupportMainCategory.light,
//                    LightSupportCategory.strip_lights_type,
//                    "",
//                    SupportPlatform.tuyaProtocolPlatform,
//                    R.drawable.sl02,
//                    SupportBrand.osaio_brand,
//                    SupportCommunicationType.WIFI
//                )
//            ).apply {
//                val attrs = DeviceSupportAttrs()
//                //配网支持WiFi快连和热点配网
//                attrs.pair_device_type_support.add(DevicePariTypeSupport.WiFi_EZ_MODE)
//                attrs.pair_device_type_support.add(DevicePariTypeSupport.HOTSPOT_COMPAT_MODE)
//                device_attrs = attrs
//            },
//            DeviceModel(
//                LightDeviceInfo(
//                    LightStripLightsSupportModel.SL08,
//                    LightStripLightsSupportModel.SL08,
//                    DeviceSupportMainCategory.light,
//                    LightSupportCategory.strip_lights_type,
//                    "",
//                    SupportPlatform.tuyaProtocolPlatform,
//                    R.drawable.sl02,
//                    SupportBrand.osaio_brand,
//                    SupportCommunicationType.WIFI
//                )
//            ).apply {
//                val attrs = DeviceSupportAttrs()
//                //配网支持WiFi快连和热点配网
//                attrs.pair_device_type_support.add(DevicePariTypeSupport.WiFi_EZ_MODE)
//                attrs.pair_device_type_support.add(DevicePariTypeSupport.HOTSPOT_COMPAT_MODE)
//                device_attrs = attrs
//            },
//            DeviceModel(
//                LightDeviceInfo(
//                    LightStripLightsSupportModel.SL12,
//                    LightStripLightsSupportModel.SL12,
//                    DeviceSupportMainCategory.light,
//                    LightSupportCategory.strip_lights_type,
//                    "",
//                    SupportPlatform.tuyaProtocolPlatform,
//                    R.drawable.sl02,
//                    SupportBrand.osaio_brand,
//                    SupportCommunicationType.WIFI
//                )
//            ).apply {
//                val attrs = DeviceSupportAttrs()
//                //配网支持WiFi快连和热点配网
//                attrs.pair_device_type_support.add(DevicePariTypeSupport.WiFi_EZ_MODE)
//                attrs.pair_device_type_support.add(DevicePariTypeSupport.HOTSPOT_COMPAT_MODE)
//                device_attrs = attrs
//            },
//
//            DeviceGroup(application.getString(R.string.floor_lamp)),
//            DeviceModel(
//                LightDeviceInfo(
//                    LightFloorLampSupportModel.FL41,
//                    LightFloorLampSupportModel.FL41,
//                    DeviceSupportMainCategory.light,
//                    LightSupportCategory.floor_lamp_type,
//                    "",
//                    SupportPlatform.tuyaProtocolPlatform,
//                    R.drawable.fl41,
//                    SupportBrand.osaio_brand,
//                    SupportCommunicationType.WIFI
//                )
//            ).apply {
//                val attrs = DeviceSupportAttrs()
//                //配网支持WiFi快连和热点配网
//                attrs.pair_device_type_support.add(DevicePariTypeSupport.WiFi_EZ_MODE)
//                attrs.pair_device_type_support.add(DevicePariTypeSupport.HOTSPOT_COMPAT_MODE)
//                device_attrs = attrs
//            },
//        )
//    }
//}