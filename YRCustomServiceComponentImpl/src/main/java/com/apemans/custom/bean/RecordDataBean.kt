package com.apemans.custom.bean

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "chat_record")
data class RecordDataBean(
    @PrimaryKey(autoGenerate = true) var id: Int = 0,

    @ColumnInfo(name = "user_account") var userAccount: String,

    @ColumnInfo(name = "user_type") var userType: Int,

    /**
     * 如果是视频或者图片，此处保存的是地址
     */
    @ColumnInfo(name = "content") var content: String,

    @ColumnInfo(name = "content_type") var contentType: Int,

    @ColumnInfo(name = "date") var date: String,

    @ColumnInfo(name = "status") var status: Int,

    @ColumnInfo(name = "msg_id") var msgId: Int = -1,

    )

annotation class UserType {
    companion object {
        //机器人（热点问题）
        const val ROBOT = 1
        //用户
        const val CUSTOMER = 2
        //客服
        const val SERVICE = 3
        const val SYSTEM = 4
    }
}

annotation class ContentType {
    companion object {
        const val TXT = 1
        const val IMAGE = 2
        const val LOG_FILE = 3
        const val VIDEO = 4
        const val TIME_STAMP = 5
        const val HOT_QA = 6
    }
}

annotation class Status {
    companion object {
        /**
         * 消息发送异常
         */
        const val NETWORK_EXCEPTION = 504
        const val SUCCESS = 200
    }
}
