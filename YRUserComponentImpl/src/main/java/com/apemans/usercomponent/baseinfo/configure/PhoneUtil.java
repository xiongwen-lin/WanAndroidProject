package com.apemans.usercomponent.baseinfo.configure;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Point;
import android.os.Build;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

import com.apemans.usercomponent.baseinfo.encrypt.MD5Util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

public class PhoneUtil {
    private static final List<String> sManufacturer = new ArrayList<String>() {
        {
            this.add("HUAWEI");
            this.add("OPPO");
            this.add("vivo");
        }
    };

    public PhoneUtil() {
    }

    public static String getPhoneMark(Context context) {
        return getUUID();
    }

    public static String getUUID() {
        String uuid = UUID.randomUUID().toString();
        return uuid.replaceAll("-", "");
    }

    public static String getUUIDWithLine() {
        return UUID.randomUUID().toString();
    }

    public static String getAndroidId(Context context) {
        String androidId = "";

        try {
            androidId = Settings.Secure.getString(context.getContentResolver(), "android_id");
        } catch (Exception var3) {
            Log.d("", ""+var3);
        }

        return androidId;
    }

    public static String getSerial() {
        return Build.SERIAL;
    }

    public static String createPhoneId(Context context) {
        StringBuilder infoSb = new StringBuilder();
        infoSb.append(Build.BRAND);
        infoSb.append(Build.BOARD);
        infoSb.append(Build.HARDWARE);
        infoSb.append(Build.MODEL);
        infoSb.append(getAndroidId(context));
        return MD5Util.MD5Hash(infoSb.toString());
    }

    public static void logPhoneId(Context context) {
        StringBuilder logSb = new StringBuilder();
        logSb.append("BRAND:");
        logSb.append(Build.BRAND);
        logSb.append(" ");
        logSb.append("BOARD:");
        logSb.append(Build.BOARD);
        logSb.append(" ");
        logSb.append("HARDWARE:");
        logSb.append(Build.HARDWARE);
        logSb.append(" ");
        logSb.append("MODEL:");
        logSb.append(Build.MODEL);
        logSb.append(" ");
        logSb.append("AndroidId:");
        logSb.append(getAndroidId(context));
        logSb.append(" ");
        logSb.append("Serial:");
        logSb.append(getSerial());
        logSb.append(" ");
        Log.d("","-->> PhoneUtil log phoneInfo=" + logSb.toString() + " phoneId=" + createPhoneId(context));
    }

    public static String getOS() {
        return Build.VERSION.RELEASE == null ? "UNKNOWN" : Build.VERSION.RELEASE;
    }

    public static String getManufacturer() {
        String manufacturer = Build.MANUFACTURER == null ? "UNKNOWN" : Build.MANUFACTURER.trim();

        try {
            if (!TextUtils.isEmpty(manufacturer)) {
                Iterator var1 = sManufacturer.iterator();

                while(var1.hasNext()) {
                    String item = (String)var1.next();
                    if (item.equalsIgnoreCase(manufacturer)) {
                        return item;
                    }
                }
            }
        } catch (Exception var3) {
            Log.d("", ""+var3);
        }

        return manufacturer;
    }

    public static String getModel() {
        return TextUtils.isEmpty(Build.MODEL) ? "UNKNOWN" : Build.MODEL.trim();
    }

    public static String getPhoneName(Context context) {
        String deviceName = null;

        try {
            deviceName = context != null ? Settings.Global.getString(context.getContentResolver(), "device_name") : null;
            if (TextUtils.isEmpty(deviceName)) {
                deviceName = context != null ? Settings.Secure.getString(context.getContentResolver(), "bluetooth_name") : null;
            }
        } catch (Exception var3) {
            var3.printStackTrace();
        }

        if (TextUtils.isEmpty(deviceName)) {
            deviceName = Build.MANUFACTURER + " " + Build.MODEL;
        }

        return deviceName;
    }

    public static int[] getDeviceSize(Context context) {
        int[] size = new int[2];

        try {
            @SuppressLint("WrongConstant") WindowManager windowManager = (WindowManager)context.getSystemService("window");
            Display display = windowManager.getDefaultDisplay();
            int rotation = display.getRotation();
            Point point = new Point();
            int screenWidth;
            int screenHeight;
            if (Build.VERSION.SDK_INT >= 17) {
                display.getRealSize(point);
                screenWidth = point.x;
                screenHeight = point.y;
            } else if (Build.VERSION.SDK_INT >= 13) {
                display.getSize(point);
                screenWidth = point.x;
                screenHeight = point.y;
            } else {
                screenWidth = display.getWidth();
                screenHeight = display.getHeight();
            }

            size[0] = getNaturalWidth(rotation, screenWidth, screenHeight);
            size[1] = getNaturalHeight(rotation, screenWidth, screenHeight);
        } catch (Exception var8) {
            if (context.getResources() != null) {
                DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
                size[0] = displayMetrics.widthPixels;
                size[1] = displayMetrics.heightPixels;
            }
        }

        return size;
    }

    private static int getNaturalWidth(int rotation, int width, int height) {
        return rotation != 0 && rotation != 2 ? height : width;
    }

    private static int getNaturalHeight(int rotation, int width, int height) {
        return rotation != 0 && rotation != 2 ? width : height;
    }
}
