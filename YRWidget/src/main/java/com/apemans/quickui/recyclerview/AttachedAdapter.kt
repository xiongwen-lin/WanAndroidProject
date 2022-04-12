package com.apemans.quickui.recyclerview

import android.view.View
import androidx.recyclerview.widget.RecyclerView

/***********************************************************
 * @Author : caro
 * @Date   : 2020/10/30
 * @Func:
 *
 *
 * @Description:
 *
 *
 ***********************************************************/
abstract class AttachedAdapter<VH : RecyclerView.ViewHolder> :RecyclerView.Adapter<VH>() {

    protected var recyclerView:RecyclerView? = null

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        this.recyclerView = recyclerView
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
        this.recyclerView = null
    }

    fun getPosition(view: View):Int{
        return recyclerView?.getChildAdapterPosition(view)?:-1
    }
}