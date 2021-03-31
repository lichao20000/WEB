
package com.linkage.module.bbms.config.bio;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.util.TimeUtil;
import com.linkage.litms.LipossGlobals;
import com.linkage.module.gwms.ConstantClass;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.dao.gw.LanEthDAO;
import com.linkage.module.gwms.dao.gw.LanVlanDAO;
import com.linkage.module.gwms.dao.gw.WlanDAO;
import com.linkage.module.gwms.obj.StrategyOBJ;
import com.linkage.module.gwms.obj.gw.LanVlanOBJ;
import com.linkage.module.gwms.util.CreateObjectFactory;
import com.linkage.module.gwms.util.corba.SuperGatherCorba;
import com.linkage.module.gwms.util.strategy.StrategyXml;

public class VlanDhcpConfigBIO
{

	/**
	 * 日志记录
	 */
	private static Logger logger = LoggerFactory.getLogger(VlanDhcpConfigBIO.class);
	private LanVlanDAO lanVlanDao;
	private LanEthDAO lanEthDao;
	private WlanDAO wlanDao;

	/**
	 * 获取DHCP配置信息的List
	 * 
	 * @author wangsenbo
	 * @date Sep 28, 2009
	 * @return List 失败返回null
	 */
	public List<LanVlanOBJ> getDHCPConfigStatus(String deviceId)
	{
		logger.debug("getDHCPConfigStatus({})", deviceId);
		List<LanVlanOBJ> list = lanVlanDao.getLanVlanList(deviceId);
		return list;
	}

	/**
	 * @return the lanVlanDao
	 */
	public LanVlanDAO getLanVlanDao()
	{
		return lanVlanDao;
	}

	/**
	 * @param lanVlanDao
	 *            the lanVlanDao to set
	 */
	public void setLanVlanDao(LanVlanDAO lanVlanDao)
	{
		this.lanVlanDao = lanVlanDao;
	}

	/**
	 * 获取Vlan配置信息的List
	 * 
	 * @author wangsenbo
	 * @date Sep 28, 2009
	 * @return List 失败返回null
	 */
	public List<LanVlanOBJ> getVlanConfigStatus(String deviceId)
	{
		logger.debug("getVlanConfigStatus({})", deviceId);
		List<LanVlanOBJ> list = lanVlanDao.getLanVlanList(deviceId);
		return list;
	}

	/**
	 * 获取Vlan数
	 * 
	 * @author wangsenbo
	 * @date Oct 28, 2009
	 * @return Map
	 */
	public Map getVlanNum(String deviceId)
	{
		logger.debug("getVlanNum({})", deviceId);
		Map rmap = lanVlanDao.getVlanNum(deviceId);
		return rmap;
	}

	/**
	 * 获取lan配置信息的List
	 * 
	 * @author wangsenbo
	 * @date Sep 28, 2009
	 * @return lanList 失败返回null
	 */
	public List getLanEth(String deviceId)
	{
		logger.debug("getLanEth({})", deviceId);
		List lanList = lanEthDao.getLanEth(deviceId);
		if (lanList == null)
		{
			int iret = new SuperGatherCorba(LipossGlobals.getGw_Type(deviceId)).getCpeParams(deviceId,
					ConstantClass.GATHER_LAN_ETHERNET);
			if (iret == 1)
			{
				lanList = lanEthDao.getLanEth(deviceId);
			}
		}
		return lanList;
	}

	/**
	 * 获取wlan配置信息的List
	 * 
	 * @author wangsenbo
	 * @date Sep 28, 2009
	 * @return List 失败返回null
	 */
	public List getWlan(String deviceId)
	{
		logger.debug("getWlan({})", deviceId);
		List wlanList = wlanDao.getWlan(deviceId);
		if (wlanList == null)
		{
			int iret = new SuperGatherCorba(LipossGlobals.getGw_Type(deviceId)).getCpeParams(deviceId,
					ConstantClass.GATHER_LAN_WLAN);
			if (iret == 1)
			{
				wlanList = wlanDao.getWlan(deviceId);
			}
		}
		return wlanList;
	}

	/**
	 * @return the lanEthDao
	 */
	public LanEthDAO getLanEthDao()
	{
		return lanEthDao;
	}

	/**
	 * @param lanEthDao
	 *            the lanEthDao to set
	 */
	public void setLanEthDao(LanEthDAO lanEthDao)
	{
		this.lanEthDao = lanEthDao;
	}

	/**
	 * @return the wlanDao
	 */
	public WlanDAO getWlanDao()
	{
		return wlanDao;
	}

	/**
	 * @param wlanDao
	 *            the wlanDao to set
	 */
	public void setWlanDao(WlanDAO wlanDao)
	{
		this.wlanDao = wlanDao;
	}

	/**
	 * 配置DHCP
	 * 
	 * @author wangsenbo
	 * @date Oct 30, 2009
	 * @return String
	 */
	public String configDHCP(long accoid, LanVlanOBJ lanVlanObj)
	{
		logger.debug("configDHCP({},{})", accoid, lanVlanObj);
		// 获取配置参数(XML)字符串
		String strategyXmlParam = StrategyXml.DHCP2Xml(lanVlanObj);
		/** 入策略表，调预读 */
		// 立即执行
		int strategyType = 0;
		// NTP配置的service_id
		int dhcpServiceId = ConstantClass.DHCP;
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
		strategyObj.setDeviceId(lanVlanObj.getDeviceId());
		// QOS serviceId
		strategyObj.setServiceId(dhcpServiceId);
		// 顺序,默认1
		strategyObj.setOrderId(1);
		// 工单类型: 新工单,工单参数为xml串的工单
		strategyObj.setSheetType(2);
		// 参数
		strategyObj.setSheetPara(strategyXmlParam);
		strategyObj.setTempId(dhcpServiceId);
		strategyObj.setIsLastOne(1);
		// 入策略表
		if (lanVlanDao.addStrategy(strategyObj))
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
	 * 配置VLAN
	 * 
	 * @author wangsenbo
	 * @date Oct 30, 2009
	 * @return String
	 */
	public String configVlan(long accoid, LanVlanOBJ lanVlanObj, String type)
	{
		logger.debug("configVlan({},{},{})", new Object[] { accoid, lanVlanObj, type });
		// 获取配置参数(XML)字符串
		String strategyXmlParam = StrategyXml.VLAN2Xml(type, lanVlanObj);
		/** 入策略表，调预读 */
		// 立即执行
		int strategyType = 0;
		// NTP配置的service_id
		int vlanServiceId = ConstantClass.VLAN;
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
		strategyObj.setDeviceId(lanVlanObj.getDeviceId());
		// QOS serviceId
		strategyObj.setServiceId(vlanServiceId);
		// 顺序,默认1
		strategyObj.setOrderId(1);
		// 工单类型: 新工单,工单参数为xml串的工单
		strategyObj.setSheetType(2);
		// 参数
		strategyObj.setSheetPara(strategyXmlParam);
		strategyObj.setTempId(vlanServiceId);
		strategyObj.setIsLastOne(1);
		// 入策略表
		if (lanVlanDao.addStrategy(strategyObj))
		{
			// 调用预读
			if (true == CreateObjectFactory.createPreProcess(Global.GW_TYPE_ITMS).processOOBatch(String.valueOf(strategyObj
					.getId())))
			{
				logger.debug("成功");
				return 1+";调用后台成功;"+strategyObj.getId();
			}
			else
			{
				logger.warn("调用预读失败");
				return -1+";调用后台失败";
			}
		}
		else
		{
			logger.warn("策略入库失败");
			return -1+";策略入库失败";
		}
	}
}
