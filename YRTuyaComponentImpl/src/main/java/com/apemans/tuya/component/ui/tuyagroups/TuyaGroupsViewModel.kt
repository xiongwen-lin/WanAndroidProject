package com.apemans.tuya.component.ui.tuyagroups

import androidx.lifecycle.ViewModel
import com.apemans.router.routerServices
import com.apemans.tuya.module.api.TuyaService

/**
 * @author Dylan Cai
 */
class TuyaGroupsViewModel : ViewModel() {
    private val tuyaService: TuyaService by routerServices()
    val deviceList = tuyaService.groupList
}