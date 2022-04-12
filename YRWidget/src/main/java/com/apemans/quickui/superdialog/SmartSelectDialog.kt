package com.apemans.quickui.superdialog

import android.graphics.Typeface
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.annotation.DimenRes
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.apemans.quickui.R
import com.apemans.quickui.gone
import com.apemans.quickui.utils.DisplayHelper.dpToPx
import com.apemans.quickui.visible
import com.ly.genjidialog.GenjiDialog
import com.ly.genjidialog.other.DialogOptions
import com.ly.genjidialog.other.ViewHolder

/***********************************************************
 * 作者: caro
 * 邮箱: 1025807062@qq.com
 * 日期: 2021/10/18 11:42
 * 说明:
 *
 * 备注:
 *
 ***********************************************************/
class SmartSelectDialog : BaseActionSuperDialog() {

    open class Item(val itemName: String, @DrawableRes val itemIcon: Int? = null)

    @ColorRes
    private var itemTextColor: Int? = null

    @ColorRes
    private var itemTextFocusColor: Int? = null

    @DimenRes
    private var itemFontSizeDimenRes: Int? = null
    private var itemFontStyle: Typeface? = null

    private var viewClose: ImageView? = null
    private var recyclerView: RecyclerView? = null

    private var itemData: MutableList<Item> = mutableListOf()

    private var onSelectItem: ((position: Int, value: Item) -> Unit)? = null

    /*recyclerView要显示的item size梳理*/
    private var showItemSize = 0

    /*item 当前显示时选中的项*/
    private var focusItem = 0

    /*item 选中时是否显示选中图标✅*/
    private var showChoose = false

    /*item text 显示位置*/
    private var itemNameGravity: Int = Gravity.CENTER

    override fun inflateLayout(): Int {
        return R.layout.select_dilaog
    }

    override fun isDarkMode(isDarkMode: Boolean, dialogOptions: DialogOptions) {
        if (isDarkMode) {
            dialogOptions.verticalMargin = requireContext().statusBarHeight.toFloat()
        }
    }

    override fun initView(viewHolder: ViewHolder) {
        super.initView(viewHolder)
        viewClose = viewHolder.getView(R.id.img_close)
        recyclerView = viewHolder.getView(R.id.recyclerView)
        if (showItemSize == 0) {
            showItemSize = itemData.size
        }
        recyclerView?.let {
            //动态设置高度
            val recyclerViewLayoutParams = it.layoutParams
            it.setPadding(0, 0, 0, 0)
            //Rv宽等他Dialog的宽
            recyclerViewLayoutParams.width = getDialogOptions().width
            //recyclerViewLayoutParams.width = DisplayHelper.getScreenWidth(context) - DisplayHelper.dpToPx(16) - DisplayHelper.dpToPx(16)
            recyclerViewLayoutParams.height = dpToPx(55) * showItemSize
        }
        val sliderAdapter = SuperAdapter.create(itemData, showItemSize,
            LinearLayoutManager.VERTICAL, R.layout.item_dialog_list_select,
            object : SuperAdapter.OnViewLoadedListener {
                override fun onViewLoaded(itemView: View, any: Any, position: Int) {
                    val viewLine = itemView.findViewById<View>(R.id.viewLine)
                    val viewTextTitle = itemView.findViewById<TextView>(R.id.txt_name)
                    val imgSelect = itemView.findViewById<ImageView>(R.id.img_select)
                    val imgItemIcon = itemView.findViewById<ImageView>(R.id.img_item_icon)
                    val dataCell: Item = any as Item
                    val itemIcon: Int? = dataCell.itemIcon
                    viewTextTitle.text = dataCell.itemName
                    viewTextTitle.gravity = itemNameGravity
                    if (position == itemData.size - 1) {
                        viewLine.gone()
                    } else {
                        viewLine.visible()
                    }

                    if (itemIcon != null) {
                        imgItemIcon.setImageResource(itemIcon)
                        imgItemIcon.visible()
                    } else {
                        imgItemIcon.visibility = View.INVISIBLE
                    }

                    if (focusItem == position) {
                        itemTextFocusColor?.let {
                            val colorStateList = ContextCompat.getColorStateList(requireContext(), it)
                            viewTextTitle.setTextColor(colorStateList)
                        }
                        if (showChoose) {
                            imgSelect.visible()
                        }

                    } else {
                        itemTextColor?.let {
                            val colorStateList = ContextCompat.getColorStateList(requireContext(), it)
                            viewTextTitle.setTextColor(colorStateList)
                        }
                        if (showChoose) {
                            imgSelect.visibility = View.INVISIBLE
                        }
                    }

                    itemFontSizeDimenRes?.let {
                        val dimen: Float = resources.getDimensionPixelSize(it).toFloat()
                        viewTextTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, dimen)
                    }
                    itemFontStyle?.let {
                        viewTextTitle.typeface = itemFontStyle
                    }
                }

                override fun onViewClicked(itemView: View, any: Any, position: Int) {
                    onSelectItem?.invoke(position, any as Item)
                    dismiss()
                }
            })
        val menusLayoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        recyclerView?.let {
            it.layoutManager = menusLayoutManager
            it.adapter = sliderAdapter
        }
    }


    fun setOnSelectItem(action: ((position: Int, item: Item) -> Unit)? = null): SmartSelectDialog {
        this.onSelectItem = action
        return this
    }

    fun setItemFontSize(@DimenRes itemFontSizeDimenRes: Int): SmartSelectDialog {
        this.itemFontSizeDimenRes = itemFontSizeDimenRes
        return this
    }

    fun setItemTextTypeface(itemFontStyle: Typeface): SmartSelectDialog {
        this.itemFontStyle = itemFontStyle
        return this
    }

    fun setItemTextColor(itemTextColor: Int): SmartSelectDialog {
        this.itemTextColor = itemTextColor
        return this
    }

    fun setItemTextFocusColor(itemTextFocusColor: Int): SmartSelectDialog {
        this.itemTextFocusColor = itemTextFocusColor
        return this
    }

    fun setItemNameGravity(gravity: Int): SmartSelectDialog {
        this.itemNameGravity = gravity
        return this
    }


    fun setItemData(itemData: MutableList<Item>): SmartSelectDialog {
        this.itemData = itemData
        return this
    }

    fun showItemSize(showItem: Int): SmartSelectDialog {
        this.showItemSize = showItem
        return this
    }

    fun showFocusItemPosition(focusItem: Int): SmartSelectDialog {
        this.focusItem = focusItem
        return this
    }

    fun showFocusChoose(showChoose: Boolean): SmartSelectDialog {
        this.showChoose = showChoose
        return this
    }

    companion object {
        fun build(
            manager: FragmentManager,
            options: (DialogOptions.(dialog: GenjiDialog) -> Unit)? = null
        ): SmartSelectDialog {
            if (options != null) {
                val dialog = newSmartSelectDialog(options)
                dialog.manager = manager
                return dialog
            }
            val dialog = newSmartSelectDialog()
            dialog.manager = manager
            return dialog
        }
    }
}

private fun newSmartSelectDialog(): SmartSelectDialog {
    return SmartSelectDialog()
}

private inline fun newSmartSelectDialog(options: DialogOptions.(dialog: GenjiDialog) -> Unit): SmartSelectDialog {
    val dialog = SmartSelectDialog()
    dialog.getDialogOptions().options(dialog)
    return dialog
}