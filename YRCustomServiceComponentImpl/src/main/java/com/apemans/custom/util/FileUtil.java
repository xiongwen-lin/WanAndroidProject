package com.apemans.custom.util;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;



import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FileUtil {
    public static final String MainDir = "Victure";
    public static final String VideoDir = "Video";
    public static final String SnapshotDir = "Snapshot";
    public static final String CacheDir = "Cache";
    public static final String PersonPortrait = "Portrait";
    public static final String LogDir = "log";
    public static final String DetectionThumbnail = "Thumbnail";
    public static final String StorageTempDir = "Temp";
    public static final String PresetPointThumbnailDir = "PresetPointThumbnail";
    public static final String EncryptDir = "Encrypt";
    public static final String NOOIE_PREVIEW_THUMBNAIL_PREFIX = "VICTURE_THUMBNAIL";
    public static final String NOOIE_SNAP_SHOT_THUMBNAIL_SUFFIX = "nooie";
    public static final String NOOIE_SNAP_SHOT_SUFFIX = "JPG";
    public static final String PRESET_POINT_PREFIX = "PRESET_POINT_";
    public static final String TEMP_PRESET_POINT_PREFIX = "TEMP_PRESET_POINT_";
    private static String mMainDir = "";
    private static final String DOT_NOOIE = ".Victure";

    public FileUtil() {
    }

    public static String getMainDir() {
        return TextUtils.isEmpty(mMainDir) ? "Victure" : mMainDir;
    }

    public static void setMainDir(String mainDir) {
        mMainDir = mainDir;
    }

    public static String getLocalRootSavePathDir(Context context, String dir) {
        if (context == null) {
            return "";
        } else {
            String path = "";
            if (!Environment.getExternalStorageState().equals("mounted") && Environment.isExternalStorageRemovable()) {
                path = getPrivateLocalRootSavePathDir(context, dir);
            } else {
                path = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + getMainDir() + File.separator + dir;
            }

            return path;
        }
    }

    public static String getPrivateLocalRootSavePathDir(Context context, String dir) {
        if (context == null) {
            return new String();
        } else {
            String path = new String();

            try {
                path = context.getExternalFilesDir("").getAbsolutePath() + File.separator + getMainDir() + File.separator + dir;
                Log.d("","-->> FileUtils getPrivateLocalRootSavePathDir filedir=" + context.getFilesDir() + " externalfiledir=" + context.getExternalFilesDir((String)null));
            } catch (Exception var4) {
                Log.d("",""+var4);
            }

            return path;
        }
    }

    public static String getStorageTempFolder(Context context) {
        String tempDir = getPrivateLocalRootSavePathDir(context, "Temp");
        return createDir(tempDir);
    }

    public static String getPreviewThumbSavePath(Context context, String deviceId) {
        return (new File(getStorageTempFolder(context), String.format("%s_%s_%s.%s", "VICTURE_THUMBNAIL", deviceId, String.valueOf(System.currentTimeMillis()), "JPG"))).getAbsolutePath();
    }

    public static String mapPreviewThumbPath(String path) {
        return path.replace("JPG", "nooie");
    }

    public static void renamePreviewThumb(String path, String newPath) {
        (new File(path)).renameTo(new File(newPath));
    }

    public static File getSnapshotDir(Context context, String account) {
        String snapshotDir = getPrivateLocalRootSavePathDir(context, account) + File.separator + "Snapshot";
        if (Build.VERSION.SDK_INT < 29) {
            snapshotDir = getLocalRootSavePathDir(context, account) + File.separator + "Snapshot";
        }

        return createDirFile(snapshotDir);
    }

    public static File getRecordVideoDir(Context context, String account) {
        String videoDir = getPrivateLocalRootSavePathDir(context, account) + File.separator + "Video";
        if (Build.VERSION.SDK_INT < 29) {
            videoDir = getLocalRootSavePathDir(context, account) + File.separator + "Video";
        }

        return createDirFile(videoDir);
    }

    public static String getNooieSavedScreenShotPath(Context context, String deviceId, String suffix, String account) {
        File saveSnapshotDir = getSnapshotDir(context, account);
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String time = format.format(new Date());
        String fileName = deviceId + "_" + time + "." + suffix;
        String path = saveSnapshotDir.getAbsolutePath() + File.separatorChar + fileName;

        try {
            (new File(path)).createNewFile();
        } catch (IOException var10) {
            var10.printStackTrace();
        }

        return path;
    }

    public static String getNooieSavedRecordPath(Context context, String deviceId, String suffix, String account) {
        File saveRecordDir = getRecordVideoDir(context, account);
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String time = format.format(new Date());
        String fileName = deviceId + "_" + time + "." + suffix;
        String path = saveRecordDir.getAbsolutePath() + File.separatorChar + fileName;

        try {
            (new File(path)).createNewFile();
        } catch (IOException var10) {
            var10.printStackTrace();
        }

        return path;
    }

    public static File getPersonPortraitInPrivate(Context context, String account) {
        StringBuilder portraitSubPathSb = new StringBuilder();
        portraitSubPathSb.append(account);
        portraitSubPathSb.append(File.separator);
        portraitSubPathSb.append("Portrait");
        String portrait = getPrivateLocalRootSavePathDir(context, portraitSubPathSb.toString());
        return createDirFile(portrait);
    }

    public static String getPortraitPhotoPathInPrivate(Context context, String account, String useid) {
        File portraitDir = getPersonPortraitInPrivate(context, account);
        StringBuffer buffer = new StringBuffer();
        buffer.append(portraitDir.getAbsolutePath()).append(File.separator).append(getPortraitPhotoName(useid));
        return buffer.toString();
    }

    public static File getPersonPortrait(Context context, String account) {
        String portrait = getPrivateLocalRootSavePathDir(context, account) + File.separator + "Temp" + File.separator + "Portrait";
        return createDirFile(portrait);
    }

    public static String getAccountNamePortrait(Context context, String userName) {
        String name = TextUtils.isEmpty(userName) ? "" : userName;
        File portraitDir = getPersonPortrait(context, name);
        String namePP = name + "." + "jpg";
        StringBuffer buffer = new StringBuffer();
        buffer.append(portraitDir.getAbsolutePath()).append(File.separator).append(namePP);
        return buffer.toString();
    }

    public static String getTmpAccountNamePortrait(Context context, String userName) {
        String name = TextUtils.isEmpty(userName) ? "" : userName;
        File portraitDir = getPersonPortrait(context, name);
        String namePP = "tmp_" + name + "." + "jpg";
        StringBuffer buffer = new StringBuffer();
        buffer.append(portraitDir.getAbsolutePath()).append(File.separator).append(namePP);
        return buffer.toString();
    }

    public static String getPortraitPhotoPath(Context context, String username, String useid) {
        File portraitDir = getPersonPortrait(context, username);
        StringBuffer buffer = new StringBuffer();
        buffer.append(portraitDir.getAbsolutePath()).append(File.separator).append(getPortraitPhotoName(useid));
        return buffer.toString();
    }

    public static String getPortraitPhotoName(String useid) {
        StringBuilder sb = new StringBuilder();
        sb.append(useid);
        sb.append(".");
        sb.append("jpg");
        return sb.toString();
    }

    public static File getDetectionThumbnailRootPathInPrivate(Context context, String account) {
        StringBuilder subPathSb = new StringBuilder();
        subPathSb.append(account);
        subPathSb.append(File.separator);
        subPathSb.append("Thumbnail");
        String detectionPath = getPrivateLocalRootSavePathDir(context, subPathSb.toString());
        return createDirFile(detectionPath);
    }

    public static File getDetectionThumbnailPathInPrivate(Context context, String account, String deviceId) {
        StringBuilder subPathSb = new StringBuilder();
        subPathSb.append(account);
        subPathSb.append(File.separator);
        subPathSb.append("Thumbnail");
        subPathSb.append(File.separator);
        subPathSb.append(deviceId);
        String detectionSubPath = getPrivateLocalRootSavePathDir(context, subPathSb.toString());
        return createDirFile(detectionSubPath);
    }

    public static String getDetectionThumbnailFilename(long startTime) {
        StringBuffer buffer = new StringBuffer();
        buffer.append(startTime).append(".").append("jpg");
        return buffer.toString();
    }

    public static String getDetectionThumbnailFilePath(Context context, String account, String deviceId, long startTime) {
        StringBuilder subPathSb = new StringBuilder();
        subPathSb.append(getDetectionThumbnailPathInPrivate(context, account, deviceId));
        subPathSb.append(File.separator);
        subPathSb.append(getDetectionThumbnailFilename(startTime));
        return subPathSb.toString();
    }

    public static String getPresetPointThumbnailFolderPath(Context context, String account) {
        if (TextUtils.isEmpty(account)) {
            return new String();
        } else {
            StringBuilder presetPointThumbnailFolderPath = new StringBuilder();
            presetPointThumbnailFolderPath.append(getPrivateLocalRootSavePathDir(context, account)).append(File.separator).append("PresetPointThumbnail").append(File.separator);
            return presetPointThumbnailFolderPath.toString();
        }
    }

    public static String createPresetPointThumbnailName(String deviceId, int position, String prefix) {
        if (!TextUtils.isEmpty(deviceId) && position >= 1) {
            StringBuilder presetPointThumbnailName = new StringBuilder();
            presetPointThumbnailName.append(prefix).append(position).append("_").append(deviceId).append(".").append("jpg");
            return presetPointThumbnailName.toString();
        } else {
            return new String();
        }
    }

    public static String createPresetPointThumbnailName(String deviceId, int position) {
        return createPresetPointThumbnailName(deviceId, position, "PRESET_POINT_");
    }

    public static String createTempPresetPointThumbnailName(String deviceId, int position) {
        return createPresetPointThumbnailName(deviceId, position, "TEMP_PRESET_POINT_");
    }

    public static String getPresetPointThumbnail(Context context, String account, String deviceId, int position, String prefix) {
        if (!TextUtils.isEmpty(account) && !TextUtils.isEmpty(deviceId) && position >= 1) {
            String presetPointThumbnailFolderPath = getPresetPointThumbnailFolderPath(context, account);
            String presetPointThumbnailName = createPresetPointThumbnailName(deviceId, position, prefix);
            if (!TextUtils.isEmpty(presetPointThumbnailFolderPath) && !TextUtils.isEmpty(presetPointThumbnailName)) {
                StringBuilder presetPointThumbnailPath = new StringBuilder();
                presetPointThumbnailPath.append(presetPointThumbnailFolderPath).append(presetPointThumbnailName);
                return presetPointThumbnailPath.toString();
            } else {
                return new String();
            }
        } else {
            return new String();
        }
    }

    public static String getPresetPointThumbnail(Context context, String account, String deviceId, int position) {
        if (!TextUtils.isEmpty(account) && !TextUtils.isEmpty(deviceId) && position >= 1) {
            String presetPointThumbnailFolderPath = getPresetPointThumbnailFolderPath(context, account);
            String presetPointThumbnailName = createPresetPointThumbnailName(deviceId, position);
            if (!TextUtils.isEmpty(presetPointThumbnailFolderPath) && !TextUtils.isEmpty(presetPointThumbnailName)) {
                StringBuilder presetPointThumbnailPath = new StringBuilder();
                presetPointThumbnailPath.append(presetPointThumbnailFolderPath).append(presetPointThumbnailName);
                return presetPointThumbnailPath.toString();
            } else {
                return new String();
            }
        } else {
            return new String();
        }
    }

    public static String getTempPresetPointThumbnail(Context context, String account, String deviceId, int position) {
        if (!TextUtils.isEmpty(account) && !TextUtils.isEmpty(deviceId) && position >= 1) {
            String presetPointThumbnailFolderPath = getPresetPointThumbnailFolderPath(context, account);
            String presetPointThumbnailName = createTempPresetPointThumbnailName(deviceId, position);
            if (!TextUtils.isEmpty(presetPointThumbnailFolderPath) && !TextUtils.isEmpty(presetPointThumbnailName)) {
                StringBuilder presetPointThumbnailPath = new StringBuilder();
                presetPointThumbnailPath.append(presetPointThumbnailFolderPath).append(presetPointThumbnailName);
                return presetPointThumbnailPath.toString();
            } else {
                return new String();
            }
        } else {
            return new String();
        }
    }

    public static File getCacheDir(Context context, String account) {
        String cacheDir = getPrivateLocalRootSavePathDir(context, account) + File.separator + "Cache";
        return createDirFile(cacheDir);
    }

    public static String getLogFolder(Context context) {
        String thumbDir = getPrivateLocalRootSavePathDir(context, "log");
        return createDir(thumbDir);
    }

    public static List<File> getLogFiles(Context context) {
        ArrayList logFiles = new ArrayList();

        try {
            File file = new File(getLogFolder(context));
            File[] fileList = file.listFiles();
            if (fileList != null && fileList.length > 0) {
                for(int i = 0; i < fileList.length; ++i) {
                    if (fileList[i] != null && fileList[i].isFile()) {
                        logFiles.add(fileList[i]);
                    }
                }
            }
        } catch (Exception var5) {
            var5.printStackTrace();
        }

        return logFiles;
    }





    public static void clearAllCache(Context context, String account) {
        try {
            deleteDir(context.getCacheDir());
            if (Environment.getExternalStorageState().equals("mounted")) {
                deleteDir(context.getExternalCacheDir());
                deleteDir(new File(getStorageTempFolder(context)));
                deleteDir(new File(getLogFolder(context)));
                if (!TextUtils.isEmpty(account)) {
                    deleteDir(getCacheDir(context, account));
                }
            }
        } catch (Exception var3) {
            var3.printStackTrace();
        }

    }






    public static String createDir(String dir) {
        File file = new File(dir);
        if (!file.exists()) {
            file.mkdirs();
        }

        return file.getAbsolutePath();
    }

    public static File createDirFile(String path) {
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }

        return file;
    }

    public static boolean deleteFile(String filePath) {
        return TextUtils.isEmpty(filePath) ? false : deleteFile(new File(filePath));
    }

    public static boolean deleteFile(File file) {
        try {
            if (file != null && file.exists()) {
                return file.delete();
            }
        } catch (Exception var2) {
            var2.printStackTrace();
        }

        return false;
    }

    public static boolean copyFile(String srcFilePath, String targetPath) {
        try {
            File targetFile = new File(targetPath);
            if (!targetFile.exists()) {
                targetFile.mkdirs();
            }

            File srcFile = new File(srcFilePath);
            if (srcFile.exists()) {
                InputStream inStream = new FileInputStream(srcFilePath);
                String targetFilePath = targetPath + File.separator + srcFile.getName();
                FileOutputStream fs = new FileOutputStream(targetFilePath);
                byte[] buffer = new byte[1024];

                int byteread;
                while((byteread = inStream.read(buffer)) != -1) {
                    fs.write(buffer, 0, byteread);
                }

                fs.flush();
                inStream.close();
                fs.close();
            }

            return true;
        } catch (Exception var9) {
            var9.printStackTrace();
            return false;
        }
    }

    public static String copyFile(Context context, Uri inputUri, String outPath) {
        if (context != null && inputUri != null && !TextUtils.isEmpty(outPath)) {
            InputStream inputStream = null;
            FileOutputStream outputStream = null;

            Object var6;
            try {
                inputStream = context.getContentResolver().openInputStream(inputUri);
                File outFile = new File(outPath);
                outputStream = new FileOutputStream(outFile);
                if (inputStream != null && outFile.exists()) {
                    byte[] buffer = new byte[1024];

                    int length;
                    while((length = inputStream.read(buffer)) > 0) {
                        outputStream.write(buffer, 0, length);
                    }

                    outputStream.flush();
                    inputStream.close();
                    outputStream.close();
                    return outPath;
                }

                var6 = null;
            } catch (Exception var11) {
                Log.d("",""+var11);
                return outPath;
            } finally {
                inputStream = null;
                outputStream = null;
            }

            return (String)var6;
        } else {
            return null;
        }
    }

    public static boolean copyAssetFile(Context context, String assetFilePath, String targetFolderPath, boolean isOverWrite) {
        if (context != null && !TextUtils.isEmpty(assetFilePath) && !TextUtils.isEmpty(targetFolderPath)) {
            try {
                File targetFolderFile = new File(targetFolderPath);
                if (!targetFolderFile.exists()) {
                    targetFolderFile.mkdirs();
                }

                String assetFileName = assetFilePath.contains(File.separator) ? assetFilePath.substring(assetFilePath.lastIndexOf(File.separator) + 1) : assetFilePath;
                if (TextUtils.isEmpty(assetFileName)) {
                    return false;
                } else {
                    InputStream inStream = context.getResources().getAssets().open(assetFilePath);
                    String targetFilePath = targetFolderPath + File.separator + assetFileName;
                    File targetFile = new File(targetFilePath);
                    if (targetFile.exists() && !isOverWrite) {
                        return true;
                    } else {
                        FileOutputStream fs = new FileOutputStream(targetFile);
                        byte[] buffer = new byte[1024];

                        int byteread;
                        while((byteread = inStream.read(buffer)) != -1) {
                            fs.write(buffer, 0, byteread);
                        }

                        fs.flush();
                        inStream.close();
                        fs.close();
                        return true;
                    }
                }
            } catch (Exception var12) {
                Log.d("",""+var12);
                return false;
            }
        } else {
            return false;
        }
    }

    public static void copyAssetFolder(Context context, String oldPath, String newPath, boolean isOverWrite) {
        try {
            String[] fileNames = context.getAssets().list(oldPath);
            if (fileNames == null) {
                return;
            }

            File file;
            int byteCount;
            if (fileNames.length > 0) {
                file = new File(newPath);
                if (!file.exists()) {
                    file.mkdirs();
                }

                StringBuilder oldFilePathSb = new StringBuilder();
                StringBuilder newFilePathSb = new StringBuilder();
                String[] var8 = fileNames;
                byteCount = fileNames.length;

                for(int var10 = 0; var10 < byteCount; ++var10) {
                    String fileName = var8[var10];
                    oldFilePathSb.setLength(0);
                    oldFilePathSb.append(oldPath).append(File.separator).append(fileName);
                    newFilePathSb.setLength(0);
                    newFilePathSb.append(newPath).append(File.separator).append(fileName);
                    copyAssetFolder(context, oldFilePathSb.toString(), newFilePathSb.toString(), isOverWrite);
                }
            } else {
                file = new File(newPath);
                if (file.exists() && !isOverWrite) {
                    return;
                }

                InputStream is = context.getAssets().open(oldPath);
                FileOutputStream fos = new FileOutputStream(new File(newPath));
                byte[] buffer = new byte[1024];
                boolean var16 = false;

                while((byteCount = is.read(buffer)) != -1) {
                    fos.write(buffer, 0, byteCount);
                }

                fos.flush();
                is.close();
                fos.close();
            }
        } catch (Exception var12) {
            Log.d("",""+var12);
        }

    }



    private static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            int subFileSize = children != null ? children.length : 0;

            for(int i = 0; i < subFileSize; ++i) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }

        return dir.delete();
    }

    public static String getFilePathByUri(Context context, Uri uri) {
        String path = null;
        if ("file".equals(uri.getScheme())) {
            path = uri.getPath();
            return path;
        } else {
            if ("content".equals(uri.getScheme()) && Build.VERSION.SDK_INT >= 19 && DocumentsContract.isDocumentUri(context, uri)) {
                String docId;
                String[] split;
                String type;
                if (isExternalStorageDocument(uri)) {
                    docId = DocumentsContract.getDocumentId(uri);
                    split = docId.split(":");
                    type = split[0];
                    if ("primary".equalsIgnoreCase(type)) {
                        path = Environment.getExternalStorageDirectory() + "/" + split[1];
                        return path;
                    }
                } else {
                    if (isDownloadsDocument(uri)) {
                        docId = DocumentsContract.getDocumentId(uri);
                        Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(docId));
                        path = getDataColumn(context, contentUri, (String)null, (String[])null);
                        return path;
                    }

                    if (isMediaDocument(uri)) {
                        docId = DocumentsContract.getDocumentId(uri);
                        split = docId.split(":");
                        type = split[0];
                        Uri contentUri = null;
                        if ("image".equals(type)) {
                            contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                        } else if ("video".equals(type)) {
                            contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                        } else if ("audio".equals(type)) {
                            contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                        }

                        String selection = "_id=?";
                        String[] selectionArgs = new String[]{split[1]};
                        path = getDataColumn(context, contentUri, "_id=?", selectionArgs);
                        return path;
                    }
                }
            }

            return null;
        }
    }

    private static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        Cursor cursor = null;
        String column = "_data";
        String[] projection = new String[]{"_data"};

        String var8;
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, (String)null);
            if (cursor == null || !cursor.moveToFirst()) {
                return null;
            }

            int column_index = cursor.getColumnIndexOrThrow("_data");
            var8 = cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }

        }

        return var8;
    }

    private static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    private static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    private static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    private static boolean isSdCardAvailable() {
        return "mounted".equals(Environment.getExternalStorageState());
    }

    public static String getTotalCacheSize(Context context, String account) throws Exception {
        long cacheSize = getFolderSize(context.getCacheDir());
        if (isSdCardAvailable()) {
            cacheSize += getFolderSize(context.getExternalCacheDir());
            cacheSize += getFolderSize(new File(getStorageTempFolder(context)));
            cacheSize += getFolderSize(new File(getLogFolder(context)));
            if (!TextUtils.isEmpty(account)) {
                cacheSize += getFolderSize(getCacheDir(context, account));
            }
        }

        return getFormatSize((double)cacheSize);
    }

    public static long getFolderSize(File file) throws Exception {
        long size = 0L;

        try {
            File[] fileList = file.listFiles();
            int fileSize = fileList != null ? fileList.length : 0;

            for(int i = 0; i < fileSize; ++i) {
                if (fileList[i].isDirectory()) {
                    size += getFolderSize(fileList[i]);
                } else {
                    size += fileList[i].length();
                }
            }
        } catch (Exception var6) {
            var6.printStackTrace();
        }

        return size;
    }

    public static String getFormatSize(double size) {
        double kiloByte = size / 1024.0D;
        if (kiloByte < 1.0D) {
            return "0K";
        } else {
            double megaByte = kiloByte / 1024.0D;
            if (megaByte < 1.0D) {
                BigDecimal result1 = new BigDecimal(Double.toString(kiloByte));
                return result1.setScale(2, 4).toPlainString() + "K";
            } else {
                double gigaByte = megaByte / 1024.0D;
                if (gigaByte < 1.0D) {
                    BigDecimal result2 = new BigDecimal(Double.toString(megaByte));
                    return result2.setScale(2, 4).toPlainString() + "M";
                } else {
                    double teraBytes = gigaByte / 1024.0D;
                    BigDecimal result4;
                    if (teraBytes < 1.0D) {
                        result4 = new BigDecimal(Double.toString(gigaByte));
                        return result4.setScale(2, 4).toPlainString() + "GB";
                    } else {
                        result4 = new BigDecimal(teraBytes);
                        return result4.setScale(2, 4).toPlainString() + "TB";
                    }
                }
            }
        }
    }

    public static String getEncryptKeyFolder(Context context) {
        return getPrivateLocalRootSavePathDir(context, "Encrypt");
    }

    public static void log(Context context, String account, String uid, String deviceId, long startTime) {
        StringBuilder logSb = new StringBuilder();
        logSb.append("\n");
        logSb.append("getMainDir: ");
        logSb.append(getMainDir());
        logSb.append("\n");
        logSb.append("getLocalRootSavePathDir: ");
        logSb.append(getLocalRootSavePathDir(context, ""));
        logSb.append("\n");
        logSb.append("getPrivateLocalRootSavePathDir: ");
        logSb.append(getPrivateLocalRootSavePathDir(context, ""));
        logSb.append("\n");
        logSb.append("getStorageTempFolder: ");
        logSb.append(getStorageTempFolder(context));
        logSb.append("\n");
        logSb.append("getPreviewThumbSavePath: ");
        logSb.append(getPreviewThumbSavePath(context, deviceId));
        logSb.append("\n");
        logSb.append("getSnapshotDir: ");
        logSb.append(getSnapshotDir(context, account));
        logSb.append("\n");
        logSb.append("getRecordVideoDir: ");
        logSb.append(getRecordVideoDir(context, account));
        logSb.append("\n");
        logSb.append("getNooieSavedScreenShotPath: ");
        logSb.append(getNooieSavedScreenShotPath(context, deviceId, "jpg", account));
        logSb.append("\n");
        logSb.append("getNooieSavedRecordPath: ");
        logSb.append(getNooieSavedRecordPath(context, deviceId, "jpg", account));
        logSb.append("\n");
        logSb.append("getPersonPortraitInPrivate: ");
        logSb.append(getPersonPortraitInPrivate(context, account));
        logSb.append("\n");
        logSb.append("getPortraitPhotoPathInPrivate: ");
        logSb.append(getPortraitPhotoPathInPrivate(context, account, uid));
        logSb.append("\n");
        logSb.append("getPersonPortrait: ");
        logSb.append(getPersonPortrait(context, account));
        logSb.append("\n");
        logSb.append("getAccountNamePortrait: ");
        logSb.append(getAccountNamePortrait(context, account));
        logSb.append("\n");
        logSb.append("getTmpAccountNamePortrait: ");
        logSb.append(getTmpAccountNamePortrait(context, account));
        logSb.append("\n");
        logSb.append("getPortraitPhotoPath: ");
        logSb.append(getPortraitPhotoPath(context, account, uid));
        logSb.append("\n");
        logSb.append("getPortraitPhotoName: ");
        logSb.append(getPortraitPhotoName(uid));
        logSb.append("\n");
        logSb.append("getDetectionThumbnailRootPathInPrivate: ");
        logSb.append(getDetectionThumbnailRootPathInPrivate(context, account));
        logSb.append("\n");
        logSb.append("getDetectionThumbnailPathInPrivate: ");
        logSb.append(getDetectionThumbnailPathInPrivate(context, account, deviceId));
        logSb.append("\n");
        logSb.append("getDetectionThumbnailFilename: ");
        logSb.append(getDetectionThumbnailFilename(startTime));
        logSb.append("\n");
        logSb.append("getDetectionThumbnailFilePath: ");
        logSb.append(getDetectionThumbnailFilePath(context, account, deviceId, startTime));
        logSb.append("\n");
        logSb.append("getCacheDir: ");
        logSb.append(getCacheDir(context, account));
        logSb.append("\n");
        logSb.append("getLogFolder: ");
        logSb.append(getLogFolder(context));
        Log.d("","-->> FileUtil log test " + logSb.toString());
    }

    /**
     * 文件操作 获取文件扩展名
     */
    public  static  String getExtensionName(String  filename){
        if (!TextUtils.isEmpty(filename)) {
            int dot = filename.lastIndexOf('.');
            if (dot > -1 && dot < filename.length() - 1) {
                return filename.substring(dot + 1);
            }
        }
        return filename;

    }
}
