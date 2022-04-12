```
    日志打印收集工具
```

##### [![Join the chat at https://gitter.im/alibaba/ARouter](https://badges.gitter.im/alibaba/ARouter.svg)](https://gitlab.apeman.com.cn/sz/app/android/pro/Logger) [![Hex.pm](https://img.shields.io/hexpm/l/plug.svg)](https://www.apache.org/licenses/LICENSE-2.0)

---

#### 最新版本

模块|Logger
---|---
最新版本|1.0.0

#### Demo展示

##### [Demo 仓库（develop分支)](https://gitlab.apeman.com.cn/zhengruidong/project-zrd/-/tree/develop)
<!-- 、[Demo Gif](https://raw.githubusercontent.com/alibaba/ARouter/master/demo/arouter-demo.gif) -->

#### 一、功能介绍
1. **支持android标准log输出接口**
2. **支持日志上传**
3. **支持更简易的log调用**
4. **项目采用kotlin语言开发，提供更切合kotlin的日志输出方式**


#### 二、典型应用
1. app开发需要使用到Log打印，上传

#### 三、基础功能
1. 添加依赖和配置
    ``` gradle
    // 版本支持
    android {
        compileSdkVersion 30
        buildToolsVersion "30.0.3"

    defaultConfig {
        minSdkVersion 21
        targetSdkVersion 30
    }

    dependencies {
        // 导入依赖
        implementation ("com.apemans:YRLogger:+")
    }
    ```

2. 初始化SDK
    ``` kotlin
    /**
     * 初始化Logger
     * 1、调用YRApiManager.init()接口，初始化配置，具体配置参数如下。
     */
    private fun initYRLoggerSDK() {
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
        var cachePath = getFilesDir().getAbsolutePath()
        var logFilePath = getExternalFilesDir(null)!!.getAbsolutePath() + File.separator + "yrlog_v1";
        var maxFileSize = 10 * 1024 * 1024L
        YRLogManager.init(true) {
            it.setCachePath(cachePath)
                .setPath(logFilePath)
                .setMaxFile(maxFileSize)
                .setDay(10)
                .setEncryptKey16("0123456789012345".encodeToByteArray())
                .setEncryptIV16("0123456789012345".encodeToByteArray())
        }
    }
    ```

3. 操作
    ``` kotlin
    // 1. 常用日志输出，默认日志格式为普通日志类型
    YRLog.d("d")
    YRLog.d("d", "dd")
    //常用日志输出，日志类型定为网络日志
    YRLog.d("d", "dd", logType = YRLog.LOG_TYPE_NETWORK)
   
    //2. 使用Lambda表达式来生成日志信息，并可以设置日志类型
    YRLog.d {
        "-->> debug log 2"
    }
    YRLog.d(YRLog.LOG_TYPE_NETWORK) {
        "-->> debug log 3 time " + System.currentTimeMillis()
    }
    ```

5. 添加混淆规则(如果使用了Proguard)
    ``` 
    // 已在工程proguard-rules.pro中加入依赖，在引入工程中打开混淆配置即可
    ```

#### 四、Q&A

1. 示例1

    示例

2. 示例2

    - 示例
    - 示例
        1. 示例
        2. 示例

#### 五、其他

1. 沟通和交流

    1. 维护：独角鲸 ；邮箱：zhengruidong@apemans.com
