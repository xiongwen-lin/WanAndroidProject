package com.apemans.smartipcimpl.ui.ipcdevices.player

import android.app.Activity
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.text.TextUtils
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.animation.LinearInterpolator
import androidx.recyclerview.widget.LinearLayoutManager
import com.apemans.base.utils.DisplayHelper
import com.apemans.yruibusiness.base.BaseComponentActivity
import com.apemans.dmapi.status.DeviceDefine
import com.apemans.logger.YRLog
import com.apemans.smartipcapi.info.IpcDeviceInfo
import com.apemans.smartipcimpl.R
import com.apemans.smartipcimpl.constants.*
import com.apemans.smartipcimpl.databinding.DeviceActivityTplPreviewBinding
import com.apemans.smartipcimpl.ui.ipcdevices.player.adapter.NooiePlayerDevicesAdapter
import com.apemans.smartipcimpl.ui.ipcdevices.player.bean.CONNECTION_MODE_QC
import com.apemans.smartipcimpl.ui.ipcdevices.player.bean.DEVICE_POWER_MODE_NORMAL
import com.apemans.smartipcimpl.ui.ipcdevices.player.bean.NOOIE_PLAYBACK_SOURCE_TYPE_NORMAL
import com.apemans.smartipcimpl.ui.ipcdevices.player.bean.NOOIE_PLAYBACK_TYPE_LIVE
import com.apemans.smartipcimpl.ui.ipcdevices.player.model.IpcPlayBaseModel
import com.apemans.smartipcimpl.ui.ipcdevices.player.old.bean.IpcType
import com.apemans.smartipcimpl.ui.ipcdevices.player.old.delegate.PlayState
import com.apemans.smartipcimpl.ui.ipcdevices.player.old.helper.DeviceHelper
import com.apemans.smartipcimpl.ui.ipcdevices.player.util.NooieDeviceHelper
import com.apemans.smartipcimpl.ui.ipcdevices.player.util.PlayFunctionHelper
import com.apemans.smartipcimpl.ui.ipcdevices.player.util.PlayerFileUtils
import com.apemans.smartipcimpl.ui.ipcdevices.player.util.PlayerGestureListenerView
import com.apemans.smartipcimpl.ui.ipcdevices.player.view.guideview.Component
import com.apemans.smartipcimpl.ui.ipcdevices.player.view.guideview.Guide
import com.apemans.smartipcimpl.ui.ipcdevices.player.view.guideview.GuideBuilder
import com.apemans.smartipcimpl.ui.ipcdevices.player.view.guideview.PanelComponent
import com.dylanc.longan.isLandscape
import com.nooie.common.utils.file.FileUtil
import com.nooie.common.utils.file.MediaStoreUtil
import com.nooie.common.utils.graphics.DisplayUtil
import com.nooie.common.utils.time.DateTimeUtil
import com.nooie.sdk.listener.OnPlayerListener
import com.nooie.sdk.media.NooieMediaPlayer
import pl.droidsonroids.gif.GifDrawable
import java.util.*

/**
 * @Author:dongbeihu
 * @Description: 播放器UI基类：一些播放设备基础跟业务无关、少变动的
 *（1）横竖屏onConfigurationChanged()
 * (2)摄像头手势控制 initPlayerGestureListenerView()
 * (3)摄像头手势引导图 showPanelGuideView()
 * @Date: 2022/1/10-10:07
 */
abstract class IpcPlayerBaseActivity : com.apemans.yruibusiness.base.BaseComponentActivity<DeviceActivityTplPreviewBinding>(),
    OnPlayerListener {
    /***
     * 设备唯一id
     *
     */
    var mDeviceId: String? = null

    private val PLAY_STATE_TAG = "NOOIE-OnPlayerListener---"
    //设置播放器比例
    private val screenFactor = 16 / 9.toFloat()
    var isShowLandsFun = false

    /**
     * 播放器
     */
    var player: NooieMediaPlayer? = null

    var mIsLive = true        //默认是直播模式

    private lateinit var playBaseModel: IpcPlayBaseModel

    private var mDeviceGuideAnim: GifDrawable? = null

    /**
     * 是否弹出手势引导图图、设备popView
     */
    var isShowGuideOrPopView = false

    var checkIsSupportCloud = false //云存储是否支持
    var checkIsSupportDiskCard = false //卡存储是否支持

    private var showDeviceList = false
    var mDeviceListAdapter: NooiePlayerDevicesAdapter? = null

    var ipcDeviceInfo: IpcDeviceInfo? = null

    /**
     * 网络连接模式
     */
    var mConnectionMode: Int = 0

    var mModel: String? = null //设备所属产品型号
    var modelType: Int = 0
    var mDeviceType: IpcType = IpcType.IPC_1080


    var bindtype = DeviceDefine.BIND_TYPE_OWNER    //当前设备归属，默认自己
    var mIsOwner = true            //是否主人或者分享者
    var mIsSubDevice = false        //是否子设备
    var mIsLpDevice = false        //是否低功耗，需要点击开启，延迟打开直播
    var mDevicePowerMode: Int = DEVICE_POWER_MODE_NORMAL

    var isOnLine = false        //默认是不在线

    var playbackType = NOOIE_PLAYBACK_TYPE_LIVE  //播放类型，默认直播
    var playbackSourceType = NOOIE_PLAYBACK_SOURCE_TYPE_NORMAL //回放数据模式，直连还是普通模式

    var playState = PlayState.PLAY_STATE_INIT
    var mPDeviceId: String? = null //绑定父设备id

    /**
     * 录像状态
     */
    var mRecording = false
    var mSaveRcecordFile = ArrayList<String>()
    /**
     * 用户账号
     */
    var mUserAccount: String? = obtainUid()

    /**
     * 设置要播放回放的具体某天时间，毫秒
     */
    var directTime: Long = 0

    /**
     * 截屏默认需要缩略图提示,P1增加方位false
     */
    var isSnapShotNeedTips  = true

    fun initBase() {
        player?.setPlayerListener(this)
        playBaseModel = registerViewModule(IpcPlayBaseModel::class.java)
        mDeviceId = intent.getStringExtra(INTENT_KEY_DEVICE_ID)
        playbackType = intent.getIntExtra(INTENT_KEY_DATA_TYPE, NOOIE_PLAYBACK_TYPE_LIVE)
        directTime = intent.getLongExtra(INTENT_KEY_TIME_STAMP, 0);

        mConnectionMode = intent.getIntExtra(INTENT_KEY_CONNECTION_MODE, CONNECTION_MODE_QC)
        playbackSourceType = intent.getIntExtra(INTENT_KEY_START, NOOIE_PLAYBACK_SOURCE_TYPE_NORMAL)
        getIpcDeviceInfo()
    }

    /**
     * 刷新加载设备信息
     */
    fun loadDeviceInfo() {
        mDeviceId = ipcDeviceInfo?.device_id
        if (mDeviceId.isNullOrEmpty() || ipcDeviceInfo == null) {
            YRLog.e("mDeviceId.isNullOrEmpty()|| ipcDeviceInfo == null,check device info!!")
            return
        }
        mModel =if (TextUtils.isEmpty(ipcDeviceInfo?.model)) IpcType.IPC_1080.type else ipcDeviceInfo?.model
        mDeviceType = if (TextUtils.isEmpty(ipcDeviceInfo?.model)) IpcType.IPC_1080
                      else IpcType.getIpcType(ipcDeviceInfo?.model)
        mPDeviceId = ipcDeviceInfo?.pDeviceId
        isOnLine = ipcDeviceInfo?.online == ONLINE_STATUS_ON && ipcDeviceInfo?.openStatus == ONLINE_STATUS_ON
        bindtype = ipcDeviceInfo?.bindType!!
        mIsOwner = DeviceDefine.BIND_TYPE_OWNER == bindtype
        mIsSubDevice = !TextUtils.isEmpty(mPDeviceId) && mPDeviceId != "1"
        modelType = DeviceHelper.convertNooieModel(mDeviceType, mModel);
        mIsLpDevice = NooieDeviceHelper.isLpDevice(mModel)
        mIsLive = playbackType == NOOIE_PLAYBACK_TYPE_LIVE

        binding.headerName.text = ipcDeviceInfo?.name
        binding.headerName2.text = ipcDeviceInfo?.name
    }

    fun initPlayClickListener() {
        binding.viewActionFullscreen.setOnClickListener {
            //横屏显示,配合onConfigurationChanged
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        }
        binding.viewActionFullscreenOff.setOnClickListener {
            //竖屏显示,配合onConfigurationChanged
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        }
        /**
         * 横屏 展示功能UI
         */
        player!!.setPlayerClickListener {
            showLandsControlUI(!isShowLandsFun)
            isShowLandsFun = !isShowLandsFun
        }
        binding.layHeader.setOnClickListener {
            if (showDeviceList) hindFolderSelect() else showFolderSelect()
        }
    }



    /**
     * 获取ICP设备列表
     */
     fun getIpcDeviceList(): List<IpcDeviceInfo> {
        var ipcDeviceListData: MutableList<IpcDeviceInfo> = ArrayList()
        playBaseModel.ipcDeviceList.value?.let { list ->
            list.forEach { ipcDevice ->
                (ipcDevice?.device?.deviceInfo as? IpcDeviceInfo)?.let {
                    if (mDeviceId != it.device_id) { //列表去除当前设备
                        ipcDeviceListData.add(it)

                    }
                }
            }
        }
        return ipcDeviceListData
    }
    /**
     * 根据device,获取ICP设备列表中的设备信息
     */
    private fun getIpcDeviceInfo(){
        playBaseModel.ipcDeviceList.value?.let { list ->
            list.forEach { ipcDevice ->
                (ipcDevice?.device?.deviceInfo as? IpcDeviceInfo)?.let {
                    if (mDeviceId == it.device_id) { //列表去除当前设备
                        ipcDeviceInfo = it
                        loadDeviceInfo()
                    }
                }
            }
        }
    }
    /**
     * 点击设备，展开设备列表
     * 注意只是多了一个遮罩层，以前的点击事件还是会触发
     */
    private fun showFolderSelect() {
        val animation = AnimationUtils.loadAnimation(context, R.anim.folder_dialog_in);
        animation.interpolator = LinearInterpolator()
        binding.deviceListContainer.clearAnimation()
        binding.deviceListContainer.visibility = View.VISIBLE
        binding.deviceListContainer.startAnimation(animation)
        showDeviceList = true
        isShowGuideOrPopView = true
    }

    fun hindFolderSelect() {//点击设备，关闭设备列表
        val animation = AnimationUtils.loadAnimation(context, R.anim.folder_dialog_out)
        animation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) {
            }

            override fun onAnimationEnd(animation: Animation?) {
                binding.deviceListContainer.clearAnimation()
                binding.deviceListContainer.postInvalidate()
                binding.deviceListContainer.visibility = View.GONE
            }

            override fun onAnimationRepeat(animation: Animation?) {
            }

        })
        animation.interpolator = LinearInterpolator()
        binding.deviceListContainer.startAnimation(animation)
        showDeviceList = false
        isShowGuideOrPopView = false
    }


    /**
     * 摄像头手势控制,直播可以控制，回放不可以展示
     */
    fun initPlayerGestureListenerView() {
        YRLog.d("displayGestureGuideView 播放类型${mIsLive},支持手势playerGestureListenerView ")
        var isSupportV = playBaseModel.checkIsSupportRotationDirectionUD(mDeviceId!!)
        var isSupportH = playBaseModel.checkIsSupportRotationDirectionLR(mDeviceId!!)
        if (mIsLive) {
            val playerGestureListenerView = PlayerGestureListenerView(
                isSupportH,
                isSupportV,
                binding.ivDirectionControlBg,
                binding.ivGestureLeftArrow,
                binding.ivGestureTopArrow,
                binding.ivGestureRightArrow,
                binding.ivGestureBottomArrow
            )
            player?.setGestureListener(playerGestureListenerView)
        } else {
            player?.setGestureListener(null)
            YRLog.d("displayGestureGuideView 回放,暂不支持手势 ")
        }
        if (PlayFunctionHelper.getGestureGuideView(
                mDeviceId,
                obtainUid()
            ) && (isSupportV || isSupportH)
        ) { //用户未使用设备，并且设备有上下左右手势控制
            showPanelGuideView()
            PlayFunctionHelper.saveGestureGuideView(mDeviceId, obtainUid(), false)
        }
    }

    //横竖屏
    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
            changeToPortraitLayout()
        } else {
            changeToLandscapeLayout()
        }
    }

    private fun showPanelGuideView() {
        var mPanelGuide: Guide? = null
        val panelComponent = PanelComponent(DisplayUtil.SCREEN_WIDTH_PX) {
            if (mPanelGuide != null) {
                mPanelGuide?.dismiss()
            }
        }
        mPanelGuide = showCustomGuideView(player, null, object :
            GuideBuilder.OnVisibilityChangedListener {
            override fun onShown() {
                showDeviceGuideAnim()
            }

            override fun onDismiss() {
                hideDeviceGuideAnim()
            }

        }, panelComponent, this)
    }

    fun showDeviceGuideAnim() {

        try {
            binding.ivPanelComponent.visibility = View.VISIBLE
            binding.tvPanelTip.visibility = View.VISIBLE
            var isSupportV = playBaseModel.checkIsSupportRotationDirectionUD(mDeviceId!!)
            var isSupportH = playBaseModel.checkIsSupportRotationDirectionLR(mDeviceId!!)
            val guideAnimRes: Int =
                if (isSupportV || isSupportH) R.raw.device_guide_ipc_100 else R.raw.device_guide_ipc_100_horizontal
            mDeviceGuideAnim = GifDrawable(resources, guideAnimRes)
            mDeviceGuideAnim?.loopCount = 1
            mDeviceGuideAnim?.setSpeed(1.0f)
            mDeviceGuideAnim?.addAnimationListener { }
            binding.ivPanelComponent.setImageDrawable(mDeviceGuideAnim)
            mDeviceGuideAnim?.start()
            isShowGuideOrPopView = true
        } catch (e: Exception) {
            binding.ivPanelComponent.visibility = View.GONE
            binding.tvPanelTip.visibility = View.GONE
            isShowGuideOrPopView = false
        }
    }

    private fun hideDeviceGuideAnim() {

        try {
            if (mDeviceGuideAnim != null) {
                mDeviceGuideAnim?.stop()
            }
        } catch (e: Exception) {
            e.toString()
        }
        binding.ivPanelComponent.visibility = View.GONE
        binding.tvPanelTip.visibility = View.GONE
        isShowGuideOrPopView = false
    }

    open fun showCustomGuideView(
        targetView: View?,
        guideBuilder: GuideBuilder?,
        listener: GuideBuilder.OnVisibilityChangedListener?,
        component: Component?,
        containerActivity: Activity?
    ): Guide? {
        var guideBuilder = guideBuilder
        if (guideBuilder == null) {
            guideBuilder = GuideBuilder()
            guideBuilder = guideBuilder.setTargetView(targetView)
                .setAlpha(100)
                .setHighTargetCorner(1)
                .setHighTargetPadding(0)
                .setOverlayTarget(false)
                .setAutoDismiss(true)
                .setOutsideTouchable(true)
        }
        guideBuilder!!.setOnVisibilityChangedListener(listener)
        guideBuilder!!.addComponent(component)
        val guide = guideBuilder!!.createGuide()
        guide.setShouldCheckLocInWindow(true)
        guide.show(containerActivity)
        return guide
    }

    /**
     * 竖屏
     */
    private fun changeToPortraitLayout() {
        binding.viewActionFullscreen.visibility = View.VISIBLE
        binding.viewActionFullscreenOff.visibility = View.GONE
        binding.previewToolbar.visibility = View.VISIBLE
        binding.layBottomCloud.visibility = View.VISIBLE
        binding.recyclerViewFunction.visibility = View.VISIBLE
        binding.layFunctionLands.visibility = View.GONE
        binding.layTplDirection.layDirection.visibility = View.GONE
        if (checkIsSupportCloud || checkIsSupportDiskCard) {
            binding.layMenu.visibility = View.VISIBLE
        }
        activity.window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)

        if (player != null) {
            val lp = player!!.layoutParams
            lp.width = DisplayHelper.getScreenWidth(this)
            lp.height = ((DisplayHelper.getScreenWidth(this) / screenFactor).toInt())
            player!!.layoutParams = lp
        }
    }

    /**
     * 横屏
     */
    private fun changeToLandscapeLayout() {
        binding.recyclerViewFunction.visibility = View.GONE
        binding.previewToolbar.visibility = View.GONE
        binding.layBottomCloud.visibility = View.GONE
        binding.viewActionFullscreen.visibility = View.GONE
        binding.viewActionFullscreenOff.visibility = View.VISIBLE
        activity.window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        showLandsControlUI(false)
    }


    /**
     * 展示横屏功能 ,
     * @param isShow 是否展示功能UI
     */
    private fun showLandsControlUI(isShow: Boolean) {
        if (!isLandscape) {
            return
        }
        if (isShow) {
            binding.layFunctionLands.visibility = View.VISIBLE
            if (checkIsSupportCloud || checkIsSupportDiskCard) { //有卡、云存储，展示回放操作栏
                binding.layMenu.visibility = View.VISIBLE
                player?.layoutParams = getScreenLpByFactor(
                    player?.layoutParams,
                    DisplayHelper.getScreenWidth(this),
                    DisplayHelper.getScreenHeight(this),
                    DisplayHelper.dpToPx(50)  //去除底部操作栏高度binding.layMenu.height 450
                )
            } else {
                player?.layoutParams = getScreenLpByFactor(
                    player?.layoutParams, DisplayHelper.getScreenWidth(this),
                    DisplayHelper.getScreenHeight(this), 0
                )
            }
        } else {
            binding.layFunctionLands.visibility = View.GONE
            binding.layMenu.visibility = View.GONE
            player?.layoutParams = getScreenLpByFactor(
                player?.layoutParams, DisplayHelper.getScreenWidth(this),
                DisplayHelper.getScreenHeight(this), 0
            )
        }
    }

    /**
     * 播放器屏幕尺寸调整
     */
    private fun getScreenLpByFactor(
        lp: ViewGroup.LayoutParams?,
        width: Int,
        height: Int,
        countHeight: Int
    ): ViewGroup.LayoutParams? {
        YRLog.d("最初屏幕getScreenLpByFactor--width=$width ,height =$height,countHeight =$countHeight")
        var width = width
        var height = height - countHeight
        if (lp == null || width == 0 || height == 0) {
            return lp
        }
        if (countHeight > 0) {  //横屏高度，留空间给底部操作栏，只能改宽度
            lp.width = width
            lp.height = height
            return lp
        }
        val factor = width.toFloat() / height
        if (factor < screenFactor) {
            width = (height * screenFactor).toInt()
        } else {
            height = (width / screenFactor).toInt()
        }
        lp.width = width
        lp.height = height
        return lp
    }

    fun displayFpsAndBit(show: Boolean) {
        binding.livePreviewTxt.visibility = if (show) View.VISIBLE else View.INVISIBLE
    }

    /**
     * 更新播放状态
     * @param type 播放类型
     * @param state 播放状态
     */
    fun updatePlayState(type: Int, state: Int) {
        playState = state
    }

    open fun refreshPlayerControl(){

    }
    //    -------------------------OnPlayerListener播放回调-------start----------------------

    override fun onVideoStart(player: NooieMediaPlayer?) {
        YRLog.d { PLAY_STATE_TAG + "player.onVideoStart()" }
        updatePlayState(PlayState.PLAY_TYPE_LIVE, PlayState.PLAY_STATE_START)


        val file = FileUtil.getPreviewThumbSavePath(this, mDeviceId)
        player?.snapShot(file)

        val isOpenAudio: Boolean = PlayFunctionHelper.getAudioState(mDeviceId!!)
        player?.setWaveoutState(isOpenAudio)
        PlayFunctionHelper.changePhoneVolume(this, isOpenAudio)
        refreshPlayerControl() //刷新功能区
    }


    override fun onVideoStop(player: NooieMediaPlayer?) {
        YRLog.d { PLAY_STATE_TAG + "player.onVideoStop()" }
    }

    override fun onAudioStart(player: NooieMediaPlayer?) {
        YRLog.d { PLAY_STATE_TAG + "player.onAudioStart()" }
        refreshPlayerControl()
    }

    override fun onAudioStop(player: NooieMediaPlayer?) {
        YRLog.d { PLAY_STATE_TAG + "player.onAudioStop()" }
        refreshPlayerControl()
    }

    override fun onTalkingStart(player: NooieMediaPlayer?) {
        YRLog.d { PLAY_STATE_TAG + "player.onTalkingStart()" }
    }

    override fun onTalkingStop(player: NooieMediaPlayer?) {
        YRLog.d { PLAY_STATE_TAG + "player.onTalkingStop()" }
    }

    override fun onRecordStart(player: NooieMediaPlayer?, result: Boolean, file: String?) {
        YRLog.d { PLAY_STATE_TAG + "player.onRecordStart()" }
        if (result) {
            if (mSaveRcecordFile != null && file != null) {
                mSaveRcecordFile.add(file)
            }
        }
    }

    override fun onRecordStop(player: NooieMediaPlayer?, result: Boolean, file: String?) {
        YRLog.d { PLAY_STATE_TAG + "player.onRecordStop()" }
        mRecording = false
        if (result) {
            if (mSaveRcecordFile.contains(file)) {
                mSaveRcecordFile.remove(file)
            }
            saveAndShowThumbnail(player, true, file)
        }
    }

    /**
     * 更新时间
     */
    override fun onRecordTimer(player: NooieMediaPlayer?, duration: Int) {
        YRLog.d { PLAY_STATE_TAG + "player.onRecordTimer()" }
        mRecording = true
        binding.tvRecordTime.text = DateTimeUtil.getTimeHms(duration.toLong())
    }

    override fun onFps(player: NooieMediaPlayer?, fps: Int) {
        YRLog.d { PLAY_STATE_TAG + "player.onFps()" }
        binding.livePreviewTxt.text = String.format("%dfps", fps);
    }

    override fun onBitrate(player: NooieMediaPlayer?, bitrate: Double) {
        YRLog.d { PLAY_STATE_TAG + "player.onBitrate()" }
        binding.livePreviewTxt.text = String.format("%.0fKb/s", bitrate);
    }

    override fun onBufferingStart(player: NooieMediaPlayer?) {
        YRLog.d { PLAY_STATE_TAG + "player.onBufferingStart()" }

    }

    override fun onBufferingStop(player: NooieMediaPlayer?) {
        YRLog.d { PLAY_STATE_TAG + "player.onBufferingStop()" }
    }

    override fun onPlayFinish(player: NooieMediaPlayer?) {
        YRLog.d { PLAY_STATE_TAG + "player.onPlayFinish()" }
    }

    override fun onPlayOneFinish(player: NooieMediaPlayer?) {
        YRLog.d { PLAY_STATE_TAG + "player.onPlayOneFinish()" }
    }

    override fun onPlayFileBad(player: NooieMediaPlayer?) {
        YRLog.d { PLAY_STATE_TAG + "player.onPlayFileBad()" }
    }
    override fun onSnapShot(player: NooieMediaPlayer?, result: Boolean, path: String?) {
        YRLog.d { PLAY_STATE_TAG + "player.onSnapShot()--path=$path" }
        if (!path.isNullOrEmpty() && path.contains(FileUtil.NOOIE_PREVIEW_THUMBNAIL_PREFIX)) {
            // 加载视频自动截首屏
            if (result) {
                val newFile = FileUtil.mapPreviewThumbPath(path)
                FileUtil.renamePreviewThumb(path, newFile)
            }
        } else {
            // 手动点击截屏
            if (isDestroyed) {
                return
            }
            if (result) {
                saveAndShowThumbnail(player, false, path)
            }
        }
    }

    /**
     * 保存截图、视频并显示
     */
    private fun saveAndShowThumbnail(player: NooieMediaPlayer?, isVedio: Boolean, file: String?) {
        val mediaType = if (isVedio) MediaStoreUtil.MEDIA_TYPE_VIDEO_MP4 else  MediaStoreUtil.MEDIA_TYPE_IMAGE_JPEG
        PlayerFileUtils.updateFileInMediaStore( mUserAccount,file, mediaType )
        PlayerFileUtils.sendRefreshPicture(this, file)
        if(isSnapShotNeedTips){
            PlayerFileUtils.showVideoThumbnail(file, isVedio, player, binding.ivThumbnail,isLandscape,isSnapShotNeedTips)
        }
    }
    //    -------------------------OnPlayerListener播放回调-------end----------------------
    /**
     * 返回键，横屏返回竖屏效果，不直接退出页面
     */
    override fun onBackPressed() {
        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        } else {
            finish()
        }
    }

    private fun obtainUid() : String {
        return ""
    }
}