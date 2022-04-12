package com.apemans.custom

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.apeman.customerservice.view.album.AlbumActivity

import com.apemans.yruibusiness.base.BaseComponentActivity
import com.apemans.custom.adapter.FeedbackAdapter
import com.apemans.custom.album.MediaConstant
import com.apemans.custom.bean.ContentType
import com.apemans.custom.bean.RecordDataBean
import com.apemans.custom.bean.Status
import com.apemans.custom.bean.UserType
import com.apemans.custom.databinding.CustomserviceActivityMainBinding
import com.apemans.custom.util.QTimeUtils
import com.apemans.custom.viewModel.CustomViewModel
import com.apemans.quickui.alerter.alertInfo
import com.apemans.yruibusiness.ui.toolbar.setToolbar
import com.apemans.quickui.click

import com.apemans.base.utils.NetUtil
import com.apemans.business.apisdk.client.define.HttpCode

import com.apemans.custom.util.FileUtil


/**
 * 客服反馈页面
 * 1、加载消息内容                       getMsgBean()
 * 2、发送消息                          sendMsg()
 *    2.1、发消息先保存本地，             insertChatRecord()
 *    2.2、发送消息是否有权限,避免频繁发    viewModel.feedbackCheck
 *    2.3、真正发送反馈消息
 *    2.4：验证异常，更新反馈状态         updateStatus()
 * 3、发送图片、视频
 *    3.1 获取数据后发送                sendMediaFile
 *    3.2 先上传图片                   viewModel.upLoadPicture
 *    3.3重复2.2， 2.3
 */
class CustomServiceActivity : com.apemans.yruibusiness.base.BaseComponentActivity<CustomserviceActivityMainBinding>() {

    /**
     * 消息数据对象
     */
    var msgList = ArrayList<RecordDataBean>()
    var feedbackAdapter: FeedbackAdapter? = null

    private lateinit var viewModel: CustomViewModel
    private var userAccount = ""
    private var userPhoto = ""
    private var uid = ""


    override fun onViewCreated(savedInstanceState: Bundle?) {
        initView()
        with(binding) {
            feedbackAdapter = FeedbackAdapter()
            rvMsgList.layoutManager = LinearLayoutManager(applicationContext)
            rvMsgList.adapter = feedbackAdapter
            feedbackAdapter?.setDataList(msgList)
            //消息重发
            feedbackAdapter?.setOnClickListener(object : FeedbackAdapter.OnItemStatusClickListener {
                override fun statusOnClick(item: RecordDataBean, position: Int) {
                    checkFeedback(item)
                }
            })
        }
        getUserInfo()
        getMsgBean()
    }

    /**
     * 获取用户信息
     */
    private fun getUserInfo() {
     /*   userAccount = UserInfo?.userAccount.toString()
        userPhoto = UserInfo?.tuyaPhoto.toString()
        uid = UserInfo?.uid.toString()*/
        feedbackAdapter?.setUserPhoto(userPhoto)
    }

    /**
     * 1、加载历史聊天内容
     */
    private fun getMsgBean() {
        viewModel = registerViewModule(CustomViewModel::class.java)
        viewModel.getChatRecord(userAccount) {
            msgList = it
            feedbackAdapter?.setDataList(msgList)
            scrollToBottom()
        }

    }

    fun initView() {
        setToolbar {
            title = resources.getString(R.string.home_person_feedback)
            leftIcon(R.drawable.ic_back, onClick = { finish() })
        }
        binding.llSend.setOnClickListener { sendMsg() }
        //选择图片,键盘收缩,输入框失去光标
        binding.btAdd.click {
            hideInputBoard(it)
            binding.etChat.clearFocus()
            binding.etChat.setBackgroundResource(R.drawable.bg_edit_text)
            binding.clFuncKit.visibility = if (binding.clFuncKit.visibility == View.VISIBLE) {
                View.GONE
            } else {
                View.VISIBLE
            }
        }
        //编辑动态更新按钮
        binding.etChat.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s?.length!! > 0) {
                    binding.btSend.setImageResource(R.drawable.ic_send_able)
                    binding.btSend.setColorFilter(resources.getColor(R.color.theme_color))
                } else binding.btSend.setImageResource(R.drawable.ic_send_unable)
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })

        binding.etChat.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                binding.clFuncKit.visibility = View.GONE
                binding.etChat.setBackgroundResource(R.drawable.select_item_cover_mask)
                scrollToBottom()
            }
        }
        binding.llFuncImg.setOnClickListener {
            startActivityForResult(
                Intent(this, AlbumActivity::class.java),
                MediaConstant.ALBUM_SELECT_CODE
            )
        }

    }

    override fun onResume() {
        /**
         * 获取客服回复消息
         */
        viewModel.getMessageLists().observe(this){ listMsg ->
            if (!listMsg.isNullOrEmpty()){
                listMsg.forEach {
                    viewModel.insertChatRecord(it) {}
                    msgList.add(it)
                }
                feedbackAdapter?.setDataList(msgList)
                feedbackAdapter?.notifyDataSetChanged()
                scrollToBottom()
            }
        }
        super.onResume()
    }

    /**
     * 2、发送消息
     */
    private fun sendMsg() {
        val content = binding.etChat.text.toString()
        if (!NetUtil.isOnline(this)) {
            alertInfo(resources.getString(R.string.help_faq_send_net_error))
            return
        }
        if (content == "") {
            alertInfo(resources.getString(R.string.help_faq_send_empty))
        } else {
            binding.etChat.setText("")
            //2.1、发消息先保存本地，
            insertChatRecord(ContentType.TXT, UserType.CUSTOMER, content) { its ->
                checkFeedback(its)
            }
        }
    }

    /**
     * 2.1、发消息先保存本地，默认发送成功，如果出现发送异常，则更新数据状态 - 调用updateStatus
     * updateStatus(RecordDataBean,Int)
     */
    private fun insertChatRecord(
        contentType: Int,
        userType: Int,
        content: String,
        block: (recordDataBean: RecordDataBean) -> Unit
    ) {

        val recordDataBean = RecordDataBean(
            0,
            userAccount,
            userType,
            content,
            contentType,
            QTimeUtils.getToday(),
            Status.SUCCESS
        )

        viewModel.insertChatRecord(recordDataBean) {
            recordDataBean.id = it.toInt()
            msgList.add(recordDataBean)
            feedbackAdapter?.setDataList(msgList)
            feedbackAdapter?.notifyDataSetChanged()
            scrollToBottom()
            block(recordDataBean)
        }
    }



    /**
     * 2.2、发送消息是否有权限,避免频繁发，再发送
     * @param imageUrl
     */
    fun checkFeedback(recordDataBean: RecordDataBean) {
        viewModel.feedbackCheck().observe(this@CustomServiceActivity) {
            if (HttpCode.SUCCESS_CODE == it.code) {
                //如果是文字，直接反馈
                if (recordDataBean.contentType == ContentType.TXT) {
                    feedback(recordDataBean, "")
                } else if (recordDataBean.contentType == ContentType.IMAGE) {
                    feedbackUpload(recordDataBean, recordDataBean.content)
                }
            } else {
                if (TextUtils.isEmpty(it.msg)) {
                    alertInfo(resources.getString(R.string.help_faq_send_Feedbacked_more_msg))
                } else {
                    alertInfo(it.msg)
                }
                updateStatus(recordDataBean, Status.NETWORK_EXCEPTION)
            }
        }
    }

    /**
     * 2.3真正发送反馈消息 ,feedback
     */
    fun feedback(recordDataBean: RecordDataBean, imageUrl: String) {
        viewModel.feedback(recordDataBean, imageUrl).observe(this@CustomServiceActivity) {
            if (HttpCode.SUCCESS_CODE != it.code) {
                updateStatus(recordDataBean, Status.NETWORK_EXCEPTION)
                if (!TextUtils.isEmpty(it.msg)) {
                    alertInfo(it.msg)
                }
            }else{
                updateStatus(recordDataBean, Status.SUCCESS)
            }
        }
    }

    /**
     * 验证异常，更新反馈状态
     */
    private fun updateStatus(recordDataBean: RecordDataBean, status: Int) {
        recordDataBean.status = status
        //更新数据库中对应数据的状态
        viewModel.updateData(recordDataBean)
        //更新items中对应数据的状态
        val position = msgList.indexOf(recordDataBean)
        msgList.remove(recordDataBean)
        msgList.add(position, recordDataBean)
        feedbackAdapter?.setDataList(msgList)
        feedbackAdapter?.notifyDataSetChanged()
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == MediaConstant.ALBUM_SELECT_CODE && resultCode == RESULT_OK) {
            val paths = data?.getStringArrayListExtra("paths")
            sendMediaFile(paths)
        }
    }

    /**
     * 发送 媒体文件
     */
    private fun sendMediaFile(paths: ArrayList<String>?) {
        paths?.forEach { filePath ->
            val extensionName = FileUtil.getExtensionName(filePath)

            if (isVideo(extensionName)) {
                insertChatRecord(ContentType.VIDEO, UserType.CUSTOMER, filePath) {
                    feedbackUpload(it, filePath)
                }
            } else {
                insertChatRecord(ContentType.IMAGE, UserType.CUSTOMER, filePath) {
                    feedbackUpload(it, filePath)
                }
            }


        }
    }


    /**
     * 上传文件
     *
     */
    private fun feedbackUpload(recordDataBean: RecordDataBean, fileName: String) {
        try {
            viewModel.upLoadPicture(uid, userAccount, fileName).observe(this) {
                if (it.resultStr == MediaConstant.SUCCESS) {
                    val imgeUrl = it.fileNameSb
                    feedback(recordDataBean, imgeUrl)
                } else {
                    updateStatus(recordDataBean, Status.NETWORK_EXCEPTION)
                }
            }
        } catch (e: Exception) {
            updateStatus(recordDataBean, Status.NETWORK_EXCEPTION)
            e.printStackTrace()
        }
    }

    /**
     * 判断是否为 视频文件
     */
    private fun isVideo(extensionName: String?): Boolean {
        return extensionName == "mp4" || extensionName == "MP4" || extensionName == "mov" || extensionName == "MOV"
                || extensionName == "avi" || extensionName == "AVI"
    }

    /**
     * 输入消息时，列表滑到底部
     */
    private fun scrollToBottom() {
        if (feedbackAdapter != null && msgList.size > 1) {
            binding.rvMsgList.scrollToPosition(feedbackAdapter!!.itemCount - 1)
        }
    }

    /**
     * 隐藏软盘
     */
    private fun hideInputBoard(v: View) {
        val imm: InputMethodManager =
            v.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(v.applicationWindowToken, 0)
    }
}