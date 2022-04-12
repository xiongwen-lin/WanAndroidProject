package com.apemans.yruibusiness.base

import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.apemans.base.mvvm.show
import kotlinx.coroutines.flow.*

/**
 * @author Dylan Cai
 */

internal var defaultLoadingObserver: RequestLoadingObserver? = null
    private set
internal var defaultErrorObserver: RequestErrorObserver = { activity, e ->
    Toast.makeText(activity, e.message, Toast.LENGTH_SHORT).show()
}
typealias RequestDialogFactory = () -> DialogFragment
typealias RequestErrorObserver = (activity: FragmentActivity, e: Throwable) -> Unit

fun <T> Flow<T>.showLoadingWith(flow: MutableSharedFlow<Boolean>) =
    onStart { flow.emit(true) }
        .onCompletion { flow.emit(false) }

fun <T> Flow<T>.catchWith(flow: MutableSharedFlow<Throwable>) =
    catch { flow.emit(it) }

fun initRequestViewModel(factory: RequestDialogFactory, errorObserver: RequestErrorObserver? = null) {
    val requestLoadingObserver = object : RequestLoadingObserver() {
        private var dialogFragment: DialogFragment? = null
        override fun onCreate(activity: FragmentActivity) {
            dialogFragment = factory()
        }

        override fun onChanged(activity: FragmentActivity, isLoading: Boolean) {
            dialogFragment?.show(activity.supportFragmentManager, isLoading)
        }

        override fun onDestroy() {
            dialogFragment = null
        }
    }
    initRequestViewModel(requestLoadingObserver, errorObserver)
}

fun initRequestViewModel(loadingObserver: RequestLoadingObserver, errorObserver: RequestErrorObserver? = null) {
    defaultLoadingObserver = loadingObserver
    if (errorObserver != null) {
        defaultErrorObserver = errorObserver
    }
}

abstract class RequestViewModel : ViewModel() {
    protected val loadingFlow = MutableSharedFlow<Boolean>()
    val isLoading: LiveData<Boolean> = loadingFlow.asLiveData()

    protected val exceptionFlow = MutableSharedFlow<Throwable>()
    val exception: LiveData<Throwable> = exceptionFlow.asLiveData()
}