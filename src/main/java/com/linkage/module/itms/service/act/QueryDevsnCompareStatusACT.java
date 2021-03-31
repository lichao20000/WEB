package com.linkage.module.itms.service.act;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.util.StringUtil;
import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.module.itms.service.bio.QueryDevsnCompareStatusBIO;

/**
 * 内蒙古查询工单序列号与设备上报序列号是否匹配
 * @author banyr (Ailk No.)
 * @version 1.0
 * @since 2018-11-2
 * @category com.linkage.module.itms.service.act
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 *
 */
public class QueryDevsnCompareStatusACT
{
	/** log */
	private static Logger logger = LoggerFactory.getLogger(QueryDevsnCompareStatusACT.class);
	
	private String loid;
	
	private String devsnfrominform;
	
	private String devsnfromeserver;
	
	private String comparetime;
	
	private String comeparestatus;
	
	private QueryDevsnCompareStatusBIO bio;
	
	public String getStatusInfo()
	{
		logger.warn("getStatusInfo query start ");
		Map<String, String> statusInfoMap = bio.getDevsnCompareStatus(loid);
		loid = StringUtil.getStringValue(statusInfoMap, "loid", "");
		devsnfrominform = StringUtil.getStringValue(statusInfoMap, "devsnfrominform", "");
		devsnfromeserver = StringUtil.getStringValue(statusInfoMap, "devsnfromeserver", "");
		comparetime = transDate(StringUtil.getStringValue(statusInfoMap, "comparetime", ""));
		comeparestatus = transMatch(StringUtil.getStringValue(statusInfoMap, "comeparestatus", ""));
		return "statusInfo";
	}

	public String getLoid()
	{
		return loid;
	}
	
	public void setLoid(String loid)
	{
		this.loid = loid;
	}
	
	public QueryDevsnCompareStatusBIO getBio()
	{
		return bio;
	}

	public void setBio(QueryDevsnCompareStatusBIO bio)
	{
		this.bio = bio;
	}
	
	private static String transMatch(String status)
	{
		if (null != status && !"".equals(status))
		{
			if("1".equals(status))
			{
				return "匹配";
			}
			else if ("0".equals(status))
			{
				return "不匹配";
			}
		}
		return "";
	}
	
	private static String transDate(String seconds)
	{
		if (null != seconds && !"".equals(seconds))
		{
			try
			{
				DateTimeUtil dt = new DateTimeUtil(
						Long.parseLong(seconds) * 1000);
				return dt.getLongDate();
			}
			catch (NumberFormatException e)
			{
				logger.error(e.getMessage(), e);
			}
			catch (Exception e)
			{
				logger.error(e.getMessage(), e);
			}
		}
		return "";
	}

	public String getDevsnfrominform()
	{
		return devsnfrominform;
	}
	
	public void setDevsnfrominform(String devsnfrominform)
	{
		this.devsnfrominform = devsnfrominform;
	}
	
	public String getDevsnfromeserver()
	{
		return devsnfromeserver;
	}
	
	public void setDevsnfromeserver(String devsnfromeserver)
	{
		this.devsnfromeserver = devsnfromeserver;
	}
	
	public String getComparetime()
	{
		return comparetime;
	}
	
	public void setComparetime(String comparetime)
	{
		this.comparetime = comparetime;
	}
	
	public String getComeparestatus()
	{
		return comeparestatus;
	}

	public void setComeparestatus(String comeparestatus)
	{
		this.comeparestatus = comeparestatus;
	}

}
