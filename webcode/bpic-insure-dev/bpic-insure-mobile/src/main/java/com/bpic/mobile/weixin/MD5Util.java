package com.bpic.mobile.weixin;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

/**
 * MD5加密工具类
 */
public class MD5Util {
    public static String md5(String originalStr) {
        if ("".equals(originalStr) && originalStr == null) {
            return "";
        }
        String digestStr = null;
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException exc) {
            System.err.println("This version of Java does not support MD5.");
            return null;
        }
        md.reset();
        md.update(originalStr.getBytes());
        byte[] rawDigest = md.digest();

        StringBuffer str = new StringBuffer();
        for (int i = 0; i < rawDigest.length; i++) {
            String bStr = Integer.toHexString((int) rawDigest[i] & 0xff);
            if (bStr.length() == 1) {
                str.append("0" + bStr);
            } else {
                str.append(bStr);
            }
        }
        digestStr = str.toString();

        return digestStr;
    }

    /**
     * 生成6位随机数验证码
     * @return
     */
    public static String randomCode() {
        StringBuilder str = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 6; i++) {
            str.append(random.nextInt(10));
        }
        return str.toString();
    }
}
