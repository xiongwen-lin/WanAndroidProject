```
    IPC 自研方案模块
```

##### [![Join the chat at https://gitter.im/alibaba/ARouter](https://badges.gitter.im/alibaba/ARouter.svg)](https://gitlab.apeman.com.cn/sz/app/android/pro/ipcSelfDevelopedSchemeCore) [![Hex.pm](https://img.shields.io/hexpm/l/plug.svg)](https://www.apache.org/licenses/LICENSE-2.0)

---

#### 最新版本

模块|ipcSelfDevelopedSchemeCore
---|---
最新版本|1.0.0

#### Demo展示

##### [Demo 仓库（develop分支)](https://gitlab.apeman.com.cn/zhengruidong/project-zrd/-/tree/develop)
<!-- 、[Demo Gif](https://raw.githubusercontent.com/alibaba/ARouter/master/demo/arouter-demo.gif) -->

#### 一、功能介绍
1. **支持Ipc自有方案的sdk接口、配置缓存、链接缓存**
2. **项目采用kotlin语言开发**


#### 二、典型应用
1. Ipc自有方案实现

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
        implementation project(path: ':YRIpcSelfDevelopSchemeImpl')
    }
    ```

2. 初始化SDK
    ``` kotlin
    /**
     * 初始化P2P
     * 1、调用IIpcSchemeCore的init()接口，初始化配置，具体配置参数如下。
     */
    private fun initP2P() {
        /**
         * 自有Ipc方案初始化配置，根据不同协议传入对应参数，具体协议配置key说明如下
         * 自有P2P协议配置
         * IPC_SCHEME_KEY_P2P_URL：自有平台返回P2P链接地址
         * IPC_SCHEME_KEY_P2P_PORT：自有平台返回P2P链接端口
         * IPC_SCHEME_KEY_UID：自有平台返回账号uid
        */
        val param = mutableMapOf<String, Any>()
        param.put(IPC_SCHEME_KEY_P2P_URL, "52.83.89.23")
        param.put(IPC_SCHEME_KEY_P2P_PORT, 9100)
        param.put(IPC_SCHEME_KEY_UID, "b02343fa4bfe870e")
        ipcControlService.createIpcSchemeCore(IPC_SCHEME_TYPE_SELF_DEVELOP)?.init(param)
    }
    ```

3. 操作
    ``` kotlin
    // 1. 自有Ipc方案设备连接
    /**
     * 自有Ipc方案设备连接，根据不同协议传入对应参数，具体协议配置key说明如下
     * IPC_SCHEME_KEY_CONNECTION_PROTOCOL：连接设备所使用的协议类型，如P2P、热点TCP、热点P2P
     * IPC_SCHEME_KEY_DEVICE_ID：设备Id
     */
    YRLog.d("d")
    YRLog.d("d", "dd")
    //2. 常用日志输出，日志类型定为网络日志
    /**
     * 自有Ipc方案设备连接，根据不同协议传入对应参数，具体协议配置key说明如下
     * IPC_SCHEME_KEY_CONNECTION_PROTOCOL：连接设备所使用的协议类型，如P2P、热点TCP、热点P2P
     * IPC_SCHEME_KEY_DEVICE_ID：设备Id
     */
    val ipcControlService by routerServices<IpcControlManagerService>()
    ipcControlService.createIpcSchemeCore(IPC_SCHEME_TYPE_SELF_DEVELOP)?.connect(param)
   
    //3. 自有Ipc方案命令发送
    /**
     * 自有Ipc方案命令发送，具体用法如下说明：
     * 根据定义好的功能点和命令所需参数，生成如下格式的json文本进行传送，并在回调接口接收命令执行结果
     * 发送命令json格式：
     * {"dpId" : {"deviceId" : "xxx", "data" : yyy}}
     * dpId:对应命令功能点，如"101"设置led指示灯开关
     * deviceId:设备id
     * data:命令所需参数，如true打开led指示灯
     * 示例如下：
     * {"101" : {"deviceId" : "123", "data" : true}}
     *
     * 命令执行结果数据模型IpcDPCmdResult
     * 命令执行结果格式：
     * {"deviceId" : "xxx", "data" : yyy}
     * deviceId:设备id
     * data:命令执行结果，内容为功能点和对应命令执行结果的键值对的json文本格式
     * {"dpId" : xxx} 如{"101":true} 设置led指示灯开
     * 示例如下：
     * {"deviceId":"55ffc4135dcc4fb1029a2c98e8096c22", "data":"{\"101\":true}"}
     */
     ipcControlService.createIpcSchemeCore(IPC_SCHEME_TYPE_SELF_DEVELOP)?.sendCmd(cmd, object :
                        IIpcSchemeResultCallback {
                        override fun onSuccess(code: Int, result: String?) {
                        }

                        override fun onError(code: Int, error: String?) {
                        }
                    })
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
