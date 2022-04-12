package com.apemans.custom.adapter


import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.apeman.customerservice.databean.SelectMediaData.selectMediaDatas
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.apemans.custom.R
import com.apemans.custom.bean.MediaData

/**
 * @Param  聊天选择图片视频素材
 * @Return
 */
class AlbumAdapter : RecyclerView.Adapter<AlbumAdapter.ViewHolder>() {
    private var mediaInfos: MutableList<MediaData> = mutableListOf()
    private var onClickListener: OnClickListener? = null
    private var overCountListener: OverCountListener? = null


    private var dp10 = com.apemans.base.utils.DisplayHelper.dpToPx(10)

    private val multiTransformation = MultiTransformation(
        CenterCrop(), RoundedCorners(
            dp10
        )
    )


    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val iv_image = view.findViewById<ImageView>(R.id.item_album_iv_image)
        val iv_video = view.findViewById<ImageView>(R.id.item_album_iv_video)
        val tv_duration = view.findViewById<TextView>(R.id.item_album_tv_duration)
        val tv_id = view.findViewById<TextView>(R.id.item_album_tv_id)
        val tv_mask = view.findViewById<TextView>(R.id.item_album_tv_mask)
    }

    fun addData(list: MutableList<MediaData>) {
        mediaInfos = list
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_album, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val mediaInfo = mediaInfos.get(position)
        if (mediaInfo.duration > 0L) {
            holder.iv_video.visibility = View.VISIBLE
            holder.tv_duration.typeface = Typeface.defaultFromStyle(Typeface.BOLD);//加粗
            holder.tv_duration.text = com.apemans.base.utils.DateTimeUtil.getUtcTimeString(
                mediaInfo.duration,
                com.apemans.base.utils.DateTimeUtil.PATTERN_HMS
            )
            holder.tv_duration.visibility = View.VISIBLE
        }

        Glide.with(holder.itemView).load(mediaInfo.path)
            .transform(multiTransformation).into(holder.iv_image)
        //本地图片
//        holder.iv_image.loadAny(
//            File(mediaInfo.path))
        val indexOf = selectMediaDatas.indexOf(mediaInfo) + 1

        if (indexOf > 0) {
            holder.tv_id.visibility = View.VISIBLE
            holder.tv_mask.visibility = View.VISIBLE
            holder.tv_id.text = "$indexOf"
            holder.iv_image.setColorFilter(Color.parseColor("#99000000"), PorterDuff.Mode.SRC_ATOP)
        } else {
            holder.tv_mask.visibility = View.GONE
            holder.tv_id.visibility = View.GONE
            holder.iv_image.clearColorFilter()
        }

        holder.itemView.setOnClickListener {

            if (selectMediaDatas.indexOf(mediaInfo) < 0) {
                if (selectMediaDatas.size >= 3) {
                    overCountListener?.overCount(selectMediaDatas.size)
                    return@setOnClickListener
                }
                selectMediaDatas.add(mediaInfo)
                onClickListener?.onItemClick(position)
                notifyDataSetChanged()
            } else {
                selectMediaDatas.remove(mediaInfo)
                onClickListener?.onItemRemove(position)
                notifyDataSetChanged()
            }

        }
    }

    override fun getItemCount(): Int {
        return mediaInfos.size
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    interface OnClickListener {
        fun onItemClick(pos: Int)
        fun onItemRemove(pos: Int)
    }

    interface OverCountListener {
        fun overCount(count: Int)
    }

    fun setOnClickListener(onClickListener: OnClickListener) {
        this.onClickListener = onClickListener
    }

    fun setOverCountListener(overCountListener: OverCountListener) {
        this.overCountListener = overCountListener
    }

}