
package com.linkage.litms.common.util;

import java.util.Arrays;

import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 一些bean比较的公用方法
 * 
 * @author fangchao (Ailk No.)
 * @version 1.0
 * @since 2013-9-26
 * @category com.linkage.litms.common.util
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 */
public class EqualsUtil
{

	private static Logger logger = LoggerFactory.getLogger(EqualsUtil.class);

	/**
	 * 比较某个对象的属性值是否与参数propertiesValues的值一致
	 * 
	 * @param bean
	 *            需要比较的对象，never null
	 * @param propertiesNames
	 *            需要比较的对象的属性名，never null or empty
	 * @param propertiesValues
	 *            比较对象的属性的期待值，never null or empty
	 * @return 如果对象的属性值与期待的属性值完全相同时，返回true，否则返回false
	 */
	public static final <T> boolean checkBeanProperties(T bean, String[] propertiesNames,
			String[] propertiesValues)
	{
		int length = propertiesNames.length;
		if (length != propertiesValues.length)
		{
			return false;
		}
		try
		{
			String value = null;
			for (int i = 0; i < length; i++)
			{
				value = BeanUtils.getProperty(bean, propertiesNames[i]);
				if (!equals(value, propertiesValues[i]))
				{
					return false;
				}
			}
		}
		catch (Exception e)
		{
			String message = "get properties[" + Arrays.toString(propertiesNames)
					+ "] form instance[" + bean + "] error.";
			logger.error(message);
			logger.error(e.getMessage(), e);
			throw new IllegalArgumentException(message, e);
		}
		return true;
	}

	/**
	 * 比较两个对象的属性值是否一致，与重写对象的equals具有类似功能
	 * 
	 * @param srcBean
	 *            需要比较的对象1，never null
	 * @param destBean
	 *            需要比较的对象2，never null or empty
	 * @param propertiesValues
	 *            比较对象的属性，never null or empty
	 * @return 如果两个对象的属性值完全相同时，返回true，否则返回false
	 */
	public static final <T> boolean propertiesEquals(T srcBean, T destBean,
			String[] propertiesNames)
	{
		try
		{
			String srcValue = null;
			String destValue = null;
			for (int i = 0; i < propertiesNames.length; i++)
			{
				srcValue = BeanUtils.getProperty(srcBean, propertiesNames[i]);
				destValue = BeanUtils.getProperty(destBean, propertiesNames[i]);
				if (!equals(srcValue, destValue))
				{
					return false;
				}
			}
		}
		catch (Exception e)
		{
			String message = "get properties[" + Arrays.toString(propertiesNames)
					+ "] form instance[" + srcBean + "] error.";
			logger.error(message);
			logger.error(e.getMessage(), e);
			throw new IllegalArgumentException(message, e);
		}
		return true;
	}

	/**
	 * 比较两个字符串的值是否相同，完全匹配，对字符串而且null和""空字符串不同
	 * 
	 * @param str1
	 *            字符串一, may be null
	 * @param str2
	 *            字符串二,may be null
	 * @return 如果两个字符串的值相同，返回true，否则返回false
	 */
	public static boolean equals(String str1, String str2)
	{
		return str1 == null ? str2 == null : str1.equals(str2);
	}
}
