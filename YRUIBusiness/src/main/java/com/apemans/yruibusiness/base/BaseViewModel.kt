package com.apemans.yruibusiness.base

import androidx.lifecycle.ViewModel
import com.apemans.yruibusiness.utils.LoadingEvent

/**
 * 作者:caro
 * ####### 增加对RxJava AutoDisposable 的支持 --> lifecycleProvider
 * 参考：https://blog.csdn.net/qq_36699930/article/details/89001357
 * 使用compose操作符绑定容器生命周期
 * 方式1：使用bindToLifecycle() --> .compose(lifecycleProvider.<Long>bindToLifecycle())
 * 方式2：使用bindUntilEvent() -->  .compose(lifecycleProvider.<Long>bindUntilEvent(ActivityEvent.DESTROY)) // 手动指定在生命周期onDestory()时，取消订阅。
 *
 * 关于Observable，可添加如下操作符，监听Observable取消订阅回调
 *  .doOnDispose(new Action() {
 *      @Override
 *      public void run() throws Exception {
 *        Log.e("xyh", "解除了订阅");
 *      }
 *  })
 *
 */
abstract class BaseViewModel : ViewModel() {

    var loadingEvent: LoadingEvent? = null

    fun registerLoadingEvent(loadingEvent: LoadingEvent) {
        this.loadingEvent = loadingEvent
    }

    fun showLoading(message: String = "") {
        loadingEvent?.showLoading(message)
    }

    fun showMessage(message: String, delay: Int = 2) {
        loadingEvent?.showMessage(message, delay)
    }

    fun dismissLoading(delay: Int = 0) {
        loadingEvent?.dismissLoading(delay)
    }
}