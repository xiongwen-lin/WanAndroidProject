package com.apemans.ipcchipproxy.scheme.util

import com.google.gson.reflect.TypeToken
import com.apemans.base.utils.JsonConvertUtil
import com.apemans.ipcchipproxy.scheme.api.IIpcSchemeResultCallback
import com.apemans.ipcchipproxy.scheme.bean.IpcDPCmdResult
import com.apemans.ipcchipproxy.scheme.bean.IpcSchemeResult
import com.apemans.ipcchipproxy.scheme.bean.IpcSchemeRetryCmdResult
import com.apemans.logger.YRLog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import java.lang.RuntimeException
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

/***********************************************************
 * 作者: zhengruidong@apemans.com
 * 日期: 2021/11/5 10:10 上午
 * 说明:
 *
 * 备注:
 *
 ***********************************************************/

suspend fun subscribeCallbackFlow(block : (IIpcSchemeResultCallback) -> Unit) : Flow<String> = flow {
    emit(runCallbackStream(block))
}
    .flowOn(Dispatchers.IO)
    .catch {
        emit("")
    }

suspend fun runCallbackFlow(block : (IIpcSchemeResultCallback) -> Unit) : Flow<String> = flow {
    emit(runCallbackStream(block))
}

suspend fun runCallbackStream(block : (IIpcSchemeResultCallback) -> Unit) : String =  suspendCoroutine {

    block.invoke(object : IIpcSchemeResultCallback {

        override fun onSuccess(code: Int, result: String?) {
            it.resume(
                JsonConvertUtil.convertToJson(IpcSchemeResult().apply {
                    this.code = code
                    this.result = result
                }).orEmpty()
            )
        }

        override fun onError(code: Int, error: String?) {
            it.resume(
                JsonConvertUtil.convertToJson(IpcSchemeResult().apply {
                    this.code = code
                    this.error = result
                }).orEmpty()
            )
        }
    })
}

suspend fun <T> retrySendCmd(retryCount: Long = 1, retryDelayTime: Long = 0, block : suspend () -> IpcSchemeRetryCmdResult<T>) : Flow<IpcSchemeRetryCmdResult<T>> {
    var sendCmdSuccess = false
    return flow<IpcSchemeRetryCmdResult<T>> {
        var sendCmdResult = block.invoke();
        sendCmdSuccess = sendCmdResult.isSuccess
        YRLog.d { "-->> retrySendCmd sendCmdSuccess $sendCmdSuccess" }
        if (sendCmdSuccess) {
            emit(sendCmdResult)
        } else {
            throw RuntimeException("Retry")
        }
    }
        .retry(retryCount) { it ->
            if (retryDelayTime > 0) {
                delay(retryDelayTime)
            }
            YRLog.d { "-->> retrySendCmd retrying sendCmdSuccess $sendCmdSuccess" }
            !sendCmdSuccess
        }
        .flowOn(Dispatchers.IO)
        .catch { e ->
            YRLog.d { "-->> retrySendCmd sendCmdSuccess exception $e" }
            emit(IpcSchemeRetryCmdResult())
        }
}

fun <T> parseIpcDPCmdResult(result : String?, dpId : String, block: () -> TypeToken<Map<String, T>>) : T? {
    try {
        return JsonConvertUtil.convertData(result, object : TypeToken<IpcDPCmdResult<Any>>(){})?.let {
            JsonConvertUtil.convertData((it.data as? String), block.invoke())?.let { data ->
                data[dpId]
            }
        }
    } catch (e: Exception) {
        YRLog.d { e }
    }
    return null
}
