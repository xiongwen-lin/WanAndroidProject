package com.apemans.usercomponent.mine.activity

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.PopupWindow
import androidx.core.content.FileProvider
import com.alibaba.android.arouter.facade.annotation.Route
import com.apemans.base.middleservice.YRMiddleServiceManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.transition.DrawableCrossFadeFactory
import com.dylanc.longan.finishAllActivitiesExceptNewest
import com.dylanc.longan.intentExtras
import com.dylanc.longan.startActivity
import com.apemans.base.utils.clickWithDuration
import com.apemans.business.apisdk.client.configure.YRApiConfigure
import com.apemans.business.apisdk.client.define.HttpCode
import com.apemans.yruibusiness.ui.toolbar.setToolbar
import com.apemans.logger.YRLog
import com.apemans.quickui.superdialog.SmartDialog
import com.apemans.router.ACTIVITY_PATH_MAIN
import com.apemans.router.startRouterActivity
import com.apemans.userapi.paths.ACTIVITY_PATH_ACCOUNT_INFO
import com.apemans.userapi.paths.ACTIVITY_PATH_UPDATA_PASSWARD
import com.apemans.userapi.request.UserInfoResult
import com.apemans.userapi.startRouterActivityCheckLogin
import com.apemans.usercomponent.R
import com.apemans.usercomponent.baseinfo.file.FileUtil
import com.apemans.usercomponent.baseinfo.graphics.BitmapUtil
import com.apemans.usercomponent.databinding.MineActivityAccountBinding
import com.apemans.usercomponent.easypermissions.EasyPermissions
import com.apemans.usercomponent.mine.util.ConstantValue
import com.apemans.usercomponent.mine.util.UCrop
import com.apemans.usercomponent.mine.widget.PhotoPopupWindows
import com.apemans.usercomponent.viewModel.AccountViewModel
import com.apemans.yrcxsdk.data.YRCXSDKDataManager
import com.dylanc.longan.toast
import java.io.File
import java.lang.Exception

@Route(path = ACTIVITY_PATH_ACCOUNT_INFO)
class AccountInfoActivity : MineBaseActivity<MineActivityAccountBinding>() {
    private var mAccount: String? = null
    private var mNickName: String? = null
    private val memberId : Long by intentExtras(ConstantValue.INTENT_KEY_MEMBER_ID, 0)
    private val memberNickName : String by intentExtras(ConstantValue.INTENT_KEY_NICK_NAME, "")
    private var mBitmap: Bitmap? = null
    private var popMenus: PhotoPopupWindows? = null
    private var haveChoosePhoto = false

    private lateinit var viewModel : AccountViewModel

    private val PERMS_CAMERA = arrayOf(Manifest.permission.CAMERA,
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE)

    private val PERMS_READ_WRITE = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE)
    private val RESULT_CODE_KITKAT_PHOTO = 321
    private val RESULT_CODE_PHOTO = 322
    private val RESULT_CODE_CAMERA = 323
    private val RESULT_CODE_CLIP = 324

    override fun onViewCreated(savedInstanceState: Bundle?) {
        initView()
        initData()
    }

    private fun initView() {
        setToolbar {
            title = resources.getString(R.string.app_settings_manage_account)
            leftIcon(R.drawable.left_arrow_black, ::onClickLeft)
        }
        onClickEvent()
        YRLog.d { "respo=====================: ${YRCXSDKDataManager.getApiToken()} xxxxxxxxxxxxxxxxxxxxxxxx: ${YRCXSDKDataManager.getApiToken()}" }
    }

    private fun initData() {
        viewModel = registerViewModule(AccountViewModel::class.java)
        viewModel.loadUserInfo().observe(this) {
            if (it.code == HttpCode.SUCCESS_CODE) {
                updateInfoView(it.data)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        // 获取手机号绑定状态
    }

    private fun updatePortrait(fromLocal: Boolean) {
        val file = File(FileUtil.getAccountNamePortrait(this, YRCXSDKDataManager.userAccount))
        val drawableCrossFadeFactory =
            DrawableCrossFadeFactory.Builder(ConstantValue.DURATION_MILLIS)
                .setCrossFadeEnabled(true).build()
        Log.d("","-->> MyProfileActivity test change portrait 6 path=${YRCXSDKDataManager.userHeadPic}")
        if (fromLocal && file.exists()) {
            Glide.with(applicationContext)
                .load(file)
                .skipMemoryCache(true) // 不使用内存缓存
                .diskCacheStrategy(DiskCacheStrategy.NONE) // 不使用磁盘缓存
                .apply(RequestOptions().circleCrop().placeholder(R.drawable.user)
                        .error(R.drawable.user))
                .transition(DrawableTransitionOptions.with(drawableCrossFadeFactory))
                .into(binding.userPhoto)
        } else {
            Glide.with(applicationContext)
                .load(YRCXSDKDataManager.userHeadPic)
                .apply(RequestOptions().circleCrop().placeholder(R.drawable.user).error(R.drawable.user))
                .transition(DrawableTransitionOptions.with(drawableCrossFadeFactory))
                .into(binding.userPhoto)
        }
    }

    private fun onClickEvent() {
        with(binding) {
            userName.clickWithDuration(500) {
                MineSetNameActivity.start(this@AccountInfoActivity, binding.userNickName.text.toString(), ConstantValue.REQUEST_CODE_CUSTOM_NAME)
            }
            updataPassword.setOnClickListener {
                startRouterActivity(ACTIVITY_PATH_UPDATA_PASSWARD)
            }
            userPhoto.setOnClickListener {
                showPopMenu()
            }
            userAccount.text = YRCXSDKDataManager.userAccount
            if (YRCXSDKDataManager.userNickname != "") {
                userNickName.text = YRCXSDKDataManager.userNickname
            } else {
                userNickName.text = YRCXSDKDataManager.userAccount
            }
            tvPhone.setOnClickListener {
                MineBindPhoneActivity.start(this@AccountInfoActivity)
            }
            logout.setOnClickListener {
                SmartDialog.build(supportFragmentManager)
                    .setTitle(resources.getString(R.string.logout))
                    .setContentText(resources.getString(R.string.account_logout_confirm))
                    .setPositiveTextName(resources.getString(R.string.confirm))
                    .setNegativeTextName(resources.getString(R.string.cancel))
                    .setOnPositive {
                        onClickLogout()
                        it.dismiss()
                    }
                    .setOnNegative {
                        it.dismiss()
                    }
                    .show()
            }
        }
    }

    private fun onClickLeft(view : View) {
        finish()
    }

    private fun onClickLogout() {
        viewModel.logout().observe(this@AccountInfoActivity){
            var hashMap = HashMap<String,Any?>()
            hashMap["logout"] = ""
            if (it == HttpCode.SUCCESS_CODE) {
                // 注销登录的一些信息
                YRMiddleServiceManager.request("yrcx://yrbusiness/setparamters",hashMap)
                // 退出登录时建议是跳转到主页面并检查是否登录，这样即使改动是否要强制登录的逻辑，不需要修改这里的代码
                // 如果开启了登录拦截，就会先跳到登录页面；如果没开启登录拦截，就会正常跳到主页面
                startRouterActivityCheckLogin(ACTIVITY_PATH_MAIN) {
                    finishAllActivitiesExceptNewest()
                }
                YRMiddleServiceManager.request("yrcx://yrplatformbridge/setparamters",hashMap)
            } else if (it == HttpCode.CODE_1006) {
                YRMiddleServiceManager.request("yrcx://yrbusiness/setparamters",hashMap)
                startRouterActivityCheckLogin(ACTIVITY_PATH_MAIN) {
                    finishAllActivitiesExceptNewest()
                }
                YRMiddleServiceManager.request("yrcx://yrplatformbridge/setparamters",hashMap)
            }
        }
    }

    private fun updateInfoView(userInfo: UserInfoResult) {
        mAccount = userInfo.account
        updatePortrait(false)
        mNickName = if (memberId > 0) {
            memberNickName
        } else {
            userInfo.nickname
        }
        binding.userNickName.text = mNickName
        binding.userAccount.text = mAccount
    }

    private fun showPopMenu() {
        popMenus?.dismiss()
        popMenus = PhotoPopupWindows(this, object : PhotoPopupWindows.OnClickSelectPhotoListener {
            override fun onClick(takePhoto: Boolean) {
                if (takePhoto) {
                    haveChoosePhoto = false
                    requestPermissions(PERMS_CAMERA)
                } else {
                    haveChoosePhoto = true
                    if (EasyPermissions.hasPermissions(activity, *PERMS_READ_WRITE)) { //已有权限
                        gotoPhoto()
                    } else {
                        requestPermissions(PERMS_READ_WRITE)
                    }
                }
            }
        })
        popMenus!!.setOnDismissListener(PopupWindow.OnDismissListener { popMenus = null })
        popMenus!!.showAtLocation(findViewById(R.id.managerAccountRoot),
            Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL, 0, 0)
    }

    private fun convertUriByPath(path: String): Uri {
        YRLog.d("-->> MyProfileActivity test change portrait 6 path=$path")
        val tmpFile = File(path)
        if (!tmpFile.exists()) {
            YRLog.d("-->> MyProfileActivity test change portrait 7 tmpfile not exist")
        }
        val uri: Uri?
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            val packageName = context.packageName
            uri = FileProvider.getUriForFile(this, "$packageName.provider", File(path))
            YRLog.d("-->> MyProfileActivity test change portrait 8 uri=" + if (uri != null) uri.path else "")
        } else {
            uri = Uri.fromFile(File(path))
            YRLog.d("-->> MyProfileActivity test change portrait 9 uri=" + if (uri != null) uri.path else "")
        }
        return uri
    }

    private fun getLocalHeaderTmpPath(): String? {
        return FileUtil.getTmpAccountNamePortrait(this, YRCXSDKDataManager.userAccount)
    }

    private fun gotoPhoto() {
        val intent = Intent()
        intent.action = Intent.ACTION_OPEN_DOCUMENT
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        intent.type = "image/*"
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false)
        intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true)
        // 4.4版本
        startActivityForResult(intent, RESULT_CODE_KITKAT_PHOTO)
    }

    private fun clipPicture(inData: Uri, outData: Uri): Boolean {
        try {
            val intent = Intent()
            intent.action = "com.android.camera.action.CROP"
            intent.setDataAndType(inData, "image/*")
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                // 加入访问权限
                grantUriPermission(this, intent, inData)
                intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION or Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
            intent.putExtra("crop", true)
            intent.putExtra("aspectX", 1)
            intent.putExtra("aspectY", 1)
            intent.putExtra("outputX", 300)
            intent.putExtra("outputY", 300)
            intent.putExtra("return-data", false)
            intent.putExtra(MediaStore.EXTRA_OUTPUT, outData)
            intent.putExtra("dragAndScale", true)
            intent.putExtra("scaleUpIfNeeded", true)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                // 加入访问权限
                grantUriPermission(this, intent, outData)
                intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION or Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
            YRLog.d("-->> MyProfileActivity test change portrait 11 crop is supported=" + (intent.resolveActivity(packageManager) != null))
            if (intent.resolveActivity(packageManager) != null) {
                startActivityForResult(intent, RESULT_CODE_CLIP)
                return true
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return false
    }

    private fun grantUriPermission(context: Context?, intent: Intent?, uri: Uri?) {
        if (context == null || intent == null || uri == null) {
            return
        }
        try {
            val resolveInfoList = context.packageManager.queryIntentActivities(
                intent, PackageManager.MATCH_DEFAULT_ONLY)
            for (resolveInfo in resolveInfoList) {
                val packageName = resolveInfo.activityInfo.packageName
                context.grantUriPermission(packageName,
                    uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION or Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun startTakePhotoIntent() {
        val intent = Intent()
        intent.action = MediaStore.ACTION_IMAGE_CAPTURE
        val cropCameraPicUri = convertUriByPath(getLocalHeaderTmpPath()!!)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, cropCameraPicUri)
        startActivityForResult(intent, RESULT_CODE_CAMERA)
    }

    private fun clipWithUcrop(sourceUri: Uri?, destinationUri: Uri?) {
        /*if (sourceUri != null) {
            if (destinationUri != null) {
                UCrop.of(sourceUri, destinationUri)
                    .withAspectRatio(16F, 9F)
                    .withMaxResultSize(300, 300)
                    .start(this)
            }
        }*/
    }

    override fun permissionsGranted() {
        super.permissionsGranted()
        if (haveChoosePhoto) {
            gotoPhoto()
        } else {
            startTakePhotoIntent()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        var saveUri: Uri = Uri.fromFile(File(FileUtil.getAccountNamePortrait(this, YRCXSDKDataManager.userAccount)))
        if (resultCode == RESULT_OK) {
            when(requestCode) {
                ConstantValue.REQUEST_CODE_CUSTOM_NAME -> {
                    val nickName = data?.getStringExtra(ConstantValue.INTENT_KEY_NICK_NAME)
                    YRCXSDKDataManager.userNickname = nickName
                    binding.userNickName.text = nickName
                }
                RESULT_CODE_CAMERA -> {
                    //clipPicture(cropUri, saveUri);
                    val cropCameraPicUri: Uri? = getLocalHeaderTmpPath()?.let { convertUriByPath(it) }
                    val saveCameraPicUri: Uri = convertUriByPath(FileUtil.getAccountNamePortrait(this, YRCXSDKDataManager.userAccount))
                    if (!cropCameraPicUri?.let { clipPicture(it, saveCameraPicUri) }!!) {
                        /*clipWithUcrop(Uri.fromFile(File(getLocalHeaderTmpPath())),
                            Uri.fromFile(File(FileUtil.getAccountNamePortrait(this, YRCXSDKDataManager.userAccount))))*/
                    }
                }
                RESULT_CODE_PHOTO -> {
                    if (data == null || data.data == null) {
                        return
                    }
                    if (!clipPicture(data.data!!, saveUri)) {
                        clipWithUcrop(
                            Uri.fromFile(File(getLocalHeaderTmpPath())),
                            Uri.fromFile(File(FileUtil.getAccountNamePortrait(this, YRCXSDKDataManager.userAccount))))
                    }
                }
                RESULT_CODE_KITKAT_PHOTO -> {
                    if (data == null || data.data == null) {
                        return
                    }
                    val imagePath = BitmapUtil.getPath(this, data.data)
                    val cropPicUri = data.data
                    val savePicUri = convertUriByPath(FileUtil.getAccountNamePortrait(this, YRCXSDKDataManager.userAccount))
                    if (!clipPicture(cropPicUri!!, savePicUri)) {
                        clipWithUcrop(cropPicUri, Uri.fromFile(File(FileUtil.getAccountNamePortrait(
                                        this, YRCXSDKDataManager.userAccount))))
                    }
                }
                RESULT_CODE_CLIP -> {
                    try {
                        YRLog.d("-->> MyProfileActivity test change portrait 10 savePicPath=" + FileUtil.getAccountNamePortrait(
                                this, YRCXSDKDataManager.userAccount))
                        if (requestCode == UCrop.REQUEST_CROP) {
                            val resultUri = if (data != null) UCrop.getOutput(data) else null
                            YRLog.d("-->> MyProfileActivity test change portrait 12 ucrop result uri=" + if (resultUri != null) resultUri.path else "")
                        }
                        mBitmap = MediaStore.Images.Media.getBitmap(contentResolver, saveUri)
                        if (mBitmap != null) {
                            val compressPath = BitmapUtil.compressImage(
                                FileUtil.getAccountNamePortrait(this, mAccount),
                                FileUtil.getPresetPointThumbnailFolderPath(this, mAccount),
                                "", 480, 800)
                            YRLog.d("-->> MyProfileActivity test change portrait 13 compressPath=$compressPath")
                            viewModel.setPortrait(File(compressPath)).observe(this) {
                                if (it == ConstantValue.SUCCESS) {
                                    updatePortrait(true)
                                } else {
                                }
                            }
                        } else {
                            toast(R.string.account_select_photo)
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
        }
    }

    companion object {
        fun start(from: Context, memberId : Long, memberNickName : String) {
            from.startActivity<AccountInfoActivity>(
                ConstantValue.INTENT_KEY_MEMBER_ID to  memberId,
                ConstantValue.INTENT_KEY_NICK_NAME to memberNickName
            )
        }
    }

}