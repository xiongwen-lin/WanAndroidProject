package com.apemans.usercomponent.baseinfo.kv;

import android.util.Log;

import com.tencent.mmkv.MMKV;

import java.util.Set;

public class BaseKVData {
    private MMKV mMMKV;

    public BaseKVData(String kvId) {
        this(kvId, true);
    }

    public BaseKVData(String kvId, boolean isMultiProcess) {
        this.mMMKV = null;
        this.createMMKV(kvId, isMultiProcess);
    }

    public void createMMKV(String kvId, boolean isMultiProcess) {
        try {
            if (isMultiProcess) {
                this.mMMKV = MMKV.mmkvWithID(kvId, 2);
            } else {
                this.mMMKV = MMKV.mmkvWithID(kvId);
            }
        } catch (Exception var4) {
            Log.d("",""+var4);
        }

    }

    public MMKV getMMKV() {
        return this.mMMKV;
    }

    public boolean putBool(String key, boolean value) {
        return this.getMMKV() == null ? false : this.getMMKV().encode(key, value);
    }

    public boolean getBool(String key, boolean defaultValue) {
        return this.getMMKV() == null ? defaultValue : this.getMMKV().decodeBool(key, defaultValue);
    }

    public boolean putInt(String key, int value) {
        return this.getMMKV() == null ? false : this.getMMKV().encode(key, value);
    }

    public int getInt(String key, int defaultValue) {
        return this.getMMKV() == null ? defaultValue : this.getMMKV().decodeInt(key, defaultValue);
    }

    public boolean putLong(String key, long value) {
        return this.getMMKV() == null ? false : this.getMMKV().encode(key, value);
    }

    public long getLong(String key, long defaultValue) {
        return this.getMMKV() == null ? defaultValue : this.getMMKV().decodeLong(key, defaultValue);
    }

    public boolean putFloat(String key, float value) {
        return this.getMMKV() == null ? false : this.getMMKV().encode(key, value);
    }

    public float getFloat(String key, float defaultValue) {
        return this.getMMKV() == null ? defaultValue : this.getMMKV().decodeFloat(key, defaultValue);
    }

    public boolean putDouble(String key, double value) {
        return this.getMMKV() == null ? false : this.getMMKV().encode(key, value);
    }

    public double getDouble(String key, double defaultValue) {
        return this.getMMKV() == null ? defaultValue : this.getMMKV().decodeDouble(key, defaultValue);
    }

    public boolean putString(String key, String value) {
        return this.getMMKV() == null ? false : this.getMMKV().encode(key, value);
    }

    public String getString(String key, String defaultValue) {
        return this.getMMKV() == null ? defaultValue : this.getMMKV().decodeString(key, defaultValue);
    }

    public boolean putBytes(String key, byte[] value) {
        return this.getMMKV() == null ? false : this.getMMKV().encode(key, value);
    }

    public byte[] getBytes(String key, byte[] defaultValue) {
        return this.getMMKV() == null ? defaultValue : this.getMMKV().decodeBytes(key, defaultValue);
    }

    public boolean putString(String key, Set<String> value) {
        return this.getMMKV() == null ? false : this.getMMKV().encode(key, value);
    }

    public Set<String> getString(String key, Set<String> defaultValue) {
        return this.getMMKV() == null ? defaultValue : this.getMMKV().decodeStringSet(key, defaultValue);
    }

    public String[] getAllKeys() {
        return this.getMMKV() == null ? null : this.getMMKV().allKeys();
    }
}
