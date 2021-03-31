/**
 * @(#)com.linkage.litms.acs.util.MathUtil.java
 */
package com.linkage.litms.common.util;

import com.linkage.module.gwms.Global;

/**
 * common math util.
 * <P>Linkage Communication Technology Co., Ltd<P>
 * <P>Copyright 2005-2007. All right reserved.<P>
 * @version 1.0.0 2007-6-29
 * @author LZJ (lizj@lianchuang.com)
 */
public class MathUtil {

	/**
	 * get 
	 * @return
	 */
	public static String getRandom() {
		String randStr = "012356789";
		StringBuffer generateRandStr = new StringBuffer();
		
		int randStrLength = 8;
		for (int i = 0; i < randStrLength; i++) {
			int randNum = Global.rand.nextInt(9);
			generateRandStr.append(randStr.substring(randNum, randNum + 1));
		}
		return generateRandStr.toString();

	}
	
}
