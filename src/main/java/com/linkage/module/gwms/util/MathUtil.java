/**
 * LINKAGE TECHNOLOGY (NANJING) CO.,LTD.<BR>
 * Copyright 2007-2010. All right reserved.
 */
package com.linkage.module.gwms.util;

import java.util.Random;

import com.linkage.module.gwms.Global;

/**
 * math util.
 * 
 * @author Alex.Yan (yanhj@lianchuang.com)
 * @version 2.0, Jul 9, 2009
 * @see
 * @since 1.0
 */
public class MathUtil {
	/**
	 * get
	 * 
	 * @return
	 */
	public static String getRandom(int length) {
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
