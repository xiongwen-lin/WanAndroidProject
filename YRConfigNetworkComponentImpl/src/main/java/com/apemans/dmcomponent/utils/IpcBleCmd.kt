package com.apemans.dmcomponent.utils

import com.dylanc.longan.logDebug
import java.util.*


//send cmd
const val BLE_CMD_LOGIN = "AT+L,VICTURE\r"
const val BLE_CMD_SET_PW = "AT+P,"
const val BLE_CMD_QUERY_PW = "AT+DP\r"
const val BLE_CMD_OPEN_HOT_SPOT = "AT+OA\r"
const val BLE_CMD_CLOSE_HOT_SPOT = "AT+CA\r"
const val BLE_CMD_DISTRIBUTE_NETWORK_SEND = "AT+S%1\$s%2\$s\r"
const val BLE_CMD_DISTRIBUTE_NETWORK_SEND_END = "AT+SEND,%1\$s\r"

//cmd response
const val BLE_CMD_LOGIN_RSP = "AR+L"
const val BLE_CMD_SET_PW_RSP = "AR+P"
const val BLE_CMD_QUERY_PW_RSP = "AR+DP"
const val BLE_CMD_OPEN_HOT_SPOT_RSP = "AR+OA"
const val BLE_CMD_CLOSE_HOT_SPOT_RSP = "AR+CA"
const val BLE_CMD_DISTRIBUTE_NETWORK_SEND_RSP = "AR+S"
const val BLE_CMD_FEATURE_RSP = "AR"
const val BLE_CMD_RSP_SUCCESS = "OK"
const val BLE_CMD_RSP_FAIL = "FAIL"
const val BLE_CMD_RSP_UNBIND = "UNBIND"
const val BLE_CMD_RSP_BOUND_BY_SELF = "BINDED1"
const val BLE_CMD_RSP_BOUND_BY_OHTER = "BINDED2"

const val BLE_CMD_SINGLE_PACKAGE_MAX_LEN = 20
const val BLE_CMD_VALUE_MAX_LEN = 14

const val BLE_CMD_QUERY_STATE_FACTORY = "0,0"

fun convertBleLongCmd(cmdValue: String): List<String> {
    logDebug("-->> debug IpcBleCmd cmdValue=$cmdValue")
    if (cmdValue.isEmpty()) {
        return emptyList()
    }
    try {
        val splitCmdList: MutableList<String> = ArrayList()
        val cmdLength: Int = getTextByteSize(cmdValue)
        if (cmdLength <= BLE_CMD_VALUE_MAX_LEN) {
            logDebug("-->> debug IpcBleCmd 1 subCmdValue=$cmdValue")
            splitCmdList.add(cmdValue)
            return splitCmdList
        }
        val currentTextSb = StringBuilder()
        val resultSb = StringBuilder()
        var i = 1
        while (getTextByteSize(currentTextSb.toString()) < cmdLength) {
            val subLen: Int = BLE_CMD_VALUE_MAX_LEN - (numLength(i) - 1)
            if (subLen <= 0) {
                break
            }
            val subStart = currentTextSb.length
            val isLast =
                resultSb.isEmpty() && cmdLength - getTextByteSize(currentTextSb.toString()) <= subLen
            if (isLast) {
                if (cmdLength - getTextByteSize(currentTextSb.toString()) > 0) {
                    val subCmdValue = cmdValue.substring(subStart)
                    currentTextSb.append(subCmdValue)
                    logDebug("-->> debug IpcBleCmd 2 subCmdValue=$subCmdValue subLen=$subLen")
                    splitCmdList.add(subCmdValue)
                }
                break
            }
            val tmpValue = cmdValue.substring(subStart, subStart + 1)
            //logDebug("-->> debug IpcBleCmd tmpValue=" + tmpValue + " newresultsblen=" + (getTextByteSize(resultSb.toString()) + getTextByteSize(tmpValue)) + " subLen=" + subLen);
            if (getTextByteSize(resultSb.toString()) + getTextByteSize(tmpValue) <= subLen) {
                resultSb.append(tmpValue)
                currentTextSb.append(tmpValue)
                if (getTextByteSize(resultSb.toString()) === subLen) {
                    logDebug("-->> debug IpcBleCmd 3 subCmdValue=$resultSb subLen=$subLen")
                    splitCmdList.add(resultSb.toString())
                    resultSb.setLength(0)
                    i++
                }
            } else {
                logDebug("-->> debug IpcBleCmd 4 subCmdValue=$resultSb subLen=$subLen")
                splitCmdList.add(resultSb.toString())
                resultSb.setLength(0)
                i++
            }
        }
        logDebug("-->> debug IpcBleCmd currentTextSb=$currentTextSb")
        return splitCmdList
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return emptyList()
}

fun getTextByteSize(text: String?): Int {
    return StringHelper.getStringByteSize(text, StringHelper.CharSet_UTF_8)
        .toInt()
}

fun numLength(num: Int): Int {
    try {
        return num.toString().length
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return 0
}

fun wifiEncodeStringOf(ssid: String, psd: String, area: String): String {
    val zone: String = CountryUtil.getCurrentTimezone() + ".00"
    //        val area: String = if (TextUtils.isEmpty(com.nooie.common.base.GlobalData.getInstance().getRegion())) com.nooie.common.base.GlobalData.getInstance()
    //            .getRegion() else com.nooie.common.base.GlobalData.getInstance().getRegion().toUpperCase()
//        val area = "us"
    val wifiInfo = java.lang.String.format(
        "WIFI:U:%s;Z:%s;R:%s;T:WPA;P:\"%s\";S:%s;",
        obtainUid(),
        zone,
        area,
        psd,
        ssid
    )
    val lenInfo = String.format(
        "L:%d;%d;%d;",
        StringHelper.getStringByteSize(psd, StringHelper.CharSet_UTF_8),
        StringHelper.getStringByteSize(ssid, StringHelper.CharSet_UTF_8),
        StringHelper.getStringByteSize(wifiInfo, StringHelper.CharSet_UTF_8)
    )
    val info = lenInfo + wifiInfo
    return info
}

private fun obtainUid() : String {
    return ""
}
