package com.apemans.usercomponent.user.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.apemans.usercomponent.R
import com.apemans.usercomponent.user.entry.SortModel

class SortAdapter(context: Context, data: List<SortModel>?) :
    RecyclerView.Adapter<SortAdapter.ViewHolder?>() {
    private val mInflater: LayoutInflater = LayoutInflater.from(context)
    private var mData: List<SortModel>?
    private val mContext: Context
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = mInflater.inflate(R.layout.user_layout_country_item, parent, false)
        val viewHolder = ViewHolder(view)
        viewHolder.tvTag = view.findViewById<View>(R.id.tag) as TextView
        viewHolder.tvName = view.findViewById<View>(R.id.name) as TextView
        return viewHolder
    }
    /*fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        val view: View = mInflater.inflate(R.layout.item, parent, false)
        val viewHolder = ViewHolder(view)
        viewHolder.tvTag = view.findViewById<View>(R.id.tag) as TextView
        viewHolder.tvName = view.findViewById<View>(R.id.name) as TextView
        return viewHolder
    }*/

    override fun onBindViewHolder(holder: ViewHolder, @SuppressLint("RecyclerView") position: Int) {
        val section = getSectionForPosition(position)

        //如果当前位置等于该分类首字母的Char的位置 ，则认为是第一次出现
        if (position == getPositionForSection(section)) {
            holder.tvTag?.visibility = View.VISIBLE
            holder.tvTag?.text = mData!![position].letters
        } else {
            holder.tvTag?.visibility = View.GONE
        }
        if (mOnItemClickListener != null) {
            holder.itemView.setOnClickListener(View.OnClickListener {
                mOnItemClickListener!!.onItemClick(
                    holder.itemView,
                    position
                )
            })
        }
        holder.tvName?.text = mData!![position].name
        holder.tvName?.setOnClickListener(View.OnClickListener {
            mOnItemClickListener?.onItemClick(holder.itemView, position)
            /*Toast.makeText(
                mContext,
                mData!![position].name,
                Toast.LENGTH_SHORT
            ).show()*/
        })
    }

    override fun getItemCount(): Int {
        return if (mData != null) mData!!.size else 0
    }

    /*val itemCount: Int
        get() = if (mData != null) mData!!.size else 0*/

    fun getItem(position: Int): Any {
        return mData!![position]
    }

    /** itemClick  */
    private var mOnItemClickListener: OnItemClickListener? = null

    interface OnItemClickListener {
        fun onItemClick(view: View?, position: Int)
    }

    fun setOnItemClickListener(mOnItemClickListener: OnItemClickListener?) {
        this.mOnItemClickListener = mOnItemClickListener
    }

    /**
     * 提供给Activity刷新数据
     * @param list
     */
    fun updateList(list: List<SortModel>?) {
        mData = list
        notifyDataSetChanged()
    }

    /**
     * 根据ListView的当前位置获取分类的首字母的Char ASCII值
     */
    private fun getSectionForPosition(position: Int): Int {
        return mData!![position].letters?.get(0)?.toInt()!!
    }

    /**
     * 根据分类的首字母的Char ASCII值获取其第一次出现该首字母的位置
     */
    fun getPositionForSection(section: Int): Int {
        for (i in 0 until itemCount) {
            val sortStr: String? = mData!![i].letters
            val firstChar = sortStr?.toUpperCase()?.get(0)
            if (firstChar != null) {
                if (firstChar.toInt() == section) {
                    return i
                }
            }
        }
        return -1
    }

    class ViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView!!) {
        var tvTag: TextView? = null
        var tvName: TextView? = null
    }

    init {
        mData = data
        mContext = context
    }

}