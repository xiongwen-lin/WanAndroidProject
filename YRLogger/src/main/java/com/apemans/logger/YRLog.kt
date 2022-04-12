package com.apemans.logger

import android.util.Log
import com.dianping.logan.Logan

/**
 * 日志输出接口类
 */
object YRLog {

     /**
      * 日志类型（LOG_TYPE_EXCEPTION：异常日志；LOG_TYPE_CUSTOM：普通日志；LOG_TYPE_NETWORK：网络日志）
      */
     const val TAG = "debug"
     const val LOG_TYPE_EXCEPTION = 1;
     const val LOG_TYPE_CUSTOM = 2;
     const val LOG_TYPE_NETWORK = 3;

     fun v(vararg args : Any?, logType : Int = LOG_TYPE_CUSTOM) {
          if (YRLogManager.DEBUG) Log.d(TAG, args.joinToString()) else Logan.w(args.joinToString(), logType)
     }

     fun d(vararg args : Any?, logType : Int = LOG_TYPE_CUSTOM) {
          if (YRLogManager.DEBUG) Log.d(TAG, args.joinToString()) else Logan.w(args.joinToString(), logType)
     }

     fun i(vararg args : Any?, logType : Int = LOG_TYPE_CUSTOM) {
          if (YRLogManager.DEBUG) Log.d(TAG, args.joinToString()) else Logan.w(args.joinToString(), logType)
     }

     fun w(vararg args : Any?, logType : Int = LOG_TYPE_CUSTOM) {
          if (YRLogManager.DEBUG) Log.d(TAG, args.joinToString()) else Logan.w(args.joinToString(), logType)
     }

     fun e(vararg args : Any?, logType : Int = LOG_TYPE_CUSTOM) {
          if (YRLogManager.DEBUG) Log.d(TAG, args.joinToString()) else Logan.w(args.joinToString(), logType)
     }

     inline fun v(logType : Int = LOG_TYPE_CUSTOM, lazyMsg: () -> Any?) {
          if (YRLogManager.DEBUG) Log.d(TAG, lazyMsg().toString()) else Logan.w(lazyMsg().toString(), logType)
     }

     inline fun d(logType : Int = LOG_TYPE_CUSTOM, lazyMsg: () -> Any?) {
          if (YRLogManager.DEBUG) Log.d(TAG, lazyMsg().toString()) else Logan.w(lazyMsg().toString(), logType)
     }

     inline fun i(logType : Int = LOG_TYPE_CUSTOM, lazyMsg: () -> Any?) {
          if (YRLogManager.DEBUG) Log.d(TAG, lazyMsg().toString()) else Logan.w(lazyMsg().toString(), logType)
     }

     inline fun w(logType : Int = LOG_TYPE_CUSTOM, lazyMsg: () -> Any?) {
          if (YRLogManager.DEBUG) Log.d(TAG, lazyMsg().toString()) else Logan.w(lazyMsg().toString(), logType)
     }

     inline fun e(logType : Int = LOG_TYPE_CUSTOM, lazyMsg: () -> Any?) {
          if (YRLogManager.DEBUG) Log.d(TAG, lazyMsg().toString()) else Logan.w(lazyMsg().toString(), logType)
     }

}