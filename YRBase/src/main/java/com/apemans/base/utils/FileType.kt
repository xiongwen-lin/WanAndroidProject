package com.apemans.base.utils

import java.util.*

/***********************************************************
 * @Author : caro
 * @Date   : 2021/7/13
 * @Func:
 * 文件类型判断
 *
 * @Description:
 *
 *
 ***********************************************************/

val String.isPhoto: Boolean
    get() = lowercase(Locale.getDefault()).run {
        endsWith(".png")
                || endsWith(".PNG")
                || endsWith(".jpeg")
                || endsWith(".JPEG")
                || endsWith(".jpg")
                || endsWith(".JPG")
                || endsWith(".gif")
                || endsWith(".GIF")
                || endsWith(".bmp")
                || endsWith(".BMP")
                || endsWith(".webp")
                || endsWith(".WEBP")
                || endsWith(".SVG")
                || endsWith(".svg")
                || endsWith(".ico")
    }

val String.isVideo: Boolean
    get() = lowercase(Locale.getDefault()).run {
        endsWith(".MP4")
                || endsWith(".mp4")
                || endsWith(".mov")
                || endsWith(".MOV")
                || endsWith(".FLV")
                || endsWith(".flv")
                || endsWith(".WMV")
                || endsWith(".wmv")
                || endsWith(".AVI")
                || endsWith(".avi")
                || endsWith(".MKV")
                || endsWith(".mkv")
                || endsWith(".mpeg")
                || endsWith(".MPEG")
                || endsWith(".m4v")
                || endsWith(".M4V")
                || endsWith(".rmvb")
                || endsWith(".RMVB")
    }