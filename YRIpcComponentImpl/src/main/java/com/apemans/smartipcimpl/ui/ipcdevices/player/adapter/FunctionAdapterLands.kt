package com.apemans.smartipcimpl.ui.ipcdevices.player.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.apemans.smartipcimpl.R
import com.apemans.smartipcimpl.ui.ipcdevices.player.bean.FunctionMenuItem
import com.apemans.smartipcimpl.ui.ipcdevices.player.bean.IPC_FUNCTION_TYPE_SOUND
import com.apemans.smartipcimpl.ui.ipcdevices.player.bean.IPC_FUNCTION_TYPE_SPEAK
import java.util.*

/**
 * @Author:dongbeihu
 * @Description:ICP 录像、拍照等功能适配器,横屏展示，不展示文字，不展示方位警报
 * @Date: 2021/12/2-16:08
 */
class FunctionAdapterLands : RecyclerView.Adapter<FunctionAdapterLands.FunctionMenuHolder>() {
    private val dataList: MutableList<FunctionMenuItem> = ArrayList()
    private var mListener: OnItemClickListener? = null
    private var showState = false;
    private var isWaveout = false;
    private var isTalking  = false;

    class FunctionMenuHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val icFunction: ImageView = itemView.findViewById(R.id.iv_function_lands)
        var itemViewNow = itemView
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FunctionMenuHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.device_item_ipc_function_lands, parent, false)
        return FunctionMenuHolder(view)
    }

    /**
     * 设置展示样式
     */
    override fun onBindViewHolder(holder: FunctionMenuHolder, position: Int) {
        val functionItem = dataList[position]
        if (functionItem.iconLands != -1) {
            holder.icFunction.load(functionItem.iconLands)
            holder.icFunction.visibility == View.VISIBLE
        } else {
            holder.itemViewNow.visibility == View.GONE  //有些功能不展示
        }
        if (functionItem.functionType == IPC_FUNCTION_TYPE_SOUND) {  //设置声音图标是否开启
            if (isWaveout) {
                holder.icFunction.load(functionItem.iconLands)
            } else {
                holder.icFunction.load(R.drawable.ic_viewfinder_mute_on)
            }

        }

        if (functionItem.functionType == IPC_FUNCTION_TYPE_SPEAK) {  //设置讲话图标是否开启
            if (isTalking) {
                holder.icFunction.load(R.drawable.talk__blue_land)
            } else {
                holder.icFunction.load(functionItem.iconLands)
            }
        }
        holder.itemViewNow.setOnClickListener {
            mListener?.onItemClick(holder.icFunction, functionItem)
        }


    }

    override fun getItemCount(): Int {
        var itemCount = 0
        dataList.forEach {
            if (it.iconLands != -1) {//有些功能不展示
                itemCount++
            }
        }

        return itemCount
    }

    fun setDataList(list: List<FunctionMenuItem>?) {
        if (list != null) {
            dataList.clear()
            dataList.addAll(list)
            notifyDataSetChanged()
        }
    }

    /**
     * 刷新状态
     */
    fun setState(stateNow: Boolean, isWaveoutNow: Boolean,isTalkingNow : Boolean) {
        showState = stateNow
        isWaveout = isWaveoutNow
        isTalking = isTalkingNow
        notifyDataSetChanged()
    }

    fun setListener(listener: OnItemClickListener) {
        mListener = listener
    }

    interface OnItemClickListener {
        fun onItemClick(positionView: ImageView, data: FunctionMenuItem)
    }
}