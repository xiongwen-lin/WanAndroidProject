package com.apemans.yruibusiness.ui.loadingstate

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.apemans.yruibusiness.utils.viewbinding.inflateBindingWithGeneric

/**
 * @author Dylan Cai
 */
abstract class BaseLoadingAdapter<VB : ViewBinding> :
    LoadingHelper.Adapter<BaseLoadingAdapter.BaseViewHolder<VB>>() {

    protected lateinit var context: Context

    override fun onCreateViewHolder(inflater: LayoutInflater, parent: ViewGroup) =
        BaseViewHolder<VB>(inflateBindingWithGeneric(parent))
            .apply { context = binding.root.context }

    override fun onBindViewHolder(holder: BaseViewHolder<VB>) {
        onBindViewHolder(holder.binding)
    }

    abstract fun onBindViewHolder(binding: VB)

    class BaseViewHolder<VB : ViewBinding>(val binding: VB) :
        LoadingHelper.ViewHolder(binding.root)
}