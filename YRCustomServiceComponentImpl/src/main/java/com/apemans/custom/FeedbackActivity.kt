package com.apemans.custom


import android.Manifest
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.PopupWindow
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.alibaba.android.arouter.facade.annotation.Route

import com.apemans.business.apisdk.client.define.HttpCode

import com.apemans.yruibusiness.base.BaseComponentActivity
import com.apemans.yruibusiness.ui.toolbar.setToolbar
import com.apemans.custom.adapter.MediaAddAdapter
import com.apemans.custom.album.MediaConstant
import com.apemans.custom.album.MediaConstant.INTENT_KEY_NICK_NAME
import com.apemans.custom.album.MediaConstant.REQUEST_CODE_CUSTOM_NAME
import com.apemans.custom.bean.MediaInfo
import com.apemans.custom.databinding.ActivityFeedbackBinding
import com.apemans.custom.util.DialogUtils
import com.apemans.custom.util.FileUtil
import com.apemans.custom.util.FileUtil.getCacheDir
import com.apemans.custom.viewModel.FeedbackViewModel
import com.apemans.custom.widget.PhotoPopupWindows
import com.apemans.logger.YRLog
import com.apemans.quickui.superdialog.SmartDialog
import com.apemans.userapi.enrty.UserInfo
import com.dylanc.longan.requestPermissionLauncher
import com.dylanc.longan.startActivity
import com.dylanc.longan.toast
import java.io.File
import java.util.*
import java.util.regex.Matcher
import java.util.regex.Pattern

/**
 * @Author:QCoder
 * @Description: 客服反馈旧版
 * @Date: 2022/2/7-15:17
 */
@Route(path = ACTIVITY_PATH_FEEDBACK_FAQ)
class FeedbackActivity : com.apemans.yruibusiness.base.BaseComponentActivity<ActivityFeedbackBinding>()  {
    var mFeedbackTypeId = -1
    private var mFeedbackProductId = -1
    private var mUploadPictureMap: MutableMap<String, String>? = null

    lateinit var mMediaAddAdapter: MediaAddAdapter
    var mMediaInfos: MutableList<MediaInfo> = mutableListOf()
    private val mPictures: MutableList<String> = mutableListOf()

    private var popMenus: PhotoPopupWindows? = null
    private var haveChoosePhoto = false
    lateinit var mCameraPath: String

    // viewModel
    private lateinit var viewModel : FeedbackViewModel

  /*  private val PERMS_CAMERA = arrayOf(
        Manifest.permission.CAMERA,
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )*/
    private val PERMS_CAMERA = Manifest.permission.CAMERA

    private val PERMS_READ_WRITE =  Manifest.permission.READ_EXTERNAL_STORAGE
    private val RESULT_CODE_KITKAT_PHOTO = 321
    private val RESULT_CODE_PHOTO = 322
    private val RESULT_CODE_CAMERA = 323
    private val RESULT_CODE_CLIP = 324

    private var userAccount = ""
    private var uid = ""

    private var mSelectPhotoDialog: Dialog? = null

    override fun onViewCreated(savedInstanceState: Bundle?) {
        setToolbar {
            title = resources.getString(R.string.feedback_page_title)
            leftIcon(R.drawable.ic_back) {
                finish()
            }
        }
        viewModel = registerViewModule(FeedbackViewModel::class.java)
        initView()
        initData()
    }


    fun initView() {
        binding.emailContainer.setOnClickListener {
          CustomNameActivity.start(this, REQUEST_CODE_CUSTOM_NAME)
        }
        binding.ivPhotoAndVideoTopAdd.setOnClickListener {
            //showPopMenu()
            showSelectPhotoDialog()
        }
        binding.tvEmail.text = userAccount
        binding.btnSend.setOnClickListener {
            onClickBtn()
        }

        mMediaAddAdapter = MediaAddAdapter()
        binding.rvPhotoAndView.layoutManager = GridLayoutManager(this, 4)
        binding.rvPhotoAndView.adapter = mMediaAddAdapter
        setupInputContent()
    }

    private fun initData() {
        mUploadPictureMap = HashMap(16)
        (mUploadPictureMap as HashMap<String, String>).clear()
        mMediaAddAdapter.setMediaClickListener(object : MediaAddAdapter.onMediaClickListener {
            override fun onMediaAdd() {
                gotoPhoto()
            }

            override fun onMediaDelete(position: Int) {
                deleteMedia(position)
            }
        })


    }

    private fun setupInputContent() {
        binding.etContent.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                checkBtnEnable()
            }
        })
        binding.ivClearContent.setOnClickListener {
            binding.etContent.setText("")
        }
        checkBtnEnable()
    }

    private fun checkBtnEnable() {
        if (binding.etContent.text.isNotEmpty()) {
            binding.btnSend.isEnabled = true
            binding.btnSend.setTextColor(resources.getColor(R.color.theme_color))
            binding.btnSend.background = resources.getDrawable(R.drawable.button_sure_radius_22)
        } else {
            binding.btnSend.isEnabled = false
            binding.btnSend.setTextColor(resources.getColor(R.color.unable_clickable_color))
            binding.btnSend.background = resources.getDrawable(R.drawable.button_sure_radius_gray)
        }
    }


    private fun deleteMedia(position: Int) {
        if (mMediaInfos.size > 0 && position < mMediaInfos.size) {
            if (position == 0 && 1 == mMediaInfos.size) {
                binding.ivPhotoAndVideoTopAdd.visibility = View.VISIBLE
            }
            mMediaInfos.removeAt(position)
        }
    }

    private fun gotoPhoto() {
        val intent = Intent()
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        intent.type = "image/*"
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false)
        intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // 4.4版本
            intent.action = Intent.ACTION_OPEN_DOCUMENT
            startActivityForResult(intent, RESULT_CODE_KITKAT_PHOTO)
        } else {
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(intent, RESULT_CODE_PHOTO)
        }
    }

    private fun onClickBtn() {
        if (binding.tvEmail.text.isEmpty()) {
            toast(resources.getString(R.string.feedback_input_contact_info))
            return
        }
        if (!isEmail(binding.tvEmail.text.toString())) {
            toast(resources.getString(R.string.feedback_tip_email))
            return
        }
        showFeedbackDialog()
    }

    //邮箱验证
    private fun isEmail(strEmail: String): Boolean {
        val strPattern =
            "^[a-zA-Z0-9][\\w\\.-]*[a-zA-Z0-9]@[a-zA-Z0-9][\\w\\.-]*[a-zA-Z0-9]\\.[a-zA-Z][a-zA-Z\\.]*[a-zA-Z]$"
        val p: Pattern = Pattern.compile(strPattern)
        val m: Matcher = p.matcher(strEmail)
        return m.matches()
    }

    private fun convertUriByPath(path: String): Uri {
        YRLog.d("-->> MyProfileActivity test change portrait 6 path=$path")
        val tmpFile = File(path)
        if (tmpFile == null || !tmpFile.exists()) {
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

    private fun showPopMenu() {
        popMenus?.dismiss()

        popMenus = PhotoPopupWindows(this, object : PhotoPopupWindows.OnClickSelectPhotoListener {
            override fun onClick(takePhoto: Boolean) {
                if (takePhoto) {
                    haveChoosePhoto = false
                    requestPermissions(PERMS_CAMERA)
                } else {
                    haveChoosePhoto = true
                    requestPermissions(PERMS_READ_WRITE)
                }
            }
        })
        popMenus!!.setOnDismissListener(PopupWindow.OnDismissListener { popMenus = null })

        popMenus!!.showAtLocation(findViewById(R.id.layout_feedback),
            Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL, 0, 0)
    }

    private val  mSelectPhotoListener = object :DialogUtils.OnClickButtonListener{
        override fun onClickLeft() {
            haveChoosePhoto = false
            requestPermissions(PERMS_CAMERA)
        }

        override fun onClickRight() {
            haveChoosePhoto = true
            requestPermissions(PERMS_READ_WRITE)
        }

    }


    /**
     * 添加素材（拍照、图库）
     */
    private fun showSelectPhotoDialog() {
        hideSelectPhotoDialog()
        val dialog = DialogUtils(this)
        mSelectPhotoDialog = dialog.showConfirmWithSubMsgDialog(
            this,
            R.string.feedback_add_photo_title,
            R.string.feedback_add_photo_content,
            R.string.take_photo,
            R.string.album,
            mSelectPhotoListener
        )
    }


    private fun hideSelectPhotoDialog() {
        if (mSelectPhotoDialog != null) {
            mSelectPhotoDialog!!.dismiss()
            mSelectPhotoDialog = null
        }
    }


    private fun upLoadPicture(picture: String?) {
        YRLog.d("-->> FeedbackActivity upLoadPicture isPicture=" + picture?.let {
            isPictureFile(it)
        })
        if (picture != null) {
            if (!isPictureFile(picture)) {
                Toast.makeText(this@FeedbackActivity,R.string.feedback_picture_type_error,Toast.LENGTH_SHORT).show()
                return
            }
        }

        if (!TextUtils.isEmpty(picture)) {
            if (picture != null) {
                viewModel.upLoadPicture(
                    uid,
                    userAccount, picture)
                    .observe(this) {
                        if (it.resultStr ==  MediaConstant.SUCCESS) {
                            addPhotoAndVideo(it.picPath)
                            mUploadPictureMap?.put(it.picPath, it.fileNameSb)
                        }
                    }
            }
        }
    }

    private fun isPictureFile(path: String): Boolean {
        val pictureTypes = arrayOf("jpg", "jpeg", "png")
        for (i in pictureTypes.indices) {
            if (!TextUtils.isEmpty(path) && (path.contains(pictureTypes[i])
                        || path.contains(pictureTypes[i].toUpperCase()))) {
                return true
            }
        }
        return false
    }

    private fun addPhotoAndVideo(path: String) {
        YRLog.d(
            "-->> FeedbackActivity addPhotoAndVideo isPicture="
                    + isPictureFile(path))
        if (!isPictureFile(path)) {
            Toast.makeText(this@FeedbackActivity,R.string.feedback_picture_type_error,Toast.LENGTH_SHORT).show()
            return
        }
        if (!TextUtils.isEmpty(path) && !mPictures.contains(path)) {
            var mediaInfo = MediaInfo()
            mediaInfo.path = path
            mMediaInfos.add(mediaInfo)
            mMediaAddAdapter.setData(mMediaInfos)
            mPictures.add(path)
        }
        if (mPictures.size > 0 && binding.ivPhotoAndVideoTopAdd.visibility == View.VISIBLE) {
            binding.ivPhotoAndVideoTopAdd.visibility = View.GONE
        }
        refreshUI()
    }

    private fun deletePhotoAndVideo(path: String) {
        if (!TextUtils.isEmpty(path) && mPictures.contains(path)) {
            mPictures.remove(path)
        }
        if (!TextUtils.isEmpty(path) && mUploadPictureMap!!.containsKey(path)) {
            mUploadPictureMap!!.remove(path)
        }
        if (mPictures.size == 0 && binding.ivPhotoAndVideoTopAdd.visibility == View.GONE) {
            binding.ivPhotoAndVideoTopAdd.visibility = View.VISIBLE
        }
        refreshUI()
    }

    private fun refreshUI() {
        binding.etContent.clearFocus()
    }

    private fun startTakePhotoIntent() {
        val intent = Intent()
        intent.action = MediaStore.ACTION_IMAGE_CAPTURE
        val file = File(
            FileUtil.getCacheDir(this, userAccount),
            getTodayStartTimeStamp().toString() + ".JPG"
        )
        mCameraPath = file.absolutePath
        val cropCameraPicUri = convertUriByPath(mCameraPath)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, cropCameraPicUri)
        startActivityForResult(intent, RESULT_CODE_CAMERA)
    }


    /**
     * 提示是否提交反馈
     */
    private fun showFeedbackDialog() {
        SmartDialog.build(supportFragmentManager)
           // .setTitle(resources.getString(R.string.nooie_play_limit_time_title))
            .setContentText(resources.getString(R.string.feedback_confirm_feedback))
            .setPositiveTextName(resources.getString(R.string.confirm))
            .setNegativeTextName(resources.getString(R.string.cancel))
            .setOnPositive {
                it.dismiss()
                submitAction()
            }.setOnNegative {
                it.dismiss()
            }.show()
    }

    private fun submitAction() {
        if (binding.tvEmail.text.isEmpty()) {
            toast(resources.getString(R.string.feedback_input_contact_info))
            return
        }
        mFeedbackTypeId = 1
        mFeedbackProductId = 1
        viewModel.postFeedback(mFeedbackTypeId, mFeedbackProductId,
            binding.tvEmail.text.toString(), binding.etContent.text.toString(), getUploadPictureParam(mUploadPictureMap))
            .observe(this){
                if (it.code == HttpCode.SUCCESS_CODE) {
                    toast(resources.getString(R.string.feedback_send_success))
                    finish()
                } else {
                    toast(it.msg)
                }
            }
    }

    private fun getUploadPictureParam(uploadPictureMap: Map<String, String>?): String? {
        val uploadPictureParamSb = StringBuilder()
        try {
            val uploadPictureParamIterator = uploadPictureMap?.entries?.iterator()
            if (uploadPictureParamIterator != null) {
                while (uploadPictureParamIterator.hasNext()) {
                    val uploadPictureParamEntry = uploadPictureParamIterator?.next()
                    if (!TextUtils.isEmpty(uploadPictureParamEntry.value)) {
                        uploadPictureParamSb.append(uploadPictureParamEntry.value)
                        if (uploadPictureParamIterator.hasNext()) {
                            uploadPictureParamSb.append(",")
                        }
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        Log.d("", "-->> FeedbackActivity getUploadPictureParam uploadPictureParamSb=$uploadPictureParamSb")
        return uploadPictureParamSb.toString()
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            when(requestCode) {
                REQUEST_CODE_CUSTOM_NAME -> {
                    // 邮箱
                    val nickName = data?.getStringExtra(INTENT_KEY_NICK_NAME)
                    binding.tvEmail.text = nickName
                }
                RESULT_CODE_CAMERA -> {
                    upLoadPicture(mCameraPath)
                }
                RESULT_CODE_PHOTO -> {
                    if (data != null) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                            val targetFile = File(FileUtil.getCacheDir(this, userAccount),
                                System.currentTimeMillis().toString() + ".JPG")
                            val copyFileResult = viewModel.copyFileToPrivateStorage(data.data!!, targetFile.absolutePath)
                                if (copyFileResult.resultStr == MediaConstant.SUCCESS) {
                                    upLoadPicture(copyFileResult.picPath)
                                }else{
                                    upLoadPicture(FileUtil.getFilePathByUri(this, data?.data))
                                }
                        } else {
                            upLoadPicture(FileUtil.getFilePathByUri(this, data?.data))
                        }
                    }
                }
                RESULT_CODE_KITKAT_PHOTO -> {
                    if (data != null) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                            val targetFile = File(FileUtil.getCacheDir(this, userAccount),
                                System.currentTimeMillis().toString() + ".JPG")
                            val copyFileResult = viewModel.copyFileToPrivateStorage(data.data!!, targetFile.absolutePath)
                            if (copyFileResult.resultStr == MediaConstant.SUCCESS) {
                                upLoadPicture(copyFileResult.picPath)
                            }else{
                                upLoadPicture(FileUtil.getFilePathByUri(this, data?.data))
                            }
                        } else {
                            upLoadPicture(FileUtil.getFilePathByUri(this, data.data))
                        }
                    }
                }
            }
        }
    }

    companion object {
        fun start(from: Context) {
            from.startActivity<FeedbackActivity>()
        }
    }

    open fun requestPermissions(perms: String) {
        requestPermissionLauncher.launch(perms.toString())
    }

    private val requestPermissionLauncher = requestPermissionLauncher(
        onGranted = {
            permissionsGranted()
        },
        onDenied = {
            YRLog.e("用户拒绝权限--")
        },
        onShowRequestRationale = {

        }
    )
    private fun permissionsGranted() {
        if (haveChoosePhoto) {
            gotoPhoto()
        } else {
            startTakePhotoIntent()
        }
    }

    private fun getTodayStartTimeStamp(): Long {
        val calendar = Calendar.getInstance()
        calendar[11] = 0
        calendar[12] = 0
        calendar[13] = 0
        return calendar.timeInMillis
    }

}