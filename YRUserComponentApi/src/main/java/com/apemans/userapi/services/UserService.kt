package com.apemans.userapi.services

import com.alibaba.android.arouter.facade.template.IProvider
import com.apemans.userapi.request.*
import kotlinx.coroutines.flow.Flow

/**
 * 用户组件的服务接口，实现类在用户组件的 provider 包下
 *
 * @author Dylan Cai
 */
interface UserService : IProvider {

//  fun isFirstLoading() : Boolean
//
//  fun isLogin(): Boolean
//
//  fun logout()
//
//  fun userLoginInfo() : UserInfo

  fun getUserInfo() : Flow<YRApiResponse<UserInfoResult>>

  fun getAccountByUid(uid : String) : Flow<YRApiResponse<TuyaAccountResult>>

  fun getUidByAccount(account : String) : Flow<YRApiResponse<TuyaUidResult>>
}