package com.apemans.usercomponent.baseinfo.graphics;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.database.ContentObserver;
import android.net.Uri;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import java.lang.reflect.Field;
import java.math.BigDecimal;

public class DisplayUtil {
    public static final int HEADER_BAR_HEIGHT_DP = 60;
    public static final int SCREEN_CONTENT_MIN_HEIGHT_PX = 1573;
    public static int SCREEN_WIDTH_PX;
    public static int SCREEN_HIGHT_PX;
    public static int SCREEN_REAL_WIDTH_PX;
    public static int SCREEN_REAL_HIGHT_PX;
    public static float DENSITY;
    public static int DENSITY_DPI;
    public static float SCREEN_WIDTH_DIP;
    public static float SCREEN_HIGHT_DIP;

    public DisplayUtil() {
    }

    public static int dpToPx(Context context, float dpvalue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int)(dpvalue * scale + 0.5F);
    }

    public static int pxToDp(Context context, float pxvalue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int)(pxvalue / scale + 0.5F);
    }

    public static void enableViewAndChildren(ViewGroup view, boolean enable) {
        if (view != null) {
            for(int i = 0; i < view.getChildCount(); ++i) {
                view.getChildAt(i).setEnabled(enable);
                if (view.getChildAt(i) instanceof ViewGroup) {
                    enableViewAndChildren((ViewGroup)view.getChildAt(i), enable);
                }
            }

        }
    }

    public static void setViewHeight(View view, int height) {
        if (view != null) {
            ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
            layoutParams.height = height;
            view.setLayoutParams(layoutParams);
        }
    }

    public static void initDisplayOpinion(Context context) {
        if (context != null) {
            try {
                DisplayMetrics dm = context.getResources().getDisplayMetrics();
                DENSITY = dm.density;
                DENSITY_DPI = dm.densityDpi;
                SCREEN_WIDTH_PX = dm.widthPixels;
                SCREEN_HIGHT_PX = dm.heightPixels;
                SCREEN_WIDTH_DIP = (float)pxToDp(context, (float)dm.widthPixels);
                SCREEN_HIGHT_DIP = (float)pxToDp(context, (float)dm.heightPixels);
                DisplayMetrics realDm = getDisplayRealMetrics(context);
                SCREEN_REAL_WIDTH_PX = realDm.widthPixels;
                SCREEN_REAL_HIGHT_PX = realDm.heightPixels;
            } catch (Exception var3) {
                Log.d("",""+var3);
            }

        }
    }

    public static int getScreenMode(Context context) {
        int mode = -1;

        try {
            mode = Settings.System.getInt(context.getContentResolver(), "screen_brightness_mode");
        } catch (Settings.SettingNotFoundException var3) {
            var3.printStackTrace();
        }

        return mode;
    }

    public static int getScreenBrightness(Context context) {
        int screenBrightness = -1;

        try {
            screenBrightness = Settings.System.getInt(context.getContentResolver(), "screen_brightness");
        } catch (Settings.SettingNotFoundException var3) {
            var3.printStackTrace();
        }

        return screenBrightness;
    }

    public static void setScreenMode(Context context, int mode) {
        try {
            Settings.System.putInt(context.getContentResolver(), "screen_brightness_mode", mode);
            Uri uri = Settings.System.getUriFor("screen_brightness");
            context.getContentResolver().notifyChange(uri, (ContentObserver)null);
        } catch (Exception var3) {
            var3.printStackTrace();
        }

    }

    public static void setScreenBrightness(Activity activity, int brightness) {
        Window window = activity.getWindow();
        android.view.WindowManager.LayoutParams lp = window.getAttributes();
        if (brightness == -1) {
            lp.screenBrightness = -1.0F;
        } else {
            lp.screenBrightness = (float)(brightness <= 0 ? 1 : brightness) / 255.0F;
        }

        window.setAttributes(lp);
    }

    public static double getScreenSize(Context context) {
        DisplayMetrics dm = getDisplayRealMetrics(context);
        if (dm == null) {
            return 0.0D;
        } else {
            double screenInches = 0.0D;

            try {
                double x = Math.pow((double)((float)dm.widthPixels / dm.xdpi), 2.0D);
                double y = Math.pow((double)((float)dm.heightPixels / dm.ydpi), 2.0D);
                BigDecimal decimal = new BigDecimal(Math.sqrt(x + y));
                decimal = decimal.setScale(1, 0);
                screenInches = decimal.doubleValue();
            } catch (Exception var9) {
                Log.d("", ""+var9);
            }

            return screenInches;
        }
    }

    public static int getStatusBarHeight(Context context) {
        int result = getStatusBarHeight1(context);
        if (result <= 0) {
            result = getStatusBarHeight2(context);
        }

        if (result <= 0) {
            result = getStatusBarHeight3(context);
        }

        return result;
    }

    public static int getStatusBarHeight1(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }

        return result;
    }

    public static int getStatusBarHeight2(Context context) {
        Class c = null;

        try {
            c = Class.forName("com.android.internal.R$dimen");
            Object obj = c.newInstance();
            Field field = c.getField("status_bar_height");
            int x = Integer.parseInt(field.get(obj).toString());
            int statusBarHeight = context.getResources().getDimensionPixelSize(x);
            return statusBarHeight;
        } catch (Exception var6) {
            var6.printStackTrace();
            return 0;
        }
    }

    public static int getStatusBarHeight3(Context context) {
        return (int)Math.ceil((double)(20.0F * context.getResources().getDisplayMetrics().density));
    }

    public static DisplayMetrics getDisplayMetrics(Context context) {
        if (context == null) {
            return null;
        } else {
            @SuppressLint("WrongConstant") WindowManager windowManager = (WindowManager)context.getSystemService("window");
            DisplayMetrics displayMetrics = new DisplayMetrics();
            windowManager.getDefaultDisplay().getMetrics(displayMetrics);
            return displayMetrics;
        }
    }

    public static DisplayMetrics getDisplayRealMetrics(Context context) {
        if (context == null) {
            return null;
        } else {
            @SuppressLint("WrongConstant") WindowManager windowManager = (WindowManager)context.getSystemService("window");
            DisplayMetrics displayMetrics = new DisplayMetrics();
            windowManager.getDefaultDisplay().getRealMetrics(displayMetrics);
            return displayMetrics;
        }
    }
}
