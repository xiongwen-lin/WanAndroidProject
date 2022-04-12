package com.apemans.usercomponent.mine.activity

import android.os.Bundle
import androidx.fragment.app.commit
import com.apemans.yruibusiness.base.BaseActivity
import com.apemans.router.routerFragments
import com.apemans.usercomponent.R
import com.apemans.usercomponent.databinding.MineActivityMainBinding
import com.apemans.usercomponent.routermap.FRAGMENT_PATH_ACCOUNT

/**
 * 设备组件的调试页面，只有一个容器来加载 Fragment
 *
 * @author Dylan Cai
 */
class MainActivity : com.apemans.yruibusiness.base.BaseActivity<MineActivityMainBinding>() {

  private val mineFragment by routerFragments(FRAGMENT_PATH_ACCOUNT) // 通过路由获取我的组件的 Fragment

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    supportFragmentManager.commit {
      setReorderingAllowed(true)
      add(R.id.fragment_container_view, mineFragment)
    }
  }

  override fun onViewCreated(savedInstanceState: Bundle?) {
  }
}