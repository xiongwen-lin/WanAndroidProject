package com.apemans.smartipcimpl.repository

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.apemans.datastore.db.base.SmartDatabase
import com.apemans.datastore.db.entity.BleDeviceEntity
import com.apemans.datastore.db.entity.DeviceEntity
import com.apemans.smartipcapi.info.BLEDeviceInfo
import com.apemans.smartipcapi.info.IpcDeviceInfo
import com.apemans.dmapi.model.DeviceModel
import com.apemans.dmapi.support.DeviceSupportMainCategory
import com.apemans.dmapi.support.SupportBrand
import com.apemans.dmapi.support.SupportPlatform
import com.apemans.smartipcapi.webapi.*
import com.apemans.smartipcimpl.bean.IpcDevice
import com.apemans.smartipcimpl.db.DeviceManagerDbHelper
import com.apemans.smartipcimpl.utils.DeviceControlHelper
import com.apemans.smartipcimpl.utils.DeviceManagerHelper
import com.apemans.ipcchipproxy.scheme.bean.NetSpotDeviceInfo
import com.apemans.smartipcapi.webapi.FirmwareVersion
import com.apemans.smartipcapi.webapi.PackageInfoResult
import com.apemans.base.utils.JsonConvertUtil
import com.apemans.business.apisdk.client.bean.YRApiResponse
import com.apemans.business.apisdk.client.define.HttpCode
import com.apemans.smartipcapi.webapi.AwsFileListResult
import com.apemans.smartipcimpl.bean.DeviceInfoModel
import com.apemans.smartipcimpl.webapi.IpcDeviceApiHelper
import kotlinx.coroutines.flow.*

/**
 * @author Dylan Cai
 */

@Suppress("ObjectPropertyName")
object DeviceManagerRepository {

    private val remoteDataSource = DeviceManagerRemoteDataSource()
    private val localDataSource = DeviceManagerLocalDataSource()

    private val _ipcDeviceList = MutableStateFlow<List<IpcDevice>>(emptyList())
    val ipcDeviceList = _ipcDeviceList
    private val _hubIpcDeviceList = MutableStateFlow<List<IpcDevice>>(emptyList())
    val hubIpcDeviceList = _hubIpcDeviceList
    private val _netSpotIpcDeviceList = MutableStateFlow<List<IpcDevice>>(emptyList())
    val netSpotIpcDeviceList = _netSpotIpcDeviceList

    fun getIpcDeviceListFlow(uid : String) = flow<List<IpcDevice>> {
        localDataSource.initDeviceConfigures()
        val localBleIpcDeviceList = localDataSource.getLocalBleIpcDeviceList(uid)
        netSpotIpcDeviceList.value = localBleIpcDeviceList
        val localIpcDeviceList = localDataSource.getLocalIpcDeviceList(uid)
        ipcDeviceList.value = localIpcDeviceList.filter {
            it.deviceType == IpcDevice.DEVICE_TYPE_IPC
        }
        hubIpcDeviceList.value = localIpcDeviceList.filter {
            it.deviceType == IpcDevice.DEVICE_TYPE_GATEWAY
        }
        var remoteIpcDeviceList = remoteDataSource.getRemoteIpcDeviceList(localIpcDeviceList, uid, 1, 100)
        ipcDeviceList.value = remoteIpcDeviceList.filter {
            it.deviceType == IpcDevice.DEVICE_TYPE_IPC
        }
        hubIpcDeviceList.value = remoteIpcDeviceList.filter {
            it.deviceType == IpcDevice.DEVICE_TYPE_GATEWAY
        }
        emit(remoteIpcDeviceList)
    }

    suspend fun getIpcDevice(deviceId : String) = remoteDataSource.getRemoteIpcDevice(deviceId)

    suspend fun getRemoteDeviceInfo(deviceId: String) = remoteDataSource.getRemoteDeviceInfo(deviceId)

    fun getIpcDeviceFromCache(uid : String, deviceId : String) = flow<DeviceModel?> {
        var localIpcDeviceModel = localDataSource.getLocalIpcDevice(uid, deviceId)
        emit(localIpcDeviceModel)
    }

    fun updateBleIpcDevice(uid: String, netSpotDeviceInfo: NetSpotDeviceInfo?) = flow<Boolean> {
        localDataSource.updateLocalBleIpcDevice(uid, netSpotDeviceInfo)
        emit(true)
    }

    fun getBleIpcDevice(uid : String, deviceId : String) = flow<IpcDevice?> {
        emit(localDataSource.getLocalBleIpcDevice(uid, deviceId))
    }

    fun getUpgradeDetailInfo(deviceId: String, model: String) = remoteDataSource.getUpgradeDetailInfo(deviceId, model)

    fun getDeviceUploadConfigure(deviceId: String) = remoteDataSource.getDeviceUploadConfigure(deviceId)

    fun updateDeviceUploadConfigure(deviceId: String, configure: String): Flow<YRApiResponse<Any>> = remoteDataSource.updateDeviceUploadConfigure(deviceId, configure)

    fun updateDeviceNotice(deviceId: String, isNotice: Int): Flow<YRApiResponse<Any>> = remoteDataSource.updateUpdateDeviceNotice(deviceId, isNotice)

    fun getPackageInfo(deviceId: String, isOwner: Boolean): Flow<YRApiResponse<PackageInfoResult>> = remoteDataSource.getPackageInfo(deviceId, isOwner)

    fun getCloudVideoFileList(deviceId: String, userId: String, fileDate: String, bindType: Int): Flow<YRApiResponse<AwsFileListResult>> = remoteDataSource.getCloudVideoFileList(deviceId,userId,fileDate,bindType)

    fun shareDevice(deviceId: String, sharers: List<String>): Flow<Map<String, YRApiResponse<ShareDeviceResult>>> = remoteDataSource.shareDevice(deviceId, sharers)

    fun deleteSharing(id: Int): Flow<YRApiResponse<Any>> = remoteDataSource.deleteSharing(id)

    fun getDeviceRelation(deviceId: String, page : Int,  count : Int): Flow<YRApiResponse<DeviceRelationListResult>> = remoteDataSource.getDeviceRelation(deviceId, page, count)

    fun cancelOrder(deviceId: String): Flow<YRApiResponse<Any>> = remoteDataSource.cancelOrder(deviceId)

    fun getUpgradeInfo(model: String): Flow<YRApiResponse<FirmwareVersion>> = remoteDataSource.getUpgradeInfo(model)

    fun getUpgradeStatus(deviceId: String): Flow<YRApiResponse<DeviceUpgradeStatusResult>> = remoteDataSource.getUpgradeStatus(deviceId)

    fun updateDeviceName(deviceId: String, name: String): Flow<YRApiResponse<Any>> = remoteDataSource.updateDeviceName(deviceId, name)

    fun deleteRemoteGatewayDevice(deviceId: String): Flow<YRApiResponse<Any>> = remoteDataSource.deleteRemoteGatewayDevice(deviceId)

    fun deleteRemoteDevice(deviceId: String): Flow<YRApiResponse<Any>> = remoteDataSource.deleteRemoteDevice(deviceId)

    fun queryIpcDeviceListFromLocal(uid : String) = flow<List<IpcDevice>> {
        localDataSource.initDeviceConfigures()
        val localBleIpcDeviceList = localDataSource.getLocalBleIpcDeviceList(uid)
        val localIpcDeviceList = localDataSource.getLocalIpcDeviceList(uid)
        emit(localIpcDeviceList)
    }

    fun queryIpcDeviceListFromRemote(uid : String, localIpcDeviceList: List<IpcDevice>) = flow<List<IpcDevice>> {
        var remoteIpcDeviceList = remoteDataSource.getRemoteIpcDeviceList(localIpcDeviceList, uid, 1, 100)
        emit(remoteIpcDeviceList)
    }

    fun getLocalIpcDeviceListFlow(uid : String) = flow<List<IpcDevice>> {
        val localIpcDeviceList : List<IpcDevice> = localDataSource.getLocalIpcDeviceList(uid).filter {
            it.deviceType == IpcDevice.DEVICE_TYPE_IPC
        }
            .orEmpty()
        emit(localIpcDeviceList)
    }

    fun getIpcDeviceInfoListFlow(page : Int, pageCount : Int) = flow<List<IpcDevice>> {
        var remoteIpcDeviceList = remoteDataSource.getRemoteIpcDeviceList(page, pageCount).single()
        remoteIpcDeviceList.filter {
            it.deviceType == IpcDevice.DEVICE_TYPE_IPC
        }
            .orEmpty()
        emit(remoteIpcDeviceList)
    }

    fun queryLocalIpcDeviceListAsMapFlow(uid : String) = flow<List<DeviceInfoModel>> {
        val deviceInfoMaps = getLocalIpcDeviceListFlow(uid)
            .catch {
                emit(listOf())
            }
            .single()
            .map {
                var deviceInfo = it.device.deviceInfo as IpcDeviceInfo
                DeviceManagerHelper.convertDeviceInfoAsMap(deviceInfo)
            }
            .orEmpty()
        emit(deviceInfoMaps)
    }

    fun queryRemoteIpcDeviceListAsMapFlow(page : Int, pageCount : Int) = flow<List<DeviceInfoModel>> {
        val deviceInfoMaps = getIpcDeviceInfoListFlow(page, pageCount)
            .catch {
                emit(listOf())
            }
            .single()
            .map {
                var deviceInfo = it.device.deviceInfo as IpcDeviceInfo
                DeviceManagerHelper.convertDeviceInfoAsMap(deviceInfo)
            }
            .orEmpty()
        emit(deviceInfoMaps)
    }

}

class DeviceManagerLocalDataSource {

    suspend fun initDeviceConfigures() {
        DeviceControlHelper.initDeviceConfigureModelFromAsset()
    }

    suspend fun getLocalIpcDeviceList(uid : String) = convertIpcDeviceFromDeviceEntity(SmartDatabase.getDatabase().deviceDao().getAllDevices(uid))

    suspend fun getLocalIpcDevice(uid : String, deviceId : String) : DeviceModel? {
        return SmartDatabase.getDatabase().deviceDao().getDevice(uid, deviceId)?.let {
            convertIpcDeviceModelFromDeviceEntity(it)
        }
    }

    suspend fun getLocalBleIpcDeviceList(uid : String) = convertBleIpcDeviceFromDeviceEntity(SmartDatabase.getDatabase().deviceDao().getAllBleDevices(uid))

    suspend fun getLocalBleIpcDevice(uid : String, deviceId : String) : IpcDevice? {
        return SmartDatabase.getDatabase().deviceDao().getBleDevice(uid, deviceId)?.let {
            convertBleIpcDeviceFromDeviceEntity(it)
        }
    }

    suspend fun updateLocalBleIpcDevice(uid: String, netSpotDeviceInfo: NetSpotDeviceInfo?) {
        DeviceManagerDbHelper.updateBleIpcDevice(uid, netSpotDeviceInfo)
    }

    private fun convertIpcDeviceFromDeviceEntity(deviceEntityList : List<DeviceEntity>) : List<IpcDevice> {
        return deviceEntityList.map {
            convertIpcDeviceFromDeviceEntity(it)
        }
    }

    private fun convertIpcDeviceFromDeviceEntity(deviceEntity : DeviceEntity) : IpcDevice {
        return deviceEntity.let {
            var deviceModel = convertIpcDeviceModelFromDeviceEntity(it)
            IpcDevice(device = deviceModel).apply {
                deviceType = IpcDevice.convertDeviceType(device.deviceInfo.model.orEmpty())
            }
        }
    }

    private fun convertIpcDeviceModelFromDeviceEntity(deviceEntity : DeviceEntity) : DeviceModel {
        return deviceEntity.let {
            var deviceInfo = IpcDeviceInfo(it.model, it.name, it.mainCategory, it.subCategory, it.deviceId, it.platform, it.iconUrl, it.brand, it.communicationType).apply {
                //id = it.id
                bindType = it.bindType
                sn = it.sn.orEmpty()
                mac = it.mac.orEmpty()
                version = it.version.orEmpty()
                localIp = it.localIp
                wanIp = it.wanIp
                time = it.time
                hbServer = it.hbServer
                hbPort = it.hbPort
                hbDomain = it.hbDomain.orEmpty()
                openStatus = it.openStatus
                online = it.online
                zone = it.zone
                isGoogle = it.googleState
                isAlexa = it.alexaState
                pDeviceId = it.pDeviceId.orEmpty()
                wifiLevel = it.wifiLevel
                batteryLevel = it.batteryLevel
                sort = it.sort
                pModel = it.pModel.orEmpty()
                pVersion = it.pVersion.orEmpty()
                modelType = it.modelType
                secret = it.secret.orEmpty()
                isTheft = it.theftState
                isNotice = it.noticeState
                appTimingConfig = it.appTimingConfig.orEmpty()
                mqttOnline = it.mqttOnline
                ownerNickname = it.ownerNickname
                ownerAccount = it.ownerAccount
            }
            DeviceModel(deviceInfo)
        }
    }

    private fun convertBleIpcDeviceFromDeviceEntity(deviceEntityList : List<BleDeviceEntity>) : List<IpcDevice> {
        return deviceEntityList.map {
            convertBleIpcDeviceFromDeviceEntity(it)
        }
    }

    private fun convertBleIpcDeviceFromDeviceEntity(deviceEntity : BleDeviceEntity) : IpcDevice {
        return deviceEntity.let {
            var deviceInfo = BLEDeviceInfo(it.model, it.name, it.mainCategory, it.subCategory, it.deviceId, it.platform, it.iconUrl, it.brand, it.communicationType).apply {
                bleDeviceId = it.bleDeviceId
                ssid = it.ssid
                /*
                bindType = it.bindType
                sn = it.sn.orEmpty()
                mac = it.mac.orEmpty()
                version = it.version.orEmpty()
                localIp = it.localIp
                wanIp = it.wanIp
                time = it.time
                hbServer = it.hbServer
                hbPort = it.hbPort
                hbDomain = it.hbDomain.orEmpty()
                openStatus = it.openStatus
                online = it.online
                zone = it.zone
                isGoogle = it.googleState
                isAlexa = it.alexaState
                pDeviceId = it.pDeviceId.orEmpty()
                wifiLevel = it.wifiLevel
                batteryLevel = it.batteryLevel
                sort = it.sort
                pModel = it.pModel.orEmpty()
                pVersion = it.pVersion.orEmpty()
                modelType = it.modelType
                secret = it.secret.orEmpty()
                isTheft = it.theftState
                isNotice = it.noticeState
                appTimingConfig = it.appTimingConfig.orEmpty()
                mqttOnline = it.mqttOnline

                 */
            }
            var deviceModel = DeviceModel(deviceInfo)
            IpcDevice(device = deviceModel).apply {
                deviceType = IpcDevice.DEVICE_TYPE_BLE_IPC
            }
        }
    }
}

class DeviceManagerRemoteDataSource {

    suspend fun getRemoteIpcDeviceList(localIpcDeviceList : List<IpcDevice>, uid : String, page : Int, pageCount : Int) = flow<List<IpcDevice>> {
        var localIpcDeviceIdList = localIpcDeviceList.map {
            it.device.deviceInfo.device_id
        }.toMutableList()
        var bindDeviceResponse = IpcDeviceApiHelper.deviceApi.getBindDevices(page, pageCount).single()
        var bindDeviceList = if (bindDeviceResponse.code == HttpCode.SUCCESS_CODE) convertIpcDeviceFromBindDevices(bindDeviceResponse.data.data) else mutableListOf()
        var devicePackMap = IpcDeviceApiHelper.deviceApi.getAllDevicePageInfo()
            .map { it ->
                var packInfoList = if (it.code == HttpCode.SUCCESS_CODE) it.data else mutableListOf()
                var packInfoMap = mutableMapOf<String, PackageInfoResult>()
                packInfoList.forEach {
                    packInfoMap[it.uuid] = it
                }
                packInfoMap
            }
            .single()
        bindDeviceList.forEach {
            if (devicePackMap.containsKey(it.device.deviceInfo.device_id) && devicePackMap[it.device.deviceInfo.device_id] != null) {
                it.packInfo = devicePackMap[it.device.deviceInfo.device_id]
            }
        }
        var gatewayDeviceResponse = IpcDeviceApiHelper.deviceApi.getGatewayDeviceList("ipc").single()
        var gatewayDeviceList = if (gatewayDeviceResponse.code == HttpCode.SUCCESS_CODE) convertIpcDeviceFromGatewayDevice(gatewayDeviceResponse.data) else mutableListOf()
        var result = mutableListOf<IpcDevice>()
        result.addAll(bindDeviceList.orEmpty())
        result.addAll(gatewayDeviceList.orEmpty())
        result.forEach {
            var deviceInfo = it.device.deviceInfo as IpcDeviceInfo
            DeviceManagerDbHelper.updateIpcDevice(uid, deviceInfo)
            localIpcDeviceIdList.remove(deviceInfo.device_id)
        }
        localIpcDeviceIdList.forEach {
            DeviceManagerDbHelper.deleteIpcDevice(uid, it)
        }
        emit(result)
    }
        .single()

    suspend fun getRemoteIpcDeviceList(page : Int, pageCount : Int) = flow<List<IpcDevice>> {
        var bindDeviceResponse = IpcDeviceApiHelper.deviceApi.getBindDevices(page, pageCount).single()
        var bindDeviceList = if (bindDeviceResponse.code == HttpCode.SUCCESS_CODE) convertIpcDeviceFromBindDevices(bindDeviceResponse.data.data) else mutableListOf()
        emit(bindDeviceList)
    }

    suspend fun getRemoteIpcDevice(deviceId: String) = flow<DeviceModel?> {
        var deviceResponse = IpcDeviceApiHelper.deviceApi.getDeviceInfo(deviceId).single()
        var device = if (deviceResponse.code == HttpCode.SUCCESS_CODE) convertDeviceModelFromBindDevice(deviceResponse.data) else null
        emit(device)
    }

    suspend fun getRemoteDeviceInfo(deviceId: String) = flow<YRApiResponse<BindDevice>> {
        var deviceResponse = IpcDeviceApiHelper.deviceApi.getDeviceInfo(deviceId).single()
        emit(deviceResponse)
    }

    fun getDeviceUploadConfigure(deviceId: String) = flow<DeviceUploadConfigure?> {
        var deviceResponse = IpcDeviceApiHelper.deviceApi.getDeviceInfo(deviceId).single()
        var configure = if (deviceResponse.code == HttpCode.SUCCESS_CODE) JsonConvertUtil.convertData(deviceResponse.data.app_timing_config, DeviceUploadConfigure::class.java) else null
        emit(configure)
    }

    fun updateDeviceUploadConfigure(deviceId: String, configure: String): Flow<YRApiResponse<Any>> {
        var body = UpdateDeviceConfigureBody(deviceId, configure)
        return IpcDeviceApiHelper.deviceApi.updateDeviceConfigure(body)
    }

    fun updateUpdateDeviceNotice(deviceId: String, isNotice: Int): Flow<YRApiResponse<Any>> {
        var body = UpdateDeviceNoticeBody(deviceId, isNotice)
        return IpcDeviceApiHelper.deviceApi.updateDeviceNotice(body)
    }

    fun getPackageInfo(deviceId: String, isOwner: Boolean): Flow<YRApiResponse<PackageInfoResult>> {
        return if (isOwner) {
            IpcDeviceApiHelper.deviceApi.getDevicePageInfo(deviceId)
        } else {
            IpcDeviceApiHelper.deviceApi.getSharingDevicePageInfo(deviceId)
        }
    }

    fun getCloudVideoFileList(deviceId: String, userId: String, fileDate: String, bindType: Int): Flow<YRApiResponse<AwsFileListResult>> {
        return IpcDeviceApiHelper.deviceApi.getCloudVideoFileList(deviceId,userId,fileDate,bindType)
    }

    fun shareDevice(deviceId: String, sharers: List<String>) = flow {
        val responses = mutableMapOf<String, YRApiResponse<ShareDeviceResult>>()
        sharers.forEach {
            var response = IpcDeviceApiHelper.deviceApi.sendDeviceSharing(SharingDeviceBody(it, deviceId)).single()
            if (it.isNotEmpty()) {
                responses[it] = response
            }
        }
        emit(responses)
    }

    fun deleteSharing(id: Int): Flow<YRApiResponse<Any>> {
        return IpcDeviceApiHelper.deviceApi.deleteSharingDevice(id)
    }

    fun getDeviceRelation(deviceId: String, page : Int,  count : Int): Flow<YRApiResponse<DeviceRelationListResult>> {
        return IpcDeviceApiHelper.deviceApi.getDeviceRelationList(deviceId, page, count)
    }

    fun cancelOrder(deviceId: String): Flow<YRApiResponse<Any>> {
        return IpcDeviceApiHelper.deviceApi.cancelOrder(deviceId)
    }

    fun getUpgradeInfo(model: String): Flow<YRApiResponse<FirmwareVersion>> {
        return IpcDeviceApiHelper.deviceApi.getFirmwareVersion(model)
    }

    fun getUpgradeDetailInfo(deviceId: String, model: String) = flow {
        var deviceInfoResponse = IpcDeviceApiHelper.deviceApi.getDeviceInfo(deviceId).single();
        var upgradeInfoResponse = IpcDeviceApiHelper.deviceApi.getFirmwareVersion(model).single();
        val upgradeInfo = FirmwareVersion("", "", "", "", "", "", "")
        if (deviceInfoResponse?.code == HttpCode.SUCCESS_CODE) {
            upgradeInfo.currentVersionCode = deviceInfoResponse?.data?.version
        }
        if (upgradeInfoResponse?.code == HttpCode.SUCCESS_CODE) {
            upgradeInfoResponse?.data?.let {
                upgradeInfo.model = it.model
                upgradeInfo.type = it.type
                upgradeInfo.version_code = it.version_code
                upgradeInfo.log = it.log
                upgradeInfo.key = it.key
                upgradeInfo.md5 = it.md5
            }
        }
        emit(upgradeInfo)
    }

    fun getUpgradeStatus(deviceId: String): Flow<YRApiResponse<DeviceUpgradeStatusResult>> {
        return IpcDeviceApiHelper.deviceApi.getDeviceUpgradeStatus(deviceId)
    }

    fun updateDeviceName(deviceId: String, name: String): Flow<YRApiResponse<Any>> {
        var body = UpdateDeviceNameBody(deviceId, name)
        return IpcDeviceApiHelper.deviceApi.updateDeviceName(body)
    }

    fun deleteRemoteGatewayDevice(deviceId: String): Flow<YRApiResponse<Any>> {
        return IpcDeviceApiHelper.deviceApi.deleteGatewayDevice(DeleteDeviceBindingBody(deviceId))
    }

    fun deleteRemoteDevice(deviceId: String): Flow<YRApiResponse<Any>> {
        return IpcDeviceApiHelper.deviceApi.deleteDevice(DeleteDeviceBindingBody(deviceId))
    }

    private fun convertIpcDeviceFromBindDevices(deviceList : List<BindDevice>) : List<IpcDevice> {
        return deviceList.map {
            convertIpcDeviceFromBindDevice(it)
        }
    }

    private fun convertIpcDeviceFromBindDevice(device : BindDevice) : IpcDevice {
        return device.let {
            var deviceModel = convertDeviceModelFromBindDevice(it)
            IpcDevice(device = deviceModel).apply {
                deviceType = IpcDevice.DEVICE_TYPE_IPC
            }
        }
    }

    private fun convertDeviceModelFromBindDevice(device : BindDevice) : DeviceModel {
        return device.let {
            var deviceInfo = IpcDeviceInfo(it.type.orEmpty(), it.name.orEmpty(), DeviceSupportMainCategory.ipc, DeviceManagerHelper.convertIpcSubCategory(it.type), it.uuid.orEmpty(), SupportPlatform.selfDevelopProtocolPlatform, "", SupportBrand.osaio_brand, DeviceManagerHelper.convertIpcCommunicationType(it.type))
            deviceInfo.apply {
                id = it.id
                bindType = it.bind_type
                sn = it.sn.orEmpty()
                mac = it.mac.orEmpty()
                version = it.version.orEmpty()
                localIp = it.local_ip
                wanIp = it.wanip
                time = it.time
                hbServer = it.hb_server
                hbPort = it.hb_port
                hbDomain = it.hb_domain.orEmpty()
                openStatus = it.open_status
                online = it.online
                zone = it.zone
                isGoogle = it.is_google
                isAlexa = it.is_alexa
                pDeviceId = it.puuid.orEmpty()
                wifiLevel = it.wifi_level
                batteryLevel = it.battery_level
                sort = it.sort
                pModel = it.p_model.orEmpty()
                pVersion = it.p_version.orEmpty()
                modelType = it.model_type
                secret = it.secret.orEmpty()
                isTheft = it.is_theft
                isNotice = it.is_notice
                appTimingConfig = it.app_timing_config.orEmpty()
                mqttOnline = it.mqtt_online
                ownerNickname = it.nickname
                ownerAccount = it.account
            }
            DeviceModel(deviceInfo)
        }
    }

    private fun convertIpcDeviceFromGatewayDevice(deviceList : List<GatewayDevice>) : List<IpcDevice> {
        return deviceList.map {
            var deviceInfo = IpcDeviceInfo(it.type.orEmpty(), it.name.orEmpty(), DeviceSupportMainCategory.ipc, DeviceManagerHelper.convertIpcSubCategory(it.type), it.uuid.orEmpty(), SupportPlatform.selfDevelopProtocolPlatform, "", SupportBrand.osaio_brand, DeviceManagerHelper.convertIpcCommunicationType(it.type))
            deviceInfo.apply {
                sn = it.sn.orEmpty()
                mac = it.mac.orEmpty()
                version = it.version.orEmpty()
                localIp = it.local_ip
                wanIp = it.wanip
                time = it.time
                hbServer = it.hb_server
                hbPort = it.hb_port
                hbDomain = it.hb_domain.orEmpty()
                openStatus = it.open_status
                online = it.online
                zone = it.zone
                sort = it.sort
                secret = it.secret.orEmpty()
                mqttOnline = it.mqtt_online
                region = it.region
                subDevices = it.child
                ownerNickname = it.nickname
                ownerAccount = it.account
            }
            var deviceModel = DeviceModel(deviceInfo)
            IpcDevice(device = deviceModel).apply {
                deviceType = IpcDevice.DEVICE_TYPE_GATEWAY
            }
        }
    }

    private fun getTestGatewayListResp() : YRApiResponse<MutableList<GatewayDevice>> {
        val json = "{\"code\":1000,\"msg\":\"ok\",\"data\":[{\"name\":\"View\",\"uuid\":\"479339aa347046e6e1f5421ac423a976\",\"sn\":\"1\",\"mac\":\"04:7E:4A:30:79:A8\",\"version\":\"3.1.81\",\"local_ip\":1964708012,\"wanip\":2130706433,\"type\":\"EC810-HUB\",\"open_status\":1,\"online\":0,\"time\":1627876375,\"hb_domain\":\"161.189.16.197\",\"hb_server\":2713522373,\"hb_port\":6126,\"zone\":8,\"region\":\"cn\",\"sort\":1,\"secret\":\"HRpTvMtU\",\"mqtt_online\":0,\"child\":[{\"name\":\"View\",\"uuid\":\"c9459325941db694030b54ba680490bd\",\"sn\":\"1\",\"mac\":\"04:7E:4A:20:4C:90\",\"version\":\"5.4.8\",\"local_ip\":1964708012,\"wanip\":2130706433,\"open_status\":1,\"online\":1,\"type\":\"EC810PRO\",\"time\":1627554737,\"zone\":8,\"region\":\"cn\",\"sort\":5}]}]}"
        val gson = Gson()
        var result = gson.fromJson<YRApiResponse<MutableList<GatewayDevice>>>(json, object : TypeToken<YRApiResponse<MutableList<GatewayDevice>>>(){}.type)
        return result
    }
}