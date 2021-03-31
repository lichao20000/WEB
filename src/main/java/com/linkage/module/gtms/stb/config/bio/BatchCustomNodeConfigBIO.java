
package com.linkage.module.gtms.stb.config.bio;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

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

import com.linkage.commons.util.DateTimeUtil;
import com.linkage.commons.util.TimeUtil;
import com.linkage.litms.common.database.DataSetBean;
import com.linkage.module.gtms.stb.config.dao.BatchCustomNodeConfigDAO;
import com.linkage.module.gtms.stb.obj.tr069.stbBatchConTask;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.obj.StrategyOBJ;
import com.linkage.module.gwms.util.CreateObjectFactory;
import com.linkage.module.gwms.util.StringUtil;
import com.linkage.module.gwms.util.corba.PreProcessCorba;
import com.linkage.module.itms.service.obj.MQPublisher;

public class BatchCustomNodeConfigBIO
{

	private BatchCustomNodeConfigDAO dao = null;
	private static Logger logger = LoggerFactory
			.getLogger(BatchCustomNodeConfigBIO.class);
	
	/**
	 * 定制任务
	 * 
	 * @param taskId
	 * @param cityId
	 * @param vendorId
	 * @param ipCheck
	 * @param macCheck
	 * @param taskName
	 * @param acc_oid
	 * @param addTime
	 * @param ipSG
	 * @param macSG
	 * @param deviceModelIds
	 * @param deviceTypeIds
	 * @param paramNodePath
	 * @param paramValue
	 * @param paramType
	 * @return
	 */
	public String batchNodeConfig(long taskId, String cityId, String vendorId,
			String ipCheck, String macCheck, String taskName, long acc_oid, long addTime,
			String ipSG, String macSG,String custSG,File uploadCustomer,String uploadFileName4Customer,
			String deviceModelIds, String deviceTypeIds,
			String paramNodePath, String paramValue, String paramType, String type)
	{
		String filePath="";
		if ("byBatchCust".equals(custSG) && null != uploadCustomer && uploadCustomer.isFile() && uploadCustomer.length()>0) {
			//如果是导入帐号，将导入帐号的文件放到指定的目录底下
			 String targetDirectory="";
			 filePath = "/accountFile";
			 String targetFileName ="";
			 HttpServletRequest request = null;
			 try {
			 //将文件存放到指定的路径中
			 request = ServletActionContext.getRequest();
			 targetDirectory = ServletActionContext.getServletContext().getRealPath(filePath);
			 //由于传输的文件名中到后台有中文乱码所以存储到数据库更改文件名
	         targetFileName = new DateTimeUtil().getYYYYMMDDHHMMSS() +"_"+ uploadFileName4Customer;
	         File target = new File(targetDirectory, targetFileName);
			 FileUtils.copyFile(uploadCustomer, target);
			} catch (IOException e) {
				logger.error("批量导入升级，上传文件时出错");
			}
	        filePath = "http://"+request.getLocalAddr()+":"+request.getServerPort()+"/lims"+filePath +"/"+targetFileName;
	        logger.warn("filePath:[{}]",filePath);
		}
		
		int ier = dao.batchNodeConfig(taskId, cityId, vendorId, ipCheck, macCheck,
				taskName, acc_oid, addTime, ipSG, macSG, filePath, deviceModelIds, deviceTypeIds,
				paramNodePath, paramValue, paramType, type);
		if (ier == 1)
		{
			return "任务定制成功!";
		}
		else
		{
			return "任务定制失败！";
		}
	}

	public String getIPString(String targetDirectory, String uploadFileName4IP,
			File uploadIP)
	{
		String reString = "";
		try
		{
			String targetFileName = new Date().getTime() + uploadFileName4IP;
			File target = new File(targetDirectory, targetFileName);
			FileUtils.copyFile(uploadIP, target);
			if (null != target && target.exists())
			{
				// 创建工作表
				HSSFWorkbook workbook = new HSSFWorkbook(new FileInputStream(target));
				// 获得sheet
				HSSFSheet sheet = workbook.getSheetAt(0);
				// 获得sheet的总行数
				int rowCount = sheet.getLastRowNum();
				// 循环解析每一行，第一行不取
				for (int i = 1; i <= rowCount; i++)
				{
					// 获得行对象
					HSSFRow row = sheet.getRow(i);
					HSSFCell cell_start = row.getCell(0);
					String ip_start = getCellString(cell_start);
					HSSFCell cell_end = row.getCell(1);
					String ip_end = getCellString(cell_end);
					if (null != ip_start && null != ip_end)
					{
						reString += ip_start.trim() + "," + ip_end.trim() + ";";
					}
				}
				if (reString != "")
				{
					reString = reString.substring(0, reString.length() - 1);
				}
			}
			else
			{
				logger.warn("复制文件失败");
			}
		}
		catch (Exception e)
		{
			logger.warn("批量导入时异常");
		}
		logger.warn("IP--reString=" + reString);
		return reString;
	}

	private String getCellString(HSSFCell cell)
	{
		// TODO Auto-generated method stub
		String result = null;
		if (cell != null)
		{
			// 单元格类型：Numeric:0,String:1,Formula:2,Blank:3,Boolean:4,Error:5
			int cellType = cell.getCellType();
			switch (cellType)
			{
				case HSSFCell.CELL_TYPE_STRING:
					result = cell.getRichStringCellValue().getString();
					break;
				case HSSFCell.CELL_TYPE_NUMERIC:
					result = cell.getNumericCellValue() + "";
					break;
				case HSSFCell.CELL_TYPE_FORMULA:
					result = cell.getCellFormula();
					break;
				case HSSFCell.CELL_TYPE_BOOLEAN:
					break;
				case HSSFCell.CELL_TYPE_BLANK:
					break;
				case HSSFCell.CELL_TYPE_ERROR:
					break;
				default:
					break;
			}
		}
		return result;
	}

	/**
	 * @return 厂商
	 */
	public List getVendorList()
	{
		return dao.getVendor();
	}

	/**
	 * @param vendorId
	 * @return
	 */
	public String getDeviceModel(String vendorId)
	{
		List list = dao.getDeviceModel(vendorId);
		StringBuffer bf = new StringBuffer();
		for (int i = 0; i < list.size(); i++)
		{
			Map map = (Map) list.get(i);
			if (i > 0)
				bf.append("#");
			bf.append(map.get("device_model_id"));
			bf.append("$");
			bf.append(map.get("device_model"));
		}
		return bf.toString();
	}

	/**
	 * @param deviceModelId
	 * @return
	 */
	public String getSoftVersion(String deviceModelId)
	{
		List list = dao.getVersionList(deviceModelId);
		StringBuffer bf = new StringBuffer();
		for (int i = 0; i < list.size(); i++)
		{
			Map map = (Map) list.get(i);
			if (i > 0)
				bf.append("#");
			bf.append(map.get("devicetype_id"));
			bf.append("$");
			bf.append(map.get("softwareversion"));
		}
		return bf.toString();
	}

	/**
	 * 定制任务
	 * 
	 * @param taskId
	 * @param cityId
	 * @param vendorId
	 * @param ipCheck
	 * @param macCheck
	 * @param taskName
	 * @param acc_oid
	 * @param addTime
	 * @param ipSG
	 * @param macSG
	 * @param deviceModelIds
	 * @param deviceTypeIds
	 * @param paramNodePath
	 * @param paramValue
	 * @param paramType
	 * @return
	 */
	public String batchNodeConfigByITV(long taskId, String cityId, String vendorId,
			String taskName, long acc_oid, long addTime, String deviceModelIds,
			String deviceTypeIds, String paramNodePath, String paramValue,
			String paramType)
	{
		int ier = dao.batchNodeConfigByITV(taskId, cityId, vendorId, taskName, acc_oid,
				addTime, deviceModelIds, deviceTypeIds, paramNodePath, paramValue,
				paramType);
		if (ier == 1)
		{
			int result = sendMq(StringUtil.getStringValue(taskId));
			if (result == 1)
			{
				return "任务下发成功!";
			}
			else
			{
				return "任务下发失败！";
			}
		}
		else
		{
			return "任务入库失败！";
		}
	}

	public String singleNodeConfigByITV(String[] deviceIds, String serviceId,
			String[] paramArr)
	{
		String strategyXmlParam = "";
		logger.debug("XML: " + strategyXmlParam);
		strategyXmlParam = paramNodeBXml(paramArr);
		/** 入策略表，调预读 */
		ArrayList<String> sqllist = new ArrayList<String>();
		String deviceId = deviceIds[0];
		List<Map<String, String>> list = dao.getDeviceInfo(deviceId);
		String oui = StringUtil.getStringValue(list.get(0), "oui");
		String device_serialnumber = StringUtil.getStringValue(list.get(0),
				"device_serialnumber");
		String username = StringUtil.getStringValue(list.get(0), "username");
		String[] stragetyIds = new String[deviceIds.length];
		// 配置的service_id
		int serviceId_int = Integer.parseInt(serviceId);
		for (int i = 0; i < deviceIds.length; i++)
		{
			StrategyOBJ strategyObj = new StrategyOBJ();
			// 策略ID
			strategyObj.createId();
			// 策略配置时间
			strategyObj.setTime(TimeUtil.getCurrentTime());
			// 用户id
			strategyObj.setAccOid(1);
			// 任务ID
			strategyObj.setTaskId(StringUtil.getStringValue(TimeUtil.getCurrentTime()));
			// 立即执行
			strategyObj.setType(1);
			// 设备ID
			strategyObj.setDeviceId(deviceIds[i]);
			// OUI
			strategyObj.setOui(oui);
			// 设备序列号
			strategyObj.setSn(device_serialnumber);
			// loid
			strategyObj.setUsername(username);
			// serviceId
			strategyObj.setServiceId(serviceId_int);
			// 顺序,默认1
			strategyObj.setOrderId(1);
			// 工单类型: 新工单,工单参数为xml串的工单
			strategyObj.setSheetType(2);
			// 参数
			strategyObj.setSheetPara(strategyXmlParam);
			strategyObj.setTempId(serviceId_int);
			strategyObj.setIsLastOne(1);
			stragetyIds[i] = String.valueOf(strategyObj.getId());
			// 入策略表
			sqllist.addAll(dao.stbStrategySQL(strategyObj));
		}
		// 立即执行
		int iCode[] = DataSetBean.doBatch(sqllist);
		if (iCode != null && iCode.length > 0) {
			boolean flag = CreateObjectFactory.createPreProcess(Global.GW_TYPE_STB).processOOBatch(stragetyIds);
			logger.warn("单台执行策略入库：  成功");
			if (flag) {
				logger.warn("单台执行策略结果成功");
				return "1";
			} else {
				logger.warn("单台执行策略结果失败");
				return "-1";
			}
		} else {
			logger.warn("单台执行策略入库：  失败");
			return "-1";
		}
	}

	public String paramNodeBXml(String[] paramArr)
	{
		String strXml = null;
		// new root
		Document doc = DocumentHelper.createDocument();
		// root node
//		Element root = doc.addElement("parameterConfig");
		Element root = doc.addElement("STB");
		Element parameterList = root.addElement("parameterList");
		for (int i = 0; i < paramArr.length; i++)
		{
			String parameterStr = paramArr[i];
			if (parameterStr == null || parameterStr.split("ailk!@#").length != 3)
			{
				logger.warn("[{}]批量配置参数的参数不正确,结束");
				logger.error("[{}]批量配置参数的参数不正确,结束");
				return null;
			}
			else
			{
				String[] parameterArr = parameterStr.split("ailk!@#");
				Element parameterEle = parameterList.addElement("parameter");
				parameterEle.addElement("name").addText(parameterArr[0]);
				parameterEle.addElement("value").addText(parameterArr[1]);
				parameterEle.addElement("type").addText(parameterArr[2]);
			}
		}
		strXml = root.asXML();
		return strXml;
	}

	/**
	 * 查询策略表中未作的数量
	 * 
	 * @return
	 */
	public int queryUndoNum()
	{
		logger.debug("queryUndoNum()");
		return dao.queryUndoNum();
	}

	/**
	 * 发送mq
	 * 
	 * @param taskId
	 * @return
	 */
	public int sendMq(String taskId)
	{
		try
		{
			logger.warn("sendMq({})", taskId);
			stbBatchConTask addobj = new stbBatchConTask("WEB", taskId, "1");
//			MQPublisher publisher = new MQPublisher("1",
//					LipossGlobals.getLipossProperty("stbParamNodeBatchCon.url"),
//					LipossGlobals.getLipossProperty("stbParamNodeBatchCon.topic"));
			MQPublisher publisher = new MQPublisher("stbBatchCon.task");
			publisher.publishMQ(addobj);
			return 1;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			logger.error(" MQ消息发布失败， mesg({})", e.getMessage());
			// 更新为未做 下次继续轮训处理
			dao.deleteBatchServAccount(taskId);
			dao.updateTaskStatus(taskId, 0);
			return -1;
		}
	}
	

	public void setDao(BatchCustomNodeConfigDAO dao)
	{
		this.dao = dao;
	}
}
