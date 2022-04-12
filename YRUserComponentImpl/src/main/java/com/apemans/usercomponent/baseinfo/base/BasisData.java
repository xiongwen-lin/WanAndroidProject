package com.apemans.usercomponent.baseinfo.base;

import android.util.Log;

import com.apemans.usercomponent.baseinfo.kv.BaseKVData;

public class BasisData extends BaseKVData {
    public static final String BASIS_DATA_KV_ID = "BASIS_DATA_KV_ID";
    public static final String KEY_ACCOUNT = "account";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_UID = "uid";
    public static final String KEY_TOKEN = "token";
    public static final String KEY_REFRESH_TOKEN = "refresh_token";
    public static final String KEY_EXPIRE_TIME = "expire_time";
    public static final String KEY_WEB_URL = "web_url";
    public static final String KEY_P2P_URL = "p2p_url";
    public static final String KEY_S3_URL = "s3_url";
    public static final String KEY_SS_URL = "ss_url";
    public static final String KEY_REGION = "region";
    public static final String KEY_GAP_TIME = "gap_time";
    public static final String KEY_PHONE_ID = "phone_id";
    public static final String KEY_PUSH_TOKEN = "push_token";
    public static final String KEY_DEBUG_MODE = "debug_mode";
    public static final String KEY_LOGIN_TYPE = "login_type";
    public static final String KEY_THIRD_PARTY_OPEN_ID = "third_party_open_id";
    public static final String KEY_THIRD_PARTY_USER_TYPE = "third_party_user_type";
    public static final String KEY_GAP_TIME_VALID = "gap_time_valid";

    private BasisData() {
        super("BASIS_DATA_KV_ID");
    }

    public static BasisData getInstance() {
        return BasisData.BasisDataHolder.INSTANCE;
    }

    public int getGapTime() {
        return this.getInt("gap_time", 0);
    }

    public void setGapTime(int gapTime) {
        this.putInt("gap_time", gapTime);
    }

    public String getPhoneId() {
        return this.getString("phone_id", "");
    }

    public void setPhoneId(String phoneId) {
        this.putString("phone_id", phoneId);
    }

    public int getDebugMode() {
        return this.getInt("debug_mode", 0);
    }

    public void setDebugMode(int debugMode) {
        this.putInt("debug_mode", debugMode);
    }

    public boolean getGapTimeValid() {
        return this.getBool("gap_time", false);
    }

    public void setGapTimeValid(boolean valid) {
        this.putBool("gap_time", valid);
    }

    public void log() {
        StringBuilder logSb = new StringBuilder();
        logSb.append("gapTime=");
        logSb.append(this.getGapTime());
        logSb.append(" ");
        logSb.append("phoneId=");
        logSb.append(this.getPhoneId());
        logSb.append(" ");
        logSb.append("gapTimeValid=");
        logSb.append(this.getGapTimeValid());
        logSb.append(" ");
        Log.d("","-->> BasisData log " + logSb.toString());
    }

    private static final class BasisDataHolder {
        private static final BasisData INSTANCE = new BasisData();

        private BasisDataHolder() {
        }
    }
}
