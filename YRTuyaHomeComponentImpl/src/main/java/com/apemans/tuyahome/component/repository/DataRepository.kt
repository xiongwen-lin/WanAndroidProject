package com.apemans.tuyahome.component.repository

import com.apemans.base.MMKVOwner
import com.apemans.base.mmkvBool

object DataRepository : MMKVOwner {
    var isFirstLaunch by mmkvBool(true)
}