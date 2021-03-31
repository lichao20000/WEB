
package com.linkage.module.itms.service.act;

import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.litms.system.UserRes;
import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.module.itms.service.bio.SimulateSheetNxBIO;

public class SimulateSheetNxACT implements SessionAware
{

	// 日志记录
	private static Logger logger = LoggerFactory
			.getLogger(SimulateSheetNxACT.class);
	// 业务类型
	private String servTypeId = null;
	// 操作类型
	private String operateType = null;
	// 工单受理时间
	private String dealdate = null;
	// 设备类型
	private String devType = null;
	// 逻辑SN
	private String username = null;
	// 属地
	private String cityId = null;
	// IP获取方式
	private String wanType = null;
	// IP地址
	private String ipaddress = null;
	// 掩码
	private String ipmask = null;
	// 网关
	private String gateway = null;
	// DNS值
	private String adslSer = null;
	// 终端物理标识
	private String voipPort = null;
	// 终端标识
	private String regId = null;
	// 终端标识类型
	private String regIdType = null;
	// 用户ID
	private String userId = null;
	private String vlanid = "45";
	private String vpiid = null;
	private String vciid = null;
	// MGC服务器地址
	private String mgcIp = null;
	// MGC服务器端口
	private int mgcPort;
	// MGC备用服务器地址
	private String standMgcIp = null;
	// MGC备用服务器端口
	private int standMgcPort;
	// 业务电话号码
	private String voipTelepone = null;
	// Session
	private Map session = null;
	// ajax
	private String ajax = null;
	
	private SimulateSheetNxBIO bio;
	/** 属地列表 */
	private List<Map<String, String>> cityList = null;

	public String excute()
	{
		logger.debug("excute()");
		return "success";
	}

	/**
	 * 当选择开户，销户时加载相关页面
	 * 
	 * @return
	 */
	public String showSheet()
	{
		logger.debug("showSheet()");
		UserRes curUser = (UserRes) session.get("curUser");
		cityId = curUser.getCityId();
		cityList = CityDAO.getNextCityListByCityPid(curUser.getCityId());
		dealdate = getNowDate();
		if ("1".equals(operateType))
		{
			logger.warn("VOIP H248业务开户工单");
			return "voipopenNx";
		}
		else if ("3".equals(operateType))
		{
			logger.warn("VOIP H248业务销户工单");
			return "voipstopNx";
		}
		else
		{
			ajax = "未知操作类型";
			return "ajax";
		}
	}

	/**
	 * 业务下发
	 * 
	 * @return
	 */
	public String sendSheet()
	{
		logger.debug("sendSheet()");
		// H248 开户
		if ("1".equals(operateType))
		{
			// 判断页面输入的"逻辑SN"(用户名)在系统中是否存在
			userId = bio.checkUserName(username); // 查询tab_hgwcustomer
			if ("".equals(userId) || null == userId)
			{
				ajax = "系统中不存在您所输入的逻辑SN，请确认！";
				return "ajax";
			}
			else
			{
				int count = bio.checkVOIPH248(userId); // 判断系统中是否存在VOIP H.248业务
				if (count <= 0)
				{ // 系统中不存在VOIP H.248业务
					// 新增hgwcust_serv_info，tab_sip_info，tab_voip_serv_param
					ajax = bio.openAccountAdd(userId, servTypeId, operateType, username,
							wanType, ipaddress, ipmask, gateway, adslSer, vlanid, vpiid,
							vciid, cityId, mgcIp, mgcPort, standMgcIp, standMgcPort,
							voipTelepone, devType, voipPort, regId, regIdType);
				}
				else
				{ // 系统中存在VOIP H.248业务 更新hgwcust_serv_info
					ajax = bio.openAccountUpdate(userId, servTypeId, operateType,
							username, wanType, ipaddress, ipmask, gateway, adslSer,
							vlanid, vpiid, vciid, cityId, mgcIp, mgcPort, standMgcIp,
							standMgcPort, voipTelepone, devType, voipPort, regId,
							regIdType);
				}
			}
			// H248 销户
		}
		else if ("3".equals(operateType))
		{
			// 销户的时候需要将工单发送给Eserver，而Eserver中的serv_type_id使用的是15，所以此处将15作为传递参数
			ajax = bio.sendVoipStopSheet("15", operateType, dealdate, username, cityId,
					voipTelepone);
			if (ajax == null)
			{
				ajax = "销户失败！";
			}
			else
			{
				ajax = "销户成功！";
			}
		}
		return "ajax";
	}

	// 系统当前时间的
	private String getNowDate()
	{
		GregorianCalendar now = new GregorianCalendar();
		SimpleDateFormat fmtrq = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
		String time = fmtrq.format(now.getTime());
		return time;
	}

	public String checkUsername()
	{
		ajax = bio.checkUsername(username);
		return "ajax";
	}

	

	public List<Map<String, String>> getCityList()
	{
		return cityList;
	}

	public void setCityList(List<Map<String, String>> cityList)
	{
		this.cityList = cityList;
	}

	@Override
	public void setSession(Map session)
	{
		this.session = session;
	}

	public SimulateSheetNxBIO getBio()
	{
		return bio;
	}

	public void setBio(SimulateSheetNxBIO bio)
	{
		this.bio = bio;
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

	public String getDevType()
	{
		return devType;
	}

	public void setDevType(String devType)
	{
		this.devType = devType;
	}

	public String getUsername()
	{
		return username;
	}

	public void setUsername(String username)
	{
		this.username = username;
	}

	public String getCityId()
	{
		return cityId;
	}

	public void setCityId(String cityId)
	{
		this.cityId = cityId;
	}

	public String getAjax()
	{
		return ajax;
	}

	public void setAjax(String ajax)
	{
		this.ajax = ajax;
	}

	public Map getSession()
	{
		return session;
	}

	public String getVoipTelepone()
	{
		return voipTelepone;
	}

	public void setVoipTelepone(String voipTelepone)
	{
		this.voipTelepone = voipTelepone;
	}

	public String getWanType()
	{
		return wanType;
	}

	public void setWanType(String wanType)
	{
		this.wanType = wanType;
	}

	public String getIpaddress()
	{
		return ipaddress;
	}

	public void setIpaddress(String ipaddress)
	{
		this.ipaddress = ipaddress;
	}

	public String getIpmask()
	{
		return ipmask;
	}

	public void setIpmask(String ipmask)
	{
		this.ipmask = ipmask;
	}

	public String getGateway()
	{
		return gateway;
	}

	public void setGateway(String gateway)
	{
		this.gateway = gateway;
	}

	public String getAdslSer()
	{
		return adslSer;
	}

	public void setAdslSer(String adslSer)
	{
		this.adslSer = adslSer;
	}

	public String getRegId()
	{
		return regId;
	}

	public void setRegId(String regId)
	{
		try
		{
			this.regId = java.net.URLDecoder.decode(regId, "UTF-8");
		}
		catch (Exception e)
		{
			this.regId = regId;
		}
	}

	public String getRegIdType()
	{
		return regIdType;
	}

	public void setRegIdType(String regIdType)
	{
		this.regIdType = regIdType;
	}

	public String getUserId()
	{
		return userId;
	}

	public void setUserId(String userId)
	{
		this.userId = userId;
	}

	public String getVlanid()
	{
		return vlanid;
	}

	public void setVlanid(String vlanid)
	{
		this.vlanid = vlanid;
	}

	public String getVpiid()
	{
		return vpiid;
	}

	public void setVpiid(String vpiid)
	{
		this.vpiid = vpiid;
	}

	public String getVciid()
	{
		return vciid;
	}

	public void setVciid(String vciid)
	{
		this.vciid = vciid;
	}

	public String getMgcIp()
	{
		return mgcIp;
	}

	public void setMgcIp(String mgcIp)
	{
		this.mgcIp = mgcIp;
	}

	public int getMgcPort()
	{
		return mgcPort;
	}

	public void setMgcPort(int mgcPort)
	{
		this.mgcPort = mgcPort;
	}

	public String getStandMgcIp()
	{
		return standMgcIp;
	}

	public void setStandMgcIp(String standMgcIp)
	{
		this.standMgcIp = standMgcIp;
	}

	public int getStandMgcPort()
	{
		return standMgcPort;
	}

	public void setStandMgcPort(int standMgcPort)
	{
		this.standMgcPort = standMgcPort;
	}

	public String getVoipPort()
	{
		return voipPort;
	}

	public void setVoipPort(String voipPort)
	{
		this.voipPort = voipPort;
	}

	
}
