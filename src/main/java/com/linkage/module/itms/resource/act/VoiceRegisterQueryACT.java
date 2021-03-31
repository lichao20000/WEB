
package com.linkage.module.itms.resource.act;

import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import action.splitpage.splitPageAction;

import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.module.itms.resource.bio.VoiceRegisterQueryBIO;

/**
 * @author Administrator (Ailk No.)
 * @version 1.0
 * @since 2013-11-12
 * @category com.linkage.module.itms.resource.act
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 */
public class VoiceRegisterQueryACT extends splitPageAction implements
		ServletRequestAware, SessionAware
{

	private static Logger logger = LoggerFactory
			.getLogger(VoiceRegisterFailQueryACT.class);
	private HttpServletRequest request;
	private Map session;
	// 开始时间
	private String startOpenDate1 = "";
	// 结束时间
	private String endOpenDate = "";
	// 结束时间
	private String endOpenDate1 = "";
	private String device_sn;
	private String vendorId;
	private String modelId;
	private String loid;
	private String device_type;
	private String enabled;
	private String voip_phone;
	private String status;
	private String reason;
	private List<Map> voiceDeviceList;
	private VoiceRegisterQueryBIO bio;
	
	// 厂商文件列表
	private Map<String, String> vendorMap;

	@Override
	public String execute() throws Exception
	{
		// TODO Auto-generated method stub
		vendorMap = bio.getVendor();
		endOpenDate = getEndDate();
		return "init";
	}

	public String VoiceRegisterQueryInfo()
	{
		this.setTime();
		voiceDeviceList = bio.VoiceRegisterQueryInfo(device_sn, modelId, loid,
				device_type, enabled, voip_phone, status, reason, startOpenDate1,
				endOpenDate1, curPage_splitPage, num_splitPage);
		maxPage_splitPage = bio.CountVoiceRegisterQueryInfo(device_sn, modelId, loid,
				device_type, enabled, voip_phone, status, reason, startOpenDate1,
				endOpenDate1, curPage_splitPage, num_splitPage);
		return "list";
	}

	// 当前时间的23:59:59,如 2011-05-11 23:59:59
	private String getEndDate()
	{
		GregorianCalendar now = new GregorianCalendar();
		SimpleDateFormat fmtrq = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
		String time = fmtrq.format(now.getTime());
		return time;
	}

	/**
	 * 时间转化
	 */
	private void setTime()
	{
		logger.debug("setTime()" + endOpenDate);
		if (endOpenDate == null || "".equals(endOpenDate))
		{
			startOpenDate1 = null;
			endOpenDate1 = null;
		}
		else
		{
			String start = endOpenDate + " 00:00:00";
			DateTimeUtil st = new DateTimeUtil(start);
			startOpenDate1 = String.valueOf(st.getLongTime());
			String end = endOpenDate + " 23:59:59";
			DateTimeUtil et = new DateTimeUtil(end);
			endOpenDate1 = String.valueOf(et.getLongTime());
			logger.warn("开始时间：" + start + "  结束时间：" + end);
		}
	}

	@Override
	public void setSession(Map<String, Object> session)
	{
		// TODO Auto-generated method stub
		this.session = session;
	}

	@Override
	public void setServletRequest(HttpServletRequest request)
	{
		// TODO Auto-generated method stub
		this.request = request;
	}

	public String getEndOpenDate()
	{
		return endOpenDate;
	}

	public void setEndOpenDate(String endOpenDate)
	{
		this.endOpenDate = endOpenDate;
	}

	public String getDevice_sn()
	{
		return device_sn;
	}

	public void setDevice_sn(String device_sn)
	{
		this.device_sn = device_sn;
	}

	public String getVendorId()
	{
		return vendorId;
	}

	public void setVendorId(String vendorId)
	{
		this.vendorId = vendorId;
	}

	public String getModelId()
	{
		return modelId;
	}

	public void setModelId(String modelId)
	{
		this.modelId = modelId;
	}

	public String getLoid()
	{
		return loid;
	}

	public void setLoid(String loid)
	{
		this.loid = loid;
	}

	public String getDevice_type()
	{
		return device_type;
	}

	public void setDevice_type(String device_type)
	{
		this.device_type = device_type;
	}

	public String getEnabled()
	{
		return enabled;
	}

	public void setEnabled(String enabled)
	{
		this.enabled = enabled;
	}

	public String getVoip_phone()
	{
		return voip_phone;
	}

	public void setVoip_phone(String voip_phone)
	{
		this.voip_phone = voip_phone;
	}

	public String getStatus()
	{
		return status;
	}

	public void setStatus(String status)
	{
		this.status = status;
	}

	public String getReason()
	{
		return reason;
	}

	public void setReason(String reason)
	{
		this.reason = reason;
	}

	public List<Map> getVoiceDeviceList()
	{
		return voiceDeviceList;
	}

	public void setVoiceDeviceList(List<Map> voiceDeviceList)
	{
		this.voiceDeviceList = voiceDeviceList;
	}

	
	public Map<String, String> getVendorMap()
	{
		return vendorMap;
	}

	
	public void setVendorMap(Map<String, String> vendorMap)
	{
		this.vendorMap = vendorMap;
	}

	public VoiceRegisterQueryBIO getBio()
	{
		return bio;
	}

	public void setBio(VoiceRegisterQueryBIO bio)
	{
		this.bio = bio;
	}
}
