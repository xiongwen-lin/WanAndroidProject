```
    IPC设备模块接口
```

##### [![Join the chat at https://gitter.im/alibaba/ARouter](https://badges.gitter.im/alibaba/ARouter.svg)](https://gitlab.apeman.com.cn/sz/app/android/pro/DeviceManagerModuleApi) [![Hex.pm](https://img.shields.io/hexpm/l/plug.svg)](https://www.apache.org/licenses/LICENSE-2.0)

---

#### 最新版本

模块|DeviceManagerModuleApi
---|---
最新版本|1.0.0

#### Demo展示

##### [Demo 仓库（develop分支)](https://gitlab.apeman.com.cn/zhengruidong/project-zrd/-/tree/develop)
<!-- 、[Demo Gif](https://raw.githubusercontent.com/alibaba/ARouter/master/demo/arouter-demo.gif) -->

#### 一、功能介绍
1. **支持Ipc设备相关功能接口**
2. **支持后台Ipc相关请求接口**
3.  **项目采用kotlin语言开发**


#### 二、典型应用
1. 项目实现Ipc相关功能UI、开发需要使用到Ipc功能、Ipc模型

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
        implementation("com.apemans:YRConfigNetworkComponentApi:+")
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
    // 1. Ipc接口调用，通过路由方式，调用DeviceManagerService
    val deviceService: DeviceManagerService by routerServices()
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