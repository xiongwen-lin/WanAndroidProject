package com.apemans.usercomponent.mine

import android.view.View
import com.alibaba.android.arouter.facade.annotation.Route
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.transition.DrawableCrossFadeFactory
import com.dylanc.longan.addStatusBarHeightToMarginTop
import com.apemans.quickui.click
import com.apemans.quickui.preference.*
import com.apemans.logger.YRLog
import com.apemans.router.startRouterActivity
import com.apemans.usercomponent.R
import com.apemans.usercomponent.databinding.UserFragmentAccountBinding
import com.apemans.usercomponent.routermap.FRAGMENT_PATH_ACCOUNT
import com.apemans.usercomponent.mine.activity.*
import com.apemans.usercomponent.mine.util.ConstantValue
import com.apemans.yrcxsdk.data.YRCXSDKDataManager

/**
 * 我的模块
 * @author Dylan Cai
 */
@Route(path = FRAGMENT_PATH_ACCOUNT)
class AccountFragment : com.apemans.yruibusiness.base.BaseFragment<UserFragmentAccountBinding>() {
    lateinit var recyclerView: PreferenceRecyclerView
    var list = mutableListOf<PreferenceBean>()

    override fun onViewCreated(root: View) {
        PreferenceGlobalConfig.itemBackground = R.drawable.selector_rv_item_press_state
        initView()
        setSettingData()
    }

    fun initView() {
        binding.ivPersonMsg.addStatusBarHeightToMarginTop()
        binding.ivPersonEditProfileArrow.setOnClickListener {
            AccountInfoActivity.start(requireContext(), 0, "")
        }
        binding.ivPersonPortrait.setOnClickListener {
            AccountInfoActivity.start(requireContext(), 0, "")
        }
        binding.containerPersonInfo.setOnClickListener {
            AccountInfoActivity.start(requireContext(), 0, "")
        }
        binding.ivPersonSupport.setOnClickListener {
            MineHelpActivity.start(requireContext())
        }
        binding.containerPersonFeedback.setOnClickListener {
            startRouterActivity("/feedback/feedback_faq_main"/*ACTIVITY_PATH_FEEDBACK_FAQ_MAIN*/)
//            FeedbackActivity.start(requireContext()) TODO 旧版反馈待清理
        }
        binding.tvPersonName.text = YRCXSDKDataManager.userAccount
        recyclerView = binding.personRecycler

        binding.ivPersonMsg.click {
            startRouterActivity("/message/message_list"/*ACTIVITY_PATH_MESSAGE_LIST*/)
        }
    }

    private fun setSettingData() {
        val gallery = PreferenceBean("相册", PreferenceViewType.UITypeNormal)
        gallery.iconDrawable = R.drawable.set_photo
        val about = PreferenceBean("关于", PreferenceViewType.UITypeNormal)
        about.iconDrawable = R.drawable.person_about
        val setting = PreferenceBean("设置", PreferenceViewType.UITypeNormal)
        setting.iconDrawable = R.drawable.person_setting
        list.add(gallery)
        list.add(about)
        list.add(setting)
        recyclerView.addPreferenceSettingEventCallback(object : PreferenceSettingEventCallback {
            override fun onClick(position: Int, curValue: String?, preferenceItem: PreferenceBean, supportParams: List<PreferenceBean.Param>?, preferenceView: PreferenceView) {
                when (position) {
                    0 -> { PhotoActivity.start(activity) }
                    1 -> { AboutActivity.start(activity) }
                    2 -> { AppSettingsActivity.start(activity) }
                }
            }
        })
        recyclerView.bindSourceData(list)
        recyclerView.setCanScrollVertical(false)
    }

    override fun onStart() {
        super.onStart()
        val drawableCrossFadeFactory =
            DrawableCrossFadeFactory.Builder(ConstantValue.DURATION_MILLIS)
                .setCrossFadeEnabled(true).build()
        YRLog.d("","-->> MyProfileActivity test change portrait 6 path=${YRCXSDKDataManager.userHeadPic}")

        Glide.with(this)
            .load(YRCXSDKDataManager.userHeadPic)
            .apply(RequestOptions().circleCrop().placeholder(R.drawable.person_portrait_icon).error(R.drawable.person_portrait_icon))
            .transition(DrawableTransitionOptions.with(drawableCrossFadeFactory))
            .into(binding.ivPersonPortrait)
    }

    override fun onResume() {
        super.onResume()
    }

}
