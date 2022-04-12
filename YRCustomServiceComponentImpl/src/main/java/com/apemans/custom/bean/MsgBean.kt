/*
 * * Copyright(c)2021.蛮羊 Inc.All rights reserved.
 */
package com.apemans.custom.bean

/**
 * @Auther: 蛮羊
 * @datetime: 2021/10/16
 * @desc: 3种消息：热点问题消息，顾客的消息，机器人消息
 * accout：区分机器人还是顾客
 * 消息类型：文本消息和图片消息和热点消息 0.1.2
 * 消息内容：文本，图片本地路径加载
 * 消息时间：用来排序
 * 消息是谁发送的:机器人（热点问题），我，机器人
 * 状态：是否发送成功
 */
data class MsgBean (
    var time: Long = 0,
    var msgId:Int= 0,
    var account: String,
    var msgType:Int=0,
    var content: String,
    var feedbackStatus :Boolean
)