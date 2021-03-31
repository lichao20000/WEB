
package com.linkage.module.gwms.diagnostics.bio;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.util.TimeUtil;
import com.linkage.litms.LipossGlobals;
import com.linkage.litms.common.util.Base64;
import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.litms.common.util.ToCode16;
import com.linkage.module.gwms.cao.gw.PingCAO;
import com.linkage.module.gwms.dao.gw.AcsStreamDAO;
import com.linkage.module.gwms.dao.gw.VersionFileDAO;
import com.linkage.module.gwms.dao.gw.WanConnSessDAO;
import com.linkage.module.gwms.obj.StrategyOBJ;
import com.linkage.module.gwms.obj.gw.PingObject;
import com.linkage.module.gwms.obj.gw.WanConnSessObj;
import com.linkage.module.gwms.report.dao.PingResultDAO;
import com.linkage.module.gwms.util.CreateObjectFactory;
import com.linkage.module.gwms.util.StringUtil;
import com.linkage.module.gwms.util.corba.DevFactoryReset;
import com.linkage.module.gwms.util.corba.DevReboot;

/**
 * @author Jason(3412)
 * @date 2009-7-8
 */
public class DiagToolsBIO
{

	private static Logger logger = LoggerFactory.getLogger(DiagToolsBIO.class);
	// 版本文件
	private VersionFileDAO versionFileDao;
	// WANDeviceSession
	private WanConnSessDAO sessDao;
	// 码流
	private AcsStreamDAO acsStreamDao;
	// ping结果记录
	private PingResultDAO pingDao;

	/**
	 * 设备重启
	 * 
	 * @param
	 * @author Jason(3412)
	 * @date 2009-7-8
	 * @return int
	 */
	public int devReboot(String deviceId, String gw_type)
	{
		logger.debug("devReboot({},{})", new Object[]{deviceId, gw_type});
		int ir = DevReboot.reboot(deviceId, gw_type);
		return ir;
	}

	/**
	 * 设备恢复出厂设置
	 * 
	 * @param
	 * @author Jason(3412)
	 * @date 2009-7-8
	 * @return int
	 */
	public int devReset(String deviceId, String gw_type)
	{
		logger.debug("devReset({})", deviceId);
		int ir = DevFactoryReset.reset(deviceId, gw_type);
		return ir;
	}

	/**
	 * 设备ping操作
	 * 
	 * @param
	 * @author Jason(3412)
	 * @date 2009-7-8
	 * @return int
	 */
	public PingObject devPing(String deviceId, String pingAddress, int packageSize,
			int times)
	{
		logger.debug("devPing({})", deviceId);
		WanConnSessObj[] arrObj = sessDao.getConntioningSess(deviceId);
		if (null == arrObj || arrObj.length < 1)
		{
			logger.warn("没有连接状态为Connecting服务类型为INTERNET的Session");
			logger.warn("未获取到端口信息");
			return null;
		}
		else
		{
			String devInterface = arrObj[0].getPingInterface();
			PingObject pingObj = new PingObject();
			pingObj.setPingAddress(pingAddress);
			pingObj.setDevInterface(devInterface);
			pingObj.setPackageSize(packageSize);
			// 0.5s
			pingObj.setTimeOut(1000);
			pingObj.setNumOfRepetitions(times);
			PingCAO pingCAO = new PingCAO();
			pingCAO.setDeviceId(deviceId);
			pingCAO.setPingObj(pingObj);
			logger.debug("bigin ping()");
			pingCAO.ping(LipossGlobals.getGw_Type(deviceId));
			// 入ping记录表
			logger.debug("pingObj.getSuccessCount" + pingObj.getSuccessCount());
			logger.debug("pingObj.getFailureCount" + pingObj.getFailureCount());
			pingDao.getPingRecord(deviceId, pingObj);
			return pingObj;
		}
	}

	/**
	 * 设备上传日志
	 * 
	 * @param 设备ID
	 * @author Jason(3412)
	 * @date 2009-7-8
	 * @return int
	 */
	public int uploadLogFile(String deviceId, long user_id, String gw_type)
	{
		logger.debug("uploadLogFile({})", deviceId);
		// 存放返回结果
		String strategyXmlParam = getUpLogFileXML(deviceId);
		
		logger.debug("uploadLogFile XML: " + strategyXmlParam);
		/** 入策略表，调预读 */
		// 立即执行
		int strategyType = 0;
		// 配置的service_id
		int serviceId = 3;
		StrategyOBJ strategyObj = new StrategyOBJ();
		// 策略ID
		strategyObj.createId();
		// 策略配置时间
		strategyObj.setTime(TimeUtil.getCurrentTime());
		// 用户id
		strategyObj.setAccOid(user_id);
		// 立即执行
		strategyObj.setType(strategyType);
		// 设备ID
		strategyObj.setDeviceId(deviceId);
		// QOS serviceId
		strategyObj.setServiceId(serviceId);
		// 顺序,默认1
		strategyObj.setOrderId(1);
		// 工单类型: 新工单,工单参数为xml串的工单
		strategyObj.setSheetType(2);
		// 参数
		strategyObj.setSheetPara(strategyXmlParam);
		strategyObj.setTempId(serviceId);
		strategyObj.setIsLastOne(1);
		// 入策略表
		if (versionFileDao.addStrategy(strategyObj))
		{
			// 调用预读
			if (true == CreateObjectFactory.createPreProcess(gw_type).processOOBatch(String.valueOf(strategyObj
					.getId())))
			{
				logger.debug("调用后台成功");
				return 1;
			}
			else
			{
				logger.warn("调用预读失败");
				return -1;
			}
		}
		else
		{
			logger.warn("策略入库失败");
			return -1;
		}
	}

	/**
	 * 获取上传日志文件的策略XML
	 * 
	 * @param
	 * @author Jason(3412)
	 * @date 2009-7-13
	 * @return String[]
	 */
	public String getUpLogFileXML(String deviceId)
	{
		logger.debug("getUpLogFileParam({})", deviceId);
		String strXml = null;
		if (deviceId == null || "".equals(deviceId))
		{
		}
		else
		{
			String filepath = "";
			String username = "";
			String passwd = "";
			String delayTime = "0";
			// 获取日志文件的路径信息
			// http://218.94.131.205:6060/FileServer/FILE/LOG?dir_id=3
			filepath = versionFileDao.getFileParam(3);
			if (null != filepath)
			{
				int indx = filepath.lastIndexOf("/");
				// http://218.94.131.205:6060/FileServer/FILE
				String tPath = filepath.substring(0, indx);
				indx = filepath.indexOf("?");
				// dir_id=3
				String tDir = filepath.substring(indx + 1, filepath.length());
				tDir += "&fileName=" + deviceId + "_" + TimeUtil.getStrNow() + ".log";
				tDir += "&device_id=" + deviceId;
				logger.warn("filepath:" + tPath + "?" + tDir);
				filepath = tPath + "?" + ToCode16.encode(Base64.encode(tDir));
				// 对应工单的参数数组
				// new doc
				Document doc = DocumentHelper.createDocument();
				// root node: NET
				Element root = doc.addElement("Upload");  
				root.addAttribute("flag", "1");
				root.addElement("CommandKey").addText("LOG");
				root.addElement("FileType").addText("2 Vendor Log File");
				root.addElement("URL").addText(filepath);
				root.addElement("Username").addText(username);
				root.addElement("Password").addText(passwd);
				root.addElement("DelaySeconds").addText(delayTime);
				strXml = doc.asXML();
			}
		}
		return strXml;
	}

	/**
	 * 获取码流数据
	 * 
	 * @param
	 * @author Jason(3412)
	 * @date 2009-7-29
	 * @return List
	 */
	@SuppressWarnings("unchecked")
	public List getAcsStream(String deviceId)
	{
		logger.debug("getAcsStream({})", deviceId);
		List retList = null;
		List streamList = acsStreamDao.queryDeviceStream(deviceId);
		if (null != streamList && false == streamList.isEmpty())
		{
			logger.debug("getAcsStream, has data");
			retList = new ArrayList();
			int size = streamList.size();
			int red = 0;
			String tmp = "";
			for (int i = 0; i < size; i++)
			{
				Map rMap = (Map) streamList.get(i);
				Map retMap = new HashMap();
				if (StringUtil.IsEmpty(tmp))
				{
					tmp = StringUtil.getStringValue(rMap.get("s_ip"));
				}
				if (tmp.equals(rMap.get("s_ip")))
				{
					red = 1;
				}
				else
				{
					red = 2;
				}
				retMap.put("source", "From:" + rMap.get("s_ip") + ":"
						+ rMap.get("s_port"));
				retMap.put("dest", "To:" + rMap.get("d_ip") + ":" + rMap.get("d_port"));
				retMap.put("time", new DateTimeUtil(StringUtil.getLongValue(rMap
						.get("inter_time"))).getLongDate());
				retMap.put("content", rMap.get("inter_content"));
				retMap.put("red", red);
				retList.add(retMap);
			}
		}
		else
		{
			logger.warn("getAcsStream, no data ");
		}
		// 参数整理
		return retList;
	}

	/**
	 * 清除该设备的码流记录
	 * 
	 * @param
	 * @author Jason(3412)
	 * @date 2009-7-29
	 * @return void
	 */
	public int clearAcsStream(String deviceId)
	{
		return acsStreamDao.removeDeviceStream(deviceId);
	}

	public void setVersionFileDao(VersionFileDAO versionFileDao)
	{
		this.versionFileDao = versionFileDao;
	}

	public void setSessDao(WanConnSessDAO sessDao)
	{
		this.sessDao = sessDao;
	}

	public void setAcsStreamDao(AcsStreamDAO acsDao)
	{
		this.acsStreamDao = acsDao;
	}

	public void setPingDao(PingResultDAO pingDao)
	{
		this.pingDao = pingDao;
	}
}
