package com.apemans.custom.util

import android.content.Context
import android.database.Cursor
import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import android.media.ThumbnailUtils
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.core.os.BuildCompat
import com.apemans.custom.R
import com.apemans.custom.album.MediaConstant
import com.apemans.custom.bean.FolderBean
import com.apemans.custom.bean.MediaData
import java.io.File
import java.text.SimpleDateFormat
import java.util.*


/**
 * Created by ${gexinyu} on 2018/5/28.
 */
object MediaUtils {
    var mapParentPath: MutableMap<String, MutableList<MediaData>> = LinkedHashMap()
    var buckMedia: MutableList<MediaData> = LinkedList()
    var folderBeans: MutableList<FolderBean> = LinkedList()
    val parentPaths: Unit
        get() {}

    /**
     * 获取视频的缩略图
     * 先通过ThumbnailUtils来创建一个视频的缩略图，然后再利用ThumbnailUtils来生成指定大小的缩略图。
     * 如果想要的缩略图的宽和高都小于MICRO_KIND，则类型要使用MICRO_KIND作为kind的值，这样会节省内存。
     *
     * @param videoPath 视频的路径
     * @param width     指定输出视频缩略图的宽度
     * @param height    指定输出视频缩略图的高度度
     * @param kind      参照MediaStore.Images(Video).Thumbnails类中的常量MINI_KIND和MICRO_KIND。
     * 其中，MINI_KIND: 512 x 384，MICRO_KIND: 96 x 96
     * @return 指定大小的视频缩略图
     */
    fun getVideoThumbnail(videoPath: String?, width: Int, height: Int, kind: Int): Bitmap? {
        var bitmap: Bitmap? = null
        // 获取视频的缩略图
        bitmap = ThumbnailUtils.createVideoThumbnail(videoPath!!, kind)
        if (bitmap != null) {
            bitmap = ThumbnailUtils.extractThumbnail(
                bitmap, width, height,
                ThumbnailUtils.OPTIONS_RECYCLE_INPUT
            )
        }
        return bitmap
    }

    // 获取视频缩略图
    fun getVideoThumbnail(filePath: String?): Bitmap? {
        var b: Bitmap? = null
        val retriever = MediaMetadataRetriever()
        try {
            retriever.setDataSource(filePath)
            b = retriever.frameAtTime
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
        return b
    }

    suspend fun getAllMedia(
        activity: Context,
        block: (allMediaList: MutableList<MediaData>) -> Unit
    ) {
        val allPhotos = getAllPhoto(activity)
        val allVideos = getAllVideo(activity)
        //图片
        val allMediaList: MutableList<MediaData> = ArrayList()
        //TODO 待换成文件下载目录
        val folderDirPath = "osaio"
        val allPhotoList: MutableList<MediaData> = ArrayList()
        val recentStr = activity.resources.getString(R.string.help_faq_album_title)
        if (allPhotos.isNotEmpty()) {
            allPhotoList.addAll(allPhotos)
        }
        //视频
        val allVideoList: MutableList<MediaData> = ArrayList()
        if (allVideos.isNotEmpty()) {
            allVideoList.addAll(allVideos)
        }
        //下面是排序
        allMediaList.addAll(allPhotoList)
        allMediaList.addAll(allVideoList)
        //                        //采用冒泡排序的方式排列数据
        sortByTimeRepoList(allMediaList)

        block(allMediaList)

        mapParentPath.clear()
        folderBeans.clear()
        buckMedia.clear()
        buckMedia.addAll(allMediaList)
        for (data in allMediaList) {
            /**
             * 将所有文件装入RECENT目录
             */
            if (mapParentPath.containsKey(recentStr)) {
                var mediaData =
                    mapParentPath[recentStr]
                if (mediaData == null) {
                    mediaData = LinkedList()
                    mediaData.add(data)
                    mapParentPath[recentStr] =
                        mediaData
                } else mediaData.add(data)
            } else if (!mapParentPath.containsKey(recentStr)) {
                val mediaDatas: MutableList<MediaData> = LinkedList()
                mediaDatas.add(data)
                mapParentPath[recentStr] =
                    mediaDatas
            }

            /**
             *将文件按seeker或其它文件夹分类
             */
            val parent = File(data.path).parent
            if (parent != null &&
                folderDirPath != null &&
                parent.contains("Seeker")
            ) {
                if (mapParentPath.containsKey("Seeker")) {
                    mapParentPath["Seeker"]?.add(data)
                } else {
                    val mediaDatas: MutableList<MediaData> = LinkedList()
                    mediaDatas.add(data)
                    mapParentPath["Seeker"] = mediaDatas
                }
            } else if (parent != null) {
                if (mapParentPath.containsKey(parent)) {
                    mapParentPath[parent]?.add(data)
                } else {
                    val mediaDatas: MutableList<MediaData> = LinkedList()
                    mediaDatas.add(data)
                    mapParentPath[parent] = mediaDatas
                }
            }
        }
        for ((key, value) in mapParentPath) {
            val folderBean = FolderBean()
            folderBean.name = key
            folderBean.cover = value[0].path
            folderBean.size = value.size
            if (key.contains("Seeker")) folderBeans.add(
                0,
                folderBean
            ) else folderBeans.add(folderBeans.size, folderBean)
        }
    }

    suspend fun getAllVideo(activity: Context): MutableList<MediaData> {
        val videoList: MutableList<MediaData> = ArrayList()
        val mVideoUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
        val projection = arrayOf(
            MediaStore.Video.Thumbnails._ID,
            MediaStore.Video.Thumbnails.DATA,
            MediaStore.Video.Media.DURATION,
            MediaStore.Video.Media.SIZE,
            MediaStore.Video.Media.DATE_ADDED,
            MediaStore.Video.Media.DISPLAY_NAME,
            MediaStore.Video.Media.DATE_MODIFIED
        )
        //全部视频
        val where = (MediaStore.Video.Media.MIME_TYPE + "=? or "
                + MediaStore.Video.Media.MIME_TYPE + "=? or "
                + MediaStore.Video.Media.MIME_TYPE + "=? or "
                + MediaStore.Video.Media.MIME_TYPE + "=? or "
                + MediaStore.Video.Media.MIME_TYPE + "=? or "
                + MediaStore.Video.Media.MIME_TYPE + "=? or "
                + MediaStore.Video.Media.MIME_TYPE + "=? or "
                + MediaStore.Video.Media.MIME_TYPE + "=? or "
                + MediaStore.Video.Media.MIME_TYPE + "=?")
        val whereArgs = arrayOf(
            "video/mp4", "video/3gp", "video/aiv", "video/rmvb", "video/vob", "video/flv",
            "video/mkv", "video/mov", "video/mpg"
        )
        val mCursor = activity.contentResolver.query(
            mVideoUri,
            projection, where, whereArgs, MediaStore.Video.Media.DATE_ADDED + " DESC "
        )
        if (mCursor != null) {
            while (mCursor.moveToNext()) {
                // 获取视频的路径
                val videoId = mCursor.getInt(mCursor.getColumnIndex(MediaStore.Video.Media._ID))
                val path =
                    mCursor.getString(mCursor.getColumnIndex(MediaStore.Video.Media.DATA))
                val duration =
                    mCursor.getLong(mCursor.getColumnIndex(MediaStore.Video.Media.DURATION))

                val displayName =
                    mCursor.getString(mCursor.getColumnIndex(MediaStore.Video.Media.DISPLAY_NAME))
                //用于展示相册初始化界面
                val timeIndex = mCursor.getColumnIndex(MediaStore.Images.Media.DATE_ADDED)
                val date = mCursor.getLong(timeIndex) * 1000
                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P || BuildCompat.isAtLeastQ()) {
//                    val idLong: Long = mCursor.getLong(id);
//                    val photoUri =
//                        ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, idLong)
                    /**
                     * 理论上应该做文件判断是否存在但因为部分Android Q以上手机此判断相当耗时，暂不处理
                     * FileManagerUtils.isContentUriExists(activity.getApplicationContext(), photoUri)
                     */

                    videoList.add(
                        MediaData(
                            videoId,
                            MediaConstant.VIDEO,
                            path,
                            path,
                            null,
                            duration,
                            date,
                            displayName,
                            false
                        )
                    )
                } else {
                    //需要判断当前文件是否存在  一定要加，不然有些文件已经不存在图片显示不出来
                    val fileIsExists = fileIsExists(path)
                    if (fileIsExists) {
                        videoList.add(
                            MediaData(
                                videoId,
                                MediaConstant.VIDEO,
                                path,
                                path,
                                null,
                                duration,
                                date,
                                displayName,
                                false
                            )
                        )
                    }
                }
            }
            mCursor.close()
        }
        return videoList
    }

    /**
     * 判断当前文件是否存在
     *
     * @param strFile
     * @return
     */
    fun fileIsExists(strFile: String?): Boolean {
        try {
            val f = File(strFile)
            if (!f.exists()) {
                return false
            }
        } catch (e: java.lang.Exception) {
            return false
        }
        return true
    }

    suspend fun getAllPhoto(activity: Context): MutableList<MediaData> {
        val mediaBeen: MutableList<MediaData> = ArrayList()
        val mImageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        val projection = arrayOf(
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.DATA,
            MediaStore.Images.Media.DATE_ADDED,
            MediaStore.Images.Thumbnails.DATA,
            MediaStore.Video.Media.DISPLAY_NAME
        )
        //全部图片
        val where = (MediaStore.Images.Media.MIME_TYPE + "=? or "
                + MediaStore.Images.Media.MIME_TYPE + "=? or "
                + MediaStore.Images.Media.MIME_TYPE + "=?")
        //指定格式
        val whereArgs = arrayOf("image/jpeg", "image/png", "image/jpg")
        //查询
        val mCursor = activity.contentResolver.query(
            mImageUri, projection, where, whereArgs,
            MediaStore.Images.Media.DATE_ADDED + " DESC "
        )
        if (mCursor != null) {
            while (mCursor.moveToNext()) {
                // 获取图片的路径
                val thumbPathIndex = mCursor.getColumnIndex(MediaStore.Images.Thumbnails.DATA)
                val timeIndex = mCursor.getColumnIndex(MediaStore.Images.Media.DATE_ADDED)
                val pathIndex = mCursor.getColumnIndex(MediaStore.Images.Media.DATA)
                val id = mCursor.getColumnIndex(MediaStore.Images.Media._ID)
                val date = mCursor.getLong(timeIndex) * 1000
                val displayName =
                    mCursor.getString(mCursor.getColumnIndex(MediaStore.Video.Media.DISPLAY_NAME))
                var thumbPath: String? = mCursor.getString(thumbPathIndex)
                var filepath: String?
                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P || BuildCompat.isAtLeastQ()) {
//                    val idLong: Long = mCursor.getLong(id);
//                    val photoUri =
//                        ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, idLong)
                    /**
                     * 理论上应该做文件判断是否存在但因为部分Android Q以上手机此判断相当耗时，暂不处理
                     * FileManagerUtils.isContentUriExists(activity.getApplicationContext(), photoUri)
                     */
                    val fi = MediaData(
                        id,
                        MediaConstant.IMAGE,
                        mCursor.getString(pathIndex),
                        null,
                        getPhotoUri(mCursor),
                        date,
                        displayName,
                        false
                    )
                    mediaBeen.add(fi)
                } else {
                    //判断文件是否存在，存在才去加入
                    filepath = mCursor.getString(pathIndex)
                    val b = fileIsExists(filepath)
                    if (b) {
                        val fi = MediaData(
                            id,
                            MediaConstant.IMAGE,
                            filepath,
                            thumbPath,
                            null,
                            date,
                            displayName,
                            false
                        )
                        mediaBeen.add(fi)
                    }
                }
            }
            mCursor.close()
        }
        return mediaBeen
    }

    /**
     * 根据时间进行排序
     *
     * @param itemInfoList
     */
    fun sortByTimeRepoList(itemInfoList: List<MediaData>?) {
        Collections.sort(
            itemInfoList
        ) { item1, item2 ->
            val date1 = Date(item1.data * 1000L)
            val date2 = Date(item2.data * 1000L)
            date2.compareTo(date1)
        }
    }

//    //获取数据库字段
//    private fun getUriColumns(uri: Uri) {
//        val cursor = MeicamContextWrap.getInstance().context.contentResolver.query(
//            uri,
//            null,
//            null,
//            null,
//            null
//        )
//        cursor!!.moveToFirst()
//        val columns = cursor.columnNames
//        for (string in columns) {
//            println(cursor.getColumnIndex(string).toString() + " = " + string)
//        }
//        cursor.close()
//    }

    fun groupListByTime(allMediaTemp: List<MediaData>): ListOfAllMedia {
        //分组算法
        val skuIdMap: MutableMap<String, MutableList<MediaData>> = LinkedHashMap()
        for (mediaData in allMediaTemp) {
            val strTime = SimpleDateFormat("yyyy年MM月dd日").format(Date(mediaData.data))
            var tempList = skuIdMap[strTime]
            /*如果取不到数据,那么直接new一个空的ArrayList**/if (tempList == null) {
                tempList = ArrayList()
                tempList.add(mediaData)
                skuIdMap[strTime] = tempList
            } else {
                tempList.add(mediaData)
            }
        }
        val lists: MutableList<List<MediaData>> = ArrayList()
        val listOfOut: MutableList<MediaData> = ArrayList()
        for ((key, value) in skuIdMap) {
            val mediaData = MediaData()
            mediaData.data = getIntTime(key)
            listOfOut.add(mediaData)
            lists.add(value)
        }
        return ListOfAllMedia(listOfOut, lists)
    }

    private fun getIntTime(time: String): Long {
        val sdr = SimpleDateFormat("yyyy年MM月dd日", Locale.CHINA)
        val date: Date
        try {
            date = sdr.parse(time)
            return date.time
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return 0
    }

    fun getPhotoUri(cursor: Cursor): Uri {
        return getMediaUri(cursor, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
    }

    fun getVideoUri(cursor: Cursor): Uri {
        return getMediaUri(cursor, MediaStore.Video.Media.EXTERNAL_CONTENT_URI)
    }

    fun getMediaUri(cursor: Cursor, uri: Uri?): Uri {
        val id = cursor.getString(cursor.getColumnIndex(MediaStore.MediaColumns._ID))
        return Uri.withAppendedPath(uri, id)
    } //    // 获取当前目录下所有的mp4 MOV文件

    //    private static List<String> getVideoFileName(String fileDir) {
    //        List<MediaData> pathList = new LinkedList<>();
    //        File file = new File(fileDir);
    //        File[] subFile = file.listFiles();
    //
    //        for (File value : subFile) {
    //            // 判断是否为文件夹
    //            if (!value.isDirectory()) {
    //                String filename = value.getName();
    //                // 判断是否为MP4结尾
    //                if (filename.trim().toLowerCase().endsWith(".mp4")) {
    //                    pathList.add(new MediaData(value,MediaConstant.VIDEO, value.getPath(),value.getPath(),null, value.length(),date, displayName,false));
    //                } else if (filename.trim().toUpperCase().endsWith(".MOV")) {
    //                    pathList.add(filename);
    //                }
    //            }else {
    //
    //            }
    //        }
    //        return pathList;
    //    }
    interface LocalMediaCallback {
        fun onLocalMediaCallback(allMediaTemp: List<MediaData>?)
    }

    class ListOfAllMedia(
        private var listOfParent: List<MediaData>?,
        private var listOfAll: List<List<MediaData>>?
    ) {
        fun getListOfParent(): List<MediaData>? {
            return if (listOfParent == null) {
                ArrayList()
            } else listOfParent
        }

        fun setListOfParent(listOfParent: List<MediaData>?) {
            this.listOfParent = listOfParent
        }

        fun getListOfAll(): List<List<MediaData>>? {
            return if (listOfAll == null) {
                ArrayList()
            } else listOfAll
        }

        fun setListOfAll(listOfAll: List<List<MediaData>>?) {
            this.listOfAll = listOfAll
        }
    }
}