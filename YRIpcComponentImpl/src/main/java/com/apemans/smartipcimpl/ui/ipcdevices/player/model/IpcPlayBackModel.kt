package com.apemans.smartipcimpl.ui.ipcdevices.player.model


import android.view.View
import android.widget.Toast
import com.apemans.base.utils.JsonConvertUtil
import com.apemans.smartipcapi.info.IpcDeviceInfo
import com.apemans.ipcchipproxy.define.*
import com.apemans.ipcchipproxy.scheme.bean.IpcSchemeResult
import com.apemans.ipcchipproxy.scheme.bean.StorageInfo
import com.apemans.ipcchipproxy.scheme.util.parseIpcDPCmdResult
import com.apemans.logger.YRLog
import com.apemans.smartipcimpl.repository.DeviceManagerRepository
import com.apemans.smartipcimpl.ui.actioncameralist.IpcControlViewModel
import com.google.gson.reflect.TypeToken
import com.nooie.common.utils.time.DateTimeUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import java.util.*
import androidx.lifecycle.viewModelScope
import com.apemans.business.apisdk.client.define.HttpCode
import com.apemans.ipcchipproxy.scheme.bean.StorageRecordVideoInfo
import com.apemans.smartipcapi.webapi.AwsFileInfo
import com.apemans.smartipcapi.webapi.AwsFileListResult
import com.apemans.smartipcapi.webapi.PackageInfoResult
import com.apemans.smartipcimpl.R
import com.apemans.smartipcimpl.ui.ipcdevices.player.bean.NOOIE_PLAYBACK_TYPE_SD
import com.apemans.smartipcimpl.ui.ipcdevices.player.old.bean.*
import com.dylanc.longan.application
import com.nooie.common.utils.collection.CollectionUtil
import com.nooie.common.utils.file.FileUtil
import com.nooie.sdk.device.bean.RecordFragment
import kotlinx.coroutines.launch

/**
 * @Author:dongbeihu
 * @Description: IPC获取数据源，实现接口功能
 * @Date: 2021/12/2-15:27
 */
class IpcPlayBackModel : IpcControlViewModel() {

    /**
     * 获取设备详情
     */
    fun loadDeviceInfo(deviceId: String) {
        viewModelScope.launch {
            DeviceManagerRepository.getIpcDevice(deviceId)
                .flowOn(Dispatchers.IO)
                .catch { e ->
                    YRLog.d { e }
                }
                .single()
                ?.let {
                    (it.deviceInfo as? IpcDeviceInfo)
                }
        }
    }


    /**
     * 获取指定某天的，存储视频列表,可能为空
     * @param start 开始时间 注意单位是秒 ，后台返回的是多少s位移
     */
    fun  getCloudVideoFileList(deviceId: String, userId: String, start :Long, bindType: Int ,isLpDevice: Boolean,block: (playbackCloudData: PlaybackCloudData?) -> Unit){
       val filedate = DateTimeUtil.getUtcTimeString(start * 1000, "yyyyMMdd")

        viewModelScope.launch {
            DeviceManagerRepository.getCloudVideoFileList(deviceId,userId,filedate,bindType)
                .flowOn(Dispatchers.IO)
                .catch { e ->
                    YRLog.d { e }
                }
                .collect {
                    val responseSuccess = it.code == HttpCode.SUCCESS_CODE
                    if (responseSuccess) {
                        if (it.data ==null){
                            block(null)
                        }else{
                           val playbackCloudData =  mapCloudVideoFileList(it.data,start,deviceId,isLpDevice,bindType )
                            block(playbackCloudData)
                        }
                    }else{
                        Toast.makeText(application,application.getString(R.string.tcp_preview_could_storage_empty),Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }

    /**
     * 获取某个时间戳的临近视频
     * @param start 开始时间 注意单位是秒 ，后台返回的是多少s位移
     */
    fun  getDetectionMsgList(deviceId: String, userId: String, start :Long, bindType: Int ,isLpDevice: Boolean,block: (playbackCloudData: PlaybackCloudData?) -> Unit){
        val filedate = DateTimeUtil.getUtcTimeString(start * 1000, "yyyyMMdd")

        viewModelScope.launch {
            DeviceManagerRepository.getCloudVideoFileList(deviceId,userId,filedate,bindType)
                .flowOn(Dispatchers.IO)
                .catch { e ->
                    YRLog.d { e }
                }
                .collect {
                    val responseSuccess = it.code == HttpCode.SUCCESS_CODE
                    if (responseSuccess) {
                        if (it.data ==null){
                            block(null)
                        }else{
                            val playbackCloudData =  mapCloudVideoFileList(it.data,start,deviceId,isLpDevice,bindType )
                            block(playbackCloudData)
                        }
                    }
                }
        }
    }

    /**
     * 整理云回放数据
     * @param start 开始时间 注意单位是秒 ，后台返回的是多少s位移
     */
    fun mapCloudVideoFileList(awsFileListResult: AwsFileListResult,start :Long,deviceId: String,isLpDevice: Boolean,bindType :Int) :PlaybackCloudData{

        var playbackCloudData = PlaybackCloudData()

        var result: ArrayList<CloudRecordInfo> = ArrayList() //记录片段
        var recordFragments: ArrayList<RecordFragment> = ArrayList()
        //NooieLog.d("-->> PlaybackPlayerPresenter onNext loadDeviceCloudRecordList currentUtcTime=" + DateTimeUtil.getUtcTimeString(DateTimeUtil.getUtcCalendar().getTimeInMillis(), DateTimeUtil.PATTERN_YMD_HMS_2) + " expireTime=" + DateTimeUtil.getUtcTimeString(response.getData().getExpirationtime() * 1000L, DateTimeUtil.PATTERN_YMD_HMS_2));
        val fileInfos: List<AwsFileInfo> = awsFileListResult.fileinfo
        for (fileInfo in CollectionUtil.safeFor(fileInfos)) {

            if (fileInfo != null) {
                val startTime: Long = if (fileInfo.s < 0) start * 1000L else (start + fileInfo.s) * 1000L
                val timeLen: Long = if (fileInfo.s < 0) (fileInfo.l + fileInfo.s) * 1000L else fileInfo.l * 1000L
                if (timeLen <= 0) {
                    continue
                }

                var recordType: RecordType = RecordType.PLAN_RECORD
                if (fileInfo.e != null && isDetectionAvailable(fileInfo.e.m)) {
                    recordType = RecordType.MOTION_RECORD
                } else if (fileInfo.e != null && isDetectionAvailable(fileInfo.e.s)) {
                    recordType = RecordType.SOUND_RECORD
                } else if (fileInfo.e != null && isDetectionAvailable( fileInfo.e.p )  ) {
                    recordType = RecordType.PIR_RECORD
                }
                val cloudRecordInfo = CloudRecordInfo(deviceId, 1, startTime, timeLen, recordType, true)
                val recordTypes: MutableList<RecordType?> = ArrayList<RecordType?>()
                if (fileInfo.e != null && isDetectionAvailable(  fileInfo.e.m) ) {
                    recordTypes.add(RecordType.MOTION_RECORD)
                }
                if (fileInfo.e != null && isDetectionAvailable(   fileInfo.e.s ) ) {
                    recordTypes.add(RecordType.SOUND_RECORD)
                }
                if (isLpDevice && fileInfo.e != null && isDetectionAvailable( fileInfo.e.p )) {
                    recordTypes.add(RecordType.PIR_RECORD)
                }
                if (CollectionUtil.isEmpty(recordTypes)) {
                    recordTypes.add(RecordType.PLAN_RECORD)
                }
                cloudRecordInfo.recordTypes = recordTypes
                if (fileInfo.e != null && (isDetectionAvailable(fileInfo.e.m )  || isDetectionAvailable(fileInfo.e.s  ) || isDetectionAvailable(fileInfo.e.p)) ) {
                    val cloudFileBean = CloudFileBean()
                    cloudFileBean.deviceId = awsFileListResult.deviceid
                    cloudFileBean.userId = awsFileListResult.userid
                    cloudFileBean.fileType = awsFileListResult.filetype
                    cloudFileBean.picType = awsFileListResult.pictype
                    cloudFileBean.startTime = fileInfo.s.toLong()
                    cloudFileBean.expiration = awsFileListResult.storage
                    cloudFileBean.bindType = bindType
                    cloudFileBean.motionDetectionTime = fileInfo.e.m
                    cloudFileBean.soundDetectionTime = fileInfo.e.s
                    cloudFileBean.pirDetectionTime = fileInfo.e.p
                    cloudFileBean.baseTime = start * 1000L
                    cloudFileBean.fileUrl = FileUtil.getDetectionThumbnailFilePath(
                            application, obtainUid(), deviceId,   start + fileInfo.s  )
                    cloudRecordInfo.cloudFileBean = cloudFileBean
                }
                result.add(cloudRecordInfo)
                val recordFragment = RecordFragment()
                recordFragment.start = fileInfo.s
                recordFragment.len = fileInfo.l
                recordFragments.add(recordFragment)
            }

        }

        playbackCloudData.result = result
        playbackCloudData.recordFragments = recordFragments
        playbackCloudData.fileType = awsFileListResult.filetype
        playbackCloudData.picType = awsFileListResult.pictype
        playbackCloudData.filePrefix = awsFileListResult.file_prefix
//        playbackCloudData.expireDate = awsFileListResult.expirationtime
        playbackCloudData.expireDate = awsFileListResult.storage
        return  playbackCloudData
    }

/**
 * 侦测：m是-1表示无移动侦测事件 s是-1表示无声音侦测事件
 */
fun isDetectionAvailable(time: Int): Boolean {
    return time != -1
}
    /**
     * 获取SD卡存储信息
     */
    fun loadStorageInfo(deviceId: String,block: (info: StorageInfo?) -> Unit) {
        viewModelScope.launch {
            var result: IpcSchemeResult? = JsonConvertUtil.convertData(
                sendStorageInfoCmd(deviceId).single(),
                IpcSchemeResult::class.java
            )
            var resultCode = result?.code ?: IPC_SCHEME_CMD_STATE_CODE_ERROR
            YRLog.d { "-->> IpcSettingViewModel loadStorageInfo code $resultCode" }
            if (resultCode == IPC_SCHEME_CMD_STATE_CODE_SUCCESS) {
                var info: StorageInfo? =  parseIpcDPCmdResult(result?.result, IPC_SCHEME_DP_STORAGE_INFO) {
                        object : TypeToken<Map<String, StorageInfo>>() {}
                    }
                block(info)
                YRLog.d { "-->> IpcSettingViewModel loadStorageInfo info ${info.toString()}" }
            }
        }
    }

    fun loadSdStorageInfo(deviceId: String,block: (hasCard: Boolean) -> Unit) {
        loadStorageInfo(deviceId){
            if (it?.status == IPC_SCHEME_STORAGE_STATUS_FORMATTING){
                block(true)
            }else{
                block(false)
            }
        }
    }

    /**
     * 卡存储是否开启
     */
    fun loadLoopRecord(deviceId: String) {
        viewModelScope.launch {
            var result = JsonConvertUtil.convertData(
                sendLoopRecordCmd( deviceId, IPC_SCHEME_DP_AUTHORITY_R, false
                ).single(), IpcSchemeResult::class.java
            )
            var code = result?.code ?: IPC_SCHEME_CMD_STATE_CODE_ERROR
            YRLog.d { "-->> IpcSettingViewModel loadLoopRecord code $code" }
            if (code == IPC_SCHEME_CMD_STATE_CODE_SUCCESS) {
                var on = parseIpcDPCmdResult(result?.result, IPC_SCHEME_DP_LOOP_RECORD) {
                    object : TypeToken<Map<String, Boolean>>() {}
                } ?: false
                YRLog.d { "-->> IpcSettingViewModel loadLoopRecord state $on" }
            }
        }
    }

    /**
     * 获取卡存储记录
     */
     fun  getSDCardRecordList(deviceId: String, start :Long,block: (listRecordInfo: ArrayList<CloudRecordInfo>?) -> Unit) {
        var resultList = ArrayList<CloudRecordInfo>() //记录片段
        viewModelScope.launch {
            var result = JsonConvertUtil.convertData(
                    sendStorageRecordVideoInfoCmd( deviceId,start).single(), IpcSchemeResult::class.java
            )
            var code = result?.code ?: IPC_SCHEME_CMD_STATE_CODE_ERROR
            YRLog.d { "-->> IpcSettingViewModel loadLoopRecord code $code" }
            if (code == IPC_SCHEME_CMD_STATE_CODE_SUCCESS) {
                var storageRecordVideoInfo = parseIpcDPCmdResult(result?.result, IPC_SCHEME_DP_STORAGE_RECORD_VIDEO_INFO) {
                    object : TypeToken<Map<String, StorageRecordVideoInfo>>() {}
                } ?: null
                YRLog.d { "-->> IpcSettingViewModel loadLoopRecord state $storageRecordVideoInfo" }
                if (storageRecordVideoInfo?.recordList.isNullOrEmpty() ){
                    block(null)
                }else{
                    val dayLen = 24 * 3600
                    val dayEndTime = (start + DateTimeUtil.DAY_SECOND_COUNT).toInt()
                    for (record in storageRecordVideoInfo?.recordList!!) {
                        //NooieLog.d("-->> LivePlayerPresenter onGetSdcardRecordInfo start=" + record.getStart() + " len=" + record.getLen() + " startTime=" + DateTimeUtil.getTimeString(record.getStart() * 1000L, DateTimeUtil.PATTERN_YMD_HMS_1));
                        if (record.start in start until dayEndTime && record.length > 0 && record.length < dayLen) {
                            //NooieLog.d("-->> LivePlayerPresenter onGetSdcardRecordInfo filter startIndexTime=" + start + " start=" + record.getStart() + " len=" + record.getLen() + " startTime=" + DateTimeUtil.getTimeString(record.getStart() * 1000L, DateTimeUtil.PATTERN_YMD_HMS_1));
                            var len = record.length
                            if (record.start + record.length - dayEndTime > 0) {
                                len = dayEndTime - record.start
                            }
                            val cloudRecordInfo = CloudRecordInfo(
                                deviceId,
                                1,
                                record.start * 1000L,
                                len * 1000L,
                                RecordType.PLAN_RECORD,
                                true
                            )
                            val recordTypes: MutableList<RecordType> = ArrayList()
                            recordTypes.add(RecordType.PLAN_RECORD)
                            cloudRecordInfo.recordTypes = recordTypes
                            resultList?.add(cloudRecordInfo)

                        }
                    }
                    block(resultList)
                }
            }
        }

    }


    /**
     * SD卡获取最近92天
     * 云存储获取最近7天
     */
    fun getUtcRecentDays(sumDay :Int) : ArrayList<CalenderBean> {

        val list: ArrayList<CalenderBean> = ArrayList()
        val todayStartTmp = DateTimeUtil.getUtcTodayStartTimeStamp()


        // 现在Anyka IPC相机只会返回92的有效数据
        for (i in 0 until sumDay) {
            val c = Calendar.getInstance()
            c.timeZone = TimeZone.getTimeZone("UTC")
            c.timeInMillis = todayStartTmp
            c.add(Calendar.DAY_OF_MONTH, -(sumDay - i - 1))
            //list.add(new CalenderBean(c, i == (sumDay - 1)));
            list.add(CalenderBean(c, false, false))
        }


        return list
    }


    /**
     * 获取当前回放时间，如果是0，则调整为当前时间
     */
    fun getTodayStartTime(mDirectTime: Long): Long {

        return if (mDirectTime == 0L) DateTimeUtil.getUtcTodayStartTimeStamp() else DateTimeUtil.getUtcDayStartTimeStamp(
            mDirectTime
        )

    }

    private fun obtainUid() : String {
        return ""
    }

}