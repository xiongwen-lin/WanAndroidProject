package com.apemans.usercomponent.mine.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.apemans.usercomponent.R

class PersonAdapter : RecyclerView.Adapter<PersonAdapter.PersonViewHolder>(){

    var lists : MutableList<String> = mutableListOf()

    class PersonViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        var view = itemView.findViewById<View>(R.id.view)
        var ivImage = itemView.findViewById<ImageView>(R.id.ivImage)
        var tvText = itemView.findViewById<TextView>(R.id.tvText)
        var ivArrar = itemView.findViewById<ImageView>(R.id.ivArrar)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PersonViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.mine_layout_person_item, parent, false)
        return PersonViewHolder(view)
    }

    override fun onBindViewHolder(holder: PersonViewHolder, position: Int) {
        val tvString = lists.get(position)
        val personViewHolder = holder
        when (position) {
            0 -> personViewHolder.ivImage.setImageResource(R.drawable.photo_black)
            1 -> personViewHolder.ivImage.setImageResource(R.drawable.person_support)
            2 -> personViewHolder.ivImage.setImageResource(R.drawable.set_icon)
            else -> personViewHolder.ivImage.setImageResource(R.drawable.inbox_message_blue)
        }
        personViewHolder.tvText.text = tvString
        personViewHolder.ivArrar.setImageResource(R.drawable.right_arrow_disable)

        personViewHolder.view.setOnClickListener {
            if (listener != null) {
                listener.onClick(position)
            }
        }
    }

    override fun getItemCount(): Int {
        if (lists == null) {
            return 0
        }
        return lists.size
    }

    fun setData(list : MutableList<String>) {
        if (list == null || list.size <= 0) {
            return
        }

        if (lists == null) {
            lists = mutableListOf()
        }

        lists.clear()
        lists.addAll(list)
        notifyDataSetChanged()
    }

    lateinit var listener : OnPersonClickLisener

    fun setPersonClickListener(listener : OnPersonClickLisener) {
        this.listener = listener
    }

    interface OnPersonClickLisener {
        fun onClick(position : Int)
    }
}