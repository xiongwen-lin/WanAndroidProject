package com.apemans.tdprintercomponentimpl.ui.setting.vm

import androidx.lifecycle.MutableLiveData
import com.apemans.tdprintercomponentimpl.ui.control.vm.TDPrinterControlViewModel
import com.apemans.tdprintercomponentimpl.ui.setting.bean.TDPrinterSettingUIState

/***********************************************************
 * 作者: zhengruidong@apemans.com
 * 日期: 2022/2/10 6:14 下午
 * 说明:
 *
 * 备注:
 *
 ***********************************************************/
class TDPrinterSettingViewModel : TDPrinterControlViewModel() {

    val uiState = MutableLiveData(TDPrinterSettingUIState())

}