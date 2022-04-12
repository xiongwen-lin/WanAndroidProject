/*
 * * Copyright(c)2021.蛮羊 Inc.All rights reserved.
 */

package com.apemans.custom.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.apemans.business.apisdk.client.bean.YRApiResponse
import com.apemans.yruibusiness.base.BaseComponentViewModel
import com.apemans.custom.bean.*
import com.apemans.custom.repository.CustomerRepository
import com.apemans.custom.util.QTimeUtils
import com.apemans.customserviceapi.webapi.CreateFeedbackBody
import com.apemans.logger.YRLog
import com.dylanc.longan.application
import com.dylanc.longan.logError
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

/**
 * @Auther: 蛮羊
 * @datetime: 2021/11/9
 * @desc:
 */
class CustomViewModel : com.apemans.yruibusiness.base.BaseComponentViewModel() {
    val FEEDBACK_CODE: Int = 7

    companion object {
        //间隔时间时长
        const val INTERVAL = 5 * 60
    }


    private val formatterShow: SimpleDateFormat = SimpleDateFormat("hh:mm a")


    var recordList = ArrayList<RecordDataBean>()
    var recordDao = RecordDatabase.getDatabase(application).chatRecordDao()


    //消息列表
    private val items = ArrayList<RecordDataBean>()


    fun getChatRecord(userAccount: String, block: (chatList: ArrayList<RecordDataBean>) -> Unit) {
      getChatRecordDB(userAccount, block)

    }

    /**
     * 获取数据库中的聊天记录
     */
    private fun getChatRecordDB(
        userAccount: String,
        block: (chatList: ArrayList<RecordDataBean>) -> Unit
    ) {
        viewModelScope.launch {
            items.clear()
            recordList = recordDao.queryAllRecord(userAccount) as ArrayList<RecordDataBean>
            val currentDate = formatterShow.format(Date(System.currentTimeMillis()))
            val hotQuestion = RecordDataBean(
                0,
                userAccount,
                UserType.ROBOT,
                "",
                ContentType.HOT_QA,
                QTimeUtils.getToday(),
                Status.SUCCESS
            )
            //如果没有聊天记录--则插入一条hq数据
            if (recordList.isEmpty()) {
                recordDao.insert(hotQuestion)
                items.add(hotQuestion)
                block(items)
                return@launch
            } else {
                block(recordList)
            }
        }
    }

    fun feedbackCheck(): LiveData<YRApiResponse<Any>> {
        Log.e("feedbackCheck", "feedbackCheck-----start")
        return CustomerRepository.checkFeedback()
            .catch { e ->
                YRLog.e { "feedbackCheck--error $e" }
            }
            .asLiveData()
    }

    /**
     * 上传内容到服务器
     * @param recordDataBean  上传的内容，如果是文字信息就是文本内容，如果是图片或者视频就是imageUrl
     */
    fun feedback(recordDataBean: RecordDataBean ,imageUrl :String ): LiveData<YRApiResponse<Any>> {
        Log.e("feedback", "feedback-----start")
        var productId: Int = 7 // 产品id
        val createFeedback = CreateFeedbackBody(
            FEEDBACK_CODE,
            productId,
            recordDataBean.userAccount,
            recordDataBean.content,
            imageUrl
        )
        return CustomerRepository.createFeedback(createFeedback)
            .catch { e ->
                YRLog.e { "feedbackCheck--error $e" }
            }
            .asLiveData()
    }

    /**
     * 获取消息列表接口
     * rows为请求消息个数 eg：20
     * time为某一消息时间，意为从该消息起后几条消息，分页查询功能，如果第一页直接传零 eg：0
     * type为消息类型，客服模块默认为7，更多消息类型见showdoc eg：7
     */
     fun getMessageLists(): LiveData<MutableList<RecordDataBean>> {
        return CustomerRepository.getMessageList(50, 0, FEEDBACK_CODE).asLiveData()
    }


    /**
     * 插入数据
     * @param chat 待插入的数据
     */
    fun insertChatRecord(chat: RecordDataBean, block: (id: Long) -> Unit) {
        viewModelScope.launch {
            val id = recordDao.insert(chat)
            block(id)
        }

    }

    /**
     * 获取最新的一条数据
     */
    private fun getLastRecord(block: (chat: RecordDataBean) -> Unit) {
        viewModelScope.launch {
            block(recordDao.queryLastRecord())
        }

    }


    /**
     * 更新数据
     */
    fun updateData(newRecord: RecordDataBean) {
        viewModelScope.launch {
            recordDao.update(newRecord)
        }
    }


    fun upLoadPicture(userId: String, userName: String, picPath: String) : LiveData<FeedbackResult>{
        return CustomerRepository.upLoadPicture(userId, userName, picPath)
            .catch {
                logError(it.message)
            }
            .asLiveData()
    }
}