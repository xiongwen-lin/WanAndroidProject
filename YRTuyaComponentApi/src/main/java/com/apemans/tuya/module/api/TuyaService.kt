package com.apemans.tuya.module.api

import androidx.lifecycle.LiveData
import com.alibaba.android.arouter.facade.template.IProvider
import com.tuya.smart.home.sdk.bean.HomeBean
import com.tuya.smart.home.sdk.bean.WeatherBean
import com.tuya.smart.sdk.bean.DeviceBean
import com.tuya.smart.sdk.bean.GroupBean
import kotlinx.coroutines.flow.Flow

/***********************************************************
 * 作者: caro
 * 邮箱: 1025807062@qq.com
 * 日期: 2021/11/3 16:31
 * 说明: 涂鸦接口服务
 *
 * 备注:
 *
 ***********************************************************/
interface TuyaService : IProvider {

    val homeList: LiveData<List<HomeBean>>
    val groupList: LiveData<List<GroupBean>>
    val deviceList: LiveData<List<DeviceBean>>

    fun queryHomeList(): Flow<List<HomeBean>>
    fun selectHome(homeId: Long): Flow<Unit>
    fun getWeather(lon: Double, lat: Double): Flow<WeatherBean>
//    /**
//     * 获取家庭列表
//     * @return List<HomeBean>
//     */
//    suspend fun queryHomeList(): List<HomeBean>
//
//    /**
//     * 创建家庭
//     * @param name 房间名称
//     * @param lon 精度
//     * @param lat 纬度
//     * @param geoName 地理名字庫
//     * @param rooms 创建房间列表
//     *
//     * @return 返回创建成功的房间HomeBean
//     */
//    suspend fun createHome(
//        name: String,
//        lon: Double = 0.0,
//        lat: Double = 0.0,
//        geoName: String? = null,
//        rooms: List<String> = emptyList()
//    ): HomeBean
//
//    /**
//     * 获取家庭详细信息
//     * @param homeId 房间ID
//     * @return HomeBean
//     */
//    suspend fun getHomeDetail(homeId: Long): HomeBean
//
//    /**
//     * 查询可以创组建群组的设备列表
//     * @param homeId 家庭 id
//     * @param groupId 群组未创建，入参 groupId 传-1；已有群组，请传实际群组 ID
//     * @param productId 选择创建群组的设备的 pid
//     *
//     * @see{https://developer.tuya.com/cn/docs/app-development/group?id=Ka6ki8l6zjfhj}
//     */
//    suspend fun queryDeviceListToAddGroup(homeId: Long, groupId: Long, productId: String): List<GroupDeviceBean>
//
//    /**
//     * 创建群组
//     * @param homeId 家庭ID
//     * @param productId 产品ID
//     * @param name 群组名称
//     * @param deviceIds 设备ID列表
//     * @return 返回创建成功的群组ID
//     *
//     * @see{https://developer.tuya.com/cn/docs/app-development/group?id=Ka6ki8l6zjfhj}
//     */
//    suspend fun createGroup(homeId: Long, productId: String, name: String, deviceIds: List<String>): Long
//
//    /**
//     * 查询家庭成员
//     * @param homeId 家庭ID
//     * @return 返回家庭成员列表
//     */
//    suspend fun queryMemberList(homeId: Long): List<MemberBean>
//
//    /**
//     * 查询家庭分享用户列表信息
//     * @param homeId 家庭ID
//     * @return 返回家庭分享用户列表
//     */
//    suspend fun queryUserShareList(homeId: Long): List<SharedUserInfoBean>
//
//    /**
//     * 添加家庭成员
//     * @param homeId      家庭ID
//     * @param countryCode 国家码
//     * @param userAccount 要添加的用户账号
//     * @param name        添加的用户名
//     * @param isAdmin     是不是admin
//     *
//     * @return
//     */
//    suspend fun addMember(homeId: Long, countryCode: String, userAccount: String, name: String, isAdmin: Boolean): MemberBean
//
//    /**
//     * 分享家庭
//     * @param homeId      家庭ID
//     * @param countryCode 国家码
//     * @param userAccount 要添加的用户账号
//     * @param deviceIds   分享的设备列表
//     *
//     * @return SharedUserInfoBean 返回分享者信息
//     */
//    suspend fun addShareWithHomeId(homeId: Long, countryCode: String, userAccount: String, deviceIds: List<String>): SharedUserInfoBean
//
//    /**
//     * 移除家庭
//     * @param homeId     家庭ID
//     */
//    suspend fun dismissHome(homeId: Long): Boolean
//
//    /**
//     * 更新房间名称
//     * @param homeId     家庭ID
//     * @param name       更新的家庭名称
//     */
//    suspend fun updateHome(homeId: Long, name: String): Boolean
//
//    /**
//     * 移除家庭成员
//     * @param memberId 家庭成员ID
//     */
//    suspend fun removeMember(memberId: Long): Boolean
//
//    /**
//     * 移除家庭分享成员
//     * @param memberId 成员ID
//     */
//    suspend fun removeUserShare(memberId: Long): Boolean
//
//    /**
//     * 获取分享用户信息
//     * @param memberId 成员ID
//     *
//     * @return ShareSentUserDetailBean 返回分享的用户详细信息
//     */
//    suspend fun getUserShareInfo(memberId: Long): ShareSentUserDetailBean
//
//    /**
//     * @param memberId 分享者ID
//     * @param name 分享的用户名
//     * 重命名分享用户的昵称
//     */
//    suspend fun renameShareNickname(memberId: Long, name: String): Boolean
}