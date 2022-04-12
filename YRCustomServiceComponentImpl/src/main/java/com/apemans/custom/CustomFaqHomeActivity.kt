package com.apemans.custom

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.alibaba.android.arouter.facade.annotation.Route
import com.dylanc.longan.context
import com.dylanc.longan.startActivity
import com.google.gson.Gson
import com.apemans.yruibusiness.base.BaseComponentActivity
import com.apemans.custom.adapter.FAQAdapter
import com.apemans.custom.bean.FAQBean
import com.apemans.custom.bean.FAQMap
import com.apemans.custom.databinding.CustomserviceActivityFaqHomeBinding
import com.apemans.yruibusiness.ui.toolbar.setToolbar
import java.lang.StringBuilder
import java.util.*


@Route(path = ACTIVITY_PATH_FEEDBACK_FAQ_MAIN)
class CustomFaqHomeActivity : com.apemans.yruibusiness.base.BaseComponentActivity<CustomserviceActivityFaqHomeBinding>() {

    private var mAdapter: FAQAdapter? = null
    private val mRootFAQs: MutableList<FAQBean> = ArrayList()
    private var mIsRoot = true
    private  var FEEDBACK_IS_SHOW_BTN  = "FEEDBACK_IS_SHOW_BTN"

    override fun onViewCreated(savedInstanceState: Bundle?) {
        initView()
        initData()
    }

    fun initView() {
        setToolbar {
            title = resources.getString(R.string.help_faq_main)
            leftIcon(R.drawable.ic_back, ::onClickLeft)
        }
        binding.containerFeedback.setOnClickListener {
            startActivity<CustomServiceActivity>()
        }
        binding.tvEmail.setOnClickListener {
            val mailToSb = StringBuilder()
            mailToSb.append("mailto:")
            mailToSb.append(getString(R.string.support_email_address))
            val uri = Uri.parse(mailToSb.toString())
            val intent = Intent(Intent.ACTION_SENDTO, uri)
            startActivity(
                Intent.createChooser(
                    intent,
                    getString(R.string.about_select_email_application)
                )
            )
        }
    }


    /**
     * 加载本地faq.json,通过手机语言，映射获取对应的faq模块数据
     */
    private fun initData() {
        val result = getFAQFromAssert()
        val lan = getLanKey()
        val faqMap: FAQMap? = convertJson(result, FAQMap::class.java)
        if (faqMap?.getData() != null && faqMap.getData()!!.containsKey(lan)) {
            faqMap.getData()!![lan]?.let { setupFAQListView(it) }
        } else {
            Log.e("CustomMainActivity", "加载faq信息失败")
        }
        var isShowBtn =  intent.getBooleanExtra(FEEDBACK_IS_SHOW_BTN,true);
        binding.containerFeedback.visibility = if (isShowBtn)View.VISIBLE else  View.GONE
    }

    private fun onClickLeft(view: View) {
        if (mIsRoot) {
            finish()
        } else {
            mIsRoot = true
            resetFAQList(mRootFAQs)
        }
    }

    private fun setupFAQListView(faqBeans: List<FAQBean>) {
        val layoutManager = LinearLayoutManager(this)
        binding.rcvFAQ.layoutManager = layoutManager
        mAdapter = FAQAdapter()
        mAdapter?.setListener(object : FAQAdapter.OnFAQItemClickListener {
            override fun onItemClick(position: Int, data: FAQBean?) {
                if (data != null && (data.children != null && data.children!!.isNotEmpty())) {
                    mIsRoot = false
                    data.children?.let { resetFAQList(it) }
                }
            }
        })
        binding.rcvFAQ.adapter = mAdapter
        mIsRoot = true
        if ((faqBeans.isNotEmpty())) {
            mRootFAQs.addAll(faqBeans)
        }
        resetFAQList(faqBeans)
    }

    /**
     * 点击返回等刷新faq模块数据
     */
    private fun resetFAQList(faqBeans: List<FAQBean>) {
        if ((faqBeans.isNotEmpty())) {
            mAdapter?.setData(faqBeans)
        }
    }

    /**
     * 获取国家码 如cn
     */
    private fun getCountryZipCode(): String {
        val locale: Locale = com.dylanc.longan.application.context.resources.configuration.locale;
        val countryZipCode = locale.country;
        return countryZipCode
    }

    /**
     * 获取faq信息
     */
    private fun getFAQFromAssert(): String {
        var result: String? = ""
        try {
            val inputStream = resources.assets.open("html/faq.json")
            val length = inputStream.available()
            val buffer = ByteArray(length)
            inputStream.read(buffer)
            result = String(buffer)
            inputStream.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return result!!
    }

    private fun <T> convertJson(content: String, clazz: Class<T>): T? {
        var result: T? = null
        val gson: Gson = Gson()
        try {
            result = gson.fromJson(content, clazz)
        } catch (var5: Exception) {
            var5.printStackTrace()
        }
        return result
    }

    private fun getLanKey(): String {
        val language = getCountryZipCode()
        val key: String
        if (language.contains("CN"/*"zh"*/)) {
            key = "zh"
        } else if (language.contains("ru")) {
            key = "ru"
        } else if (language.contains("pl")) {
            key = "pl"
        } else if (language.contains("it")) {
            key = "it"
        } else if (language.contains("fr")) {
            key = "fr"
        } else if (language.contains("es")) {
            key = "es"
        } else if (language.contains("de")) {
            key = "de"
        } else if (language.contains("ja")) {
            key = "ja"
        } else {
            key = "en"
        }
        return key
    }

}