package com.apemans.base.utils

import android.annotation.SuppressLint
import android.content.ContentUris
import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.net.toUri
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File


/***********************************************************
 * @Author : caro
 * @Date   : 2021/8/12
 * @Func:
 *
 *
 * @Description:
 *
 *
 ***********************************************************/

private const val TAG = "MediStoreApi"

data class MediaStoreFileProperty(
    val id: Long,
    val uri: Uri,
    val date: Long,
    val name: String,
    val size: Long,
    val mimeType: Int,
)

data class MediaItem(
    val id: Long,
    val path: String?,
    val uri: Uri,
    val name: String,
    val length: Long,
    val date: Long,
    val isVideo: Boolean,
    val videoDuration: Long,
)

/**
 * 插入文件名字到共享目录DCIM/$dir 下，名称为insertName
 * @param dir 待插入的文件路径 如果该目录不存在，则会创建
 * @param insertName  插入的文件名称
 * NOTE：1：如果插入的文件名在对应路径存在，则会再次插入并名字为：insertName（1）
 * NOTE：2：DCIM目录下只允许插入视频或图片 -- Media 媒体类型文件
 */
@RequiresApi(Build.VERSION_CODES.Q)
fun insertMediaFileToDCIMDir(
    context: Context,
    dir: String,
    insertName: String,
): Uri? {
    //插入到共享区相册路径下dir
    val values = ContentValues()
    val isPhoto = insertName.isPhoto
    val isVideo = insertName.isVideo
    //Media类型
    val mimeType = when {
        isPhoto -> {
            "image/*"
        }
        isVideo -> {
            "video/*"
        }
        else -> {
            throw RuntimeException("DCIM下只允许插入Media文件，否则会报：Error:java.lang.Exception: Primary directory DCIM not allowed for content://media/external/file; allowed directories are [Download, Documents]")
        }
    }
    values.put(MediaStore.Files.FileColumns.MIME_TYPE, mimeType)
    values.put(MediaStore.Files.FileColumns.DISPLAY_NAME, insertName)

    var uriExternal: Uri? = null
    val path: String
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        path = Environment.DIRECTORY_DCIM + "/$dir"
        when {
            isPhoto -> {
                uriExternal = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                values.put(MediaStore.Images.Media.RELATIVE_PATH, path)
            }
            isVideo -> {
                uriExternal = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                values.put(MediaStore.Video.Media.RELATIVE_PATH, path)
            }
        }

        Log.i("MediaStore", "above android Q uriExternal == $uriExternal ")
        val uri = uriExternal?.let { context.contentResolver.insert(it, values) }
        Log.i("MediaStore", "insertMediaFileToDCIMDir $dir - > $insertName uri == $uri")
        return uri
    }
    return null

}

/**
 * 将文件拷贝到DCIM/$APP目录下
 * @param dir DCIM/$dir 路径
 * @param insertName 文件名
 */
fun File.copyFileToDCIMDir(context: Context, dir: String, insertName: String) {
    Log.i("MediaStore", "fileName = $insertName")


    val isPhoto = insertName.isPhoto
    val isVideo = insertName.isVideo
    //Media类型
    val mimeType = when {
        isPhoto -> {
            "image/*"
        }
        isVideo -> {
            "video/*"
        }
        else -> {
            throw RuntimeException("DCIM下只允许插入Media文件，否则会报：Error:java.lang.Exception: Primary directory DCIM not allowed for content://media/external/file; allowed directories are [Download, Documents]")
        }
    }

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        //插入到共享区相册路径下dir
        val values = ContentValues()
        values.put(MediaStore.Files.FileColumns.MIME_TYPE, mimeType)
        values.put(MediaStore.Files.FileColumns.DISPLAY_NAME, insertName)

        var uriExternal: Uri? = null
        val path = Environment.DIRECTORY_DCIM + "/$dir"
        when {
            isPhoto -> {
                uriExternal = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                values.put(MediaStore.Images.Media.RELATIVE_PATH, path)
            }
            isVideo -> {
                uriExternal = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                values.put(MediaStore.Video.Media.RELATIVE_PATH, path)
            }
        }
        Log.i(TAG, "above android Q uriExternal == $uriExternal ")
        val uri = uriExternal?.let { context.contentResolver.insert(it, values) }
        Log.i(TAG, "insertMediaFileToDCIMDir $dir - > $insertName uri == $uri")

        uri?.apply {
            inputStream().use { inputStream ->
                context.contentResolver.openOutputStream(this)?.use {
                    inputStream.copyTo(it)
                }
            }
        }
        //Copy完成后，删除缓存文件
        delete()

        return
    }

    @Suppress("DEPRECATION")
    val relativePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).absolutePath + "/$dir/"
    if (!File(relativePath).exists()) {
        File(relativePath).mkdirs()
    }

    val pathReal = relativePath + insertName
    val relativePathFile = File(pathReal)
    if (relativePathFile.exists()) {
        relativePathFile.delete()
    }
    relativePathFile.createNewFile()
    val oldFileNamePath = this.absolutePath
    com.apemans.base.utils.FileCopy.copyFile(oldFileNamePath, relativePathFile.absolutePath)

    //Copy完成后，删除缓存文件
    delete()

    //通知相册刷新
    refreshAlbum(context, pathReal, isVideo, isPhoto)
}

/**
 * 插入文件名字到共享目录Download/$dir 下，名称为insertName
 * @param dir 待插入的文件路径 如果该目录不存在，则会创建
 * @param insertName 待插入的文件显示名
 * Note:只有下载目录和Documents目录支持content插入。如果是DCIM，则不允许
 * W/System.err: java.lang.IllegalArgumentException: Primary directory DCIM not allowed for content://media/external/file;
 * allowed directories are [Download, Documents]
 */
fun insertFileNameToExternalDownload(context: Context, dir: String, insertName: String): Uri? {
    val uriExternal: Uri
    val path: String
    //插入到共享区下载路径下dir
    val values = ContentValues()
    values.put(MediaStore.MediaColumns.DISPLAY_NAME, insertName)
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        path = Environment.DIRECTORY_DOWNLOADS + "/$dir"
        values.put(MediaStore.MediaColumns.RELATIVE_PATH, path)
        uriExternal = MediaStore.Downloads.EXTERNAL_CONTENT_URI
    } else {
        @Suppress("DEPRECATION")
        path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).absolutePath + "/$dir"
        @Suppress("DEPRECATION")
        values.put(MediaStore.MediaColumns.DATA, path)
        @Suppress("DEPRECATION")
        uriExternal = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toUri()
    }
    val uri = context.contentResolver.insert(uriExternal, values)
    Log.i(TAG, "insertFileNameToExternalDownload $dir - > $insertName uri == $uri")
    return uri
}

/**
 * 删除文件
 * @param uri 待删除文件的uri
 */
fun Context.deleteFileByUri(uri: Uri): Int {
    return contentResolver.delete(uri, null, null)
}

/**
 * @param uri 待更新文件的Uri
 * @param newFileName 待更新的名字，不能带有后缀.
 *                    比如，将hello.mp3 更新名字为 newHello.mp3,
 *                    则传入的newFileName是newHello,而不是newHello.mp3
 *                    fileName.substringBeforeLast(".")
 * @return the number of rows updated 返回更新的行数
 * @throws NullPointerException if uri or values are null
 */
fun updateFileName(context: Context, uri: Uri, newFileName: String): Int {
    val values = ContentValues()
    values.put(MediaStore.Files.FileColumns.DISPLAY_NAME, newFileName)
    val updated = context.contentResolver.update(uri, values, null, null)
    Log.i(TAG, "更新名称update newFileName = $newFileName updated = $updated")
    return updated
}

/**
 * 查询共享区相册DCIM @param dir 路径下的 @param cacheName 名字
 * @param dir 要查询的目录
 * @param fileName 要查询的名字
 */

fun queryExternalDCIMDirFile(
    context: Context,
    dir: String,
    fileName: String,
): MediaStoreFileProperty? {
    // 注意，DATA 数据在 Android Q 以前代表了文件的路径，但在 Android Q上该路径无法被访问，因此没有意义。
    var pathQuery = ""
    val queryRelativePath: String

    val uriExternal: Uri
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
        @Suppress("DEPRECATION")
        pathQuery = MediaStore.Files.FileColumns.DATA
        @Suppress("DEPRECATION")
        val pubShareDCIMPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).absolutePath
        @Suppress("DEPRECATION")
        uriExternal = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).toUri()
        //取绝对路径目录
        queryRelativePath = "$pubShareDCIMPath/$dir/"

    } else {
        pathQuery = MediaStore.Files.FileColumns.RELATIVE_PATH
        uriExternal = MediaStore.Files.getContentUri("external")
        //相对路径目录
        queryRelativePath = "${Environment.DIRECTORY_DCIM}/$dir/"
    }
    val projection = arrayOf(
        MediaStore.Files.FileColumns._ID,
        pathQuery,
        MediaStore.Files.FileColumns.DATE_ADDED,
        MediaStore.Files.FileColumns.DISPLAY_NAME,
        MediaStore.Files.FileColumns.SIZE,
        MediaStore.Files.FileColumns.MIME_TYPE,
    )

    val selection = "$pathQuery like ?" + " AND " + MediaStore.Files.FileColumns.DISPLAY_NAME + "= ?"

    val args = arrayOf(
        "$queryRelativePath%",
        fileName
    )

    Log.i(TAG, "开始查询queryRelativePath = $queryRelativePath ")

    context.contentResolver.query(
        uriExternal,
        projection,
        selection,
        args,
        null
    )?.apply {
        var uri: Uri? = null
        var mediaStoreFileProperty: MediaStoreFileProperty? = null
        while (moveToNext()) {
            val id = getLong(getColumnIndexOrThrow(MediaStore.Files.FileColumns._ID))
            uri = ContentUris.withAppendedId(uriExternal, id)
            @SuppressLint("Range")
            val date = getLong(getColumnIndex(MediaStore.Files.FileColumns.DATE_ADDED))

            @SuppressLint("Range")
            val name = getString(getColumnIndex(MediaStore.Files.FileColumns.DISPLAY_NAME))

            @SuppressLint("Range")
            val size = getLong(getColumnIndex(MediaStore.Files.FileColumns.SIZE))

            @SuppressLint("Range")
            val mimeType = getInt(getColumnIndex(MediaStore.Files.FileColumns.MIME_TYPE))
            val isVideo = mimeType == MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO
            Log.i(
                "查询", "查询到fileName = $fileName " +
                        "id = $id " +
                        "uri = $uri " +
                        "date = $date " +
                        "name = $name " +
                        "size = $size " +
                        "mimeType = $mimeType " +
                        "isVideo = $isVideo"
            )
            mediaStoreFileProperty = MediaStoreFileProperty(id, uri, date, name, size, mimeType)
            break
        }
        close()

        return mediaStoreFileProperty
    }
    return null
}


/**
 * 获取共享区DCIM/$albumFolder目录下媒体《视频+照片》文件列表
 */
suspend fun loadExternalDCIMPathMediaFiles(
    context: Context,
    albumFolder: String,
): List<MediaItem> {
    val uriExternal: Uri = MediaStore.Files.getContentUri("external")
    var dir = ""

    // 注意，DATA 数据在 Android Q 以前代表了文件的路径，但在 Android Q上该路径无法被访问，因此没有意义。
    var pathQuery = ""
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
        @Suppress("DEPRECATION")
        pathQuery = MediaStore.MediaColumns.DATA
        @Suppress("DEPRECATION")
        val pubShareDCIMPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).absolutePath
        //取绝对路径
        dir = "$pubShareDCIMPath/$albumFolder"
    } else {
        pathQuery = MediaStore.MediaColumns.RELATIVE_PATH
        //相对路径
        dir = "${Environment.DIRECTORY_DCIM}/$albumFolder"
    }

    val args = if (dir.isEmpty()) null else arrayOf("$dir%")

    val projection = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        arrayOf(
            MediaStore.Video.Media._ID,
            pathQuery,
            MediaStore.Video.Media.DURATION,//视频的时长
            MediaStore.Images.Media.DATE_ADDED,
            MediaStore.Images.Media.DISPLAY_NAME,
            MediaStore.Images.Media.SIZE,
            MediaStore.Files.FileColumns.MEDIA_TYPE
        )
    } else {
        arrayOf(
            MediaStore.Video.Media._ID,
            pathQuery,
            MediaStore.Images.Media.DATE_ADDED,
            MediaStore.Images.Media.DISPLAY_NAME,
            MediaStore.Images.Media.SIZE,
            MediaStore.Files.FileColumns.MEDIA_TYPE
        )
    }
    val sortOrder = MediaStore.Files.FileColumns.DATE_ADDED + " desc"
    //val sortOrder = "${MediaStore.MediaColumns.DATE_ADDED} desc"
    val selection = (pathQuery + " like ?" + " AND (" + MediaStore.Files.FileColumns.MEDIA_TYPE
            + "=" + MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE
            + " OR "
            + MediaStore.Files.FileColumns.MEDIA_TYPE + "="
            + MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO + ")"
            )
    return withContext(Dispatchers.IO) {
        context.contentResolver.query(
            uriExternal,
            projection,
            selection,
            args,
            sortOrder
        )?.let { cursor ->
            var path: String?
            var date: Long /*单位Unix时间-秒*/
            var size: Long
            var name: String
            val items = mutableListOf<MediaItem>()
            var isVideo: Boolean
            var duration: Long /*duration 毫秒*/
            try {
                while (cursor.moveToNext()) {
                    val id =
                        cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.MediaColumns._ID))
                    val uri = ContentUris.withAppendedId(uriExternal, id)
                    @SuppressLint("Range")
                    date = cursor.getLong(cursor.getColumnIndex(MediaStore.Images.Media.DATE_ADDED))
                    @SuppressLint("Range")
                    name = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME))
                    @SuppressLint("Range")
                    size = cursor.getLong(cursor.getColumnIndex(MediaStore.Images.Media.SIZE))
                    @SuppressLint("Range")
                    isVideo = cursor.getInt(cursor.getColumnIndex(MediaStore.Files.FileColumns.MEDIA_TYPE)) == MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        //path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.RELATIVE_PATH)) // == -->Eg:DCIM/APPNAME
                        path = null
                        @SuppressLint("Range")
                        duration = cursor.getLong(cursor.getColumnIndex(MediaStore.Video.Media.DURATION))
                        items += MediaItem(
                            id,
                            path,
                            uri,
                            name,
                            size,
                            date,
                            isVideo,
                            duration
                        )
                    } else {
                        @Suppress("DEPRECATION")
                        @SuppressLint("Range")
                        path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA))
                        path?.let {
                            duration = it.videoDuration
                            //避免子目录出现
                            val subDir = it.substringAfterLast("$dir/")
                            if (!subDir.contains("/") && File(it).exists()) {
                                items += MediaItem(
                                    id,
                                    path,
                                    uri,
                                    name,
                                    size,
                                    date,
                                    isVideo,
                                    duration
                                )
                            }
                        }
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                try {
                    cursor.close()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            Log.i(TAG, "获取到 $dir 下媒体相册列表[items size = ${items.size}] items = $items")
            return@withContext items
        }

        return@withContext emptyList()
    }
}

/**
 * 解析出视频文件时长
 */
private val String.videoDuration: Long
    get() {
        var duration: Long = 0L
        try {
            val mmr = MediaMetadataRetriever()
            mmr.setDataSource(this)
            val extractMetadata =
                mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)
            extractMetadata?.let {
                duration = it.toLong() / 1000
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return 0L
        }
        return duration
    }

/**
 * 插入bitmap到共享区默认下Environment.DIRECTORY_DCIM
 * @param bitmap 数据源
 * @param displayName 显示名字
 * @param relativeExternalPath 存储在共享区的路径
 * @param mimeType 图片类型
 * @param compressFormat 压缩格式
 */
fun insertBitmapToExternal(
    context: Context,
    bitmap: Bitmap,
    displayName: String,
    relativeExternalPath: String,
    mimeType: String = "image/jpeg",
    compressFormat: Bitmap.CompressFormat = Bitmap.CompressFormat.JPEG,
) {
    val values = ContentValues()
    values.put(MediaStore.MediaColumns.DISPLAY_NAME, displayName)
    values.put(MediaStore.MediaColumns.MIME_TYPE, mimeType)
    //将图片的添加时间设置为当前的时间
    values.put(MediaStore.Images.ImageColumns.DATE_ADDED, System.currentTimeMillis())
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        values.put(MediaStore.MediaColumns.RELATIVE_PATH, relativeExternalPath)
        //将图片的拍摄时间设置为当前的时间
        values.put(MediaStore.Images.ImageColumns.DATE_TAKEN, System.currentTimeMillis())
    } else {
        @Suppress("DEPRECATION")
        values.put(MediaStore.MediaColumns.DATA, relativeExternalPath)
    }
    //插入文件
    val contentResolver = context.contentResolver
    contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)?.let { uri ->
        val outputStream = contentResolver.openOutputStream(uri)
        outputStream?.apply {
            bitmap.compress(compressFormat, 100, this)
            close()
            Log.i(TAG, "Add bitmap to album succeeded.")
        }
    }
}

private var mMediaScanner: MediaScannerConnection? = null

/**
 * 刷新相册
 */
fun refreshAlbum(context: Context, fileAbsolutePath: String, isVideo: Boolean, isPhoto: Boolean) {
    mMediaScanner = MediaScannerConnection(context, object : MediaScannerConnection.MediaScannerConnectionClient {
        override fun onScanCompleted(path: String, uri: Uri) {}
        override fun onMediaScannerConnected() {
            mMediaScanner?.apply {
                if (!isConnected) {
                    Log.e(TAG, " refreshAlbum() 无法更新图库，未连接，广播通知更新图库，异常情况下 ")
                    return
                }
                if (isVideo) {
                    scanFile(fileAbsolutePath, "video/*")
                }
                if (isPhoto) {
                    scanFile(fileAbsolutePath, "image/*")
                }
            }
        }
    })
    mMediaScanner?.connect()
}