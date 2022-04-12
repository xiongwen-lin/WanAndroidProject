# 模块
    YRUserComponentApi

# 功能介绍
``` 
    暴露用户模块相关信息给其他模块使用
    1. 用户是否已登录退出
    2. 获取用户基本信息
    3. 提供可以跳转的路径
```

# 引用
```
    1. 添加依赖和配置
    android {
    compileSdkVersion 30

        defaultConfig {
            minSdkVersion 21
            targetSdkVersion 30
        } 
    }

    dependencies{
        implementation ("com.apemans:YRUserComponentApi:+")
    }
```
# 初始化
```
    通过aRouter跳转
    获取 IProvider 实例，跳转路径  /user/user
```

# 获取用户信息
```
    通过路由跳转，获取 IProvider 实例，跳转路径  /user/user
    获取信息方法： userLoginInfo
                方法名： userLoginInfo
                参数 ： 无
                返回值： UserInfo（bean == 包括下方提到的字段信息）
        1. 用户账号
        2. 当前登录用户国家码
        3. 用户头像
        4. 用户昵称
```

# 用户是否登录
```
    方法名： isLogin
    参数 ： 无
    返回值： boolean （true == 已登录   false == 未登录）
```
# 其他
```
    1. 沟通和交流
        维护：岩鹭
        邮箱：xiongwen@apemans.com
```








