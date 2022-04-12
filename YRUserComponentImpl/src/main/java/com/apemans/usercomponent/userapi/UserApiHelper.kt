package com.apemans.usercomponent.userapi

import com.apemans.business.apisdk.ApiManager

object UserApiHelper {
    var userApi = ApiManager.getService(IUserApi::class.java)

    var userGlobalApi = ApiManager.getService(IGlobalApi::class.java)
}