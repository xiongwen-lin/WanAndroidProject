package com.apemans.smartipcimpl.ui.ipcdevices.player.util

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import com.apemans.dmapi.support.SupportBrand
import com.apemans.smartipcimpl.R
import com.dylanc.longan.application
import com.nooie.common.utils.file.FileUtil
import com.nooie.common.utils.file.MediaStoreUtil
import com.nooie.common.utils.graphics.DisplayUtil
import com.nooie.sdk.media.NooieMediaPlayer
import java.io.File
import java.lang.IllegalArgumentException
import java.lang.RuntimeException
import java.lang.StringBuilder

/**
 * @Author:dongbeihu
 * @Description: 播放模块文件处理器：截图、拍照、录像文件
 * @Date: 2021/12/7-11:14
 */
object PlayerFileUtils {

    private var thumbnailBitmap: Bitmap? = null
    private var firstAnim: ObjectAnimator? = null
    private var secondAnim: AnimatorSet? = null


    fun sendRefreshPicture(context: Context, file: String?) {
        if (TextUtils.isEmpty(file) || !File(file).exists()) {
            return
        }
        try {
            val file = File(file)
            val uri = Uri.fromFile(file)
            context.sendBroadcast(Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri))
        } catch (e: Exception) {
            Log.e("PlayerUtils", "sendRefreshPicture----${e.message}")
        }
    }



    /**
     * 展示截屏、保存视频动画
     */
    fun showVideoThumbnail(file: String?, video: Boolean,player: NooieMediaPlayer?,ivThumbnail :ImageView,isLandscape :Boolean,isSnapShotNeedTips :Boolean) {

        if (TextUtils.isEmpty(file)||ivThumbnail == null || player == null) {
            return
        }
        if (isSnapShotNeedTips){
            Toast.makeText(ivThumbnail.context,if (video) R.string.living_file_save_to_album else R.string.living_photo_file_save_to_album,
                Toast.LENGTH_SHORT ).show()
        }

        ivThumbnail.setImageBitmap(null)
        if (thumbnailBitmap != null && !thumbnailBitmap?.isRecycled!!) {
            thumbnailBitmap?.recycle()
            thumbnailBitmap = null
        }

        if (firstAnim != null && firstAnim!!.isRunning) {
            firstAnim!!.cancel()
        }
        if (secondAnim != null && secondAnim!!.isRunning ) {
            secondAnim!!.cancel()
        }

        // get bitmap
        thumbnailBitmap = createBitmap(file!!, video)
        ivThumbnail.setVisibility(View.VISIBLE)
        val playerParams = player.layoutParams as ConstraintLayout.LayoutParams
        val width = player.measuredWidth //playerParams.width;
        val height = player.measuredHeight //playerParams.height;
        val params = ivThumbnail.getLayoutParams() as ConstraintLayout.LayoutParams
        var w = width
        var h = height
        if (height * 1.0 / width > 9.0 / 16) {
            // width is base
            h = w * 9 / 16
        } else {
            // height is base
            w = h * 16 / 9
        }
        params.width = w
        params.height = h
        ivThumbnail.layoutParams = params
        ivThumbnail.scaleY = 1.0f
        ivThumbnail.scaleX = 1.0f
        ivThumbnail.translationY = 0f
        ivThumbnail.translationX = 0f

        // first animation
        firstAnim = ObjectAnimator.ofFloat(ivThumbnail, "Alpha", 0.0f, 1.0f)
        firstAnim?.duration = 300
        firstAnim?.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animator: Animator) {}
            override fun onAnimationEnd(animator: Animator) {
                if (ivThumbnail == null) {
                    return
                }
                // setup image
                if (thumbnailBitmap == null) {
                    //Snapshot default image
                    ivThumbnail.setImageResource(R.drawable.device_default_preview)
                } else {
                    ivThumbnail.setImageBitmap(thumbnailBitmap)
                }

                // second animation
                val margin = DisplayUtil.dpToPx(application, 16f)
                val landMarginBottom = DisplayUtil.dpToPx(application, 100f)
                val pivotX = width - margin

                ivThumbnail.pivotX = pivotX.toFloat()
                if (isLandscape) {
                    ivThumbnail.pivotY = (height - landMarginBottom).toFloat()
                } else {
                    ivThumbnail.pivotY = (height - margin).toFloat()
                }
            }

            override fun onAnimationCancel(animator: Animator) {}
            override fun onAnimationRepeat(animator: Animator) {}
        })
        val scaleX = ObjectAnimator.ofFloat(ivThumbnail, "ScaleX", 1.0f, 0.2f)
        val scaleY = ObjectAnimator.ofFloat(ivThumbnail, "ScaleY", 1.0f, 0.2f)
        secondAnim = AnimatorSet()
        secondAnim?.playTogether(scaleX, scaleY)
        secondAnim?.duration = 600
        secondAnim?.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animator: Animator) {}
            override fun onAnimationEnd(animator: Animator) {
                dismissThumbnail(ivThumbnail)
            }

            override fun onAnimationCancel(animator: Animator) {}
            override fun onAnimationRepeat(animator: Animator) {}
        })
        val showThumbnailAnim = AnimatorSet()
        showThumbnailAnim.playSequentially(firstAnim, secondAnim)
        showThumbnailAnim.start()
    }

    /**
     * 重置动画缩略图
     */
   private   fun dismissThumbnail(ivThumbnail :ImageView){
       Thread.sleep(1200)
       ivThumbnail.visibility = View.GONE
       ivThumbnail.scaleY = 1.0f
       ivThumbnail.scaleX = 1.0f
       ivThumbnail.setImageBitmap(null)
   }

    private fun createBitmap(file: String, video: Boolean): Bitmap? {
        if (video) {
            val retriever = MediaMetadataRetriever()
            try {
                retriever.setDataSource(file)
                return retriever.frameAtTime
            } catch (e: IllegalArgumentException) {
                e.printStackTrace()
            } catch (e: RuntimeException) {
                e.printStackTrace()
            } finally {
                try {
                    retriever.release()
                } catch (e: RuntimeException) {
                    e.printStackTrace()
                }
            }
        } else {
            return BitmapFactory.decodeFile(file)
        }
        return null
    }

    fun updateFileInMediaStore(account: String?, path: String?, mediaType: String?) {
        if (TextUtils.isEmpty(mediaType) || !File(path).exists()) {
            return
        }
        val relativeSubFolderSb = StringBuilder()
        relativeSubFolderSb.append(SupportBrand.osaio_brand).append(File.separator).append(account)
        if (MediaStoreUtil.MEDIA_TYPE_IMAGE_JPEG.equals(mediaType, ignoreCase = true)) {
            if (path?.contains(FileUtil.SnapshotDir) == true){
                relativeSubFolderSb.append(File.separator).append(FileUtil.SnapshotDir)
            } else  if (path?.contains(FileUtil.PresetPointThumbnailDir) == true){
                relativeSubFolderSb.append(File.separator).append(FileUtil.PresetPointThumbnailDir)
            }
            MediaStoreUtil.createMediaStoreFileForImage(application, path, relativeSubFolderSb.toString(), null, null, mediaType, null)
        } else if (MediaStoreUtil.MEDIA_TYPE_VIDEO_MP4.equals(mediaType, ignoreCase = true)) {
            relativeSubFolderSb.append(File.separator).append(FileUtil.VideoDir)
            MediaStoreUtil.createMediaStoreFileForVideo(application, path, relativeSubFolderSb.toString(), null, null, mediaType, null)
        }
    }
}