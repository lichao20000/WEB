
package com.linkage.module.gtms.stb.diagnostic.serv;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.util.DateTimeUtil;
import com.linkage.commons.util.StringUtil;
import com.linkage.litms.system.UserRes;
import com.linkage.module.gtms.stb.diagnostic.dao.StbBatchRestartDAO;
import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.module.gwms.share.act.FileUploadAction;

public class StbBatchRestartBIO
{

	private StbBatchRestartDAO dao;
	private static Logger logger = LoggerFactory
			.getLogger(StbBatchRestartBIO.class);
	// 回传消息
	private String msg = null;
	// 查询条件
	private String importQueryField = "username";
	public static boolean flag = true;

	/*
	 * 开始日期格式转换 yyyy-mm-dd --> 毫秒
	 */
	public static long startTimeOfMonth(Date date)
	{
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		System.out.println(cal.getTime());
		return cal.getTimeInMillis() / 1000;
	}

	/*
	 * 结束日期转换 yyyy-mm-dd --> 毫秒
	 */
	public static long endTimeOfMonth(Date date)
	{
		// 将当前月设置为第一天后，加1个月，然后-1秒。
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		cal.add(Calendar.MONTH, 1);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		System.out.println(cal.getTime());
		return cal.getTimeInMillis() / 1000 - 1;
	}

	/**
	 * 查询设备（带列表）（针对导入查询）
	 * 
	 * @param curUser
	 * @param cityId
	 * @param fileName
	 * @return
	 */
	public List<HashMap<String, String>> getDeviceList(UserRes curUser, String cityId,
			String fileName)
	{
		logger.warn("getDeviceListBIO({},{},{})", new Object[] { cityId, fileName });
		if (fileName.length() < 4)
		{
			this.msg = "上传的文件名不正确！";
			return null;
		}
		String fileName_ = fileName.substring(fileName.length() - 3, fileName.length());
		logger.warn("fileName_;{}", fileName_);
		if (!"txt".equals(fileName_))
		{
			this.msg = "上传的文件格式不正确！";
			return null;
		}
		List<String> dataList = null;
		try
		{
			dataList = getImportDataByTXT(fileName);
			logger.warn("dataList;{}", dataList);
		}
		catch (FileNotFoundException e)
		{
			logger.warn("{}文件没找到！", fileName);
			this.msg = "文件没找到！";
			return null;
		}
		catch (IOException e)
		{
			logger.warn("{}文件解析出错！", fileName);
			this.msg = "文件解析出错！";
			return null;
		}
		catch (Exception e)
		{
			logger.warn("{}文件解析出错！", fileName);
			this.msg = "文件解析出错！";
			return null;
		}
		List<HashMap<String, String>> list = null;
		ArrayList<String> cityArray = null;
		Map<String, String> cityMap = CityDAO.getCityIdCityNameMap();
		List<HashMap<String, String>> listNew = new ArrayList<HashMap<String, String>>();
		ArrayList<String> listSql = new ArrayList<String>();
		if (dataList.size() < 1)
		{
			this.msg = "文件未解析到合法数据！";
			return null;
		}
		else if (dataList.size() > 20000)// ???????????????????????????????
		{
			this.msg = "文件行数不要超过20000行";
			return null;
		}
		else
		{
			dao.insertTmp(fileName, dataList, importQueryField);
			if ("username".equals(importQueryField))
			{
				list = dao.queryDeviceByImportUsername(fileName);
				if (!StringUtil.IsEmpty(cityId) && !"00".equals(cityId))
				{
					logger.warn("StbBatchRestartBIO=>cityArraystart...:" + cityArray);
					cityArray = CityDAO.getAllNextCityIdsByCityPid(cityId);
					logger.warn("StbBatchRestartBIO=>cityArrayend...:" + cityArray);
				}
				if (null != list && list.size() > 0)
				{
					String retMessage = "";
					for (HashMap<String, String> map : list)
					{
						logger.warn("StbBatchRestartBIO=>map...........................:" + map);
						String fileUsername = StringUtil.getStringValue(map,
								"file_username", "");
						String servAccount = StringUtil.getStringValue(map,
								"serv_account", "");
						String deviceSN = StringUtil.getStringValue(map,
								"device_serialnumber", "");
						String cityIdTmp = StringUtil.getStringValue(map, "city_id", "");
						long taskId = new DateTimeUtil().getLongTime();
						logger.warn("StbBatchRestartBIO=>taskId...:" + taskId);
						if (StringUtil.IsEmpty(fileUsername))
						{
							continue;
						}
						if (StringUtil.IsEmpty(deviceSN))
						{
							retMessage = "在设备表不存在";
							listSql.add(dao.saveResSql(taskId, fileUsername, retMessage));
						}
						else
						{
							if (StringUtil.IsEmpty(cityIdTmp))
							{
								retMessage = "用户属地不在定制管理员属地范围内";
								listSql.add(dao.saveResSql(taskId, fileUsername,
										retMessage));
							}
							else
							{
								if (null != cityArray && cityArray.size() > 0
										&& !cityArray.contains(cityIdTmp))
								{
									retMessage = "用户属地不在定制管理员属地范围内";
									listSql.add(dao.saveResSql(taskId, fileUsername,
											retMessage));
								}
								else
								{
									if (StringUtil.IsEmpty(servAccount))
									{
										retMessage = "在用户表中不存在";
										listSql.add(dao.saveResSql(taskId, fileUsername,
												retMessage));
									}
									else
									{
										map.put("city_name", StringUtil.getStringValue(
												cityMap, StringUtil.getStringValue(map,
														"city_id", ""), ""));
										listNew.add(map);
									}
								}
							}
						}
					}
					if (null != listSql && listSql.size() > 0)
					{
						int res = dao.excuteBatch(listSql);
						if (res > 0)
						{
							logger.warn("==更新成功==res=" + res);
						}
					}
				}
			}
		}
		if (null == listNew || listNew.size() < 1)
		{
			this.msg = "账号不存在";
		}
		return listNew;
	}

	/**
	 * 执行批量重启设备入库
	 * @param deviceList
	 * @param startTime
	 * @param type
	 * @param curUser
	 * @param fileUsername
	 * @return
	 */
	public String insertRestartDev(List<String> deviceList,
			Long startTime, int intervalTime, UserRes curUser)
	{
		String resMessage = "";
		ArrayList<String> listSql = new ArrayList<String>();
		long taskId = new DateTimeUtil().getLongTime();
		for (String infoStr : deviceList)
		{
			String[] infoStrArr = infoStr.split("##");
			if (null != infoStrArr && infoStrArr.length <= 3) 
			{
				String deviceId = infoStrArr[0];
				String fileUsername = infoStrArr[1];
				String deviceSN = infoStrArr[2];
				listSql.add(dao.insertRestartdevSql(taskId, fileUsername, deviceId, deviceSN));
			}
		}
		listSql.add(dao.insertRestartdevTaskSql(taskId, curUser.getUser().getId(), startTime, intervalTime));
		logger.warn("...................listSql.........." + listSql);
		if (null != listSql && listSql.size() > 0)
		{
			int res = dao.excuteBatch(listSql);
			if (res > 0)
			{
				logger.warn("==更新成功==res=" + res);
				resMessage = "设置成功";
			}else
			{
				resMessage = "设置失败";
			}
		}
		else
		{
			resMessage = "设置失败";
		}
		return resMessage;
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
	public List<String> getImportDataByTXT(String fileName) throws FileNotFoundException,
			IOException
	{
		logger.debug("getImportDataByTXT{}", fileName);
		List<String> list = new ArrayList<String>();
		BufferedReader in = new BufferedReader(new FileReader(getFilePath() + fileName));
		String line = in.readLine();
		this.importQueryField = "username";
		// 从第二行开始读取数据，并对每行数据去掉首尾空格
		while ((line = in.readLine()) != null)
		{
			if (!"".equals(line.trim()))
			{
				list.add(line.trim());
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
	 * 获取今天总数
	 */
	public long getTodayCount()
	{
		return dao.getTodayCount();
	}

	public StbBatchRestartDAO getDao()
	{
		return dao;
	}

	public void setDao(StbBatchRestartDAO dao)
	{
		this.dao = dao;
	}

	public static Logger getLogger()
	{
		return logger;
	}

	public String getMsg()
	{
		return msg;
	}

	public void setMsg(String msg)
	{
		this.msg = msg;
	}
}
