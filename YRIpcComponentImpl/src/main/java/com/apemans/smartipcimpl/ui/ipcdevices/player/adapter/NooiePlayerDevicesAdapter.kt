package com.apemans.smartipcimpl.ui.ipcdevices.player.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.apemans.smartipcapi.info.IpcDeviceInfo
import com.apemans.smartipcimpl.R

import com.nooie.common.utils.time.DateTimeUtil
import java.util.*

/**
 * @Author:dongbeihu
 * @Description:日期适配器
 * @Date: 2021/12/2-16:08
 */
class NooiePlayerDevicesAdapter : RecyclerView.Adapter<NooiePlayerDevicesAdapter.FunctionMenuHolder>() {
    private val dataList: MutableList<IpcDeviceInfo> = ArrayList()
    private var mListener: OnItemClickListener? = null

    class FunctionMenuHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvFunction: TextView = itemView.findViewById(R.id.tv_device)
        var itemViewNow = itemView
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FunctionMenuHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.device_item_ipc_device, parent, false)
        return FunctionMenuHolder(view)
    }

    /**
     * 设置展示样式
     */
    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: FunctionMenuHolder, position: Int) {
        val item = dataList[position]
        holder.tvFunction.text = item.name

        holder.itemViewNow.setOnClickListener {
            mListener?.onItemClick(position, item)
        }

    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    fun setDataList(list: List<IpcDeviceInfo>?) {
        if (list != null) {
            dataList.clear()
            dataList.addAll(list)
            notifyDataSetChanged()
        }
    }

    fun setListener(listener: OnItemClickListener) {
        mListener = listener
    }

    interface OnItemClickListener {
        fun onItemClick(position: Int, data: IpcDeviceInfo)
    }
}