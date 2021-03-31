/**
 * 
 */
package com.linkage.litms.common;

/**
 * @author zxj E-mail：qixq@lianchuang.com
 * @version $Revision$ 
 * @since 2009-3-18
 * @category com.linkage.litms.common
 * 
 */
public class StaticTypeCommon {

	/**
	 * 产生唯一主键，利用较低的重复概率
	 * 长度为10位，返回类型为String,可以进行Long转换
	 * 
	 */
	public static String generateId() {
		return String.valueOf(Math.round(Math.random() * 10000000000L));
	}

	/**
	 * 产生唯一主键，利用较低的重复概率
	 * @return long
	 */
	public static long generateLongId() {
		return Math.round(Math.random() * 10000000000L);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		

	}

}
