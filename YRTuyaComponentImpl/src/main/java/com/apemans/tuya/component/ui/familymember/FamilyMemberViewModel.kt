package com.apemans.tuya.component.ui.familymember

import androidx.lifecycle.asLiveData
import com.apemans.yruibusiness.base.RequestViewModel
import com.apemans.yruibusiness.base.catchWith
import com.apemans.yruibusiness.base.showLoadingWith
import com.apemans.tuya.component.repository.TuyaRepository

/**
 * @author Dylan Cai
 */
class FamilyMemberViewModel : com.apemans.yruibusiness.base.RequestViewModel() {

    fun getAccount(uid: String) =
        TuyaRepository.getAccount(uid)
            .showLoadingWith(loadingFlow)
            .catchWith(exceptionFlow)
            .asLiveData()

    fun getUserShareInfo(memberId: Long) =
        TuyaRepository.getUserShareInfo(memberId)
            .showLoadingWith(loadingFlow)
            .catchWith(exceptionFlow)
            .asLiveData()

    fun removeMember(memberId: Long) =
        TuyaRepository.removeMember(memberId)
            .showLoadingWith(loadingFlow)
            .catchWith(exceptionFlow)
            .asLiveData()

    fun removeUserShare(memberId: Long) =
        TuyaRepository.removeUserShare(memberId)
            .showLoadingWith(loadingFlow)
            .catchWith(exceptionFlow)
            .asLiveData()

    fun renameShareNickname(memberId: Long, name: String) =
        TuyaRepository.renameShareNickname(memberId, name)
            .showLoadingWith(loadingFlow)
            .catchWith(exceptionFlow)
            .asLiveData()
}