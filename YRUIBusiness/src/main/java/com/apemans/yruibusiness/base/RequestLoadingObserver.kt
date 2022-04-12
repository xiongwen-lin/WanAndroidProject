package com.apemans.yruibusiness.base

import androidx.fragment.app.FragmentActivity

/**
 * @author Dylan Cai
 */
abstract class RequestLoadingObserver {
  abstract fun onCreate(activity: FragmentActivity)

  abstract fun onChanged(activity: FragmentActivity, isLoading: Boolean)

  abstract fun onDestroy()
}