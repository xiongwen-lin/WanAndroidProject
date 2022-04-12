package com.apemans.tuya.component.provider

import android.content.Context
import androidx.lifecycle.LiveData
import com.alibaba.android.arouter.facade.annotation.Route
import com.apemans.tuya.module.api.TUYA_API_MANAGER
import com.apemans.tuya.module.api.TuyaService
import com.apemans.tuya.component.repository.TuyaRepository
import com.tuya.smart.home.sdk.bean.HomeBean
import com.tuya.smart.home.sdk.bean.WeatherBean
import com.tuya.smart.sdk.bean.DeviceBean
import com.tuya.smart.sdk.bean.GroupBean
import kotlinx.coroutines.flow.Flow

/***********************************************************
 * 作者: caro
 * 邮箱: 1025807062@qq.com
 * 日期: 2021/11/3 17:21
 * 说明: 涂鸦Api接口服务实现
 *
 * 备注:
 *
 ***********************************************************/
@Route(path = TUYA_API_MANAGER)
class TuyaServiceProvider : TuyaService {
    override val homeList: LiveData<List<HomeBean>>
        get() = TuyaRepository.homeList
    override val groupList: LiveData<List<GroupBean>>
        get() = TuyaRepository.groupList
    override val deviceList: LiveData<List<DeviceBean>>
        get() = TuyaRepository.deviceList

    override fun queryHomeList() = TuyaRepository.queryHomeList()

    override fun selectHome(homeId: Long) = TuyaRepository.selectHome(homeId)

    override fun getWeather(lon: Double, lat: Double) = TuyaRepository.getWeather(lon, lat)

    override fun init(context: Context?) = Unit
}