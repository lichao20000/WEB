package com.linkage.module.gtms.config.serv;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.util.DateTimeUtil;
import com.linkage.commons.util.StringUtil;
import com.linkage.litms.LipossGlobals;
import com.linkage.litms.system.UserRes;
import com.linkage.module.gtms.config.dao.StackRefreshToolsDAO;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.module.gwms.share.act.FileUploadAction;
import com.linkage.module.gwms.util.CreateObjectFactory;
import com.linkage.module.gwms.util.corba.PreProcessCorba;

public class StackRefreshToolsBIO {
	
	private StackRefreshToolsDAO dao;
	/** 开启双栈 service_id */
	public static String SERVICE_ID_DOUBLE_STACK = "2302";
	/** 开启单栈 service_id */
	public static String SERVICE_ID_SINGLE_STACK = "2301";

	private static Logger logger = LoggerFactory.getLogger(StackRefreshToolsBIO.class);
	// 回传消息
	private String msg = null;
	// 查询条件
	private String importQueryField = "username";

	public static boolean flag = true;
	
	public String doConfig(UserRes curUser, List<String> list, String type, String faultList) {
		logger.warn("StackRefreshToolsBIO.doConfig()");
		
		String serviceId = "";
		long time = new DateTimeUtil().getLongTime(); // 入表时间，同时为任务id
		if ("1".equals(type)) {
			serviceId = SERVICE_ID_SINGLE_STACK;
		} else if ("2".equals(type)) {
			serviceId = SERVICE_ID_DOUBLE_STACK;
		} 
		// 入数据库
		int result = -1;
		try {
			result = dao.doConfig(curUser.getUser().getId(), list, serviceId, time, faultList);
		} catch (Exception e) {
			logger.warn("更新表失败");
			msg = "-4";
		}
		String[] array = new String[list.size()];

		if (result>0) {
			PreProcessCorba ppc = new PreProcessCorba("1");
		      if (1 == ppc.processDeviceStrategy((String[])list.toArray(array), serviceId, new String[] { StringUtil.getStringValue(Long.valueOf(time)) })) {
		        logger.warn("调用后台预读模块成功");
		        this.msg = "1";
		      } else {
		        logger.warn("调用后台预读模块失败");
		        this.msg = "-4";
		      }
			
			/*if (1 == CreateObjectFactory.createPreProcess("1").processDeviceStrategy(
					list.toArray(array), serviceId,
					new String[] { StringUtil.getStringValue(time) })) {
				logger.warn("调用后台预读模块成功");
				msg = "1";
			} else {
				logger.warn("调用后台预读模块失败");
				msg = "-4";
			}*/
		}

		return msg;
	}
	
	public String doConfig4JX(UserRes curUser, String type, String filePath) {
		logger.warn("StackRefreshToolsBIO.doConfig4JX()");
		
		String serviceId = "";
		long time = new DateTimeUtil().getLongTime(); // 入表时间，同时为任务id
		if ("1".equals(type)) {
			serviceId = SERVICE_ID_SINGLE_STACK;
		} else if ("2".equals(type)) {
			serviceId = SERVICE_ID_DOUBLE_STACK;
		} 
		int result = dao.doConfig4JX(curUser.getUser().getId(), curUser.getCityId(), serviceId, time, filePath);
		if(result>0){
			logger.warn("更新表成功");
			msg = "1";
		}
		return msg;
	}

	/*
	 * 开始日期格式转换 yyyy-mm-dd --> 毫秒
	 */
	public static long startTimeOfMonth(Date date) {
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
	public static long endTimeOfMonth(Date date) {
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
	 * @param upMax 
	 * @param countMax 
	 * @return
	 */
	public List<HashMap<String, String>> getDeviceList(UserRes curUser,
			String cityId, String fileName, String faultList, long countMax, long upMax) {
		logger.warn("getDeviceList({},{},countMax:{},upMax:{})", new Object[] { cityId, fileName,countMax,upMax });
		
		faultList = "";
		
		if (fileName.length() < 4) {
			this.msg = "上传的文件名不正确！";
			return null;
		}
		String fileName_ = fileName.substring(fileName.length() - 3, fileName.length());
		if (!"xls".equals(fileName_) && !"txt".equals(fileName_)) {
			this.msg = "上传的文件格式不正确！";
			return null;
		}
		List<String> dataList = null;
		try {
			if ("txt".equals(fileName_)) {
				dataList = getImportDataByTXT(fileName);
			} else {
				dataList = getImportDataByXLS(fileName);
			}
		} catch (FileNotFoundException e) {
			logger.warn("{}文件没找到！", fileName);
			this.msg = "文件没找到！";
			return null;
		} catch (IOException e) {
			logger.warn("{}文件解析出错！", fileName);
			this.msg = "文件解析出错！";
			return null;
		} catch (Exception e) {
			logger.warn("{}文件解析出错！", fileName);
			this.msg = "文件解析出错！";
			return null;
		}
		List<HashMap<String, String>> list = null;
		ArrayList<String> cityArray = null;
		Map<String, String> cityMap = CityDAO.getCityIdCityNameMap();
		List<HashMap<String, String>> listNew = new ArrayList<HashMap<String, String>>();

		if (dataList.size() < 1) {
			this.msg = "文件未解析到合法数据！";
			return null;
		} else if (dataList.size() > upMax)
		{
			this.msg = "文件行数不要超过"+upMax+"行";
			return null;
		} else {
			long todayCount = dao.getTodayCount();
			if (countMax < todayCount) {
				this.msg = "今日执行用户数已满，不能超过"+countMax+"万个";
				return null;
			}
			dao.insertTmp(fileName, dataList, importQueryField);
			if ("username".equals(importQueryField)) {
				list = dao.queryDeviceByImportUsername(fileName);
				if (!StringUtil.IsEmpty(cityId) && !"00".equals(cityId)) {
					cityArray = CityDAO.getAllNextCityIdsByCityPid(cityId);
				}
				if (null != list && list.size() > 0) {
					String retMessage = "";
					for (HashMap<String, String> map : list) {
						String fileUsername = StringUtil.getStringValue(map, "file_username", "");
						String loid = StringUtil.getStringValue(map, "loid", "");
						String cityIdTmp = StringUtil.getStringValue(map, "city_id", "");
						String deviceId = StringUtil.getStringValue(map, "device_id", "");
						String netUsername = StringUtil.getStringValue(map, "net_username", "");
						

						if (StringUtil.IsEmpty(fileUsername)) {
							continue;
						}
						if (StringUtil.IsEmpty(loid)) {
							retMessage = "用户表中没有查询结果";
							faultList += fileUsername+"##"+retMessage + ";";
						} else {
							if (StringUtil.IsEmpty(cityIdTmp)) {
								retMessage = "用户表中用户属地为空";
								faultList += fileUsername+"##"+retMessage + ";";
							} else {
								if (null != cityArray && cityArray.size() > 0 && !cityArray.contains(cityIdTmp)) {
									retMessage = "没有操作该用户的权限";
									faultList += fileUsername+"##"+retMessage + ";";
								} else {
									if (StringUtil.IsEmpty(deviceId)) {
										retMessage = "用户没有绑定设备";
										faultList += fileUsername+"##"+retMessage + ";";
									}else{
										if (StringUtil.IsEmpty(netUsername)) {
											retMessage = "没有查到宽带账号";
											faultList += fileUsername+"##"+retMessage + ";";
										} else {
											map.put("city_name",StringUtil.getStringValue(cityMap, StringUtil.getStringValue(map, "city_id", ""), ""));
											listNew.add(map);
										}
									}
								}
							}
						}
					}

					if (!StringUtil.IsEmpty(faultList) && faultList.endsWith(";")) {
						faultList = faultList.substring(0, faultList.length()-1);
						logger.warn("以下账号无法定制："+faultList);
					}
				}
			}
		}
		if (null == listNew || listNew.size() < 1) {
			this.msg = "账号不存在";
		}else{
			HashMap<String,String> faultMap = new HashMap<String,String>();
			faultMap.put("faultMap", StringUtil.getStringValue(faultList));
			listNew.add(faultMap);
		}
		return listNew;
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
	public List<String> getImportDataByTXT(String fileName)
			throws FileNotFoundException, IOException {
		logger.warn("getImportDataByTXT:{}", fileName);
		List<String> list = new ArrayList<String>();
		BufferedReader in = new BufferedReader(new FileReader(getFilePath()
				+ fileName));
		String line = in.readLine();
		/**
		 * 判断导入文件查询的条件，默认为用户账号，当匹配到设备序列号时， 则以设备序列号作为条件，否则一律以用户账号作为条件
		 */
		if (null != line && line.contains("设备序列号")) {
			this.importQueryField = "device_serialnumber";
		} else if (null != line && line.contains("宽带账号")) {
			this.importQueryField = "kdusername";
		} else {
			this.importQueryField = "username";
		}
		// 从第二行开始读取数据，并对每行数据去掉首尾空格
		while ((line = in.readLine()) != null) {
			if (!"".equals(line.trim())) {
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
	 * 解析文件（xls格式）
	 * 
	 * @param fileName
	 *            文件名
	 * @return
	 * @throws IOException
	 * @throws BiffException
	 * @throws
	 */
	public List<String> getImportDataByXLS(String fileName)
			throws BiffException, IOException {
		logger.debug("getImportDataByXLS{}", fileName);
		List<String> list = new ArrayList<String>();
		File f = new File(getFilePath() + fileName);
		Workbook wwb = Workbook.getWorkbook(f);
		;
		Sheet ws = null;
		// 总sheet数
		// int sheetNumber = wwb.getNumberOfSheets();
		int sheetNumber = 1;
		for (int m = 0; m < sheetNumber; m++) {
			ws = wwb.getSheet(m);
			// 当前页总记录行数和列数
			int rowCount = ws.getRows();
			// int columeCount = ws.getColumns();
			if (null != ws.getCell(0, 0).getContents()) {
				String line = ws.getCell(0, 0).getContents().trim();
				/**
				 * 判断导入文件查询的条件，默认为用户账号，当匹配到设备序列号时， 则以设备序列号作为条件，否则一律以用户账号作为条件
				 */
				if (null != line && "设备序列号".equals(line)) {
					this.importQueryField = "device_serialnumber";
				} else if (null != line && "宽带账号".equals(line)) {
					this.importQueryField = "kdusername";
				} else {
					this.importQueryField = "username";
				}
			}
			// 取当前页所有值放入list中
			for (int i = 1; i < rowCount; i++) {
				String temp = ws.getCell(0, i).getContents();
				if (null != temp && !"".equals(temp)) {
					if (!"".equals(ws.getCell(0, i).getContents().trim())) {
						list.add(ws.getCell(0, i).getContents().trim());
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
	public String getFilePath() {
		// 获取系统的绝对路径
		String lipossHome = "";
		String a = FileUploadAction.class.getResource("/").getPath();
		try {
			lipossHome = java.net.URLDecoder.decode(a.substring(0, a.lastIndexOf("WEB-INF") - 1), "UTF-8");
		} catch (Exception e) {
			logger.error(e.getMessage());
			lipossHome = null;
		}
		logger.debug("{}待解析的文件路径", lipossHome);
		return lipossHome + "/temp/";
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
	public int getImportDataByTXT4JX(String fileName)
			throws FileNotFoundException, IOException {
		logger.warn("getImportDataByTXT4JX:{}", fileName);
		int count = 0;
		BufferedReader in = new BufferedReader(new FileReader(getFilePath() + fileName));
		String line = in.readLine();
		
		// 从第二行开始读取数据，并对每行数据去掉首尾空格
		while ((line = in.readLine()) != null) {
			if (!"".equals(line.trim())) {
				count++;
			}
		}
		in.close();
		in = null;
		
		return count;
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
	public int getImportDataByXLS4JX(String fileName) throws BiffException, IOException {
		logger.debug("getImportDataByXLS4JX{}", fileName);
		int count = 0;
		File f = new File(getFilePath() + fileName);
		Workbook wwb = Workbook.getWorkbook(f);
		Sheet ws = null;
		int sheetNumber = 1;
		for (int m = 0; m < sheetNumber; m++) {
			ws = wwb.getSheet(m);
			int rowCount = ws.getRows();
			for (int i = 1; i < rowCount; i++) {
				String temp = ws.getCell(0, i).getContents();
				if (null != temp && !"".equals(temp)) {
					if (!"".equals(ws.getCell(0, i).getContents().trim())) {
						count++;
					}
				}
			}
		}
		return count;
	}

	
	/**
	 * 获取今天总数
	 */
	public long getTodayCount() { 
		return dao.getTodayCount();		
	}
	
	
	public String addTask4NX(long taskid, long accoid, String deviceIds, String param, String strategy_type, String type){
		try {
			logger.warn("StackRefreshToolsBIO.addTask4NX()");
			
			String serviceId = "";
			if ("1".equals(type)) {
				serviceId = SERVICE_ID_SINGLE_STACK;
			} else if ("2".equals(type)) {
				serviceId = SERVICE_ID_DOUBLE_STACK;
			} 
			
			List<String> netAccounts = new ArrayList<String>();
			
			if(StringUtil.IsEmpty(deviceIds) || "0".equals(deviceIds)){
				String sqlSpell = null;
				if(!StringUtil.IsEmpty(param)){
					String[] paramArr = param.split("\\|");
					if(null!=paramArr &&  paramArr.length>=11){
						sqlSpell = paramArr[10];
					}
				}
				
				if(StringUtil.IsEmpty(sqlSpell)){
					logger.warn("==[{}]设备为空，查询sql为空，程序结束==", taskid);
					return "false";
				}
				
				if ((LipossGlobals.inArea(Global.NMGDX) || LipossGlobals.inArea(Global.SDDX) || LipossGlobals.inArea(Global.AHDX)
						|| LipossGlobals.inArea(Global.HBDX) || LipossGlobals.inArea(Global.SXLT) || LipossGlobals.inArea(Global.HBLT)
						|| LipossGlobals.inArea(Global.GSDX) ||LipossGlobals.inArea(Global.SXDX) || LipossGlobals.inArea(Global.ZJLT)) && sqlSpell.contains("["))
				{
					sqlSpell = sqlSpell.replaceAll("\\[", "\\'");
				}
				
				ArrayList<HashMap<String, String>> devList = dao.getDevIds4NX(sqlSpell);
				if(null==devList || devList.size()==0){
					logger.warn("==[{}]设备为空，程序结束==", taskid);
					return "false";
				}
				
				int num = dao.addTask4NX(taskid, accoid, sqlSpell, 2, serviceId, strategy_type);
				if(num>0){
					return "true";
				}else{
					return "false";
				}
			}
			else
			{
			
				String[] deviceIdsArr = null==deviceIds ? null : deviceIds.split(",");
				if(null==deviceIdsArr || 0==deviceIdsArr.length){
					logger.warn("==[{}]设备为空，程序结束==", taskid);
					return "false";
				}
			
				for(String deviceId : deviceIdsArr){
					if(!StringUtil.IsEmpty(deviceId)){
						netAccounts.add(deviceId);
					}
				}
			}
			
			if(null==netAccounts || netAccounts.size()==0){
				logger.warn("==[{}]设备查询结果为空，程序结束==", taskid);
				return "false";
			}
			// 批量插入设备ID到临时表
			String filenameTmp = taskid + "";
			dao.insertTmp(filenameTmp, netAccounts);
			// 根据设备序列号获取设备信息
			ArrayList<HashMap<String, String>> devList = dao.getTaskDevList4NX(filenameTmp);
			List<HashMap<String, String>> devListNew = new ArrayList<HashMap<String, String>>();
			
			if(null!=devList && devList.size()>0){
				for(HashMap<String, String> map : devList)
				{
					String aDeviceId = StringUtil.getStringValue(map, "a_device_id", "");
					
					if(StringUtil.IsEmpty(map.get("device_id")))
					{
						logger.warn("taskid[{}]-aDeviceId[{}]没有查到设备", taskid, aDeviceId);
					}
					else if(StringUtil.IsEmpty(map.get("wan_type")))
					{
						logger.warn("taskid[{}]-aDeviceId[{}]没有查到上网方式", taskid, aDeviceId);
					}
					else if(!"1".equals(map.get("wan_type")) && !"2".equals(map.get("wan_type")) && !"5".equals(map.get("wan_type"))) //5是桥接+路由，目前仅甘肃有
					{
						logger.warn("taskid[{}]-aDeviceId[{}]既不是路由也不是桥接", taskid, aDeviceId);
					}
					else if(StringUtil.IsEmpty(map.get("username")))
					{
						logger.warn("taskid[{}]-aDeviceId[{}]没有查到loid", taskid, aDeviceId);
					}
					else{
						devListNew.add(map);
					}
				}
				if(null==devListNew || devListNew.size()==0)
				{
					logger.warn("taskid[{}] devListNew is null", taskid);
					return "false";
				}
			}else{
				logger.warn("taskid[{}] devList is null", taskid);
				return "false";
			}
			
			logger.warn("taskid[{}]-devListNew.size[{}]", taskid, devListNew.size());
			//陕西电信要求5万限制
			if (LipossGlobals.inArea(Global.SXDX) || LipossGlobals.inArea(Global.ZJLT)) {
				if(devListNew.size()>50000){
					logger.warn("taskid[{}] 定制设备超过50000条，程序结束==", taskid);
					return "false50000";
				}
			}else {
				if(devListNew.size()>10000){
					logger.warn("taskid[{}] 定制设备超过10000条，程序结束==", taskid);
					return "false10000";
				}
			}
			// 获取sqlList
			ArrayList<String> sqlList = dao.sqlList(devListNew, taskid, serviceId);
			ArrayList<String> sqlListTmp = new ArrayList<String>();
			int count = 0;
			
			// 批量插入设备信息
			if(null!=sqlList && sqlList.size()>0){
				for(String sql : sqlList){
					sqlListTmp.add(sql);
					if(sqlListTmp.size()>=200){
						int res = dao.insertTaskDev4NX(sqlListTmp);
						if(res>0){
							count += sqlListTmp.size();
						}
						sqlListTmp.clear();
					}
				}
			}
			if(sqlListTmp.size()>0){
				int res = dao.insertTaskDev4NX(sqlListTmp);
				if(res>0){
					count += sqlListTmp.size();
				}
				sqlListTmp.clear();
			}
			
			logger.warn("==[{}]插入tab_stack_task_dev表[{}]条数据==", taskid, count/3);
			
			int num = dao.addTask4NX(taskid, accoid, "", 1, serviceId, strategy_type);
			
			List<String> devIdList = new ArrayList<String>();
			for(Map<String,String> map : devListNew){
				devIdList.add(StringUtil.getStringValue(map,"device_id",""));
			}
			
			
			if(num>0){
				
				if (devIdList.size()>10000) {
					
					int listsize = (null == devIdList) ? 0 : devIdList.size();// List总记录条数，做非空判断避免程序出现异常
					int perpagesize = 10000;// 每个子List存放的记录条数
					int sumpagenumber = listsize / perpagesize;// 总共需要多少个子List，整数相除会直接舍去小数部分
					int lastListsize = listsize % perpagesize;// 最后一个List的size
					if (lastListsize != 0) {// 如果最后一个List的大小不为0，则所需的List个数需要加1
						sumpagenumber++;
					}
					for (int i = 0; i < sumpagenumber; i++) {
						int starnum = i * perpagesize;// 每个子List起始的位置
						List<String> optList = null;
						if (starnum + perpagesize < listsize) {
							optList = devIdList.subList(starnum, starnum + perpagesize);
						} else {
							optList = devIdList.subList(starnum, listsize);
						}
						String[] newdeviceIds=optList.toArray(new String[optList.size()]);
						logger.warn("每次总量："+newdeviceIds.length);
						int ret = CreateObjectFactory.createPreProcess("1").processDeviceStrategy(
								newdeviceIds, serviceId, new String[] { StringUtil.getStringValue(taskid) });
						logger.warn("调用配置模块进行配量参数配置结果为(ret={})", new Object[] { ret });
						if (1 != ret)
						{
							logger.warn("调用后台预读模块失败");
							return "false";
						}
					}
					logger.warn("调用后台预读模块成功");
					return "true";
				}else {
					String[] array = new String[devIdList.size()];
					if (1 == CreateObjectFactory.createPreProcess("1").processDeviceStrategy(
							devIdList.toArray(array), serviceId,
							new String[] { StringUtil.getStringValue(taskid) })) {
						logger.warn("调用后台预读模块成功");
						return "true";
					} else {
						logger.warn("调用后台预读模块失败");
						return "false";
					}
							
					/*		if (1 == CreateObjectFactory.createPreProcess("1").processDeviceStrategy(
									devIdList.toArray(array), serviceId,
									new String[] { StringUtil.getStringValue(taskid) })) {
								logger.warn("调用后台预读模块成功");
								return "true";
							} else {
								logger.warn("调用后台预读模块失败");
								return "false";
							}*/
					
				}		
				
			}else{
				return "false";
			}
			
		} catch (Exception e) {
			logger.warn("批量定制任务执行失败e[{}]", e);
			return "false";
		}
	}
	
	
	public String addTask4CQ(long taskid, long accoid, String deviceIds, String param, String strategy_type, String type, 
			String taskName, String task_wan_type, String paramPath, String paramValue, String paramType){
		try {
			logger.warn("StackRefreshToolsBIO.addTask4CQ()");
			
			String serviceId = "";
			if ("1".equals(type)) {
				serviceId = SERVICE_ID_SINGLE_STACK;
			} else if ("2".equals(type)) {
				serviceId = SERVICE_ID_DOUBLE_STACK;
			} 
			
			List<String> netAccounts = new ArrayList<String>();
			
			if(StringUtil.IsEmpty(deviceIds) || "0".equals(deviceIds)){
				String sqlSpell = null;
				if(!StringUtil.IsEmpty(param)){
					String[] paramArr = param.split("\\|");
					if(null!=paramArr &&  paramArr.length>=11){
						sqlSpell = paramArr[10];
					}
				}
				
				if(StringUtil.IsEmpty(sqlSpell)){
					logger.warn("==[{}]设备为空，查询sql为空，程序结束==", taskid);
					return "false";
				}
				
				if((LipossGlobals.inArea(Global.CQDX) || LipossGlobals.inArea(Global.HBLT) || LipossGlobals.inArea(Global.SXLT)) && sqlSpell.contains("[")){
					sqlSpell = sqlSpell.replaceAll("\\[", "\\'");
				}
				
				ArrayList<HashMap<String, String>> devList = dao.getDevIds4NX(sqlSpell);
				if(null==devList || devList.size()==0){
					logger.warn("==[{}]设备为空，程序结束==", taskid);
					return "false";
				}
				
				int num = dao.addTask4CQ(taskid, accoid, sqlSpell, 2, serviceId, strategy_type,
						taskName, removeStr(task_wan_type), removeStr(paramPath), removeStr(paramValue), removeStr(paramType));
				if(num>0){
					return "true";
				}else{
					return "false";
				}
			}
			else
			{
			
				String[] deviceIdsArr = null==deviceIds ? null : deviceIds.split(",");
				if(null==deviceIdsArr || 0==deviceIdsArr.length){
					logger.warn("==[{}]设备为空，程序结束==", taskid);
					return "false";
				}
			
				for(String deviceId : deviceIdsArr){
					if(!StringUtil.IsEmpty(deviceId)){
						netAccounts.add(deviceId);
					}
				}
			}
			
			if(null==netAccounts || netAccounts.size()==0){
				logger.warn("==[{}]设备查询结果为空，程序结束==", taskid);
				return "false";
			}
			// 批量插入设备ID到临时表
			String filenameTmp = taskid + "";
			dao.insertTmp(filenameTmp, netAccounts);
			// 根据设备序列号获取设备信息
			ArrayList<HashMap<String, String>> devList = dao.getTaskDevList4NX(filenameTmp);
			List<HashMap<String, String>> devListNew = new ArrayList<HashMap<String, String>>();
			
			if(null!=devList && devList.size()>0){
				for(HashMap<String, String> map : devList)
				{
					String aDeviceId = StringUtil.getStringValue(map, "a_device_id", "");
					
					if(StringUtil.IsEmpty(map.get("device_id")))
					{
						logger.warn("taskid[{}]-aDeviceId[{}]没有查到设备", taskid, aDeviceId);
					}
					else if(StringUtil.IsEmpty(map.get("wan_type")))
					{
						logger.warn("taskid[{}]-aDeviceId[{}]没有查到上网方式", taskid, aDeviceId);
					}
					else if(!"1".equals(map.get("wan_type")) && !"2".equals(map.get("wan_type")))
					{
						logger.warn("taskid[{}]-aDeviceId[{}]既不是路由也不是桥接", taskid, aDeviceId);
					}
					else if(StringUtil.IsEmpty(map.get("username")))
					{
						logger.warn("taskid[{}]-aDeviceId[{}]没有查到loid", taskid, aDeviceId);
					}
					else{
						devListNew.add(map);
					}
				}
				if(null==devListNew || devListNew.size()==0)
				{
					logger.warn("taskid[{}] devListNew is null", taskid);
					return "false";
				}
			}else{
				logger.warn("taskid[{}] devList is null", taskid);
				return "false";
			}
			
			logger.warn("taskid[{}]-devListNew.size[{}]", taskid, devListNew.size());
			if(devListNew.size()>10000){
				logger.warn("taskid[{}] 定制设备超过10000条，程序结束==", taskid);
				return "false10000";
			}
			// 获取sqlList
			ArrayList<String> sqlList = dao.sqlList4CQ(devListNew, taskid, serviceId);
			ArrayList<String> sqlListTmp = new ArrayList<String>();
			int count = 0;
			
			// 批量插入设备信息
			if(null!=sqlList && sqlList.size()>0){
				for(String sql : sqlList){
					sqlListTmp.add(sql);
					if(sqlListTmp.size()>=200){
						int res = dao.insertTaskDev4NX(sqlListTmp);
						if(res>0){
							count += sqlListTmp.size();
						}
						sqlListTmp.clear();
					}
				}
			}
			if(sqlListTmp.size()>0){
				int res = dao.insertTaskDev4NX(sqlListTmp);
				if(res>0){
					count += sqlListTmp.size();
				}
				sqlListTmp.clear();
			}
			
			logger.warn("==[{}]插入tab_stack_task_dev表[{}]条数据==", taskid, count);
			
			int num = dao.addTask4CQ(taskid, accoid, "", 1, serviceId, strategy_type,
					taskName, removeStr(task_wan_type), removeStr(paramPath), removeStr(paramValue), removeStr(paramType));
			
			List<String> devIdList = new ArrayList<String>();
			for(Map<String,String> map : devListNew){
				devIdList.add(StringUtil.getStringValue(map,"device_id",""));
			}
			
			String[] array = new String[devIdList.size()];
			
			if(num>0){
				
//				PreProcessCorba ppc = new PreProcessCorba("1");
//				if (1 == ppc.processDeviceStrategy(new String[]{"deviceIds"}, serviceId,
//							new String[] { StringUtil.getStringValue(taskid) })) {
//					logger.warn("调用后台预读模块成功");
//					return "true";
//				} else {
//					logger.warn("调用后台预读模块失败");
//					return "false";
//				}
						
				if (1 == CreateObjectFactory.createPreProcess("1").processDeviceStrategy(
						devIdList.toArray(array), serviceId,
						new String[] { StringUtil.getStringValue(taskid) })) {
					logger.warn("调用后台预读模块成功");
					return "true";
				} else {
					logger.warn("调用后台预读模块失败");
					return "false";
				}
				
			}else{
				return "false";
			}
			
		} catch (Exception e) {
			logger.warn("批量定制任务执行失败e[{}]", e);
			return "false";
		}
	}
	
	/**
	 * 查询任务列表
	 */
	public List<Map> queryTaskList(String task_name_query, String status_query,
			String expire_time_start, String expire_time_end,int curPage_splitPage,
			int num_splitPage){
		int status = -1;
		if(!StringUtil.IsEmpty(status_query)){
			status = Integer.valueOf(status_query);
		}
		long expireTimeStart;
		long expireTimeEnd;
		if(null==expire_time_start||"".equals(expire_time_start)){
			expireTimeStart = -1;
		}else{
			expireTimeStart = dealTime(expire_time_start);
		}
		if(null==expire_time_end||"".equals(expire_time_end)){
			expireTimeEnd = -1;
		}else{
			expireTimeEnd = dealTime(expire_time_end);
		}
		
		return dao.queryTaskList4CQ(task_name_query, status, expireTimeStart, expireTimeEnd, curPage_splitPage, num_splitPage);
	}
	
	public int queryTaskCount(String task_name_query, String status_query,
			String expire_time_start, String expire_time_end, int curPage_splitPage,
			int num_splitPage) {
		int status = -1;
		long expireTimeStart;
		long expireTimeEnd;
		if(!StringUtil.IsEmpty(status_query)){
			status = Integer.valueOf(status_query);
		}
		if(null==expire_time_start||"".equals(expire_time_start)){
			expireTimeStart = -1;
		}else{
			expireTimeStart = dealTime(expire_time_start);
		}
		if(null==expire_time_end||"".equals(expire_time_end)){
			expireTimeEnd = -1;
		}else{
			expireTimeEnd = dealTime(expire_time_end);
		}
		return dao.queryTaskCount4CQ(task_name_query, status, expireTimeStart, expireTimeEnd, curPage_splitPage, num_splitPage);
	}
	
	public List<Map> queryTaskGyCityList(String task_id,String task_name, int curPage_splitPage, int num_splitPage){
		return dao.queryTaskGyCityList(task_id, task_name, curPage_splitPage, num_splitPage);
	}
	
	public List<Map> queryTaskGyCityList(String task_id,String task_name){
	return dao.queryTaskGyCityList(task_id, task_name);
	}
	
	public int queryTaskGyCityCount(String task_id,String task_name, int curPage_splitPage, int num_splitPage){
		int total =  dao.queryTaskGyCityCount(task_id);
		int maxPage = 1;
		if (total % num_splitPage == 0) {
			maxPage = total / num_splitPage;
		} else {
			maxPage = total / num_splitPage + 1;
		}
		return maxPage;
	}
	
	/**
	 * 根据条件查询设备列表
	 * @param task_id 任务id
	 * @param city_id 属地id
	 * @param type 
	 * @param curPage_splitPage
	 * @param num_splitPage
	 * @return
	 */
	public List<Map> queryDevList4CQ(String task_id, String city_id, String type, int curPage_splitPage, int num_splitPage){
		return dao.queryDevList4CQ(task_id, city_id, type, curPage_splitPage, num_splitPage);
	}
	
	/**
	 * 设备列表最大页数
	 * @param countNum
	 * @param curPage_splitPage
	 * @param num_splitPage
	 * @return
	 */
	public int queryDevCount4CQ(String countNum, int curPage_splitPage, int num_splitPage)
	{
		int total = StringUtil.getIntegerValue(countNum);
		int maxPage = 1;
		if (total % num_splitPage == 0) {
			maxPage = total / num_splitPage;
		} else {
			maxPage = total / num_splitPage + 1;
		}
		return maxPage;
	}
	
	public String update(String task_id,String type){
		String res = dao.update(task_id,type)+"";
		return res;
	}
	
	public int updateCount(String task_id,String type){
		return dao.updateCount(task_id,type);
	}
	
	public String del(String task_id){
		return(dao.del(task_id)+"");
	}
	
	public int delCount(String task_id){
		return(dao.delCount(task_id));
	}
	
	public long dealTime(String time) {
		SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date str = new Date();
		try {
			str = date.parse(time);
		}
		catch (ParseException e) {
			logger.warn("选择开始或者结束的时间格式不对:" + time);
		}

		return str.getTime() / 1000L;
	}
	
	
	public String removeStr(String str)
	{
		if(!StringUtil.IsEmpty(str))
		{
			if(str.startsWith(","))
			{
				str = str.substring(1,str.length());
			}
			
			if(str.endsWith(","))
			{
				str = str.substring(0,str.length()-1);
			}
		}
		return str;
	}

	public StackRefreshToolsDAO getDao() {
		return dao;
	}

	public void setDao(StackRefreshToolsDAO dao) {
		this.dao = dao;
	}

	public static Logger getLogger() {
		return logger;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

}
