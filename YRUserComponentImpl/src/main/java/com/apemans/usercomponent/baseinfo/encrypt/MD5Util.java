package com.apemans.usercomponent.baseinfo.encrypt;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Util {
    public MD5Util() {
    }

    public static String MD5Hash(String s) {
        if (s == null) {
            return null;
        } else {
            MessageDigest md5 = null;

            try {
                md5 = MessageDigest.getInstance("MD5");
            } catch (Exception var3) {
                var3.printStackTrace();
            }

            return md5SumToString(md5.digest(s.getBytes()));
        }
    }

    public static String MD5Hash(byte[] content) {
        if (content == null) {
            return null;
        } else {
            MessageDigest md5 = null;

            try {
                md5 = MessageDigest.getInstance("MD5");
            } catch (Exception var3) {
                var3.printStackTrace();
            }

            return md5SumToString(md5.digest(content));
        }
    }

    private static String md5SumToString(byte[] md5Sum) {
        StringBuilder hexValue = new StringBuilder();

        for(int i = 0; i < md5Sum.length; ++i) {
            int val = md5Sum[i] & 255;
            if (val < 16) {
                hexValue.append("0");
            }

            hexValue.append(Integer.toHexString(val));
        }

        return hexValue.toString();
    }

    public static String getFileMd5(File file) {
        FileInputStream fis = null;

        try {
            try {
                MessageDigest messageDigest = MessageDigest.getInstance("MD5");
                String var31;
                if (file == null) {
                    var31 = "";
                    return var31;
                }

                if (!file.exists()) {
                    var31 = "";
                    return var31;
                }

                fis = new FileInputStream(file);
                byte[] buffer = new byte[10485760];

                int len;
                while((len = fis.read(buffer)) > 0) {
                    messageDigest.update(buffer, 0, len);
                }

                BigInteger bigInt = new BigInteger(1, messageDigest.digest());

                String md5;
                for(md5 = bigInt.toString(16); md5.length() < 32; md5 = "0" + md5) {
                }

                String var7 = md5;
                return var7;
            } catch (NoSuchAlgorithmException var25) {
                var25.printStackTrace();
            } catch (FileNotFoundException var26) {
                var26.printStackTrace();
            } catch (IOException var27) {
                var27.printStackTrace();
            } catch (Exception var28) {
                var28.printStackTrace();
            }

            return "";
        } finally {
            try {
                if (fis != null) {
                    fis.close();
                    fis = null;
                }
            } catch (IOException var24) {
                var24.printStackTrace();
            }

        }
    }

    public static String getFileMd5ByRaf(File file) {
        RandomAccessFile randomAccessFile = null;

        try {
            try {
                MessageDigest messageDigest = MessageDigest.getInstance("MD5");
                String var30;
                if (file == null) {
                    var30 = "";
                    return var30;
                }

                if (!file.exists()) {
                    var30 = "";
                    return var30;
                }

                randomAccessFile = new RandomAccessFile(file, "r");
                byte[] bytes = new byte[10485760];
                boolean var4 = false;

                int len;
                while((len = randomAccessFile.read(bytes)) != -1) {
                    messageDigest.update(bytes, 0, len);
                }

                BigInteger bigInt = new BigInteger(1, messageDigest.digest());

                String md5;
                for(md5 = bigInt.toString(16); md5.length() < 32; md5 = "0" + md5) {
                }

                String var7 = md5;
                return var7;
            } catch (NoSuchAlgorithmException var25) {
                var25.printStackTrace();
            } catch (FileNotFoundException var26) {
                var26.printStackTrace();
            } catch (IOException var27) {
                var27.printStackTrace();
            } catch (Exception var28) {
                var28.printStackTrace();
            }

            return "";
        } finally {
            try {
                if (randomAccessFile != null) {
                    randomAccessFile.close();
                    randomAccessFile = null;
                }
            } catch (IOException var24) {
                var24.printStackTrace();
            }

        }
    }
}
