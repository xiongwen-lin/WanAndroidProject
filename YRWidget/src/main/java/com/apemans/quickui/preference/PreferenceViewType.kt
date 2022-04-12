package com.apemans.quickui.preference

/***********************************************************
 * @Author : caro
 * @Date   : 4/19/21
 * @Func:
 *
 * Item 对应的UI类型
 * @Description:
 *
 *
 ***********************************************************/
annotation class PreferenceViewType {
    companion object {
        //分类类型
        const val UITypeCategory = 0

        //普通类型（设置项名称+当前值+arrow right）
        const val UITypeNormal = 1

        //开关类型 设置项名称+当前值?+开关+arrow right）
        const val UITypeSwitch = 2

        //进度条设置类型 (设置项名称+bar样式+当前值）
        const val UTTypeBar = 3
    }
}
