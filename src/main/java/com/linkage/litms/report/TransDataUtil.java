package com.linkage.litms.report;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * 这个类是Integer、String、Long与Byte之间转换方法集合
 *
 * @author yuhaiteng
 * @version 1.00, 2007-9-3
 * @since CronManager 1.0
 */
public class TransDataUtil {
    /**
     * 短整形转换字节流
     *
     * @param s
     *            整形值
     * @param ln
     *            字节数组长度
     * @return 返回字节数组
     */
    public static byte[] getBytes(short s, int ln) {
        byte[] buf = new byte[ln];
        for (int i = buf.length - 1; i >= 0; i--) {
            buf[i] = (byte) (s & 0x00ff);
            s >>= 8;
        }

        return buf;
    }

    /**
     * 整形转换字节流
     *
     * @param s
     *            整形值
     * @param ln
     *            字节数组长度
     * @return 返回字节数组
     */
    public static byte[] getBytes(int s, int ln) {
        byte[] buf = new byte[ln];
        for (int i = buf.length - 1; i >= 0; i--) {
            buf[i] = (byte) (s & 0x000000ff);
            s >>= 8;
        }

        return buf;
    }

    /**
     * 字符串转换字节流
     *
     * @param s
     *            字符串
     * @param ln
     *            字节数组长度
     * @return 返回字节数组
     */
    public static byte[] getBytes(String s, int ln) {
        byte[] buf;
        if (ln != -1) {
            buf = new byte[ln];
            byte[] tmp = s.getBytes();
            for (int i = buf.length - 1; i >= 0; i--) {
                if (i < tmp.length) {
                    buf[i] = tmp[i];
                }
            }
            return buf;
        } else {
            return s.getBytes();
        }
    }

    /**
     * 长整形转换字节流
     *
     * @param s
     *            长整形值
     * @param ln
     *            字节数组长度
     * @return 返回字节数组
     */
    public static byte[] getBytes(long s, int ln) {
        byte[] buf = new byte[ln];
        for (int i = buf.length - 1; i >= 0; i--) {
            buf[i] = (byte) (s & 0x00000000000000ff);
            s >>= 8;
        }

        return buf;
    }

    /**
     * 字节数组转换字符串
     *
     * @param buf
     *            字节数组
     * @return 返回字符串
     */
    public static String getString(byte[] buf) {
        String s = new String(buf);

        return s.trim();
    }

    public static short getShort(byte[] buf) {
        if (buf == null) {
            throw new IllegalArgumentException("byte array is null!");
        }
        if (buf.length > 2) {
            throw new IllegalArgumentException("byte array size > 1 !");
        }
        short r = 0;
        for (int i = 0; i < buf.length; i++) {
            r <<= 8;
            r |= (buf[i] & 0x00ff);
        }
        return r;
    }

    /**
     * 字节数组转换整形
     *
     * @param buf
     *            字节数组
     * @return 返回整形
     */
    public static int getInt(byte[] buf) {
        if (buf == null) {
            throw new IllegalArgumentException("byte array is null!");
        }
        if (buf.length > 4) {
            throw new IllegalArgumentException("byte array size > 2 !");
        }
        int r = 0;
        for (int i = 0; i < buf.length; i++) {
            r <<= 8;
            r |= (buf[i] & 0x000000ff);
        }
        return r;
    }

    /**
     * 字节数组转换长整形
     *
     * @param buf
     *            字节数组
     * @return 返回长整形
     */
    public static long getLong(byte[] buf) {
        if (buf == null) {
            throw new IllegalArgumentException("byte array is null!");
        }
        if (buf.length > 8) {
            throw new IllegalArgumentException("byte array size > 4 !");
        }
        long r = 0;
        for (int i = 0; i < buf.length; i++) {
            r <<= 8;
            r |= (buf[i] & 0x00000000000000ff);
        }
        return r;
    }
    
    public static long parseDate(String pattern, String text) {
	Date d = new SimpleDateFormat(pattern).parse(text, new ParsePosition(0));
	return d.getTime();
    }
}