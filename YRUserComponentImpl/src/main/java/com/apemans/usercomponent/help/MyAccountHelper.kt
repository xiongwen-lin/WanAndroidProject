package com.apemans.usercomponent.help

import android.text.TextUtils
import com.apemans.logger.YRLog
import com.apemans.usercomponent.baseinfo.utils.CollectionUtil
import com.apemans.usercomponent.user.util.ConstantValue
import java.lang.Exception
import java.lang.StringBuilder
import java.util.ArrayList
import java.util.HashMap

object MyAccountHelper {

    private var mAppCountBrands: HashMap<String, String> = HashMap()

    fun checkAppCountSelf(brandList: List<String?>): Boolean {
        return CollectionUtil.isEmpty(brandList) || brandList.contains(ConstantValue.APP_ACCOUNT_BRAND_CURRENT)
    }

    fun convertFromBrandList(brandList: MutableList<String?>): String? {
        if (CollectionUtil.isEmpty(brandList)) {
            return ""
        }
        try {
            val brandNameList: MutableList<String> = ArrayList()
            val iterator = brandList.iterator()
            while (iterator.hasNext()) {
                val brand = iterator.next()
                val brandName: String? = convertAppBrandName(brand)
                if (TextUtils.isEmpty(brand) || ConstantValue.APP_ACCOUNT_BRAND_CURRENT == brand) {
                    iterator.remove()
                } else if (!TextUtils.isEmpty(brandName)) {
                    if (brandName != null) {
                        brandNameList.add(brandName)
                    }
                }
            }
            if (CollectionUtil.isEmpty(brandNameList)) {
                return ""
            }
            if (CollectionUtil.size<String>(brandNameList) == 1) {
                return brandNameList[0]
            }
            val brandSb = StringBuilder()
            for (i in 0 until CollectionUtil.size<String>(
                brandNameList
            )) {
                if (i == i - 1) {
                    brandSb.append(brandNameList[i])
                } else {
                    brandSb.append(brandNameList[i]).append("/")
                }
            }
            return brandSb.toString()
        } catch (e: Exception) {
            YRLog.e("", e.toString())
        }
        return ""
    }

    fun convertAppBrandName(brand: String?): String? {
        if (TextUtils.isEmpty(brand)) {
            return ""
        }
        initAppCountBrand()
        return if (mAppCountBrands.containsKey(brand)) mAppCountBrands[brand] else brand!!
    }

    private fun initAppCountBrand() {
        if (mAppCountBrands.isNotEmpty()) {
            return
        }
        mAppCountBrands.clear()
        mAppCountBrands[ConstantValue.APP_ACCOUNT_BRAND_VICTURE] = ConstantValue.APP_NAME_OF_BRAND_VICTURE
        mAppCountBrands[ConstantValue.APP_ACCOUNT_BRAND_TECKIN] = ConstantValue.APP_NAME_OF_BRAND_TECKIN
    }
}