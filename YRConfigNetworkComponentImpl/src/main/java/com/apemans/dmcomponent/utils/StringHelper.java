package com.apemans.dmcomponent.utils;

import android.text.TextUtils;

/**
 * @author Dylan Cai
 */
public class StringHelper {
    public static final String CharSet_UTF_8 = "UTF-8";

    public StringHelper() {
    }

    public static long getStringByteSize(String value, String charSet) {
        int byteSize = 0;

        try {
            if (TextUtils.isEmpty(charSet)) {
                charSet = "UTF-8";
            }

            if (value != null) {
                byteSize = value.getBytes(charSet).length;
            }
        } catch (Exception var4) {
            var4.printStackTrace();
        }

        return (long)byteSize;
    }

    public static byte[] getStringByte(String value, String charSet) {
        byte[] result = null;

        try {
            if (TextUtils.isEmpty(charSet)) {
                charSet = "UTF-8";
            }

            if (value != null) {
                result = value.getBytes(charSet);
            }
        } catch (Exception var4) {
            var4.printStackTrace();
        }

        return result;
    }

    public static String safeString(String value) {
        return value == null ? new String() : value;
    }
}
