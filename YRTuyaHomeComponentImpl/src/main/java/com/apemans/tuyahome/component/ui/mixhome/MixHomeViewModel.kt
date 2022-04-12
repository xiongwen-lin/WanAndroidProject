package com.apemans.tuyahome.component.ui.mixhome

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.apemans.base.middleservice.YRMiddleConst
import com.apemans.base.middleservice.YRMiddleServiceManager
import com.apemans.yruibusiness.base.RequestViewModel
import com.apemans.tuyahome.component.bean.Device
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.*

class MixHomeViewModel : RequestViewModel() {

    private val originList = mutableListOf<Device>()

    private val categoryCache = mutableListOf<String>()
    private val _categoryList = MutableLiveData<List<String>>()
    val categoryList: LiveData<List<String>> get() = _categoryList

    private val _deviceList = MutableLiveData<List<Device>>()
    val deviceList: LiveData<List<Device>> get() = _deviceList

    private var currentTag: String = "ALL"

    fun initDevices(lifecycleOwner: LifecycleOwner) {
        YRMiddleServiceManager.listening("yrcx://yrsmarthomekitservice/querydevice", lifecycleOwner, mapOf("type" to "all")) {
            if (it.code == YRMiddleConst.MIDDLE_SUCCESS) {
                try {
                    val json = it.data as String
                    val list = Gson().fromJson<List<Device>>(json, object : TypeToken<List<Device>>() {}.type)
                    if (list.isNotEmpty()) {
                        if (originList.isEmpty()) {
                            originList.addAll(list)
                            if (list.isNotEmpty()) {
                                categoryCache.add(list.first().category)
                            }
                        } else {
                            list.forEach { device ->
                                val find = originList.find { it.uuid == device.uuid }
                                if (find != null) {
                                    originList[originList.indexOf(find)] = device
                                } else {
                                    originList.add(device)
                                }
                                if (!categoryCache.contains(device.category)) {
                                    categoryCache.add(device.category)
                                }
                            }
                        }
                    }
//                    originList.sortBy { device ->
//                        when (device.type) {
//                            "Ea" -> 3
//                            "Smart home" -> 2
//                            "Router" -> 1
//                            else -> 0
//                        }
//                    }
                    _categoryList.postValue(categoryCache)
                    val originList1 = originList
                    _deviceList.postValue(originList1)
                } catch (e : Exception) {
                }
            } else {
                exceptionFlow.tryEmit(IllegalStateException(it.errorMsg))
            }
        }
    }

    fun refresh() {

    }

    fun updateCategory(text: CharSequence) {
        _deviceList.value = if (text == "ALL") {
            originList
        } else {
            originList.filter { it.category == text }
        }
        currentTag = text.toString()
    }
}