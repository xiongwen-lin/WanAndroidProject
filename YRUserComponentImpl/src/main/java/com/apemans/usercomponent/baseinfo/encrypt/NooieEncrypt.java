package com.apemans.usercomponent.baseinfo.encrypt;

import com.apemans.business.apisdk.utils.encrypt.SHAUtil;

public class NooieEncrypt {
    public NooieEncrypt() {
    }

    public static String signWithoutToken(String secretKey, String appId, long timestamp) {
        String plain = appId + timestamp;
        String hash = SHAUtil.HMAC_SHA256(secretKey, plain);
        return SHAUtil.encodeBASE64(hash);
    }

    public static String signWithToken(String secretKey, String appId, long timestamp, String uid, String token) {
        String plain = appId + timestamp + uid + token;
        String hash = SHAUtil.HMAC_SHA256(secretKey, plain);
        return SHAUtil.encodeBASE64(hash);
    }

    public static String signWithMd5(String secretKey, String appId, long timestamp, String md5) {
        String plain = appId + timestamp + md5;
        String hash = SHAUtil.HMAC_SHA256(secretKey, plain);
        return hash;
    }
}
