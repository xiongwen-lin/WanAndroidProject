# 模块
    YRUserComponentlmpl

# 功能介绍
``` 
    1. 用户登录注册
    2. 用户修改密码
    3. 用户修改头像
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
        implementation project(path: ':YRUserComponentlmpl')
    }
```
# 初始化
```
    通过aRouter跳转
    1. 用户登录注册路径： /user/login
    2. 我的模块：/user/account
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

# 用户模块对接方案

##  一. 数据存储

![image.png](https://p1-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/08e39dfa3cfb41c7ad39d0f7b65ab0ff~tplv-k3u1fbpfcp-watermark.image?)

## 二. 数据对接时序图

![image.png](https://p9-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/aba3572aa6164dc7accb1d2d11b2c979~tplv-k3u1fbpfcp-watermark.image?)

## 三. 数据对接时序图说明

说明：（下列序号 与 二.数据对接时序图 序号一致）

1. 数据更新 yrcx://yrplatformbridge/setparamters    

		其它模块更新数据，通过发送中间件协议将数据发送到YRPlatformBridge模块

2. 调用旧模块UserInfoPresenter.updataUserInfo()  

		桥接模块接收到数据更新协议后，通过调用旧模块UserInfoPresenter.updataUserInfo()将数据同步到就模块

3. 本地缓存SDKGlobalData 

		旧模块UserInfoPresenter.updataUserInfo()解析数据，并将数据保存SDKGlobalData

4. yrcx://yrbusiness/setparamters

		YRCXSDK接收到数据更新协议后，同步更新YRBussiness中的数据

5. 数据存储MMKV存储

		YRCXSDK模块创建用户信息类用来做数据持久化保存(保存字段在文档中有细分)

6. YRCXSDK 模块注册 YRBussiness 监听

        当 YRBussiness 模块接收到yrcx://yrbusiness/setparamters协议后，同步将数据更新到YRCXSDK模块

7. 数据存储MMKV存储  

		YRCXSDK模块创建用户信息类用来做数据持久化保存（保存字段在文档中有细分）

8. 数据更新       

		旧模块有数据更新，如退出登录/修改用户昵称/修改用户头像等等

9. 引用YRPlatformBridge模块YRUserPlatformHelper.updataUserInfo()  

		旧模块直接引用YRPlatformBridge模块YRUserPlatformHelper.updataUserInfo()更新信息

10. yrcx://yrbusiness/setparamters

		YRUserPlatformHelper.updataUserInfo()发送中间件协议，将数据信息更新到YRBussiness模块

11. 更新本地存储数据  

		YRBussiness模块接收更新信息后，修改本地保存数据

12. YRCXSDK 模块注册了 YRBussiness 监听

        当 YRBussiness 模块接收到yrcx://yrbusiness/setparamters协议后，同步将数据更新到YRCXSDK模块

13. 更新本地存储数据

## 补充说明：

    1. YRCXSDK模块用来提供外部模块需要获取的信息，涉及用户账号,昵称,用户头像等信息（用户基本信息），在YRCXSDK模块支持可读可写，其它信息，如uid，token等信息，外部模块只能够读取，不能修改。

## 四.更新前的准备

① ：YRPlatformBridge模块 

		1. 创建中间件服务类  YRPlatformBridgeKit
		2. 实现 yrcx://yrplatformbridge/setparamters 协议
		3. 创建单例类 YRUserPlatformHelper （用来发送中间件信息 YRUserPlatformHelper.updataUserInfo() ）  
		4. 创建一个接口 YRIUserInfoHelper， 定义updataUserInfo()

②：旧模块   

		1. 创建YRUserInfoPresenter 实现  YRIUserInfoHelper 接口
		2. YRUserInfoPresenter.updataUserInfo()用于更新信息

③：YRCXSDK

		1. 创建中间件服务类  YRCXSDKKit
		2. 实现 yrcx://yrcxsdk/setparamters 协议
		3. 实现 yrcx://yrcxsdk/getparamters 协议
		4. 创建单例类YRUserInfoConfig

④：YRBussiness     

		1. 创建中间件服务类YRBussinessKit   
		2. 实现 yrcx://yrbussiness/setparamters 协议
		3. 实现 yrcx://yrbussiness/getparamters 协议
		4. 创建YRNetInfoConfig类，用于存储网络相关信息

## 五. 用户模块登录

一.用户模块登录流程图

![image.png](https://p1-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/e800897c26c545ada9efdcaf2931d5e2~tplv-k3u1fbpfcp-watermark.image?)

二. 用户模块登录接口的调用以及数据更新

说明：（调用接口并实现数据更新）

1. 点击登录按钮

        ①若gap_time_valid 为 false 调用接口 global/time更新  gap_time 和  gap_time_valid 字段
        ②调用 account/get-baseurl接口无字段更新

1. 账号不是Osaio账号

		①没有同意过协议
		②若gap_time_valid为 false 调用接口 global/time更新  gap_time 和  gap_time_valid 字段
		③调用 account/get-baseurl接口无字段更新
		④弹窗

2. 账号是Osaio账号

		①若gap_time_valid为 false 调用接口 global/time更新  gap_time 和  gap_time_valid 字段
		②调用接口 account/get-baseurl更新  web_url，p2p_url， s3_url， ss_url， region 字段
		③调用接口 login/login 更新  account ， password， uid， token， refresh_token， expire_time 字段
		④调用接口 user/put 更新 pushToken字段
		⑤登录涂鸦 -- 调用 yrcx://yrtuya/loginwithuid 更新字段 headPic 用户头像

## 六. 用户模块注册

一. 用户模块注册流程图

![image.png](https://p6-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/42404b93a67d495bada48b8ba7f8492b~tplv-k3u1fbpfcp-watermark.image?)

二. 用户模块注册接口的调用以及数据更新

说明：（调用接口并实现数据更新） 

1. 获取验证码

        ①  getGlobalTime()  若gap_time_valid为 false 调用接口 global/time更新  gap_time 和  gap_time_valid 字段
        ②  调用接口 account/get-baseurl更新  web_url，p2p_url， s3_url， ss_url， region 字段
        ③调用  register/send

2. 账号不是Osaio账号

        ①没有同意过协议
        ②若gap_time_valid为 false 调用接口 global/time更新  gap_time 和  gap_time_valid 字段
        ③调用 account/get-baseurl接口无字段更新

3. 点击注册按钮

        ①若gap_time_valid为 false 调用接口 global/time更新  gap_time 和  gap_time_valid 字段
        ②调用 account/get-baseurl接口更新  web_url，p2p_url， s3_url， ss_url， region 字段
        ③调用 register/verify 接口无字段更新

4. 设置密码

        ①若gap_time_valid为 false 调用接口 global/time更新  gap_time 和  gap_time_valid 字段
        ②调用 account/get-baseurl接口更新  web_url，p2p_url， s3_url， ss_url， region 字段
        ③调用 register/register 接口更新字段 account ， password， uid， token， refresh_token， expire_time
        ④登录涂鸦 -- 调用 yrcx://yrtuya/loginwithuid 更新字段 headPic 用户头像

## 七.用户模块退出登录

一. 用户模块退出登录流程图

![image.png](https://p6-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/82811557945545d2a47e29ea1881b4f2~tplv-k3u1fbpfcp-watermark.image?)

二. 用户模块退出登录接口的调用以及数据更新
说明：（调用接口并实现数据更新）

1. 点击退出登录按钮

        ①调用接口login/logout更新 userAccount,password,uid,token,refreshToken,expireTime,pushToken,webUrl,p2pUrl,s3Url,ssUrl,region,headPic字段(清空这些字段信息）

## 八.用户模块忘记密码

一.用户模块忘记密码流程图

![image.png](https://p6-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/59bdaee6d6194cf7affc56a731f91a47~tplv-k3u1fbpfcp-watermark.image?)

二.用户模块忘记密码接口的调用以及数据更新

1. 忘记密码

        ①若gap_time_valid为 false 调用接口 global/time更新  gap_time 和  gap_time_valid 字段
        ②调用 account/get-baseurl接口无更新字段
        ③是osaio账号或已同意协议。
        ④获取验证码  
	        若gap_time_valid为 false 调用接口 global/time更新  gap_time 和  gap_time_valid 字段 
	        调用 account/get-baseurl接口更新  web_url，p2p_url， s3_url， ss_url， region 字段
	        调用 login/send 接口，无更新字段
        ⑤输入验证码
	        若gap_time_valid为 false 调用接口 global/time更新  gap_time 和  gap_time_valid 字段 
	        调用 account/get-baseurl接口更新  web_url，p2p_url， s3_url， ss_url， region 字段
	        调用 login/verify 接口 无字段更新
        ⑥设置密码
	        若gap_time_valid为 false 调用接口 global/time更新  gap_time 和  gap_time_valid 字段 
	        调用 account/get-baseurl接口更新  web_url，p2p_url， s3_url， ss_url， region 字段
	        调用 login/reset 接口 无字段更新，去登录页面

## 九.用户模块其它接口信息

1. 修改密码

        ①调用接口  user/update  更新 password 字段

2. 上报用户信息

        ①调用接口 user/put 更新 push_token 字段

3. 修改用户头像

        ①调用接口  photoput_presignurl  无更新字段
        ②调用接口  "x-amz-server-side-encryption:AES256"  upLoadFileToCloud （PUT 类型）
        ③调用涂鸦 yrcx://yrtuya/uploaduseravatar 更新headPic字段（用户头像信息）
        ④调用接口 user/update 无更新字段

4. 修改用户昵称

        ①调用接口 user/update 更新 nickName 字段
        ②调用涂鸦修改昵称接口 yrcx://yrtuya/rerinckname 无更新字段

## 十. 旧个人中心跳转到登入页

1. 账号设置页 点击退出登录

        调用桥接模块  YRUserPlatformHelper.startActivity() -->  startActivity() 发送中间件协议跳转到用户模块登入界面
        yrcx://yrusercomponentimplkit/startactivity

2. 异地登录  

        接收到异地登录通知后 （具体看ForceLogoutBroadcastReceiver 与 MyAccountReceiverListener 的 clearLoginInfo() 方法）调用桥接模块
        YRUserPlatformHelper. startActivity() -->  startActivity() 发送中间件协议跳转到用户模块登入界面yrcx://yrusercomponentimplkit/startactivity

3. 可能还有一些没有完善的细节后续发现再补充


# 其他
```
    1. 沟通和交流
        维护：岩鹭
        邮箱：xiongwen@apemans.com
```








