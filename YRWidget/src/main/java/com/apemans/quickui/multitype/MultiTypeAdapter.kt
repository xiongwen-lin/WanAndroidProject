@file:Suppress("NOTHING_TO_INLINE")

package com.apemans.quickui.multitype

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.DiffUtil
import com.drakeet.multitype.ItemViewDelegate
import com.drakeet.multitype.KotlinClassLinker
import com.drakeet.multitype.Linker
import com.drakeet.multitype.MultiTypeAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlin.reflect.KClass


typealias ItemsLiveData<T> = MutableLiveData<List<T>>

inline fun <reified T : Any> MultiTypeAdapter(delegate: ItemViewDelegate<T, *>) =
    multiTypeAdapter { register(delegate) }

inline fun <reified T : Any> MultiTypeAdapter(
    vararg delegates: ItemViewDelegate<T, *>,
    linker: KotlinClassLinker<T>
) =
    multiTypeAdapter { register(*delegates).withKotlinClassLinker(linker) }

inline fun <reified T : Any> MultiTypeAdapter(
    vararg delegates: ItemViewDelegate<T, *>,
    linker: Linker<T>
) =
    multiTypeAdapter { register(*delegates).withLinker(linker) }

inline fun multiTypeAdapter(block: MultiTypeAdapter.() -> Unit) =
    MultiTypeAdapter().apply(block)

inline fun <reified T : Any> MultiTypeAdapter.register(vararg delegate: ItemViewDelegate<T, *>) =
    register(T::class).to(*delegate)

inline fun <T : Any> MultiTypeAdapter.observeItemsChanged(
    owner: LifecycleOwner,
    items: LiveData<List<T>>,
    detectMoves: Boolean = true,
    noinline areItemsTheSame: (T, T) -> Boolean
) {
    items.observe(owner) {
        submitItems(it, detectMoves, areItemsTheSame)
    }
}

inline fun <T : Any> MultiTypeAdapter.observeItemsChanged(
    scope: CoroutineScope,
    items: StateFlow<List<T>>,
    detectMoves: Boolean = true,
    noinline areItemsTheSame: (T, T) -> Boolean
) {
    scope.launch {
        observeItemsChanged(items, detectMoves, areItemsTheSame)
    }
}

suspend inline fun <T : Any> MultiTypeAdapter.observeItemsChanged(
    items: StateFlow<List<T>>,
    detectMoves: Boolean = true,
    noinline areItemsTheSame: (T, T) -> Boolean
) {
    items.collect {
        submitItems(it, detectMoves, areItemsTheSame)
    }
}

fun <T : Any> MultiTypeAdapter.submitItems(
    newItems: List<T>,
    detectMoves: Boolean = true,
    areItemsTheSame: (T, T) -> Boolean
) {
    val result = DiffUtil.calculateDiff(object : DiffUtil.Callback() {
        @Suppress("UNCHECKED_CAST")
        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
            areItemsTheSame(items[oldItemPosition] as T, newItems[newItemPosition])

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) =
            items[oldItemPosition] == newItems[newItemPosition]

        override fun getOldListSize() = items.size
        override fun getNewListSize() = newItems.size
    }, detectMoves)
    items = newItems
    result.dispatchUpdatesTo(this)
}

@Suppress("UNCHECKED_CAST")
inline fun <T> ItemViewDelegate<*, *>.getItem(position: Int) = adapterItems[position] as T

inline fun <T> KotlinClassLinker(crossinline block: (position: Int, item: T) -> KClass<out ItemViewDelegate<T, *>>) =
    object : KotlinClassLinker<T> {
        override fun index(position: Int, item: T): KClass<out ItemViewDelegate<T, *>> {
            return block(position, item)
        }
    }
