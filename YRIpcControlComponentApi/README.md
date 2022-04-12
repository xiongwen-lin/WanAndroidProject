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