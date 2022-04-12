```
    APP公工资源包
```

##### [![Join the chat at https://gitter.im/alibaba/ARouter](https://badges.gitter.im/alibaba/ARouter.svg)](https://gitlab.apeman.com.cn/sz/app/android/pro/YRResource) [![Hex.pm](https://img.shields.io/hexpm/l/plug.svg)](https://www.apache.org/licenses/LICENSE-2.0)

---

#### 最新版本

模块|YRResource
---|---
最新版本|1.0.0

#### Demo展示

##### [Demo 仓库（develop分支)](https://gitlab.apeman.com.cn/zhengruidong/project-zrd/-/tree/develop)
<!-- 、[Demo Gif](https://raw.githubusercontent.com/alibaba/ARouter/master/demo/arouter-demo.gif) -->

#### 一、功能介绍
1. **公共主题色、尺寸、图片资源定义**


#### 二、典型应用
1. 统一全局UI主题资源

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
        implementation ("com.apemans:YRResource:+")
    }
    ```

2. 主题色
    ``` xml
    /**
     * 初始化
     * 1、调用接口，初始化配置，具体配置参数如下。
     */

    <!--主题颜色-->
    <color name="theme_color">#FF4D8CEC</color>
    <!--主题文本颜色-->
    <color name="theme_text_color">#FF414245</color>
    <!--主题副文颜色-->
    <color name="theme_sub_text_color">#80414245</color>
    <!--主题背景色-->
    <color name="theme_background_color">#FFFFFFFF</color>
    <!--主题次级背景色-->
    <color name="theme_secondary_background_color">#FFF8F8F8</color>
    <!--主题不可点击颜色-->
    <color name="theme_disable_color">#4D414245</color>
    <!--主题点击反馈颜色-->
    <color name="theme_pressing_on_color">#0D414245</color>
    <!--主题分割线颜色-->
    <color name="theme_line_color">#FFFAFAFA</color>
    <!--主题安全颜色-->
    <color name="theme_security_color">#FF44C669</color>
    <!--主题警示颜色-->
    <color name="theme_warning_color">#FFFF6B59</color>
    <!--主题阴影色-->
    <color name="theme_shadow_color">#FFE9EDF3</color>
    <!--主题阴影色-->
    <color name="theme_mask_layer_color">#B2000000</color>
    <!--主题系统颜色-->
    <color name="theme_primary_color">#FFECECEC</color>
    <color name="theme_primary_dark_color">#FFECECEC</color>
    <color name="theme_accent_color">#FFECECEC</color>


    ```

3. 主题尺寸
    ``` xml
    <!--一级标题大小-->
    <dimen name="theme_level_1_title_size">18sp</dimen>
    <!--二级标题大小-->
    <dimen name="theme_level_2_title_size">16sp</dimen>
    <!--三级标题大小-->
    <dimen name="theme_level_3_title_size">14sp</dimen>
    <!--主文本大小-->
    <dimen name="theme_primary_text_size">14sp</dimen>
    <!--次级文本大小-->
    <dimen name="theme_secondary_text_size">12sp</dimen>
    <!--按钮文本大小-->
    <dimen name="theme_btn_text_size">16sp</dimen>
    <!--导航栏高度-->
    <dimen name="theme_toolbar_height">55dp</dimen>
    <!--按钮高度-->
    <dimen name="theme_button_height">44dp</dimen>
    <!--按钮阴影大小-->
    <dimen name="theme_button_shadow_size">6dp</dimen>
    <!--按钮圆角半径大小-->
    <dimen name="theme_button_radius_size">10dp</dimen>
    <!--卡片圆角半径大小-->
    <dimen name="theme_card_radius_size">10dp</dimen>
    ```  

3. 主题样式
    ``` xml 
    <!-- 常用按钮统一主题 -->
    <style name="theme_apemans_normal_btn_style">
        <item name="android:background">@drawable/bg_btn_apemans_normal</item>
        <item name="android:layout_height">@dimen/theme_button_height</item>
        <item name="android:textColor">@android:color/white</item>
        <item name="android:textSize">@dimen/sp_14</item>
    </style>
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
