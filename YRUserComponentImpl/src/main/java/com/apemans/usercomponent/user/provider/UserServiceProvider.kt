package com.apemans.usercomponent.user.provider

import android.content.Context
import com.alibaba.android.arouter.facade.annotation.Route
import com.apemans.userapi.paths.GROUP_USER
import com.apemans.userapi.request.*
import com.apemans.userapi.services.UserService
import com.apemans.usercomponent.userapi.UserApiHelper
import kotlinx.coroutines.flow.Flow

/**
 * 用户组件服务接口 [UserService] 的实现类
 *
 * @author Dylan Cai
 */
@Route(path = "$GROUP_USER/user")
class UserServiceProvider : UserService {
  override fun getUserInfo(): Flow<YRApiResponse<UserInfoResult>> {
    return UserApiHelper.userApi.getUserInfo()
  }

  override fun getAccountByUid(uid : String): Flow<YRApiResponse<TuyaAccountResult>> {
    return UserApiHelper.userApi.getAccountByUid(uid)
  }

  override fun getUidByAccount(account : String): Flow<YRApiResponse<TuyaUidResult>> {
    return UserApiHelper.userApi.getUidByAccount(account)
  }

  override fun init(context: Context) {}
}