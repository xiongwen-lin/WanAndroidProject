package com.apemans.quickui.superdialog

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


/**
 * Author: caro
 * Date  : 2019-06-23
 * Contact : 1025807062@qq.com
 * Description：
 * SuperAdapter is an adapter created to avoid unused RecyclerView adapter and duplicated code.
 * List of [items] is defined with <*> type, so any object is accepted.
 * Any view can be inflated using [viewToInflate] reference.
 * An [onViewLoadedListener] needs to be defined to act directly to the loaded view.
 * NOTE ：
 * Reference ：https://github.com/vinceMuni/SuperAdapter/blob/master/superadapter/src/main/java/com/amicotravelbot/superadapter/SuperAdapter.kt
 */
class SuperAdapter() : RecyclerView.Adapter<SuperAdapter.SuperViewHolder>() {

    //show item config.
    var showCount:Int = 0
    var orin:Int = LinearLayoutManager.VERTICAL

    companion object {
        fun create(items: List<*>,showCount:Int, orin:Int,viewToInflate: Int, onViewLoadedListener: OnViewLoadedListener): SuperAdapter {
            return SuperAdapter(
                items,
                showCount,
                orin,
                viewToInflate,
                onViewLoadedListener
            )
        }
    }

    private lateinit var items: List<*>
    private var viewToInflate: Int = 0
    private lateinit var onViewLoadedListener: OnViewLoadedListener

    private constructor(items: List<*>,showCount:Int,orin:Int, viewToInflate: Int, onViewLoadedListener: OnViewLoadedListener): this(){
        this.items = items
        this.showCount = showCount
        this.orin = orin
        this.viewToInflate = viewToInflate
        this.onViewLoadedListener = onViewLoadedListener
    }

    private var itemWidth = 0
    private var itemHeight = 0
    fun setItemCount(count:Int,parentWidth:Int,parentHeight:Int){
        itemWidth = parentWidth /count
        itemHeight = parentHeight /count
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SuperViewHolder {
        var itemView = LayoutInflater.from(parent.context).inflate(viewToInflate, parent,false)
        if (showCount>0){
            val layoutParams : ViewGroup.LayoutParams = itemView.layoutParams
            var parentHeight = parent.measuredHeight
            var parentWidth = parent.measuredWidth

            if(orin == LinearLayoutManager.VERTICAL) {
                layoutParams.height = if (itemHeight == 0) {
                    parentHeight / showCount
                }else{
                    itemHeight
                }
            }else{
                layoutParams.width = if (itemWidth == 0) {
                    parentWidth / showCount
                }else{
                    itemWidth
                }
            }
          itemView.layoutParams = layoutParams
        }
        return SuperViewHolder(
            itemView,
            onViewLoadedListener
        )
    }

    override fun getItemCount(): Int {
        return if (items == null) 0 else items.size
    }

    override fun onBindViewHolder(holder: SuperViewHolder, position: Int) {
        holder.bindItem(items[position]!!)
    }

    class SuperViewHolder(itemView: View, var onViewLoadedListener: OnViewLoadedListener): RecyclerView.ViewHolder(itemView) {

        fun bindItem(any: Any){
            onViewLoadedListener.onViewLoaded(itemView, any,adapterPosition)
            itemView.setOnClickListener {
                onViewLoadedListener.onViewClicked(itemView, any,adapterPosition)
            }
        }
    }

    interface OnViewLoadedListener {

        /**
         * @param itemView
         * @param any
         * It is called when the item view has been inflated and loaded
         */
        fun onViewLoaded(itemView: View, any: Any,position: Int)

        /**
         * @param itemView
         * @param any
         * This is a generic method called when the item view has been clicked.
         * Any view-specific click listener can be defined inside [onViewLoaded].
         */
        fun onViewClicked(itemView: View, any: Any,position: Int)
    }
}