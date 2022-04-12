package com.apemans.quickui.editbox

import android.content.Context
import android.graphics.Color
import android.os.Build
import android.text.Editable
import android.text.InputType
import android.text.TextUtils
import android.text.TextWatcher
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.AttributeSet
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.animation.Animation
import android.view.animation.CycleInterpolator
import android.view.animation.TranslateAnimation
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.widget.AppCompatEditText
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.apemans.quickui.R
import java.lang.Exception

/***********************************************************
 * 作者: caro
 * 邮箱: 1025807062@qq.com
 * 日期: 2021/10/22 10:16
 * 说明:
 *
 * 备注:
 * 如果EditText在自定义控件中包含，并且同时设置其他属性的时候，需要注意顺序问题，
 * setInputType需要放在setSingleLine后面，不知道算不算android的一个Bug。
 *
 ***********************************************************/

class SmartEditBox @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0,
) : ConstraintLayout(context, attrs, defStyleAttr), TextWatcher {
    private var imgLeft: ImageView
    private var editText: AppCompatEditText
    private var rightBtnGroup: LinearLayout
    private var imgVisible: ImageView
    private var imgClear: ImageView
    private var imgArrowDown: ImageView
    private var editRoot: ConstraintLayout
    private var txtTitle: TextView
    private var txtTips: TextView
    private var txtRight: TextView

    private var focusEditBackground: Int = R.drawable.edit_focus_background
    private var normalEditBackground: Int = R.drawable.edit_normal_background
    private var errorEditBackground: Int = R.drawable.edit_error_background
    private var iconEyeOnDrawable: Int = R.drawable.ic_login_eye_on
    private var iconEyeOffDrawable: Int = R.drawable.ic_login_eye_off
    private var cursorDrawable: Int? = null //Int = R.drawable.ic_cursor

    /*记录当前是否是Error提示错误状态*/
    private var errorTipsStatus = false

    /**
     * 文字大小
     */
    private var textSize: Float = 15f
    private var textColor: Int = Color.BLACK
    private var hintColor: Int = Color.GRAY

    private var isPasswordType: Boolean = false

    private var contentLines = -1

    //密码默认不可见
    private var showPasswordVisible = false

    private var onTextChanged: ((smartEditBox: SmartEditBox, textChanged: String) -> Unit)? = null
    private var onArrowClick: ((arrowView: View, smartEditBox: SmartEditBox) -> Unit)? = null
    private var onRightTextClick: ((rightView: View, smartEditBox: SmartEditBox) -> Unit)? = null
    private var onFocusChangeView: ((onFocusChangeView: View, smartEditBox: SmartEditBox) -> Unit)? = null

    private var button_gravity: Int = Gravity.CENTER


    init {
        val rootView = LayoutInflater.from(context).inflate(R.layout.layout_edit_box, this, true)
        editRoot = rootView.findViewById(R.id.edit_root)
        imgLeft = rootView.findViewById(R.id.img_left)
        editText = rootView.findViewById(R.id.edit_text)
        rightBtnGroup = rootView.findViewById(R.id.right_btn_group)
        imgVisible = rootView.findViewById(R.id.img_visible)
        imgClear = rootView.findViewById(R.id.img_clear)
        imgArrowDown = rootView.findViewById(R.id.img_arrow_down)
        txtTips = rootView.findViewById(R.id.txt_tips)
        txtTitle = rootView.findViewById(R.id.txt_title)
        txtRight = rootView.findViewById(R.id.right_text)
        if (attrs != null) {
            val attr = context.obtainStyledAttributes(attrs, R.styleable.SmartEditBox)

            if (attr.hasValue(R.styleable.SmartEditBox_focus_edit_background)) {
                focusEditBackground = attr.getResourceId(R.styleable.SmartEditBox_focus_edit_background, 0)
            }
            if (attr.hasValue(R.styleable.SmartEditBox_normal_edit_background)) {
                normalEditBackground = attr.getResourceId(R.styleable.SmartEditBox_normal_edit_background, R.drawable.edit_normal_background)
            }
            if (attr.hasValue(R.styleable.SmartEditBox_error_edit_background)) {
                errorEditBackground = attr.getResourceId(R.styleable.SmartEditBox_error_edit_background, R.drawable.edit_error_background)
            }

            if (attr.hasValue(R.styleable.SmartEditBox_textsize)) {
                textSize = attr.getDimensionPixelSize(R.styleable.SmartEditBox_textsize, 15).toFloat()
            }

            if (attr.hasValue(R.styleable.SmartEditBox_textcolor)) {
                textColor = attr.getColor(R.styleable.SmartEditBox_textcolor, Color.BLACK)
            }

            if (attr.hasValue(R.styleable.SmartEditBox_titleColor)) {
                val titleColor = attr.getColor(R.styleable.SmartEditBox_titleColor, Color.BLACK)
                txtTitle.setTextColor(titleColor)
            }
            if (attr.hasValue(R.styleable.SmartEditBox_title)) {
                val title = attr.getString(R.styleable.SmartEditBox_title)
                txtTitle.text = title
                txtTitle.visibility = View.VISIBLE
            }
            if (attr.hasValue(R.styleable.SmartEditBox_titleSize)) {
                val titleSize = attr.getDimensionPixelSize(R.styleable.SmartEditBox_titleSize, 15).toFloat()
                txtTitle.textSize = titleSize
            }

            if (attr.hasValue(R.styleable.SmartEditBox_hintcolor)) {
                hintColor = attr.getColor(R.styleable.SmartEditBox_hintcolor, Color.GRAY)
            }

            if (attr.hasValue(R.styleable.SmartEditBox_hint)) {
                val hint = attr.getString(R.styleable.SmartEditBox_hint)
                editText.hint = hint
            }

            if (attr.hasValue(R.styleable.SmartEditBox_text)) {
                val text = attr.getString(R.styleable.SmartEditBox_text)
                text?.apply {
                    editText.setText(this)
                }
            }

            if (attr.hasValue(R.styleable.SmartEditBox_clear_icon)) {
                val iconClearDrawable = attr.getDrawable(R.styleable.SmartEditBox_clear_icon)
                if (iconClearDrawable != null) {
                    imgClear.visibility = View.VISIBLE
                    imgClear.setImageDrawable(iconClearDrawable)
                } else {
                    imgClear.visibility = View.GONE
                }
            }
            if (attr.hasValue(R.styleable.SmartEditBox_arrow_icon)) {
                val iconArrowDrawable = attr.getDrawable(R.styleable.SmartEditBox_arrow_icon)
                if (iconArrowDrawable != null) {
                    imgArrowDown.visibility = View.VISIBLE
                    imgArrowDown.setImageDrawable(iconArrowDrawable)
                } else {
                    imgArrowDown.visibility = View.GONE
                }
            }

            if (attr.hasValue(R.styleable.SmartEditBox_eye_on_icon)) {
                iconEyeOnDrawable = attr.getResourceId(R.styleable.SmartEditBox_eye_on_icon, -1)
            }
            if (attr.hasValue(R.styleable.SmartEditBox_eye_off_icon)) {
                iconEyeOffDrawable = attr.getResourceId(R.styleable.SmartEditBox_eye_off_icon, -1)
            }

            if (attr.hasValue(R.styleable.SmartEditBox_android_singleLine)) {
                editText.isSingleLine = attr.getBoolean(R.styleable.SmartEditBox_android_singleLine, true)
            }
            if (attr.hasValue(R.styleable.SmartEditBox_android_gravity)) {
                editText.gravity = attr.getInt(R.styleable.SmartEditBox_android_gravity, -1)
            }

            if (attr.hasValue(R.styleable.SmartEditBox_android_inputType)) {
                val inputType = attr.getInt(R.styleable.SmartEditBox_android_inputType, EditorInfo.TYPE_NULL)
                editText.inputType = inputType

                val isPasswordInputType = isAnyPasswordInputType(inputType)
                Log.i("SmartEditBox", "inputType = $inputType isPasswordInputType=$isPasswordInputType")
                if (isPasswordInputType) {
                    isPasswordType = true
                    imgVisible.setImageResource(iconEyeOffDrawable)
                    imgVisible.visibility = View.VISIBLE
                    //editText.transformationMethod = HideReturnsTransformationMethod.getInstance()
                    editText.transformationMethod = PasswordTransformationMethod.getInstance()
                    editText.text?.length?.let { editText.setSelection(it) }

                }

            }
            if (attr.hasValue(R.styleable.SmartEditBox_right_text_color)) {
                val rigthTextColor = attr.getColor(R.styleable.SmartEditBox_right_text_color, Color.BLACK)
                txtRight.setTextColor(rigthTextColor)
            }
            if (attr.hasValue(R.styleable.SmartEditBox_right_text)) {
                val value = attr.getString(R.styleable.SmartEditBox_right_text)
                txtRight.visibility = View.VISIBLE
                txtRight.text = value
            }
            if (attr.hasValue(R.styleable.SmartEditBox_android_maxLines)) {
                editText.maxLines = attr.getInt(R.styleable.SmartEditBox_android_maxLines, -1)
            }
            if (attr.hasValue(R.styleable.SmartEditBox_android_lines)) {
                contentLines = attr.getInt(R.styleable.SmartEditBox_android_lines, -1)
                editText.setLines(contentLines)
            }

            if (attr.hasValue(R.styleable.SmartEditBox_cursor_drawable)) {
                cursorDrawable = attr.getResourceId(R.styleable.SmartEditBox_cursor_drawable, R.drawable.ic_cursor)
            }
            if (attr.hasValue(R.styleable.SmartEditBox_theme_icon)) {
                val themeIcon = attr.getResourceId(R.styleable.SmartEditBox_theme_icon, R.drawable.email)
                imgLeft.visibility = View.VISIBLE
                imgLeft.setImageResource(themeIcon)
            }

            if (attr.hasValue(R.styleable.SmartEditBox_button_gravity)) {
                val gravity = attr.getInt(R.styleable.SmartEditBox_button_gravity, 0)
                if (gravity == 0) {
                    button_gravity = Gravity.CENTER
                }
                if (gravity == 1) {
                    button_gravity = Gravity.TOP
                }
                if (gravity == 2) {
                    button_gravity = Gravity.BOTTOM
                }
            }
            //recycle
            attr.recycle()
        }

        editRoot.setBackgroundResource(normalEditBackground)
        editText.setHintTextColor(hintColor)
        editText.setTextColor(textColor)
        editText.textSize = textSize
        //设置光标
        cursorDrawable?.let {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                editText.setTextCursorDrawable(it)
            } else {
                try {
                    val f = this::class.java.getDeclaredField("mCursorDrawableRes")
                    f.isAccessible = true
                    f[editText] = it
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }

        imgVisible.setOnClickListener {
            showPasswordVisible = if (showPasswordVisible) {
                hidePassword(true)
                false
            } else {
                hidePassword(false)
                true
            }
        }

        imgClear.setOnClickListener {
            editText.setText("")
        }
        imgArrowDown.setOnClickListener {
            onArrowClick?.invoke(it, this)
        }
        txtRight.setOnClickListener {
            onRightTextClick?.invoke(it,this)
        }

        editText.addTextChangedListener(this)
        editText.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                onFocusChangeView?.invoke(editText,this)
                editRoot.setBackgroundResource(focusEditBackground)
            } else {
                editRoot.setBackgroundResource(normalEditBackground)
            }
        }

    }


    private fun showButtonGravity(view: View, gravity: Int = Gravity.CENTER) {
        val layoutParams: LinearLayout.LayoutParams = view.layoutParams as LinearLayout.LayoutParams
        layoutParams.gravity = gravity
        view.layoutParams = layoutParams
    }

    private fun isPassword(isPassword: Boolean) {
        if (isPassword) {
            imgVisible.setImageResource(iconEyeOffDrawable)

        } else {
            imgVisible.setImageResource(iconEyeOnDrawable)
        }
        imgVisible.visibility = View.VISIBLE
    }

    private fun hidePassword(hide: Boolean) {
        if (hide) {
            editText.transformationMethod = PasswordTransformationMethod.getInstance()
        } else {
            editText.transformationMethod = HideReturnsTransformationMethod.getInstance()
        }
        editText.text?.length?.let { editText.setSelection(it) }

        if (hide) {
            imgVisible.setImageResource(iconEyeOffDrawable)

        } else {
            imgVisible.setImageResource(iconEyeOnDrawable)
        }
        imgVisible.visibility = View.VISIBLE
    }


    fun addOnTextChanged(onTextChanged: (smartEditBox: SmartEditBox, textChanged: String) -> Unit) {
        this.onTextChanged = onTextChanged
    }


    fun addOnArrowClick(onArrowClick: ((arrowView: View, smartEditBox: SmartEditBox) -> Unit)? = null) {
        this.onArrowClick = onArrowClick
    }

    fun addOnRightTextClick(onRightTextClick: ((rightView: View, smartEditBox: SmartEditBox) -> Unit)? = null) {
        this.onRightTextClick = onRightTextClick
    }

    fun addSetOnFocusChangeListener(onFocusChange: ((onFocusChangeView: View, smartEditBox: SmartEditBox) -> Unit)? = null) {
        this.onFocusChangeView = onFocusChange
    }

    fun showTips(tips: String) {
        txtTips.visibility = View.VISIBLE
        txtTips.text = tips
    }

    fun setHint(hint: String) {
        editText.hint = hint
    }

    fun setText(text: String) {
        editText.setText(text)
    }

    fun getText(): String {
        return editText.text.toString()
    }

    fun getEditText(): EditText {
        return editText
    }

    fun setTitle(title: String) {
        txtTitle.text = title
    }

    fun setRightText(rightText: String) {
        txtRight.text = rightText
    }

    private fun isAnyPasswordInputType(inputType: Int): Boolean {
        //val inputType: Int = editText.inputType
        return isPasswordInputType(inputType) || isVisiblePasswordInputType(inputType)
    }

    private fun isPasswordInputType(inputType: Int): Boolean {
        val variation = inputType and (EditorInfo.TYPE_MASK_CLASS or EditorInfo.TYPE_MASK_VARIATION)
        return (variation
                == EditorInfo.TYPE_CLASS_TEXT or EditorInfo.TYPE_TEXT_VARIATION_PASSWORD) || (variation
                == EditorInfo.TYPE_CLASS_TEXT or EditorInfo.TYPE_TEXT_VARIATION_WEB_PASSWORD) || (variation
                == EditorInfo.TYPE_CLASS_NUMBER or EditorInfo.TYPE_NUMBER_VARIATION_PASSWORD)
    }

    private fun isVisiblePasswordInputType(inputType: Int): Boolean {
        val variation = inputType and (EditorInfo.TYPE_MASK_CLASS or EditorInfo.TYPE_MASK_VARIATION)
        return (variation
                == EditorInfo.TYPE_CLASS_TEXT or EditorInfo.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD)
    }


    override fun beforeTextChanged(p0: CharSequence?, start: Int, count: Int, after: Int) {
        if (errorTipsStatus) {
            editRoot.setBackgroundResource(focusEditBackground)
            errorTipsStatus = false
        }
    }

    override fun onTextChanged(p0: CharSequence?, start: Int, before: Int, count: Int) {
        p0 ?: return
        val content = p0.toString()

        imgClear.visibility = if (TextUtils.isEmpty(content)) {
            View.GONE
        } else {
            View.VISIBLE
        }
        if (isPasswordType) {
            imgVisible.visibility = if (TextUtils.isEmpty(content)) {
                View.GONE
            } else {
                View.VISIBLE
            }
        }
        txtTips.visibility = View.GONE
        txtTips.text = ""
        onTextChanged?.invoke(this, content)
    }

    override fun afterTextChanged(p0: Editable?) {
        val lineCount = editText.lineCount
        Log.i("SmartEdit", "lineCount = $lineCount  contentLines = $contentLines")

        if (contentLines > 2) {
            showButtonGravity(imgClear, button_gravity)
            showButtonGravity(imgVisible, button_gravity)
            showButtonGravity(imgArrowDown, button_gravity)
            return
        }

        if (lineCount > 1) {
            showButtonGravity(imgClear, button_gravity)
            showButtonGravity(imgVisible, button_gravity)
            showButtonGravity(imgArrowDown, button_gravity)
        } else {
            //单行默认居中
            showButtonGravity(imgClear, Gravity.CENTER)
            showButtonGravity(imgVisible, Gravity.CENTER)
            showButtonGravity(imgArrowDown, Gravity.CENTER)
        }
    }

    /**
     * 显示错误提示
     */
    fun showError(errorTips: String, shake: Boolean = true): SmartEditBox {
        showTips(errorTips)
        if (shake) {
            startShakeAnimation()
        }
        editRoot.setBackgroundResource(errorEditBackground)
        errorTipsStatus = true
        return this
    }


    /**
     * 开始晃动
     * @param counts 晃动次数
     */
    fun startShakeAnimation(counts: Int = 4) {
        if (animation == null) {
            animation = shakeAnimation(counts)
        }
        startAnimation(animation)
    }

    /**
     * 晃动动画
     *
     * @param counts 0.5秒钟晃动多少下
     * @return
     */
    private fun shakeAnimation(counts: Int): Animation {
        val translateAnimation: Animation = TranslateAnimation(0f, 10f, 0f, 0f)
        translateAnimation.interpolator = CycleInterpolator(counts.toFloat())
        translateAnimation.duration = 500
        return translateAnimation
    }

    /**
     * 显示键盘
     */
    fun focusShowKeyboard() {
        editText.requestFocus()
        editText.isFocusableInTouchMode = true
        editText.showKeyboard()
    }

    /**
     * 隐藏键盘
     */
    fun hideSoftKeyboard() {
        editText.hideKeyboard()
    }

    private fun EditText.showKeyboard(delayMillis: Long = 200) =
        postDelayed({
            windowInsetsControllerCompat?.show(WindowInsetsCompat.Type.ime())
        }, delayMillis)

    private fun EditText.hideKeyboard() = windowInsetsControllerCompat?.hide(WindowInsetsCompat.Type.ime())

    private inline val View.windowInsetsControllerCompat get() = ViewCompat.getWindowInsetsController(this)
}




