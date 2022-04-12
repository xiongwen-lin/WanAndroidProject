package com.apemans.usercomponent.user.spinner

import android.content.Context
import android.content.res.Resources
import android.content.res.TypedArray
import android.graphics.drawable.Drawable
import android.text.*
import android.text.util.Linkify
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.AutoCompleteTextView
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.apemans.usercomponent.R

/**
 * InputFrameView
 *
 * @author Administrator
 * @date 2019/3/14
 */
class InputFrameView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) :
    LinearLayout(context, attrs) {
    private var INPUT_THEME_2_FLAG = 0
    var tvInputTitle: TextView? = null
    var etInput : AutoCompleteTextView? = null
    var ivInputBtn: ImageView? = null
    var tvInputBtn: TextView? = null
    private val mThemeType: Int
    var mListener: OnInputFrameClickListener? = null
    private var mTextWatcher: TextWatcher? = null
    private var mEtInputFocusChangeListener: OnFocusChangeListener? = null
    private var mIsOpenInputToggle = false
    private var mIsShowBtn = true
    private var mInputBtnType = INPUT_BTN_TYPE_ICON
    private var titleColor = 0
    private fun init(context: Context) {
        titleColor = R.color.theme_white
        val view: View = LayoutInflater.from(context).inflate(layoutId, this, false)
        tvInputTitle = view.findViewById<View>(R.id.tvInputTitle) as TextView
        etInput = view.findViewById<View>(R.id.etInput) as AutoCompleteTextView
        ivInputBtn = view.findViewById<View>(R.id.btnInput) as ImageView
        tvInputBtn = view.findViewById<View>(R.id.btnTxtInput) as TextView
        ivInputBtn!!.setImageResource(mEyeCloseResId)
        tvInputBtn!!.text = ""
        displayInputBtn()
        etInput!!.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                if (mTextWatcher != null) {
                    mTextWatcher!!.beforeTextChanged(s, start, count, after)
                }
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                displayInputBtn()
                if (mTextWatcher != null) {
                    mTextWatcher!!.onTextChanged(s, start, before, count)
                }
            }

            override fun afterTextChanged(s: Editable) {
                if (mTextWatcher != null) {
                    mTextWatcher!!.afterTextChanged(s)
                }
            }
        })
        etInput!!.setOnFocusChangeListener { view, hasFocus ->
            if (hasFocus) {
                tvInputTitle!!.setTextColor(resources.getColor(titleColor))
            } else {
                tvInputTitle!!.setTextColor(resources.getColor(titleColor))
            }
            if (mEtInputFocusChangeListener != null) {
                mEtInputFocusChangeListener!!.onFocusChange(view, hasFocus)
            }
        }
        ivInputBtn!!.setOnClickListener {
            togglePwInputBtn()
            if (mListener != null) {
                mListener!!.onInputBtnClick()
            }
        }
        tvInputBtn!!.setOnClickListener(View.OnClickListener {
            if (mListener != null) {
                mListener!!.onInputBtnClick()
            }
        })
        etInput?.setOnEditorActionListener(object : TextView.OnEditorActionListener {
            override fun onEditorAction(textView: TextView, i: Int, keyEvent: KeyEvent?): Boolean {
                if (i == EditorInfo.IME_ACTION_GO) {
                    if (mListener != null) {
                        mListener!!.onEditorAction()
                    }
                    return true
                }
                return false
            }
        })
        etInput?.setOnClickListener(View.OnClickListener {
            if (mListener != null) {
                mListener!!.onEtInputClick()
            }
        })
        //etInput.setFilters(new InputFilter[]{new InputFilter.LengthFilter(ConstantValue.MAX_NAME_LENGTH)});
        addView(view)
    }

    private var mEyeOpenResId = 0
    private var mEyeCloseResId = 0
    private fun initAttr() {
        when (mThemeType) {
            INPUT_THEME_1 -> {
                mEyeOpenResId = R.drawable.eye_open_icon_state_list
                mEyeCloseResId = R.drawable.eye_close_icon_state_list
            }
            INPUT_THEME_2 -> {
                mEyeOpenResId = R.drawable.eye_open_icon_state_list
                mEyeCloseResId = R.drawable.eye_close_icon_state_list
            }
            else -> {
                mEyeOpenResId = R.drawable.eye_open_icon_white_state_list
                mEyeCloseResId = R.drawable.eye_close_icon_white_state_list
            }
        }
    }

    private val layoutId: Int
        get() {
            var layoutId: Int = R.layout.user_layout_input_frame
            when (mThemeType) {
                INPUT_THEME_1 -> layoutId = R.layout.user_layout_input_frame_1
                INPUT_THEME_2 -> layoutId = R.layout.user_layout_input_frame2
            }
            return layoutId
        }

    fun setHintTexe(text: String?): InputFrameView {
        with(etInput) { this?.setHint(text) }
        return this
    }

    fun setBtnInputView(isShow: Boolean): InputFrameView {
        if (isShow) {
            INPUT_THEME_2_FLAG = INPUT_THEME_2
            etInput?.textAlignment = View.TEXT_ALIGNMENT_TEXT_START
            ivInputBtn!!.visibility = View.VISIBLE
            etInput?.inputType = getPwInputType(etPwInputType, false)
        }
        return this
    }

    fun setPPOEPasswordTextAlign(): InputFrameView {
        etInput?.textAlignment = View.TEXT_ALIGNMENT_TEXT_END
        return this
    }

    @JvmName("getEtInput1")
    fun getEtInput(): AutoCompleteTextView? {
        return etInput
    }

    val inputText: String
        get() = if (etInput != null) etInput!!.text.toString().trim { it <= ' ' } else ""
    val inputTextNoTrim: String
        get() = if (etInput != null) etInput!!.text.toString() else ""

    fun setInputTitle(title: String?): InputFrameView {
        if (tvInputTitle != null) {
            tvInputTitle!!.text = title
        }
        return this
    }

    fun setInputTitleVisible(visible: Int): InputFrameView {
        if (tvInputTitle != null) {
            tvInputTitle!!.visibility = visible
        }
        return this
    }

    fun setEtInputText(text: String?) {
        if (etInput != null) {
            etInput!!.setText(text)
        }
    }

    fun setEtSelection(position: Int) {
        if (etInput != null) {
            etInput!!.setSelection(position)
        }
    }

    fun setEtInputHint(hint: String?): InputFrameView {
        if (etInput != null) {
            etInput!!.hint = hint
        }
        return this
    }

    private var etPwInputType = InputType.TYPE_CLASS_TEXT
    fun setEtPwInputType(type: Int): InputFrameView {
        etPwInputType = type
        return this
    }

    fun setEtInputType(type: Int): InputFrameView {
        if (etInput != null) {
            etInput!!.inputType = type
            if (isPasswordInputType(type)) {
                ivInputBtn!!.setImageResource(mEyeCloseResId)
            } else {
                ivInputBtn!!.setImageResource(mEyeOpenResId)
            }
        }
        return this
    }

    fun setTextAlign(textAlign: Int): InputFrameView {
        if (etInput != null) {
            etInput!!.textAlignment = textAlign
        }
        return this
    }

    fun setEtInputGravity(gravity: Int): InputFrameView {
        if (gravity == Gravity.CENTER) {
            etInput?.setPadding(dp2px(30f), 0, dp2px(30f), dp2px(20f))
            ivInputBtn!!.setPaddingRelative(0, 0, 0, dp2px(20f))
        }
        if (etInput != null) {
            etInput!!.gravity = gravity
        }
        return this
    }

    fun setEtInputMaxLength(maxLength: Int): InputFrameView {
        if (etInput != null) {
            etInput!!.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(maxLength))
        }
        return this
    }

    fun setInputBtn(resId: Int): InputFrameView {
        if (ivInputBtn != null) {
            ivInputBtn!!.setImageResource(resId)
        }
        return this
    }

    fun setTextInputBtn(text: String?): InputFrameView {
        if (tvInputBtn != null) {
            tvInputBtn!!.text = text
        }
        return this
    }

    fun setTextInputBtnColor(color: Int): InputFrameView {
        if (tvInputBtn != null) {
            tvInputBtn!!.setTextColor(resources.getColor(color))
        }
        return this
    }

    fun setTextInputBtnBg(resId: Int): InputFrameView {
        if (tvInputBtn != null) {
            tvInputBtn!!.setBackgroundResource(resId)
        }
        return this
    }

    fun setTextInputBtnBg(drawable: Drawable?): InputFrameView {
        if (tvInputBtn != null) {
            tvInputBtn!!.background = drawable
        }
        return this
    }

    fun setInputBtnType(type: Int): InputFrameView {
        mInputBtnType = type
        return this
    }

    fun setInputBtnIsShow(isshow: Boolean): InputFrameView {
        mIsShowBtn = isshow
        displayInputBtn()
        return this
    }

    fun setEtInputToggle(open: Boolean): InputFrameView {
        mIsOpenInputToggle = open
        return this
    }

    fun setCursorVisilbe(visilbe: Boolean): InputFrameView {
        if (etInput != null) {
            etInput!!.isCursorVisible = visilbe
        }
        return this
    }

    fun setIpvFocusable(focusable: Boolean): InputFrameView {
        if (etInput != null) {
            etInput!!.isFocusable = focusable
            etInput!!.isFocusableInTouchMode = focusable
            if (focusable) {
                etInput!!.requestFocus()
            }
        }
        return this
    }

    fun setIpvEnable(enable: Boolean): InputFrameView {
        if (etInput != null) {
            etInput!!.isEnabled = enable
        }
        return this
    }

    fun setIpvAutoLink(auto: Boolean): InputFrameView {
        if (etInput != null) {
            etInput!!.autoLinkMask = if (auto) Linkify.ALL else 0
        }
        return this
    }

    private fun togglePwInputBtn() {
        if (mIsOpenInputToggle && etInput != null && ivInputBtn != null) {
            if (!isPasswordInputType(etInput!!.inputType)) {
                etInput!!.inputType = getPwInputType(etPwInputType, false)
                ivInputBtn!!.setImageResource(mEyeCloseResId)
            } else {
                etInput!!.inputType = getPwInputType(etPwInputType, true)
                ivInputBtn!!.setImageResource(mEyeOpenResId)
            }
            etInput!!.setSelection(etInput!!.text.toString().trim { it <= ' ' }.length)
        }
    }

    private fun displayInputBtn() {
        if (!mIsShowBtn) {
            ivInputBtn!!.visibility = View.GONE
            tvInputBtn?.visibility = View.GONE
            return
        }
        when (mInputBtnType) {
            INPUT_BTN_TYPE_ICON -> if (mIsShowBtn) {
                tvInputBtn?.visibility = View.GONE
                if (mThemeType != INPUT_THEME_2) {
                    ivInputBtn!!.visibility = if (TextUtils.isEmpty(
                            etInput?.text.toString().trim { it <= ' ' })
                    ) View.GONE else View.VISIBLE
                }
                if (INPUT_THEME_2_FLAG == INPUT_THEME_2) {
                    /*ivInputBtn!!.visibility =
                        if (TextUtils.isEmpty(etInput?.getText().toString().trim { it <= ' ' }))
                            View.GONE else View.VISIBLE*/
                    ivInputBtn!!.visibility = View.VISIBLE
                }
            }
            INPUT_BTN_TYPE_TEXT -> if (mIsShowBtn) {
                ivInputBtn!!.visibility = View.GONE
                tvInputBtn?.visibility = View.VISIBLE
            }
            else -> {
                ivInputBtn!!.visibility = View.GONE
                tvInputBtn?.visibility = View.GONE
            }
        }
    }

    fun setInputTextChangeListener(textWatcher: TextWatcher?): InputFrameView {
        mTextWatcher = textWatcher
        return this
    }

    fun setOnClickListener(listener: OnInputFrameClickListener?): InputFrameView {
        mListener = listener
        return this
    }

    fun setEtInputFocusChangeListener(listener: OnFocusChangeListener?): InputFrameView {
        mEtInputFocusChangeListener = listener
        return this
    }

    fun setTheme(themeType: Int): InputFrameView {
        setTheme(R.color.black_010C11, R.color.black_010C11, themeType)
        return this
    }

    fun setTheme(themeType: Int, etInputColor: Int): InputFrameView {
        setTheme(R.color.black_010C11, etInputColor, themeType)
        return this
    }

    fun setTheme(titleColor: Int, textColor: Int, themeType: Int) {
        when (themeType) {
            INPUT_FRAME_THEME_TYPE_TEXT_CENTER -> {
                if (etInput != null) {
                    etInput!!.setPadding(
                        etInput!!.paddingLeft,
                        etInput!!.paddingTop,
                        dp2px(4f),
                        etInput!!.paddingBottom
                    )
                }
            }
        }
        this.titleColor = titleColor
        if (tvInputTitle != null) {
            tvInputTitle!!.setTextColor(resources.getColor(this.titleColor))
        }
        if (etInput != null) {
            etInput!!.context.setTheme(getInputThemeStyleByType(themeType))
            etInput!!.setTextColor(resources.getColor(textColor))
        }
    }

    private fun getInputThemeStyleByType(themeType: Int): Int {
        var themeStyle: Int = R.style.VictureAutocomplete
        when (themeType) {
            INPUT_FRAME_THEME_TYPE_TEXT_CENTER -> themeStyle = R.style.VictureInputFrameTextCenter
        }
        return themeStyle
    }

    fun release() {
        tvInputTitle = null
        if (etInput != null) {
            etInput!!.addTextChangedListener(null)
            etInput!!.setOnClickListener(null)
            etInput!!.onFocusChangeListener = null
            etInput!!.setOnEditorActionListener(null)
            etInput = null
        }
        if (ivInputBtn != null) {
            ivInputBtn!!.setOnClickListener(null)
            ivInputBtn = null
        }
        if (tvInputBtn != null) {
            tvInputBtn!!.setOnClickListener(null)
            tvInputBtn = null
        }
        mTextWatcher = null
        mListener = null
        removeAllViews()
    }

    // 禁止输入空格
    fun setTextWatcher(): InputFrameView {
        with(etInput) { this!!.addTextChangedListener(textChanged) }
        return this
    }

    // 禁止输入空格
    var textChanged: TextWatcher = object : TextWatcher {
        override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
        override fun onTextChanged(
            charSequence: CharSequence,
            start: Int,
            before: Int,
            count: Int
        ) {
            // 禁止EditText输入空格
            if (charSequence.toString().contains(" ")) {
                val str = charSequence.toString().split(" ").toTypedArray()
                val sb = StringBuffer()
                for (i in str.indices) {
                    sb.append(str[i])
                }
                etInput?.setText(sb.toString())
                etInput?.setSelection(start)
            }
        }

        override fun afterTextChanged(editable: Editable) {}
    }

    // 禁止输入空格
    fun setTextPasswordWatcher(): InputFrameView {
        etInput?.addTextChangedListener(textPasswordChanged)
        return this
    }

    // 禁止输入空格
    var textPasswordChanged: TextWatcher = object : TextWatcher {
        override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
        override fun onTextChanged(
            charSequence: CharSequence,
            start: Int,
            before: Int,
            count: Int
        ) {
            // 禁止EditText输入空格
            var flag = 0
            var str = arrayOf<String?>()
            if (charSequence.toString().contains(" ")) {
                str = charSequence.toString().split(" ").toTypedArray()
            } else if (charSequence.toString().contains("`")) {
                str = charSequence.toString().split("`").toTypedArray()
            } else if (charSequence.toString().contains("\"")) {
                str = charSequence.toString().split("\"").toTypedArray()
            } else if (charSequence.toString().contains(",")) {
                str = charSequence.toString().split(",").toTypedArray()
            } else if (charSequence.toString().contains("=")) {
                str = charSequence.toString().split("=").toTypedArray()
            } else if (charSequence.toString().contains("!")) {
                str = charSequence.toString().split("!").toTypedArray()
            } else if (charSequence.toString().contains(";")) {
                str = charSequence.toString().split(";").toTypedArray()
            } else if (charSequence.toString().contains("\\")) {
                str = charSequence.toString().split("\\\\").toTypedArray()
            } else if (charSequence.toString().contains("-")) {
                str = charSequence.toString().split("-").toTypedArray()
            } else if (charSequence.toString().contains("|")) {
                str = charSequence.toString().split("\\|").toTypedArray()
            } else {
                flag = -1
            }
            if (flag == 0) {
                val sb = StringBuffer()
                for (i in str.indices) {
                    sb.append(str[i])
                }
                etInput?.setText(sb.toString())
                etInput?.setSelection(start)
            }
        }

        override fun afterTextChanged(editable: Editable) {}
    }

    // 禁止输入空格
    fun setTextSSIDWatcher(): InputFrameView {
        etInput?.addTextChangedListener(textSSIDChanged)
        return this
    }

    // 禁止输入空格
    var textSSIDChanged: TextWatcher = object : TextWatcher {
        override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
        override fun onTextChanged(
            charSequence: CharSequence,
            start: Int,
            before: Int,
            count: Int
        ) {
            // 禁止EditText输入空格
            var flag = 0
            var str = arrayOf<String?>()
            // 如果用start = 0，当获取ssid时，会进入判断使得展示出的ssid不包含空格
            if (charSequence.toString().contains(" ") && charSequence.toString()
                    .substring(0, 1) == " " /*&& start == 0*/) {
                str = charSequence.toString().split(" ").toTypedArray()
            } else if (charSequence.toString().contains("`")) {
                str = charSequence.toString().split("`").toTypedArray()
            } else if (charSequence.toString().contains("\"")) {
                str = charSequence.toString().split("\"").toTypedArray()
            } else if (charSequence.toString().contains(",")) {
                str = charSequence.toString().split(",").toTypedArray()
            } else if (charSequence.toString().contains(":")) {
                str = charSequence.toString().split(":").toTypedArray()
            } else if (charSequence.toString().contains("~")) {
                str = charSequence.toString().split("~").toTypedArray()
            } else if (charSequence.toString().contains("\\")) {
                str = charSequence.toString().split("\\\\").toTypedArray()
            } else if (charSequence.toString().contains("$")) {
                str = charSequence.toString().split("\\$").toTypedArray()
            } else if (charSequence.toString().contains("%")) {
                str = charSequence.toString().split("%").toTypedArray()
            } else if (charSequence.toString().contains("<")) {
                str = charSequence.toString().split("<").toTypedArray()
            } else if (charSequence.toString().contains(">")) {
                str = charSequence.toString().split(">").toTypedArray()
            } else if (charSequence.toString().contains("/")) {
                str = charSequence.toString().split("/").toTypedArray()
            } else if (charSequence.toString().contains("'")) {
                str = charSequence.toString().split("'").toTypedArray()
            } else if (charSequence.toString().contains(";")) {
                str = charSequence.toString().split(";").toTypedArray()
            } else {
                flag = -1
            }
            if (flag == 0) {
                val sb = StringBuffer()
                for (i in str.indices) {
                    sb.append(str[i])
                }
                etInput?.setText(sb.toString())
                etInput?.setSelection(start)
            }
        }

        override fun afterTextChanged(editable: Editable) {}
    }

    interface OnInputFrameClickListener {
        fun onInputBtnClick()

        /**
         * remember to call hideInputMethod()
         */
        fun onEditorAction()
        fun onEtInputClick()
    }

    companion object {
        const val INPUT_THEME_DEFAULT = 0
        const val INPUT_THEME_1 = 1
        const val INPUT_THEME_2 = 2
        const val INPUT_BTN_TYPE_ICON = 1
        const val INPUT_BTN_TYPE_TEXT = 2
        private fun dp2px(dp: Float): Int {
            val r = Resources.getSystem()
            return TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dp,
                r.displayMetrics
            ).toInt()
        }

        const val INPUT_FRAME_THEME_TYPE_DEFAULT = 1
        const val INPUT_FRAME_THEME_TYPE_TEXT_CENTER = 2
        fun isPasswordInputType(inputType: Int): Boolean {
            val variation =
                inputType and (EditorInfo.TYPE_MASK_CLASS or EditorInfo.TYPE_MASK_VARIATION)
            return (variation
                    == EditorInfo.TYPE_CLASS_TEXT or EditorInfo.TYPE_TEXT_VARIATION_PASSWORD) || (variation
                    == EditorInfo.TYPE_CLASS_TEXT or EditorInfo.TYPE_TEXT_VARIATION_WEB_PASSWORD) || (variation
                    == EditorInfo.TYPE_CLASS_NUMBER or EditorInfo.TYPE_NUMBER_VARIATION_PASSWORD)
        }

        fun getPwInputType(type: Int, isVisible: Boolean): Int {
            return if (isVisible) {
                if (type == InputType.TYPE_CLASS_NUMBER) {
                    InputType.TYPE_CLASS_NUMBER or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                } else {
                    InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                }
            } else {
                if (type == InputType.TYPE_CLASS_NUMBER) {
                    InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_VARIATION_PASSWORD
                } else {
                    InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                }
            }
        }
    }

    init {
        val a: TypedArray = context.obtainStyledAttributes(attrs, R.styleable.InputFrameView, 0, 0)
        mThemeType = a.getInt(R.styleable.InputFrameView_input_theme, INPUT_THEME_DEFAULT)
        initAttr()
        init(context)
    }
}