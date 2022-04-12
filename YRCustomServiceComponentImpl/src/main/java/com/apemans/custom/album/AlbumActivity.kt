package com.apeman.customerservice.view.album

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.Animation
import android.view.animation.TranslateAnimation
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.apeman.customerservice.databean.SelectMediaData
import com.apemans.yruibusiness.base.BaseComponentActivity
import com.apemans.custom.R
import com.apemans.custom.adapter.SelectMediaAdapter
import com.apemans.custom.bean.MediaData
import com.apemans.custom.databinding.ActivityAlbumBinding
import com.apemans.custom.util.MediaUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.launch


class AlbumActivity : com.apemans.yruibusiness.base.BaseComponentActivity<ActivityAlbumBinding>(), AlbumFragment.OnSelectMedia {


    private lateinit var selectMediaAdapter: SelectMediaAdapter
    private lateinit var demoCollectionAdapter: AlbumFragmentAdapter

    private lateinit var viewPager: ViewPager2

    private var position = 0

    private lateinit var allFragment: AlbumFragment
    private lateinit var videoFragment: AlbumFragment
    private lateinit var pictureFragment: AlbumFragment
    private var mediaJob: Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        verifyStoragePermissions()

        initTitle()
        initViewPager()
        initFoot()
        viewPager.currentItem = 0
    }


    override fun onResume() {
        super.onResume()
//        if (MediaUtils.buckMedia.isNotEmpty()) {
//            Handler(Looper.getMainLooper()).postDelayed({
//                fragmentNotify(MediaUtils.buckMedia)
//            }, 100)
//        }
    }

    private fun initTitle() {
        binding.activityAlbumIvLeft.setOnClickListener {
            finish()
        }
    }

    private fun initViewPager() {
        viewPager = findViewById(R.id.viewpager2)
        val list = mutableListOf<Fragment>()
        allFragment = AlbumFragment.newInstance(0)
        videoFragment = AlbumFragment.newInstance(1)
        pictureFragment = AlbumFragment.newInstance(2)
        list.add(allFragment)
        list.add(videoFragment)
        list.add(pictureFragment)
        binding.activityAlbumLlAll.setOnClickListener {
            viewPager.currentItem = 0
        }
        binding.activityAlbumLlVideo.setOnClickListener {
            viewPager.currentItem = 1
        }
        binding.activityAlbumLlPicture.setOnClickListener {
            viewPager.currentItem = 2
        }
        demoCollectionAdapter = AlbumFragmentAdapter(this)
        demoCollectionAdapter.addFragments(list)
        viewPager.adapter = demoCollectionAdapter
        viewPager.offscreenPageLimit = 2
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(pos: Int) {
                super.onPageSelected(pos)
                moveTo(pos)
                position = pos
            }
        })
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun initFoot() {
        initSelectRecycleView()

        binding.activityAlbumRlNext.setOnClickListener {

            val intent = Intent()
            val paths = ArrayList<String>()
            selectMediaAdapter.getData().forEach {
                paths.add(it.videoClipPath)
            }
            intent.putStringArrayListExtra("paths", paths)
            setResult(RESULT_OK, intent)
            finish()
        }
    }

    private fun initSelectRecycleView() {
        selectMediaAdapter = SelectMediaAdapter(this, R.layout.item_select_media)

        binding.rvSelectMedia.layoutManager = LinearLayoutManager(
            this,
            LinearLayoutManager.HORIZONTAL,
            false
        )
        binding.rvSelectMedia.adapter = selectMediaAdapter
    }


    private fun moveTo(pos: Int) {
        val animation = TranslateAnimation(
            Animation.RELATIVE_TO_SELF, position * 1f, Animation.RELATIVE_TO_SELF, pos * 1f,
            Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0f
        )
        animation.duration = 300
        animation.repeatMode = Animation.REVERSE
        animation.interpolator = AccelerateInterpolator()
        animation.fillAfter = true
        binding.ivBgSelect.startAnimation(animation)
    }


    override fun onMediaPath(path: String) {
        selectMediaAdapter.addItem(path)
        if (binding.linearLayout2.visibility == View.GONE) {
            binding.linearLayout2.visibility = View.VISIBLE
//            viewpager2.setPaddingBottom(Util.dpToPx(this, 70))
        }
        allFragment.notifyAdapter()
        videoFragment.notifyAdapter()
        pictureFragment.notifyAdapter()
    }

    override fun onRemovePath(path: String) {
        selectMediaAdapter.removeClipVideo(path)
        if (selectMediaAdapter.getData().size <= 0) {
            binding.linearLayout2.visibility = View.GONE
//            viewpager2.setPaddingBottom(Util.dpToPx(this, 0))
        }

        allFragment.notifyAdapter()
        videoFragment.notifyAdapter()
        pictureFragment.notifyAdapter()
    }


    private fun getMediaJob(block: (mediaData: MutableList<MediaData>) -> Unit): Job {
        return lifecycleScope.launch(Dispatchers.IO) {
            MediaUtils.getAllMedia(this@AlbumActivity) {
                ensureActive()
                block(it)
            }
        }
    }

    private fun getMedia() {
        /*
         * 要判断是否有权限，无权限的时候不能去读写SD卡
         * To determine whether you have permission, you cannot read or write to the SD card without permission
         * */
        if (mediaJob !== null) {
            return
        }
        mediaJob = getMediaJob { mutableList ->
            val loseItems = mutableListOf<MediaData>()
            if (SelectMediaData.selectMediaDatas.isNotEmpty()) {
                for ((i, item) in SelectMediaData.selectMediaDatas.withIndex()) {
                    val findLast = mutableList.findLast { it.path == item.path }
                    if (findLast == null) {
                        onRemovePath(item.path)
                        loseItems.add(item)
                    }
                }
                for (item in loseItems) {
                    SelectMediaData.selectMediaDatas.remove(item)
                }
            }
            mediaJob = null
            Handler(Looper.getMainLooper()).post {
                fragmentNotify(mutableList)
            }
        }
    }


    private fun fragmentNotify(mediaData: List<MediaData>) {
        allFragment.addData(mediaData.toMutableList())
        videoFragment.addData(mediaData.filter { it.duration > 0L }.toMutableList())
        pictureFragment.addData(mediaData.filter { it.duration <= 0L }.toMutableList())
    }


    private val REQUEST_EXTERNAL_STORAGE = 1
    private val PERMISSIONS_STORAGE = arrayOf(
        "android.permission.READ_EXTERNAL_STORAGE",
        "android.permission.WRITE_EXTERNAL_STORAGE"
    )


    private fun verifyStoragePermissions() {
        try {
            //检测是否有写的权限 ,不需要
            val permissionWrite: Int = ActivityCompat.checkSelfPermission(
                this,
                "android.permission.WRITE_EXTERNAL_STORAGE"
            )
            val permissionRead: Int = ActivityCompat.checkSelfPermission(
                this,
                "android.permission.READ_EXTERNAL_STORAGE"
            )
            if (permissionRead != PackageManager.PERMISSION_GRANTED) {
                // 没有写的权限，去申请写的权限，会弹出对话框
                ActivityCompat.requestPermissions(
                    this,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
                )
                return
            }
            getMedia()
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_EXTERNAL_STORAGE -> {
                grantResults[0] == PackageManager.PERMISSION_GRANTED
                if (grantResults.size == 2 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                    grantResults[1] == PackageManager.PERMISSION_GRANTED
                ) {
                    getMedia()
                }
            }
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        SelectMediaData.selectMediaDatas.clear()

    }

    class AlbumFragmentAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {
        private val fragments = mutableListOf<Fragment>()

        fun addFragments(fragments: List<Fragment>) {
            this.fragments.addAll(fragments)
        }

        override fun getItemCount(): Int = fragments.size

        override fun createFragment(position: Int): Fragment {
            // Return a NEW fragment instance in createFragment(int)
            return fragments[position]
        }
    }

    override fun onViewCreated(savedInstanceState: Bundle?) {

    }

}