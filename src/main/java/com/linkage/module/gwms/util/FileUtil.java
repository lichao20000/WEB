
package com.linkage.module.gwms.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import jxl.Sheet;
import jxl.Workbook;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 文件操作工具类
 * 
 * @author Jason(3412)
 * @date 2009-8-11
 */
public class FileUtil
{

	private static Logger logger = LoggerFactory.getLogger(FileUtil.class);

	/**
	 * <Pre>
	 * 简单封装创建文件夹操作 
	 * <b>当文件不存在时，才会进行文件夹的多层目录创建，
	 * 但创建文件夹失败（如权限不足）时，抛出UnsupportedOperationException运行时异常</b>
	 * </pre>
	 * 
	 * @param file
	 *            创建的文件,never null
	 * @throws UnsupportedOperationException
	 *             当创建文件时失败时
	 */
	public static void mkdirs(File file)
	{
		if (!file.exists())
		{
			if (!file.mkdirs())
			{
				logger.error("can not make directory by path[{}], may be auth limit",
						file.getAbsolutePath());
				throw new UnsupportedOperationException("");
			}
		}
	}

	/**
	 * <pre>
	 * 删除文件，该方法只支持文件删除和空文件夹删除，
	 * 如果要删除非空文件夹请调用commons-io.jar中FileUtils类公用方法
	 * 如果删除文件失败，则记录warn日志，不进行其他任何处理
	 * </pre>
	 * 
	 * @param file
	 *            删除文件对象，never null
	 */
	public static void delete(File file)
	{
		if (file.exists())
		{
			try
			{
				if (!file.delete())
				{
					logger.warn("delete file[{}] failed.", file);
				}
			}
			catch (Exception ingore)
			{
			}
		}
	}

	/**
	 * 输入excel文件,解析所有sheet的所有行,返回ArrayList
	 * 
	 * @param file
	 *            输入的excel文件
	 * @return ArrayList<String>
	 */
	public static List<String> file2list(File file)
	{
		logger.debug("file2list({})", file);
		return file2list(file, -1);
	}

	/**
	 * 输入excel文件,解析指定的行数,返回ArrayList
	 * 
	 * @param file
	 *            输入的excel文件 fileReadCount 需要解析的行数，为-1时表示不做控制
	 * @return ArrayList<String>
	 */
	public static List<String> file2list(File file, int fileReadCount)
	{
		logger.debug("file2list({},{})", file, fileReadCount);
		if (null == file)
		{
			logger.debug("file2list(file) is null");
			return null;
		}
		// 初始化返回值和字段名数组
		ArrayList<String> arr = new ArrayList<String>();
		Workbook wwb = null;
		Sheet ws = null;
		try
		{
			// 读取excel文件
			wwb = Workbook.getWorkbook(file);
			// 总sheet数
			int sheetNumber = wwb.getNumberOfSheets();
			logger.debug("sheetNumber:" + sheetNumber);
			// 增加限制，只解析1500行
			for (int m = 0; m < sheetNumber && m < 1500; m++)
			{
				// 只解析第一个sheet
				ws = wwb.getSheet(m);
				// 当前页总记录行数和列数
				int rowCount = ws.getRows();
				int columeCount = ws.getColumns();
				logger.debug("rowCount:" + rowCount);
				logger.debug("columeCount:" + columeCount);
				// 需要做解析的行数，为-1的时候表示全部解析
				if (fileReadCount > 0 && fileReadCount < rowCount)
				{
					rowCount = fileReadCount + 1;
				}
				// 第一行为字段名，所以行数大于1才执行
				if (rowCount > 1 && columeCount > 0)
				{
					// 取当前页所有值放入list中
					for (int i = 1; i < rowCount; i++)
					{
						String temp = ws.getCell(0, i).getContents().trim();
						if (null != temp && !"".equals(temp))
						{
							arr.add(temp);
						}
					}
				}
			}
		}
		catch (Exception e)
		{
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		finally
		{
			try
			{
				if (null != wwb)
				{
					wwb.close();
				}
			}
			catch (Exception e)
			{
				logger.debug(e.getMessage());
				e.printStackTrace();
			}
		}
		return arr;
	}
}
