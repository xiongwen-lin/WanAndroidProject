package com.apemans.smartipcimpl.ui.ipcdevices

import android.view.View
import com.alibaba.android.arouter.facade.annotation.Route
import com.apemans.dmapi.ACTIVITY_PATH_PAIR_DEVICE
import com.dylanc.longan.addStatusBarHeightToMarginTop
import com.dylanc.longan.design.FragmentStateAdapter
import com.dylanc.longan.design.setupWithViewPager2
import com.apemans.smartipcimpl.databinding.DeviceFragmentIpcDevicesBinding
import com.apemans.smartipcimpl.ui.actioncameralist.ActionCameraListFragment
import com.apemans.yruibusiness.base.BaseFragment
import com.apemans.ipccontrolapi.define.IPC_SCHEME_TYPE_SELF_DEVELOP
import com.apemans.ipccontrolapi.services.IpcControlManagerService
import com.apemans.ipcchipproxy.define.IPC_SCHEME_KEY_P2P_PORT
import com.apemans.ipcchipproxy.define.IPC_SCHEME_KEY_P2P_URL
import com.apemans.ipcchipproxy.define.IPC_SCHEME_KEY_UID
import com.apemans.router.routerServices
import com.apemans.router.startRouterActivity
import com.apemans.smartipcapi.services.path.FRAGMENT_PATH_SMART_IPC_DEVICE_LIST

/**
 * @author Dylan Cai
 */
@Route(path = FRAGMENT_PATH_SMART_IPC_DEVICE_LIST)
class IPCDevicesFragment : com.apemans.yruibusiness.base.BaseFragment<DeviceFragmentIpcDevicesBinding>() {

    private val tabTitles = listOf("Video camera", "Router")
    private val ipcControlService by routerServices<IpcControlManagerService>()

    override fun onViewCreated(root: View) {
        initP2P()
        with(binding) {
            tvTitle.addStatusBarHeightToMarginTop()
            viewPager2.adapter = FragmentStateAdapter(ActionCameraListFragment(), ActionCameraListFragment())
            tabLayout.setupWithViewPager2(viewPager2) { tab, i ->
                tab.text = tabTitles[i]
            }
            btnAdd.setOnClickListener {
                //startActivity<AddDeviceActivity>()
                startRouterActivity(ACTIVITY_PATH_PAIR_DEVICE)
            }
        }
    }

    private fun initP2P() {
        val param = mutableMapOf<String, Any>()
        param.put(IPC_SCHEME_KEY_P2P_URL, "52.83.89.23")
        param.put(IPC_SCHEME_KEY_P2P_PORT, 9100)
        param.put(IPC_SCHEME_KEY_UID, obtainUid())
        ipcControlService.createIpcSchemeCore(IPC_SCHEME_TYPE_SELF_DEVELOP)?.init(param)
    }

    private fun obtainUid() : String {
        return ""
    }
}