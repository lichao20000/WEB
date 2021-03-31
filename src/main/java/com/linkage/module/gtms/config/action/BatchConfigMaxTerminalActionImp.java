
package com.linkage.module.gtms.config.action;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.util.StringUtil;
import com.linkage.module.gtms.config.serv.BatchConfigMaxTerminalServImp;

/**
 * @author Administrator (Ailk No.)
 * @version 1.0
 * @since 2013-5-21
 * @category com.linkage.module.gtms.config.action
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 */
public class BatchConfigMaxTerminalActionImp implements SessionAware, ServletRequestAware
{

	private Logger logger = LoggerFactory
			.getLogger(BatchConfigMaxTerminalActionImp.class);
	private BatchConfigMaxTerminalServImp batchconfigmtserv;
	/** 模式 */
	private String mode;
	/** 上网数最大值 */
	private String total_number;
	/** 策略表id */
	private String id;
	private String ajax;
	private String gw_type;
	private String deviceIds;
	private String param;

	public String getDeviceIds()
	{
		return deviceIds;
	}

	public void setDeviceIds(String deviceIds)
	{
		this.deviceIds = deviceIds;
	}

	public String getParam()
	{
		return param;
	}

	public void setParam(String param)
	{
		this.param = param;
	}

	@SuppressWarnings({ "rawtypes", "unused" })
	private Map session = null;
	@SuppressWarnings("unused")
	private HttpServletRequest request;

	/**
	 * 保存
	 */
	public String maxTerminalConfig()
	{
		try
		{
			ajax = "";
			// 调用WEB配置模块
			String[] paramNodePaths = {
					"InternetGatewayDevice.Services.X_CT-COM_MWBAND.Mode",
					"InternetGatewayDevice.Services.X_CT-COM_MWBAND.TotalTerminalNumber" };
			String[] paramValues = { mode, total_number };
			String[] paramTypes = { "2", "2" };
			int len = paramNodePaths.length;
			String[] paramArr = new String[len];
			for (int i = 0; i <= len - 1; i++)
			{
				paramArr[i] = paramNodePaths[i] + "ailk!@#" + paramValues[i]
						+ "ailk!@#" + paramTypes[i];
			}
			// 配置模块返回参数，-1表示参数为空，-1表示调用配置模块失败
			if (true == StringUtil.IsEmpty(deviceIds))
			{
				logger.debug("任务中没有设备");
				ajax = "任务中没有设备";
			}
			// 直接传deviceId数组调配置模块接口
			else if (!"0".equals(deviceIds))
			{
				logger.warn("批量参数配置小于50，直接传deviceId数组调配置模块接口");
				String[] deviceId_array = null;
				deviceId_array = deviceIds.split(",");
				ajax = batchconfigmtserv.doConfigAll(deviceId_array, "116", paramArr,
						"1");
			}
			else
			{// 调用后台corba接口
				logger.warn("直接把SQL传给配置模块做批量参数修改,param=" + param);
				String[] _param = param.split("\\|");
				String matchSQL = _param[10];
				ajax = batchconfigmtserv.doConfigAll(
						new String[] { matchSQL.replace("[", "\'") }, "116", paramArr,
						"1");
			}
		}
		catch (Exception e)
		{
			logger.error("doConfigAll err:" + e);
			e.printStackTrace();
			return "ajax";
		}
		return "ajax";
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

	@SuppressWarnings("rawtypes")
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

	public BatchConfigMaxTerminalServImp getBatchconfigmtserv()
	{
		return batchconfigmtserv;
	}

	public void setBatchconfigmtserv(BatchConfigMaxTerminalServImp batchconfigmtserv)
	{
		this.batchconfigmtserv = batchconfigmtserv;
	}
}
