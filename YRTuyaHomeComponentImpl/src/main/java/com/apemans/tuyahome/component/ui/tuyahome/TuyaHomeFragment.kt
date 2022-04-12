package com.apemans.tuyahome.component.ui.tuyahome

import android.app.Activity
import android.content.Intent
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.PopupMenu
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.alibaba.android.arouter.core.LogisticsCenter
import com.alibaba.android.arouter.exception.NoRouteFoundException
import com.alibaba.android.arouter.facade.Postcard
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.facade.callback.InterceptorCallback
import com.alibaba.android.arouter.facade.callback.NavigationCallback
import com.alibaba.android.arouter.facade.enums.RouteType
import com.alibaba.android.arouter.facade.service.DegradeService
import com.alibaba.android.arouter.facade.service.InterceptorService
import com.alibaba.android.arouter.facade.service.PretreatmentService
import com.alibaba.android.arouter.launcher.ARouter
import com.alibaba.android.arouter.utils.Consts
import com.alibaba.android.arouter.utils.TextUtils
import com.apemans.base.middleservice.YRMiddleConst
import com.apemans.base.middleservice.YRMiddleServiceManager
import com.apemans.yruibusiness.base.BaseFragment
import com.apemans.yruibusiness.base.requestViewModels
import com.apemans.quickui.superdialog.SmartDialog
import com.apemans.router.routerFragments
import com.apemans.router.startRouterActivity
import com.apemans.tuya.module.api.*
import com.apemans.tuyahome.api.FRAGMENT_PATH_TUYA_HOME
import com.apemans.tuyahome.component.R
import com.apemans.tuyahome.component.databinding.TuyahomeFragmentTuyaHomeBinding
import com.apemans.tuyahome.component.ui.SharedViewModel
import com.apemans.tuyahome.component.widget.MenuItem
import com.apemans.tuyahome.component.widget.TuyaMorePopupWindow
import com.dylanc.longan.*
import com.dylanc.longan.design.FragmentStateAdapter
import com.dylanc.longan.design.setupWithViewPager2

/**
 * @author Dylan Cai
 */
@Route(path = FRAGMENT_PATH_TUYA_HOME)
class TuyaHomeFragment : BaseFragment<TuyahomeFragmentTuyaHomeBinding>() {

    private val viewModel: TuyaHomeViewModel by requestViewModels()
    private val sharedViewModel: SharedViewModel by applicationViewModels()
    private val tabTitles = listOf(R.string.device, R.string.group)
    private val devicesFragment by routerFragments(FRAGMENT_PATH_TUYA_DEVICES)
    private val groupsFragment by routerFragments(FRAGMENT_PATH_TUYA_GROUPS)

    override fun onViewCreated(root: View) {
        with(binding) {
            header.addStatusBarHeightToMarginTop()
            viewPager2.adapter = FragmentStateAdapter(devicesFragment, groupsFragment)
            tabLayout.setupWithViewPager2(viewPager2) { tab, i ->
                tab.setText(tabTitles[i])
            }
            viewModel.homeList.observe(viewLifecycleOwner) {
                if (it.isNotEmpty()) {
                    tvHome.text = it.first().name
                }
            }
            viewModel.loadData()
            btnMore.setOnClickListener {
                TuyaMorePopupWindow(
                    requireContext(), listOf(
                        MenuItem(R.drawable.device_home_add, getString(R.string.add_device)),
                        MenuItem(R.drawable.device_home_group, getString(R.string.create_group)),
                        MenuItem(R.drawable.device_home_family, getString(R.string.manage_home)),
                        MenuItem(
                            R.drawable.device_home_automatic,
                            getString(R.string.auto_control)
                        ),
                    )
                ) { popup, menuItem ->
                    when (menuItem.text) {
                        getString(R.string.add_device) -> {
                            startRouterActivity(ACTIVITY_PATH_ADD_TUYA_DEVICE)
                        }
                        getString(R.string.create_group) -> {
                            if (viewModel.deviceList.value.orEmpty().isNotEmpty()) {
//                                createGroup.launch(null)
                                ARouter.getInstance()
                                    .build(ACTIVITY_PATH_CREATE_GROUP)
                                    .navigation(this@TuyaHomeFragment, REQUEST_CODE_CREATE_GROUP)
//                                startRouterActivityForResult(
//                                    ACTIVITY_PATH_CREATE_GROUP,
//                                    REQUEST_CODE_CREATE_GROUP
//                                )
                            } else {
                                SmartDialog.build(parentFragmentManager)
                                    .setTitle(getString(R.string.create_group))
                                    .setContentText(getString(R.string.create_group_middle_tip))
                                    .setPositiveTextName(getString(R.string.confirm_upper))
                                    .setOnPositive {
                                        it.dismiss()
                                    }
                                    .show()
                            }
                        }
                        getString(R.string.manage_home) -> startRouterActivity(
                            ACTIVITY_PATH_HOME_LIST
                        )
                    }
                    popup.dismiss()
                }.showAsDropDown(it, -100.dp.toInt(), 10.dp.toInt())
            }
            btnMessage.setOnClickListener {
                startRouterActivity("/message/message_list")
            }
            btnRvType.setOnClickListener {
                sharedViewModel.isSingleRowType.value = !sharedViewModel.isSingleRowType.value!!
            }
            tvHome.setOnClickListener {
                val list = viewModel.homeList.value.orEmpty()
                PopupMenu(requireContext(), it)
                    .apply {
                        for (i in list.indices) {
                            menu.add(0, i, 0, list[i].name)
                        }
                        setOnMenuItemClickListener { item ->
                            tvHome.text = item.title
                            viewModel.selectHome(list[item.itemId].homeId)
                            true
                        }
                    }.show()
            }

            YRMiddleServiceManager.listening("yrcx://yrsmarthomekitservice/querydevice", viewLifecycleOwner, mapOf("type" to "tuya")) {
                mainThread {
                    if (it.code == YRMiddleConst.MIDDLE_SUCCESS) {
                        logDebug(it.data?.toString())
                    } else {
                        logDebug(it.errorMsg)
                    }
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_CREATE_GROUP && data != null) {
            val stringExtra = data.getStringExtra("product_id")
            val stringExtra1 = data.getStringArrayExtra("check_ids")
            startRouterActivity(
                ACTIVITY_PATH_SAVE_GROUP,
                "product_id" to stringExtra,
                "device_ids" to stringExtra1,
            )
        }
    }

//    private val createGroup = registerForActivityResult(CreateGroupContract()) {
//        if (it != null) {
//            SaveGroupActivity.start(it.productId, it.deviceIds)
//        }
//    }

    companion object {
        private const val REQUEST_CODE_CREATE_GROUP = 1
    }

    fun Postcard.navigation(fragment: Fragment, requestCode: Int, callback: NavigationCallback? = null): Any? {
        val pretreatmentService = ARouter.getInstance().navigation(PretreatmentService::class.java)
        if (null != pretreatmentService && !pretreatmentService.onPretreatment(context, this)) {
            // Pretreatment failed, navigation canceled.
            return null
        }

        try {
            LogisticsCenter.completion(this)
        } catch (ex: NoRouteFoundException) {
            ARouter.logger.warning(Consts.TAG, ex.message)

            if (ARouter.debuggable()) {
                // Show friendly tips for user.
                fragment.requireActivity().runOnUiThread {
                    Toast.makeText(
                        fragment.requireContext(), """There's no route matched!
 Path = [$path]
 Group = [$group]""", Toast.LENGTH_LONG
                    ).show()
                }
            }

            if (null != callback) {
                callback.onLost(this)
            } else {
                // No callback for this invoke, then we use the global degrade service.
                val degradeService = ARouter.getInstance().navigation(DegradeService::class.java)
                degradeService?.onLost(context, this)
            }
            return null
        }

        callback?.onFound(this)

        val interceptorService = ARouter.getInstance().build("/arouter/service/interceptor").navigation() as InterceptorService
        if (!isGreenChannel) {   // It must be run in async thread, maybe interceptor cost too mush time made ANR.
            interceptorService.doInterceptions(this, object : InterceptorCallback {
                /**
                 * Continue process
                 *
                 * @param postcard route meta
                 */
                override fun onContinue(postcard: Postcard) {
                    postcard._navigation(fragment, requestCode, callback)
                }

                /**
                 * Interrupt process, pipeline will be destory when this method called.
                 *
                 * @param exception Reson of interrupt.
                 */
                override fun onInterrupt(exception: Throwable) {
                    callback?.onInterrupt(this@navigation)
                    ARouter.logger.info(Consts.TAG, "Navigation failed, termination by interceptor : " + exception.message)
                }
            })
        } else {
            return _navigation(fragment, requestCode, callback)
        }
        return null
    }

    @Suppress("FunctionName", "DEPRECATION")
    private fun Postcard._navigation(fragment: Fragment, requestCode: Int, callback: NavigationCallback?): Any? {
        val currentContext = fragment.requireContext()
        when (type) {
            RouteType.ACTIVITY -> {
                // Build intent
                val intent = Intent(currentContext, destination)
                intent.putExtras(extras)

                // Set flags.
                val flags = flags
                if (0 != flags) {
                    intent.flags = flags
                }

                // Non activity, need FLAG_ACTIVITY_NEW_TASK
                if (currentContext !is Activity) {
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }

                // Set Actions
                val action = action
                if (!TextUtils.isEmpty(action)) {
                    intent.action = action
                }

                fragment.requireActivity().runOnUiThread {
                    if (requestCode >= 0) {  // Need start for result
                        if (currentContext is Activity) {
                            fragment.startActivityForResult(intent, requestCode, optionsBundle)
                        } else {
                            ARouter.logger.warning(Consts.TAG, "Must use [navigation(activity, ...)] to support [startActivityForResult]")
                        }
                    } else {
                        ActivityCompat.startActivity(currentContext, intent, optionsBundle)
                    }

                    if (-1 != enterAnim && -1 != exitAnim && currentContext is Activity) {    // Old version.
                        currentContext.overridePendingTransition(enterAnim, exitAnim)
                    }

                    callback?.onArrival(this)
                }
            }
            RouteType.PROVIDER -> return provider
            RouteType.BOARDCAST, RouteType.CONTENT_PROVIDER, RouteType.FRAGMENT -> {
                val fragmentMeta = destination
                try {
                    val instance = fragmentMeta.getConstructor().newInstance()
                    if (instance is android.app.Fragment) {
                        instance.arguments = extras
                    } else if (instance is Fragment) {
                        instance.arguments = extras
                    }
                    return instance
                } catch (ex: java.lang.Exception) {
                    ARouter.logger.error(Consts.TAG, "Fetch fragment instance error, " + TextUtils.formatStackTrace(ex.stackTrace))
                }
                return null
            }
            RouteType.METHOD, RouteType.SERVICE -> return null
            else -> return null
        }

        return null
    }

}