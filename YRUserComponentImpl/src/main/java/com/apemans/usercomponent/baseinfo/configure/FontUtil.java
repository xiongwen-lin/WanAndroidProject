package com.apemans.usercomponent.baseinfo.configure;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.text.TextUtils;

import java.lang.reflect.Field;

public class FontUtil {
    public FontUtil() {
    }

    public static Typeface loadTypeface(Context ctx, String path) {
        Typeface typeface = Typeface.createFromAsset(ctx.getAssets(), path);
        return typeface;
    }

    public static void setDefaultTypeface(Context ctx, String fontPath) {
        try {
            Field field = Typeface.class.getDeclaredField("MONOSPACE");
            field.setAccessible(true);
            field.set((Object)null, loadTypeface(ctx, fontPath));
        } catch (NoSuchFieldException var3) {
            var3.printStackTrace();
        } catch (IllegalAccessException var4) {
            var4.printStackTrace();
        }

    }

    public static String getLang(Context context) {
        if (context == null) {
            return "";
        } else {
            String lang;
            if (Build.VERSION.SDK_INT >= 24) {
                lang = context.getResources().getConfiguration().getLocales().get(0).getLanguage();
            } else {
                lang = context.getResources().getConfiguration().locale.getLanguage();
            }

            if (TextUtils.equals(lang, "zh")) {
                String country;
                if (Build.VERSION.SDK_INT >= 24) {
                    country = context.getResources().getConfiguration().getLocales().get(0).getCountry();
                } else {
                    country = context.getResources().getConfiguration().locale.getCountry();
                }

                if (TextUtils.equals(country, "TW")) {
                    return "tw";
                }

                if (TextUtils.equals(country, "HK")) {
                    return "tw";
                }

                if (TextUtils.equals(country, "MO")) {
                    return "tw";
                }
            }

            return lang;
        }
    }

    public static boolean isZh(Context context) {
        return getLang(context).endsWith("zh");
    }

    public static boolean isFr(Context context) {
        return getLang(context).endsWith("fr");
    }

    public static boolean isIt(Context context) {
        return getLang(context).endsWith("it");
    }

    public static boolean isDe(Context context) {
        return getLang(context).endsWith("de");
    }

    public static boolean isEs(Context context) {
        return getLang(context).endsWith("es");
    }

    public static boolean isJa(Context context) {
        return getLang(context).endsWith("ja");
    }
}
