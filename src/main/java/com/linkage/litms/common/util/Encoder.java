/*
 * @(#)Encoder.java	1.00 1/5/2006
 *
 * Copyright 2005 联创科技.版权所有
 */
package com.linkage.litms.common.util;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/**
 * 编码互相转换，所有方法都是静态的
 * 
 * @author yuht
 * @version 1.00, 1/5/2006
 * @since Liposs 2.1
 */
public class Encoder {
	/**
	 * Ascii编码转换GB2312编码 
	 * 
	 * @param s
	 *            Ascii编码字符串
	 * @return GB2312编码的字符串
	 */
	public static String AsciiToChineseString(String s) {
		char[] orig = s.toCharArray();
		byte[] dest = new byte[orig.length];
		for (int i = 0; i < orig.length; i++) {
			dest[i] = (byte) (orig[i] & 0xFF);
		}
		try {
			
			// struts2 版本由2.3. => 2.5.26 不支持 ByteToCharConverter 2020/12/11 modify by zhangyu25
			
//			ByteToCharConverter toChar = ByteToCharConverter
//					.getConverter("gb2312");
//			return new String(toChar.convertAll(dest));
			
			return new String(dest, 0, dest.length, "GB2312");
		} catch (Exception e) {
			return s;
		}
	}

	/**
	 * GB2312编码转换Ascii编码
	 * 
	 * @param s
	 *            GB2312编码的字符串
	 * @return Ascii编码字符串
	 */
	public static String ChineseStringToAscii(String s) {
		try {
			// struts2 版本由2.3.X => 2.5.26 不支持 CharToByteConverter 2020/12/11 modify by zhangyu25
			
//			CharToByteConverter toByte = CharToByteConverter
//					.getConverter("gb2312");
//			byte[] orig = toByte.convertAll(s.toCharArray());
			
			Charset charset = Charset.forName("GB2312");
	        ByteBuffer bb = charset.encode(s);
	        byte[] orig = bb.array();
			
			char[] dest = new char[orig.length];
			for (int i = 0; i < orig.length; i++) {
				dest[i] = (char) (orig[i] & 0xFF);
			}
			return new String(dest);
		} catch (Exception e) {
			return s;
		}
	}
    
    /**
     * 字符集转化；<br>
     * 参数：Sting s(字节编码）；String c1(源）; String c2(目的）
     */
    protected static String decode(String s, String c1, String c2)
        throws Exception {
        StringBuffer sb = new StringBuffer(); //字节编码转换，c1为源字节编码、c2目的字节编码
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            switch (c) {
                case '+' :
                    sb.append(' ');
                    break;
                case '%' :
                    try {
                        sb.append(
                            (char) Integer.parseInt(
                                s.substring(i + 1, i + 3),
                                16));
                    } catch (NumberFormatException e) {
                    	e.printStackTrace();
                    }
                    i += 2;
                    break;
                default :
                    sb.append(c);
                    break;
            }
        }
        String result = sb.toString();
        byte[] inputBytes = result.getBytes(c1);
        return new String(inputBytes, c2);
    }

    /**
     * ISO转化到GB
     */
    public static String toGB(String str) throws Exception {
        return decode(str, "8859_1", "GB2312"); //编码从ISO转化到GB
    }

    /**
     * GB转化到ISO
     */
    public static String toISO(String str) throws Exception {
        return decode(str, "GB2312", "8859_1"); //编码从GB转换到ISO
    }

    public static String toItself(String str) throws Exception {
        return str;
    }
    
	// 将 s 进行 BASE64 编码
	public static String getBase64(String s) {
		if (s == null)
			return null;
		return (new BASE64Encoder()).encode(s.getBytes());
	}

	// 将 BASE64 编码的字符串 s 进行解码
	public static String getFromBase64(String s) {
		if (s == null)
			return null;
		BASE64Decoder decoder = new BASE64Decoder();
		try {
			byte[] b = decoder.decodeBuffer(s);
			return new String(b);
		} catch (Exception e) {
			return null;
		}
	}
	
	public static String toGBKFromUTF(String str){
        try {
            return  java.net.URLDecoder.decode(str,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            return str;
        }
    }
    
}
