package com.apemans.smartipcimpl.ui.ipcdevices.player.adapter

import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.apemans.smartipcimpl.R
import com.apemans.smartipcimpl.ui.ipcdevices.player.bean.FunctionMenuItem
import com.apemans.smartipcimpl.ui.ipcdevices.player.bean.IPC_FUNCTION_TYPE_SOUND
import com.apemans.smartipcimpl.ui.ipcdevices.player.bean.IPC_FUNCTION_TYPE_SPEAK
import com.dylanc.longan.application
import java.util.*

/**
 * @Author:dongbeihu
 * @Description:ICP 录像、拍照等功能适配器
 * @Date: 2021/12/2-16:08
 */
class FunctionAdapter : RecyclerView.Adapter<FunctionAdapter.FunctionMenuHolder>() {
    private val dataList: MutableList<FunctionMenuItem> = ArrayList()
    private var mListener: OnItemClickListener? = null

    private var showState = false;
    private var isWaveout = false;
    private var isTalking  = false;

    class FunctionMenuHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvFunction: TextView = itemView.findViewById(R.id.tv_function)
        val icFunction: ImageView = itemView.findViewById(R.id.ic_function)
        var itemViewNow = itemView
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FunctionMenuHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.device_item_ipc_function, parent, false)
        return FunctionMenuHolder(view)
    }

    /**
     * 设置展示样式
     */


    @RequiresApi(Build.VERSION_CODES.M)
    override fun onBindViewHolder(holder: FunctionMenuHolder, position: Int) {
        val functionItem = dataList[position]
        holder.icFunction.load(functionItem.icon)
        holder.tvFunction.text = application.getString(functionItem.text)


        if (functionItem.functionType == IPC_FUNCTION_TYPE_SOUND) {  //设置声音图标是否开启
            if (isWaveout) {
                holder.icFunction.load(functionItem.icon)
            } else {
                holder.icFunction.load(R.drawable.ic_viewfinder_mute_off)
            }
        }

        if (functionItem.functionType == IPC_FUNCTION_TYPE_SPEAK) {  //设置讲话图标是否开启
            if (isTalking) {
                holder.icFunction.load(R.drawable.talk__blue_land)
            } else {
                holder.icFunction.load(functionItem.icon)
            }
        }

        /**
         * 播放状态未更新，图标置灰,不可点击
         */
        if (!showState) {
            holder.icFunction.alpha = 0.2F
            holder.tvFunction.setTextColor(holder.itemView.context.getColor(R.color.grey_400))
        } else {
            holder.icFunction.alpha = 1F
            holder.tvFunction.setTextColor(holder.itemView.context.getColor(R.color.black))
            holder.itemViewNow.setOnClickListener {
                mListener?.onItemClick(holder.icFunction, functionItem)
            }
        }
    }

    /**
     * 刷新状态
     * @param stateNow 是，展示功能区，否置灰
     * @param isWaveoutNow 是否开启声音
     */
    fun setState(stateNow: Boolean, isWaveoutNow: Boolean,isTalkingNow : Boolean) {
        showState = stateNow
        isWaveout = isWaveoutNow
        isTalking = isTalkingNow
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    fun setDataList(list: List<FunctionMenuItem>?) {
        if (list != null) {
            dataList.clear()
            dataList.addAll(list)
            notifyDataSetChanged()
        }
    }

    fun setListener(listener: OnItemClickListener) {
        mListener = listener
    }

    /**
     * 点击，获取当前功能数据，可能刷新某个图标
     */
    interface OnItemClickListener {
        fun onItemClick(positionView: ImageView, data: FunctionMenuItem)
    }
}