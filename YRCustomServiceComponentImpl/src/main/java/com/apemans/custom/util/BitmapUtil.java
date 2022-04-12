package com.apemans.custom.util;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.BitmapDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class BitmapUtil {
    public BitmapUtil() {
    }



    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.text".equals(uri.getAuthority());
    }

    public static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        Cursor cursor = null;
        String column = "_data";
        String[] projection = new String[]{"_data"};

        String var8;
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, (String)null);
            if (cursor == null || !cursor.moveToFirst()) {
                return null;
            }

            int index = cursor.getColumnIndexOrThrow("_data");
            var8 = cursor.getString(index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }

        }

        return var8;
    }

    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight, int maxSize) {
        int height = options.outHeight;
        int width = options.outWidth;
        int inSampleSize = 1;
        int reqHeight1;
        int halfHeight;
        if (maxSize > 0) {
            if (height > width) {
                reqHeight1 = maxSize;
                if (height > reqHeight) {
                    for(halfHeight = height / 2; halfHeight / inSampleSize > reqHeight1; inSampleSize *= 2) {
                    }
                }
            } else {
                reqHeight1 = maxSize;
                if (width > reqWidth) {
                    for(halfHeight = width / 2; halfHeight / inSampleSize > reqHeight1; inSampleSize *= 2) {
                    }
                }
            }
        } else if (height > reqHeight || width > reqWidth) {
            reqHeight1 = height / 2;

            for(halfHeight = width / 2; reqHeight1 / inSampleSize > reqHeight && halfHeight / inSampleSize > reqWidth; inSampleSize *= 2) {
            }
        }

        return inSampleSize;
    }

    public static Bitmap getSmallBitmap(String filePath) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);
        options.inSampleSize = calculateInSampleSize(options, 300, 300, 300);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(filePath, options);
    }

    public static BitmapDrawable getBitmapWithOption(String file) {
        if (!TextUtils.isEmpty(file) && (new File(file)).exists()) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.RGB_565;
            return new BitmapDrawable(BitmapFactory.decodeFile(file));
        } else {
            return null;
        }
    }

    public static BitmapDrawable getBitmapWithOption(String file, int reqWidth, int reqHeight) {
        if (!TextUtils.isEmpty(file) && (new File(file)).exists()) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(file, options);
            options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight, 0);
            options.inPreferredConfig = Bitmap.Config.RGB_565;
            options.inJustDecodeBounds = false;
            return new BitmapDrawable(BitmapFactory.decodeFile(file));
        } else {
            return null;
        }
    }

    public static void logBitmapInfo(Context context, int resId, int reqWidth, int reqHeight) {
        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeResource(context.getResources(), resId, options);
            options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight, 0);
            options.inPreferredConfig = Bitmap.Config.RGB_565;
            options.inJustDecodeBounds = false;
            Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), resId, options);
            Log.d("","-->> BitmapUtil logBitmapInfo res outW=" + options.outWidth + " outH=" + options.outHeight + " inSimpleSize=" + options.inSampleSize + " size=" + bitmap.getByteCount());
        } catch (Exception var6) {
            var6.printStackTrace();
        }

    }

    public static void logBitmapInfo(Context context, String file, int reqWidth, int reqHeight) {
        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(file, options);
            options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight, 0);
            options.inPreferredConfig = Bitmap.Config.RGB_565;
            options.inJustDecodeBounds = false;
            Bitmap bitmap = BitmapFactory.decodeFile(file, options);
            Log.d("","-->> BitmapUtil logBitmapInfo file outW=" + options.outWidth + " outH=" + options.outHeight + " inSimpleSize=" + options.inSampleSize + " size=" + bitmap.getByteCount());
        } catch (Exception var6) {
            var6.printStackTrace();
        }

    }

    public static int getRotateAngle(String filePath) {
        short rotate_angle = 0;

        try {
            ExifInterface exifInterface = new ExifInterface(filePath);
            int orientation = exifInterface.getAttributeInt("Orientation", 1);
            switch(orientation) {
                case 3:
                    rotate_angle = 180;
                    break;
                case 6:
                    rotate_angle = 90;
                    break;
                case 8:
                    rotate_angle = 270;
            }
        } catch (IOException var4) {
            var4.printStackTrace();
        }

        return rotate_angle;
    }

    public static Bitmap setRotateAngle(int angle, Bitmap bitmap) {
        if (bitmap != null) {
            Matrix m = new Matrix();
            m.postRotate((float)angle);
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), m, true);
            return bitmap;
        } else {
            return bitmap;
        }
    }

    public static Bitmap createCircleImage(Bitmap source) {
        int length = source.getWidth() < source.getHeight() ? source.getWidth() : source.getHeight();
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        Bitmap target = Bitmap.createBitmap(length, length, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(target);
        canvas.drawCircle((float)(length / 2), (float)(length / 2), (float)(length / 2), paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(source, 0.0F, 0.0F, paint);
        return target;
    }

    public static String compressImage(String filePath, String targetDir, String targetFileName, int reqWidth, int reqHeight) {
        File oldFile = new File(filePath);
        Log.d("","-->> BitmapUtil compressImage path=" + oldFile.getPath() + " targetPath=" + targetDir + File.separator + oldFile.getName());
        if (oldFile.exists() && !oldFile.isDirectory()) {
            File targetDirFile = new File(targetDir);
            if (!targetDirFile.exists()) {
                targetDirFile.mkdirs();
            }

            targetFileName = !TextUtils.isEmpty(targetFileName) ? targetFileName : oldFile.getName();
            String targetPath = targetDir + File.separator + targetFileName;
            int quality = 50;
            Bitmap bm = getSmallBitmapByPath(filePath, reqWidth, reqHeight);
            int degree = getRotateAngle(filePath);
            if (degree != 0) {
                bm = setRotateAngle(degree, bm);
            }

            File outputFile = new File(targetPath);

            try {
                if (!outputFile.exists()) {
                    outputFile.getParentFile().mkdirs();
                } else {
                    outputFile.delete();
                }

                FileOutputStream out = new FileOutputStream(outputFile);
                bm.compress(Bitmap.CompressFormat.JPEG, quality, out);
                out.close();
            } catch (Exception var13) {
                var13.printStackTrace();
                return filePath;
            }

            return outputFile.getPath();
        } else {
            return "";
        }
    }

    public static Bitmap getSmallBitmapByPath(String filePath, int reqWidth, int reqHeight) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(filePath, options);
    }

    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        int height = options.outHeight;
        int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            int heightRatio = Math.round((float)height / (float)reqHeight);
            int widthRatio = Math.round((float)width / (float)reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }

        return inSampleSize;
    }
}
