
package com.linkage.module.itms.service.act;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.util.StringUtil;
import com.linkage.litms.system.UserRes;
import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.module.itms.service.bio.BssSheetDelBIO;
import com.linkage.module.itms.service.obj.SheetObj;

/**
 * 删除语音和iptv工单
 * 
 * @author banyr (Ailk No.)
 * @version 1.0
 * @since 2018-7-6
 * @category com.linkage.module.itms.service.act
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 */
public class BssSheetDelACT implements SessionAware
{

	public static Logger logger = LoggerFactory.getLogger(BssSheetDelACT.class);
	public static final String CRM_ID = "FROMWEB-0000002";
	public static final String USER_TYPE = "1";
	private SheetObj obj = new SheetObj();
	private String ajax = null;
	@SuppressWarnings("rawtypes")
	private Map session;
	private List<Map<String, String>> cityList = null;
	/** 封装语音及IPTV信息 */
	private List<Map<String, String>> message = null;
	private List<String> sipVoipPortList = new ArrayList<String>();
	private List<String> sipVoipUsernameList = new ArrayList<String>();
	private List<String> hvoipPhoneList = new ArrayList<String>();
	private List<String> iptvUsernameList = new ArrayList<String>();
	private List<String> iptvLanPortList = new ArrayList<String>();
	// 业务类型
	private String servTypeId;
	// 操作类型
	private String operateType;
	// 工单受理时间
	private String dealdate;
	// 属地
	private String cityId;
	// sipVoip认证帐号
	private String sipVoipUsername;
	// sipVoip端口
	private String sipVoipPort;
	// h248Voip业务电话号码
	private String hvoipPhone;
	// 开通的端口：L1,L2,L3,L4
	protected String iptvLanPort;
	// IPTV宽带接入账号
	private String iptvUsername;
	private String loid;
	private BssSheetDelBIO bio;

	public String initLoid()
	{
		int result = 0;
		if (!StringUtil.IsEmpty(loid))
		{
			result = bio.qyeryLoid(loid);
			logger.warn("loid is exit : " + result + "loid : " + loid);
		}
		if (result > 0)
		{
			ajax = String.valueOf(result);
			return "ajax";
		}
		else
		{
			ajax = "请输入正确的LOID";
			return "ajax";
		}
	}

	public String getIptvPort()
	{
		List<Map<String, String>> iptvPortMessage = bio.getIptvPort(iptvUsername, loid);
		String iptvPort = "";
		if (!iptvPortMessage.isEmpty())
		{
			iptvPort = iptvPortMessage
					.get(0)
					.get("real_bind_port")
					.replaceAll(
							"InternetGatewayDevice.LANDevice.1.LANEthernetInterfaceConfig.",
							"L");
			ajax = iptvPort;
			logger.warn("iptv Port is : " + ajax);
		}
		return "ajax";
	}

	public String showSheet()
	{
		UserRes curUser = (UserRes) session.get("curUser");
		cityId = curUser.getCityId();
		cityList = CityDAO.getAllNextCityListByCityPid(curUser.getCityId());
		// SIP VOIP
		if (14 == StringUtil.getIntegerValue(servTypeId))
		{
			message = bio.getSipVoipMessage(loid);
			if (message.isEmpty())
			{
				ajax = "此用户无VOIP业务（SIP）";
				return "ajax";
			}
			for (int i = 0; i < message.size(); i++)
			{
				sipVoipPortList.add(message.get(i).get("voip_port"));
				sipVoipUsernameList.add(message.get(i).get("voip_username"));
				logger.warn("message.............. + " + message.get(i));
			}
			return "stpvoipstop";
		}// H248 VOIP
		else if (15 == StringUtil.getIntegerValue(servTypeId))
		{
			message = bio.getH248VoipMessage(loid);
			if (message.isEmpty())
			{
				ajax = "此用户无VOIP业务（H248）";
				return "ajax";
			}
			for (int i = 0; i < message.size(); i++)
			{
				hvoipPhoneList.add(message.get(i).get("voip_phone"));
				logger.warn("message.............. + " + message.get(i));
			}
			logger.warn("h248 VOIP业务销户工单");
			return "h248voipstop";
		}
		// Iptv
		else if (21 == StringUtil.getIntegerValue(servTypeId))
		{
			message = bio.getIptvMessage(loid);
			if (message.isEmpty())
			{
				ajax = "此用户无Iptv业务";
				return "ajax";
			}
			boolean bindPort = true;
			for (int i = 0; i < message.size(); i++)
			{
				if (null != message.get(i).get("real_bind_port"))
				{
					iptvUsernameList.add(message.get(i).get("username"));
					iptvLanPortList
							.add(message
									.get(i)
									.get("real_bind_port")
									.replaceAll(
											"InternetGatewayDevice.LANDevice.1.LANEthernetInterfaceConfig.",
											"L"));
					bindPort = false;
				}
				logger.warn("message.............. + " + message.get(i));
			}
			if (bindPort)
			{
				ajax = "此用户无Iptv业务";
				return "ajax";
			}
			logger.warn("iptvUsernameList.............. + " + iptvUsernameList);
			logger.warn("iptvLanPortList.............. + " + iptvLanPortList);
			logger.warn("IPTV业务销户工单");
			return "iptvstop";
		}
		else
		{
			ajax = "未知业务类型";
			return "ajax";
		}
	}

	// 获取dealtime
	@SuppressWarnings("deprecation")
	private static String getDealTime()
	{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
		String dstr = "";
		Date d = new Date();
		dstr += sdf.format(d);
		if (d.getMonth() + 1 < 10)
		{
			dstr += '0';
		}
		dstr += d.getMonth() + 1;
		if (d.getDate() < 10)
		{
			dstr += '0';
		}
		dstr += d.getDate();
		if (d.getHours() < 10)
		{
			dstr += '0';
		}
		dstr += d.getHours();
		if (d.getMinutes() < 10)
		{
			dstr += '0';
		}
		dstr += d.getMinutes() + "00";
		return dstr;
	}

	public String sendSheet()
	{
		paramToObj();
		String returnMessage = "";
		obj.setUserOperateId("3");
		// SIP VOIP业务
		if (14 == StringUtil.getIntegerValue(servTypeId))
		{
			obj.setSipVoipUsername(sipVoipUsername);
			obj.setSipVoipPort(sipVoipPort);
			returnMessage = bio.getSipVoipSheetResult(obj);
			ajax = getMessage(returnMessage, "resultDes");
		}
		// h248 VOIP业务
		else if (15 == StringUtil.getIntegerValue(servTypeId))
		{
			obj.setHvoipPhone(hvoipPhone);
			returnMessage = bio.getH248VoipSheetResult(obj);
			ajax = getMessage(returnMessage, "resultDes");
		}
		// IPTV业务
		else if (21 == StringUtil.getIntegerValue(servTypeId))
		{
			obj.setIptvUserName(iptvUsername);
			obj.setIptvPort(iptvLanPort);
			returnMessage = bio.getIptvSheetResult(obj);
			ajax = getMessage(returnMessage, "resultDes");
		}
		else
		{
			ajax = "未知业务类型";
		}
		logger.warn("result message" + ajax);
		return "ajax";
	}

	/**
	 * 参数信息放入SheetObj对象
	 */
	private void paramToObj()
	{
		dealdate = getDealTime();
		obj.setCmdId(CRM_ID);
		obj.setUserServTypeId(servTypeId);
		obj.setUserOperateId(operateType);
		obj.setDealDate(dealdate);
		obj.setUserType(USER_TYPE);
		obj.setLoid(loid);
		obj.setCityId(cityId);
		logger.warn(CRM_ID + "-" + servTypeId + "-" + operateType + "-" + dealdate + "-"
				+ USER_TYPE + "-" + loid + "-" + cityId);
	}

	private String getMessage(String xmlStr, String node)
	{
		String result = "";
		Document doc = null;
		try
		{
			doc = DocumentHelper.parseText(xmlStr);
			Element rootElt = doc.getRootElement();
			result = rootElt.elementTextTrim(node);
		}
		catch (Exception e)
		{
			logger.error("getMessage()--exception:" + e.getMessage());
		}
		return result;
	}

	public SheetObj getObj()
	{
		return obj;
	}

	public void setObj(SheetObj obj)
	{
		this.obj = obj;
	}

	public String getAjax()
	{
		return ajax;
	}

	public void setAjax(String ajax)
	{
		this.ajax = ajax;
	}

	public List<Map<String, String>> getCityList()
	{
		return cityList;
	}

	public void setCityList(List<Map<String, String>> cityList)
	{
		this.cityList = cityList;
	}

	public String getServTypeId()
	{
		return servTypeId;
	}

	public void setServTypeId(String servTypeId)
	{
		this.servTypeId = servTypeId;
	}

	public String getOperateType()
	{
		return operateType;
	}

	public void setOperateType(String operateType)
	{
		this.operateType = operateType;
	}

	public String getDealdate()
	{
		return dealdate;
	}

	public void setDealdate(String dealdate)
	{
		this.dealdate = dealdate;
	}

	public String getCityId()
	{
		return cityId;
	}

	public void setCityId(String cityId)
	{
		this.cityId = cityId;
	}

	public String getSipVoipUsername()
	{
		return sipVoipUsername;
	}

	public void setSipVoipUsername(String sipVoipUsername)
	{
		this.sipVoipUsername = sipVoipUsername;
	}

	public String getSipVoipPort()
	{
		return sipVoipPort;
	}

	public void setSipVoipPort(String sipVoipPort)
	{
		this.sipVoipPort = sipVoipPort;
	}

	public String getHvoipPhone()
	{
		return hvoipPhone;
	}

	public void setHvoipPhone(String hvoipPhone)
	{
		this.hvoipPhone = hvoipPhone;
	}

	public String getIptvLanPort()
	{
		return iptvLanPort;
	}

	public void setIptvLanPort(String iptvLanPort)
	{
		this.iptvLanPort = iptvLanPort;
	}

	public String getIptvUsername()
	{
		return iptvUsername;
	}

	public void setIptvUsername(String iptvUsername)
	{
		this.iptvUsername = iptvUsername;
	}

	public String getLoid()
	{
		return loid;
	}

	public void setLoid(String loid)
	{
		this.loid = loid;
	}

	public BssSheetDelBIO getBio()
	{
		return bio;
	}

	public void setBio(BssSheetDelBIO bio)
	{
		this.bio = bio;
	}

	@SuppressWarnings({ "rawtypes" })
	public Map getSession()
	{
		return session;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void setSession(Map session)
	{
		this.session = session;
	}

	public List<Map<String, String>> getMessage()
	{
		return message;
	}

	public void setMessage(List<Map<String, String>> message)
	{
		this.message = message;
	}

	public List<String> getSipVoipPortList()
	{
		return sipVoipPortList;
	}

	public void setSipVoipPortList(List<String> sipVoipPortList)
	{
		this.sipVoipPortList = sipVoipPortList;
	}

	public List<String> getSipVoipUsernameList()
	{
		return sipVoipUsernameList;
	}

	public void setSipVoipUsernameList(List<String> sipVoipUsernameList)
	{
		this.sipVoipUsernameList = sipVoipUsernameList;
	}

	public List<String> getHvoipPhoneList()
	{
		return hvoipPhoneList;
	}

	public void setHvoipPhoneList(List<String> hvoipPhoneList)
	{
		this.hvoipPhoneList = hvoipPhoneList;
	}

	public List<String> getIptvUsernameList()
	{
		return iptvUsernameList;
	}

	public void setIptvUsernameList(List<String> iptvUsernameList)
	{
		this.iptvUsernameList = iptvUsernameList;
	}

	public List<String> getIptvLanPortList()
	{
		return iptvLanPortList;
	}

	public void setIptvLanPortList(List<String> iptvLanPortList)
	{
		this.iptvLanPortList = iptvLanPortList;
	}
}
