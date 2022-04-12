package com.apemans.userapi.request

/* -- 全球服务相关 -- */

/**
 * 服务器utc时间数据类
 */
data class GlobalTimeResult(var time : Long)

/**
 * 全球服务地址数据类
 */
data class GlobalUrlResult(
    var web : String,
    var p2p : String,
    var s3 : String,
    var mq : String,
    var ms : String,
    var ss : String,
    var region : String,
    var exist : String,
    var schema : List<String>
)

/* -- 全球服务相关 -- */

/* -- 用户相关 -- */

/**
 *
 */
data class RegisterResult(
    var uid : String,
    var api_token : String,
    var refresh_token : String,
    var expire_time : String
)

/**
 * 登陆信息数据类
 */
data class LoginResult(
    var api_token : String,
    var uid : String,
    var refresh_token : String,
    var expire_time : String,
    var isdebug : Int,
    var register_country : String
)

/**
 * 用户信息数据类
 */
data class UserInfoResult(
    var level : Int,
    var account : String,
    var nickname : String,
    var photo : String,
    var country : String,
    var isdebug : Int,
    var register_time : Int,
    var two_auth : Int
)

/**
 * 文件预签名数据类
 */
data class AwsFilePreSign(
    var url : String
)

/**
 * 创建反馈Body类
 */
data class CreateFeedbackBody(
    var type_id : Int,
    var product_id : Int,
    var email : String,
    var content : String,
    var image : String?
)

/**
 * uid数据类
 */
data class TuyaUidResult(
    var uid : String
)

/**
 * 账号数据类
 */
data class TuyaAccountResult(
    var account : String
)
