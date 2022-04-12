package com.apemans.dmcomponent.ui.pairmethod.fragment.createqrcode

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.apemans.dmcomponent.utils.wifiEncodeStringOf

class CreateQrCodeViewModel : ViewModel() {
    private val _wifiEncodeString = MutableLiveData<String>()
    val wifiEncodeString: LiveData<String> get() = _wifiEncodeString

    fun createQrCode(ssid:String,pwd:String)  {
        _wifiEncodeString.value = wifiEncodeStringOf(ssid, pwd, "us")
    }
}