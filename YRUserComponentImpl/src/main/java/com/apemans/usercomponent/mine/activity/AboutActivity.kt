package com.apemans.usercomponent.mine.activity

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.view.View
import androidx.core.content.ContextCompat
import com.apemans.base.utils.clickWithDuration
import com.dylanc.longan.addStatusBarHeightToMarginTop
import com.dylanc.longan.startActivity
import com.apemans.yruibusiness.ui.toolbar.setToolbar
import com.apemans.usercomponent.R
import com.apemans.usercomponent.TermsServiceRepository
import com.apemans.usercomponent.databinding.MineActivityAboutBinding
import com.dylanc.longan.appVersionName
import java.lang.String
import java.lang.StringBuilder

class AboutActivity : MineBaseActivity<MineActivityAboutBinding>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
        setupWebsiteClickableTv()
        setupEmailClickableTv()
    }

    private fun initView() {
        binding.imageView3.addStatusBarHeightToMarginTop()
        setToolbar {
            title = getString(R.string.about)
        }
        binding.tvVersion.text = String.format(getString(R.string.about_version), appVersionName)
        binding.termsOfService.clickWithDuration(500) {
            // 服务条款
            TermsServiceRepository.openTermsServiceWebSite(this)
        }
        binding.privacyPolicy.clickWithDuration(500) {
            // 隐私政策
            TermsServiceRepository.openPrivacyWebSite(this)
        }
    }

    private fun setupWebsiteClickableTv() {
        val style = SpannableStringBuilder()
        val text = "http://osaio.net"
        binding.tvWebsite.text = text
        binding.webLayout.setOnClickListener {
            val intent = Intent()
            intent.action = Intent.ACTION_VIEW
            intent.data = Uri.parse("http://osaio.net")
            startActivity(intent)
        }

        //设置文字
//        style.append(text)
//
//        //设置部分文字点击事件
//        val clickableSpan: ClickableSpan = object : ClickableSpan() {
//            override fun onClick(widget: View) {
//                val intent = Intent()
//                intent.action = Intent.ACTION_VIEW
//                val content_url = Uri.parse("http://osaio.net")
//                intent.data = content_url
//                startActivity(intent)
//            }
//        }
//        style.setSpan(clickableSpan, 0, text.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
//        binding.tvWebsite.text = style
//
//        //设置部分文字颜色
//        val foregroundColorSpan = ForegroundColorSpan(ContextCompat.getColor(this, R.color.theme_text_color))
//        style.setSpan(foregroundColorSpan, 0, text.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
//
//        //配置给TextView
//        binding.tvWebsite.movementMethod = LinkMovementMethod.getInstance()
//        binding.tvWebsite.text = style
    }

    private fun setupEmailClickableTv() {
        val style = SpannableStringBuilder()
        val text = getString(R.string.support_email_address)
        binding.tvEmail.text = text
        binding.emailLayout.setOnClickListener {
            val mailToSb = StringBuilder()
            mailToSb.append("mailto:")
            mailToSb.append(getString(R.string.support_email_address))
            val uri = Uri.parse(mailToSb.toString())
            val intent = Intent(Intent.ACTION_SENDTO, uri)
            startActivity(Intent.createChooser(intent, getString(R.string.about_select_email_application)))
        }
        //设置文字
        /*style.append(text)

        //设置部分文字点击事件
        val clickableSpan: ClickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                val mailToSb = StringBuilder()
                mailToSb.append("mailto:")
                mailToSb.append(getString(R.string.support_email_address))
                val uri = Uri.parse(mailToSb.toString())
                val intent = Intent(Intent.ACTION_SENDTO, uri)
                startActivity(Intent.createChooser(intent, getString(R.string.about_select_email_application)))
            }
        }
        style.setSpan(clickableSpan, 0, text.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        binding.tvEmail.text = style

        //设置部分文字颜色
        val foregroundColorSpan = ForegroundColorSpan(ContextCompat.getColor(this, R.color.theme_text_color))
        style.setSpan(foregroundColorSpan, 0, text.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

        //配置给TextView
        binding.tvEmail.movementMethod = LinkMovementMethod.getInstance()
        binding.tvEmail.text = style*/
    }

    private fun setupPrivacyClickableTv() {
        val style = SpannableStringBuilder()
        val conditionUse = getString(R.string.terms_of_service)
        val privacy = getString(R.string.privacy_policy)
        val text = String.format(getString(R.string.about_privacy), conditionUse, privacy)
        //设置文字
        style.append(text)
        //设置部分文字颜色
        val conditionForegroundColorSpan = ForegroundColorSpan(ContextCompat.getColor(this, R.color.theme_subtext_color))
        style.setSpan(conditionForegroundColorSpan, text.indexOf(conditionUse), text.indexOf(conditionUse) + conditionUse.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        val privacyForegroundColorSpan = ForegroundColorSpan(ContextCompat.getColor(this, R.color.theme_subtext_color))
        style.setSpan(privacyForegroundColorSpan, text.indexOf(privacy), text.indexOf(privacy) + privacy.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

        //配置给TextView
//        binding.tvPrivacy.movementMethod = LinkMovementMethod.getInstance()
//        binding.tvPrivacy.text = style
    }


    private fun onRightClick(v: View) {

    }

    companion object {
        fun start(from: Context) {
            from.startActivity<AboutActivity>()
        }
    }
}