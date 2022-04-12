package com.apemans.usercomponent.mine.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.apemans.usercomponent.R
import com.apemans.usercomponent.mine.bean.FAQBean
import java.util.ArrayList

class FAQAdapter : RecyclerView.Adapter<FAQAdapter.FAQViewHolder?>() {
    private var mData: MutableList<FAQBean>? = ArrayList<FAQBean>()
    private var mListener: OnFAQItemClickListener? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FAQViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.mine_item_faq, parent, false)
        return FAQViewHolder(view)
    }

    override fun onBindViewHolder(holder: FAQViewHolder, position: Int) {
        var id = position
        val faqBean: FAQBean = mData!![id]
        holder.tvQueTitle.text = faqBean.title
        holder.tvQueContent.text = faqBean.content
        holder.tvQueContent.visibility = if (faqBean.isExpand) View.VISIBLE else View.GONE
        holder.vh.setOnClickListener {
            if ((faqBean.children != null && faqBean.children!!.isNotEmpty())) {
                if (mListener != null) {
                    mListener!!.onItemClick(position, faqBean)
                }
            } else {
                mData!![holder.adapterPosition].toggleExpand()
                holder.tvQueContent.visibility =
                    if (mData!![holder.adapterPosition].isExpand) View.VISIBLE else View.GONE
            }
        }
    }

    override fun getItemCount(): Int {
        return if (mData != null) mData!!.size else 0
    }

    fun setData(data: List<FAQBean>?) {
        var data: List<FAQBean>? = data
        if (mData == null) {
            mData = ArrayList<FAQBean>()
        }
        if (data == null) {
            data = ArrayList<FAQBean>()
        }
        mData!!.clear()
        mData!!.addAll(data)
        notifyDataSetChanged()
    }

    fun setListener(listener: OnFAQItemClickListener) {
        mListener = listener
    }

    fun release() {
        if (mData != null) {
            mData!!.clear()
            mData = null
        }
    }

    class FAQViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var tvQueTitle: TextView = view.findViewById<TextView>(R.id.tvQueTitle)
        var tvQueContent: TextView = view.findViewById<TextView>(R.id.tvQueContent)
        var vh = view
    }

    interface OnFAQItemClickListener {
        fun onItemClick(position: Int, data: FAQBean?)
    }
}