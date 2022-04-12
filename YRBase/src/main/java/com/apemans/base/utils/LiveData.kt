@file:Suppress("unused", "NOTHING_TO_INLINE")

package com.apemans.base.mvvm

import android.app.Dialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData

/**
 * @author Dylan Cai
 */

inline fun LiveData<Boolean>.observe(
  activity: FragmentActivity,
  dialogFragment: DialogFragment
) =
  observe(activity) { dialogFragment.show(activity.supportFragmentManager, it) }

inline fun LiveData<Boolean>.observe(
  fragment: Fragment,
  dialogFragment: DialogFragment
) =
  observe(fragment.viewLifecycleOwner) { dialogFragment.show(fragment.parentFragmentManager, it) }

inline fun LiveData<Boolean>.observe(lifecycleOwner: LifecycleOwner, dialog: Dialog?) {
  observe(lifecycleOwner) { isLoading ->
    if (isLoading && dialog?.isShowing != true) {
      dialog?.show()
    } else if (!isLoading && dialog?.isShowing == true) {
      dialog.dismiss()
    }
  }
}

inline fun DialogFragment.show(fragmentManager: FragmentManager, isShow: Boolean) {
  if (isShow && !isShowing) {
    show(fragmentManager, toString())
  } else if (!isShow && isShowing) {
    dismiss()
  }
}

inline val DialogFragment.isShowing: Boolean
  get() = dialog?.isShowing == true && !isRemoving
