
package com.linkage.litms.common.impt;

import java.io.File;
import java.io.IOException;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.log4j.Logger;

import com.linkage.commons.util.StringUtil;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

public class ExcelReader<E> extends FileReader<E>
{

	private static Logger logger = Logger.getLogger(ExcelReader.class);
	protected Workbook excel;
	protected Sheet currentSheet;
	protected int sheetCount;
	protected int indexSheet;
	protected int rowCount;
	protected int columnCount;

	public ExcelReader(String excelFilepath) throws IOException
	{
		this(new File(excelFilepath));
	}

	public ExcelReader(File excelFile) throws IOException
	{
		try
		{
			excel = Workbook.getWorkbook(excelFile);
			sheetCount = excel.getNumberOfSheets();
			indexSheet = 0;
			initSheet();
		}
		catch (BiffException e)
		{
			logger.error(e.getMessage(), e);
			throw new IOException(e.getMessage(), e);
		}
	}

	public boolean hasNextSheet()
	{
		return indexSheet < sheetCount - 1;
	}

	public boolean goNextSheet()
	{
		if (hasNextSheet())
		{
			indexSheet++;
			initSheet();
			return true;
		}
		else
		{
			return false;
		}
	}

	protected void initSheet()
	{
		currentSheet = excel.getSheet(indexSheet);
		currentRow = titleRow;
		rowCount = currentSheet.getRows();
		columnCount = currentSheet.getColumns();
	}

	public E getTitle()
	{
		if (titleRow == -1)
		{
			return null;
		}
		if (titleRow > rowCount)
		{
			// get title with wrong title row, then return title instance with any
			// properties
			logger.warn("title row is [" + titleRow + "],but row count is [" + rowCount
					+ "]");
			return newBeanInstance();
		}
		return newBeanInstanceWithProperties(titleRow);
	}

	/**
	 * <pre>
	 * 获取Excel下一行数据
	 * 如果Excel读取了最后一行数据,即没有下一行数据，则返回null
	 * 如果Excel没到最后一行之前，总是返回Bean实例。如果某一行都没有值，则返回一个空属性值的Bean实例
	 * </pre>
	 */
	@Override
	public E getNextRow()
	{
		if (hasNextRow())
		{
			currentRow++;
			return newBeanInstanceWithProperties(currentRow);
		}
		else
		{
			return null;
		}
	}

	@Override
	public boolean hasNextRow()
	{
		// Excel start row with zero
		return currentRow < rowCount - 1;
	}

	public int getRowCount()
	{
		return rowCount;
	}

	public int getColumnCount()
	{
		return columnCount;
	}

	public void destory()
	{
		super.destory();
		currentSheet = null;
		if (excel != null)
		{
			excel.close();
			excel = null;
		}
	}

	protected E newBeanInstanceWithProperties(int row)
	{
		E result = newBeanInstance();
		int length = Math.min(columnCount, beanProperties.length);
		try
		{
			String content = null;
			for (int i = 0; i < length; i++)
			{
				content = currentSheet.getCell(i, row).getContents();
				if (!StringUtil.IsEmpty(content))
				{
					BeanUtils.copyProperty(result, beanProperties[i], content.trim());
				}
			}
		}
		catch (Exception e)
		{
			logger.error(e.getMessage(), e);
		}
		return result;
	}
}
