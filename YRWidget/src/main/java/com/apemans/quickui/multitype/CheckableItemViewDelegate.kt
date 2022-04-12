package com.apemans.quickui.multitype


import androidx.recyclerview.widget.RecyclerView
import com.drakeet.multitype.ItemViewDelegate

/**
 * @author Dylan Cai
 */
interface ICheckable {
    val groupId: Int get() = -1
    var isChecked: Boolean
}

@JvmInline
value class CheckType private constructor(val value: Int) {
    companion object {
        val SINGLE_CANCEL_DISABLE = CheckType(-3)
        val SINGLE = CheckType(-1)
        val MULTIPLE = CheckType(-2)
        fun limit(number: Int): CheckType {
            check(number >= 0) { "Limit the number of checked cannot be negative." }
            return CheckType(number)
        }
    }
}

abstract class CheckableItemViewDelegate<T : ICheckable, VH : RecyclerView.ViewHolder>(
    private val type: CheckType
) : ItemViewDelegate<T, VH>() {

    private val selectedPositions by lazy {
        mutableMapOf<Int, Int>().withDefault { -1 }
    }

    @Suppress("UNCHECKED_CAST")
    fun checkItem(position: Int) {
        val item = adapterItems[position] as T
        when (type) {
            CheckType.SINGLE_CANCEL_DISABLE -> {
                val lastPosition = selectedPositions.getValue(item.groupId)
                val selectedItem = if (lastPosition >= 0) adapterItems[lastPosition] as T else null
                if (item != selectedItem) {
                    item.isChecked = true
                    selectedItem?.isChecked = false
                    selectedPositions[item.groupId] = position
                    if (lastPosition >= 0) {
                        adapter.notifyItemChanged(lastPosition)
                    }
                }
            }
            CheckType.SINGLE -> {
                val lastPosition = selectedPositions.getValue(item.groupId)
                val selectedItem = if (lastPosition >= 0) adapterItems[lastPosition] as T else null
                if (item != selectedItem) {
                    item.isChecked = true
                    selectedItem?.isChecked = false
                    selectedPositions[item.groupId] = position
                    if (lastPosition >= 0) {
                        adapter.notifyItemChanged(lastPosition)
                    }
                } else {
                    item.isChecked = !item.isChecked
                }
            }
            CheckType.MULTIPLE -> {
                item.isChecked = !item.isChecked
            }
            else -> {
                val checkedNumber = adapterItems.filter {
                    it is ICheckable && it.isChecked && it.groupId == item.groupId
                }.size
                if (checkedNumber >= type.value && !item.isChecked) {
                    return
                }
                item.isChecked = !item.isChecked
            }
        }
        adapter.notifyItemChanged(position)
    }

    fun checkAllItems(groupId: Int = -1) {
        if (type != CheckType.MULTIPLE) {
            throw IllegalStateException("The type must be CheckType.MULTIPLE to check all.")
        }
        adapterItems.forEach {
            if (it is ICheckable && it.groupId == groupId) {
                it.isChecked = true
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    fun getCheckedItems(groupId: Int = -1): List<T> {
        val list = mutableListOf<T>()
        if (type == CheckType.SINGLE && selectedPositions.getValue(groupId) > 0) {
            list.add(adapterItems[selectedPositions.getValue(groupId)] as T)
        } else {
            adapterItems.forEach {
                if (it is ICheckable && it.groupId == groupId) {
                    list.add(it as T)
                }
            }
        }
        return list
    }
}
