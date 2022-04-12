package com.apemans.custom.bean;


public class FileInfo {

    private String videoClipPath;//源视频文件路径
    private long fileDuration;//源视频文件时长

    public String getVideoClipPath() {
        return videoClipPath;
    }

    public void setVideoClipPath(String videoClipPath) {
        this.videoClipPath = videoClipPath;
    }
    public long getFileDuration() {
        return fileDuration;
    }

    public void setFileDuration(long fileDuration) {
        this.fileDuration = fileDuration;
    }


}
