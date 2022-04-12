package com.apemans.custom.review

import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.MediaController
import android.widget.Toast
import com.apemans.yruibusiness.base.BaseComponentActivity
import com.apemans.custom.album.MediaConstant
import com.apemans.custom.bean.ContentType
import com.apemans.custom.databinding.CustomserviceActivityReviewBinding
import com.bumptech.glide.Glide


/**
 * 客服消息，图片视频预览
 */
class ReviewActivity : com.apemans.yruibusiness.base.BaseComponentActivity<CustomserviceActivityReviewBinding>() {
    private lateinit var localMediaController: MediaController

    override fun onViewCreated(savedInstanceState: Bundle?) {
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);

        binding.activityReviewIvReturn.setOnClickListener { finish() }
        val type = intent.getIntExtra(MediaConstant.MEDIA_TYPE, -1)
        val path = intent.getStringExtra(MediaConstant.MEDIA_PATH)
        if (path == null) {
            Toast.makeText(this, "", Toast.LENGTH_SHORT).show()
            finish()
        }
        if (type == ContentType.VIDEO) {
            binding.ivImage.visibility = View.GONE
            binding.vvVideo.visibility = View.VISIBLE
            initLocalVideo(path!!)
        } else if (type == ContentType.IMAGE) {
            binding.ivImage.visibility = View.VISIBLE
            binding.vvVideo.visibility = View.GONE
            Glide.with(this).load(path).into(binding.ivImage)
        }


    }

    /**
     * 播放本地视频
     */
    private fun initLocalVideo(path: String) {
        //设置有进度条可以拖动快进
        localMediaController = QMediaController(this)
        binding.vvVideo.setVideoURI(Uri.parse(path))
        binding.vvVideo.start()
    }

}
