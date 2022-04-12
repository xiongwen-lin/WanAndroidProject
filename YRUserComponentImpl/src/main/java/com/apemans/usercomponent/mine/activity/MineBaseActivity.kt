package com.apemans.usercomponent.mine.activity

import android.os.Bundle
import androidx.viewbinding.ViewBinding
import com.apemans.base.middleservice.YRMiddleServiceManager
import com.dylanc.longan.finishAllActivities
import com.apemans.logger.YRLog
import com.apemans.quickui.superdialog.SmartDialog
import com.apemans.router.ACTIVITY_PATH_MAIN
import com.apemans.userapi.startRouterActivityCheckLogin
import com.apemans.usercomponent.R
import com.apemans.usercomponent.easypermissions.EasyPermissions

open class MineBaseActivity <VB : ViewBinding> : com.apemans.yruibusiness.base.BaseComponentActivity<VB>() {
    val mUserAccount : String = ""

    override fun onResume() {
        super.onResume()
    }

    private fun showForceLogoutDialog() {
        // okhttp中 Callback 是封装好的在子线程运行，Android是不允许在子线程中弹出 Toast 提示的，和不允许在子线程中进行 UI 操作一样
        // 所以如果需要弹框或toast需要切换到主线程执行
//        Looper.prepare()
        var hashMap = HashMap<String,Any?>()
        hashMap["logout"] = ""
        SmartDialog.build(supportFragmentManager)
            .setTitle("被迫下线")
            .setContentText("你的账号在其他设备登录")
            .setPositiveTextName(resources.getString(R.string.confirm))
            .setOnPositive {
                // 注销登录的一些信息
                YRMiddleServiceManager.request("yrcx://yrbusiness/setparamters",hashMap)
                startRouterActivityCheckLogin(ACTIVITY_PATH_MAIN) {
                    finishAllActivities()
                    //finishAllActivitiesExceptNewest()
                }
                it.dismiss()
                YRMiddleServiceManager.request("yrcx://yrplatformbridge/setparamters",hashMap)
            }
            .show()
//        Looper.loop()
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onViewCreated(savedInstanceState: Bundle?) {
    }

    open fun requestPermissions(perms: Array<String>) {
        if (EasyPermissions.hasPermissions(this, *perms)) {
            YRLog.e("-----------homeActivity 无权限  permissionsGranted")
            permissionsGranted()
        } else {
            YRLog.e("-----------homeActivity 无权限  系统弹窗EasyPermission")
            EasyPermissions.requestPermissions(this,
                resources.getString(R.string.permissions_required), 100, *perms)
        }
    }

    protected open fun permissionsGranted() {}

}