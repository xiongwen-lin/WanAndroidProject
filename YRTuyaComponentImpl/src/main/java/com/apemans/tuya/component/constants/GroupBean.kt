package com.apemans.tuya.component.constants

import android.text.TextUtils
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.apemans.tuya.component.ui.tuyadevices.items.BaseDeviceViewDelegate
import com.tuya.smart.sdk.bean.DeviceBean
import com.tuya.smart.sdk.bean.GroupBean

val GroupBean.deviceType: String
    get() = if (!TextUtils.isEmpty(productId) &&
        (productId == SMART_PLUG_PRODUCTID || productId == SMART_PLUG_PRODUCTID_NEW || productId == SMART_PLUG_PRODUCTID_OLD || productId == SMART_PLUG_US_PRODUCTID_TWO || productId == SMART_PLUG_US_PRODUCTID_THREE || productId == SMART_PLUG_EU_PRODUCTID || productId == SMART_PLUG_UK_PRODUCTID || productId == SMART_PLUG_UK_PRODUCTID_TWO || productId == SMART_PLUG_UK_PRODUCTID_THREE || productId == SMART_PLUG_EU_PRODUCTID_TWO || productId == SMART_PLUG_EU_PRODUCTID_THREE || productId == SMART_PLUG_JP_PRODUCTID_ONE || productId == SMART_PLUG_EU_PRODUCTID_EIGHT)
    ) {
        GroupType.PLUG
    } else {
        GroupType.LAMP
    }

val DeviceBean.isOpen: Boolean
    get() {
        val schemaStr = productBean.schemaInfo.schema
        if (schemaStr.isNullOrEmpty() || dps.isNullOrEmpty()) {
            return false
        }
        val schemas = Gson().fromJson<List<BaseDeviceViewDelegate.Schema>>(schemaStr, object : TypeToken<List<BaseDeviceViewDelegate.Schema>>() {}.type)
        for (schema in schemas) {
            if (schema.code.contains("switch") && schema.code != "switch_all" && schema.code != "switch_overcharge") {
                if (dps.containsKey(schema.id.toString())) {
                    try {
                        return dps[schema.id.toString()] as Boolean
                    } catch (e: ClassCastException) {
                        e.printStackTrace()
                    }
                }
            }
        }
        return false
    }