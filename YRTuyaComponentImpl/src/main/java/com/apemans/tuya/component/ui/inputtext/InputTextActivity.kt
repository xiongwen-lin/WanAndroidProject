package com.apemans.tuya.component.ui.inputtext

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContract
import com.apemans.yruibusiness.base.BaseComponentActivity
import com.apemans.yruibusiness.ui.toolbar.setToolbar
import com.dylanc.longan.*
import com.apemans.tuya.component.databinding.TuyaActivityInputTextBinding
import com.apemans.tuya.component.ui.inputtext.InputTextContract.Companion.KEY_HINT
import com.apemans.tuya.component.ui.inputtext.InputTextContract.Companion.KEY_NAME
import com.apemans.tuya.component.ui.inputtext.InputTextContract.Companion.KEY_TITLE
import com.apemans.tuya.component.ui.inputtext.InputTextContract.Companion.KEY_VALUE

/**
 * @author Dylan Cai
 */
class InputTextActivity : com.apemans.yruibusiness.base.BaseComponentActivity<TuyaActivityInputTextBinding>() {

    private val name: String by safeIntentExtras(KEY_NAME)
    private val title: String by safeIntentExtras(KEY_TITLE)
    private val hint: String? by intentExtras(KEY_HINT)
    private val value: String? by intentExtras(KEY_VALUE)

    override fun onViewCreated(savedInstanceState: Bundle?) {
        with(binding) {
            setToolbar(title)
            edtValue.setText(value)
            edtValue.hint = hint
            btnDone.enableWhenOtherTextNotEmpty(edtValue)
            btnDone.doOnClick {
                finishWithResult(KEY_VALUE to edtValue.textString)
            }
        }
    }
}

class InputTextRequest(
    val name: String,
    val title: String = name,
    val hint: String? = null,
    val value: String? = null
)

class InputTextContract : ActivityResultContract<InputTextRequest, String>() {
    override fun createIntent(context: Context, input: InputTextRequest) =
        Intent(context, InputTextActivity::class.java).apply {
            putExtra(KEY_NAME, input.name)
            putExtra(KEY_TITLE, input.title)
            putExtra(KEY_HINT, input.hint)
            putExtra(KEY_VALUE, input.value)
        }

    override fun parseResult(resultCode: Int, intent: Intent?): String? =
        if (resultCode == Activity.RESULT_OK) intent?.getStringExtra(KEY_VALUE) else null

    companion object {
        const val KEY_TITLE = "title"
        const val KEY_NAME = "name"
        const val KEY_HINT = "hint"
        const val KEY_VALUE = "value"
    }
}

