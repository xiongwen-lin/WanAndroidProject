package com.apemans.usercomponent.user.adapter

import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import java.util.ArrayList

class UserPageAdapter : PagerAdapter() {

    private var listView = ArrayList<View>()

    fun setViewList(list: ArrayList<View>) {
        listView = list
    }

    override fun getCount(): Int {
        return listView.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        container.addView(listView[position],0)
        return listView[position]
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(listView[position])
    }

    private lateinit var onListener : OnListenerViewPagerChange

    fun setViewPagerChangeListener(listener : OnListenerViewPagerChange) {
        onListener = listener
    }

    interface OnListenerViewPagerChange {
        fun selectPager(position : Int)
    }
}