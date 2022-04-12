package com.apemans.usercomponent.mine.activity

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.dylanc.longan.context
import com.dylanc.longan.startActivity
import com.google.gson.Gson
import com.apemans.yruibusiness.ui.toolbar.setToolbar
import com.apemans.usercomponent.R
import com.apemans.usercomponent.databinding.MineActivityFaqBinding
import com.apemans.usercomponent.mine.adapter.FAQAdapter
import com.apemans.usercomponent.mine.bean.FAQBean
import com.apemans.usercomponent.mine.bean.FAQMap
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.*
import kotlin.collections.HashMap

class MineFAQActivity : MineBaseActivity<MineActivityFaqBinding>() {

    private var mAdapter: FAQAdapter? = null
    private val mRootFAQs: MutableList<FAQBean> = ArrayList()
    private var mIsRoot = true
    private var titleString = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
        initData()
    }

    private fun initView() {
        setToolbar {
            title = resources.getString(R.string.help_faq)
            leftIcon(R.drawable.left_arrow_black, ::onClickLeft)
        }
    }

    private fun initData() {
        var hashMap = HashMap<String, FAQBean>()
        hashMap["en"] = FAQBean()
        val dis = Observable.just(getLanKey())
            .flatMap { lan ->
                val result = getFAQFromAssert()
                val faqMap: FAQMap? = convertJson(result, FAQMap::class.java)
                if (faqMap?.getData() != null && faqMap.getData()!!.containsKey(lan)) {
                    Observable.just(faqMap.getData()!![lan])
                } else Observable.just(null)
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                setupFAQListView(it.orEmpty())
            }, {

            })
    }

    override fun onStart() {
        super.onStart()
    }

    private fun onClickLeft(view : View) {
        if (mIsRoot) {
            finish()
        } else {
            mIsRoot = true
            resetFAQList(mRootFAQs)
        }
    }

    override fun onRestart() {
        super.onRestart()
    }

    override fun onResume() {
        super.onResume()
    }

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

    private fun setupFAQListView(faqBeans: List<FAQBean>) {
        val layoutManager = LinearLayoutManager(this)
        binding.rcvFAQ.layoutManager = layoutManager
        mAdapter = FAQAdapter()
        mAdapter!!.setListener(object : FAQAdapter.OnFAQItemClickListener {
            override fun onItemClick(position: Int, data: FAQBean?) {
                if (data != null && (data.children != null && data.children!!.isNotEmpty())) {
                    mIsRoot = false
                    /*titleString = data.title.toString()
                    setTitle()*/
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

    private fun resetFAQList(faqBeans: List<FAQBean>) {
        if ((faqBeans.isNotEmpty())) {
            mAdapter?.setData(faqBeans)
        }
    }

    /**
     * 获取国家码 cn
     */
    private fun getCountryZipCode(): String {
        val locale: Locale = com.dylanc.longan.application.context.resources.configuration.locale;
        val countryZipCode = locale.country;
        return countryZipCode
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

    override fun onPause() {
        super.onPause()
    }

    override fun onStop() {
        super.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    companion object {
        fun start(from: Context) {
            from.startActivity<MineFAQActivity>()
        }
    }
}
