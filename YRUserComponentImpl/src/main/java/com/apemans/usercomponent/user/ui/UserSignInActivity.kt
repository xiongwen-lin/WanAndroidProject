package com.apemans.usercomponent.user.ui

import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.*
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.view.View
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.alibaba.android.arouter.facade.annotation.Route
import com.apemans.base.middleservice.YRMiddleServiceManager
import com.apemans.base.utils.LanguageUtil
import com.apemans.business.apisdk.client.define.HttpCode
import com.apemans.logger.YRLog
import com.apemans.messagepush.push.um.UmPushClient
import com.apemans.quickui.superdialog.SmartDialog
import com.apemans.router.ACTIVITY_PATH_MAIN
import com.apemans.router.startRouterActivity
import com.apemans.userapi.paths.KEY_COUNTRY_CODE
import com.apemans.userapi.paths.KEY_COUNTRY_NAME
import com.apemans.userapi.request.GlobalUrlResult
import com.apemans.userapi.request.YRApiResponse
import com.apemans.usercomponent.R
import com.apemans.usercomponent.TermsServiceRepository
import com.apemans.usercomponent.baseinfo.configure.CountryUtil
import com.apemans.usercomponent.baseinfo.graphics.DisplayUtil
import com.apemans.usercomponent.databinding.UserActivityLoginBinding
import com.apemans.usercomponent.help.MyAccountHelper
import com.apemans.usercomponent.user.util.ConstantValue
import com.apemans.usercomponent.user.util.GlobalPrefs
import com.apemans.usercomponent.user.util.UserHelper
import com.apemans.usercomponent.user.widget.WebViewDialogView
import com.apemans.usercomponent.viewModel.UserSigninViewModel
import com.apemans.yrcxsdk.data.YRCXSDKDataManager
import com.dylanc.longan.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

@Route(path = "/user/login")
class UserSignInActivity : UserBaseActivity<UserActivityLoginBinding>() {
    lateinit var countryName: String
    var countryCode: Int = 86
    private lateinit var viewModle : UserSigninViewModel
    val account: String by intentExtras(ConstantValue.INTENT_KEY_ACCOUNT, "")
    private val password: String by intentExtras(ConstantValue.INTENT_KEY_PSD, "")
    private val isClearTask: Boolean by intentExtras(ConstantValue.INTENT_KEY_DATA_PARAM, false)
    private var btnSignInAnimator: ObjectAnimator? = null
    private var btnSignUpAnimator: ObjectAnimator? = null
    private var mPrivacyOfAccountMovingType = 0
    private val PRIVACY_OF_ACCOUNT_MOVING_TYPE_SIGN_IN = 1
    private val PRIVACY_OF_ACCOUNT_MOVING_TYPE_SIGN_UP = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
        initData()
    }

    fun initView() {
        setupInputFrameView()
        setupView()
        setupPrivacyClickableTv()
        setupPrivacyDialogForAccountMoving()
        setBtnSignInAnimator()
        setBtnSignUpAnimator()
    }

    private fun initData() {
        if (account.isNotEmpty()) {
            binding.ipvAccount.setText(account)
        }
        if (password.isNotEmpty()) {
            binding.ipvPsd.setText(password)
        }
        viewModle = registerViewModule(UserSigninViewModel::class.java)
    }

    private fun setupInputFrameView() {
        binding.ipvAccount.apply {
            setTitle(resources.getString(R.string.feedback_email))
            setHint("input email")
            addOnTextChanged { _, _ -> checkBtnEnable() }
        }
        binding.ipvPsd.apply {
            setTitle(resources.getString(R.string.router_password))
            setHint("input password")
            addOnTextChanged { _, _ -> checkBtnEnable() }
        }
        binding.ipvSelectCountry.apply {
            setTitle(resources.getString(R.string.sign_in_country_title))
            addOnTextChanged { _, _ -> checkBtnEnable() }
            addOnArrowClick { _, _ ->
                //监听arrow按钮点击回调
                UserCountryListActivity.start(activity, 1) }
        }
        binding.ipvRegisterAccount.apply {
            setTitle(resources.getString(R.string.feedback_email))
            setHint("input account")
            addOnTextChanged { _, _ -> checkBtnEnable() }
        }
        binding.tvRegisterCode.apply {
            addOnRightTextClick { _, _ -> tryToSendVerifyCode() }
            addSetOnFocusChangeListener { _, _ -> tryToSendVerifyCode() }
            addOnTextChanged { _, _ -> checkBtnEnable() }
        }
        checkBtnEnable()
    }

    private fun setupView() {
        with(binding) {
            ivLogo.addStatusBarHeightToMarginTop()
            btnSignIn.setOnClickListener { clickSignin() }
            btnSignUp.setOnClickListener { clickSignUp() }
            btnSignInTag.setOnClickListener { setShowView(true) }
            btnSignUpTag.setOnClickListener { setShowView(false) }
            tvForgetPsd.setOnClickListener {
                UserForgotPsdActivity.start(this@UserSignInActivity, binding.ipvAccount.getEditText().text.toString(), ConstantValue.USER_FIND_PWD_VERIFY)
            }
        }
    }

    private fun setShowView(flag: Boolean) {
        with(binding) {
            ipvAccount.visibility = if (flag) View.VISIBLE else View.GONE
            ipvPsd.visibility = if (flag) View.VISIBLE else View.GONE
            btnSignIn.visibility = if (flag) View.VISIBLE else View.GONE
            tvForgetPsd.visibility = if (flag) View.VISIBLE else View.GONE
            vSignInSelectedTag.visibility = if (flag) View.VISIBLE else View.GONE
            ipvSelectCountry.visibility = if (!flag) View.VISIBLE else View.GONE
            vSignUpSelectedTag.visibility = if (!flag) View.VISIBLE else View.GONE
            ipvRegisterAccount.visibility = if (!flag) View.VISIBLE else View.GONE
            tvRegisterCode.visibility = if (!flag) View.VISIBLE else View.GONE
            cbPrivacy.visibility = if (!flag) View.VISIBLE else View.GONE
            tvPrivacy.visibility = if (!flag) View.VISIBLE else View.GONE
            btnSignUp.visibility = if (!flag) View.VISIBLE else View.GONE
        }
    }

    private fun setupPrivacyClickableTv() {
        val style = SpannableStringBuilder()
        val conditionUse = getString(R.string.terms_of_service)
        val privacy = getString(R.string.privacy_policy)
        val text = java.lang.String.format(
            getString(R.string.sign_up_create_account_tip),
            conditionUse, privacy)
        //设置文字
        style.append(text)
        //设置部分文字点击事件
        val conditionClickableSpan: ClickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                TermsServiceRepository.openTermsServiceWebSite(activity)
            }
        }
        style.setSpan(conditionClickableSpan, text.indexOf(conditionUse),
            text.indexOf(conditionUse) + conditionUse.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        binding.tvPrivacy.text = style
        //设置部分文字点击事件
        val privacyClickableSpan: ClickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                TermsServiceRepository.openPrivacyWebSite(activity)
            }
        }
        style.setSpan(privacyClickableSpan, text.indexOf(privacy),
            text.indexOf(privacy) + privacy.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        binding.tvPrivacy.text = style
        //设置部分文字颜色
        val conditionForegroundColorSpan =
            ForegroundColorSpan(ContextCompat.getColor(this, R.color.theme_blue))
        style.setSpan(conditionForegroundColorSpan, text.indexOf(conditionUse),
            text.indexOf(conditionUse) + conditionUse.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        val privacyForegroundColorSpan =
            ForegroundColorSpan(ContextCompat.getColor(this, R.color.theme_blue))
        style.setSpan(privacyForegroundColorSpan,
            text.indexOf(privacy), text.indexOf(privacy) + privacy.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        //配置给TextView
        binding.tvPrivacy.movementMethod = LinkMovementMethod.getInstance()
        binding.tvPrivacy.text = style
    }

    override fun onResume() {
        super.onResume()
    }

    private fun checkBtnEnable() {
        if (binding.ipvAccount.getEditText().text.isNotEmpty() && binding.ipvPsd.getEditText().text.isNotEmpty()) {
            binding.btnSignIn.apply {
                isEnabled = true
                setTextColor(resources.getColor(R.color.black_010C11))
            }
        } else {
            binding.btnSignIn.apply {
                isEnabled = false
                setTextColor(resources.getColor(R.color.theme_white))
            }
        }

        if (binding.ipvRegisterAccount.getEditText().text.isNotEmpty() && binding.tvRegisterCode.getEditText().text.isNotEmpty()) {
            binding.btnSignUp.apply {
                isEnabled = true
                setTextColor(resources.getColor(R.color.black_010C11))
            }
        } else {
            binding.btnSignUp.apply {
                isEnabled = false
                setTextColor(resources.getColor(R.color.theme_white))
            }
        }
    }

    private fun clickSignin() {
        with(binding) {
            if (ipvAccount.getEditText().text.isEmpty() || ipvPsd.getEditText().text.isEmpty()) {
                if (ipvAccount.getEditText().text.isEmpty())
                    ipvAccount.showTips(resources.getString(R.string.sign_in_account_empty))
                else
                    ipvPsd.showTips(resources.getString(R.string.sign_in_password_empty))
            }
            if (!checkPrivacyIsReadByAccount(ipvAccount.getEditText().text.toString())) {
                showBtnSignInLoading()
                checkAccountSourceForSignIn(ipvAccount.getEditText().text.toString(), ipvPsd.getEditText().text.toString())
            } else {
                tryToSignIn()
            }
        }
    }

    private fun clickSignUp() {
        with(binding) {
            if (ipvRegisterAccount.getEditText().text.isEmpty()) {
                ipvRegisterAccount.showTips(resources.getString(R.string.sign_in_account_empty))
            } else if (!cbPrivacy.isChecked) {
                toast(resources.getString(R.string.sign_up_tip_check_user_privacy))
            } else {
                showBtnSignUpLoading()
                UserSignUpActivity.start(this@UserSignInActivity, ipvRegisterAccount.getEditText().text.toString(),countryCode.toString(), tvRegisterCode.getEditText().text.toString())
            }
        }
    }

    private fun checkAccountSourceForSignIn(response : YRApiResponse<GlobalUrlResult>) {
        if (response.code != HttpCode.SUCCESS_CODE) {
            toast(resources.getString(R.string.network_error0))
        } else {
            val brandList: MutableList<String?> = response.data.schema as MutableList<String?>
            val isSelf: Boolean = MyAccountHelper.checkAppCountSelf(brandList)
            if (isSelf) {
                if (binding.ipvRegisterAccount.visibility == View.VISIBLE) {
                    toast(R.string.sign_up_account_exist)
                } else {
                    tryToSignIn()
                }
            } else {
                MyAccountHelper.convertFromBrandList(brandList)?.let {
                    checkAccountSourceOnSuccess(true, it, true)
                }
            }
        }
    }

    private fun setupPrivacyDialogForAccountMoving() {
        binding.wdvSignIn.setListener(object : WebViewDialogView.WebViewDialogListener {
            override fun onCancel() {
                hidePrivacyDialogForAccountMoving()
            }

            override fun onConfirm() {
                getAccountByPrivacyOfAccountMovingType(mPrivacyOfAccountMovingType)?.let {
                    dealOnConfirmPrivacyOfAccountMoving(mPrivacyOfAccountMovingType, it)
                }
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

    private fun checkAccountSourceForSignIn(userAccount: String, userPsd: String) {
        lifecycleScope.launch {
            viewModle.checkAccountSourceForSignIn(userAccount, userPsd)
                .observe(this@UserSignInActivity) {
                    checkAccountSourceForSignIn(it)
                }
        }
    }

    private fun checkPrivacyIsReadByAccount(account: String): Boolean {
        return TextUtils.isEmpty(account) || GlobalPrefs.getPrivacyIsReadByAccount(account)
    }

    private fun dealOnConfirmPrivacyOfAccountMoving(type: Int, account: String) {
        GlobalPrefs.setPrivacyIsReadByAccount(account, true)
        hidePrivacyDialogForAccountMoving()
        if (binding.ipvAccount.visibility == View.VISIBLE) {
            tryToSignIn()
        } else {
            setShowView(true)
        }
    }

    private fun getAccountByPrivacyOfAccountMovingType(type: Int): String? {
        var account = ""
        when (type) {
            PRIVACY_OF_ACCOUNT_MOVING_TYPE_SIGN_IN -> {
                account = binding.ipvAccount.getEditText().text.toString()
            }
            PRIVACY_OF_ACCOUNT_MOVING_TYPE_SIGN_UP -> {
                account = binding.ipvRegisterAccount.getEditText().text.toString()
            }
        }
        return account
    }

    private fun tryToSignIn() {
        if (TextUtils.isEmpty(binding.ipvAccount.getEditText().text.toString()) || TextUtils.isEmpty(binding.ipvPsd.getEditText().text.toString())) {
            if (binding.ipvAccount.getEditText().text.isEmpty())
                binding.ipvAccount.showTips(resources.getString(R.string.sign_in_account_empty))
            else
                binding.ipvPsd.showTips(resources.getString(R.string.sign_in_password_empty))
            return
        }
//        lifecycleScope.launch {
            showBtnSignInLoading()
            viewModle.signIn(binding.ipvAccount.getEditText().text.toString(), binding.ipvPsd.getEditText().text.toString(), countryCode.toString())
                .observe(this@UserSignInActivity) {
                    var msg = ""
                    YRLog.d {
                        "11111111111111111111111111: $it"
                    }
                    msg = if (it == HttpCode.SUCCESS_CODE) {
                        ConstantValue.SUCCESS
                    } else if (it == HttpCode.CODE_1077) {
                        ConstantValue.ERROR
                    } else if (it == HttpCode.CODE_1055) {
                        resources.getString(R.string.sign_in_account_not_exist);
                    } else if (it == HttpCode.CODE_1053) {
                        resources.getString(R.string.sign_in_password_incorrect);
                    } else if (it == HttpCode.CODE_1052) {
                        resources.getString(R.string.camera_share_account_invalid);
                    } else {
                        resources.getString(R.string.network_error0)
                    }
                    notifySignInResult(msg,it)
                }
//        }
    }

    private fun hidePrivacyDialogForAccountMoving() {
        binding.wdvSignIn.visibility = View.GONE
    }

    private fun showBtnSignInLoading() {
        binding.btnSignIn.text = ""
        btnSignInAnimator!!.start()
        binding.ivSignInLoading.visibility = View.VISIBLE
        binding.btnSignUpTag.isEnabled = false
        binding.btnSignInTag.isEnabled = false
        binding.btnSignIn.isClickable = false
        binding.ipvAccount.isEnabled = false
        binding.ipvPsd.isEnabled = false
    }

    private fun hideBtnSignInLoading() {
        binding.btnSignIn.setText(R.string.submit)
        binding.ivSignInLoading.visibility = View.GONE
        btnSignInAnimator!!.pause()
        binding.btnSignUpTag.isEnabled = true
        binding.btnSignInTag.isEnabled = true
        binding.btnSignIn.isClickable = true
        binding.ipvAccount.isEnabled = true
        binding.ipvPsd.isEnabled = true
    }

    private fun showBtnSignUpLoading() {
        binding.btnSignUp.text = ""
        binding.ivSignUpLoading.visibility = View.VISIBLE
        btnSignUpAnimator!!.start()
    }

    private fun hideBtnSignUpLoading() {
        binding.btnSignUp.setText(R.string.submit)
        binding.ivSignUpLoading.visibility = View.GONE
        btnSignUpAnimator!!.pause()
    }

    private fun setBtnSignInAnimator() {
        btnSignInAnimator = ObjectAnimator.ofFloat(binding.ivSignInLoading, "Rotation", 0f, 360f)
        btnSignInAnimator?.duration = 2000
        btnSignInAnimator?.repeatCount = -1
    }

    private fun setBtnSignUpAnimator() {
        btnSignUpAnimator = ObjectAnimator.ofFloat(binding.ivSignUpLoading, "Rotation", 0f, 360f)
        btnSignUpAnimator?.duration = 2000
        btnSignUpAnimator?.repeatCount = -1
    }

    private fun checkAccountSourceOnSuccess(isOtherBrand: Boolean, brand: String, isSignIn: Boolean) {
        if (isOtherBrand) {
            showAccountMigrationDialog(brand, isSignIn)
        }
    }

    private fun showAccountMigrationDialog(brand: String, isSignIn: Boolean) {
        val content = String.format(getString(R.string.cancel_normal), brand)
        SmartDialog.build(supportFragmentManager)
            .setTitle("You can only use your Osaio account after you read and agree to our privacy policy.")
            .setContentText("Thank you for trusting and using Osaio. This is your first time logging in with your victure account. As an open smart home platform, protecting users\\’ personal information and privacy is a basic principle of Osaio\\n\\n\n" +
                    "        If you do not agree to use your third-party account on Osaio, you may not use Osaio or re-register a new account.\\nAt the same time, if you change your mind, you can agree to the privacy policy and log in at any time.")
            .setNegativeTextName(getString(R.string.cancel_normal))
            .setPositiveTextName(getString(R.string.privacy_policy))
            .setOnNegative {
                hideBtnSignInLoading()
                it.dismiss()
            }.setOnPositive {
                setPrivacyOfAccountMovingType(if (isSignIn) PRIVACY_OF_ACCOUNT_MOVING_TYPE_SIGN_IN else PRIVACY_OF_ACCOUNT_MOVING_TYPE_SIGN_UP)
                showPrivacyDialogForAccountMoving()
                hideBtnSignInLoading()
                it.dismiss()
            }.show()
    }

    private fun setPrivacyOfAccountMovingType(type: Int) {
        mPrivacyOfAccountMovingType = type
    }

    private fun showPrivacyDialogForAccountMoving() {
        if (isDestroyed || checkNull(binding.wdvSignIn)) {
            return
        }
        val privacyUrl = TermsServiceRepository.getPrivacyPolicyByRegion(context)
        binding.wdvSignIn.visibility = View.VISIBLE
        binding.wdvSignIn.loadContent(privacyUrl)
    }

    private fun checkNull(vararg args: Any?): Boolean {
        for (`object` in args) {
            if (`object` == null) {
                return true
            }
        }
        return false
    }

    private fun tryToSendVerifyCode() {
        with(binding) {
            if (ipvRegisterAccount.getEditText().text.isEmpty()) {
                toast(resources.getString(R.string.sign_in_account_empty))
            } else {
                // 发送验证码
                viewModle.sendRegisterVerifyCode(ipvRegisterAccount.getEditText().text.toString(), countryCode.toString())
                    .observe(this@UserSignInActivity){
                        notifySendRegisterVerifyCode(it)
                }
            }
        }
    }

    private suspend fun showVerifyCodeCounter() {
        startVerifyCodeCounter()
            .collect {
                if (ConstantValue.SUCCESS == it) {
                    binding.tvRegisterCode.setRightText(resources.getString(R.string.sign_in_resend))
                } else {
                    binding.tvRegisterCode.setRightText("${it}s")
                }
            }
    }

    private fun startVerifyCodeCounter() = flow {
        var limitTime = 180
        while(limitTime > 0) {
            emit(limitTime.toString())
            delay(1000)
            limitTime--
        }
        if (limitTime <= 0) {
            emit(ConstantValue.SUCCESS)
        }
    }.flowOn(Dispatchers.IO)

    private fun notifySignInResult(msg: String, code: Int) {
        if (isDestroyed) {
            return
        }
        hideBtnSignInLoading()
        if (ConstantValue.SUCCESS == msg) {
            YRCXSDKDataManager.userAccount = binding.ipvAccount.getEditText().text.toString() // 模拟保存登录数据到本地
            YRCXSDKDataManager.firstLogin = false
            initGlobalData()
            tuyaOnLogin()
            //上报用户信息（用于接受全局推送）
            reportUserInfo()
            toast("Login success")
            var hashMap = HashMap<String,Any?>()
            hashMap["login"] = this
            YRMiddleServiceManager.request("yrcx://yrplatformbridge/setparamters",hashMap)
            finish()
//            startRouterActivity(ACTIVITY_PATH_MAIN) { finish() }
            //loginSuccess() // 登录成功后需要调用该方法才能使登录拦截的逻辑形成闭环，不要再调用 finish() 方法
        } else if (ConstantValue.ERROR == msg) {
            if (TextUtils.isEmpty(binding.ipvAccount.getEditText().text)) {
                return
            }
            val account = binding.ipvAccount.getEditText().text.toString()
            val password = binding.ipvPsd.getEditText().text.toString()
            UserTwoAuthLoginActivity.start(this, ConstantValue.REQUEST_CODE_FOR_TWO_AUTH_LOGIN, account,
                password, CountryUtil.getCurrentCountry(this), false, isClearTask)
        } else {
            toast(msg)
        }
    }

    private fun tuyaOnLogin() {
        YRMiddleServiceManager.requestAsync("yrcx://yrtuya/onlogin", emptyMap()) {
        }
    }

    private fun notifySendRegisterVerifyCode(code: Int) {
        if (isDestroyed) {
            return
        }
        hideBtnSignUpLoading()
        var errorMsg = resources.getString(R.string.sign_in_verify_code_incorrect)
        if (code == HttpCode.SUCCESS_CODE) {
            errorMsg = resources.getString(R.string.input_verify_code_sended)
            toast(errorMsg)
            lifecycleScope.launch { showVerifyCodeCounter() } // 开始倒计时
        } else if (code == HttpCode.CODE_1054) {
            setPrivacyOfAccountMovingType(PRIVACY_OF_ACCOUNT_MOVING_TYPE_SIGN_UP)
            if (!checkPrivacyIsReadByAccount(binding.ipvRegisterAccount.getEditText().text.toString())) {
                checkAccountSourceForSignIn(binding.ipvRegisterAccount.getEditText().text.toString(),"")
            } else {
                errorMsg = resources.getString(R.string.sign_up_account_exist)
                toast(errorMsg)
            }
        } else if (code == HttpCode.CODE_1052) {
            errorMsg = resources.getString(R.string.camera_share_account_invalid)
            toast(errorMsg)
        }
    }

    private fun reportUserInfo() {
        val nickname = ""
        val photoUrl = ""
        val appVersion = "$appVersionName($appVersionCode)"
        val screenSize = "${DisplayUtil.SCREEN_WIDTH_PX.toString()}*${DisplayUtil.SCREEN_HIGHT_PX}"
        val language = LanguageUtil.getLocal(this).language
        YRCXSDKDataManager.userAccount?.let {
            YRCXSDKDataManager.userCountryCode?.let { it1 ->
                viewModle.reportUserInfo(
                    it, YRCXSDKDataManager.userPassword.toString(),
                    UserHelper.getCurrentTimeZone().toFloat(), it1, 1,
                    nickname, photoUrl, UserHelper.createPhoneId(this@UserSignInActivity),1,
                    UmPushClient.getPushType(), UmPushClient.getToken(),
                    appVersion, appVersionCode.toString(), Build.MODEL, Build.BRAND,
                    Build.VERSION.SDK_INT.toString(), screenSize,
                    language, packageName).observe(this@UserSignInActivity){

                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1) {
            when (resultCode) {
                RESULT_OK -> {
                    if (data != null) {
                        countryName = data.getStringExtra(KEY_COUNTRY_NAME).toString()
                        countryCode = data.getIntExtra(KEY_COUNTRY_CODE, 86)
                        binding.ipvSelectCountry.setText(countryName)
                    }
                }
            }
        }
    }

    companion object {
        fun start(from: Context, account: String, password: String, isClearTask: Boolean) {
            from.startActivity<UserSignInActivity>(
                ConstantValue.INTENT_KEY_ACCOUNT to account,
                ConstantValue.INTENT_KEY_PSD to password,
                ConstantValue.INTENT_KEY_DATA_PARAM to isClearTask
            )
        }
    }
}