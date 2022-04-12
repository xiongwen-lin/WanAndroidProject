package com.apemans.logger

import com.apemans.logger.xlog.LogLevel
import com.apemans.logger.xlog.LogModel

/***********************************************************
 * 作者: zhengruidong@apemans.com
 * 日期: 2022/1/7 5:30 下午
 * 说明:
 *
 * 备注:YRLogConfigure 配置类
 *
 * tag 日志的tag
 * debug 是否是debug 模式
 * consoleLogOpen 是否打印控制台日志
 * oneFileEveryday 是否每天一个日志文件
 * defCachePath 默认的位置
 * cachePath mmap 位置 默认缓存的位置,
 * logPath 实际保存的log 位置
 * namePreFix 文件名称前缀 例如该值为TEST，生成的文件名为：TEST_20170102.xlog
 * model 写入文件的模式
 * maxFileSize 最大文件大小
 * 默认情况下，所有日志每天都写入一个文件。可以通过更改max_file_size将日志分割为多个文件。
 * 单个日志文件的最大字节大小，默认为0，表示不分割
 * 最大 当文件不能超过 10M
 * logLevel 日志级别,debug 版本下建议把控制台日志打开，日志级别设为 Verbose 或者 Debug, release 版本建议把控制台日志关闭，日志级别使用 Info.
 * pubKey 通过 python gen_key.py 获取到的公钥
 * maxAliveTime 单个文件最大保留时间 最小 1天 默认时间 10天
 * cacheDays 缓存的天数  一般情况下填0即可。非0表示会在 _cachedir 目录下存放几天的日志。原来缓存日期的意思是几天后从缓存目录移到日志目录
 ***********************************************************/
class YRLogConfigure {
    var tag = "log_tag"
    var debug = true
    var consoleLogOpen = true
    var oneFileEveryday = true
    val defCachePath = ""
    var cachePath = ""
    var logPath = ""
    var namePreFix = "log"
    var model = LogModel.Async
    var maxFileSize = 0L
    var logLevel = LogLevel.LEVEL_INFO
    var pubKey = ""
    var maxAliveTime = 10
    var cacheDays = 0
}