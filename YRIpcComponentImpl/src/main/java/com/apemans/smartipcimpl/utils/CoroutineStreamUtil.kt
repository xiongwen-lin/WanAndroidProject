package com.apemans.smartipcimpl.utils

import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

/***********************************************************
 * 作者: zhengruidong@apemans.com
 * 日期: 2021/11/18 4:05 下午
 * 说明:
 *
 * 备注:
 *
 ***********************************************************/

const val SCOPE_TASK_STATE_START = "START"
const val SCOPE_TASK_STATE_COMPLETION = "COMPLETION"
const val SCOPE_TASK_STATE_EXCEPTION = "EXCEPTION"
const val SCOPE_TASK_STATE_RESULT_SUCCESS = "SUCCESS"
const val SCOPE_TASK_STATE_RESULT_ERROR = "ERROR"

class ScopeTaskResult<T> {
    var state: String = ""
    var data: T? = null
}

fun <T,V> runScopeTask(scope: CoroutineScope, loadingState: MutableLiveData<ScopeTaskResult<V>>? = null, flowBlock: suspend () -> Flow<T?>, resultBlock: ((T?) -> ScopeTaskResult<V>?)? = null) {
    scope.launch {
        var result = flowBlock.invoke()
            .flowOn(Dispatchers.IO)
            .onStart { loadingState?.value?.state = SCOPE_TASK_STATE_START }
            .onCompletion { loadingState?.value?.state = SCOPE_TASK_STATE_COMPLETION }
            .catch { e ->
                emit(null)
            }
            .single()
        resultBlock?.invoke(result)?.let {
            if (it.state == SCOPE_TASK_STATE_RESULT_SUCCESS || it.state == SCOPE_TASK_STATE_RESULT_ERROR) {
                loadingState?.value = it
            }
        }
    }
}