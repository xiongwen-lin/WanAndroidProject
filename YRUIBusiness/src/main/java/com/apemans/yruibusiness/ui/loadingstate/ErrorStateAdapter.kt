package com.apemans.yruibusiness.ui.loadingstate

import com.apemans.yruibusiness.databinding.LayoutErrorBinding


/**
 * @author Dylan Cai
 */
class ErrorStateAdapter : BaseLoadingAdapter<LayoutErrorBinding>() {

  override fun onBindViewHolder(holder: BaseViewHolder<LayoutErrorBinding>) {
    super.onBindViewHolder(holder)
    holder.binding.btnReload.setOnClickListener {
      holder.onReloadListener?.onReload()
    }
  }

  override fun onBindViewHolder(binding: LayoutErrorBinding) {
  }
}