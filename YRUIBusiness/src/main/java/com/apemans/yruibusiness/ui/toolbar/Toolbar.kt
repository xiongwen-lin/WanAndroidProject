@file:Suppress("unused")

package com.apemans.yruibusiness.ui.toolbar

import androidx.annotation.StringRes
import com.apemans.yruibusiness.ui.loadingstate.LoadingHelper

/**
 * @author Dylan Cai
 */

fun com.apemans.yruibusiness.base.BaseActivity<*>.setToolbar(
    title: String,
    navIconType: NavIconType = com.apemans.yruibusiness.ui.toolbar.NavIconType.NORMAL,
    block: (ToolbarConfig.() -> Unit)? = null
) =
    setToolbar {
        this.title = title
        this.navIconType = navIconType
        block?.invoke(this)
    }

fun com.apemans.yruibusiness.base.BaseActivity<*>.setToolbar(
    @StringRes title: Int = -1,
    navIconType: NavIconType = com.apemans.yruibusiness.ui.toolbar.NavIconType.NORMAL,
    block: (ToolbarConfig.() -> Unit)? = null
) =
    setToolbar {
        this.titleRes = title
        this.navIconType = navIconType
        block?.invoke(this)
    }

fun com.apemans.yruibusiness.base.BaseActivity<*>.setToolbar(block: ToolbarConfig.() -> Unit) =
    loadingHelper.setDecorHeader(ToolbarAdapter(ToolbarConfig().apply(block)))

fun com.apemans.yruibusiness.base.BaseFragment<*>.setToolbar(title: String, navIconType: NavIconType = com.apemans.yruibusiness.ui.toolbar.NavIconType.NORMAL) =
    setToolbar {
        this.title = title
        this.navIconType = navIconType
    }

fun com.apemans.yruibusiness.base.BaseFragment<*>.setToolbar(@StringRes title: Int = -1, navIconType: NavIconType = com.apemans.yruibusiness.ui.toolbar.NavIconType.NORMAL) =
    setToolbar {
        this.titleRes = title
        this.navIconType = navIconType
    }

fun com.apemans.yruibusiness.base.BaseFragment<*>.setToolbar(block: ToolbarConfig.() -> Unit) =
    loadingHelper.addChildDecorHeader(ToolbarAdapter(ToolbarConfig().apply(block), true))

fun com.apemans.yruibusiness.base.BaseActivity<*>.updateToolbar(block: ToolbarConfig.() -> Unit) =
    loadingHelper.getAdapter<ToolbarAdapter>(com.apemans.yruibusiness.ui.toolbar.ToolbarAdapter::class.java.name).run {
        config.block()
        notifyDataSetChanged()
    }

fun com.apemans.yruibusiness.base.BaseFragment<*>.updateToolbar(block: ToolbarConfig.() -> Unit) =
    loadingHelper.getAdapter<ToolbarAdapter>(com.apemans.yruibusiness.ui.toolbar.ToolbarAdapter::class.java.name).run {
        config.block()
        notifyDataSetChanged()
    }

fun com.apemans.yruibusiness.base.BaseActivity<*>.setMultiHeader(block: MultiHeaderBuilder.() -> Unit) =
    MultiHeaderBuilder(loadingHelper).apply(block).build()

fun com.apemans.yruibusiness.base.BaseFragment<*>.setMultiHeader(block: MultiHeaderBuilder.() -> Unit) =
    MultiHeaderBuilder(loadingHelper).apply(block).build()

fun MultiHeaderBuilder.toolbar(title: String, navIconType: NavIconType = NavIconType.NORMAL) =
    toolbar {
        this.title = title
        this.navIconType = navIconType
    }

fun MultiHeaderBuilder.toolbar(@StringRes title: Int = -1, navIconType: NavIconType = NavIconType.NORMAL) =
    toolbar {
        this.titleRes = title
        this.navIconType = navIconType
    }

fun MultiHeaderBuilder.toolbar(block: ToolbarConfig.() -> Unit) =
    add(ToolbarAdapter(ToolbarConfig().apply(block)))

class MultiHeaderBuilder(private val loadingHelper: LoadingHelper) {
    private val adapters = mutableMapOf<Any, LoadingHelper.Adapter<*>>()

    fun add(adapter: LoadingHelper.Adapter<*>) =
        add(adapter.javaClass.name, adapter)

    fun add(viewType: Any, adapter: LoadingHelper.Adapter<*>) {
        adapters[viewType] = adapter
    }

    fun build() {
        for ((viewType, adapter) in adapters) {
            loadingHelper.register(viewType, adapter)
        }
        loadingHelper.setDecorHeader(viewType = adapters.map { it.key }.toTypedArray())
    }
}