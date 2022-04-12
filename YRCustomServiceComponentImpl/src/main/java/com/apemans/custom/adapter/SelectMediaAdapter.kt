package com.apemans.custom.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

import com.bumptech.glide.Glide
import com.apemans.custom.R
import com.apemans.custom.bean.FileInfo

/**
 * 聊天选择图片视频素材
 * @Param
 * @Return
 */
class SelectMediaAdapter(val context: Context, val res: Int) :
    RecyclerView.Adapter<SelectMediaAdapter.ViewHolder>() {
    private var selectMedias = mutableListOf<FileInfo>()
    private var onClickListener: onMyClickListener? = null

    var selectPos = 0


    fun getData(): MutableList<FileInfo> {
        return selectMedias
    }


    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val iv_select = view.findViewById<ImageView>(R.id.item_select_media_image)
        val tv_duration = view.findViewById<TextView>(R.id.item_album_tv_duration)
        val iv_mask = view.findViewById<ImageView>(R.id.cover_mask)

    }

    fun addData(selectMedias: MutableList<FileInfo>) {
        this.selectMedias = selectMedias
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(res, parent, false))
    }

    fun setOnMyClickListener(onMyClickListener: onMyClickListener) {
        this.onClickListener = onMyClickListener
    }


    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val shotVideoInfo = selectMedias[position]

        Glide.with(context).load(shotVideoInfo.videoClipPath).into(
            holder.iv_select
        )


        if (position == selectPos) {
            Glide.with(context).load(R.drawable.select_item_cover_mask).into(
                holder.iv_mask
            )
        } else Glide.with(context).load(R.drawable.unselect_item_cover_mask).into(
            holder.iv_mask
        )

        holder.itemView.setOnClickListener {
            onClickListener?.onItemClick(position, selectPos == position, true)
            if (selectPos != position) {
                selectPos = position
                notifyDataSetChanged()
            }
        }
    }


    override fun getItemCount(): Int {
        return selectMedias.size
    }

    interface onMyClickListener {
        fun onItemClick(position: Int, isSelect: Boolean, fromUser: Boolean)
    }

    fun addItem(filePath: String) {
        val fileInfo = FileInfo()
        fileInfo.videoClipPath = filePath
        selectMedias.add(fileInfo)

        val index = index()
        if (index != -1)
            selectPos = index
        notifyDataSetChanged()
    }

    private fun index(): Int {
        var indexOf = -1
        for (it in selectMedias) {
            if (it.videoClipPath == null) {
                indexOf = selectMedias.indexOf(it)
                if (indexOf > selectPos)
                    return indexOf
            }
        }
        return indexOf
    }

    fun removeClipVideo(path: String) {
        val first = selectMedias.first { it.videoClipPath == path }
        selectMedias.remove(first)
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }
}