package com.apemans.quickui.superdialog

import android.graphics.Typeface
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.util.TypedValue
import android.view.KeyEvent
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.annotation.DimenRes
import androidx.annotation.DrawableRes
import androidx.annotation.StyleRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import com.animsh.animatedcheckbox.AnimatedCheckBox
import com.apemans.quickui.superdialog.listener.OnDialogDismiss
import com.apemans.quickui.superdialog.listener.OnDialogShow
import com.ly.genjidialog.GenjiDialog
import com.ly.genjidialog.extensions.addShowDismissListener
import com.ly.genjidialog.extensions.convertListenerFun
import com.ly.genjidialog.extensions.onKeyListenerForOptions
import com.ly.genjidialog.other.DialogGravity
import com.ly.genjidialog.other.DialogOptions
import com.ly.genjidialog.other.ViewHolder
import com.apemans.quickui.R
import com.apemans.quickui.editbox.SmartEditBox
import com.apemans.quickui.gone
import com.apemans.quickui.utils.DisplayHelper.dpToPx
import com.apemans.quickui.utils.DisplayHelper.getStatusBarHeight
import com.apemans.quickui.utils.screenWidthPx
import com.apemans.quickui.visible
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.dylanc.longan.application

/***********************************************************
 * 作者: caro
 * 日期: 2021/9/9 15:45
 * 说明: Dialog基础属性
 *
 * 备注:
 * 备注：https://github.com/chnouman/AwesomeDialog 不满足需求，故参考其AwesomeDialog并继承SuperDialog
 * 写了个AwesomeDialog
 * 主要用途：提示类
 *
 * @Description:
 * unLeak -- 当不是一次性使用时，只需要将unLeak属性设置为true ，unLeak = true
 *           设置为true时？LeakCannary会报内存泄漏
 * canClick -- 否可以触发取消，默认：true，比如在动画开始时将此属性设置false，防止在动画进行时，被再次触发动画，
 *             当使用上面两种自定义特殊动画时，我已经默认添加了改变这个状态值的监听
 * isFullHorizontal -- dialog是否横向占满，默认：false
 * isFullVertical -- dialog是否纵向占满，默认：false，该纵向占满并非全屏，纵向占满会自动扣掉状态栏的高度
 * outCancel  -- 是否点击外部取消，默认：false，当 touchCancel == true时此属性无效，
 *               必须是 touchCancel和该属性均为false时，那么点击屏幕区域和返回按钮都不能关闭dialog
 * touchCancel -- 是否点击屏幕区域取消（不包含返回按钮），默认：false
 * 更多参数参考：https://github.com/q876625596/GenjiDialogV2 README 中 DialogOptions的描述
 *
 * 备注：
 * 1：Android动态设置字体大小的姿势https://cloud.tencent.com/developer/article/1330414
 * 2：通过setTextColor时。对外面传入的titleColor一定是这样传进来的：ContextCompat.getColor(requireContext(), R.color.colorPrimary)
 *     ContextCompat.getColor(requireContext(), R.color.colorPrimary) 对应@ColorInt
 *     viewTitle!!.setTextColor(it)
 *     如果外面传入的是R.color.colorPrimary，则以下方式显示.故 传入的color变量请用  @ColorRes 标记
 *
 ***********************************************************/
abstract class BaseActionSuperDialog : GenjiDialog() {
    private val TAG = BaseActionSuperDialog::class.simpleName
    protected var manager: FragmentManager? = null
    private var headerImageRes: Int? = null
    private var animateIcon: Boolean = false
    private var dialogBackgroundRes: Int? = null
    private var dialogBackgroundColor: Int? = null
    private var canAbleDismiss: Boolean = false
    private var onPositiveEvent: ((dialog: BaseActionSuperDialog) -> Unit)? = null
    private var onNegativeEvent: ((dialog: BaseActionSuperDialog) -> Unit)? = null
    private var onRemindEvent: ((isChecked: Boolean) -> Unit)? = null
    private var onEditBoxTextChanged: ((smartEditBox: SmartEditBox, textChanged: String) -> Unit)? = null

    private var headerImageView: ImageView? = null
    private var titleTextView: TextView? = null
    private var contentTextView: TextView? = null
    private var buttonPanel: LinearLayout? = null
    private var positiveTextView: TextView? = null
    private var negativeTextView: TextView? = null
    private var root_remind: LinearLayout? = null
    private var checkbox_remind: AnimatedCheckBox? = null
    private var txt_remind: TextView? = null
    private var smartEditBox: SmartEditBox? = null
    private var showEditBox: Boolean = false
    private var editBoxHint: String? = null
    private var editBoxDefaultText: String? = null

    /*显示键盘*/
    private var showKeyboard = false

    /*显示提示*/
    private var showRemind = false

    /**
     * 设置自动消失时间
     */
    private var autoDelayMillisDismiss = 2000L
    private var openAutoDismiss = false

    /*Dialog默认左右边距：20dp*/
    private val default_margin_left_right_dp = 20

    /**
     * 头部图片
     */
    private var headUrl  =""

    abstract fun inflateLayout(): Int

    private fun findDialogBasicView(viewHolder: ViewHolder) {
        headerImageView = viewHolder.getView(R.id.img_header)
        titleTextView = viewHolder.getView(R.id.txt_title)
        contentTextView = viewHolder.getView(R.id.txt_content)
        buttonPanel = viewHolder.getView(R.id.buttonPanel)
        positiveTextView = viewHolder.getView(R.id.txt_positive)
        negativeTextView = viewHolder.getView(R.id.txt_negative)
        root_remind = viewHolder.getView(R.id.root_remind)
        checkbox_remind = viewHolder.getView(R.id.checkbox_remind)
        txt_remind = viewHolder.getView(R.id.txt_remind)
        smartEditBox = viewHolder.getView(R.id.smartEditBox)
    }


    /**
     * 对原有dialogOptions参数进行修改
     */
    open fun updateDialogOptions(dialogOptions: DialogOptions) {
        //如果没有设置dialogOptions.width参数值，则使用默认值左右margin
        if (dialogOptions.width == 0) {
            dialogOptions.width = requireContext().screenWidthPx - dpToPx(default_margin_left_right_dp) * 2
        }
    }

    open fun initView(viewHolder: ViewHolder) {
        findDialogBasicView(viewHolder)
        positiveTextView?.setOnClickListener {
            onPositiveEvent?.let {
                it.invoke(this@BaseActionSuperDialog)
                //dismissAllowingStateLoss()
                return@setOnClickListener
            }
        }
        negativeTextView?.setOnClickListener {
            onNegativeEvent?.invoke(this@BaseActionSuperDialog)
            //dismissAllowingStateLoss()
        }
        /****Header ImageView****/
        if (headUrl.isNullOrEmpty()){
            headerImageRes?.let {
                headerImageView?.setImageResource(it)
                headerImageView?.visibility = View.VISIBLE
            }
        }else{
            headerImageView?.let { Glide.with(application).load(headUrl).skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE).into(it) }
            headerImageView?.visibility = View.VISIBLE
        }
        if (animateIcon) {
            val pulseAnimation = AnimationUtils.loadAnimation(context, R.anim.pulse)
            headerImageView?.startAnimation(pulseAnimation)
        }
        /****Title****/
        titleName?.let {
            titleTextView?.text = it
            titleTextView?.visibility = View.VISIBLE
        }
        titleColor?.let {
            val colorStateList = ContextCompat.getColorStateList(requireContext(), it)
            titleTextView?.setTextColor(colorStateList)
        }

        titleSize?.let {
            val dimen: Float = resources.getDimensionPixelSize(it).toFloat()
            Log.i("AwesomeDialog", "titleSizeDimenRes floatTextSize:$dimen")
            titleTextView?.setTextSize(TypedValue.COMPLEX_UNIT_PX, dimen)
        }

        titleFontStyle?.let {
            titleTextView?.typeface = titleFontStyle
        }

        /****Sub Head Title****/
        contentText?.let {
            contentTextView?.text = it
            contentTextView?.visibility = View.VISIBLE
        }
        contentTextColor?.let {
            contentTextView?.setTextColor(ContextCompat.getColor(requireContext(), it))
        }
        contentTextSize?.let {
            val dimen: Float = resources.getDimensionPixelSize(it).toFloat()
            contentTextView?.setTextSize(TypedValue.COMPLEX_UNIT_PX, dimen)
        }
        contentTextFontStyle?.let {
            contentTextView?.typeface = contentTextFontStyle
        }
        /****Yes Button****/
        positiveTextName?.let { text ->
            positiveTextView?.text = text
            positiveTextView?.visibility = View.VISIBLE
            buttonPanel?.visibility = View.VISIBLE
        }

        positiveTextColor?.let {
            positiveTextView?.setTextColor(ContextCompat.getColor(requireContext(), it))
        }
        positiveTextSize?.let {
            val dimen: Float = resources.getDimensionPixelSize(it).toFloat()
            positiveTextView?.setTextSize(TypedValue.COMPLEX_UNIT_PX, dimen)
        }
        positiveTextFontStyle?.let {
            positiveTextView?.typeface = it
        }
        positiveBackground?.let {
            positiveTextView?.setBackgroundColor(it)
        }

        /****No Button****/
        negativeTextName?.let { text ->
            negativeTextView?.text = text
            negativeTextView?.visibility = View.VISIBLE
            buttonPanel?.visibility = View.VISIBLE
        }

        negativeTextColor?.let {
            negativeTextView?.setTextColor(ContextCompat.getColor(requireContext(), it))
        }
        negativeTextNoSize?.let {
            val dimen: Float = resources.getDimensionPixelSize(it).toFloat()
            negativeTextView?.setTextSize(TypedValue.COMPLEX_UNIT_PX, dimen)

        }
        negativeTextFontStyle?.let {
            negativeTextView?.typeface = contentTextFontStyle
        }

        negativeTextBackgroundResource?.let {
            negativeTextView?.setBackgroundResource(it)
        }

        checkbox_remind?.setOnCheckedChangeListener { checkBox, isChecked ->
            //Log.i(TAG, "isChecked = $isChecked")
            onRemindEvent?.invoke(isChecked)
        }

        root_remind?.setOnClickListener {
            checkbox_remind?.apply {
                isChecked = !isChecked
            }
        }

        if (showEditBox) {
            smartEditBox?.visibility = View.VISIBLE
            editBoxHint?.let {
                smartEditBox?.setHint(it)
            }
            editBoxDefaultText?.let {
                smartEditBox?.setText(it)
            }
        }

        root_remind?.apply {
            if (showRemind) {
                visible()
            } else {
                gone()
            }
        }

        onEditBoxTextChanged?.let { getEditBox().addOnTextChanged(it) }

    }

    abstract fun isDarkMode(isDarkMode: Boolean, dialogOptions: DialogOptions)

    override fun extendsOptions(): DialogOptions? {
        /*继承SuperDialog的时候一定要使用getDialogOptions()，用原有的进行修改*/
        return getDialogOptions().apply {
            layoutId = inflateLayout()
            updateDialogOptions(this)

            val isDarkMode = requireActivity().isDarkMode()
            isDarkMode(isDarkMode, this)

            //设置True防止内存泄漏
            //unLeak = true

            if (canAbleDismiss) {
                canClick = true
                touchCancel = true
                outCancel = true
            } else {
                canClick = false
                touchCancel = false
                outCancel = false
            }
            //使用扩展
            convertListenerFun { viewHolder, dialog ->
                initView(viewHolder)
            }
            //使用扩展
            onKeyListenerForOptions { keyCode, event ->
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    //点击返回键不Dismiss dialog
                    dismiss()
                    return@onKeyListenerForOptions true
                }
                return@onKeyListenerForOptions false
            }

            if (showEditBox && !unLeak) {
                Handler(Looper.getMainLooper()).postDelayed({
                    smartEditBox?.focusShowKeyboard()
                }, 200)
            }

            addShowDismissListener("SmartDialog") {
                onDialogShow {
                    Log.i("Dialog", "DialogShow -----------------")
                    onDialogShow?.invoke(this@BaseActionSuperDialog)
                    if (showKeyboard) {
                        Handler(Looper.getMainLooper()).postDelayed({
                            smartEditBox?.focusShowKeyboard()
                        }, 200)
                    }
                }
                onDialogDismiss {
                    Log.i("Dialog", "DialogDismiss -----------------")
                    onDialogDismiss?.invoke(this@BaseActionSuperDialog)
                    if (showKeyboard) {
                        smartEditBox?.hideSoftKeyboard()
                    }
                }
            }
        }
    }

    //Title 4个属性
    private var titleName: String? = null

    @ColorRes
    protected var titleColor: Int? = null

    @DimenRes
    protected var titleSize: Int? = null
    private var titleFontStyle: Typeface? = null

    //Sub Title 4个属性
    private var contentText: String? = null

    @ColorRes
    protected var contentTextColor: Int? = null

    @DimenRes
    protected var contentTextSize: Int? = null
    private var contentTextFontStyle: Typeface? = null

    //Positive 属性
    protected var positiveTextName: String? = null

    @ColorRes
    protected var positiveTextColor: Int? = null

    @DimenRes
    protected var positiveTextSize: Int? = null
    private var positiveTextFontStyle: Typeface? = null

    @DrawableRes
    protected var positiveBackground: Int? = null

    //Negative 属性
    protected var negativeTextName: String? = null
    private var negativeTextColor: Int? = null
    private var negativeTextNoSize: Int? = null
    private var negativeTextFontStyle: Typeface? = null
    private var negativeTextBackgroundResource: Int? = null

    //Dialog 显示删除 Callback
    protected var onDialogShow: OnDialogShow? = null
    protected var onDialogDismiss: OnDialogDismiss? = null

    fun setOnPositive(action: ((dialog: BaseActionSuperDialog) -> Unit)? = null): BaseActionSuperDialog {
        this.onPositiveEvent = action
        return this
    }

    fun setOnNegative(action: ((dialog: BaseActionSuperDialog) -> Unit)? = null): BaseActionSuperDialog {
        this.onNegativeEvent = action
        return this
    }

    fun setOnRemindEvent(action: ((icChecked: Boolean) -> Unit)? = null): BaseActionSuperDialog {
        this.onRemindEvent = action
        return this
    }

    /**
     * 监听Dialog显示
     */
    fun addShowOnDialogShow(onDialogShow: OnDialogShow): BaseActionSuperDialog {
        this.onDialogShow = onDialogShow
        return this
    }

    /**
     * 监听Dialog消失
     */
    fun addShowOnDialogDismiss(onDialogDismiss: OnDialogDismiss): BaseActionSuperDialog {
        this.onDialogDismiss = onDialogDismiss
        return this
    }

    fun setCanAbleDismiss(canAbleDismiss: Boolean): BaseActionSuperDialog {
        this.canAbleDismiss = canAbleDismiss
        return this
    }

    fun setDialogBackgroundColor(dialogBackgroundColor: Int): BaseActionSuperDialog {
        this.dialogBackgroundColor = dialogBackgroundColor
        return this
    }

    fun setDialogBackgroundRes(dialogBackgroundRes: Int): BaseActionSuperDialog {
        this.dialogBackgroundRes = dialogBackgroundRes
        return this
    }

    fun setHeadImage(@DrawableRes headImage: Int): BaseActionSuperDialog {
        this.headerImageRes = headImage
        return this
    }

    fun setHeadImageUrl( headImageUrl: String?): BaseActionSuperDialog {
        if (headImageUrl != null) {
            headUrl = headImageUrl
        }
        return this
    }
    fun setHeadAnimateIcon(animateIcon: Boolean): BaseActionSuperDialog {
        this.animateIcon = animateIcon
        return this
    }

    fun setTitle(title: String): BaseActionSuperDialog {
        this.titleName = title
        return this
    }

    fun setTitleSize(@DimenRes titleSizeDimenRes: Int): BaseActionSuperDialog {
        this.titleSize = titleSizeDimenRes
        return this
    }

    fun setTitleColor(titleColor: Int): BaseActionSuperDialog {
        this.titleColor = titleColor
        return this
    }

    fun setTitleTypeface(titleFontStyle: Typeface): BaseActionSuperDialog {
        this.titleFontStyle = titleFontStyle
        return this
    }

    fun setContentText(contentText: String): BaseActionSuperDialog {
        this.contentText = contentText
        return this
    }

    fun setContentTextSize(@DimenRes titleSize: Int): BaseActionSuperDialog {
        this.contentTextSize = titleSize
        return this
    }

    fun setContentTextColor(@ColorRes subHeadTitleColor: Int): BaseActionSuperDialog {
        this.contentTextColor = subHeadTitleColor
        return this
    }

    fun setContentTextTypeface(subHeadFontStyle: Typeface): BaseActionSuperDialog {
        this.contentTextFontStyle = subHeadFontStyle
        return this
    }

    fun setPositiveTextName(textYesButton: String): BaseActionSuperDialog {
        this.positiveTextName = textYesButton
        return this
    }

    fun setPositiveTextColor(@ColorRes textColor: Int): BaseActionSuperDialog {
        this.positiveTextColor = textColor
        return this
    }

    fun setPositiveTextSize(@DimenRes textSize: Int): BaseActionSuperDialog {
        this.positiveTextSize = textSize
        return this
    }

    fun setPositiveTextFontStyle(typeface: Typeface): BaseActionSuperDialog {
        this.positiveTextFontStyle = typeface
        return this
    }

    fun setPositiveBackground(@DrawableRes background: Int): BaseActionSuperDialog {
        this.positiveBackground = background
        return this
    }

    fun setNegativeTextName(textNoButton: String): BaseActionSuperDialog {
        this.negativeTextName = textNoButton
        return this
    }

    fun setNegativeTextColor(@ColorRes textColor: Int): BaseActionSuperDialog {
        this.negativeTextColor = textColor
        return this
    }

    fun setNegativeTextSize(@DimenRes textSize: Int): BaseActionSuperDialog {
        this.negativeTextNoSize = textSize
        return this
    }

    fun setNegativeTextFontStyle(typeface: Typeface): BaseActionSuperDialog {
        this.negativeTextFontStyle = typeface
        return this
    }

    fun setNegativeBackgroundResource(@DrawableRes backGroundResource: Int): BaseActionSuperDialog {
        this.negativeTextBackgroundResource = backGroundResource
        return this
    }

    fun showEditBox(show: Boolean, showKeyboard: Boolean = true, hint: String? = null, defaultText: String? = null): BaseActionSuperDialog {
        this.showEditBox = show
        this.editBoxHint = hint
        this.showKeyboard = showKeyboard
        this.editBoxDefaultText = defaultText

        if (showEditBox) {
            //getDialogOptions().unLeak = true
        }
        return this
    }

    fun showRemind(showRemind: Boolean): BaseActionSuperDialog {
        this.showRemind = showRemind
        return this
    }

    /**
     * 通过获取SmartEditBox进行设置
     */
    fun getEditBox(): SmartEditBox {
        if (smartEditBox == null) {
            throw RuntimeException("Please confirm your layout have smart edit box and after shown involk")
        }
        return smartEditBox!!
    }

    fun addEditBoxInputChangeListener(onEditBoxTextChanged: (smartEditBox: SmartEditBox, textChanged: String) -> Unit): BaseActionSuperDialog {
        this.onEditBoxTextChanged = onEditBoxTextChanged
        return this
    }

    /**
     * 获取输入的Edit content
     */
    fun getEditBoxContent(): String? {
        return smartEditBox?.getText()
    }

    fun setPositive(
        name: String,
        @DimenRes textSize: Int,
        @DrawableRes background: Int,
        typeface: Typeface,
        action: ((dialog: BaseActionSuperDialog) -> Unit)? = null
    ): BaseActionSuperDialog {
        this.positiveTextName = name
        this.onPositiveEvent = action
        this.positiveBackground = background
        this.positiveTextSize = textSize
        this.positiveTextFontStyle = typeface
        return this
    }

    fun setOnNegative(
        name: String,
        @DimenRes textSize: Int,
        @DrawableRes background: Int,
        typeface: Typeface,
        action: ((dialog: BaseActionSuperDialog) -> Unit)? = null
    ): BaseActionSuperDialog {
        this.negativeTextName = name
        this.onNegativeEvent = action
        this.negativeTextBackgroundResource = background
        this.negativeTextNoSize = textSize
        this.negativeTextFontStyle = typeface
        return this
    }

    fun openAutoDismiss(openAutoDismiss: Boolean, autoDelayMillisDismiss: Long): BaseActionSuperDialog {
        this.openAutoDismiss = openAutoDismiss
        this.autoDelayMillisDismiss = autoDelayMillisDismiss
        if (openAutoDismiss && autoDelayMillisDismiss > 100) {
            android.os.Handler(Looper.getMainLooper()).postDelayed({
                dismissAllowingStateLoss()
            }, autoDelayMillisDismiss)
        }
        return this
    }

    /**
     * 显示弹出框
     */
    fun show(
        gravity: DialogGravity = DialogGravity.CENTER_CENTER,
        @StyleRes newAnim: Int = R.style.AlphaEnterExitAnimation,
    ) {
        manager?.let {
            showOnWindow(it, gravity, newAnim)
            if (!getDialogOptions().unLeak) {
                onDialogShow?.invoke(this@BaseActionSuperDialog)
            }
        }
    }

    fun showOnView(
        view: View,
        gravityAsView: DialogGravity = DialogGravity.CENTER_BOTTOM,
        @StyleRes newAnim: Int = R.style.AlphaEnterExitAnimation,
        needOffsetY: Boolean = true,
        offsetYAsView: Int = 0,
        offsetXAsView: Int = 0
    ) {
        manager?.let {
            val statusBarHeight = getStatusBarHeight()
            if (needOffsetY) {
                //预留5dp的空隙
                val offsetY = -(statusBarHeight - dpToPx(offsetYAsView))
                showOnView(it, view, gravityAsView, newAnim, offsetXAsView, offsetY)
            } else {
                showOnView(view, gravityAsView, newAnim)
            }
            if (!getDialogOptions().unLeak) {
                onDialogShow?.invoke(this@BaseActionSuperDialog)
            }
        }
    }

    /**
     * 显示弹出框
     */
    fun showOnView(
        view: View,
        gravityAsView: DialogGravity = DialogGravity.CENTER_BOTTOM,
        @StyleRes newAnim: Int = R.style.AlphaEnterExitAnimation,
    ) {
        manager?.let {
            showOnView(it, view, gravityAsView, newAnim)

            if (!getDialogOptions().unLeak) {
                onDialogShow?.invoke(this@BaseActionSuperDialog)
            }
        }
    }

    fun addLifeCycleOwner(owner: LifecycleOwner) {
        owner.lifecycle.addObserver(object : LifecycleObserver {
            @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
            fun onDestroy() {
                dismiss()
            }
        })
    }
}