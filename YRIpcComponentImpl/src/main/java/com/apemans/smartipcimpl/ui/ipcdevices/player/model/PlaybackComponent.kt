package com.apemans.smartipcimpl.ui.ipcdevices.player.model

import android.text.TextUtils
import com.apemans.dmapi.status.DeviceDefine
import com.apemans.logger.YRLog
import com.apemans.smartipcimpl.ui.ipcdevices.player.bean.*
import com.apemans.smartipcimpl.ui.ipcdevices.player.old.bean.CloudFileBean
import com.apemans.smartipcimpl.ui.ipcdevices.player.old.bean.CloudRecordInfo
import com.apemans.smartipcimpl.ui.ipcdevices.player.old.bean.PlaybackCloudData
import com.apemans.smartipcimpl.ui.ipcdevices.player.old.delegate.PlayState
import com.nooie.common.bean.UrlBean
import com.nooie.common.utils.collection.CollectionUtil
import com.nooie.common.utils.log.NooieLog
import com.nooie.common.utils.network.IPv4IntTransformer
import com.nooie.common.utils.time.DateTimeUtil
import com.nooie.sdk.base.Constant
import com.nooie.sdk.device.bean.RecordFragment
import com.nooie.sdk.media.NooieMediaPlayer
import java.util.*
import kotlin.math.abs

/**
 * @Author:dongbeihu
 * @Description: 回放业务组件
 *
 * @Date: 2021/12/22-15:33
 */
class PlaybackComponent {
    var player: NooieMediaPlayer? = null

    /**
     * 回放小片段，包含首尾时间
     */
    private var mRecordFragments: List<RecordFragment>? = null
    private var mFileType: String? = null
    private var mPicType: String? = null
    private var mFilePrefix: String? = null
    private var mExpireDate = 0
    private val mAlarmRecords: List<CloudRecordInfo> = ArrayList()

    private val mPickAlarmSeek = -1

    private var mDeviceId: String? = null
    var bindtype = DeviceDefine.BIND_TYPE_OWNER    //当前设备归属，默认自己
    private var mIsSubDevice = false
    private var mIsLpDevice = false
    private var mConnectionMode: Int = CONNECTION_MODE_QC
    private var mModelType = 0
    private var mPlaybackType = 0
    private var apDeviceId : String? = ""
    private var mPlaybackSourceType: Int = NOOIE_PLAYBACK_SOURCE_TYPE_NORMAL

    /**
     *
     * 当天开始时间 如：1640131200  == 2021-12-22 08:00:00
     */
    private var mTodayStartTime: Long = 0

    /**
     * 滑动时间点，TODO后期传入
     */
    private var mCurrentSeekTime: Long = 0

    /**
     * 设置外部要播放哪天的时间 （毫秒）
     */
    private var mDirectTime: Long = 0
    private var mIsInitCurrentTime = true
    private val DEFAULT_DETECTION_TIME_LEN = 0

    private val mIsAsc = false

    fun initData(player: NooieMediaPlayer, deviceId: String?, bindtype: Int, isSubDevice: Boolean, isLpDevice: Boolean, connectionMode: Int, modelType: Int,
                 playbackType: Int, playbackSourceType: Int, apDeviceId: String?) {
        this.player = player
        mDeviceId = deviceId
        this.bindtype = bindtype
        mIsSubDevice = isSubDevice
        mIsLpDevice = isLpDevice
        mConnectionMode = connectionMode
        mModelType = modelType
        mPlaybackType = playbackType
        mPlaybackSourceType = playbackSourceType
        this.apDeviceId = apDeviceId
    }

    /**
     * 开始播放之前更新下，播放模式（云、卡），具体哪天
     */
    fun setupPlayback(playbackType: Int, playbackSourceType: Int, directTime: Long) {
        mPlaybackType = playbackType
        mPlaybackSourceType = playbackSourceType
        mDirectTime = directTime
        mIsInitCurrentTime = true
        mTodayStartTime = if (directTime == 0L) DateTimeUtil.getUtcTodayStartTimeStamp() else DateTimeUtil.getUtcDayStartTimeStamp(mDirectTime)
        mCurrentSeekTime = mTodayStartTime
//        clearAlarmRecords()
//        resetTimeLine()

    }

    /**
     * 加载云回放
     * (mCurrentSeekTime / 1000L)
     * @param  currentSeekTime  0则无滑动时间，初始时间取第一个视频的startTime
     */
    fun showCloudRecordList(playbackCloudData: PlaybackCloudData, currentSeekTime: Long, isPause: Boolean) {
        if (mConnectionMode == CONNECTION_MODE_AP_DIRECT) {
            return
        }
        mCurrentSeekTime = if (currentSeekTime == 0L){
            pickStartVideoTime(mDirectTime, playbackCloudData.result)
        }else{
            currentSeekTime
        }

//        syncTimeShaft
        if (!isPause) {
            tryStartCloudVideo(mModelType, mCurrentSeekTime/1000L, playbackCloudData.recordFragments, playbackCloudData.fileType,
                    bindtype, mTodayStartTime / 1000L, playbackCloudData.expireDate, playbackCloudData.filePrefix)
        }

    }

    /**
     * 加载卡回放
     * @param  currentSeekTime  0则无滑动时间，初始时间取第一个视频的startTime
     */
    fun showSDCardRecordList(recordInfoList: List<CloudRecordInfo?>,currentSeekTime: Long, isPause: Boolean) {

        if (recordInfoList?.size != 0 && player !=null) {
            mCurrentSeekTime = if (currentSeekTime == 0L){
                pickStartVideoTime(mDirectTime, recordInfoList)
            }else{
                currentSeekTime
            }

            if (!isPause) {
                if (mIsLpDevice) {
                    startLpSDPlayback(mDeviceId!!, mConnectionMode, mIsSubDevice, mModelType, (mCurrentSeekTime / 1000L).toInt())
                } else {
                    startSDPlayback(mDeviceId!!, mConnectionMode, mModelType, (mCurrentSeekTime / 1000L).toInt())
                }
            }
        }
    }


    private fun startLpSDPlayback(deviceId: String, connectionMode: Int, isSubDevice: Boolean, modelType: Int, start: Int) {
        if (connectionMode == CONNECTION_MODE_AP_DIRECT) {

            updatePlayState(PlayState.PLAY_TYPE_SD_PLAYBACK, PlayState.PLAY_STATE_START)
            val playbackSeekTime = getPlaybackSeekTime(start * 1000L, mTodayStartTime, NOOIE_PLAYBACK_TYPE_SD)
            if (isSubDevice) {
                if (modelType == Constant.MODEL_TYPE_MH_EC810_CAM) {
                    player!!.startAPP2PPlayback(apDeviceId, 0, playbackSeekTime, modelType) { code ->
                        NooieLog.d("-->>> PlaybackComponent startLpSDPlayback LpSD onResult code=$code")
                        onStartPlayback(code, start.toLong(), NOOIE_PLAYBACK_TYPE_SD)
                    }
                } else {
                    player!!.startAPPlayback(apDeviceId, 0, playbackSeekTime, modelType) { code ->
                        NooieLog.d("-->>> PlaybackComponent startLpSDPlayback LpSD onResult code=$code")
                        onStartPlayback(code, start.toLong(),NOOIE_PLAYBACK_TYPE_SD)
                    }
                }
            } else {
                if (modelType == Constant.MODEL_TYPE_MH_EC810_CAM) {
                    player!!.startAPP2PPlayback(apDeviceId, 0, playbackSeekTime, modelType) { code ->
                        NooieLog.d("-->>> PlaybackComponent startLpSDPlayback LpSD onResult code=$code")
                        onStartPlayback(code, start.toLong(), NOOIE_PLAYBACK_TYPE_SD)
                    }
                } else {
                    player!!.startAPPlayback(apDeviceId, 0, playbackSeekTime, modelType) { code ->
                        NooieLog.d("-->>> PlaybackComponent startLpSDPlayback LpSD onResult code=$code")
                        onStartPlayback(code, start.toLong(), NOOIE_PLAYBACK_TYPE_SD)
                    }
                }
            }
        } else {

            updatePlayState(PlayState.PLAY_TYPE_SD_PLAYBACK, PlayState.PLAY_STATE_START)
            val playbackSeekTime = getPlaybackSeekTime(start * 1000L, mTodayStartTime, NOOIE_PLAYBACK_TYPE_SD)
            if (isSubDevice) {
                player!!.startMhSDPlayback(mDeviceId, modelType, playbackSeekTime) { code ->
                    NooieLog.d("-->>> PlaybackComponent startLpSDPlayback onResult code=$code")
                    onStartPlayback(code, start.toLong(), NOOIE_PLAYBACK_TYPE_SD)
                }
            } else {
                player!!.startMhSDPlayback(mDeviceId, modelType, playbackSeekTime) { code ->
                    NooieLog.d("-->>> PlaybackComponent startLpSDPlayback onResult code=$code")
                    onStartPlayback(code, start.toLong(),NOOIE_PLAYBACK_TYPE_SD)
                }
            }
        }
    }

    private fun startSDPlayback(deviceId: String, connectionMode: Int, modelType: Int, start: Int) {
        if (connectionMode == CONNECTION_MODE_AP_DIRECT) {

            updatePlayState(PlayState.PLAY_TYPE_SD_PLAYBACK, PlayState.PLAY_STATE_START)
            val playbackSeekTime = getPlaybackSeekTime(start * 1000L, mTodayStartTime, NOOIE_PLAYBACK_TYPE_SD)
            player!!.startAPPlayback(apDeviceId, 0, playbackSeekTime, modelType) { code ->
                NooieLog.d("-->>> PlaybackComponent startSDPlayback onResult code=$code")
                onStartPlayback(code, start.toLong(), NOOIE_PLAYBACK_TYPE_SD)
            }
        } else {

            updatePlayState(PlayState.PLAY_TYPE_SD_PLAYBACK, PlayState.PLAY_STATE_START)
            val playbackSeekTime = getPlaybackSeekTime(start * 1000L, mTodayStartTime, NOOIE_PLAYBACK_TYPE_SD)
            player!!.startNooieSDPlayback(mDeviceId, 0, modelType, playbackSeekTime) { code ->
                NooieLog.d("-->>> PlaybackComponent startSDPlayback onResult code=$code")
                onStartPlayback(code, start.toLong(), NOOIE_PLAYBACK_TYPE_SD)
            }
        }
    }


    fun tryStartCloudVideo(
            modelType: Int,
            start: Long,
            list: List<RecordFragment>,
            fileType: String,
            bindType: Int,
            baseTime: Long,
            expire: Int,
            filePrefix: String
    ) {
        //该时间点有内容，直接播放，无录影，直接提示用户，不跳转到其他时间点
        if (!checkSeekTimeExist(start * 1000L, true)) {
//            ToastUtil.showToast(this, java.lang.String.format(getResources().getString(R.string.seek_no_record), ""))
//            return
        }
        val webUrl = obtainS3Url()
        val uid = obtainUid()
        val token = obtainToken()

        if (player == null || TextUtils.isEmpty(webUrl) || TextUtils.isEmpty(uid) || TextUtils.isEmpty(token)) {
            return
        }

        try {
            val urlBean = IPv4IntTransformer.convertIpAndPort(webUrl)
            if (mIsLpDevice) {
                startLpCloudPlayback(mDeviceId!!, mConnectionMode, mIsSubDevice, uid, token, obtainGapTime(), urlBean, modelType, start, list, fileType, bindType, baseTime, expire, filePrefix)
            } else {
                startCloudPlayback(mDeviceId!!, mConnectionMode, uid, token, obtainGapTime(), urlBean, modelType, start, list, fileType, bindType, baseTime, expire, filePrefix)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * 普通设备回放
     */
    private fun startCloudPlayback(deviceId: String, connectionMode: Int, uid: String, token: String,
                                   gapTime: Int, urlBean: UrlBean, modelType: Int, start: Long, list: List<RecordFragment>, fileType: String,
                                   bindType: Int, baseTime: Long, expire: Int, filePrefix: String) {

        updatePlayState(PlayState.PLAY_TYPE_CLOUD_PLAYBACK, PlayState.PLAY_STATE_START)
        val playbackSeekTime = getPlaybackSeekTime(start * 1000L, mTodayStartTime, NOOIE_PLAYBACK_TYPE_CLOUD)
        player!!.startNooieCloudPlayback(deviceId, modelType, list, uid, fileType, urlBean.ip, urlBean.port,
                bindType, baseTime, gapTime, expire, if (urlBean.isEncryption) 1 else 0, obtainAppId(),
                token, obtainAppSecret(), filePrefix, playbackSeekTime) { code ->
            YRLog.d("-->>> PlaybackComponent startCloudPlayback onResult code=$code")
            onStartPlayback(code, start, NOOIE_PLAYBACK_TYPE_CLOUD)

            YRLog.e("-->>> PlaybackComponent startCloudPlayback params---- deviceId=$deviceId,start =$start,modelType=$modelType," +
                    "list.size=${list.size},uid=$uid,fileType=$fileType,ip=${urlBean.ip},port=${urlBean.port},bindType= $bindType , " +
                    "baseTime=$baseTime,gapTime = $gapTime,expire = $expire,rlBean.isEncryption = ${urlBean.isEncryption}," +
                    "mAppId = ${obtainAppId()},token = $token,mSecret = ${obtainAppSecret()},filePrefix = $filePrefix,playbackSeekTime =$playbackSeekTime")
        }
    }

    /**
     * 低功耗回放
     */
    private fun startLpCloudPlayback(deviceId: String,
                                     connectionMode: Int, isSubDevice: Boolean, uid: String, token: String, gapTime: Int, urlBean: UrlBean,
                                     modelType: Int, start: Long, list: List<RecordFragment>, fileType: String,
                                     bindType: Int,
                                     baseTime: Long,
                                     expire: Int,
                                     filePrefix: String) {


        updatePlayState(PlayState.PLAY_TYPE_CLOUD_PLAYBACK, PlayState.PLAY_STATE_START)
        val playbackSeekTime: Int = getPlaybackSeekTime(start * 1000L,
                mTodayStartTime, NOOIE_PLAYBACK_TYPE_CLOUD)
        if (isSubDevice) {
            player?.startMhCloudPlayback(mDeviceId, modelType, list, uid, fileType, urlBean.ip, urlBean.port, bindType, baseTime, gapTime, expire, if (urlBean.isEncryption) 1 else 0,
                    obtainAppId(), token, obtainAppSecret(), filePrefix, playbackSeekTime) { code ->
                onStartPlayback(code, start, NOOIE_PLAYBACK_TYPE_CLOUD)
            }
        } else {
            player?.startMhCloudPlayback(mDeviceId, modelType, list, uid, fileType, urlBean.ip, urlBean.port, bindType, baseTime,
                    gapTime, expire, if (urlBean.isEncryption) 1 else 0,
                    obtainAppId(), token, obtainAppSecret(), filePrefix, playbackSeekTime) { code ->
                onStartPlayback(code, start, NOOIE_PLAYBACK_TYPE_CLOUD)
            }
        }
    }


    private fun onStartPlayback(code: Int, start: Long, playbackType: Int) {
        if (code == Constant.OK) {

        }
        updatePlayState(if (playbackType == NOOIE_PLAYBACK_TYPE_CLOUD) PlayState.PLAY_TYPE_CLOUD_PLAYBACK else PlayState.PLAY_TYPE_SD_PLAYBACK,
                PlayState.PLAY_STATE_FINISH)
    }

    /**
     * TODO 刷新播放状态
     */
    private fun updatePlayState(type: Int, state: Int) {

    }

    /**
     * 跳到第一个视频回放
     * @param timeStamp
     * @param recordInfoList
     * @return
     */
    private fun pickStartVideoTime(timeStamp: Long, recordInfoList: List<CloudRecordInfo?>): Long {
        if (!mIsInitCurrentTime) {
            return pickTimeCloseToCurrent(mCurrentSeekTime, recordInfoList)
        } else if (mPlaybackSourceType == NOOIE_PLAYBACK_SOURCE_TYPE_DIRECT) {
            mIsInitCurrentTime = false
            return timeStamp
        }

        //从Live跳到回放，默认从第一个时间点开始播
        mIsInitCurrentTime = false
 // 原先的       return if (CollectionUtil.isEmpty(recordInfoList)) timeStamp else if (mIsAsc) recordInfoList[0]!!.startTime + getDetectionTimeLen(recordInfoList[0]) * 1000L else recordInfoList[recordInfoList.size - 1]!!.startTime + getDetectionTimeLen(recordInfoList[recordInfoList.size - 1]) * 1000L
        return if (CollectionUtil.isEmpty(recordInfoList)) timeStamp else  recordInfoList[0]!!.startTime + getDetectionTimeLen(recordInfoList[0]) * 1000L
    }


    /**
     * 云卡切换，从同一个开始播放的时间点播放 -> 没有，就找附近可播的时间点
     *
     * @param currentSeekTime
     * @param recordInfoList
     * @return
     */
    private fun pickTimeCloseToCurrent(currentSeekTime: Long, recordInfoList: List<CloudRecordInfo?>): Long {
//        if (timerShaftPortrait != null && timerShaftPortrait.isInRecordList(currentSeekTime)) {
//            return currentSeekTime
//        }
        var resultTime = currentSeekTime
        var seekTimeGap: Long = 0
        if (CollectionUtil.isNotEmpty(recordInfoList)) {
            for (i in recordInfoList.indices) {
                val recordInfo = recordInfoList[i]
                val timeGap = abs(recordInfo!!.startTime - currentSeekTime)
                if (i == 0) {
                    seekTimeGap = abs(recordInfo.startTime - currentSeekTime)
                    resultTime = recordInfo.startTime + getDetectionTimeLen(recordInfo) * 1000L
                } else if (timeGap < seekTimeGap) {
                    seekTimeGap = timeGap
                    resultTime = recordInfo.startTime + getDetectionTimeLen(recordInfo) * 1000L
                }
            }
        }
        return resultTime
    }

    private fun getDetectionTimeLen(cloudRecordInfo: CloudRecordInfo?): Int {
        var detectionTimeLen: Int = DEFAULT_DETECTION_TIME_LEN
        if (cloudRecordInfo == null || cloudRecordInfo.cloudFileBean == null) {
            return detectionTimeLen
        }
        detectionTimeLen = getDetectionTimeLen(cloudRecordInfo.cloudFileBean)
        return detectionTimeLen
    }

    private fun getDetectionTimeLen(cloudFileBean: CloudFileBean?): Int {
        var detectionTimeLen: Int = DEFAULT_DETECTION_TIME_LEN
        if (cloudFileBean == null) {
            return detectionTimeLen
        }
        if (isDetectionAvailable(cloudFileBean.motionDetectionTime)) {
            detectionTimeLen = cloudFileBean.motionDetectionTime
        } else if (isDetectionAvailable(cloudFileBean.soundDetectionTime)) {
            detectionTimeLen = cloudFileBean.soundDetectionTime
        } else if (isDetectionAvailable(cloudFileBean.pirDetectionTime)) {
            detectionTimeLen = cloudFileBean.pirDetectionTime
        }
        val isOverDayTimeLen: Boolean = (cloudFileBean.startTime + detectionTimeLen) * 1000L - cloudFileBean.baseTime >= DateTimeUtil.DAY_SECOND_COUNT * 1000L
        if (isOverDayTimeLen) {
            detectionTimeLen = if (cloudFileBean.baseTime / 1000L + DateTimeUtil.DAY_SECOND_COUNT - cloudFileBean.startTime < DEFAULT_DETECTION_TIME_LEN) 0 else DEFAULT_DETECTION_TIME_LEN
        }
        return detectionTimeLen
    }

    /**
     * 开始播放时间，卡和云播放时间格式不一样
     */
    private fun getPlaybackSeekTime(seekTime: Long, todayStartTime: Long, playbackType: Int): Int {
        YRLog.d("NOOIE-JNI-demux-cloud seekToSelectTime seektime=" + DateTimeUtil.getUtcTimeString(
                seekTime,
                DateTimeUtil.PATTERN_HMS) + " mTodayStartTime=" + todayStartTime + " time=" + seekTime)
        var playbackSeekTime = (seekTime / 1000L).toInt() //卡直接拿时间
        if (playbackType == NOOIE_PLAYBACK_TYPE_CLOUD) {
            val timePick = (seekTime / 1000L).toInt() - (todayStartTime/ 1000L).toInt()
            playbackSeekTime = if (timePick > 0) timePick  else 0
        }
        return playbackSeekTime

    }

    /**
     * 该时间点有内容，直接播放，无录影，直接提示用户，不跳转到其他时间点
     */
    private fun checkSeekTimeExist(seekTime: Long, showToast: Boolean): Boolean {

        //        if (timerShaftPortrait != null && !timerShaftPortrait.isInRecordList(seekTime)) {
        //            if (showToast && mPlayerView != null) {
        //                mPlayerView.showNoRecording(false, seekTime)
        //            }
        //            return false
        //        }
        return true
    }

    fun isDetectionAvailable(time: Int): Boolean {
        return time != -1
    }

    private fun obtainAppId() : String {
        return ""
    }

    private fun obtainAppSecret() : String {
        return ""
    }

    private fun obtainUid() : String {
        return ""
    }

    private fun obtainToken() : String {
        return ""
    }

    private fun obtainS3Url() : String {
        return ""
    }

    private fun obtainGapTime() : Int {
        return 0
    }
}