/*
 * * * Copyright (c) 2021 海龙 Inc. All rights reserved.
 */

package com.apemans.base.utils;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.text.TextUtils;
import android.util.DisplayMetrics;

import java.util.Locale;

public class LanguageUtil {

    public static String getStringByLocale(Context context, int stringId, String language, String country) {
        String packageName = context.getPackageName();
        Resources resources = getApplicationResource(context.getApplicationContext().getPackageManager(), packageName, new Locale(language, country));
        if (resources == null) {
            return "";
        } else {
            try {
                return resources.getString(stringId);
            } catch (Exception var6) {
                return "";
            }
        }
    }

    public static String getAllLanguageStr(Context context, int stringId) {
        StringBuilder builder = new StringBuilder();
        builder.append(getStringByLocale(context, stringId, "", "")).append(",");
        builder.append(getStringByLocale(context, stringId, "de", "")).append(",");
        builder.append(getStringByLocale(context, stringId, "en", "")).append(",");
        builder.append(getStringByLocale(context, stringId, "es", "")).append(",");
        builder.append(getStringByLocale(context, stringId, "fr", "")).append(",");
        builder.append(getStringByLocale(context, stringId, "it", "")).append(",");
        builder.append(getStringByLocale(context, stringId, "pl", "")).append(",");
        builder.append(getStringByLocale(context, stringId, "ru", "")).append(",");
        builder.append(getStringByLocale(context, stringId, "zh", ""));
        return new String(builder);
    }

    public static String getStringToEnglish(Context context, int stringId) {
        return getStringByLocale(context, stringId, "en", "US");
    }

    private static Resources getApplicationResource(PackageManager pm, String pkgName, Locale l) {
        Resources resourceForApplication = null;

        try {
            resourceForApplication = pm.getResourcesForApplication(pkgName);
            updateResource(resourceForApplication, l);
        } catch (PackageManager.NameNotFoundException var5) {
            var5.printStackTrace();
        }

        return resourceForApplication;
    }

    private static void updateResource(Resources resource, Locale l) {
        Configuration config = resource.getConfiguration();
        config.locale = l;
        resource.updateConfiguration(config, (DisplayMetrics) null);
    }

    public static Locale getLocal(Context context) {
        Locale locale = null;
        if (Build.VERSION.SDK_INT > 24) {
            locale = context.getResources().getConfiguration().getLocales().get(0);
        } else {
            locale = context.getResources().getConfiguration().locale;
        }

        return locale;
    }

    public static String getLanguageKeyByLocale(Context context) {
        String languageKey = "en";

        try {
            String language = !TextUtils.isEmpty(getLocal(context).getLanguage()) ? getLocal(context).getLanguage() : "en";

            if (language.equalsIgnoreCase("zh")) {
                languageKey = "zh";
            } else if (language.equalsIgnoreCase("de")) {
                languageKey = "de";
            } else if (language.equalsIgnoreCase("en")) {
                languageKey = "en";
            } else if (language.equalsIgnoreCase("es")) {
                languageKey = "es";
            } else if (language.equalsIgnoreCase("fr")) {
                languageKey = "fr";
            } else if (language.equalsIgnoreCase("it")) {
                languageKey = "it";
            } else if (language.equalsIgnoreCase("ja")) {
                languageKey = "ja";
            } else {
                languageKey = "en";
            }
        } catch (Exception var3) {
            var3.printStackTrace();
        }

        return languageKey;
    }
}
