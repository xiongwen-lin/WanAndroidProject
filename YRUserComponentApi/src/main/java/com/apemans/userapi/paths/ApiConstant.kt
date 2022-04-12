package com.apemans.userapi.paths

/**
 * Api常量类
 */
object ApiConstant {
    /* -- 全球服务相关 -- */
    const val API_PATH_OF_GLOBAL_TIME = "global/time"
    const val API_PATH_OF_GLOBAL_URL_WITH_COUNTRY = "account/country"
    const val API_PATH_OF_GLOBAL_URL_WITH_COUNTRY_OR_ACCOUNT = "account/get-baseurl"

    const val API_PATH_OF_UPLOAD_FEEDBACK_SIGN = "feedbackput_presignurl"
    const val API_PATH_OF_FEEDBACK_CHECK = "feedback/check"
    const val API_PATH_OF_FEEDBACK_CREATE = "feedback/create"

    /* -- 用户相关 -- */
    const val API_PATH_OF_USER_REGISTER_SEND = "register/send"
    const val API_PATH_OF_USER_REGISTER_VERIFY = "register/verify"
    const val API_PATH_OF_USER_REGISTER = "register/register"
    const val API_PATH_OF_USER_LOGIN_VERIFY = "login/verify"
    const val API_PATH_OF_USER_LOGIN = "login/login"
    const val API_PATH_OF_USER_LOGIN_SEND = "login/send"
    const val API_PATH_OF_USER_LOGIN_RESET = "login/reset"
    const val API_PATH_OF_USER_LOGOUT = "login/logout"
    const val API_PATH_OF_USER_GET_INFO = "user/read"
    const val API_PATH_OF_USER_UPDATE = "user/update"
    const val API_PATH_OF_USER_PUT = "user/put"

    const val API_PATH_OF_USER_TWO_AUTH_MAIL = "login/two_auth/mail"
    const val API_PATH_OF_USER_TWO_AUTH_AUTH = "login/two_auth"

    const val API_PATH_OF_USER_TUYA = "user/tuya"
    const val API_PATH_OF_USER_TUYA_ACCOUNT = "user/account"
}