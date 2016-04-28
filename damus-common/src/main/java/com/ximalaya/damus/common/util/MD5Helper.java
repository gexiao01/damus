package com.ximalaya.damus.common.util;

import java.io.File;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

public class MD5Helper {
    private final static String[] hexDigits = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9",
            "a", "b", "c", "d", "e", "f" };

    static Logger logger = Logger.getLogger(MD5Helper.class);

    public static final String ENCODING = "UTF-8";

    private static String byteArrayToHexString(byte[] b) {
        StringBuffer resultSb = new StringBuffer();
        for (int i = 0; i < b.length; i++) {
            resultSb.append(byteToHexString(b[i]));
        }
        return resultSb.toString();
    }

    private static String byteToHexString(byte b) {
        int n = b;
        if (n < 0) {
            n = 256 + n;
        }
        int d1 = n / 16;
        int d2 = n % 16;
        return hexDigits[d1] + hexDigits[d2];
    }

    public static String MD5Encode32(String origin) {
        String resultString = null;
        try {
            resultString = origin;
            MessageDigest md = MessageDigest.getInstance("MD5");
            resultString = byteArrayToHexString(md.digest(resultString.getBytes(ENCODING)));
        } catch (Exception ex) {
        }
        return resultString;
    }

    /**
     * 对字节数组MD5，失败抛异常
     * 
     * @throws NoSuchAlgorithmException
     */
    public static String MD5Encode32(byte[] origin) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        return byteArrayToHexString(md.digest(origin));
    }

    public static String MD5Encode16(String origin) {
        return MD5Encode32(origin).substring(8, 24);
    }

    public static String MD5Encode8(String origin) {
        return MD5Encode32(origin).substring(16, 24);
    }

    public static boolean validateFile(String fileName) {

        if (StringUtils.isEmpty(fileName)) {
            return false;
        }
        if (fileName.endsWith(".md5")) {
            fileName = fileName.substring(0, fileName.length() - 4);
        }

        try {
            byte[] bs = FileUtils.readFileToByteArray(new File(fileName));
            String md5Str = FileUtils.readFileToString(new File(fileName + ".md5"), ENCODING);

            String checkMd5 = byteArrayToHexString(bs);
            return md5Str.contains(checkMd5);
        } catch (Exception e) {
            logger.warn("validateFile error: " + fileName + " " + e.getCause());
            return false;
        }
    }
}