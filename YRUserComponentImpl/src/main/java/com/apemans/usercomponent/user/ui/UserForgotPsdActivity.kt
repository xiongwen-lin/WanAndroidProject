package com.apemans.usercomponent.user.ui

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import androidx.lifecycle.lifecycleScope
import com.apemans.business.apisdk.client.define.HttpCode
import com.dylanc.longan.*
import com.apemans.yruibusiness.ui.toolbar.setToolbar
import com.apemans.quickui.superdialog.SmartDialog
import com.apemans.userapi.request.GlobalUrlResult
import com.apemans.userapi.request.YRApiResponse
import com.apemans.usercomponent.databinding.UserActivityForgotPsdBinding
import com.apemans.usercomponent.user.util.ConstantValue
import kotlinx.coroutines.launch
import com.apemans.usercomponent.R
import com.apemans.usercomponent.TermsServiceRepository
import com.apemans.usercomponent.help.MyAccountHelper
import com.apemans.usercomponent.user.util.GlobalPrefs
import com.apemans.usercomponent.user.widget.WebViewDialogView
import com.apemans.usercomponent.viewModel.UserForgotPasswordViewModel

class UserForgotPsdActivity : UserBaseActivity<UserActivityForgotPsdBinding>() {
    private var doneAnimator: ObjectAnimator? = null
    // by 为 委托 （让后面的方法去实现自己想做的操作）
    // intentExtras -- intent 中通过key取值(被封装过的 -- 需要判断是否为null, 或者传一个默认值) （原本的 intent.extras[name]）
    val account: String? by intentExtras(ConstantValue.INTENT_KEY_ACCOUNT)
    // intentExtras -- intent 中通过key取值(被封装过的 -- 人为确认这个参数绝对不为null,为null造成的后果自己负责)
    val type: Int by safeIntentExtras(ConstantValue.INTENT_KEY_VERIFY_TYPE)

    private var mLastDoneBtnClickTime : Long = 0
    var mIsCodeSending : Boolean = false
    var mCountryCode : String = ""
    private lateinit var viewModel : UserForgotPasswordViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupInputFrameView()
        initView()
        initData()
    }

    private fun setupInputFrameView() {
        binding.ipvAccount.apply {
            setTitle(resources.getString(R.string.feedback_email))
            setHint("input email")
            addOnTextChanged { _, _ ->
                checkBtnEnable()
            }
        }
        checkBtnEnable()
    }

    fun initView() {
        setToolbar {
            title = resources.getString(R.string.sign_in_forget_pwd)
            leftIcon(R.drawable.left_arrow_black, ::onClickLeft)
        }

        binding.btnDone.setOnClickListener {
            if (!checkPrivacyIsReadByAccount(binding.ipvAccount.getEditText().text.toString())) {
                showBtnDoneLoading()
                viewModel.checkAccountSourceForgetPassword(binding.ipvAccount.getEditText().text.toString())
                    .observe(this) {
                        hideBtnDoneLoading()
                        checkAccountSourceForgetPassword(it)
                    }
            } else {
                getResetVerifyCode()
            }
        }
        binding.ipvAccount.setText(account?:"")
        setupPrivacyDialogForAccountMoving()
        setBtnDownAnimator()
    }

    private fun setupPrivacyDialogForAccountMoving() {
        binding.wdvForgetPassword.setListener(object : WebViewDialogView.WebViewDialogListener {
            override fun onCancel() {
                hidePrivacyDialogForAccountMoving()
            }

            override fun onConfirm() {
                dealOnConfirmPrivacyOfAccountMoving(binding.ipvAccount.getEditText().text.toString())
            }

            override fun onOutSideClick() {
                hidePrivacyDialogForAccountMoving()
            }

            override fun onPageStarted() {
            }

            override fun onPageFinished() {
            }
        })
    }

    private fun initData() {
        viewModel = registerViewModule(UserForgotPasswordViewModel::class.java)
    }

    private fun onClickLeft(view : View) {
        finish()
    }

    @SuppressLint("WrongConstant")
    fun getResetVerifyCode() {
        if (binding.ipvAccount.getEditText().text.toString().isEmpty()) {
            toast(resources.getString(R.string.sign_in_account_empty))
        } else if ((System.currentTimeMillis() - mLastDoneBtnClickTime) < ConstantValue.BTN_CLICK_GAP_TIME) {
            toast(resources.getString(R.string.repeat_click_btn_soon))
        } else if (!mIsCodeSending){
            mLastDoneBtnClickTime = System.currentTimeMillis()
            mIsCodeSending = true
            showBtnDoneLoading()
            lifecycleScope.launch {
                viewModel.sendVerifyCode(binding.ipvAccount.getEditText().text.toString(), mCountryCode, type)
                        .observe(this@UserForgotPsdActivity) {
                            hideBtnDoneLoading()
                            sendVerifyCodeResult(it)
                }
            }
        }
    }

    private fun checkBtnEnable() {
        if (binding.ipvAccount.getEditText().text.toString().isNotEmpty()) {
            binding.btnDone.isEnabled = true
            binding.btnDone.setTextColor(resources.getColor(R.color.black_010C11))
        } else {
            binding.btnDone.isEnabled = false
            binding.btnDone.setTextColor(resources.getColor(R.color.theme_white))
        }
    }

    private fun checkAccountSourceForgetPassword(response : YRApiResponse<GlobalUrlResult>) {
        if (response.code != HttpCode.SUCCESS_CODE) {
            toast(resources.getString(R.string.network_error0))
        } else {
            val brandList: MutableList<String?> = response.data.schema as MutableList<String?>
            val isSelf: Boolean = MyAccountHelper.checkAppCountSelf(brandList)
            if (isSelf) {
                getResetVerifyCode()
            } else {
                MyAccountHelper.convertFromBrandList(brandList)?.let {
                    checkAccountSourceOnSuccess(true, it)
                }
            }
        }
    }

    private fun checkAccountSourceOnSuccess(isOtherBrand: Boolean, brand: String) {
        if (isOtherBrand) {
            showAccountMigrationDialog(brand)
        }
    }

    private fun showAccountMigrationDialog(brand: String) {
        val content = String.format(getString(R.string.cancel_normal), brand)
        SmartDialog.build(supportFragmentManager)
            .setTitle("You can only use your Osaio account after you read and agree to our privacy policy.")
            .setContentText("Thank you for trusting and using Osaio. This is your first time logging in with your victure account. As an open smart home platform, protecting users\\’ personal information and privacy is a basic principle of Osaio\\n\\n\n" +
                    "        If you do not agree to use your third-party account on Osaio, you may not use Osaio or re-register a new account.\\nAt the same time, if you change your mind, you can agree to the privacy policy and log in at any time.")
            .setNegativeTextName(getString(R.string.cancel_normal))
            .setPositiveTextName(getString(R.string.privacy_policy))
            .setOnNegative {
                hideBtnDoneLoading()
                it.dismiss()
            }.setOnPositive {
                showPrivacyDialogForAccountMoving()
                hideBtnDoneLoading()
                it.dismiss()
            }.show()
    }

    private fun dealOnConfirmPrivacyOfAccountMoving(account: String) {
        GlobalPrefs.setPrivacyIsReadByAccount(account, true)
        hidePrivacyDialogForAccountMoving()
        getResetVerifyCode()
    }

    private fun showPrivacyDialogForAccountMoving() {
        val privacyUrl = TermsServiceRepository.getPrivacyPolicyByRegion(context)
        binding.wdvForgetPassword.visibility = View.VISIBLE
        binding.wdvForgetPassword.loadContent(privacyUrl)
    }

    private fun hidePrivacyDialogForAccountMoving() {
        binding.wdvForgetPassword.visibility = View.GONE
    }

    private fun checkPrivacyIsReadByAccount(account: String): Boolean {
        return TextUtils.isEmpty(account) || GlobalPrefs.getPrivacyIsReadByAccount(account)
    }

    private fun setBtnDownAnimator() {
        doneAnimator = ObjectAnimator.ofFloat(binding.ivLoading, "Rotation", 0f, 360f)
        doneAnimator?.duration = 2000;
        doneAnimator?.repeatCount = -1;
    }

    private fun showBtnDoneLoading() {
        binding.btnDone.text = "";
        binding.ivLoading.visibility = View.VISIBLE;
        doneAnimator?.start();
    }

    private fun hideBtnDoneLoading() {
        binding.btnDone.setText(R.string.submit);
        binding.ivLoading.visibility = View.GONE;
        doneAnimator?.pause();
    }

    private fun sendVerifyCodeResult(code: Int) {
        if (code == HttpCode.SUCCESS_CODE) {
            UserInputVerifyCodeActivity.start(this, binding.ipvAccount.getEditText().text.toString(), mCountryCode, type)
        } else if (code == HttpCode.CODE_1055){
            toast(resources.getString(R.string.sign_in_account_not_exist))
        } else if (code == HttpCode.CODE_1052) {
            toast(resources.getString(R.string.camera_share_account_invalid))
        }
    }

    companion object {
        // KEY_ACCOUNT to account 类似于intent中传参key -- value
        fun start(context: Context, account: String, type: Int) {
            context.startActivity<UserForgotPsdActivity>(
                ConstantValue.INTENT_KEY_ACCOUNT to account,
                ConstantValue.INTENT_KEY_VERIFY_TYPE to type,
            )
        }
    }
}