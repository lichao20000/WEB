package com.linkage.litms.common.util;

import java.security.NoSuchAlgorithmException;

/**
 * 
 * @author xiangzl (Ailk No.)
 * @version 1.0
 * @since 2015-11-4
 * @category com.linkage.litms.common.util
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 *
 */
public final class MD5
{

    /**
     * 默认构造方法
     */
    private MD5() {

    }

    /**
     * 获得MD5加密字符串
     * 
     * @param source
     *            源字符串
     * 
     * @return 加密后的字符串
     * 
     */
    public static String getMD5(String source) {
        if (source != null) {
            return getMD5(source.getBytes());
        } else {
            return null;
        }
    }

    /**
     * 获得MD5加密字符串
     * 
     * @param source
     *            源字节数组
     * 
     * @return 加密后的字符串
     */
    public static String getMD5(byte[] source) {
        String s = null;
        char[] hexDigits = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
            'a', 'b', 'c', 'd', 'e', 'f' };
        final int temp = 0xf;
        final int arraySize = 32;
        final int strLen = 16;
        final int offset = 4;
        
        try {
            java.security.MessageDigest md = java.security.MessageDigest
                    .getInstance("MD5");
            md.update(source);
            byte[] tmp = md.digest();
            char[] str = new char[arraySize];
            int k = 0;
            for (int i = 0; i < strLen; i++) {
                byte byte0 = tmp[i];
                str[k++] = hexDigits[byte0 >>> offset & temp];
                str[k++] = hexDigits[byte0 & temp];
            }
            s = new String(str);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        
        return s;
    }
}
