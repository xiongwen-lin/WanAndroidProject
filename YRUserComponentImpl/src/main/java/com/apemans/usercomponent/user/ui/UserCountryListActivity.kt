package com.apemans.usercomponent.user.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alibaba.android.arouter.facade.annotation.Route
import com.apemans.yruibusiness.ui.toolbar.setToolbar
import com.apemans.userapi.paths.ACTIVITY_PATH_SELECT_COUNTRY
import com.apemans.userapi.paths.KEY_COUNTRY_CODE
import com.apemans.userapi.paths.KEY_COUNTRY_NAME
import com.apemans.usercomponent.R
import com.apemans.usercomponent.databinding.UserActivityCountryBinding
import com.apemans.usercomponent.user.adapter.SortAdapter
import com.apemans.usercomponent.user.entry.PinyinComparator
import com.apemans.usercomponent.user.entry.SortModel
import com.apemans.usercomponent.user.view.ClearEditText
import com.apemans.usercomponent.user.view.SideBar
import com.apemans.usercomponent.user.widget.CharacterParser
import java.util.*

/**
 * Android使用RecyclerView实现（仿微信）的联系人A-Z字母排序和过滤搜索功能:
 * 1、支持字母、汉字搜索
 * 2、全局使用一个RecyclerView，根据查询条件过滤数据源，然后更新列表并展示
 * 3、拼音解析使用了jar包，见libs目录
 * 4、本例可使用jar包(PinyinUtils.java类)、CharacterParser.java两种形式来解析汉字，详见说明
 *
 * GitHub：https://github.com/xupeng92/SortRecyclerView
 *
 * CSDN：http://blog.csdn.net/SilenceOO/article/details/75661590?locationNum=5&fps=1
 */
@Route(path = ACTIVITY_PATH_SELECT_COUNTRY)
class UserCountryListActivity : UserBaseActivity<UserActivityCountryBinding>() {
    private var mClearEditText: ClearEditText? = null
    private var mRecyclerView: RecyclerView? = null
    private var sideBar: SideBar? = null
    private var dialog: TextView? = null
    var manager: LinearLayoutManager? = null
    private var adapter: SortAdapter? = null
    private var sourceDataList: MutableList<SortModel>? = null

    /**
     * 根据拼音来排列RecyclerView里面的数据类
     */
    private var pinyinComparator: PinyinComparator? = null
    protected override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.user_activity_country)
        initView()
    }

    private fun initView() {
        setToolbar {
            title = resources.getString(R.string.country_list_title)
            leftIcon(R.drawable.left_arrow_black, ::onClickLeft)
        }
        //不使用jar包来解析汉字
        characterParser = CharacterParser.instance
        pinyinComparator = PinyinComparator()
        sideBar = findViewById<View>(R.id.sideBar) as SideBar?
        dialog = findViewById<View>(R.id.dialog) as TextView?
        sideBar?.setTextView(dialog)

        //设置右侧SideBar触摸监听
        sideBar?.setOnTouchingLetterChangedListener(object :
            SideBar.OnTouchingLetterChangedListener {
            override fun onTouchingLetterChanged(s: String?) {
                //该字母首次出现的位置
                val position: Int? = s?.get(0)?.let { adapter?.getPositionForSection(it.toInt()) }

                if (position != -1) {
                    if (position != null) {
                        manager?.scrollToPositionWithOffset(position, 0)
                    }
                }
            }
        })
        mRecyclerView = findViewById<View>(R.id.recyclerView) as RecyclerView?
        sourceDataList = filledData(resources.getStringArray(R.array.dataArray))

        // 根据a-z进行排序源数据
        Collections.sort(sourceDataList, pinyinComparator)

        //RecyclerView配置manager
        manager = LinearLayoutManager(this)
        manager!!.orientation = LinearLayoutManager.VERTICAL
        mRecyclerView?.layoutManager = manager
        adapter = SortAdapter(this, sourceDataList)
        mRecyclerView?.adapter = adapter

        //item点击事件
        adapter!!.setOnItemClickListener(object : SortAdapter.OnItemClickListener {
            override fun onItemClick(view: View?, position: Int) {
                /*Toast.makeText(
                    this@UserCountryListActivity,
                    (adapter!!.getItem(position) as SortModel).name,
                    Toast.LENGTH_SHORT
                ).show()*/
                var str = (adapter!!.getItem(position) as SortModel).name
                var stringName: List<String>? = str?.split(",")
                stringName?.get(0)?.let { setResultForActivity(it, stringName?.get(1)) }
            }
        })
        mClearEditText = findViewById<View>(R.id.filter_edit) as ClearEditText?

        //根据输入框输入值的改变来过滤搜索
        mClearEditText?.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                //当输入框里面的值为空，更新为原来的列表，否则为过滤数据列表
                filterData(s.toString())
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable) {}
        })
    }

    private fun onClickLeft(view : View) {
        finish()
    }

    private var characterParser: CharacterParser? = null

    /**
     * 为ListView填充数据
     */
    private fun filledData(data: Array<String>): MutableList<SortModel> {
        val mSortList: MutableList<SortModel> = ArrayList<SortModel>()
        for (i in data.indices) {
            val sortModel = SortModel()
            sortModel.name = data[i]
            //sortModel.setName(data[i])
            val pinyin = characterParser!!.getSelling(data[i])
            val sortString = pinyin.substring(0, 1).toUpperCase()
            if (sortString.matches(Regex("[A-Z]"))) {
                sortModel.letters = sortString.toUpperCase()
                //sortModel.setLetters(sortString.toUpperCase())
            } else {
                sortModel.letters = "#"
                //sortModel.setLetters("#")
            }
            mSortList.add(sortModel)
        }

        return mSortList
    }

    /**
     * 根据输入框中的值来过滤数据并更新ListView
     */
    private fun filterData(filterStr: String) {
        var filterDataList: MutableList<SortModel>? = ArrayList<SortModel>()
        if (TextUtils.isEmpty(filterStr)) {
            filterDataList = sourceDataList
        } else {
            filterDataList!!.clear()
            for (sortModel in sourceDataList!!) {
                val name: String? = sortModel.name
                if (name != null) {
                    if (name.indexOf(filterStr) != -1 || characterParser!!.getSelling(name).startsWith(filterStr)) {
                        filterDataList.add(sortModel)
                    }
                }
            }
        }
        // 根据a-z进行排序
        Collections.sort(filterDataList, pinyinComparator)
        adapter?.updateList(filterDataList)
    }

    fun setResultForActivity(countryName: String, countryCode: String) {
        var intent: Intent = Intent()
        intent.putExtra(KEY_COUNTRY_NAME, countryName)
        intent.putExtra(KEY_COUNTRY_CODE, countryCode.toInt())
        setResult(RESULT_OK, intent)
        finish()
    }

    companion object {
        fun start(from: Activity, requestCode: Int) {
            var intent = Intent(from, UserCountryListActivity::class.java)
            from.startActivityForResult(intent, requestCode)
        }
    }
}
