package com.apemans.usercomponent.baseinfo.time;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;

import com.apemans.base.utils.LanguageUtil;
import com.apemans.usercomponent.baseinfo.utils.CollectionUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class DateTimeUtil {
    public static final String PATTERN_YMD_HMS_1 = "yyyy-MM-dd HH:mm:ss";
    public static final String PATTERN_YMD_HM = "yyyy-MM-dd HH:mm";
    public static final String PATTERN_YMD_HMS_2 = "yyyyMMdd-HHmmss";
    public static final String PATTERN_YMD_HMS_3 = "yyyyMMdd_HHmmss";
    public static final String PATTERN_YMD_HMS_4 = "yyyy-MM-dd HH-mm-ss";
    public static final String PATTERN_YMD_HMS_5 = "yyyy-MM-dd_HH-mm-ss";
    public static final String PATTERN_YMD = "yyyy-MM-dd";
    public static final String PATTERN_YMD_1 = "yyyy/MM/dd";
    public static final String PATTERN_DMY_1 = "dd/MM/yyyy";
    public static final String PATTERN_MD = "MM-dd";
    public static final String PATTERN_MD_1 = "M/d";
    public static final String PATTERN_D = "d";
    public static final String PATTERN_HMS = "HH:mm:ss";
    public static final String PATTERN_YM = "yyyy-MM";
    public static final String PATTERN_HM = "HH:mm";
    public static final int HOUR_COUNT = 24;
    public static final int MINUTE_COUNT = 60;
    public static final int SECOND_COUNT = 60;
    public static final long DAY_SECOND_COUNT = 86400L;

    public DateTimeUtil() {
    }

    public static int getYear(long time) {
        return getDateFieldValue(time, 1);
    }

    public static int getUtcYear(long time) {
        return getUtcDateFieldValue(time, 1);
    }

    public static int getMonth(long time) {
        return getDateFieldValue(time, 2);
    }

    public static int getUtcMonth(long time) {
        return getUtcDateFieldValue(time, 2);
    }

    public static int getDayOfMonth(long time) {
        return getDateFieldValue(time, 5);
    }

    public static int getUtcDayOfMonth(long time) {
        return getUtcDateFieldValue(time, 5);
    }

    public static boolean isToday(long time) {
        return getTimeString(time, "yyyy/MM/dd").equals(getTimeString(getTodayStartTimeStamp(), "yyyy/MM/dd"));
    }

    public static boolean isTodayUtc(long time) {
        return getUtcTimeString(time, "yyyy/MM/dd").equals(getUtcTimeString(getUtcTodayStartTimeStamp(), "yyyy/MM/dd"));
    }

    private static int getDateFieldValue(long time, int field) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        int value = calendar.get(field);
        return value;
    }

    private static int getUtcDateFieldValue(long time, int field) {
        Calendar calendar = getUtcCalendar();
        calendar.setTimeInMillis(time);
        int value = calendar.get(field);
        return value;
    }

    public static int getHour(long time) {
        return getDateFieldValue(time, 11);
    }

    public static int getMinute(long time) {
        return getDateFieldValue(time, 12);
    }

    public static String getWeek() {
        Calendar cal = Calendar.getInstance();
        int i = cal.get(7);
        switch(i) {
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            default:
                return "";
        }
    }

    public static String getTimeStringDefaultPattern(long time) {
        return getTimeString(time, "yyyy-MM-dd HH:mm:ss");
    }

    public static String getTimeString(long time, String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        Date date = new Date(time);
        return sdf.format(date);
    }

    public static String getUtcTimeString(long time, String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        TimeZone utc = TimeZone.getTimeZone("UTC");
        sdf.setTimeZone(utc);
        Date date = new Date(time);
        return sdf.format(date);
    }

    public static long getTimeMillis(int year, int month, int day, int hour, int minute, int second) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day, hour, minute, second);
        return calendar.getTimeInMillis();
    }

    @NonNull
    private static String getString(Context context, int id) {
        return context.getResources().getString(id);
    }

    public static String formatDayTime(int minutes) {
        int hour = minutes / 60;
        int min = minutes % 60;
        Calendar calendar = Calendar.getInstance();
        calendar.set(11, hour);
        calendar.set(12, min);
        return getTimeString(calendar.getTimeInMillis(), "h:mm a");
    }

    public static String formatDayTimeByMinute(int minutes) {
        int hour = minutes / 60;
        int min = minutes % 60;
        Calendar calendar = Calendar.getInstance();
        calendar.set(11, hour);
        calendar.set(12, min);
        return getTimeString(calendar.getTimeInMillis(), "HH:mm");
    }

    public static long getTodayStartTimeStamp() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(11, 0);
        calendar.set(12, 0);
        calendar.set(13, 0);
        return calendar.getTimeInMillis();
    }

    public static long getDayStartTimeStamp(long time) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        calendar.set(11, 0);
        calendar.set(12, 0);
        calendar.set(13, 0);
        return calendar.getTimeInMillis();
    }

    public static long getUtcTodayStartTimeStamp() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getTimeZone("UTC"));
        calendar.set(11, 0);
        calendar.set(12, 0);
        calendar.set(13, 0);
        return calendar.getTimeInMillis();
    }

    public static long getUtcDayStartTimeStamp(long time) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getTimeZone("UTC"));
        calendar.setTimeInMillis(time);
        calendar.set(11, 0);
        calendar.set(12, 0);
        calendar.set(13, 0);
        return calendar.getTimeInMillis();
    }

    public static Calendar getUtcCalendar() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getTimeZone("UTC"));
        return calendar;
    }

    public static long formatDate(int year, int month, int day) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        StringBuilder builder = new StringBuilder();
        builder.append(year);
        builder.append("-");
        if (month < 10) {
            builder.append(0);
        }

        builder.append(month);
        builder.append("-");
        if (day < 10) {
            builder.append(0);
        }

        builder.append(day);
        builder.append(" 00:00:00");

        try {
            return dateFormat.parse(builder.toString()).getTime();
        } catch (ParseException var6) {
            var6.printStackTrace();
            return 0L;
        }
    }

    public static String formatDate(Context context, long time, String pattern) {
        Locale locale = LanguageUtil.getLocal(context) != null ? LanguageUtil.getLocal(context) : Locale.getDefault();
        SimpleDateFormat formatter = new SimpleDateFormat(pattern, locale);
        return formatter.format(new Date(time));
    }

    public static String getMonthDisplayName(Context context, long time) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        Locale locale = LanguageUtil.getLocal(context) != null ? LanguageUtil.getLocal(context) : Locale.getDefault();
        return calendar.getDisplayName(2, 2, locale);
    }

    public static String getUtcMonthDisplayName(Context context, long time) {
        Calendar calendar = getUtcCalendar();
        calendar.setTimeInMillis(time);
        Locale locale = LanguageUtil.getLocal(context) != null ? LanguageUtil.getLocal(context) : Locale.getDefault();
        return calendar.getDisplayName(2, 2, locale);
    }

    public static String getDisplayName(Context context, long time, int field, int style) {
        Calendar calendar = getUtcCalendar();
        calendar.setTimeInMillis(time);
        Locale locale = LanguageUtil.getLocal(context) != null ? LanguageUtil.getLocal(context) : Locale.getDefault();
        return calendar.getDisplayName(field, style, locale);
    }

    public static String getUtcDisplayName(Context context, long time, int field, int style) {
        Calendar calendar = getUtcCalendar();
        calendar.setTimeInMillis(time);
        Locale locale = LanguageUtil.getLocal(context) != null ? LanguageUtil.getLocal(context) : Locale.getDefault();
        return calendar.getDisplayName(field, style, locale);
    }

    public static long parseDate(String dateStr, String pattern) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);

        try {
            return !TextUtils.isEmpty(dateStr) ? dateFormat.parse(dateStr).getTime() : 0L;
        } catch (ParseException var4) {
            var4.printStackTrace();
            return 0L;
        }
    }

    public static String getTimeStrByTimeLen(int timeLen) {
        int h = timeLen / 3600;
        int m = timeLen % 3600 / 60;
        int s = timeLen % 60;
        StringBuilder timeSb = new StringBuilder();
        timeSb.append(String.format("%02d", h));
        timeSb.append(":");
        timeSb.append(String.format("%02d", m));
        timeSb.append(":");
        timeSb.append(String.format("%02d", s));
        return timeSb.toString();
    }

    public static String getOnlyTimeId() {
        long w = 100000000L;
        long r = 0L;
        Class var4 = DateTimeUtil.class;
        synchronized(DateTimeUtil.class) {
            r = (long)((Math.random() + 1.0D) * 1.0E8D);
        }

        return System.currentTimeMillis() + "_" + String.valueOf(r).substring(1);
    }

    public static String getTimeHms(long time) {
        if (time < 0L) {
            return "00:00:00";
        } else {
            int h = (int)(time / 3600L);
            int m = (int)(time % 3600L / 60L);
            int s = (int)(time % 60L);
            StringBuilder builder = new StringBuilder();
            builder.append(h >= 10 ? h : "0" + h);
            builder.append(":");
            builder.append(m >= 10 ? m : "0" + m);
            builder.append(":");
            builder.append(s >= 10 ? s : "0" + s);
            return builder.toString();
        }
    }

    public static String localToUtc(long localTime, String utcTimePatten) {
        SimpleDateFormat formatStr = new SimpleDateFormat(utcTimePatten);
        formatStr.setTimeZone(TimeZone.getTimeZone("UTC"));
        String strDate = formatStr.format(localTime);
        return strDate;
    }

    public static long parseLocalTimeByDateStr(String dateStr) {
        long localTime = 0L;

        try {
            SimpleDateFormat formatStr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = formatStr.parse(dateStr);
            localTime = date.getTime();
            Log.d("","-->> DateTimeUtil parseLocalTimeByDateStr dateStr=" + dateStr + " localTime=" + localTime);
        } catch (Exception var5) {
            var5.printStackTrace();
        }

        return localTime;
    }

    public static int convertWeekDay(int i) {
        int weekDay = -1;
        switch(i) {
            case 0:
                weekDay = 1;
                break;
            case 1:
                weekDay = 2;
                break;
            case 2:
                weekDay = 3;
                break;
            case 3:
                weekDay = 4;
                break;
            case 4:
                weekDay = 5;
                break;
            case 5:
                weekDay = 6;
                break;
            case 6:
                weekDay = 7;
        }

        return weekDay;
    }

    public static List<Integer> convertWeekDayKeys(List<Integer> weekDays) {
        List<Integer> weekDayKeys = new ArrayList();
        if (CollectionUtil.isEmpty(weekDays)) {
            return weekDayKeys;
        } else {
            Iterator var2 = CollectionUtil.safeFor(weekDays).iterator();

            while(var2.hasNext()) {
                Integer weekDay = (Integer)var2.next();
                weekDayKeys.add(convertWeekDayKey(weekDay));
            }

            return weekDayKeys;
        }
    }

    public static int convertWeekDayKey(int weekDay) {
        int weekDayKey = -1;
        switch(weekDay) {
            case 1:
                weekDayKey = 0;
                break;
            case 2:
                weekDayKey = 1;
                break;
            case 3:
                weekDayKey = 2;
                break;
            case 4:
                weekDayKey = 3;
                break;
            case 5:
                weekDayKey = 4;
                break;
            case 6:
                weekDayKey = 5;
                break;
            case 7:
                weekDayKey = 6;
        }

        return weekDayKey;
    }

    public static int getWeekDayByTime(long time) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        return calendar.get(7);
    }
}
