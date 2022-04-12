package com.apemans.smartipcimpl.ui.ipcdevices.player.old.bean;

import com.apemans.smartipcapi.webapi.AwsFileListResult;
import com.nooie.sdk.device.bean.RecordFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * PlaybackCloudData
 *
 * @author Administrator
 * @date 2020/8/11
 */
public class PlaybackCloudData {

    List<CloudRecordInfo> result = new ArrayList<>();
    List<RecordFragment> recordFragments = new ArrayList<>();
    String fileType = "";
    String picType = "";
    String filePrefix = "";
    int expireDate = 7;


    public List<CloudRecordInfo> getResult() {
        return result;
    }

    public void setResult(List<CloudRecordInfo> result) {
        this.result = result;
    }

    public List<RecordFragment> getRecordFragments() {
        return recordFragments;
    }

    public void setRecordFragments(List<RecordFragment> recordFragments) {
        this.recordFragments = recordFragments;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getPicType() {
        return picType;
    }

    public void setPicType(String picType) {
        this.picType = picType;
    }

    public String getFilePrefix() {
        return filePrefix;
    }

    public void setFilePrefix(String filePrefix) {
        this.filePrefix = filePrefix;
    }

    public int getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(int expireDate) {
        this.expireDate = expireDate;
    }


}