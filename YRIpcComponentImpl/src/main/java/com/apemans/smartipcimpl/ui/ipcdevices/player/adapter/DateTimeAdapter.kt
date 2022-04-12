package com.apemans.smartipcimpl.ui.ipcdevices.player.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.apemans.smartipcimpl.R
import com.apemans.smartipcimpl.ui.ipcdevices.player.old.bean.CalenderBean
import com.nooie.common.utils.time.DateTimeUtil
import java.util.*

/**
 * @Author:dongbeihu
 * @Description:日期适配器
 * @Date: 2021/12/2-16:08
 */
class DateTimeAdapter : RecyclerView.Adapter<DateTimeAdapter.FunctionMenuHolder>() {
    private val dataList: MutableList<CalenderBean> = ArrayList()
    private var mListener: OnItemClickListener? = null

    class FunctionMenuHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvFunction: TextView = itemView.findViewById(R.id.tv_date)
        var itemViewNow = itemView
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FunctionMenuHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.device_item_ipc_datetime, parent, false)
        return FunctionMenuHolder(view)
    }

    /**
     * 设置展示样式
     */
    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: FunctionMenuHolder, position: Int) {
        val item = dataList[position]
        var month  = item.calendar.get(Calendar.MONTH)+1
        var day  = item.calendar.get(Calendar.DAY_OF_MONTH)
        var showTime = "$month/$day"

        var todayStartTmp =  DateTimeUtil.getUtcTodayStartTimeStamp()
        val today = DateTimeUtil.getUtcTimeString(todayStartTmp, DateTimeUtil.PATTERN_MD_1)

        if (today.equals(showTime) ){
            holder.tvFunction.text = holder.tvFunction.context.getString(R.string.ipc_cloudstorage_date_today)
        }else{
            holder.tvFunction.text = showTime
        }

        if (item.isSelected) {
            holder.tvFunction.setTextColor(R.color.transparent20_white)
        }
        holder.itemViewNow.setOnClickListener {
            mListener?.onItemClick(position, item)
        }

    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    fun setDataList(list: List<CalenderBean>?) {
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
        fun onItemClick(position: Int, data: CalenderBean)
    }
}