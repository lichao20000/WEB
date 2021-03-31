
package com.linkage.litms.common.impt;

import org.apache.log4j.Logger;

/**
 * <一句话功能简述> <功能详细描述>
 * 
 * @author fangchao
 * @version [版本号, 2013-9-24]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public abstract class FileReader<E>
{

	private static Logger logger = Logger.getLogger(FileReader.class);
	/**
	 * 标题所在的行号，-1表示没有标题
	 */
	protected int titleRow = -1;
	/**
	 * 当前所在行
	 */
	protected int currentRow = -1;
	protected Class<E> beanClass = null;
	protected String[] beanProperties = new String[0];

	/**
	 * 设置标题行号
	 * 
	 * @param titleRow
	 * @see [类、类#方法、类#成员]
	 */
	public void setTitleRow(int titleRow)
	{
		if (titleRow < 0)
		{
			throw new IllegalArgumentException("illegal title row[" + titleRow
					+ "], must larger than zero");
		}
		this.titleRow = titleRow;
		// 当前所在行默认为标题行的下一行
		currentRow = titleRow;
	}

	public void setContentStartRow(int contentStartRow)
	{
		if (contentStartRow < 0)
		{
			throw new IllegalArgumentException("illegal content start row["
					+ contentStartRow + "], must larger than zero");
		}
		currentRow = contentStartRow - 1;
	}

	public void setBeanProperties(Class<E> beanClass, String[] beanProperties)
	{
		this.beanClass = beanClass;
		this.beanProperties = beanProperties;
		checkBean();
	}

	public E getTitle()
	{
		// default return null, subclass must implements it
		return null;
	}

	/**
	 * 返回当前所在行数，注：有些文件如Excel的开始行从0开始
	 * 
	 * @return 返回当前处理的索引行
	 */
	public int getCurrentRow()
	{
		return currentRow;
	}

	public abstract E getNextRow();

	public abstract boolean hasNextRow();

	public void destory()
	{
		// do nothing
	}

	protected E newBeanInstance()
	{
		checkBean();
		try
		{
			return beanClass.newInstance();
		}
		catch (InstantiationException e)
		{
			logger.error(e.getMessage(), e);
			throw new UnsupportedOperationException("Class[" + beanClass
					+ "] cann't new instance with nullary constructor", e);
		}
		catch (IllegalAccessException e)
		{
			logger.error(e.getMessage(), e);
			throw new UnsupportedOperationException(e.getMessage(), e);
		}
	}

	protected void checkBean()
	{
		if (beanClass == null)
		{
			throw new IllegalArgumentException("illegal bean Class, must not be null");
		}
		if (beanProperties == null || beanProperties.length == 0)
		{
			throw new IllegalArgumentException(
					"illegal bean properties, must not be null");
		}
	}
}
