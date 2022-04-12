package com.apemans.logger.xlog

import android.content.Context
import com.tencent.mars.xlog.Log

object XLogHelper {


    init {
        System.loadLibrary("c++_shared")
        System.loadLibrary("marsxlog")
    }


    //创建
    fun create(context: Context): Builder {
        return Builder(context)
    }

    //=====================================================================================
    // v
    //=====================================================================================
    fun v(msg: String) {
        Log.v(Builder.tag, msg)
    }

    fun v(format: String, vararg obj: Any?) {
        Log.v(Builder.tag, format, *obj)
    }

    //=====================================================================================
    // f
    //=====================================================================================
    fun f(msg: String) {
        Log.f(Builder.tag, msg)
    }

    fun f(format: String, vararg obj: Any?) {
        Log.f(Builder.tag, format, *obj)
    }


    //=====================================================================================
    // e
    //=====================================================================================
    fun e(msg: String) {
        Log.e(Builder.tag, msg)
    }

    fun e(format: String, vararg obj: Any?) {
        Log.e(Builder.tag, format, *obj)
    }

    //=====================================================================================
    // w
    //=====================================================================================
    fun w(msg: String) {
        Log.w(Builder.tag, msg)
    }

    fun w(format: String, vararg obj: Any?) {
        Log.w(Builder.tag, format, *obj)
    }

    //=====================================================================================
    // w
    //=====================================================================================
    fun i(msg: String) {
        Log.i(Builder.tag, msg)
    }

    fun i(format: String, vararg obj: Any?) {
        Log.i(Builder.tag, format, *obj)
    }

    //=====================================================================================
    // d
    //=====================================================================================
    fun d(msg: String) {
        Log.d(Builder.tag, msg)
    }

    fun d(format: String, vararg obj: Any?) {
        Log.d(Builder.tag, format, *obj)
    }

    //关闭日志，不再写入
    fun close() {
        Log.appenderClose()
    }

    //当日志写入模式为异步时，调用该接口会把内存中的日志写入到文件。
    //isSync : true  为同步 flush，flush 结束后才会返回。
    //isSync : false 为异步 flush，不等待 flush 结束就返回。
    fun flush(isSync: Boolean) {
        Log.appenderFlushSync(isSync)
    }


}