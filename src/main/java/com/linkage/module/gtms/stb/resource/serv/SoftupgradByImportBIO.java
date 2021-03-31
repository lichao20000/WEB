
package com.linkage.module.gtms.stb.resource.serv;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.util.StringUtil;
import com.linkage.module.gtms.stb.resource.dao.SoftupgradByImportDAO;
import com.linkage.module.gwms.share.act.FileUploadAction;

/**
 * @author yinlei3 (73167.)
 * @version 1.0
 * @since 2016年3月8日
 * @category com.linkage.module.gtms.stb.resource.serv
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 */
public class SoftupgradByImportBIO
{

	/** 日志记录 */
	private static Logger logger = LoggerFactory.getLogger(SoftupgradByImportBIO.class);
	/** dao */
	private SoftupgradByImportDAO dao;

	/**
	 * 解析生成策略
	 */
	public String makeStrategy(String fileName, String softStrategy_type,
			String goal_softwareversion)
	{
		if (StringUtil.IsEmpty(fileName))
		{
			return "未上传文件";
		}
		String result = "";
		String fileName_ = fileName.substring(fileName.length() - 3, fileName.length());
		logger.debug("fileName_;{}", fileName_);
		if (!"xls".equals(fileName_) && !"txt".equals(fileName_))
		{
			return "上传的文件格式不正确！";
		}
		ArrayList<Map<String, String>> dataList = null;
		try
		{
			if ("txt".equals(fileName_))
			{
				dataList = getImportDataByTXT(fileName);
			}
			else
			{
				dataList = getImportDataByXLS(fileName);
			}
		}
		catch (FileNotFoundException e)
		{
			logger.error("{}文件没找到！", fileName);
			return "文件没找到！";
		}
		catch (IOException e)
		{
			logger.error("{}文件解析出错！{}", fileName, e);
			return "文件解析出错！";
		}
		catch (Exception e)
		{
			logger.error("{}文件解析出错！{}", fileName, e);
			return "文件解析出错！";
		}
		if (dataList.size() < 1)
		{
			return "文件未解析到合法数据！";
		}
		else
		{
			// 生成策略
			result = dao.addStrategy(dataList, goal_softwareversion);
		}
		return result;
	}

	/**
	 * 解析文件（txt格式）
	 * 
	 * @param fileName
	 *            文件名
	 * @return
	 * @throws FileNotFoundException
	 *             IOException
	 */
	public ArrayList<Map<String, String>> getImportDataByTXT(String fileName)
			throws FileNotFoundException, IOException
	{
		logger.debug("getImportDataByTXT{}", fileName);
		ArrayList<Map<String, String>> list = new ArrayList<Map<String, String>>();
		BufferedReader in = new BufferedReader(new FileReader(getFilePath() + fileName));
		String line = in.readLine();
		logger.warn("标题行为:" + line);
		/**
		 * 判断导入文件查询的条件，默认为用户账号，当匹配到设备序列号时， 则以设备序列号作为条件，否则一律以用户账号作为条件
		 */
		boolean username;
		if (null != line && line.contains("设备序列号"))
		{
			username = false;
		}
		else
		{
			username = true;
		}
		// 从第二行开始读取数据，并对每行数据去掉首尾空格
		while ((line = in.readLine()) != null)
		{
			if (!StringUtil.IsEmpty(line.trim()))
			{
				Map<String, String> taskMap = dao.getDeviceInfo(line.trim(), username);
				if (taskMap != null && !taskMap.isEmpty())
				{
					list.add(taskMap);
				}
			}
		}
		in.close();
		in = null;
		// 处理完毕时，则删掉文件
		File f = new File(getFilePath() + fileName);
		f.delete();
		f = null;
		return list;
	}

	/**
	 * 解析文件（xls格式）
	 * 
	 * @param fileName
	 *            文件名
	 * @return
	 * @throws IOException
	 * @throws BiffException
	 * @throws
	 */
	public ArrayList<Map<String, String>> getImportDataByXLS(String fileName)
			throws BiffException, IOException
	{
		logger.debug("getImportDataByXLS{}", fileName);
		ArrayList<Map<String, String>> list = new ArrayList<Map<String, String>>();
		File f = new File(getFilePath() + fileName);
		Workbook wwb = Workbook.getWorkbook(f);
		;
		Sheet ws = null;
		ws = wwb.getSheet(0);
		// 当前页总记录行数和列数
		int rowCount = ws.getRows();
		// int columeCount = ws.getColumns();
		boolean username;
		if (null != ws.getCell(0, 0).getContents())
		{
			String line = ws.getCell(0, 0).getContents().trim();
			/**
			 * 判断导入文件查询的条件，默认为用户账号，当匹配到设备序列号时， 则以设备序列号作为条件，否则一律以用户账号作为条件
			 */
			if (null != line && "设备序列号".equals(line))
			{
				username = false;
			}
			else
			{
				logger.warn("标题行为:" + line);
				username = true;
			}
			// 取当前页所有值放入list中
			for (int i = 1; i < rowCount; i++)
			{
				String temp = ws.getCell(0, i).getContents();
				if (!StringUtil.IsEmpty(temp))
				{
					Map<String, String> taskMap = dao
							.getDeviceInfo(temp.trim(), username);
					if (taskMap != null && !taskMap.isEmpty())
					{
						list.add(taskMap);
					}
				}
			}
		}
		f.delete();
		f = null;
		return list;
	}

	/**
	 * 获取文件路径
	 * 
	 * @return
	 */
	public String getFilePath()
	{
		// 获取系统的绝对路径
		String lipossHome = "";
		String a = FileUploadAction.class.getResource("/").getPath();
		try
		{
			lipossHome = java.net.URLDecoder.decode(
					a.substring(0, a.lastIndexOf("WEB-INF") - 1), "UTF-8");
		}
		catch (Exception e)
		{
			logger.error(e.getMessage());
			lipossHome = null;
		}
		logger.debug("{}待解析的文件路径", lipossHome);
		return lipossHome + "/temp/";
	}

	/**
	 * 获取厂商集合
	 * 
	 * @return 厂商集合
	 */
	@SuppressWarnings("rawtypes")
	public List getVersionPathList()
	{
		return dao.getversionPath();
	}

	public void setDao(SoftupgradByImportDAO dao)
	{
		this.dao = dao;
	}
}
