package com.apemans.smartipcimpl.ui.ipcdevices.player


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.alibaba.android.arouter.facade.annotation.Route
import com.apemans.base.utils.NetUtil
import com.apemans.ipcchipproxy.define.IPC_SCHEME_STORAGE_STATUS_NORMAL
import com.apemans.ipcchipproxy.define.IPC_SCHEME_STORAGE_STATUS_NO_SD
import com.apemans.logger.YRLog
import com.apemans.quickui.alerter.alertInfo
import com.apemans.smartipcapi.info.IpcDeviceInfo
import com.apemans.smartipcapi.services.path.FRAGMENT_PATH_SMART_IPC_DEVICE_PLAY
import com.apemans.smartipcimpl.R
import com.apemans.smartipcimpl.constants.*
import com.apemans.smartipcimpl.ui.ipcdevices.player.adapter.DateTimeAdapter
import com.apemans.smartipcimpl.ui.ipcdevices.player.adapter.NooiePlayerDevicesAdapter
import com.apemans.smartipcimpl.ui.ipcdevices.player.bean.*
import com.apemans.smartipcimpl.ui.ipcdevices.player.com.hdl.ruler.bean.OnBarMoveListener
import com.apemans.smartipcimpl.ui.ipcdevices.player.com.hdl.ruler.bean.OnBarMoveListenerIPC
import com.apemans.smartipcimpl.ui.ipcdevices.player.com.hdl.ruler.bean.TimeSlot
import com.apemans.smartipcimpl.ui.ipcdevices.player.com.hdl.ruler.utils.DateUtils
import com.apemans.smartipcimpl.ui.ipcdevices.player.model.IpcPlayBackModel
import com.apemans.smartipcimpl.ui.ipcdevices.player.model.PlaybackComponent
import com.apemans.smartipcimpl.ui.ipcdevices.player.old.bean.*
import java.util.*


/***********************************************************
 * 作者: zhengruidong@apemans.com
 * 日期: 2021/11/10 7:05 下午
 * 说明:IPC视频回放页面
 * 回放业务：(1)初始化是否有云、卡存储：initStorageInfo()
 *         (2)初始化云、卡存储的天数数据：setDataList(sdCardDateList)\ setDataList(cloudDateList)
 *         (3)点击某天回放： chooseTimeline(data)
 *         (3)滑动选择回放某个时间点: playbackSeekerTime(currentTime)
 *         (4)开始云回放：startLoadDeviceCloudRecord()
 *            开始sd卡回放: checkBeforeLoadingSDPlayback()
 *         (5)加载刻度尺：开始点和渲染视频区域  freshTimeline()
 *
 *
 ***********************************************************/
@Route(path = FRAGMENT_PATH_SMART_IPC_DEVICE_PLAY)
class IpcPlayerActivity : IpcPlayerLiveActivity() {
    private lateinit var playbackViewModel: IpcPlayBackModel

    /**
     * 要回放的当天总数据(云数据)
     */
    var playbackCloudData = PlaybackCloudData()
    /**
     * 要回放的当天总数据(sd 卡数据)
     */
    var listRecordInfo =  ArrayList<CloudRecordInfo>()
    /**
     *  当前拖动时间
     */
    var dragTime: Long = 0

    /**
     * Sd卡日期列表
     */
    var sdCardDateList = ArrayList<CalenderBean>()

    /**
     * 云存储日期列表
     */
    var cloudDateList = ArrayList<CalenderBean>()

    /**
     * 回放视频区域
     */
    var timeSlotData = ArrayList<TimeSlot>()

    var playbackComponent: PlaybackComponent? = null

    var isPause = true

    override fun onViewCreated(savedInstanceState: Bundle?) {
        player = binding.livePreviewPlayer
        playbackViewModel = registerViewModule(IpcPlayBackModel::class.java)
        initBase() //初始化设备信息
        initStorageInfo()//存储卡、云状态

        initLiveDevice()
        initPlayFunction()

        initClickListener()
        initDeviceList()
        initPlaybackComponent()
        initPlayerGestureListenerView()
    }
    /**
     * 初始化设备列表，点击更换设备
     */
    private fun initDeviceList() {
        mDeviceListAdapter = NooiePlayerDevicesAdapter()
        val linearLayoutManager = LinearLayoutManager(this)
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        binding.rvDeviceList.layoutManager = linearLayoutManager
        binding.rvDeviceList.adapter = mDeviceListAdapter
        mDeviceListAdapter?.setDataList(getIpcDeviceList())
        mDeviceListAdapter?.setListener(object : NooiePlayerDevicesAdapter.OnItemClickListener{
            override fun onItemClick(position: Int, data: IpcDeviceInfo) {
                ipcDeviceInfo  = data
                hindFolderSelect()
                loadDeviceInfo()
                restartLivePlayer()
                initStorageInfo()
                initFunctionData()
                mDeviceListAdapter?.setDataList(getIpcDeviceList()) //刷新列表
            }
        })
    }
    /**
     * 根据直播或者回放控制
     */
    override fun onResume() {
        super.onResume()
        isPause = false
        if (!NetUtil.isOnline(this)) {
            alertInfo(getString(R.string.ty_network_error))
            return
        }
        queryDeviceUpdateStatus() //检查当前设备是否在升级
        mDevicePowerMode = DEVICE_POWER_MODE_NORMAL

        if (mIsLive) {  //直播
            resumeData(true, false, false) //
        } else {  //回放
            resumePlaybackData(true)
        }
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON) //屏幕高亮
    }

    override fun onPause() {
        super.onPause()
        isPause = true

    }
    /**
     * 其他页面过来加载回放，刷新UI
     */
    private fun resumePlaybackData(isQueryUpdate: Boolean) {
        if (playbackType == NOOIE_PLAYBACK_TYPE_LIVE) return
        refreshPlayerControl()
        displayFpsAndBit(true)
        startLoadPlayback(playbackType, playbackViewModel.getTodayStartTime(directTime) / 1000L, false)
    }

    /**
     * 回放組件初始化，
     */
    private fun initPlaybackComponent() {
        playbackComponent = PlaybackComponent()
        playbackComponent?.initData(player!!, mDeviceId, bindtype, mIsSubDevice, mIsLpDevice, mConnectionMode!!, modelType, playbackType, playbackSourceType, apDeviceId)
        playbackComponent?.setupPlayback(playbackType, playbackSourceType, directTime)
    }

    /**
     * (1)初始化是否有云、卡存储
     */
    private fun initStorageInfo() {
        binding.layMenu.visibility = View.GONE

        if (mDeviceId == null) {
            return
        }

        playbackViewModel.loadPackageInfo(mDeviceId!!, mIsOwner) {
            if (it?.status == PACKAGE_INFO_STATUS_DEFAULT || it?.status == PACKAGE_INFO_STATUS_ON) { //支持云存储
                binding.icCloud.visibility = View.VISIBLE
                cloudDateList = playbackViewModel.getUtcRecentDays(it?.file_time)//云套餐支持天数
                setDataList(cloudDateList)
                playbackType = NOOIE_PLAYBACK_TYPE_CLOUD
                checkIsSupportCloud = true
                binding.layMenu.visibility = View.VISIBLE //没有卡、云存储，隐藏回放操作栏
            }else{
                checkIsSupportCloud = false
                binding.icCloud.visibility = View.GONE
            }
            YRLog.d ("initStorageInfo---checkIsSupportCloud = $checkIsSupportCloud")
        }


        checkIsSupportDiskCard()

        binding.icSdCard.setOnClickListener {
            binding.icSdCard.setBackgroundResource(R.color.theme_color)
            binding.icCloud.background = null
            playbackType = NOOIE_PLAYBACK_TYPE_SD
            setDataList(sdCardDateList)
        }
        binding.icCloud.setOnClickListener {
            binding.icCloud.setBackgroundResource(R.color.theme_color)
            binding.icSdCard.background = null
            playbackType = NOOIE_PLAYBACK_TYPE_CLOUD
            setDataList(cloudDateList)
        }
    }


    /**
     * (2)初始化云、卡存储的天数数据
     */
    private fun setDataList(dateList: ArrayList<CalenderBean>) {
        if (dateList == null) {
            return
        }
        initTimeline()
        dateTimeAdapter?.setDataList(dateList)
        if (dateList.size > 1) {
            binding.rcyTime.scrollToPosition(dateList.size - 1)
        }
    }

    /**
     * (3)点击某天回放
     */
    fun chooseTimeline( data: CalenderBean){
        if (isShowGuideOrPopView) return
        mIsLive = false
        initPlayerGestureListenerView()
        directTime = data.calendar.timeInMillis  //选择从某天开始

        if (playbackType == NOOIE_PLAYBACK_TYPE_CLOUD) {
            startLoadDeviceCloudRecord(data.calendar.timeInMillis / 1000L,true)
        } else if (playbackType == NOOIE_PLAYBACK_TYPE_SD) {
            checkBeforeLoadingSDPlayback( data.calendar.timeInMillis / 1000L, false,true);
        }
    }

    /**
     * (3)滑动选择回放某个时间点
     * 1、检测当前seekTime 是否有存储数据，没有提示
     * 2、播放当前刻度的时间下的视频
     * @param currentTime 是实际时间，需要加时区
     */
    private  fun playbackSeekerTime (currentTime: Long){
        if (!checkSeekTimeExist(currentTime )){
            showNoRecording(currentTime)
            return
        }
        resetDefault()
        playbackComponent?.setupPlayback(playbackType, NOOIE_PLAYBACK_SOURCE_TYPE_NORMAL, directTime)
        val time = directTime + (currentTime - DateUtils.getTodayStart(directTime)  )

        if (playbackType == NOOIE_PLAYBACK_TYPE_CLOUD){
            playbackComponent?.showCloudRecordList(playbackCloudData, time, isPause)
        }else if (playbackType == NOOIE_PLAYBACK_TYPE_SD){
            playbackComponent?.showSDCardRecordList(listRecordInfo, time,isPause)
        }

    }


    /**
     * 其他页面过来加载回放
     * @param playbackType 卡回放、云回放
     * @param start 开始回放的时间， 秒
     * @param isNeedDelay 是否延迟回放
     */
    private fun startLoadPlayback(playbackType: Int, start: Long, isNeedDelay: Boolean) {
        if (playbackType == NOOIE_PLAYBACK_TYPE_CLOUD) {
            startLoadDeviceCloudRecord(start,false);
        } else {
            checkBeforeLoadingSDPlayback( start, isNeedDelay,false);
        }
    }



    /**
     * （4）开始sd卡回放
     */
    private fun checkBeforeLoadingSDPlayback(start: Long, isNeedDelay: Boolean,isShowTimeLine: Boolean) {
        playbackViewModel.loadStorageInfo(mDeviceId!!) { storageInfo ->
            if (storageInfo?.status == IPC_SCHEME_STORAGE_STATUS_NORMAL) {   //卡正常
                 playbackViewModel.getSDCardRecordList(mDeviceId!!, start){
                    if (it == null ){
                        alertInfo(resources.getString(R.string.tcp_preview_storge_empty))
                    }else{
                        resetDefault()
                        listRecordInfo = it
                        playbackComponent?.setupPlayback(playbackType, NOOIE_PLAYBACK_SOURCE_TYPE_NORMAL, directTime)
                        playbackComponent?.showSDCardRecordList(it, 0,isPause)
                        freshTimeline(it)
                        isShowTimeLine(isShowTimeLine)
                    }
                }

            }else{
                alertInfo(getString(R.string.tcp_preview_sdcard_storage_error))
            }
        }
    }

    /**
     * (4)开始云回放:获取云存储某天，存储文件,并播放回放视频
     * @param start 分
     * @param isShowTimeLine 其他页面过来不是点击某天的，不用显示时分刻度尺
     */
    private fun startLoadDeviceCloudRecord(start: Long,isShowTimeLine :Boolean) {
//        if (!checkIsSupportCloud){
//            alertInfo(resources.getString(R.string.tcp_preview_could_storage_empty))
//            return
//        }
        if (mDeviceId != null && mUserAccount != null && start != 0L) {
            playbackViewModel.getCloudVideoFileList(mDeviceId!!, mUserAccount!!, start, bindtype, mIsLpDevice) {
                resetDefault()
                if (it == null) {
                    alertInfo(resources.getString(R.string.tcp_preview_storge_empty))
                    return@getCloudVideoFileList
                }

                playbackCloudData = it
                playbackComponent?.setupPlayback(playbackType, NOOIE_PLAYBACK_SOURCE_TYPE_NORMAL, directTime)
                playbackComponent?.showCloudRecordList(playbackCloudData, 0, isPause)
                freshTimeline(playbackCloudData.result)
                isShowTimeLine(isShowTimeLine)
            }
        }

    }

    /**
     * （5）刷新刻度尺:开始点和渲染视频区域
     *  currentTimeMillis 初始化开始时间
     * @param data 后台保存侦测的时间片段  毫秒
     */
    private fun freshTimeline(data: List<CloudRecordInfo>) {
        if (data.size == null) {
            return
        }
        binding.timeline.currentTimeMillis = DateUtils.getTodayStart(directTime)+ (data[0].startTime - directTime) //0 点毫秒+实际开始时间

        binding.timeline.openMove()
        timeSlotData.clear()
        data.forEach {
            var startTime = DateUtils.getTodayStart(directTime)+ (it.startTime - directTime)
            timeSlotData.add(TimeSlot(DateUtils.getTodayStart(directTime), startTime, startTime+ it.timeLen))
        }
        /**
         * 添加有侦测的录像片段
         */
        binding.timeline.setVedioTimeSlot(timeSlotData)
    }

    /**
     * 初始化时间，点击事件
     */
    private fun initTimeline() {
        dateTimeAdapter?.setListener(object : DateTimeAdapter.OnItemClickListener {
            override fun onItemClick(position: Int, data: CalenderBean) {
                if (data != null) {
                    chooseTimeline(data)
                }
            }
        })
        binding.icCloseTimeline.setOnClickListener { isShowTimeLine(false) }
        binding.timeline.setOnBarMoveListener { currentTime ->
            dragTime = currentTime
            playbackSeekerTime(currentTime)
            YRLog.d { "拿到拖动时间timeline---onBarMoveFinish() ${DateUtils.getDateTime(currentTime)}" }
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        if (player != null) {
            player?.destroy()
            player = null
        }
    }
//   -------------------------------- 以下辅助方法----------------------------------------------------------
    private  fun checkSeekTimeExist(currentTime: Long) :Boolean{
        var isTimeExist = false
        for (timeSlot in timeSlotData) {
            if (timeSlot.startTime.toInt() < currentTime.toInt() && currentTime.toInt() < timeSlot.endTime.toInt() ){
                isTimeExist  =true
            }
        }
        return isTimeExist
    }
    /**
     * 展示\关闭回放刻度尺UI
     */
    private fun isShowTimeLine(isShow: Boolean) {
        binding.icCloud.visibility = if (!isShow && checkIsSupportCloud) View.VISIBLE else View.GONE
        binding.icSdCard.visibility = if (!isShow && checkIsSupportDiskCard) View.VISIBLE else View.GONE
        binding.rcyTime.visibility = if (isShow) View.GONE else View.VISIBLE
        displayLiveTag(!isShow)
        binding.timeline.visibility = if (isShow) View.VISIBLE else View.GONE
        binding.icCloseTimeline.visibility = if (isShow) View.VISIBLE else View.GONE
    }

    /**
     * 卡回放是否支持
     * 注意：分享者不支持sd卡
     */
    private fun checkIsSupportDiskCard() {
        if (!mIsOwner){
            binding.icSdCard.visibility = View.GONE
            checkIsSupportDiskCard =false
            return
        }
        playbackViewModel.loadStorageInfo(mDeviceId!!) {
            if (it?.status != IPC_SCHEME_STORAGE_STATUS_NO_SD) {
                binding.icSdCard.visibility = View.VISIBLE
                sdCardDateList = playbackViewModel.getUtcRecentDays(92) //回放日期列表
                if (!checkIsSupportCloud) {
                    playbackType = NOOIE_PLAYBACK_TYPE_SD
                    setDataList(sdCardDateList)
                }
                binding.layMenu.visibility = View.VISIBLE //没有卡、云存储，隐藏回放操作栏

                checkIsSupportDiskCard = true
            }else{
                binding.icSdCard.visibility = View.GONE
                checkIsSupportDiskCard =false
            }
            YRLog.d ("initStorageInfo---checkIsSupportDiskCard = $checkIsSupportDiskCard")
        }
    }



    private fun showNoRecording(time: Long) {
        alertInfo(java.lang.String.format( resources.getString(R.string.seek_no_record),
            DateUtils.getDateTime(time)
        )
        )
    }
}