package com.apemans.dmcomponent.ui.adddevice.items

import com.apemans.quickui.multitype.ICheckable

/**
 * @author Dylan Cai
 */
data class CategoryTitle(
    val name: String, override var isChecked: Boolean = false
) : ICheckable