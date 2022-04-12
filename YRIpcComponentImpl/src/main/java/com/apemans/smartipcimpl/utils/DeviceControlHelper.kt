/*
 * Copyright (c) 2021 独角鲸 Inc. All rights reserved.
 */

package com.apemans.smartipcimpl.utils

import android.content.Context
import com.dylanc.longan.application
import com.google.gson.reflect.TypeToken
import com.apemans.base.utils.JsonConvertUtil
import com.apemans.smartipcapi.info.BLEDeviceInfo
import com.apemans.smartipcapi.info.IpcDeviceInfo
import com.apemans.dmapi.model.DeviceModel
import com.apemans.dmapi.status.DeviceDefine
import com.apemans.dmapi.status.DeviceDefine.BIND_TYPE_OWNER
import com.apemans.dmapi.support.DeviceSupportMainCategory
import com.apemans.dmapi.support.SupportBrand
import com.apemans.dmapi.support.SupportPlatform
import com.apemans.smartipcapi.webapi.PIRPlanConfigure
import com.apemans.ipcchipproxy.define.*
import com.apemans.ipcchipproxy.scheme.bean.DetectionPlanSchedule
import com.apemans.ipcchipproxy.scheme.bean.IpcDPCmdData
import com.apemans.ipcchipproxy.scheme.bean.NetSpotDeviceInfo
import com.apemans.smartipcapi.api.IIpcDeviceHelper
import com.apemans.smartipcapi.bean.IpcP2PConnectionInfo
import com.apemans.smartipcapi.bean.NetSpotDeviceConfigure
import com.apemans.smartipcimpl.R
import com.apemans.smartipcimpl.bean.DeviceConfigureModel
import com.apemans.smartipcimpl.constants.*
import com.apemans.smartipcimpl.repository.DeviceManagerRepository
import com.apemans.base.utils.DataConvertUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.io.InputStream
import java.lang.Exception
import java.lang.StringBuilder
import kotlin.math.floor

/***********************************************************
 * 作者: zhengruidong@apemans.com
 * 日期: 2021/10/14 11:45 上午
 * 说明:
 *
 * 备注:
 *
 ***********************************************************/
object DeviceControlHelper : IIpcDeviceHelper {

    private const val DEVICE_CONFIGURE_MODEL_PATH = "YRDeviceConfigureModel.json"

    private var deviceConfigureModels : MutableList<DeviceConfigureModel> = mutableListOf()
    private var netSpotDeviceConfigure : NetSpotDeviceConfigure = NetSpotDeviceConfigure()

    fun initDeviceConfigureModelFromAsset() {
        if (!deviceConfigureModels.isNullOrEmpty()) {
            return
        }
        var configureModels : List<DeviceConfigureModel>? = convertJsonFileFromAssert(application, DEVICE_CONFIGURE_MODEL_PATH)?.let {
            if (it.isEmpty()) {
                null
            } else {
                JsonConvertUtil.convertData(it, object : TypeToken<List<DeviceConfigureModel>>(){})
            }
        }
        if (!configureModels.isNullOrEmpty()) {
            deviceConfigureModels.addAll(configureModels)
        }
    }

    fun getDeviceConfigureModelByModel(model: String) : DeviceConfigureModel? {
        return deviceConfigureModels.find { configureModel ->
            checkMatchModel(model, configureModel.model)
        }
    }

    fun createYRIpcConfigure(deviceInfo : IpcDeviceInfo) : Map<String, Any> {
        val result = mutableMapOf<String, Any>()
        return deviceInfo.run {
            result.put(IPC_SCHEME_KEY_DEVICE_ID, deviceInfo.device_id)
            result.put(IPC_SCHEME_KEY_MODEL, deviceInfo.model.orEmpty())
            result.put(IPC_SCHEME_KEY_UID, obtainUid())
            var serverDomain = if (deviceInfo.hbDomain.isNullOrEmpty()) IPv4IntTransformer.littleNumToIP(deviceInfo.hbServer) else deviceInfo.hbDomain
            result.put(IPC_SCHEME_KEY_SERVER_DOMAIN, serverDomain)
            result.put(IPC_SCHEME_KEY_SERVER_PORT, deviceInfo.hbPort)
            result.put(IPC_SCHEME_KEY_SECRET, deviceInfo.secret.orEmpty())
            getDeviceConfigureModelByModel(deviceInfo.model)?.run {
                result.put(IPC_SCHEME_KEY_DPS, JsonConvertUtil.convertToJson(dps).orEmpty())
                result.put(IPC_SCHEME_KEY_MODEL_TYPE, modelType)
                result.put(IPC_SCHEME_KEY_CONN_TYPE, connType)
                result.put(IPC_SCHEME_KEY_CMD_TYPE, cmdType)
            }
            result.put(IPC_SCHEME_KEY_PARENT_DEVICE_ID, deviceInfo.pDeviceId)
            result
        }
    }

    fun createYRIpcConfigure(deviceInfo : IpcP2PConnectionInfo) : Map<String, Any> {
        val result = mutableMapOf<String, Any>()
        return deviceInfo.run {
            result.put(IPC_SCHEME_KEY_DEVICE_ID, deviceInfo.deviceId.orEmpty())
            result.put(IPC_SCHEME_KEY_MODEL, deviceInfo.model.orEmpty())
            result.put(IPC_SCHEME_KEY_UID, deviceInfo.uid.orEmpty())
            var serverDomain = if (deviceInfo.hbDomain.isNullOrEmpty()) IPv4IntTransformer.littleNumToIP(deviceInfo.hbServer) else deviceInfo.hbDomain.orEmpty()
            result.put(IPC_SCHEME_KEY_SERVER_DOMAIN, serverDomain)
            result.put(IPC_SCHEME_KEY_SERVER_PORT, deviceInfo.hbPort)
            result.put(IPC_SCHEME_KEY_SECRET, deviceInfo.secret.orEmpty())
            getDeviceConfigureModelByModel(deviceInfo.model.orEmpty())?.run {
                result.put(IPC_SCHEME_KEY_DPS, JsonConvertUtil.convertToJson(dps).orEmpty())
                result.put(IPC_SCHEME_KEY_MODEL_TYPE, modelType)
                result.put(IPC_SCHEME_KEY_CONN_TYPE, connType)
                result.put(IPC_SCHEME_KEY_CMD_TYPE, cmdType)
            }
            result.put(IPC_SCHEME_KEY_PARENT_DEVICE_ID, deviceInfo.parentDeviceId.orEmpty())
            result
        }
    }

    fun convertIpcP2PConnectionInfo(deviceInfo : IpcDeviceInfo, userId: String) : IpcP2PConnectionInfo {
        return IpcP2PConnectionInfo().apply {
            deviceId = deviceInfo.device_id
            parentDeviceId = deviceInfo.pDeviceId
            model = deviceInfo.model
            uid = userId
            hbDomain = deviceInfo.hbDomain
            hbServer = deviceInfo.hbServer
            hbPort = deviceInfo.hbPort
            secret = deviceInfo.secret
            online = deviceInfo.online == DeviceDefine.ONLINE
        }
    }

    fun createYRIpcConfigure(deviceInfo : NetSpotDeviceInfo) : Map<String, Any> {
        val result = mutableMapOf<String, Any>()
        return deviceInfo.run {
            result.put(IPC_SCHEME_KEY_DEVICE_ID, deviceInfo.deviceId.orEmpty())
            result.put(IPC_SCHEME_KEY_MODEL, deviceInfo.model.orEmpty())
            result.put(IPC_SCHEME_KEY_UID, obtainUid())
            getDeviceConfigureModelByModel(deviceInfo.model.orEmpty())?.run {
                result.put(IPC_SCHEME_KEY_DPS, JsonConvertUtil.convertToJson(dps).orEmpty())
                result.put(IPC_SCHEME_KEY_MODEL_TYPE, modelType)
                result.put(IPC_SCHEME_KEY_CONN_TYPE, connType)
                result.put(IPC_SCHEME_KEY_CMD_TYPE, cmdType)
            }
            result
        }
    }

    fun convertIpcDevice(netSpotDeviceInfo : NetSpotDeviceInfo?, bleDeviceInfo: BLEDeviceInfo?) : DeviceModel? {
        return netSpotDeviceInfo?.let {
            var deviceInfo = IpcDeviceInfo(it.model.orEmpty(), bleDeviceInfo?.name.orEmpty(), DeviceSupportMainCategory.ipc, DeviceManagerHelper.convertIpcSubCategory(it.model.orEmpty()), it.deviceId.orEmpty(), SupportPlatform.selfDevelopProtocolPlatform, "", SupportBrand.osaio_brand, DeviceManagerHelper.convertIpcCommunicationType(it.model.orEmpty()))
            deviceInfo.apply {
                online = DeviceDefine.ONLINE
                openStatus = DeviceDefine.SWITCH_ON
                bindType = DeviceDefine.BIND_TYPE_OWNER
            }
            var deviceModel = DeviceModel(deviceInfo)
            deviceModel
        }
    }

    fun <T> createDpCmd(dpId: String?, deviceId: String, authority : String? = null, data: T? = null) : String {
        if (dpId.isNullOrEmpty()) {
            return ""
        }
        var dpCmd = mutableMapOf<String, Any>()
        dpCmd[dpId] = JsonConvertUtil.convertToJson(IpcDPCmdData<T>().apply {
            this.deviceId = deviceId
            this.data = data
            this.authority = authority
        }).orEmpty() ?: ""
        return JsonConvertUtil.convertToJson(dpCmd).orEmpty()
    }

    fun checkMatchModel(model : String?, targetModel : String?) : Boolean {
        return !model.isNullOrEmpty() && !targetModel.isNullOrEmpty() && model.startsWith(targetModel, true)
    }

    /**
     *
     * @param context
     * @param assetPath assert文件夹下面的相对路径
     * @return
     */
    fun convertJsonFileFromAssert(context: Context, assetPath: String?): String? {
        if (context == null || assetPath.isNullOrEmpty()) {
            return null
        }
        var result: String? = ""
        try {
            val inputStream = context.resources.assets.open(assetPath!!)
            result = streamToString(inputStream)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return result
    }

    fun convertDetectionSchedule(planList: List<PIRPlanConfigure>?) : List<DetectionPlanSchedule>? {
        return planList?.map {
            DetectionPlanSchedule().apply {
                id = it.id
                startTime = it.startTime
                endTime = it.endTime
                weekArr = it.weekArr
            }
        }
    }

    fun convertPIRPlanConfigure(planSchedule: DetectionPlanSchedule) : PIRPlanConfigure {
        return PIRPlanConfigure().apply {
            id = planSchedule.id
            startTime = planSchedule.startTime
            endTime = planSchedule.endTime
            weekArr = planSchedule.weekArr
        }
    }

    fun convertPIRPlanConfigures(planSchedule: DetectionPlanSchedule, pirPlanConfigureList: List<PIRPlanConfigure>?) : List<PIRPlanConfigure> {
        val result = mutableListOf<PIRPlanConfigure>()
        val pirPlanConfigure = convertPIRPlanConfigure(planSchedule)
        if (pirPlanConfigureList.isNullOrEmpty()) {
            planSchedule.id = 1
            result.add(pirPlanConfigure)
            return result
        }

        val configureExist = pirPlanConfigureList.find {
            it.id == planSchedule.id
        }?.apply {
            id = planSchedule.id
            startTime = planSchedule.startTime
            endTime = planSchedule.endTime
            weekArr = planSchedule.weekArr
        } != null
        if (configureExist) {
            result.addAll(pirPlanConfigureList)
            return result
        }
        val pirConfigureSize = pirPlanConfigureList.size
        for (i in 0 until pirConfigureSize) {
            pirPlanConfigureList.getOrNull(i)?.let {
                it.id = i + 1
                result.add(it)
            }
        }
        if (pirConfigureSize >= DETECTION_PLAN_SCHEDULE_SIZE) {
            result.removeLast()
        }
        result.add(pirPlanConfigure.apply { id = result.size + 1 })
        return result
    }

    fun convertDetectionScheduleTime(startTime: Int, endTime: Int) : String {
        return DateTimeToolUtil.convertFormatTimeByMin(startTime).plus(" ~ ").plus(DateTimeToolUtil.convertFormatTimeByMin(endTime))
    }

    fun convertDetectionScheduleWeekDay(weekArr: List<Int>?) : String {
        if (weekArr.isNullOrEmpty() || weekArr.size != 7) {
            return ""
        }
        val weekDayTextList = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")
        val weekDayValidNum = weekArr.sum()
        var weekDayText = ""
        if (weekDayValidNum == 7) {
            weekDayText = "Every Day"
        } else if (weekDayValidNum == 2 && weekArr.getOrNull(7 - 1) == 1 && weekArr.getOrNull(7 - 2) == 1) {
            weekDayText = "Weekend"
        } else if (weekDayValidNum == 5 && !(weekArr.getOrNull(7 - 1) == 1 && weekArr.getOrNull(7 - 2) == 1)) {
            weekDayText = "Working Day"
        } else {
            weekDayText = weekArr.mapIndexed { index, i ->
                if (i == 1) (weekDayTextList.getOrNull(index) ?: "") else ""
            }.filter {
                !it.isNullOrEmpty()
            }.joinToString()
        }
        return weekDayText
    }

    fun computeHourForTime(min: Int) : Int {
        return min / MIN_LENGTH_OF_HOUR
    }

    fun computeMinForTime(min: Int) : Int {
        return min % MIN_LENGTH_OF_HOUR
    }

    fun computeTimeByHourAndMin(hour: Int, min: Int) : Int {
        return hour * MIN_LENGTH_OF_HOUR + min
    }

    fun checkDetectionScheduleValid(id: Int, startTime: Int, endTime: Int, weekArr: List<Int>?) : Boolean {
        val scheduleIsValid = id > 0 && startTime >= 0 && startTime < endTime && weekArr?.size == WEEK_DAY_LENGTH
        return scheduleIsValid
    }

    fun convertFlashLightStateTitleByMode(context: Context, mode: Int) : String {
        return when(mode) {
            IPC_SCHEME_FLASH_LIGHT_MODE_COLOR -> {
                context.getString(R.string.camera_share_title)
            }
            IPC_SCHEME_FLASH_LIGHT_MODE_ALARM -> {
                context.getString(R.string.camera_share_title)
            }
            else -> {
                context.getString(R.string.camera_share_title)
            }
        }
    }

    fun convertFlashLightStateContentByMode(context: Context, mode: Int) : String {
        return when(mode) {
            IPC_SCHEME_FLASH_LIGHT_MODE_COLOR -> {
                context.getString(R.string.camera_share_title)
            }
            IPC_SCHEME_FLASH_LIGHT_MODE_ALARM -> {
                context.getString(R.string.camera_share_title)
            }
            else -> {
                context.getString(R.string.camera_share_title)
            }
        }
    }

    fun convertMediaModeTitleByMode(context: Context, mode: Int) : String {
        return when(mode) {
            IPC_SCHEME_SHOOTING_IMAGE -> {
                context.getString(R.string.camera_share_title)
            }
            IPC_SCHEME_SHOOTING_VIDEO -> {
                context.getString(R.string.camera_share_title)
            }
            else -> {
                context.getString(R.string.camera_share_title)
            }
        }
    }

    fun convertStorageCapacity(size: Int) : Double {
        var capacity = floor((size / 1024.0) * 10 + 0.5) / 10
        return Math.max(0.0, capacity)
    }

    override fun updateNetSpotDeviceConfigure(uid : String, callback : ((Boolean) -> Unit)?) {
        GlobalScope.launch {
            flow<Boolean> {
                var deviceId = IpcDeviceCmdHelper.getNetSpotDeviceInfo()?.deviceId
                if (deviceId.isNullOrEmpty()) {
                    emit(false)
                }
                var bleDeviceInfo = (DeviceManagerRepository.getBleIpcDevice(uid, deviceId.orEmpty()).single()?.device?.deviceInfo as? BLEDeviceInfo)
                var deviceModel = convertIpcDevice(IpcDeviceCmdHelper.getNetSpotDeviceInfo(), bleDeviceInfo)
                updateNetSpotDeviceConfigure(IpcDeviceCmdHelper.getNetSpotDeviceInfo(), deviceModel)
                emit(true)
            }
                .flowOn(Dispatchers.IO)
                .catch { e ->
                    callback?.invoke(false)
                }
                .collect {
                    callback?.invoke(it)
                }
        }

    }

    override fun updateNetSpotDeviceConfigure(netSpotDeviceInfo: NetSpotDeviceInfo?, ipcDevice: DeviceModel?) {
        netSpotDeviceConfigure.apply {
            this.netSpotDeviceInfo = netSpotDeviceInfo
            this.ipcDevice = ipcDevice
        }
    }

    override fun getNetSpotDeviceConfigure(): NetSpotDeviceConfigure? = netSpotDeviceConfigure

    override fun checkNetSpotDeviceConfigureValid() : Boolean {
        return netSpotDeviceConfigure?.netSpotDeviceInfo != null && netSpotDeviceConfigure?.ipcDevice != null
    }

    override fun checkNetSpotSsidValid(ssid: String?) : Boolean {
        return ssid?.lowercase()?.let {
            it.startsWith(IPC_SCHEME_NET_SPOT_SSID_PREFIX_VICTURE) || it.startsWith(IPC_SCHEME_NET_SPOT_SSID_PREFIX_SECURITY_CAM)
                    || it.startsWith(IPC_SCHEME_NET_SPOT_SSID_PREFIX_TECKIN) || it.startsWith(IPC_SCHEME_NET_SPOT_SSID_PREFIX_OSAIO)
        } ?: false
    }

    override fun checkCloudValid(endTime: Long, deviceTimeZone: Float) : Boolean {
        var gapTime = 0
        var t: Long = ((System.currentTimeMillis() + gapTime * 1000L + (deviceTimeZone * 3600 * 1000L)) / 1000L).toLong()
        return endTime - t > 0
    }

    override fun checkCloudSubscribe(status: Int) : Boolean {
        return status != 0
    }

    override fun convertCloudExpireTime(time: Long) : Long {
        return if (time > 1) {
            (time - 1) * 1000L
        } else {
            0
        }
    }

    override fun createEnterMark(uid: String?): String? {
        return StringBuilder().append(uid).append("_").append(System.currentTimeMillis()).toString()
    }

    override fun createCloudPackUrl(webUrl: String?, deviceId: String?, model: String?, enterMark: String?, origin: String?): String? {
        if (webUrl.isNullOrEmpty()) {
            return ""
        }
        val urlSb = StringBuilder()
        urlSb.append(webUrl)
            .append("pack/list")
            .append("?")
            .append(KEY_CLOUD_PACK_PARAM_UUID)
            .append("=")
            .append(deviceId)
            .append("&")
            .append(KEY_CLOUD_PACK_PARAM_MODEL)
            .append("=")
            .append(model)
            .append("&")
            .append(KEY_CLOUD_PACK_PARAM_ENTER_MARK)
            .append("=")
            .append(enterMark)
            .append("&")
            .append(KEY_CLOUD_PACK_PARAM_ORIGIN)
            .append("=")
            .append(origin)
        return urlSb.toString()
    }

    /**
     *
     * @param version1
     * @param version2
     * @return version1 > version2 as 1, version1 == version2 as 0, version1 < version2 as -1
     */
    override fun compareVersion(version1: String, version2: String): Int {
        if (version1.isEmpty() || version2.isEmpty()) {
            return 0
        }
        val versionCodes1 = version1.split("\\.".toRegex()).toTypedArray()
        val versionCodes2 = version2.split("\\.".toRegex()).toTypedArray()
        return if (versionCodes1 != null && versionCodes2 != null && versionCodes1.size == versionCodes2.size && versionCodes1.size == 3) {
            if (DataConvertUtil.toInt(versionCodes1[0]) > DataConvertUtil.toInt(versionCodes2[0])) {
                1
            } else if (DataConvertUtil.toInt(versionCodes1[0]) == DataConvertUtil.toInt(versionCodes2[0])) {
                if (DataConvertUtil.toInt(versionCodes1[1]) > DataConvertUtil.toInt(versionCodes2[1])) {
                    1
                } else if (DataConvertUtil.toInt(versionCodes1[1]) == DataConvertUtil.toInt(versionCodes2[1])) {
                    if (DataConvertUtil.toInt(versionCodes1[2]) > DataConvertUtil.toInt(versionCodes2[2])) {
                        1
                    } else if (DataConvertUtil.toInt(versionCodes1[2]) == DataConvertUtil.toInt(versionCodes2[2])) {
                        0
                    } else {
                        -1
                    }
                } else {
                    -1
                }
            } else {
                -1
            }
        } else 0
    }

    override fun checkIsChildDevice(parentDeviceId: String?): Boolean {
        return !parentDeviceId.isNullOrEmpty() && parentDeviceId != "1"
    }

    override fun checkIsOwnerDevice(bindType: Int) : Boolean {
        return bindType == BIND_TYPE_OWNER
    }

    private fun streamToString(inputStream: InputStream?): String? {
        var result: String? = ""
        if (inputStream == null) {
            return result
        }
        try {
            val length = inputStream.available()
            val buffer = ByteArray(length)
            inputStream.read(buffer)
            result = String(buffer)
            inputStream.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return result
    }

    private fun obtainUid() : String {
        return ""
    }

}