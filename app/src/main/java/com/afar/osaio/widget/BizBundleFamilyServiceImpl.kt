package com.afar.osaio.widget

import com.tuya.smart.commonbiz.bizbundle.family.api.AbsBizBundleFamilyService

class BizBundleFamilyServiceImpl : AbsBizBundleFamilyService() {
    private var mHomeId: Long = 0
    override fun getCurrentHomeId(): Long {
        return mHomeId
    }

    override fun setCurrentHomeId(homeId: Long) {
        mHomeId = homeId
    }

    override fun shiftCurrentFamily(l: Long, s: String) {
        mHomeId = l
    }
}