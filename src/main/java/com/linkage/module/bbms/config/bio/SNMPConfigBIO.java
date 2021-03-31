
package com.linkage.module.bbms.config.bio;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.util.TimeUtil;
import com.linkage.litms.LipossGlobals;
import com.linkage.module.gwms.ConstantClass;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.dao.tabquery.SgwSecurityDAO;
import com.linkage.module.gwms.obj.StrategyOBJ;
import com.linkage.module.gwms.obj.tabquery.SgwSecurityOBJ;
import com.linkage.module.gwms.util.CreateObjectFactory;
import com.linkage.module.gwms.util.corba.SuperGatherCorba;
import com.linkage.module.gwms.util.strategy.StrategyXml;

public class SNMPConfigBIO
{

	/**
	 * 日志记录
	 */
	private static Logger logger = LoggerFactory.getLogger(SNMPConfigBIO.class);
	private SgwSecurityDAO sgwSecurityDAO;

	/**
	 * @param sgwSecurityDAO
	 *            the sgwSecurityDAO to set
	 */
	public void setSgwSecurityDAO(SgwSecurityDAO sgwSecurityDAO)
	{
		this.sgwSecurityDAO = sgwSecurityDAO;
	}

	/**
	 * 通过deviceId获得设备SNMP的配置状态
	 * 
	 * @param
	 * @author wangsenbo
	 * @date 2009-10-23
	 * @return 成功返回设备SNMP的配置状态，用";"分隔 失败返回"-1"
	 */
	public String getSNMPConfigStatus(String deviceId)
	{
		logger.debug("getSNMPConfigStatus({})", deviceId);
		int iret = new SuperGatherCorba(LipossGlobals.getGw_Type(deviceId)).getCpeParams(deviceId,
				ConstantClass.GATHER_SNMP);
		// int iret = 1;
		Map rmap = null;
		StringBuffer ajax = new StringBuffer();
		if (iret == 1)
		{
			rmap = sgwSecurityDAO.getSgwSecurity(deviceId);
			if (rmap != null)
			{
				ajax.append(rmap.get("is_enable"));// 是否启用 0
				ajax.append(";").append(rmap.get("snmp_version"));// 协议版本 1
				if (rmap.get("security_username") == null)
				{
					ajax.append(";").append("");// 鉴权用户 2
				}
				else
				{
					ajax.append(";").append(rmap.get("security_username"));// 鉴权用户 2
				}
				ajax.append(";").append(rmap.get("security_model"));// 安全模型 3
				if (rmap.get("engine_id") == null)
				{
					ajax.append(";").append("");// snmp引擎 4
				}
				else
				{
					ajax.append(";").append(rmap.get("engine_id"));// snmp引擎 4
				}
				ajax.append(";").append(rmap.get("context_name"));// 5
				ajax.append(";").append(rmap.get("security_level"));// 安全级别 6
				ajax.append(";").append(rmap.get("auth_protocol"));// 鉴权协议 7
				if (rmap.get("auth_passwd") == null)
				{
					ajax.append(";").append("");// 鉴权密钥 8
				}
				else
				{
					ajax.append(";").append(rmap.get("auth_passwd"));// 鉴权密钥 8
				}
				ajax.append(";").append(rmap.get("privacy_protocol"));// 加解密协议 9
				if (rmap.get("privacy_passwd") == null)
				{
					ajax.append(";").append("");// 私钥 10
				}
				else
				{
					ajax.append(";").append(rmap.get("privacy_passwd"));// 私钥 10
				}
				if (rmap.get("snmp_r_passwd") == null)
				{
					ajax.append(";").append("");// 读口令 11
				}
				else
				{
					ajax.append(";").append(rmap.get("snmp_r_passwd"));
				}
				if (rmap.get("snmp_w_passwd") == null)
				{
					ajax.append(";").append("");// 写口令 12
				}
				else
				{
					ajax.append(";").append(rmap.get("snmp_w_passwd"));// 写口令 12
				}
				logger.debug("SNMP信息：" + ajax.toString());
				return ajax.toString();
			}
		}
		return "-1" + ";" + Global.G_Fault_Map.get(iret);
	}

	/**
	 * 通过 设置设备SNMP的配置状态
	 * 
	 * @param
	 * @author wangsenbo
	 * @date 2009-10-23
	 * @return String
	 */
	public String SNMPConfigSave(long accoid, SgwSecurityOBJ sgwSecurityObj)
	{
		logger.debug("SNMPConfigSave({},{})", accoid, sgwSecurityObj);
		// 获取配置参数(XML)字符串
		String strategyXmlParam = StrategyXml.sgwSecurityOBJ2Xml(sgwSecurityObj);
		/** 入策略表，调预读 */
		// 立即执行
		int strategyType = 0;
		// NTP配置的service_id
		int snmpServiceId = ConstantClass.SNMP;
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
		strategyObj.setDeviceId(sgwSecurityObj.getDeviceId());
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
		if (sgwSecurityDAO.addStrategy(strategyObj))
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
}
