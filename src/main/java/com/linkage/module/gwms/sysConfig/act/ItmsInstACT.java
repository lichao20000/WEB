
package com.linkage.module.gwms.sysConfig.act;

import java.util.List;
import java.util.Map;
import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ResourceBind.BindInfo;
import ResourceBind.ResultInfo;
import ResourceBind.UnBindInfo;
import com.linkage.litms.LipossGlobals;
import com.linkage.litms.system.UserRes;
import com.linkage.module.gwms.sysConfig.bio.UserInstReleaseBIO;
import com.linkage.module.gwms.util.CreateObjectFactory;
import com.linkage.module.gwms.util.ResourceBindInterface;
import com.opensymphony.xwork2.ActionSupport;

@SuppressWarnings("unchecked")
public class ItmsInstACT extends ActionSupport implements SessionAware
{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// 日志记录
	private static Logger logger = LoggerFactory.getLogger(ItmsInstACT.class);
	private List deviceList;
	private String message;
	private UserInstReleaseBIO userInstReleaseBio;
	// session
	private Map session;
	private String username = null;
	private String userId = null;
	private String deviceId = null;
	private String deviceNo = null;
	private String loopbackIp = null;
	private String bindtype = null;
	private String ajax = null;
	/** 终端类型 */
	private String gw_type = null;
	
	private ResourceBindInterface G_ResourceBind = null;
	
	/**
	 * itms现场安装页面初始化
	 * 
	 * @author wangsenbo
	 * @date Jul 22, 2010
	 * @param
	 * @return String
	 */
	public String execute()
	{
		return "init";
	}
	
	/**
	 * itms查询设备信息 绑定
	 * 
	 * @author wangsenbo
	 * @date Nov 13, 2009
	 * @return String
	 */
	public String getDeviceInfo()
	{
		logger.debug("getDeviceInfo()");
		UserRes curUser = (UserRes) session.get("curUser");
		String cityId = curUser.getCityId();
		deviceList = userInstReleaseBio.queryDevice(deviceNo, cityId,
				loopbackIp);
		if (deviceList.isEmpty())
		{
			deviceList = null;
		}
		return "device";
	}
	
	/**
	 * itms解绑
	 * 
	 * @author wangsenbo
	 * @date Nov 13, 2009
	 * @return String
	 */
	public String bind()
	{
		logger.debug("release()");
		
		UserRes curUser = (UserRes) session.get("curUser");
		BindInfo[] arr = new BindInfo[1];
		arr[0] = new BindInfo();
		arr[0].accOid = String.valueOf(curUser.getUser().getId());
		arr[0].accName = curUser.getUser().getAccount();
		arr[0].username = username;
		arr[0].deviceId = deviceId;
		if(null==bindtype||"".equals(bindtype)){
			this.ajax = "bindtype为空";
			return "ajax";
		}
		arr[0].userline = Integer.parseInt(bindtype);
		
		this.gw_type = LipossGlobals.getGw_Type(deviceId);
//		if (Global.GW_TYPE_ITMS.equals(gw_type)) {
//			this.G_ResourceBind = Global.G_ResourceBind_itms;
//		} else {
//			this.G_ResourceBind = Global.G_ResourceBind_bbms;
//		}
//		
//		if(null == this.G_ResourceBind){
//			String ior = userInstReleaseBio.getCorbaIor();
//			if(null==ior){
//				this.ajax = "资源绑定模块服务没起";
//				return "ajax";
//			}
//			org.omg.CORBA.Object objRef = null;
//			try
//			{
//				String[] args = null;
//				ORB PP_ORB = ORB.init(args, null);
//				objRef = PP_ORB.string_to_object(ior);
//				this.G_ResourceBind = BlManagerHelper.narrow(objRef);
//				ResultInfo rs = this.G_ResourceBind.bind("web", 0, arr);
//				logger.warn("{}-{}",new Object[]{rs.status,rs.resultId});
//				this.ajax = rs.status;
//			}
//			catch (Exception ex)
//			{
//				logger.error("rebind ResourceBind Error.\n{}", ex);
//			}
//		}else{
//			try
//			{
//				ResultInfo rs = this.G_ResourceBind.bind("web", 0, arr);
//				logger.warn("{}-{}",new Object[]{rs.status,rs.resultId});
//				this.ajax = rs.status;
//			}catch (Exception ex)
//			{
//				logger.error("rebind ResourceBind Error.\n{}", ex);
//				String ior = userInstReleaseBio.getCorbaIor();
//				if(null==ior){
//					this.ajax = "资源绑定模块服务没起";
//					return "ajax";
//				}
//				org.omg.CORBA.Object objRef = null;
//				try
//				{
//					String[] args = null;
//					ORB PP_ORB = ORB.init(args, null);
//					objRef = PP_ORB.string_to_object(ior);
//					this.G_ResourceBind = BlManagerHelper.narrow(objRef);
//					ResultInfo rs = this.G_ResourceBind.bind("web", 0, arr);
//					logger.warn("{}-{}",new Object[]{rs.status,rs.resultId});
//					this.ajax = rs.status;
//				}
//				catch (Exception ee)
//				{
//					logger.error("rebind ResourceBind Error.\n{}", ee);
//				}
//			}
//		}
		
		this.G_ResourceBind = CreateObjectFactory.createResourceBind(this.gw_type);
		ResultInfo rs = this.G_ResourceBind.bind(arr);
		logger.warn("{}-{}",new Object[]{rs.status,rs.resultId});
		this.ajax = rs.status;
		
		return "ajax";
	}

	/**
	 * itms解绑
	 * 
	 * @author wangsenbo
	 * @date Nov 13, 2009
	 * @return String
	 */
	public String release()
	{
		logger.debug("release()");

		UserRes curUser = (UserRes) session.get("curUser");
		UnBindInfo[] arr = new UnBindInfo[1];
		arr[0] = new UnBindInfo();
		arr[0].accOid = String.valueOf(curUser.getUser().getId());
		arr[0].accName = curUser.getUser().getAccount();
		arr[0].userId = userId;
		arr[0].deviceId = deviceId;
		if(null==bindtype||"".equals(bindtype)){
			this.ajax = "bindtype为空";
			return "ajax";
		}
		arr[0].userline = Integer.parseInt(bindtype);
		
		this.gw_type = LipossGlobals.getGw_Type(deviceId);
//		if (Global.GW_TYPE_ITMS.equals(gw_type)) {
//			this.G_ResourceBind = Global.G_ResourceBind_itms;
//		} else {
//			this.G_ResourceBind = Global.G_ResourceBind_bbms;
//		}
//		
//		if(null==this.G_ResourceBind){
//			String ior = userInstReleaseBio.getCorbaIor();
//			if(null==ior){
//				this.ajax = "资源绑定模块服务没起";
//				return "ajax";
//			}
//			org.omg.CORBA.Object objRef = null;
//			try
//			{
//				String[] args = null;
//				ORB PP_ORB = ORB.init(args, null);
//				objRef = PP_ORB.string_to_object(ior);
//				this.G_ResourceBind = BlManagerHelper.narrow(objRef);
//				ResultInfo rs = this.G_ResourceBind.unBind("web", 0, arr);
//				logger.warn("{}-{}",new Object[]{rs.status,rs.resultId});
//				this.ajax = rs.status;
//			}
//			catch (Exception ex)
//			{
//				logger.error("rebind ResourceBind Error.\n{}", ex);
//			}
//		}else{
//			try
//			{
//				ResultInfo rs = this.G_ResourceBind.unBind("web", 0, arr);
//				logger.warn("{}-{}",new Object[]{rs.status,rs.resultId});
//				this.ajax = rs.status;
//			}catch (Exception ex)
//			{
//				logger.error("rebind ResourceBind Error.\n{}", ex);
//				String ior = userInstReleaseBio.getCorbaIor();
//				if(null==ior){
//					this.ajax = "资源绑定模块服务没起";
//					return "ajax";
//				}
//				org.omg.CORBA.Object objRef = null;
//				try
//				{
//					String[] args = null;
//					ORB PP_ORB = ORB.init(args, null);
//					objRef = PP_ORB.string_to_object(ior);
//					this.G_ResourceBind = BlManagerHelper.narrow(objRef);
//					ResultInfo rs = G_ResourceBind.unBind("web", 0, arr);
//					logger.warn("{}-{}",new Object[]{rs.status,rs.resultId});
//					this.ajax = rs.status;
//				}
//				catch (Exception ee)
//				{
//					logger.error("rebind ResourceBind Error.\n{}", ee);
//				}
//			}
//		}
		this.G_ResourceBind = CreateObjectFactory.createResourceBind(this.gw_type);
		ResultInfo rs = this.G_ResourceBind.release(arr);
		logger.warn("{}-{}",new Object[]{rs.status,rs.resultId});
		this.ajax = rs.status;
		
		return "ajax";
	}

//	public String userDelete(){
//		
//		logger.warn("userDelete{} start",this.username);
//		if(null==Global.G_ResourceBind){
//			String ior = userInstReleaseBio.getCorbaIor();
//			if(null==ior){
//				this.ajax = "资源绑定模块服务没起";
//				return "ajax";
//			}
//			org.omg.CORBA.Object objRef = null;
//			try
//			{
//				String[] args = null;
//				ORB PP_ORB = ORB.init(args, null);
//				objRef = PP_ORB.string_to_object(ior);
//				Global.G_ResourceBind = BlManagerHelper.narrow(objRef);
//				Global.G_ResourceBind.userDelete("web", 0, this.username);
//				logger.warn("userDelete{} first end",this.username);
//				this.ajax = "成功";
//			}
//			catch (Exception ex)
//			{
//				logger.error("rebind ResourceBind Error.\n{}", ex);
//				this.ajax = "失败";
//			}
//		}else{
//			try
//			{
//				Global.G_ResourceBind.userDelete("web", 0, this.username);
//
//				logger.warn("userDelete{} end",this.username);
//				this.ajax = "成功";
//				
//			}catch (Exception ex)
//			{
//				logger.error("rebind ResourceBind Error.\n{}", ex);
//				String ior = userInstReleaseBio.getCorbaIor();
//				if(null==ior){
//					this.ajax = "资源绑定模块服务没起";
//					return "ajax";
//				}
//				org.omg.CORBA.Object objRef = null;
//				try
//				{
//					String[] args = null;
//					ORB PP_ORB = ORB.init(args, null);
//					objRef = PP_ORB.string_to_object(ior);
//					Global.G_ResourceBind = BlManagerHelper.narrow(objRef);
//					Global.G_ResourceBind.userDelete("web", 0, this.username);
//					this.ajax = "成功";
//				}
//				catch (Exception ee)
//				{
//					logger.error("rebind ResourceBind Error.\n{}", ee);
//					this.ajax = "失败";
//				}
//			}
//		}
//		
//		return "ajax";
//	}
	
	/**
	 * @return the deviceList
	 */
	public List getDeviceList()
	{
		return deviceList;
	}

	/**
	 * @param deviceList
	 *            the deviceList to set
	 */
	public void setDeviceList(List deviceList)
	{
		this.deviceList = deviceList;
	}

	/**
	 * @return the userInstReleaseBio
	 */
	public UserInstReleaseBIO getUserInstReleaseBio()
	{
		return userInstReleaseBio;
	}

	/**
	 * @param userInstReleaseBio
	 *            the userInstReleaseBio to set
	 */
	public void setUserInstReleaseBio(UserInstReleaseBIO userInstReleaseBio)
	{
		this.userInstReleaseBio = userInstReleaseBio;
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
	 * @return the message
	 */
	public String getMessage()
	{
		return message;
	}

	/**
	 * @param message
	 *            the message to set
	 */
	public void setMessage(String message)
	{
		this.message = message;
	}

	/**
	 * @return the username
	 */
	public String getUsername()
	{
		return username;
	}

	/**
	 * @param username
	 *            the username to set
	 */
	public void setUsername(String username)
	{
		this.username = username;
	}

	/**
	 * @return the userId
	 */
	public String getUserId()
	{
		return userId;
	}

	/**
	 * @param userId
	 *            the userId to set
	 */
	public void setUserId(String userId)
	{
		this.userId = userId;
	}

	/**
	 * @return the deviceId
	 */
	public String getDeviceId()
	{
		return deviceId;
	}

	/**
	 * @param deviceId
	 *            the deviceId to set
	 */
	public void setDeviceId(String deviceId)
	{
		this.deviceId = deviceId;
	}

	/**
	 * @return the deviceNo
	 */
	public String getDeviceNo()
	{
		return deviceNo;
	}

	/**
	 * @param deviceNo
	 *            the deviceNo to set
	 */
	public void setDeviceNo(String deviceNo)
	{
		this.deviceNo = deviceNo;
	}

	/**
	 * @return the loopbackIp
	 */
	public String getLoopbackIp()
	{
		return loopbackIp;
	}

	/**
	 * @param loopbackIp
	 *            the loopbackIp to set
	 */
	public void setLoopbackIp(String loopbackIp)
	{
		this.loopbackIp = loopbackIp;
	}

	public String getBindtype() {
		return bindtype;
	}

	public void setBindtype(String bindtype) {
		this.bindtype = bindtype;
	}

	public String getAjax() {
		return ajax;
	}

	public void setAjax(String ajax) {
		this.ajax = ajax;
	}

	public String getGw_type() {
		return gw_type;
	}

	public void setGw_type(String gw_type) {
		this.gw_type = gw_type;
	}

}
