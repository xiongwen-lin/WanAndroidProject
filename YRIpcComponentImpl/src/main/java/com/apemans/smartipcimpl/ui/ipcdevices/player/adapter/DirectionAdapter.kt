package com.apemans.smartipcimpl.ui.ipcdevices.player.adapter

import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.apemans.smartipcapi.webapi.PresetPointConfigure
import com.apemans.smartipcimpl.R
import com.apemans.smartipcimpl.constants.PRESET_POINT_MAX_LEN
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.dylanc.longan.application
import com.nooie.common.utils.file.FileUtil
import java.io.File

import java.util.*

/**
 * @Author:dongbeihu
 * @Description:朝向界面适配器
 * @Date: 2021/12/2-16:08
 */
class DirectionAdapter : RecyclerView.Adapter<DirectionAdapter.DirectionHolder>() {
    val dataList: MutableList<PresetPointConfigure> = ArrayList()
    private var mListener: OnItemClickListener? = null

    private var deviceId = "";

    /**
     * 设置是否处于编辑状态
     */
    private var isEdit = false

    class DirectionHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tv_item_default: TextView = itemView.findViewById(R.id.tv_ipc_direction_item_default)
        val ic_item: ImageView = itemView.findViewById(R.id.ic_ipc_direction_item)
        val ic_item_delete: ImageView = itemView.findViewById(R.id.ic_ipc_direction_item_delete)
        val tv_item_name: TextView = itemView.findViewById(R.id.tv_ipc_direction_item_name)
        val card_view: CardView = itemView.findViewById(R.id.card_view)
        val ic_nav_add: ImageView = itemView.findViewById(R.id.ic_nav_add)

        var itemViewNow = itemView
    }


    override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
    ): DirectionHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.device_manager_item_ipc_direction, parent, false)
        return DirectionHolder(view)
    }

    /**
     * 设置展示样式
     */
    override fun onBindViewHolder(holder: DirectionHolder, position: Int) {
        if (position == dataList.size) {  //添加View
            holder.card_view.setBackgroundResource(R.drawable.rectangle_9)
            holder.ic_nav_add.visibility = View.VISIBLE
            holder.tv_item_default.visibility = View.GONE
            holder.ic_item_delete.visibility = View.GONE
            holder.ic_item.visibility = View.GONE
            holder.tv_item_name.text =
                    holder.card_view.resources.getString(R.string.tcp_preview_dicrection_add_bearing)
            holder.itemViewNow.setOnClickListener {
                mListener?.onItemClick(position, null)
            }
        } else {
//            holder.card_view.setBackgroundResource(R.drawable.device_default_preview)
            holder.ic_nav_add.visibility = View.GONE

            holder.ic_item.visibility = View.VISIBLE
            val item = dataList[position]

            val imageUrl = FileUtil.getPresetPointThumbnail(application, obtainUid(), deviceId, item.position)
            Glide.with(application).load(imageUrl).placeholder(R.drawable.device_default_preview).skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE).apply( RequestOptions()
                .transforms( CenterCrop(),  RoundedCorners(10)
                )).into(holder.ic_item)

            if (position == 0) {
                holder.tv_item_default.visibility = View.VISIBLE
            } else {
                holder.tv_item_default.visibility = View.GONE
            }
            if (isEdit) {
                holder.ic_item_delete.visibility = View.VISIBLE
            } else {
                holder.ic_item_delete.visibility = View.GONE
            }
            holder.tv_item_name.text = item.name

            holder.itemViewNow.setOnClickListener {
                mListener?.onItemClick(position, item)
            }
            holder.ic_item_delete.setOnClickListener {
                mListener?.onItemRemove(position, item)
            }
        }


    }

    override fun getItemCount(): Int {
        return if (dataList.size < PRESET_POINT_MAX_LEN) { //当不超过三个时，支持添加View
            dataList.size + 1
        } else dataList.size

    }

    fun setDataList(list: List<PresetPointConfigure>?, deviceIds: String) {
        if (list != null) {
            dataList.clear()
            dataList.addAll(list)
            notifyDataSetChanged()
        }
        this.deviceId = deviceIds
    }

    fun setEdit(isEditNow: Boolean) {
        isEdit = isEditNow
        notifyDataSetChanged()
    }

    fun setListener(listener: OnItemClickListener) {
        mListener = listener
    }

    interface OnItemClickListener {
        fun onItemClick(position: Int, data: PresetPointConfigure?)

        /**
         * 删除
         */
        fun onItemRemove(position: Int, data: PresetPointConfigure?)
    }

    private fun obtainUid() : String {
        return ""
    }
}