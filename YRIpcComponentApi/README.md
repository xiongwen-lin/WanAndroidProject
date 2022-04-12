```
    IPC控制模块Api接口
```

##### [![Join the chat at https://gitter.im/alibaba/ARouter](https://badges.gitter.im/alibaba/ARouter.svg)](https://gitlab.apeman.com.cn/sz/app/android/pro/IpcControlModuleApir) [![Hex.pm](https://img.shields.io/hexpm/l/plug.svg)](https://www.apache.org/licenses/LICENSE-2.0)

---

#### 最新版本

模块|IpcControlModuleApi
---|---
最新版本|1.0.0

#### Demo展示

##### [Demo 仓库（develop分支)](https://gitlab.apeman.com.cn/zhengruidong/project-zrd/-/tree/develop)
<!-- 、[Demo Gif](https://raw.githubusercontent.com/alibaba/ARouter/master/demo/arouter-demo.gif) -->

#### 一、功能介绍
1. **支持Ipc控制相关api，分装不同方案的sdk，提供统一的控制接口**
2. **项目采用kotlin语言开发**


#### 二、典型应用
1. 根据方案类型，创建Ipc控制方案并返回统一Api接口（实现方案参考设计原型和ipcChipProxy模块）

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
        implementation project(path: ':YRIpcControlComponentApi')
    }
    ```

2. 初始化SDK
    ``` kotlin
    /**
     * 初始化
     * 1、调用接口，初始化配置，具体配置参数如下。
     */
    ```

3. 操作
    ``` kotlin
    // 1. Ipc控制接口调用
    val ipcControlService by routerServices<IpcControlManagerService>()
   
    //2. 根据方案类型，生产对应的Ipc控制接口（IIpcSchemeCore），如调用Ipc自有方案
    ipcControlService.createIpcSchemeCore(IPC_SCHEME_TYPE_SELF_DEVELOP)
    ```

4. 添加混淆规则(如果使用了Proguard)
    ``` 
    // 已在工程proguard-rules.pro中加入依赖，在引入工程中打开混淆配置即可
    ```

#### 四、中间件服务

**目录**<br />
[1. 模块](#1)<br />
[2. 使用](#2)<br />
[3. 方法](#3)<br />
&emsp;[3.1 startp2pconnection](#3.1)<br />
&emsp;[3.2 removep2pconnection](#3.2)<br />
&emsp;[3.3 checkp2pconnectionexist](#3.3)<br />
&emsp;[3.4 startnetspottcp](#3.4)<br />
&emsp;[3.5 startnetspotp2p](#3.5)<br />
&emsp;[3.6 removenetspot](#3.6)<br />
&emsp;[3.7 checkisnetspotmode](#3.7)<br />
&emsp;[3.8 getnetspotdeviceinfo](#3.8)<br />
&emsp;[3.9 sendstartnetspotpaircmd](#3.9)<br />
&emsp;[3.10 sendgetnetspotpairstatecmd](#3.10)<br />
&emsp;[3.11 sendheartbeatcmd](#3.11)<br />
&emsp;[3.12 updatenetspotdeviceconfigure](#3.12)<br />
&emsp;[3.13 checknetspotssidvalid](#3.13)<br />


## <a name="1"> 1. 模块 </a>
协议地址：**yrcx://yripccomponentdevice**


## <a name="2"> 2. 使用 </a>
支持同步、异步、监听方式调用


## <a name="3"> 3. 方法 </a>
### <a name="3.1"> 3.1 startp2pconnection </a>

**功能:** 进行p2p连接

**参数:** 
```json     
{
    "extra": "{\"deviceId\":\"\",\"parentDeviceId\":\"\",\"model\":\"\",\"uid\":\"\",\"hbDomain\":\"\",\"hbServer\":0,\"hbPort\":0,\"secret\":\"\",\"online\":false}"
}
```

**返回值:** boolean

### <a name="3.2"> 3.2 removep2pconnection </a>
**功能:** 移除p2p连接

**参数:** 
```json  
{
    "deviceId": ""
}
```

**返回值:** boolean

### <a name="3.3"> 3.3 checkp2pconnectionexist </a>
**功能:** 判断p2p连接是否存在

**参数:** 
```json 
{
    "deviceId": ""
}
```

**返回值:** boolean

### <a name="3.4"> 3.4 startnetspottcp </a>
**功能:** 进行tcp协议直连

**参数:**  
```json 
{
    "deviceSsid": ""
}
```

**返回值:** boolean

### <a name="3.5"> 3.5 startnetspotp2p </a>
**功能:** 进行p2p协议直连

**参数:**  
```json 
{
    "deviceSsid": "", 
    "bleDeviceId": "", 
    "deviceModel": ""
}
```

**返回值:** boolean

### <a name="3.6"> 3.6 removenetspot </a>
**功能:** 断开直连

**参数:**  
```json 
{
    "deviceId": ""
}
```

**返回值:** boolean

### <a name="3.7"> 3.7 checkisnetspotmode </a>
**功能:** 判断直连是否存在

**参数:** 

**返回值:** boolean

### <a name="3.8"> 3.8 getnetspotdeviceinfo </a>
**功能:** 获取当前直连信息

**参数:** 

**返回值:**  
```json 
"{\"deviceId\": \"\",\"model\": \"\",\"apDeviceId\": \"\",\"ssid\": \"\",\"bleDeviceId\": \"\"}"
```

### <a name="3.9"> 3.9 sendstartnetspotpaircmd </a>
**功能:** 发送ap热点配网命令

**参数:**  
```json 
{
    "extra": "{\"uid\":\"\",\"ssid\":\"\",\"psd\":\"\",\"region\":\"\",\"zone\":\"\",\"encrypt\":\"\"}"
}
```

**返回值:**  
```json 
{
    "code": 0, 
    "data": "{"code": 1, "data": 0}"
}
```

### <a name="3.10"> 3.10 sendgetnetspotpairstatecmd </a>
**功能:** 发送获取ap热点配网状态命令

**参数:**  
```json 
```

**返回值:**  
```json 
{
    "code": 0, 
    "data": "{"code": 1, "data": 0}"
}
```

### <a name="3.11"> 3.11 sendheartbeatcmd </a>
**功能:** 发送心跳命令

**参数:**  
```json 
{
    "deviceId": ""
}
```

**返回值:**  
```json 
{
    "code": 0, 
    "data": "{"code": 1, "data": 0}"
}
```

### <a name="3.12"> 3.12 updatenetspotdeviceconfigure </a>
**功能:** 更新直连配置信息

**参数:**
```json 
{
    "uid": ""
}
```

**返回值:** boolean

### <a name="3.12"> 3.12 checknetspotssidvalid </a>
**功能:** 判断设备ssid是否合法

**参数:**
```json 
{
    "deviceSsid": ""
}
```

**返回值:** boolean

#### 五、其他

1. 沟通和交流

    1. 维护：独角鲸 ；邮箱：zhengruidong@apemans.com