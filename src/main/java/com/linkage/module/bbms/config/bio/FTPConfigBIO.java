
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
import com.linkage.module.gwms.dao.gw.ServiceManageDAO;
import com.linkage.module.gwms.obj.StrategyOBJ;
import com.linkage.module.gwms.util.CreateObjectFactory;
import com.linkage.module.gwms.util.corba.SuperGatherCorba;

public class FTPConfigBIO
{

	/**
	 * 日志记录
	 */
	private static Logger logger = LoggerFactory.getLogger(FTPConfigBIO.class);
	private ServiceManageDAO serviceManageDao;

	public String getFTP(String deviceId)
	{
		logger.debug("getFTP({})", deviceId);
		int iret = new SuperGatherCorba(LipossGlobals.getGw_Type(deviceId)).getCpeParams(deviceId,
				ConstantClass.GATHER_DEVICEINFO_FTP);
//		int iret = 1;
		Map rmap = null;
		StringBuffer ajax = new StringBuffer();
		if (iret == 1)
		{
			rmap = serviceManageDao.getFTP(deviceId);
			if (rmap != null)
			{
				// 0 服务状态
				ajax.append(rmap.get("ftp_enable"));
				// 1 FTP用户名
				ajax.append(";").append(
						rmap.get("ftp_username") == null ? "" : rmap.get("ftp_username"));
				// 2 FTP用户密码
				ajax.append(";").append(rmap.get("ftp_passwd"));
				// 3 FTP端口
				ajax.append(";").append(rmap.get("ftp_port"));
				logger.debug("FTP服务信息：" + ajax.toString());
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

	public String ftpConfig(long accoid, String deviceId, String ftpenable,
			String ftpUserName, String ftpPassword, String ftpPort)
	{
		logger.debug("ftpConfig({},{},{},{},{},{})", new Object[] { accoid,
				deviceId, ftpenable, ftpUserName, ftpPassword, ftpPort });
		// 获取配置参数(XML)字符串
		String strategyXmlParam = ftp2Xml(ftpenable, ftpUserName, ftpPassword, ftpPort);
		/** 入策略表，调预读 */
		// 立即执行
		int strategyType = 0;
		// NTP配置的service_id
		int ftpServiceId = ConstantClass.FTP;
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
		strategyObj.setDeviceId(deviceId);
		// QOS serviceId
		strategyObj.setServiceId(ftpServiceId);
		// 顺序,默认1
		strategyObj.setOrderId(1);
		// 工单类型: 新工单,工单参数为xml串的工单
		strategyObj.setSheetType(2);
		// 参数
		strategyObj.setSheetPara(strategyXmlParam);
		strategyObj.setTempId(ftpServiceId);
		strategyObj.setIsLastOne(1);
		// 入策略表
		if (serviceManageDao.addStrategy(strategyObj))
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

	private String ftp2Xml(String ftpenable, String ftpUserName, String ftpPassword,
			String ftpPort)
	{
		logger.debug("ftp2Xml({},{},{},{})", new Object[] { ftpenable, ftpUserName,
				ftpPassword, ftpPort });
		String strXml = null;
		// new doc
		Document doc = DocumentHelper.createDocument();
		// root node: NET
		Element root = doc.addElement("FTP");
		root.addElement("FtpEnable").addText(ftpenable);
		if ("0".equals(ftpenable))
		{
			root.addElement("FtpUserName").addText("");
			root.addElement("FtpPassword").addText("");
			root.addElement("FtpPort").addText("");
		}
		else if ("1".equals(ftpenable))
		{
			root.addElement("FtpUserName")
					.addText(null == ftpUserName ? "" : ftpUserName);
			root.addElement("FtpPassword").addText(
					null == ftpPassword ? "" : ftpPassword);
			root.addElement("FtpPort").addText(null == ftpPort ? "" : ftpPort);
		}
		strXml = doc.asXML();
		return strXml;
	}

	/**
	 * @return the serviceManageDao
	 */
	public ServiceManageDAO getServiceManageDao()
	{
		return serviceManageDao;
	}

	/**
	 * @param serviceManageDao
	 *            the serviceManageDao to set
	 */
	public void setServiceManageDao(ServiceManageDAO serviceManageDao)
	{
		this.serviceManageDao = serviceManageDao;
	}
}
