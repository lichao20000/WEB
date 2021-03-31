package com.linkage.module.gtms.stb.resource.action;

import action.splitpage.splitPageAction;
import com.linkage.commons.util.StringUtil;
import com.linkage.litms.LipossGlobals;
import com.linkage.litms.common.WebUtil;
import com.linkage.litms.system.UserRes;
import com.linkage.module.gtms.stb.resource.serv.GwDeviceQueryBIO;
import com.linkage.module.gtms.stb.resource.serv.SoftUpgradeBIO;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.module.gwms.util.CreateObjectFactory;
import org.apache.commons.io.FileUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.*;

/**
 * @author 王森博
 */
@SuppressWarnings({"rawtypes","unused","unchecked"})
public class SoftUpgradeACT extends splitPageAction implements SessionAware,ServletRequestAware
{
	private static final long serialVersionUID = 1L;
	private static Logger logger = LoggerFactory.getLogger(SoftUpgradeACT.class);
	private String deviceId;
	private String deviceIds;
	private String ajax;
	private SoftUpgradeBIO bio;
	private String pathId;
	private String strategyId;
	private Map servStrategyMap;
	private String isRefresh;
	/** 属地ID */
	private String cityId = "-1";
	/** 属地列表 */
	private List<Map<String, String>> cityList = null;
	private List vendorList;
	private String vendorId = "-1";
	private String devicetypeId;
	private String strategyType;
	private String message;
	private List tasklist;
	//山西联通导入升级任务
	private List importTasklist;
	private String taskId;
	private Map taskResultMap = null;
	private List taskResultMapList = null;
	private String accoid;
	private String areaId;
	private GwDeviceQueryBIO gwDeviceQueryBio;
	private String gw_type;
	private String goal_softwareversion;
	private String softStrategy_type;
	private String isImport;
	/**湖南联通特制 	传入参数showType；
	 * 1：展示查询信息，以及“查看结果”、“查看详细”；
	 * 2：在1的基础上增加“激活\失效”按钮权限；
	 * 3：在2的基础上增加“删除”按钮权限。*/
	private String showType="3";
	/**湖南联通特制  是否校验设备IP类型与网络类型*/
	private String check_net="0";
	
	private String source_devicetypeId;
	private String instArea=Global.instAreaShortName;
	/** 酒店 */
	private String isHotel;
	/** vip */
	private String isVIP;
	private String param;
	private String isOther;
	private String status;
	private List resultList;
	// 激活、失效、删除任务前需要输入用户名密码进行验证
	private String userName;
	private String password;
	private String softwareversion;
	private String versionPath;
	// 上传文件的处理开始
	private File upload;// 实际上传文件
	private String uploadFileName; // 上传文件名
	/** 实际上传文件 */
	private File uploadMAC;
	/** 上传MAC地址段文件名 */
	private String uploadFileName4MAC;
	/** 上传业务用户文件 */
	private File uploadCustomer;
	/** 上传业务用户文件名 */
	private String uploadFileName4Customer;

	private String vendorPathId;// 格式为：10\1323\2,以\1,\2分隔
	
	private InputStream exportExcelStream;

	private List upRecordList;

	private List checkIPList;
	
	private List checkMACList;
	// 查询开始时间
	private String startTime;
	// 查询结束时间
	private String endTime;
	// 业务帐号
	private String servAccount;
	// 查询类型
	private String queryType;
	// 任务备注
	private String taskDetail;
	// 任务描述
	private String taskDesc;
	// 查看结果时的结果
	private int flag;
	// 设备类型
	private String deviceModelIds;
	// 导出数据
	private String[] title;
	private String[] column;
	private String fileName = "";
	private List data = null;
	private String cituName = "";
	/**
	 * 策略名 add by zhangcong@ 2011-09-05 需求单:JSDX_ITV_HTW-REQ-20110824-XMN-003
	 */
	private String strategyName;
	private Map session;
	private HttpServletRequest request;
	private String queryCityId;
	private String queryVendorId;
	private String queryVaild;

	private String upResult;

	// IP地址段,格式是：192.1.1.1\1192.1.1.255\2,以\1和\2
	private String ipSG = "";

	private String ipCheck = "0";
	/** 是否启用MAC地址段 */
	private String macCheck = "0";

	private String custCheck = "";
	/**是否启用设备序列号校验**/
	private String devsnCheck = "";
	/**山西联通任务类型,0 ： 按属地升级，1：按厂商导入升级**/
	private String taskType = "";
	private String taskImportType = "";

	/** MAC地址段 */
	private String macSG = "";
	
	/** 手工输入的用户信息 */
	private String custSG = "";

	/** 导入or手输IP byBatchIP：导入 */
	private String btnValue4IP;
	/** 导入or手输MAC byBatchMAC 导入 */
	private String btnValue4MAC;
	/** 导入or手输MAC byBatchCustomer 导入 */
	private String btnValue4Customer;
	/** 多地市定制 */
	private String sxSoftCitys;
	

	public InputStream getExportExcelStream()
	{
		FileInputStream fio = null;
		String path = ServletActionContext.getServletContext().getRealPath("/");
		String separa = System.getProperty("file.separator");
		String filePath = path + "gtms" + path + "stb" + separa + "resource"
				+ separa + "batchUPtemplate.xls";
		try {
			fio = new FileInputStream(filePath);
		} catch (FileNotFoundException e) {
			logger.warn("读取文件异常", e);
		}
		return fio;
	}

	/**
	 * 获取设备可升级版本
	 */
	public String showSoftwareversion() 
	{
		ajax = bio.showSoftwareversion(deviceId);
		return "ajax";
	}
	
	/**
	 * 获取版本详细
	 */
	public String querySoftVersionDetail() 
	{
		ajax = bio.querySoftVersionDetail(taskId);
		return "ajax";
	}

	public String showALLSoftwareversion() 
	{
		ajax = bio.showAllSoftwareversion();
		return "ajax";
	}
	
	public String getUpgradeResult() 
	{
		resultList = bio.getUpgradeResult(deviceId);
		return "resultList";
	}
	
	public String softUpgrade() 
	{
		UserRes curUser = WebUtil.getCurrentUser();
		if (Global.HNLT.equals(instArea)) {
			ajax = bio.softUpgrade_hnlt(curUser.getUser().getId(), pathId,deviceId);
		}else{
			ajax = bio.softUpgrade(curUser.getUser().getId(), pathId, deviceId,
					goal_softwareversion, softStrategy_type);
		}
		logger.warn(ajax);
		return "ajax";
	}

	public String xjBatchSoftUpgrade() 
	{
		logger.warn(
				"xjBatchSoftUpgrade begin, deviceIds=[{}], goal_softwareversion=[{}],softStrategy_type=[{}],gw_type=[{}]",
				new Object[]{deviceIds, goal_softwareversion,softStrategy_type, gw_type});

		UserRes curUser = WebUtil.getCurrentUser();
		
		long taskid = Math.round(Math.random() * 100000L);
		if(Global.XJDX.equals(Global.instAreaShortName)){
			long accoid = curUser.getUser().getId();
			bio.xjbatchSoftUpgrade(taskid, vendorId, cityId, pathId, strategyType, accoid, devicetypeId);
			taskId = Long.valueOf(taskid).toString();
		}
		String[] deviceId_array;
		if (!"0".equals(deviceIds)) {
			deviceId_array = deviceIds.split(",");
			String[] paramArr;
			paramArr = new String[3];
			paramArr[0] = goal_softwareversion;
			paramArr[1] = softStrategy_type;
			paramArr[2] = taskId;
			CreateObjectFactory.createPreProcess(gw_type).processDeviceStrategy(deviceId_array,
					"5", paramArr);
			ajax = 1 + ";调用后台成功;" + deviceId;
			return "ajax";
		}

		List list = gwDeviceQueryBio.getDeviceList("4", curUser.getAreaId(),param);
		deviceId_array = new String[list.size()];
		for (int i = 0; i < list.size(); i++) {
			Map map = (Map) list.get(i);
			deviceId_array[i] = StringUtil.getStringValue(map.get("device_id"));
		}
		String[] paramArr;
		paramArr = new String[3];
		paramArr[0] = goal_softwareversion;
		paramArr[1] = softStrategy_type;
		paramArr[2] = taskId;
		CreateObjectFactory.createPreProcess(gw_type).processDeviceStrategy(
														deviceId_array, "5",paramArr);
		ajax = 1 + ";调用后台成功;" + deviceId;
		return "ajax";
	}
	
	/**
	 * 批量软件升级，新增了导入升级功能，直接入策略表
	 */
	public String batchSoftUpgradeNew()
	{
		logger.warn(
				"batchSoftUpgradeNew begin, deviceIds=[{}], goal_softwareversion=[{}],softStrategy_type=[{}],gw_type=[{}],taskDesc=[{}]",
				new Object[]{deviceIds, goal_softwareversion,softStrategy_type, gw_type,taskDesc});

		UserRes curUser = WebUtil.getCurrentUser();
		String[] deviceId_array;
		long accoid = curUser.getUser().getId();
		if (!"0".equals(deviceIds)) {
			deviceId_array = deviceIds.split(",");
		}else{
			List list = gwDeviceQueryBio.getDeviceList("4", curUser.getAreaId(),param);
			deviceId_array = new String[list.size()];
			for (int i = 0; i < list.size(); i++) {
				Map map = (Map) list.get(i);
				deviceId_array[i] = StringUtil.getStringValue(map.get("device_id"));
			}
		}
		logger.warn("softStrategy_type--"+softStrategy_type);
		bio.batchUp(accoid, deviceId_array, softStrategy_type,taskDesc);

//		List list = gwDeviceQueryBio.getDeviceList("4", curUser.getAreaId(),param);
//		deviceId_array = new String[list.size()];
//		for (int i = 0; i < list.size(); i++) {
//			Map map = (Map) list.get(i);
//			deviceId_array[i] = StringUtil.getStringValue(map.get("device_id"));
//		}
//		String[] paramArr;
//		paramArr = new String[2];
//		paramArr[0] = goal_softwareversion;
//		paramArr[1] = softStrategy_type;
//		CreateObjectFactory.createPreProcess(gw_type).processDeviceStrategy(deviceId_array, "5",
//				paramArr);
		ajax = 1 + ";调用后台成功;" + deviceId;
		return "ajax";
	}

	public String getStrategyById()
	{
		logger.debug("getStrategyById()");
		if (true == StringUtil.IsEmpty(strategyId)) {
			isRefresh = "0";
			servStrategyMap = null;
		} else {
			servStrategyMap = bio.getStrategyById(strategyId);
			if ("执行完成".equals(servStrategyMap.get("status"))) {
				isRefresh = "0";
			} else {
				isRefresh = "1";
			}
		}
		return "result";
	}

	/**
	 * 批量策略版本升级任务定制初始化页面
	 */
	public String init()
	{
		UserRes curUser = WebUtil.getCurrentUser();
		cityId = curUser.getCityId();
		cityList = CityDAO.getNextCityListByCityPid(cityId);
		vendorList = bio.getVendor();
		accoid = StringUtil.getStringValue(curUser.getUser().getId());
		areaId = StringUtil.getStringValue(curUser.getAreaId());
		if (null == queryCityId) {
			queryCityId = cityId;
		}
		
		tasklist = bio.getSoftupTask(curPage_splitPage, num_splitPage,queryCityId,queryVendorId,
				queryVaild,taskDesc,source_devicetypeId,softwareversion,startTime,endTime,taskType);
		maxPage_splitPage = bio.countSoftupTask(curPage_splitPage,num_splitPage,queryCityId,
				queryVendorId, queryVaild,taskDesc,source_devicetypeId,softwareversion,startTime,endTime,taskType);
		
		if(Global.NXDX.equals(instArea))
		{
			return "initnxdx";
		}
		if(Global.SXLT.equals(instArea))
		{
			if(!cityId.equals("00")){
				cituName = CityDAO.getCityIdCityNameMap().get(cityId);
				logger.warn("cituName");
			}
			return "initsxlt";
		}
		return "init";
	}
	
	public String initSXLTImport()
	{
		UserRes curUser = WebUtil.getCurrentUser();
		cityId = curUser.getCityId();
		cityList = CityDAO.getNextCityListByCityPid(cityId);
		vendorList = bio.getVendor();
		accoid = StringUtil.getStringValue(curUser.getUser().getId());
		areaId = StringUtil.getStringValue(curUser.getAreaId());
		if (null == queryCityId) {
			queryCityId = cityId;
		}
		
		tasklist = bio.getSoftupTask(curPage_splitPage, num_splitPage,queryCityId,queryVendorId,
				queryVaild,taskDesc,source_devicetypeId,softwareversion,startTime,endTime,taskType);
		maxPage_splitPage = bio.countSoftupTask(curPage_splitPage,num_splitPage,queryCityId,
				queryVendorId, queryVaild,taskDesc,source_devicetypeId,softwareversion,startTime,endTime,taskType);
		
		 
		if(Global.SXLT.equals(instArea))
		{
			if(!cityId.equals("00")){
				cituName = CityDAO.getCityIdCityNameMap().get(cityId);
				logger.warn("cituName");
			}
			return "initsxlt";
		}
		return "init";
	}
	
	public String add()
	{
		UserRes curUser = WebUtil.getCurrentUser();
		cityId = curUser.getCityId();
		cityList = CityDAO.getNextCityListByCityPid(cityId);
		vendorList = bio.getVendor();
		accoid = StringUtil.getStringValue(curUser.getUser().getId());
		areaId = StringUtil.getStringValue(curUser.getAreaId());
		return "add";
	}
	
	/**
	 * 批量策略版本升级任务定制初始化页面
	 */
	public String initForUpgradeTask()
	{
		UserRes curUser = WebUtil.getCurrentUser();
		cityId = curUser.getCityId();
		cityList = CityDAO.getNextCityListByCityPid(cityId);
		vendorList = bio.getVendor();
		accoid = StringUtil.getStringValue(curUser.getUser().getId());
		areaId = StringUtil.getStringValue(curUser.getAreaId());
		if (null == queryCityId) {
			queryCityId = cityId;
		}
		tasklist = bio.getSoftupTask(curPage_splitPage, num_splitPage,queryCityId,queryVendorId,
				queryVaild,taskDesc,source_devicetypeId,softwareversion,startTime,endTime,taskType);
		maxPage_splitPage = bio.countSoftupTask(curPage_splitPage,num_splitPage,queryCityId,
				queryVendorId, queryVaild,taskDesc,source_devicetypeId,softwareversion,startTime,endTime,taskType);
		return "initTask";
	}
	
	public String validateCurUser()
	{
		return "validateCurUser";
	}
	
	/**
	 * 根据厂商获取目标版本
	 */
	public String getTargetVersion() 
	{
		ajax = bio.getTargetVersion(vendorId);
		return "ajax";
	}

	/**
	 * 批量策略版本升级任务定制
	 */
	public String batchSoftUpgrade() 
	{
		UserRes curUser = WebUtil.getCurrentUser();
		message = bio.batchSoftUpgrade(vendorId, cityId, pathId, strategyType,
				curUser.getUser().getId(), devicetypeId, ipCheck, ipSG);
		return "message";
	}

	/**
	 * 批量策略版本升级任务定制
	 */
	public String batchSoftUpgradebak() 
	{
		UserRes curUser = WebUtil.getCurrentUser();
		if (btnValue4IP.equals("byBatchIP")) {
			ipSG = getIPString();
		}
		if (btnValue4MAC.equals("byBatchMAC")) {
			macSG = getMACString();
		}
		if (btnValue4Customer.equals("byBatchCust")) {
			custSG = "";
		}
		if(Global.HNLT.equals(instArea))
		{
			message = bio.batchSoftUpgradeHNLT(vendorId, cityId, pathId,
					strategyType, curUser.getUser().getId(), devicetypeId, ipCheck,
					ipSG, macCheck, macSG, custCheck, custSG, uploadCustomer,
					uploadFileName4Customer,check_net,taskDesc,taskDetail);
			long  user_id=((UserRes) session.get("curUser")).getUser().getId();
			String user_ip=request.getRemoteHost();
			String result = "0";
			if(message.contains("成功"))
			{
				result = "1";
			}
			bio.addLog(user_id,user_ip,"批量软件升级任务定制",result);
		}
		else
		{
			message = bio.batchSoftUpgradeBak(vendorId, cityId, pathId,
					strategyType, curUser.getUser().getId(), devicetypeId, ipCheck,
					ipSG, macCheck, macSG, custCheck, custSG, uploadCustomer,
					uploadFileName4Customer,check_net);
		}
		logger.warn("batchSoftUpgradebak message=" + message);
		return "message";
	}
	
	public String batchSoftUpgradeSxlt() 
	{
		UserRes curUser = WebUtil.getCurrentUser();
		if (btnValue4IP.equals("byBatchIP")) {
			ipSG = getIPString();
		}
		if (btnValue4MAC.equals("byBatchMAC")) {
			macSG = getMACString();
		}
		if (btnValue4Customer.equals("byBatchCust")) {
			custSG = "";
		}
		
		message = bio.batchSoftUpgradeBakForsxlt(taskType,taskId,vendorId, sxSoftCitys, pathId,
				strategyType, curUser.getUser().getId(), devicetypeId, ipCheck,
				ipSG, macCheck, macSG, custCheck, custSG, uploadCustomer,
				uploadFileName4Customer,check_net,devsnCheck);
		logger.warn("batchSoftUpgradebak message=" + message);
		return "message";
	}
	
	/**
	 * 批量策略版本升级任务定制
	 */
	public String batchSoftUpgradeForHblt() 
	{
		UserRes curUser = WebUtil.getCurrentUser();
		message = bio.batchSoftUpgrade2(vendorId, cityId, pathId, strategyType,
				curUser.getUser().getId(), devicetypeId, ipCheck, ipSG,taskDesc);
		return "message";
	}

	/**
	 * 根据目标版本获取适用版本
	 */
	public String checkVersionByTarget() 
	{
		ajax = bio.checkVersionByTarget(pathId);
		return "ajax";
	}

	public String getCountSoftupTaskResult() 
	{
		if(Global.HNLT.equals(instArea))
		{
			taskResultMapList = bio.getCountSoftupTaskResultHnlt(taskId,cityId);
			return "hnlttaskResult";
		}
	
		if(LipossGlobals.inArea(Global.SXLT) && "1".equals(taskType)){
			
			taskResultMap = bio.getCountSoftupTaskResult_v2(taskId,taskImportType);
		}else{
			taskResultMap = bio.getCountSoftupTaskResult(taskId);
		}
		
		if(LipossGlobals.inArea(Global.SXLT)){
			if("1".equals(taskType)){
				return "taskResultImport";
			}
		}
		return "taskResult";
	}
	

	/**
	 * 导出软件升级统计结果
	 */
	public String exportSoftupTaskResult()
	{
		logger.warn("exportSoftupTaskResult()");
		title = new String[]{"属地", "任务触发成功", "软件升级成功", "待触发", "不支持", "小计",
				"总占比量", "成功占比"};
		column = new String[]{"city_name", "updatesucc", "softupsucc",
				"updatefailed", "unsupport", "total",
				"totalPer", "succPer"};
		fileName = "软件升级统计结果";

		data = bio.getCountSoftupTaskResultHnlt(taskId, cityId);
		return "excel";
	}


	/**
	 * 取消任务
	 */
	public String cancerTask() 
	{
		ajax = bio.cancerTask(taskId,isImport);
		if(Global.HNLT.equals(instArea))
		{
			long  user_id=((UserRes) session.get("curUser")).getUser().getId();
			String user_ip=request.getRemoteHost();
			String result = "0";
			if(ajax.contains("成功"))
			{
				result = "1";
			}
			bio.addLog(user_id,user_ip,"批量软件升级任务失效",result);
		}
		return "ajax";
	}
	
	/**
	 * 激活、失效、删除任务前需要输入用户名密码验证
	 */
	public String validateUser() 
	{
		UserRes curUser = WebUtil.getCurrentUser();
		if (curUser.getUser().getAccount().equals(userName)
				&& curUser.getUser().getPasswd().equals(password)) {
			ajax = "1";
		} else {
			ajax = "0";
		}
		return "ajax";
	}
	
	/**
	 * 删除任务
	 */
	public String deleteTask() 
	{
		ajax = bio.deleteTask(taskId,isImport);
		long  user_id=((UserRes) session.get("curUser")).getUser().getId();
		String user_ip=request.getRemoteHost();
		String result = "0";
		if(ajax.contains("成功"))
		{
			result = "1";
		}
		bio.addLog(user_id,user_ip,"批量软件升级任务删除",result);
		return "ajax";
	}
	
	/**
	 * 激活任务
	 */
	public String validTask()
	{
		ajax = bio.validTask(taskId);
		if(Global.HNLT.equals(instArea))
		{
			long  user_id=((UserRes) session.get("curUser")).getUser().getId();
			String user_ip=request.getRemoteHost();
			String result = "0";
			if(ajax.contains("成功"))
			{
				result = "1";
			}
			bio.addLog(user_id,user_ip,"批量软件升级任务激活",result);
		}
		return "ajax";
	}

	public String execUpload()
	{
		try {
			UserRes curUser = WebUtil.getCurrentUser();
			String targetDirectory = ServletActionContext.getServletContext()
					.getRealPath("/upload");
			String targetFileName = new Date().getTime() + uploadFileName;
			File target = new File(targetDirectory, targetFileName);
			FileUtils.copyFile(upload, target);
			long currTime = new Date().getTime() / 1000L;
			if (null != target && target.exists()) {
				List<String> serList = new ArrayList<String>();
				// 创建工作表
				HSSFWorkbook workbook = new HSSFWorkbook(new FileInputStream(target));
				// 获得sheet
				HSSFSheet sheet = workbook.getSheetAt(0);
				// 获得sheet的总行数
				int rowCount = sheet.getLastRowNum();
				// 循环解析每一行，第一行不取
				for (int i = 1; i <= rowCount; i++) {
					// 获得行对象
					HSSFRow row = sheet.getRow(i);
					HSSFCell cell = row.getCell(0);
					String ser_account = getCellString(cell);
					if (null != ser_account) {
						serList.add(ser_account.trim());
					}
				}
				Map<String, String> vendorMap = new HashMap<String, String>();
				String[] vendorArray = vendorPathId.split("\2");
				for (String s : vendorArray) {
					String[] temp = s.split("\1");
					vendorMap.put(temp[0], temp[1]);
				}
				// 插入到表中
				bio.batchExcelUpdate(curUser.getUser().getId(), serList,
						currTime, currTime, vendorMap, taskDetail, strategyName);
				ajax = "导入数据成功";
			} else {
				ajax = "复制文件失败";
			}
			setUploadFileName(target.getPath());// 保存文件的存放路径
		} catch (Exception e) {
			logger.warn("批量导入时异常", e);
			ajax = "导入数据库失败！";
		}
		return "batchsuccess";
	}

	private String getCellString(HSSFCell cell) 
	{
		String result = null;
		if (cell != null) {
			// 单元格类型：Numeric:0,String:1,Formula:2,Blank:3,Boolean:4,Error:5
			int cellType = cell.getCellType();
			switch (cellType) {
				case HSSFCell.CELL_TYPE_STRING :
					result = cell.getRichStringCellValue().getString();
					break;
				case HSSFCell.CELL_TYPE_NUMERIC :
					result = cell.getNumericCellValue() + "";
					break;
				case HSSFCell.CELL_TYPE_FORMULA :
					result = cell.getCellFormula();
					break;
				case HSSFCell.CELL_TYPE_BOOLEAN :
					break;
				case HSSFCell.CELL_TYPE_BLANK :
					break;
				case HSSFCell.CELL_TYPE_ERROR :
					break;
				default :
					break;
			}
		}
		return result;
	}

	/**
	 * 批量导入升级任务定制初始化页面
	 */
	public String initExcel()
	{
		UserRes curUser = WebUtil.getCurrentUser();
		vendorList = bio.getVendor();
		accoid = StringUtil.getStringValue(curUser.getUser().getId());
		areaId = StringUtil.getStringValue(curUser.getAreaId());
		String cityId = StringUtil.getStringValue(curUser.getCityId());

		tasklist = bio.getExcelBatchSoftupTask(curPage_splitPage,
				num_splitPage, queryVaild, startTime, endTime, cityId);
		maxPage_splitPage = bio.countExcelBatchSoftupTask(curPage_splitPage,
				num_splitPage, queryVaild, startTime, endTime, cityId);
		return "initexcel";
	}
	
	/**
	 * 批量导入升级初始化页面
	 */
	public String initImport()
	{
		UserRes curUser = WebUtil.getCurrentUser();
		vendorList = bio.getVendor();
		accoid = StringUtil.getStringValue(curUser.getUser().getId());
		areaId = StringUtil.getStringValue(curUser.getAreaId());
		String cityId = StringUtil.getStringValue(curUser.getCityId());

		tasklist = bio.getExcelBatchSoftupTask(curPage_splitPage,
				num_splitPage, queryVaild, startTime, endTime, cityId);
		maxPage_splitPage = bio.countExcelBatchSoftupTask(curPage_splitPage,
				num_splitPage, queryVaild, startTime, endTime, cityId);
		return "initImport";
	}

	/**
	 * 取消任务
	 */
	public String cancerExcelBatchTask() 
	{
		ajax = bio.cancerExcelBatchTask(taskId);
		return "ajax";
	}

	/**
	 * 激活任务
	 */
	public String validExcelBatchTask()
	{
		ajax = bio.validExcelBatchTask(taskId);
		return "ajax";
	}

	/**
	 * 批量导入升级任务定制初始化页面
	 */
	public String downloadTemplate()
	{
		return "toExport";
	}

	/**
	 * 查询升级结果，如果升级结果不等于null，查询指定结果，如果为null查询所有结果
	 */
	public String queryUpRecordByTaskId() 
	{
		if (null == upResult) {
			totalRowCount_splitPage = bio.queryCountByTaskId(taskId, upResult,"1");
			if (totalRowCount_splitPage > 0) {
				upRecordList = bio.queryUpRecordByTaskId(taskId, upResult, "1",
						(curPage_splitPage - 1) * num_splitPage , num_splitPage,true);
			}
		} else {
			totalRowCount_splitPage = bio.queryCountByTaskId(taskId, upResult,null);
			if (totalRowCount_splitPage > 0) {
				upRecordList = bio.queryUpRecordByTaskId(taskId, upResult,
						null, (curPage_splitPage - 1) * num_splitPage ,num_splitPage, true);
			}
		}
		return "taskupresult";
	}
	
	public String queryUpRecordByTaskId_import() 
	{
		if (null == upResult) {
			totalRowCount_splitPage = bio.queryCountByTaskId_import(taskId, upResult,taskImportType);
			if (totalRowCount_splitPage > 0) {
				upRecordList = bio.queryUpRecordByTaskId_import(taskId, upResult, taskImportType,
						(curPage_splitPage - 1) * num_splitPage , num_splitPage,true);
			}
		} else {
			totalRowCount_splitPage = bio.queryCountByTaskId_import(taskId, upResult,taskImportType);
			if (totalRowCount_splitPage > 0) {
				upRecordList = bio.queryUpRecordByTaskId_import(taskId, upResult,
						taskImportType, (curPage_splitPage - 1) * num_splitPage ,num_splitPage, true);
			}
		}
		return "taskupresult_import";
	}
	/**
	 * 查询升级结果，湖南联通和通用版差异较大，单独写一套逻辑
	 */
	public String queryUpRecordByTaskIdHnlt()
	{
		if (null == upResult || StringUtil.IsEmpty(upResult)) {
			totalRowCount_splitPage = bio.queryCountByTaskIdHnlt(taskId, upResult,"1",cityId);
			if (totalRowCount_splitPage > 0) {
				upRecordList = bio.queryUpRecordByTaskIdHnlt(taskId, upResult, "1",cityId,
						(curPage_splitPage - 1) * num_splitPage , num_splitPage,true);
			}
		}
		else
		{
			totalRowCount_splitPage = bio.queryCountByTaskIdHnlt(taskId, upResult, null,cityId);
			if (totalRowCount_splitPage > 0) {
				upRecordList = bio.queryUpRecordByTaskIdHnlt(taskId, upResult,
						null,cityId, (curPage_splitPage - 1) * num_splitPage, num_splitPage, true);
			}
		}




		return "hnlttaskupresult";
	}

	/**
	 * 导出升级结果，湖南联通专用
	 */
	public String exportUpRecordByTaskIdHnlt()
	{
		if (null == upResult || StringUtil.IsEmpty(upResult)) {
			data = bio.exportUpRecordByTaskIdHnlt(taskId, upResult, "1",cityId);
		}
		else
		{
			data = bio.exportUpRecordByTaskIdHnlt(taskId, upResult, null,cityId);
		}
		title = new String[]{"业务账号", "属地", "机顶盒MAC", "机顶盒序列号", "厂商", "型号",
				"软件版本", "硬件版本", "认证APK版本", "EPG版本", "接入类型", "网络类型", "行串","最近上线时间"};
		column = new String[]{"serv_account", "city_name", "cpe_mac",
				"device_serialnumber", "vendor_add", "device_model",
				"softwareversion", "hardwareversion", "apk_version_name", "epg_version", "network_type",
				"addressing_type", "category", "cpe_currentupdatetime"};
		fileName = "软件升级详细数据";

		return "excel";
	}


	/**
	 * 查询策略的详细信息
	 */
	public String queryTaskDetailById() 
	{
		upRecordList = bio.queryTaskHardSoftById(taskId);
		taskResultMap = bio.queryTaskById(taskId,taskType);
		if(Global.HNLT.equals(instArea)){
			checkIPList =null;
		}else if(Global.XJDX.equals(instArea) || Global.NXDX.equals(instArea)){
			checkIPList =null;
		}else{
			checkIPList = bio.queryIPDuanById(taskId);
		}
		
		if(Global.CQDX.equals(instArea)){
			String file_path = StringUtil.getStringValue(taskResultMap, "file_path", "");
			try{
				int index = file_path.indexOf("/", 10);
				file_path = LipossGlobals.getLipossProperty("cdnip")+ file_path.substring(++index);
				taskResultMap.put("file_path", file_path);
			}
			catch (Exception e) {}
			
			checkMACList = bio.queryMACById(taskId);
		}
		if(LipossGlobals.inArea(Global.SXLT)){
			this.taskType = taskType;
		}
		return "taskDetail";
	}
	
	/**
	 * 查询策略的详细信息
	 */
	public String updateTaskDesc() 
	{
		ajax = bio.updateTaskDesc(taskId,taskDesc);
		return "ajax";
	}

	public String queryBatchExcel() 
	{
		// if("1".equals(queryType))
		if (!StringUtil.IsEmpty(startTime) && !StringUtil.IsEmpty(endTime)) {
			totalRowCount_splitPage = bio.getBatchExcelRecord(startTime,endTime,servAccount);
			if (totalRowCount_splitPage > 0) {
				tasklist = bio.queryBatchExcel(startTime, endTime, servAccount,
						(curPage_splitPage - 1) * num_splitPage + 1, num_splitPage,true);
			}
		}
		return "batchexcel";
		// else
		// {
		// return "initquery";
		// }
	}
	
	public String getBatchExcelSTS() 
	{
		UserRes curUser = WebUtil.getCurrentUser();
		cityId = curUser.getUser().getCityId();
		Map resultMap = new HashMap();
		List tempList = bio.getBatchExcelSTS(taskId, cityId);
		if (null != tempList) {
			flag = tempList.size();
			for (int i = 0; i < tempList.size(); i++) 
			{
				Map tempMap = (Map) tempList.get(i);
				if ("0".equals(tempMap.get("result").toString())) {
					if (null == resultMap.get("notdo")) {
						resultMap.put("notdo", tempMap.get("softupsum"));
					} else {
						int temp = Integer.parseInt(tempMap.get("softupsum")
								.toString())
								+ Integer.parseInt(resultMap.get("notdo")
										.toString());
						resultMap.put("notdo", temp);
					}
				} else if ("1".equals(tempMap.get("result").toString())) {
					resultMap.put("updatesucc", tempMap.get("softupsum"));
				} else if ("2".equals(tempMap.get("result").toString())) {
					resultMap.put("softupsucc", tempMap.get("softupsum"));
				} else if ("-1".equals(tempMap.get("result").toString())) {
					resultMap.put("updatefailed", tempMap.get("softupsum"));
				}
			}
		}
		taskResultMap = resultMap;
		// resultList = bio.getBatchExcelSTS(taskId);
		return "exceltaskResult";
	}
	
	/**
	 * 查看结果后，点击数字，获取设备详细信息
	 */
	public String queryBatchExcelList() 
	{
		if (null != taskId && null != upResult) {
			totalRowCount_splitPage = bio.getBatchExcelList(taskId, upResult);
			if (totalRowCount_splitPage > 0) {
				upRecordList = bio.queryBatchExcelList(taskId, upResult,
						(curPage_splitPage - 1) * num_splitPage + 1, num_splitPage,true);
			}
		}
		return "exceltaskupresult";
	}
	
	/**
	 * 下载获取设备详细信息
	 */
	public String exportBatchExcelList() 
	{
		logger.warn("exportBatchExcelList()");
		title = new String[]{"厂商", "硬件型号", "业务帐号", "用户类型", "设备序列号", "新软件版本",
				"旧软件版本", "升级结果", "操作时间"};
		column = new String[]{"vendor_add", "device_model", "serv_account",
				"group_type", "device_serialnumber", "newsoftversion",
				"oldsoftversion", "result", "start_time"};
		fileName = "";
		int result = StringUtil.getIntegerValue(upResult);
		strategyName = bio.getStrategyName(taskId);
		switch (result) {
			case 0 :
				fileName = strategyName + "_未操作";
				break;
			case 1 :
				fileName = strategyName + "_更新服务器地址修改成功";
				break;
			case 2 :
				fileName = strategyName + "_软件升级成功";
				break;
			case -1 :
				fileName = strategyName + "_更新服务器地址修改失败";
				break;
		}
		data = bio.exportBatchExcelList(taskId, upResult);
		return "excel";
	}
	
	/**
	 * 查看详细
	 */
	public String getBatchExcelDetail()
	{
		Map tempMap = bio.getBatchExcelDetail(taskId);
		taskResultMap = (Map) tempMap.get("task");
		taskResultMap.put("hotelCount", bio.getHotelUser(taskId).size());
		taskResultMap.put("vipCount", bio.getVIPUser(taskId).size());
		taskResultMap.put("senCount", bio.getSenUser(taskId).size());
		upRecordList = (List) tempMap.get("vendorList");
		return "exceltaskdetail";
	}

	/**
	 * 批量导入升级
	 */
	public String batchImportUp() 
	{
		try {
			UserRes curUser = WebUtil.getCurrentUser();
			String targetDirectory = ServletActionContext.getServletContext()
															.getRealPath("/upload");
			String targetFileName = new Date().getTime() + uploadFileName;
			File target = new File(targetDirectory, targetFileName);
			FileUtils.copyFile(upload, target);
			long currTime = new Date().getTime() / 1000L;
			if (null != target && target.exists()) {
				List<String> serList = new ArrayList<String>();
				HSSFWorkbook workbook = new HSSFWorkbook(new FileInputStream(target));
				HSSFSheet sheet = workbook.getSheetAt(0);
				// 获得sheet的总行数
				int rowCount = sheet.getLastRowNum();
				// 循环解析每一行，第一行不取
				for (int i = 1; i <= rowCount; i++) {
					// 获得行对象
					HSSFRow row = sheet.getRow(i);
					HSSFCell cell = row.getCell(0);
					String ser_account = getCellString(cell);
					if (null != ser_account) {
						serList.add(ser_account.trim());
					}
				}
				// 插入到表中
				bio.batchImportUp(curUser.getUser().getId(), serList, currTime,
						currTime, vendorId, pathId, taskDetail, strategyName,deviceModelIds);
				ajax = "导入数据成功";
			} else {
				ajax = "复制文件失败";
			}
			setUploadFileName(target.getPath());// 保存文件的存放路径
		} catch (Exception e) {
			logger.warn("批量导入时异常", e);
			ajax = "导入数据库失败！";
		}
		return "batchsuccess";
	}
	
	public String delete()
	{
		if (bio.delete(taskId) > 0) {
			initImport();
		}
		return "initImport";
	}

	/**
	 * 批量导入升级初始化页面(有大的改动)
	 */
	public String initImportSoftUp() 
	{
		logger.warn("initImportSoft");
		UserRes curUser = WebUtil.getCurrentUser();
		vendorList = bio.getVendor();
		accoid = StringUtil.getStringValue(curUser.getUser().getId());
		areaId = StringUtil.getStringValue(curUser.getAreaId());
		String cityId = StringUtil.getStringValue(curUser.getCityId());

		tasklist = bio.getExcelBatchSoftupTask(curPage_splitPage,
				num_splitPage, queryVaild, startTime, endTime, cityId);
		maxPage_splitPage = bio.countExcelBatchSoftupTask(curPage_splitPage,
				num_splitPage, queryVaild, startTime, endTime, cityId);
		return "initImportSoftUp";
	}
	
	/**
	 * 批量导入升级
	 */
	public String batchImportSoftUp() 
	{
		boolean flag = false;
		UserRes curUser = WebUtil.getCurrentUser();
		String cityId = curUser.getCityId();
		String filePath = "/soft/upload";
		String targetDirectory = "";
		String targetFileName = "";
		HttpServletRequest request = null;
		try {
			// 将文件存放到指定的路径中
			request = ServletActionContext.getRequest();
			/**
			 * String reUrl = request.getRemoteAddr(); String ipStr =
			 * request.getLocalAddr(); int port = request.getServerPort();
			 * 
			 * logger.warn("batchImportSoftUp()=======");
			 * logger.warn("ipStr==="+ipStr); logger.warn("reUrl==="+reUrl);
			 * logger.warn("port==="+port);
			 * logger.warn("localport==="+request.getLocalPort());
			 * logger.warn("remotePort==="+request.getRemotePort());
			 * logger.warn("uri==="+request.getRequestURI());
			 * logger.warn("url==="+request.getRequestURL());
			 */
			targetDirectory = ServletActionContext.getServletContext().getRealPath(filePath);
			// 由于传输的文件名中到后台有中文乱码所以存储到数据库更改文件名
			String tempFileName = "batchUPtemplate.xls";
			targetFileName = new Date().getTime() + tempFileName;
			File target = new File(targetDirectory, targetFileName);
			FileUtils.copyFile(upload, target);
		} catch (IOException e) {
			logger.error("批量导入升级，上传文件时出错");
		}
		// 生成时间
		long currTime = new Date().getTime() / 1000L;
		// 插入到表中
		// filePath = targetDirectory +"/"+targetFileName;
		// filePath = "http://192.168.12.60:8080/lims"+filePath
		// +"/"+targetFileName;
		filePath = "http://" + request.getLocalAddr() + ":"
				+ request.getServerPort() + "/lims" + filePath + "/"+ targetFileName;
		logger.warn("filePath=" + filePath);
		flag = bio.batchImportSoftUp(curUser.getUser().getId(), currTime,
				currTime, vendorId, pathId, taskDetail, strategyName,
				deviceModelIds, filePath, isHotel, isVIP, isOther, cityId);
		if (flag) {
			ajax = "导入数据成功";
		} else {
			ajax = "导入数据失败";
		}
		return "batchsuccess";
	}
	
	/**
	 * 生效或失效任务
	 */
	public String cancelBatchTask() 
	{
		ajax = bio.cancelBatchTask(taskId, status);
		return "ajax";
	}
	
	/**
	 * 删除任务
	 */
	public String deleteSoftUpTask() 
	{
		ajax = bio.deleteSoftUpTask(this.taskId);
		return "ajax";
	}
	
	/**
	 * 下载错误帐号
	 */
	public String exportFailAccount() 
	{
		logger.warn("exportFailAccount()");
		title = new String[]{"策略名称", "失败类型", "业务账号", "设备序列号", "策略要求厂商", "设备厂商",
				"策略要求型号", "实际设备型号", "策略要求属地", "实际设备属地", "策略是否包含vip",
				"策略是否包含酒店", "策略是否包含敏感", "实际用户类型"};
		column = new String[]{"strategyname", "reason", "serv_account",
				"dev_sn", "mVendor", "vendor", "mDeviceMdel", "deviceMdel",
				"mCityName", "cityName", "isVip", "isHotel", "isSensitive",
				"groupType"};
		fileName = "错误帐号";
		data = bio.exportFailAccount(taskId);
		return "excel";
	}
	
	/**
	 * 下载酒店用户
	 */
	public String exportHotelUser()
	{
		logger.warn("exportFailAccount()");
		title = new String[]{"业务帐号,设备序列号,定制时间"};
		column = new String[]{"serv_account", "dev_sn", "add_time"};
		fileName = "酒店用户";
		data = bio.getHotelUser(taskId);
		return "excel";
	}
	
	/**
	 * 下载VIP用户
	 */
	public String exportVIPUser()
	{
		logger.warn("exportFailAccount()");
		title = new String[]{"业务帐号,设备序列号,定制时间"};
		column = new String[]{"serv_account", "dev_sn", "add_time"};
		fileName = "VIP用户";
		data = bio.getVIPUser(taskId);
		return "excel";
	}
	
	/**
	 * 下载敏感用户
	 */
	public String exportSenUser()
	{
		logger.warn("exportFailAccount()");
		title = new String[]{"业务帐号", "设备序列号", "定制时间"};
		column = new String[]{"serv_account", "dev_sn", "add_time"};
		fileName = "敏感用户";
		data = bio.getVIPUser(taskId);
		return "excel";
	}

	/**
	 * 解析导入文件
	 */
	public String getIPString()
	{
		logger.warn("getIPList");
		String targetDirectory = ServletActionContext.getServletContext()
				.getRealPath("/accountFile");
		return bio.getIPString(targetDirectory, uploadFileName, upload);
	}

	/**
	 * 解析导入文件
	 */
	public String getMACString() 
	{
		logger.warn("getMACList");
		String targetDirectory = ServletActionContext.getServletContext()
				.getRealPath("/accountFile");
		return bio.getIPString(targetDirectory, uploadFileName4MAC, uploadMAC);
	}
	
	public String parseFile(){
		logger.debug("sxlt parse file begin");
		ajax = bio.parseFile(vendorId,fileName);
		return "ajax";
	}
	
	
	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public String getAjax() {
		return ajax;
	}

	public void setAjax(String ajax) {
		this.ajax = ajax;
	}

	public SoftUpgradeBIO getBio() {
		return bio;
	}

	public void setBio(SoftUpgradeBIO bio) {
		this.bio = bio;
	}

	public String getPathId() {
		return pathId;
	}

	public void setPathId(String pathId) {
		this.pathId = pathId;
	}

	public String getStrategyId() {
		return strategyId;
	}

	public void setStrategyId(String strategyId) {
		this.strategyId = strategyId;
	}

	public Map getServStrategyMap() {
		return servStrategyMap;
	}

	public void setServStrategyMap(Map servStrategyMap) {
		this.servStrategyMap = servStrategyMap;
	}

	public String getIsRefresh() {
		return isRefresh;
	}

	public void setIsRefresh(String isRefresh) {
		this.isRefresh = isRefresh;
	}

	public String getCityId() {
		return cityId;
	}

	public void setCityId(String cityId) {
		this.cityId = cityId;
	}

	public List<Map<String, String>> getCityList() {
		return cityList;
	}

	public void setCityList(List<Map<String, String>> cityList) {
		this.cityList = cityList;
	}

	public List getVendorList() {
		return vendorList;
	}

	public void setVendorList(List vendorList) {
		this.vendorList = vendorList;
	}

	public String getVendorId() {
		return vendorId;
	}

	public void setVendorId(String vendorId) {
		this.vendorId = vendorId;
	}

	public String getDevicetypeId() {
		return devicetypeId;
	}

	public void setDevicetypeId(String devicetypeId) {
		this.devicetypeId = devicetypeId;
	}

	public String getStrategyType() {
		return strategyType;
	}

	public void setStrategyType(String strategyType) {
		this.strategyType = strategyType;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public List getTasklist() {
		return tasklist;
	}

	public void setTasklist(List tasklist) {
		this.tasklist = tasklist;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public Map getTaskResultMap() {
		return taskResultMap;
	}

	public void setTaskResultMap(Map taskResultMap) {
		this.taskResultMap = taskResultMap;
	}

	public String getAccoid() {
		return accoid;
	}

	public void setAccoid(String accoid) {
		this.accoid = accoid;
	}

	public String getAreaId() {
		return areaId;
	}

	public void setAreaId(String areaId) {
		this.areaId = areaId;
	}

	public List getResultList() {
		return resultList;
	}

	public void setResultList(List resultList) {
		this.resultList = resultList;
	}
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}
	public String getSoftwareversion() {
		return softwareversion;
	}

	public void setSoftwareversion(String softwareversion) {
		this.softwareversion = softwareversion;
	}

	public String getVersionPath() {
		return versionPath;
	}

	public void setVersionPath(String versionPath) {
		this.versionPath = versionPath;
	}
	public void setPassword(String password) {
		this.password = password;
	}

	public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}

	public String getIsHotel() {
		return isHotel;
	}

	public void setIsHotel(String isHotel) {
		this.isHotel = isHotel;
	}

	public String getIsVIP() {
		return isVIP;
	}

	public void setIsVIP(String isVIP) {
		this.isVIP = isVIP;
	}

	public String getIsOther() {
		return isOther;
	}

	public void setIsOther(String isOther) {
		this.isOther = isOther;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	
	public String getSoftStrategy_type() {
		return softStrategy_type;
	}

	public void setSoftStrategy_type(String softStrategy_type) {
		this.softStrategy_type = softStrategy_type;
	}

	public String getTaskDesc() {
		return taskDesc;
	}

	public void setTaskDesc(String taskDesc) {
		this.taskDesc = taskDesc;
	}

	public String getIsImport() {
		return isImport;
	}

	public void setIsImport(String isImport) {
		this.isImport = isImport;
	}

	public String getInstArea() {
		return instArea;
	}

	public void setInstArea(String instArea) {
		this.instArea = instArea;
	}
	
	public List getCheckMACList(){
		return checkMACList;
	}

	public void setCheckMACList(List checkMACList){
		this.checkMACList = checkMACList;
	}

	public String getShowType() {
		return showType;
	}

	public void setShowType(String showType) {
		this.showType = showType;
	}

	public String getCheck_net() {
		return check_net;
	}

	public void setCheck_net(String check_net) {
		this.check_net = check_net;
	}

	public String getSource_devicetypeId() {
		return source_devicetypeId;
	}

	public void setSource_devicetypeId(String source_devicetypeId) {
		this.source_devicetypeId = source_devicetypeId;
	}

	public String getGoal_softwareversion() {
		return goal_softwareversion;
	}

	public void setGoal_softwareversion(String goal_softwareversion) {
		this.goal_softwareversion = goal_softwareversion;
	}

	public String getGw_type() {
		return gw_type;
	}

	public void setGw_type(String gw_type) {
		this.gw_type = gw_type;
	}
	
	public GwDeviceQueryBIO getGwDeviceQueryBio() {
		return gwDeviceQueryBio;
	}

	public void setGwDeviceQueryBio(GwDeviceQueryBIO gwDeviceQueryBio) {
		this.gwDeviceQueryBio = gwDeviceQueryBio;
	}
	
	public File getUploadMAC() {
		return uploadMAC;
	}

	public void setUploadMAC(File uploadMAC) {
		this.uploadMAC = uploadMAC;
	}

	public String getUploadFileName4MAC() {
		return uploadFileName4MAC;
	}

	public void setUploadFileName4MAC(String uploadFileName4MAC) {
		this.uploadFileName4MAC = uploadFileName4MAC;
	}

	public File getUploadCustomer() {
		return uploadCustomer;
	}

	public void setUploadCustomer(File uploadCustomer) {
		this.uploadCustomer = uploadCustomer;
	}

	public String getUploadFileName4Customer() {
		return uploadFileName4Customer;
	}

	public void setUploadFileName4Customer(String uploadFileName4Customer) {
		this.uploadFileName4Customer = uploadFileName4Customer;
	}
	
	public String getStrategyName() {
		return strategyName;
	}

	public void setStrategyName(String strategyName) {
		this.strategyName = strategyName;
	}
	public String getParam() {
		return param;
	}

	public void setParam(String param) {
		this.param = param;
	}
	public String getTaskDetail() {
		return taskDetail;
	}

	public void setTaskDetail(String taskDetail) {
		this.taskDetail = taskDetail;
	}

	public String getQueryType() {
		return queryType;
	}

	public void setQueryType(String queryType) {
		this.queryType = queryType;
	}

	public String getStartTime() {
		return startTime;
	}
	public String getDeviceIds() {
		return deviceIds;
	}

	public void setDeviceIds(String deviceIds) {
		this.deviceIds = deviceIds;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getServAccount() {
		return servAccount;
	}

	public void setServAccount(String servAccount) {
		this.servAccount = servAccount;
	}

	public String getQueryCityId() {
		return queryCityId;
	}

	public void setQueryCityId(String queryCityId) {
		this.queryCityId = queryCityId;
	}

	public String getQueryVendorId() {
		return queryVendorId;
	}

	public void setQueryVendorId(String queryVendorId) {
		this.queryVendorId = queryVendorId;
	}

	public String getQueryVaild() {
		return queryVaild;
	}

	public void setQueryVaild(String queryVaild) {
		this.queryVaild = queryVaild;
	}
	
	public List getCheckIPList() {
		return checkIPList;
	}

	public void setCheckIPList(List checkIPList) {
		this.checkIPList = checkIPList;
	}
	public List getUpRecordList() {
		return upRecordList;
	}

	public void setUpRecordList(List upRecordList) {
		this.upRecordList = upRecordList;
	}

	public String getUpResult() {
		return upResult;
	}

	public void setUpResult(String upResult) {
		this.upResult = upResult;
	}
	
	public void setExportExcelStream(InputStream exportExcelStream) {
		this.exportExcelStream = exportExcelStream;
	}

	public File getUpload() {
		return upload;
	}

	public String getVendorPathId() {
		return vendorPathId;
	}

	public void setVendorPathId(String vendorPathId) {
		this.vendorPathId = vendorPathId;
	}

	public void setUpload(File upload) {
		this.upload = upload;
	}

	public String getUploadFileName() {
		return uploadFileName;
	}

	public void setUploadFileName(String uploadFileName) {
		this.uploadFileName = uploadFileName;
	}
	
	public String getMacCheck() {
		return macCheck;
	}

	public void setMacCheck(String macCheck) {
		this.macCheck = macCheck;
	}

	public String getCustCheck() {
		return custCheck;
	}

	public void setCustCheck(String custCheck) {
		this.custCheck = custCheck;
	}

	public String getBtnValue4IP() {
		return btnValue4IP;
	}

	public void setBtnValue4IP(String btnValue4IP) {
		this.btnValue4IP = btnValue4IP;
	}

	public String getBtnValue4MAC() {
		return btnValue4MAC;
	}

	public void setBtnValue4MAC(String btnValue4MAC) {
		this.btnValue4MAC = btnValue4MAC;
	}

	public String getBtnValue4Customer() {
		return btnValue4Customer;
	}

	public void setBtnValue4Customer(String btnValue4Customer) {
		this.btnValue4Customer = btnValue4Customer;
	}
	
	public String getIpSG() {
		return ipSG;
	}

	public void setIpSG(String ipSG) {
		this.ipSG = ipSG;
	}

	public String getIpCheck() {
		return ipCheck;
	}

	public void setIpCheck(String ipCheck) {
		this.ipCheck = ipCheck;
	}

	public String getMacSG() {
		return macSG;
	}

	public void setMacSG(String macSG) {
		this.macSG = macSG;
	}

	public String getCustSG() {
		return custSG;
	}

	public void setCustSG(String custSG) {
		this.custSG = custSG;
	}

	public String getDeviceModelIds() {
		return deviceModelIds;
	}

	public void setDeviceModelIds(String deviceModelIds) {
		this.deviceModelIds = deviceModelIds;
	}

	public String[] getTitle() {
		return title;
	}

	public void setTitle(String[] title) {
		this.title = title;
	}

	public String[] getColumn() {
		return column;
	}

	public void setColumn(String[] column) {
		this.column = column;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public List getData() {
		return data;
	}

	public void setData(List data) {
		this.data = data;
	}

	
	public String getSxSoftCitys()
	{
		return sxSoftCitys;
	}

	
	public void setSxSoftCitys(String sxSoftCitys)
	{
		this.sxSoftCitys = sxSoftCitys;
	}

	
	public String getCituName()
	{
		return cituName;
	}

	
	public void setCituName(String cituName)
	{
		this.cituName = cituName;
	}

	public List getTaskResultMapList() {
		return taskResultMapList;
	}

	public void setTaskResultMapList(List taskResultMapList) {
		this.taskResultMapList = taskResultMapList;
	}

	public Map getSession() {
		return session;
	}

	public void setSession(Map session) {
		this.session = session;
	}

	@Override
	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}
	public String getDevsnCheck()
	{
		return devsnCheck;
	}

	
	public void setDevsnCheck(String devsnCheck)
	{
		this.devsnCheck = devsnCheck;
	}

	
	public String getTaskType()
	{
		return taskType;
	}

	
	public void setTaskType(String taskType)
	{
		this.taskType = taskType;
	}
	public List getImportTasklist()
	{
		return importTasklist;
	}

	
	public void setImportTasklist(List importTasklist)
	{
		this.importTasklist = importTasklist;
	}
	public String getTaskImportType() {
		return taskImportType;
	}

	public void setTaskImportType(String taskImportType) {
		this.taskImportType = taskImportType;
	}

}
