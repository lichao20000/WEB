
package com.linkage.module.gtms.config.action;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.util.StringUtil;
import com.linkage.module.gtms.config.serv.ConfigMaxTerminalServImp;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.util.CreateObjectFactory;
import com.linkage.module.gwms.util.PreProcessInterface;
import com.linkage.module.gwms.util.corba.SuperGatherCorba;

/**
 * @author Administrator (Ailk No.)
 * @version 1.0
 * @since 2013-5-21
 * @category com.linkage.module.gtms.config.action
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 */
public class ConfigMaxTerminalActionImp implements ConfigMaxTerminalAction, SessionAware,
		ServletRequestAware
{

	private Logger logger = LoggerFactory.getLogger(ConfigMaxTerminalActionImp.class);
	private ConfigMaxTerminalServImp configmtserv;
	/**设备id  */
	private String device_id;
	
	/**模式   */
	private String mode;
	
	/**最大值  */
	private String total_number;
	
	/**策略表id */
	private String id;
	
	private String ajax;
	private String gw_type;
	
	private Map session = null;
	private HttpServletRequest request;

	/**
	 * 获取上网数
	 */
	public String maxTerminal()
	{
		logger.debug("Enter maxTerminal()");
		
		ajax = "";
		
		//调用采集模块
		SuperGatherCorba sgc = new SuperGatherCorba(gw_type);
		
		//采集模块返回值
		int rsint = sgc.getCpeParams(device_id, 31, 1);
		
		
		//当返回参数不等于1，表示调用采集模块失败
		if (rsint != 1)
		{
			// 调用采集失败
			ajax = "-1#";
			if (null == Global.G_Fault_Map.get(rsint))
			{
				ajax += Global.G_Fault_Map.get(100000).getFaultReason();
			}
			else
			{
				ajax += Global.G_Fault_Map.get(rsint).getFaultReason();
			}
		}
		else
		{	
			//调用采集模块成功，查询最大上网数
			Map<String, String> map = configmtserv.queryMaxTerminal(device_id);
			if (null != map && !map.isEmpty())
			{
				ajax = device_id + "#" + map.get("m_mode") + "#"
						+ map.get("total_number");
			}
		}
		return "ajax";
	}
	
	
	/**
	 * 保存
	 */
	public String maxTerminalSave()
	{
		logger.debug("Enter maxTerminalSave()");
		
		ajax = "";
		
		// 调用WEB配置模块
		String[] deviceIds = { device_id };
		String[] paramArr = { mode, total_number };
		PreProcessInterface pprCorba = CreateObjectFactory.createPreProcess(gw_type);
		int ret = pprCorba.processDeviceStrategy(deviceIds, "101", paramArr);
		
		//配置模块返回参数，-1表示参数为空，-1表示调用配置模块失败
		if (-1 == ret)
		{
			ajax = "-1#参数为空！";
		}
		else if (-2 == ret)
		{
			ajax = "-1#调用配置模块失败！！";
		}
		else
		{
			//ajax = configmtserv.updateMaxTerminal(device_id, mode, total_number);
			//调用配置模块成功，查询策略信息
			Map<String, String> map = configmtserv.queryStrategyByDeviceId(device_id);
			if (null != map && !map.isEmpty())
			{
				ajax += "1#" + map.get("id") + "#" + map.get("service_name") + "#"
						+ map.get("start_time") + "#" + map.get("end_time") + "#"
						+ map.get("status") + "#" + map.get("fault_desc");
			}
			else
			{
				//-1代表没有策略数据信息
				ajax = "1#-1";
			}
		}
		return "ajax";
	}
	
	/**
	 * 刷新
	 */
	public String maxTerminalRefresh()
	{
		logger.debug("maxTerminalRefresh()");
		
		ajax = "";
		
		//根据策略id，刷新页面策略信息
		Map<String, String> map = configmtserv.queryStrategyByDeviceId(device_id);
		if (null != map && !map.isEmpty())
		{
			// 为了前台处理数据方便
			ajax = "1#" + map.get("id") + "#" + map.get("service_name") + "#"
					+ map.get("start_time") + "#" + map.get("end_time") + "#"
					+ map.get("status") + "#" + map.get("fault_desc");
		}
		else
		{
			//-1代表没有策略数据信息
			ajax = "1#-1";
		}
		return "ajax";
	}

	public String getDevice_id()
	{
		return device_id;
	}

	public void setDevice_id(String device_id)
	{
		this.device_id = device_id;
	}

	public String getMode()
	{
		return mode;
	}

	public void setMode(String mode)
	{
		this.mode = mode;
	}

	public String getAjax()
	{
		return ajax;
	}

	public void setAjax(String ajax)
	{
		this.ajax = ajax;
	}

	public ConfigMaxTerminalServImp getConfigmtserv()
	{
		return configmtserv;
	}

	public void setConfigmtserv(ConfigMaxTerminalServImp configmtserv)
	{
		this.configmtserv = configmtserv;
	}

	public String getId()
	{
		return id;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	public String getTotal_number()
	{
		return total_number;
	}

	public void setTotal_number(String total_number)
	{
		this.total_number = total_number;
	}

	public void setServletRequest(HttpServletRequest req)
	{
		this.request = req;
	}

	public void setSession(Map session)
	{
		this.session = session;
	}

	public String getGw_type()
	{
		return gw_type;
	}

	public void setGw_type(String gw_type)
	{
		this.gw_type = gw_type;
	}
}
