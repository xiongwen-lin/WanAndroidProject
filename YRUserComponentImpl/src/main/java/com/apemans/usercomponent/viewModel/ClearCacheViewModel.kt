package com.apemans.usercomponent.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.apemans.yruibusiness.base.BaseComponentViewModel
import com.apemans.usercomponent.repository.ClearCacheRepository
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart

class ClearCacheViewModel : com.apemans.yruibusiness.base.BaseComponentViewModel() {

    fun getCacheSize () : LiveData<String> {
        return ClearCacheRepository.getCacheSize()
            .onStart {  }
            .onCompletion {  }
            .catch {  }
            .asLiveData()
    }

    fun clearCache() : LiveData<Boolean> {
        return ClearCacheRepository.clearCache()
            .onStart {  }
            .onCompletion {  }
            .catch {  }
            .asLiveData()
    }
}