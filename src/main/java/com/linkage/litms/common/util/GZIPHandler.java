/*
 * @(#)GZIPHandler.java	1.00 1/5/2006
 *
 * Copyright 2005 联创科技.版权所有
 */
package com.linkage.litms.common.util;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 通过GZIP加密和解密，所有方法都是静态的
 *
 * @author yuht
 * @version 1.00, 1/5/2006
 * @since Liposs 2.1
 */
public class GZIPHandler {
	/**
	 * 加密字节数组
	 *
	 * @param bytesData
	 *            需要加密的字节数组
	 * @return 加密之后的字节数组
	 */
	private static Logger m_logger = LoggerFactory.getLogger(GZIPHandler.class);
	static public byte[] Compress(byte[] bytesData) {
		GZIPOutputStream gzos = null;
		ByteArrayOutputStream strbuf = new ByteArrayOutputStream();
		byte out[] = null;
		try {
			gzos = new GZIPOutputStream(strbuf, bytesData.length);
			gzos.write(bytesData, 0, bytesData.length);
			gzos.finish();
			out = strbuf.toByteArray();
		} catch (Exception ioException) {
			m_logger.error("ioException    " + ioException.getMessage());
			ioException.printStackTrace();
			out = new byte[0];
		} finally {
			try {
				gzos.close();
				strbuf.close();
			} catch (IOException ioException) {
				ioException.printStackTrace();
			}
		}
		return out;
	}

	/**
	 * 解密字节数组
	 *
	 * @param bytesData
	 *            需要解密的字节数组
	 * @return 解密之后的字节数组
	 */
	static public String Decompress(byte[] bytesData) {
		StringBuffer xmlStr = null;
		GZIPInputStream gzip = null;
		BufferedReader zipReader = null;
		ByteArrayInputStream strbuf = null;
		try {
			strbuf = new ByteArrayInputStream(bytesData);
			gzip = new GZIPInputStream(strbuf);
			zipReader = new BufferedReader(new InputStreamReader(gzip));
			char chars[] = new char[1024];
			int len = 0;
			xmlStr = new StringBuffer();
			while ((len = zipReader.read(chars, 0, chars.length)) >= 0) {
				xmlStr.append(chars, 0, len);
			}
			chars = null;
		} catch (IOException ioExc) {
			m_logger.error("Exception Occured in Catch Block"
					+ ioExc.getMessage());
		} finally {
			try {
				if (gzip != null)
					gzip.close();
				zipReader.close();
				strbuf.close();
			} catch (IOException ioExc) {
				m_logger.error("Exception Occured in finally block"
						+ ioExc.getMessage());
			}
		}
		return xmlStr.toString();
	}
}
