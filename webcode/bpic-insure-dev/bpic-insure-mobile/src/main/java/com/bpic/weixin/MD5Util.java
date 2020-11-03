package com.bpic.weixin;

import org.apache.commons.codec.digest.DigestUtils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

/**
 * MD5加密工具类
 */
public class MD5Util {

    //private static final String baseMD5 = "baseMD5";

    public static String md5(String originalStr) {
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

        StringBuilder str = new StringBuilder();
        for (int i = 0; i < rawDigest.length; i++) {
            String bStr = Integer.toHexString((int) rawDigest[i] & 0xff);
            if (bStr.length() == 1) {
                str.append("0").append(bStr);
            } else {
                str.append(bStr);
            }
        }
        digestStr = str.toString();

        return digestStr;
    }

    /**
     * @param text 明文
     * @param key 密钥
     * @return 密文
     */
    // 带秘钥加密
    public static String md5(String text, String key) throws Exception {
        // 加密后的字符串
        String md5str = DigestUtils.md5Hex(text + key);
        System.out.println("MD5加密后的字符串为:" + md5str);
        return md5str;
    }

    // 不带秘钥加密
    public static String md52(String text) throws Exception {
        // 加密后的字符串
        String md5str = DigestUtils.md5Hex(text);
        System.out.println("MD52加密后的字符串为:" + md5str + "\t长度：" + md5str.length());
        return md5str;
    }

    /**
     * MD5验证方法
     *
     * @param text 明文
     * @param key 密钥
     * @param md5 密文
     */
    // 根据传入的密钥进行验证
    public static boolean verify(String text, String key, String md5) throws Exception {
        String md5str = md5(text, key);
        if (md5str.equalsIgnoreCase(md5)) {
            System.out.println("MD5验证通过");
            return true;
        }
        return false;
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
