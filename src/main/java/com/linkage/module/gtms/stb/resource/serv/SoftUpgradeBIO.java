package com.linkage.module.gtms.stb.resource.serv;

import com.linkage.commons.db.DBOperation;
import com.linkage.commons.util.DateTimeUtil;
import com.linkage.commons.util.StringUtil;
import com.linkage.commons.util.TimeUtil;
import com.linkage.litms.LipossGlobals;
import com.linkage.module.gtms.stb.dao.GwStbVendorModelVersionDAO;
import com.linkage.module.gtms.stb.resource.dao.SoftUpgradeDAO;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.module.gwms.obj.StrategyOBJ;
import com.linkage.module.gwms.resource.obj.MQPublisher;
import com.linkage.module.gwms.resource.obj.SoftTask;
import com.linkage.module.gwms.share.act.FileUploadAction;
import com.linkage.module.gwms.util.CreateObjectFactory;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

import org.apache.commons.io.FileUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.struts2.ServletActionContext;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;

import java.io.*;
import java.util.*;

@SuppressWarnings("rawtypes")
public class SoftUpgradeBIO 
{
	private static Logger logger = LoggerFactory.getLogger(SoftUpgradeBIO.class);
	private SoftUpgradeDAO dao;
	private GwStbVendorModelVersionDAO vmvDaoStb;

	private static Map<String,String> netTypeMap=new HashMap<String,String>();
	private static Map<String,String> devTypeMap=new HashMap<String,String>();
	static{
		netTypeMap.put("public_net","公 网");
		netTypeMap.put("private_net","专 网");
		netTypeMap.put("unknown_net","未 知");
		devTypeMap.put("0","行");
		devTypeMap.put("1","串");
		devTypeMap.put("2","未 知");
	}
	
	private String instArea=Global.instAreaShortName;
	
	// 批量软件升级mq
//	public static final String MQ_SOFT_TASK_ENAB = LipossGlobals.getLipossProperty("mqSoftTask.enab");
//	public static final String MQ_SOFT_TASK_URL = LipossGlobals.getLipossProperty("mqSoftTask.url");
//	public static final String MQ_SOFT_TASK_TOPIC = LipossGlobals.getLipossProperty("mqSoftTask.topic");

	/**
	 * 获取设备可升级版本
	 */
	public String showSoftwareversion(String deviceId) 
	{
		List list =null;
		Map devMap=null;
		if (Global.HNLT.equals(instArea)) {
			list = dao.showSoftwareversionByDeviceId_hnlt(deviceId);
			devMap=dao.getDeviceInfo(deviceId);
		}else{
			list = dao.showSoftwareversionByDeviceId(deviceId);
		}
		
		StringBuffer bf = new StringBuffer();
		for (int i = 0; i < list.size(); i++) {
			Map map = (Map) list.get(i);
			if (i > 0) {
				bf.append("#");
			}
			if(Global.HNLT.equals(instArea)){
				bf.append(StringUtil.getStringValue(map,"id",""));
				bf.append(",");
				bf.append(StringUtil.getStringValue(map,"net_type","unknown_net"));
			}else{
				bf.append(StringUtil.getStringValue(map,"id",""));
			}
			
			bf.append("$");
			bf.append(StringUtil.getStringValue(map,"softwareversion",""));
			
			if(!Global.HNLT.equals(instArea)){
				bf.append("("+StringUtil.getStringValue(map,"version_path","")+")");
			}
		}
		
		if(Global.HNLT.equals(instArea))
		{
			if(devMap!=null && !devMap.isEmpty())
			{
				bf.append("&"+StringUtil.getStringValue(devMap,"vendor_add",""));
				bf.append(","+StringUtil.getStringValue(devMap,"device_model",""));
				bf.append(","+StringUtil.getStringValue(devMap,"cpe_mac",""));
				bf.append(","+StringUtil.getStringValue(devMap,"serv_account",""));
				bf.append(","+StringUtil.getStringValue(devMap,"hardwareversion",""));
				bf.append(","+StringUtil.getStringValue(devMap,"softwareversion",""));
				bf.append(","+StringUtil.getStringValue(devMap,"apk_version_name",""));
				bf.append(","+StringUtil.getStringValue(devMap,"epg_version",""));
				bf.append(","+new DateTimeUtil(StringUtil.getLongValue(
						devMap,"complete_time")*1000).getYYYY_MM_DD_HH_mm_ss());
				bf.append(","+new DateTimeUtil(StringUtil.getLongValue(
						devMap,"cpe_currentupdatetime")*1000).getYYYY_MM_DD_HH_mm_ss());
				bf.append(","+StringUtil.getStringValue(devMap,"network_type",""));
				bf.append(","+StringUtil.getStringValue(devMap,"addressing_type",""));
				bf.append(","+StringUtil.getStringValue(devMap,"loopback_ip",""));
				bf.append(","+StringUtil.getStringValue(devMap,"public_ip",""));
				bf.append(","+netTypeMap.get(StringUtil.getStringValue(devMap,"net_type","unknown_net")));
				bf.append(","+netTypeMap.get(StringUtil.getStringValue(devMap,"ip_type","unknown_net")));
				bf.append(","+devTypeMap.get(StringUtil.getStringValue(devMap,"category","2")));
			}
		}
		
		return bf.toString();
	}
	
	/**
	 * 获取版本详细
	 */
	public String querySoftVersionDetail(String id) 
	{
		Map map=dao.querySoftVersionDetail(id);
		
		StringBuffer bf = new StringBuffer();
		if(map!=null && !map.isEmpty())
		{
			bf.append(StringUtil.getStringValue(map,"version_desc",""));
			bf.append(","+StringUtil.getStringValue(map,"version_path",""));
			bf.append(","+StringUtil.getStringValue(map,"file_size",""));
			bf.append(","+StringUtil.getStringValue(map,"md5",""));
			bf.append(","+netTypeMap.get(StringUtil.getStringValue(map,"net_type","unknown_net")));
			bf.append(","+StringUtil.getStringValue(map,"epg_version",""));
		}
		
		return bf.toString();
	}

	public String showAllSoftwareversion() 
	{
		List list = dao.showAllSoftwareversionByDeviceId();
		StringBuffer bf = new StringBuffer();
		for (int i = 0; i < list.size(); i++) {
			Map map = (Map) list.get(i);
			if (i > 0) {
				bf.append("#");
			}
			bf.append(map.get("id")+"$");
			bf.append(StringUtil.getStringValue(map.get("softwareversion")));
			bf.append("("+StringUtil.getStringValue(map.get("version_path"))+")");
		}
		return bf.toString();
	}
	
	public List getUpgradeResult(String deviceId) 
	{
		List list;
		if (Global.HNLT.equals(instArea)) {
			list = dao.getUpgradeResult_hnlt(deviceId);
		}else{
			list = dao.getUpgradeResult(deviceId);
		}
		
		return list;
	}

	public String softUpgrade(long accOId, String pathId, String deviceId,
			String devicetypeId, String softStrategy_type) 
	{
		logger.warn("softUpgrade({},{},{})", new Object[]{deviceId, accOId,devicetypeId});
		
		//HBLT-RMS-20190916-LH-064 机顶盒升级黑名单限制
		if (Global.HBLT.equals(instArea))
		{
			Map<String, String> devInfoBlackListMap = dao.getDevBlackListInfo(deviceId);
			if (null != devInfoBlackListMap) {
				logger.warn("设备在黑名单中，不予升级(deviceId=[{}])", deviceId);
				return 0 + ";此设备在黑名单中，不予升级;" + deviceId;
			}
		}
		// 入策略表
		String[] deviceIdIdArr = new String[1];
		deviceIdIdArr[0] = deviceId;
		ArrayList<String> paramArr = new ArrayList<String>();
		paramArr.add(devicetypeId);
		paramArr.add(softStrategy_type);
		if (Global.JXDX.equals(instArea))
		{
			long taskid = Math.round(Math.random() * 100000L);
			String taskId = StringUtil.getStringValue(taskid);
			paramArr.add(taskId);
		}
		CreateObjectFactory.createPreProcess(Global.GW_TYPE_STB).processDeviceStrategy(
				deviceIdIdArr, "5",(String[]) paramArr.toArray(new String[paramArr.size()]));
		return 1 + ";调用后台成功;" + deviceId;
	}
	
	public String softUpgrade_hnlt(long accOId, String id, String deviceId) 
	{
		logger.warn("softUpgrade_hnlt({},{},{})", new Object[]{deviceId,accOId,id});
		// 入策略表stb_gw_strategy_soft
		int i=dao.insertStrategy(accOId,id,deviceId);
		if(i>0){
			return 1 + ";调用后台成功;" + deviceId;
		}
		return "0";
	}

	public String batchSoftUpgrade(long accOId,String pathId,String deviceIds,String devicetypeId) 
	{
		logger.warn("batchSoftUpgrade({},{},{})", new Object[]{deviceIds, accOId,devicetypeId});
		if (!"0".equals(deviceIds)) {
			String[] deviceId_array = deviceIds.split(",");
			String[] paramArr;
			paramArr = new String[1];
			paramArr[0] = devicetypeId;
			CreateObjectFactory.createPreProcess(Global.GW_TYPE_STB)
									.processDeviceStrategy(deviceId_array, "5", paramArr);
		}
		return 1 + ";调用后台成功;" + deviceIds;
	}
	
	/**
	 * 软件升级结果
	 */
	public Map getStrategyById(String strategyId) 
	{
		return dao.getStrategyById(strategyId);
	}

	public List getVendor() 
	{
		return vmvDaoStb.getVendor();
	}

	/**
	 * 根据厂商获取目标版本
	 */
	public String getTargetVersion(String vendorId) 
	{
		List list = dao.getTargetVersion(vendorId);
		StringBuffer bf = new StringBuffer();
		for (int i = 0; i < list.size(); i++) {
			Map map = (Map) list.get(i);
			if (i > 0) {
				bf.append("#");
			}
			bf.append(StringUtil.getStringValue(map.get("id"))+"$");
			bf.append(StringUtil.getStringValue(map.get("softwareversion")));
			bf.append("("+StringUtil.getStringValue(map.get("version_path"))+")");
		}
		return bf.toString();
	}

	public String batchSoftUpgrade(String vendorId, String cityId,
			String pathId, String strategyType, long accoid,
			String devicetypeId, String ipCheck, String ipSG) 
	{
		int ier = dao.batchSoftUpgrade(vendorId, cityId, pathId, strategyType,
				accoid, devicetypeId, ipCheck, ipSG);
		if (ier == 1) {
			return "任务定制成功!";
		} else {
			return "任务定制失败！";
		}
	}

	public int xjbatchSoftUpgrade(long taskId ,String vendorId, String cityId, String pathId,
			String strategyType, long accoid, String devicetypeId)
	{
		return dao.xjbatchSoftUpgrade(taskId, vendorId, cityId, pathId, 
						strategyType, accoid, devicetypeId);
	}
	
	
	public String batchSoftUpgradeBakForsxlt(String taskType,String tempTaskId ,String vendorId, String cityId,
			String pathId, String strategyType, long accoid,
			String devicetypeId, String ipCheck, String ipSG, String maccheck,
			String macSG, String custcheck, String custSG, File uploadCustomer,
			String uploadFileName4Customer,String check_net,String devsnCheck)
	{
		String customerFilePath = "";
		
		// ftp配置文件
		String ftpEnable = "";
		String ftpIp = "";
		String ftpName = "";
		String ftpPasswd = "";
		String ftpPath = "";
		long taskId = Math.round(Math.random() * 100000L);
		
		if (null != uploadCustomer && uploadCustomer.isFile()
				&& uploadCustomer.length() > 0) 
		{
			// 如果是导入帐号，将导入帐号的文件放到指定的目录底下
			String targetDirectory = "";
			customerFilePath = "/accountFile";
			if(Global.CQDX.equals(instArea)){
				customerFilePath = "/softAccount";
			}
			
			String targetFileName = "";
			HttpServletRequest request = null;
			try {
				// 将文件存放到指定的路径中
				request = ServletActionContext.getRequest();
				targetDirectory = ServletActionContext.getServletContext()
										.getRealPath(customerFilePath);
				// 由于传输的文件名中到后台有中文乱码所以存储到数据库更改文件名
				targetFileName = new DateTimeUtil().getYYYYMMDDHHMMSS() + "_"
						+ uploadFileName4Customer;
				File target = new File(targetDirectory, targetFileName);
				FileUtils.copyFile(uploadCustomer, target);
				
				if(Global.CQDX.equals(instArea)){
					getDeviceList(taskId, targetDirectory + "/" + targetFileName);
				}
				
				ftpEnable = LipossGlobals.getLipossProperty("stbSoftUpgrade.ftpEnable");
				// 是否使用FTP
				if("1".equals(ftpEnable)){
					ftpIp = LipossGlobals.getLipossProperty("stbSoftUpgrade.ftpIp");
					ftpName = LipossGlobals.getLipossProperty("stbSoftUpgrade.ftpName");
					ftpPasswd = LipossGlobals.getLipossProperty("stbSoftUpgrade.ftpPasswd");
					ftpPath = LipossGlobals.getLipossProperty("stbSoftUpgrade.ftpPath") 
								+ targetFileName;
					
					File ftpFile = new File(ftpPath);
					FileUtils.copyFile(uploadCustomer, ftpFile);
				}
			} catch (IOException e) {
				logger.error("批量导入升级，上传文件时出错");
			}
			customerFilePath = LipossGlobals.getLipossProperty("cdnip") + request.getContextPath()  
								+ customerFilePath+ "/" + targetFileName;
		}
		logger.warn("SoftUpgardeBIO=" + devicetypeId);
		int ier = dao.batchSoftUpgradeBak(taskId, vendorId, cityId, pathId,
				strategyType, accoid, devicetypeId, ipCheck, ipSG, maccheck,
				macSG, custcheck, custSG, customerFilePath,ftpEnable,ftpIp,
				ftpName,ftpPasswd,ftpPath,check_net,taskType,devsnCheck);
		if (ier == 1) {
			if(LipossGlobals.inArea(Global.SXLT)){
				//更新任务id，保持和任务表一致
				dao.updateTaskId(StringUtil.getLongValue(tempTaskId),taskId,custcheck,devsnCheck);
			}
			return "任务定制成功!";
		} else {
			return "任务定制失败！";
		}
	}
	public String batchSoftUpgradeBak(String vendorId, String cityId,
			String pathId, String strategyType, long accoid,
			String devicetypeId, String ipCheck, String ipSG, String maccheck,
			String macSG, String custcheck, String custSG, File uploadCustomer,
			String uploadFileName4Customer,String check_net)
	{
		String customerFilePath = "";
		
		// ftp配置文件
		String ftpEnable = "";
		String ftpIp = "";
		String ftpName = "";
		String ftpPasswd = "";
		String ftpPath = "";
		long taskId = Math.round(Math.random() * 100000L);
		
		if (null != uploadCustomer && uploadCustomer.isFile()
				&& uploadCustomer.length() > 0) 
		{
			// 如果是导入帐号，将导入帐号的文件放到指定的目录底下
			String targetDirectory = "";
			customerFilePath = "/accountFile";
			if(Global.CQDX.equals(instArea)){
				customerFilePath = "/softAccount";
			}
			
			String targetFileName = "";
			HttpServletRequest request = null;
			try {
				// 将文件存放到指定的路径中
				request = ServletActionContext.getRequest();
				targetDirectory = ServletActionContext.getServletContext()
										.getRealPath(customerFilePath);
				// 由于传输的文件名中到后台有中文乱码所以存储到数据库更改文件名
				targetFileName = new DateTimeUtil().getYYYYMMDDHHMMSS() + "_"
						+ uploadFileName4Customer;
				File target = new File(targetDirectory, targetFileName);
				FileUtils.copyFile(uploadCustomer, target);
				
				if(Global.CQDX.equals(instArea)){
					getDeviceList(taskId, targetDirectory + "/" + targetFileName);
				}
				
				ftpEnable = LipossGlobals.getLipossProperty("stbSoftUpgrade.ftpEnable");
				// 是否使用FTP
				if("1".equals(ftpEnable)){
					ftpIp = LipossGlobals.getLipossProperty("stbSoftUpgrade.ftpIp");
					ftpName = LipossGlobals.getLipossProperty("stbSoftUpgrade.ftpName");
					ftpPasswd = LipossGlobals.getLipossProperty("stbSoftUpgrade.ftpPasswd");
					ftpPath = LipossGlobals.getLipossProperty("stbSoftUpgrade.ftpPath") 
								+ targetFileName;
					
					File ftpFile = new File(ftpPath);
					FileUtils.copyFile(uploadCustomer, ftpFile);
				}
			} catch (IOException e) {
				logger.error("批量导入升级，上传文件时出错");
			}
			customerFilePath = LipossGlobals.getLipossProperty("cdnip") + request.getContextPath()  
								+ customerFilePath+ "/" + targetFileName;
		}
		logger.warn("SoftUpgardeBIO=" + devicetypeId);
		int ier = dao.batchSoftUpgradeBak(taskId, vendorId, cityId, pathId,
				strategyType, accoid, devicetypeId, ipCheck, ipSG, maccheck,
				macSG, custcheck, custSG, customerFilePath,ftpEnable,ftpIp,
				ftpName,ftpPasswd,ftpPath,check_net,null,null);
		if (ier == 1) {
			return "任务定制成功!";
		} else {
			return "任务定制失败！";
		}
	}

	public String batchSoftUpgradeHNLT(String vendorId, String cityId,
									  String pathId, String strategyType, long accoid,
									  String devicetypeId, String ipCheck, String ipSG, String maccheck,
									  String macSG, String custcheck, String custSG, File uploadCustomer,
									  String uploadFileName4Customer,String check_net,String taskDesc,String taskDetail)
	{
		String customerFilePath = "";

		// ftp配置文件
		String ftpEnable = "";
		String ftpIp = "";
		String ftpName = "";
		String ftpPasswd = "";
		String ftpPath = "";
		long taskId = Math.round(Math.random() * 100000L);

		if (null != uploadCustomer && uploadCustomer.isFile()
				&& uploadCustomer.length() > 0)
		{
			// 如果是导入帐号，将导入帐号的文件放到指定的目录底下
			String targetDirectory = "";
			customerFilePath = "/accountFile";
			if(Global.CQDX.equals(instArea)){
				customerFilePath = "/softAccount";
			}

			String targetFileName = "";
			HttpServletRequest request = null;
			try {
				// 将文件存放到指定的路径中
				request = ServletActionContext.getRequest();
				targetDirectory = ServletActionContext.getServletContext()
						.getRealPath(customerFilePath);
				// 由于传输的文件名中到后台有中文乱码所以存储到数据库更改文件名
				targetFileName = new DateTimeUtil().getYYYYMMDDHHMMSS() + "_"
						+ uploadFileName4Customer;
				File target = new File(targetDirectory, targetFileName);
				FileUtils.copyFile(uploadCustomer, target);

				if(Global.CQDX.equals(instArea)){
					getDeviceList(taskId, targetDirectory + "/" + targetFileName);
				}

				ftpEnable = LipossGlobals.getLipossProperty("stbSoftUpgrade.ftpEnable");
				// 是否使用FTP
				if("1".equals(ftpEnable)){
					ftpIp = LipossGlobals.getLipossProperty("stbSoftUpgrade.ftpIp");
					ftpName = LipossGlobals.getLipossProperty("stbSoftUpgrade.ftpName");
					ftpPasswd = LipossGlobals.getLipossProperty("stbSoftUpgrade.ftpPasswd");
					ftpPath = LipossGlobals.getLipossProperty("stbSoftUpgrade.ftpPath")
							+ targetFileName;

					File ftpFile = new File(ftpPath);
					FileUtils.copyFile(uploadCustomer, ftpFile);
				}
			} catch (IOException e) {
				logger.error("批量导入升级，上传文件时出错");
			}
			customerFilePath = LipossGlobals.getLipossProperty("cdnip") + request.getContextPath()
					+ customerFilePath+ "/" + targetFileName;
		}
		logger.warn("SoftUpgardeBIO=" + devicetypeId);
		int ier = dao.batchSoftUpgradeHNLT(taskId, vendorId, cityId, pathId,
					strategyType, accoid, devicetypeId, ipCheck, ipSG, maccheck,
					macSG, custcheck, custSG, customerFilePath,ftpEnable,ftpIp,
					ftpName,ftpPasswd,ftpPath,check_net,taskDesc,taskDetail);

		if (ier == 1) {
			return "任务定制成功!";
		} else {
			return "任务定制失败！";
		}
	}

	public List getDeviceList(long taskId, String fileName)
	{
		String fileName_ = fileName.substring(fileName.length()-3, fileName.length());
		logger.warn("fileName_:{}",fileName_);
		List<String> dataList = null;
		try{
			if("txt".equals(fileName_)){
				dataList = getImportDataByTXT(fileName);
			}else{
				dataList = getImportDataByXLS(fileName);
				logger.warn("in getDeviceList,dataList.size="+dataList.size());
			}
		}catch(Exception e){
			logger.warn("{}文件解析出错！{}",fileName,e);
			return null;
		}

		List list = null;
		if(dataList.size()<1){
			return null;
		}else{
			dao.insertAccount(taskId, dataList);
		}

		return list;
	}
	
	public List<String> getImportDataByTXT(String fileName) throws FileNotFoundException,IOException
	{
		logger.warn("getImportDataByTXT{}",fileName);
		List<String> list = new ArrayList<String>();
		BufferedReader in = new BufferedReader(new FileReader(fileName));
		
		//山西联通要求读取第一行
		String line = "";
		if(!LipossGlobals.inArea(Global.SXLT)){
			line = in.readLine();
		}
		
		//从第二行开始读取数据，并对每行数据去掉首尾空格
		while ((line = in.readLine()) != null) {
			if(!StringUtil.IsEmpty(line) && !list.contains(line.trim())){
				list.add(line.trim());
			}
		}
		in.close();
		in = null;
		return list;
	}

	/**
	 * 解析文件（xls格式）
	 */
	public List<String> getImportDataByXLS(String fileName) throws BiffException, IOException
	{
		logger.warn("getImportDataByXLS{}",fileName);
		List<String> list = new ArrayList<String>();
		File f = new File(fileName);
		Workbook wwb = Workbook.getWorkbook(f);;
		Sheet ws = null;
		//总sheet数
		//int sheetNumber = wwb.getNumberOfSheets();
		int sheetNumber = 1;
		for (int m=0;m<sheetNumber;m++){
			ws = wwb.getSheet(m);

			//当前页总记录行数和列数
			int rowCount = ws.getRows();

			//取当前页所有值放入list中
			//山西联通要求读取第一行
			int i = 0;
			if(!LipossGlobals.inArea(Global.SXLT)){
				i = 1;
			}
			for (;i<rowCount;i++){
				String temp = ws.getCell(0, i).getContents();
				if(!StringUtil.IsEmpty(temp) && !list.contains(temp.trim())){
					list.add(temp.trim());
				}
			}
		}
		
		f = null;
		return list;
	}
	
	public String batchSoftUpgrade2(String vendorId, String cityId,
			String pathId, String strategyType, long accoid,
			String devicetypeId, String ipCheck, String ipSG, String taskDesc) 
	{
		int ier = dao.batchSoftUpgrade2(vendorId, cityId, pathId, strategyType,
				accoid, devicetypeId, ipCheck, ipSG,taskDesc);
		if (ier == 1) {
			return "任务定制成功!";
		} else {
			return "任务定制失败！";
		}
	}
	
	public String batchSoftUpgradeForHblt(String vendorId, String cityId,
			String pathId, String strategyType, long accoid,
			String devicetypeId, String ipCheck, String ipSG, String maccheck,
			String macSG, String custcheck, String custSG, File uploadCustomer,
			String uploadFileName4Customer) 
	{
		String customerFilePath = "";
		if (null != uploadCustomer && uploadCustomer.isFile()
				&& uploadCustomer.length() > 0) 
		{
			// 如果是导入帐号，将导入帐号的文件放到指定的目录底下
			String targetDirectory = "";
			customerFilePath = "/accountFile";
			String targetFileName = "";
			HttpServletRequest request = null;
			try {
				// 将文件存放到指定的路径中
				request = ServletActionContext.getRequest();
				targetDirectory = ServletActionContext.getServletContext().getRealPath(customerFilePath);
				// 由于传输的文件名中到后台有中文乱码所以存储到数据库更改文件名
				targetFileName = new DateTimeUtil().getYYYYMMDDHHMMSS() + "_"
						+ uploadFileName4Customer;
				File target = new File(targetDirectory, targetFileName);
				FileUtils.copyFile(uploadCustomer, target);
			} catch (IOException e) {
				logger.error("批量导入升级，上传文件时出错");
			}
			customerFilePath = "http://" + request.getLocalAddr() + ":"
					+ request.getServerPort() + "/lims" + customerFilePath
					+ "/" + targetFileName;
		}
		logger.warn("SoftUpgardeBIO=" + devicetypeId);
		int ier = dao.batchSoftUpgradeForHblt(vendorId, cityId, pathId,
				strategyType, accoid, devicetypeId, ipCheck, ipSG, maccheck,
				macSG, custcheck, custSG, customerFilePath);
		if (ier == 1) {
			return "任务定制成功!";
		} else {
			return "任务定制失败！";
		}
	}

	/**
	 * 根据目标版本获取适用版本
	 */
	public String checkVersionByTarget(String pathId) 
	{
		List list = dao.checkVersionByTarget(pathId);
		StringBuffer bf = new StringBuffer();
		
		String deviceModel = "";
		String tempDM = "";
		String tempDTD = "";
		String softwareversion = "";
		for (int i = 0; i < list.size(); i++) {
			Map map = (Map) list.get(i);
			tempDM = StringUtil.getStringValue(map.get("device_model"));
			tempDTD = StringUtil.getStringValue(map.get("devicetype_id"));
			softwareversion = StringUtil.getStringValue(map.get("softwareversion"));
			if (softwareversion == null || " ".equals(softwareversion)) {
				softwareversion = "";
			}
			if (!deviceModel.equalsIgnoreCase(tempDM)) {
				deviceModel = tempDM;
				if (i > 0) {
					bf.append("|");
				}
				bf.append(tempDM + '\1');
				bf.append(tempDTD + "$" + softwareversion);
			} else {
				bf.append("#" + tempDTD + "$" + softwareversion);
			}
		}
		return bf.toString();
	}

	public List getSoftupTask(int curPage_splitPage, int num_splitPage,
			String queryCityId, String queryVendorId, String queryVaild, String taskDesc,
			String source_devicetypeId,String goal_devicetypeId,String startTime,String endTime,String taskType) 
	{
		//山西多属地
		if(LipossGlobals.inArea(Global.SXLT)){
			return dao.getSoftupTaskSxlt(curPage_splitPage, num_splitPage, queryCityId,queryVendorId, 
					queryVaild,taskDesc,source_devicetypeId,goal_devicetypeId,startTime,endTime,taskType);
		}
		else{
			return dao.getSoftupTask(curPage_splitPage, num_splitPage, queryCityId,queryVendorId, 
					queryVaild,taskDesc,source_devicetypeId,goal_devicetypeId,startTime,endTime);
		}
	}

	public int countSoftupTask(int curPage_splitPage, int num_splitPage,
			String queryCityId, String queryVendorId, String queryVaild, 
			String taskDesc,String source_devicetypeId,String softwareversion,String startTime,String endTime,String taskType)
	{
		if(LipossGlobals.inArea(Global.SXLT)){
			return dao.countSoftupTaskSxlt(curPage_splitPage, num_splitPage,queryCityId, queryVendorId, 
					queryVaild,taskDesc,source_devicetypeId,softwareversion,startTime,endTime,taskType);
		}
		else{
			return dao.countSoftupTask(curPage_splitPage, num_splitPage,queryCityId, queryVendorId, 
					queryVaild,taskDesc,source_devicetypeId,softwareversion,startTime,endTime);
		}
		
	}

	/**
	 * 取消任务
	 */
	public String cancerTask(String taskId, String isImport) 
	{
		int ier = dao.cancerTask(taskId,isImport);
		return ier >= 1 ? "1,失效任务成功！" : "0,失效任务失败！";
	}
	
	/**
	 * 删除任务
	 */
	public String deleteTask(String taskId, String isImport) 
	{
		int ier = dao.deleteTask(taskId,isImport);
		return ier >= 1 ? "1,删除任务成功！" : "0,删除任务失败！";
	}
	
	/**
	 * 激活任务
	 */
	public String validTask(String taskId) 
	{
		int ier = dao.validTask(taskId);
		return ier == 1 ? "1,激活任务成功！" : "0,激活任务失败！";
	}

	public Map getCountSoftupTaskResult(String taskId) 
	{
		return dao.getCountResult(taskId);
	}
	
	public Map getCountSoftupTaskResult_v2(String taskId,String importType) 
	{
		return dao.getCountResult_v2(taskId,importType);
	}


	public List<HashMap<String, String>> getCountSoftupTaskResultHnlt(String taskId, String cityId)
	{
		return dao.getCountResultHnlt(taskId,cityId);
	}

	/**
	 * 添加导入任务
	 */
	public void batchExcelUpdate(long userId, List<String> serList,
			long batchId, long addTime, Map<String, String> vendorMap,
			String taskDetail, String strategyName) 
	{
		dao.batchExcelUpdate(userId, serList, batchId, addTime, vendorMap,
				taskDetail, strategyName);
	}

	/**
	 * 根据task_Id,结果查询升级记录
	 */
	public List queryUpRecordByTaskId(String taskID, String result,
			String isAll, int startRow, int countRow, boolean split) 
	{
		return dao.queryUpRecordByTaskId(taskID, result, isAll, startRow,countRow, split);
	}
	
	/**
	 * 根据task_Id,结果查询升级记录
	 */
	public List queryUpRecordByTaskId_import(String taskID, String result,
			String importtype, int startRow, int countRow, boolean split) 
	{
		return dao.queryUpRecordByTaskId_import(taskID, result, importtype, startRow,countRow, split);
	}

	/**
	 * 查询总记录数
	 */
	public int queryCountByTaskId(String taskID, String result, String isAll)
	{
		return dao.queryCountByTaskId(taskID, result, isAll);
	}
	
	/**
	 * 查询总记录数
	 */
	public int queryCountByTaskId_import(String taskID, String result, String importtype)
	{
		return dao.queryCountByTaskId_import(taskID, result, importtype);
	}

	/**
	 * 根据task_Id,结果查询升级记录-湖南联通
	 */
	public List queryUpRecordByTaskIdHnlt(String taskID, String result,
									  String isAll,String cityId, int startRow, int countRow, boolean split)
	{
		return dao.queryUpRecordByTaskIdHnlt(taskID, result, isAll, cityId,startRow,countRow, split);
	}

	/**
	 * 根据task_Id,导出升级记录-湖南联通
	 */
	public List exportUpRecordByTaskIdHnlt(String taskID, String result, String isAll,String cityId)
	{
		return dao.exportUpRecordByTaskIdHnlt(taskID, result, isAll, cityId);
	}


	/**
	 * 查询总记录数-湖南联通
	 */
	public int queryCountByTaskIdHnlt(String taskID, String result, String isAll,String cityId)
	{
		return dao.queryCountByTaskIdHnlt(taskID, result, isAll,cityId);
	}

	/**
	 * 查询策略下详细信息
	 */
	public Map queryTaskById(String taskId,String taskType) 
	{
		return dao.queryTaskById(taskId,taskType);
	}

	/**
	 * 查询策略下的对应IP地址段
	 */
	public List queryIPDuanById(String taskId)
	{
		return dao.queryIPDuanById(taskId);
	}

	public List queryMACById(String taskId) 
	{
		return dao.queryMACById(taskId);
	}
	
	/**
	 * 查询策略适应的软硬件型号
	 */
	public List queryTaskHardSoftById(String taskId) 
	{
		return dao.queryTaskHardSoftById(taskId);
	}

	public List queryBatchExcel(String startTime, String endTime,
			String servAccount, int startRow, int countRow, boolean split) 
	{
		return dao.queryBatchExcel(startTime, endTime, servAccount, startRow,countRow, split);
	}

	public int getBatchExcelRecord(String startTime, String endTime,String servAccount) 
	{
		return dao.getBatchExcelRecord(startTime, endTime, servAccount);
	}

	public List getExcelBatchSoftupTask(int curPage_splitPage,
			int num_splitPage, String queryVaild, String startTime,
			String endTime, String cityId) 
	{
		return dao.getExcelBatchSoftupTask(curPage_splitPage, num_splitPage,
				queryVaild, startTime, endTime, cityId);
	}
	
	public int countExcelBatchSoftupTask(int curPage_splitPage,
			int num_splitPage, String queryVaild, String startTime,
			String endTime, String cityId) 
	{
		return dao.countExcelBatchSoftupTask(curPage_splitPage, num_splitPage,
				queryVaild, startTime, endTime, cityId);
	}
	
	public String cancerExcelBatchTask(String taskId) 
	{
		int ier = dao.cancerExcelBatchTask(taskId);
		return ier == 1 ? "1,失效任务成功！" : "0,失效任务失败！";
	}
	
	/**
	 * 优化批量软件升级
	 */
	public String cancelBatchTask(String taskId, String status) 
	{
		String loadStatus = "-1";
		String message = "-1,加载未完成，禁止失效操作";
		loadStatus = dao.getLoadStatus(taskId);

		if ("1".equals(loadStatus)) 
		{
			if ("1".equals(status)) 
			{
				int ire = dao.validExcelBatchTask(taskId);
				if (ire > 0) {
					SoftTask obj = new SoftTask("WEB",
							StringUtil.getIntegerValue(taskId), 1);
					try {
//						MQPublisher publisher = new MQPublisher(
//								MQ_SOFT_TASK_ENAB, MQ_SOFT_TASK_URL,MQ_SOFT_TASK_TOPIC);
						MQPublisher publisher = new MQPublisher("soft.task");
						publisher.publishMQ(obj);
					} catch (Exception e) {
						e.printStackTrace();
						logger.error(" MQ消息发布失败， mesg({})", e.getMessage());
					}
					message = "1,激活任务成功！";

				} else {
					message = "0,激活任务失败！";
				}
			} else if ("0".equals(status)) {
				boolean flag = dao.cancelBatchTask(taskId);
				if (flag) {
					SoftTask obj = new SoftTask("WEB",
							StringUtil.getIntegerValue(taskId), 2);
					try {
//						MQPublisher publisher = new MQPublisher(
//								MQ_SOFT_TASK_ENAB, MQ_SOFT_TASK_URL,MQ_SOFT_TASK_TOPIC);
						MQPublisher publisher = new MQPublisher("soft.task");
						publisher.publishMQ(obj);
					} catch (Exception e) {
						e.printStackTrace();
						logger.error(" MQ消息发布失败， mesg({})", e.getMessage());
					}
					message = "1,失效任务成功！";

				} else {
					message = "0,失效任务失败！";
				}
			}

		}
		logger.warn("cancelBatchTask()返回结果" + message);
		return message;
	}

	/**
	 * 删除任务
	 */
	public String deleteSoftUpTask(String taskId)
	{
		String message = "-1,删除任务操作失败";
		int ires = this.dao.deleteSoftUpTask(taskId);
		if (ires > 0) {
			SoftTask obj = new SoftTask("WEB",
					StringUtil.getIntegerValue(taskId), 3);
			try {
//				MQPublisher publisher = new MQPublisher(MQ_SOFT_TASK_ENAB,
//						MQ_SOFT_TASK_URL, MQ_SOFT_TASK_TOPIC);
				MQPublisher publisher = new MQPublisher("soft.task");
				publisher.publishMQ(obj);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error(" MQ消息发布失败， mesg({})", e.getMessage());
			}
			message = "1,删除任务成功！";
		}
		return message;
	}
	
	/**
	 * 激活任务
	 */
	public String validExcelBatchTask(String taskId)
	{
		int ier = dao.validExcelBatchTask(taskId);
		return ier == 1 ? "1,激活任务成功！" : "0,激活任务失败！";
	}
	
	/**
	 * 添加导入任务
	 */
	public void batchImportUp(long userId, List<String> serList, long batchId,
			long addTime, String vendorId, String pathId, String taskDetail,
			String strategyName, String deviceModelIds) 
	{
		dao.batchImportUp(userId, serList, batchId, addTime, vendorId, pathId,
				taskDetail, strategyName, deviceModelIds);
	}
	
	/**
	 * 添加导入任务
	 */
	public boolean batchImportSoftUp(long userId, long batchId, long addTime,
			String vendorId, String pathId, String taskDetail,
			String strategyName, String deviceModelIds, String filePath,
			String isHotel, String isVIP, String isOther, String cityId) 
	{
		// 要根据文件的上传的路径将文件保存到服务器
		return dao.batchImportSoftUp(userId, batchId, addTime, vendorId,
				pathId, taskDetail, strategyName, deviceModelIds, filePath,
				isHotel, isVIP, isOther, cityId);
	}

	public List getBatchExcelSTS(String taskID, String cityId)
	{
		return dao.getBatchExcelSTS(taskID, cityId);
	}

	public List queryBatchExcelList(String taskID, String result, int startRow,
			int countRow, boolean split) 
	{
		return dao.queryBatchExcelList(taskID, result, startRow, countRow,split);
	}
	
	/**
	 * 导出设备详细信息
	 */
	public List exportBatchExcelList(String taskID, String result)
	{
		return dao.exportBatchExcelList(taskID, result);
	}

	public int getBatchExcelList(String taskID, String result) 
	{
		return dao.getBatchExcelList(taskID, result);
	}

	public Map getBatchExcelDetail(String taskID)
	{
		return dao.getBatchExcelDetail(taskID);
	}

	public int delete(String taskId)
	{
		return this.dao.delete(taskId);
	}
	
	/**
	 *  下载错误帐号
	 */
	@SuppressWarnings("unchecked")
	public List exportFailAccount(String taskId)
	{
		List reList = new ArrayList();
		// 设备类型
		List lt = dao.getDeviceModel(taskId);
		String tempDeviceModel = "";
		String vendor = "";
		Map tmpMap = null;
		if (null != lt && !lt.isEmpty())
		{
			String vendorId = StringUtil.getStringValue(((Map) lt.get(0)).get("vendor_id"));
			vendor = getVendorMap().get(vendorId);
			for (int i = 0; i < lt.size(); i++) {
				tmpMap = (Map) lt.get(i);
				tempDeviceModel += getDeviceModelMap().get(tmpMap.get("device_model_id"))+ ",";
			}
		}
		if (tempDeviceModel.length() > 1) {
			tempDeviceModel = tempDeviceModel.substring(0,tempDeviceModel.length() - 1);
		}
		List list = dao.exportFailAccount(taskId);
		Map<String, String> reMap = null;
		if (null != list && !list.isEmpty()) 
		{
			for (int i = 0; i < list.size(); i++) 
			{
				Map mp = (Map) list.get(i);
				reMap = new HashMap<String, String>();
				reMap.put("strategyname",StringUtil.getStringValue(mp.get("strategyname")));
				reMap.put("serv_account",StringUtil.getStringValue(mp.get("serv_account")));
				// 失败类型
				int status = StringUtil.getIntegerValue(mp.get("status"));
				String reason = "";
				switch (status) {
					case 0 :
						reason = "未匹配";
						break;
					case 2 :
						reason = "厂商不符";
						break;
					case 3 :
						reason = "型号不符";
						break;
					case 4 :
						reason = "属地不符";
						break;
					case 5 :
						reason = "系统无机顶盒信息";
						break;
					case 6 :
						reason = "酒店或VIP用户或敏感用户";
						break;
					default :
						reason = "符合要求";
						break;
				}
				reMap.put("reason", reason);
				// 用户类型
				int type = StringUtil.getIntegerValue(mp.get("group_type"));
				String groupTye = "";
				switch (type) {
					case 0 :
						groupTye = "未知用户";
						break;
					case 1 :
						groupTye = "酒店用户";
						break;
					case 2 :
						groupTye = "VIP用户";
						break;
					case 3 :
						groupTye = "公众用户";
						break;
					case 4 :
						groupTye = "敏感用户";
						break;
					default :
						break;
				}
				reMap.put("groupType", groupTye);
				reMap.put("dev_sn", StringUtil.getStringValue(mp.get("device_serialnumber")));
				reMap.put("mVendor", vendor);
				String vendorId = StringUtil.getStringValue(mp.get("vendor_id"));
				if (vendorId.equals("")) {
					reMap.put("vendor", "");
				} else {
					reMap.put("vendor", this.getVendorMap().get(vendorId));
				}
				// 策略要求实际型号
				reMap.put("mDeviceMdel", tempDeviceModel);
				// 设备实际型号
				String deviceModelId = StringUtil.getStringValue(mp.get("device_model_id"));
				if (deviceModelId.equals("")) {
					reMap.put("deviceMdel", "");
				} else {
					reMap.put("deviceMdel",getDeviceModelMap().get(deviceModelId));
				}
				// 策略要求属地
				String mCityId = StringUtil.getStringValue(mp.get("m_city_id"));
				if ("".equals(mCityId)) {
					reMap.put("mCityName", "");
				} else {
					reMap.put("mCityName",CityDAO.getCityIdCityNameMap().get(mCityId));
				}
				// 设备属地
				String cityId = StringUtil.getStringValue(mp.get("city_id"));
				if ("".equals(mCityId)) {
					reMap.put("cityName", "");
				} else {
					reMap.put("cityName",CityDAO.getCityIdCityNameMap().get(cityId));
				}
				if (1 == StringUtil.getIntegerValue(mp.get("is_vip"))) {
					reMap.put("isVip", "是");
				} else {
					reMap.put("isVip", "否");
				}
				if (1 == StringUtil.getIntegerValue(mp.get("is_hotel"))) {
					reMap.put("isHotel", "是");
				} else {
					reMap.put("isHotel", "否");
				}
				if (1 == StringUtil.getIntegerValue(mp.get("is_sensitive"))) {
					reMap.put("isSensitive", "是");
				} else {
					reMap.put("isSensitive", "否");
				}
				reList.add(reMap);
			}
		}
		return reList;
	}
	
	public List getHotelUser(String taskId)
	{
		return dao.getHotelUser(taskId);
	}
	
	public List getVIPUser(String taskId) 
	{
		return dao.getVIPUser(taskId);
	}
	
	public List getSenUser(String taskId) 
	{
		return dao.getSenUser(taskId);
	}
	
	public Map<String, String> getVendorMap() 
	{
		Map<String, String> map = new HashMap<String, String>();
		List list = dao.getVendor();
		if (null != list && !list.isEmpty()) {
			for (int i = 0; i < list.size(); i++) {
				Map mp = (Map) list.get(i);
				map.put(StringUtil.getStringValue(mp.get("vendor_id")),
						StringUtil.getStringValue(mp.get("vendor_add")));
			}
		}
		return map;
	}
	
	public Map<String, String> getDeviceModelMap()
	{
		Map<String, String> map = new HashMap<String, String>();
		List list = dao.getDeviceModel();
		if (null != list && !list.isEmpty()) {
			for (int i = 0; i < list.size(); i++) {
				Map mp = (Map) list.get(i);
				map.put(StringUtil.getStringValue(mp.get("device_model_id")),
						StringUtil.getStringValue(mp.get("device_model")));
			}
		}
		return map;
	}
	
	public String getStrategyName(String taskId)
	{
		return dao.getStrategyName(taskId);
	}

	public String getIPString(String targetDirectory, String uploadFileName4IP,File uploadIP) 
	{
		String reString = "";
		try {
			String targetFileName = new Date().getTime() + uploadFileName4IP;
			File target = new File(targetDirectory, targetFileName);
			FileUtils.copyFile(uploadIP, target);
			if (null != target && target.exists()) {
				HSSFWorkbook workbook = new HSSFWorkbook(new FileInputStream(target));
				HSSFSheet sheet = workbook.getSheetAt(0);
				// 获得sheet的总行数
				int rowCount = sheet.getLastRowNum();
				// 循环解析每一行，第一行不取
				for (int i = 1; i <= rowCount; i++) {
					// 获得行对象
					HSSFRow row = sheet.getRow(i);

					HSSFCell cell_start = row.getCell(0);
					String ip_start = getCellString(cell_start);

					HSSFCell cell_end = row.getCell(1);
					String ip_end = getCellString(cell_end);
					if (null != ip_start && null != ip_end) {
						reString += ip_start.trim() + "," + ip_end.trim() + ";";
					}
				}
				if (reString != "") {
					reString = reString.substring(0, reString.length() - 1);
				}
			} else {
				logger.warn("复制文件失败");
			}
		} catch (Exception e) {
			logger.warn("批量导入时异常");
		}
		return reString;
	}

	private String getCellString(HSSFCell cell) 
	{
		String result = null;
		if (cell != null) 
		{
			// 单元格类型：Numeric:0,String:1,Formula:2,Blank:3,Boolean:4,Error:5
			int cellType = cell.getCellType();
			switch (cellType) 
			{
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
	 * 设备批量升级
	 */
	@SuppressWarnings("unchecked")
	public void batchUp(long accoid,String[] deviceId_array,String softStrategy_type,String taskDesc)
	{
		logger.debug("batchUp({},{},{})", new Object[] { accoid, deviceId_array,softStrategy_type });
		
		if(!LipossGlobals.inArea(Global.NXDX))
		{
			// 先入任务表，再入策略表
			dao.batchSoftUpgrade2("-1", "-1", "-1", softStrategy_type,
						accoid, "", "0", "0",taskDesc);
		}
		// 获取配置参数(XML)字符串
		ArrayList<String> sqllist = new ArrayList<String>();
		Map<String, String> softParamMap = dao.getSoftFileInfo();
		Map<String, String> softUpMap = dao.getSoftUp();
		String deviceTypeId = "";
		String devicetype_id = "";
		String softSheet_para = "";
		String taskId = "";
		int size = deviceId_array.length;
		// 软件升级策略ID数组
		String[] strategyId_array = new String[size];
		// 级策略ID
		long id = 0L;
		/** 入策略表 */
		int softType = StringUtil.getIntegerValue(softStrategy_type);
		for (int i = 0; i < size; i++)
		{
			//HBLT-RMS-20190916-LH-064 机顶盒升级黑名单限制
			if (Global.HBLT.equals(instArea))
			{
				Map<String, String> devInfoBlackListMap = dao.getDevBlackListInfo(deviceId_array[i]);
				if (null != devInfoBlackListMap) {
					logger.warn("deviceId=[{}]设备在黑名单中，不予升级", deviceId_array[i]);
					continue;
				}
			}
			
			devicetype_id = dao.getDevicetypeId(deviceId_array[i]);
			deviceTypeId = (String) softUpMap.get("1|" + devicetype_id);
			if (deviceTypeId == null || "".equals(deviceTypeId))
			{
				// 如果映射表没有该devicetype_id的纪录，默认不需要升级
				deviceTypeId = devicetype_id;
				logger.debug("deviceTypeId为空{}", devicetype_id);
			}
			// 目标版本和原设备的版本一致，不需要升级
			if (deviceTypeId.equals(devicetype_id))
			{
				logger.warn("[{}]目标版本和原设备版本一致，不需要升级",deviceId_array[i]);
			}
			else
			{
				if(StringUtil.IsEmpty(softParamMap.get(deviceTypeId)))
				{
					logger.warn("[{}]目标版本未配置升级文件路径，升级失败",deviceId_array[i]);
				}
				else 
				{
					softSheet_para = softUpXml(softParamMap.get(deviceTypeId));
					// 生成入策略的sql
					taskId = dao.getTaskId();
					id = StrategyOBJ.createStrategyId();
					if(StringUtil.IsEmpty(taskId))
					{
						taskId = StringUtil.getStringValue(Math.round(Math.random() * 100000L));
					}
					List sql = inStrategy(id, accoid, softType, deviceId_array[i], 5, 1, 2,
							softSheet_para, taskId, 5, 1,devicetype_id);
					strategyId_array[i] = StringUtil.getStringValue(id);
					sqllist.addAll(sql);
					if(sqllist.size() >= 100)
					{
						DBOperation.executeUpdate(sqllist);
						sqllist.clear();
					}
				}
			}
		}
		
		if (sqllist!=null && sqllist.size()>0) {
			DBOperation.executeUpdate(sqllist);
		}
		
		if (Global.JXDX.equals(instArea))
		{
			/** 入机顶盒导入升级记录表 */
			List sqlList2 = new ArrayList();
			for (int i = 0; i < size; i++)
			{
				taskId = dao.getTaskId();
				long add_time = System.currentTimeMillis() / 1000;
				String sql = dao.inSoftup(taskId, deviceId_array[i], add_time);
				sqlList2.add(sql);
			}
			int[] softupRecode = dao.doBatch(sqlList2);
			logger.warn("入库导入升级记录表成功，入库条数：" + softupRecode.length);
		}
		// 启用一个新的线程来做入库
		//LipossGlobals.ALL_SQL_IPTV.addAll(sqllist);
	}
	
	private String softUpXml(String versionPath)
	{
		Document doc = DocumentHelper.createDocument();
		Element root = doc.addElement("STBSOFT");
		root.addElement("VersionPath").addText(versionPath);
		return doc.asXML();
	}
	
	public List<String> inStrategy(long id, long accoid, int strategyType,
			String deviceId, int serviceId, int orderId, int sheetType, String sheetPara,
			String taskId, int tempId, int isLastOne,String deviceTypeIdOld)
	{
		StrategyOBJ strategyObj = new StrategyOBJ();
		// 策略ID
		strategyObj.setId(id);
		// 策略配置时间
		strategyObj.setTime(TimeUtil.getCurrentTime());
		// 用户id
		strategyObj.setAccOid(accoid);
		// 立即执行
		strategyObj.setType(strategyType);
		// 设备ID
		strategyObj.setDeviceId(deviceId);
		// QOS serviceId
		strategyObj.setServiceId(serviceId);
		// 顺序,默认1
		strategyObj.setOrderId(orderId);
		// 工单类型: 新工单,工单参数为xml串的工单
		strategyObj.setSheetType(sheetType);
		// 参数
		strategyObj.setSheetPara(sheetPara);
		// task_id
		strategyObj.setTaskId(taskId);
		strategyObj.setTempId(tempId);
		strategyObj.setIsLastOne(isLastOne);
		return dao.strategySQL(strategyObj,deviceTypeIdOld);
	}
	
	
	/**
	 * 修改任务描述
	 */
	public String updateTaskDesc(String taskId, String taskDesc) 
	{
		int ier = dao.updateTaskDesc(taskId,taskDesc);
		return ier >= 1 ? "1" : "0";
	}
	
	public String parseFile(String vendorId,String fileName){
		logger.warn("sxlt parse file begin , vendroId = {},filename={}",vendorId,fileName);
		String ret = "";
		String fileName_ = fileName.substring(fileName.length()-3, fileName.length());
		//获取文件路径
		String lipossHome = "";
		String a = FileUploadAction.class.getResource("/").getPath();
		try{
			lipossHome = java.net.URLDecoder.decode(a.substring(0,a.lastIndexOf("WEB-INF")-1),"UTF-8");
		}catch(Exception e){
			logger.error(e.getMessage());
			lipossHome = null;
			ret = "0:"+e.getMessage();
			return ret;
		}
		String path = LipossGlobals.getLipossProperty("work_memory_path");
		if(StringUtil.IsEmpty(path)){
			path = "/temp/";
		}
		fileName = lipossHome + path + fileName;
		
		//开始分析
		List<String> dataList = null;
		try{
			if("txt".equals(fileName_)){
				dataList = getImportDataByTXT(fileName);
			}else{
				dataList = getImportDataByXLS(fileName);
			}
		}catch(Exception e){
			logger.warn("{}文件解析出错！{}",fileName,e);
			ret = "0:"+e.getMessage();
			return ret;
		}

		//入校验库
		if(dataList.size()<1){
			return "0:文件中不存在数据,请重新上传有效文件";
		}else{
			long taskId = Math.round(Math.random() * 100000L);
			String type = dataList.get(0);
			String retType = "servAccount";
			dataList.remove(0);
			int total = dataList.size();
			logger.warn("file.size:{}",total);
			int delNum = 0;
			int retNum = 0;
			if("用户账号".equals(type)){
				//入校验表
				dao.insertAccount(taskId, dataList);
				//删除不满足条件的数据
				delNum = dao.del(taskId, vendorId, "1");
			}else{
				//入校验表
				dao.insertDevsn(taskId, dataList);
				//删除不满足条件的数据
				delNum = dao.del(taskId, vendorId, "0");
				retType = "devsn";
			}
			retNum = total - delNum;
			ret = "1:"+taskId+":"+retType+":"+retNum;
		}
		return ret;
	}
	
	
	public GwStbVendorModelVersionDAO getVmvDaoStb() {
		return vmvDaoStb;
	}
	
	public void setVmvDaoStb(GwStbVendorModelVersionDAO vmvDaoStb) {
		this.vmvDaoStb = vmvDaoStb;
	}
	
	public SoftUpgradeDAO getDao() {
		return dao;
	}

	public void setDao(SoftUpgradeDAO dao) {
		this.dao = dao;
	}

	public void addLog(long user_id, String user_ip, String operation_content,String result) {
		dao.addLog(user_id,user_ip,operation_content,result);
	}
}
