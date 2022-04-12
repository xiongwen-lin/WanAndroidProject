package com.apemans.custom.album

/**
 * @Author:dongbeihu
 * @Description:
 * @Date: 2021/11/17-11:27
 */
object  MediaConstant {
    /***
     * 图片选择器
     */
    const val  ALBUM_SELECT_CODE =10091

    //音乐和视频类型
    const val ALL_MEDIA = 0
    const val VIDEO = 1
    const val IMAGE = 2
    val MEDIATYPECOUNT = intArrayOf(ALL_MEDIA, VIDEO, IMAGE)

    const val MEDIA_PATH = "mediaPath"
    const val MEDIA_TYPE = "mediaType"
    const val SUCCESS = "SUCCESS"
    const val ERROR = "ERROR"

    const val REQUEST_CODE_CUSTOM_NAME = 0x05
    const val INTENT_KEY_NICK_NAME = "INTENT_KEY_NICK_NAME"
}