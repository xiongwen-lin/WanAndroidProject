@file:Suppress("unused")

package com.apemans.yruibusiness.base

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.view.ContextThemeWrapper
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding
import com.apemans.yruibusiness.utils.LoadingBuilder
import com.apemans.yruibusiness.utils.LoadingEvent
import com.apemans.yruibusiness.ui.loadingstate.LoadingHelper
import com.apemans.yruibusiness.utils.viewbinding.inflateBindingWithGeneric

/**
 * ViewBindingKTX + LoadingHelper 的封装范例
 *
 * @author Dylan Cai
 */
abstract class BaseFragment<VB : ViewBinding> : androidx.fragment.app.Fragment(), LoadingEvent {

    private var _binding: VB? = null
    protected val binding: VB get() = _binding!!
    lateinit var activity: Activity
    var _vm: BaseViewModel? = null
    //val viewModel: BaseViewModel get() = _vm!!

    //Fragment Theme 主题
    var themeStyle: Int? = null

    lateinit var loadingHelper: LoadingHelper private set
    protected open val contentView: View? = null
    protected open val enableLoadingHelper get() = true

    private var isActivityVisible = false
    private var isFragmentVisible = true
    private var mIsFirstVisible = true

    private val loadingDialog by lazy(LazyThreadSafetyMode.NONE) {
        LoadingBuilder.loadingFactory
    }

    /**
     * 设置主题
     * @return 要设置的主题Style
     */
    protected open fun registerThemeStyle(): Int? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        //设置Fragment主题
        themeStyle = registerThemeStyle()
        _binding = if (themeStyle != null) {
            val contextThemeWrapper: Context = ContextThemeWrapper(requireContext(), themeStyle!!)
            val localInflater = inflater.cloneInContext(contextThemeWrapper)
            inflateBindingWithGeneric(localInflater, container, false)
        } else {
            inflateBindingWithGeneric(inflater, container, false)
        }

        val rootView = binding.root
        activity = requireActivity()
        return when {
            !enableLoadingHelper -> rootView
            contentView != null -> {
                loadingHelper = LoadingHelper(contentView!!, ::onReload)
                rootView
            }
            else -> {
                loadingHelper = LoadingHelper(rootView, ::onReload)
                loadingHelper.decorView
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onViewCreated(binding.root)
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        isFragmentVisible = !hidden
        dispatcherHiddenChanged(hidden)
        if (isActivityVisible) {
            if (hidden) {
                onFragmentPause()
            } else {
                onFragmentResume()
            }
        }
    }

    private fun dispatcherHiddenChanged(hidden: Boolean) {
        for (fragment in childFragmentManager.fragments) {
            if (fragment is BaseFragment<*> && !fragment.isHidden) {
                fragment.onHiddenChanged(hidden)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        isActivityVisible = true
        if (isFragmentVisible) {
            onFragmentResume()
        }
        if (isActivityVisible && isFragmentVisible && mIsFirstVisible) {
            mIsFirstVisible = false
            onFragmentFirstVisible()
        }
    }

    override fun onPause() {
        super.onPause()
        isActivityVisible = false
        mIsFirstVisible = false
        if (isFragmentVisible) {
            onFragmentPause()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        mIsFirstVisible = true
        onFragmentDestroy()
    }

    override fun showLoading(message: String) {
        loadingDialog.showLoading(requireContext(), message)
    }

    override fun dismissLoading(delay: Int) {
        loadingDialog.dismissLoading(delay)
    }

    override fun showMessage(message: String, delay: Int) {
        loadingDialog.showMessage(message, delay)
    }

    protected abstract fun onViewCreated(root: View)

    protected open fun onFragmentResume() {

    }

    protected open fun onFragmentPause() {

    }

    protected open fun onFragmentFirstVisible() {

    }

    protected open fun onFragmentDestroy() {

    }

    protected fun showLoadingView() = loadingHelper.showLoadingView()

    protected fun showContentView() = loadingHelper.showContentView()

    protected fun showErrorView() = loadingHelper.showErrorView()

    protected fun showEmptyView() = loadingHelper.showEmptyView()

    protected fun showCustomView(viewType: Any) = loadingHelper.showView(viewType)

    protected open fun onReload() {}

    private fun LoadingHelper(view: View, onReload: () -> Unit) = LoadingHelper(view).apply {
        setOnReloadListener(onReload)
    }

    fun isFirstVisible(): Boolean {
        return mIsFirstVisible
    }

    /**
     * 注册viewModule
     * @param clazz viewModule类
     */
    inline fun <reified T : BaseViewModel> registerViewModule(clazz: Class<T>): T {
        val m = ViewModelProvider(this, defaultViewModelProviderFactory)[clazz]
        m.registerLoadingEvent(this)
        _vm = m
        return m
    }

    /**
     * 注册viewModule
     * @param factory repo provider
     * @param clazz viewModule类
     */
    inline fun <reified T : BaseViewModel> registerViewModule(factory: ViewModelProvider.Factory, clazz: Class<T>): T {
        val m = ViewModelProvider(this, factory).get(clazz)
        m.registerLoadingEvent(this)
        _vm = m
        return m
    }

}

