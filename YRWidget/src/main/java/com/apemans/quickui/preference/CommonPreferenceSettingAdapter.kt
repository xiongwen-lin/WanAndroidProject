package com.apemans.quickui.preference


/***********************************************************
 * @Author : caro
 * @Date   : 1/9/21
 * @Func:
 *
 *
 * @Description:
 *
 *
 ***********************************************************/
class CommonPreferenceSettingAdapter(
    //val cateGoryStyleLayoutResId: Int = R.layout.layout_preference_category,
    val cateGoryStyleLayoutResId: Int = PreferenceGlobalConfig.preferenceCategoryViewLayoutId,
    //val preferenceStyleLayoutResId: Int = R.layout.layout_preferenceview,
    val preferenceStyleLayoutResId: Int = PreferenceGlobalConfig.preferenceViewViewLayoutId,
    //val seekBarStyleLayoutResId: Int = R.layout.layout_preference_bar
    val seekBarStyleLayoutResId: Int = PreferenceGlobalConfig.preferenceBarViewLayoutId
) : BasePreferenceSettingTypeAdapter() {

    override fun provideCateGoryStyleLayoutResId(): Int = cateGoryStyleLayoutResId

    override fun providePreferenceStyleLayoutResId(): Int = preferenceStyleLayoutResId

    override fun provideBarStyleLayoutResId(): Int = seekBarStyleLayoutResId

}