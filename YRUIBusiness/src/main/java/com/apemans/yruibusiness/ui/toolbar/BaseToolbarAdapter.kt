package com.apemans.yruibusiness.ui.toolbar

import androidx.viewbinding.ViewBinding
import com.apemans.yruibusiness.ui.loadingstate.BaseLoadingAdapter

/**
 * @author Dylan Cai
 */

abstract class BaseToolbarAdapter<VB : ViewBinding>(
  val config: ToolbarConfig
) : BaseLoadingAdapter<VB>()