package com.apemans.quickui.recyclerview.awesome

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import com.drakeet.multitype.MultiTypeAdapter
import com.apemans.quickui.R
import com.apemans.quickui.recyclerview.SmartRefreshLayout
import com.apemans.quickui.statelayout.StateLayout

/***************************************************************************************************
 * @Author : caro
 * @Date   : 2020/11/2
 * @Func:
 *
 *
 * @Description:
 *
 **************************************************************************************************/

class AwesomeDataRecyclerView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0,
) : ConstraintLayout(context, attrs, defStyleAttr) {
    //View
    var viewStateLayout: StateLayout
    private var smartRefreshLayout: SmartRefreshLayout
    var recyclerView: RecyclerView

    var adapter: MultiTypeAdapter

    //数据源
    var dataList: MutableList<Any>? = null

    var layoutManager: RecyclerView.LayoutManager? = null

    init {
        val rootView = LayoutInflater.from(context).inflate(R.layout.layout_awesome_data_recyclerview, this, true)
        viewStateLayout = rootView.findViewById(R.id.viewStateLayout)
        smartRefreshLayout = rootView.findViewById(R.id.smartRefreshLayout)
        recyclerView = rootView.findViewById(R.id.recyclerView)
        adapter = MultiTypeAdapter()
        recyclerView.adapter = adapter

        //屏蔽默认刷新动画
        (recyclerView.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false

        //默认Loading
        showLoading()
    }

    fun bindSourceData(dataList: MutableList<Any>) {
        this.dataList = dataList
        this.dataList?.apply {
            adapter.items = this
            notifyDataSetChanged()
            if (this.isNotEmpty()) {
                viewStateLayout.showContent()
            }
        }
    }

    fun registerLayoutManager(layoutManager: RecyclerView.LayoutManager) {
        this.layoutManager = layoutManager
        recyclerView.layoutManager = layoutManager
    }

    fun showEmpty(
        @DrawableRes showResId: Int,
        descText: String?,
        btnText: String?,
        @DrawableRes btnBackgroundResId: Int,
    ) {
        viewStateLayout.showEmpty()

        val viewEmptyImage = viewStateLayout.findViewById<ImageView>(R.id.viewEmptyImage)
        val viewEmptyTextDesc = viewStateLayout.findViewById<TextView>(R.id.viewEmptyTextDesc)
        val viewEmptyBtn = viewStateLayout.findViewById<TextView>(R.id.viewEmptyBtn)

        viewEmptyImage.visibility = View.VISIBLE
        viewEmptyImage.setImageResource(showResId)

        if (!descText.isNullOrEmpty()) {
            viewEmptyTextDesc.text = descText
            viewEmptyTextDesc.visibility = View.VISIBLE
        } else {
            viewEmptyTextDesc.visibility = View.GONE
        }

        if (!btnText.isNullOrEmpty()) {
            viewEmptyBtn.text = btnText
            viewEmptyBtn.visibility = View.VISIBLE
        } else {
            viewEmptyBtn.visibility = View.GONE
        }

        viewEmptyBtn.setBackgroundResource(btnBackgroundResId)
    }

    fun showError(
        @DrawableRes showResId: Int,
        errorDescText: String?,
        errorBtnText: String?,
        @DrawableRes btnBackgroundResId: Int,
    ) {
        viewStateLayout.showError()

        val viewErrorImage = viewStateLayout.findViewById<ImageView>(R.id.viewErrorImage)
        val viewErrorTextDesc = viewStateLayout.findViewById<TextView>(R.id.viewErrorTextDesc)
        val viewErrorBtn = viewStateLayout.findViewById<TextView>(R.id.viewErrorBtn)

        viewErrorImage.visibility = View.VISIBLE
        viewErrorImage.setImageResource(showResId)

        if (!errorBtnText.isNullOrEmpty()) {
            viewErrorBtn.text = errorBtnText
            viewErrorBtn.visibility = VISIBLE
        } else {
            viewErrorBtn.visibility = GONE
        }

        if (!errorDescText.isNullOrEmpty()) {
            viewErrorTextDesc.text = errorDescText
            viewErrorTextDesc.visibility = VISIBLE
        } else {
            viewErrorTextDesc.visibility = GONE
        }
        viewErrorBtn.setBackgroundResource(btnBackgroundResId)
    }

    fun showLoading() {
        viewStateLayout.showLoading()
    }

    fun showContent() {
        viewStateLayout.showContent()
    }

    fun notifyItemChanged(position: Int) {
        adapter.notifyItemChanged(position)
    }

    fun notifyItemDownloadChanged(position: Int) {
        adapter.notifyItemChanged(position)
    }

    fun notifyDataSetChanged() {
        adapter.notifyDataSetChanged()
    }

    fun notifyItemInserted(position: Int) {
        adapter.notifyItemInserted(position)
    }

    fun notifyItemRemoved(position: Int) {
        adapter.notifyItemRemoved(position)
    }


}