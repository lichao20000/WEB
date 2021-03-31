
package com.linkage.module.gwms.config.bio;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.util.TimeUtil;
import com.linkage.litms.LipossGlobals;
import com.linkage.litms.common.StaticTypeCommon;
import com.linkage.module.gwms.ConstantClass;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.config.dao.DevManageDAO;
import com.linkage.module.gwms.obj.StrategyOBJ;
import com.linkage.module.gwms.util.CreateObjectFactory;
import com.linkage.module.gwms.util.StringUtil;

public class DevManageBIO
{

	/** log */
	private static Logger logger = LoggerFactory.getLogger(DevManageBIO.class);
	private DevManageDAO dao;

	public void ItmsToBbms(long accoid, String[] deviceId_array, String url)
	{
		logger
				.debug("ItmsToBbms({},{},{})",
						new Object[] { accoid, deviceId_array, url });
		// 获取配置参数(XML)字符串
		String xml = Xml(url);
		ArrayList<String> sqllist = new ArrayList<String>();
		int size = deviceId_array.length;
		// 策略ID数组
		String[] strategyId_array = new String[size];
		/** 入策略表 */
		// 下次连到系统
		int strategyType = 4;
		// service_id
		int serviceId = ConstantClass.ManagementServer;
		for (int i = 0; i < size; i++)
		{
			StrategyOBJ strategyObj = new StrategyOBJ();
			// 策略ID
			strategyObj.createId();
			// 策略配置时间
			strategyObj.setTime(TimeUtil.getCurrentTime());
			// 用户id
			strategyObj.setAccOid(accoid);
			// 立即执行
			strategyObj.setType(strategyType);
			// 设备ID
			strategyObj.setDeviceId(deviceId_array[i]);
			// QOS serviceId
			strategyObj.setServiceId(serviceId);
			// 顺序,默认1
			strategyObj.setOrderId(1);
			// 工单类型: 新工单,工单参数为xml串的工单
			strategyObj.setSheetType(2);
			// 参数
			strategyObj.setSheetPara(xml);
			strategyObj.setTempId(serviceId);
			strategyObj.setIsLastOne(1);
			// 生成入策略的sql
			sqllist.addAll(dao.strategySQL(strategyObj));
			strategyId_array[i] = StringUtil.getStringValue(strategyObj.getId());
		}
		LipossGlobals.ALL_SQL_IPTV.addAll(sqllist);
		// if (dao.doSQLList(sqllist))
		// {
		// }
		// else
		// {
		// logger.warn("策略入库失败");
		// return "策略入库失败";
		// }
	}

	private String Xml(String url)
	{
		logger.debug("Xml({})", url);
		String strXml = null;
		// new doc
		Document doc = DocumentHelper.createDocument();
		// root node: NET
		Element root = doc.addElement("Manage");
		root.addElement("ACSURL").addText(url);
		strXml = doc.asXML();
		return strXml;
	}

	/**
	 * @return the dao
	 */
	public DevManageDAO getDao()
	{
		return dao;
	}

	/**
	 * @param dao
	 *            the dao to set
	 */
	public void setDao(DevManageDAO dao)
	{
		this.dao = dao;
	}

	/**
	 * 设备IP转URL的软件升级和转换
	 * 
	 * @author wangsenbo
	 * @date Dec 24, 2009
	 * @return void
	 */
	@SuppressWarnings("unchecked")
	public void ipToUrl(long accoid, String[] deviceId_array, String softStrategy_type,
			String acsUrl)
	{
		logger.debug("ItmsToBbms({},{},{})", new Object[] { accoid, deviceId_array,
				acsUrl });
		// 获取配置参数(XML)字符串
		String xml = Xml(acsUrl);
		ArrayList<String> sqllist = new ArrayList<String>();
		Map softParamMap = dao.getSoftFileInfo();
		Map softUpMap = dao.getSoftUp();
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
		// 下次连到系统
		int strategyType = 4;
		// service_id
		int serviceId = ConstantClass.ManagementServer;
		for (int i = 0; i < size; i++)
		{
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
				softSheet_para = deviceTypeId + "|||";
			}
			else
			{
				softSheet_para = deviceTypeId + (String) softParamMap.get(deviceTypeId);
			}
			// 生成入策略的sql
			taskId = StringUtil.getStringValue(StaticTypeCommon.generateLongId());
			id = StrategyOBJ.createStrategyId();
			List<String> sql1 = inStrategy(id, accoid, softType, deviceId_array[i], 5, 1, 1,
					softSheet_para, taskId,3,0);
			strategyId_array[i] = StringUtil.getStringValue(id);
			id = StrategyOBJ.createStrategyId();
			List<String> sql2 = inStrategy(id, accoid, strategyType, deviceId_array[i],
					serviceId, 2, 2, xml, taskId,3,1);
			sqllist.addAll(sql1);
			sqllist.addAll(sql2);
		}
		if (softType == 0)
		{
			// 如果立即执行，则马上入库，并调用预处理
			if (dao.doSQLList(sqllist))
			{
				// 调用预处理
				logger.debug("立即执行，开始调用预处理...");
				invokePreProcess(strategyId_array);
				for (String z : strategyId_array)
				{
					logger.debug("ZZZZZZZZZZZZZZZ---:" + z);
				}
			}
		}
		else
		{
			// 启用一个新的线程来做入库
			LipossGlobals.ALL_SQL_IPTV.addAll(sqllist);
		}
	}

	public List<String> inStrategy(long id, long accoid, int strategyType, String deviceId,
			int serviceId, int orderId, int sheetType, String sheetPara, String taskId,int tempId,int isLastOne )
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
		return dao.strategySQL(strategyObj);
	}

	/**
	 * 开始调用预读模块
	 * 
	 * @param invokeStruct
	 */
	private void invokePreProcess(String[] idArr)
	{
		if(null != idArr){
			CreateObjectFactory.createPreProcess(Global.GW_TYPE_ITMS).processOOBatch(idArr);
		}
	}

	/**
	 * 0:立即执行 1：第一次连到系统 2：周期上报 3：重新启动 4：下次连到系统 5：终端启动 执行策略的方式
	 * 
	 * @author Jason(3412)
	 * @date 2008-12-17
	 * @return String
	 */
	@SuppressWarnings("unchecked")
	public String getStrategyList(String name, String... typeIds)
	{
		StringBuffer tmpBufer = new StringBuffer();
		Map<String, String> map = dao.getStrategyType(typeIds);
		tmpBufer
				.append("<SELECT name=\"" + name + "\" class=\"bk\" style='width:150px'>");
		Set<String> set = map.keySet();
		for (String tempid : set)
		{
			tmpBufer.append("<OPTION value='").append(tempid);
			if ("5".equals(tempid))
			{
				tmpBufer.append("' selected>");
			}
			else
			{
				tmpBufer.append("'>");
			}
			tmpBufer.append(map.get(tempid)).append("</OPTION>");
		}
		tmpBufer.append("</SELECT>");
		// tmpBufer.append("<OPTION value=\"0\">立即执行</OPTION>");
		// tmpBufer.append("<OPTION value=\"1\">第一次连到系统</OPTION>");
		// tmpBufer.append("<OPTION value=\"2\">周期上报</OPTION>");
		// tmpBufer.append("<OPTION value=\"3\">重新启动</OPTION>");
		// tmpBufer.append("<OPTION value=\"4\">下次连接到系统时自动配置</OPTION>");
		// tmpBufer.append("<OPTION value=\"5\" selected>终端启动</OPTION>");
		// tmpBufer.append("</SELECT>");
		return tmpBufer.toString();
	}
}
