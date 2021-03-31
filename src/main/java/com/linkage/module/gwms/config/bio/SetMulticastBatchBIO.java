package com.linkage.module.gwms.config.bio;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
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
import com.linkage.litms.system.UserRes;
import com.linkage.module.gwms.config.dao.SetMulticastBatchDAO;
import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.module.gwms.share.act.FileUploadAction;
import com.linkage.module.gwms.util.corba.PreProcessCorba;

public class SetMulticastBatchBIO {
	
	private SetMulticastBatchDAO dao;
	
	/** 设置组播Multicast service_id */
	public static String SERVICE_ID_Set_Multicast = "2401";

	private static Logger logger = LoggerFactory.getLogger(SetMulticastBatchBIO.class);
	// 回传消息
	private String msg = null;
	// 查询条件
	private String importQueryField = "username";

	public static boolean flag = true;
	
	public String doConfig(UserRes curUser, List<String> list, String type, String faultList, String taskname) {
		logger.warn("SetMulticastBatchBIO.doConfig()");
		
		long time = new DateTimeUtil().getLongTime(); // 入表时间，同时为任务id
		try {
			int res = dao.doConfig(curUser.getUser().getId(), list, SERVICE_ID_Set_Multicast, time, faultList, taskname);
			if(res>=0){
				msg = "1";
				
				if (res>0) {
					PreProcessCorba ppc = new PreProcessCorba("1");
				      if (1 == ppc.processDeviceStrategy(new String[]{"deviceIds"}, SERVICE_ID_Set_Multicast, new String[] { StringUtil.getStringValue(Long.valueOf(time)) })) {
				        logger.warn("调用后台预读模块成功");
				        this.msg = "1";
				      } else {
				        logger.warn("调用后台预读模块失败");
				        this.msg = "-4";
				      }
					
//					if (1 == CreateObjectFactory.createPreProcess("1").processDeviceStrategy(
//							list.toArray(array), serviceId,
//							new String[] { StringUtil.getStringValue(time) })) {
//						logger.warn("调用后台预读模块成功");
//						msg = "1";
//					} else {
//						logger.warn("调用后台预读模块失败");
//						msg = "-4";
//					}
				}
			}
		} catch (Exception e) {
			logger.warn("更新表失败");
			msg = "-4";
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
	 * @return
	 */
	public List<HashMap<String,String>> getDeviceList(UserRes curUser,
			String cityId, String fileName) {
		logger.warn("getDeviceList({},{},{})", new Object[] { cityId, fileName });
		
		String faultList = "";
		
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
		} else if (dataList.size() > 3000)
		{
			this.msg = "文件行数不要超过3000行";
			return null;
		} else {
			long todayCount = dao.getTodayCount();
			if (100000 < todayCount) {
				this.msg = "今日执行用户数已满，不能超过10万个";
				return null;
			}
			dao.insertTmp(fileName, dataList, importQueryField);
			if ("username".equals(importQueryField) || "device_serialnumber".equals(importQueryField)) {
				list = dao.queryDeviceByImportUsername(fileName, importQueryField);
				if (!StringUtil.IsEmpty(cityId) && !"00".equals(cityId)) {
					cityArray = CityDAO.getAllNextCityIdsByCityPid(cityId);
				}
				
				List<String> listQCF = new ArrayList<String>();
				
				if (null != list && list.size() > 0) {
					String retMessage = "";
					for (HashMap<String, String> map : list) {
						String fileUsername = StringUtil.getStringValue(map, "file_username", "");
						String loid = StringUtil.getStringValue(map, "loid", "");
						String cityIdTmp = StringUtil.getStringValue(map, "city_id", "");
						String deviceId = StringUtil.getStringValue(map, "device_id", "");
//						String netUsername = StringUtil.getStringValue(map, "net_username", "");
						
						if (StringUtil.IsEmpty(fileUsername)) {
							continue;
						}
						
						if(!listQCF.contains(deviceId)){
							listQCF.add(deviceId);
						}else{
							logger.warn("[{}]设备重复，跳过", deviceId);
							continue;
						}
						
						if (StringUtil.IsEmpty(cityIdTmp)) {
							retMessage = "设备表中设备属地为空";
							faultList += fileUsername+"##"+retMessage + ";";
						} else {
							if (null != cityArray && cityArray.size() > 0 && !cityArray.contains(cityIdTmp)) {
								retMessage = "没有操作该设备的权限";
								faultList += fileUsername+"##"+retMessage + ";";
							} else {
								if (StringUtil.IsEmpty(deviceId)) {
									retMessage = "没有查到设备";
									faultList += fileUsername+"##"+retMessage + ";";
								}else{
									map.put("city_name",StringUtil.getStringValue(cityMap, StringUtil.getStringValue(map, "city_id", ""), ""));
									listNew.add(map);
								}
							}
						}

							
//						if (StringUtil.IsEmpty(loid)) {
//							retMessage = "用户表中没有查询结果";
//							faultList += fileUsername+"##"+retMessage + ";";
//						} else {
//							if (StringUtil.IsEmpty(cityIdTmp)) {
//								retMessage = "用户表中用户属地为空";
//								faultList += fileUsername+"##"+retMessage + ";";
//							} else {
//								if (null != cityArray && cityArray.size() > 0 && !cityArray.contains(cityIdTmp)) {
//									retMessage = "没有操作该用户的权限";
//									faultList += fileUsername+"##"+retMessage + ";";
//								} else {
//									if (StringUtil.IsEmpty(deviceId)) {
//										retMessage = "用户没有绑定设备";
//										faultList += fileUsername+"##"+retMessage + ";";
//									}else{
//										if (StringUtil.IsEmpty(netUsername)) {
//											retMessage = "没有查到iptv账号";
//											faultList += fileUsername+"##"+retMessage + ";";
//										} else {
//											map.put("city_name",StringUtil.getStringValue(cityMap, StringUtil.getStringValue(map, "city_id", ""), ""));
//											listNew.add(map);
//										}
//									}
//								}
//							}
//						}
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

	
	public List getOrderTaskList(int curPage_splitPage, int num_splitPage,
			String startTime, String endTime, String taskname,long acc_oid,String accName) {
		
		return dao.getOrderTaskList(curPage_splitPage, num_splitPage,
				 startTime, endTime, taskname,acc_oid,accName);
	}
	
	public int countOrderTask(int curPage_splitPage, int num_splitPage,
			String startTime, String endTime, String taskname,long acc_oid,String accName) {
		return dao.countOrderTask(curPage_splitPage, num_splitPage,
				 startTime, endTime, taskname,acc_oid,accName);
	}
	
	public List<Map> getTaskResult(String taskId ,int curPage_splitPage,int num_splitPage) {
		return dao.getTaskResult(taskId,curPage_splitPage, num_splitPage);
	}
	
	public int countDeviceTask(int curPage_splitPage, int num_splitPage,String taskId) {
		return dao.countTaskResult(curPage_splitPage, num_splitPage, taskId);
	}
	
	public Map<String, String> getTaskDetail(String taskId) {
		return dao.getTaskDetail(taskId);
	}
	
	public String doDelete(String taskId) {
		return dao.doDelete(taskId);
	}
	
	/**
	 * @author wangsenbo
	 * @date Nov 18, 2009
	 * @return List<Map>
	 */
	public List<Map> getInfoExcelDevice(String taskId)
	{
		return dao.getInfoExcelDevice(taskId);
	}
	
	public String addTask4NX(String taskname, long taskid, long accoid, String deviceIds, String param, String strategy_type){
		try {
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
					logger.warn("==[{}][{}]设备为空，查询sql为空，程序结束==", taskid, taskname);
					return "false";
				}
				
				ArrayList<HashMap<String, String>> devList = dao.getDevIds4NX(sqlSpell);
				if(null==devList || devList.size()==0){
					logger.warn("==[{}][{}]设备为空，程序结束==", taskid, taskname);
					return "false";
				}
				
				int num = dao.addTask4NX(taskname, taskid, accoid, sqlSpell, 2, SERVICE_ID_Set_Multicast, strategy_type);
				if(num>0){
//					String[] array = new String[devList.size()];
//					PreProcessCorba ppc = new PreProcessCorba("1");
//					if (1 == ppc.processDeviceStrategy((String[])devList.toArray(array), SERVICE_ID_Set_Multicast,
//								new String[] { StringUtil.getStringValue(taskid) })) {
//						logger.warn("调用后台预读模块成功");
//						return "true";
//					} else {
//						logger.warn("调用后台预读模块失败");
//						return "false";
//					}
//							
////							if (1 == CreateObjectFactory.createPreProcess("1").processDeviceStrategy(
////									list.toArray(array), serviceId,
////									new String[] { StringUtil.getStringValue(time) })) {
////								logger.warn("调用后台预读模块成功");
////								msg = "1";
////							} else {
////								logger.warn("调用后台预读模块失败");
////								msg = "-4";
////							}
					
					return "true";
				}else{
					return "false";
				}
			}
			else
			{
			
				String[] deviceIdsArr = null==deviceIds ? null : deviceIds.split(",");
				if(null==deviceIdsArr || 0==deviceIdsArr.length){
					logger.warn("==[{}][{}]设备为空，程序结束==", taskid, taskname);
					return "false";
				}
			
				for(String deviceId : deviceIdsArr){
					if(!StringUtil.IsEmpty(deviceId)){
						netAccounts.add(deviceId);
					}
				}
			}
			
			if(null==netAccounts || netAccounts.size()==0){
				logger.warn("==[{}][{}]设备查询结果为空，程序结束==", taskid, taskname);
				return "false";
			}
			// 批量插入设备ID到临时表
			String filenameTmp = taskid+"-"+taskname;
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
//					else if(StringUtil.IsEmpty(map.get("wan_type")))
//					{
//						logger.warn("taskid[{}]-aDeviceId[{}]没有查到上网方式", taskid, aDeviceId);
//					}
//					else if(!"1".equals(map.get("wan_type")) && !"2".equals(map.get("wan_type")))
//					{
//						logger.warn("taskid[{}]-aDeviceId[{}]既不是路由也不是桥接", taskid, aDeviceId);
//					}
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
			ArrayList<String> sqlList = dao.sqlList(devListNew, taskname, taskid);
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
			
			logger.warn("==[{}]插入tab_ids_task表[{}]条数据==", taskid, count);
			int num = dao.addTask4NX(taskname, taskid, accoid, "", 1, SERVICE_ID_Set_Multicast, strategy_type);
			if(num>0){
				
				
				PreProcessCorba ppc = new PreProcessCorba("1");
				if (1 == ppc.processDeviceStrategy(new String[]{"deviceIds"}, SERVICE_ID_Set_Multicast,
							new String[] { StringUtil.getStringValue(taskid) })) {
					logger.warn("调用后台预读模块成功");
					return "true";
				} else {
					logger.warn("调用后台预读模块失败");
					return "false";
				}
						
//						if (1 == CreateObjectFactory.createPreProcess("1").processDeviceStrategy(
//								list.toArray(array), serviceId,
//								new String[] { StringUtil.getStringValue(time) })) {
//							logger.warn("调用后台预读模块成功");
//							msg = "1";
//						} else {
//							logger.warn("调用后台预读模块失败");
//							msg = "-4";
//						}
				
			}else{
				return "false";
			}
			
		} catch (Exception e) {
			logger.warn("批量定制任务执行失败e[{}]", e);
			return "false";
		}
	}
	
	/**
	 * 获取今天总数
	 */
	public long getTodayCount() { 
		return dao.getTodayCount();		
	}
	
	public List<Map> queryModifyVlanInfo(String type, String cityId) {
		logger.warn("ModifyVlanCountBIO--queryModifyVlanInfo(),type:{},cityID:{}", type, cityId);
		List date = this.dao.queryModifyVlanInfo(getService(type));
		if ((date == null) || (date.isEmpty())) {
			return new ArrayList();
		}
		Map cityMap = CityDAO.getCityIdCityNameMap();
		ArrayList cityList = CityDAO.getNextCityIdsByCityPid(cityId);
		Collections.sort(cityList);
		List list = new ArrayList();
		for (int i = 0; i < cityList.size(); i++) {
			long countAll = 0L;
			long failnum = 0L;
			long successnum = 0L;
			long noupnum = 0L;
			String city_id = (String) cityList.get(i);
			ArrayList tlist = CityDAO.getAllNextCityIdsByCityPid(city_id);
			Map tmap = new HashMap();
			tmap.put("", cityList);
			tmap.put("city_id", city_id);
			tmap.put("city_name", cityMap.get(city_id));
			for (int j = 0; j < tlist.size(); j++) {
				String cityId2 = (String) tlist.get(j);
				for (int k = 0; k < date.size(); k++) {
					Map rmap = (Map) date.get(k);
					if (!cityId2.equals(rmap.get("city_id")))
						continue;
					String status = StringUtil.getStringValue(rmap.get("status"));
					String result_id = StringUtil.getStringValue(rmap.get("result_id"));
					if (("100".equals(status)) && ("1".equals(result_id))) {
						successnum += StringUtil.getLongValue(rmap.get("total"));
					}
					else if (((!"".equals(status)) || (!"".equals(result_id)))
							&& ((!"100".equals(status)) || (!"1".equals(result_id)))) {
						failnum += StringUtil.getLongValue(rmap.get("total"));
					}
					else if (("".equals(status)) && ("".equals(result_id))) {
						noupnum += StringUtil.getLongValue(rmap.get("total"));
					}
					else {
						countAll += StringUtil.getLongValue(rmap.get("total"));
					}
				}
			}
			countAll = countAll + successnum + noupnum + failnum;
			tmap.put("allup", Long.valueOf(countAll));
			tmap.put("successnum", Long.valueOf(successnum));
			tmap.put("noupnum", Long.valueOf(noupnum));
			tmap.put("failnum", Long.valueOf(failnum));
			list.add(tmap);
			tlist = null;
		}
		return list;
	}

	public List<Map> getDev(String type, String status, String cityId, int curPage_splitPage, int num_splitPage) {
		return this.dao.getDevList(getService(type), status, cityId, curPage_splitPage, num_splitPage);
	}

	public int getDevCount(String type, String status, String cityId, int curPage_splitPage, int num_splitPage) {
		return this.dao.getDevCount(getService(type), status, cityId, curPage_splitPage, num_splitPage);
	}

	public List<Map> getDevExcel(String type, String cityId, String status) {
		return this.dao.getDevExcel(getService(type), status, cityId);
	}

	/**
	 * 查询单双栈刷新统计结果
	 * 
	 * @param type
	 * @param cityId3
	 * @param type2
	 * @return
	 */
	public List<Map> queryModifyVlanInfo(String starttime1, String endtime1, String type, String cityId) {
		logger.debug("ModifyVlanCountBIO--queryModifyVlanInfo(),starttime:{},endtime:{},type:{},cityID:{}",
				starttime1, endtime1, type, cityId);
		List<Map> date = dao.queryModifyVlanInfo(getService(type), starttime1, endtime1, cityId);
		if (null == date || date.isEmpty()) {
			return new ArrayList<Map>();
		}
		// 按属地统计
		Map cityMap = CityDAO.getCityIdCityNameMap();
		// 根据属地cityId获取下一层属地ID(包含自己)
		ArrayList<String> cityList = CityDAO.getNextCityIdsByCityPid(cityId);
		Collections.sort(cityList);
		// 最终结果的list
		List<Map> list = new ArrayList<Map>();
		// 遍历属地
		for (int i = 0; i < cityList.size(); i++) {
			// 计数
			// 总数
			long countAll = 0;
			// 失败总数
			long failnum = 0;
			// 成功总数
			long successnum = 0;
			// 未触发总数
			long noupnum = 0;
			String city_id = cityList.get(i);
			ArrayList<String> tlist = CityDAO.getAllNextCityIdsByCityPid(city_id);
			Map<String, Object> tmap = new HashMap<String, Object>();
			tmap.put("", cityList);
			tmap.put("city_id", city_id);
			tmap.put("city_name", cityMap.get(city_id));
			for (int j = 0; j < tlist.size(); j++) {
				String cityId2 = tlist.get(j);
				for (int k = 0; k < date.size(); k++) {
					Map rmap = (Map) date.get(k);
					if (cityId2.equals(rmap.get("city_id"))) {
						//
						String status = StringUtil.getStringValue(rmap.get("status"));
						String result_id = StringUtil.getStringValue(rmap.get("result_id"));
						// 判断成功0---100
						if ("100".equals(status) && "1".equals(result_id)) {
							successnum = successnum + StringUtil.getLongValue(rmap.get("total"));
						}
						// 判断失败非0---100
						else if ((!"".equals(status) || !"".equals(result_id))
								&& (!"100".equals(status) || !"1".equals(result_id))) {
							failnum = failnum + StringUtil.getLongValue(rmap.get("total"));
						}
						// 判断未作状态
						else if ("".equals(status) && "".equals(result_id)) {
							noupnum = noupnum + StringUtil.getLongValue(rmap.get("total"));
						}
						else {
							countAll = countAll + StringUtil.getLongValue(rmap.get("total"));
						}
					}
				}
			}
			countAll = countAll + successnum + noupnum + failnum;
			tmap.put("allup", countAll);
			tmap.put("successnum", successnum);
			tmap.put("noupnum", noupnum);
			tmap.put("failnum", failnum);
			list.add(tmap);
			tlist = null;
		}
		return list;
	}

	/**
	 * 详情页查询
	 * 
	 * @param type
	 * @param status
	 * @param cityId
	 * @param num_splitPage
	 * @param curPage_splitPage
	 * @param endtime1
	 * @param starttime1
	 * @return
	 */
	public List<Map> getDev(String type, String status, String cityId, int curPage_splitPage, int num_splitPage,
			String starttime1, String endtime1) {
		return dao.getDevList(getService(type), status, cityId, curPage_splitPage, num_splitPage, starttime1, endtime1);
	}

	public int getDevCount(String type, String status, String cityId, int curPage_splitPage, int num_splitPage,
			String starttime1, String endtime1) {
		return dao
				.getDevCount(getService(type), status, cityId, curPage_splitPage, num_splitPage, starttime1, endtime1);
	}

	public List<Map> getDevExcel(String type, String cityId, String status, String starttime1, String endtime1) {
		return dao.getDevExcel(getService(type), status, cityId, starttime1, endtime1);
	}

	private String getService(String type) {
		return SERVICE_ID_Set_Multicast;
	}
	

	public SetMulticastBatchDAO getDao() {
		return dao;
	}

	public void setDao(SetMulticastBatchDAO dao) {
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
