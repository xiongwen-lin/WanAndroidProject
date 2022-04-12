package com.apemans.quickui.preference

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView

/***********************************************************
 * @Author : caro
 * @Date   : 2020/11/11
 * @Func:
 * 设置项布局
 *
 * @Description:
 *
 *
 ***********************************************************/
abstract class BasePreferenceSettingTypeAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private lateinit var onSettingFuncEventClick: PreferenceSettingEventCallback
    private lateinit var settingItemList: List<PreferenceBean>

    fun bindViewEvent(onSettingFuncEventClick: PreferenceSettingEventCallback) {
        this.onSettingFuncEventClick = onSettingFuncEventClick
    }

    fun bindData(settingItemList: List<PreferenceBean>) {
        this.settingItemList = settingItemList
    }

    /*分类Layout*/
    @LayoutRes
    open fun provideCateGoryStyleLayoutResId(): Int = -1

    abstract fun providePreferenceStyleLayoutResId(): Int

    open fun provideBarStyleLayoutResId(): Int = -1

    open fun provideBarStyleView(): PreferenceBar? = null

    override fun onCreateViewHolder(
        parent: ViewGroup,
        @PreferenceViewType viewType: Int,
    ): RecyclerView.ViewHolder {
        return when (viewType) {
            PreferenceViewType.UITypeCategory -> {
                val view: PreferenceCategory = LayoutInflater.from(parent.context)
                    .inflate(provideCateGoryStyleLayoutResId(), parent, false) as PreferenceCategory
                CateGoryViewHolder(view)
            }
            PreferenceViewType.UITypeNormal -> {
                val view: PreferenceView = LayoutInflater.from(parent.context)
                    .inflate(providePreferenceStyleLayoutResId(), parent, false) as PreferenceView
                PreferenceViewViewHolder(view, onSettingFuncEventClick)
            }
            PreferenceViewType.UITypeSwitch -> {
                val view: PreferenceView = LayoutInflater.from(parent.context)
                    .inflate(providePreferenceStyleLayoutResId(), parent, false) as PreferenceView
                PreferenceViewViewHolder(view, onSettingFuncEventClick)

            }
            PreferenceViewType.UTTypeBar -> {
                if (provideBarStyleView() != null) {
                    PreferenceBarViewHolder(provideBarStyleView()!!, onSettingFuncEventClick)
                } else {
                    val layoutID = provideBarStyleLayoutResId()
                    val view: PreferenceBar = LayoutInflater.from(parent.context)
                        .inflate(layoutID, parent, false) as PreferenceBar
                    PreferenceBarViewHolder(view, onSettingFuncEventClick)
                }
            }
            else -> {
                throw RuntimeException("un deal viewType")
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            PreferenceViewType.UITypeCategory -> {
                val cateGoryViewHolder = holder as CateGoryViewHolder
                cateGoryViewHolder.bind(settingItemList[position])
            }
            PreferenceViewType.UITypeNormal -> {
                val preferenceView = holder as PreferenceViewViewHolder<*>
                preferenceView.bind(settingItemList[position])
            }
            PreferenceViewType.UITypeSwitch -> {
                val preferenceView = holder as PreferenceViewViewHolder<*>
                preferenceView.bind(settingItemList[position])
            }

            PreferenceViewType.UTTypeBar -> {
                val preferenceBarHolder = holder as PreferenceBarViewHolder<*>
                preferenceBarHolder.bind(settingItemList[position])
            }
        }
    }

    override fun getItemCount(): Int = settingItemList.size

    override fun getItemViewType(position: Int): Int {
        return settingItemList[position].itemUIType
    }

    class CateGoryViewHolder(private val preferenceCategory: PreferenceCategory) :
        RecyclerView.ViewHolder(preferenceCategory) {
        private val context: Context = itemView.context

        fun bind(preferenceItem: PreferenceBean) {
            val categoryName = preferenceItem.name
            preferenceCategory.setCateGoryName(categoryName)
        }
    }
}
