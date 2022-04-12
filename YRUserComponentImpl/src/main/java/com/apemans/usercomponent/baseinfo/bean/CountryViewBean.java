package com.apemans.usercomponent.baseinfo.bean;

import android.content.Context;

import com.apemans.usercomponent.baseinfo.configure.FontUtil;
import com.apemans.usercomponent.baseinfo.contact.ContactItemInterface;

public class CountryViewBean implements ContactItemInterface {
    private String countryName;
    private String key;
    private String number;
    private String pinyin;
    private boolean isChinese;

    public CountryViewBean(String key, String countryName, String number, String pinyin, Context context) {
        this.countryName = countryName;
        this.key = key;
        this.number = number;
        this.pinyin = pinyin;
        this.isChinese = FontUtil.isZh(context);
    }

    public String getItemForIndex() {
        return this.isChinese ? this.pinyin : this.countryName;
    }

    public String getCountryName() {
        return this.countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getKey() {
        return this.key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getNumber() {
        return this.number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getPinyin() {
        return this.pinyin;
    }

    public void setPinyin(String pinyin) {
        this.pinyin = pinyin;
    }

    public boolean isChinese() {
        return this.isChinese;
    }

    public void setChinese(boolean isChinese) {
        this.isChinese = isChinese;
    }
}
