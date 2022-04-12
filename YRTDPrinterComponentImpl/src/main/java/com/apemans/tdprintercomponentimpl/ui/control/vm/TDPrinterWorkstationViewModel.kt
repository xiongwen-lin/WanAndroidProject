package com.apemans.tdprintercomponentimpl.ui.control.vm

import androidx.lifecycle.MutableLiveData
import com.apemans.tdprintercomponentimpl.ui.control.bean.TDPrinterWorkstationUIState

/***********************************************************
 * 作者: zhengruidong@apemans.com
 * 日期: 2022/2/10 6:14 下午
 * 说明:
 *
 * 备注:
 *
 ***********************************************************/
open class TDPrinterWorkstationViewModel : TDPrinterControlViewModel() {

    val uiState = MutableLiveData(TDPrinterWorkstationUIState())
}