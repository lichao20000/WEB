
package com.linkage.module.bbms.config.bio;

import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.util.TimeUtil;
import com.linkage.litms.LipossGlobals;
import com.linkage.module.gwms.ConstantClass;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.dao.gw.GwTr069DAO;
import com.linkage.module.gwms.obj.StrategyOBJ;
import com.linkage.module.gwms.obj.gw.GwTr069OBJ;
import com.linkage.module.gwms.util.CreateObjectFactory;
import com.linkage.module.gwms.util.StringUtil;
import com.linkage.module.gwms.util.corba.SuperGatherCorba;

public class TR069ConfigBIO
{

	/**
	 * 日志记录
	 */
	private static Logger logger = LoggerFactory.getLogger(TR069ConfigBIO.class);
	private GwTr069DAO gwTr069Dao;

	public String getTR069(String deviceId)
	{
		logger.debug("getTR069({})", deviceId);
		int iret = new SuperGatherCorba(LipossGlobals.getGw_Type(deviceId)).getCpeParams(deviceId,
				ConstantClass.GATHER_MANAGE_SERVER);
		// int iret = 1;
		Map rmap = null;
		StringBuffer ajax = new StringBuffer();
		if (iret == 1)
		{
			rmap = gwTr069Dao.getTR069(deviceId);
			if (rmap != null)
			{
				// 0 标志位 1表示成功 -1表示失败
				ajax.append("1");
				// 1 ACS URL
				ajax.append(";").append(rmap.get("url") == null ? "" : rmap.get("url"));
				// 2 ACS 用户名
				ajax.append(";").append(
						rmap.get("username") == null ? "" : rmap.get("username"));
				// 3 ACS 用户密码
				ajax.append(";").append(
						rmap.get("passwd") == null ? "" : rmap.get("passwd"));
				// 4 CPE 用户名
				ajax.append(";").append(
						rmap.get("conn_req_username") == null ? "" : rmap
								.get("conn_req_username"));
				// 5 CPE 用户密码
				ajax.append(";").append(
						rmap.get("conn_req_passwd") == null ? "" : rmap
								.get("conn_req_passwd"));
				// 6 周期上报开关
				ajax.append(";").append(rmap.get("peri_inform_enable"));
				// 7 上报周期
				ajax.append(";").append(rmap.get("peri_inform_interval"));
				logger.debug("TR069信息：" + ajax.toString());
				return ajax.toString();
			}
			else
			{
				return "-1;没有采集到信息";
			}
		}
		else
		{
			logger.debug("采集失败");
			String message = Global.G_Fault_Map.get(iret).getFaultReason();
			if (null == message)
			{
				message = Global.G_Fault_Map.get(100000).getFaultReason();
			}
			return "-1;" + message;
		}
	}

	/**
	 * @author wangsenbo
	 * @date Jan 27, 2010
	 * @param flag=0：编辑所有参数
	 *            flag=1：只编辑上报周期相关 flag=2：只更改ACS地址 flag=3：同时更改ACS地址和ACS相关
	 * @return String
	 */
	public String obj2Xml(GwTr069OBJ obj, String flag)
	{
		logger.debug("obj2Xml(GwTr069OBJ:{})", obj);
		String strXml = null;
		// new doc
		Document doc = DocumentHelper.createDocument();
		// root node: NET
		Element root = doc.addElement("TR069");
		root.addAttribute("flag", flag);
		if ("0".equals(flag))
		{
			root.addElement("ACSURL").addText(
					true == StringUtil.IsEmpty(obj.getUrl()) ? "" : obj.getUrl());
			root.addElement("Username").addText(
					true == StringUtil.IsEmpty(obj.getAcsUsername()) ? "" : obj
							.getAcsUsername());
			root.addElement("Password").addText(
					true == StringUtil.IsEmpty(obj.getAcsPasswd()) ? "" : obj
							.getAcsPasswd());
			root.addElement("PeriodicInformEnable").addText(
					true == StringUtil.IsEmpty(obj.getPeriInformEnable()) ? "0" : obj
							.getPeriInformEnable());
			root.addElement("PeriodicInformInterval").addText(
					true == StringUtil.IsEmpty(obj.getPeriInformInterval()) ? "" : obj
							.getPeriInformInterval());
			root.addElement("PeriodicInformTime").addText("");
			root.addElement("ConnectionRequestUsername").addText(
					true == StringUtil.IsEmpty(obj.getCpeUsername()) ? "" : obj
							.getCpeUsername());
			root.addElement("ConnectionRequestPassword ").addText(
					true == StringUtil.IsEmpty(obj.getCpePasswd()) ? "" : obj
							.getCpePasswd());
		}
		if ("1".equals(flag))
		{
			root.addElement("ACSURL").addText("");
			root.addElement("Username").addText("");
			root.addElement("Password").addText("");
			root.addElement("PeriodicInformEnable").addText(
					true == StringUtil.IsEmpty(obj.getPeriInformEnable()) ? "0" : obj
							.getPeriInformEnable());
			root.addElement("PeriodicInformInterval").addText(
					true == StringUtil.IsEmpty(obj.getPeriInformInterval()) ? "" : obj
							.getPeriInformInterval());
			root.addElement("PeriodicInformTime").addText("");
			root.addElement("ConnectionRequestUsername").addText("");
			root.addElement("ConnectionRequestPassword ").addText("");
		}
		if ("2".equals(flag))
		{
			root.addElement("ACSURL").addText(
					true == StringUtil.IsEmpty(obj.getUrl()) ? "" : obj.getUrl());
			root.addElement("Username").addText("");
			root.addElement("Password").addText("");
			root.addElement("PeriodicInformEnable").addText("");
			root.addElement("PeriodicInformInterval").addText("");
			root.addElement("PeriodicInformTime").addText("");
			root.addElement("ConnectionRequestUsername").addText("");
			root.addElement("ConnectionRequestPassword ").addText("");
		}
		if ("3".equals(flag))
		{
			root.addElement("ACSURL").addText(
					true == StringUtil.IsEmpty(obj.getUrl()) ? "" : obj.getUrl());
			root.addElement("Username").addText(
					true == StringUtil.IsEmpty(obj.getAcsUsername()) ? "" : obj
							.getAcsUsername());
			root.addElement("Password").addText(
					true == StringUtil.IsEmpty(obj.getAcsPasswd()) ? "" : obj
							.getAcsPasswd());
			root.addElement("PeriodicInformEnable").addText("");
			root.addElement("PeriodicInformInterval").addText("");
			root.addElement("PeriodicInformTime").addText("");
			root.addElement("ConnectionRequestUsername").addText(
					true == StringUtil.IsEmpty(obj.getCpeUsername()) ? "" : obj
							.getCpeUsername());
			root.addElement("ConnectionRequestPassword ").addText(
					true == StringUtil.IsEmpty(obj.getCpePasswd()) ? "" : obj
							.getCpePasswd());
		}
		strXml = doc.asXML();
		return strXml;
	}

	public String TR069ConfigSave(long accoid, GwTr069OBJ obj, String manageFlag)
	{
		logger.debug("TR069ConfigSave({},{},{})",
				new Object[] { accoid, obj, manageFlag });
		// 获取配置参数(XML)字符串
		String strategyXmlParam = obj2Xml(obj, manageFlag);
		/** 入策略表，调预读 */
		// 立即执行
		int strategyType = 0;
		// NTP配置的service_id
		int snmpServiceId = ConstantClass.TR069;
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
		strategyObj.setDeviceId(obj.getDeviceId());
		// QOS serviceId
		strategyObj.setServiceId(snmpServiceId);
		// 顺序,默认1
		strategyObj.setOrderId(1);
		// 工单类型: 新工单,工单参数为xml串的工单
		strategyObj.setSheetType(2);
		// 参数
		strategyObj.setSheetPara(strategyXmlParam);
		strategyObj.setTempId(snmpServiceId);
		strategyObj.setIsLastOne(1);
		// 入策略表
		if (gwTr069Dao.addStrategy(strategyObj))
		{
			// 调用预读
			if (true == CreateObjectFactory.createPreProcess(Global.GW_TYPE_ITMS).processOOBatch(String.valueOf(strategyObj
					.getId())))
			{
				logger.debug("成功");
				return 1 + ";调用后台成功;" + strategyObj.getId();
			}
			else
			{
				logger.warn("调用预读失败");
				return -1 + ";调用后台失败";
			}
		}
		else
		{
			logger.warn("策略入库失败");
			return -1 + ";策略入库失败";
		}
	}

	/**
	 * @return the gwTr069Dao
	 */
	public GwTr069DAO getGwTr069Dao()
	{
		return gwTr069Dao;
	}

	/**
	 * @param gwTr069Dao
	 *            the gwTr069Dao to set
	 */
	public void setGwTr069Dao(GwTr069DAO gwTr069Dao)
	{
		this.gwTr069Dao = gwTr069Dao;
	}
}
