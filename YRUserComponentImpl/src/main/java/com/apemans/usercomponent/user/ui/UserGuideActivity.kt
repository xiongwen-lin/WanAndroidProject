package com.apemans.usercomponent.user.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.alibaba.android.arouter.facade.annotation.Route
import com.apemans.logger.YRLog
import com.apemans.quickui.immersive
import com.apemans.userapi.paths.ACTIVITY_PATH_FIRST_START
import com.apemans.usercomponent.R
import com.apemans.usercomponent.TermsServiceRepository
import com.apemans.usercomponent.databinding.UserActivityGuideBinding
import com.apemans.usercomponent.user.adapter.UserPageAdapter
import java.lang.Exception
import java.lang.String

@Route(path = ACTIVITY_PATH_FIRST_START)
class UserGuideActivity : UserBaseActivity<UserActivityGuideBinding>() {

    private var listView = ArrayList<View>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        immersive()
        initView()
    }

    private fun initView() {
        listView.add(layoutInflater.inflate(R.layout.user_activity_first_start_instruction, null))
        listView.add(layoutInflater.inflate(R.layout.user_activity_first_start_show, null))
        listView.add(layoutInflater.inflate(R.layout.user_activity_first_start_show, null))
        var pagerAdapter = UserPageAdapter()
        pagerAdapter.setViewList(listView)
        binding.viewPage.adapter = pagerAdapter
        binding.viewPage.currentItem = 0
        onClickEvnet()
        setupPrivacyClickableTv()
    }

    private val listTitle = arrayOf("See The truth","See Affection","See Light","See More")
    private val listMsg = arrayOf("A pair of remote, never forgetting details, and loyal eyes",
        "Share this safety and warmth with family and pets",
        "Manipulate light and electricity the way you like",
        "Multi-brands and equipment settle in, gradually upgrade your life")
    private val listRes = arrayOf(R.drawable.bg_boot_01,R.drawable.bg_boot_02,
        R.drawable.bg_boot_03,R.drawable.bg_boot_04)
    private fun onClickEvnet() {
        with(binding) {
            listView[0].findViewById<Button>(R.id.btnPrivacyAgree).setOnClickListener {
                viewPage.currentItem = 1
            }
            listView[0].findViewById<TextView>(R.id.btnPrivacyReject).setOnClickListener {
                finish()
            }
            for(i in 1 until listView.size) {
                listView[i].findViewById<TextView>(R.id.tvTitle).text = listTitle[i-1]
                listView[i].findViewById<TextView>(R.id.tvMsg).text = listMsg[i-1]
//                listView[i].findViewById<View>(R.id.layoutShow).setBackgroundResource(listRes[i-1])
            }


            listView[1].findViewById<Button>(R.id.btnStart).setOnClickListener {
                viewPage.currentItem = 2
            }

            listView[2].findViewById<Button>(R.id.btnStart).setOnClickListener {
                UserSignInActivity.start(this@UserGuideActivity, "","", false)
                finish()
            }
        }
    }

    @SuppressLint("CutPasteId")
    private fun setupPrivacyClickableTv() {
        with(binding) {
            val style = SpannableStringBuilder()
            val conditionUse = getString(R.string.terms_of_service)
            val privacy = getString(R.string.privacy_policy)
            val text =
                String.format(getString(R.string.first_start_instruction_tip), conditionUse, privacy)

            //设置文字
            style.append(text)
            try {
                val termsStart = text.indexOf(conditionUse)
                val termsEnd = termsStart + conditionUse.length
                val termSetStyleValid = termsStart in 0 until termsEnd
                if (termSetStyleValid) {
                    //设置部分文字点击事件
                    val conditionClickableSpan: ClickableSpan = object : ClickableSpan() {
                        override fun onClick(widget: View) {
                            TermsServiceRepository.openTermsServiceWebSite(activity)
                        }
                    }
                    style.setSpan(
                        conditionClickableSpan,
                        termsStart,
                        termsEnd,
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                    listView[0].findViewById<TextView>(R.id.tvPrivacy).text = style
//                    binding.tvPrivacy.text = style
                    //设置部分文字颜色
                    val conditionForegroundColorSpan =
                        ForegroundColorSpan(ContextCompat.getColor(this@UserGuideActivity, R.color.theme_blue))
                    style.setSpan(
                        conditionForegroundColorSpan,
                        termsStart,
                        termsEnd,
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                }
                val privacyStart = text.indexOf(privacy)
                val privacyEnd = privacyStart + privacy.length
                val privacySetStyleValid = privacyStart >= 0 && termsEnd > termsStart
                if (privacySetStyleValid) {
                    //设置部分文字点击事件
                    val privacyClickableSpan: ClickableSpan = object : ClickableSpan() {
                        override fun onClick(widget: View) {
                            TermsServiceRepository.openPrivacyWebSite(activity)
                        }
                    }
                    style.setSpan(
                        privacyClickableSpan,
                        privacyStart,
                        privacyEnd,
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                    listView[0].findViewById<TextView>(R.id.tvPrivacy).text = style
//                    binding.tvPrivacy.text = style
                    //设置部分文字颜色
                    val privacyForegroundColorSpan =
                        ForegroundColorSpan(ContextCompat.getColor(this@UserGuideActivity, R.color.theme_blue))
                    style.setSpan(
                        privacyForegroundColorSpan,
                        privacyStart,
                        privacyEnd,
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                }
            } catch (e: Exception) {
                YRLog.d(e)
            }
            //配置给TextView
            listView[0].findViewById<TextView>(R.id.tvPrivacy).movementMethod = LinkMovementMethod.getInstance()
            listView[0].findViewById<TextView>(R.id.tvPrivacy).text = style
//            binding.tvPrivacy.movementMethod = LinkMovementMethod.getInstance()
//            binding.tvPrivacy.text = style
        }
    }
}