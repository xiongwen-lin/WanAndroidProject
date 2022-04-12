package com.apemans.usercomponent.baseinfo.data;

public class DataHelper {
    public DataHelper() {
    }

    public static int toInt(String value) {
        int result = 0;

        try {
            result = Integer.valueOf(value);
        } catch (Exception var3) {
        }

        return result;
    }

    public static int toInt(String value, int radix) {
        int result = 0;

        try {
            result = Integer.valueOf(value, radix);
        } catch (Exception var4) {
        }

        return result;
    }

    public static long toLong(String value) {
        long result = 0L;

        try {
            result = Long.valueOf(value);
        } catch (Exception var4) {
        }

        return result;
    }

    public static float toFloat(String value) {
        float result = 0.0F;

        try {
            result = Float.valueOf(value);
        } catch (Exception var3) {
        }

        return result;
    }

    public static double toDouble(String value) {
        double result = 0.0D;

        try {
            result = Double.valueOf(value);
        } catch (Exception var4) {
        }

        return result;
    }
}
