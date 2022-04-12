package com.apemans.userapi.request

/**
 * Post请求基类
 */
open class BaseRequestBody

/**
 * 发送注册验证码Body类
 */
data class RegisterSendBody(
    var account : String,
    var country : String
)

/**
 * 严重注册码Body类
 */
data class RegisterVerifyBody(
    var account : String,
    var code : String
)

/**
 * 注册Body类
 */
data class RegisterBody(
    var account : String,
    var password : String,
    var code : String,
    var country : String
)

/**
 * 登陆接口Body类
 */
data class LoginBody(
    var account : String,
    var password : String,
    var country : String,
    var phone_code : String,
    var zone : Float,
    var phone_brand : String
    )

/**
 * 忘记密码Body类
 */
data class ResetPwBody(
    var account : String,
    var password : String,
    var code : String
)

/**
 * 登陆后修改用户密码类
 */
data class ResetUserPasswordBody(
    var oldpwd : String,
    var password : String,
)

/**
 * 上报用户信息Body类
 */
data class UploadUserBody(
    var push_type : Int,
    var device_type : Int,
    var push_token : String,
    var phone_code : String,
    var country : String,
    var zone : Float,
    var nickname : String,
    var photo : String,
    var app_version : String,
    var app_version_code : String,
    var phone_model : String,
    var phone_brand : String,
    var phone_version : String,
    var phone_screen : String,
    var language : String,
    var package_name : String,
    var phone_name : String
)

/**
 * 发送双重认证验证码Body类
 */
data class TwoAuthMailBody(
    var account : String,
    var country : String
)

/**
 * 双重认证登陆Body类
 */
data class TwoAutoLoginBody(
    var account : String,
    var country : String,
    var zone : Float,
    var code : String,
    var phone_code : String,
    var phone_brand : String,
    var phone_model : String,
    var phone_name : String
)