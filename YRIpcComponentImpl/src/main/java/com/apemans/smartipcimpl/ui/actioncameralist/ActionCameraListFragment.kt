package com.apemans.smartipcimpl.ui.actioncameralist

import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.apemans.quickui.multitype.KotlinClassLinker
import com.apemans.quickui.multitype.MultiTypeAdapter
import com.apemans.quickui.multitype.observeItemsChanged
import com.apemans.base.utils.WifiObserver
import com.scwang.smart.refresh.header.ClassicsHeader
import com.apemans.yruibusiness.base.BaseComponentFragment
import com.apemans.smartipcapi.info.IpcDeviceInfo
import com.apemans.logger.YRLog
import com.apemans.smartipcimpl.bean.IpcDevice
import com.apemans.smartipcimpl.databinding.DeviceManagerLayoutIpcListBinding
import com.apemans.smartipcimpl.ui.actioncameralist.item.ActionCameraViewDelegate
import com.apemans.smartipcimpl.ui.actioncameralist.item.IpcDraggingViewDelegate
import com.apemans.smartipcimpl.ui.actioncameralist.item.IpcDvViewDelegate
import com.apemans.smartipcimpl.ui.actioncameralist.item.IpcNormalViewDelegate
import com.apemans.smartipcimpl.ui.ipcdevices.player.IpcPlayerActivity
import com.apemans.yruibusiness.utils.viewbinding.OnItemClickListener
import com.apemans.dmapi.status.DeviceDefine
import com.apemans.smartipcimpl.constants.*
import com.apemans.smartipcimpl.ui.ipcdevices.player.bean.*
import com.apemans.smartipcimpl.ui.ipcdevices.setting.GatewayDeviceInfoActivity
import com.apemans.smartipcimpl.ui.ipcdevices.setting.IpcSettingActivity
import com.dylanc.longan.startActivity
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

/**
 * @author Dylan Cai
 */
class ActionCameraListFragment: com.apemans.yruibusiness.base.BaseComponentFragment<DeviceManagerLayoutIpcListBinding>() {

    private val viewModel : ActionCameraListViewModel by viewModels()

    private val mIpcAdapter = MultiTypeAdapter(
        ActionCameraViewDelegate(ipcItemClickListener), IpcDraggingViewDelegate(), linker = ipcLinker
    )

    private val mNetSpotIpcAdapter = MultiTypeAdapter(ActionCameraViewDelegate(bleIpcItemClickListener))
    private val mHubIpcAdapter = MultiTypeAdapter(IpcNormalViewDelegate(ipcNormalItemClickListener))

    private val mIpcDvAdapter = MultiTypeAdapter(IpcDvViewDelegate(ipcDvItemClickListener))

    override fun onViewCreated(root: View) {
        with(binding) {
            /*
            rflDeviceManagerIpcList
                .setEnableLoadMore(true)
                .setEnableAutoLoadMore(true)

             */
            var refreshHeader = rflDeviceManagerIpcList.refreshHeader as? ClassicsHeader
            refreshHeader?.apply {
                setEnableLastTime(false)
            }
            /*
            var refreshFooter = rflDeviceManagerIpcList.refreshFooter as? ClassicsFooter
            refreshFooter?.apply {
            }

             */
            rvIpc.adapter = mIpcAdapter

            rvNetSpotIpc.adapter = mNetSpotIpcAdapter

            rvHubIpc.adapter = mHubIpcAdapter

            rvIpcDv.adapter = mIpcDvAdapter

            binding.rflDeviceManagerIpcList.setOnRefreshListener {
                startRefresh()
                lifecycleScope.launch {
                    delay(3000)
                    it.finishRefresh()
                }
            }

            displayDeviceListView(false)
            viewModel.ipcDvModeEnable.observe(viewLifecycleOwner) { enable ->
                displayDeviceListView(enable)
            }

            viewModel.dvIpcDeviceList.value = listOf()
            mIpcDvAdapter.observeItemsChanged(viewLifecycleOwner, viewModel.dvIpcDeviceList) { oldItem,newItem->
                oldItem.device.deviceInfo.device_id == newItem.device.deviceInfo.device_id
            }
        }

        lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                mIpcAdapter.observeItemsChanged(scope = this, items = viewModel.ipcDeviceList){ oldItem,newItem->
                    oldItem.device.deviceInfo.device_id == newItem.device.deviceInfo.device_id
                }

                mNetSpotIpcAdapter.observeItemsChanged(scope = this, items = viewModel.netSpotIpcDeviceList){ oldItem,newItem->
                    oldItem.device.deviceInfo.device_id == newItem.device.deviceInfo.device_id
                }

                mHubIpcAdapter.observeItemsChanged(scope = this, items = viewModel.hubIpcDeviceList){ oldItem,newItem->
                    oldItem.device.deviceInfo.device_id == newItem.device.deviceInfo.device_id
                }

                launch {
                    viewModel.ipcDeviceList.collect {
                        binding.tvDeviceManagerIpcListTitle.visibility = if (it.isNullOrEmpty()) View.GONE else View.VISIBLE
                    }
                }

                launch {
                    viewModel.netSpotIpcDeviceList.collect {
                        binding.tvDeviceManagerNetSpotIpcListTitle.visibility = if (it.isNullOrEmpty()) View.GONE else View.VISIBLE
                    }
                }

                launch {
                    viewModel.hubIpcDeviceList.collect {
                        binding.tvDeviceManagerHubIpcListTitle.visibility = if (it.isNullOrEmpty()) View.GONE else View.VISIBLE
                    }
                }
            }
        }

        setupWifiObserver()

//        val params = mutableMapOf<String, Any>()
//        params["uid"] = NetConfigure.uid
//        YRMiddleServiceManager.listening("yrcx://yripccomponentdevice/queryIpcDevice", lifecycle, params) {
//            var response = it
//            YRLog.d { "queryIpcDevice" }
//        }
    }

    override fun onResume() {
        super.onResume()
        startRefresh()
        viewModel.loadNetSpotDevice()
    }

    private fun startRefresh() {
        viewModel.loadIpcDeviceList(obtainUid())
    }

    private fun displayDeviceListView(ipcDvModeEnable: Boolean) {
        with(binding) {
            val p2pIpcListVisibility = if (ipcDvModeEnable) View.GONE else View.VISIBLE
            val dvIpcListVisibility = if (ipcDvModeEnable) View.VISIBLE else View.GONE
            rflDeviceManagerIpcList.visibility = p2pIpcListVisibility
            rvIpcDv.visibility = dvIpcListVisibility
        }
    }

    private val WIFI_STATE_INIT = 0
    private val WIFI_STATE_AVAILABLE = 1
    private val WIFI_STATE_LOST = 2
    private var lastWifiState = WIFI_STATE_INIT
    private fun setupWifiObserver() {
        WifiObserver(requireContext(), viewLifecycleOwner, object :WifiObserver.Event {
            override fun onAvailable() {
                YRLog.d { "-->> ActionCameraListFragment network change onAvailable" }
                onNetworkStateChange(WIFI_STATE_AVAILABLE)
            }

            override fun onLost() {
                YRLog.d { "-->> ActionCameraListFragment network change onLost" }
                onNetworkStateChange(WIFI_STATE_LOST)
            }

            override fun wifiRSSILevelChange(rssiLevel: Int) {
                YRLog.d { "-->> ActionCameraListFragment network change wifiRSSILevelChange" }
            }
        })
    }

    private fun checkWifiChange(state: Int) : Boolean {
        return lastWifiState != WIFI_STATE_INIT && state != lastWifiState
    }

    private fun onNetworkStateChange(state: Int) {
        YRLog.d { "-->> ActionCameraListFragment onNetworkStateChange state $state lastWifiState $lastWifiState isChange ${checkWifiChange(state)}" }
        if (checkWifiChange(state)) {
            viewModel.checkNetSpotDeviceOnNetworkChange()
        }
        lastWifiState = state
    }

    private val ipcLinker get() = KotlinClassLinker<IpcDevice>{ _, data->
        when (data.itemType) {
            IpcDevice.ITEM_TYPE_IPC_DRAGGING -> IpcDraggingViewDelegate()::class
            else -> ActionCameraViewDelegate::class
        }
    }

    private val ipcItemClickListener get() = com.apemans.yruibusiness.utils.viewbinding.OnItemClickListener<IpcDevice> { item, position ->


        (item?.device?.deviceInfo as? IpcDeviceInfo)?.let {
            if (!it.device_id.isNullOrEmpty()) {
                if (it.online != DeviceDefine.ONLINE) {
                    startIpcPlayerActivityOffline(it)
                    return@let
                }

                startActivity<IpcPlayerActivity>(
                    Pair(INTENT_KEY_DEVICE_ID, it.device_id),//设备信息
                    Pair(INTENT_KEY_DATA_TYPE, NOOIE_PLAYBACK_TYPE_LIVE) //播放类型，直播、卡回放、云回放
//                    Pair(INTENT_KEY_TIME_STAMP, 1642032000738), //回放时间
                )
            }
        }

//        viewModel.testNetSpot() {
//            var netspotInfo = viewModel.getNetSpotDeviceInfo()
//            var deviceId = netspotInfo?.deviceId ?: ""
////            startActivity<IpcSettingActivity>(
////                Pair(INTENT_KEY_DEVICE_ID, deviceId),
////                Pair(INTENT_KEY_MODEL, "HC320"),
////                Pair(INTENT_KEY_BIND_TYPE, DeviceDefine.BIND_TYPE_OWNER),
////            )
//                    startActivity<IpcPlayerActivity>(
//                    Pair(INTENT_KEY_DEVICE_ID, netspotInfo?.deviceId),//设备信息
//                    Pair(INTENT_KEY_DATA_TYPE, NOOIE_PLAYBACK_TYPE_LIVE) ,//播放类型，直播、卡回放、云回放
//                    Pair(INTENT_KEY_MODEL, "HC320")
//                )
//
//        }

        //viewModel.testYRIpcMiddleService()
    }

    private fun startIpcPlayerActivityOffline(ipcDeviceInfo :IpcDeviceInfo){
        var  playbackType = NOOIE_PLAYBACK_TYPE_SD
        val mIsOwner = DeviceDefine.BIND_TYPE_OWNER == ipcDeviceInfo.bindType
        viewModel.loadPackageInfo(ipcDeviceInfo.device_id, mIsOwner) {
            if (it?.status == PACKAGE_INFO_STATUS_DEFAULT || it?.status == PACKAGE_INFO_STATUS_ON) { //支持云存储
                playbackType = NOOIE_PLAYBACK_TYPE_CLOUD
            }
            startActivity<IpcPlayerActivity>(
                Pair(INTENT_KEY_DEVICE_ID, ipcDeviceInfo.device_id),//设备信息
                Pair(INTENT_KEY_DATA_TYPE, playbackType) )//播放类型，直播、卡回放、云回放
          }
    }

    private val bleIpcItemClickListener get() = com.apemans.yruibusiness.utils.viewbinding.OnItemClickListener<IpcDevice> { item, position ->
    }

    private val ipcNormalItemClickListener get() = com.apemans.yruibusiness.utils.viewbinding.OnItemClickListener<IpcDevice> { item, position ->
        (item?.device?.deviceInfo as? IpcDeviceInfo)?.let {
            if (!it.device_id.isNullOrEmpty()) {
                startActivity<GatewayDeviceInfoActivity>(
                    Pair(INTENT_KEY_DEVICE_ID, it.device_id),
                    Pair(INTENT_KEY_DEVICE_ONLINE_STATE, it.online == DeviceDefine.ONLINE)
                )
            }
        }
    }

    private val ipcDvItemClickListener get() = com.apemans.yruibusiness.utils.viewbinding.OnItemClickListener<IpcDevice> { item, position ->
        (item?.device?.deviceInfo as? IpcDeviceInfo)?.let {
            if (!it.device_id.isNullOrEmpty()) {
                startActivity<IpcSettingActivity>(
                    Pair(INTENT_KEY_DEVICE_ID, it.device_id),
                    Pair(INTENT_KEY_MODEL, "HC320"),
                    Pair(INTENT_KEY_BIND_TYPE, DeviceDefine.BIND_TYPE_OWNER),
                    Pair(INTENT_KEY_DEVICE_ONLINE_STATE, true)
                )
            }
        }
    }

    private fun obtainUid() : String {
        return ""
    }
}