package com.apemans.tuya.component.repository

import androidx.lifecycle.LiveData
import com.apemans.quickui.multitype.ItemsLiveData
import com.apemans.router.routerServices
import com.apemans.tuya.module.api.exception.TuyaException
import com.apemans.userapi.services.UserService
import com.dylanc.longan.Logger
import com.dylanc.longan.application
import com.dylanc.longan.logDebug
import com.google.gson.Gson
import com.tuya.smart.android.user.api.ILoginCallback
import com.tuya.smart.android.user.bean.User
import com.tuya.smart.api.service.MicroServiceManager
import com.tuya.smart.commonbiz.bizbundle.family.api.AbsBizBundleFamilyService
import com.tuya.smart.home.sdk.TuyaHomeSdk
import com.tuya.smart.home.sdk.bean.*
import com.tuya.smart.home.sdk.builder.ActivatorBuilder
import com.tuya.smart.home.sdk.callback.*
import com.tuya.smart.sdk.api.*
import com.tuya.smart.sdk.bean.DeviceBean
import com.tuya.smart.sdk.bean.GroupBean
import com.tuya.smart.sdk.bean.GroupDeviceBean
import com.tuya.smart.sdk.bean.TimerTask
import com.tuya.smart.sdk.bean.message.MessageBean
import com.tuya.smart.sdk.enums.ActivatorModelEnum
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

/**
 * @author Dylan Cai
 */

@Suppress("ObjectPropertyName")
object TuyaRepository : Logger {

    //    private val tuyaSupportDeviceDataSource = TuyaSupportDeviceDataSource()
//    val supportCategories = tuyaSupportDeviceDataSource.supportCategories
    private val userService: UserService by routerServices()

    var selectedHomeId: Long = -1
        private set

    private val homeListCache = mutableListOf<HomeBean>()
    private val _homeList = ItemsLiveData<HomeBean>()
    val homeList: LiveData<List<HomeBean>> get() = _homeList

    private val _supportedDevices = ItemsLiveData<Any>()
    val supportedDevices: LiveData<List<Any>> get() = _supportedDevices

    private val _deviceList = ItemsLiveData<DeviceBean>()
    val deviceList: LiveData<List<DeviceBean>> get() = _deviceList

    private var groupListCache = mutableListOf<GroupBean>()
    private val _groupList = ItemsLiveData<GroupBean>()
    val groupList: LiveData<List<GroupBean>> get() = _groupList

    private val remoteDataSource = TuyaRemoteDataSource()

    fun queryHomeList() = flow {
        val list = remoteDataSource.queryHomeList()
        homeListCache.clear()
        homeListCache.addAll(list)
        _homeList.postValue(list)
        getHomeDetail(list.first().homeId)
        emit(list)
    }

    fun queryDeviceList() = flow {
        if (selectedHomeId == -1) {
            val homeList = remoteDataSource.queryHomeList()
            selectedHomeId = homeList.first().homeId
        }
        emit(remoteDataSource.getHomeDetail(selectedHomeId).deviceList)
    }

    fun selectHome(homeId: Long) = flow {
        emit(getHomeDetail(homeId))
    }

    fun getDeviceBean(deviceId: String): DeviceBean? =
        remoteDataSource.getDeviceBean(deviceId)

    fun getWeather(lon: Double, lat: Double) = flow {
        emit(remoteDataSource.getWeather(selectedHomeId, lon, lat))
    }

    fun updateDevicesAndGroups() = flow {
        val homeBean = remoteDataSource.getHomeDetail(selectedHomeId)
        _deviceList.postValue(homeBean.deviceList)
        groupListCache = homeBean.groupList
        _groupList.postValue(groupListCache)
        emit(Unit)
    }

    private suspend fun getHomeDetail(homeId: Long) {
        val homeBean = remoteDataSource.getHomeDetail(homeId)
        _deviceList.postValue(homeBean.deviceList)
        groupListCache = homeBean.groupList
        _groupList.postValue(groupListCache)
        selectedHomeId = homeId
        val service = MicroServiceManager.getInstance().findServiceByInterface<AbsBizBundleFamilyService>(AbsBizBundleFamilyService::class.java.name)
        //设置为当前家庭的homeId
        service.currentHomeId = selectedHomeId
    }

    fun createHome(name: String) = flow {
        val home = remoteDataSource.createHome(name)
        homeListCache.add(home)
        _homeList.value = homeListCache
        emit(Unit)
    }

    fun createGroup(productId: String, name: String, deviceIds: List<String>) = flow {
        val group = remoteDataSource.createGroup(selectedHomeId, productId, name, deviceIds)
        groupListCache.add(0, group)
        _groupList.value = groupListCache
        emit(Unit)
    }

//    fun getSupportDevice(type: String) {
//        val supportList = when (type) {
//            application.getString(R.string.electrical) -> {
//                tuyaSupportDeviceDataSource.electricalDevices
//            }
//            application.getString(R.string.lighting) -> {
//                tuyaSupportDeviceDataSource.lightingDevices
//            }
//            else -> throw IllegalArgumentException()
//        }
//        _supportedDevices.value = supportList
//    }

    fun queryDeviceListToAddGroup(homeId: Long, productId: String) = flow {
        val list = remoteDataSource.queryDeviceListToAddGroup(homeId, -1, productId)
        emit(list)
    }

    fun queryMemberList(homeId: Long) = flow {
        emit(remoteDataSource.queryMemberList(homeId))
    }

    fun queryUserShareList(homeId: Long) = flow {
        emit(remoteDataSource.queryUserShareList(homeId))
    }

    fun queryHomeDeviceList(homeId: Long) = flow {
        val homeDetail = remoteDataSource.getHomeDetail(homeId)
        emit(homeDetail.deviceList)
    }

    fun addMember(
        homeId: Long,
        email: String,
        countryCode: String,
        name: String,
        isAdmin: Boolean
    ) = flow {
        val uid = userService.getUidByAccount(email).single().data.uid
        emit(remoteDataSource.addMember(homeId, countryCode, uid, name, isAdmin))
    }

    fun queryUserShared(homeId: Long, email: String) = flow {
        val uid = userService.getUidByAccount(email).single().data.uid
        if (remoteDataSource.queryUserShareList(homeId).any { it.userName == uid }) {
            emit(null)
        } else {
            emit(uid)
        }
    }

    fun addShareWithHomeId(
        homeId: Long,
        countryCode: String,
        userAccount: String,
        deviceIds: List<String>
    ) = flow {
        emit(remoteDataSource.addShareWithHomeId(homeId, countryCode, userAccount, deviceIds))
    }

    fun dismissHome(homeId: Long) = flow {
        remoteDataSource.dismissHome(homeId)
        homeListCache.removeAll { it.homeId == homeId }
        _homeList.value = homeListCache
        emit(Unit)
    }

    fun dismissGroup(groupId: Long) = flow {
        val success = remoteDataSource.dismissGroup(groupId)
        if (success) {
            groupListCache.removeAll { it.id == groupId }
            _groupList.value = groupListCache
        }
        emit(success)
    }

    fun updateDeviceListForGroup(groupId: Long, deviceIds: List<String>) = flow {
        emit(remoteDataSource.updateDeviceListForGroup(groupId, deviceIds))
    }

    fun updateHome(homeId: Long, name: String) = flow {
        remoteDataSource.updateHome(homeId, name)
        homeListCache.find { it.homeId == homeId }?.name = name
        _homeList.value = homeListCache
        emit(Unit)
    }

    fun getAccount(uid: String) = flow {
        val response = userService.getAccountByUid(uid).single()
        if (response.code == 1055) {
            throw IllegalStateException("账号不存在")
        }
        emit(response.data.account)
    }

    fun getUid(account: String) = flow {
        val response = userService.getUidByAccount(account).single()
        if (response.code == 1055) {
            throw IllegalStateException("账号不存在")
        }
        emit(response.data.uid)
    }

    fun getTimerWithTask(taskName: String, devId: String) = flow {
        emit(remoteDataSource.getTimerWithTask(taskName, devId))
    }

    fun getUserShareInfo(memberId: Long) = flow {
        emit(remoteDataSource.getUserShareInfo(memberId))
    }

    fun queryDevShareUserList(deviceId: String) = flow {
        emit(remoteDataSource.queryDevShareUserList(deviceId))
    }

    fun removeMember(memberId: Long) = flow {
        emit(remoteDataSource.removeMember(memberId))
    }

    fun removeUserShare(memberId: Long) = flow {
        emit(remoteDataSource.removeUserShare(memberId))
    }

    fun removeDevice(deviceId: String) = flow {
        emit(remoteDataSource.removeDevice(deviceId))
    }

    fun resetFactory(deviceId: String) = flow {
        emit(remoteDataSource.resetFactory(deviceId))
    }

    fun removeShareDevice(deviceId: String) = flow {
        emit(remoteDataSource.removeShareDevice(deviceId))
    }

    fun renameShareNickname(memberId: Long, name: String) = flow {
        emit(remoteDataSource.renameShareNickname(memberId, name))
    }

    fun addTimerWithTask(taskName: String, devId: String, loops: String, dps: Map<String, Any?>, time: String) = flow {
        emit(remoteDataSource.addTimerWithTask(taskName, devId, loops, dps, time))
    }

    fun ezPair(ssid: String, pwd: String) =
        remoteDataSource.ezPair(ssid, pwd, selectedHomeId)
            .map {
                _deviceList.value = _deviceList.value.orEmpty().toMutableList().apply {
                    add(0, it)
                }
            }

    var threeHolesPowerStripWallSwitchList = listOf(
        "iGuAESc6917owGUr",
        "ugdkg6rn63peraai",
        "ycccdik7krsxuybg"
    )

    val SMART_STRIP_PRODUCTID_FOUR = "j7ewsefbjxaprlqy"

    var dimmerPlugList = listOf(
        "rk9wwke99mdncz2n",
        "qtbtyifjcm3ou6f1"
    )

    var powerStripMultiWallList = listOf(
        "01wjigkru2tgixxp",
        "omwxkdvwpxtyjans",
        "EQD8hAQw543vzh6O",
        "J4b9HONUUjrBxJXK"
    )

    var oldLampList = listOf(
        "isehgkqn5uqlrorl",
        "fnxgcsysunpyxkou",
        "ttrn0dlxota0rrav",
        "nscnnpeguv620u4f"
    )
    var lampList = listOf(
        "hdnoe1sqimwad9f4",
        "gswrpjab2vfawful",
        "5abhrka6ejfr0hvx",
        "5at5rg1h1viwp6ol",
        "gmjdt6mvy1mntvqn",
        "g56afofns8lpko6v",
        "behqxmx1m8e4sr08",
        "b6vjkghax6amtwdf",
        "1ogqu4bxwzrjxu8v",
        "c2uurdygilpx7nko",
        "s3a7fbytlm1gprhz",
        "ee2nlhpqplatlcoh",
        "8vbbgyn8uosnntvf",
        "rcaduvje2rrpoe9d",
        "pahtehvb1nisjsa4",
        "ohmqju5mfrmjktsu",
        "trtvoqie5as0bfpo"
    )

    var normalSwitchList = listOf(
        "octeoqhuayzof69q",
        "iqgfsxokdkzzehmj",
        "4bVOiYN0zdh6vTYq",
        "dok3rzi3pnnqu6ju",
        "viv1giuyu2tk4kt4",
        "cya3zxfd38g4qp8d",
        "5bvnmoqjth5nd4de",
        "sOhGq6u1M2JwB5d8",
        "twezq8g8ykoaggey",
        "fbvia0apnlnattcy",
        "vnya2spfopsh9lro",
        "pJnpT0XcM5FTRjOd",
        "bYdRrWx5iLCyAfPs",
        "A6bBfm2fmKKRfIxU",
        "8jkyyvxsep3yr5ql",
        "z6ai9dh9a0aujfdy",
        "sfurldd0ddoc2mat",
        "f0o1eyjw1pfojq87",
        "99oomugbqvd1axj0",
        "ga8gcmwpkl3jc4ek",
        "ep0rgcsdmq4sp6cd",
        "zae4ua68xt9wxfap",
        "nkd4cs6u1vfu9ksi",
        "rofiypesat1paym2",
        "xyfjwup7nwnsqak7",
        "xmpy4utews9k3waf",
        "xajto1x7xm4w3x0s"
    )

    private const val switch_1_id = "1"
    private const val switch_2_id = "2"
    private const val switch_3_id = "3"
    private const val switch_4_id = "4"
    private const val switch_usb1_id = "5"
    private const val DP_ID_SWITCH_ON = "1"
    private const val OLD_LED_SWITCH_ID = "1"
    private const val NEW_LED_SWITCH_ID = "1"

    fun sendCommand(deviceBean: DeviceBean, on: Boolean) = flow {
        val productId = deviceBean.productId
        val dpsMap = when {
            threeHolesPowerStripWallSwitchList.contains(productId) ->
                mapOf(switch_1_id to on, switch_2_id to on, switch_3_id to on)
            productId == SMART_STRIP_PRODUCTID_FOUR ->
                mapOf(switch_1_id to on, switch_2_id to on, switch_3_id to on, switch_4_id to on, switch_usb1_id to on)
            dimmerPlugList.contains(productId) ->
                mapOf(DP_ID_SWITCH_ON to on)
            powerStripMultiWallList.contains(productId) ->
                mapOf(switch_1_id to on, switch_2_id to on)
            oldLampList.contains(productId) ->
                mapOf(OLD_LED_SWITCH_ID to on)
            lampList.contains(productId) ->
                mapOf(NEW_LED_SWITCH_ID to on)
            normalSwitchList.contains(productId) ->
                mapOf(DP_ID_SWITCH_ON to on)
            else -> throw IllegalArgumentException("设备不支持")
        }
        emit(remoteDataSource.sendDeviceCommand(deviceBean.devId, Gson().toJson(dpsMap)))
    }

    fun getMessageList(callback: ITuyaDataCallback<List<MessageBean>>) = remoteDataSource.getMessageList(callback)
}

class TuyaRemoteDataSource : Logger {
    suspend fun queryHomeList(): List<HomeBean> = suspendCoroutine {
        TuyaHomeSdk.getHomeManagerInstance().queryHomeList(object : ITuyaGetHomeListCallback {
            override fun onSuccess(homeBeans: MutableList<HomeBean>) {
                it.resume(homeBeans)
            }

            override fun onError(errorCode: String, error: String) {
                it.resumeWithException(TuyaException(errorCode, error))
            }
        })
    }

    suspend fun createHome(
        name: String,
        lon: Double = 0.0,
        lat: Double = 0.0,
        geoName: String? = null,
        rooms: List<String> = emptyList()
    ) = suspendCoroutine<HomeBean> {
        TuyaHomeSdk.getHomeManagerInstance()
            .createHome(name, lon, lat, geoName, rooms, object : ITuyaHomeResultCallback {
                override fun onSuccess(bean: HomeBean) {
                    it.resume(bean)
                }

                override fun onError(errorCode: String, errorMsg: String) {
                    it.resumeWithException(TuyaException(errorCode, errorMsg))
                }
            })
    }

    suspend fun getHomeDetail(homeId: Long) = suspendCoroutine<HomeBean> {
        TuyaHomeSdk.newHomeInstance(homeId).getHomeDetail(object : ITuyaHomeResultCallback {
            override fun onSuccess(bean: HomeBean) {
                it.resume(bean)
            }

            override fun onError(errorCode: String, errorMsg: String) {
                it.resumeWithException(TuyaException(errorCode, errorMsg))
            }
        })
    }

    fun getDeviceBean(deviceId: String) =
        TuyaHomeSdk.getDataInstance().getDeviceBean(deviceId)

    suspend fun queryDeviceListToAddGroup(homeId: Long, groupId: Long, productId: String) =
        suspendCoroutine<List<GroupDeviceBean>> {
            TuyaHomeSdk.newHomeInstance(homeId)
                .queryDeviceListToAddGroup(groupId, productId, object :
                    ITuyaResultCallback<List<GroupDeviceBean>> {
                    override fun onSuccess(groupDeviceBeans: List<GroupDeviceBean>?) {
                        it.resume(groupDeviceBeans.orEmpty())
                    }

                    override fun onError(errorCode: String, errorMsg: String) {
                        it.resumeWithException(TuyaException(errorCode, errorMsg))
                    }
                })
        }

    suspend fun queryDevShareUserList(deviceId: String) =
        suspendCoroutine<List<SharedUserInfoBean>> {
            TuyaHomeSdk.getDeviceShareInstance()
                .queryDevShareUserList(deviceId, object :
                    ITuyaResultCallback<List<SharedUserInfoBean>> {
                    override fun onSuccess(groupDeviceBeans: List<SharedUserInfoBean>) {
                        it.resume(groupDeviceBeans)
                    }

                    override fun onError(errorCode: String, errorMsg: String) {
                        it.resumeWithException(TuyaException(errorCode, errorMsg))
                    }
                })
        }

    suspend fun createGroup(
        homeId: Long,
        productId: String,
        name: String,
        deviceIds: List<String>
    ) = suspendCoroutine<GroupBean> {
        TuyaHomeSdk.newHomeInstance(homeId)
            .createGroup(productId, name, deviceIds, object : ITuyaResultCallback<Long> {
                override fun onSuccess(groupId: Long) {
                    it.resume(TuyaHomeSdk.getDataInstance().getGroupBean(groupId)!!)
                }

                override fun onError(errorCode: String, errorMsg: String) {
                    it.resumeWithException(TuyaException(errorCode, errorMsg))
                }
            })
    }

    suspend fun queryMemberList(homeId: Long) = suspendCoroutine<List<MemberBean>> {
        TuyaHomeSdk.getMemberInstance().queryMemberList(homeId, object :
            ITuyaGetMemberListCallback {
            override fun onSuccess(members: List<MemberBean>) {
                it.resume(members)
            }

            override fun onError(code: String, msg: String) {
                it.resumeWithException(TuyaException(code, msg))
            }
        })
    }

    suspend fun queryUserShareList(homeId: Long) = suspendCoroutine<List<SharedUserInfoBean>> {
        TuyaHomeSdk.getDeviceShareInstance()
            .queryUserShareList(homeId, object : ITuyaResultCallback<List<SharedUserInfoBean>> {
                override fun onSuccess(members: List<SharedUserInfoBean>) {
                    it.resume(members)
                }

                override fun onError(code: String, msg: String) {
                    it.resumeWithException(TuyaException(code, msg))
                }
            })
    }

    suspend fun getTimerWithTask(taskName: String, devId: String) = suspendCoroutine<TimerTask> {
        TuyaHomeSdk.getTimerManagerInstance()
            .getTimerWithTask(taskName, devId, object : IGetTimerWithTaskCallback {

                override fun onSuccess(timerTask: TimerTask) {
                    it.resume(timerTask)
                }

                override fun onError(code: String, msg: String) {
                    it.resumeWithException(TuyaException(code, msg))
                }
            })
    }

    suspend fun addTimerWithTask(taskName: String, devId: String, loops: String, dps: Map<String, Any?>, time: String) = suspendCoroutine<Unit> {
        TuyaHomeSdk.getTimerManagerInstance()
            .addTimerWithTask(taskName, devId, loops, dps, time, object : IResultStatusCallback {
                override fun onSuccess() {
                    it.resume(Unit)
                }

                override fun onError(code: String, msg: String) {
                    it.resumeWithException(TuyaException(code, msg))
                }
            })
    }

    suspend fun addMember(
        homeId: Long,
        countryCode: String,
        userAccount: String,
        name: String,
        isAdmin: Boolean
    ) = suspendCoroutine<MemberBean> {
        TuyaHomeSdk.getMemberInstance()
            .addMember(homeId, countryCode, userAccount, name, isAdmin, object :
                ITuyaMemberResultCallback {
                override fun onSuccess(memberBean: MemberBean) {
                    it.resume(memberBean)
                }

                override fun onError(code: String, msg: String) {
                    it.resumeWithException(TuyaException(code, msg))
                }
            })
    }

    suspend fun addShareWithHomeId(
        homeId: Long,
        countryCode: String,
        userAccount: String,
        deviceIds: List<String>
    ) = suspendCoroutine<SharedUserInfoBean> {
        TuyaHomeSdk.getDeviceShareInstance()
            .addShareWithHomeId(homeId, countryCode, userAccount, deviceIds,
                object : ITuyaResultCallback<SharedUserInfoBean> {
                    override fun onSuccess(sharedUserInfoBean: SharedUserInfoBean) {
                        it.resume(sharedUserInfoBean)
                    }

                    override fun onError(code: String, msg: String) {
                        it.resumeWithException(TuyaException(code, msg))
                    }
                })
    }

    suspend fun dismissHome(homeId: Long) = suspendCoroutine<Boolean> {
        TuyaHomeSdk.newHomeInstance(homeId).dismissHome(object : IResultCallback {
            override fun onSuccess() {
                it.resume(true)
            }

            override fun onError(code: String, msg: String) {
                it.resumeWithException(TuyaException(code, msg))
            }
        })
    }

    suspend fun updateHome(homeId: Long, name: String) = suspendCoroutine<Boolean> {
        val homeBean = TuyaHomeSdk.getDataInstance().getHomeBean(homeId)!!
        TuyaHomeSdk.newHomeInstance(homeBean.homeId)
            .updateHome(name, homeBean.lon, homeBean.lat, homeBean.geoName,
                object : IResultCallback {
                    override fun onSuccess() {
                        it.resume(true)
                    }

                    override fun onError(code: String, msg: String) {
                        it.resumeWithException(TuyaException(code, msg))
                    }
                })
    }

    suspend fun removeMember(memberId: Long) = suspendCoroutine<Boolean> {
        TuyaHomeSdk.getMemberInstance().removeMember(memberId,
            object : IResultCallback {
                override fun onSuccess() {
                    it.resume(true)
                }

                override fun onError(code: String, msg: String) {
                    it.resumeWithException(TuyaException(code, msg))
                }
            })
    }

    suspend fun removeUserShare(memberId: Long) = suspendCoroutine<Boolean> {
        TuyaHomeSdk.getDeviceShareInstance().removeUserShare(memberId,
            object : IResultCallback {
                override fun onSuccess() {
                    it.resume(true)
                }

                override fun onError(code: String, msg: String) {
                    it.resumeWithException(TuyaException(code, msg))
                }
            })
    }

    suspend fun removeDevice(deviceId: String) = suspendCoroutine<Boolean> {
        TuyaHomeSdk.newDeviceInstance(deviceId).removeDevice(object : IResultCallback {
            override fun onSuccess() {
                it.resume(true)
            }

            override fun onError(code: String, msg: String) {
                it.resumeWithException(TuyaException(code, msg))
            }
        })
    }

    suspend fun resetFactory(deviceId: String) = suspendCoroutine<Boolean> {
        TuyaHomeSdk.newDeviceInstance(deviceId).resetFactory(object : IResultCallback {
            override fun onSuccess() {
                it.resume(true)
            }

            override fun onError(code: String, msg: String) {
                it.resumeWithException(TuyaException(code, msg))
            }
        })
    }

    suspend fun removeShareDevice(deviceId: String) = suspendCoroutine<Boolean> {
        TuyaHomeSdk.getDeviceShareInstance().removeReceivedDevShare(deviceId, object : IResultCallback {
            override fun onSuccess() {
                it.resume(true)
            }

            override fun onError(code: String, msg: String) {
                it.resumeWithException(TuyaException(code, msg))
            }
        })
    }

    suspend fun dismissGroup(groupId: Long) = suspendCoroutine<Boolean> {
        TuyaHomeSdk.newGroupInstance(groupId).dismissGroup(object : IResultCallback {
            override fun onSuccess() {
                it.resume(true)
            }

            override fun onError(code: String, msg: String) {
                it.resumeWithException(TuyaException(code, msg))
            }
        })
    }

    suspend fun updateDeviceListForGroup(groupId: Long, deviceIds: List<String>) = suspendCoroutine<Boolean> {
        TuyaHomeSdk.newGroupInstance(groupId).updateDeviceList(deviceIds, object : IResultCallback {
            override fun onSuccess() {
                it.resume(true)
            }

            override fun onError(code: String, msg: String) {
                it.resumeWithException(TuyaException(code, msg))
            }
        })
    }

    suspend fun getUserShareInfo(memberId: Long) = suspendCoroutine<ShareSentUserDetailBean> {
        TuyaHomeSdk.getDeviceShareInstance().getUserShareInfo(memberId,
            object : ITuyaResultCallback<ShareSentUserDetailBean> {
                override fun onSuccess(detailBean: ShareSentUserDetailBean) {
                    it.resume(detailBean)
                }

                override fun onError(code: String, msg: String) {
                    it.resumeWithException(TuyaException(code, msg))
                }
            })
    }

    suspend fun renameShareNickname(memberId: Long, name: String) = suspendCoroutine<Boolean> {
        TuyaHomeSdk.getDeviceShareInstance().renameShareNickname(memberId, name,
            object : IResultCallback {
                override fun onSuccess() {
                    it.resume(true)
                }

                override fun onError(code: String, msg: String) {
                    it.resumeWithException(TuyaException(code, msg))
                }
            })
    }

    suspend fun getActivatorToken(homeId: Long) = suspendCoroutine<String> {
        TuyaHomeSdk.getActivatorInstance().getActivatorToken(homeId, object : ITuyaActivatorGetToken {
            override fun onSuccess(token: String) {
                it.resume(token)
            }

            override fun onFailure(errorCode: String, errorMsg: String) {
                it.resumeWithException(TuyaException(errorCode, errorMsg))
            }
        })
    }

    suspend fun getWeather(homeId: Long, lon: Double, lat: Double) = suspendCoroutine<WeatherBean> {
        TuyaHomeSdk.newHomeInstance(homeId).getHomeWeatherSketch(lon, lat, object : IIGetHomeWetherSketchCallBack {
            override fun onSuccess(result: WeatherBean) {
                it.resume(result)
            }

            override fun onFailure(errorCode: String, errorMsg: String) {
                it.resumeWithException(TuyaException(errorCode, errorMsg))
            }
        })
    }

    private lateinit var tuyaActivator: ITuyaActivator

    @ExperimentalCoroutinesApi
    fun pair(ssid: String, pwd: String, token: String, mode: ActivatorModelEnum) = callbackFlow {
        tuyaActivator = TuyaHomeSdk.getActivatorInstance().newMultiActivator(
            ActivatorBuilder()
                .setContext(application)
                .setSsid(ssid)
                .setPassword(pwd)
                .setToken(token)
                .setActivatorModel(mode)
                .setTimeOut(100)
                .setListener(object : ITuyaSmartActivatorListener {
                    override fun onError(errorCode: String, errorMsg: String) {
                        cancel(errorMsg, TuyaException(errorCode, errorMsg))
                    }

                    override fun onActiveSuccess(devResp: DeviceBean) {
                        trySend(devResp)
                    }

                    override fun onStep(step: String?, data: Any?) {
                        logDebug(step)
                    }
                })
        )
        tuyaActivator.start()
        awaitClose { tuyaActivator.stop() }
    }

    fun ezPair(ssid: String, pwd: String, homeId: Long) =
        flow {
            emit(getActivatorToken(homeId))
        }.flatMapConcat {
            pair(ssid, pwd, it, ActivatorModelEnum.TY_EZ)
        }

    fun apPair(ssid: String, pwd: String, homeId: Long) =
        flow {
            emit(getActivatorToken(homeId))
        }.flatMapConcat {
            pair(ssid, pwd, it, ActivatorModelEnum.TY_AP)
        }

    suspend fun sendDeviceCommand(deviceId: String, dps: String) = suspendCoroutine<Unit> {
        val device = TuyaHomeSdk.newDeviceInstance(deviceId)
        device.registerDevListener(object : IDevListener {
            override fun onDpUpdate(devId: String?, dpStr: String?) {

            }

            override fun onRemoved(devId: String?) {

            }

            override fun onStatusChanged(devId: String?, online: Boolean) {

            }

            override fun onNetworkStatusChanged(devId: String?, status: Boolean) {

            }

            override fun onDevInfoUpdate(devId: String?) {

            }
        })
        device.publishDps(dps, object : IResultCallback {
            override fun onSuccess() {
            }

            override fun onError(code: String, error: String) {
                it.resumeWithException(TuyaException(code, error))
            }
        })
    }

    suspend fun sendGroupCommand(groupId: Long, dps: String) = suspendCoroutine<Unit> {
        TuyaHomeSdk.newGroupInstance(groupId).publishDps(dps, object : IResultCallback {
            override fun onSuccess() {
                it.resume(Unit)
            }

            override fun onError(code: String, error: String) {
                it.resumeWithException(TuyaException(code, error))
            }
        })
    }

    fun getMessageList(callback: ITuyaDataCallback<List<MessageBean>>) {
        TuyaHomeSdk.getMessageInstance()
            .getMessageList(callback)
    }

    suspend fun loginWithUid(countryCode: String, uid: String,pwd: String) = suspendCoroutine<User> {
        TuyaHomeSdk.getUserInstance().loginWithUid(countryCode,uid,pwd, object : ILoginCallback {
            override fun onSuccess(user: User) {
                it.resume(user)
            }

            override fun onError(code: String, error: String) {
                it.resumeWithException(TuyaException(code, error))
            }
        })
    }
}