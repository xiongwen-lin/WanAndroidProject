package com.apemans.usercomponent.mine.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.apemans.usercomponent.R
import com.apemans.usercomponent.mine.entry.MediaInfo
import com.apemans.usercomponent.mine.widget.SquareImageView

class MediaAddAdapter : RecyclerView.Adapter<MediaAddAdapter.MediaViewHolder>() {

    var mMediaInfos : MutableList<MediaInfo> = mutableListOf()
    val ITEM_ADD = 1
    val MAX_ADD = 3

    class MediaViewHolder(view : View) : RecyclerView.ViewHolder(view) {
        var view: View = view.findViewById<View>(R.id.view)
        var ivMediaIcon: SquareImageView = view.findViewById<SquareImageView>(R.id.ivMediaIcon)
        var ivMediaAddIcon: ImageView = view.findViewById<ImageView>(R.id.ivMediaAddIcon)
        var ivMediaDeleteIcon: ImageView = view.findViewById<ImageView>(R.id.ivMediaDeleteIcon)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MediaViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.mine_item_media_add, parent, false)
        return MediaViewHolder(view)
    }

    override fun onBindViewHolder(holder: MediaViewHolder, position: Int) {
        var viewHolder = holder
        var mdeiaIndo = mMediaInfos[position]
        if (position == mMediaInfos.size - 1) {
            if (position == MAX_ADD) {
                viewHolder.view.visibility = View.GONE
            } else {
                viewHolder.view.visibility = View.VISIBLE
                viewHolder.ivMediaDeleteIcon.visibility = View.GONE
                viewHolder.ivMediaAddIcon.visibility = View.VISIBLE
                Glide.with(viewHolder.ivMediaIcon.context).load(mdeiaIndo.path).apply(RequestOptions().centerCrop()).into(holder.ivMediaIcon)
                viewHolder.ivMediaAddIcon.setOnClickListener {
                    listener.onMediaAdd()
                }
            }
        } else {
            viewHolder.ivMediaDeleteIcon.visibility = View.VISIBLE
            viewHolder.ivMediaAddIcon.visibility = View.GONE
            Glide.with(viewHolder.ivMediaIcon.context).load(mdeiaIndo.path).apply(RequestOptions().centerCrop()).into(holder.ivMediaIcon)
            viewHolder.ivMediaDeleteIcon.setOnClickListener {
                if (mMediaInfos.size > 0) {
                    listener.onMediaDelete(position)
                    deleteMedia(position)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return mMediaInfos.size
    }

    fun setData(list : List<MediaInfo>) {
        if (list.isEmpty()) {
            return
        }

        if (mMediaInfos.isNotEmpty()) {
            mMediaInfos.clear()
        }
        mMediaInfos.addAll(list)
        mMediaInfos.add(list.size, MediaInfo())
        notifyDataSetChanged()
    }

    fun addData(mediaInfo: MediaInfo?) {
        if (mMediaInfos.size > MAX_ADD || mediaInfo == null) {
            return
        }
        /*if (mMediaInfos.size == 0) {
            init()
        }*/
        mMediaInfos.add(mMediaInfos.size - 1, mediaInfo)
        notifyDataSetChanged()
    }

    private fun deleteMedia(position : Int) {
        if (mMediaInfos.size <= 0) {
            return
        }
        if (position >= mMediaInfos.size) {
            return
        }
        mMediaInfos.removeAt(position)
        if (mMediaInfos.size == 1) {
            mMediaInfos.clear()
        }
        notifyDataSetChanged()
    }

    lateinit var listener : onMediaClickListener

    fun setMediaClickListener(listener : onMediaClickListener) {
        this.listener = listener
    }

    interface onMediaClickListener {
        fun onMediaAdd()
        fun onMediaDelete(position : Int)
    }
}