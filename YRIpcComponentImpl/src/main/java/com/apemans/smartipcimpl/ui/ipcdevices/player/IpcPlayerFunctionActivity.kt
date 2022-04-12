package com.apemans.smartipcimpl.ui.ipcdevices.player

import android.Manifest
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.text.TextUtils
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ImageView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.apemans.base.utils.DisplayHelper
import com.apemans.dmapi.status.DeviceDefine
import com.apemans.smartipcapi.webapi.PresetPointConfigure

import com.apemans.logger.YRLog
import com.apemans.quickui.alerter.alertInfo
import com.apemans.quickui.superdialog.SmartDialog
import com.apemans.smartipcimpl.R
import com.apemans.smartipcimpl.constants.*
import com.apemans.smartipcimpl.ui.ipcdevices.player.adapter.DateTimeAdapter
import com.apemans.smartipcimpl.ui.ipcdevices.player.adapter.DirectionAdapter
import com.apemans.smartipcimpl.ui.ipcdevices.player.adapter.FunctionAdapter
import com.apemans.smartipcimpl.ui.ipcdevices.player.adapter.FunctionAdapterLands
import com.apemans.smartipcimpl.ui.ipcdevices.player.bean.*
import com.apemans.smartipcimpl.ui.ipcdevices.player.model.IpcPlayFunctionModel
import com.apemans.smartipcimpl.ui.ipcdevices.player.util.MyItemTouchHelperCallBack
import com.apemans.smartipcimpl.ui.ipcdevices.player.util.PlayFunctionHelper
import com.apemans.smartipcimpl.ui.ipcdevices.setting.IpcSettingActivity
import com.apemans.smartipcimpl.ui.ipcdevices.setting.IpcStorageActivity
import com.dylanc.longan.isLandscape
import com.dylanc.longan.lifecycleOwner
import com.dylanc.longan.requestPermissionLauncher
import com.dylanc.longan.startActivity
import com.nooie.common.utils.file.FileUtil
import com.nooie.sdk.media.NooieMediaPlayer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File


/**
 * @Author:dongbeihu
 * @Description: IpcPlayActivity实现 控制业务，主要是功能:
 *  功能事件functionMenuEvent()、侦测方位管理showDirection()
 * @Date: 2021/12/1-14:26
 */
abstract class IpcPlayerFunctionActivity : IpcPlayerBaseActivity() {
    /**
     * 当前方位数据对象
     */
    var directionList = arrayListOf<PresetPointConfigure>()

    var directionAdapter: DirectionAdapter? = null

    /**
     * 功能区：拍照、录像、方位等
     */
    var functionAdapter: FunctionAdapter? = null

    /**
     * 横屏功能区：拍照、录像等，不包含方位和警报
     */
    var functionAdapterLands: FunctionAdapterLands? = null

    var dateTimeAdapter: DateTimeAdapter? = null

    private lateinit var playFunctionViewModel: IpcPlayFunctionModel
    private lateinit var positionView: ImageView
    private var type = 0
    private var isLands = false

    private  var directionPosition  =0

    fun initPlayFunction() {
        playFunctionViewModel = registerViewModule(IpcPlayFunctionModel::class.java)
        initView()
        initFunctionData()
    }

    fun initView() {
        //竖屏功能区初始化
        functionAdapter = FunctionAdapter()
        val layoutManager = GridLayoutManager(this, 3)
        layoutManager.orientation = RecyclerView.VERTICAL
        binding.recyclerViewFunction.layoutManager = layoutManager
        binding.recyclerViewFunction.adapter = functionAdapter
        functionAdapter?.setListener(object : FunctionAdapter.OnItemClickListener {
            override fun onItemClick(positionView: ImageView, data: FunctionMenuItem) {
                if (data != null) {
                    functionMenuEvent(positionView, data, false)
                }
            }
        })

        //横屏功能区初始化
        functionAdapterLands = FunctionAdapterLands()
        binding.rcyFunctionLand.layoutManager = LinearLayoutManager(this)
        binding.rcyFunctionLand.adapter = functionAdapterLands
        functionAdapterLands?.setListener(object : FunctionAdapterLands.OnItemClickListener {
            override fun onItemClick(positionView: ImageView, data: FunctionMenuItem) {
                if (data != null) {
                    functionMenuEvent(positionView, data, true)
                }
            }
        })

        //日期功能区初始化
        dateTimeAdapter = DateTimeAdapter()
        val linearLayoutManager = LinearLayoutManager(this)
        linearLayoutManager.orientation = LinearLayoutManager.HORIZONTAL
        binding.rcyTime.layoutManager = linearLayoutManager
        binding.rcyTime.adapter = dateTimeAdapter
    }

    //获取用户可操作功能
    fun initFunctionData() {
        playFunctionViewModel.getFunctionMenuList(mDeviceId, mModel, mConnectionMode, mDeviceType,mIsOwner) {
            functionAdapter?.setDataList(it)
            functionAdapterLands?.setDataList(it)
        }

        playFunctionViewModel.loadDetectionPlanInfo(mDeviceId!!) {
            directionList = it as ArrayList<PresetPointConfigure>
        }
    }

    /**
     * 刷新功能控制区域，是否支持展示使用拍照等功能
     * */

    override fun refreshPlayerControl() {
        if (player != null) {
            functionAdapter?.setState(player!!.isPlayingng, player!!.isWaveout,player!!.isTalking)
            functionAdapterLands?.setState(player!!.isPlayingng, player!!.isWaveout,player!!.isTalking)
        }
    }

    /**
     * 重置播放器，退出页面、切换直播回放的时候
     */
    fun resetDefault() {
        YRLog.d("-->> IpcPlayerLiveActivity resetDefault 1")
        if (player != null) {
//            player?.setPlayerListener(null)
            player?.stop()
            refreshPlayerControl()
        }
        playFunctionViewModel.setFlashLightMode(mDeviceId!!, false)

//        stopLpDevicePlaybackTask()
//        stopLpDeviceShortLinkTask()
        PlayFunctionHelper.changePhoneVolume(this, false)
        binding.containerLpDeviceController.visibility = View.GONE
        YRLog.d("-->> IpcPlayerLiveActivity resetDefault 2")
//        player?.setPlayerListener(this)
    }
    /**
     * 操作栏点击事件处理
     */
    private fun functionMenuEvent(positionView: ImageView,  data: FunctionMenuItem,  isLands: Boolean  ) {
        if (mDeviceId == null || player == null || isShowGuideOrPopView) {
            return
        }
        if (player?.isPlayingng == false) {
            alertInfo(getString(R.string.play_close_when_loading_tip))
            return
        }
        when (data.functionType) {
            IPC_FUNCTION_TYPE_TAKE_PHOTO -> {
                goCheckPermissionFun( Manifest.permission.READ_EXTERNAL_STORAGE, positionView,data.functionType, isLands )
            }
            IPC_FUNCTION_TYPE_RECORD -> {
                goCheckPermissionFun(  Manifest.permission.READ_EXTERNAL_STORAGE,positionView, data.functionType, isLands )
            }
            IPC_FUNCTION_TYPE_SPEAK -> {
                goCheckPermissionFun(Manifest.permission.RECORD_AUDIO, positionView,data.functionType,isLands )
            }
            IPC_FUNCTION_TYPE_SOUND -> {
                playFunctionViewModel.sound(player!!, mDeviceId, positionView, isLands)
            }
            IPC_FUNCTION_TYPE_ALARM -> {
                playFunctionViewModel.alarm(mDeviceId!!, positionView, isLands)
            }
            IPC_FUNCTION_TYPE_PHOTO -> {
                goCheckPermissionFun(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    positionView,
                    data.functionType,
                    isLands
                )

            }
            IPC_FUNCTION_TYPE_FLASH_LIGHT -> {
                playFunctionViewModel.flashLight(mDeviceId!!, positionView, isLands)
            }
            IPC_FUNCTION_TYPE_DIRECTION -> {  //方位帧操作
                showDirection()
            }
            else -> {
                alertInfo(getString(data.text))
            }
        }
    }


    fun initClickListener() {
        initPlayClickListener()
        binding.icBack.setOnClickListener {
            finish()
        }
        binding.ivSetting.setOnClickListener {
            if (isShowGuideOrPopView){return@setOnClickListener}
            startActivity<IpcSettingActivity>(
                Pair(INTENT_KEY_DEVICE_ID, mDeviceId),
                Pair(INTENT_KEY_MODEL, mModel),
                Pair(INTENT_KEY_BIND_TYPE, bindtype)
            )
        }
        binding.layBottomCloud.setOnClickListener {
            startActivity<IpcStorageActivity>()
        }

    }

    /**
     * 检测当前设备升级状态
     */
    fun queryDeviceUpdateStatus() {
        playFunctionViewModel.checkUpgradeState(mDeviceId!!) {
            if (it == DeviceDefine.UPGRADE_TYPE_INSTALLING) {
                binding.UpgradingState.visibility = View.VISIBLE
                binding.livePreviewPlayer.alpha = 0.1f
                binding.layMenu.alpha = 0.1f
                binding.viewActionFullscreen.visibility = View.GONE
            } else {
                binding.UpgradingState.visibility = View.GONE
                binding.viewActionFullscreen.visibility = View.VISIBLE
                binding.livePreviewPlayer.alpha = 1f
                binding.layMenu.alpha = 1f
            }
        }
    }

    /**
     * 方位View控制：添加、编辑、删除
     */
    private fun showDirection() {
        binding.recyclerViewFunction.visibility = View.GONE
        binding.layBottomCloud.visibility = View.GONE
        binding.layTplDirection.layDirection.visibility = View.VISIBLE

        directionAdapter = DirectionAdapter()
        val layoutManager = GridLayoutManager(this, 2)
        layoutManager.orientation = RecyclerView.VERTICAL
        binding.layTplDirection.recyclerViewDirection.layoutManager = layoutManager
        val itemTouchHelper = ItemTouchHelper(MyItemTouchHelperCallBack())
        itemTouchHelper.attachToRecyclerView(binding.layTplDirection.recyclerViewDirection)

        binding.layTplDirection.recyclerViewDirection.adapter = directionAdapter
        directionAdapter?.setDataList(directionList, mDeviceId!!)
        directionAdapter?.setListener(object : DirectionAdapter.OnItemClickListener {
            override fun onItemClick(position: Int, data: PresetPointConfigure?) {
                if (data == null) {
                    startScreenShot(position)
                } else {
                    showDirectionDialog(position, data,"")
                }
            }

            override fun onItemRemove(position: Int, data: PresetPointConfigure?) {
                if (directionList.contains(data)) {
                    showDeletePresetPointDialog(data)
                }
            }
        })

        //方位---添加
        binding.layTplDirection.layAddDirection.setOnClickListener { startScreenShot(0) }
        //方位---编辑
        binding.layTplDirection.icEdit.setOnClickListener {
            setDirectionEditMode(true)
        }
        //方位---取消编辑
        binding.layTplDirection.tvCancel.setOnClickListener {
            setDirectionEditMode(false)
        }

        //方位---关闭
        binding.layTplDirection.icNavCancelOff.setOnClickListener {
            binding.recyclerViewFunction.visibility = View.VISIBLE
            binding.layBottomCloud.visibility = View.VISIBLE
            binding.layTplDirection.layDirection.visibility = View.GONE
            setDirectionEditMode(false)
        }
        freshDirectionRcView()
    }

    private fun  setDirectionEditMode(isEdit :Boolean){
        binding.layTplDirection.tvCancel.visibility = if (isEdit)View.VISIBLE else View.GONE
        binding.layTplDirection.icEdit.visibility = if (isEdit)View.GONE else View.VISIBLE
        directionAdapter?.setEdit(isEdit)
    }

    /**
     * 添加图标不一样，0个是大图
     */
    private fun freshDirectionRcView() {
        if (directionList.isEmpty()) {
            binding.layTplDirection.recyclerViewDirection.visibility = View.GONE
            binding.layTplDirection.layAddDirection.visibility = View.VISIBLE
        } else {
            binding.layTplDirection.recyclerViewDirection.visibility = View.VISIBLE
            directionAdapter?.setDataList(directionList, mDeviceId!!)
            binding.layTplDirection.layAddDirection.visibility = View.GONE
        }
    }

    /**
     * 更新服务器方位配置，将本地与远程同步
     */
    private fun updateDeviceUploadConfigure() {
        playFunctionViewModel.updateDeviceUploadConfigure(mDeviceId!!, directionList) {
            if (!it) {
                alertInfo(getString(R.string.get_fail))
            } else {
                directionAdapter?.setDataList(directionList, mDeviceId!!)
            }
        }
    }




    /**
     * @param position 列表位置，0起步
     */
    fun startScreenShot(position: Int) {
        directionPosition = position
       val  mCurrentScreenShotPath = FileUtil.getTempPresetPointThumbnail(this, mUserAccount, mDeviceId, position + 1)
        if (TextUtils.isEmpty(mCurrentScreenShotPath)) {
            return
        }
        isSnapShotNeedTips = false
        player!!.snapShot(mCurrentScreenShotPath)
    }


    override fun onSnapShot(player: NooieMediaPlayer?, result: Boolean, path: String?) {
        super.onSnapShot(player, result, path)
        if ( !isSnapShotNeedTips  && path?.contains(FileUtil.TEMP_PRESET_POINT_PREFIX) == true){
            showDirectionDialog(directionPosition, null,path)
            isSnapShotNeedTips = true
        }
    }

    /**
     * @param position 列表位置，0起步
      * 方向对话输入框
     * 注意：ImageUrl是本地地址，而且更新图片时候，图片名称一样
     */
    private fun showDirectionDialog(position: Int, data: PresetPointConfigure?,  mCurrentScreenShotPath :String ) {
        var hint = data?.name ?: getString(R.string.tcp_preview_location) + (position + 1)

        var mCurrentScreenShot = if (data != null) {
            FileUtil.getPresetPointThumbnail(this, mUserAccount, mDeviceId, position + 1)
        }else{
            mCurrentScreenShotPath
        }
        val file = File(mCurrentScreenShot)
        if (!file.exists()){
            mCurrentScreenShot = ""
        }

        SmartDialog.build(supportFragmentManager, lifecycleOwner)
            .setTitle(getString(R.string.tcp_preview_location_name))
            .setHeadImageUrl(mCurrentScreenShot)
            .setHeadImage(R.drawable.device_default_preview)
            .showEditBox(
                show = true,
                showKeyboard = true,
                hint, hint
            )
            .setPositiveTextName(getString(R.string.confirm_upper))
            .setNegativeTextName(getString(R.string.cancel))
            .setOnPositive {
                val text = it.getEditBox().getEditText().text.toString()
                if (text.isNullOrEmpty() || text.length > 31) {
                    it.getEditBox()
                        .showError(getString(R.string.tcp_preview_dicrection_edit_error), true)
                    return@setOnPositive
                }

                if (data == null) {  //添加方位,注意已删除过的position
                    val correctPosition: Int = PlayFunctionHelper.getCorrectPresetPointPosition(
                        position+1,
                        directionList
                    )

                    val newFile = FileUtil.getPresetPointThumbnail(this, mUserAccount, mDeviceId, correctPosition)
                    FileUtil.renamePreviewThumb(mCurrentScreenShot, newFile) //重命名
                    var presetPointConfigure = PresetPointConfigure()
                    presetPointConfigure.name = text
                    presetPointConfigure.position = correctPosition
                    directionList.add(presetPointConfigure)
                    updateDeviceUploadConfigure()
                } else {  //更新方位名称
                    data.name = text
                    directionList[position] = data
                    updateDeviceUploadConfigure()
                }
                it.dismiss()
                freshDirectionRcView()

            }
            .addEditBoxInputChangeListener { smartEditBox, textChanged ->
                if (textChanged == "") {
                    smartEditBox.showError(
                        getString(R.string.tcp_preview_dicrection_edit_error),
                        true
                    )
                }
            }
            .setOnNegative {
                it.dismiss()
            }
            .show()
    }


    /**
     * 确认删除，本地、缓存、服务器都删除
     */
    private fun showDeletePresetPointDialog(presetPointConfigure: PresetPointConfigure?) {
        if (presetPointConfigure == null) {
            return;
        }
        SmartDialog.build(supportFragmentManager, lifecycleOwner)
            .setTitle(getString(R.string.preset_point_delete_tip))
            .setPositiveTextName(getString(R.string.confirm_upper))
            .setNegativeTextName(getString(R.string.cancel))
            .setOnPositive {

                deleteFileAsyncLocalList(presetPointConfigure)
                updateDeviceUploadConfigure()
                it.dismiss()
                freshDirectionRcView()
            }
            .setOnNegative {
                it.dismiss()
            }
            .show()
    }

    /**
     * 删除文件并重命名
     * 注意：需要位置和文件对应，重命名的情况
     *
     */
    private fun deleteFileAsyncLocalList(presetPointConfigure: PresetPointConfigure){
        FileUtil.deleteFile(FileUtil.getPresetPointThumbnail( this,
            mUserAccount, mDeviceId,presetPointConfigure.position
        ) )
        directionList.remove(presetPointConfigure)
    }



    /**
     * 获取某项权限后，再调用viewmodel的功能接口
     */
    private fun goCheckPermissionFun(  permission: String,  positionView: ImageView,  type: Int,  isLands: Boolean ) {
        this.positionView = positionView
        this.type = type
        this.isLands = isLands
        requestPermissionLauncher.launch(permission)
    }

    private val requestPermissionLauncher = requestPermissionLauncher(
        onGranted = {
            if (type == IPC_FUNCTION_TYPE_RECORD) {
                playFunctionViewModel.record(player, mDeviceId, positionView, mRecording, isLands)
                binding.tvRecordTime.visibility = if (mRecording) View.GONE else View.VISIBLE
            } else if (type == IPC_FUNCTION_TYPE_TAKE_PHOTO) {
                playFunctionViewModel.takePhoto(player, mDeviceId, positionView, isLands)
            } else if (type == IPC_FUNCTION_TYPE_SPEAK) {
                playFunctionViewModel.talk(player!!, mDeviceId, positionView, isLands)
            } else  if (type == IPC_FUNCTION_TYPE_PHOTO){
                playFunctionViewModel.openPhoto(mDeviceId!!)
            }
        },
        onDenied = {
              YRLog.e("用户拒绝权限--")
        },
        onShowRequestRationale = {

        }
    )
}
