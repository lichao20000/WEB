
package com.linkage.module.bbms.report.act;

import java.util.List;
import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import action.splitpage.splitPageAction;

import com.linkage.module.bbms.report.bio.SyslogQueryBIO;

/**
 * Syslog日志分析
 * 
 * @author zhangcong@asiainfo-linkage.com
 */
public class SyslogQueryACT extends splitPageAction implements SessionAware
{
	// 日志记录
	private static Logger logger = LoggerFactory.getLogger(SyslogQueryACT.class);
	// session
	private Map session;
	/** 查询时间 */
	private String stat_time = "";
	//保留查询结束时间
	private String end_time = "";
	/**
	 * 数据
	 */
	private List<Map> data;
	private List<Map> logTypes = null;
	private SyslogQueryBIO syslogQueryBio;
	/**
	 * 设备SN(OUI -SN)
	 */
	private String tdDeviceSn;
	
	private String tdDeviceId;
	/**
	 * 日志类型描述
	 */
	private String logTypeDesc;
	/** 报表类型 */
	private String logType;
	/**
	 * 日志类型显示
	 */
	private String resHtml;

	/**
	 * 统计
	 * 
	 * @author zhangcong@asiainfo-linkage.com
	 * @date 2011-07-26 02:20:42
	 * @param
	 * @return String
	 */
	public String execute()
	{
		logger.debug("execute()");
		//logger.info("============logType:" + logType);
		String oui = tdDeviceSn.split("-")[0].trim();
		String sn = tdDeviceSn.split("-")[1].trim();
		//查询记录
		data = syslogQueryBio.querySyslog(logType, stat_time,getEnd_time(), oui ,sn , curPage_splitPage,
				num_splitPage);
		
		//查询总条
		maxPage_splitPage = syslogQueryBio.querySyslogCount(logType, stat_time,getEnd_time(),  oui,sn,
				num_splitPage);
		
		setLogTypeDesc();
		return "list";
	}

	/**
	 * 分页查询
	 * 
	 * @return
	 */
	public String goPageQuerySysLog()
	{
		logger.debug("goPage()");
		String oui = tdDeviceSn.split("-")[0].trim();
		String sn = tdDeviceSn.split("-")[1].trim();
		//查询记录
		data = syslogQueryBio.querySyslog(logType, stat_time,getEnd_time(), oui,sn, curPage_splitPage,
				num_splitPage);
		//查询总条
		maxPage_splitPage = syslogQueryBio.querySyslogCount(logType, stat_time,getEnd_time(),  oui,sn,
				num_splitPage);
		
		setLogTypeDesc();
		return "list";
	}
	
	/**
	 * 设备日志类型描述
	 */
	public void setLogTypeDesc()
	{
		if(null == logTypes)
		{
			logTypes = syslogQueryBio.querySysLogTypes();
		}
		if(null != logTypes && !logTypes.isEmpty())
		{
			for(Map map:logTypes)
			{
				String type_id = String.valueOf(map.get("type_id"));
				String type_name = String.valueOf(map.get("type_name"));
				if(logType.equals(type_id))
				{
					logTypeDesc = type_name;
					break;
				}
			}
			 
		}
	}
	
	/**
	 * 拼接日志类型
	 * @return
	 */
	public String init()
	{
		resHtml = "<select name='logType' class='bk'>";
		logger.warn("===resHtml=="+resHtml+"===========");
		if(null == logTypes)
		{
			logTypes = syslogQueryBio.querySysLogTypes();
		}
		logger.warn("===logTypes=="+logTypes+"===========");
		if(null != logTypes && !logTypes.isEmpty())
		{
			for(Map map:logTypes)
			{
				String type_id = String.valueOf(map.get("type_id"));
				String type_name = String.valueOf(map.get("type_name"));
				resHtml += "<option value='" + type_id + "'>" + type_name + "</option>";
			}
			 
		}
		resHtml += "</select>";
		return "showIndex";
	}
	
	public List<Map> getLogTypes()
	{
		return logTypes;
	}

	public void setLogTypes(List<Map> logTypes)
	{
		this.logTypes = logTypes;
	}

	public String getResHtml()
	{
		return resHtml;
	}

	public void setResHtml(String resHtml)
	{
		this.resHtml = resHtml;
	}

	/**
	 * @return the session
	 */
	public Map getSession()
	{
		return session;
	}

	/**
	 * @param session
	 *            the session to set
	 */
	public void setSession(Map session)
	{
		this.session = session;
	}

	/**
	 * @return the stat_time
	 */
	public String getStat_time()
	{
		return stat_time;
	}

	/**
	 * @param stat_time
	 *            the stat_time to set
	 */
	public void setStat_time(String stat_time)
	{
		this.stat_time = stat_time;
	}

	public String getEnd_time()
	{
		if (null == end_time || "".equals(end_time) || "null".equals(end_time))
		{
			this.end_time = this.stat_time;
		}
		return this.end_time;
	}

	/**
	 * 设置查询结束时间
	 * @param end_time
	 */
	public void setEnd_time(String end_time)
	{
		this.end_time = end_time;
	}

	public String getTdDeviceSn()
	{
		return tdDeviceSn;
	}

	public void setTdDeviceSn(String tdDeviceSn)
	{
		this.tdDeviceSn = tdDeviceSn.replaceAll("\n", "").replaceAll("	", "");
	}

	/**
	 * @return the data
	 */
	public List<Map> getData()
	{
		return data;
	}

	/**
	 * @param data
	 *            the data to set
	 */
	public void setData(List<Map> data)
	{
		this.data = data;
	}

	public SyslogQueryBIO getSyslogQueryBio()
	{
		return syslogQueryBio;
	}

	public void setSyslogQueryBio(SyslogQueryBIO syslogQueryBio)
	{
		this.syslogQueryBio = syslogQueryBio;
	}

	public String getLogType()
	{
		return logType;
	}

	public void setLogType(String logType)
	{
		this.logType = logType;
	}

	public String getTdDeviceId()
	{
		return tdDeviceId;
	}

	public void setTdDeviceId(String tdDeviceId)
	{
		this.tdDeviceId = tdDeviceId;
	}

	public String getLogTypeDesc()
	{
		return logTypeDesc;
	}

	public void setLogTypeDesc(String logTypeDesc)
	{
		this.logTypeDesc = logTypeDesc;
	}
}
