package com.apemans.logger

import android.content.Context
import com.apemans.logger.xlog.LogLevel
import com.apemans.logger.xlog.XLogHelper
import com.dianping.logan.Logan
import com.dianping.logan.LoganConfig

/**
 * 日志管理类
 */
object YRLogManager {

    //设置日志输出模式是否未debug模式
    var DEBUG : Boolean = false

    /**
     * 初始化日志设置
     * LoganConfig 配置参数
     * cachePath mmap缓存路径
     * logFilePath file文件路径
     * maxFileSize 删除文件最大值
     * day 删除天数
     * encryptKey 128位ase加密Key
     * encryptIv 128位aes加密IV
     * debug 是否为debug模式, true 表示使用调试模式，日志输出到控制台。false表示关闭调试模式，日志输出到文件中
     */
    fun init(debug : Boolean, block : (builder : LoganConfig.Builder) -> LoganConfig.Builder) {
        DEBUG = debug
        Logan.init(block.invoke(LoganConfig.Builder()).build());
        Logan.setDebug(DEBUG);
        Logan.setOnLoganProtocolStatus { cmd, code ->
            YRLog.d("clogan > cmd : $cmd | code : $code");
        };
    }

    /**
     * 初始化日志设置
     * YRLogConfig 配置参数
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
     */
    fun init(context: Context, configure: YRLogConfigure?) {
        if (context == null) {
            return
        }
        if (!checkLogConfigureValid(configure)) {
            XLogHelper.create(context)
                .build()
        }
        with(XLogHelper.create(context)) {
            configure?.let {
                if (!it.tag.isNullOrEmpty()) {
                    setTag(configure?.tag.orEmpty())
                }
                setDebug(it.debug)
                setConsoleLogOpen(it.consoleLogOpen)
                setOneFileEveryday(it.oneFileEveryday)
                if (!it.cachePath.isNullOrEmpty()) {
                    setCachePath(configure?.cachePath.orEmpty())
                }
                if (!it.logPath.isNullOrEmpty()) {
                    setLogPath(configure?.logPath.orEmpty())
                }
                if (!it.namePreFix.isNullOrEmpty()) {
                    setNamePreFix(configure?.namePreFix.orEmpty())
                }
                setModel(it.model)
                setMaxFileSize(it.maxFileSize.toFloat())
                setLogLevel(it.logLevel)
                if (!it.pubKey.isNullOrEmpty()) {
                    setNamePreFix(configure?.pubKey.orEmpty())
                }
                setMaxAliveTime(it.maxAliveTime)
                setCacheDays(it.cacheDays)
            }
            build()
        }
    }

    private fun checkLogConfigureValid(configure: YRLogConfigure?) : Boolean {
        return configure?.let {
            configure == null || configure.cachePath.isNullOrEmpty() ||
                    configure.logPath.isNullOrEmpty()
        } ?: false
    }
}